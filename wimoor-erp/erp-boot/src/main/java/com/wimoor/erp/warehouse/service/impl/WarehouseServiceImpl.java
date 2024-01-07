package com.wimoor.erp.warehouse.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.api.AdminClientOneFeignManager;
import com.wimoor.erp.assembly.mapper.AssemblyFormMapper;
import com.wimoor.erp.assembly.pojo.entity.AssemblyForm;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.inventory.mapper.InventoryMapper;
import com.wimoor.erp.inventory.pojo.entity.Inventory;
import com.wimoor.erp.purchase.mapper.PurchasePlanMapper;
import com.wimoor.erp.stock.mapper.ChangeWhFormMapper;
import com.wimoor.erp.stock.mapper.DispatchFormMapper;
import com.wimoor.erp.stock.pojo.entity.ChangeWhForm;
import com.wimoor.erp.stock.pojo.entity.DispatchForm;
import com.wimoor.erp.warehouse.mapper.WarehouseMapper;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
 

  
@Service("warehouseService")
@RequiredArgsConstructor
public class WarehouseServiceImpl extends  ServiceImpl<WarehouseMapper,Warehouse> implements IWarehouseService {
	 
	final ISerialNumService serialnumService;
	 
	final InventoryMapper inventoryMapper;
	 
	final PurchasePlanMapper purchasePlanMapper;
	 
	final AssemblyFormMapper assemblyFormMapper;
	 
	final ChangeWhFormMapper changeWhFormMapper;
	 
	final DispatchFormMapper dispatchFormMapper;

	final AdminClientOneFeignManager adminClientOneFeign;
	


	@Cacheable(value = "warehosueCache" )
	public List<Warehouse> findByType(String ftype, String shopid) {
		return this.baseMapper.findByType(ftype, shopid);
	}

	public String selectTypeByName(String ftypename) {
		return this.baseMapper.selectTypeByName(ftypename);
	}

	

	/**
	 * 获取所有自有仓，精确到仓位（正品仓，废品仓，测试仓）
	 */
	public List<Warehouse> selectSelfAllByShopId(String shopid) {
		QueryWrapper<Warehouse> queryWrapper = new QueryWrapper<Warehouse>();
		queryWrapper.eq("shopid", shopid);
		queryWrapper.like("ftype",  "self_%");
		queryWrapper.eq("disabled", false);
		queryWrapper.isNull("fbawareid");
		queryWrapper.orderByDesc("isdefault");
		return this.baseMapper.selectList(queryWrapper);
	}

	/**
	 * 获取默认正品仓
	 */
	public List<Warehouse> selectDefaultSelfUsableByShopId(String shopid) {
		QueryWrapper<Warehouse> queryWrapper = new QueryWrapper<Warehouse>();
		queryWrapper.eq("shopid", shopid);
		queryWrapper.eq("disabled", false);
		queryWrapper.like("ftype",  "self_usable");// 正品仓
		queryWrapper.orderByDesc("isdefault");
		queryWrapper.orderByAsc("number");
		List<Warehouse> list = this.baseMapper.selectList(queryWrapper);
		return list;
	}
	
	public List<Warehouse> getSelfAllByShopIdOrderByDefault(String shopid) {
		 QueryWrapper<Warehouse> queryWrapper = new QueryWrapper<Warehouse>();
		 queryWrapper.eq("shopid", shopid);
		 queryWrapper.eq("ftype", "self");
		 queryWrapper.eq("disabled", false);
		 queryWrapper.orderByDesc("isdefault");
		 queryWrapper.orderByAsc("number");
		 return this.list(queryWrapper);
	}
 
	public List<Warehouse> getWarehouseTreeList(UserInfo user){
		 QueryWrapper<Warehouse> queryWrapper=new QueryWrapper<Warehouse>();
		 queryWrapper.eq("shopid", user.getCompanyid());
		 queryWrapper.like("ftype", "self%");
		 queryWrapper.eq("disabled", false);
		 queryWrapper.isNull("fbawareid");
		 queryWrapper.orderByAsc("findex");
		 List<Warehouse>  list = this.list(queryWrapper);
		   Map<String, String> usermap = adminClientOneFeign.getAllUserName();
		 List<Warehouse> result= recursionWarehouseSelectList(user,null,list,usermap);
		 for (int i = 0; i < result.size(); i++) {
	        	Warehouse trees = result.get(i);
				if(trees.getChildren()==null || trees.getChildren().size()==0) {
					result.remove(trees);
				}
		 }
		 return result;
	}
	
