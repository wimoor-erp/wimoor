package com.wimoor.amazon.profit.service;

import java.math.BigDecimal;

import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.common.pojo.entity.DimensionsInfo;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;
import com.wimoor.amazon.profit.pojo.vo.CostDetail;

public interface ICalculateProfitService {
	public CostDetail getProfit(ProductInfo info, BigDecimal price, AmazonAuthority auth,DimensionsInfo dim_local,BigDecimal cost) ;
}
