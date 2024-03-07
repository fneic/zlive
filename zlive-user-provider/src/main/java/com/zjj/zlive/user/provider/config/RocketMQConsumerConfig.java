package com.zjj.zlive.user.provider.config;

import com.alibaba.fastjson.JSON;
import com.zjj.zlive.user.dto.UserDTO;
import com.zjj.zlive.user.provider.service.IUserService;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * @ClassName RocketMQConsumerConfig
 * @Description
 * @Author Zhou JunJie
 * @Date 2024/3/6 23:06
 **/
@Configuration
public class RocketMQConsumerConfig implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(RocketMQConsumerConfig.class);

    @Resource
    private RedisTemplate<String, UserDTO> redisTemplate;

    @Resource
    private RocketMQConsumerProperties consumerProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        doInit();
    }

    private void doInit() throws MQClientException {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
        defaultMQPushConsumer.setNamesrvAddr(consumerProperties.getNameSrv());
        defaultMQPushConsumer.setConsumerGroup(consumerProperties.getGroupName());
        //每次消费一条消息
        defaultMQPushConsumer.setConsumeMessageBatchMaxSize(1);
        defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        defaultMQPushConsumer.subscribe("user-update-cache", "*");
        defaultMQPushConsumer.setMessageListener((MessageListenerConcurrently) (list, consumeConcurrentlyContext) -> {
            MessageExt messageExt = list.get(0);
            String msg = new String(messageExt.getBody());
            UserDTO userDTO = JSON.parseObject(msg, UserDTO.class);
            if(userDTO == null || userDTO.getUserId() == null){
                LOGGER.error("用户id为空，参数异常，内容：{}",msg);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
            String key = IUserService.USERINFO_CACHE_PREFIX + userDTO.getUserId();
            redisTemplate.delete(key);
            LOGGER.info("延迟双删处理，信息：{}",msg);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        defaultMQPushConsumer.start();
        LOGGER.info("mq 消费者启动成功,nameSrv is {}", consumerProperties.getNameSrv());
    }
}
