package com.wimoor.erp.assembly.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.erp.assembly.pojo.vo.AssemblyVO;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.assembly.mapper.AssemblyFormEntryMapper;
import com.wimoor.erp.assembly.mapper.AssemblyFormMapper;
import com.wimoor.erp.assembly.mapper.AssemblyMapper;
import com.wimoor.erp.assembly.pojo.dto.AssemblyFormListDTO;
import com.wimoor.erp.assembly.pojo.entity.Assembly;
import com.wimoor.erp.assembly.pojo.entity.AssemblyEntryInstock;
import com.wimoor.erp.assembly.pojo.entity.AssemblyForm;
import com.wimoor.erp.assembly.pojo.entity.AssemblyFormEntry;
import com.wimoor.erp.assembly.service.IAssemblyEntryInstockService;
import com.wimoor.erp.assembly.service.IAssemblyFormEntryService;
import com.wimoor.erp.assembly.service.IAssemblyFormService;
import com.wimoor.erp.assembly.service.IAssemblyService;
import com.wimoor.erp.common.pojo.entity.EnumByInventory;
import com.wimoor.erp.common.pojo.entity.Operate;
import com.wimoor.erp.common.pojo.entity.Status;
import com.wimoor.erp.inventory.mapper.InventoryMapper;
import com.wimoor.erp.inventory.pojo.entity.InventoryParameter;
import com.wimoor.erp.inventory.service.IInventoryFormAgentService;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.material.service.IStepWisePriceService;
import com.wimoor.erp.purchase.mapper.PurchaseFormEntryMapper;
import com.wimoor.erp.purchase.pojo.dto.PurchaseSaveDTO;
import com.wimoor.erp.purchase.pojo.entity.PurchaseForm;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormEntry;
import com.wimoor.erp.purchase.service.IPurchaseFormService;
import com.wimoor.erp.purchase.service.IPurchasePlanService;
import com.wimoor.erp.purchase.service.IPurchaseWareHouseStatusService;
import com.wimoor.erp.ship.pojo.dto.ShipInboundShipmentDTO;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import lombok.RequiredArgsConstructor;

 

@Service("assemblyFormService")
@RequiredArgsConstructor
public class AssemblyFormServiceImpl extends  ServiceImpl<AssemblyFormMapper,AssemblyForm> implements IAssemblyFormService {
	final InventoryMapper inventoryMapper;
	@Autowired
	@Lazy
	ISerialNumService serialNumService;
	 
	final AssemblyFormEntryMapper assemblyFormEntryMapper;
	@Autowired
	@Lazy
	IAssemblyService assemblyService;
	
	@Autowired
	@Lazy
	IStepWisePriceService stepWisePriceService;
	
	@Autowired
	@Lazy
	IMaterialService materialService;
	 
	final PurchaseFormEntryMapper purchaseFormEntryMapper;
	@Autowired
	@Lazy
	IPurchaseFormService purchaseFormService;
	
	@Autowired
	@Lazy
	IAssemblyFormEntryService assemblyFormEntryService;
	
	@Autowired
	@Lazy
	IInventoryFormAgentService inventoryFormAgentService;
	
	@Autowired
	@Lazy
	IPurchasePlanService purchasePlanService;
	
	@Autowired
	@Lazy
	IAssemblyEntryInstockService assemblyEntryInstockService;
	
	@Autowired
	@Lazy
	IInventoryService inventoryService;
	 
	final AssemblyMapper assemblyMapper;
	@Autowired
	@Lazy
	IWarehouseService warehouseService;
	
	@Autowired
	@Lazy
	IPurchaseWareHouseStatusService purchaseWareHouseStatusService;
	public void refreshInbound(String shopid,String warehouseid,String materialid) {
		this.baseMapper.refreshInbound(shopid, warehouseid, materialid);
	}
	public Map<String, Object> getCountNum(Map<String, Object> param) {
		Map<String, Object> numMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = this.baseMapper.getCountNum(param);
		int pendingNum = 0;
		int pendingNointerNum = 0;
		int inProgressNum = 0;
		if(list != null && list.size() > 0) {
			for(Map<String, Object> map : list) {
				String auditstatus = map.get("auditstatus").toString();
				if("1".equals(auditstatus)) {
					String needpo =null;
					if(map.get("needpo")!=null) {
						needpo = map.get("needpo").toString();
					}
					if("1".equals(needpo)) {
						pendingNointerNum++;
					}else {
						pendingNum++;
					}
				}
				else if("2".equals(auditstatus)) {
					inProgressNum++;
				}
			}
		}
		numMap.put("pendingNum", pendingNum);
		numMap.put("pendingNointerNum", pendingNointerNum);
		numMap.put("inProgressNum", inProgressNum);
		return numMap;
	}
	
