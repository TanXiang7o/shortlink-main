package org.tx.shortlink.shop.DTO.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusReqDTO {
    private Long userId;

    private String tradeNo;

    private String newState;

    private String oldState;

    private String payNo;

    private LocalDateTime payTime;
}
