package com.wimoor.erp.purchase.alibaba.service.impl;

import com.wimoor.erp.purchase.alibaba.pojo.entity.PurchaseAlibabaSettlement;
import com.wimoor.erp.purchase.alibaba.pojo.entity.PurchaseAlibabaSettlementPayReturn;
import com.wimoor.common.GeneralUtil;
import com.wimoor.erp.purchase.alibaba.mapper.PurchaseAlibabaSettlementPayReturnMapper;
import com.wimoor.erp.purchase.alibaba.service.IPurchaseAlibabaSettlementPayReturnService;
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
 * 还款明细列表 服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2023-11-01
 */
@Service
public class PurchaseAlibabaSettlementPayReturnServiceImpl extends ServiceImpl<PurchaseAlibabaSettlementPayReturnMapper, PurchaseAlibabaSettlementPayReturn> implements IPurchaseAlibabaSettlementPayReturnService {

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
	
	@Override
	public Boolean uploadSettlementSheet(Workbook workbook, PurchaseAlibabaSettlement settlement) {
		// TODO Auto-generated method stub
		Sheet sheet = workbook.getSheetAt(3);
		if(sheet==null) {
			return false;
		}
		boolean haserror=false;
		LambdaQueryWrapper<PurchaseAlibabaSettlementPayReturn> query=new LambdaQueryWrapper<PurchaseAlibabaSettlementPayReturn>();
		query.eq(PurchaseAlibabaSettlementPayReturn::getSettlementid, settlement.getId());
		this.baseMapper.delete(query);
		for (int i = 9; i <= sheet.getLastRowNum(); i++) {
			Row info=sheet.getRow(i);
			Cell cell = info.getCell(0);
			PurchaseAlibabaSettlementPayReturn detail=new PurchaseAlibabaSettlementPayReturn();
			detail.setReturntime(getDate(cell));
			cell=info.getCell(1);
			detail.setAmount(getDecimal(cell));
			cell=info.getCell(2);
			detail.setReturntype(getValueStr(cell));
			cell=info.getCell(3);
			detail.setNumber(getValueStr(cell));
		    detail.setSettlementid(settlement.getId());
		    if(detail.getAmount()!=null&&(detail.getAmount().floatValue()>0.00001||detail.getAmount().floatValue()<-0.00001)) {
		    	this.baseMapper.insert(detail);
		    }
		}
		return haserror;
	}

}