	//获取仓库下面的子仓位函数 递归生成
	public List<Warehouse> recursionWarehouseSelectList(UserInfo user,String parentId, List<Warehouse> wareList,Map<String, String> usermap ) {
    	List<Warehouse> warehouseTreeSelectList=new ArrayList<Warehouse>();
    	if(wareList==null||wareList.size()==0)return warehouseTreeSelectList;
        Optional.ofNullable(wareList).orElse(new ArrayList<>())
            .stream()
            .filter(warehouse -> (parentId==null&&warehouse.getParentid()==null)||(parentId!=null&&warehouse.getParentid()!=null&&warehouse.getParentid().equals(parentId)))
            .forEach(warehouse -> {
            	Warehouse treeSelectVO = new Warehouse();
            	BeanUtil.copyProperties(warehouse, treeSelectVO);
            	try {
            		
            		if(usermap!=null) {
            			treeSelectVO.setOperator(usermap.get(treeSelectVO.getOperator()));
            		}
            	}catch(Exception e) {
            		e.printStackTrace();
            	}
            	
                List<Warehouse> children = recursionWarehouseSelectList(user,warehouse.getId(), wareList,usermap);
                if (CollectionUtil.isNotEmpty(children)) {
                	for(Warehouse item:children) {
                		item.setName(item.getName().replace(warehouse.getName()+"-",""));
                		item.setValue(item.getId());
                		item.setLabel(item.getName());
                	}
                    treeSelectVO.setChildren(children);
                } 
                warehouseTreeSelectList.add(treeSelectVO);
            });
        
        Collections.sort(warehouseTreeSelectList, new Comparator<Warehouse>() {
			@Override
			public int compare(Warehouse o1, Warehouse o2) {
				// TODO Auto-generated method stub
				int findex1 = o1.getFindex()!=null?o1.getFindex():100000;
				int findex2 = o2.getFindex()!=null?o2.getFindex():100000;
				return findex1-findex2;
			}
        });//使用Collections的sort方法，并且重写compare方法
       return warehouseTreeSelectList;
    }
	

	public List<Map<String, Object>> selectFbaSale(String shopid, String sku, String delivery_cycle) {
		return this.baseMapper.selectFbaSale(shopid, sku, delivery_cycle);
	}

	public List<Map<String, Object>> selectSelfSale(String shopid, List<String> warehouseidList, String materialid) {
		return this.baseMapper.selectSelfSale(shopid, warehouseidList, materialid);
	}

	public Integer saveCheckOut(String wname, String sernum, String parentid) {
		return this.baseMapper.saveCheckOut(wname, sernum, parentid);
	}

	public Integer saveQuality(String wname, String sernum, String parentid) {
		return this.baseMapper.saveQuality(wname, sernum, parentid);
	}

	public Integer saveScrap(String wname, String sernum, String parentid) {
		return this.baseMapper.saveScrap(wname, sernum, parentid);
	}

