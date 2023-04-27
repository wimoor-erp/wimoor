package com.wimoor.erp.assembly.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
