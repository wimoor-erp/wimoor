package com.wimoor.amazon.inbound.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.inbound.mapper.ShipInboundBoxMapper;
import com.wimoor.amazon.inbound.mapper.ShipInboundshipmentTraceuploadMapper;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundBox;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundshipmentTraceupload;
import com.wimoor.amazon.inbound.service.IShipInboundshipmentTraceuploadService;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2023-07-26
 */
@Service
@RequiredArgsConstructor
public class ShipInboundshipmentTraceuploadServiceImpl extends ServiceImpl<ShipInboundshipmentTraceuploadMapper, ShipInboundshipmentTraceupload> implements IShipInboundshipmentTraceuploadService {

	final ShipInboundBoxMapper shipInboundBoxMapper;
	
	@Override
	public List<ShipInboundshipmentTraceupload> listByAuth() {
		// TODO Auto-generated method stub
		return this.baseMapper.listByAuth();
	}

	@Override
	public int saveItem(UserInfo user ,String shipmentid) {
		// TODO Auto-generated method stub
				ShipInboundshipmentTraceupload item = this.baseMapper.selectById(shipmentid);
				if(item!=null) {
					item.setStatus(0);
					item.setOpttime(new Date());
					item.setCreator(user.getId());
					item.setShopid(user.getCompanyid());
					return  this.baseMapper.updateById(item);
				}else {
					item=new ShipInboundshipmentTraceupload();
					item.setShipmentid(shipmentid);
					item.setStatus(0);
					item.setShopid(user.getCompanyid());
					item.setOpttime(new Date());
					item.setCreator(user.getId());
					item.setCreatetime(new Date());
					return this.baseMapper.insert(item);
				}
	}

	
	@Override
	public void getTempExcelReport(SXSSFWorkbook workbook) {
			 //创建sheet
			 Sheet sheet = workbook.createSheet("sheet1");
			 List<String> titlelist = new ArrayList<String>();
			 titlelist.add("货件编码");
			 titlelist.add("跟踪编码");
			 titlelist.add("箱子号(可不填)");
			 // 在索引0的位置创建行（最顶端的行）
			Row row = sheet.createRow(0);
			for (int i = 0; i < titlelist.size(); i++) {
				Cell cell = row.createCell(i);
				cell.setCellValue(titlelist.get(i));
			}
		
	}

	@Override
	public void uploadTraceuploadFile(UserInfo user, Row info) {
		String shipmentid = null;
		if (info.getCell(0) != null) {
			info.getCell(0).setCellType(CellType.STRING);
			shipmentid = info.getCell(0).getStringCellValue();
		}
		String transnumber = null;
		if (info.getCell(1) != null) {
			info.getCell(1).setCellType(CellType.STRING);
			transnumber = info.getCell(1).getStringCellValue();
		}
		String boxnum = null;
		if (info.getCell(2) != null) {
			info.getCell(2).setCellType(CellType.STRING);
			boxnum = info.getCell(2).getStringCellValue();
		}
		if(StrUtil.isNotEmpty(shipmentid) && StrUtil.isNotEmpty(transnumber)) {
			LambdaQueryWrapper<ShipInboundBox> queryWrapper=new LambdaQueryWrapper<ShipInboundBox>();
			queryWrapper.eq(ShipInboundBox::getShipmentid, shipmentid);
			if(boxnum!=null && StrUtil.isNotEmpty(boxnum.trim())) {
				queryWrapper.eq(ShipInboundBox::getBoxnum, Integer.parseInt(boxnum));
			}
			List<ShipInboundBox> boxlist = shipInboundBoxMapper.selectList(queryWrapper);
			if(boxlist!=null && boxlist.size()>0) {
				for (ShipInboundBox boxitem:boxlist) {
					boxitem.setTrackingId(transnumber);
					shipInboundBoxMapper.updateById(boxitem);
					saveItem(user, shipmentid);
				}
			}
		}
		
	
	}

	@Override
	public void saveStatus(UserInfo user, String shipmentid) {
		// TODO Auto-generated method stub
		ShipInboundshipmentTraceupload item = this.baseMapper.selectById(shipmentid);
		if(item!=null) {
			item.setStatus(1);
			item.setOpttime(new Date());
			item.setShopid(user.getCompanyid());
			item.setCreator(user.getId());
			 this.baseMapper.updateById(item);
		}else {
			item=new ShipInboundshipmentTraceupload();
			item.setShipmentid(shipmentid);
			item.setShopid(user.getCompanyid());
			item.setStatus(1);
			item.setOpttime(new Date());
			item.setCreator(user.getId());
			item.setCreatetime(new Date());
			this.baseMapper.insert(item);
		}
	}
}
