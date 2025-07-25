package com.wimoor.admin.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.admin.mapper.SysRoleMenuMapper;
import com.wimoor.admin.pojo.entity.SysMenu;
import com.wimoor.admin.pojo.entity.SysRoleMenu;
import com.wimoor.admin.service.ISysMenuService;
import com.wimoor.admin.service.ISysRoleMenuService;

import cn.hutool.core.collection.CollectionUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {
    @Autowired
    @Lazy
	ISysMenuService iSysMenuService;
    
    @Override
    public List<BigInteger> listMenuIds(BigInteger roleId) {
        return this.baseMapper.listMenuIds(roleId);
    }

    @Override
    public boolean update(BigInteger roleId, List<BigInteger> menuIds) {
        boolean result = true;
        Set<BigInteger> menuset=new HashSet<BigInteger>();
        for(BigInteger menuid:menuIds) {
        	SysMenu menu = iSysMenuService.getById(menuid);
        	 while(menu!=null) {
        		if(!menuset.contains(new BigInteger(menu.getId()))) {
            		menuset.add(new BigInteger(menu.getId()));
            	}
            	if(!menu.getParentId().equals("0")&&!menuset.contains(new BigInteger(menu.getParentId()))) {
            		menuset.add(new BigInteger(menu.getParentId()));
            	}
            	menu = iSysMenuService.getById(menu.getParentId());
        	}
        	
        }
        
        List<BigInteger> dbMenuIds = this.listMenuIds(roleId) ;
        // 删除数据库存在此次提交不存在的
        if (CollectionUtil.isNotEmpty(dbMenuIds)) {
            List<BigInteger> removeMenuIds = dbMenuIds.stream().filter(id -> !menuset.contains(id)).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(removeMenuIds)) {
                this.remove(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId)
                        .in(SysRoleMenu::getMenuId, removeMenuIds));
            }
        }

        // 插入数据库不存在的
        if (CollectionUtil.isNotEmpty(menuset)) {
            List<BigInteger> insertMenuIds = menuset.stream().filter(id -> !dbMenuIds.contains(id)).collect(Collectors.toList());
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
