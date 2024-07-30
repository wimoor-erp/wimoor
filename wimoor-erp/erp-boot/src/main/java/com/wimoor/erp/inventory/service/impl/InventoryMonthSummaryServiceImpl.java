package com.wimoor.erp.inventory.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.erp.inventory.mapper.InventoryMonthSummaryMapper;
import com.wimoor.erp.inventory.pojo.entity.InventoryMonthSummary;
import com.wimoor.erp.inventory.service.IInventoryMonthSummaryService;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.warehouse.mapper.WarehouseMapper;
import com.wimoor.erp.warehouse.mapper.WhseUnsalableReportMapper;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;

import lombok.RequiredArgsConstructor;

@Service("inventoryMonthSummaryService")
@RequiredArgsConstructor
public class InventoryMonthSummaryServiceImpl extends ServiceImpl<InventoryMonthSummaryMapper,InventoryMonthSummary> implements IInventoryMonthSummaryService {
	 
	final InventoryMonthSummaryMapper inventoryMonthSummaryMapper;
	 
	final WarehouseMapper warehouseMapper;
	 
	final IMaterialService materialService;
	 
	final WhseUnsalableReportMapper whseUnsalableReportMapper;
	public Integer getBeginInvMap(String shopid, String warehouseid,String materialid ) {
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Calendar c=Calendar.getInstance();
        c.add(Calendar.MONTH, -2);
        c.set(Calendar.DAY_OF_MONTH,c.getActualMaximum(Calendar.DAY_OF_MONTH));//设置为最后一天
		String first_day=format.format(c.getTime()) ;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shopid", new BigInteger(shopid));
		param.put("warehouseid", new BigInteger(warehouseid));
		param.put("materialid", new BigInteger(materialid));
		param.put("byday",GeneralUtil.getDatez(first_day));
		Map<String, Object> result = whseUnsalableReportMapper.localByDay(param);
	    return result==null||result.get("invqty")==null?Integer.parseInt("0"):Integer.parseInt(result.get("invqty").toString());
	 
	}
	public Integer getpurchase(String shopid, String warehouseid,String materialid ) {
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar=Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		String first_day=format.format(calendar.getTime()) ;
		Calendar calendarEnd=Calendar.getInstance();
		calendarEnd.set(Calendar.DAY_OF_MONTH, 1);
		String end_day=format.format(calendarEnd.getTime()) ;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shopid", new BigInteger(shopid));
		param.put("warehouseid", new BigInteger(warehouseid));
		param.put("materialid", new BigInteger(materialid));
		param.put("begindate",GeneralUtil.getDatez(first_day));
		param.put("enddate",GeneralUtil.getDatez(end_day));
		Map<String, Object> result = inventoryMonthSummaryMapper.getpurchase(param);
	    return result==null||result.get("qty")==null?Integer.parseInt("0"):Integer.parseInt(result.get("qty").toString());
	 
	}
	
	
	private Integer getEndInvMap(String shopid, String warehouseid, String materialid) {
		// TODO Auto-generated method stub
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
	     Calendar c = Calendar.getInstance();
	     c.add(Calendar.MONTH, -1);
	     c.set(Calendar.DAY_OF_MONTH,c.getActualMaximum(Calendar.DAY_OF_MONTH));//设置为最后一天
		String first_day=format.format(c.getTime()) ;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shopid", new BigInteger(shopid));
		param.put("warehouseid", new BigInteger(warehouseid));
		param.put("materialid", new BigInteger(materialid));
		param.put("byday",GeneralUtil.getDatez(first_day));
		Map<String, Object> result = whseUnsalableReportMapper.localByDay(param);
	    return result==null||result.get("invqty")==null?Integer.parseInt("0"):Integer.parseInt(result.get("invqty").toString());
	}
	private Integer getOut(String shopid, String warehouseid, String materialid) {
		// TODO Auto-generated method stub
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar=Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		String first_day=format.format(calendar.getTime()) ;
		Calendar calendarEnd=Calendar.getInstance();
		calendarEnd.set(Calendar.DAY_OF_MONTH, 1);
		String end_day=format.format(calendarEnd.getTime()) ;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shopid", new BigInteger(shopid));
		param.put("warehouseid", new BigInteger(warehouseid));
		param.put("materialid", new BigInteger(materialid));
		param.put("begindate",GeneralUtil.getDatez(first_day));
		param.put("enddate",GeneralUtil.getDatez(end_day));
		Map<String, Object> result = inventoryMonthSummaryMapper.getOtherOut(param);
	    return result==null||result.get("qty")==null?Integer.parseInt("0"):Integer.parseInt(result.get("qty").toString());
	}
	private Integer getIn(String shopid, String warehouseid, String materialid) {
		// TODO Auto-generated method stub
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar=Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		String first_day=format.format(calendar.getTime()) ;
		Calendar calendarEnd=Calendar.getInstance();
		calendarEnd.set(Calendar.DAY_OF_MONTH, 1);
		String end_day=format.format(calendarEnd.getTime()) ;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shopid", new BigInteger(shopid));
		param.put("warehouseid", new BigInteger(warehouseid));
		param.put("materialid", new BigInteger(materialid));
		param.put("begindate",GeneralUtil.getDatez(first_day));
		param.put("enddate",GeneralUtil.getDatez(end_day));
		Map<String, Object> result = inventoryMonthSummaryMapper.getOtherIn(param);
	    return result==null||result.get("qty")==null?Integer.parseInt("0"):Integer.parseInt(result.get("qty").toString());
	}
	
