package com.wimoor.amazon.report.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.report.pojo.entity.ProductRecommended;
 

public interface IReportAmzProductRecommendedListService {
	//public void handlerExtData(String sellerid , Marketplace marketplace,	GetMatchingProductForIdResponse resp) ;

	public List<ProductRecommended> findNeedRefreshList(String id, String marketplaceid);
	
	//public Map<String, Object> calculateAmazonCostDetail(ProductRecommendedExt ext,ProductRcdDimensions dim, String profitCfgId, String cost_);
	
	public Page<Map<String, Object>> getProductRecommendByGrouplist(IPage<?> page,Map<String, Object> map);
}