	// 同时删除父亲的数据和同级的数据
	public Integer deleteInfoById(String id) throws ERPBizException {
		int result = 0;// 总操作条数
		// 父亲(找到父亲的孩子里,是否有inventory或者plan在用它,有则不能disabled)
		Warehouse warehouse = this.baseMapper.selectById(id);
		if (warehouse != null) {
			QueryWrapper<Warehouse> queryWrapper = new QueryWrapper<Warehouse>();
			queryWrapper.eq("parentid", warehouse.getId());
			List<Warehouse> wlist = this.baseMapper.selectList(queryWrapper);
			if(warehouse.getParentid()!=null) {
				QueryWrapper<Warehouse> queryWrapperWarehouse = new QueryWrapper<Warehouse>();
				queryWrapperWarehouse.eq("parentid", warehouse.getParentid());
				queryWrapperWarehouse.eq("disabled",false);
				List<Warehouse> wBrotherlist = this.baseMapper.selectList(queryWrapperWarehouse);
				if (wBrotherlist != null && wBrotherlist.size()== 1) {
					throw new ERPBizException("仓库中至少需要一个仓位！");
				}
			}
		
			QueryWrapper<Inventory> queryWrapperInventory = new QueryWrapper<Inventory>();
			queryWrapperInventory.eq("warehouseid", warehouse.getId());
			List<Inventory> stocklist = inventoryMapper.selectList(queryWrapperInventory);
			if (stocklist != null && stocklist.size() > 0) {
				for(Inventory item:stocklist) {
					if(item.getQuantity()>0) {
						throw new ERPBizException("该仓库还存在着产品库存！");
					}
				}
			}
			for (int i = 0; i < wlist.size(); i++) {
				Warehouse subwarehouse = wlist.get(i);
				List<AssemblyForm> asseList = assemblyFormMapper.getAssemblyFormByWarehouseid(subwarehouse.getId());
				if (asseList != null && asseList.size() > 0) {
					throw new ERPBizException("该仓库还有产品库存！");
				}
				List<ChangeWhForm> changeList = changeWhFormMapper.getChangeWhFormByWarehouseid(subwarehouse.getId());
				if (changeList != null && changeList.size() > 0) {
					throw new ERPBizException("该仓库还有产品库存！");
				}
				List<DispatchForm> dispatchList = dispatchFormMapper.getDispatchFormByWarehouseid(subwarehouse.getId());
				if (dispatchList != null && dispatchList.size() > 0) {
					throw new ERPBizException("该仓库还有产品库存！");
				}
				QueryWrapper<Inventory> queryWrapperInventory2 = new QueryWrapper<Inventory>();
				queryWrapperInventory2.eq("warehouseid", subwarehouse.getId());
				List<Inventory> incList = inventoryMapper.selectList(queryWrapperInventory2);
				if (incList != null && incList.size() > 0) {
					throw new ERPBizException("该仓库还有产品库存！");
				}
				wlist.get(i).setDisabled(true);
				this.baseMapper.updateById(subwarehouse);
				result += 1;
			}
			warehouse.setDisabled(true);
			if(warehouse.getIsdefault()&&warehouse.getFtype().equals("self_usable")) {
				 throw new ERPBizException("默认仓位不支持删除，请将其它正品仓位设置为默认之后再删除此仓位。");
			}
			this.baseMapper.updateById(warehouse);
			
			result += 1;
		}
		return result;
	}

	public Integer saveMyware(Warehouse wh) throws ERPBizException {
		QueryWrapper<Warehouse> queryWrapper = new QueryWrapper<Warehouse>();
		queryWrapper.eq("name", wh.getName());
		queryWrapper.eq("shopid", wh.getShopid());
		queryWrapper.eq("ftype", wh.getFtype());
		List<Warehouse> warehouselist = this.baseMapper.selectList(queryWrapper);
		boolean result = false;
		if(warehouselist != null && warehouselist.size() > 0) {
			Warehouse warehouse = warehouselist.get(0);
			if(warehouse.getDisabled()) {
				wh.setId(warehouse.getId());
				result = this.updateById(wh);
			}else {
				throw new ERPBizException("该仓库已经存在！");
			}
		}else {
			result = save(wh);
		}
		if (result) {
			 return 1;
		} else {
			return -1;
		}
	}
	// 级联更新
	public Integer updateMyware(Warehouse wh, String parentid) throws ERPBizException {
		int result = 0;
		// 当前的wh是父亲的对象
		if(wh.getIsdefault()==true){
			clearDefaultWare(wh.getShopid(),wh.getFtype());
		} 
		result += this.baseMapper.updateById(wh);
		return result;
	}

	

	public Warehouse findAvailableBySelf(String warehouseid) {
		QueryWrapper<Warehouse> queryWrapper = new QueryWrapper<Warehouse>();
		if (GeneralUtil.isEmpty(warehouseid)) {
			return null;
		}
		queryWrapper.eq("parentid", warehouseid);
		queryWrapper.orderByDesc("opttime");
		List<Warehouse> list = this.baseMapper.selectList(queryWrapper);
		Warehouse warehouse = null;
		Warehouse temp = null;
		for (int i = 0; i < list.size(); i++) {
			temp = list.get(i);
			if (!"self_unusable".equals(temp.getFtype())) {
				warehouse = temp;
				if ("self_usable".equals(temp.getFtype())) {
					break;
				}
			}
		}
		return warehouse;
	}

	public LinkedList<Map<String, Object>> selectShipFbaSale(String shopid, String sku, String materialid, String planid, String groupid) {
		return this.baseMapper.selectShipFbaSale(shopid, sku, materialid, planid,groupid);
	}

