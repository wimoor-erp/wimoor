package com.wimoor.amazon.adv.report.service.impl;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.wimoor.amazon.adv.report.service.IAmzAdvReportPlacementService;
import com.wimoor.amazon.adv.sp.dao.AmzAdvReportPlacementMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportPlacement;
import com.wimoor.amazon.base.BaseService;
 
@Service("amzAdvReportPlacementService")
public class AmzAdvReportPlacementServiceImpl  extends BaseService<AmzAdvReportPlacement> implements IAmzAdvReportPlacementService{
@Resource
AmzAdvReportPlacementMapper amzAdvReportPlacementMapper;
	
 
@Cacheable(cacheNames = "advPlacementCache")
synchronized public AmzAdvReportPlacement loadIDByPlacementName(String name) {
	AmzAdvReportPlacement result = amzAdvReportPlacementMapper.selectByPlacement(name);
	if(result==null) {
		 AmzAdvReportPlacement record=new AmzAdvReportPlacement();
		 record.setName(name);
		 int i=amzAdvReportPlacementMapper.myinsert(name);
		 if(i>0) {
			   result = amzAdvReportPlacementMapper.selectByPlacement(name);
			 return result;
		 }else {
			   result = amzAdvReportPlacementMapper.selectByPlacement(name);
			   if(result!=null) {
				   return result;
			   }else {
				  i=amzAdvReportPlacementMapper.myinsert(name);
				  result = amzAdvReportPlacementMapper.selectByPlacement(name);
				  return result;
			   }
		 }
	}else {
		return result;
	}
}
 
}
