package com.wimoor.erp.stock.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.inventory.pojo.entity.InventoryParameter;
import com.wimoor.erp.inventory.service.IInventoryFormAgentService;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.stock.mapper.ChangeWhFormMapper;
import com.wimoor.erp.stock.pojo.entity.ChangeWhForm;
import com.wimoor.erp.stock.pojo.entity.ChangeWhFormEntry;
import com.wimoor.erp.stock.service.IChangeWhFormEntryService;
import com.wimoor.erp.stock.service.IChangeWhFormService;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import lombok.RequiredArgsConstructor;
 

@Service("changeWhFormService")
@RequiredArgsConstructor
public class ChangeWhFormServiceImpl extends  ServiceImpl<ChangeWhFormMapper,ChangeWhForm> implements IChangeWhFormService {
 
	 
	final IChangeWhFormEntryService changeWhFormEntryService;
	 
	final IInventoryFormAgentService inventoryFormAgentService;
	 
	final IWarehouseService warehouseService;
	 
	final IMaterialService materialService;
	 
	final ISerialNumService serialNumService;

	public IPage<Map<String, Object>> findByCondition(Page<?> page,Map<String, Object> map) {
		return this.baseMapper.findByCondition(page,map);
	}

	public Map<String, Object> findById(String id) {
		return this.baseMapper.findById(id);
	}

	public Map<String, Object> saveAction(ChangeWhForm form, List<Map<String, Object>> skuList, UserInfo user) {
		if (skuList != null && skuList.size() > 0) {
			List<ChangeWhFormEntry> oldentrylist = changeWhFormEntryService.selectByFormid(form.getId());
			if (oldentrylist != null && oldentrylist.size() > 0) {
				changeWhFormEntryService.deleteByFormid(form.getId());
			}
			
			InventoryParameter parameter = new InventoryParameter();
			parameter.setShopid(form.getShopid());
			parameter.setFormid(form.getId());
			parameter.setNumber(form.getNumber());
			parameter.setWarehouse(form.getWarehouseid());
			parameter.setOperator(form.getOperator());
			parameter.setOpttime(new Date());

			for (int i = 0; i < skuList.size(); i++) {
				ChangeWhFormEntry entry = new ChangeWhFormEntry();
				entry.setFormid(form.getId());
				entry.setMaterialFrom(skuList.get(i).get("sku_from").toString());
				entry.setMaterialTo(skuList.get(i).get("sku_to").toString());
				entry.setAmount(Integer.parseInt(skuList.get(i).get("amount").toString()));
				changeWhFormEntryService.save(entry);
				//出库
				parameter.setAmount(Integer.parseInt(skuList.get(i).get("amount").toString()));
				parameter.setFormtype("dispatch-inner");
				parameter.setMaterial(skuList.get(i).get("sku_from").toString());
				inventoryFormAgentService.outStockByDirect(parameter);
				//入库
				parameter.setFormtype("dispatch-inner");
				parameter.setMaterial(skuList.get(i).get("sku_to").toString());
				inventoryFormAgentService.inStockByDirect(parameter);
			}

		}

		Map<String, Object> map = new HashMap<String, Object>();
		int result = 0;
		String msg = "";
		ChangeWhForm oldFormEntity = getById(form.getId());
		if (oldFormEntity != null) {
			result = this.baseMapper.updateById(form);
			msg = "更新";
		} else {
			result = this.baseMapper.insert(form);
			msg = "添加";
		}
		if (result > 0) {
			msg += "成功！";
		} else {
			msg += "失败！";
		}
		map.put("msg", msg);
		map.put("id", form.getId());
		return map;
	}

