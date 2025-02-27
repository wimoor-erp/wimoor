package com.wimoor.erp.stock.service.impl;

import java.math.BigInteger;
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.pojo.entity.EnumByInventory;
import com.wimoor.erp.common.pojo.entity.Status;
import com.wimoor.erp.inventory.pojo.entity.Inventory;
import com.wimoor.erp.inventory.pojo.entity.InventoryParameter;
import com.wimoor.erp.inventory.service.IInventoryFormAgentService;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.stock.mapper.ErpDispatchOverseaFormMapper;
import com.wimoor.erp.stock.pojo.entity.ErpDispatchOverseaForm;
import com.wimoor.erp.stock.pojo.entity.ErpDispatchOverseaFormEntry;
import com.wimoor.erp.stock.service.IErpDispatchOverseaFormEntryService;
import com.wimoor.erp.stock.service.IErpDispatchOverseaFormService;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import lombok.RequiredArgsConstructor;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2023-02-24
 */
@Service
@RequiredArgsConstructor
public class ErpDispatchOverseaFormServiceImpl extends ServiceImpl<ErpDispatchOverseaFormMapper, ErpDispatchOverseaForm> implements IErpDispatchOverseaFormService {
	 
	final IErpDispatchOverseaFormEntryService iErpDispatchOverseaFormEntryService;
	 
	final IInventoryFormAgentService inventoryFormAgentService;
	 
	final IMaterialService materialService;
	 
	final IWarehouseService warehouseService;
	 
	final ISerialNumService serialNumService;
	 
	final IInventoryService inventoryService;
	@Override
	public int submitAction(UserInfo user,String ids,String status ) {
		int result = 0;
		String[] idlist = ids.split(",");
		for (int i = 0; i < idlist.length; i++) {
			String id = idlist[i];
			if (GeneralUtil.isNotEmpty(id)) {
				ErpDispatchOverseaForm dispatchForm = this.getById(id);
				if ("1".equals(status)||"0".equals(status)) {
				    //提交后不可编辑
				}
				if ("2".equals(status)) {
					if(dispatchForm.getAuditstatus()==1) {
					   changeInventory(user,dispatchForm);
					}
				}else if("3".equals(status)) {
					if(dispatchForm.getAuditstatus()==1) {
						changeInventory(user,dispatchForm);
						outBoundOut(user,dispatchForm);
					}
					if(dispatchForm.getAuditstatus()==2) {
						outBoundOut(user,dispatchForm);
					}
				}else if("4".equals(status)) {
					if(dispatchForm.getAuditstatus()==1||dispatchForm.getAuditstatus()==0) {
						changeInventory(user,dispatchForm);
						outBoundOut(user,dispatchForm);
						inBoundToFulfillable(user,dispatchForm);
					}
					if(dispatchForm.getAuditstatus()==2) {
						outBoundOut(user,dispatchForm);
						inBoundToFulfillable(user,dispatchForm);
					}
					if(dispatchForm.getAuditstatus()==3) {
						inBoundToFulfillable(user,dispatchForm);
					}
				}else if("5".equals(status)) {
					if(dispatchForm.getAuditstatus()==2) {
						cancelInventory(user,dispatchForm);
					}
					if(dispatchForm.getAuditstatus()==3) {
						cancelOut(user,dispatchForm);
					}
					if(dispatchForm.getAuditstatus()==4) {
						cancelIn(user,dispatchForm);
					}
					if(dispatchForm.getAuditstatus()==0) {
						this.baseMapper.deleteById(dispatchForm);
					}
				}
				if(!"0".equals(status)) {
					dispatchForm.setAudittime(new Date());
					dispatchForm.setAuditstatus(Integer.parseInt(status));
					result = this.baseMapper.updateById(dispatchForm);
				}
			}
		}
		return result;
	}

	@Override
	public Map<String, Object> saveAction(UserInfo user, ErpDispatchOverseaForm dispatchForm, String skumapstr) {
		// TODO Auto-generated method stub
		if (GeneralUtil.isNotEmpty(skumapstr)) {
			JSONArray skulist = GeneralUtil.getJsonArray(skumapstr);
			dispatchForm.setId(null);
			return saveForm(user,dispatchForm, skulist);
		}
		return null;
	}

