package com.wimoor.amazon.adv.common.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.wimoor.common.user.UserInfo;

public interface IAmzAdvertCreateService {
	
	void insertAdvcrt(JSONObject jsonobject,UserInfo user);
	
	void insertSBAdv(JSONObject jsonobject,UserInfo user);
	
	Object insertFirstAdv(JSONObject jsonobject,UserInfo user);
	
	List<Map<String,Object>> UpdateFirstAdv(JSONObject jsonobject,UserInfo user);

	void amzInsertSDAdv(JSONObject jsonobject, UserInfo user);
}
