package com.wimoor.amazon.summary.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.wimoor.amazon.api.ErpClientOneFeignManager;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.orders.mapper.OrdersReportMapper;
import com.wimoor.amazon.orders.mapper.OrdersSummaryMapper;
import com.wimoor.amazon.orders.mapper.SummaryAllMapper;
import com.wimoor.amazon.orders.pojo.dto.AmazonOrderSummaryDTO;
import com.wimoor.amazon.orders.pojo.entity.OrdersSummary;
import com.wimoor.amazon.product.service.IProductInOptService;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.amazon.summary.service.IOrdersSumService;
import com.wimoor.amazon.util.ChartPoint;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.IPictureService;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.util.StrUtil;
 
@Service("ordersSumService")  
public class OrdersSumServiceImpl implements IOrdersSumService {
    @Resource
    OrdersSummaryMapper ordersSummaryMapper;
    @Resource
    OrdersReportMapper ordersReportMapper;
    @Resource
    SummaryAllMapper summaryAllMapper;
    @Autowired
    ErpClientOneFeignManager erpClientOneFeign;
    @Autowired
    IProductInfoService iProductInfoService;
    @Autowired
    IProductInOptService iProductInOptService;
    @Autowired
    IPictureService iPictureService;
    @Autowired
    IAmazonAuthorityService amazonAuthorityService;
    public static String getKeyByTimeType(Map<String, Object> map,Calendar c){
    	String tempkey="";
    	Integer m= (c.get(Calendar.MONTH)+1);
    	String mstr=m<10?"0"+m.toString():m.toString();
    	Integer d=c.get(Calendar.DAY_OF_MONTH);
    	String dstr=d<10?"0"+d.toString():d.toString();
  	  if("Daily".equals(map.get("bytime"))){
		   tempkey =tempkey+  mstr;
		   tempkey =tempkey+ "-"+dstr ; 
		  }
  	  else if("Weekly".equals(map.get("bytime"))){
  		 tempkey= GeneralUtil.getSundayOfThisWeek(c.getTime());
  		 tempkey=tempkey.substring(tempkey.length()-5, tempkey.length());
  	  }
     else if("Monthly".equals(map.get("bytime"))){
			   tempkey =tempkey+  (c.get(Calendar.YEAR));
			   tempkey =tempkey+ "-"+ m; 
		  }
		return tempkey;
    }

    public static Boolean checkTimeType(Map<String, Object> map,Calendar c,Date beginDate,Date endDate){
    	 Calendar calendar = Calendar.getInstance();      
    	    if(endDate != null){        
    	         calendar.setTime(endDate);      
    	    }        
    	    //int w = calendar.get(Calendar.DAY_OF_WEEK)  ;  
    	    int m = calendar.get(Calendar.MONTH);
    	    int y = calendar.get(Calendar.YEAR);
      if(c.get(Calendar.YEAR)<y) return true;
  	  if("Daily".equals(map.get("bytime"))){
		     return c.getTime().equals(endDate)||c.getTime().before(endDate);
	  }
  	  else if("Weekly".equals(map.get("bytime"))){
  		    return c.getTime().equals(endDate)||  GeneralUtil.getSundayOfThisWeek(c.getTime()).compareTo(GeneralUtil.getSundayOfThisWeek(endDate))<=0;
  	  }
	  else if("Monthly".equals(map.get("bytime"))){
		    return  c.get(Calendar.YEAR)==y?c.get(Calendar.MONTH)<=m:c.get(Calendar.YEAR)<y; 
	  }
	return null;
	 
    }
    public static void step(Map<String, Object> map,Calendar c,Date beginDate,Date endDate){
  	  if("Daily".equals(map.get("bytime"))){
		    c.add(Calendar.DATE, 1);
		  }
	  else if("Weekly".equals(map.get("bytime"))){
		    c.add(Calendar.DATE,7); 
		  }
	   else if("Monthly".equals(map.get("bytime"))){
		   c.add(Calendar.MONTH,1); 
		  }
 
	 
    }
     
	
	public List<Map<String, Object>> weekReport(Map<String,Object> map) {
		return   ordersSummaryMapper.weekAmount(map) ;
	}
	
	 
	public int returnCalendarDay(String field, Calendar c, int daysize) {
		if ("last30".equals(field)) {
			c.add(Calendar.DATE, -30);
			daysize = 30;
		}else  if("last60".equals(field)){
			c.add(Calendar.DATE, -60);
			daysize = 60;
		}
		else if("last90".equals(field)){
			c.add(Calendar.DATE, -90);
			daysize = 90;
		}else {
			c.add(Calendar.DATE, -180);
			daysize = 180;
		}
		return daysize;
	}
	 
	
	private Date getStepStart(Calendar calendar, String summaryType) {
		if("month".equals(summaryType) ){
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			return calendar.getTime();
		}else if("week".equals(summaryType)) {
			calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
			return calendar.getTime();
		}else  {
			return calendar.getTime();
		}
	}	
	private Date getStep(Calendar calendar, String summaryType) {
		// TODO Auto-generated method stub
		if("month".equals(summaryType) ){
			calendar.add(Calendar.MONTH,1);
			return calendar.getTime();
		}else if("week".equals(summaryType)) {
			calendar.add(Calendar.DATE,7);
			return calendar.getTime();
		}else  {
			calendar.add(Calendar.DATE,1);
			return calendar.getTime();
		}
	}
	