	@Override
	public Map<String, Object> findById(String id) {
		// TODO Auto-generated method stub
		return this.baseMapper.findById(id);
	}

	@Override
	public String uploadDispatchStockByExcel(Sheet sheet, UserInfo user) {
		// TODO Auto-generated method stub
		Row whrow = sheet.getRow(0);
		Cell whnamecell = whrow.getCell(1);
		String whname_out = whnamecell.getStringCellValue();
		Row whrow2 = sheet.getRow(1);
		Cell whnamecell2 = whrow2.getCell(1);
		String whname_in = whnamecell2.getStringCellValue();
		Row whrow3 = sheet.getRow(2);
		Cell remarkcell = whrow3.getCell(1);
		remarkcell.setCellType(CellType.STRING);
		String remark = remarkcell.getStringCellValue();
		if (GeneralUtil.isEmpty(whname_out)||GeneralUtil.isEmpty(whname_in)) {
			return "仓库名称不能为空";
		}
		String shopid = user.getCompanyid();
		Warehouse wh_out = warehouseService.getWarehouseByName(shopid,whname_out);
		if (wh_out == null || !wh_out.getFtype().contains("self_")) {
			return "调出仓库无法匹配";
		}
		Warehouse wh_in = warehouseService.getWarehouseByName(shopid,whname_in);
		if (wh_in == null || !wh_in.getFtype().contains("self_")) {
			return "调入仓库无法匹配";
		}
		ErpDispatchOverseaForm dispatchForm = new ErpDispatchOverseaForm();
		dispatchForm.setId(warehouseService.getUUID());
		dispatchForm.setShopid(shopid);
		try {
			dispatchForm.setNumber(serialNumService.readSerialNumber(shopid, "PD"));
		} catch (Exception e) {
			e.printStackTrace();
			try {
				dispatchForm.setNumber(serialNumService.readSerialNumber(shopid, "PD"));
			} catch (Exception e1) {
				e1.printStackTrace();
				throw new BizException("编码获取失败,请联系管理员");
			}
		}
		dispatchForm.setFromWarehouseid(new BigInteger(wh_out.getId()));
		dispatchForm.setToWarehouseid(new BigInteger(wh_in.getId()));
		dispatchForm.setRemark(remark);
		dispatchForm.setAuditor(user.getId());
		dispatchForm.setCreator(user.getId());
		dispatchForm.setCreatedate(new Date());
		dispatchForm.setOperator(user.getId());
		dispatchForm.setAuditstatus(1);
		dispatchForm.setAudittime(new Date());
		dispatchForm.setOpttime(new Date());
		List<String> skuList = new ArrayList<String>();
		List<String> invList = new ArrayList<String>();
		boolean isok = true;
		for (int i = 4; i <= sheet.getLastRowNum(); i++) {
			Row skuRow = sheet.getRow(i);
			Cell cell = skuRow.getCell(0);
			cell.setCellType(CellType.STRING);
			String sku = cell.getStringCellValue();
			if(GeneralUtil.isEmpty(sku)) {
				continue;
			}
			Material material = materialService.selectBySKU(shopid,sku);
			if (material==null) {
				isok = false;
				skuList.add(sku);
			} else {
				Double qty = skuRow.getCell(1).getNumericCellValue();
				//判断库存是否充足
				Inventory inv = inventoryService.selectNowInv(wh_out.getId(), material.getId(), shopid, Status.fulfillable);
				if(inv!=null && inv.getQuantity()>=qty){
					//skuMap.put(material.getId(),(int)Math.floor(qty));
				} else {
					isok = false;
					invList.add(sku);
				}
			}
		}
		if (isok) {
			//this.saveForm(user,dispatchForm, skuMap);
			return "上传成功";
		} else {
			String msg = "上传失败,";
			if(skuList.size()>0){
				msg += "SKU:";
				for (int i = 0; i < skuList.size(); i++) {
					msg += skuList.get(i) + " ";
				}
				msg += "不匹配,请先创建产品。<br/>";
			}
			if(invList.size()>0){
				msg += "SKU:";
				for (int i = 0; i < invList.size(); i++) {
					msg += invList.get(i) + " ";
				}
				msg += "在"+wh_out.getName()+"库存不足。";
			}
			return msg;
		}
	}

