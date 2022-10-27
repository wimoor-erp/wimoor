package com.wimoor.amazon.product.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.product.mapper.ProductInPresaleMapper;
import com.wimoor.amazon.product.pojo.entity.IProductInPresaleService;
import com.wimoor.amazon.product.pojo.entity.ProductInPresale;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.user.UserInfo;

import lombok.RequiredArgsConstructor;
 

@Service("productInPresaleService")
@RequiredArgsConstructor
public class ProductInPresaleServiceServiceImpl extends ServiceImpl<ProductInPresaleMapper,ProductInPresale> implements IProductInPresaleService {

	public Map<String, Integer> selectBySKUMarket(String sku, String marketplaceid, String groupid) {
		Map<String, Integer> presaleMap = null; 
		List<ProductInPresale> presalelist = this.baseMapper.selectEvent(sku, marketplaceid, groupid);
		if (presalelist != null && presalelist.size() > 0) {
			presaleMap = new HashMap<String, Integer>();
			for (ProductInPresale productInPresale : presalelist) {
				String key = GeneralUtil.formatDate(productInPresale.getDate(), "yyyy-MM-dd");
				presaleMap.put(key, productInPresale.getQuantity());
			}
		}
		return presaleMap;
	}
	
	public int getTotalPreSales(Map<String, Integer> presaleMap, int stocking_cycle, int presale) {
		int sumpresales = 0;
		Calendar now = Calendar.getInstance();
		for (int i = 1; i <= stocking_cycle; i++) {
			now.add(Calendar.DAY_OF_MONTH, 1);
			String dateStr = GeneralUtil.formatDate(now.getTime(), "yyyy-MM-dd");
			if (presaleMap.get(dateStr) != null) {
				sumpresales += presaleMap.get(dateStr);
			} else {
				sumpresales += presale;
			}
		}
		return sumpresales;
	}
	
	public int getLocalTotalPreSales(Map<String, Map<String, Integer>> sumpresaleMap, int stocking_cycle,
			List<Map<String, Object>> fbasale) {
		int sumpresales = 0;
		for (Map<String, Object> item : fbasale) {
			String marketplaceid = item.get("marketplaceid").toString();
			int presale = item.get("presales") == null ? 0:Integer.parseInt(item.get("presales").toString());
			Map<String, Integer> presaleMap = sumpresaleMap.get(marketplaceid);
			if (presaleMap == null) {// 没有则使用presale
				for (int i = 1; i <= stocking_cycle; i++) {
					sumpresales += presale;
				}
			} else {
				Calendar now = Calendar.getInstance();
				for (int i = 1; i <= stocking_cycle; i++) {
					now.add(Calendar.DAY_OF_MONTH, 1);
					String dateStr = GeneralUtil.formatDate(now.getTime(), "yyyy-MM-dd");
					if (presaleMap.get(dateStr) != null) {
						sumpresales += presaleMap.get(dateStr);
					} else {
						sumpresales += presale;
					}
				}
			}
		}
		return sumpresales;
	}
	
	public int getSalesday(Map<String,Integer> presaleMap, int quantity, int presale) {
		int salesday = 0;
		Calendar now = Calendar.getInstance();
		while(quantity>0&&salesday<365){
			now.add(Calendar.DAY_OF_MONTH, 1);
			String dateStr = GeneralUtil.formatDate(now.getTime(),"yyyy-MM-dd");
			if (presaleMap.get(dateStr) != null) {
				quantity = quantity - presaleMap.get(dateStr);
			} else {
				quantity = quantity - presale;
			}
			if(quantity>=0){
				salesday+=1;
			}
		}
		return salesday;
	}
	
