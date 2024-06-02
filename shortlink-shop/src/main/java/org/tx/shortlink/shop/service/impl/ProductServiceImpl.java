package org.tx.shortlink.shop.service.impl;

import org.springframework.beans.BeanUtils;
import org.tx.shortlink.shop.DTO.resp.ProductRespDTO;
import org.tx.shortlink.shop.entity.ProductDO;
import org.tx.shortlink.shop.mapper.ProductMapper;
import org.tx.shortlink.shop.service.IProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tx
 * @since 2024-06-02
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, ProductDO> implements IProductService {

    @Override
    public ProductRespDTO getDetailById(Long id) {
        ProductDO productDO = getById(id);
        ProductRespDTO productRespDTO = new ProductRespDTO();
        BeanUtils.copyProperties(productDO, productRespDTO);
        return productRespDTO;
    }
}
