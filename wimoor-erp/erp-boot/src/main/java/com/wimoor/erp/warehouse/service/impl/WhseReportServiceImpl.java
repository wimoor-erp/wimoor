package com.wimoor.erp.warehouse.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.map.LinkedMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.erp.inventory.service.IInventoryMonthSummaryService;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.warehouse.mapper.WhseUnsalableReportMapper;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.pojo.entity.WhseUnsalableReport;
import com.wimoor.erp.warehouse.service.IWarehouseService;
import com.wimoor.erp.warehouse.service.IWhseReportService;

import lombok.RequiredArgsConstructor;
 

 
@Service("whseReportService")
@RequiredArgsConstructor
public class WhseReportServiceImpl extends  ServiceImpl<WhseUnsalableReportMapper,WhseUnsalableReport> implements IWhseReportService {
	 
	 final IInventoryMonthSummaryService inventoryMonthSummaryService;
	 final IMaterialService materialService;
	 final IWarehouseService iWarehouseService;
	 
	public Map<String, Object> initShipData(List<Map<String, Object>> list) {
		if (list.size() > 0 && list != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < list.size(); i++) {
				String materialid = list.get(i).get("materialid").toString();
				String qty = list.get(i).get("salesum").toString();
				if (GeneralUtil.isNotEmpty(materialid)) {
					map.put(materialid, qty);
				}
			}
			return map;
		} else {
			return null;
		}
	}

	public Map<String, Object> initInvData(List<Map<String, Object>> list) {
		if (list.size() > 0 && list != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < list.size(); i++) {
				String materialid = list.get(i).get("materialid").toString();
				String qty = list.get(i).get("qty").toString();
				if (GeneralUtil.isNotEmpty(materialid)) {
					map.put(materialid, qty);
				}
			}
			return map;
		} else {
			return null;
		}
	}

	public Date initParamDate(Integer num) {
		Calendar c = Calendar.getInstance();
		if (num != null) {
			c.add(Calendar.DATE, num);
			Date date = c.getTime();
			return date;
		} else {
			return null;
		}
	}
 

	public void findLocalInvDead(SXSSFWorkbook workbook, Map<String, Object> params) {
		List<LinkedHashMap<String, Object>> list = this.baseMapper.getLocalDeadRpt(params);
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> item = list.get(i);
			BigDecimal numnow = (BigDecimal) item.get("0~30天库龄");
			BigDecimal num30 = (BigDecimal) item.get("30~60天库龄");
			BigDecimal num60 = (BigDecimal) item.get("60~90天库龄");
			BigDecimal num90 = (BigDecimal) item.get("90~180天库龄");
			BigDecimal num180 = (BigDecimal) item.get("180~365天库龄");
			BigDecimal num365 = (BigDecimal) item.get("365天以上库龄");
			item.put("0~30天库龄", numnow.floatValue() >= num30.floatValue() ? numnow.subtract(num30) : numnow);
			item.put("30~60天库龄", num30.floatValue() >= num60.floatValue() ? num30.subtract(num60) : num30);
			item.put("60~90天库龄", num60.floatValue() >= num90.floatValue() ? num60.subtract(num90) : num60);
			item.put("90~180天库龄", num90.floatValue() >= num180.floatValue() ? num90.subtract(num180) : num90);
			item.put("180~365天库龄", num180.floatValue() >= num365.floatValue() ? num180.subtract(num365) : num180);
		}
		Sheet sheet = workbook.createSheet("sheet1");
		// 在索引0的位置创建行（最顶端的行）
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			if (i == 0) {
				int titlestep = 0;
				Row trow = sheet.createRow(i);
				for (Entry<String, Object> entry : map.entrySet()) {
					String key = entry.getKey();
					Cell titlecell = trow.createCell(titlestep++);
					if (key.contains(",")) {
						key = key.split(",")[1];
					}
					titlecell.setCellValue(key);
				}
			}
			Row row = sheet.createRow(i + 1);
			int step = 0;
			for (Entry<String, Object> entry : map.entrySet()) {
				Cell cell = row.createCell(step++);
				Object key = entry.getValue();
				if (key != null) {
					cell.setCellValue(key.toString());
				} else {
					cell.setCellValue("--");
				}
			}
		}
	}

 
	@Override
	public IPage<Map<String, Object>> findUnsalableReportByCondition(Page<?> page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		IPage<Map<String, Object>> pagelist = this.baseMapper.findByCondition(page,param);
		if(page.getCurrent()==1) {
			Map<String, Object> map = this.baseMapper.findPageTotalSum(param);
			if (pagelist!=null&&pagelist.getRecords().size() > 0) {
				BigDecimal totalqty = (BigDecimal) map.get("totalqty");
				BigDecimal totalqtysum30 = (BigDecimal) map.get("totalqtysum30");
				BigDecimal totalqtysum60 = (BigDecimal) map.get("totalqtysum60");
				BigDecimal totalqtysum90 = (BigDecimal) map.get("totalqtysum90");
				BigDecimal totalqtysum180 = (BigDecimal) map.get("totalqtysum180");
				BigDecimal totalqtysum365 = (BigDecimal) map.get("totalqtysum365");
				Map<String, Object> summary =new HashMap<String,Object>();
				summary.put("allqtysum", map.get("totalqty"));
				summary.put("alluseqty", map.get("totaluseqty"));
				summary.put("allqtysum0", totalqty.floatValue()>=totalqtysum30.floatValue()?totalqty.subtract(totalqtysum30):totalqty);
				summary.put("allqtysum30", totalqtysum30.floatValue()>=totalqtysum60.floatValue()?totalqtysum30.subtract(totalqtysum60):totalqtysum30);
				summary.put("allqtysum60", totalqtysum60.floatValue()>=totalqtysum90.floatValue()?totalqtysum60.subtract(totalqtysum90):totalqtysum60);
				summary.put("allqtysum90", totalqtysum90.floatValue()>=totalqtysum180.floatValue()?totalqtysum90.subtract(totalqtysum180):totalqtysum90);
				summary.put("allqtysum180", totalqtysum180.floatValue()>=totalqtysum365.floatValue()?totalqtysum180.subtract(totalqtysum365):totalqtysum180);
				summary.put("allqtysum365", map.get("totalqtysum365"));
				summary.put("allsalesum30", map.get("totalsales30"));
				summary.put("allsalesum60", map.get("totalsales60"));
				summary.put("allsalesum90", map.get("totalsales90"));
				summary.put("allsalesum180", map.get("totalsales180"));
				summary.put("allsalesum365", map.get("totalsales365"));
				summary.put("allnowinv90", map.get("totalnowinv"));
				summary.put("allnostock", map.get("totalnostock"));
				pagelist.getRecords().get(0).put("summary", summary);
			}
		}
		return pagelist;
	}

	 
 
	@Override
	public void generateReprot() {
		// TODO Auto-generated method stub
			try {

		        String today = GeneralUtil.formatDate(new Date());
				List<Map<String, Object>> shoplist = this.baseMapper.findAllShop();
				// 循环 店铺 shopid
				for (int i = 0; i < shoplist.size(); i++) {
					String shopid = shoplist.get(i).get("shopid").toString();
					// 获取仓库
					LambdaQueryWrapper<Warehouse> wquery=new 	LambdaQueryWrapper<Warehouse>();
					wquery.eq(Warehouse::getShopid, shopid);
					wquery.eq(Warehouse::getDisabled, false);
					wquery.and(wrapper->{
						wrapper.or().eq(Warehouse::getFtype, "self_test");
						wrapper.or().eq(Warehouse::getFtype, "self_usable");
					});
					List<Warehouse> selflist = iWarehouseService.list(wquery);
					LambdaQueryWrapper<WhseUnsalableReport> dquery=new 	LambdaQueryWrapper<WhseUnsalableReport>();
					dquery.eq(WhseUnsalableReport::getShopid, shopid);
					this.baseMapper.delete(dquery);
		 
					for (int j = 0; j < selflist.size(); j++) {
						Warehouse warehouse = selflist.get(j);
						String warehouseid = warehouse.getId();
						Map<String, Object> param = new HashMap<String, Object>();
						param.put("warehouseid", warehouseid);
						param.put("shopid", shopid);
						List<Map<String, Object>> materialList = this.baseMapper.selectLocalInventory(param);
						for (int x = 0; x < materialList.size(); x++) {
							String materialid =  materialList.get(x).get("materialid").toString();
							Material material = materialService.getById(materialid);
					        if(material==null) {
					        	continue;
					        }
							BigDecimal invnum=new BigDecimal("0");
							BigDecimal num30=new BigDecimal("0");
							BigDecimal num60=new BigDecimal("0");
							BigDecimal num90=new BigDecimal("0");
							BigDecimal num180=new BigDecimal("0");
							BigDecimal num365=new BigDecimal("0");
							BigDecimal ship30=new BigDecimal("0");
							BigDecimal ship60=new BigDecimal("0");
							BigDecimal ship90=new BigDecimal("0");
							BigDecimal ship180=new BigDecimal("0");
							BigDecimal ship365=new BigDecimal("0");
				            invnum=new BigDecimal(materialList.get(x).get("qtysum").toString());
				            if(invnum.intValue()==0) {
				            	continue;
				            }
				            
				            ship30 = getShipMap(shopid, warehouseid,materialid, 30);
				            ship60 = getShipMap(shopid, warehouseid,materialid, 60);
				            ship90 = getShipMap(shopid, warehouseid,materialid, 90);
				            
				      
				            num30 = getInvMap(shopid, warehouseid,materialid, 30);
				            if(num30.intValue()-ship30.intValue()>0) {
				            	num60 = getInvMap(shopid, warehouseid,materialid, 60);
				            }
				            if(num60.intValue()-ship60.intValue()>0) {
				            	num90 = getInvMap(shopid, warehouseid,materialid, 90);
				            }
				            if(num90.intValue()-ship90.intValue()>0) {
				            	num180 = getInvMap(shopid, warehouseid,materialid, 180);
				            	ship180 = getShipMap(shopid, warehouseid,materialid, 180);
				            }
				            if(num180.intValue()-ship180.intValue()>0) {
				            	num365 = getInvMap(shopid, warehouseid,materialid, 365);
				            	ship365 = getShipMap(shopid, warehouseid,materialid, 365);
				            }
							// 自有仓--出库量 fromdate-->endDate 数据格式("KQ012",508)
			
		 
							WhseUnsalableReport whse = new WhseUnsalableReport();
							whse.setMtid(materialid);
							whse.setSku(material.getSku());
							whse.setGroupid("all");
							whse.setName(today);
							whse.setWid(warehouse.getParentid());
							whse.setShopid(shopid);
							whse.setQtysum(invnum);
							whse.setQtysum30(num30);
							whse.setQtysum60(num60);
							whse.setQtysum90(num90);
							whse.setQtysum180(num180);
							whse.setQtysum365(num365);
							whse.setSalesum30(ship30);
							whse.setSalesum60(ship60);
							whse.setSalesum90(ship90);
							whse.setSalesum180(ship180);
							whse.setSalesum365(ship365);
							LambdaQueryWrapper<WhseUnsalableReport> keyquery=new LambdaQueryWrapper<WhseUnsalableReport>();
							keyquery.eq(WhseUnsalableReport::getShopid, whse.getShopid());
							keyquery.eq(WhseUnsalableReport::getWid, whse.getWid());
							keyquery.eq(WhseUnsalableReport::getMtid, whse.getMtid());
							keyquery.eq(WhseUnsalableReport::getSku, whse.getSku());
							keyquery.eq(WhseUnsalableReport::getGroupid, whse.getGroupid());
							WhseUnsalableReport oldone = this.baseMapper.selectOne(keyquery);
							if(oldone!=null) {
								whse.setQtysum(invnum.add(oldone.getQtysum()));
								whse.setQtysum30(num30.add(oldone.getQtysum30()));
								whse.setQtysum60(num60.add(oldone.getQtysum60()));
								whse.setQtysum90(num90.add(oldone.getQtysum90()));
								whse.setQtysum180(num180.add(oldone.getQtysum180()));
								whse.setQtysum365(num365.add(oldone.getQtysum365()));
								whse.setSalesum30(ship30.add(oldone.getSalesum30()));
								whse.setSalesum60(ship60.add(oldone.getSalesum60()));
								whse.setSalesum90(ship90.add(oldone.getSalesum90()));
								whse.setSalesum180(ship180.add(oldone.getSalesum180()));
								whse.setSalesum365(ship365.add(oldone.getSalesum365()));
								this.baseMapper.update(whse,keyquery);
							}else {
								this.baseMapper.insert(whse);
							}
						}
						 
					 
					}

				}
			
				if(shoplist!=null) {
					for(Map<String, Object> item:shoplist) {
						item.clear();
					}
					shoplist.clear();
					shoplist=null;
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
	}

	
	@Override
	public List<Map<String, Object>> findUnsalableReportByDay( Map<String, Object> param) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> pagelist = this.baseMapper.findByDay(param);
		return pagelist;
	}

	
	private BigDecimal getShipMap(String shopid, String warehouseid,String materialid, int days) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> param = new HashMap<String, Object>();
		Date date = new Date();
		param.put("toDate", GeneralUtil.formatDate(date, sdf));
		param.put("warehouseid", warehouseid);
		param.put("shopid", shopid);
		param.put("materialid", materialid);
		String fromDate = GeneralUtil.formatDate(initParamDate(-1*days), sdf);
		param.put("fromDate", fromDate);
		  Map<String, Object> result = this.baseMapper.localOutInventoryByRange(param);
	    return result==null||result.get("salesum")==null?new BigDecimal("0"):new BigDecimal(result.get("salesum").toString());
	}

	private BigDecimal getInvMap(String shopid, String warehouseid,String materialid, int days) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shopid", new BigInteger(shopid));
		param.put("warehouseid", new BigInteger(warehouseid));
		param.put("materialid", new BigInteger(materialid));
		String fromDate = GeneralUtil.formatDate(initParamDate(-1*days), sdf);
		param.put("byday",GeneralUtil.getDatez(fromDate));
		Map<String, Object> result = this.baseMapper.localByDay(param);
	    return result==null||result.get("invqty")==null?new BigDecimal("0"):new BigDecimal(result.get("invqty").toString());
	 
	}

	@Override
	public void setUnsalableReportByDayExcel(SXSSFWorkbook workbook, List<Map<String, Object>> list) {
		// TODO Auto-generated method stub
		Sheet sheet = workbook.createSheet("sheet1");
		// 在索引0的位置创建行（最顶端的行）
		Map<String, String> titlechange = new LinkedMap<String, String>();
		titlechange.put("sku", "SKU");
		titlechange.put("pname", "名称");
		titlechange.put("warehouse", "本地仓库");
		titlechange.put("inbound", "待入库");
		titlechange.put("fulfillable", "可用");
		titlechange.put("outbound", "待出库");
		titlechange.put("outbound", "待出库");
		titlechange.put("qtyx","超长库存数量");
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			if (i == 0) {
				int titlestep = 0;
				Row trow = sheet.createRow(i);
				for (Entry<String, String> entry : titlechange.entrySet()) {
					String value = entry.getValue();
					Cell titlecell = trow.createCell(titlestep++);
					titlecell.setCellValue(value);
				}
			}
			Row row = sheet.createRow(i + 1);
			int step = 0;
			for (Entry<String, String> entry : titlechange.entrySet()) {
				Cell cell = row.createCell(step++);
				Object value = map.get(entry.getKey());
				if (value != null) {
					cell.setCellValue(value.toString());
				} else {
					cell.setCellValue("--");
				}
			}
		}
	}
}
