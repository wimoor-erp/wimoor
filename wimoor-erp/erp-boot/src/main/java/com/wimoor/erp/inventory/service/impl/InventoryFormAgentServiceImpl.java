package com.wimoor.erp.inventory.service.impl;

 
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wimoor.common.mvc.BizException;
import com.wimoor.erp.common.pojo.entity.Operate;
import com.wimoor.erp.common.pojo.entity.Status;
import com.wimoor.erp.inventory.pojo.entity.InventoryParameter;
import com.wimoor.erp.inventory.service.IInventoryFormAgentService;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IMaterialService;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
 
//库存操作 
@Service("inventoryFormAgentService")
@RequiredArgsConstructor
public class InventoryFormAgentServiceImpl implements IInventoryFormAgentService {
	 
	final IInventoryService inventoryService;
    final IMaterialService iMaterialService;
	@Transactional
	public  Boolean outbound( List<InventoryParameter>   paramList) throws BizException {
		for (int i = 0; i < paramList.size(); i++) {
			InventoryParameter para = paramList.get(i);
			if(StrUtil.isBlank(para.getMaterial())) {
				if(StrUtil.isNotBlank(para.getSku())) {
					Material m = iMaterialService.getBySku(para.getShopid(),para.getSku());
					para.setMaterial(m.getId());
				}else {
					throw new BizException("扣除库存时，本地产品未设置，请联系管理员");
				}
			}
			
	        inventoryService.SubStockByStatus(para, Status.outbound, Operate.out);
		}
		return  true;
	}
	
	@Transactional
	public Boolean out(List<InventoryParameter> paramList) throws BizException {
		// TODO Auto-generated method stub
		for (int i = 0; i < paramList.size(); i++) {
			InventoryParameter para = paramList.get(i);
			if(StrUtil.isBlank(para.getMaterial())) {
				if(StrUtil.isNotBlank(para.getSku())) {
					Material m = iMaterialService.getBySku(para.getShopid(),para.getSku());
					para.setMaterial(m.getId());
				}else {
					throw new BizException("扣除库存时，本地产品未设置，请联系管理员");
				}
			}
	        inventoryService.SubStockByStatus(para, Status.fulfillable, Operate.out);
		}
		return  true;
	}
	
	
	@Transactional
	public Boolean undoOut( List<InventoryParameter>   paramList) throws BizException {
		for (int i = 0; i < paramList.size(); i++) {
			InventoryParameter para = paramList.get(i);
			if(StrUtil.isBlank(para.getMaterial())) {
				if(StrUtil.isNotBlank(para.getSku())) {
					Material m = iMaterialService.getBySku(para.getShopid(),para.getSku());
					para.setMaterial(m.getId());
				}else {
					throw new BizException("扣除库存时，本地产品未设置，请联系管理员");
				}
			}
	        inventoryService.UndoSubStockByStatus(para, Status.fulfillable, Operate.out);
		}
		return true;
	}
	
	@Transactional
	public Boolean undoOutbound( List<InventoryParameter>   paramList) throws BizException {
		for (int i = 0; i < paramList.size(); i++) {
			InventoryParameter para = paramList.get(i);
			if(StrUtil.isBlank(para.getMaterial())) {
				if(StrUtil.isNotBlank(para.getSku())) {
					Material m = iMaterialService.getBySku(para.getShopid(),para.getSku());
					para.setMaterial(m.getId());
				}else {
					throw new BizException("扣除库存时，本地产品未设置，请联系管理员");
				}
			}
	        inventoryService.UndoSubStockByStatus(para, Status.outbound, Operate.undoout);
		}
		return true;
	}
	@Transactional
	public Integer inStockByDirect(InventoryParameter invpara) throws BizException {
		int result1 = inventoryService.inStockByDirect(invpara);
		return result1;
	}
	@Transactional
	public Integer inStockByRead(InventoryParameter invpara) throws BizException {
		int result1 = inventoryService.inStockByReady(invpara);
		return result1;
	}
	@Transactional
	public Integer outStockByDirect(InventoryParameter invpara) throws BizException {
		int result1 = inventoryService.outStockByDirect(invpara);
		return result1;
	}
	@Transactional
	public Integer outStockByRead(InventoryParameter invpara) throws BizException {
		int result1 = inventoryService.outStockByReady(invpara);
		return result1;
	}
	@Transactional
	public Integer outStockByReadChange(InventoryParameter invpara) throws BizException {
		int result1 = inventoryService.outStockByReadyChange(invpara);
		return result1;
	}
	@Transactional
	public Integer outStockReadyChange(InventoryParameter invpara) throws BizException {
		int result1 = inventoryService.outStockReadyChange(invpara);
		return result1;
	}
	@Transactional
	public Integer inStockDirectCancel(InventoryParameter invpara) throws BizException {
		int result1 = inventoryService.inStockDirectCancel(invpara);
		return result1;
	}
	@Transactional
	public Integer outStockDirectCancel(InventoryParameter invpara) throws BizException {
		int result1 = inventoryService.outStockDirectCancel(invpara);
		return result1;
	}
	@Transactional
	public Integer outStockReadyCancel(InventoryParameter invpara) throws BizException {
		int result1 = inventoryService.outStockReadyCancel(invpara);
		return result1;
	}
	@Transactional
	public Integer inStockReadyCancel(InventoryParameter invpara) throws BizException {
		int result1 = inventoryService.inStockReadyCancel(invpara);
		return result1;
	}


	 

}
