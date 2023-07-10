package com.wimoor.admin.service;


import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.admin.pojo.entity.SysUserRole;

public interface ISysUserRoleService extends IService<SysUserRole> {
	public List<SysUserRole> findByUserId(String userid);
}
