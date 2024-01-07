package com.wimoor.erp.assembly.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.assembly.mapper.AssemblyEntryInstockMapper;
import com.wimoor.erp.assembly.pojo.entity.AssemblyEntryInstock;
import com.wimoor.erp.assembly.pojo.entity.AssemblyForm;
import com.wimoor.erp.assembly.pojo.entity.AssemblyFormEntry;
import com.wimoor.erp.assembly.service.IAssemblyEntryInstockService;
import com.wimoor.erp.assembly.service.IAssemblyFormEntryService;
import com.wimoor.erp.assembly.service.IAssemblyFormService;
import com.wimoor.erp.common.pojo.entity.EnumByInventory;
import com.wimoor.erp.inventory.pojo.entity.InventoryParameter;
import com.wimoor.erp.inventory.service.IInventoryFormAgentService;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IMaterialService;

import lombok.RequiredArgsConstructor;
 
@Service("assemblyEntryInstockService")
@RequiredArgsConstructor
public class AssemblyEntryInstockServiceImpl extends ServiceImpl<AssemblyEntryInstockMapper,AssemblyEntryInstock> implements IAssemblyEntryInstockService{
	 @Autowired
	 @Lazy
	IAssemblyFormService assemblyFormService;
	 
	final IAssemblyFormEntryService assemblyFormEntryService;
	 
	final IInventoryFormAgentService inventoryFormAgentService;
	 
	final IMaterialService materialService;
	 
	final IInventoryService inventoryService;
	public List<Map<String,Object>> selectByFormId(String formid) {
		return this.baseMapper.selectByFormId(formid);
	}
 
