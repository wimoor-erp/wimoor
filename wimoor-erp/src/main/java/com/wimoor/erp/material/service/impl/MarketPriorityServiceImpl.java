package com.wimoor.erp.material.service.impl;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.erp.common.mapper.MarketPriorityMapper;
import com.wimoor.erp.common.pojo.entity.MarketPriority;
import com.wimoor.erp.common.service.IMarketPriorityService;
import com.wimoor.erp.material.pojo.entity.MaterialCategory;

@Service("marketPriorityService")
public class MarketPriorityServiceImpl extends  ServiceImpl<MarketPriorityMapper,MarketPriority> implements IMarketPriorityService {
	  

	public String saveMarketPriority(List<Map<String, Object>> mapList, String groupid) {
		String msg=null;
		int result = 0;
		for (int i = 0; i < mapList.size(); i++) {
			QueryWrapper<MarketPriority> queryWrapper=new QueryWrapper<MarketPriority>();
			MarketPriority mar = new MarketPriority();
			mar.setGroupid(groupid);
			mar.setMarketplaceid(mapList.get(i).get("marketplaceId").toString());
			mar.setPriority(Integer.parseInt(mapList.get(i).get("priority").toString().trim()));
			queryWrapper.eq("groupid", groupid);
			queryWrapper.eq("marketplaceId", mar.getMarketplaceid());
	 
			MarketPriority oldmar = this.getOne(queryWrapper);
			if (oldmar==null) {
				if(this.save(mar)) {
					result ++;
				}
			} else {
				if(this.update(mar, queryWrapper)) {
					result ++;
				}
			}
		}
		if(result>0){
			msg="操作成功！";
		} else {
			msg="操作失败！";
		}
		return msg;
	}
	 
	

}
