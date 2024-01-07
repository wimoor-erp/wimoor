package com.wimoor.erp.purchase.alibaba.service.impl;

import com.wimoor.erp.purchase.alibaba.pojo.entity.PurchaseAlibabaSettlement;
import com.wimoor.common.GeneralUtil;
import com.wimoor.erp.purchase.alibaba.mapper.PurchaseAlibabaSettlementMapper;
import com.wimoor.erp.purchase.alibaba.service.IPurchaseAlibabaSettlementService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2023-11-01
 */
@Service
public class PurchaseAlibabaSettlementServiceImpl extends ServiceImpl<PurchaseAlibabaSettlementMapper, PurchaseAlibabaSettlement> implements IPurchaseAlibabaSettlementService {
	public String getString(	Row info ) {
		if(info==null)return null;
		Cell cell = info.getCell(1);
		if(cell==null)return null;
		Object value = cell.getCellTypeEnum().equals(CellType.STRING)? cell.getStringCellValue():cell.getNumericCellValue();
		return value==null?null:value.toString();
	}
	public BigDecimal getDecimal(Row info) {
		if(info==null)return null;
		Cell cell = info.getCell(1);
		if(cell==null)return null;
		 if( cell.getCellTypeEnum().equals(CellType.STRING)) {
				String value=cell.getStringCellValue();
				if(value.contains("-")) {
					return null;
				}
				return value!=null?new BigDecimal(value):null;
		 }else {
			 return new BigDecimal(cell.getNumericCellValue());
		 }
	}
	public Integer getInteger(Row info) {
		if(info==null)return null;
		Cell cell = info.getCell(1);
		if(cell==null)return null;
		 if( cell.getCellTypeEnum().equals(CellType.STRING)) {
			 String value=cell.getStringCellValue();
				if(value.contains("-")) {
					return null;
				}
				return value!=null?  Integer.parseInt(value):null;
		 }else {
			 Double value =  Double.valueOf(cell.getNumericCellValue());
			 return value.intValue();
		 }
	}
	public Date getDate(Row info) {
		if(info==null)return null;
		Cell cell = info.getCell(1);
		if(cell==null)return null;
		return getDate(cell);
	}
	public Date getDate(Cell cell) {
		if(cell==null)return null;
		if(cell.getCellTypeEnum().equals(CellType.STRING)) {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String value = cell.getStringCellValue();
			if(value==null)return null;
			else {
				try {
					return fmt.parse(value);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return  GeneralUtil.getDatez(value);
				}
			}
			
		}else {
			return cell.getDateCellValue();
		}
	}
	public Boolean setSettlementSheet(Workbook workbook, PurchaseAlibabaSettlement settlement) {
		// TODO Auto-generated method stub
		Sheet sheet = workbook.getSheetAt(0);
		if(sheet.getLastRowNum()>=8) {
			Row info=sheet.getRow(1);
			settlement.setAlibabaAccount(getString(info));
			info=sheet.getRow(2);
			settlement.setAmount(getDecimal(info));
			info=sheet.getRow(3);
			settlement.setQuantity(getInteger(info));
			info=sheet.getRow(4);
			settlement.setPostdate(getDate(info));
			info=sheet.getRow(5);
			settlement.setPaydate(getDate(info));
			info=sheet.getRow(6);
			settlement.setLoaddate(getDate(info));
			info=sheet.getRow(6);
			settlement.setRemark(getString(info));
		}
		sheet = workbook.getSheetAt(1);
		if(sheet!=null&&sheet.getLastRowNum()>=7) {
			Row info=sheet.getRow(2);
			settlement.setPayamount(getDecimal(info));
			info=sheet.getRow(3);
			settlement.setPaytimes(getInteger(info));
		}
		sheet = workbook.getSheetAt(2);
		if(sheet!=null&&sheet.getLastRowNum()>=7) {
			Row info=sheet.getRow(3);
			settlement.setReturnamount(getDecimal(info));
			info=sheet.getRow(4);
			settlement.setReturntimes(getInteger(info));
		}
		sheet = workbook.getSheetAt(3);
		if(sheet!=null&&sheet.getLastRowNum()>=6) {
			Row info=sheet.getRow(2);
			settlement.setPayreturnamount(getDecimal(info));
		}
		settlement.setOpttime(new Date());
	    LambdaQueryWrapper<PurchaseAlibabaSettlement> query=new LambdaQueryWrapper<PurchaseAlibabaSettlement>();
	    query.eq(PurchaseAlibabaSettlement::getAcct, settlement.getAcct());
	    query.eq(PurchaseAlibabaSettlement::getShopid, settlement.getShopid());
	    query.eq(PurchaseAlibabaSettlement::getAlibabaAccount, settlement.getAlibabaAccount());
	    query.eq(PurchaseAlibabaSettlement::getPostdate, settlement.getPostdate());
		PurchaseAlibabaSettlement one = this.baseMapper.selectOne(query);
		if(one!=null) {
			settlement.setId(one.getId());
			this.baseMapper.updateById(settlement);
		}else {
			this.baseMapper.insert(settlement);
		}
		return true;
	}
}