	private Integer getShip(String shopid, String warehouseid, String materialid) {
		// TODO Auto-generated method stub
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar=Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		String first_day=format.format(calendar.getTime()) ;
		Calendar calendarEnd=Calendar.getInstance();
		calendarEnd.set(Calendar.DAY_OF_MONTH, 1);
		String end_day=format.format(calendarEnd.getTime()) ;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shopid", new BigInteger(shopid));
		param.put("warehouseid", new BigInteger(warehouseid));
		param.put("materialid", new BigInteger(materialid));
		param.put("begindate",GeneralUtil.getDatez(first_day));
		param.put("enddate",GeneralUtil.getDatez(end_day));
		Map<String, Object> result = inventoryMonthSummaryMapper.getShip(param);
	    return result==null||result.get("qty")==null?Integer.parseInt("0"):Integer.parseInt(result.get("qty").toString());
	}
	
	private Integer getDispathIn(String shopid, String warehouseid, String materialid) {
		// TODO Auto-generated method stub
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar=Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		String first_day=format.format(calendar.getTime()) ;
		Calendar calendarEnd=Calendar.getInstance();
		calendarEnd.set(Calendar.DAY_OF_MONTH, 1);
		String end_day=format.format(calendarEnd.getTime()) ;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shopid", new BigInteger(shopid));
		param.put("warehouseid", new BigInteger(warehouseid));
		param.put("materialid", new BigInteger(materialid));
		param.put("begindate",GeneralUtil.getDatez(first_day));
		param.put("enddate",GeneralUtil.getDatez(end_day));
		Map<String, Object> result = inventoryMonthSummaryMapper.getDispathIn(param);
	    return result==null||result.get("qty")==null?Integer.parseInt("0"):Integer.parseInt(result.get("qty").toString());
	}
	
