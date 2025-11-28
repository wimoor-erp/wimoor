package com.wimoor.amazon.product.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.product.pojo.entity.ProductInTags;

@Mapper
public interface ProductInTagsMapper extends BaseMapper<ProductInTags>{
	List<String> getPidList(@Param("param") Map<String, Object> param);
	List<String> getTagNameList(String pid);
}