	@Cacheable(value = "findordershopReport#60" )
	public List<Map<String, Object>> findordershopReport(Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = ordersSummaryMapper.findordershopReport(parameter);
		Calendar calendar = Calendar.getInstance();
		String endDateStr = (String) parameter.get("endDate");
		String beginDateStr = (String) parameter.get("beginDate");
		Date endDate = null;
		Date beginDate = null;
		if (endDateStr != null && beginDateStr != null) {
			endDate = GeneralUtil.getDatez(endDateStr);
			beginDate = GeneralUtil.getDatez(beginDateStr);
		}
		calendar.setTime(beginDate);
		Date end = endDate;
		Map<String, Object> fieldmap = new HashMap<String, Object>();
		String summaryType=parameter.get("summaryType").toString();  
		for (Date step = getStepStart(calendar,summaryType); step.before(end) || step.equals(end);step= getStep(calendar ,summaryType)) {
			String field = GeneralUtil.formatDate(step, GeneralUtil.FMT_YMD);
			fieldmap.put("v" + field.replace("-", ""), field);
		}
		List<String> materlist = null;
		if(parameter.get("color") != null || parameter.get("owner") != null) {
			 String color="";
			 if(parameter.get("color")!=null) {
				 color=parameter.get("color").toString();
			 }
			 String owner="";
			 if(parameter.get("owner")!=null) {
				 owner=parameter.get("owner").toString();
			 }
			 String shopid="";
			 if(parameter.get("shopid")!=null) {
				 shopid=parameter.get("shopid").toString();
			 }
			 Result<List<String>> result = erpClientOneFeign.findMarterialForColorOwner(shopid,owner,color);
			 if(Result.isSuccess(result)&&result.getData()!=null) {
				 materlist=result.getData();
			 }
		}
		Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
		List<Map<String, Object>> sumList = new ArrayList<Map<String, Object>>();
		if(list != null && list.size() > 0) {
			List<String> mskus=new LinkedList<String>();
			for(Map<String, Object> item:list) {
				String sku = item.get("sku").toString();
				String date = "v" + item.get("purchase_date");
				date = date.replaceAll("-", "");
				int dateSum = item.get("quantity") == null?0:Integer.parseInt(item.get("quantity").toString());
				if(map.get(sku) == null) {
					item.put(date, dateSum);
					item.put("vsum", dateSum);
					map.put(sku, item);
				}else {
					if(map.get(sku).get(date) != null) {
						map.get(sku).put(date, dateSum + Integer.parseInt(map.get(sku).get(date).toString()));
					}else {
						map.get(sku).put(date, dateSum);
					}
					dateSum = (Integer) map.get(sku).get("vsum") + dateSum;
					map.get(sku).put("vsum", dateSum);
				}
				mskus.add(item.get("msku").toString());
			}
			Result<Map<String, Object>> materialResult = erpClientOneFeign.findMaterialMapBySku(mskus);
			Map<String, Object> materials =null;
		    if(materialResult!=null&&Result.isSuccess(materialResult)&&materialResult.getData()!=null){
		    	 materials = materialResult.getData();
		    }
			Map<String, Object> sumDate = new HashMap<String, Object>();
			for(String key : map.keySet()) { 
				Map<String, Object> map2 = map.get(key);
				for(String fileKey : fieldmap.keySet()) {
					if(map2.containsKey(fileKey)) {
						continue;
					}
					map2.put(fileKey, 0);
				}
				String msku=map2.get("msku").toString();
				if(materlist != null) {
					if(!materlist.contains(msku)) continue;
				}
				for(String datekey : map2.keySet()) {
					if(materlist != null) {
						if(!materlist.contains(msku)) continue;
					}
					if(datekey.contains("v")) {
						String newkey = datekey.replaceAll("v", "");
						if(newkey.contains("sum")) newkey = "汇总";	
						if(sumDate.get(newkey) == null) {
							sumDate.put(newkey, map2.get(datekey));
						}else {
							if(newkey.contains("sum")) newkey = "汇总";
							int num = map2.get(datekey) == null?0:Integer.parseInt(map2.get(datekey).toString());
							int num2 = sumDate.get(newkey) == null?0:Integer.parseInt(sumDate.get(newkey).toString());
							sumDate.put(newkey, num+num2);
						}
					}
				}
				
			    
				if (map2.get("sku") != null) {
					map2.put("amz_sku", map2.get("sku"));
				}
			    if(materials!=null&&materials.get(msku)!=null){
			    	@SuppressWarnings("unchecked")
					Map<String,Object> material = (Map<String, Object>) materials.get(msku);
			    	Object image=map2.get("image");
			    	map2.putAll(material);
			    	if(map2.get("image")==null&&image!=null) {
                            	map2.put("image", image);
			    	}
			    } 
			    
			    sumList.add(map2);
			}
			for(Map<String, Object> item:list) {
				item.put("sumData",sumDate);
			}
		}
		return sumList;
	}
	
