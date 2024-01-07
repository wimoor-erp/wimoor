package com.wimoor.amazon.inbound.service.impl;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.inbound.mapper.ShipInboundTransMapper;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundTrans;
import com.wimoor.amazon.inbound.service.IShipInboundTransService;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.common.GeneralUtil;
 

@Service("shipInboundTransService")
public class ShipInboundTransServiceImpl extends  ServiceImpl<ShipInboundTransMapper,ShipInboundTrans> implements IShipInboundTransService {

	@Resource
	IMarketplaceService marketplaceService;

	public List<Map<String, Object>> getShipmentFeeReport(Map<String, Object> param) {
		return this.baseMapper.transFeeShared(param);
	}
	
	public IPage<Map<String, Object>> getShipmentFeeReport(Page<?> page,Map<String, Object> param) {
		return this.baseMapper.transFeeShared(page,param);
	}

	public void setShipmentFeeReport(SXSSFWorkbook workbook, Map<String, Object> params) {
		Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
		titlemap.put("sellersku", "SKU");
		titlemap.put("NAME", "产品名称");
		titlemap.put("ownername", "负责人");
		titlemap.put("qty", "发货数量");
		titlemap.put("skufee", "运费");
		titlemap.put("msku", "本地SKU");
		titlemap.put("avgfee", "单件平均费用");
		
		List<Map<String, Object>> list = this.baseMapper.transFeeShared(params);
		Sheet sheet = workbook.createSheet("sheet1");
		// 在索引0的位置创建行（最顶端的行）
		Row trow = sheet.createRow(0);
		Object[] titlearray = titlemap.keySet().toArray();
		for (int i = 0; i < titlearray.length; i++) {
			Cell cell = trow.createCell(i); // 在索引0的位置创建单元格(左上端)
			Object value = titlemap.get(titlearray[i].toString());
			cell.setCellValue(value.toString());
		}
		for (int i = 0; i < list.size(); i++) {
			Row row = sheet.createRow(i + 1);
			Map<String, Object> map = list.get(i);
			for (int j = 0; j < titlearray.length; j++) {
				Cell cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
				Object value = map.get(titlearray[j].toString());
				if (value != null) {
					if(value instanceof BigDecimal) {
						cell.setCellValue(new BigDecimal(value.toString()).doubleValue());
					}else {
						cell.setCellValue(value.toString());
					}
				}
			}
		}
	}
	
    public List<Map<String, Object>> transFeeSharedDetail(Map<String,Object> params) {
    	List<Map<String, Object>> list = this.baseMapper.transFeeSharedDetail(params);
    	return list;
    }
    
