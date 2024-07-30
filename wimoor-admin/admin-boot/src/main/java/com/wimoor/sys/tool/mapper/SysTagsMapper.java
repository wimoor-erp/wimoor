package com.wimoor.sys.tool.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.sys.tool.pojo.entity.SysTags;

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
    @Select({"<script>",
	        " SELECT",
	        " 	t1.id,t1.name,t1.value,t1.color,ifnull(t2.sort,0)*100000+ifnull(t1.sort,0) sort ",
	        " FROM",
	        " 	t_sys_tags t1",
	        " 	INNER JOIN t_sys_tags_groups t2 ON t2.id=t1.taggroupid ",
	        " WHERE t2.shop_id = #{shopid} ",
	        "</script>"})
	List<SysTags> listbyshop(String shopid);
}
