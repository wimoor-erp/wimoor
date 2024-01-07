package com.wimoor.amazon.orders.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.orders.pojo.dto.AmazonOrdersRemoveDTO;
import com.wimoor.amazon.orders.pojo.entity.AmzOrderRemoves;
import com.wimoor.amazon.orders.pojo.vo.AmazonOrdersRemoveVo;
@Mapper
public interface AmzOrderRemovesMapper  extends BaseMapper<AmzOrderRemoves> {
	IPage<AmazonOrdersRemoveVo> selectRemoveList(Page<?> page,@Param("param")AmazonOrdersRemoveDTO param);
	List<AmazonOrdersRemoveVo> selectRemoveList(@Param("param")AmazonOrdersRemoveDTO param);
	Map<String, Object> selectPInfoBySku(Map<String, Object> paramMap);
}


 
