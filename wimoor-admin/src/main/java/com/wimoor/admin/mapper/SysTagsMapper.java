package com.wimoor.admin.mapper;

import com.wimoor.admin.pojo.entity.SysTags;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2022-10-28
 */

@Mapper
public interface SysTagsMapper extends BaseMapper<SysTags> {
	List<SysTags> list(Page<SysTags> page, SysTags tags);
}
