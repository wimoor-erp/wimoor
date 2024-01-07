package com.wimoor.erp.stock.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
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
import com.wimoor.erp.customer.pojo.entity.Customer;
import com.wimoor.erp.customer.service.ICustomerService;
import com.wimoor.erp.inventory.pojo.entity.InventoryParameter;
import com.wimoor.erp.inventory.service.IInventoryFormAgentService;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.stock.mapper.OutWarehouseFormMapper;
import com.wimoor.erp.stock.pojo.entity.OutWarehouseForm;
import com.wimoor.erp.stock.pojo.entity.OutWarehouseFormEntry;
import com.wimoor.erp.stock.service.IOutWarehouseFormEntryService;
import com.wimoor.erp.stock.service.IOutWarehouseFormService;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
 

@Service("outWarehouseForm")
@RequiredArgsConstructor
public class OutWarehouseFormServiceImpl  extends  ServiceImpl<OutWarehouseFormMapper,OutWarehouseForm> implements IOutWarehouseFormService {
	 
	final IOutWarehouseFormEntryService outWarehouseFormEntryService;
	 
	final IInventoryFormAgentService inventoryFormAgentService;
	 
	final IWarehouseService warehouseService;
	 
	final IMaterialService materialService;
	 
	final ICustomerService customerService;
	 
	final ISerialNumService serialNumService;

	public IPage<Map<String, Object>> findByCondition(Page<?> page,Map<String, Object> map) {
		return this.baseMapper.findByCondition(page,map);
	}

	public Map<String, Object> findById(String id) {
		return this.baseMapper.findById(id);
	}