	public List<Map<String,String>> getSalesField(Map<String, Object> parameter) {
		List<Map<String,String>> orderlist = new ArrayList<Map<String,String>>();
		Calendar calendar = Calendar.getInstance();
		String endDateStr = (String) parameter.get("endDate");
		String beginDateStr = (String) parameter.get("beginDate");
		String summaryType=parameter.get("summaryType").toString();
		Date endDate = null;
		Date beginDate = null;
		if (endDateStr != null && beginDateStr != null) {
			endDate = GeneralUtil.getDatez(endDateStr);
			beginDate = GeneralUtil.getDatez(beginDateStr);
		} else {
			endDate = calendar.getTime();
			calendar.add(Calendar.DATE, -7);
			beginDate = calendar.getTime();
		}
		calendar.setTime(beginDate);
		Date end = endDate;

		for (Date step = getStepStart(calendar,summaryType); step.before(end) || step.equals(end); step=getStep(calendar,summaryType)) {
			Map<String,String> map = getStepMap(step,summaryType);
			orderlist.add(map);
		}
		return orderlist;
	}

	private Map<String,String> getStepMap(Date step,String summaryType) {
		// TODO Auto-generated method stub
		Map<String,String> map=new HashMap<String,String>();
		if("month".equals(summaryType) ){
			SimpleDateFormat FMT_YMD = new SimpleDateFormat("YY年MM月");
			SimpleDateFormat FMT_YMD2 = new SimpleDateFormat("yyyyMMdd");
			String field = GeneralUtil.formatDate(step, FMT_YMD);
			map.put("name", field); 
			map.put("label", "v"+ GeneralUtil.formatDate(step, FMT_YMD2));
		}else if("week".equals(summaryType)) {
			SimpleDateFormat FMT_YMD = new SimpleDateFormat("MM月dd日");
			SimpleDateFormat FMT_YMD2 = new SimpleDateFormat("yyyyMMdd");
			String field = GeneralUtil.formatDate(step, FMT_YMD);
			map.put("name", field); 
			map.put("label", "v"+ GeneralUtil.formatDate(step, FMT_YMD2));
		}else  {
			SimpleDateFormat FMT_YMD = new SimpleDateFormat("MM月dd日");
			SimpleDateFormat FMT_YMD2 = new SimpleDateFormat("yyyyMMdd");
			String field = GeneralUtil.formatDate(step, FMT_YMD);
			map.put("name", field); 
			map.put("label", "v"+ GeneralUtil.formatDate(step, FMT_YMD2));
		}
		
		return map;
	}

	

