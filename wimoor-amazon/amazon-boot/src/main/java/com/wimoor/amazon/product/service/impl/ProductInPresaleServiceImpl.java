package com.wimoor.amazon.product.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.api.ErpClientOneFeignManager;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.common.pojo.entity.DaysalesFormula;
import com.wimoor.amazon.common.pojo.vo.Chart;
import com.wimoor.amazon.common.pojo.vo.ChartLine;
import com.wimoor.amazon.common.pojo.vo.ChartMarkpoint;
import com.wimoor.amazon.common.service.IDaysalesFormulaService;
import com.wimoor.amazon.common.service.impl.DaysalesFormulaServiceImpl;
import com.wimoor.amazon.product.mapper.ProductInOrderMapper;
import com.wimoor.amazon.product.mapper.ProductInPresaleMapper;
import com.wimoor.amazon.product.mapper.ProductInfoMapper;
import com.wimoor.amazon.product.pojo.dto.PlanDTO;
import com.wimoor.amazon.product.pojo.dto.ProductPresaleListDTO;
import com.wimoor.amazon.product.pojo.entity.ProductInPresale;
import com.wimoor.amazon.product.service.IProductInPresaleService;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.amazon.util.ExcelUtil;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.util.StrUtil;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
 

@Service("productInPresaleService")
@RequiredArgsConstructor
public class ProductInPresaleServiceImpl extends ServiceImpl<ProductInPresaleMapper,ProductInPresale> implements IProductInPresaleService {
	@Resource
	IAmazonAuthorityService amazonAuthorityService;
    final ProductInfoMapper productInfoMapper;
    final ProductInOrderMapper productInOrderMapper;
    final IMarketplaceService marketplaceService;
    final IAmazonGroupService amazonGroupService;
    @Resource
    IDaysalesFormulaService daysalesFormulaService;
    final ErpClientOneFeignManager erpClientOneFeign;
    public IPage<Map<String, Object>> listProduct(ProductPresaleListDTO dto){
    	if(StrUtil.isBlankOrUndefined(dto.getMarketplaceid())) {
    		dto.setMarketplaceid(null);
    	}
    	if(StrUtil.isBlankOrUndefined(dto.getOwner())) {
    		dto.setOwner(null);
    	}
    	if(StrUtil.isBlankOrUndefined(dto.getSku())) {
    		dto.setSku(null);
    	}else {
    		dto.setFromDate(null);
    		dto.setToDate(null);
    		dto.setSku("%"+dto.getSku().trim()+"%");
    	}
    	if(StrUtil.isBlankOrUndefined(dto.getMsku())) {
    		dto.setMsku(null);
    	}else {
    		dto.setMsku("%"+dto.getMsku().trim()+"%");
    	}
    	if(StrUtil.isBlankOrUndefined(dto.getAsin())) {
    		dto.setAsin(null);
    	}else {
    		dto.setAsin("%"+dto.getAsin().trim()+"%");
    	}
    	if(StrUtil.isBlankOrUndefined(dto.getGroupid())) {
    		dto.setGroupid(null);
    	}
    	IPage<Map<String, Object>> result = this.baseMapper.listProduct(dto.getPage(),dto);
    	List<Map<String, Object>> list = result.getRecords();
    	List<String> mskulist=new ArrayList<String>();
        for(Map<String, Object> item:list) {
        	String sku=item.get("psku").toString();
    		String marketplaceid=item.get("marketplaceid").toString();
    		String groupid=item.get("groupid").toString();
    		if(marketplaceid.equals("EU")) {
				int sumseven = item.get("sumseven") == null ? 0 : Integer.parseInt(item.get("sumseven").toString());// 七日销量
				int summonth = item.get("summonth") == null ? 0 : Integer.parseInt(item.get("summonth").toString());
				int sum15 = item.get("sum15") == null ? 0 : Integer.parseInt(item.get("sum15").toString());
				Date openDate = item.get("openDate") == null ? null : AmzDateUtils.getDate(item.get("openDate"));
				DaysalesFormula formula = daysalesFormulaService.selectByShopid(dto.getShopid());
				Integer qty = DaysalesFormulaServiceImpl.getAvgSales(formula , summonth, sumseven, sum15, openDate);
				item.put("avgsales", qty);
			}
    		mskulist.add(item.get("msku").toString());
    		Map<String, ProductInPresale> prelist = getPresale(sku,marketplaceid,groupid);
    		Calendar cend=Calendar.getInstance();
    		cend.add(Calendar.DATE, 10);
    		cend.set(Calendar.DATE, 1);
    		cend.add(Calendar.MONTH, 3);
    		
    		Calendar c=Calendar.getInstance();
    		c.add(Calendar.DATE, 10);
    		c.set(Calendar.DATE, 1);
    		int month=c.get(Calendar.MONTH);
    		int summonth=0;
    		int i=1;
    		while(c.before(cend)){
 			   ProductInPresale old = prelist.get(GeneralUtil.formatDate(c.getTime()));
 			   int sales=item.get("avgsales")== null ? 0 : Integer.parseInt(item.get("avgsales").toString());
 			   if(old!=null) {
 				  sales=old.getQuantity();
 			   }
 			  if(month!=c.get(Calendar.MONTH)) {
 				 item.put("month"+i, month);
  				 item.put("month"+i+"sales", summonth);
 				 summonth=0;
 				 i++;
 				 month=c.get(Calendar.MONTH);
 			   }
 			  summonth=summonth+sales;
 			c.add(Calendar.DATE, 1);
 		   }
    		c.add(Calendar.DATE, 1);
    		if(month!=c.get(Calendar.MONTH)) {
 				item.put("month"+i, month);
 				item.put("month"+i+"sales", summonth);
 				summonth=0;
 				i++;
 				month=c.get(Calendar.MONTH);
 			}
    		Integer threeamount = 0;
    		Integer firstamount = 0;
    		if(item.get("month3sales")!=null) {
    			threeamount=Integer.parseInt(item.get("month3sales").toString());
    		}
    		if(item.get("month1sales")!=null) {
    			firstamount=Integer.parseInt(item.get("month1sales").toString());
    			if(firstamount>0) {
    				double multp = Math.pow(threeamount*1.0/firstamount, 1d/3)-1;
    				if(multp>0.000001) {
    					 item.put("multplus",Math.ceil(multp*100));
    				}else {
    	    		    item.put("multplus","0");
    				}
    			}
    			
    		}
        }
        
        try {
        	PlanDTO plandto=new PlanDTO();
        	plandto.setShopid(dto.getShopid());
        	plandto.setMskulist(mskulist);
        	plandto.setOwner(dto.getOwner());
        	List<Map<String,Object>> listMap=new LinkedList<Map<String,Object>>();
        	if(plandto.getMskulist()==null||plandto.getMskulist().size()==0) {
        		return result;
        	}
        	Result<Map<String, Object>> resultMaterial = erpClientOneFeign.getMaterialInfoBySkuList(plandto);
        	if(Result.isSuccess(resultMaterial)&&resultMaterial.getData()!=null) {
        		Map<String, Object> mskuInfoMap=resultMaterial.getData();
        	     for(Map<String, Object> item:list) {
        	    	 String msku=item.get("msku").toString();
        	    	 String psku=item.get("psku").toString();
        	    	 if(mskuInfoMap.get(msku)!=null) {
        	    		 @SuppressWarnings("unchecked")
						 Map<String, Object> mskuinfo =  (Map<String, Object>)mskuInfoMap.get(msku);
        	    		 item.putAll(mskuinfo);
        	    		 item.put("msku", msku);
        	    		 item.put("psku", psku);
        	    		 listMap.add(item);
        	    	 }
        	     }
        	     return result;
        	}
        }catch(FeignException e) {
        	e.printStackTrace();
        	throw new BizException(BizException.getMessage(e, "本地SKU信息获取失败,请联系管理员"));
        }
    	return null;
    }
    
