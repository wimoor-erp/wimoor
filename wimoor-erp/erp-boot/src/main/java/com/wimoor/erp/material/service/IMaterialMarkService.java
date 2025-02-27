package com.wimoor.erp.material.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.material.pojo.entity.MaterialMark;

public interface IMaterialMarkService extends IService<MaterialMark>{
	  public boolean saveNotice(UserInfo user,String materialid,String notice) ; 
	  public boolean hide(UserInfo user,String materialid)  ;
	  public boolean show(UserInfo user,String materialid) ;
	  public MaterialMark showNotice(String materialid);
}
