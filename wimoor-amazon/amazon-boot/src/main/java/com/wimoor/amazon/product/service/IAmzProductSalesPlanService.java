package com.wimoor.amazon.product.service;

import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.product.pojo.dto.PlanDTO;
import com.wimoor.amazon.product.pojo.dto.PlanDetailDTO;
import com.wimoor.amazon.product.pojo.entity.AmzProductSalesPlan;
import com.wimoor.amazon.product.pojo.entity.AmzProductSalesPlanShipItem;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
	public IPage<Map<String, Object>> getPlanModel(Page<Object> page, PlanDTO dto);
	public Integer getAfterSales(AmzProductSalesPlanShipItem item);
	public List<Map<String, Object>> handlePurchase(PlanDTO dto, List<Map<String, Object>> list) ;
	public void setPurchaseRecord(List<Map<String,Object>> record);
    public List<Map<String, Object>> handleShip(PlanDTO dto, List<Map<String, Object>> list);
	public  void handleTags(PlanDTO dto, IPage<Map<String, Object>> page) ;
	public            List<Map<String, Object>> ExpandCountryDataByGroup(PlanDetailDTO dto);
	public Map<String,List<Map<String,Object>>> ExpandCountrysDataByGroup(PlanDetailDTO dto);
	
}