	private Integer getDispathOut(String shopid, String warehouseid, String materialid) {
		// TODO Auto-generated method stub
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar=Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		String first_day=format.format(calendar.getTime()) ;
		Calendar calendarEnd=Calendar.getInstance();
		calendarEnd.set(Calendar.DAY_OF_MONTH, 1);
		String end_day=format.format(calendarEnd.getTime()) ;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shopid", new BigInteger(shopid));
		param.put("warehouseid", new BigInteger(warehouseid));
		param.put("materialid", new BigInteger(materialid));
		param.put("begindate",GeneralUtil.getDatez(first_day));
		param.put("enddate",GeneralUtil.getDatez(end_day));
		Map<String, Object> result = inventoryMonthSummaryMapper.getDispathOut(param);
	    return result==null||result.get("qty")==null?Integer.parseInt("0"):Integer.parseInt(result.get("qty").toString());
	}
	private Integer getChangeIn(String shopid, String warehouseid, String materialid) {
		// TODO Auto-generated method stub
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar=Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		String first_day=format.format(calendar.getTime()) ;
		Calendar calendarEnd=Calendar.getInstance();
		calendarEnd.set(Calendar.DAY_OF_MONTH, 1);
		String end_day=format.format(calendarEnd.getTime()) ;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shopid", new BigInteger(shopid));
		param.put("warehouseid", new BigInteger(warehouseid));
		param.put("materialid", new BigInteger(materialid));
		param.put("begindate",GeneralUtil.getDatez(first_day));
		param.put("enddate",GeneralUtil.getDatez(end_day));
		Map<String, Object> result = inventoryMonthSummaryMapper.getChangeIn(param);
	    return result==null||result.get("qty")==null?Integer.parseInt("0"):Integer.parseInt(result.get("qty").toString());
	}
	
	private Integer getChangeOut(String shopid, String warehouseid, String materialid) {
		// TODO Auto-generated method stub
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar=Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		String first_day=format.format(calendar.getTime()) ;
		Calendar calendarEnd=Calendar.getInstance();
		calendarEnd.set(Calendar.DAY_OF_MONTH, 1);
		String end_day=format.format(calendarEnd.getTime()) ;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shopid", new BigInteger(shopid));
		param.put("warehouseid", new BigInteger(warehouseid));
		param.put("materialid", new BigInteger(materialid));
		param.put("begindate",GeneralUtil.getDatez(first_day));
		param.put("enddate",GeneralUtil.getDatez(end_day));
		Map<String, Object> result = inventoryMonthSummaryMapper.getChangeOut(param);
	    return result==null||result.get("qty")==null?Integer.parseInt("0"):Integer.parseInt(result.get("qty").toString());
	}
	
	private Integer getAssemblyIn(String shopid, String warehouseid, String materialid) {
		// TODO Auto-generated method stub
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar=Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		String first_day=format.format(calendar.getTime()) ;
		Calendar calendarEnd=Calendar.getInstance();
		calendarEnd.set(Calendar.DAY_OF_MONTH, 1);
		String end_day=format.format(calendarEnd.getTime()) ;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shopid", new BigInteger(shopid));
		param.put("warehouseid", new BigInteger(warehouseid));
		param.put("materialid", new BigInteger(materialid));
		param.put("begindate",GeneralUtil.getDatez(first_day));
		param.put("enddate",GeneralUtil.getDatez(end_day));
		Map<String, Object> result = inventoryMonthSummaryMapper.getAssemblyIn(param);
	    return result==null||result.get("qty")==null?Integer.parseInt("0"):Integer.parseInt(result.get("qty").toString());
	}
	
