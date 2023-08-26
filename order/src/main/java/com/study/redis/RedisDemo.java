package com.study.redis;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.study.common.entity.ResultDto;
import com.study.order.entity.Customer;
import com.study.order.mapper.CustomerMapper;
import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * Redis 处理高并发
 * 分布式锁、缓存雪崩、缓存击穿、缓存穿透
 *
 * @author hecong
 * @version 1.0
 * @date 2023/8/25 22:00
 */
@RestController
@RequestMapping("/redis")
public class RedisDemo {

    // 命名规则  LOCK:业务模块名:场景名:PREFIX
    // 查询并发锁KEY
    public static final String LOCK_CUSTOMER_HOT_CACHE_PREFIX = "lock:customer:hot_cache:";
    // 双写不一致读写串行KEY
    public static final String LOCK_CUSTOMER_UPDATE_PREFIX = "locK:customer:update:";
    // 锁过期时间
    public static final Integer CUSTOMER_CACHE_TIMEOUT = 60 * 60 * 24;
    // 此业务布隆过滤器
    public static final String RBLOOM_CUSTOMER_NAME = "rbloom_customer_name";

    public static RBloomFilter<Object> bloomFilter = null;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RedisTemplate redisTemplate;

//    @Autowired
//    private Redisson redisson;

    @Autowired
    private RedissonClient redisson;


    @PostConstruct
    public void afterInitialized() {
        // 在 Bean 初始化完成后执行的逻辑
        bloomFilter = redisson.getBloomFilter("RBLOOM_CUSTOMER_NAME");
    }


    @RequestMapping("/product/id")
    public ResultDto getCustomerById(@PathVariable("id") String id) {
        Assert.notNull(id, "为找到合适数据");
        // TODO 布隆过滤 -海量无效Key攻击
        Assert.isTrue(bloomFilter.contains(id), "为找到对应数据");
        // TODO 本地缓存 -Redis 单节点峰值为八到十万，新增本地缓存
        // TODO Redis缓存 -Redis一级缓存获取
        Customer customerFromCache = getCustomerFromCache(id);
        if (customerFromCache != null) {
            return ResultDto.success(customerFromCache);
        }
        // TODO 分布式锁 -此时可能出现热点key重建需做并发锁串行化  此处不能用读写锁不然读请求已经可以打穿，压力透传到下面逻辑
        RLock lock = redisson.getLock(StrUtil.format(LOCK_CUSTOMER_HOT_CACHE_PREFIX, id));
        lock.lock();
        try {
            customerFromCache = getCustomerFromCache(id);
            if (customerFromCache != null) {
                return ResultDto.success(customerFromCache);
            }
            // TODO Redisson 分布式读写锁锁 - 解决双写不一致问题
            RLock updateLock = redisson.getLock(StrUtil.format(LOCK_CUSTOMER_UPDATE_PREFIX, id));
            updateLock.lock();
            try {
                // TODO DB查询数据
                Customer customer = customerMapper.selectById(id);
                if (customer != null) {
                    // TODO 添加缓存
                    redisTemplate.opsForValue().set(StrUtil.format(LOCK_CUSTOMER_HOT_CACHE_PREFIX, customer.getId()), JSON.toJSONString(customer),
                            redisUtil.getExpireTime(CUSTOMER_CACHE_TIMEOUT), TimeUnit.SECONDS);
                }
            } finally {
                updateLock.unlock();
            }
        } finally {
            lock.unlock();
        }
        return ResultDto.success();
    }

    @PostMapping("/create")
    public ResultDto createCustomer(Customer customer) {
        int insert = customerMapper.insert(customer);
        Assert.isTrue(insert == 1, "新增失败");
        redisTemplate.opsForValue().set(StrUtil.format(LOCK_CUSTOMER_HOT_CACHE_PREFIX, customer.getId()), JSON.toJSONString(customer),
                redisUtil.getExpireTime(CUSTOMER_CACHE_TIMEOUT), TimeUnit.SECONDS);
        return ResultDto.success(customer);
    }

    @PostMapping("/update")
    public ResultDto updateCustomer(Customer customer) {
        Assert.notNull(customer.getId(), "请传参");
        RLock lock = redisson.getLock(StrUtil.format(LOCK_CUSTOMER_UPDATE_PREFIX, customer.getId()));
        lock.lock();
        try {
            customerMapper.updateCustomerAge(customer.getId(), 100);
            redisUtil.set(StrUtil.format(LOCK_CUSTOMER_HOT_CACHE_PREFIX, customer.getId()), JSON.toJSONString(customer), redisUtil.getExpireTime(CUSTOMER_CACHE_TIMEOUT), TimeUnit.SECONDS);
        } finally {
            lock.unlock();
        }
        return ResultDto.success("更新成功");
    }

    @RequestMapping("/delete")
    public ResultDto deleteCustomer(Customer customer) {
        Assert.notNull(customer.getId(), "请传参");
        RLock lock = redisson.getLock(StrUtil.format(LOCK_CUSTOMER_UPDATE_PREFIX, customer.getId()));
        lock.lock();
        try {
            customerMapper.deleteById(customer.getId());
            redisUtil.del(StrUtil.format(LOCK_CUSTOMER_HOT_CACHE_PREFIX, customer.getId()));
        } finally {
            lock.unlock();
        }
        return ResultDto.success("删除成功");
    }


    Customer getCustomerFromCache(String id) {
        Customer customer = null;
        if (StrUtil.isNotBlank(redisUtil.get(StrUtil.format(LOCK_CUSTOMER_HOT_CACHE_PREFIX, id)))) {
            customer = JSON.parseObject(redisUtil.get(id), Customer.class);
            //TODO 缓存Key续期
            redisUtil.expire(StrUtil.format(LOCK_CUSTOMER_HOT_CACHE_PREFIX, id), redisUtil.getExpireTime(CUSTOMER_CACHE_TIMEOUT), TimeUnit.SECONDS);
        }
        return customer;
    }

}
