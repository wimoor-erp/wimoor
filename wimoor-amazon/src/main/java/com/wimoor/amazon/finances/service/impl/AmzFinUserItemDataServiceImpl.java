package com.wimoor.amazon.finances.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.wimoor.amazon.finances.mapper.AmzFinUserItemDataMapper;
import com.wimoor.amazon.finances.pojo.entity.AmzFinUserItemData;
import com.wimoor.amazon.finances.service.IAmzFinUserItemDataService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 客户导入的SKU财务项费用-应用于商品营收其他费用项目导入 服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-10-27
 */
@Service
public class AmzFinUserItemDataServiceImpl extends ServiceImpl<AmzFinUserItemDataMapper, AmzFinUserItemData> implements IAmzFinUserItemDataService {
	
	public IPage<Map<String, Object>> getFinDataList(Page<?> page,UserInfo user, Map<String, Object> params) {
		if (params.containsKey("month")) {
			return this.baseMapper.getFinDataMonthList(page,params);
		} else {
			return this.baseMapper.getFinDataList(page,params);
		}
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
			this.baseMapper.insertBatch(insertList);
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
			amzFinUserItemData = this.baseMapper.selectById(id.toString());
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
		List<AmzFinUserItemData> oldAmzFinUserItemDataList = this.baseMapper.selectList(queryWrapper);
		if(id != null && StrUtil.isNotEmpty(id.toString())) {
			if(oldAmzFinUserItemDataList==null||oldAmzFinUserItemDataList.size()==0||oldAmzFinUserItemDataList.get(0).getId().equals(id)) {
				return this.baseMapper.updateById(amzFinUserItemData);
			}else {
				throw new BizException(sku+"在"+ params.get("byday").toString() + "已经有了一笔记录了！");
			}
		}else {
			if(oldAmzFinUserItemDataList != null && oldAmzFinUserItemDataList.size() > 0) {
				throw new BizException(sku+"在"+ params.get("byday").toString() + "已经有了一笔记录了！");
			}
			params.put("id", amzFinUserItemData.getId());
			return this.baseMapper.insert(amzFinUserItemData);
		}
	}

	public int deleteFinItemData(String id) {
		return this.baseMapper.deleteById(id);
	}
	

	public List<Map<String, Object>> getFinDataList(UserInfo user, Map<String, Object> params) {
		if (params.containsKey("month")) {
			return this.baseMapper.getFinDataMonthList(params);
		} else {
			return this.baseMapper.getFinDataList(params);
		}
	}
	
	@Cacheable(value = "findSettlementSKUCache",key="#ekey")
	public List<Map<String, Object>> getFinDataLastWeek(String ekey,UserInfo user, Map<String, Object> params) {
		return this.baseMapper.getFinDataLastWeek(params);
	}


	@Override
	public List<Map<String, Object>> getFinDataForSku(Map<String, Object> params) {
		return this.baseMapper.getFinDataForSku(params);
	}


	@Override
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


}
