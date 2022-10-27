package com.wimoor.amazon.finances.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Resource;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.googlecode.aviator.AviatorEvaluator;
import com.wimoor.amazon.finances.mapper.AmzFinSettlementFormulaMapper;
import com.wimoor.amazon.finances.mapper.AmzFinUserItemDataMapper;
import com.wimoor.amazon.finances.mapper.AmzFinUserItemMapper;
import com.wimoor.amazon.finances.pojo.entity.AmzFinSettlementFormula;
import com.wimoor.amazon.finances.pojo.entity.AmzFinUserItem;
import com.wimoor.amazon.finances.pojo.entity.AmzFinUserItemData;
import com.wimoor.amazon.finances.service.IAmzFinConfigService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import cn.hutool.core.util.StrUtil;
 

@Service("amzFinConfigService")
public class AmzFinConfigServiceImpl extends  ServiceImpl<AmzFinUserItemMapper, AmzFinUserItem> implements IAmzFinConfigService {
	@Resource
	AmzFinUserItemMapper amzFinUserItemMapper;
	@Resource
	AmzFinUserItemDataMapper amzFinUserItemDataMapper;
	@Resource
	AmzFinSettlementFormulaMapper amzFinSettlementFormulaMapper;

	public IPage<Map<String, Object>> findFinListByShopid(Page<?>page,String shopid) {
		return amzFinUserItemMapper.findFinListByShopid(page,shopid);
	}

	public List<Map<String, Object>> findFinListByShopidNoPage(String shopid) {
		return amzFinUserItemMapper.findFinListByShopid(shopid);
	}

	@CacheEvict(value = { "findSettlementSKUCache" }, allEntries = true)
	public void saveFinItemData(List<AmzFinUserItemData> amzFinUserItemDataList) {
		if (amzFinUserItemDataList != null && amzFinUserItemDataList.size() > 0) {
			Map<String, AmzFinUserItemData> map = new HashMap<String, AmzFinUserItemData>();
			for (AmzFinUserItemData item : amzFinUserItemDataList) {
				String key = GeneralUtil.formatDate(item.getByday());
				key = key + item.getGroupid();
				key = key + item.getMarketplaceid();
				key = key + item.getItemid();
				key = key + item.getSku();
				key = key + item.getCurrency();
				if (map.containsKey(key)) {
					AmzFinUserItemData oldone = map.get(key);
					oldone.setAmount(item.getAmount().add(oldone.getAmount()));
					map.put(key, oldone);
				} else {
					map.put(key, item);
				}
			}
			List<AmzFinUserItemData> insertList = new ArrayList<AmzFinUserItemData>();
			for (Entry<String, AmzFinUserItemData> entry : map.entrySet()) {
				insertList.add(entry.getValue());
			}
			amzFinUserItemDataMapper.insertBatch(insertList);
		}
	}