	public void deleteChangeWhForm(UserInfo user, String id) {
		ChangeWhForm form = this.getById(id);
		List<ChangeWhFormEntry> entrylist = changeWhFormEntryService.selectByFormid(id);
		
		InventoryParameter parameter = new InventoryParameter();
		parameter.setShopid(form.getShopid());
		parameter.setFormid(id);
		parameter.setNumber(form.getNumber());
		parameter.setWarehouse(form.getWarehouseid());
		parameter.setOperator(user.getId());
		parameter.setOpttime(new Date());
	 
		for (int i = 0; i < entrylist.size(); i++) {
			// 删除入库，对已经入库的产品进行出库操作
			parameter.setAmount(entrylist.get(i).getAmount());
			parameter.setFormtype("dispatch-inner");
			parameter.setMaterial(entrylist.get(i).getMaterialTo());
			inventoryFormAgentService.outStockByDirect(parameter);
			// 删除出库，对已经出库的产品进行入库操作
			parameter.setFormtype("dispatch-inner");
			parameter.setMaterial(entrylist.get(i).getMaterialFrom());
			inventoryFormAgentService.inStockByDirect(parameter);
		}
		
	}

	@Transactional
	public String uploadchangeStockByExcel(Sheet sheet, UserInfo user) {
		Row whrow = sheet.getRow(0);
		Cell whnamecell = whrow.getCell(1);
		String whname = whnamecell.getStringCellValue();
		Row whrow3 = sheet.getRow(1);
		Cell remarkcell = whrow3.getCell(1);
		remarkcell.setCellType(CellType.STRING);
		String remark = remarkcell.getStringCellValue();
		if (GeneralUtil.isEmpty(whname)) {
			return "仓库名称不能为空";
		}
		String shopid = user.getCompanyid();
		Warehouse whhouse = warehouseService.getWarehouseByName(shopid,whname);
		if (whhouse == null || !whhouse.getFtype().contains("self_")) {
			return "操作仓库无法匹配";
		}
		ChangeWhForm form = new ChangeWhForm();
		form.setShopid(shopid);
		try {
			form.setNumber(serialNumService.readSerialNumber(shopid, "CH"));
		} catch (Exception e) {
			e.printStackTrace();
			try {
				form.setNumber(serialNumService.readSerialNumber(shopid, "CH"));
			} catch (Exception e1) {
				e1.printStackTrace();
				throw new BizException("编码获取失败,请联系管理员");
			}
		}
		form.setWarehouseid(whhouse.getId());
		form.setRemark(remark);
		form.setCreator(user.getId());
		form.setCreatedate(new Date());
		form.setOperator(user.getId());
		form.setOpttime(new Date());
		form.setAuditstatus(1);
		form.setId(warehouseService.getUUID());
		
		List<Map<String, Object>> skumapList = new ArrayList<Map<String, Object>>();
		List<String> skuList = new ArrayList<String>();
		boolean isok = true;
		for (int i = 3; i <= sheet.getLastRowNum(); i++) {
			Row skuRow = sheet.getRow(i);
			Cell cell = skuRow.getCell(0);
			cell.setCellType(CellType.STRING);
			String sku = cell.getStringCellValue();
			Cell cell2 = skuRow.getCell(1);
			cell2.setCellType(CellType.STRING);
			String sku2 = cell2.getStringCellValue();
			if(GeneralUtil.isEmpty(sku) || GeneralUtil.isEmpty(sku2)) {
				continue;
			}
			Material material = materialService.selectBySKU(shopid,sku);
			Material material2 = materialService.selectBySKU(shopid,sku2);
			if (material==null || material2==null) {
				isok = false;
				skuList.add("["+sku+"]-["+sku2+"]");
			} else {
				Map<String, Object> skuMap = new HashMap<String, Object>();
				Double qty = skuRow.getCell(2).getNumericCellValue();
				skuMap.put("sku_from", material.getId());
				skuMap.put("sku_to", material2.getId());
				skuMap.put("amount", (int)Math.floor(qty));
				skumapList.add(skuMap);
			}
		}
		if (isok) {
			this.saveAction(form, skumapList, user);
			return "上传成功";
		} else {
			String msg = "导入失败,SKU:";
			for (int i = 0; i < skuList.size(); i++) {
				msg += skuList.get(i) + " ";
			}
			msg += "不匹配,请先创建产品。";
			return msg;
		}
	}


}
