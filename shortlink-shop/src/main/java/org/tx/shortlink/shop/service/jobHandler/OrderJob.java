package org.tx.shortlink.shop.service.jobHandler;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderJob {
    private static Logger logger = LoggerFactory.getLogger(OrderJob.class);

    @XxlJob("TimeoutOrderJobHandler")
    public void TimeoutOrderJobHandler(){
        
    }
}