	public String getStringFromMap(Map<String, Object> parameter, String key) {
		if (parameter.get(key) == null)
			return null;
		return (String) parameter.get(key);
	}
	public SXSSFWorkbook setProductSalasExcelBook(List<Map<String, Object>> list,List<Map<String, String>> field) {
		// TODO Auto-generated method stub
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		if(list != null && list.size() > 0){
			Sheet sheet = workbook.createSheet("sheet1");
			// 在索引0的位置创建行（最顶端的行）
			Row trow = sheet.createRow(0);
			Cell cell = trow.createCell(0); // 在索引0的位置创建单元格(左上端)
			cell.setCellValue("SKU");
			cell = trow.createCell(1); // 在索引1的位置创建单元格(左上端)
			cell.setCellValue("负责人");
			cell = trow.createCell(2); // 在索引1的位置创建单元格(左上端)
			cell.setCellValue("上架日期"); 
			cell = trow.createCell(3); // 在索引1的位置创建单元格(左上端)
			cell.setCellValue("汇总"); 
			Map<String, Object> linkmap = list.get(0);
			if(linkmap.get("endDate") == null || linkmap.get("beginDate") == null) {
				throw new BizException("请先选择日期！");
			}
			int temp = 0;
			for(Map<String, String> item:field){
			    cell = trow.createCell(temp + 4); // 在索引0的位置创建单元格(左上端)
			    String value =item.get("name").toString();
				cell.setCellValue(value.toString());
				temp++;
			}
			for (int i = 0; i < list.size(); i++) {
				Row row = sheet.createRow(i + 1);
				Map<String, Object> map = list.get(i);
				cell = row.createCell(0); // 在索引0的位置创建单元格(左上端)
				String sku = map.get("amz_sku") == null ? "-" : map.get("amz_sku").toString();
				cell.setCellValue(sku);
				cell = row.createCell(1); // 在索引1的位置创建单元格(左上端)
				String create = map.get("ownername") == null ? "-" : map.get("ownername").toString();
				cell.setCellValue(create);
				cell = row.createCell(2); 
				
				String openDate = null;;
				if(map.get("opendate")!=null) {
					openDate=map.get("opendate").toString();
					cell.setCellValue(GeneralUtil.formatDate(GeneralUtil.StringfromDate(openDate, "yyyy-MM-dd")));
				}else {
					cell.setCellValue("--");
				}
				cell = row.createCell(3); 
				String vsum = map.get("vsum") == null ? "-" : map.get("vsum").toString();
				cell.setCellValue(vsum);
				int timeTemp = 0;
				for(Map<String, String>  item:field){
				    cell = row.createCell(timeTemp + 4);
				    String value = item.get("label").toString();
				    if(map.get(value) != null && StrUtil.isNotEmpty(map.get(value).toString())){
				    	cell.setCellValue(Double.parseDouble(map.get(value).toString()));
				    }else {
				    	cell.setCellValue(0);
				    }
				    timeTemp++;
				}
			}
		}else {
			throw new BizException("没有数据可导出！");
		}
		return workbook;
	}
	
 
	
