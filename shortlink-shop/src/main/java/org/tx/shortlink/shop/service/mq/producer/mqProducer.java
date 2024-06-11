package org.tx.shortlink.shop.service.mq.producer;

import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.client.core.RocketMQClientTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class mqProducer {

    private final RocketMQClientTemplate rocketMQClientTemplate;

    @Value("${rocketmq.producer.topic}")
    private String topic;

    public void send(Map<String, String> msg) {
        rocketMQClientTemplate.send(topic+":TAGA", MessageBuilder.withPayload(msg.toString()).build());
    }
}
