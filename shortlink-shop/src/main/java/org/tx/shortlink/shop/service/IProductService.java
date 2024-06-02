package org.tx.shortlink.shop.service;

import org.tx.shortlink.shop.DTO.resp.ProductRespDTO;
import org.tx.shortlink.shop.entity.ProductDO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tx
 * @since 2024-06-02
 */
public interface IProductService extends IService<ProductDO> {

    ProductRespDTO getDetailById(Long id);
}