	public Map<String, Object> saveAssemblyInStock(AssemblyEntryInstock entity,AssemblyForm assemblyForm, UserInfo user) throws BizException {
		Map<String, Object> msgMap = new HashMap<String, Object>();
		List<Map<String, Object>> instocklist = selectByFormId(entity.getFormid());
		Integer amount =  0;
		for (int i = 0; i < instocklist.size(); i++) {
			amount = amount + Integer.parseInt(instocklist.get(i).get("amount").toString());
		}
		amount=amount+entity.getAmount();
		msgMap.put("resAmount", assemblyForm.getAmountHandle() - amount);
		msgMap.put("inAmount", amount);
		if(amount.intValue() > assemblyForm.getAmount().intValue()){
			String msku="";
			if(assemblyForm.getMainmid()!=null) {
				Material mt = materialService.getById(assemblyForm.getMainmid());
				if(mt!=null) {
					msku=mt.getSku();
				}
			}
			throw new BizException(msku+"入库数量不可大于待组装数量");
		}
		if (amount.intValue() == assemblyForm.getAmount().intValue()) {
			assemblyForm.setAuditstatus(3);
		}else {
			assemblyForm.setAuditstatus(2);
		}
		int result = super.baseMapper.insert(entity);
	
		if (result > 0) {
			// 操作库存
			InventoryParameter invpara = new InventoryParameter();
			invpara.setAmount(Integer.parseInt(entity.getAmount().toString()));
			invpara.setFormid(entity.getFormid());
			invpara.setFormtype("assembly");
			invpara.setMaterial(assemblyForm.getMainmid());
			invpara.setOperator(entity.getOperator());
			invpara.setOpttime(new Date());
			invpara.setNumber(assemblyForm.getNumber());
			invpara.setShopid(assemblyForm.getShopid());
			invpara.setStatus(EnumByInventory.alReady);
			invpara.setWarehouse(assemblyForm.getWarehouseid());
			if (entity.getAmount() > 0) {
				Integer result2 = inventoryFormAgentService.inStockByRead(invpara);
				if (result2 > 0) {
					msgMap.put("msg", "入库操作成功！");
					assemblyForm.setAmountHandle(assemblyForm.getAmountHandle()+Integer.parseInt(entity.getAmount().toString()));
					assemblyFormService.updateById(assemblyForm);
				} else {
					msgMap.put("msg", "库存操作失败！");
				}
			}
		}
		
		 
		List<AssemblyFormEntry> list = assemblyFormEntryService.selectEntityByFormid(entity.getFormid());
		for (int i = 0; i < list.size(); i++) {
			AssemblyFormEntry entry = list.get(i);
			InventoryParameter invpara = new InventoryParameter();
			amount = (entry.getAmount() / assemblyForm.getAmount()) * entity.getAmount();
			invpara.setAmount(Integer.parseInt(amount.toString()));
			invpara.setFormid(entity.getFormid());
			invpara.setFormtype("assembly");
			invpara.setMaterial(entry.getMaterialid());
			invpara.setOperator(entity.getOperator());
			invpara.setOpttime(new Date());
			invpara.setShopid(assemblyForm.getShopid());
			invpara.setStatus(EnumByInventory.alReady);
			invpara.setNumber(assemblyForm.getNumber());
			invpara.setWarehouse(entry.getWarehouseid());
			if (invpara.getAmount() > 0)
				inventoryFormAgentService.outStockByDirect(invpara);
		    }
		assemblyFormService.updateById(assemblyForm);
		return msgMap;
	}

	
	public Map<String, Object> saveSplitInStock(AssemblyEntryInstock entity,AssemblyForm assemblyForm, UserInfo user) throws BizException {
		Map<String, Object> msgMap = new HashMap<String, Object>();
		List<Map<String, Object>> instocklist = selectByFormId(entity.getFormid());
		Integer amount =  0;
		for (int i = 0; i < instocklist.size(); i++) {
			amount = amount + Integer.parseInt(instocklist.get(i).get("amount").toString());
		}
		amount=amount+entity.getAmount();
		if(amount.intValue()>assemblyForm.getAmount().intValue()){
			String msku="";
			if(assemblyForm.getMainmid()!=null) {
				Material mt = materialService.getById(assemblyForm.getMainmid());
				if(mt!=null) {
					msku=mt.getSku();
				}
			}
			throw new BizException(msku+"入库数量不可大于待组装数量");
		}
		else if (amount.intValue() == assemblyForm.getAmount().intValue()) {
			assemblyForm.setAuditstatus(3);
		}else {
			assemblyForm.setAuditstatus(2);
		}
		int result = this.baseMapper.insert(entity);
	
		if (result > 0) {
			// 操作库存
			InventoryParameter invpara = new InventoryParameter();
			invpara.setAmount(Integer.parseInt(entity.getAmount().toString()));
			invpara.setFormid(entity.getFormid());
			invpara.setFormtype("assembly");
			invpara.setMaterial(assemblyForm.getMainmid());
			invpara.setOperator(entity.getOperator());
			invpara.setOpttime(new Date());
			invpara.setNumber(assemblyForm.getNumber());
			invpara.setShopid(assemblyForm.getShopid());
			invpara.setStatus(EnumByInventory.alReady);
			invpara.setWarehouse(assemblyForm.getWarehouseid());
			if (entity.getAmount() > 0) {
				Integer result2 = inventoryFormAgentService.outStockByDirect(invpara);
				if (result2 > 0) {
					msgMap.put("msg", "入库操作成功！");
				} else {
					msgMap.put("msg", "库存操作失败！");
				}
			}
		}
		
	 
		List<AssemblyFormEntry> list = assemblyFormEntryService.selectEntityByFormid( entity.getFormid());
		for (int i = 0; i < list.size(); i++) {
			AssemblyFormEntry entry = list.get(i);
			InventoryParameter invpara = new InventoryParameter();
			  amount = (entry.getAmount() / assemblyForm.getAmount()) * entity.getAmount();
			invpara.setAmount(Integer.parseInt(amount.toString()));
			invpara.setFormid(entity.getFormid());
			invpara.setFormtype("assembly");
			invpara.setMaterial(entry.getMaterialid());
			invpara.setOperator(entity.getOperator());
			invpara.setOpttime(new Date());
			invpara.setShopid(assemblyForm.getShopid());
			invpara.setStatus(EnumByInventory.alReady);
			invpara.setNumber(assemblyForm.getNumber());
			invpara.setWarehouse(entry.getWarehouseid());
			if (invpara.getAmount() > 0)
				inventoryFormAgentService.inStockByRead(invpara);
		}
		assemblyFormService.updateById(assemblyForm);
		return msgMap;
	}
	
	public Map<String, Object> saveMineAndinStock(AssemblyEntryInstock entity, UserInfo user) throws BizException {
		AssemblyForm assemblyForm = assemblyFormService.getById(entity.getFormid());
		entity.setWarehouseid(assemblyForm.getWarehouseid());
		if(assemblyForm.getFtype()!=null&&"dis".equals(assemblyForm.getFtype().trim())){
			return saveSplitInStock(entity, assemblyForm, user);
		}else{
			return saveAssemblyInStock(entity, assemblyForm, user);
		}
	}

	public Integer findhasAssemblyFromShipment(String shipmentid) {
		// TODO Auto-generated method stub
		return this.baseMapper.findhasAssemblyFormNum(shipmentid);
	}

