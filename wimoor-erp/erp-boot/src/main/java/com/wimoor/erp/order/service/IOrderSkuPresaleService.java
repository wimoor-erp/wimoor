package com.wimoor.erp.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.order.pojo.dto.PresaleListDTO;
import com.wimoor.erp.order.pojo.entity.OrderSkuPresale;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IOrderSkuPresaleService extends IService<OrderSkuPresale>{

	/**
	 * 获取某站点SKU未来的每日预估销量
	 * @param sku
	 * @param warehouseid
	 * @return
	 */
	Map<String, Integer> selectBySKUMarket(String sku, String warehouseid);

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

	void refreshData(String  warehouseid );
	public List<OrderSkuPresale> selectByGroup(String sku, String warehouseid );
	public Map<String,OrderSkuPresale> getPresale(String sku,String warehouseid) ;
	public com.wimoor.erp.common.pojo.entity.Chart getSales(String shopid,String warehouseid,String sku) ;

	String uploadPreSalesByExcel(Workbook workbook, UserInfo user);

	void downloadPreSalesByExcel(Sheet sheet, Sheet sheet2, String groupid, UserInfo user);
	
	public void convertEstimatToPresale();
	
	public IPage<Map<String, Object>> listProduct(PresaleListDTO dto);

	void replaceBatch(List<OrderSkuPresale> preList);

	List<Map<String, Object>> getProductPreSalesByMonth(String sku, String warehouseid);

	List<Map<String, Object>> getProductPreSales(String sku, String warehouseid, String month);
}
