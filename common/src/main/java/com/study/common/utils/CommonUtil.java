package com.study.common.utils;

import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

/**
 * 系统通用工具类
 *
 * @author hecong
 * @version 1.0
 * @date 2023/7/20 11:52
 */
public class CommonUtil {

    static Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    /**
     * 获取服务IP
     * @return
     */
    public static String getHostIp() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            return address.getHostAddress();
        } catch (Exception e) {
            logger.error("获取IP失败：{}", e.getMessage());
        }
        return StrUtil.EMPTY;
    }

    /**
     * 获取实例名称
     * @return
     */
    public static String getInstanceName() {
        try {
            return System.getenv("instanceName");
        } catch (Exception e) {
            logger.error("获取实例名称：{}", e.getMessage());
        }
        return StrUtil.EMPTY;
    }


}
