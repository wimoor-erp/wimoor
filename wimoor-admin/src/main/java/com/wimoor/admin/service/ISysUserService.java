package com.wimoor.admin.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.admin.pojo.entity.SysUser;
import com.wimoor.admin.pojo.vo.UserVO;
import com.wimoor.common.user.UserInfo;

public interface ISysUserService  extends IService<SysUser> {
	public SysUser getUserAllByAccount(String account);
	
	public SysUser getUserAllById(String userid);
	
	public SysUser bindOpenId(String openid, String account, String password);

	public SysUser getUserAllByOpenid(String account);
	
    public Map<String, Object> getUserInfoById(String id) ;
    
	UserInfo convertToUserInfo(SysUser user);

	public IPage<UserVO> listQuery(Page<?> page, String name);

	public int unbindWechat(UserInfo userInfo, String ftype);

	public int unbindAccount(UserInfo userInfo);

	public List<SysUser> findMpUserByOpenid(String openid);

	public List<SysUser> findAppUserByOpenid(String openid);

	SysUser verifyAccount(String account, String password);
}
