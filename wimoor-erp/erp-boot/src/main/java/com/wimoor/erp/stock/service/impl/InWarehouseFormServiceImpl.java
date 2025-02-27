package com.wimoor.erp.stock.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import com.wimoor.erp.stock.mapper.InWarehouseFormMapper;
import com.wimoor.erp.stock.pojo.entity.InWarehouseForm;
import com.wimoor.erp.stock.pojo.entity.InWarehouseFormEntry;
import com.wimoor.erp.stock.service.IInWarehouseFormEntryService;
import com.wimoor.erp.stock.service.IInWarehouseFormService;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import lombok.RequiredArgsConstructor;

 

@Service("inWarehouseFormService")
@RequiredArgsConstructor
public class InWarehouseFormServiceImpl extends ServiceImpl<InWarehouseFormMapper,InWarehouseForm> implements IInWarehouseFormService {
	 
	final IWarehouseService warehouseService;
	 
	final IMaterialService materialService;
	 
	final IInWarehouseFormEntryService inWarehouseFormEntryService;
	 
	final IInventoryFormAgentService inventoryFormAgentService;
	 
	final ISerialNumService serialNumService;

	public IPage<Map<String, Object>> findByCondition(Page<?> page ,Map<String, Object> map) {
		return this.baseMapper.findByCondition(page,map);
	}

	public Map<String, Object> findById(String id) {
		return this.baseMapper.findById(id);
	}

