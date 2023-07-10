package com.wimoor.amazon.profit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.profit.pojo.entity.ProductTier;

public interface IProductTierService extends IService<ProductTier> {

	ProductTier selectByPKey(String productTierId);

}
