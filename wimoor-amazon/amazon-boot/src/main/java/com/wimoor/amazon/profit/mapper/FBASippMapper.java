package com.wimoor.amazon.profit.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.profit.pojo.entity.FBASipp;

@Mapper
public interface FBASippMapper  extends BaseMapper<FBASipp> {

	FBASipp findFormat(Map<String,Object> map);

}
