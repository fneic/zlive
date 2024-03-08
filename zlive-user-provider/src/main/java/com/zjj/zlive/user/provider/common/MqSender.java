package com.zjj.zlive.user.provider.common;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @ClassName MqSender
 * @Description
 * @Author Zhou JunJie
 * @Date 2024/3/8 23:03
 **/
@Component
@Slf4j
public class MqSender {
    @Resource
    private MQProducer mqProducer;

    public void sendCacheDeleteMessage(String key){
        Message message = new Message();
        message.setBody(key.getBytes(StandardCharsets.UTF_8));
        message.setTopic(MQConstant.DELETE_CACHE_TOPIC);
        message.setDelayTimeLevel(1);
        try {
            mqProducer.send(message);
        } catch (Exception e) {
            log.error("延迟双删失败,发送消息失败，key：{},原因：{}",key,e.getStackTrace());
        }
    }
}
