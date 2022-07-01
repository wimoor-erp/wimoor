package com.wimoor.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.admin.pojo.entity.SysRoleMenu;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    @Select("<script>" +
            "  select role_id from sys_role_menu where menu_id=#{menuId} " +
            "</script>")
    List<Integer> listByMenuId(Integer menuId);


    @Select(" SELECT " +
            " 	t1.menu_id  " +
            " FROM " +
            " 	sys_role_menu t1 " +
            " 	INNER JOIN sys_menu t2 ON t1.menu_id = t2.id  " +
            " WHERE role_id =#{roleId}")
    List<Long> listMenuIds(BigInteger roleId);
}
