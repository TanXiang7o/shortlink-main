package com.nageoffer.shortlink.project.mq.consumer;


import com.alibaba.fastjson2.JSON;
import com.nageoffer.shortlink.project.dto.biz.ShortLinkStatsRecordDTO;
import com.nageoffer.shortlink.project.mq.idempotent.MessageQueueIdempotentHandler;
import com.nageoffer.shortlink.project.service.ShortLinkStatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.annotation.RocketMQMessageListener;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.apache.rocketmq.client.core.RocketMQListener;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@RocketMQMessageListener(endpoints = "${rocketmq.simple-consumer.endpoints}", topic = "${rocketmq.simple-consumer.topic}",
        consumerGroup = "${rocketmq.simple-consumer.consumer-group}",tag = "${rocketmq.simple-consumer.tag}")
public class mqConsumer implements RocketMQListener {

    private final MessageQueueIdempotentHandler messageQueueIdempotentHandler;

    private final ShortLinkStatsService shortLinkStatsService;

    @Override
    public ConsumeResult consume(MessageView messageView) {
        String str = StandardCharsets.UTF_8.decode(messageView.getBody()).toString();
        System.out.println("消费消息：" + str);
        String messageId = messageView.getMessageId().toString();
        if (!messageQueueIdempotentHandler.isMessageProcessed(messageId)) {
            // 判断当前的这个消息流程是否执行完成
            if (messageQueueIdempotentHandler.isAccomplish(messageId)) {
                return ConsumeResult.SUCCESS;
            }
        }
        Map<String, String> producerMap = convertStringToMap(str);
        String fullShortUrl = producerMap.get("fullShortUrl");
        String gid = producerMap.get("gid");
        ShortLinkStatsRecordDTO statsRecord = JSON.parseObject("{"+producerMap.get("statsRecord")+"}", ShortLinkStatsRecordDTO.class);
        boolean res = shortLinkStatsService.actualSaveShortLinkStats(fullShortUrl, gid, statsRecord, messageId);
        if (!res){
            return ConsumeResult.FAILURE;
        }
        return ConsumeResult.SUCCESS;
    }

    public static Map<String, String> convertStringToMap(String input) {
        Map<String, String> map = new HashMap<>();

        // 去除首尾的大括号
        String processedInput = input.trim().substring(1, input.length() - 1);

        // 分割字符串以获取键值对
        String[] keyValuePairs = processedInput.split(",(?![^{]*\\})"); // 正则表达式避免分割嵌套的大括号内容

        for (String pair : keyValuePairs) {
            String[] entry = pair.split("=", 2);
            String key = entry[0].trim();
            String value = entry.length > 1 ? entry[1].trim() : "";

            // 特殊处理statsRecord的值，因为它是一个嵌套的结构
            if (key.equals("statsRecord")) {
                value = value.substring(1, value.length() - 1); // 去除statsRecord值的大括号
                map.put(key, value);
            } else {
                // 直接存储非嵌套的键值对
                map.put(key, value);
            }
        }

        return map;
    }


}