	public Map<String, Object> saveForm(InWarehouseForm inWarehouseForm, Map<String, Object> skuMap, UserInfo user) throws BizException {
		int result = 0;
		String msg = "";
		Map<String, Object> map = new HashMap<String, Object>();
		if (skuMap != null && skuMap.size() > 0) {
			List<Map<String, Object>> oldentrylist = inWarehouseFormEntryService.selectByFormid(inWarehouseForm.getId());
			if (oldentrylist != null && oldentrylist.size() > 0) {
				inWarehouseFormEntryService.deleteByFormid(inWarehouseForm.getId());
			}
			// 入库，变动库存
			InventoryParameter parameter = new InventoryParameter();
			parameter.setShopid(inWarehouseForm.getShopid());
			parameter.setFormid(inWarehouseForm.getId());
			parameter.setFormtype("otherin");
			parameter.setNumber(inWarehouseForm.getNumber());
			parameter.setWarehouse(inWarehouseForm.getWarehouseid());
			parameter.setOperator(inWarehouseForm.getOperator());

			for (String skuId : skuMap.keySet()) {
				InWarehouseFormEntry inWarehouseFormEntry = new InWarehouseFormEntry();
				inWarehouseFormEntry.setFormid(inWarehouseForm.getId());
				inWarehouseFormEntry.setMaterialid(skuId);
				inWarehouseFormEntry.setAmount(Integer.parseInt(skuMap.get(skuId).toString()));
				inWarehouseFormEntryService.save(inWarehouseFormEntry);// 在前端传来map，在后端保存入库清单
				parameter.setMaterial(skuId);
				parameter.setAmount(Integer.parseInt(skuMap.get(skuId).toString()));
				inventoryFormAgentService.inStockByDirect(parameter);
			}
			InWarehouseForm oldInWarehouseForm = getById(inWarehouseForm.getId());
			if (oldInWarehouseForm != null) {
				if(updateById(inWarehouseForm)) {
					result++;
				}
				msg = "更新";
			} else {
				if(save(inWarehouseForm)) {
					result ++;
				}
				msg = "添加";
			}
		}
		if (result > 0) {
			msg += "成功！";
		} else {
			msg += "失败！";
		}
		map.put("msg", msg);
		map.put("id", inWarehouseForm.getId());
		return map;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> saveAction(InWarehouseForm inWarehouseForm, String sku, UserInfo user) throws BizException {
		Map<String, Object> skuMap = null;
		if (GeneralUtil.isNotEmpty(sku)) {
			skuMap = (Map<String, Object>) JSON.parse(sku);
		}
		return saveForm(inWarehouseForm, skuMap, user);
	}

	public void deleteOtherInInventory(UserInfo user, String id) throws BizException {
		InWarehouseForm inWarehouseForm = this.getById(id);
		List<Map<String, Object>> entrylist = inWarehouseFormEntryService.selectByFormid(id);
		// 删除入库，对已经入库的产品进行出库操作
		InventoryParameter parameter_out = new InventoryParameter();
		parameter_out.setShopid(inWarehouseForm.getShopid());
		parameter_out.setFormid(id);
		parameter_out.setFormtype("otherin");
		parameter_out.setWarehouse(inWarehouseForm.getWarehouseid());
		parameter_out.setOperator(user.getId());
		parameter_out.setNumber(inWarehouseForm.getNumber());
		parameter_out.setOpttime(new Date());

		for (int i = 0; i < entrylist.size(); i++) {
			parameter_out.setMaterial(entrylist.get(i).get("materialid").toString());
			parameter_out.setAmount(Integer.parseInt(entrylist.get(i).get("amount").toString()));
			inventoryFormAgentService.outStockByDirect(parameter_out);
		}

	}

	@Transactional
	public String uploadInStockByExcel(Sheet sheet, UserInfo user)  {
		Row whrow = sheet.getRow(0);
		Cell whnamecell = whrow.getCell(1);
		String whname = whnamecell.getStringCellValue();
		if (GeneralUtil.isNotEmpty(whname)) {
			QueryWrapper<Warehouse> queryWrapper=new QueryWrapper<Warehouse>();
			queryWrapper.eq("disabled", false);
			queryWrapper.eq("shopid", user.getCompanyid());
			queryWrapper.eq("name", whname);
			queryWrapper.like("ftype", "self_%");
			List<Warehouse> list = warehouseService.list(queryWrapper);
			if (list == null || list.size() != 1) {
				return "仓库无法匹配";
			}
			Warehouse wh = list.get(0);
			InWarehouseForm inWarehouseForm = new InWarehouseForm();
			inWarehouseForm.setAuditor(user.getId());
			inWarehouseForm.setOperator(user.getId());
			inWarehouseForm.setOpttime(new Date());
			inWarehouseForm.setCreatedate(new Date());
			inWarehouseForm.setCreator(user.getId());
			inWarehouseForm.setWarehouseid(wh.getId());
			inWarehouseForm.setRemark("上传表单");
			inWarehouseForm.setShopid(user.getCompanyid());
			inWarehouseForm.setAudittime(new Date());
			inWarehouseForm.setAuditstatus(2);
			inWarehouseForm.setId(warehouseService.getUUID());
			try {
				inWarehouseForm.setNumber(serialNumService.readSerialNumber(user.getCompanyid(), "IN"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new BizException("读取编码失败");
			}
			Map<String, Object> skuMap = new HashMap<String, Object>();

			for (int i = 2; i <= sheet.getLastRowNum(); i++) {
				Row skuRow = sheet.getRow(i);
				Cell cell = skuRow.getCell(0);
				cell.setCellType(CellType.STRING);
				String sku = cell.getStringCellValue();
				if(GeneralUtil.isEmpty(sku)) {
					continue;
				}
				Double qty = skuRow.getCell(1).getNumericCellValue();
		 
				QueryWrapper<Material> queryMaterial=new QueryWrapper<Material>();
				queryMaterial.eq("sku", sku);
				queryMaterial.eq("shopid", user.getCompanyid());
				queryMaterial.eq("isDelete", false);
				List<Material> mlist = materialService.list(queryMaterial);
				if (mlist.size() != 1) {
					return "导入失败SKU("+sku+")不匹配";
				}
				Material m = mlist.get(0);
				 
				skuMap.put(m.getId(),(int)Math.floor(qty));
			}
			saveForm(inWarehouseForm, skuMap, user);
			return "上传成功";
		}
		return "仓库名称不能为空";
	}

	@Override
	public void getListDetailForExport(SXSSFWorkbook workbook, Map<String, Object> map) {
			List<Map<String, Object>> list = findDetailByCondition(map);
			Map<Integer, String> title = new HashMap<Integer, String>();
			title.put(0, "单据编码");
			title.put(1, "入库仓库");
			title.put(2, "入库申请人");
			title.put(3, "入库时间");
			title.put(4, "备注");
			title.put(5, "SKU");
			title.put(6, "产品名称");
			title.put(7, "入库数量");
			title.put(8, "可用库存");
			Map<String, String> titlemap = new HashMap<String, String>();
			titlemap.put("单据编码", "number");
			titlemap.put("入库仓库", "warehouse");
			titlemap.put("入库申请人", "creator");
			titlemap.put("入库时间", "opttime");
			titlemap.put("备注", "remark");
			titlemap.put("SKU", "sku");
			titlemap.put("产品名称", "name");
			titlemap.put("入库数量", "amount");
			titlemap.put("可用库存", "quantity");

			Sheet sheet = workbook.createSheet();
			Row row = sheet.createRow(0);
			for(Integer key: title.keySet()){
				Cell cell = row.createCell(key);
				cell.setCellValue(title.get(key));
			}
			for(int i = 0; i < list.size(); i++){
				Map<String, Object> rmap = list.get(i);
				Row crow = sheet.createRow(i+1);
				for(Integer key: title.keySet()){
					Cell cell = crow.createCell(key);
					if(rmap.get(titlemap.get(title.get(key)))==null){
						cell.setCellValue("");
					} else {
						cell.setCellValue(rmap.get(titlemap.get(title.get(key))).toString());
					}
				}
			}

	}

	private List<Map<String, Object>> findDetailByCondition(Map<String, Object> map) {
		return this.baseMapper.findDetailByCondition(map);
	}


}