	public Map<String, Object> getSumOrdersPrice(List<Map<String, Object>> list) {
		Map<String, Object> summap = new HashMap<String, Object>();
		BigDecimal allpricermb = new BigDecimal("0");
		BigDecimal allprice = new BigDecimal("0");
		BigDecimal allqty = new BigDecimal("0");
		BigDecimal allorder = new BigDecimal("0");
		for (Map<String, Object> item : list) {
			allpricermb = allpricermb.add(item.get("orderpricermb")==null?new BigDecimal("0"):new BigDecimal(item.get("orderpricermb").toString()));
			allprice = allprice.add(item.get("orderprice")==null?new BigDecimal("0"):new BigDecimal(item.get("orderprice").toString()));
			allqty = allqty.add(item.get("quantity")==null?new BigDecimal("0"):new BigDecimal(item.get("quantity").toString()));
			allorder = allorder.add(item.get("ordersum")==null?new BigDecimal("0"):new BigDecimal(item.get("ordersum").toString()));
		}
		summap.put("allpricermb", allpricermb);
		summap.put("allqty", allqty);
		summap.put("allorder", allorder);
		summap.put("allprice", allprice);
		summap.put("allcurrency", list.get(0).get("currency"));
		return summap;
	}

	public void getOrdersPriceExport(SXSSFWorkbook workbook, Map<String, Object> paramMap) {
		Sheet sheet = workbook.createSheet("sheet1");
		List<Map<String, Object>> list = ordersSummaryMapper.getOrdersPrice(paramMap);
		Map<String,Object> titleMap = new HashMap<String, Object>();
		titleMap.put("sku", "SKU");
		titleMap.put("fowner", "产品负责人");
		titleMap.put("groupname", "店铺");
		titleMap.put("marketname", "站点");
		titleMap.put("amounttype", "金额分类");
		titleMap.put("amountdescription", "金额描述");
		titleMap.put("amount", "金额");
		titleMap.put("quantity_purchased", "购买数量");
		titleMap.put("currency", "币别");
		if(list==null){
			list = new ArrayList<Map<String,Object>>();
		}
		list.add(0,titleMap);
		
		List<String> titleList = new ArrayList<String>();
		titleList.add("sku");
		titleList.add("fowner");
		titleList.add("groupname");
		titleList.add("marketname");
		titleList.add("amount_type");
		titleList.add("amount_description");
		titleList.add("amount");
		titleList.add("quantity_purchased");
		titleList.add("currency");
		
		for(int i=0;i<list.size();i++) {
			Map<String, Object> map = list.get(i);
			Row row = sheet.createRow(i);
			for(int step = 0; step < titleList.size(); step++) {
				Cell cell = row.createCell(step);
				Object value = map.get(titleList.get(step));
				if(value!=null) {
					cell.setCellValue(value.toString());
				}else {
					cell.setCellValue("--");
				}
			}
		}
		
	}

	@Override
	public List<Map<String, Object>> getOrderChannel(String shopid) {
		// TODO Auto-generated method stub
		return this.ordersReportMapper.findAllSaleChannel(shopid);
	}
	public List<AmazonAuthority> getCurrentUserAuthList(UserInfo user)  {
		List<AmazonAuthority> amazonAuthorityList = amazonAuthorityService.selectByshopid(user.getCompanyid());
		List<AmazonAuthority> result = new ArrayList<AmazonAuthority>();
		if (user.getGroups()!=null&&user.getGroups().size()>0) {
			for (int i = 0; i < amazonAuthorityList.size(); i++) {
				if (user.getGroups().contains(amazonAuthorityList.get(i).getId())) {
					result.add(amazonAuthorityList.get(i));
				}
			}
		} else {
			result = amazonAuthorityList;
		}
		if (result.size() == 0) {
			AmazonAuthority amazonA = new AmazonAuthority();
			result.add(amazonA);
		}
		return result;
	}
	
