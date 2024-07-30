package com.wimoor.admin.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.admin.mapper.SysUserRoleMapper;
import com.wimoor.admin.pojo.entity.SysUserRole;
import com.wimoor.admin.service.ISysUserRoleService;

@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

	public List<SysUserRole> findByUserId(String userid){
		QueryWrapper<SysUserRole> queryWrapper =new QueryWrapper<SysUserRole>();
		queryWrapper.eq("user_id", userid);
		return this.list(queryWrapper);
	}
}
