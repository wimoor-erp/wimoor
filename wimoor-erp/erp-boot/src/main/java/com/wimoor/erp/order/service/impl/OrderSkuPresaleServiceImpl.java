package com.wimoor.erp.order.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.googlecode.aviator.Expression;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.pojo.entity.ChartLine;
import com.wimoor.erp.common.pojo.entity.ChartPoint;
import com.wimoor.erp.order.mapper.OrderSkuPresaleMapper;
import com.wimoor.erp.order.pojo.dto.PresaleListDTO;
import com.wimoor.erp.order.pojo.entity.OrderSkuPresale;
import com.wimoor.erp.order.service.IOrderSkuPresaleService;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.service.IWarehouseService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import com.googlecode.aviator.AviatorEvaluator;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;


@Service("OrderSkuPresaleService")
@RequiredArgsConstructor
public class OrderSkuPresaleServiceImpl extends ServiceImpl<OrderSkuPresaleMapper, OrderSkuPresale> implements IOrderSkuPresaleService {
	final IWarehouseService iWarehouseService;
   public static String getDaySalesFormula(){
	   return "(summonth*0.1)/30 + (sumhalfmonth*0.2)/15 + (sumseven*0.7)/7";
   }
    public IPage<Map<String, Object>> listProduct(PresaleListDTO dto){
    	if(StrUtil.isBlankOrUndefined(dto.getWarehouseid())) {
    		dto.setWarehouseid(null);
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
    	IPage<Map<String, Object>> result = this.baseMapper.listProduct(dto.getPage(),dto);
    	List<Map<String, Object>> list = result.getRecords();
    	List<String> mskulist=new ArrayList<String>();
        for(Map<String, Object> item:list) {
        	String sku=item.get("psku").toString();
    		String warehouseid=item.get("warehouseid").toString();
				int sumseven = item.get("sumseven") == null ? 0 : Integer.parseInt(item.get("sumseven").toString());// 七日销量
				int summonth = item.get("summonth") == null ? 0 : Integer.parseInt(item.get("summonth").toString());
				int sum15 = item.get("sum15") == null ? 0 : Integer.parseInt(item.get("sum15").toString());
				Date openDate = item.get("openDate") == null ? null : GeneralUtil.getDate(item.get("openDate"));
				Integer qty = getAvgSales( getDaySalesFormula(), summonth, sumseven, sum15, openDate);
				item.put("avgsales", qty);
    		mskulist.add(item.get("msku").toString());
    		Map<String, OrderSkuPresale> prelist = getPresale(sku,warehouseid);
    		Calendar cend=Calendar.getInstance();
    		cend.add(Calendar.DATE, 10);
    		cend.set(Calendar.DATE, 1);
    		cend.add(Calendar.MONTH, 3);
    		
    		Calendar c=Calendar.getInstance();
    		c.add(Calendar.DATE, 10);
    		c.set(Calendar.DATE, 1);
    		int month=c.get(Calendar.MONTH);
    		  summonth=0;
    		int i=1;
    		 SimpleDateFormat FMT_YMD = new SimpleDateFormat("yyyy-MM");
    		while(c.before(cend)){
				OrderSkuPresale old = prelist.get(GeneralUtil.formatDate(c.getTime()));
 			   int sales=item.get("avgsales")== null ? 0 : Integer.parseInt(item.get("avgsales").toString());
 			   if(old!=null) {
 				  sales=old.getQuantity();
 			   }
 			  if(month!=c.get(Calendar.MONTH)) {
 				 Calendar beforemonth=Calendar.getInstance();
 				 beforemonth.setTime(c.getTime());
 				 beforemonth.add(Calendar.DATE, -1);
 				 item.put("month"+i, FMT_YMD.format(beforemonth.getTime()));
  				 item.put("month"+i+"sales", summonth);
 				 summonth=0;
 				 i++;
 				 month=c.get(Calendar.MONTH);
 			   }
 			  summonth=summonth+sales;
 			c.add(Calendar.DATE, 1);
 		   }
    		if(month!=c.get(Calendar.MONTH)) {
    			Calendar beforemonth=Calendar.getInstance();
 				beforemonth.setTime(c.getTime());
 				beforemonth.add(Calendar.DATE, -1);
 				item.put("month"+i, FMT_YMD.format(beforemonth.getTime()));
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
       	return result;

    }
    
    public void convertEstimatToPresale() {
    	List<Map<String, Object>> list = this.baseMapper.selectEstimated();
    	for(Map<String, Object> item:list) {
    		String sku=item.get("sku").toString();
    		String warehouseid=item.get("warehouseid").toString();
    		String operator=item.get("operator").toString();
    		Integer presales=  Integer.parseInt(item.get("presales").toString());
    		Date endTime=GeneralUtil.getDate(item.get("endTime"));
    		Calendar endCalendar=Calendar.getInstance();
    		endCalendar.setTime(endTime);
    		LambdaQueryWrapper<OrderSkuPresale> query=new LambdaQueryWrapper<OrderSkuPresale>();
    		query.eq(OrderSkuPresale::getSku, sku);
    		query.eq(OrderSkuPresale::getWarehouseid, warehouseid);
    		for(Calendar c =Calendar.getInstance();c.before(endCalendar);c.add(Calendar.DATE, 1)) {
    			query.eq(OrderSkuPresale::getDate,GeneralUtil.formatDate(c.getTime()));
    			OrderSkuPresale oldone = this.baseMapper.selectOne(query);
    			if(oldone==null) {
    				OrderSkuPresale newone=new OrderSkuPresale();
    				newone.setDate(c.getTime());
    				newone.setSku(sku);
    				newone.setWarehouseid(warehouseid);
    				newone.setOperator(operator);
    				newone.setOpttime(new Date());
    				newone.setQuantity(presales);
    				this.baseMapper.insert(newone);
    			}
    		}
    	}
    }
	public Map<String,OrderSkuPresale> getPresale(String sku,String warehouseid) {
		LambdaQueryWrapper<OrderSkuPresale> query=new LambdaQueryWrapper<OrderSkuPresale>();
		query.eq(OrderSkuPresale::getSku, sku);
		query.eq(OrderSkuPresale::getWarehouseid, warehouseid);
		List<OrderSkuPresale> list = this.list(query);
		HashMap<String,OrderSkuPresale> result=new HashMap<String,OrderSkuPresale>();
		for(OrderSkuPresale item:list) {
			result.put(GeneralUtil.formatDate(item.getDate()), item);
		}
		return result;
	}

	
	public com.wimoor.erp.common.pojo.entity.Chart getSales(String shopid,String warehouseid,String sku) {
		// TODO Auto-generated method stub
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("warehouseid", warehouseid);
	    param.put("sku",sku);
		List<Map<String, Object>> list =null;
	   int avgsales=0;
		if(list!=null&&list.size()>0) {
			Map<String, Object> salesitem = list.get(0);
			    String avgsalesstr=salesitem.get("avgsales")!=null?salesitem.get("avgsales").toString():"0";
				int sumseven = salesitem.get("sumseven") == null ? 0 : Integer.parseInt(salesitem.get("sumseven").toString());// 七日销量
				int summonth = salesitem.get("summonth") == null ? 0 : Integer.parseInt(salesitem.get("summonth").toString());
				int sum15 = salesitem.get("sum15") == null ? 0 : Integer.parseInt(salesitem.get("sum15").toString());
				Date openDate = salesitem.get("openDate") == null ? null : GeneralUtil.getDate(salesitem.get("openDate"));
				Integer qty = getAvgSales(getDaySalesFormula() , summonth, sumseven, sum15, openDate);
				avgsalesstr=qty.toString();
			if(StrUtil.isNotBlank(avgsalesstr)) {
				avgsales=Integer.valueOf(avgsalesstr);
			}
		}

	    Map<String, OrderSkuPresale> prelist =  getPresale(sku,warehouseid);
		Calendar c=Calendar.getInstance();
		SimpleDateFormat fmt = new SimpleDateFormat("MM-dd");
		com.wimoor.erp.common.pojo.entity.Chart chart=new com.wimoor.erp.common.pojo.entity.Chart();
		chart.setLegends(Arrays.asList("预估销量"));
		 List<ChartLine> lines=new LinkedList<ChartLine>();
		List<String> labels=new LinkedList<String>();
		List<Object> datalist=new LinkedList<Object>();
		 List<ChartPoint> marketpoint = new LinkedList<ChartPoint>();
		 for(int i=1;i<=180 ;i++) {
			   OrderSkuPresale old = prelist.get(GeneralUtil.formatDate(c.getTime()));
			int sales;
			String key=fmt.format(c.getTime());
			if(old==null||old.getQuantity()==null) {
				sales=avgsales;
			} else {
				sales=old.getQuantity();
				ChartPoint point=new ChartPoint();
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
	
	public Map<String, Integer> selectBySKUMarket(String sku, String warehouseid ) {
		Map<String, Integer> presaleMap = null; 
		List<OrderSkuPresale> presalelist = this.baseMapper.selectEvent(sku, warehouseid);
		if (presalelist != null && presalelist.size() > 0) {
			presaleMap = new HashMap<String, Integer>();
			for (OrderSkuPresale OrderSkuPresale : presalelist) {
				String key = GeneralUtil.formatDate(OrderSkuPresale.getDate(), "yyyy-MM-dd");
				presaleMap.put(key, OrderSkuPresale.getQuantity());
			}
		}
		return presaleMap;
	}
	public List<OrderSkuPresale> selectByGroup(String sku,String warehouseid ){
		return this.baseMapper.selectByGroup(sku, warehouseid);
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

	@SuppressWarnings("unused")
	public void calculationPresales(UserInfo user, Map<String, Object> item ,List<Map<String, Object>> fbasale, Map<String, Map<String, Integer>> sumpresaleMap ,Map<String, Object> param) {
		//对于欧洲站点，重新计算日均销量
		Integer salesummary = param.get("salesummary") == null ? 0 : (Integer) param.get("salesummary");
		Integer fbaplannumber = param.get("fbaplannumber") == null ? 0 : (Integer) param.get("fbaplannumber");
		if(item.get("marketplaceid")!=null && item.get("marketplaceid").toString().equals("EU")){
			int summonth = item.get("salesmonth") == null ? 0 : Integer.parseInt(item.get("salesmonth").toString());
			int sumseven = item.get("salesweek") == null ? 0 : Integer.parseInt(item.get("salesweek").toString());
			int sum15 = item.get("salesfifteen") == null ? 0 : Integer.parseInt(item.get("salesfifteen").toString());
			String openDate = item.get("openDate") == null ? null : item.get("openDate").toString();
			int qty = getAvgSales(getDaySalesFormula(), summonth, sumseven, sum15, GeneralUtil.parseDate(openDate));
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
		Map<String,Integer> presaleMap = selectBySKUMarket(item.get("sku").toString(), item.get("warehouseid").toString());
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
	public void refreshData(String  warehouseid) {
		// TODO Auto-generated method stub
		try{
			this.baseMapper.refreshData(warehouseid);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public String uploadPreSalesByExcel(Workbook workbook, UserInfo user) {
		// TODO Auto-generated method stub
			Sheet sheet = workbook.getSheetAt(0);

			List<Warehouse> warehouseList = this.iWarehouseService.lambdaQuery().eq(Warehouse::getShopid, user.getCompanyid()).list();
			HashMap<String,Warehouse> warehouseMap=new HashMap<String,Warehouse>();
			for(Warehouse item:warehouseList) {
				warehouseMap.put(item.getName(), item);
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
				
				String warehouseid = "";
				String userid = user.getId();
				Warehouse warehouse=warehouseMap.get(groupname);
				if(warehouse==null) {throw new BizException("无法正常获取仓库，请确认仓库是否存在或已经改名");}
				warehouseid=warehouse.getId();
			    if(StrUtil.isEmpty(marketplacename)) {
			    	throw new BizException("站点名称不能为空");
			    }
			    Double yearnum = null;
			    yearnum=getNumeric(yearcell) ;
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
						  quantitystr=getNumeric(monthcell) ;
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
						OrderSkuPresale record = new OrderSkuPresale();
						record.setSku(sku);
						record.setWarehouseid(warehouseid);
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
						LambdaQueryWrapper<OrderSkuPresale> query=new LambdaQueryWrapper<OrderSkuPresale>();
			    		query.eq(OrderSkuPresale::getSku,sku);
			    		query.eq(OrderSkuPresale::getWarehouseid,warehouseid);
			    		query.eq(OrderSkuPresale::getDate, GeneralUtil.formatDate(beginCalendar.getTime()));
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
				String warehouseid = "";
				String marketplaceid="";
				String userid = user.getId();
				Warehouse warehouse=warehouseMap.get(groupname);
				if(warehouse==null) {throw new BizException("无法正常获取仓库，请确认仓库是否存在或已经改名");}
				warehouseid=warehouse.getId();
			    if(StrUtil.isEmpty(marketplacename)) {
			    	throw new BizException("站点名称不能为空");
			    }

			    Double yearstr =getNumeric(yearcell) ;
			    Double monthstr=getNumeric(monthcell) ;
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
						  quantitystr=getNumeric(daycell)  ;
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
					LambdaQueryWrapper<OrderSkuPresale> query=new LambdaQueryWrapper<OrderSkuPresale>();
		    		query.eq(OrderSkuPresale::getSku,sku);
		    		query.eq(OrderSkuPresale::getDate, GeneralUtil.formatDate(beginCalendar.getTime()));
		    		query.eq(OrderSkuPresale::getWarehouseid, warehouseid);
					this.remove(query);
					if(quantitystr.intValue()==-1) {
						continue;
					}
						OrderSkuPresale record = new OrderSkuPresale();
						record.setSku(sku);
						record.setWarehouseid(warehouseid);
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
	public void downloadPreSalesByExcel(Sheet sheet, Sheet sheet2, String warehouseid, UserInfo user) {
		// TODO Auto-generated method stub
		Warehouse warehouse = iWarehouseService.getById(warehouseid);
		LambdaQueryWrapper<OrderSkuPresale> query=new LambdaQueryWrapper<OrderSkuPresale>();
		query.gt(OrderSkuPresale::getDate, GeneralUtil.formatDate(Calendar.getInstance().getTime()));
		query.eq(OrderSkuPresale::getWarehouseid, warehouseid);
		List<OrderSkuPresale> list = this.baseMapper.selectList(query);
		 SimpleDateFormat yearfmt = new SimpleDateFormat("yyyy");
		 SimpleDateFormat monthfmt = new SimpleDateFormat("MM");
		Map<String,Map<String,Map<String,Map<String,OrderSkuPresale>>>> map=new HashMap<String,Map<String,Map<String,Map<String,OrderSkuPresale>>>>();
		for(OrderSkuPresale item:list) {
			String key=item.getSku()+item.getWarehouseid();
			Map<String,Map<String,   Map<String, OrderSkuPresale>>> yearMap = map.get(key);
			if(yearMap==null) {
				yearMap=new HashMap<String,Map<String,  Map<String, OrderSkuPresale>>>();
			}
		    String year=yearfmt.format(item.getDate());
		    Map<String,Map<String,  OrderSkuPresale>> monthMap = yearMap.get(year);
		    if(monthMap==null) {
		    	monthMap=new HashMap<String,Map<String, OrderSkuPresale>>();
		    }
		    String month=monthfmt.format(item.getDate());
		    Map<String, OrderSkuPresale> dayMap = monthMap.get(month);
		    if(dayMap==null) {
		    	dayMap=new HashMap<String, OrderSkuPresale>();
		    }
		    
		    String daykey=GeneralUtil.formatDate(item.getDate());
		    OrderSkuPresale pre = dayMap.get(daykey);
		    if(pre==null) {
		    	dayMap.put(daykey, item);
		    }
		    monthMap.put(month, dayMap);
		    yearMap.put(year, monthMap);
		    map.put(key, yearMap);
		}
	
		 int sheet1=1;
		 int sheet0=1;
       
	for(  Entry<String, Map<String, Map<String, Map<String, OrderSkuPresale>>>> skuMap:map.entrySet()) {
    	for(Entry<String, Map<String, Map<String, OrderSkuPresale>>> yearMap:skuMap.getValue().entrySet()) {
    		Map<String,Integer> qty=new HashMap<String,Integer>();
    	    OrderSkuPresale one =null;
    		   for(Entry<String, Map<String, OrderSkuPresale>> monthMap:yearMap.getValue().entrySet()) {
    		    		  Map<String, OrderSkuPresale> dayMap = monthMap.getValue();
    						// 在索引0的位置创建行（最顶端的行）
    		    		    Entry<String, OrderSkuPresale> item = dayMap.entrySet().iterator().next();
    		    		    one = item.getValue();
    						Row row2 =sheet2.createRow(sheet1);
    						sheet1++;
    						Cell cell0 = row2.createCell(0);
    						cell0.setCellValue(one.getSku());
    						Cell cell1 = row2.createCell(1);
    						cell1.setCellValue(warehouse.getName());

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
    						    OrderSkuPresale pre = dayMap.get(day);
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
    		cell1.setCellValue(warehouse.getName());
    		Cell cell2 = row.createCell(2);
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
	public void replaceBatch(List<OrderSkuPresale> preList) {
		// TODO Auto-generated method stub
		this.baseMapper.replaceBatch(preList);
	}
	
	//加缓存PreSalesByMonth
		public List<Map<String,Object>> getProductPreSalesByMonth(String sku, String warehouseid) {
			List<OrderSkuPresale> presalelist = this.baseMapper.selectMonthDateEvent(sku, warehouseid);
			Map<String,OrderSkuPresale> item=new HashMap<String,OrderSkuPresale>();
			List<Map<String,Object>> result=new LinkedList<Map<String,Object>>();
		    for(OrderSkuPresale pre:presalelist) {
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
			List<Map<String, Object>> monthsale = this.baseMapper.selectMonthDateSales(sku,warehouseid,GeneralUtil.formatDate(begin),GeneralUtil.formatDate(end));
			for(Map<String, Object> mitem:monthsale) {
				qtymap.put(mitem.get("month").toString(), mitem.get("quantity"));
			}
			Warehouse warehouse = iWarehouseService.getById(warehouseid);
			List<Map<String, Object>> holidaylist = this.baseMapper.selectHoliday(warehouse.getCountry());
			for(Map<String, Object> mitem:holidaylist) {
				holiday.put(mitem.get("month").toString(), mitem.get("holiday"));
			}
		    SimpleDateFormat FMT_YMD = new SimpleDateFormat("yy-MM");
		    for(int i=1;i<=12;i++) {
				HashMap<String, Object> point = new HashMap<String,Object>();
		    	OrderSkuPresale pre = item.get(GeneralUtil.formatDate(c.getTime()));
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
		public List<Map<String, Object>> getProductPreSales(String sku, String warehouseid, String month) {
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
			List<Map<String, Object>> list  = this.baseMapper.selectDateSales(sku, warehouseid,GeneralUtil.formatDate(beginDate),GeneralUtil.formatDate(endDate));
			
		 
			cold.setTime(c.getTime());
			cold.add(Calendar.MONTH, 1);
			
			List<OrderSkuPresale> prelist = this.baseMapper.selectAllDayPresale(sku,warehouseid,GeneralUtil.formatDate(c.getTime()),GeneralUtil.formatDate(cold.getTime()));
			
			for(Map<String, Object> mitem:list) {
				qtymap.put(mitem.get("day").toString(), mitem.get("quantity"));
			}
			Map<String,OrderSkuPresale> premap=new HashMap<String,OrderSkuPresale>();
			for(OrderSkuPresale mitem:prelist) {
				premap.put(GeneralUtil.formatDate(mitem.getDate()),mitem);
			}
			
		    for(int i=1;i<monthday;i++) {
				HashMap<String, Object> point = new HashMap<String,Object>();
			    OrderSkuPresale pre = premap.get(GeneralUtil.formatDate(c.getTime()));
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

	public static int getAvgSales(String dayformula, int summonth, int sumseven, int sumhalfmonth, Date openDay) {
		int qty = 0;
		double monthday = 30.0;
		double halfmonthday = 15.0;
		double weekday = 7.0;
		double monthrate = 0.2;
		double halfmonthrate = 0.3;
		double weekdayrate = 0.5;
		if (openDay != null) {
			Calendar c = Calendar.getInstance();
			long diff = c.getTime().getTime() - openDay.getTime();// 这样得到的差值是微秒级别
			long days = diff / (1000 * 60 * 60 * 24);
			if (days > 1) {
				days = days - 1;
			}
			if (days != 0) {
				if (days < weekday) {
					weekday = days;
					halfmonthday = days;
					monthday = days;
				} else if (days < halfmonthday) {
					halfmonthday = days;
					monthday = days;
				} else if (days < monthday) {
					monthday = days;
				}
			}
		}
		double dayquantity = 0;
		if (dayformula != null ) {
			HashMap<String,Object> param=new HashMap<String,Object>();
			param.put("sumhalfmonth", sumhalfmonth);
			param.put("summonth", summonth);
			param.put("sumseven", sumseven);
			Expression compiledExp = AviatorEvaluator.compile(dayformula);
			Object result = compiledExp.execute(param);
			dayquantity =Double.parseDouble(result.toString());
		} else {
			dayquantity = (summonth * monthrate) / (monthday) + (sumseven * weekdayrate) / (weekday)
					+ (sumhalfmonth * halfmonthrate) / (halfmonthday);
		}
		qty = (int) Math.ceil(dayquantity * 10);
		int dom = qty % 10;
		if (dom > 8) {
			qty = qty / 10 + 1;
		} else {
			qty = qty / 10;
		}
		return qty;
	}
	public static Double getNumeric(Cell cell) {
		Double result=null;
		if(cell==null)return null;
		if(cell.getCellTypeEnum().equals(CellType.NUMERIC)) {
			result=cell.getNumericCellValue();
		}
		if(cell.getCellTypeEnum().equals(CellType.STRING)) {
			String str=cell.getStringCellValue();
			if(StrUtil.isAllNotBlank(str))
				result=Double.parseDouble(str);
		}
		return result;
	}
}
