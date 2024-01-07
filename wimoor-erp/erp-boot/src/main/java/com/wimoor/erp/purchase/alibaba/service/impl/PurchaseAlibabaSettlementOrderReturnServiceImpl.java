package com.wimoor.erp.purchase.alibaba.service.impl;

import com.wimoor.erp.purchase.alibaba.pojo.entity.PurchaseAlibabaSettlement;
import com.wimoor.erp.purchase.alibaba.pojo.entity.PurchaseAlibabaSettlementOrderReturn;
import com.wimoor.common.GeneralUtil;
import com.wimoor.erp.purchase.alibaba.mapper.PurchaseAlibabaSettlementOrderReturnMapper;
import com.wimoor.erp.purchase.alibaba.service.IPurchaseAlibabaSettlementOrderReturnService;
import com.wimoor.erp.purchase.alibaba.service.IPurchaseFormEntryAlibabaInfoService;
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
import org.springframework.beans.factory.annotation.Autowired;
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
public class PurchaseAlibabaSettlementOrderReturnServiceImpl extends ServiceImpl<PurchaseAlibabaSettlementOrderReturnMapper, PurchaseAlibabaSettlementOrderReturn> implements IPurchaseAlibabaSettlementOrderReturnService {
	@Autowired
	IPurchaseFormEntryAlibabaInfoService purchaseFormEntryAlibabaInfoService;
	
	public String getValueStr(	Cell cell ) {
		if(cell==null)return null;
		Object value = cell.getCellTypeEnum().equals(CellType.STRING)? cell.getStringCellValue():cell.getNumericCellValue();
		return value==null?null:value.toString();
	}
	public BigDecimal getDecimal(Cell cell) {
		// TODO Auto-generated method stub
		if(cell==null)return null;
		if(cell.getCellTypeEnum().equals(CellType.STRING)) {
			String value=cell.getStringCellValue();
			if(value.contains("-")) {
				return null;
			}
			return value!=null?new BigDecimal(value):null;
		}else {
			return new BigDecimal(cell.getNumericCellValue());
		}
		
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
	public Boolean paySettlementSheetOrder(Workbook workbook, PurchaseAlibabaSettlement settlement) {
		Sheet sheet = workbook.getSheetAt(2);
		boolean haserror=false;
		LambdaQueryWrapper<PurchaseAlibabaSettlementOrderReturn> query=new LambdaQueryWrapper<PurchaseAlibabaSettlementOrderReturn>();
		query.eq(PurchaseAlibabaSettlementOrderReturn::getSettlementid, settlement.getId());
		this.baseMapper.delete(query);
		for (int i = 11; i <= sheet.getLastRowNum(); i++) {
			Row info=sheet.getRow(i);
			if(info==null||info.getLastCellNum()<5) {
				continue;
			}
			Cell cell = info.getCell(0);
			PurchaseAlibabaSettlementOrderReturn detail=new PurchaseAlibabaSettlementOrderReturn();
			detail.setOrderid(getValueStr(cell));
			cell=info.getCell(1);
		    detail.setName(getValueStr(cell));
		    cell=info.getCell(2);
		    detail.setPayamount(getDecimal(cell));
		    cell=info.getCell(3);
		    detail.setReturntime(getDate(cell));
		    cell=info.getCell(4);
		    detail.setReturnamount(getDecimal(cell));
		    
		    cell=info.getCell(5);
		    detail.setReturntype(getValueStr(cell));
		    cell=info.getCell(6);
		    detail.setReturnto(getValueStr(cell));
		    
		    detail.setSettlementid(settlement.getId());
		    
		    this.baseMapper.insert(detail);
			 
		}
		return haserror;
	}


	
}
