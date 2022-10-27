package com.wimoor.amazon.report.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.common.pojo.dto.SummaryMutilParameterQueryDTO;
import com.wimoor.amazon.orders.pojo.dto.AmazonOrdersReturnDTO;
import com.wimoor.amazon.orders.pojo.vo.AmazonOrdersReturnVo;
import com.wimoor.amazon.report.pojo.entity.AmzOrderReturns;

@Mapper
public interface AmzOrderReturnsMapper extends BaseMapper<AmzOrderReturns> {
	List<Map<String,Object>> returnsOrder(SummaryMutilParameterQueryDTO parameter);
	Map<String,Object> findByOrderIdSku(SummaryMutilParameterQueryDTO parameter);
	Map<String,Object> returnsOrderSum(SummaryMutilParameterQueryDTO parameter);
	IPage<AmazonOrdersReturnVo> selectReturnsList(Page<?> page,@Param("param")AmazonOrdersReturnDTO param);
	List<AmazonOrdersReturnVo> selectReturnsList(@Param("param")AmazonOrdersReturnDTO param);
}
 