	@CacheEvict(value = { "findSettlementSKUCache" }, allEntries = true)
	public int saveFinItemData(Map<String, Object> params) {
		UserInfo user = (UserInfo) params.get("user");
		Object id = params.get("id");
		String marketplaceid = (String) params.get("marketplaceid");
		String sku = (String) params.get("sku");
		String groupid = (String) params.get("groupid");
		String itemid = (String) params.get("itemid");
		String currency = (String) params.get("currency");
		Date byday = GeneralUtil.getDatez(params.get("byday").toString());
		BigDecimal amount = (BigDecimal) params.get("amount");
		AmzFinUserItemData amzFinUserItemData = null;
		if(id != null && StrUtil.isNotEmpty(id.toString())) {
			amzFinUserItemData = amzFinUserItemDataMapper.selectById(id.toString());
		}else {
			amzFinUserItemData = new AmzFinUserItemData();
		}
		amzFinUserItemData.setMarketplaceid(marketplaceid);
		amzFinUserItemData.setSku(sku);
		amzFinUserItemData.setShopid(user.getCompanyid());
		amzFinUserItemData.setByday(byday);
		amzFinUserItemData.setItemid(itemid);
		amzFinUserItemData.setCurrency(currency);
		amzFinUserItemData.setGroupid(groupid);
		amzFinUserItemData.setAmount(amount);
		amzFinUserItemData.setOperator(user.getId());
		amzFinUserItemData.setOpttime(new Date());
 
		LambdaQueryWrapper<AmzFinUserItemData> queryWrapper=new LambdaQueryWrapper<AmzFinUserItemData>();
		queryWrapper.eq(AmzFinUserItemData::getItemid, itemid);
		queryWrapper.eq(AmzFinUserItemData::getGroupid, groupid);
		queryWrapper.eq(AmzFinUserItemData::getMarketplaceid, marketplaceid);
		queryWrapper.eq(AmzFinUserItemData::getByday, byday);
		queryWrapper.eq(AmzFinUserItemData::getShopid, user.getCompanyid());
		queryWrapper.eq(AmzFinUserItemData::getSku, sku);
		queryWrapper.eq(AmzFinUserItemData::getCurrency, currency);
		List<AmzFinUserItemData> oldAmzFinUserItemDataList = amzFinUserItemDataMapper.selectList(queryWrapper);
		if(id != null && StrUtil.isNotEmpty(id.toString())) {
			if(oldAmzFinUserItemDataList==null||oldAmzFinUserItemDataList.size()==0||oldAmzFinUserItemDataList.get(0).getId().equals(id)) {
				return amzFinUserItemDataMapper.updateById(amzFinUserItemData);
			}else {
				throw new BizException(sku+"在"+ params.get("byday").toString() + "已经有了一笔记录了！");
			}
		}else {
			if(oldAmzFinUserItemDataList != null && oldAmzFinUserItemDataList.size() > 0) {
				throw new BizException(sku+"在"+ params.get("byday").toString() + "已经有了一笔记录了！");
			}
			params.put("id", amzFinUserItemData.getId());
			return amzFinUserItemDataMapper.insert(amzFinUserItemData);
		}
	}

	public int deleteFinItemData(String id) {
		return amzFinUserItemDataMapper.deleteById(id);
	}
	
	public IPage<Map<String, Object>> getFinDataList(Page<?> page,UserInfo user, Map<String, Object> params) {
		if (params.containsKey("month")) {
			return amzFinUserItemDataMapper.getFinDataMonthList(page,params);
		} else {
			return amzFinUserItemDataMapper.getFinDataList(page,params);
		}
	}
	
	public List<Map<String, Object>> getFinDataList(UserInfo user, Map<String, Object> params) {
		if (params.containsKey("month")) {
			return amzFinUserItemDataMapper.getFinDataMonthList(params);
		} else {
			return amzFinUserItemDataMapper.getFinDataList(params);
		}
	}
	
	@Cacheable(value = "findSettlementSKUCache",key="#ekey")
	public List<Map<String, Object>> getFinDataLastWeek(String ekey,UserInfo user, Map<String, Object> params) {
		return amzFinUserItemDataMapper.getFinDataLastWeek(params);
	}

	public List<Map<String, Object>> findFinListByShopid(String shopid) {
		return amzFinUserItemMapper.findFinListByShopid(shopid);
	}

	public static Map<String, String> findSysFinMap() {
		Map<String, String> titlemap = new HashMap<String, String>();
		titlemap.put("setincome", "结算收入");
		titlemap.put("spend", "广告费用");
		titlemap.put("price", "采购成本");
		titlemap.put("othersfee", "其它成本");
		titlemap.put("principal", "销售额");
		titlemap.put("salenum", "销售数量");
		titlemap.put("ordernum", "订单数量");
		titlemap.put("avgprice", "平均售价");
		titlemap.put("commission", "销售佣金");
		titlemap.put("fbafee", "FBA费用");
		titlemap.put("refund", "退款金额");
		titlemap.put("refundnum", "退款数量");
		titlemap.put("otherfee", "其它收支");
		titlemap.put("shipmentfee", "运费预估");
		titlemap.put("storagefee", "仓储费");
		titlemap.put("longTermFee", "长期仓储费");
		titlemap.put("firstShipment", "货件头程运费");
		titlemap.put("vat", "VAT税费");
		titlemap.put("itemshopfee", "店铺费用");
		return titlemap;
	}

	public static Map<String, String> findSysNeedDisplayFinMap() {
		Map<String, String> titlemap = new HashMap<String, String>();
		titlemap.put("price", "采购成本");
		titlemap.put("shipmentfee", "运费预估");
		titlemap.put("othersfee", "其它成本");
		titlemap.put("firstShipment", "货件头程运费");
		titlemap.put("vat", "VAT税费");
		titlemap.put("itemshopfee", "店铺费用");
		return titlemap;
	}

