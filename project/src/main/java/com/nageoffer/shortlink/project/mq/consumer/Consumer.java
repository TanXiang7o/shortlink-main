package com.nageoffer.shortlink.project.mq.consumer;

//public class Consumer {
//
//    public static final String CONSUMER_GROUP = "shortlinkConsumer";
//    public static final String DEFAULT_NAMESRVADDR = "172.17.206.17:9876";
//    public static final String TOPIC = "shortlink";
//
//    public static void main(String[] args) throws MQClientException {
//
//        /*
//         * Instantiate with specified consumer group name.
//         */
//        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(CONSUMER_GROUP);
//
//        /*
//         * Specify name server addresses.
//         * <p/>
//         *
//         * Alternatively, you may specify name server addresses via exporting environmental variable: NAMESRV_ADDR
//         * <pre>
//         * {@code
//         * consumer.setNamesrvAddr("name-server1-ip:9876;name-server2-ip:9876");
//         * }
//         * </pre>
//         */
//        // Uncomment the following line while debugging, namesrvAddr should be set to your local address
//         consumer.setNamesrvAddr(DEFAULT_NAMESRVADDR);
//
//        /*
//         * Specify where to start in case the specific consumer group is a brand-new one.
//         */
//        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
//
//        /*
//         * Subscribe one more topic to consume.
//         */
//        consumer.subscribe(TOPIC, "*");
//
//        /*
//         *  Register callback to execute on arrival of messages fetched from brokers.
//         */
//        consumer.registerMessageListener((MessageListenerConcurrently) (msg, context) -> {
//            System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msg);
//            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//        });
//
//        /*
//         *  Launch the consumer instance.
//         */
//        consumer.start();
//
//        System.out.printf("Consumer Started.%n");
//    }
//}