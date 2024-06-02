package org.tx.shortlink.shop.service;

import org.tx.shortlink.shop.DTO.resp.ProductOrderRespDTO;
import org.tx.shortlink.shop.entity.ProductOrderDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tx
 * @since 2024-06-02
 */
public interface IProductOrderService extends IService<ProductOrderDO> {

    ProductOrderRespDTO getByTradeNo(String tradeNo);

    List<ProductOrderRespDTO> getByUserId(Long userId);
}
