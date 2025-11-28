package com.wimoor.erp.warehouse.service.impl;

import com.wimoor.erp.warehouse.pojo.dto.ShelfInvListDto;
import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelfInventory;
import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelfInventoryOptRecord;
import com.wimoor.erp.warehouse.pojo.vo.WarehouseShelfInventoryOptRecordVo;
import com.wimoor.common.pojo.entity.Picture;
import com.wimoor.common.service.IPictureService;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.warehouse.mapper.WarehouseShelfInventoryOptRecordMapper;
import com.wimoor.erp.warehouse.service.IWarehouseShelfInventoryOptRecordService;
import com.wimoor.erp.warehouse.service.IWarehouseShelfInventoryService;
import com.wimoor.erp.warehouse.service.IWarehouseShelfService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作记录 服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-03-19
 */
@Service
public class WarehouseShelfInventoryOptRecordServiceImpl extends ServiceImpl<WarehouseShelfInventoryOptRecordMapper, WarehouseShelfInventoryOptRecord> implements IWarehouseShelfInventoryOptRecordService {

	@Lazy
	@Autowired
	IWarehouseShelfService iWarehouseShelfService;
	
	@Lazy
	@Autowired
	IWarehouseShelfInventoryService iWarehouseShelfInventoryService;
	
	@Autowired
	IMaterialService iMaterialService;
	
	@Autowired
	IPictureService iPictureService;
	
	@Override
	public List<WarehouseShelfInventoryOptRecordVo> getRecordVo(String shopid, String formid, String formtype,String materialid) {
		// TODO Auto-generated method stub
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("shopid",shopid);
		if(formid!=null&&formid.contains(",")) {

			param.put("formids",Arrays.asList(formid.split(",")));
		}else {
			param.put("formid",formid);
		}
		param.put("formtype",formtype);
		param.put("materialid",materialid);
		List<WarehouseShelfInventoryOptRecordVo> result = this.baseMapper.selectRecordsByParams(param);
		if(result!=null&&result.size()>0) {
			for(WarehouseShelfInventoryOptRecordVo item:result) {
				item.setShelfname(iWarehouseShelfService.getAllParentName(item.getShelfid().toString()));
			}
		}
		return result;
	}

	public List<WarehouseShelfInventoryOptRecord> getRecord(String shopid, String formid, String formtype,String shelfid) {
		// TODO Auto-generated method stub
		LambdaQueryWrapper<WarehouseShelfInventoryOptRecord> query=new LambdaQueryWrapper<WarehouseShelfInventoryOptRecord>();
		query.eq(WarehouseShelfInventoryOptRecord::getShopid, shopid);
		query.eq(WarehouseShelfInventoryOptRecord::getFormid, formid);
		query.eq(WarehouseShelfInventoryOptRecord::getFormtype, formtype);
		if(shelfid!=null) {
			query.eq(WarehouseShelfInventoryOptRecord::getShelfid, shelfid);
		}
		query.orderByDesc(WarehouseShelfInventoryOptRecord::getOpttime);
		List<WarehouseShelfInventoryOptRecord> list = this.baseMapper.selectList(query);
		if(list!=null&&list.size()>0) {
			for(WarehouseShelfInventoryOptRecord item:list) {
				Material material = iMaterialService.getById(item.getMaterialid());
				item.setMaterial(material);
				Picture picture = iPictureService.getById(material.getImage());
				if(picture!=null) {
					material.setPicture(picture);
				} 
				item.setShelfname(iWarehouseShelfService.getAllParentName(item.getShelfid().toString()));
				LambdaQueryWrapper<WarehouseShelfInventory> queryinv=new LambdaQueryWrapper<WarehouseShelfInventory>();
				queryinv.eq(WarehouseShelfInventory::getShopid, shopid);
				queryinv.eq(WarehouseShelfInventory::getMaterialid, item.getMaterialid());
				queryinv.eq(WarehouseShelfInventory::getShelfid, item.getShelfid());
				if(item.getWarehouseid()!=null) {
					queryinv.eq(WarehouseShelfInventory::getWarehouseid, item.getWarehouseid());
				}else {
					queryinv.isNull(WarehouseShelfInventory::getWarehouseid);
				}
				WarehouseShelfInventory inv = iWarehouseShelfInventoryService.getOne(queryinv);
				item.setShelfInventory(inv);
			}
		}
		return list;
	}

	@Override
	public IPage<Map<String, Object>> getOptList(ShelfInvListDto condition, String shopid) {
		 IPage<Map<String, Object>> list = this.baseMapper.getOptList(condition.getPage(),shopid,condition.getWarehouseid(),condition.getSearch(),
				 condition.getStartDate(),condition.getEndDate() );
		for(Map<String, Object> item:list.getRecords()) {
			String shelfid=item.get("shelfid").toString();
			String shelfname=iWarehouseShelfService.getAllParentName(shelfid);
			item.put("shelfname",shelfname);
		}
		return list;
	}
}
