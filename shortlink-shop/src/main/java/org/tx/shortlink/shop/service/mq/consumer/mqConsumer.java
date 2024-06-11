package org.tx.shortlink.shop.service.mq.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.annotation.RocketMQMessageListener;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.apache.rocketmq.client.core.RocketMQListener;
import org.springframework.stereotype.Service;
import org.tx.shortlink.shop.service.mq.idempotent.MessageQueueIdempotentHandler;

import java.nio.charset.StandardCharsets;

@Service
@Slf4j
@RequiredArgsConstructor
@RocketMQMessageListener(endpoints = "${rocketmq.simple-consumer.endpoints}", topic = "${rocketmq.simple-consumer.topic}",
        consumerGroup = "${rocketmq.simple-consumer.consumer-group}",tag = "${rocketmq.simple-consumer.tag}")
public class mqConsumer implements RocketMQListener {

    private final MessageQueueIdempotentHandler messageQueueIdempotentHandler;
    @Override
    public ConsumeResult consume(MessageView messageView) {
        String str = StandardCharsets.UTF_8.decode(messageView.getBody()).toString();
        System.out.println("消费消息：" + str);
        return ConsumeResult.SUCCESS;
    }
}