	@Override
	public IPage<Map<String, Object>> findByCondition(Page<Object> page, Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.baseMapper.findByCondition(page,map);
	}

	public Map<String, Object> saveForm(UserInfo user,ErpDispatchOverseaForm dispatchForm, JSONArray skuMap) {
		Integer status = dispatchForm.getAuditstatus();
		dispatchForm.setAuditstatus(0);
		int result = 0;
		String msg = "";
		if (skuMap.size() > 0) {
			ErpDispatchOverseaForm oldFormEntity = getById(dispatchForm.getId());
			if (oldFormEntity != null) {
				result = this.baseMapper.updateById(dispatchForm);
				msg = "更新";
			} else {
				result = this.baseMapper.insert(dispatchForm);
				msg = "添加";
			}
			List<Map<String, Object>> oldentrylist = iErpDispatchOverseaFormEntryService.selectByFormid(dispatchForm.getId());
			if (oldentrylist != null && oldentrylist.size() > 0) {
				iErpDispatchOverseaFormEntryService.deleteByFormid(dispatchForm.getId());
			}
			for (int i=0;i<skuMap.size();i++) {
				JSONObject item = skuMap.getJSONObject(i);
				ErpDispatchOverseaFormEntry dispatchFormEntry = new ErpDispatchOverseaFormEntry();
				dispatchFormEntry.setFormid(new BigInteger(dispatchForm.getId()));
				dispatchFormEntry.setMaterialid(new BigInteger(item.getString("id")));
				dispatchFormEntry.setAmount(item.getInteger("amount"));
				dispatchFormEntry.setSellersku(item.getString("psku"));
				dispatchFormEntry.setFnsku(item.getString("fnsku"));
				iErpDispatchOverseaFormEntryService.save(dispatchFormEntry);
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if(result>0) {
			submitAction(user,dispatchForm.getId(),status.toString());
		}
		if (result > 0) {
			msg += "成功！";
		} else {
			msg += "失败！";
		}
		map.put("msg", msg);
		map.put("id", dispatchForm.getId());
		return map;
	}

	public void inBoundToFulfillable(UserInfo user ,ErpDispatchOverseaForm dispatchForm){
		InventoryParameter parameter_in = new InventoryParameter();
		parameter_in.setShopid(dispatchForm.getShopid());
		parameter_in.setFormid(dispatchForm.getId());
		parameter_in.setFormtype("overseaship");
		parameter_in.setWarehouse(dispatchForm.getToWarehouseid().toString());
		parameter_in.setOperator(dispatchForm.getOperator());
		parameter_in.setNumber(dispatchForm.getNumber());
		parameter_in.setStatus(EnumByInventory.alReady);
		List<Map<String, Object>> entrylist = iErpDispatchOverseaFormEntryService.selectByFormid(dispatchForm.getId());
		if (entrylist != null && entrylist.size() > 0) {
			for (int j = 0; j < entrylist.size(); j++) {
				parameter_in.setMaterial(entrylist.get(j).get("materialid").toString());
				parameter_in.setAmount(Integer.parseInt(entrylist.get(j).get("amount").toString()));
				inventoryFormAgentService.inStockByRead(parameter_in);
			}
		}
	}
	public void outBoundOut(UserInfo user ,ErpDispatchOverseaForm dispatchForm) {
		InventoryParameter parameter_out = new InventoryParameter();
		parameter_out.setShopid(dispatchForm.getShopid());
		parameter_out.setFormid(dispatchForm.getId());
		parameter_out.setNumber(dispatchForm.getNumber());
		parameter_out.setFormtype("overseaship");
		parameter_out.setWarehouse(dispatchForm.getFromWarehouseid().toString());
		parameter_out.setOperator(dispatchForm.getOperator());
		parameter_out.setStatus(EnumByInventory.alReady);
		List<Map<String, Object>> entrylist = iErpDispatchOverseaFormEntryService.selectByFormid(dispatchForm.getId());
		if (entrylist != null && entrylist.size() > 0) {
			for (int j = 0; j < entrylist.size(); j++) {
				parameter_out.setMaterial(entrylist.get(j).get("materialid").toString());
				parameter_out.setAmount(Integer.parseInt(entrylist.get(j).get("amount").toString()));
				inventoryFormAgentService.outStockByRead(parameter_out);
			}
		}
	}
	 

    public void changeInventory(UserInfo user,ErpDispatchOverseaForm dispatchForm) {
		List<ErpDispatchOverseaFormEntry> entrylist = iErpDispatchOverseaFormEntryService.findByFormid(dispatchForm.getId());
		InventoryParameter parameter_out = new InventoryParameter();
		parameter_out.setShopid(dispatchForm.getShopid());
		parameter_out.setFormid(dispatchForm.getId());
		parameter_out.setNumber(dispatchForm.getNumber());
		parameter_out.setFormtype("overseaship");
		parameter_out.setWarehouse(dispatchForm.getFromWarehouseid().toString());
		parameter_out.setOperator(dispatchForm.getOperator());
		parameter_out.setStatus(EnumByInventory.Ready);
		
		InventoryParameter parameter_in = new InventoryParameter();
		parameter_in.setShopid(dispatchForm.getShopid());
		parameter_in.setFormid(dispatchForm.getId());
		parameter_in.setFormtype("overseaship");
		parameter_in.setWarehouse(dispatchForm.getToWarehouseid().toString());
		parameter_in.setOperator(dispatchForm.getOperator());
		parameter_in.setNumber(dispatchForm.getNumber());
		parameter_in.setStatus(EnumByInventory.Ready);
		
		for(int i=0;i<entrylist.size();i++) {
			ErpDispatchOverseaFormEntry entry = entrylist.get(i);
			String materialid = entry.getMaterialid().toString();
			parameter_out.setMaterial(materialid);
			parameter_out.setAmount(entry.getAmount());
			inventoryFormAgentService.outStockByRead(parameter_out);
			parameter_in.setMaterial(materialid);
			parameter_in.setAmount(entry.getAmount());
			inventoryFormAgentService.inStockByRead(parameter_in);
		}
    }
   
    public void cancelOut(UserInfo user,ErpDispatchOverseaForm dispatchForm) {
		List<ErpDispatchOverseaFormEntry> entrylist = iErpDispatchOverseaFormEntryService.findByFormid(dispatchForm.getId());
		InventoryParameter parameter_out = new InventoryParameter();
		parameter_out.setShopid(dispatchForm.getShopid());
		parameter_out.setFormid(dispatchForm.getId());
		parameter_out.setNumber(dispatchForm.getNumber());
		parameter_out.setFormtype("overseaship");
		parameter_out.setWarehouse(dispatchForm.getFromWarehouseid().toString());
		parameter_out.setOperator(dispatchForm.getOperator());
		parameter_out.setStatus(EnumByInventory.alReady);
		
		InventoryParameter parameter_in = new InventoryParameter();
		parameter_in.setShopid(dispatchForm.getShopid());
		parameter_in.setFormid(dispatchForm.getId());
		parameter_in.setFormtype("overseaship");
		parameter_in.setWarehouse(dispatchForm.getToWarehouseid().toString());
		parameter_in.setOperator(dispatchForm.getOperator());
		parameter_in.setNumber(dispatchForm.getNumber());
		parameter_in.setStatus(EnumByInventory.Ready);
		
		for(int i=0;i<entrylist.size();i++) {
			ErpDispatchOverseaFormEntry entry = entrylist.get(i);
			String materialid = entry.getMaterialid().toString();
			parameter_out.setMaterial(materialid);
			parameter_out.setAmount(entry.getAmount());
			inventoryFormAgentService.outStockReadyCancel(parameter_out);
		 
			parameter_in.setMaterial(materialid);
			parameter_in.setAmount(entry.getAmount());
			inventoryFormAgentService.inStockReadyCancel(parameter_in);
		}
    }
    public void cancelIn(UserInfo user,ErpDispatchOverseaForm dispatchForm) {
		List<ErpDispatchOverseaFormEntry> entrylist = iErpDispatchOverseaFormEntryService.findByFormid(dispatchForm.getId());
		InventoryParameter parameter_out = new InventoryParameter();
		parameter_out.setShopid(dispatchForm.getShopid());
		parameter_out.setFormid(dispatchForm.getId());
		parameter_out.setNumber(dispatchForm.getNumber());
		parameter_out.setFormtype("overseaship");
		parameter_out.setWarehouse(dispatchForm.getFromWarehouseid().toString());
		parameter_out.setOperator(dispatchForm.getOperator());
		parameter_out.setStatus(EnumByInventory.alReady);
		
		InventoryParameter parameter_in = new InventoryParameter();
		parameter_in.setShopid(dispatchForm.getShopid());
		parameter_in.setFormid(dispatchForm.getId());
		parameter_in.setFormtype("overseaship");
		parameter_in.setWarehouse(dispatchForm.getToWarehouseid().toString());
		parameter_in.setOperator(dispatchForm.getOperator());
		parameter_in.setNumber(dispatchForm.getNumber());
		parameter_in.setStatus(EnumByInventory.alReady);
		
		for(int i=0;i<entrylist.size();i++) {
			ErpDispatchOverseaFormEntry entry = entrylist.get(i);
			String materialid = entry.getMaterialid().toString();
			parameter_out.setMaterial(materialid);
			parameter_out.setAmount(entry.getAmount());
			inventoryFormAgentService.outStockReadyCancel(parameter_out);
		 
			parameter_in.setMaterial(materialid);
			parameter_in.setAmount(entry.getAmount());
			inventoryFormAgentService.inStockReadyCancel(parameter_in);
		}
    }
    
    public void cancelInventory(UserInfo user,ErpDispatchOverseaForm dispatchForm) {
		List<ErpDispatchOverseaFormEntry> entrylist = iErpDispatchOverseaFormEntryService.findByFormid(dispatchForm.getId());
		InventoryParameter parameter_out = new InventoryParameter();
		parameter_out.setShopid(dispatchForm.getShopid());
		parameter_out.setFormid(dispatchForm.getId());
		parameter_out.setNumber(dispatchForm.getNumber());
		parameter_out.setFormtype("overseaship");
		parameter_out.setWarehouse(dispatchForm.getFromWarehouseid().toString());
		parameter_out.setOperator(dispatchForm.getOperator());
		parameter_out.setStatus(EnumByInventory.Ready);
		
		InventoryParameter parameter_in = new InventoryParameter();
		parameter_in.setShopid(dispatchForm.getShopid());
		parameter_in.setFormid(dispatchForm.getId());
		parameter_in.setFormtype("overseaship");
		parameter_in.setWarehouse(dispatchForm.getToWarehouseid().toString());
		parameter_in.setOperator(dispatchForm.getOperator());
		parameter_in.setNumber(dispatchForm.getNumber());
		parameter_in.setStatus(EnumByInventory.Ready);
		
		for(int i=0;i<entrylist.size();i++) {
			ErpDispatchOverseaFormEntry entry = entrylist.get(i);
			String materialid = entry.getMaterialid().toString();
			parameter_out.setMaterial(materialid);
			parameter_out.setAmount(entry.getAmount());
			inventoryFormAgentService.outStockReadyCancel(parameter_out);
		 
			parameter_in.setMaterial(materialid);
			parameter_in.setAmount(entry.getAmount());
			inventoryFormAgentService.inStockReadyCancel(parameter_in);
		}
    }
    
	public List<Map<String, Object>> getShipArrivalTimeRecord(String shopid, String country,String sku, String groupid){
		return this.baseMapper.getShipArrivalTimeRecord(shopid, country, sku, groupid);
	}
}