	public List<Warehouse> getSubWarehouse(String warehouseid) {
		QueryWrapper<Warehouse> queryWrapper = new QueryWrapper<Warehouse>();
		queryWrapper.eq("parentid", warehouseid);
		queryWrapper.eq("disabled", 0);
		queryWrapper.orderByDesc("isdefault","ftype");
		queryWrapper.orderByAsc("findex");
		return list(queryWrapper);
	}

	public Warehouse getSubWarehouseByType(String warehouseid,String type) {
		if(GeneralUtil.isNotEmpty(type)){
			List<Warehouse> warelist = getSubWarehouse(warehouseid); 
			if(warelist != null && warelist.size() > 0){
				for (int i = 0; i < warelist.size(); i++) {
					if(warelist.get(i).getFtype().equals(type) && warelist.get(i).getParentid()!=null){
						return warelist.get(i);
					}
				}
			}
		}
		return null;
	}
	
	public List<Warehouse> getSubWarehouseListByType(String warehouseid,String type) {
		if(GeneralUtil.isNotEmpty(type) && GeneralUtil.isNotEmpty(warehouseid)){
			String[] warehouseArray = warehouseid.split(",");
			List<String> warehouseList = new ArrayList<String>();
			for(int i =0; i < warehouseArray.length; i++) {
				warehouseList.add(warehouseArray[i]);
			}
			QueryWrapper<Warehouse> queryWrapper = new QueryWrapper<Warehouse>();
			queryWrapper.in("parentid", warehouseList);
			queryWrapper.eq("disabled", 0);
			queryWrapper.eq("ftype", type);
			return this.baseMapper.selectList(queryWrapper);
		}
		return null;
	}
	
	public Warehouse getSelfWarehouse(String warehouseid) {
		if(GeneralUtil.isNotEmpty(warehouseid)){
			Warehouse warehouse = this.baseMapper.selectById(warehouseid);
			if(warehouse.getParentid() == null) {
				return warehouse;
			}else {
				return this.baseMapper.selectById(warehouse.getParentid());
			}
		}
		return null;
	}
	
	public Map<String, Object> getTotalInvAndWorth(String warehouseid) {
		return this.baseMapper.getTotalInvAndWorth(warehouseid);
	}

	public Warehouse getParentWarehouse(String warehouseid) {
		return this.baseMapper.getParentWarehouse(warehouseid);
	}


	public void clearDefaultWare(String shopid,String ftype) {
		this.baseMapper.clearWareDefault(shopid,ftype);
	}

	public List<Warehouse> findBydefault(String shopid) {
		return this.baseMapper.findWareDefault(shopid);
	}
	
	public Warehouse getWarehouseByid(String warehouseid) {
		return this.baseMapper.selectById(warehouseid);
	}
	
