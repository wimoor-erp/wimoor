package com.wimoor.erp.common.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.erp.common.mapper.ProductInPresaleMapper;
import com.wimoor.erp.common.pojo.entity.ProductInPresale;
import com.wimoor.erp.common.service.IProductInPresaleService;

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


}
