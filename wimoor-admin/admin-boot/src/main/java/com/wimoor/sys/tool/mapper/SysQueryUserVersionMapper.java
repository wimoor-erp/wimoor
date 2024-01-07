package com.wimoor.sys.tool.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.sys.tool.pojo.entity.SysQueryUserVersion;

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