    public void convertEstimatToPresale() {
    	List<Map<String, Object>> list = this.baseMapper.selectEstimated();
    	for(Map<String, Object> item:list) {
    		String sku=item.get("sku").toString();
    		String marketplaceid=item.get("marketplaceid").toString();
    		String groupid=item.get("groupid").toString();
    		String operator=item.get("operator").toString();
    		Integer presales=new Integer(item.get("presales").toString());
    		Date endTime=AmzDateUtils.getDate(item.get("endTime"));
    		Calendar endCalendar=Calendar.getInstance();
    		endCalendar.setTime(endTime);
    		LambdaQueryWrapper<ProductInPresale> query=new LambdaQueryWrapper<ProductInPresale>();
    		query.eq(ProductInPresale::getSku, sku);
    		query.eq(ProductInPresale::getMarketplaceid, marketplaceid);
    		query.eq(ProductInPresale::getGroupid,groupid);
    		for(Calendar c =Calendar.getInstance();c.before(endCalendar);c.add(Calendar.DATE, 1)) {
    			query.eq(ProductInPresale::getDate,GeneralUtil.formatDate(c.getTime()));
    			ProductInPresale oldone = this.baseMapper.selectOne(query);
    			if(oldone==null) {
    				ProductInPresale newone=new ProductInPresale();
    				newone.setDate(c.getTime());
    				newone.setSku(sku);
    				newone.setGroupid(groupid);
    				newone.setMarketplaceid(marketplaceid);
    				newone.setOperator(operator);
    				newone.setOpttime(new Date());
    				newone.setQuantity(presales);
    				this.baseMapper.insert(newone);
    			}
    		}
    	}
    }
	public Map<String,ProductInPresale> getPresale(String sku,String marketplaceid,AmazonAuthority auth) {
		LambdaQueryWrapper<ProductInPresale> query=new LambdaQueryWrapper<ProductInPresale>();
		query.eq(ProductInPresale::getSku, sku);
		query.eq(ProductInPresale::getMarketplaceid, marketplaceid);
		query.eq(ProductInPresale::getGroupid, auth.getGroupid());
		List<ProductInPresale> list = this.list(query);
		HashMap<String,ProductInPresale> result=new HashMap<String,ProductInPresale>();
		for(ProductInPresale item:list) {
			result.put(GeneralUtil.formatDate(item.getDate()), item);
		}
		return result;
	}
	public Map<String,ProductInPresale> getPresale(String sku,String marketplaceid,String groupid) {
		LambdaQueryWrapper<ProductInPresale> query=new LambdaQueryWrapper<ProductInPresale>();
		query.eq(ProductInPresale::getSku, sku);
		query.eq(ProductInPresale::getMarketplaceid, marketplaceid);
		query.eq(ProductInPresale::getGroupid,groupid);
		List<ProductInPresale> list = this.list(query);
		HashMap<String,ProductInPresale> result=new HashMap<String,ProductInPresale>();
		for(ProductInPresale item:list) {
			result.put(GeneralUtil.formatDate(item.getDate()), item);
		}
		return result;
	}
	
