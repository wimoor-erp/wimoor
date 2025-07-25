package com.wimoor.amazon.auth.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.api.AdminClientOneFeignManager;
import com.wimoor.amazon.auth.mapper.AmazonGroupMapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.report.mapper.ReportRequestRecordMapper;
import com.wimoor.common.user.UserInfo;

import lombok.RequiredArgsConstructor;
/**
 * 用户业务类
 */
@Service
@RequiredArgsConstructor
public class AmazonGroupServiceImpl extends ServiceImpl<AmazonGroupMapper, AmazonGroup> implements IAmazonGroupService {
   
	final AdminClientOneFeignManager adminClientOneFeign;
	
	final ReportRequestRecordMapper reportRequestRecordMapper;
	
	public List<AmazonGroup> getGroupByUser(UserInfo user){
			List<AmazonGroup> groupList = null;
			if (user.getGroups() != null && user.getGroups().size() >0) {
				groupList = this.lambdaQuery().in(AmazonGroup::getId, user.getGroups()).orderByAsc(AmazonGroup::getFindex).list();	 
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

	@Override
	public AmazonGroup findAmazonGroupByName(String groupname, String shopid) {
		// TODO Auto-generated method stub
		LambdaQueryWrapper<AmazonGroup> query = new LambdaQueryWrapper<AmazonGroup>();
		query.eq(AmazonGroup::getShopid, shopid);
		query.eq(AmazonGroup::getName, groupname);
		List<AmazonGroup> amazonGroup = this.list(query);
		if (amazonGroup != null && amazonGroup.size() > 0) {
			return amazonGroup.get(0);
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> selectTaskInfoList(String sellerid, String marketplaceid) {
		return reportRequestRecordMapper.selectTaskInfoList(sellerid, marketplaceid);
	}

 
}
