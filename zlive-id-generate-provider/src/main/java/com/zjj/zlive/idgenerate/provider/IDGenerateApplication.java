package com.zjj.zlive.idgenerate.provider;

import com.zjj.zlive.idgenerate.provider.service.IdGenerateService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName Application
 * @Description
 * @Author Zhou JunJie
 * @Date 2024/3/7 19:56
 **/
@SpringBootApplication
@EnableDubbo
@EnableDiscoveryClient
public class IDGenerateApplication {


    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(IDGenerateApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }


}
