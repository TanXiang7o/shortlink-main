package org.tx.shortlink.shop.DTO.req;

import lombok.Data;

@Data
public class PageQuery {

    private Long pageNo;

    private Long pageSize;

    private String orderBy;

    private Boolean isAsc;
}
