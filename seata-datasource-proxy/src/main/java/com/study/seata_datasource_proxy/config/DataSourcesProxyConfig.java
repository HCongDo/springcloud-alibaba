package com.study.seata_datasource_proxy.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import io.seata.rm.datasource.DataSourceProxy;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;


@Configuration
@EnableConfigurationProperties({MybatisPlusProperties.class})
public class DataSourcesProxyConfig {

    //自动填充注入
    @Bean
    public MyBatisAutoFill myBatisMetaObjectHandler() {
        return new MyBatisAutoFill();
    }
    //id生成器注入
    @Bean
    public CustomerIdGenerator customerIdGenerator() {
        return new CustomerIdGenerator();
    }
    //获取数据源
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return new DruidDataSource();
    }
    //配种数据源
    //@Primary标识必须配置在代码数据源上，否则本地事务失效
//    @Primary
    @Bean
    public DataSourceProxy dataSourceProxy(DataSource druidDataSource) {
        return new DataSourceProxy(druidDataSource);
    }


    private MybatisPlusProperties properties;
    public DataSourcesProxyConfig(MybatisPlusProperties properties) {
        this.properties = properties;
    }

    //配置mybatisplus的数据源,所有插件会失效,所以注入进来,配置给代理数据源
    @Bean
    public MybatisSqlSessionFactoryBean sqlSessionFactory(DataSourceProxy dataSourceProxy) throws Exception {
        // 这里必须用 MybatisSqlSessionFactoryBean 代替了 SqlSessionFactoryBean，否则 MyBatisPlus 不会生效
        MybatisSqlSessionFactoryBean sqlBean = new MybatisSqlSessionFactoryBean();
        sqlBean.setDataSource(dataSourceProxy);
        sqlBean.setTransactionFactory(new SpringManagedTransactionFactory());
        try {
            //注意:!!!!这个如果写的有sql,须有该配置,try cach以下,打开不捕获异常,没sql会报错
            sqlBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                    .getResources("classpath:/mapper/**/*.xml"));
        } catch (IOException ignored) {
        }
        MybatisConfiguration configuration = this.properties.getConfiguration();
        if (configuration == null) {
            configuration = new MybatisConfiguration();
        }
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //向代理数据源添加分页拦截器
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        sqlBean.setPlugins(interceptor);
        //代理数据源添加id生成器,字段自动填充
        sqlBean.setGlobalConfig(new GlobalConfig()
                .setMetaObjectHandler(myBatisMetaObjectHandler())
                .setIdentifierGenerator(customerIdGenerator()));
        sqlBean.setConfiguration(configuration);
        return sqlBean;
    }
}
