package com.wimoor.amazon.auth.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.api.AdminClientOneFeign;
import com.wimoor.amazon.auth.mapper.AmazonGroupMapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.api.admin.pojo.dto.SysUserRoleDTO;
import com.wimoor.common.result.Result;
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
			Result<List<SysUserRoleDTO>> result = adminClientOneFeign.getUserRoleById(user.getId());
			List<SysUserRoleDTO> rolelist = result.getData();
			List<AmazonGroup> groupList = null;
			if (rolelist != null && rolelist.size() > 1) {
				groupList = new ArrayList<AmazonGroup>();
				for (SysUserRoleDTO role : rolelist) {
					List<Map<String, Object>> listmap = this.baseMapper.getRoleGroupListByRoleid(role.getRoleId().toString());
					if (listmap == null || listmap.size() == 0) {
						groupList = this.baseMapper.findShopNameByUser(user.getCompanyid());
						break;
					} else {
						groupList = this.baseMapper.getUserGroupList(user.getId());
					}
				}
			} else {
				groupList = this.baseMapper.getUserGroupList(user.getId());
			}
			if (groupList == null || groupList.size() == 0) {
				groupList = this.baseMapper.findShopNameByUser(user.getCompanyid());
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
