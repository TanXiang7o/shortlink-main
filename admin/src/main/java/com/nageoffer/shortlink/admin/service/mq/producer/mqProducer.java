package com.nageoffer.shortlink.admin.service.mq.producer;

import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.client.core.RocketMQClientTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class mqProducer {

    private final RocketMQClientTemplate rocketMQClientTemplate;

    @Value("${rocketmq.producer.topic}")
    private String topic;

    public void send(String tag,Object msg) {
        rocketMQClientTemplate.send(topic+":"+tag, MessageBuilder.withPayload(msg.toString()).build());
    }
}