	public Map<String, Object> saveForm(OutWarehouseForm outWarehouseForm, Map<String, Object> skuMap, UserInfo user) throws BizException {
		Map<String, Object> map = new HashMap<String, Object>();
		String msg = "";
		int result = 0;
		OutWarehouseForm oldOutWarehouseForm = getById(outWarehouseForm.getId());
		if (oldOutWarehouseForm != null) {
			result = this.baseMapper.updateById(outWarehouseForm);
			msg = "更新";
		} else {
			result = this.baseMapper.insert(outWarehouseForm);
			msg = "添加";
		}
		if (result > 0) {
			msg += "成功！";
		} else {
			msg += "失败！";
		}
		map.put("msg", msg);
		map.put("id", outWarehouseForm.getId());

		if (skuMap.size() > 0) {
			List<Map<String, Object>> oldentrylist = outWarehouseFormEntryService
					.selectByFormid(outWarehouseForm.getId());
			if (oldentrylist != null && oldentrylist.size() > 0) {
				outWarehouseFormEntryService.deleteByFormid(outWarehouseForm.getId());
			}
			// 出库，变动库存
			InventoryParameter parameter_out = new InventoryParameter();
			parameter_out.setShopid(outWarehouseForm.getShopid());
			parameter_out.setFormid(outWarehouseForm.getId());
			parameter_out.setFormtype("otherout");
			parameter_out.setWarehouse(outWarehouseForm.getWarehouseid());
			parameter_out.setOperator(outWarehouseForm.getOperator());
			parameter_out.setNumber(outWarehouseForm.getNumber());
			parameter_out.setOpttime(new Date());

			for (String skuId : skuMap.keySet()) {
				OutWarehouseFormEntry outWarehouseFormEntry = new OutWarehouseFormEntry();
				outWarehouseFormEntry.setFormid(outWarehouseForm.getId());
				outWarehouseFormEntry.setMaterialid(skuId);
				outWarehouseFormEntry.setAmount(Integer.parseInt(skuMap.get(skuId).toString()));
				outWarehouseFormEntryService.save(outWarehouseFormEntry);
				parameter_out.setMaterial(skuId);
				parameter_out.setAmount(Integer.parseInt(skuMap.get(skuId).toString()));
				inventoryFormAgentService.outStockByDirect(parameter_out);
			}
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> saveAction(OutWarehouseForm outWarehouseForm, String skumapstr, UserInfo user) throws BizException {
		if (GeneralUtil.isNotEmpty(skumapstr)) {
			Map<String, Object> skuMap = (Map<String, Object>) JSON.parse(skumapstr);
			if (skuMap.size() > 0) {
				return saveForm(outWarehouseForm, skuMap, user);
			}
		}
		return null;
	}

	public void deleteOtherOutInventory(UserInfo user, String id) throws BizException {
		OutWarehouseForm outWarehouseForm = this.getById(id);
		List<Map<String, Object>> entrylist = outWarehouseFormEntryService.selectByFormid(id);
		// 删除出库库，对已经出库的产品进行入库操作
		InventoryParameter parameter = new InventoryParameter();
		parameter.setShopid(outWarehouseForm.getShopid());
		parameter.setFormid(outWarehouseForm.getId());
		parameter.setFormtype("otherout");
		parameter.setNumber(outWarehouseForm.getNumber());
		parameter.setWarehouse(outWarehouseForm.getWarehouseid());
		parameter.setOperator(outWarehouseForm.getOperator());
		for (int i = 0; i < entrylist.size(); i++) {
			parameter.setMaterial(entrylist.get(i).get("materialid").toString());
			parameter.setAmount(Integer.parseInt(entrylist.get(i).get("amount").toString()));
			inventoryFormAgentService.inStockByDirect(parameter);
		}
	}

	@Transactional
	public String uploadOutStockByExcel(Sheet sheet, UserInfo user) throws BizException {
		String whname = null;
		String customer = null;
		String address = null;
		String express = null;
		String expressno = null;
		String remark = "";
		Row whrow = sheet.getRow(0);
		Cell whnamecell = whrow.getCell(1);
		if (whnamecell != null) {
			whname = whnamecell.getStringCellValue();
			if(StrUtil.isNotEmpty(whname)) {
				whname=whname.trim();
			}
		}
		Row row1 = sheet.getRow(1);
		Cell customercell = row1.getCell(1);
		if (customercell != null) {
			customer = customercell.getStringCellValue();
		}

		Row row2 = sheet.getRow(2);
		Cell adresscell = row2.getCell(1);
		if (adresscell != null) {
			address = adresscell.getStringCellValue();
		}

		Row row3 = sheet.getRow(3);
		Cell expresscell = row3.getCell(1);
		if (expresscell != null) {
			express = expresscell.getStringCellValue();
		}
		Row row4 = sheet.getRow(4);
		Cell expressnocell = row4.getCell(1);
		if (expressnocell != null) {
			expressnocell.setCellType(CellType.STRING);
			try {
				expressno = expressnocell.getStringCellValue();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Row row5 = sheet.getRow(5);
		Cell remarkcell = row5.getCell(1);
		if (remarkcell != null) {
			try {
				remark = remarkcell.getStringCellValue();
			} catch (Exception e1) {
				e1.printStackTrace();
				double remark_ = remarkcell.getNumericCellValue();
				Date date = HSSFDateUtil.getJavaDate(remark_);
				SimpleDateFormat sfd = new SimpleDateFormat("yyyy/MM/dd");
				remark = sfd.format(date);
			}
		}

		if (GeneralUtil.isNotEmpty(whname)) {
			QueryWrapper<Warehouse> queryWrapper=new QueryWrapper<Warehouse>();
			queryWrapper.eq("disabled", false);
			queryWrapper.eq("shopid", user.getCompanyid());
			queryWrapper.eq("name", whname);
			List<Warehouse> list = warehouseService.list(queryWrapper);
			if (list == null || list.size() == 0) {
				return "仓库无法匹配";
			}
			Warehouse wh = list.get(0);
			if (!wh.getFtype().contains("self_")) {
				return "仓库无法匹配";
			}

			OutWarehouseForm warehouseForm = new OutWarehouseForm();
			warehouseForm.setAuditor(user.getId());
			warehouseForm.setAuditstatus(2);// 0：未提交，1：提交未审核，2：已审核
			warehouseForm.setAudittime(new Date());
			warehouseForm.setOperator(user.getId());
			warehouseForm.setOpttime(new Date());
			warehouseForm.setCreator(user.getId());
			warehouseForm.setCreatedate(new Date());
			warehouseForm.setShopid(user.getCompanyid());
			warehouseForm.setWarehouseid(wh.getId());

			if (GeneralUtil.isNotEmpty(customer)) {
				QueryWrapper<Customer> queryWrapper2=new QueryWrapper<Customer>();
				queryWrapper2.eq("shopid", user.getCompanyid());
				queryWrapper2.eq("name", customer);
				List<Customer> clist = customerService.list(queryWrapper2);
				if (clist == null || clist.size() == 0) {
					return "客户简称无法匹配";
				}
				warehouseForm.setPurchaser(clist.get(0).getId());
			}

			warehouseForm.setToaddress(address);
			warehouseForm.setExpress(express);
			warehouseForm.setExpressno(expressno);
			warehouseForm.setRemark(remark);
			try {
				warehouseForm.setNumber(serialNumService.readSerialNumber(user.getCompanyid(), "O"));
			} catch (Exception e) {
				try {
					warehouseForm.setNumber(serialNumService.readSerialNumber(user.getCompanyid(), "O"));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					throw new BizException("获取单据编码失败，请联系技术支持");
				}
			}

			Map<String, Object> skuMap = new HashMap<String, Object>();
			List<String> skuList = new ArrayList<String>();
			boolean isok = true;
			for (int i = 7; i <= sheet.getLastRowNum(); i++) {
				Row skuRow = sheet.getRow(i);
				if (skuRow == null) {
					continue;
				}
				String sku = null;
				if (skuRow.getCell(0) != null) {
					sku = skuRow.getCell(0).getStringCellValue();
				}
				Double qty = (double) 0;
				if (skuRow.getCell(1) != null) {
					skuRow.getCell(1).setCellType(CellType.NUMERIC);
					qty = skuRow.getCell(1).getNumericCellValue();
				}
				if (GeneralUtil.isNotEmpty(sku)) {
					QueryWrapper<Material> queryWrapperMaterial=new QueryWrapper<Material>();
					queryWrapperMaterial.eq("sku", sku);
					queryWrapperMaterial.eq("shopid", user.getCompanyid());
					queryWrapperMaterial.eq("isDelete", false);
					List<Material> mlist = materialService.list(queryWrapperMaterial);
					if (mlist.size() != 1) {
						isok = false;
						skuList.add(sku);
						continue;
					}
					Material m = mlist.get(0);
					skuMap.put(m.getId(), (int)Math.floor(qty));
				}
			}
			if (isok) {
				saveForm(warehouseForm, skuMap, user);
			} else {
				String msg = "导入失败,SKU:";
				for (int i = 0; i < skuList.size(); i++) {
					msg += skuList.get(i) + " ";
				}
				msg += "不匹配,请先创建产品。";
				return msg;
			}
			return "上传成功";
		}
		return "上传失败";
	}

	public void getListDetailForExport(SXSSFWorkbook workbook, Map<String, Object> map) {
		List<Map<String, Object>> list = findDetailByCondition(map);
		Map<Integer, String> title = new HashMap<Integer, String>();
		title.put(0, "单据编码");
		title.put(1, "调出仓库");
		title.put(2, "出库申请人");
		title.put(3, "出库时间");
		title.put(4, "备注");
		title.put(5, "SKU");
		title.put(6, "产品名称");
		title.put(7, "出库数量");
		title.put(8, "可用库存");
		Map<String, String> titlemap = new HashMap<String, String>();
		titlemap.put("单据编码", "number");
		titlemap.put("调出仓库", "warehouse");
		titlemap.put("出库申请人", "creator");
		titlemap.put("出库时间", "opttime");
		titlemap.put("备注", "remark");
		titlemap.put("SKU", "sku");
		titlemap.put("产品名称", "name");
		titlemap.put("出库数量", "amount");
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
