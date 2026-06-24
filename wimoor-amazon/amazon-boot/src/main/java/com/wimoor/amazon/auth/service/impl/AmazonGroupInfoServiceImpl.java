package com.wimoor.amazon.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.auth.mapper.AmazonGroupInfoMapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroupInfo;
import com.wimoor.amazon.auth.service.IAmazonGroupInfoService;
import org.springframework.stereotype.Service;

@Service
public class AmazonGroupInfoServiceImpl extends ServiceImpl<AmazonGroupInfoMapper, AmazonGroupInfo> implements IAmazonGroupInfoService {

	@Override
	public AmazonGroupInfo getByGroupid(String groupid) {
		return this.lambdaQuery().eq(AmazonGroupInfo::getGroupid, groupid).one();
	}

	@Override
	public boolean saveOrUpdateByGroupid(AmazonGroupInfo groupInfo) {
		if (groupInfo.getGroupid() == null || groupInfo.getGroupid().isEmpty()) {
			return false;
		}
		AmazonGroupInfo existing = this.getByGroupid(groupInfo.getGroupid());
		if (existing != null) {
			groupInfo.setGroupid(existing.getGroupid());
			return this.updateById(groupInfo);
		} else {
			return this.save(groupInfo);
		}
	}
}
