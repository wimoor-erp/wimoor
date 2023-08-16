package com.wimoor.admin.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.admin.pojo.entity.SystemTarget;
 
@Mapper
public interface SystemTargetMapper extends BaseMapper<SystemTarget>{
	List<SystemTarget> findAllTarget();
}