	public void setShipmentFeeDetailReport(SXSSFWorkbook workbook, Map<String, Object> params) {
		Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
		titlemap.put("psku", "SKU");
		titlemap.put("ShipmentId", "货件编码");
		titlemap.put("ownername", "产品负责人");
		titlemap.put("optname", "发货人");
		titlemap.put("groupname", "发货店铺");
		titlemap.put("marketplace", "收货站点");
		titlemap.put("warehouse", "发货仓库");
		titlemap.put("opttime", "发货日期");
		titlemap.put("shipcompany", "发货承运商");
		titlemap.put("channame", "发货渠道");
		titlemap.put("name", "产品名称");
		titlemap.put("qty", "发货数量");
		titlemap.put("transweight", "实际结算重量");
		titlemap.put("skuweight", "SKU预估重量");
		titlemap.put("shipmentfee", "货件运费");
		titlemap.put("skufee", "SKU分摊费用");
		titlemap.put("skufeeavg", "SKU单件费用");
		List<Map<String, Object>> list = this.baseMapper.transFeeSharedDetail(params);
		Sheet sheet = workbook.createSheet("sheet1");
		// 在索引0的位置创建行（最顶端的行）
		Row trow = sheet.createRow(0);
		Object[] titlearray = titlemap.keySet().toArray();
		for (int i = 0; i < titlearray.length; i++) {
			Cell cell = trow.createCell(i); // 在索引0的位置创建单元格(左上端)
			Object value = titlemap.get(titlearray[i].toString());
			cell.setCellValue(value.toString());
		}
		for (int i = 0; i < list.size(); i++) {
			Row row = sheet.createRow(i + 1);
			Map<String, Object> map = list.get(i);
			for (int j = 0; j < titlearray.length; j++) {
				Cell cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
				Object value = map.get(titlearray[j].toString());
				if (value != null||"skufeeavg".equals(titlearray[j].toString())) {
					if("skufeeavg".equals(titlearray[j].toString())) {
						if(map.get("skufee")!=null && map.get("qty")!=null) {
							String skufeeStr=map.get("skufee").toString();
							BigDecimal qty=new BigDecimal(map.get("qty").toString()) ;
							BigDecimal skufee=new BigDecimal(skufeeStr);
							Double rate = 0.0;
							if(skufee!=null&&qty!=null&&qty.intValue()>0) {
								rate=skufee.doubleValue()/qty.doubleValue();
							} 
							cell.setCellValue(GeneralUtil.formatterNum(rate,2));
						}else {
							cell.setCellValue("--");
						}
					}else {
						cell.setCellValue(value.toString());
					}
				}
			}
		}
	}
	/**
	 * 'SKU头程历史明细'中的数据 , 显示最近一周的货件计算的分摊均价, 最近一周没有再往前取一周, 最多取至10周前。如仍然不存在发货货件，则为空，做注释提示
	 * @param shopid
	 * @param marketplaceid
	 * @param groupid
	 * @param sku
	 * @return
	 */
	 public Map<String,Object>  shipmentTransFee(String shopid,String marketplaceid,String groupid,String sku) {
	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    	HashMap<String,Object> param=new HashMap<String,Object>();
			param.put("shopid", shopid);
			param.put("marketplaceid", marketplaceid);
			param.put("groupid", groupid);
			String search = sku;
	        Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -70);
		    String fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
			param.put("fromDate", fromDate);
	        String toDate = GeneralUtil.formatDate(new Date(), sdf);
			param.put("endDate", toDate);
			param.put("search", search);
		 
	 
			List<Map<String, Object>> list = getShipmentFeeDetailReport(param);
			Calendar end = Calendar.getInstance();
			end.add(Calendar.DATE, -7);
			int i=0;
			BigDecimal totalskufee=new BigDecimal("0");
			BigDecimal totalqty=new BigDecimal("0");
			Date opttime=null;
			while(cal.getTime().before(end.getTime())) {
				while(i<list.size()) {
					Map<String, Object> map = list.get(i);
					opttime = AmzDateUtils.getDate(map.get("opttime"));
					if(opttime.before(end.getTime())) {
						break;
					}
					if(map.get("skufee")!=null && map.get("qty")!=null) {
						String skufeeStr=map.get("skufee").toString();
						BigDecimal qty=new BigDecimal(map.get("qty").toString()) ;
						BigDecimal skufee=new BigDecimal(skufeeStr);
						totalqty=totalqty.add(qty);
						totalskufee=totalskufee.add(skufee);
					} 
				  i++;
				}
				if(totalskufee.floatValue()>0.001) {
					break;
				}
			  end.add(Calendar.DATE, -7);
			}
			HashMap<String,Object> result=new HashMap<String,Object>();
			result.put("opttime", opttime);
			Double rate = totalskufee.doubleValue()/totalqty.doubleValue();
			result.put("skufeeavg", rate);
			return result;
	    }
	 
	public IPage<Map<String, Object>> getShipmentFeeDetailReport(Page<?> page,Map<String, Object> param) {
		IPage<Map<String, Object>> list = this.baseMapper.transFeeSharedDetail(page,param);
		if(list!=null && list.getRecords().size()>0) {
			for(int i=0;i<list.getRecords().size();i++) {
				Map<String, Object> map = list.getRecords().get(i);
				if(map.get("skufee")!=null && map.get("qty")!=null) {
					String skufeeStr=map.get("skufee").toString();
					BigDecimal qty=new BigDecimal(map.get("qty").toString()) ;
					BigDecimal skufee=new BigDecimal(skufeeStr);
					//应该找出表t_profitcfg中的shipmentStyle 再去判断使用谁
					if(skufee!=null&&qty!=null&&qty.intValue()>0) {
						Double rate = skufee.doubleValue()/qty.doubleValue();
						map.put("skufeeavg", rate );
					}else {
						map.put("skufeeavg", 0);
					}
				}else {
					map.put("skufeeavg", null);
				}
			}
		}
		return list;
	}
	public  List<Map<String, Object>> getShipmentFeeDetailReport(Map<String, Object> param ) {
		 List<Map<String, Object>> list = this.baseMapper.transFeeSharedDetail(param );
		if(list!=null && list.size()>0) {
			for(int i=0;i<list.size();i++) {
				Map<String, Object> map = list.get(i);
				if(map.get("skufee")!=null && map.get("qty")!=null) {
					String skufeeStr=map.get("skufee").toString();
					BigDecimal qty=new BigDecimal(map.get("qty").toString()) ;
					BigDecimal skufee=new BigDecimal(skufeeStr);
					//应该找出表t_profitcfg中的shipmentStyle 再去判断使用谁
					if(skufee!=null&&qty!=null&&qty.intValue()>0) {
						Double rate = skufee.doubleValue()/qty.doubleValue();
						list.get(i).put("skufeeavg", rate );
					}else {
						list.get(i).put("skufeeavg", 0);
					}
				}else {
					list.get(i).put("skufeeavg", null);
				}
			}
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public IPage<Map<String, Object>>  transSKUFeeShared(Page<?> page,Map<String, Object> param) {
		 IPage<Map<String, Object>> pagelist = this.baseMapper.transFeeSharedWeek(page,param);
		 HashMap<String,Object> result=new HashMap<String,Object>();
		 HashMap<String,Object> temp=new HashMap<String,Object>();
		 LinkedList<String> labels=new LinkedList<String>();
		 LinkedList<String> series=new LinkedList<String>();
		 LinkedList<String> seriesfee=new LinkedList<String>();
		 LinkedList<String> seriesavgfee=new LinkedList<String>();
		 
		if(pagelist!=null && pagelist.getRecords().size()>0) {
			String fromdateStr = param.get("fromDate").toString();
			String enddateStr = param.get("endDate").toString();
			Date fromdate = GeneralUtil.getDatez(fromdateStr);
			Date firtday = getFirstDayofWeek(fromdate);
			Calendar c=Calendar.getInstance();
			c.setTime(firtday);
			Date enddate = GeneralUtil.getDatez(enddateStr);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			 for(Map<String, Object> item:pagelist.getRecords()) {
				 if(item.get("opttime2")!=null){
					 temp.put(item.get("opttime2").toString().substring(0,10), item);
				 }
			 }
			while (c.getTime().before(enddate)) {
				String key = sdf.format(c.getTime());
				if(temp.get(key)==null) {
					series.add("0"); 
					seriesfee.add("0");
					seriesavgfee.add("0");
				}else {
					Map<String, Object> myItem = (Map<String, Object>)temp.get(key);
					if(myItem==null||myItem.get("qty")==null) {
						series.add("0");
					}else {				
						series.add(myItem.get("qty").toString()); 
					}
					if(myItem==null||myItem.get("skufee")==null) {
						seriesfee.add("0");
					}else {				
						seriesfee.add(myItem.get("skufee").toString()); 
					}
					if(myItem==null||myItem.get("avgfee")==null) {
						seriesavgfee.add("0");
					}else {				
						seriesavgfee.add(myItem.get("avgfee").toString()); 
					}
				}
				labels.add(key);
				c.add(Calendar.DATE, 7);
			}
			 
		}
		result.put("labels", labels);
		result.put("series", series);
		result.put("seriesfee", seriesfee);
		result.put("seriesavgfee", seriesavgfee);
		if(pagelist!=null&&pagelist.getRecords().size()>0) {
		   pagelist.getRecords().get(0).put("chartdata", result);
		}
		return pagelist;
	}
	
	   public static  Date getFirstDayofWeek(Date time) {
		    Calendar cal = Calendar.getInstance();
		    cal.setTime(time);
		    // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		    int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		    if (1 == dayWeek) {
		        cal.add(Calendar.DAY_OF_MONTH, -1);
		    }
		    cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		    int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		    cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		    return  cal.getTime();
		}

	@Override
	public List<Map<String, Object>> calculateTransFeeSharedDetail(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return this.baseMapper.transFeeShared(param);
	}
 
}