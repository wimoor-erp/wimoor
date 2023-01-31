package com.wimoor.amazon.product.service;

import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.product.pojo.dto.PlanDTO;
import com.wimoor.amazon.product.pojo.entity.AmzProductSalesPlan;
import com.wimoor.amazon.product.pojo.entity.AmzProductSalesPlanShipItem;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  处理计划
 * </p>
 *
 * @author wimoor team
 * @since 2022-11-28
 */
public interface IAmzProductSalesPlanService extends IService<AmzProductSalesPlan> {
	public void refreshData(Set<String> shopset) ;
	public void refreshData(String groupid);
	public void refreshData(String groupid,String marketplaceid,String sku);
	public void handlePresale(AmazonAuthority auth);
	public List<Map<String, Object>> getPlanModel(PlanDTO dto);
	public Integer getAfterSales(AmzProductSalesPlanShipItem item);
	List<Map<String, Object>> ExpandCountryDataByGroup(String shopid, String groupid, String warehouseid, String msku,String plantype, Boolean iseu, Integer amount);
}
