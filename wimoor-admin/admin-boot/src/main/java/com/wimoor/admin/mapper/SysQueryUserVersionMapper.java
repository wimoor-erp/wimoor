package com.wimoor.admin.mapper;

import com.wimoor.admin.pojo.entity.SysQueryUserVersion;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2023-04-26
 */
@Mapper
public interface SysQueryUserVersionMapper extends BaseMapper<SysQueryUserVersion> {
	SysQueryUserVersion selectLastRow();
	List<Map<String, Object>> selectByQueryAndField(@Param("versionid") String versionId);
}
