package com.wimoor.amazon.orders.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.common.pojo.dto.SummaryMutilParameterQueryDTO;
import com.wimoor.amazon.orders.pojo.dto.AmazonOrdersReturnDTO;
import com.wimoor.amazon.orders.pojo.entity.AmzOrderReturns;
import com.wimoor.amazon.orders.pojo.vo.AmazonOrdersReturnVo;

@Mapper
public interface AmzOrderReturnsMapper extends BaseMapper<AmzOrderReturns> {
	List<Map<String,Object>> returnsOrder(SummaryMutilParameterQueryDTO parameter);
	Map<String,Object> findByOrderIdSku(SummaryMutilParameterQueryDTO parameter);
	Map<String,Object> returnsOrderSum(SummaryMutilParameterQueryDTO parameter);
	List<Map<String, Object>> getBySellerId(@Param("sellerid")String sellerid);
	List<AmazonOrdersReturnVo> selectReturnsList(@Param("param")AmazonOrdersReturnDTO param);
	IPage<AmazonOrdersReturnVo> selectReturnsList(Page<?> page,@Param("param")AmazonOrdersReturnDTO param);
	IPage<Map<String,Object>> selectReturnsListBySKU(Page<?> page,@Param("param")AmazonOrdersReturnDTO param);
	List<Map<String,Object>> selectReturnsSummaryByDay(@Param("param")AmazonOrdersReturnDTO param);
	List<Map<String,Object>> selectOrderSummaryByDay(@Param("param")AmazonOrdersReturnDTO param);
	List<Map<String, Object>> selectReturnsSummaryByType(@Param("param")AmazonOrdersReturnDTO condition);
}
 
