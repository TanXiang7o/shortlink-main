package org.tx.shortlink.shop.DTO.req;

import lombok.Data;

@Data
public class OrderQuery extends PageQuery{
    private String tradeNo;
    private String state;
    private Long userId;
}