	public   List<AssemblyEntryInstock> findAssemblyFromShipment(String shipmentid) {
		// TODO Auto-generated method stub
		QueryWrapper<AssemblyEntryInstock> queryWrapper=new QueryWrapper<AssemblyEntryInstock>();
		queryWrapper.eq("shipmentid", shipmentid);
		return this.baseMapper.selectList(queryWrapper);
	}
	@Transactional
	public void cancelInStock(UserInfo user,AssemblyEntryInstock entity){
		AssemblyForm assemblyForm = assemblyFormService.getById(entity.getFormid());
		if(assemblyForm.getAmountHandle()>=entity.getAmount()) {
			assemblyForm.setAmountHandle(assemblyForm.getAmountHandle()-entity.getAmount());
		}else {
			 Material m = materialService.getById(assemblyForm.getMainmid());
			 throw new BizException(m.getSku()+"组装待处理数量错误，请联系管理员");
		}
	    assemblyForm.setAuditstatus(2);
		// 操作库存
		InventoryParameter invpara = new InventoryParameter();
		invpara.setAmount(Integer.parseInt(entity.getAmount().toString()));
		invpara.setFormid(entity.getFormid());
		invpara.setFormtype("assembly");
		invpara.setMaterial(assemblyForm.getMainmid());
		invpara.setOperator(entity.getOperator());
		invpara.setOpttime(new Date());
		invpara.setNumber(assemblyForm.getNumber());
		invpara.setShopid(assemblyForm.getShopid());
		invpara.setStatus(EnumByInventory.alReady);
		invpara.setWarehouse(assemblyForm.getWarehouseid());
		inventoryService.inStockReadyCancel(invpara);
		invpara.setStatus(EnumByInventory.Ready);
		inventoryService.inStockByReady(invpara);
		List<AssemblyFormEntry> list = assemblyFormEntryService.selectEntityByFormid( entity.getFormid());
		for (int i = 0; i < list.size(); i++) {
			AssemblyFormEntry entry = list.get(i);
			invpara = new InventoryParameter();
			int amount = (entry.getAmount() / assemblyForm.getAmount()) * entity.getAmount();
			invpara.setAmount(amount);
			invpara.setFormid(entity.getFormid());
			invpara.setFormtype("assembly");
			invpara.setMaterial(entry.getMaterialid());
			invpara.setOperator(entity.getOperator());
			invpara.setOpttime(new Date());
			invpara.setShopid(assemblyForm.getShopid());
			invpara.setStatus(EnumByInventory.alReady);
			invpara.setNumber(assemblyForm.getNumber());
			invpara.setWarehouse(entry.getWarehouseid());
			if (invpara.getAmount() > 0)
				inventoryService.outStockDirectCancel(invpara);
		    }
			this.baseMapper.deleteById(entity);
	    	assemblyFormService.updateById(assemblyForm);
	}
 public void findProcessHandle(SXSSFWorkbook workbook,Map<String, Object> param) {
	List<Map<String, Object>> list =this.baseMapper.findProcessHandle(param);
	 if(list!=null && list.size()>0) {
		//操作Excel
		Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
		titlemap.put("byday", "日期");
		titlemap.put("company", "公司");
		titlemap.put("warheousename", "仓库");
		titlemap.put("sku", "SKU");
		titlemap.put("name", "名称");
		titlemap.put("handle", "处理数量");
		titlemap.put("ftype", "处理类型");
		titlemap.put("handlesub", "消耗子产品或辅料数量");
		titlemap.put("num", "子产品或辅料数量");
		titlemap.put("sub", "子产品或辅料对应关系");
		
		Sheet sheet = workbook.createSheet("sheet1");
		// 在索引0的位置创建行（最顶端的行）
		Row trow = sheet.createRow(0);
		Object[] titlearray = titlemap.keySet().toArray();
		for (int i = 0; i < titlearray.length; i++) {
			Cell cell = trow.createCell(i); // 在索引0的位置创建单元格(左上端)
			Object value = titlemap.get(titlearray[i].toString());
			cell.setCellValue(value.toString());
		}
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> item = list.get(i);
				if(item==null) {
					continue;
				}
				item.put("company", param.get("company"));
				Row row = sheet.createRow(i + 1);
				Map<String, Object> map =item;
				for (int j = 0;j < titlearray.length; j++) {
					Cell cell = row.createCell(j); 
					String field=titlearray[j].toString();
					   if(map.get(field)!=null) {
			            	cell.setCellValue(map.get(field).toString());
						} 
				}
		    } 
		 
	 }
}

	@Override
	public void changeAssemblyFormWarehouse(UserInfo user, String number, String warehouseid) {
		LambdaQueryWrapper<AssemblyForm> queryWrapper=new LambdaQueryWrapper<AssemblyForm>();
		queryWrapper.eq(AssemblyForm::getNumber, number);
		queryWrapper.eq(AssemblyForm::getShopid, user.getCompanyid());
		AssemblyForm assForm = assemblyFormService.getOne(queryWrapper);
		List<Map<String, Object>> instockList = this.baseMapper.selectByFormId(assForm.getId());
		if(instockList!=null &&instockList.size()>0) {
			throw new BizException("已经有了入库操作记录。暂不支持更改入库仓库！");
		}else{
			//操作库存 把旧仓库 主SKU待入库减掉
			if(assForm.getFtype().equals("ass")) {
				String oldwarehouseid = assForm.getWarehouseid();
				// 操作旧库存
				InventoryParameter invpara = new InventoryParameter();
				invpara.setAmount(assForm.getAmount());
				invpara.setFormid(assForm.getId());
				invpara.setFormtype("assembly");
				invpara.setMaterial(assForm.getMainmid());
				invpara.setOperator(user.getId());
				invpara.setOpttime(new Date());
				invpara.setNumber(assForm.getNumber());
				invpara.setShopid(assForm.getShopid());
				invpara.setStatus(EnumByInventory.Ready);
				invpara.setWarehouse(oldwarehouseid);
				if (invpara.getAmount() > 0) {
					inventoryService.inStockReadyCancel(invpara);
			    }
				// 操作新的仓库库存
				InventoryParameter invpara2 = new InventoryParameter();
				invpara2.setAmount(assForm.getAmount());
				invpara2.setFormid(assForm.getId());
				invpara2.setFormtype("assembly");
				invpara2.setMaterial(assForm.getMainmid());
				invpara2.setOperator(user.getId());
				invpara2.setOpttime(new Date());
				invpara2.setNumber(assForm.getNumber());
				invpara2.setShopid(assForm.getShopid());
				invpara2.setStatus(EnumByInventory.Ready);
				invpara2.setWarehouse(warehouseid);
				if (invpara.getAmount() > 0) {
					inventoryService.inStockByReady(invpara2);
			    }
				List<AssemblyFormEntry> entryList = this.assemblyFormEntryService.selectEntityByFormid(assForm.getId());
				for (int i = 0; i < entryList.size(); i++) {
					AssemblyFormEntry entry = entryList.get(i);
					entry.setWarehouseid(warehouseid);
					this.assemblyFormEntryService.updateById(entry);	
				}
			}else {
				//拆分单 换仓库
				List<AssemblyFormEntry> entryList = this.assemblyFormEntryService.selectEntityByFormid(assForm.getId());
				for (int i = 0; i < entryList.size(); i++) {
					InventoryParameter invpa = new InventoryParameter();
					AssemblyFormEntry entry = entryList.get(i);
					long subAmount =  entry.getAmount() ;
					invpa.setAmount((int) subAmount);
					invpa.setFormid(entry.getFormid());
					invpa.setFormtype("assembly");
					invpa.setNumber(assForm.getNumber());
					invpa.setMaterial(entry.getMaterialid());
					invpa.setOperator(assForm.getOperator());
					invpa.setOpttime(new Date());
					invpa.setShopid(assForm.getShopid());
					invpa.setStatus(EnumByInventory.Ready);
					invpa.setWarehouse(assForm.getWarehouseid());
					entry.setWarehouseid(warehouseid);
					this.assemblyFormEntryService.updateById(entry);
					inventoryService.inStockReadyCancel(invpa);
				}
				
				for (int i = 0; i < entryList.size(); i++) {
					InventoryParameter invpa3 = new InventoryParameter();
					AssemblyFormEntry entry = entryList.get(i);
					long subAmount =  entry.getAmount() ;
					invpa3.setAmount((int) subAmount);
					invpa3.setFormid(entry.getFormid());
					invpa3.setFormtype("assembly");
					invpa3.setNumber(assForm.getNumber());
					invpa3.setMaterial(entry.getMaterialid());
					invpa3.setOperator(assForm.getOperator());
					invpa3.setOpttime(new Date());
					invpa3.setShopid(assForm.getShopid());
					invpa3.setStatus(EnumByInventory.Ready);
					invpa3.setWarehouse(warehouseid);
					entry.setWarehouseid(warehouseid);
					this.assemblyFormEntryService.updateById(entry);
					inventoryService.inStockByReady(invpa3);
				}
			}
			assForm.setWarehouseid(warehouseid);
			assemblyFormService.updateById(assForm);
		}
		
	}

 }
 
 
