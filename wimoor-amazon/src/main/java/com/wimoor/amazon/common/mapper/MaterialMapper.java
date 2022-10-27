package com.wimoor.amazon.common.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.common.pojo.entity.Material;
@Mapper
public interface MaterialMapper extends BaseMapper<Material>{
	List<Map<String,Object>> getOwnerList(String shopid);
}
