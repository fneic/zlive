package com.zjj.zlive.user.provider.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName RedissonConfig
 * @Description
 * @Author Zhou JunJie
 * @Date 2024/3/8 21:17
 **/
@Configuration
public class RedissonConfig {

    @Value("${spring.data.redis.host}")
    String host;

    @Value("${spring.data.redis.port}")
    String port;

    @Value("${spring.data.redis.password}")
    String password;

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + host + ":" + port).setPassword(password);
        return Redisson.create(config);
    }
}