	@Override
	public Map<String, Object> searchOrderSummary(UserInfo userinfo, AmazonOrderSummaryDTO model) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		if(model.getFulfillmentChannel()!=null&&model.getFulfillmentChannel().size()>0) {
			map.put("fulfillmentChannel", model.getFulfillmentChannel());
		}
		map.put("is_business_order", model.getIsBusinessOrder());
		if(model.getSalechannel()!=null&&model.getSalechannel().size()>0) {
			map.put("salechannel", model.getSalechannel());
		}
		map.put("orderStatus", model.getOrderStatus());
		map.put("discountfrom", model.getDiscountfrom());
		map.put("discountto", model.getDiscountto());
		map.put("bytime", model.getBytime());
		map.put("sku", model.getSku());
		if (StrUtil.isNotEmpty(model.getGroupid())) {
			if (!"all".equals(model.getGroupid())) {
				map.put("groupid", model.getGroupid());
			}
		} else {
			map.put("groupid", null);
		}
		String endDateStr =model.getEnddate();
		String beginDateStr =model.getFromdate();
		if (endDateStr != null)
			endDateStr = endDateStr.substring(0, 10);
		if (beginDateStr != null)
			beginDateStr = beginDateStr.substring(0, 10);
		map.put("endDate", endDateStr);
		map.put("beginDate", beginDateStr);
		String shopid = userinfo.getCompanyid();
		map.put("shopid", shopid);
		List<AmazonAuthority> authList = getCurrentUserAuthList(userinfo);
		map.put("amazonAuthIdList",authList);
 
	Calendar c=Calendar.getInstance();
	List<Map<String, Object>> result = null;
	Date endDate = null;
	Date beginDate = null;
	Date endDateplus = null;
	Date beginDateplus = null;
	if (endDateStr != null && beginDateStr != null) {
		endDate = GeneralUtil.getDatez(endDateStr);
		beginDate = GeneralUtil.getDatez(beginDateStr);
		Calendar cTime = Calendar.getInstance();
		cTime.setTime(endDate);
		cTime.add(Calendar.DATE, +1);
		endDateplus = cTime.getTime();
		map.put("endDate", endDateplus);
		cTime.setTime(beginDate);
		beginDateplus = cTime.getTime();
		map.put("beginDate", beginDateplus);
	}else {
		map.put("endDate", GeneralUtil.getStrDate(c.getTime()));
		c.add(Calendar.DATE, -32);
		map.put("beginDate", GeneralUtil.getStrDate(c.getTime()));
	}  
	
	if (map.get("sku") != null && map.get("sku").toString().trim() != "") {
		result = ordersReportMapper.selectByMutilParameter(map);
	} else {
		result = summaryAllMapper.selectByMutilParameter(map);
	}
	Map<String, Object> mapresult = new HashMap<String, Object>();
	List<String> steplist = new ArrayList<String>();
	List<BigDecimal> quantitylist = new ArrayList<BigDecimal>();
	List<Object> orderlist = new ArrayList<Object>();
	
	Date lastEndDate = null;
	Date lastBeginDate = null;
	int days = (int) ((endDateplus.getTime() - beginDateplus.getTime()) / (24 * 60 * 60 * 1000));
	days = days * -1;
	lastEndDate = beginDateplus;
	Calendar c2 = Calendar.getInstance();
	c2.setTime(lastEndDate);
	lastEndDate = c2.getTime();
	c2.add(Calendar.DATE, days);
	lastBeginDate = c2.getTime();
	map.put("lastEndDate", lastEndDate);
	map.put("lastBeginDate", lastBeginDate);

