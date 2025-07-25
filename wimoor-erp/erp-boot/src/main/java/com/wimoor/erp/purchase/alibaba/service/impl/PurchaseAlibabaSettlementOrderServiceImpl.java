package com.wimoor.erp.purchase.alibaba.service.impl;

import com.wimoor.erp.purchase.alibaba.pojo.entity.PurchaseAlibabaSettlement;
import com.wimoor.erp.purchase.alibaba.pojo.entity.PurchaseAlibabaSettlementOrder;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.erp.purchase.alibaba.mapper.PurchaseAlibabaSettlementOrderMapper;
import com.wimoor.erp.purchase.alibaba.service.IPurchaseAlibabaSettlementOrderService;
import com.wimoor.erp.purchase.alibaba.service.IPurchaseFormEntryAlibabaInfoService;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.math.BigInteger;
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
public class PurchaseAlibabaSettlementOrderServiceImpl extends ServiceImpl<PurchaseAlibabaSettlementOrderMapper, PurchaseAlibabaSettlementOrder> implements IPurchaseAlibabaSettlementOrderService {
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
		Sheet sheet = workbook.getSheetAt(0);
		boolean haserror=false;
		LambdaQueryWrapper<PurchaseAlibabaSettlementOrder> query=new LambdaQueryWrapper<PurchaseAlibabaSettlementOrder>();
		query.eq(PurchaseAlibabaSettlementOrder::getSettlementid, settlement.getId());
		this.baseMapper.delete(query);
		for (int i = 11; i <= sheet.getLastRowNum(); i++) {
			Row info=sheet.getRow(i);
			int lastindex = info.getLastCellNum()+1;
			Cell cell = info.getCell(0);
			PurchaseAlibabaSettlementOrder detail=new PurchaseAlibabaSettlementOrder();
			detail.setOrderid(getValueStr(cell));
			cell=info.getCell(1);
			detail.setPaytime(GeneralUtil.getDatez(getValueStr(cell)));
			cell=info.getCell(2);
		    detail.setName(getValueStr(cell));
		    cell=info.getCell(3);
		    detail.setPayamount(getDecimal(cell));
		    cell=info.getCell(4);
		    detail.setConfirmamount(getDecimal(cell));
		    cell=info.getCell(5);
		    detail.setConfirmtime(getDate(cell));
		    cell=info.getCell(5);
		    detail.setConfirmtime(getDate(cell));
		    cell=info.getCell(6);
		    detail.setPaytype(getValueStr(cell));
		    cell=info.getCell(7);
		    detail.setReturntype(getValueStr(cell));
		    cell=info.getCell(8);
		    detail.setReturnamount(getDecimal(cell));
		    detail.setSettlementid(settlement.getId());
		    this.baseMapper.insert(detail);
			try {
				BigDecimal price = detail.getConfirmamount();
				if(detail.getReturnamount()!=null) {
					price=price.subtract(detail.getReturnamount());
				}
				purchaseFormEntryAlibabaInfoService.checkPay(new BigInteger(detail.getOrderid().toString()),price,settlement.getAcct());
			}catch(BizException e) {
				haserror=true;
				Cell errorcell = info.createCell(lastindex);
				errorcell.setCellValue(e.getMessage());
			}
		}
		return haserror;
	}


	
}
