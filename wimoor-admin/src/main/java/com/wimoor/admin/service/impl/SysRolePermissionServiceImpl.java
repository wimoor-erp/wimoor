package com.wimoor.admin.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.admin.mapper.SysRolePermissionMapper;
import com.wimoor.admin.pojo.dto.RolePermissionDTO;
import com.wimoor.admin.pojo.entity.SysRolePermission;
import com.wimoor.admin.service.ISysRolePermissionService;

import cn.hutool.core.collection.CollectionUtil;

@Service
public class SysRolePermissionServiceImpl extends ServiceImpl<SysRolePermissionMapper, SysRolePermission> implements ISysRolePermissionService {


    @Override
    public List<BigInteger> listPermissionId(BigInteger roleId) {
        return this.baseMapper.listPermissionId(null, roleId);
    }

    @Override
    public List<BigInteger> listMenuPermissionId(BigInteger menuId) {
        return this.baseMapper.listMenuPermissionId(menuId);
    }
    
    @Override
    public List<BigInteger> listPermissionId(BigInteger menuId, BigInteger roleId) {
        return this.baseMapper.listPermissionId(menuId, roleId);
    }

    @Override
    public boolean update(RolePermissionDTO rolePermission) {
        boolean result = true;
        List<BigInteger> permissionIds = rolePermission.getPermissionIds();
        BigInteger menuId = rolePermission.getMenuId();
        BigInteger roleId = rolePermission.getRoleId();
        List<BigInteger> dbPermissionIds = this.baseMapper.listPermissionId(menuId, roleId);

        // 删除数据库存在此次提交不存在的
        if (CollectionUtil.isNotEmpty(dbPermissionIds)) {
            List<BigInteger> removePermissionIds = dbPermissionIds.stream().filter(id -> !permissionIds.contains(id)).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(removePermissionIds)) {
                this.remove(new LambdaQueryWrapper<SysRolePermission>().eq(SysRolePermission::getRoleId, roleId)
                        .in(SysRolePermission::getPermissionId, removePermissionIds));
            }
        }

        // 插入数据库不存在的
        if (CollectionUtil.isNotEmpty(permissionIds)) {
            List<BigInteger> insertPermissionIds = permissionIds.stream().filter(id -> !dbPermissionIds.contains(id)).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(insertPermissionIds)) {
                List<SysRolePermission> roleMenus = new ArrayList<>();
                for (BigInteger insertPermissionId : insertPermissionIds) {
                    SysRolePermission sysRolePermission = new SysRolePermission().setRoleId(roleId).setPermissionId(insertPermissionId);
                    roleMenus.add(sysRolePermission);
                }
                result = this.saveBatch(roleMenus);
            }
        }
        return result;
    }


}
