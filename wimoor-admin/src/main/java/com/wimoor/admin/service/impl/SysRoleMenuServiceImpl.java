package com.wimoor.admin.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.admin.mapper.SysRoleMenuMapper;
import com.wimoor.admin.pojo.entity.SysRoleMenu;
import com.wimoor.admin.service.ISysRoleMenuService;

import cn.hutool.core.collection.CollectionUtil;

@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {

    @Override
    public List<BigInteger> listMenuIds(BigInteger roleId) {
        return this.baseMapper.listMenuIds(roleId);
    }

    @Override
    public boolean update(BigInteger roleId, List<BigInteger> menuIds) {
        boolean result = true;
        List<BigInteger> dbMenuIds = this.list(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId))
                .stream().map(item -> item.getMenuId()).collect(Collectors.toList());

        // 删除数据库存在此次提交不存在的
        if (CollectionUtil.isNotEmpty(dbMenuIds)) {
            List<BigInteger> removeMenuIds = dbMenuIds.stream().filter(id -> !menuIds.contains(id)).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(removeMenuIds)) {
                this.remove(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId)
                        .in(SysRoleMenu::getMenuId, removeMenuIds));
            }
        }

        // 插入数据库不存在的
        if (CollectionUtil.isNotEmpty(menuIds)) {
            List<BigInteger> insertMenuIds = menuIds.stream().filter(id -> !dbMenuIds.contains(id)).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(insertMenuIds)) {
                List<SysRoleMenu> roleMenus = new ArrayList<>();
                for (BigInteger insertMenuId : insertMenuIds) {
                    SysRoleMenu roleMenu = new SysRoleMenu().setRoleId(roleId).setMenuId(insertMenuId);
                    roleMenus.add(roleMenu);
                }
                result = this.saveBatch(roleMenus);
            }
        }
        return result;
    }
}
