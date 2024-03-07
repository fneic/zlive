package com.zjj.zlive.datasource.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @ClassName AutoConnectConfig
 * @Description
 * @Author Zhou JunJie
 * @Date 2024/3/6 19:41
 **/
@Configuration
public class ShardingJdbcDatasourceAutoInitConnectionConfig {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(ShardingJdbcDatasourceAutoInitConnectionConfig.class);
    @Bean
    public ApplicationRunner runner(DataSource dataSource) {
        return args -> {
            LOGGER.info(" ================== [ShardingJdbcDatasourceAutoInitConnectionConfig] dataSource: {}", dataSource);
            //手动触发下连接池的连接创建
            Connection connection = dataSource.getConnection();
        };
    }
}