	public IPage<Map<String, Object>> findByCondition(AssemblyFormListDTO dto) {
		return this.baseMapper.findByCondition(dto.getPage(),dto);
	}

	public Map<String, Object> findById(String id) {
		return this.baseMapper.findById(id);
	}

	public int findSumNeed(String shopid) {
		return this.baseMapper.findSumNeed(shopid);
	}

	private void saveEntryFormList(List<AssemblyFormEntry> list, List<AssemblyFormEntry> oldentrylist) throws BizException {
		for (AssemblyFormEntry entry : list) {
			AssemblyFormEntry old = null;
			if (oldentrylist != null && oldentrylist.size() > 0) {
				for (AssemblyFormEntry oldentry : oldentrylist) {
					if (oldentry.getMaterialid().equals(entry.getMaterialid())) {
						old = oldentry;
						oldentrylist.remove(oldentry);
						break;
					}
				}
				for (AssemblyFormEntry removeold : oldentrylist) {
					assemblyFormEntryMapper.deleteById(removeold);
				}
			}
			AssemblyForm form = this.baseMapper.selectById(entry.getFormid());
			entry.setSubnumber(entry.getAmount()/form.getAmount());
			if (old == null) {
				assemblyFormEntryService.save(entry);
			} else {
				assemblyFormEntryService.updateById(entry);
			}
		}
	}
 
	private void startOperate(AssemblyForm form, int amount_handle, UserInfo user) {
		if (form.getFtype() != null && "dis".equals(form.getFtype())) {
			startSplit(form, user);
		} else {
			startAssembly(form, amount_handle, user);
		}
	}

	void startSplit(AssemblyForm form, UserInfo user) throws BizException {
		if (form.getAuditstatus() == 1) {
			form.setAuditstatus(2);
			// 操作库存和库存记录
			if (form.getAmount() > 0) {
				InventoryParameter invpara = new InventoryParameter();
				invpara.setFormid(form.getId());
				invpara.setMaterial(form.getMainmid());
				invpara.setOperator(form.getOperator());
				invpara.setFormtype("assembly");
				invpara.setAmount(form.getAmount());
				invpara.setNumber(form.getNumber());
				invpara.setWarehouse(form.getWarehouseid());
				invpara.setStatus(EnumByInventory.Ready);
				invpara.setShopid(form.getShopid());
				invpara.setOpttime(new Date());
				inventoryFormAgentService.outStockByRead(invpara);
			}
		}
	}

	void startAssembly(AssemblyForm form, int amount_handle, UserInfo user) throws BizException {
		if (form.getAuditstatus() == 1 || form.getAuditstatus() == 2) {// 操作库存和库存记录
			form.setAuditstatus(2);
			for (int i = 0; i < form.getMyEntrylist().size(); i++) {
				InventoryParameter invpa = new InventoryParameter();
				AssemblyFormEntry entry = form.getMyEntrylist().get(i);
				Map<String, Object> inv = inventoryService.findInvDetailById(entry.getMaterialid(),
						form.getWarehouseid(), form.getShopid());
				Integer inventory = 0;
				if (inv.get("fulfillable") != null) {
					String fulstr = inv.get("fulfillable").toString();
					inventory = Integer.parseInt(fulstr);
				}
				int amount = (entry.getAmount() / form.getAmount()) * amount_handle;
				if (inventory > 0) {
					invpa.setAmount(amount);
					invpa.setFormid(entry.getFormid());
					invpa.setFormtype("assembly");
					invpa.setNumber(form.getNumber());
					invpa.setMaterial(entry.getMaterialid());
					invpa.setOperator(form.getOperator());
					invpa.setOpttime(new Date());
					invpa.setShopid(form.getShopid());
					invpa.setStatus(EnumByInventory.Ready);
					invpa.setWarehouse(form.getWarehouseid());
					inventoryFormAgentService.outStockByRead(invpa);
				}
			}
		}
	}
	List<AssemblyFormEntry> getEntry(String formid) {
 
		QueryWrapper<AssemblyFormEntry> queryWrapper=new QueryWrapper<AssemblyFormEntry>();
		queryWrapper.eq("formid", formid);
		List<AssemblyFormEntry> oldentrylist = assemblyFormEntryMapper.selectList(queryWrapper);
		return oldentrylist;
	}
 

