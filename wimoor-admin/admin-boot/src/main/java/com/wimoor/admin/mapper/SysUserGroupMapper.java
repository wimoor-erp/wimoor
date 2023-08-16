package com.wimoor.admin.mapper;

import com.wimoor.admin.pojo.entity.SysUserGroup;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户客户对店铺的权限绑定 Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2022-09-29
 */
@Mapper
public interface SysUserGroupMapper extends BaseMapper<SysUserGroup> {

}
