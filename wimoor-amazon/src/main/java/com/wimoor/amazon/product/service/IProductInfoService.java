package com.wimoor.amazon.product.service;


import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;

/**
 * <p>
 * 产品信息 服务类
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-27
 */
public interface IProductInfoService extends IService<ProductInfo> {

	List<ProductInfo> selectBySku(String sku, String marketplaceid, String amazonAuthId);

	ProductInfo productOnlyone(String amazonAuthId, String sku, String marketplaceid);

	List<Map<String, Object>> findShopSku(String shopid, String sku);

	List<ProductInfo> selectByAsin(String amazonAuthId, String asin, String marketplaceid);
 

}