	public Map<String, Object> saveAssemblyForm(AssemblyForm form, UserInfo user) throws BizException {
		Map<String, Object> msgMap = new HashMap<String, Object>();
		AssemblyForm old = this.baseMapper.selectById(form.getId());
		int result = 0;
		List<AssemblyVO> asslist = assemblyService.selectByMainmid(form.getMainmid());
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (AssemblyVO item : asslist) {
			map.put(item.getSubmid(), item.getSubnumber());
		}
		
		for (AssemblyFormEntry entry : form.getMyEntrylist()) {
			if (entry != null) {
				if (entry.getAmount() != map.get(entry.getMaterialid()) * form.getAmount()) {
					throw new BizException("数据出错，需求量与组装单位无法匹配");
				}
			}
		}
		List<AssemblyFormEntry> oldentrylist = null;
		if (old == null) {
			result += this.baseMapper.insert(form);
		} else {
			result += this.baseMapper.updateById(form);
			oldentrylist = getEntry(form.getId());
		}
        for(AssemblyFormEntry entry:form.getMyEntrylist()) {
        	entry.setFormid(form.getId());
        }
		saveEntryFormList(form.getMyEntrylist(), oldentrylist);
		preOperateInventory(form, user);
		if (result > 0) {
			msgMap.put("msg", "操作成功！");
		} else {
			msgMap.put("msg", "操作失败！");
		}
		msgMap.put("forms", form);
		msgMap.put("id", form.getId());
		return msgMap;
	}

	private void preOperateInventory(AssemblyForm form, UserInfo user) {
		if (form.getFtype() != null && "dis".equals(form.getFtype())) {
			readyInSubInventory(form, form.getAmount(), user);
		} else {
			readyInMainInventory(form, form.getAmount(), user);
		}
	}

	private void deletePreInventory(AssemblyForm form, UserInfo user) {
		if (form.getFtype() != null && "dis".equals(form.getFtype())) {
			readyOutSubInventory(form, form.getAmount(), user);
		} else {
			readyOutMainInventory(form, form.getAmount(), user);
		}
	}

	public Integer readyInMainInventory(AssemblyForm form, int needin, UserInfo user) throws BizException {
		Integer rescount = 0;
		if (form.getAmount() > 0) { // 操作库存和库存记录
			InventoryParameter invpara = new InventoryParameter();
			invpara.setFormid(form.getId());
			invpara.setMaterial(form.getMainmid());
			invpara.setOperator(form.getOperator());
			invpara.setFormtype("assembly");
			invpara.setAmount(needin);
			invpara.setNumber(form.getNumber());
			invpara.setWarehouse(form.getWarehouseid());
			invpara.setStatus(EnumByInventory.Ready);
			invpara.setShopid(form.getShopid());
			invpara.setOpttime(new Date());
			rescount += inventoryFormAgentService.inStockByRead(invpara);
		}
		return rescount;
	}

	Integer readyInSubInventory(AssemblyForm form, int needin, UserInfo user) throws BizException {
		Integer rescount = 0;
		for (int i = 0; i < form.getMyEntrylist().size(); i++) {
			InventoryParameter invpa = new InventoryParameter();
			AssemblyFormEntry entry = form.getMyEntrylist().get(i);
			long subAmount = (entry.getAmount() / form.getAmount()) * needin;
			invpa.setAmount((int) subAmount);
			invpa.setFormid(entry.getFormid());
			invpa.setFormtype("assembly");
			invpa.setNumber(form.getNumber());
			invpa.setMaterial(entry.getMaterialid());
			invpa.setOperator(form.getOperator());
			invpa.setOpttime(new Date());
			invpa.setShopid(form.getShopid());
			invpa.setStatus(EnumByInventory.Ready);
			invpa.setWarehouse(form.getWarehouseid());
			rescount += inventoryFormAgentService.inStockByRead(invpa);
		}
		return rescount;
	}

	public Map<String, Object> selectSubASList(String warehouseid, String materialid, String shopid) {
		return inventoryMapper.selectSubASList(warehouseid, materialid, shopid);
	}

