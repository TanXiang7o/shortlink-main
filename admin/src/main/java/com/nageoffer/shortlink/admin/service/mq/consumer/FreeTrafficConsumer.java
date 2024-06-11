package com.nageoffer.shortlink.admin.service.mq.consumer;

import com.nageoffer.shortlink.admin.common.convention.result.Result;
import com.nageoffer.shortlink.admin.dao.entity.TrafficDO;
import com.nageoffer.shortlink.admin.remote.ProductRemoteService;
import com.nageoffer.shortlink.admin.remote.dto.resp.ProductRespDTO;
import com.nageoffer.shortlink.admin.service.ITrafficService;
import com.nageoffer.shortlink.admin.service.mq.idempotent.MessageQueueIdempotentHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.annotation.RocketMQMessageListener;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.apache.rocketmq.client.core.RocketMQListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
@RocketMQMessageListener(endpoints = "${rocketmq.simple-consumer.endpoints}", topic = "${rocketmq.simple-consumer.topic}",
        consumerGroup = "${rocketmq.simple-consumer.consumer-group}",tag = "${rocketmq.simple-consumer.tag}",consumptionThreadCount = 5)
public class FreeTrafficConsumer implements RocketMQListener {

    private final MessageQueueIdempotentHandler messageQueueIdempotentHandler;

    private final ProductRemoteService productRemoteService;

    private final ITrafficService trafficService;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ConsumeResult consume(MessageView messageView) {
        String username = StandardCharsets.UTF_8.decode(messageView.getBody()).toString();
        System.out.println("注册资源包发放：" + username);
        String messageId = messageView.getMessageId().toString();
        if (!messageQueueIdempotentHandler.isMessageProcessed(messageId)) {
            // 判断当前的这个消息流程是否执行完成
            if (messageQueueIdempotentHandler.isAccomplish(messageId)) {
                return ConsumeResult.SUCCESS;
            }
        }
        Result<ProductRespDTO> product = productRemoteService.getDetailById(1L);
        TrafficDO trafficDO = TrafficDO.builder()
                .username(username)
                .productId(product.getData().getId())
                .productTitle(product.getData().getTitle())
                .tradeNo("user_init")
                .dayLimit(product.getData().getDayTimes())
                .build();
        if (product.getData().getValidDays()!=null){
            trafficDO.setExpireDate(LocalDateTime.now().plusDays(product.getData().getValidDays().longValue()));
        }
        boolean saved = trafficService.save(trafficDO);
        if (saved){
            return ConsumeResult.SUCCESS;
        }else {
            return ConsumeResult.FAILURE;
        }
    }
}