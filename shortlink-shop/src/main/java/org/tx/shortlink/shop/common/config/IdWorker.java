package org.tx.shortlink.shop.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class IdWorker {
    @Bean
    public SnowflakeDistributeId snowflakeDistributeId(){
        return new SnowflakeDistributeId(0, 0);
    }
}
