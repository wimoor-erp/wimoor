package com.wimoor.erp.finance.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.finance.pojo.entity.FinanceProject;

public interface IFinanceProjectService  extends IService<FinanceProject> {
	public Map<String, Object> saveProject(String name, UserInfo user);

	public Map<String, Object> updateProject(String id, String name, UserInfo user);

	public Map<String, Object> delProject(String id);
	

	public List<FinanceProject> findProject(String shopid);
}
