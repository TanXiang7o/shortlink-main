package org.tx.shortlink.shop.DTO.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO<T>{
    private Long total;
    private Long pages;
    private List<T> data;
}
