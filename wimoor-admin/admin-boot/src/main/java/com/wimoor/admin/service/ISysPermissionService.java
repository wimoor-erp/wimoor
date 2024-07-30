package com.wimoor.admin.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.admin.pojo.entity.SysPermission;
import com.wimoor.admin.pojo.vo.PermissionVO;

public interface ISysPermissionService extends IService<SysPermission> {

    List<SysPermission> listPermRoles();


    List<String> listBtnPermByRoles(List<String> roles);

    /**
     * 刷新Redis缓存中角色菜单的权限规则，角色和菜单信息变更调用
     */
    boolean refreshPermRolesRules();

    IPage<PermissionVO> list(Page<PermissionVO> page, String name, Long menuId);
}
