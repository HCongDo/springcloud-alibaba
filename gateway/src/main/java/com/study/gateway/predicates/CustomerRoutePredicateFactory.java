package com.study.gateway.predicates;

import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.GatewayPredicate;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * 自定义断言 - 需在yml文件中配置才可生效
 *
 * @author hecong
 * @version 1.0
 * @date 2023/7/19 21:58
 */
@Component
public class CustomerRoutePredicateFactory extends AbstractRoutePredicateFactory<CustomerRoutePredicateFactory.Config> {


    public CustomerRoutePredicateFactory() {
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("name");
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return new GatewayPredicate() {
            @Override
            public boolean test(ServerWebExchange exchange) {
                if (config.getName().equals("xushu")) {
                    return true;
                }
                return false;
            }
        };
    }


    // 用于接收配置文件中 断言的信息
    @Validated
    public static class Config {
        private String name;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }

}
