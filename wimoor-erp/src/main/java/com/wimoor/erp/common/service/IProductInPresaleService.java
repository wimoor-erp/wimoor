package com.wimoor.erp.common.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.pojo.entity.ProductInPresale;

public interface IProductInPresaleService extends IService<ProductInPresale>{

	/**
	 * 获取某站点SKU未来的每日预估销量
	 * @param sku
	 * @param marketplaceid
	 * @param groupid
	 * @return
	 */
	Map<String, Integer> selectBySKUMarket(String sku, String marketplaceid, String groupid);

	/**
	 * 根据未来每日销量，计算备货周期内需要消耗的库存
	 * @param presaleMap
	 * @param stocking_cycle
	 * @param presale
	 * @return
	 */
	int getTotalPreSales(Map<String, Integer> presaleMap, int stocking_cycle, int presale);

	/**
	 * 根据未来每日销量，计算现有库存可以销售的天数，不足一天按一天计算
	 * @param presaleMap
	 * @param quantity
	 * @param presale
	 * @return
	 */
	int getSalesday(Map<String, Integer> presaleMap, int quantity, int presale);

	/**
	 * 根据各个站点未来每日销量，计算本地仓库在备货周期内需要消耗的库存
	 * @param sumpresaleMap
	 * @param cycle
	 * @param fbasale
	 * @return
	 */
	int getLocalTotalPreSales(Map<String, Map<String, Integer>> sumpresaleMap, int cycle,
			List<Map<String, Object>> fbasale);

	/**
	 * 根据各个站点未来每日销量，计算本地仓库现有库存可以销售的天数，不足一天按一天计算
	 * @param sumpresaleMap
	 * @param selfsale_quantity_sum
	 * @param fbasale
	 * @return
	 */
	int getLocalSalesday(Map<String, Map<String, Integer>> sumpresaleMap, int selfsale_quantity_sum,
			List<Map<String, Object>> fbasale);

	List<Date> getdatelist(Calendar startcal, Calendar endcal);

	
}