	private Integer getStock(String shopid, String warehouseid, String materialid) {
		// TODO Auto-generated method stub
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar=Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		String first_day=format.format(calendar.getTime()) ;
		Calendar calendarEnd=Calendar.getInstance();
		calendarEnd.set(Calendar.DAY_OF_MONTH, 1);
		String end_day=format.format(calendarEnd.getTime()) ;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shopid", new BigInteger(shopid));
		param.put("warehouseid", new BigInteger(warehouseid));
		param.put("materialid", new BigInteger(materialid));
		param.put("begindate",GeneralUtil.getDatez(first_day));
		param.put("enddate",GeneralUtil.getDatez(end_day));
		Map<String, Object> result = inventoryMonthSummaryMapper.getStock(param);
	    return result==null||result.get("qty")==null?Integer.parseInt("0"):Integer.parseInt(result.get("qty").toString());
	}
	
	
	private Integer getAssemblyOut(String shopid, String warehouseid, String materialid) {
		// TODO Auto-generated method stub
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar=Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		String first_day=format.format(calendar.getTime()) ;
		Calendar calendarEnd=Calendar.getInstance();
		calendarEnd.set(Calendar.DAY_OF_MONTH, 1);
		String end_day=format.format(calendarEnd.getTime()) ;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shopid", new BigInteger(shopid));
		param.put("warehouseid", new BigInteger(warehouseid));
		param.put("materialid", new BigInteger(materialid));
		param.put("begindate",GeneralUtil.getDatez(first_day));
		param.put("enddate",GeneralUtil.getDatez(end_day));
		Map<String, Object> result = inventoryMonthSummaryMapper.getAssemblyOut(param);
	    return result==null||result.get("qty")==null?Integer.parseInt("0"):new BigDecimal(result.get("qty").toString()).intValue();
	}
	// 先清空表的数据 查出所有的仓库 仓库下的sku 一条一条insert
	public void generateReprot() {
		Set<String> shoplist=null;
  		Calendar calendar=Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date beginDate = calendar.getTime();
		int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		Calendar calendarend=Calendar.getInstance();
		calendarend.set(Calendar.DAY_OF_MONTH, 1);
		Date endDate = calendarend.getTime();
		String month=GeneralUtil.formatDate(beginDate);
		if(shoplist==null) {
			List<Map<String, Object>> shopListAll= this.whseUnsalableReportMapper.findAllShop();
			shoplist=new HashSet<String>();
			for(Map<String, Object> item:shopListAll) {
				String shopid = item.get("shopid").toString();
			    shoplist.add(shopid);
			}
		}
		// 循环 店铺 shopid
		for (String shopid:shoplist) {
			// 获取仓库
			List<Warehouse> selflist = warehouseMapper.findAll(shopid);
			for (int j = 0; j < selflist.size(); j++) {
				Warehouse warehouse = selflist.get(j);
				String warehouseid = warehouse.getId();
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("warehouseid", warehouseid);
				param.put("shopid", shopid);
				param.put("begindate", beginDate);
				param.put("enddate", endDate);
				List<Map<String, Object>> result = inventoryMonthSummaryMapper.findHaveInv(param);
				
				for (int x = 0; x < result.size(); x++) {
					Map<String,Object> material = result.get(x);
			        if(material==null) {
			        	continue;
			        }
			        String materialid=material.get("materialid").toString();
                    InventoryMonthSummary ims = new InventoryMonthSummary();
                    ims.setMaterialid(new BigInteger(materialid));
                    ims.setWarehouseid(new BigInteger(warehouse.getId()));
                    ims.setShopid(new BigInteger(shopid));
                    
                    ims.setMonth(calendar.getTime());
                    ims.setStartqty(getBeginInvMap(shopid,warehouseid,materialid));
                    ims.setEndqty(getEndInvMap(shopid,warehouseid,materialid));
                    
                    ims.setPurchase(getpurchase(shopid,warehouseid,materialid));
                    ims.setShipment(getShip(shopid,warehouseid,materialid)*-1);
                    ims.setOtherin(getIn(shopid,warehouseid,materialid));
                    ims.setOtherout(getOut(shopid,warehouseid,materialid)*-1);
                    ims.setStock(getStock(shopid,warehouseid,materialid));
                    Integer dispathout = getDispathOut(shopid,warehouseid,materialid);
                    Integer dispathIn = getDispathIn(shopid,warehouseid,materialid);
                    Integer changeout = getChangeOut(shopid,warehouseid,materialid);
                    Integer changeIn = getChangeIn(shopid,warehouseid,materialid);
                    
                    ims.setDispatch(dispathIn-dispathout+changeIn-changeout); 
                    
                    Integer assemblyin=this.getAssemblyIn(shopid, warehouseid,materialid);
                    Integer assemblyOut=this.getAssemblyOut(shopid, warehouseid,materialid);
                    
                    ims.setAssembly(assemblyin-assemblyOut);
                    
                    int logicEnd = ims.getStartqty()+ims.getPurchase()+ims.getOtherin()+ims.getShipment()+ims.getOtherout()+ims.getAssembly()+ims.getDispatch()+ims.getStock();
                    
                    ims.setDiff(logicEnd-ims.getEndqty());
                    
                    double subadd = (ims.getStartqty()+ims.getEndqty())/2.0;
                    Integer outinv = ims.getShipment();
                    outinv+=ims.getOtherout();
                    if(ims.getAssembly()<0) {
                    	outinv+=ims.getAssembly();
                    }
                    if(ims.getAssembly()<0) {
                    	outinv+=ims.getDispatch();
                    }
                    outinv=outinv*-1;
                    if(subadd!=0) {
                        ims.setPeriod(new BigDecimal(outinv/subadd));
                        if(ims.getPeriod().floatValue()!=0.0) {
                        	ims.setTurndays(new BigDecimal(days/ims.getPeriod().doubleValue()));
                        }
                    }
          
                    ims.setRefreshtime(new Date());
                    QueryWrapper<InventoryMonthSummary> queryWrapper2=new QueryWrapper<InventoryMonthSummary>();
                    queryWrapper2.eq("materialid", ims.getMaterialid());
                    queryWrapper2.eq("warehouseid", ims.getWarehouseid());
                    queryWrapper2.eq("shopid", shopid);
                    queryWrapper2.eq("month",month);
					inventoryMonthSummaryMapper.delete(queryWrapper2);
                    inventoryMonthSummaryMapper.insert(ims);
				}
			 
			}

		}
	
	}
	@Override
	public IPage<Map<String, Object>> findReport(Page<?> page,Map<String, Object> param) {
		// TODO Auto-generated method stub
		return inventoryMonthSummaryMapper.findReport(page,param);
	}
	