	public Map<String, String> findFinMapByShopid(String shopid) {
		Map<String, String> titlemap = new HashMap<String, String>();
		List<Map<String, Object>> list = findFinListByShopid(shopid);
		for (Map<String, Object> item : list) {
			titlemap.put(item.get("id").toString(), item.get("name").toString());
		}
		return titlemap;
	}

	private List<AmzFinSettlementFormula> getAmzFinSettlementFormulaByShopid(String shopid){
		LambdaQueryWrapper<AmzFinSettlementFormula> queryWrapper=new LambdaQueryWrapper<AmzFinSettlementFormula>();
		queryWrapper.eq(AmzFinSettlementFormula::getShopid, shopid);
		List<AmzFinSettlementFormula> list = amzFinSettlementFormulaMapper.selectList(queryWrapper);
		return list;
	}
	public AmzFinSettlementFormula getAmzFinSettlementFormula(String shopid) {
		List<AmzFinSettlementFormula> list = getAmzFinSettlementFormulaByShopid(shopid);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			AmzFinSettlementFormula formula = new AmzFinSettlementFormula();
			formula.setFormula("(shipmentfee+othersfee+price+spend+storagefee+longTermFee+vat)");
			formula.setField("shipmentfee,storagefee,longTermFee,othersfee,price,spend,vat");
			return formula;
		}
	}

	public Map<String,Object> getAmzFinSettlementFormulaConvert(String shopid) {
		List<AmzFinSettlementFormula> list = getAmzFinSettlementFormulaByShopid(shopid);
		Map<String,Object> map=new HashMap<String,Object>();
		if (list != null && list.size() > 0) {
			AmzFinSettlementFormula formula = list.get(0);
			Map<String, String> titleBasemap = findSysFinMap();
			Map<String, String> finItemMap = findFinMapByShopid(shopid);
			String formulastr = formula.getFormula();
			for (Entry<String, String> entry : finItemMap.entrySet()) {
				if (formulastr.contains("field" + entry.getKey())) {
					formulastr = formulastr.replace("field" + entry.getKey(), entry.getValue());
				}
			}
			for (Entry<String, String> entry : titleBasemap.entrySet()) {
				if (formulastr.contains(entry.getKey())) {
					formulastr = formulastr.replace(entry.getKey(), entry.getValue());
				}
			}
			map.put("formulaData", formulastr);
			map.put("pricetype", formula.getPricetype());
			return map;
		} else {
			map.put("formulaData", "(运费预估+其它成本+采购成本+仓储费+长期仓储费+广告费用+VAT税费)");
			map.put("pricetype", 1);
			return map;
		}
	}

	@CacheEvict(value = { "findSettlementSKUCache" }, allEntries = true)
	public String saveformulaData(UserInfo user, String formuladata,String pricetype) {
		String formulastr = formuladata;
		String formulastr2 = formuladata;
		String formulafield = null;
		Map<String, String> titlemapBase = findSysFinMap();
		Map<String, String> titlemapFinItem = findFinMapByShopid(user.getCompanyid());
		for (Entry<String, String> entry : titlemapFinItem.entrySet()) {
			if (formulastr.contains(entry.getValue())) {
				formulastr = formulastr.replace(entry.getValue(), "field" + entry.getKey());
				formulastr2 = formulastr2.replace(entry.getValue(), "");
				if (formulafield != null) {
					formulafield = formulafield + "," + entry.getKey();
				} else {
					formulafield = entry.getKey();
				}
			}
		}
		for (Entry<String, String> entry : titlemapBase.entrySet()) {
			if (formulastr.contains(entry.getValue())) {
				if(formulastr.indexOf("+"+entry.getValue())>0) {
					formulastr = formulastr.replace("+"+entry.getValue(), "+"+entry.getKey());
					formulastr2 = formulastr2.replace("+"+entry.getValue(), "");
				}else {
					formulastr = formulastr.replace(entry.getValue(), entry.getKey());
					formulastr2 = formulastr2.replace(entry.getValue(), "");
				}
				
			
				if (formulafield != null) {
					formulafield = formulafield + "," + entry.getKey();
				} else {
					formulafield = entry.getKey();
				}
			}
		}
		try {
			AviatorEvaluator.compile(formulastr);
		} catch (Exception e) {
			throw new BizException("表达式错误");
		}
		if (GeneralUtil.conValidate(formulastr2)) {
			throw new BizException("含有无法识别的字段");
		}
		AmzFinSettlementFormula formula = getAmzFinSettlementFormula(user.getCompanyid());
		if (formula.getShopid() != null) {
			formula.setFormula(formulastr);
			formula.setOperator(user.getId());
			formula.setOpttime(new Date());
			formula.setField(formulafield);
			if(StrUtil.isNotBlank(pricetype)) {
				formula.setPricetype(Integer.parseInt(pricetype));
			}
			amzFinSettlementFormulaMapper.updateById(formula);
		} else {
			formula = new AmzFinSettlementFormula();
			formula.setCreatedate(new Date());
			formula.setCreator(user.getId());
			formula.setOperator(user.getId());
			formula.setOpttime(new Date());
			formula.setShopid(user.getCompanyid());
			formula.setFormula(formulastr);
			formula.setField(formulafield);
			if(StrUtil.isNotBlank(pricetype)) {
				formula.setPricetype(Integer.parseInt(pricetype));
			}
			amzFinSettlementFormulaMapper.insert(formula);
		}
		return formuladata;
	}

	public Map<String, String> getformulaTitle(String shopid) {
		AmzFinSettlementFormula formula = getAmzFinSettlementFormula(shopid);
		String field = formula.getField();
		String[] fields = field.split(",");
		Map<String, String> titlemap1 = new HashMap<String, String>();
		Map<String, String> titlemap2 = findSysNeedDisplayFinMap();
		List<Map<String, Object>> list = findFinListByShopid(shopid);
		for (Map<String, Object> item : list) {
			titlemap1.put(item.get("id").toString(), item.get("name").toString());
		}
		Map<String, String> result = new HashMap<String, String>();
		for (String field2 : fields) {
			if (titlemap2.get(field2) != null) {
				result.put(field2, titlemap2.get(field2));
			} else if (titlemap1.get(field2) != null) {
				result.put("field" + field2, titlemap1.get(field2));
			}
		}
		return result;
	}

	public void setExcelBookOverall(SXSSFWorkbook workbook, List<Map<String, Object>> list) {
    	
		Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
		titlemap.put("groupname", "店铺名称");
		titlemap.put("marketname", "站点");
		titlemap.put("principal", "销售额");
		titlemap.put("commission", "销售佣金");
		titlemap.put("fbafee", "FBA费用");
		titlemap.put("refund", "退款金额");
		titlemap.put("storagefee", "仓储费");
		titlemap.put("advfee", "广告费");
		titlemap.put("shipcharge", "国际物流");
		titlemap.put("reserve", "预留金差额");
		titlemap.put("savefee", "折扣");
		titlemap.put("untransfer", "转账失败补");
		titlemap.put("other", "其他");
		titlemap.put("setin", "结算收入");		
		titlemap.put("price", "采购成本");
		titlemap.put("profit", "利润");		

		

		if (list.size() > 0 && list != null) {
			Sheet sheet = workbook.createSheet("sheet1");
			// 在索引0的位置创建行（最顶端的行）
			Row trow = sheet.createRow(0);
			Cell cell = null;
			Object[] titlearray = titlemap.keySet().toArray();
			for (int i = 0; i < titlearray.length; i++) {
				cell = trow.createCell(i); // 在索引0的位置创建单元格(左上端)
				Object value = titlemap.get(titlearray[i].toString());
				cell.setCellValue(value.toString());
			}
			// Font f = new Font("宋体", Font.PLAIN, 12);
			// FontMetrics fm = sun.font.FontDesignMetrics.getMetrics(f);
			for (int i = 0; i < list.size(); i++) {
				Row row = sheet.createRow(i + 1);
				Map<String, Object> map = list.get(i);
				for (int j = 0; j < titlearray.length; j++) {
					cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
					Object value = map.get(titlearray[j].toString());
					if (value != null) {
						if(value instanceof BigDecimal) {
							cell.setCellValue(new Double(value.toString()));
						}else {
							cell.setCellValue(value.toString());
						}
					}
				}
			}
		} else {
			try {
				throw new Exception("没有数据可导出！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setExcelBook(SXSSFWorkbook workbook, List<Map<String, Object>> list) {
		Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
		titlemap.put("symbol", "币种");
		titlemap.put("name", "货币符号");
		titlemap.put("price", "标准汇率(以100RMB为基准)");
		titlemap.put("myprice", "我的汇率");
		if (list.size() > 0 && list != null) {
			Sheet sheet = workbook.createSheet("sheet1");
			// 在索引0的位置创建行（最顶端的行）
			Row trow = sheet.createRow(0);
			Cell cell = null;
			Object[] titlearray = titlemap.keySet().toArray();
			for (int i = 0; i < titlearray.length; i++) {
				cell = trow.createCell(i); // 在索引0的位置创建单元格(左上端)
				Object value = titlemap.get(titlearray[i].toString());
				cell.setCellValue(value.toString());
			}
			// Font f = new Font("宋体", Font.PLAIN, 12);
			// FontMetrics fm = sun.font.FontDesignMetrics.getMetrics(f);
			for (int i = 0; i < list.size(); i++) {
				Row row = sheet.createRow(i + 1);
				Map<String, Object> map = list.get(i);
				for (int j = 0; j < titlearray.length; j++) {
					cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
					Object value = map.get(titlearray[j].toString());
					if (value != null) {
						cell.setCellValue(value.toString());
					}
				}
			}
		} else {
			try {
				throw new Exception("没有数据可导出！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setExcelBookByOtherFee(SXSSFWorkbook workbook, List<Map<String, Object>> list, String ftype) {
		if (list.size() > 0 && list != null) {
			Sheet sheet = workbook.createSheet("sheet1");
			// 在索引0的位置创建行（最顶端的行）
			Row trow = sheet.createRow(0);
			Cell cell = null;
			cell = trow.createCell(0);
			cell.setCellValue("店铺");
			cell = trow.createCell(1);
			cell.setCellValue("站点");
			cell = trow.createCell(2);
			cell.setCellValue("费用类型");
			cell = trow.createCell(3);
			cell.setCellValue("SKU");
			cell = trow.createCell(4);
			cell.setCellValue("货币");
			cell = trow.createCell(5);
			cell.setCellValue("金额");
			if(StrUtil.isEmpty(ftype)) {
				cell = trow.createCell(6);
				cell.setCellValue("日期");
			}
			for (int i = 0; i < list.size(); i++) {
				Row row = sheet.createRow(i + 1);
				Map<String, Object> map = list.get(i);
				cell = row.createCell(0);  
				Object groupname = map.get("groupname");
				if (groupname != null) {
					cell.setCellValue(groupname.toString());
				}
				cell = row.createCell(1);  
				Object marketname = map.get("marketname");
				if (marketname != null) {
					cell.setCellValue(marketname.toString());
				}
				cell = row.createCell(2);  
				Object itemname = map.get("itemname");
				if (itemname != null) {
					cell.setCellValue(itemname.toString());
				}
				cell = row.createCell(3);  
				Object sku = map.get("sku");
				if (sku != null) {
					cell.setCellValue(sku.toString());
				}
				cell = row.createCell(4);  
				Object currency = map.get("currency");
				if (currency != null) {
					cell.setCellValue(currency.toString());
				}
				cell = row.createCell(5);  
				Object amount = map.get("amount");
				if (amount != null) {
					cell.setCellValue(amount.toString());
				}
				if(StrUtil.isEmpty(ftype)) {
					cell = row.createCell(6);  
					Object byday = map.get("byday");
					if (byday != null) {
						cell.setCellValue(byday.toString());
					}
				}
			}
		} else {
			try {
				throw new Exception("没有数据可导出！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public List<Map<String, Object>> getFinDataForSku(Map<String, Object> params) {
		return amzFinUserItemDataMapper.getFinDataForSku(params);
	}

	@Override
	public List<AmzFinUserItem> getFinItemList(String companyid) {
		LambdaQueryWrapper<AmzFinUserItem> queryWrapper=new LambdaQueryWrapper<AmzFinUserItem>();
		queryWrapper.eq(AmzFinUserItem::getShopid,companyid);
		queryWrapper.eq(AmzFinUserItem::getDisable, false);
		List<AmzFinUserItem> list = amzFinUserItemMapper.selectList(queryWrapper);
		return list;
	}

}
