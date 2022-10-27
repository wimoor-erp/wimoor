package com.wimoor.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.admin.pojo.entity.SysMenu;
 
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wimoor
 * @date 2022-03-10
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenu> listRoute(String userid);
    List<SysMenu> listRouteAll();
}
