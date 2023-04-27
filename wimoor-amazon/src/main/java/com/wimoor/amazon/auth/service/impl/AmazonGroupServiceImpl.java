package com.wimoor.amazon.auth.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.api.AdminClientOneFeign;
import com.wimoor.amazon.auth.mapper.AmazonGroupMapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.common.user.UserInfo;

import lombok.RequiredArgsConstructor;
/**
 * 用户业务类
 */
@Service
@RequiredArgsConstructor
public class AmazonGroupServiceImpl extends ServiceImpl<AmazonGroupMapper, AmazonGroup> implements IAmazonGroupService {
   
	final AdminClientOneFeign adminClientOneFeign;
	
	public List<AmazonGroup> getGroupByUser(UserInfo user){
			List<AmazonGroup> groupList = null;
			if (user.getGroups() != null && user.getGroups().size() >0) {
				groupList = this.lambdaQuery().in(AmazonGroup::getId, user.getGroups()).list();	 
			} else {
				groupList = this.baseMapper.findShopNameByCompany(user.getCompanyid());
			}
			if (groupList == null || groupList.size() == 0) {
				groupList = this.baseMapper.findShopNameByCompany(user.getCompanyid());
			}
		   return groupList;
	}

	@Override
	public List<AmazonGroup> selectByShopId(String shopid) {
		// TODO Auto-generated method stub
			QueryWrapper<AmazonGroup> query = new QueryWrapper<AmazonGroup>();
			query.eq("shopid", shopid);
			List<AmazonGroup> glist = this.baseMapper.selectList(query);
			return glist;
	}

 
}