	/**
	 *
	 */
	@Transactional
	public int deleteAssemblyForm(String id, UserInfo user) {
		// 单据对象
		AssemblyForm form = this.baseMapper.selectById(id);
		String formid = form.getId();
	 
		QueryWrapper<AssemblyFormEntry> queryWrapper=new QueryWrapper<AssemblyFormEntry>();
		queryWrapper.eq("formid", formid);
		List<AssemblyFormEntry> list = assemblyFormEntryMapper.selectList(queryWrapper);
		form.setMyEntrylist(list);
		deletePreInventory(form,user);
		form.setAuditstatus(5);
		form.setAuditor(user.getId());
		form.setAudittime(new Date());
		form.setOperator(user.getId());
		form.setOpttime(new Date());
		int reslt2 = this.baseMapper.updateById(form);
		if ( reslt2 >0) {
			return reslt2;
		} else {
			return 0;
		}
	}
	@Transactional
	public Map<String, Object> saveMutilForm(List<AssemblyForm> formlist, 
			UserInfo user,PurchaseSaveDTO dto)throws BizException {
		String planwarehouseid = dto.getPlanwarehouseid();
		Map<String, Object> msgMap = purchaseFormService.savePurchaseDataAction(dto);
		AssemblyForm form = null;
		String planitemid = null;
		if(formlist.size()==0)return msgMap;
		List<PurchaseForm> purchaseFormList=null;
		Map<String,String> materialOrder=new HashMap<String,String>();
		if( msgMap.containsKey("formList")) {
			Object purFormObject = msgMap.get("formList");
			if(purFormObject!=null) {
				purchaseFormList=(List<PurchaseForm>)purFormObject;
				for(PurchaseForm purchaseForm:purchaseFormList) {
                         List<PurchaseFormEntry> purEntrylist = purchaseForm.getEntrylist();
                         for(PurchaseFormEntry entry:purEntrylist) {
                        	 materialOrder.put(entry.getMaterialid(), entry.getId());
                         }
				}
			}
		}
		for (int i = 0; i < formlist.size(); i++) {
			form = formlist.get(i);
			form.setFtype("ass");
			for (AssemblyFormEntry entry : form.getMyEntrylist()) {
				if (materialOrder.containsKey(entry.getMaterialid())) {
					entry.setPurchaseFromEntryId(materialOrder.get(entry.getMaterialid()));
				}
			}
			if(form.getAmount()!=0) {
				msgMap.putAll(saveAssemblyForm(form, user));
			}
			if (form.getPlanitem() != null) {
				planitemid = form.getPlanitem();
			}
			if (!GeneralUtil.isEmpty(planitemid)) {
				Warehouse planwarehouse = warehouseService.getSelfWarehouse(planwarehouseid);
			}
			
		}
		return msgMap;
	}
	@Transactional
	public Map<String, Object> updateToStopOperateEvent(String formid, UserInfo user) throws BizException {
		AssemblyForm assemblyForm = this.baseMapper.selectById(formid);
		if (assemblyForm.getFtype() != null && "dis".equals(assemblyForm.getFtype())) {
			return updateToStopSplitEvent(assemblyForm, user);
		} else {
			return updateToStopAssemblyEvent(assemblyForm, user);
		}
	}

	void readyOutSubInventory(AssemblyForm assemblyForm, int needOutAmount, UserInfo user) {
		List<AssemblyFormEntry> subList = assemblyForm.getMyEntrylist();
		for (int i = 0; i < subList.size(); i++) {
			InventoryParameter invpa = new InventoryParameter();
			AssemblyFormEntry entry = subList.get(i);
			if (entry.getPhedamount() == null) {
				entry.setPhedamount(0);
			}
			long subAmount = (entry.getAmount() / assemblyForm.getAmount()) * needOutAmount;
			invpa.setAmount((int) subAmount);
			invpa.setFormid(entry.getId());
			invpa.setFormtype("assembly");
			invpa.setMaterial(entry.getMaterialid());
			invpa.setOperator(assemblyForm.getOperator());
			invpa.setOpttime(new Date());
			invpa.setShopid(assemblyForm.getShopid());
			invpa.setStatus(EnumByInventory.alReady);
			invpa.setNumber(assemblyForm.getNumber());
			invpa.setWarehouse(assemblyForm.getWarehouseid());
			inventoryService.SubStockByStatus(invpa, Status.inbound, Operate.stop);
		}
	}

