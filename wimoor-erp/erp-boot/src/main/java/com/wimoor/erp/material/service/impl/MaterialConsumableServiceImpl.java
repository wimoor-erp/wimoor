package com.wimoor.erp.material.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.pojo.entity.EnumByInventory;
import com.wimoor.erp.customer.pojo.entity.Customer;
import com.wimoor.erp.customer.service.ICustomerService;
import com.wimoor.erp.inventory.mapper.InventoryMapper;
import com.wimoor.erp.inventory.pojo.entity.InventoryParameter;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.material.mapper.MaterialConsumableMapper;
import com.wimoor.erp.material.mapper.MaterialMapper;
import com.wimoor.erp.material.pojo.dto.MaterialConsumableDTO;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.pojo.entity.MaterialConsumable;
import com.wimoor.erp.material.pojo.vo.MaterialConsumableVO;
import com.wimoor.erp.material.service.IMaterialConsumableInventoryService;
import com.wimoor.erp.material.service.IMaterialConsumableService;
import com.wimoor.erp.ship.pojo.dto.ConsumableOutFormDTO;
import com.wimoor.erp.ship.pojo.dto.ConsumableSkuDTO;
import com.wimoor.erp.ship.pojo.dto.PackageSkuDTO;
import com.wimoor.erp.stock.mapper.OutWarehouseFormEntryMapper;
import com.wimoor.erp.stock.mapper.OutWarehouseFormMapper;
import com.wimoor.erp.stock.pojo.entity.OutWarehouseForm;
import com.wimoor.erp.stock.pojo.entity.OutWarehouseFormEntry;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
@Service("materialConsumableService")
@RequiredArgsConstructor
public class MaterialConsumableServiceImpl extends ServiceImpl<MaterialConsumableMapper,MaterialConsumable> implements IMaterialConsumableService {
    final InventoryMapper inventoryMapper;
	final ICustomerService customerService;
	final MaterialMapper materialMapper;
	final IMaterialConsumableInventoryService iMaterialConsumableInventoryService;
	final OutWarehouseFormMapper outWarehouseFormMapper;
	final OutWarehouseFormEntryMapper outWarehouseFormEntryMapper;
	final IWarehouseService warehouseService;
	final ISerialNumService serialNumService;
	@Lazy
	@Autowired
	IInventoryService inventoryService;
	
	public List<MaterialConsumable> selectByMainmid(String materialid) {
		QueryWrapper<MaterialConsumable> queryWrapper=new QueryWrapper<MaterialConsumable>();
		queryWrapper.eq("materialid",materialid);
		return this.list(queryWrapper);
	}

	public int deleteByMainmid(String materialid) {
		// TODO Auto-generated method stub
		QueryWrapper<MaterialConsumable> queryWrapper=new QueryWrapper<MaterialConsumable>();
		queryWrapper.eq("materialid",materialid);
		return this.baseMapper.delete(queryWrapper);
	}

	public List<MaterialConsumableVO> selectConsumableByMainmid(String id,String warehouseid ,String shopid) {
		List<MaterialConsumableVO> list=this.baseMapper.selectConsumableByMainMmid(id);
		if(list!=null && list.size()>0) {
			for(int i=0;i<list.size();i++) {
				String submaterialid = list.get(i).getId();
				Map<String, Object> invlist = inventoryMapper.findInvByWarehouseId(submaterialid, warehouseid, shopid);
				if(invlist.get("inbound")!=null) {
					list.get(i).setInbound(Integer.parseInt(invlist.get("inbound").toString()));
				}
				if(invlist.get("outbound")!=null) {
					list.get(i).setOutbound(Integer.parseInt(invlist.get("outbound").toString()));
				}
				if(invlist.get("fulfillable")!=null) {
					list.get(i).setFulfillable(Integer.parseInt(invlist.get("fulfillable").toString()));
				}
			}
		}
		return list;
	}
	
	
	public List<MaterialConsumableVO> selectConsumableByMainmid(String id,String shopid) {
		List<MaterialConsumableVO> list=this.baseMapper.selectConsumableByMainMmid(id);
		if(list!=null && list.size()>0) {
			for(int i=0;i<list.size();i++) {
				String submaterialid = list.get(i).getId();
				Map<String, Object> invlist = inventoryMapper.findInvByWarehouseId(submaterialid, null, shopid);
				if (invlist!=null  && invlist.size()>0) {
					if(invlist.get("inbound")!=null) {
						list.get(i).setInbound(Integer.parseInt(invlist.get("inbound").toString()));
					}
					if(invlist.get("outbound")!=null) {
						list.get(i).setOutbound(Integer.parseInt(invlist.get("outbound").toString()));
					}
					if(invlist.get("fulfillable")!=null) {
						list.get(i).setFulfillable(Integer.parseInt(invlist.get("fulfillable").toString()));
					}
				}
			}
		}
		return list;
	}
	
