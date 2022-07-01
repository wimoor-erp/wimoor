package com.wimoor.erp.warehouse.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.api.amzon.inbound.pojo.vo.ShipInboundItemVo;
import com.wimoor.api.amzon.inbound.pojo.vo.ShipInboundShipmenSummarytVo;
import com.wimoor.api.erp.assembly.pojo.vo.AssemblyVO;
import com.wimoor.api.erp.warehouse.pojo.vo.WarehouseShelfInventoryVo;
import com.wimoor.common.mvc.BizException;
import com.wimoor.erp.assembly.service.IAssemblyService;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.material.pojo.entity.DimensionsInfo;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IDimensionsInfoService;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.warehouse.mapper.WarehouseShelfInventoryMapper;
import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelf;
import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelfInventory;
import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelfInventoryOptPro;
import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelfInventoryOptRecord;
import com.wimoor.erp.warehouse.pojo.vo.WarehouseShelfInventorySummaryVo;
import com.wimoor.erp.warehouse.service.IWarehouseShelfInventoryOptRecordService;
import com.wimoor.erp.warehouse.service.IWarehouseShelfInventoryService;
import com.wimoor.erp.warehouse.service.IWarehouseShelfService;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 货架产品库存 服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-03-19
 */
@Service
@RequiredArgsConstructor
public class WarehouseShelfInventoryServiceImpl extends ServiceImpl<WarehouseShelfInventoryMapper, WarehouseShelfInventory> implements IWarehouseShelfInventoryService {

	final IMaterialService materialService;
	final IDimensionsInfoService dimensionsInfoService;
	final IWarehouseShelfInventoryOptRecordService iWarehouseShelfInventoryOptRecordService;
	final IInventoryService inventoryService;
    final IAssemblyService assemblyService;
    
	@Lazy
	@Autowired
	IWarehouseShelfService iWarehouseShelfService;
	@Override
	public WarehouseShelfInventorySummaryVo sumByShelf(WarehouseShelf item) {
		// TODO Auto-generated method stub
		return this.baseMapper.sumByShelf(item);
	}
 
	
	public ShipInboundShipmenSummarytVo formInvAssemblyShelf(ShipInboundShipmenSummarytVo itemsum) {
 
		List<ShipInboundItemVo> itemvoList=itemsum.getItemList();
		for(ShipInboundItemVo item:itemvoList) {
			List<WarehouseShelfInventoryVo> shelfinvVoList = findByMaterial(itemsum.getShopid(),itemsum.getWarehouseid(),item.getMaterialid());
			for(WarehouseShelfInventoryVo inv:shelfinvVoList) {
				inv.setShelfname(iWarehouseShelfService.getAllParentName(inv.getShelfid()));
			}
			item.setShelfInvList(shelfinvVoList);
			if(item.getIssfg()==1) {
				List<AssemblyVO> assvolist = assemblyService.selectByMainmid(item.getMaterialid());
				for(AssemblyVO assvo:assvolist) {
					Map<String, Object> map = inventoryService.findInvDetailById(assvo.getSubmid(), itemsum.getWarehouseid(), itemsum.getShopid());
					if(map!=null) {
						assvo.setInbound(map.get("inbound")!=null?Integer.parseInt(map.get("inbound").toString()):0);
						assvo.setFulfillable(map.get("fulfillable")!=null?Integer.parseInt(map.get("fulfillable").toString()):0);
						assvo.setOutbound(map.get("outbound")!=null?Integer.parseInt(map.get("outbound").toString()):0);
					}
					List<WarehouseShelfInventoryVo> mshelfinvVoList = findByMaterial(itemsum.getShopid(),itemsum.getWarehouseid(),assvo.getSubmid());
					for(WarehouseShelfInventoryVo inv:mshelfinvVoList) {
						inv.setShelfname(iWarehouseShelfService.getAllParentName(inv.getShelfid()));
					}
					assvo.setShelfInvList(mshelfinvVoList);
				}
			  item.setAssemblyList(assvolist);
			}
		}
		itemsum.setItemList(itemvoList);
		return itemsum;
	}
 
	@Override
	public List<WarehouseShelfInventoryVo> findByMaterial(String shopid,String warehouseid,String materialid) {
		// TODO Auto-generated method stub
		return this.baseMapper.findByMaterial(shopid,warehouseid,materialid);
	}
	
	/**
	 * 刷新当前库存中的产品对应容量，之所以对所有这个库位对应的这个产品进行刷新，是考虑客户修改产品尺寸之后没有做对应的刷新库位中容量的动作。
	 * 可以最大程度的保证库位容量的准确。
	 * @param inv
	 */
	private Float getSize(WarehouseShelfInventoryOptPro inv) {
	  	Material material = materialService.getById(inv.getMaterialid());
     	if(material==null) {
     		throw new BizException("未找到对应的产品。");
     	}
     	BigDecimal size = new BigDecimal("0");
     	DimensionsInfo dim = dimensionsInfoService.getById(material.getPkgdimensions());
     	if(dim!=null) {
     		BigDecimal height=dim.getHeight()!=null?dim.getHeight():new BigDecimal("0.0001");
     		BigDecimal width=dim.getWidth()!=null?dim.getWidth():new BigDecimal("0");
     		BigDecimal length=dim.getLength()!=null?dim.getLength():new BigDecimal("0");
     		size = height.multiply(width).multiply(length);
     	}
         return size.floatValue() ;
	}
	