	public Warehouse getWarehouseByName(String shopid, String whname) {
		QueryWrapper<Warehouse> queryWrapper = new QueryWrapper<Warehouse>();
		queryWrapper.eq("disabled", false);
		queryWrapper.eq("shopid", shopid);
		queryWrapper.eq("name", whname);
		List<Warehouse> list = this.list(queryWrapper);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public void saveWareTypeList(String warehouselistStr, String shopid, UserInfo user,Warehouse house) throws Exception {
		JSONArray warehouselistArray = GeneralUtil.getJsonArray(warehouselistStr);
		if(warehouselistArray!=null && warehouselistArray.size()>0) {
			for(int i=0;i<warehouselistArray.size();i++) {
				Warehouse wh=new Warehouse();
				JSONObject item = warehouselistArray.getJSONObject(i);
				wh.setName(house.getName()+"-"+item.getString("name"));
				String ftype = item.getString("ftype");
				boolean isdefault = item.getBooleanValue("isdefault");
				wh.setAddress(house.getAddress());
				wh.setDisabled(false);
				wh.setFlevel("2");
				String sernum=null;
				if(isdefault) {
					//查出相对应的self_xxx仓的默认仓 取消掉。
					QueryWrapper<Warehouse> queryWrapper = new QueryWrapper<Warehouse>();
					queryWrapper.eq("ftype","self_"+ftype);
					queryWrapper.eq("shopid",shopid);
					List<Warehouse> ftypelist = this.baseMapper.selectList(queryWrapper);
					if(ftypelist!=null && ftypelist.size()>0) {
						for (Warehouse warehouse : ftypelist) {
							warehouse.setIsdefault(false);
							this.baseMapper.updateById(warehouse);
						} 
					}
				}
				if("test".equals(ftype)) {
					sernum = serialnumService.readSerialNumber(shopid, "T");
				}else if("usable".equals(ftype)) {
					sernum = serialnumService.readSerialNumber(shopid, "Q");
				}else if("unusable".equals(ftype)) {
					sernum = serialnumService.readSerialNumber(shopid, "S");
				}
				wh.setFtype("self_"+ftype);
				wh.setNumber(sernum);
				wh.setIsdefault(isdefault);
				wh.setOperator(user.getId());
				wh.setOpttime(new Date());
				wh.setParentid(house.getId());
				if(item.getIntValue("safetime")==0) {
					wh.setStockingCycle(house.getStockingCycle());
				}else {
					wh.setStockingCycle(item.getIntValue("safetime"));
				}
				if(item.getIntValue("mintime")==0) {
					wh.setMincycle(house.getMincycle());
				}else {
					wh.setMincycle(item.getIntValue("mintime"));
				}
				wh.setShopid(shopid);
				wh.setRemark(item.getString("remark"));
				this.baseMapper.insert(wh);
			}
		}
	}

	@Override
	public List<Warehouse> findWarehouselistByParent(String warehouseid) {
		QueryWrapper<Warehouse> queryWrapper = new QueryWrapper<Warehouse>();
		queryWrapper.eq("parentid",warehouseid);
		queryWrapper.eq("disabled",false);
		queryWrapper.orderByAsc("findex");
		List<Warehouse> list = this.baseMapper.selectList(queryWrapper);
		return list;
	}

	@Override
	public int updateWarehousePlace(Map<String, Object> map) throws Exception {
		String shopid=map.get("shopid").toString();
		//取消该仓库-仓位类型其它的默认仓库
		boolean isdefault=false;
		if(map.get("isdefault")!=null) {
			String defaultstr=map.get("isdefault").toString();
			if("true".equals(defaultstr)) {
				isdefault=true;
			}
		}
		if(isdefault==true) {
			QueryWrapper<Warehouse> queryWrapper = new QueryWrapper<Warehouse>();
			queryWrapper.eq("ftype",map.get("ftype").toString());
			queryWrapper.eq("parentid",map.get("parentid").toString());
			queryWrapper.eq("isdefault",true);
			List<Warehouse> list = this.baseMapper.selectList(queryWrapper);
			if(list!=null && list.size()>0) {
				for(int i=0;i<list.size();i++) {
					Warehouse item = list.get(i);
					item.setIsdefault(false);
					this.baseMapper.updateById(item);
				}
			}
		}else {
			//如果当前没有默认的了 则把当前父亲的自动带的仓库设置为默认
			QueryWrapper<Warehouse> queryWrapper = new QueryWrapper<Warehouse>();
			queryWrapper.eq("ftype",map.get("ftype").toString());
			queryWrapper.eq("parentid",map.get("parentid").toString());
			queryWrapper.eq("isdefault",false);
			List<Warehouse> list = this.baseMapper.selectList(queryWrapper);
			if(list!=null && list.size()>0) {
				for(int i=0;i<list.size();i++) {
					Warehouse item = list.get(i);
					item.setIsdefault(true);
					this.baseMapper.updateById(item);
				}
			}
		}
		String parentid=map.get("parentid").toString();
		Warehouse parentwarehouse = this.baseMapper.selectById(parentid);
		String wname=parentwarehouse.getName()+"-"+map.get("name").toString();
		
		QueryWrapper<Warehouse> queryWrapper = new QueryWrapper<Warehouse>();
		queryWrapper.eq("name", wname);
		queryWrapper.eq("parentid",parentid);
		if(map.get("id")!=null) {
			queryWrapper.notIn("id", map.get("id").toString());
		}
		List<Warehouse> warehouselist = this.baseMapper.selectList(queryWrapper);
		Warehouse oldone=null;
		if(warehouselist!=null && warehouselist.size()>0) {
			oldone=warehouselist.get(0);
			if(oldone.getDisabled()==true) {
				map.put("id",oldone.getId());
			}
			if(oldone.getDisabled()==false) {
				throw new ERPBizException("该仓库名已经存在！");
			}
		}
		
		if(map.get("id")!=null) {
			//修改操作
			String id = map.get("id").toString();
			Warehouse warehouse = this.baseMapper.selectById(id);
			if(warehouse!=null) {
				int mincycle=0;
				if(map.get("mincycle")!=null) {
					String mincyclestr = map.get("mincycle").toString();
					mincycle=Integer.parseInt(mincyclestr);
					warehouse.setMincycle(mincycle);
				}else {
					warehouse.setMincycle(parentwarehouse.getMincycle());
				}
				int stockcycle=0;
				if(map.get("stockcycle")!=null) {
					String stockcyclestr = map.get("stockcycle").toString();
					stockcycle=Integer.parseInt(stockcyclestr);
					warehouse.setStockingCycle(stockcycle);
				}else {
					warehouse.setStockingCycle(parentwarehouse.getStockingCycle());
				}
				if(map.get("remark")!=null) {
					warehouse.setRemark(map.get("remark").toString());
				}
				warehouse.setIsdefault(isdefault);
				warehouse.setOpttime(new Date());
				warehouse.setOperator(map.get("operator").toString());
				warehouse.setName(wname);
				warehouse.setDisabled(false);
				return this.baseMapper.updateById(warehouse);
			}
			return 0;
		}else {
			//做新增的操作
			String ftype=map.get("ftype").toString();
			if(map.get("parentid")!=null) {
				Warehouse warehouse=new Warehouse();
				warehouse.setAddress(parentwarehouse.getAddress());
				warehouse.setFlevel("2");
				warehouse.setFindex(null);
				warehouse.setFtype(ftype);
				warehouse.setDisabled(false);
				warehouse.setIsdefault(isdefault);
				warehouse.setIsstocktaking(false);
				int mincycle=0;
				if(map.get("mincycle")!=null) {
					String mincyclestr = map.get("mincycle").toString();
					mincycle=Integer.parseInt(mincyclestr);
					warehouse.setMincycle(mincycle);
				}else {
					warehouse.setMincycle(parentwarehouse.getMincycle());
				}
				int stockcycle=0;
				if(map.get("stockcycle")!=null) {
					String stockcyclestr = map.get("stockcycle").toString();
					stockcycle=Integer.parseInt(stockcyclestr);
					warehouse.setStockingCycle(stockcycle);
				}else {
					warehouse.setStockingCycle(parentwarehouse.getStockingCycle());
				}
				if(map.get("remark")!=null) {
					warehouse.setRemark(map.get("remark").toString());
				}
				String sernum=null;
				if("self_test".equals(ftype)) {
					sernum = serialnumService.readSerialNumber(shopid, "T");
				}else if("self_usable".equals(ftype)) {
					sernum = serialnumService.readSerialNumber(shopid, "Q");
				}else if("self_unusable".equals(ftype)) {
					sernum = serialnumService.readSerialNumber(shopid, "S");
				}
				warehouse.setOpttime(new Date());
				warehouse.setOperator(map.get("operator").toString());
				warehouse.setName(wname);
				warehouse.setNumber(sernum);
				warehouse.setParentid(parentid);
				warehouse.setShopid(shopid);
				return this.baseMapper.insert(warehouse);
			}
			return 0;
		}
	}

	@Override
	public List<Warehouse> getPlaceWarehouseList(String id) {
		QueryWrapper<Warehouse> queryWrapper = new QueryWrapper<Warehouse>();
		queryWrapper.eq("parentid",id);
		queryWrapper.eq("disabled",false);
		return this.baseMapper.selectList(queryWrapper);
	}

	@Override
	public Warehouse getPlaceWarehouse(String id) {
		return this.baseMapper.selectById(id);
	}



	@Override
	public String getUUID() {
		// TODO Auto-generated method stub
		return this.baseMapper.getUUID();
	}

	@Cacheable(value = "warehosueCache" )
	public IPage<Warehouse> findByCondition(Page<?> page,String search, String shopid, String ftype,String parentid ) {
		if(GeneralUtil.isEmpty(search)) {
			search = null;
		}else {
			search = "%"+search.trim() + "%";
		}
		if(StrUtil.isBlankOrUndefined(parentid)) {
			parentid=null;
		}
		return this.baseMapper.findByCondition(page,search,shopid,ftype,parentid);
	}

	@Override
	@Cacheable(value = "warehosueCache" )
	public List<Warehouse> getOverseaWarehouse(String shopid, String ftype, String groupid, String country) {
		// TODO Auto-generated method stub
		return this.baseMapper.getOverseaWarehouse(shopid,ftype, groupid, country);
	}
 
 
}
