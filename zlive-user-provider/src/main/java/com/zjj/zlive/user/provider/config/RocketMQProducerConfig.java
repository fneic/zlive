package com.zjj.zlive.user.provider.config;

import jakarta.annotation.Resource;
import org.apache.catalina.realm.LockOutRealm;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName RocketMQProducerConfig
 * @Description
 * @Author Zhou JunJie
 * @Date 2024/3/6 23:19
 **/
@Configuration
public class RocketMQProducerConfig {

    private final static Logger LOGGER = LoggerFactory.getLogger(RocketMQProducerConfig.class);

    @Resource
    private RocketMQProducerProperties producerProperties;

    @Value("${spring.application.name}")
    private String applicationName;

    private int threadId = 1;

    @Bean
    public MQProducer mqProducer(){
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(16, 20, 5, TimeUnit.MINUTES, new ArrayBlockingQueue<>(1000), (task) -> {
            Thread thread = new Thread(task);
            thread.setName(applicationName + "-mq-producer-" + ++threadId);
            return thread;
        });
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer();
        defaultMQProducer.setNamesrvAddr(producerProperties.getNameSrv());
        defaultMQProducer.setProducerGroup(producerProperties.getGroupName());
        defaultMQProducer.setRetryTimesWhenSendFailed(producerProperties.getRetryTimes());
        defaultMQProducer.setRetryTimesWhenSendAsyncFailed(producerProperties.getRetryTimes());
        defaultMQProducer.setRetryAnotherBrokerWhenNotStoreOK(true);
        defaultMQProducer.setAsyncSenderExecutor(threadPoolExecutor);
        try {
            defaultMQProducer.start();
            LOGGER.info("生产者启动成功，nameSrv is :{}",producerProperties.getNameSrv());
        } catch (MQClientException e) {
            LOGGER.error("生产者启动失败，nameSrv is :{}",producerProperties.getNameSrv());
            throw new RuntimeException(e);
        }
        return defaultMQProducer;
    }
}
