package org.tx.shortlink.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.tx.shortlink.shop.DTO.resp.ProductOrderRespDTO;
import org.tx.shortlink.shop.entity.ProductOrderDO;
import org.tx.shortlink.shop.mapper.ProductOrderMapper;
import org.tx.shortlink.shop.service.IProductOrderService;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tx
 * @since 2024-06-02
 */
@Service
public class ProductOrderServiceImpl extends ServiceImpl<ProductOrderMapper, ProductOrderDO> implements IProductOrderService {

    @Override
    public ProductOrderRespDTO getByTradeNo(String tradeNo) {
        ProductOrderDO productOrderDO = getOne(lambdaQuery().eq(ProductOrderDO::getTradeNo, tradeNo));
        ProductOrderRespDTO productOrderRespDTO = new ProductOrderRespDTO();
        BeanUtils.copyProperties(productOrderDO, productOrderRespDTO);
        return productOrderRespDTO;
    }

    @Override
    public List<ProductOrderRespDTO> getByUserId(Long userId) {
        List<ProductOrderDO> productOrderDOS = list(lambdaQuery().eq(ProductOrderDO::getUserId, userId));
        return productOrderDOS.stream().map(productOrderDO -> {
            ProductOrderRespDTO productOrderRespDTO = new ProductOrderRespDTO();
            BeanUtils.copyProperties(productOrderDO, productOrderRespDTO);
            return productOrderRespDTO;
        }).toList();
    }
}
