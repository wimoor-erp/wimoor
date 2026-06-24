package com.wimoor.amazon.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroupInfo;

public interface IAmazonGroupInfoService extends IService<AmazonGroupInfo> {

	AmazonGroupInfo getByGroupid(String groupid);

	boolean saveOrUpdateByGroupid(AmazonGroupInfo groupInfo);
}