	public Chart getSales(String shopid,String marketplaceid,String amazonauthid,String sku) {
		// TODO Auto-generated method stub
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("marketplaceid", marketplaceid);
		param.put("amazonauthid",amazonauthid);
	    param.put("sku",sku);
		List<Map<String, Object>> list =null;
		if(marketplaceid.equals("EU")) {
			  list = productInOrderMapper.getProductEUSales(param);
		}else {
		      list = productInOrderMapper.getProductCountrySales(param);
		}
	   int avgsales=0;
		if(list!=null&&list.size()>0) {
			Map<String, Object> salesitem = list.get(0);
			String avgsalesstr=salesitem.get("avgsales")!=null?salesitem.get("avgsales").toString():"0";
			if(marketplaceid.equals("EU")) {
				int sumseven = salesitem.get("sumseven") == null ? 0 : Integer.parseInt(salesitem.get("sumseven").toString());// 七日销量
				int summonth = salesitem.get("summonth") == null ? 0 : Integer.parseInt(salesitem.get("summonth").toString());
				int sum15 = salesitem.get("sum15") == null ? 0 : Integer.parseInt(salesitem.get("sum15").toString());
				Date openDate = salesitem.get("openDate") == null ? null : AmzDateUtils.getDate(salesitem.get("openDate"));
				DaysalesFormula formula = daysalesFormulaService.selectByShopid(shopid.toString());
				Integer qty = DaysalesFormulaServiceImpl.getAvgSales(formula , summonth, sumseven, sum15, openDate);
				avgsalesstr=qty.toString();
			}
			if(StrUtil.isNotBlank(avgsalesstr)) {
				avgsales=Integer.valueOf(avgsalesstr);
			}
		}
		AmazonAuthority auth = amazonAuthorityService.getById(amazonauthid);
	    Map<String, ProductInPresale> prelist =  getPresale(sku,marketplaceid,auth);
		Calendar c=Calendar.getInstance();
		SimpleDateFormat fmt = new SimpleDateFormat("MM-dd");
		Chart chart=new Chart();
		chart.setLegends(Arrays.asList("预估销量"));
		 List<ChartLine> lines=new LinkedList<ChartLine>();
		List<String> labels=new LinkedList<String>();
		List<Object> datalist=new LinkedList<Object>();
		 List<ChartMarkpoint> marketpoint = new LinkedList<ChartMarkpoint>();
		 for(int i=1;i<=180 ;i++) {
			   ProductInPresale old = prelist.get(GeneralUtil.formatDate(c.getTime()));
			int sales;
			String key=fmt.format(c.getTime());
			if(old==null||old.getQuantity()==null) {
				sales=avgsales;
			} else {
				sales=old.getQuantity();
				ChartMarkpoint point=new ChartMarkpoint();
				point.setValue(key);
				point.setName(key);
				point.setValue(sales+"<br>修改日期："+GeneralUtil.formatDate(old.getOpttime()));
				point.setXAxis(key);
				point.setYAxis(sales);
				marketpoint.add(point);
			}
			datalist.add(sales);
			labels.add(key);
			c.add(Calendar.DATE, 1);
		 }
		 chart.setLabels(labels);
		 ChartLine e=new ChartLine();
		 e.setData(datalist);
		
		e.setMarkpoint(marketpoint);
		lines.add(e);
		chart.setLines(lines);
		return chart;
	}
	
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
	public List<ProductInPresale> selectByGroup(String sku,String marketplaceid,String groupid ){
		return this.baseMapper.selectByGroup(sku, marketplaceid, groupid);
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

	@Override
	public void refreshData(AmazonAuthority auth) {
		// TODO Auto-generated method stub
		this.baseMapper.refreshData(auth);
	}

	@Override
	public String uploadPreSalesByExcel(Workbook workbook, UserInfo user) {
		// TODO Auto-generated method stub
			Sheet sheet = workbook.getSheetAt(0);
			HashMap<String,Marketplace> marketmap=new HashMap<String,Marketplace>();
			for(Marketplace item:marketplaceService.findAllMarketplace()) {
				marketmap.put(item.getName(), item);
			}
			List<AmazonGroup> groupList = amazonGroupService.selectByShopId(user.getCompanyid());
			HashMap<String,AmazonGroup> groupmap=new HashMap<String,AmazonGroup>();
			for(AmazonGroup item:groupList) {
				groupmap.put(item.getName(), item);
			}
			Calendar beginCalendar =Calendar.getInstance();
			Calendar endCalendar =Calendar.getInstance();
			int blank = 0;
			for(int i=1;i<=sheet.getLastRowNum();i++) {
				Row row = sheet.getRow(i);
				Cell skucell = row.getCell(0);
				if(skucell==null||StrUtil.isEmpty(skucell.getStringCellValue())) {
				    blank++;
				    if(blank>=100) {
				    	break;
				    }
				    continue;
				 }
				Cell groupnamecell = row.getCell(1);
				String sku = skucell.getStringCellValue();
				String groupname=groupnamecell.getStringCellValue();
				Cell marketnamecell = row.getCell(2);
				String marketplacename=marketnamecell.getStringCellValue();
				Cell yearcell = row.getCell(3);
				
				String groupid = "";
				String marketplaceid="";
				String userid = user.getId();
				AmazonGroup group=groupmap.get(groupname);
				if(group==null) {throw new BizException("无法正常获取店铺，请确认店铺是否存在或已经改名");}
				groupid=group.getId();
			    if(StrUtil.isEmpty(marketplacename)) {
			    	throw new BizException("站点名称不能为空");
			    }
			    Marketplace market = marketmap.get(marketplacename);
			    if(marketplacename.equals("欧洲")) {
			    	marketplaceid="EU";
			    }else {
				    if(market==null) {
				    	throw new BizException("无法找到您录入的站点");
				    }
			    	marketplaceid=market.getMarketplaceid();
			    }
			    Double yearnum = null;
			    yearnum=ExcelUtil.getNumeric(yearcell) ;
			    if(yearnum==null) {
			    	continue;
			    }
			
			    int year=yearnum.intValue();
				Double quantitystr = 0.0;
			    for(Integer month=1;month<=12;month++) {
					Integer daynum = GeneralUtil.getDayNumOfMonth(year, month);
					Cell  monthcell = row.getCell(3+month);
					try {
						  if(monthcell==null) {
							  continue;
						  }
						  quantitystr=ExcelUtil.getNumeric(monthcell) ;
						 if(quantitystr==null||quantitystr.intValue()==0) {
							 continue;
						 }
					}catch(NullPointerException e) {
						continue;
					}
					int remainder = quantitystr.intValue() % daynum;
					int quantity= quantitystr.intValue()/daynum;
					Date begin  =GeneralUtil.parseDate(year+"-"+month+"-"+1);
					beginCalendar.setTime(begin);
					
					Date end =GeneralUtil.parseDate(year+"-"+month+"-"+daynum);
					endCalendar.setTime(end);
					endCalendar.add(Calendar.DATE, 1);
					if(quantitystr.intValue()==-1) {
						continue;
					}
					if(remainder>0) {
						end =GeneralUtil.parseDate(year+"-"+month+"-"+remainder);
						endCalendar.setTime(end);
						endCalendar.add(Calendar.DATE, 1);
					}

					Date newbegin =null;
					for(int day=1;day<=daynum;day++) {
						ProductInPresale record = new ProductInPresale();
						record.setSku(sku);
						record.setMarketplaceid(marketplaceid);
						record.setGroupid(groupid);
						if(day<=remainder) {
							record.setQuantity(quantity+1);
						}else {
							if(	newbegin==null) {
								newbegin = beginCalendar.getTime();
								begin=newbegin;
								end =GeneralUtil.parseDate(year+"-"+month+"-"+daynum);
								endCalendar.setTime(end);
								endCalendar.add(Calendar.DATE, 1);
							}
							record.setQuantity(quantity);
						}
						record.setOperator(userid);
						record.setDate(beginCalendar.getTime());
						record.setOpttime(new Date());
						LambdaQueryWrapper<ProductInPresale> query=new LambdaQueryWrapper<ProductInPresale>();
			    		query.eq(ProductInPresale::getSku,sku);
			    		query.eq(ProductInPresale::getMarketplaceid,marketplaceid);
			    		query.eq(ProductInPresale::getDate, GeneralUtil.formatDate(beginCalendar.getTime()));
			    		query.eq(ProductInPresale::getGroupid, groupid);
						this.remove(query);
						this.baseMapper.insert(record);
						beginCalendar.add(Calendar.DATE, 1);
					}
			    }
			}
		
		    sheet = workbook.getSheetAt(1);
		    blank = 0;
			for(int i=1;i<=sheet.getLastRowNum();i++) {
				Row row = sheet.getRow(i);
				if(row==null) {
					continue;
				}
				Cell skucell = row.getCell(0);
				if(skucell==null||StrUtil.isEmpty(skucell.getStringCellValue())) {
				    blank++;
				    if(blank>=100) {
				    	break;
				    }
				    continue;
				 }
				Cell groupnamecell = row.getCell(1);
				String sku = skucell.getStringCellValue();
				String groupname=groupnamecell.getStringCellValue();
				Cell marketnamecell = row.getCell(2);
				String marketplacename=marketnamecell.getStringCellValue();
				Cell yearcell = row.getCell(3);
				Cell monthcell = row.getCell(4);
				String groupid = "";
				String marketplaceid="";
				String userid = user.getId();
				AmazonGroup group=groupmap.get(groupname);
				if(group==null) {throw new BizException("无法正常获取店铺，请确认店铺是否存在或已经改名");}
				groupid=group.getId();
			    if(StrUtil.isEmpty(marketplacename)) {
			    	throw new BizException("站点名称不能为空");
			    }
			    Marketplace market = marketmap.get(marketplacename);
			    if(marketplacename.equals("欧洲")) {
			    	marketplaceid="EU";
			    }else {
				    if(market==null) {
				    	throw new BizException("无法找到您录入的站点");
				    }
			    	marketplaceid=market.getMarketplaceid();
			    }
			    Double yearstr =ExcelUtil.getNumeric(yearcell) ;
			    Double monthstr=ExcelUtil.getNumeric(monthcell) ; 
			    if(yearstr==null||monthstr==null) {
			    	continue;
			    }
			    int year=yearstr.intValue();
			    int month=monthstr.intValue();
			    Integer daynum = GeneralUtil.getDayNumOfMonth(year, month);
				for(int day=1;day<=daynum;day++) {
					Cell  daycell = row.getCell(4+day);
					Double quantitystr=0.0;
					try {
						  quantitystr=ExcelUtil.getNumeric(daycell)  ;
						  if(quantitystr==null) {
							  continue;
						  }
					}catch(NullPointerException e) {
						continue;
					}
				 
					int quantity= quantitystr.intValue();
					beginCalendar.set(year, month-1, day);
					beginCalendar.set(Calendar.MILLISECOND, 0);
					month=monthstr.intValue();
					LambdaQueryWrapper<ProductInPresale> query=new LambdaQueryWrapper<ProductInPresale>();
		    		query.eq(ProductInPresale::getSku,sku);
		    		query.eq(ProductInPresale::getMarketplaceid,marketplaceid);
		    		query.eq(ProductInPresale::getDate, GeneralUtil.formatDate(beginCalendar.getTime()));
		    		query.eq(ProductInPresale::getGroupid, groupid);
					this.remove(query);
					if(quantitystr.intValue()==-1) {
						continue;
					}
						ProductInPresale record = new ProductInPresale();
						record.setSku(sku);
						record.setMarketplaceid(marketplaceid);
						record.setGroupid(groupid);
				        record.setQuantity(quantity);
						record.setOperator(userid);
						record.setDate(beginCalendar.getTime());
						record.setOpttime(new Date());
						this.baseMapper.insert(record);
					}
					
			    }
			return "上传成功";
	}

	@Override
	public void downloadPreSalesByExcel(Sheet sheet, Sheet sheet2, String groupid, UserInfo user) {
		// TODO Auto-generated method stub
		AmazonGroup group = amazonGroupService.getById(groupid);
		Map<String, Marketplace> marketmap = marketplaceService.findMapByMarketplaceId();
		LambdaQueryWrapper<ProductInPresale> query=new LambdaQueryWrapper<ProductInPresale>();
		query.gt(ProductInPresale::getDate, GeneralUtil.formatDate(Calendar.getInstance().getTime()));
		query.eq(ProductInPresale::getGroupid, groupid);
		List<ProductInPresale> list = this.baseMapper.selectList(query);
		 SimpleDateFormat yearfmt = new SimpleDateFormat("yyyy");
		 SimpleDateFormat monthfmt = new SimpleDateFormat("MM");
		Map<String,Map<String,Map<String,Map<String,ProductInPresale>>>> map=new HashMap<String,Map<String,Map<String,Map<String,ProductInPresale>>>>();
		for(ProductInPresale item:list) {
			String key=item.getSku()+item.getMarketplaceid();
			Map<String,Map<String,   Map<String, ProductInPresale>>> yearMap = map.get(key);
			if(yearMap==null) {
				yearMap=new HashMap<String,Map<String,  Map<String, ProductInPresale>>>();
			}
		    String year=yearfmt.format(item.getDate());
		    Map<String,Map<String,  ProductInPresale>> monthMap = yearMap.get(year);
		    if(monthMap==null) {
		    	monthMap=new HashMap<String,Map<String, ProductInPresale>>();
		    }
		    String month=monthfmt.format(item.getDate());
		    Map<String, ProductInPresale> dayMap = monthMap.get(month);
		    if(dayMap==null) {
		    	dayMap=new HashMap<String, ProductInPresale>();
		    }
		    
		    String daykey=GeneralUtil.formatDate(item.getDate());
		    ProductInPresale pre = dayMap.get(daykey);
		    if(pre==null) {
		    	dayMap.put(daykey, item);
		    }
		    monthMap.put(month, dayMap);
		    yearMap.put(year, monthMap);
		    map.put(key, yearMap);
		}
	
		 int sheet1=1;
		 int sheet0=1;
       
	for(  Entry<String, Map<String, Map<String, Map<String, ProductInPresale>>>> skuMap:map.entrySet()) {
    	for(Entry<String, Map<String, Map<String, ProductInPresale>>> yearMap:skuMap.getValue().entrySet()) {
    		Map<String,Integer> qty=new HashMap<String,Integer>();
    	    ProductInPresale one =null;
    		   for(Entry<String, Map<String, ProductInPresale>> monthMap:yearMap.getValue().entrySet()) {
    		    		  Map<String, ProductInPresale> dayMap = monthMap.getValue();
    						// 在索引0的位置创建行（最顶端的行）
    		    		    Entry<String, ProductInPresale> item = dayMap.entrySet().iterator().next();
    		    		    one = item.getValue();
    						Row row2 =sheet2.createRow(sheet1);
    						sheet1++;
    						Cell cell0 = row2.createCell(0);
    						cell0.setCellValue(one.getSku());
    						Cell cell1 = row2.createCell(1);
    						cell1.setCellValue(group.getName());
    						Cell cell2 = row2.createCell(2);
    						if(one.getMarketplaceid().equals("EU")) {
    							cell2.setCellValue("欧洲");
    						}else {
    							cell2.setCellValue(marketmap.get(one.getMarketplaceid()).getName());
    						}
    						Cell cell3 = row2.createCell(3);
    						String year=yearfmt.format(one.getDate());
    						cell3.setCellValue(year);
    						Cell cell4 = row2.createCell(4);
    						String month=monthfmt.format(one.getDate());
    						cell4.setCellValue(month);
    						int sum=0;
    						for(Integer i=1;i<=31;i++) {
    						    Cell cell = row2.createCell(i+4);
    						    String day=year+"-"+month+"-";
    						    if(i<10) {
    						    	day=day+"0"+i.toString();
    						    }else {
    						    	day=day+i.toString();
    						    }
    						    ProductInPresale pre = dayMap.get(day);
    						    if(pre!=null&&pre.getQuantity()!=null) {
    						    	 cell.setCellValue(pre.getQuantity());
    						    	 sum=sum+pre.getQuantity();
    						    }else {
    						    	cell.setCellValue("");
    						    }
    						   
    						} 
    						qty.put(month, sum);
    		    	  }
    		Row row = sheet.createRow(sheet0);
    		sheet0++;
    		Cell cell0 = row.createCell(0);
    		cell0.setCellValue(one.getSku());
    		Cell cell1 = row.createCell(1);
    		cell1.setCellValue(group.getName());
    		Cell cell2 = row.createCell(2);
    		if(one.getMarketplaceid().equals("EU")) {
				cell2.setCellValue("欧洲");
			}else {
				cell2.setCellValue(marketmap.get(one.getMarketplaceid()).getName());
			}
    		Cell cell3 = row.createCell(3);
    		String year=yearfmt.format(one.getDate());
			cell3.setCellValue(year);
    		Cell cell = null;
    		for(int i=1;i<=12;i++) {
    		    cell = row.createCell(i+3);
    		    String month=null;
    		    if(i<10) {
    		    	month="0"+i;
    		    }else {
    		    	month=i+"";
    		    }
    		    Integer qtyint = qty.get(month);
    		    if(qtyint!=null) {
    		    	 cell.setCellValue( qtyint);
    		    }else {
    		    	 cell.setCellValue("");
    		    }
    		   
    		} 
    		
    	}
	}		
     
	}

	@Override
	public void replaceBatch(List<ProductInPresale> preList) {
		// TODO Auto-generated method stub
		this.baseMapper.replaceBatch(preList);
	}
	
	//加缓存PreSalesByMonth
		public List<Map<String,Object>> getProductPreSalesByMonth(String sku, String marketplaceid, String groupid) {
			List<ProductInPresale> presalelist = this.baseMapper.selectMonthDateEvent(sku, marketplaceid, groupid);
			Map<String,ProductInPresale> item=new HashMap<String,ProductInPresale>();
			List<Map<String,Object>> result=new LinkedList<Map<String,Object>>();
		    for(ProductInPresale pre:presalelist) {
		    	item.put(GeneralUtil.formatDate(pre.getDate()), pre);
		    }
		    Calendar c=Calendar.getInstance();
		    c.set(Calendar.DATE, 1);
			Calendar cold=Calendar.getInstance();
			cold.setTime(c.getTime());
			cold.add(Calendar.YEAR, -1);
			cold.set(Calendar.DATE, 1);
			Date begin = cold.getTime();
			cold.add(Calendar.MONTH,12);
			Date end=cold.getTime();
			Map<String, Object> qtymap =new HashMap<String,Object>();
			Map<String,Object> holiday=new HashMap<String,Object>();
			List<Map<String, Object>> monthsale = this.baseMapper.selectMonthDateSales(sku, marketplaceid, groupid,GeneralUtil.formatDate(begin),GeneralUtil.formatDate(end));
			for(Map<String, Object> mitem:monthsale) {
				qtymap.put(mitem.get("month").toString(), mitem.get("quantity"));
			}
			List<Map<String, Object>> holidaylist = this.baseMapper.selectHoliday(marketplaceid);
			for(Map<String, Object> mitem:holidaylist) {
				holiday.put(mitem.get("month").toString(), mitem.get("holiday"));
			}
		    SimpleDateFormat FMT_YMD = new SimpleDateFormat("yy-MM");
		    for(int i=1;i<=12;i++) {
				HashMap<String, Object> point = new HashMap<String,Object>();
		    	ProductInPresale pre = item.get(GeneralUtil.formatDate(c.getTime()));
	    		point.put("date",FMT_YMD.format(c.getTime()));
		    	if(pre!=null) {
		    		point.put("value", pre.getQuantity());
		    	} 
		    	cold.setTime(c.getTime());
		    	cold.add(Calendar.MONTH,-12);
	    		if(qtymap!=null&&qtymap.get((cold.get(Calendar.MONTH)+1)+"")!=null) {
	    			point.put("oldvalue", qtymap.get((cold.get(Calendar.MONTH)+1)+""));
	    		}else {
	    			point.put("oldvalue",0);
	    		}
	    
	    		if(holiday!=null) {
	    			if(holiday.get(c.get(Calendar.MONTH)+"")!=null) {
	    				point.put("holiday",holiday.get((c.get(Calendar.MONTH)+1)+""));
	    			}
	    		}
	    		result.add(point);
		    	c.add(Calendar.MONTH, 1);
		    }
			return result;
		}

		@Override
		public List<Map<String, Object>> getProductPreSales(String sku, String marketplaceid, String groupid,
				String month) {
			// TODO Auto-generated method stub
			List<Map<String,Object>> result=new LinkedList<Map<String,Object>>();
		    Calendar c=Calendar.getInstance();
		    String[] amonth = month.split("-");
		    int year = c.get(Calendar.YEAR);
		    year=year/100;
		    c.set(Calendar.YEAR, Integer.parseInt(year+amonth[0]));
		    c.set(Calendar.MONTH,Integer.parseInt(amonth[1])-1);
		    c.set(Calendar.DATE, 1);
		    int monthday = c.getMaximum(Calendar.DAY_OF_MONTH);
		    SimpleDateFormat FMT_YMD = new SimpleDateFormat("MM.dd");
		    
			Calendar cold=Calendar.getInstance();
			cold.setTime(c.getTime());
			cold.add(Calendar.YEAR, -1);
			Date beginDate=cold.getTime();
			cold.add(Calendar.MONTH, 1);
			Date endDate=cold.getTime();
			Map<String,Object> qtymap=new HashMap<String,Object>();
			List<Map<String, Object>> list  = this.baseMapper.selectDateSales(sku, marketplaceid, groupid,GeneralUtil.formatDate(beginDate),GeneralUtil.formatDate(endDate));
			
		 
			cold.setTime(c.getTime());
			cold.add(Calendar.MONTH, 1);
			
			List<ProductInPresale> prelist = this.baseMapper.selectAllDayPresale(sku, marketplaceid, groupid,GeneralUtil.formatDate(c.getTime()),GeneralUtil.formatDate(cold.getTime()));
			
			for(Map<String, Object> mitem:list) {
				qtymap.put(mitem.get("day").toString(), mitem.get("quantity"));
			}
			Map<String,ProductInPresale> premap=new HashMap<String,ProductInPresale>();
			for(ProductInPresale mitem:prelist) {
				premap.put(GeneralUtil.formatDate(mitem.getDate()),mitem);
			}
			
		    for(int i=1;i<monthday;i++) {
				HashMap<String, Object> point = new HashMap<String,Object>();
			    ProductInPresale pre = premap.get(GeneralUtil.formatDate(c.getTime()));
	    		point.put("date",FMT_YMD.format(c.getTime()));
		    	if(pre!=null) {
		    		point.put("value", pre.getQuantity());
		    	} 
	    		if(qtymap!=null&&qtymap.get((c.get(Calendar.DAY_OF_MONTH)+1)+"")!=null) {
	    			point.put("oldvalue", qtymap.get((c.get(Calendar.DAY_OF_MONTH)+1)+""));
	    		}else {
	    			point.put("oldvalue",0);
	    		}
	    		result.add(point);
		    	c.add(Calendar.DATE, 1);
		    }
		    return result;
		}

	
}
