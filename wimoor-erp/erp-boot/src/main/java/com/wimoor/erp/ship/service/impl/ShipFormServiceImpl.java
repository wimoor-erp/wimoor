package com.wimoor.erp.ship.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import javax.annotation.Resource;

import cn.hutool.core.bean.BeanUtil;
import com.wimoor.common.service.impl.SystemLogAspect;
import com.wimoor.erp.ship.pojo.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wimoor.common.mvc.BizException;
import com.wimoor.common.mvc.FileUpload;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.api.AmazonClientOneFeignManager;
import com.wimoor.erp.assembly.service.IAssemblyFormService;
import com.wimoor.erp.common.pojo.entity.EnumByInventory;
import com.wimoor.erp.inventory.pojo.entity.InventoryParameter;
import com.wimoor.erp.inventory.service.IInventoryFormAgentService;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IMaterialConsumableService;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.ship.mapper.ShipPlanItemMapper;
import com.wimoor.erp.ship.service.IShipFormService;
import com.wimoor.erp.warehouse.service.IWarehouseService;
import com.wimoor.erp.warehouse.service.IWarehouseShelfInventoryOptRecordService;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrConfig;

@Service
public class ShipFormServiceImpl implements IShipFormService{
	@Autowired
	IInventoryService inventoryService;
	 
	@Autowired
	IWarehouseService warehouseService;
	 
	@Autowired
	IInventoryFormAgentService inventoryFormAgentService;
	
	@Autowired
	IMaterialService materialService;
	
	@Autowired
	IAssemblyFormService assemblyFormService;
	
	@Autowired
	AmazonClientOneFeignManager amazonClientOneFeign;
	
	@Autowired
	FileUpload fileUpload;
	
	@Autowired
	IMaterialConsumableService materialConsumableService;
	
	@Autowired
	IWarehouseShelfInventoryOptRecordService iWarehouseShelfInventoryOptRecordService;
	
	@Autowired
	ShipPlanItemMapper shipPlanItemMapper;
	@Resource
    QrConfig qrconfig;
    @Autowired
    private SystemLogAspect systemLogAspect;

	public void fulfillableToOutbound(UserInfo user,ShipFormDTO dto) {
		String warehouseid=dto.getWarehouseid();
		String formid=dto.getFormid();
		String number=dto.getNumber();
		for (int i = 0; i < dto.getList().size(); i++) {
			ShipItemDTO item = dto.getList().get(i);
			int shipqty =item.getQuantity();
		    String sellersku=item.getSku();
			String materialid=null;
			if(materialid==null) {
				Material m = materialService.getBySku(user.getCompanyid(),item.getMsku());
				if(m==null) {
					throw new BizException("平台SKU【"+item.getSku()+"】对应本地SKU【"+item.getMsku()+"】无法找到对应信息");
				}
				materialid=m.getId();
				item.setMaterialid(m.getId());
			}
	 
			 Map<String, Object> map = inventoryService.findInvDetailById(materialid, warehouseid, user.getCompanyid());
			 int fulfillable=0;
			 if(map!=null&&map.containsKey("fulfillable")) {
				 Object fulobj = map.get("fulfillable");
                if(fulobj!=null&&StrUtil.isNotEmpty(fulobj.toString())) {
                	fulfillable = Integer.parseInt(fulobj.toString());
                }
			 }
			 if(fulfillable<shipqty) {
				 Material material = materialService.getById(materialid);
				 if(material==null) {
					 throw new BizException("平台SKU["+sellersku+"]无法对应本地产品");
				 }
				 else if(!"1".equals(material.getIssfg())) {
					 throw new BizException("平台SKU["+sellersku+"]对应本地产品SKU["+material.getSku()+"]库存不足无法发货");
				 }
				 int needassembly = shipqty-fulfillable;
				 assemblyFormService.createAssemblyFormByShipment(user,warehouseid,material,dto.getNumber(),needassembly);
			 }
			addMaterialFullfillableToOutbound(user, number,formid,warehouseid,materialid ,shipqty);
		  
		}
	};

	private void addMaterialFullfillableToOutbound(UserInfo user,String number,String formid, String warehouseid, String material, Integer amount) {
		InventoryParameter para = new InventoryParameter();
		para.setAmount(amount);
		para.setFormid(formid);
		para.setMaterial(material);
		EnumByInventory statusinv = EnumByInventory.Ready;
		para.setStatus(statusinv);
		para.setOperator(user.getId());
		para.setOpttime(new Date());
		para.setWarehouse(warehouseid);
		para.setFormtype("outstockform");
		para.setNumber(number);
		para.setShopid(user.getCompanyid());
	    inventoryFormAgentService.outStockByRead(para);
	}

