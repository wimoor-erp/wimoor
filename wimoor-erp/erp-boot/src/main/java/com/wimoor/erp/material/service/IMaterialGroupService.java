package com.wimoor.erp.material.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.material.pojo.entity.ERPGroup;
import com.wimoor.erp.material.pojo.entity.MaterialGroup;

public interface IMaterialGroupService extends IService<MaterialGroup> {
  	List<ERPGroup> initfindAllGroup(UserInfo user);
	public int changeMaterialGroup(String materialid,List<String> grouplist,String userid) throws BizException;
	public List<ERPGroup> getMaterialGroup(UserInfo user,String materialid);
	int newGroup(String groupname, String groupcolor, UserInfo user);
    int deleteGroup(String groupkey);
	
}