	private WarehouseShelfInventory getOldOne(WarehouseShelfInventory inv) {
		LambdaQueryWrapper<WarehouseShelfInventory> wrapper=new LambdaQueryWrapper<WarehouseShelfInventory>();
		wrapper.eq(WarehouseShelfInventory::getShopid, inv.getShopid());
		wrapper.eq(WarehouseShelfInventory::getShelfid, inv.getShelfid());
		wrapper.eq(WarehouseShelfInventory::getMaterialid, inv.getMaterialid());
	    WarehouseShelfInventory oldone = this.baseMapper.selectOne(wrapper);
	    return oldone;
	}
	
	@Override
	public WarehouseShelfInventoryOptRecord add(WarehouseShelfInventoryOptPro opt) {
		// TODO Auto-generated method stub
		Float size = getSize(opt);
		WarehouseShelfInventory inv=new WarehouseShelfInventory();
		WarehouseShelfInventoryOptRecord record=new WarehouseShelfInventoryOptRecord();
		BeanUtil.copyProperties(opt, inv);
		BeanUtil.copyProperties(opt, record);
	    WarehouseShelfInventory oldone =getOldOne(inv);
		if(oldone!=null) {
	    	oldone.setOperator(inv.getOperator());
	    	oldone.setQuantity(inv.getQuantity()+oldone.getQuantity());
	    	oldone.setSize(oldone.getQuantity()*size);
	    	this.baseMapper.updateById(oldone);
	    	//////////添加历史操作记录//////////////////////
	    	record.setBalanceQty(oldone.getQuantity());
	    	record.setBalanceSize(oldone.getSize());
	    	record.setSize(inv.getQuantity()*size);
	    	iWarehouseShelfInventoryOptRecordService.save(record);
	    	 
	    	 
	    }else {
	    	inv.setSize(inv.getQuantity()*size);
	    	this.baseMapper.insert(inv);
	    	//////////添加历史操作记录//////////////////////	    	
	    	record.setBalanceQty(inv.getQuantity());
	    	record.setBalanceSize(inv.getSize());
	    	record.setSize(inv.getQuantity()*size);
	    	iWarehouseShelfInventoryOptRecordService.save(record);
	    }
	    
		return record;
	}

	@Override
	public WarehouseShelfInventoryOptRecord sub(WarehouseShelfInventoryOptPro opt) {
		// TODO Auto-generated method stub
 		Float size = getSize(opt);
		WarehouseShelfInventory inv=new WarehouseShelfInventory();
		WarehouseShelfInventoryOptRecord record=new WarehouseShelfInventoryOptRecord();

		BeanUtil.copyProperties(opt, inv); 
		BeanUtil.copyProperties(opt, record);
	    WarehouseShelfInventory oldone =getOldOne(inv);
	    int result=0; 
	    if(oldone==null) { 
	    	throw new BizException("当前库位没有该产品可以下货");
	    } 
	    oldone.setOperator(inv.getOperator());
	    if(oldone.getQuantity()-inv.getQuantity()<0) {
	    	throw new BizException("当前库位该产品没有足够的数量可以下货");
	    }
	    oldone.setQuantity(oldone.getQuantity()-opt.getQuantity());
    	oldone.setSize(oldone.getQuantity()*size);
	    result = this.baseMapper.updateById(oldone);
    	//////////添加历史操作记录//////////////////////
	    if(result>0) {
		    record.setBalanceQty(oldone.getQuantity());
	    	record.setBalanceSize(oldone.getSize());
	    	record.setSize(inv.getQuantity()*size);
	    	iWarehouseShelfInventoryOptRecordService.save(record);
	    }

	    return record;
	}

	public IPage<WarehouseShelfInventoryVo> getShelfList(Page<?> page, Map<String, Object> param) {
		return this.baseMapper.getShelfList(page,param);
	}

    
    public IPage<WarehouseShelfInventoryVo> getShelfInventoryList(Page<?> page, Map<String, Object> param) {
    	String shelfid=param.get("shelfid")==null?null:param.get("shelfid").toString();
    	if(param.get("allchildren")==null)return null;
    	IPage<WarehouseShelfInventoryVo> result = null;
    	if("true".equals(param.get("allchildren").toString())) {
    		if(shelfid!=null) {
    			WarehouseShelf  shelf = iWarehouseShelfService.getById(shelfid)  ;
        		param.put("warehouseid", shelf.getWarehouseid());
        		param.put("treepath", shelf.getTreepath());
        		param.put("shelfid",null);
    		   } else {
    			   param.put("treepath",null);
    		   }
    		  result =  this.baseMapper.getShelfInventoryList(page,param);
    	}else {
    		if(shelfid==null) {
    			throw new BizException("没有对应库位无法选择查询当前库位下的子产品");
    		}
    		WarehouseShelf  shelf = iWarehouseShelfService.getById(shelfid)  ;
    		param.put("warehouseid", shelf.getWarehouseid());
    		param.put("shelfid",shelfid);
    		param.put("treepath",null);
    		result =  this.baseMapper.getShelfInventoryList(page,param);
    	}
    	if(result!=null&&result.getSize()>0) {
    		 for(WarehouseShelfInventoryVo item:result.getRecords()) {
     			item.setShelfname(iWarehouseShelfService.getAllParentName(item.getShelfid()));
     		}
    	}
		return result;
	}
    
  

	
}