	public int getLocalSalesday(Map<String,Map<String,Integer>> sumpresaleMap, int quantity, List<Map<String, Object>> fbasale) {
		int salesday = 0;
		Calendar now = Calendar.getInstance();
		while(quantity>0&&salesday<365){
			now.add(Calendar.DAY_OF_MONTH, 1);
			String dateStr = GeneralUtil.formatDate(now.getTime(),"yyyy-MM-dd");
			int maxpresale=0;
			for(Map<String, Object> item : fbasale){
				String marketplaceid = item.get("marketplaceid").toString();
				Map<String,Integer> presaleMap = sumpresaleMap.get(marketplaceid);
				int  presale=0;
				if(presaleMap!=null&&presaleMap.get(dateStr) != null) {
					presale=presaleMap.get(dateStr);
				}
				else if(item.get("presales") != null )  {
					presale = Integer.parseInt(item.get("presales").toString());
				}
		        quantity = quantity - presale;
				if(maxpresale<presale) {
						maxpresale=presale;
				}
				
			}
			if(quantity>0) {
				salesday+=1;
			}
			if(maxpresale==0) {
				salesday=365;
			}
		}
		return salesday;
	}





	
	public List<Date> getdatelist(Calendar startcal,Calendar endcal) {
		List<Date> datelist = new ArrayList<Date>();
		while(startcal.before(endcal)){
			if(GeneralUtil.formatDate(startcal.getTime()).equals(GeneralUtil.formatDate(endcal.getTime()))){
				break;
			}
			datelist.add(startcal.getTime());
			startcal.add(Calendar.DAY_OF_MONTH, 1);
		}
		return datelist;
	}

	public void calculationPresales(UserInfo user, Map<String, Object> item ,List<Map<String, Object>> fbasale, Map<String, Map<String, Integer>> sumpresaleMap ,Map<String, Object> param) {
		//对于欧洲站点，重新计算日均销量
		Integer salesummary = param.get("salesummary") == null ? 0 : (Integer) param.get("salesummary");
		Integer fbaplannumber = param.get("fbaplannumber") == null ? 0 : (Integer) param.get("fbaplannumber");
		if(item.get("marketplaceid")!=null && item.get("marketplaceid").toString().equals("EU")){
			int summonth = item.get("salesmonth") == null ? 0 : Integer.parseInt(item.get("salesmonth").toString());
			int sumseven = item.get("salesweek") == null ? 0 : Integer.parseInt(item.get("salesweek").toString());
			int sum15 = item.get("salesfifteen") == null ? 0 : Integer.parseInt(item.get("salesfifteen").toString());
			String openDate = item.get("openDate") == null ? null : item.get("openDate").toString();
			//DaysalesFormula formula = daysalesFormulaService.selectByShopid(user.getCompanyid());
			int qty = 0;//DaysalesFormulaServiceImpl.getAvgSales(formula, summonth, sumseven, sum15, GeneralUtil.parseDate(openDate));
			item.put("sales", qty);
		} 
		if(item.get("sales")==null){
			item.put("sales", 0);
		}
		//如果预估销量没有修改过，用系统计算的日均销量
		if(item.get("presales")==null){
			item.put("presales", item.get("sales"));
		}
		int presale = item.get("presales") == null ? 0:Integer.parseInt(item.get("presales").toString());
		salesummary += presale;
		int mincycle = item.get("mincycle") ==null ? 0 :Integer.parseInt(item.get("mincycle").toString());
		int stocking_cycle = item.get("stocking_cycle") == null ? 0:Integer.parseInt(item.get("stocking_cycle").toString());
		int quantity = 0;
		if (item.get("quantity") != null) {
			quantity = Integer.parseInt(item.get("quantity").toString());//FBA库存
		}
		int salesday = 0;
		int aftersalesday = stocking_cycle;
		int planquantity = 0;//计算FBA仓库建议发货货量=备货周期内建议发货库存-FBA库存>最小发货库存？备货周期内建议发货库存-FBA库存：最小发货库存
		Map<String,Integer> presaleMap = selectBySKUMarket(item.get("sku").toString(),
				item.get("marketplaceid").toString(),item.get("groupid").toString());
		if (presaleMap != null) {
			item.put("hasfullcalendar", true);
			if (sumpresaleMap == null) {
				sumpresaleMap = new HashMap<String, Map<String, Integer>>();
			}
			sumpresaleMap.put(item.get("marketplaceid").toString(), presaleMap);
			// 计算备货周期内总销量
			int sumpresales_stocking = getTotalPreSales(presaleMap, stocking_cycle, presale);
			int sumpresales_mincycle = getTotalPreSales(presaleMap, mincycle, presale);
			if(sumpresales_stocking - quantity>0){
				planquantity = sumpresales_stocking - quantity > sumpresales_mincycle ? sumpresales_stocking - quantity : sumpresales_mincycle;
			}
			salesday = getSalesday(presaleMap, quantity, presale);// 根据销量分摊库存，求可售天数
		} else {
			if(stocking_cycle * presale - quantity>0){
				planquantity = stocking_cycle * presale - quantity > mincycle * presale ? stocking_cycle * presale - quantity : mincycle * presale;
			}
			if(presale != 0){
				salesday = quantity / presale;
			}
		}
		item.put("planquantity", planquantity);
		fbaplannumber += planquantity;
		if (presale != 0) {
			item.put("salesday", salesday);
			if (salesday > aftersalesday) {
				item.put("aftersalesday", salesday);
			} else {
				item.put("aftersalesday", aftersalesday);
			}
		}
		item.put("ftype", "fba");
		item.put("materialid", param.get("materialid"));
		item.put("assembly_time", param.get("assembly_time"));
		item.put("delivery_cycle", param.get("delivery_cycle2"));
		param.put("salesummary", salesummary);
		param.put("fbaplannumber", fbaplannumber);
	}
	