	void readyOutMainInventory(AssemblyForm assemblyForm, int needOutAmount, UserInfo user) {
		InventoryParameter invpara = new InventoryParameter();
		invpara.setFormid(assemblyForm.getId());
		invpara.setMaterial(assemblyForm.getMainmid());
		invpara.setOperator(user.getId());
		invpara.setFormtype("assembly");
		invpara.setAmount(needOutAmount);
		invpara.setNumber(assemblyForm.getNumber());
		invpara.setWarehouse(assemblyForm.getWarehouseid());
		invpara.setStatus(EnumByInventory.alReady);
		invpara.setShopid(assemblyForm.getShopid());
		invpara.setOpttime(new Date());
		inventoryService.SubStockByStatus(invpara, Status.inbound, Operate.stop);
	}
	@Transactional
	public Map<String, Object> updateToStopSplitEvent(AssemblyForm assemblyForm, UserInfo user) throws BizException {
		Map<String, Object> msgMap = new HashMap<String, Object>();
		Integer oldstatus = assemblyForm.getAuditstatus();
		assemblyForm.setAuditstatus(4);
		String formid = assemblyForm.getId();
		List<Map<String, Object>> instocklist = assemblyEntryInstockService.selectByFormId(formid);
		int amount = 0;// 已经入库数量
		if (instocklist != null && instocklist.size() > 0) {
			for (int i = 0; i < instocklist.size(); i++) {
				amount = amount + Integer.parseInt(instocklist.get(i).get("amount").toString());
			}
		}
		int retAmount = assemblyForm.getAmount() - amount;
		if (retAmount > 0 && oldstatus == 2) {
//			InventoryParameter invpara = new InventoryParameter();
//			invpara.setFormid(formid);
//			invpara.setMaterial(assemblyForm.getMainmid());
//			invpara.setOperator(assemblyForm.getOperator());
//			invpara.setFormtype("assembly");
//			invpara.setAmount(retAmount);
//			invpara.setNumber(assemblyForm.getNumber());
//			invpara.setWarehouse(assemblyForm.getWarehouseid());
//			invpara.setStatus(EnumByInventory.alReady);
//			invpara.setShopid(assemblyForm.getShopid());
//			invpara.setOpttime(new Date());
//			inventoryService.SubStockByStatus(invpara, Status.outbound, Operate.stop);
//			inventoryService.AddStockByStatus(invpara, Status.fulfillable, Operate.stop);
			////////////////// 子SKU清除outbound库存，还原到fulfillable////////////////////////////
			List<AssemblyFormEntry> subList = assemblyFormEntryService.selectEntityByFormid(formid);
			assemblyForm.setMyEntrylist(subList);
			this.readyOutSubInventory(assemblyForm, retAmount, user);
		}
		if (this.updateById(assemblyForm)) {
			msgMap.put("status", assemblyForm.getAuditstatus());
			msgMap.put("msg", "操作成功！");
		} else {
			msgMap.put("msg", "操作失败！");
		}
		return msgMap;
	}
	@Transactional
	public Map<String, Object> updateToStopAssemblyEvent(AssemblyForm assemblyForm, UserInfo user) throws BizException {
		Map<String, Object> msgMap = new HashMap<String, Object>();
		Integer oldstatus = assemblyForm.getAuditstatus();
		assemblyForm.setAuditstatus(4);
		assemblyForm.setOperator(user.getId());
		assemblyForm.setOpttime(new Date());
		String formid = assemblyForm.getId();
		List<Map<String, Object>> instocklist = assemblyEntryInstockService.selectByFormId(formid);
		int amount = 0;// 已经入库数量
		if (instocklist != null && instocklist.size() > 0) {
			for (int i = 0; i < instocklist.size(); i++) {
				amount = amount + Integer.parseInt(instocklist.get(i).get("amount").toString());
			}
		}
		int retAmount = assemblyForm.getAmount() - amount;
		int retAmount_handle = assemblyForm.getAmountHandle() - amount;
		if (oldstatus == 2) {
			List<AssemblyFormEntry> subList = assemblyFormEntryService.selectEntityByFormid(formid);
			assemblyForm.setMyEntrylist(subList);
			if (retAmount > 0) {
				this.readyOutMainInventory(assemblyForm, retAmount, user);
			}
			////////////////// 子SKU清除outbound库存，还原到fulfillable////////////////////////////
//			if (retAmount_handle > 0) {
//				for (int i = 0; i < subList.size(); i++) {
//					InventoryParameter invpa = new InventoryParameter();
//					AssemblyFormEntry entry = subList.get(i);
//					if (entry.getPhedamount() == null) {
//						entry.setPhedamount(0);
//					}
//					// 需要减掉的outbound数量=
//					long subAmount = (entry.getAmount() / assemblyForm.getAmount()) * retAmount_handle;
//					invpa.setAmount((int) subAmount);
//					invpa.setFormid(entry.getId());
//					invpa.setFormtype("assembly");
//					invpa.setMaterial(entry.getMaterialid());
//					invpa.setOperator(assemblyForm.getOperator());
//					invpa.setOpttime(new Date());
//					invpa.setShopid(assemblyForm.getShopid());
//					invpa.setStatus(EnumByInventory.alReady);
//					invpa.setNumber(assemblyForm.getNumber());
//					invpa.setWarehouse(assemblyForm.getWarehouseid());
//					inventoryService.SubStockByStatus(invpa, Status.outbound, Operate.stop);
//					inventoryService.AddStockByStatus(invpa, Status.fulfillable, Operate.stop);
//				}
//			}
		}
		if ( this.updateById(assemblyForm)) {
			msgMap.put("status", assemblyForm.getAuditstatus());
			msgMap.put("msg", "操作成功！");
		} else {
			msgMap.put("msg", "操作失败！");
		}
		return msgMap;
	}

	public AssemblyForm getLastOneFormByMaterial(Object id) {
		if (id == null)
			return null;
	 
		IPage<AssemblyForm> list = this.baseMapper.findLastByMaterial(new Page<AssemblyForm>(0,1),id.toString().trim());
		if (list != null && list.getRecords().size() == 1) {
			return list.getRecords().get(0);
		}
		return null;
	}
	
	public List<AssemblyForm> getLastFormsByMaterials(List<String> ids) {
		if (ids == null||ids.size()==0)
			return null;
		List<AssemblyForm> list = this.baseMapper.findLastByMaterials(ids);
		return list;
	}
	