	@Override
	public void setExcelfindReport(SXSSFWorkbook workbook, Map<String, Object> param) {
		// TODO Auto-generated method stub
		Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
		titlemap.put("sku", "sku");
		titlemap.put("name", "产品名称");
		titlemap.put("ownername", "产品负责人");
		titlemap.put("warehousename", "仓库");
		titlemap.put("startqty", "期初");
		titlemap.put("endqty", "期末");
		titlemap.put("purchase", "采购");
		titlemap.put("otherin", "入库");
		titlemap.put("otherout", "出库");
		titlemap.put("shipment", "发货");
		titlemap.put("dispatch", "调库");
		titlemap.put("assembly", "组装");
		titlemap.put("stock", "盘点");
		titlemap.put("diff", "库存差异");
		titlemap.put("period", "周转个数");
		titlemap.put("turndays", "周转天数");
		
		List<Map<String, Object>> list = inventoryMonthSummaryMapper.findReport(param);
		Sheet sheet = workbook.createSheet("sheet1");
		// 在索引0的位置创建行（最顶端的行）
		Row trow = sheet.createRow(0);
		Object[] titlearray = titlemap.keySet().toArray();
		for (int i = 0; i < titlearray.length; i++) {
			Cell cell = trow.createCell(i); // 在索引0的位置创建单元格(左上端)
			Object value = titlemap.get(titlearray[i].toString());
			cell.setCellValue(value.toString());
		}
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Row row = sheet.createRow(i + 1);
				Map<String, Object> map = list.get(i);
				for (int j = 0; j < titlearray.length; j++) {
					Cell cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
					String key = titlearray[j].toString();
					Object value = map.get(key);
					if (value != null) {
						cell.setCellValue(value.toString());
					}
				}
			}
		}
	}



}