	public int calculationSuggessPlan(Map<String, Object> map, Map<String, Object> param, int cycle, List<Map<String, Object>> fbasale,Map<String, Map<String, Integer>> sumpresaleMap) {
		Integer salesummary = param.get("salesummary") == null ? 0 : (Integer) param.get("salesummary");
		Integer fbaplannumber = param.get("fbaplannumber") == null ? 0 : (Integer) param.get("fbaplannumber");
		Integer selfsale_quantity_sum = param.get("selfsale_quantity_sum") == null ? 0 : (Integer) param.get("selfsale_quantity_sum");
		int plan = 0;
		int min_cycle = Integer.parseInt(map.get("min_cycle").toString());
		int sumplan = fbaplannumber;// 各个站点在FBA备货周期后的建议发货量之和 
		// 本地仓库缺货量 = 各个站点在FBA备货周期后的建议发货量之和 - (本地库存数量 + inbound库存数量)
		int salfplan = sumplan - selfsale_quantity_sum;
		// 本地仓库安全库存
		int safeplan = cycle * salesummary;
		// 最小补货周期 * 销量
		int min_plan = min_cycle * salesummary;
		if(sumpresaleMap != null){
			safeplan = getLocalTotalPreSales(sumpresaleMap, cycle, fbasale);
			min_plan = getLocalTotalPreSales(sumpresaleMap, min_cycle, fbasale);
		}
		if (salfplan > 0) {
			if ((salfplan + safeplan) > min_plan) {
				plan = salfplan + safeplan;
			} else {
				plan = min_plan;
			}
		} else {
			if (salfplan + safeplan > 0) {
				plan = min_plan;
			} else {
				plan = 0;
			}
		}
		//可售天数
		int salesday = 0;
		//发货后可售天数
		int aftersalesday = 0;
		if (sumpresaleMap != null) {//根据销量分摊库存，求可售天数
			salesday = getLocalSalesday(sumpresaleMap,selfsale_quantity_sum,fbasale);
			aftersalesday = getLocalSalesday(sumpresaleMap,selfsale_quantity_sum + plan,fbasale);
		} else if (salesummary != 0) {
			salesday = selfsale_quantity_sum / salesummary;
			aftersalesday = (selfsale_quantity_sum + plan) / salesummary;
		}
		if (salesummary != 0) {
			map.put("salesday", salesday);
			map.put("aftersalesday", aftersalesday);
		} else {
			map.put("salesday", "--");
			map.put("aftersalesday", "--");
		}
		return plan;
	}
}
