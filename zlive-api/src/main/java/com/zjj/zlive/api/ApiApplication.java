package com.zjj.zlive.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * @ClassName ApiApplication
 * @Description
 * @Author Zhou JunJie
 * @Date 2024/3/5 21:34
 **/
@SpringBootApplication
public class ApiApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ApiApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.SERVLET);
        ApplicationContext applicationContext = new XmlWebApplicationContext();
        applicationContext.getBean("");
        springApplication.run(args);
    }
}
