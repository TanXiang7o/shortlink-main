package org.tx.shortlink.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.tx.shortlink.shop.DTO.req.OrderQuery;
import org.tx.shortlink.shop.DTO.req.OrderStatusReqDTO;
import org.tx.shortlink.shop.DTO.resp.PageDTO;
import org.tx.shortlink.shop.DTO.resp.ProductOrderRespDTO;
import org.tx.shortlink.shop.entity.ProductOrderDO;

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

    boolean create(ProductOrderDO productOrderDO);

    ProductOrderRespDTO getByTradeNo(String tradeNo, Long userId);

    List<ProductOrderRespDTO> getByUserId(Long userId);

    Boolean updateStatus(OrderStatusReqDTO orderStatusReqDTO);

    PageDTO<ProductOrderRespDTO> queryOrdersPage(OrderQuery query);

    String getToken(Long userId);
}