	private void subMaterialOutbound(UserInfo user,String number,String formid, String warehouseid, String material, Integer amount) {
		InventoryParameter para = new InventoryParameter();
		para.setAmount(amount);
		para.setFormid(formid);
		para.setMaterial(material);
		EnumByInventory statusinv = EnumByInventory.alReady;
		para.setStatus(statusinv);
		para.setOperator(user.getId());
		para.setOpttime(new Date());
		para.setWarehouse(warehouseid);
		para.setFormtype("outstockform");
		para.setNumber(number);
		para.setShopid(user.getCompanyid());
	    inventoryFormAgentService.outStockByRead(para);
	}
	
	public void addFullfillableToOutboundChange(String userid,String shopid, String formid,String number,String warehouseid, String material, Integer amount) {
		InventoryParameter para = new InventoryParameter();
		para.setAmount(amount);
		para.setFormid(formid);
		para.setMaterial(material);
		EnumByInventory statusinv = EnumByInventory.Ready;
		para.setStatus(statusinv);
		para.setOperator(userid);
		para.setOpttime(new Date());
		para.setWarehouse(warehouseid);
		para.setFormtype("outstockform");
		para.setNumber(number);
		para.setShopid(shopid);
        inventoryFormAgentService.outStockByReadChange(para);
	}
	public void subFulfillableToOutboundChange(String userid, String shopid,String formid,String number,String warehouseid, String material, Integer amount) {
		InventoryParameter para = new InventoryParameter();
		para.setAmount(amount);
		para.setMaterial(material);
		EnumByInventory statusinv = EnumByInventory.Ready;
		para.setStatus(statusinv);
		para.setFormid(formid);
		para.setOpttime(new Date());
		para.setOperator(userid);
		para.setWarehouse(warehouseid);
		para.setFormtype("outstockform");
		para.setShopid(shopid);
		para.setNumber(number);
		inventoryFormAgentService.outStockReadyChange(para);
	}
 
	private void cancelfulfillable(UserInfo user,ShipFormDTO dto) throws BizException {
		   List<ShipItemDTO> listmap = dto.getList();
		for (int i = 0; i < listmap.size(); i++) {
			ShipItemDTO item = listmap.get(i);
			if(item.getMaterialid()==null) {
				if(item.getMsku()==null) {
					item.setMsku(item.getSku());
				}
				Material m = this.materialService.getBySku(user.getCompanyid(), item.getMsku());
				item.setMaterialid(m.getId());
			}
			InventoryParameter para = new InventoryParameter();
			Integer amount = item.getQuantity();
			para.setAmount(amount);
			para.setMaterial(item.getMaterialid());
			EnumByInventory statusinv = EnumByInventory.Ready;
			para.setStatus(statusinv);
			para.setFormid(dto.getFormid());
			para.setOpttime(new Date());
			para.setWarehouse(dto.getWarehouseid());
			para.setFormtype("outstockform");
			para.setShopid(user.getCompanyid());
			para.setNumber(dto.getNumber());
			para.setOperator(user.getId());
			inventoryFormAgentService.outStockDirectCancel(para);
		 
		}
	}

 
	 
	private void subMaterialFulfillableToOutbound(UserInfo user,String formid,String number,String warehouseid, String material, Integer amount) {
		InventoryParameter para = new InventoryParameter();
		para.setAmount(amount);
		para.setMaterial(material);
		EnumByInventory statusinv = EnumByInventory.Ready;
		para.setStatus(statusinv);
		para.setFormid(formid);
		para.setOpttime(new Date());
		para.setOperator(user.getId());
		para.setWarehouse(warehouseid);
		para.setFormtype("outstockform");
		para.setShopid(user.getCompanyid());
		para.setNumber(number);
	    inventoryFormAgentService.outStockReadyCancel(para);
	  
	}


	
	public void fulfillableOut(UserInfo user,ShipFormDTO dto) throws BizException {
		  List<ShipItemDTO> listmap = dto.getList();
		for (int i = 0; i < listmap.size(); i++) {
			ShipItemDTO item = listmap.get(i);
			if(item.getMsku()!=null && item.getQuantity()!=null) {
				Material m = this.materialService.getBySku(user.getCompanyid(), item.getMsku());
				item.setMaterialid(m.getId());
				subFulfillableOut(user, dto.getFormid(),dto.getNumber(),dto.getWarehouseid(),item.getMaterialid(),item.getQuantity());
			}
		}
	}
	