	public List<AssemblyForm> getLastFormByMaterial(Object id, int i) {
		if (id == null) {
			return null;
		}
		IPage<AssemblyForm> list = this.baseMapper.findLastByMaterial(new Page<AssemblyForm>(1,i),id.toString());
		return list.getRecords();
	}

	@Transactional
	public Map<String, Object> updateAssemblyEvent(String formid, int amount_handle, UserInfo user) throws BizException {
		Map<String, Object> map=new HashMap<String, Object>();
		AssemblyForm form = this.baseMapper.selectById(formid);
		if (form.getAmount() < (form.getAmountHandle() + amount_handle)) {
			throw new BizException("当前可处理数量大于总处理数量！");
		}
		form.setAmountHandle(form.getAmountHandle() + amount_handle);
		List<AssemblyFormEntry> oldentrylist = getEntry(form.getId());
		form.setMyEntrylist(oldentrylist);
		//startOperate(form, amount_handle, user);
		this.updateById(form);
		map.put("msg", "操作成功!");
		map.put("form", form);
		return map;
	}

	public Map<String, Object> getLastAssRecord(String shopid, String warehouseid) {
		return this.baseMapper.getLastAssRecord(shopid, warehouseid);
		
	}

	public List<Map<String, Object>> getAssemblySumReport(Map<String, Object> param) {
		return this.baseMapper.getAssemblySumReport(param);
	}
	@Transactional
	public Map<String, Object> updateAssemblyAmount(String formid, String value, UserInfo user) {
		HashMap<String, Object> msg = new HashMap<String, Object>();
		if (GeneralUtil.isEmpty(formid)) {
			throw new BizException("找不到对应采购单");
		}
		AssemblyForm form = getById(formid);
		if (form == null) {
			throw new BizException("找不到对应采购单");
		}
		if (GeneralUtil.isEmpty(value)) {
			throw new BizException("组装数量不能为空");
		}
		int amount = Integer.parseInt(value);
		if (amount<form.getAmountHandle()) {
			throw new BizException("组装数量不能小于已处理数量");
		}
		if(!form.getFtype().equals("ass")) {
			throw new BizException("拆分单不能修改拆分数量");
		}
		if (amount > form.getAmount()) {// add
			this.readyInMainInventory(form, amount - form.getAmount(), user);
		} else if (amount < form.getAmount()) {// sub
			readyOutMainInventory(form, form.getAmount() - amount, user);
		}
		if(amount==form.getAmountHandle()) {
			form.setAuditstatus(3);
		}
		form.setAmount(amount);
		int count = this.baseMapper.updateById(form);
		List<Assembly> list = assemblyService.selectAssemblySub(form.getMainmid());
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (Assembly sub : list) {
			map.put(sub.getSubmid(),  sub.getSubnumber());
		}
		List<AssemblyFormEntry> entrylist = getEntry(form.getId());
		for (AssemblyFormEntry entry : entrylist) {
			entry.setAmount(amount * map.get(entry.getMaterialid()));
			assemblyFormEntryMapper.updateById(entry);
		}
		if (count > 0) {
			msg.put("message", "更新成功");
		}else {
			msg.put("message", "更新失败");
		}
		msg.put("value", amount);
		return msg;
	}
	@Transactional
	public String assemblyCompareToSku(String sku, String shopid, JSONArray array) {
		Material Mainmaterial = materialService.findBySKU(sku, shopid);
		if (Mainmaterial == null) {
			return "SKU:" + sku + "已经不存在！";
		}
		String mainid = Mainmaterial.getId();
		QueryWrapper<Assembly> queryWrapper=new QueryWrapper<Assembly>();
		queryWrapper.eq("mainmid", mainid);
		List<Assembly> list = assemblyMapper.selectList(queryWrapper);
		Map<String,Integer> map=new HashMap<String,Integer>();
		for(Assembly item:list) {
			map.put(item.getSubmid(), item.getSubnumber());
		}
		Boolean hasCompareSku = false;
		for (int i = 0; i < array.size(); i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			String materialId = jsonObject.get("materialid").toString();
			String subNumber = jsonObject.get("num").toString();
			Integer itemqty=map.get(materialId);
			if (itemqty == null||subNumber==null ||itemqty!=Integer.parseInt(subNumber)) {
				hasCompareSku = true;
			}
		}
		if (hasCompareSku) {
			return "当前子产品列表的单位数量与提交表单时不一致,请注意！";
		} else {
			return null;
		}
	}
	@Transactional
    public void createNewAssemblyFormByShipment(UserInfo user, String warehouseid, Material material,
			ShipInboundShipmentDTO shipment, int needassembly) {
    	    AssemblyForm assemblyform = new AssemblyForm();
    	    assemblyform.setWarehouseid(warehouseid);
    	    assemblyform.setShopid(user.getCompanyid());
    	    assemblyform.setAuditor(user.getId());
    	    assemblyform.setAmount(needassembly);
    	    assemblyform.setAmountHandle(needassembly);
    	    assemblyform.setAuditstatus(3);
    	    assemblyform.setAudittime(new Date());
    	    assemblyform.setCreator(user.getId());
    	    assemblyform.setCreatedate(new Date());
    	    assemblyform.setFtype("ass");
    	    assemblyform.setMainmid(material.getId());
    	    assemblyform.setNumber(shipment.getShipmentid());
    	    assemblyform.setOperator(user.getId());
    	    assemblyform.setOpttime(new Date());
    	    assemblyform.setPlanitem(null);
    	    assemblyform.setRemark("货件【"+shipment.getShipmentid()+"】自动创建");
    	    List<Map<String, Object>> sublist = assemblyService.selectByMainDetailmid(material.getId(),warehouseid);
    	    if(sublist==null||sublist.size()==0) {
    	    	throw new BizException("主产品【"+material.getSku()+"】库存不足，系统自动组装，无法找到组装清单");
    	    }
    	    this.save(assemblyform);
			for (int i = 0; i < sublist.size(); i++) {
				InventoryParameter invpa = new InventoryParameter();
				Map<String, Object> assembly = sublist.get(i);
				String submid=assembly.get("submid").toString();
				int subnumber =0;
				int amount=0;
				if(assembly.get("subnumber")!=null) {
					subnumber=Integer.parseInt(assembly.get("subnumber").toString());
					amount=subnumber*needassembly;
				}
				int inventoryqty=0;
				if(assembly.get("inventoryqty")!=null) {
					inventoryqty=Integer.parseInt(assembly.get("inventoryqty").toString());
				}
				if(subnumber<=0) {
					throw new BizException("主产品【"+material.getSku()+"】库存不足，系统自动组装子产品【"+assembly.get("sku")+"】组装清单错误");
				}
				else if(inventoryqty<amount) {
	            	throw new BizException("主产品【"+material.getSku()+"】库存不足，系统自动组装子产品【"+assembly.get("sku")+"】库存不足");
	            }
				AssemblyFormEntry entry = new AssemblyFormEntry();
				entry.setFormid(assemblyform.getId());
				entry.setMaterialid(submid);
				entry.setWarehouseid(assemblyform.getWarehouseid());
				entry.setWhamount(null);
				entry.setPhamount(0);
				entry.setPhedamount(0);
				entry.setAmount(amount);
				assemblyFormEntryMapper.insert(entry);
				if (entry.getPhedamount() == null) {
					entry.setPhedamount(0);
				}
				// 需要减掉的outbound数量=
				long subAmount = needassembly;
				invpa.setAmount((int) subAmount);
				invpa.setFormid(entry.getId());
				invpa.setFormtype("assembly");
				invpa.setMaterial(entry.getMaterialid());
				invpa.setOperator(user.getId());
				invpa.setOpttime(new Date());
				invpa.setShopid(user.getCompanyid());
				invpa.setStatus(EnumByInventory.alReady);
				invpa.setNumber(shipment.getShipmentid());
				invpa.setWarehouse(assemblyform.getWarehouseid());
				inventoryService.SubStockByStatus(invpa, Status.fulfillable, Operate.out);
			}
    	    InventoryParameter invpa1 = new InventoryParameter();
    	    invpa1.setAmount(needassembly);
			invpa1.setFormid(assemblyform.getId());
			invpa1.setFormtype("assembly");
			invpa1.setMaterial(material.getId());
			invpa1.setOperator(user.getId());
			invpa1.setOpttime(new Date());
			invpa1.setShopid(user.getCompanyid());
			invpa1.setStatus(EnumByInventory.alReady);
			invpa1.setNumber(shipment.getShipmentid());
			invpa1.setWarehouse(warehouseid);
			inventoryService.AddStockByStatus(invpa1, Status.fulfillable, Operate.in);
			
			AssemblyEntryInstock assembRecord = new AssemblyEntryInstock();
			assembRecord.setAmount(needassembly);
			assembRecord.setFormid(assemblyform.getId());
			assembRecord.setOperator(user.getId());
			assembRecord.setOpttime(new Date());
			assembRecord.setRemark("系统货件【"+shipment.getShipmentid()+"】自动组装");
			assembRecord.setWarehouseid(null);
			assembRecord.setShipmentid(shipment.getShipmentid());
			assemblyEntryInstockService.save(assembRecord);
			
			
    }
    @Transactional
	public void createAssemblyFormByShipment(UserInfo user, String warehouseid, Material material,
			ShipInboundShipmentDTO shipment, int needassembly) {
		// TODO Auto-generated method stub
		Integer assamount=assemblyService.findCanAssembly(material.getId(), warehouseid, user.getCompanyid());
		if(assamount==null) {
			assamount=0;
		}
		if(assamount<needassembly) {
			throw new BizException("本地SKU【"+material.getSku()+"】库存不足，尝试使用待组装库存，因子产品库存不足无法组装");
		}
		List<AssemblyForm> formlist = this.baseMapper.getCanAssemblyFormByMaterial(user.getCompanyid(), warehouseid, material.getId());
		for(int i=0;i<formlist.size()&&needassembly>0;i++) {
			AssemblyForm item = formlist.get(i);
			if(item.getAmountHandle()==null) {
				item.setAmountHandle(0);
			}
			int canass = item.getAmount()-item.getAmountHandle();
			if(canass<needassembly) {
				//commit
				updateAssemblyEvent(item.getId(),canass,user);
				AssemblyEntryInstock assembRecord = new AssemblyEntryInstock();
				assembRecord.setAmount(canass);
				assembRecord.setFormid(item.getId());
				assembRecord.setOperator(user.getId());
				assembRecord.setOpttime(new Date());
				assembRecord.setRemark("系统货件【"+shipment.getShipmentid()+"】自动组装");
				assembRecord.setWarehouseid(null);
				assembRecord.setShipmentid(shipment.getShipmentid());
			    assemblyEntryInstockService.saveAssemblyInStock(assembRecord, item, user);
				needassembly=needassembly-canass;
			}else {
				//commit
				updateAssemblyEvent(item.getId(),needassembly,user);
				AssemblyEntryInstock assembRecord = new AssemblyEntryInstock();
				assembRecord.setAmount(needassembly);
				assembRecord.setFormid(item.getId());
				assembRecord.setOperator(user.getId());
				assembRecord.setOpttime(new Date());
				assembRecord.setRemark("系统货件【"+shipment.getShipmentid()+"】自动组装");
				assembRecord.setWarehouseid(null);
				assembRecord.setShipmentid(shipment.getShipmentid());
			    assemblyEntryInstockService.saveAssemblyInStock(assembRecord, item, user);
				needassembly=0;
			}
			
		}
	if(needassembly>0) {
		throw new BizException("本地SKU【"+material.getSku()+"】库存不足，尝试使用待组装库存，因没有找到可用组装单无法自动组装。");
		//createNewAssemblyFormByShipment(user,warehouseid,material,shipment,needassembly);
	}
	
	}
	@Transactional
	public void cancelByShipment(UserInfo user, ShipInboundShipmentDTO shipment) {
		// TODO Auto-generated method stub
		  List<AssemblyEntryInstock> list = assemblyEntryInstockService.findAssemblyFromShipment(shipment.getShipmentid());
		  for(AssemblyEntryInstock item:list) {
			    assemblyEntryInstockService.cancelInStock(user, item);
				AssemblyForm assemblyForm = this.getById(item.getFormid());
				if(assemblyForm.getAmountHandle()==0&&assemblyForm.getNumber().equals(shipment.getShipmentid())) {
					this.deleteAssemblyForm(assemblyForm.getId(), user);
				}
		  }
	}

