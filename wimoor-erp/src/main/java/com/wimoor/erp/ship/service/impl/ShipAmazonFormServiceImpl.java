package com.wimoor.erp.ship.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wimoor.api.amzon.inbound.pojo.dto.ShipInboundItemDTO;
import com.wimoor.api.amzon.inbound.pojo.dto.ShipInboundPlanDTO;
import com.wimoor.api.amzon.inbound.pojo.dto.ShipInboundShipmentDTO;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.api.AmazonClientOneFeign;
import com.wimoor.erp.assembly.service.IAssemblyFormService;
import com.wimoor.erp.common.pojo.entity.EnumByInventory;
import com.wimoor.erp.common.pojo.entity.Operate;
import com.wimoor.erp.common.pojo.entity.Status;
import com.wimoor.erp.inventory.pojo.entity.InventoryParameter;
import com.wimoor.erp.inventory.service.IInventoryFormAgentService;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.ship.pojo.dto.ShipAmazonShipmentDTO;
import com.wimoor.erp.ship.service.IShipAmazonFormService;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import cn.hutool.core.util.StrUtil;

@Service
public class ShipAmazonFormServiceImpl implements IShipAmazonFormService{
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
	AmazonClientOneFeign amazonClientOneFeign;
	
	public void fulfillableToOutbound(UserInfo user, ShipInboundShipmentDTO shipmentobj) {
		for (int i = 0; i < shipmentobj.getItemList().size(); i++) {
			 ShipInboundItemDTO item = shipmentobj.getItemList().get(i);
			int shipqty =item.getQuantityshipped();
		    String sellersku=item.getSellersku();
			String materialid=item.getMaterialid();
			String warehouseid=shipmentobj.getInboundplan().getWarehouseid();
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
				 assemblyFormService.createAssemblyFormByShipment(user,warehouseid,material,shipmentobj,needassembly); 
			 }
			addMaterialFullfillableToOutbound(user, shipmentobj.getInboundplan(),materialid ,shipqty);
		  
		}
	};

	private void outbound(UserInfo user, ShipInboundShipmentDTO shipment) throws BizException {
		ShipInboundPlanDTO plan = shipment.getInboundplan();
		for (int i = 0; i < shipment.getItemList().size(); i++) {
			ShipInboundItemDTO item = shipment.getItemList().get(i);
			InventoryParameter para = new InventoryParameter();
			Integer shiped = item.getQuantity();
			if (item.getQuantityshipped() != null) {
				shiped =item.getQuantityshipped();
			}
			para.setAmount(shiped);
			para.setShopid(user.getCompanyid());
			para.setNumber(plan.getNumber());
			para.setFormid(plan.getId());
			EnumByInventory statusinv = EnumByInventory.Ready;
			para.setStatus(statusinv);
			para.setOperator(user.getId());
			para.setOpttime(new Date());
			para.setFormid(plan.getId());
			para.setMaterial(item.getMaterialid());
			para.setOperator(plan.getOperator());
			para.setOpttime(new Date());
			para.setWarehouse(plan.getWarehouseid());
			para.setFormtype("outstockform");
	        inventoryService.SubStockByStatus(para, Status.outbound, Operate.out);
			 
		}
	}



	private void addMaterialFullfillableToOutbound(UserInfo user, ShipInboundPlanDTO plan, String material, Integer amount) {
		InventoryParameter para = new InventoryParameter();
		para.setAmount(amount);
		para.setFormid(plan.getId());
		para.setMaterial(material);
		EnumByInventory statusinv = EnumByInventory.Ready;
		para.setStatus(statusinv);
		para.setOperator(user.getCompanyid());
		para.setOpttime(new Date());
		para.setWarehouse(plan.getWarehouseid());
		para.setFormtype("outstockform");
		para.setNumber(plan.getNumber());
		para.setShopid(plan.getShopid());
	    inventoryFormAgentService.outStockByRead(para);
	}

	private void addFullfillableToOutboundChange(UserInfo user, ShipInboundPlanDTO plan, String material, Integer amount) {
		InventoryParameter para = new InventoryParameter();
		para.setAmount(amount);
		para.setFormid(plan.getId());
		para.setMaterial(material);
		EnumByInventory statusinv = EnumByInventory.Ready;
		para.setStatus(statusinv);
		para.setOperator(user.getId());
		para.setOpttime(new Date());
		para.setWarehouse(plan.getWarehouseid());
		para.setFormtype("outstockform");
		para.setNumber(plan.getNumber());
		para.setShopid(plan.getShopid());
        inventoryFormAgentService.outStockByReadChange(para);
	}

 
	private void cancelfulfillable(UserInfo user, ShipInboundShipmentDTO shipment) throws BizException {
		ShipInboundPlanDTO plan = shipment.getInboundplan();
		 List<ShipInboundItemDTO> listmap = shipment.getItemList();
		for (int i = 0; i < listmap.size(); i++) {
			ShipInboundItemDTO item = listmap.get(i);
			InventoryParameter para = new InventoryParameter();
			Integer amount = item.getQuantityshipped();
			para.setAmount(amount);
			para.setMaterial(item.getMaterialid());
			EnumByInventory statusinv = EnumByInventory.Ready;
			para.setStatus(statusinv);
			para.setFormid(plan.getId());
			para.setOpttime(new Date());
			para.setWarehouse(plan.getWarehouseid());
			para.setFormtype("outstockform");
			para.setShopid(plan.getShopid());
			para.setNumber(plan.getNumber());
			para.setOperator(user.getId());
			inventoryFormAgentService.outStockDirectCancel(para);
		 
		}
	}

 
	 
	private void subMaterialFulfillableToOutbound(UserInfo user, ShipInboundPlanDTO plan, String material, Integer amount) {
		InventoryParameter para = new InventoryParameter();
		para.setAmount(amount);
		para.setMaterial(material);
		EnumByInventory statusinv = EnumByInventory.Ready;
		para.setStatus(statusinv);
		para.setFormid(plan.getId());
		para.setOpttime(new Date());
		para.setOperator(user.getId());
		para.setWarehouse(plan.getWarehouseid());
		para.setFormtype("outstockform");
		para.setShopid(plan.getShopid());
		para.setNumber(plan.getNumber());
	    inventoryFormAgentService.outStockReadyCancel(para);
	  
	}

	private void subFulfillableToOutboundChange(UserInfo user, ShipInboundPlanDTO plan, String material, Integer amount) {
		InventoryParameter para = new InventoryParameter();
		para.setAmount(amount);
		para.setMaterial(material);
		EnumByInventory statusinv = EnumByInventory.Ready;
		para.setStatus(statusinv);
		para.setFormid(plan.getId());
		para.setOpttime(new Date());
		para.setOperator(user.getId());
		para.setWarehouse(plan.getWarehouseid());
		para.setFormtype("outstockform");
		para.setShopid(plan.getShopid());
		para.setNumber(plan.getNumber());
	    inventoryFormAgentService.outStockReadyChange(para);
	}
	
	private void fulfillableOut(UserInfo user, ShipInboundPlanDTO plan, ShipInboundShipmentDTO shipment) throws BizException {
		  List<ShipInboundItemDTO> listmap = shipment.getItemList();
		for (int i = 0; i < listmap.size(); i++) {
			  ShipInboundItemDTO item = listmap.get(i);
			if(item.getMaterialid()!=null && item.getQuantityshipped()!=null) {
				subFulfillableOut(user, plan, item.getMaterialid(), item.getQuantityshipped());
			}
		}
	}
	
	private void subFulfillableOut(UserInfo user, ShipInboundPlanDTO plan, String material, Integer amount) {
		InventoryParameter para = new InventoryParameter();
		para.setAmount(amount);
		para.setMaterial(material);
		para.setFormid(plan.getId());
		para.setOpttime(new Date());
		para.setOperator(user.getId());
		para.setWarehouse(plan.getWarehouseid());
		para.setFormtype("outstockform");
		para.setShopid(plan.getShopid());
		para.setNumber(plan.getNumber());
	    inventoryFormAgentService.outStockByDirect(para);
	}
	
	private void cancelfulfillableToOutbound(UserInfo user, ShipInboundShipmentDTO shipment) throws BizException {
		List<ShipInboundItemDTO> itemlist = shipment.getItemList();
		ShipInboundPlanDTO plan = shipment.getInboundplan();
		for (int i = 0; i < itemlist.size(); i++) {
			ShipInboundItemDTO item = itemlist.get(i);
			subMaterialFulfillableToOutbound(user, plan,item.getMaterialid(),item.getQuantityshipped());
		}
	}

	@Override
	public String updateDisable(UserInfo user, String shipmentid, boolean isDelAmz, String disableShipment) {
		// TODO Auto-generated method stub
			Result<ShipInboundShipmentDTO> shipmentResult = amazonClientOneFeign.getShipmentidAction(shipmentid);
			ShipInboundShipmentDTO shipment = shipmentResult.getData();
			Integer oldstatus = shipment.getStatus();
			if (oldstatus == 0) {
				return "fail";
			}
			shipment.setStatus(0);
			shipment.setStatus0date(new Date());
			shipment.setShipmentstatus(ShipInboundShipmentDTO.ShipmentStatus_CANCELLED);
			shipment.setOperator(user.getId());
			shipment.setOpttime(new Date());
			if(shipment.getSyncInv()==null||shipment.getSyncInv()==0||shipment.getSyncInv()==2) {
				if (oldstatus >= 5) {
					cancelfulfillable(user, shipment);
				} else {
					cancelfulfillableToOutbound(user, shipment);
				}
				if("1".equals(disableShipment)) {
					assemblyFormService.cancelByShipment(user,shipment);
				}
			}
			if (isDelAmz) {// 需要刪除亚马逊货件
				try {
					Result<String> result = amazonClientOneFeign.updateShipmentAction(shipment);
					if(Result.isSuccess(result)) {
						if(!result.getData().equals(shipmentid)) {
							 throw new BizException("亚马逊操作失败");
						}
					}else {
						 throw new BizException(result.getMsg());
					}
				} catch (Exception e) {
					 throw new BizException(e.getMessage());
				}
			}
		
			return "fail";
	}

 

	public void updateItemQty(UserInfo user, ShipAmazonShipmentDTO shipmentAmz) {
		List<ShipInboundItemDTO> itemlist = shipmentAmz.getItemList();
		if(itemlist==null||itemlist.size()==0)return;
		Result<ShipInboundShipmentDTO> shipmentResult = amazonClientOneFeign.getShipmentidAction(itemlist.get(0).getShipmentid());
		ShipInboundShipmentDTO shipment = shipmentResult.getData();
		Map<String,Integer> skuQuantity=new HashMap<String,Integer>();
		itemlist.forEach(item->{
			skuQuantity.put(item.getSellersku(), item.getQuantityshipped());
		});
		for (int i = 0; i <shipment.getItemList().size(); i++) {
			ShipInboundItemDTO shipInboundItem = shipment.getItemList().get(i);
			Integer qty = shipInboundItem.getQuantityshipped();
			Integer newqty = skuQuantity.get(shipInboundItem.getSellersku());
	              if (newqty!=null&&qty != newqty) {
					if (qty < newqty) {// 修改数量大于计划数量,changeadd
						addFullfillableToOutboundChange(user, shipment.getInboundplan(), shipInboundItem.getMaterialid(), newqty - qty);
					} else {// 修改数量小于计划数量,changesub
						subFulfillableToOutboundChange(user, shipment.getInboundplan(),  shipInboundItem.getMaterialid(), qty - newqty);
					}
		 
				}
	         shipInboundItem.setQuantityshipped(newqty);
		}
		shipment.setStatus(3);
		shipment.setStatus3date(new Date());
		shipment.setOperator(user.getId());
		shipment.setOpttime(new Date());
		if(StrUtil.isNotBlank(shipmentAmz.getIntendedBoxContentsSource())) {
			shipment.setIntendedBoxContentsSource(shipmentAmz.getIntendedBoxContentsSource());
		}
		amazonClientOneFeign.updateShipmentAction(shipment);
	}

	@Override
	public void marketShipped(UserInfo user, String shipmentid) {
		// TODO Auto-generated method stub
		Result<ShipInboundShipmentDTO> shipmentResult = amazonClientOneFeign.getShipmentidAction(shipmentid);
		ShipInboundShipmentDTO shipment = shipmentResult.getData();
		if (shipment.getStatus() != 4) {
			throw new BizException("错误：系统检测到装箱环节没有处理完成，请确认您当前已经成功下载装箱信息。");
		}
		shipment.setStatus(5);
		shipment.setStatus5date(new Date());
		shipment.setShipmentstatus(ShipInboundShipmentDTO.ShipmentStatus_SHIPPED);
		shipment.setOperator(user.getId());
		shipment.setOpttime(new Date());
		shipment.setShipedDate(new Date());
		outbound(user,shipment);
		amazonClientOneFeign.updateShipmentAction(shipment);
	}
	

}
