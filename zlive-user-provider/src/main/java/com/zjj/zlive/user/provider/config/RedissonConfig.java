package com.zjj.zlive.user.provider.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
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
@ConfigurationProperties("spring.data.redis")
public class RedissonConfig {

    String host;

    String port;

    String password;

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + host + ":" + port).setPassword(password);
        return Redisson.create(config);
    }
}
