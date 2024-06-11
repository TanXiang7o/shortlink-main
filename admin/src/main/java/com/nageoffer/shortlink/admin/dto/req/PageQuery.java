package com.nageoffer.shortlink.admin.dto.req;

import lombok.Data;

@Data
public class PageQuery {


    private Long pageNo = 1L;

    private Long pageSize = 10L;

    private String orderBy;

    private Boolean isAsc;
}