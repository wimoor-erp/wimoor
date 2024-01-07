package com.wimoor.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.admin.pojo.entity.SysPermission;
import com.wimoor.admin.pojo.vo.PermissionVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermission> {


    List<SysPermission> listPermRoles();

    List<String> listBtnPermByRoles(List<String> roles);

    List<PermissionVO> list(Page<PermissionVO> page, String name, Long menuId);
}
