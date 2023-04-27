package com.wimoor.amazon.orders.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.common.pojo.dto.SummaryMutilParameterQueryDTO;
import com.wimoor.amazon.orders.pojo.entity.OrdersSummary;
import com.wimoor.amazon.orders.pojo.vo.ProductSalesRankVo;

@Mapper
public interface OrdersSummaryMapper extends BaseMapper<OrdersSummary> {
	List<Map<String, Object>> lastQtyAmount(Map<String, Object> parameter);

	List<OrdersSummary> selectByPrimaryKeyUpPurchaseDate(OrdersSummary key);

	List<OrdersSummary> selectBySkuMarketplace(Map<String, Object> map);

	int refreshByOrder(Map<String, Object> map);

	List<Map<String, Object>> lastTop5(Map<String, Object> parameter);

	List<Map<String, Object>> lastQtyMarket(Map<String, Object> parameter);

	Map<String, Object> last2Qty(Map<String, Object> parameter);

	Map<String, Object> lastweekQty(Map<String, Object> parameter);

	void refreshShopSummary(HashMap<String, Object> map);

	List<Map<String, Object>> weekAmount(Map<String, Object> map);

	List<OrdersSummary> selectBySkuOfEU(Map<String, Object> map);

	List<Map<String, Object>> getOrderSumField(Map<String, Object> parameter);
	
	IPage<Map<String, Object>> ordershopReport(Page<?> page,@Param("param")Map<String, Object> parameter);

	void saveBatch(List<OrdersSummary> list);
 
	List<Map<String, Object>> selectShopLimit();
	
	List<Map<String, Object>> findordershopReport(Map<String, Object> param);
	
	List<OrdersSummary> selectBySkuMarketplaceForShip(Map<String, Object> map);
	
	IPage<Map<String, Object>> getOrdersPrice(Page<?> page,Map<String, Object> map);

	Map<String, Object> getSumOrdersPrice(Map<String, Object> map);

	OrdersSummary selectByUniqueKey(OrdersSummary orderssum);

	Map<String, Object> getSummaryOrder(Map<String, Object> paramMap);

	List<Map<String, Object>> getOrdersPrice(Map<String, Object> map);
	
	List<ProductSalesRankVo> top5(SummaryMutilParameterQueryDTO dto);
}