package com.wimoor.erp.common.service;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.material.pojo.entity.EstimatedSales;

public interface IEstimatedSalesService extends IService<EstimatedSales>{
 
	EstimatedSales findEstimatedSales(Map<String, Object> param);
	
	Boolean updateEstimatedSale(UserInfo user, Map<String, Object> param);
	
	Boolean checkEstimatedSaleIsinvalid(EstimatedSales estimatedSales);
	
	void checkEstimatedSaleIsinvalid();

	int deleteEstimatedSale(UserInfo user, Map<String, Object> param);
}
