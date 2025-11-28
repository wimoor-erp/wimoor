package com.wimoor.amazon.product.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.common.pojo.vo.Chart;
import com.wimoor.amazon.product.pojo.dto.ProductPresaleListDTO;
import com.wimoor.amazon.product.pojo.entity.ProductInPresale;
import com.wimoor.common.user.UserInfo;

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

	void refreshData(AmazonAuthority auth);
	public List<ProductInPresale> selectByGroup(String sku,String marketplaceid,String groupid );
	public Map<String,ProductInPresale> getPresale(String sku,String marketplaceid,AmazonAuthority auth) ;
	public Map<String,ProductInPresale> getPresale(String sku,String marketplaceid,String groupid) ;
	public Chart getSales(String shopid,String marketplaceid,String amazonauthid,String sku) ;

	String uploadPreSalesByExcel(Workbook workbook, UserInfo user);

	void downloadPreSalesByExcel(Sheet sheet, Sheet sheet2, String groupid, UserInfo user);
	
	public void convertEstimatToPresale();
	
	public IPage<Map<String, Object>> listProduct(ProductPresaleListDTO dto);

	void replaceBatch(List<ProductInPresale> preList);

	List<Map<String, Object>> getProductPreSalesByMonth(String sku, String marketplaceid, String groupid);

	List<Map<String, Object>> getProductPreSales(String sku, String marketplaceid, String groupid, String month);
}
