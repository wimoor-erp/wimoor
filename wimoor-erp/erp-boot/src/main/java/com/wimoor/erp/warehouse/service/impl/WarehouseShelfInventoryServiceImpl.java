package com.wimoor.erp.warehouse.service.impl;

import java.math.BigDecimal;
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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.mvc.BizException;
import com.wimoor.erp.assembly.pojo.vo.AssemblyVO;
import com.wimoor.erp.assembly.service.IAssemblyService;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.material.pojo.entity.DimensionsInfo;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IDimensionsInfoService;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.ship.pojo.dto.ShipInboundItemVo;
import com.wimoor.erp.ship.pojo.dto.ShipInboundShipmenSummarytVo;
import com.wimoor.erp.warehouse.mapper.WarehouseShelfInventoryMapper;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelf;
import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelfInventory;
import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelfInventoryOptRecord;
import com.wimoor.erp.warehouse.pojo.vo.WarehouseShelfInventorySummaryVo;
import com.wimoor.erp.warehouse.pojo.vo.WarehouseShelfInventoryVo;
import com.wimoor.erp.warehouse.service.IErpWarehouseAddressService;
import com.wimoor.erp.warehouse.service.IWarehouseService;
import com.wimoor.erp.warehouse.service.IWarehouseShelfInventoryOptRecordService;
import com.wimoor.erp.warehouse.service.IWarehouseShelfInventoryService;
import com.wimoor.erp.warehouse.service.IWarehouseShelfService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
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
	final IErpWarehouseAddressService iErpWarehouseAddressService;
	@Lazy
	@Autowired
	IWarehouseShelfService iWarehouseShelfService;
	final IWarehouseService iWarehouseService;
	@Override
	public WarehouseShelfInventorySummaryVo sumByShelf(WarehouseShelf item) {
		// TODO Auto-generated method stub
		return this.baseMapper.sumByShelf(item);
	}
 
	
	public ShipInboundShipmenSummarytVo formInvAssemblyShelf(ShipInboundShipmenSummarytVo itemsum) {
		List<ShipInboundItemVo> itemvoList=itemsum.getItemList();
		for(ShipInboundItemVo item:itemvoList) {
			if(item.getMaterialid()==null) {
				if(item.getMsku()==null) {
					item.setMsku(item.getSellersku());
				}
				Material material=materialService.getBySku(itemsum.getShopid(), item.getMsku());
				if(material!=null) {
					item.setMaterialid(material.getId());
				}
			}
		    if(item.getMaterialid()==null){
					throw new BizException(item.getSellersku()+"找不到对应的本地产品无法处理");
			}
		    if(StrUtil.isBlank(itemsum.getWarehouseid())) {
		    	return itemsum;
		    }
			Warehouse warehouse = iWarehouseService.getById(itemsum.getWarehouseid());
			List<WarehouseShelfInventoryVo> shelfinvVoList = findByMaterial(itemsum.getShopid(),warehouse,item.getMaterialid());
			for(WarehouseShelfInventoryVo inv:shelfinvVoList) {
				inv.setShelfname(iWarehouseShelfService.getAllParentName(inv.getShelfid()));
			}
			item.setShelfInvList(shelfinvVoList);
			item.setShelfInvRecordList(iWarehouseShelfInventoryOptRecordService.getRecordVo(itemsum.getShopid(), item.getId(),
																							"outstockform", item.getMaterialid()));
			Material m = materialService.getById(item.getMaterialid());
			if(m!=null&&m.getIssfg()!=null) {
				item.setIssfg(Integer.parseInt(m.getIssfg()));
			}
			if(m!=null&&m.getIssfg()!=null&&m.getIssfg().equals("1")) {
				List<AssemblyVO> assvolist = assemblyService.selectByMainmid(item.getMaterialid());
				for(AssemblyVO assvo:assvolist) {
					assvo.setSubamount(assvo.getSubnumber()*item.getQuantityShipped());
					Map<String, Object> map = inventoryService.findInvDetailById(assvo.getSubmid(), itemsum.getWarehouseid(), itemsum.getShopid());
					if(map!=null) {
						assvo.setInbound(map.get("inbound")!=null?Integer.parseInt(map.get("inbound").toString()):0);
						assvo.setFulfillable(map.get("fulfillable")!=null?Integer.parseInt(map.get("fulfillable").toString()):0);
						assvo.setOutbound(map.get("outbound")!=null?Integer.parseInt(map.get("outbound").toString()):0);
					}
					List<WarehouseShelfInventoryVo> mshelfinvVoList = findByMaterial(itemsum.getShopid(),warehouse,assvo.getSubmid());
					for(WarehouseShelfInventoryVo inv:mshelfinvVoList) {
						inv.setShelfname(iWarehouseShelfService.getAllParentName(inv.getShelfid()));
					}
					assvo.setShelfInvList(mshelfinvVoList);
					assvo.setShelfInvRecordList(iWarehouseShelfInventoryOptRecordService.getRecordVo(itemsum.getShopid(),
							                           item.getId(),
							                           "outstockform",
							                           assvo.getSubmid()));
				}
			  item.setAssemblyList(assvolist);
			}
		}
		itemsum.setItemList(itemvoList);
		return itemsum;
	}
 
	@Override
	public List<WarehouseShelfInventoryVo> findByMaterial(String shopid,Warehouse warehouse,String materialid) {
		// TODO Auto-generated method stub
		return this.baseMapper.findByMaterial(shopid,warehouse.getAddressid(),warehouse.getId(),materialid);
	}
	
	/**
	 * 刷新当前库存中的产品对应容量，之所以对所有这个库位对应的这个产品进行刷新，是考虑客户修改产品尺寸之后没有做对应的刷新库位中容量的动作。
	 * 可以最大程度的保证库位容量的准确。
	 * @param inv
	 */
	private Float getSize(WarehouseShelfInventoryOptRecord inv) {
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
		if(inv.getWarehouseid()!=null) {
			wrapper.eq(WarehouseShelfInventory::getWarehouseid, inv.getWarehouseid());
		}else {
			wrapper.isNull(WarehouseShelfInventory::getWarehouseid);
		}
	    WarehouseShelfInventory oldone = this.baseMapper.selectOne(wrapper);
	    return oldone;
	}
	
	@Override
	public WarehouseShelfInventoryOptRecord add(WarehouseShelfInventoryOptRecord opt) {
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
	public WarehouseShelfInventoryOptRecord sub(WarehouseShelfInventoryOptRecord opt) {
		// TODO Auto-generated method stub
 		Float size = getSize(opt);
		WarehouseShelfInventory inv=new WarehouseShelfInventory();
		WarehouseShelfInventoryOptRecord record=new WarehouseShelfInventoryOptRecord();
		BeanUtil.copyProperties(opt, inv); 
		BeanUtil.copyProperties(opt, record);
	    WarehouseShelfInventory oldone =getOldOne(inv);
	    int result=0; 
	    if(oldone==null) { 
	    	Material m = materialService.getById(inv.getMaterialid());
	    	throw new BizException(m.getSku()+"当前库位没有该产品可以下货");
	    } 
	    oldone.setOperator(inv.getOperator());
	    if(oldone.getQuantity()-inv.getQuantity()<0) {
	    	Material m = materialService.getById(oldone.getMaterialid());
	    	throw new BizException(m.getSku()+"当前库位该产品没有足够的数量可以下货");
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

	public IPage<WarehouseShelfInventoryVo> getUnShelfInventoryList(Page<?> page, Map<String, Object> param) {
		return this.baseMapper.getUnShelfInventoryList(page,param);
	}

    
    public IPage<WarehouseShelfInventoryVo> getShelfInventoryList(Page<?> page, Map<String, Object> param) {
    	String shelfid=param.get("shelfid")==null?null:param.get("shelfid").toString();
    	if(param.get("allchildren")==null)return null;
    	IPage<WarehouseShelfInventoryVo> result = null;
    	if("true".equals(param.get("allchildren").toString())) {
    		if(shelfid!=null) {
    			WarehouseShelf  shelf = iWarehouseShelfService.getById(shelfid)  ;
        		param.put("addressid", shelf.getAddressid());
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
    		param.put("addresssid", shelf.getAddressid());
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
    
    public List<WarehouseShelfInventoryVo> getShelfInventoryStockList( Map<String, Object> param) {
        List<WarehouseShelfInventoryVo> result = this.baseMapper.getShelfInventoryStockList(param);  
    	if(result!=null&&result.size()>0) {
    		 for(WarehouseShelfInventoryVo item:result) {
     			item.setShelfname(iWarehouseShelfService.getAllParentName(item.getShelfid()));
     		}
    	}
		return result;
	}


	@Override
	public IPage<WarehouseShelfInventoryVo> getShelfInventoryStockList(Page<?> page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		String shelfid=param.get("shelfid")==null?null:param.get("shelfid").toString();
    	IPage<WarehouseShelfInventoryVo> result = null;
    	if(shelfid!=null) {
    			WarehouseShelf  shelf = iWarehouseShelfService.getById(shelfid)  ;
        		param.put("addressid", shelf.getAddressid());
        		param.put("treepath", shelf.getTreepath());
        		param.put("shelfid",null);
    	}  
        result =  this.baseMapper.getShelfInventoryStockList(page,param);  
    	if(result!=null&&result.getSize()>0) {
    		 for(WarehouseShelfInventoryVo item:result.getRecords()) {
     			item.setShelfname(iWarehouseShelfService.getAllParentName(item.getShelfid()));
     		}
    	}
		return result;
	}


	@Override
	public void downloadShelfStockList(SXSSFWorkbook workbook, List<WarehouseShelfInventoryVo> list) {
		Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
		titlemap.put("shelfname", "库位代码");
		titlemap.put("image", "产品图片");
		titlemap.put("sku", "本地SKU");
		titlemap.put("name", "产品名称");
		titlemap.put("quantity", "当前库位库存数量");
		titlemap.put("amount", "盘点数量");
		
		if (list.size() > 0 && list != null) {
			Sheet sheet = workbook.createSheet("sheet1");
			// 在索引0的位置创建行（最顶端的行）
			Row trow = sheet.createRow(0);
			Cell cell = null;
			Object[] titlearray = titlemap.keySet().toArray();
			for (int i = 0; i < titlearray.length; i++) {
				cell = trow.createCell(i); // 在索引0的位置创建单元格(左上端)
				Object value = titlemap.get(titlearray[i].toString());
				cell.setCellValue(value.toString());
			}
			// Font f = new Font("宋体", Font.PLAIN, 12);
			// FontMetrics fm = sun.font.FontDesignMetrics.getMetrics(f);
			for (int i = 0; i < list.size(); i++) {
				Row row = sheet.createRow(i + 1);
				WarehouseShelfInventoryVo item = list.get(i);
				for (int j = 0; j < titlearray.length; j++) {
					cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
					String shelfname = item.getShelfname();
					String image=item.getImage();
					String sku=item.getSku();
					String name=item.getName();
					Long quantity = item.getQuantity();
					if (j==0) {
						cell.setCellValue(shelfname);
					}
					if (j==1) {
						cell.setCellValue(image);
					}
					if (j==2) {
						cell.setCellValue(sku);
					}
					if (j==3) {
						cell.setCellValue(name);
					}
					if (j==4) {
						cell.setCellValue(quantity);
					}
				}
			}
		} else {
			try {
				throw new Exception("没有数据可导出！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	
}
