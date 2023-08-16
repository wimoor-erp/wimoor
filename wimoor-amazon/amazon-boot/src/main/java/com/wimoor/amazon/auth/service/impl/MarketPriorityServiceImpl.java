package com.wimoor.amazon.auth.service.impl;


import java.util.Date;
import java.util.List;


import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.auth.mapper.MarketPriorityMapper;
import com.wimoor.amazon.auth.pojo.entity.MarketPriority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IMarketPriorityService;
import com.wimoor.common.user.UserInfo;

@Service("marketPriorityService")
public class MarketPriorityServiceImpl extends  ServiceImpl<MarketPriorityMapper,MarketPriority> implements IMarketPriorityService {
	  

	public String saveMarketPriority(UserInfo userinfo,List<MarketPriority> list) {
		String msg=null;
		int result = 0;
		for (int i = 0; i < list.size(); i++) {
			MarketPriority newone=list.get(i);
			newone.setOperator(userinfo.getId());
			newone.setOpttime(new Date());
			QueryWrapper<MarketPriority> queryWrapper=new QueryWrapper<MarketPriority>();
			queryWrapper.eq("groupid", newone.getGroupid());
			queryWrapper.eq("marketplaceId", newone.getMarketplaceid());
			MarketPriority oldmar = this.getOne(queryWrapper);
			if (oldmar==null) {
				if(this.save(newone)) {
					result ++;
				}
			} else {
				if(this.update(newone, queryWrapper)) {
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

	@Override
	public List<Marketplace> findMarketplaceByGroup(String groupid) {
		// TODO Auto-generated method stub
		return this.baseMapper.findMarketplaceByGroup(groupid) ;
	}
	 
	

}
