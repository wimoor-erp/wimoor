package com.wimoor.erp.material.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.wimoor.erp.material.pojo.entity.MaterialTags;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface MaterialTagsMapper extends BaseMapper<MaterialTags>{
	List<String> getmskuList(@Param("list") List<String> list);

	List<String> getMidList(@Param("taglist") List<String> taglist);
	
	List<Map<String,String>> getTagsBySku(Map<String,Object> param);
}
