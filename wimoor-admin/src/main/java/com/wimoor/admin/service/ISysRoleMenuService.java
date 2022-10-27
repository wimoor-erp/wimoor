package com.wimoor.admin.service;


import java.math.BigInteger;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.admin.pojo.entity.SysRoleMenu;

public interface ISysRoleMenuService extends IService<SysRoleMenu> {

    List<BigInteger> listMenuIds(BigInteger roleId);

    /**
     * 修改角色菜单
     * @param roleId
     * @param menuIds
     * @return
     */
    boolean update(BigInteger roleId, List<BigInteger> menuIds);
}
