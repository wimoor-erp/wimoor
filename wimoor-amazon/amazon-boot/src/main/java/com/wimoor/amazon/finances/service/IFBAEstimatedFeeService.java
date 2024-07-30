package com.wimoor.amazon.finances.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.finances.pojo.entity.FBAEstimatedFee;
public interface IFBAEstimatedFeeService  extends IService<FBAEstimatedFee>{

	FBAEstimatedFee getOneBySku(String sku,String asin,String amazonauthid,String marketplaceid);

	Long selectCount(QueryWrapper<FBAEstimatedFee> query);
 
}
