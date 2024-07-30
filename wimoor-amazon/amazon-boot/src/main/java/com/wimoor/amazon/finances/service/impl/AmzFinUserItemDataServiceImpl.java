package com.wimoor.amazon.finances.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.common.pojo.entity.ExchangeRate;
import com.wimoor.amazon.common.service.IExchangeRateService;
import com.wimoor.amazon.finances.mapper.AmzFinUserItemDataMapper;
import com.wimoor.amazon.finances.mapper.AmzFinUserItemMapper;
import com.wimoor.amazon.finances.pojo.entity.AmzFinUserItem;
import com.wimoor.amazon.finances.pojo.entity.AmzFinUserItemData;
import com.wimoor.amazon.finances.service.IAmzFinUserItemDataService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;

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
	@Autowired
	AmzFinUserItemMapper amzFinUserItemMapper;
	@Autowired
	IAmazonGroupService amazonGroupService;
	@Autowired
	IMarketplaceService marketplaceService;
	@Autowired
	IExchangeRateService exchangeRateService;
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
	public int saveFinItemData(AmzFinUserItemData amzFinUserItemData) {
		boolean isnew=amzFinUserItemData.idIsNULL();
		LambdaQueryWrapper<AmzFinUserItemData> queryWrapper=new LambdaQueryWrapper<AmzFinUserItemData>();
		queryWrapper.eq(AmzFinUserItemData::getItemid, amzFinUserItemData.getItemid());
		queryWrapper.eq(AmzFinUserItemData::getGroupid, amzFinUserItemData.getGroupid());
		queryWrapper.eq(AmzFinUserItemData::getMarketplaceid, amzFinUserItemData.getMarketplaceid());
		queryWrapper.eq(AmzFinUserItemData::getByday,GeneralUtil.formatDate(amzFinUserItemData.getByday()) );
		queryWrapper.eq(AmzFinUserItemData::getShopid, amzFinUserItemData.getShopid());
		queryWrapper.eq(AmzFinUserItemData::getSku, amzFinUserItemData.getSku());
		queryWrapper.eq(AmzFinUserItemData::getCurrency, amzFinUserItemData.getCurrency());
		List<AmzFinUserItemData> oldAmzFinUserItemDataList = this.baseMapper.selectList(queryWrapper);
		if(!isnew) {
			if(oldAmzFinUserItemDataList==null||oldAmzFinUserItemDataList.size()==0||oldAmzFinUserItemDataList.get(0).getId().equals(amzFinUserItemData.getId())) {
				return this.baseMapper.updateById(amzFinUserItemData);
			}else {
				throw new BizException(amzFinUserItemData.getSku()+"在"+ GeneralUtil.formatDate(amzFinUserItemData.getByday())+ "已经有了一笔记录了！");
			}
		}else {
			if(oldAmzFinUserItemDataList != null && oldAmzFinUserItemDataList.size() > 0) {
				throw new BizException(amzFinUserItemData.getSku()+"在"+GeneralUtil.formatDate(amzFinUserItemData.getByday())+ "已经有了一笔记录了！");
			}
			
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
	
	public List<Map<String, Object>> getFinDataLastWeek(List<Map<String, Object>> params) {
		return this.baseMapper.getFinDataLastWeek(params);
	}


	@Override
	public List<Map<String, Object>> getFinDataForSku(Map<String, Object> params) {
		return this.baseMapper.getFinDataForSku(params);
	}


	@Override
	public void setExcelBookByOtherFee(SXSSFWorkbook workbook, List<Map<String, Object>> list) {
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
			cell = trow.createCell(6);
			cell.setCellValue("日期");
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
				cell = row.createCell(6);  
				Object byday = map.get("byday");
				if (byday != null) {
					cell.setCellValue(byday.toString());
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

	@Override
	public void setExcelBookByOtherFeeMonth(SXSSFWorkbook workbook, List<Map<String, Object>> list,String title) {
		if (list.size() > 0 && list != null) {
			Sheet sheet = workbook.createSheet("sheet1");
			// 在索引0的位置创建行（最顶端的行）
			Row trow = sheet.createRow(0);
			Cell cell = null;
			cell = trow.createCell(0);
			cell.setCellValue("日期");
			cell = trow.createCell(1);
			cell.setCellValue("店铺");
			cell = trow.createCell(2);
			cell.setCellValue("站点");
			cell = trow.createCell(3);
			cell.setCellValue("费用类型");
			cell = trow.createCell(4);
			cell.setCellValue("货币");
			cell = trow.createCell(5);
			cell.setCellValue("金额");
			for (int i = 0; i < list.size(); i++) {
				Row row = sheet.createRow(i + 1);
				Map<String, Object> map = list.get(i);
				cell = row.createCell(0);  
			    cell.setCellValue(title);
				cell = row.createCell(1);  
				Object groupname = map.get("groupname");
				if (groupname != null) {
					cell.setCellValue(groupname.toString());
				}
				cell = row.createCell(2);  
				Object marketname = map.get("marketname");
				if (marketname != null) {
					cell.setCellValue(marketname.toString());
				}
				cell = row.createCell(3);  
				Object itemname = map.get("itemname");
				if (itemname != null) {
					cell.setCellValue(itemname.toString());
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
			}
		} else {
			try {
				throw new Exception("没有数据可导出！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public List<Map<String,Object>> loadFile(UserInfo user, InputStream inputStream ) {
		Workbook workbook = null;
		try {
			workbook = WorkbookFactory.create(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			int sindex = 1;
			List<AmzFinUserItemData> list =new ArrayList<AmzFinUserItemData>();
			for (int i = sindex; i <= sheet.getLastRowNum(); i++) {
				Row info = sheet.getRow(i);
				boolean isblankrow=true;
				if(info==null) {
					continue;
				}
				Iterator<Cell> iterator = info.cellIterator();
				while(iterator.hasNext()) {
					Cell cell = iterator.next();
					if(cell.getCellTypeEnum() !=CellType.BLANK) {
						isblankrow=false;
						break;
					}
				}
				if (info == null||isblankrow) {
					continue;
				}
				AmzFinUserItemData item = uploadFinItemDataFile(user, info );
				list.add(item);
			}
			try {
				saveFinItemData(list);
			}catch(Exception e){
				e.printStackTrace();
				 throw new BizException("请按照模板检查输入日期格式或者费用格式");
			}
		} catch (EncryptedDocumentException | InvalidFormatException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				if (workbook != null) {
					workbook.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public AmzFinUserItemData uploadFinItemDataFile(UserInfo user,  Row info ) {
		if (info.getCell(0).getCellTypeEnum() == CellType.BLANK || info.getCell(1).getCellTypeEnum() == CellType.BLANK
				|| info.getCell(2).getCellTypeEnum() == CellType.BLANK
				|| info.getCell(3).getCellTypeEnum() == CellType.BLANK
				|| info.getCell(4).getCellTypeEnum() == CellType.BLANK
				|| info.getCell(5).getCellTypeEnum() == CellType.BLANK
				|| info.getCell(6).getCellTypeEnum() == CellType.BLANK) {
			throw new BizException("Excel文件格式解析出错,该项为必填项!");
		}
		String groupname = info.getCell(0).getStringCellValue();
		AmazonGroup group = amazonGroupService.findAmazonGroupByName(groupname, user.getCompanyid());
		String marketname = info.getCell(1).getStringCellValue();
		String marketplaceid=null;
		List<Marketplace> marketlist = marketplaceService.findAllMarketplace();
		for (Marketplace item : marketlist) {
			if (item.getName().equals(marketname)) {
				marketplaceid = item.getMarketplaceid();
				break;
			}
		}
		if(marketplaceid==null) {
			throw new BizException("无法找到站点");
		}
		String sku = info.getCell(2).getStringCellValue();
		Date date = info.getCell(3).getDateCellValue();
		String ftype =null;
		if(info.getCell(4)!=null) {
			if(info.getCell(4).getCellTypeEnum().equals(CellType.NUMERIC)) {
				Double ftypeObj = info.getCell(4).getNumericCellValue();
				ftype=ftypeObj.intValue()+"";
			}
			if(info.getCell(4).getCellTypeEnum().equals(CellType.STRING)) {
				ftype = info.getCell(4).getStringCellValue();
			}
		}else {
			throw new BizException("费用类型不能为空");
		}
		
		String currency = info.getCell(5).getStringCellValue();
		LambdaQueryWrapper<ExchangeRate> queryRate = new LambdaQueryWrapper<ExchangeRate>();
		queryRate.eq(ExchangeRate::getSymbol, currency);
		List<ExchangeRate> currencylist = exchangeRateService.list(queryRate);
		for (ExchangeRate item : currencylist) {
			if (currency.equals(item.getSymbol())) {
				currency = item.getName();
				break;
			}
		}
		double amount = info.getCell(6).getNumericCellValue();

		AmzFinUserItemData amzFinUserItemData = new AmzFinUserItemData();
		LambdaQueryWrapper<AmzFinUserItem> queryItem = new LambdaQueryWrapper<AmzFinUserItem>();
		queryItem.eq(AmzFinUserItem::getName, ftype);
		queryItem.eq(AmzFinUserItem::getDisable, false);
		queryItem.eq(AmzFinUserItem::getShopid, user.getCompanyid());
		List<AmzFinUserItem> list = amzFinUserItemMapper.selectList(queryItem);
		if (list.size() > 0) {
			amzFinUserItemData.setItemid(list.get(0).getId());
		} else {
			throw new BizException("无法找到财务项");
		}
		amzFinUserItemData.getId();// 提前生成id ，防止批量插入时，生成的id一致
		amzFinUserItemData.setSku(sku);
		amzFinUserItemData.setShopid(user.getCompanyid());
		amzFinUserItemData.setByday(date);
		amzFinUserItemData.setMarketplaceid(marketplaceid);
		amzFinUserItemData.setGroupid(group.getId());
		amzFinUserItemData.setCurrency(currency);
		amzFinUserItemData.setAmount(new BigDecimal(amount));
		amzFinUserItemData.setOperator(user.getId());
		amzFinUserItemData.setOpttime(new Date());
		return amzFinUserItemData;
	}

}
