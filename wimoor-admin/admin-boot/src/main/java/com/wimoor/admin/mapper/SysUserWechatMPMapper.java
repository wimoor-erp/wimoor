package com.wimoor.admin.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.admin.pojo.entity.SysUserWechatMP;
@Mapper
public interface SysUserWechatMPMapper extends BaseMapper<SysUserWechatMP> {
	SysUserWechatMP selectByUserId(String userid);
}