	public Integer findhasAssemblyFromShipment(String shipmentid) {
		// TODO Auto-generated method stub
		return assemblyEntryInstockService.findhasAssemblyFromShipment(shipmentid);
	}

	@Override
	@Transactional
	public Map<String, Object> resetAssForm(UserInfo user,String id) {
		Map<String, Object> maps=new HashMap<String, Object>();
		AssemblyForm  assform = this.baseMapper.selectById(id);
		if(assform!=null) {
			int amountvalue= assform.getAmount();
			int handlervalue=assform.getAmountHandle();
			assform.setAuditstatus(2);//组装中
			//把主SKU的待入库加回来
			InventoryParameter para=new InventoryParameter();
			Status status=Status.inbound;
			Operate operate=Operate.in;
			para.setFormid(assform.getId());
			para.setMaterial(assform.getMainmid());
			para.setWarehouse(assform.getWarehouseid());
			if(amountvalue-handlervalue<0) {
				para.setAmount(0);		
			}else {
				para.setAmount(amountvalue-handlervalue);
			}
			para.setOperator(user.getId());
			para.setOpttime(new Date());
			para.setStatus(EnumByInventory.alReady);
			para.setShopid(assform.getShopid());
			para.setFormtype("assembly");
			inventoryService.AddStockByStatus(para, status, operate);
			int res = this.baseMapper.updateById(assform);
			if(res>0) {
				maps.put("msg", "操作成功!");
				maps.put("code", "success");
			}else {
				maps.put("msg", "操作失败!");
				maps.put("code", "fail");
			}
			 
		}
		return maps;
	}

}
