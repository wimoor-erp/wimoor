package com.wimoor.admin.service;

import com.wimoor.admin.pojo.entity.SysUserBind;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wimoor team
 * @since 2023-05-25
 */
public interface ISysUserBindService extends IService<SysUserBind> {

	String getBindId(String userid);

	Boolean save(String userid, String bindid);

	void bindMpUser(String openid, String id);

	List<SysUserBind> getBindListUser(String userid);

	List<SysUserBind> getBindList(String bindid);

}
