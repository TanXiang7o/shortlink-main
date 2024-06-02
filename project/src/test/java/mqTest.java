import com.nageoffer.shortlink.project.ShortLinkApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ConcurrentHashMap;

@SpringBootTest(classes = ShortLinkApplication.class)
public class mqTest {
    ConcurrentHashMap<String,String> map = new ConcurrentHashMap<>();
}

//    public RocketMQTemplate rocketMqTemplate(){
//        RocketMQTemplate rocketMqTemplate = new RocketMQTemplate();
//        DefaultMQProducer defaultMqProducer = new DefaultMQProducer();
//        defaultMqProducer.setProducerGroup("TestTopic");
//        defaultMqProducer.setNamesrvAddr("172.17.206.17:9876");
//        rocketMqTemplate.setProducer(defaultMqProducer);
//        return rocketMqTemplate;
//    }
//
//
//
//
//    @Test
//    void Test1(){
//        final RocketMQTemplate rocketMQTemplate = rocketMqTemplate();
//        Map<String,String> producerMap = Map.of("key1", "value1", "key2", "value2");
//        rocketMQTemplate.convertAndSend("TestTopic", JSONUtil.parseObj(producerMap).toString());
//    }
//}
