package com.wimoor.erp.warehouse.service.impl;

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
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
 

 
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
		try {
			String today = GeneralUtil.formatDate(new Date());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			// 预计算各天数的日期，避免循环内重复计算
			String fromDate30 = GeneralUtil.formatDate(initParamDate(-30), sdf);
			String fromDate60 = GeneralUtil.formatDate(initParamDate(-60), sdf);
			String fromDate90 = GeneralUtil.formatDate(initParamDate(-90), sdf);
			String fromDate180 = GeneralUtil.formatDate(initParamDate(-180), sdf);
			String fromDate365 = GeneralUtil.formatDate(initParamDate(-365), sdf);
			String byday30 = fromDate30;
			String byday60 = fromDate60;
			String byday90 = fromDate90;
			String byday180 = fromDate180;
			String byday365 = fromDate365;

			List<Map<String, Object>> shoplist = this.baseMapper.findAllShop();
			for (int i = 0; i < shoplist.size(); i++) {
				System.out.println(shoplist.get(i)+""+new Date());
				try {
					String shopid = shoplist.get(i).get("shopid").toString();
					LambdaQueryWrapper<Warehouse> wquery = new LambdaQueryWrapper<Warehouse>();
					wquery.eq(Warehouse::getShopid, shopid);
					wquery.eq(Warehouse::getDisabled, false);
					wquery.and(wrapper -> {
						wrapper.or().eq(Warehouse::getFtype, "self_test");
						wrapper.or().eq(Warehouse::getFtype, "self_usable");
					});
					List<Warehouse> selflist = iWarehouseService.list(wquery);

					for (int j = 0; j < selflist.size(); j++) {
						Warehouse warehouse = selflist.get(j);
						String warehouseid = warehouse.getId();
						// 删除旧数据
						LambdaQueryWrapper<WhseUnsalableReport> dquery = new LambdaQueryWrapper<WhseUnsalableReport>();
						dquery.eq(WhseUnsalableReport::getShopid, shopid);
						dquery.eq(WhseUnsalableReport::getWid, warehouseid);
						this.baseMapper.delete(dquery);

						// 获取当前库存
						Map<String, Object> param = new HashMap<String, Object>();
						param.put("warehouseid", warehouseid);
						param.put("shopid", shopid);
						List<Map<String, Object>> materialList = this.baseMapper.selectLocalInventory(param);
						if (materialList == null || materialList.isEmpty()) {
							continue;
						}

						// 收集有效 materialid，构建库存映射
						List<String> materialIds = new ArrayList<>();
						Map<String, BigDecimal> invnumMap = new HashMap<>();
						for (Map<String, Object> item : materialList) {
							String materialid = item.get("materialid").toString();
							BigDecimal invnum = new BigDecimal(item.get("qtysum").toString());
							if (invnum.intValue() > 0) {
								materialIds.add(materialid);
								invnumMap.put(materialid, invnum);
							}
						}
						if (materialIds.isEmpty()) {
							continue;
						}

						// 批量加载 material 信息（1次查询替代N次）
						Map<String, Material> materialMap = new HashMap<>();
						List<Material> materials = materialService.listByIds(materialIds);
						for (Material m : materials) {
							materialMap.put(m.getId(), m);
						}

						// 批量加载出库量数据 ship（3次查询替代 3*N 次）
						Map<String, BigDecimal> ship30Map = batchGetShipMap(shopid, warehouseid, fromDate30);
						Map<String, BigDecimal> ship60Map = batchGetShipMap(shopid, warehouseid, fromDate60);
						Map<String, BigDecimal> ship90Map = batchGetShipMap(shopid, warehouseid, fromDate90);

						// 批量加载历史库存数据 inv（3次查询替代 3*N 次）
						Map<String, BigDecimal> inv30Map = batchGetInvMap(shopid, warehouseid, byday30);
						Map<String, BigDecimal> inv60Map = batchGetInvMap(shopid, warehouseid, byday60);
						Map<String, BigDecimal> inv90Map = batchGetInvMap(shopid, warehouseid, byday90);

						// 按需加载 180 天数据
						Map<String, BigDecimal> inv180Map = new HashMap<>();
						Map<String, BigDecimal> ship180Map = new HashMap<>();
						boolean need180 = false;
						for (String mid : materialIds) {
							BigDecimal num90 = inv90Map.getOrDefault(mid, BigDecimal.ZERO);
							BigDecimal ship90 = ship90Map.getOrDefault(mid, BigDecimal.ZERO);
							if (num90.intValue() - ship90.intValue() > 0) {
								need180 = true;
								break;
							}
						}
						if (need180) {
							inv180Map = batchGetInvMap(shopid, warehouseid, byday180);
							ship180Map = batchGetShipMap(shopid, warehouseid, fromDate180);
						}

						// 按需加载 365 天数据
						Map<String, BigDecimal> inv365Map = new HashMap<>();
						Map<String, BigDecimal> ship365Map = new HashMap<>();
						boolean need365 = false;
						if (need180) {
							for (String mid : materialIds) {
								BigDecimal num180 = inv180Map.getOrDefault(mid, BigDecimal.ZERO);
								BigDecimal ship180 = ship180Map.getOrDefault(mid, BigDecimal.ZERO);
								if (num180.intValue() - ship180.intValue() > 0) {
									need365 = true;
									break;
								}
							}
						}
						if (need365) {
							inv365Map = batchGetInvMap(shopid, warehouseid, byday365);
							ship365Map = batchGetShipMap(shopid, warehouseid, fromDate365);
						}

						// 组装报表数据并批量插入
						List<WhseUnsalableReport> reportList = new ArrayList<>();
						for (String materialid : materialIds) {
							Material material = materialMap.get(materialid);
							if (material == null) {
								continue;
							}
							BigDecimal invnum = invnumMap.get(materialid);
							BigDecimal ship30 = ship30Map.getOrDefault(materialid, BigDecimal.ZERO);
							BigDecimal ship60 = ship60Map.getOrDefault(materialid, BigDecimal.ZERO);
							BigDecimal ship90 = ship90Map.getOrDefault(materialid, BigDecimal.ZERO);
							BigDecimal num30 = inv30Map.getOrDefault(materialid, BigDecimal.ZERO);
							BigDecimal num60 = BigDecimal.ZERO;
							BigDecimal num90 = BigDecimal.ZERO;
							BigDecimal num180 = BigDecimal.ZERO;
							BigDecimal num365 = BigDecimal.ZERO;
							BigDecimal ship180 = BigDecimal.ZERO;
							BigDecimal ship365 = BigDecimal.ZERO;

							if (num30.intValue() - ship30.intValue() > 0) {
								num60 = inv60Map.getOrDefault(materialid, BigDecimal.ZERO);
							}
							if (num60.intValue() - ship60.intValue() > 0) {
								num90 = inv90Map.getOrDefault(materialid, BigDecimal.ZERO);
							}
							if (num90.intValue() - ship90.intValue() > 0) {
								num180 = inv180Map.getOrDefault(materialid, BigDecimal.ZERO);
								ship180 = ship180Map.getOrDefault(materialid, BigDecimal.ZERO);
							}
							if (num180.intValue() - ship180.intValue() > 0) {
								num365 = inv365Map.getOrDefault(materialid, BigDecimal.ZERO);
								ship365 = ship365Map.getOrDefault(materialid, BigDecimal.ZERO);
							}

							WhseUnsalableReport whse = new WhseUnsalableReport();
							whse.setMtid(materialid);
							whse.setSku(material.getSku());
							whse.setGroupid("all");
							whse.setName(today);
							whse.setWid(warehouse.getId());
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
							reportList.add(whse);
						}

						// 批量插入（1次调用替代N次insert）
						if (!reportList.isEmpty()) {
							this.saveBatch(reportList);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (shoplist != null) {
				for (Map<String, Object> item : shoplist) {
					item.clear();
				}
				shoplist.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void generateOverseaReprot() {
		try {
			String today = GeneralUtil.formatDate(new Date());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String fromDate30 = GeneralUtil.formatDate(initParamDate(-30), sdf);
			String fromDate60 = GeneralUtil.formatDate(initParamDate(-60), sdf);
			String fromDate90 = GeneralUtil.formatDate(initParamDate(-90), sdf);
			String fromDate180 = GeneralUtil.formatDate(initParamDate(-180), sdf);
			String fromDate365 = GeneralUtil.formatDate(initParamDate(-365), sdf);
			String byday30 = fromDate30;
			String byday60 = fromDate60;
			String byday90 = fromDate90;
			String byday180 = fromDate180;
			String byday365 = fromDate365;

			List<Map<String, Object>> shoplist = this.baseMapper.findAllShop();
			for (int i = 0; i < shoplist.size(); i++) {
				try {
					String shopid = shoplist.get(i).get("shopid").toString();
					LambdaQueryWrapper<Warehouse> wquery = new LambdaQueryWrapper<Warehouse>();
					wquery.eq(Warehouse::getShopid, shopid);
					wquery.eq(Warehouse::getDisabled, false);
					wquery.and(wrapper -> {
						wrapper.or().eq(Warehouse::getFtype, "oversea_usable");
					});
					List<Warehouse> selflist = iWarehouseService.list(wquery);

					for (int j = 0; j < selflist.size(); j++) {
						Warehouse warehouse = selflist.get(j);
						String warehouseid = warehouse.getId();
						LambdaQueryWrapper<WhseUnsalableReport> dquery = new LambdaQueryWrapper<WhseUnsalableReport>();
						dquery.eq(WhseUnsalableReport::getShopid, shopid);
						dquery.eq(WhseUnsalableReport::getWid, warehouseid);
						this.baseMapper.delete(dquery);

						Map<String, Object> param = new HashMap<String, Object>();
						param.put("warehouseid", warehouseid);
						param.put("shopid", shopid);
						List<Map<String, Object>> materialList = this.baseMapper.selectLocalInventory(param);
						if (materialList == null || materialList.isEmpty()) {
							continue;
						}

						List<String> materialIds = new ArrayList<>();
						Map<String, BigDecimal> invnumMap = new HashMap<>();
						for (Map<String, Object> item : materialList) {
							String materialid = item.get("materialid").toString();
							BigDecimal invnum = new BigDecimal(item.get("qtysum").toString());
							if (invnum.intValue() > 0) {
								materialIds.add(materialid);
								invnumMap.put(materialid, invnum);
							}
						}
						if (materialIds.isEmpty()) {
							continue;
						}

						Map<String, Material> materialMap = new HashMap<>();
						List<Material> materials = materialService.listByIds(materialIds);
						for (Material m : materials) {
							materialMap.put(m.getId(), m);
						}

						// 海外仓出库量批量查询
						Map<String, BigDecimal> ship30Map = batchGetOverseaShipMap(shopid, warehouseid, fromDate30);
						Map<String, BigDecimal> ship60Map = batchGetOverseaShipMap(shopid, warehouseid, fromDate60);
						Map<String, BigDecimal> ship90Map = batchGetOverseaShipMap(shopid, warehouseid, fromDate90);

						// 历史库存批量查询
						Map<String, BigDecimal> inv30Map = batchGetInvMap(shopid, warehouseid, byday30);
						Map<String, BigDecimal> inv60Map = batchGetInvMap(shopid, warehouseid, byday60);
						Map<String, BigDecimal> inv90Map = batchGetInvMap(shopid, warehouseid, byday90);

						// 按需加载 180 天（注意：原代码 180/365 用的是 getShipMap 而非 getOverseaShipMap）
						Map<String, BigDecimal> inv180Map = new HashMap<>();
						Map<String, BigDecimal> ship180Map = new HashMap<>();
						boolean need180 = false;
						for (String mid : materialIds) {
							BigDecimal num90 = inv90Map.getOrDefault(mid, BigDecimal.ZERO);
							BigDecimal ship90 = ship90Map.getOrDefault(mid, BigDecimal.ZERO);
							if (num90.intValue() - ship90.intValue() > 0) {
								need180 = true;
								break;
							}
						}
						if (need180) {
							inv180Map = batchGetInvMap(shopid, warehouseid, byday180);
							ship180Map = batchGetShipMap(shopid, warehouseid, fromDate180);
						}

						Map<String, BigDecimal> inv365Map = new HashMap<>();
						Map<String, BigDecimal> ship365Map = new HashMap<>();
						boolean need365 = false;
						if (need180) {
							for (String mid : materialIds) {
								BigDecimal num180 = inv180Map.getOrDefault(mid, BigDecimal.ZERO);
								BigDecimal ship180 = ship180Map.getOrDefault(mid, BigDecimal.ZERO);
								if (num180.intValue() - ship180.intValue() > 0) {
									need365 = true;
									break;
								}
							}
						}
						if (need365) {
							inv365Map = batchGetInvMap(shopid, warehouseid, byday365);
							ship365Map = batchGetShipMap(shopid, warehouseid, fromDate365);
						}

						List<WhseUnsalableReport> reportList = new ArrayList<>();
						for (String materialid : materialIds) {
							Material material = materialMap.get(materialid);
							if (material == null) {
								continue;
							}
							BigDecimal invnum = invnumMap.get(materialid);
							BigDecimal ship30 = ship30Map.getOrDefault(materialid, BigDecimal.ZERO);
							BigDecimal ship60 = ship60Map.getOrDefault(materialid, BigDecimal.ZERO);
							BigDecimal ship90 = ship90Map.getOrDefault(materialid, BigDecimal.ZERO);
							BigDecimal num30 = inv30Map.getOrDefault(materialid, BigDecimal.ZERO);
							BigDecimal num60 = BigDecimal.ZERO;
							BigDecimal num90 = BigDecimal.ZERO;
							BigDecimal num180 = BigDecimal.ZERO;
							BigDecimal num365 = BigDecimal.ZERO;
							BigDecimal ship180 = BigDecimal.ZERO;
							BigDecimal ship365 = BigDecimal.ZERO;

							if (num30.intValue() - ship30.intValue() > 0) {
								num60 = inv60Map.getOrDefault(materialid, BigDecimal.ZERO);
							}
							if (num60.intValue() - ship60.intValue() > 0) {
								num90 = inv90Map.getOrDefault(materialid, BigDecimal.ZERO);
							}
							if (num90.intValue() - ship90.intValue() > 0) {
								num180 = inv180Map.getOrDefault(materialid, BigDecimal.ZERO);
								ship180 = ship180Map.getOrDefault(materialid, BigDecimal.ZERO);
							}
							if (num180.intValue() - ship180.intValue() > 0) {
								num365 = inv365Map.getOrDefault(materialid, BigDecimal.ZERO);
								ship365 = ship365Map.getOrDefault(materialid, BigDecimal.ZERO);
							}

							WhseUnsalableReport whse = new WhseUnsalableReport();
							whse.setMtid(materialid);
							whse.setSku(material.getSku());
							whse.setGroupid("all");
							whse.setName(today);
							whse.setWid(warehouse.getId());
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
							reportList.add(whse);
						}

						if (!reportList.isEmpty()) {
							this.saveBatch(reportList);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (shoplist != null) {
				for (Map<String, Object> item : shoplist) {
					item.clear();
				}
				shoplist.clear();
			}
		} catch (Exception e) {
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

		private BigDecimal getOverseaShipMap(String shopid, String warehouseid,String materialid, int days) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> param = new HashMap<String, Object>();
		Date date = new Date();
		param.put("toDate", GeneralUtil.formatDate(date, sdf));
		param.put("warehouseid", warehouseid);
		param.put("shopid", shopid);
		param.put("materialid", materialid);
		String fromDate = GeneralUtil.formatDate(initParamDate(-1*days), sdf);
		param.put("fromDate", fromDate);
		  Map<String, Object> result = this.baseMapper.OverseaOutInventoryByRange(param);
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

	/** 批量查询某仓库所有物料在指定日期范围内的出库量，返回 materialid -> salesum 映射 */
	private Map<String, BigDecimal> batchGetShipMap(String shopid, String warehouseid, String fromDate) {
		Map<String, Object> param = new HashMap<>();
		param.put("shopid", shopid);
		param.put("warehouseid", warehouseid);
		param.put("fromDate", fromDate);
		List<Map<String, Object>> results = this.baseMapper.batchLocalOutInventoryByRange(param);
		Map<String, BigDecimal> map = new HashMap<>();
		if (results != null) {
			for (Map<String, Object> row : results) {
				String materialid = row.get("materialid").toString();
				BigDecimal salesum = row.get("salesum") == null ? BigDecimal.ZERO : new BigDecimal(row.get("salesum").toString());
				map.put(materialid, salesum);
			}
		}
		return map;
	}

	/** 批量查询海外仓某仓库所有物料在指定日期范围内的出库量 */
	private Map<String, BigDecimal> batchGetOverseaShipMap(String shopid, String warehouseid, String fromDate) {
		Map<String, Object> param = new HashMap<>();
		param.put("shopid", shopid);
		param.put("warehouseid", warehouseid);
		param.put("fromDate", fromDate);
		List<Map<String, Object>> results = this.baseMapper.batchOverseaOutInventoryByRange(param);
		Map<String, BigDecimal> map = new HashMap<>();
		if (results != null) {
			for (Map<String, Object> row : results) {
				if (row.get("materialid") == null) continue;
				String materialid = row.get("materialid").toString();
				BigDecimal salesum = row.get("salesum") == null ? BigDecimal.ZERO : new BigDecimal(row.get("salesum").toString());
				map.put(materialid, salesum);
			}
		}
		return map;
	}

	/** 批量查询某仓库所有物料在指定日期的历史库存，返回 materialid -> invqty 映射 */
	private Map<String, BigDecimal> batchGetInvMap(String shopid, String warehouseid, String byday) {
		Map<String, Object> param = new HashMap<>();
		param.put("shopid", new BigInteger(shopid));
		param.put("warehouseid", new BigInteger(warehouseid));
		param.put("byday", byday);
		List<Map<String, Object>> results = this.baseMapper.batchLocalByDay(param);
		Map<String, BigDecimal> map = new HashMap<>();
		if (results != null) {
			for (Map<String, Object> row : results) {
				String materialid = row.get("materialid").toString();
				BigDecimal invqty = row.get("invqty") == null ? BigDecimal.ZERO : new BigDecimal(row.get("invqty").toString());
				map.put(materialid, invqty);
			}
		}
		return map;
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
