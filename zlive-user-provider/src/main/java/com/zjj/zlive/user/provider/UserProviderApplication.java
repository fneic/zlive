package com.zjj.zlive.user.provider;

import com.zjj.zlive.user.consistant.UserTagsEnum;
import com.zjj.zlive.user.provider.service.IUserTagService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.concurrent.CountDownLatch;

/**
 * @ClassName UserProviderApplication
 * @Description
 * @Author Zhou JunJie
 * @Date 2024/3/5 20:23
 **/
@SpringBootApplication
@EnableDubbo
@EnableDiscoveryClient
public class UserProviderApplication implements CommandLineRunner {

    @Resource
    private IUserTagService userTagService;
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(UserProviderApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        boolean b = userTagService.containsTag(10002L, UserTagsEnum.IS_OLD_USER);
        boolean b1 = userTagService.setTag(10002L, UserTagsEnum.IS_VIP);
    }
}