	public List<MaterialConsumableVO> selectConsumableBySubmid(String id,String wahouseid,String shopid) {
		List<MaterialConsumableVO> list=this.baseMapper.selectConsumableBySubmid(id);
		if(StrUtil.isBlankOrUndefined(wahouseid)) {
			wahouseid=null;
		}
		if(list!=null && list.size()>0) {
			for(int i=0;i<list.size();i++) {
				MaterialConsumableVO vo=list.get(i);
				String materialid = vo.getMaterialid();
				Map<String, Object> invlist = inventoryMapper.findInvByWarehouseId(materialid, wahouseid, shopid);
				int qty=0;
				if (invlist!=null  && invlist.size()>0) {
					if(invlist.get("inbound")!=null) {
						vo.setInbound(Integer.parseInt(invlist.get("inbound").toString()));
						qty=qty+vo.getInbound();
					}
					if(invlist.get("outbound")!=null) {
						vo.setOutbound(Integer.parseInt(invlist.get("outbound").toString()));
						qty=qty+vo.getOutbound();
					}
					if(invlist.get("fulfillable")!=null) {
						vo.setFulfillable(Integer.parseInt(invlist.get("fulfillable").toString()));
						qty=qty+vo.getFulfillable();
					}
				}
				if(vo.getAmount().compareTo(new BigDecimal(0))>0) {
					vo.setNeedPlan((new BigDecimal(qty).multiply(vo.getAmount())).intValue());
				}
			}
		}
		return list;
	}
	

@SuppressWarnings("unchecked")
public 	List<Map<String, Object>> selectConsumableByMainSKU(String shopid,String warehouseid,List<Map<String,Object>> skulist){
	 if(skulist==null||skulist.size()==0) {return null;}
	 Map<String,Integer> mainSKUQty=new HashMap<String,Integer>();
	 for(Map<String, Object> item:skulist) {
         String sku=item.get("sku").toString();		 
         Integer qty = mainSKUQty.get(sku);
         if(qty==null) {
        	 qty=0;
         }
		qty=qty+Integer.parseInt(item.get("amount").toString());
		mainSKUQty.put(sku, qty);
	 }
	 Map<String,Map<String,Object>> consumableMap =new HashMap<String,Map<String,Object>>();
	 for(Entry<String, Integer> entry:mainSKUQty.entrySet()) {
		 String sku=entry.getKey();
	     Material mainMaterial = materialMapper.getMaterailBySku(shopid,sku);
	     Integer mainPurchaseQty = mainSKUQty.get(sku);
		 List<MaterialConsumableVO> list = this.selectConsumableByMainmid(mainMaterial.getId(),shopid);
		 Map<String, Object> mainmap = inventoryMapper.getSelfInvBySKU(warehouseid ,mainMaterial.getId());
		 mainmap.put("material", mainMaterial);
		 mainmap.put("mainPurchaseQty", mainPurchaseQty);
		 for(MaterialConsumableVO item:list) {
			 BigDecimal unitAmount = item.getAmount();
			 String key = item.getId();
			 Material submaterial = materialMapper.selectById(key);
			 Map<String,Object> conitem = consumableMap.get(key);
			 if(conitem==null) {
				 conitem=new HashMap<String,Object>();
				 consumableMap.put(key, conitem);
			 }
			 Integer needpurchase =0;
			 List<Map<String,Object>> mainMap=null;
			 if(conitem.get("mainlist")!=null) {
				 mainMap = (List<Map<String,Object>>) conitem.get("mainlist");
			 }else {
				 mainMap=new ArrayList<Map<String,Object>>();
				 conitem.put("mainlist",mainMap);
			 }
			 mainmap.put("rate", unitAmount);
			 mainMap.add(mainmap);
			 conitem.put("mainlist",mainMap);
			 if(conitem.get("needpurchase")!=null) {
				 needpurchase = (Integer) conitem.get("needpurchase");
			 }
			 needpurchase=needpurchase+(int)Math.ceil(unitAmount.doubleValue()*(mainPurchaseQty*1.0));
			 conitem.put("needpurchase",needpurchase);
			 conitem.put("material", submaterial);
			 String supplierid = submaterial.getSupplier();
			 if(supplierid!=null) {
				 Customer supplier = customerService.getById(supplierid);
				 conitem.put("supplier",supplier);
			 }
			 Map<String, Object> map = inventoryMapper.getSelfInvBySKU(warehouseid,submaterial.getId());
			 conitem.putAll(map);
		 }
	 }
	 List<Map<String, Object>> result=new ArrayList<Map<String,Object>>();
	 result.addAll(consumableMap.values());
	 return  result;
	  
  }

public List<Map<String, Object>> findConsumableDetailList(Map<String, Object> maps) {
	return this.baseMapper.findConsumableDetailList(maps);
}




@CacheEvict(value = { "materialListCache","inventoryByMskuCache" }, allEntries = true)
public int saveInventoryConsumable(UserInfo user,ConsumableOutFormDTO dto) {
    int result=0;
		String warehouseid=dto.getWarehouseid();
		if(StrUtil.isBlank(warehouseid)) {
			throw new BizException("出库仓库不能为空");
		}
		boolean hasConsumableQty=false;
		if(dto.getSkulist()!=null && dto.getSkulist().size()>0) {
			for(ConsumableSkuDTO item:dto.getSkulist()) {
				Material material = this.materialMapper.getMaterailBySku(user.getCompanyid(),item.getSku());
				if(material==null) {
					throw new BizException("找不到对应的本地SKU");
				}
				item.setMaterialid(material.getId());
				BigDecimal residue=item.getResidue();
				 if(residue!=null&&residue.floatValue()>0.0000001) {
					 iMaterialConsumableInventoryService.save(user,material.getId(),warehouseid,residue);
				 }
			     if(item.getAmount()>0) {
			    	 hasConsumableQty=true;
			    }
			}
		}
		boolean hasPackageQty=false;
		if(dto.getPkglist()!=null && dto.getPkglist().size()>0) {
			for(PackageSkuDTO item:dto.getPkglist()) {
				Material material = this.materialMapper.getMaterailBySku(user.getCompanyid(),item.getSku());
				if(material==null) {
					throw new BizException("找不到对应的本地SKU");
				}
				item.setMaterialid(material.getId());
				 if(item.getAmount()>0) {
					 hasPackageQty=true;
				 }
			}
		}
		if(hasPackageQty==false&&hasConsumableQty==false) {
			return result;
		}
		OutWarehouseForm form=new OutWarehouseForm();
		String bigid = warehouseService.getUUID();
		String formid = bigid;
		if(StrUtil.isNotEmpty(dto.getShipmentid()) ) {
			dto.setNumber(dto.getShipmentid());
		}
		if(StrUtil.isBlankOrUndefined(dto.getNumber())) {
			try {
				dto.setNumber(serialNumService.readSerialNumber(user.getCompanyid(), "O"));
			} catch (Exception e) {
				e.printStackTrace();
				try {
					dto.setNumber(serialNumService.readSerialNumber(user.getCompanyid(), "O"));
				} catch (Exception e1) {
					e1.printStackTrace();
					throw new BizException("编码获取失败,请联系管理员");
				}
			}
		}
		form.setNumber(dto.getNumber());
		form.setAudittime(new Date());
		form.setAuditstatus(2);
		form.setCreatedate(new Date());
		form.setCreator(user.getId());
		form.setWarehouseid(warehouseid);
		form.setShopid(user.getCompanyid());
		form.setOpttime(new Date());
		form.setOperator(user.getId());
		if(StrUtil.isNotEmpty(dto.getShipmentid())) {
			form.setRemark("发货单耗材出库");
		}else {
			if(dto.getNumber().contains("sea")) {
				form.setRemark("海外仓备货单耗材出库");
			}else {
				form.setRemark("出库单耗材出库");
			}
		}
		form.setId(formid);
		outWarehouseFormMapper.insert(form);
			if(hasConsumableQty==true) {
				for(int i=0;i<dto.getSkulist().size();i++) {
					ConsumableSkuDTO skuinfo = dto.getSkulist().get(i);
					if(skuinfo.getMaterialid()!=null&&skuinfo.getAmount()!=null&&skuinfo.getAmount()>0) {
						int qty= skuinfo.getAmount();
						OutWarehouseFormEntry record=new OutWarehouseFormEntry();
						record.setAmount(qty);
						record.setFormid(formid);
						record.setMaterialid(skuinfo.getMaterialid());
						outWarehouseFormEntryMapper.insert(record);
						
						InventoryParameter para=new InventoryParameter();
						para.setAmount(qty);
						para.setFormid(formid);
						para.setFormtype("outstockform");
						para.setNumber(dto.getNumber());
						para.setOperator(user.getId());
						para.setOpttime(new Date());
						para.setShopid(user.getCompanyid());
						para.setStatus(EnumByInventory.alReady);
						para.setWarehouse(warehouseid);
						para.setMaterial(skuinfo.getMaterialid());
						para.setSku(skuinfo.getSku());
						result+= inventoryService.outStockByDirect(para);
					}
				 }
		     }
		if(hasPackageQty==true) {
			for(int i=0;i<dto.getPkglist().size();i++) {
				PackageSkuDTO skuinfo = dto.getPkglist().get(i);
				if(skuinfo.getMaterialid()!=null&&skuinfo.getAmount()!=null&&skuinfo.getAmount()>0) {
					int qty= skuinfo.getAmount();
					OutWarehouseFormEntry record=new OutWarehouseFormEntry();
					record.setAmount(qty);
					record.setFormid(formid);
					record.setMaterialid(skuinfo.getMaterialid());
					outWarehouseFormEntryMapper.insert(record);
					
					InventoryParameter para=new InventoryParameter();
					para.setAmount(qty);
					para.setFormid(formid);
					para.setFormtype("outstockform");
					para.setNumber(dto.getNumber());
					para.setOperator(user.getId());
					para.setOpttime(new Date());
					para.setShopid(user.getCompanyid());
					para.setStatus(EnumByInventory.alReady);
					para.setWarehouse(warehouseid);
					para.setMaterial(skuinfo.getMaterialid());
					para.setSku(skuinfo.getSku());
					result+= inventoryService.outStockByDirect(para);
				}
			   }
			}
	return result;
}

public List<Map<String, Object>> findConsumableHistory(String shopid, String shipmentid) {
	QueryWrapper<OutWarehouseForm> queryWrapper=new QueryWrapper<OutWarehouseForm>();
	queryWrapper.eq("number",shipmentid);
	queryWrapper.eq("shopid",shopid);
	List<OutWarehouseForm> formlist = outWarehouseFormMapper.selectList(queryWrapper);
	if(formlist!=null && formlist.size()>0) {
		OutWarehouseForm form = formlist.get(0);
		String formid = form.getId();
		List<Map<String, Object>> list = outWarehouseFormEntryMapper.findFormEntryByFormid(formid);
		return list;
	}
	return null;
}

public List<Map<String, Object>> findConsumableDetailByShipment(String shopid, String shipmentid ) {
	// TODO Auto-generated method stub
	List<Map<String, Object>> result = this.baseMapper.findConsumableDetailByShipment( shopid,  shipmentid) ;
	LambdaQueryWrapper<OutWarehouseForm> query=new LambdaQueryWrapper<OutWarehouseForm>();
	query.eq(OutWarehouseForm::getShopid, shopid);
	query.eq(OutWarehouseForm::getNumber, shipmentid);
	OutWarehouseForm form=outWarehouseFormMapper.selectOne(query);
	Map<String,Integer> entryMap=new HashMap<String,Integer>();
	if(form!=null) {
		LambdaQueryWrapper<OutWarehouseFormEntry> queryEntry=new LambdaQueryWrapper<OutWarehouseFormEntry>();
		queryEntry.eq(OutWarehouseFormEntry::getFormid, form.getId());
		List<OutWarehouseFormEntry> list = outWarehouseFormEntryMapper.selectList(queryEntry);
		for(OutWarehouseFormEntry item:list) {
			entryMap.put(item.getMaterialid(), item.getAmount());
		}
		for(Map<String, Object> item:result) {
			item.put("out", entryMap.get(item.get("materialid").toString()));
		}
	}
	return result;
}

@Override
public List<Map<String, Object>> findConsumableDetailBySkuList(UserInfo user, MaterialConsumableDTO dto) {
	// TODO Auto-generated method stub
	Map<String, Object> map=new HashMap<String,Object>();
	map.put("shopid", user.getCompanyid());
	map.put("warehouseid", dto.getWarehouseid());
	if(dto.getSkulist()==null || dto.getSkulist().size()==0) {
		throw new BizException("主产品列表不能为空");
	}
	List<String> skulist=new ArrayList<String>();
	Map<String,Integer> entryNewMap=new HashMap<String,Integer>();
	for(Map<String, Object> item:dto.getSkulist()) {
		String sku= item.get("sku").toString();
		skulist.add(sku);
		Integer qty=item.get("qty")!=null?Integer.parseInt(item.get("qty").toString()):0;
		entryNewMap.put(sku, qty);
	}
	map.put("mskulist", skulist);
	List<Map<String, Object>> result = this.baseMapper.findConsumableDetailBySkuList(map) ;
	Map<String,Integer> entryMap=new HashMap<String,Integer>();
	if(StrUtil.isNotBlank(dto.getNumber())) {
		LambdaQueryWrapper<OutWarehouseForm> query=new LambdaQueryWrapper<OutWarehouseForm>();
		query.eq(OutWarehouseForm::getShopid, user.getCompanyid());
		query.eq(OutWarehouseForm::getNumber, dto.getNumber());
		OutWarehouseForm form=outWarehouseFormMapper.selectOne(query);
		if(form!=null) {
			LambdaQueryWrapper<OutWarehouseFormEntry> queryEntry=new LambdaQueryWrapper<OutWarehouseFormEntry>();
			queryEntry.eq(OutWarehouseFormEntry::getFormid, form.getId());
			List<OutWarehouseFormEntry> list = outWarehouseFormEntryMapper.selectList(queryEntry);
			for(OutWarehouseFormEntry item:list) {
				entryMap.put(item.getMaterialid(), item.getAmount());
			}
		}
	}
	
	for(Map<String, Object> item:result) {
		item.put("warehouseid", dto.getWarehouseid());
		String mainsku=item.get("mainsku").toString();
		Integer shipqty = entryNewMap.get(mainsku);
		if(shipqty!=null) {
		  Double units=item.get("units")!=null?Double.parseDouble(item.get("units").toString()):0.00;
		  Double residue=item.get("residue")!=null?Double.parseDouble(item.get("residue").toString()):0.00;
		  Double need = units*shipqty+residue;
		  int intneed = need.intValue();
		  item.put("need",intneed );
		  item.put("residue",need-intneed  );
		}
		item.put("out", entryMap.get(item.get("materialid").toString()));
		
	}
	return result;
}


}