	private void subFulfillableOut(UserInfo user, String formid,String number,String warehouseid, String material, Integer amount) {
		InventoryParameter para = new InventoryParameter();
		para.setAmount(amount);
		para.setMaterial(material);
		para.setFormid(formid);
		para.setOpttime(new Date());
		para.setOperator(user.getId());
		para.setWarehouse(warehouseid);
		para.setFormtype("outstockform");
		para.setShopid(user.getCompanyid());
		para.setNumber(number);
	    inventoryFormAgentService.outStockByDirect(para);
	}
	
	private void cancelfulfillableToOutbound(UserInfo user, ShipFormDTO dto) throws BizException {
		   List<ShipItemDTO> itemlist = dto.getList();
		   for (int i = 0; i < itemlist.size(); i++) {
			ShipItemDTO item = itemlist.get(i);
			if(item.getMaterialid()==null) {
				if(item.getMsku()==null) {
					item.setMsku(item.getSku());
				}
				Material m = this.materialService.getBySku(user.getCompanyid(), item.getMsku());
				item.setMaterialid(m.getId());
			}
			subMaterialFulfillableToOutbound(user,dto.getFormid(),dto.getNumber(),dto.getWarehouseid(),item.getMaterialid(),item.getQuantity());
		}
	}
	
	 
	
	@Override
	@Transactional
	public String updateDisable(UserInfo user, ShipFormDTO dto) {
		// TODO Auto-generated method stub
				if (dto.getOpttype()!=null&&dto.getOpttype().contains("cancelfulfillable")) {
					cancelfulfillable(user, dto);
				} else {
					cancelfulfillableToOutbound(user, dto);
				}
				if(dto.getOpttype()!=null&&dto.getOpttype().contains("cancelassembly")) {
					List<ShipItemDTO> listmap = dto.getList();
					Map<String, Integer> shipmap=new HashMap<String, Integer>();
					for (int i = 0; i < listmap.size(); i++) {
						ShipItemDTO item = listmap.get(i);
						if(item.getMaterialid()==null) {
							if(item.getMsku()==null) {
								item.setMsku(item.getSku());
							}
							Material m = this.materialService.getBySku(user.getCompanyid(), item.getMsku());
							item.setMaterialid(m.getId());
						}
						shipmap.put(item.getMaterialid(), item.getQuantity());
					}
					assemblyFormService.cancelByForm(user,dto.getNumber(),shipmap);
				}
			return "success";
	}

 

	public void updateItemQty(UserInfo user, ShipFormDTO dto) {
		List<ShipItemDTO> itemlist = dto.getList();
		for (int i = 0; i <itemlist.size(); i++) {
			ShipItemDTO item = itemlist.get(i);
			if(item.getMaterialid()==null) {
				if(item.getMsku()==null) {
					item.setMsku(item.getSku());
				}
				Material m = this.materialService.getBySku(user.getCompanyid(), item.getMsku());
				item.setMaterialid(m.getId());
			}
			 
			Integer qty = item.getQuantity();
				if (qty > 0) {// 修改数量大于计划数量,changeadd
					addFullfillableToOutboundChange(user.getId(),user.getCompanyid(),dto.getFormid(),
							dto.getNumber(),dto.getWarehouseid(), item.getMaterialid(),  qty);
				} else if (qty < 0){// 修改数量小于计划数量,changesub
					subFulfillableToOutboundChange(user.getId(),user.getCompanyid(),dto.getFormid(),
							dto.getNumber(),dto.getWarehouseid(),item.getMaterialid(), qty*-1);
				}
		}
	}

	@Override
	public void subOutbound(UserInfo user, ShipFormDTO dto) {
		// TODO Auto-generated method stub
		String warehouseid=dto.getWarehouseid();
		String formid=dto.getFormid();
		String number=dto.getNumber();
		for (int i = 0; i < dto.getList().size(); i++) {
			ShipItemDTO item = dto.getList().get(i);
			int shipqty =item.getQuantity();
			String materialid=null;
			if(materialid==null) {
				Material m = materialService.getBySku(user.getCompanyid(),item.getMsku());
				if(m==null) {
					throw new BizException("平台SKU【"+item.getSku()+"】对应本地SKU【"+item.getMsku()+"】无法找到对应信息");
				}
				materialid=m.getId();
				item.setMaterialid(m.getId());
			}
			 subMaterialOutbound(user, number,formid,warehouseid,materialid ,shipqty);
		}
		
	}


}
