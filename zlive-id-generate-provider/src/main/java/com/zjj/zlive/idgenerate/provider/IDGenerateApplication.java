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
public class IDGenerateApplication implements CommandLineRunner {

    @Resource
    private IdGenerateService idGenerateService;
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(IDGenerateApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < 200; i++) {
            Long seqId = idGenerateService.getUnSeqId(2);
            System.out.println(seqId);
        }
        Thread.sleep(2000);
        for (int i = 0; i < 50; i++) {
            Long seqId = idGenerateService.getUnSeqId(2);
            System.out.println(seqId);
        }
    }
}