	List<Object> listLabel = new ArrayList<Object>();
	List<Object> listData = new ArrayList<Object>();
	List<Object> orderData = new ArrayList<Object>();

	HashMap<String, BigDecimal> tempmap = new HashMap<String, BigDecimal>();
	HashMap<String, BigDecimal> tempmap2 = new HashMap<String, BigDecimal>();
	BigDecimal unitsum = new BigDecimal("0");
	BigDecimal ordersum = new BigDecimal("0");
	for (int i = 0; i < result.size(); i++) {
		String step = (String) result.get(i).get("purchase_date");
		BigDecimal quantity = (BigDecimal) result.get(i).get("quantity");
		BigDecimal ordernumber = new BigDecimal(result.get(i).get("ordernumber").toString());
		String tempkey = null;
		if ("Daily".equals(map.get("bytime")) || "Weekly".equals(map.get("bytime"))) {
			tempkey = ChartPoint.getKeyByTimeType(map.get("bytime").toString(), step);  ;
		} else {
			tempkey = step;
		}
		unitsum = unitsum.add(quantity);
		ordersum = ordersum.add(ordernumber);
		tempmap.put(tempkey, quantity);
		tempmap2.put(tempkey, ordernumber);
	}
	 
	for (c.setTime(beginDate); ChartPoint.checkTimeType(map.get("bytime").toString(), c, beginDate, endDate); ChartPoint.step(map.get("bytime").toString(), c, beginDate, endDate)) {
		String tempkey = ChartPoint.getKeyByTimeType(map.get("bytime").toString(), c);
		BigDecimal value = (BigDecimal) tempmap.get(tempkey);
		BigDecimal value2 = tempmap2.get(tempkey);
		listLabel.add(tempkey);
		if (value == null) {
			listData.add(new BigDecimal("0"));
			orderData.add(new BigDecimal("0"));
		} else {
			listData.add(value);
			orderData.add(value2);
		}
		steplist.add(tempkey);
		quantitylist.add(new BigDecimal("0"));
		orderlist.add(new BigDecimal("0"));

	}
	Map<String, Object> last =null;
	if(map.get("sku")!=null&&map.get("sku").toString().trim()!="") {
		last = ordersReportMapper.selectLastByMutilParameter(map);
	}else {
		last= summaryAllMapper.selectLastByMutilParameter(map);
	}
	BigDecimal lastunit =new BigDecimal("0");
	
	BigDecimal lastorder=new BigDecimal("0");
	if(last!=null){
	lastorder=last.get("ordernumber")!=null? new BigDecimal(last.get("ordernumber").toString()):lastorder;
	lastunit=last.get("quantity")!=null?(BigDecimal) last.get("quantity"):lastunit;
	}
	lastunit=unitsum.subtract(lastunit);
	lastorder=ordersum.subtract(lastorder);
	mapresult.put("labels", listLabel);
	mapresult.put("listdata", listData);
	mapresult.put("orderdata", orderData);
	mapresult.put("unitsum", unitsum);
	mapresult.put("ordersum", ordersum);
	mapresult.put("lastunitsum", lastunit);
	mapresult.put("lastordersum", lastorder);
	return mapresult;
	}

	@Override
	public List<OrdersSummary> orderSummaryBySkuDate(String sku, String marketplace, String beginDate, String endDate,
			String amazonAuthId, UserInfo user, String ftype) {
		//Calendar c = Calendar.getInstance();
		Map<String, Object> param = new HashMap<String, Object>();
		List<OrdersSummary> list;
		param.put("sku", sku);
		param.put("marketplaceid", marketplace);
		param.put("amazonAuthId", amazonAuthId);
		param.put("startDate", beginDate);
		param.put("endDate", endDate);
		param.put("shopid", user.getCompanyid());
		list = ordersSummaryMapper.selectBySkuMarketplace(param);
		return list;
	}
}
