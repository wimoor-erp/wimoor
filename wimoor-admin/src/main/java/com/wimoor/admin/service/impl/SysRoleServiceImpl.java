package com.wimoor.admin.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.admin.mapper.SysRoleMapper;
import com.wimoor.admin.pojo.entity.SysRole;
import com.wimoor.admin.pojo.entity.SysRoleMenu;
import com.wimoor.admin.pojo.entity.SysRolePermission;
import com.wimoor.admin.pojo.entity.SysUserRole;
import com.wimoor.admin.service.ISysRoleMenuService;
import com.wimoor.admin.service.ISysRolePermissionService;
import com.wimoor.admin.service.ISysRoleService;
import com.wimoor.admin.service.ISysUserRoleService;

import cn.hutool.core.lang.Assert;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    private final ISysRoleMenuService iSysRoleMenuService;
    private ISysUserRoleService iSysUserRoleService;
    private ISysRolePermissionService iSysRolePermissionService;


    @Override
    public boolean delete(List<BigInteger> ids) {
        Optional.ofNullable(ids).orElse(new ArrayList<>()).forEach(id -> {
            long count = iSysUserRoleService.count(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getRoleId, id));
            Assert.isTrue(count <= 0, "该角色已分配用户，无法删除");
            iSysRoleMenuService.remove(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, id));
            iSysRolePermissionService.remove(new LambdaQueryWrapper<SysRolePermission>().eq(SysRolePermission::getRoleId, id));
        });
        return this.removeByIds(ids);
    }

}
