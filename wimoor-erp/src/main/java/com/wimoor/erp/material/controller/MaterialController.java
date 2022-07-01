package com.wimoor.erp.material.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.api.erp.assembly.pojo.vo.AssemblyVO;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.api.AdminClientOneFeign;
import com.wimoor.erp.assembly.service.IAssemblyService;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.material.pojo.entity.DimensionsInfo;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.pojo.entity.MaterialCategory;
import com.wimoor.erp.material.pojo.entity.MaterialCustoms;
import com.wimoor.erp.material.pojo.entity.StepWisePrice;
import com.wimoor.erp.material.pojo.vo.MaterialConsumableVO;
import com.wimoor.erp.material.pojo.vo.MaterialInfoVO;
import com.wimoor.erp.material.pojo.vo.MaterialSupplierVO;
import com.wimoor.erp.material.service.IDimensionsInfoService;
import com.wimoor.erp.material.service.IMaterialCategoryService;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.material.service.IStepWisePriceService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
 

@Api(tags = "产品接口")
@RestController
@RequestMapping("/api/v1/material")
@Slf4j
@RequiredArgsConstructor
public class MaterialController {
	
	   final IMaterialService iMaterialService;
	   
	   final IStepWisePriceService stepWisePriceService;
	   
	   final IDimensionsInfoService dimensionsInfoService;
	   
	   final IAssemblyService assemblyService;
	   
	   final IInventoryService inventoryService;
	   
	   final AdminClientOneFeign adminClientOneFeign;
	   
	   final IMaterialCategoryService materialCategoryService;
	   
	    /**
	     * 提供用于用户登录认证信息
	     */
	    @ApiOperation(value = "根据id 获取产品信息")
	    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path", dataType = "String")
	    @GetMapping("/id/{id}")
	    public Result<Map<String, Object>> getInfoBySku(@PathVariable String id) {
	    	//Map<String, Object> data = iMaterialService.findMaterialById(id);
	    	log.debug(id+"获取所有信息---时间："+new Date());
	        return Result.success(null);
	    }

	    @ApiOperation(value = "列表分页")
	    @ApiImplicitParams({
	            @ApiImplicitParam(name = "page", value = "页码", paramType = "query", dataType = "Long"),
	            @ApiImplicitParam(name = "limit", value = "每页数量", paramType = "query", dataType = "Long"),
	            @ApiImplicitParam(name = "search", value = "模糊查询", paramType = "query", dataType = "String"),
	            @ApiImplicitParam(name = "ftype", value = "查询类型当为shipment时只查询库存大于0的", paramType = "query", dataType = "String"),
	            @ApiImplicitParam(name = "isDelete", value = "是否逻辑删除", paramType = "query", dataType = "String"),
	            @ApiImplicitParam(name = "searchlist", value = "SKU批量查询", paramType = "query", dataType = "String"),
	            @ApiImplicitParam(name = "issfg", value = "组装类型", paramType = "query", dataType = "String"),
	            @ApiImplicitParam(name = "warehouseid", value = "仓库ID", paramType = "query", dataType = "String"),
	            @ApiImplicitParam(name = "categoryid", value = "产品分类ID", paramType = "query", dataType = "String"),
	            @ApiImplicitParam(name = "supplier", value = "供应商ID", paramType = "query", dataType = "String"),
	            @ApiImplicitParam(name = "owner", value = "负责人ID", paramType = "query", dataType = "String"),
	            @ApiImplicitParam(name = "name", value = "产品名称", paramType = "query", dataType = "String"),
	            @ApiImplicitParam(name = "remark", value = "产品备注", paramType = "query", dataType = "String"),
	            @ApiImplicitParam(name = "color", value = "颜色标签对应的颜色", paramType = "query", dataType = "String") 
	    })
	    
	    @GetMapping
	    public Result<IPage<Map<String, Object>>> list(Integer page, Integer limit, 
	    		String search, 
	    		String ftype,
	    		String isDelete,
	    		String searchlist,
	    		String issfg,
	    		String warehouseid,
	    		String categoryid,
	    		String supplier,
	    		String owner,
	    		String name,
	    		String remark,
	    		String color
	    		) {
	      
	     	Map<String, Object> map = new HashMap<String, Object>();
	    	UserInfo userinfo = UserInfoContext.get();
			String shopid = userinfo.getCompanyid();
 
			if (StringUtils.isNotBlank(search)) {
				search = "%" + search + "%";
			} else {
				search = null;
			}
 
			String[] list = null;
			if (StringUtils.isNotEmpty(searchlist)) {
				list = searchlist.split(",");
			}
			List<String> skulist = new ArrayList<String>();
			if (list != null) {
				for (int i = 0; i < list.length; i++) {
					if (StringUtils.isNotEmpty(list[i])) {
						skulist.add("%" + list[i] + "%");
					}
				}
			} else {
				skulist = null;
			}
		 
			if (StringUtils.isNotEmpty(issfg) && issfg.equals("true")) {
				issfg = "2";
			} else if (StringUtils.isEmpty(issfg) || issfg.equals("false")) {
				issfg = null;
			}
			List<Integer> issfglist = new ArrayList<Integer>();
			if (issfg != null) {
				String[] issfgarray = issfg.split(",");
				for (int i = 0; i < issfgarray.length; i++) {
					if (StringUtils.isNotEmpty(issfgarray[i])) {
						issfglist.add(Integer.parseInt(issfgarray[i]));
					}
				}
			} else {
				issfglist = null;
			}
	 
			if (StringUtils.isEmpty(warehouseid)) {
				warehouseid = null;
			}
			if (StringUtils.isEmpty(isDelete)) {
				isDelete = null;
			}
 
			if (StringUtils.isEmpty(categoryid)) {
				categoryid = null;
			}
		 
			if (StringUtils.isEmpty(supplier)) {
				supplier = null;
			}
	 
			if (StringUtils.isEmpty(owner)) {
				owner = null;
			}
	 
			if (StringUtils.isEmpty(name)) {
				name = null;
			} else {
				name = "%" + name + "%";
			}
			 
			if (StringUtils.isEmpty(remark)) {
				remark = null;
			} else {
				remark = "%" + remark + "%";
			}
		 
			if (StringUtils.isEmpty(color) || "all".equals(color)) {
				color = null;
			} else {
				color = "%" + color + "%";
			}
			map.put("shopid", shopid);
			map.put("search", search);
			map.put("searchsku", skulist);
			map.put("issfglist", issfglist);
			map.put("supplierid", supplier);
			map.put("categoryid", categoryid);
			map.put("name", name);
			map.put("remark", remark);
			map.put("owner", owner);
			map.put("color", color);
			map.put("warehouseid", warehouseid);
			map.put("ftype", ftype);
			map.put("isDelete", isDelete);
	 
	         //IPage<Map<String, Object>> result = iMaterialService.findByCondition(map, new Page<>(page, limit));
	         return Result.success(null);
	    }
	    
	    
	    @ApiOperation("根据本地产品ID查询产品详情")
	    @GetMapping("/getMaterialInfo")
		public Result<MaterialInfoVO> getMaterialInfoAction(@ApiParam("产品ID")@RequestParam String id) {
	    	MaterialInfoVO vo=new MaterialInfoVO();
			Map<String, Object> data = iMaterialService.findMaterialById(id);
			if (data == null) {
				return null;
			}
			UserInfo userinfo = UserInfoContext.get();
			String shopid=userinfo.getCompanyid();
			DimensionsInfo itemDim =null;
			DimensionsInfo pkgDim =null;
			DimensionsInfo boxDim =null;
			if(data.get("itemDimensions")!=null) {
				itemDim = dimensionsInfoService.getById(data.get("itemDimensions").toString());
			}
			if(data.get("pkgDimensions")!=null) {
				pkgDim = dimensionsInfoService.getById(data.get("pkgDimensions").toString());
			}
			if(data.get("boxDimensions")!=null) {
				boxDim = dimensionsInfoService.getById(data.get("boxDimensions").toString());
			}
			List<StepWisePrice> priceList = stepWisePriceService.selectByMaterial(data.get("id").toString());
			List<AssemblyVO> assemblyList = assemblyService.selectByMainmid(data.get("id").toString());
			List<MaterialConsumableVO> consumableList =  iMaterialService.selectConsumableByMainmid(data.get("id").toString(),shopid);
			List<MaterialSupplierVO> supplierList =iMaterialService.selectSupplierByMainmid(data.get("id").toString());
			MaterialCustoms Customs=iMaterialService.selectCustomsByMaterialId(data.get("id").toString());
			if (data.get("operator") != null) {
				Result<Map<String, Object>> info = adminClientOneFeign.getUserByUserId(data.get("operator").toString());
				if (info != null&&info.getData()!=null) {
					data.put("operator", info.getData().get("name"));
				}else {
					data.put("operator", "--");
				}
			}else{
				data.put("operator", "--");
			}
			if (data.get("creator") != null) {
				Result<Map<String, Object>> info = adminClientOneFeign.getUserByUserId(data.get("creator").toString());
				if (info != null&&info.getData()!=null) {
					data.put("creator", info.getData().get("name"));
				}else {
					data.put("creator", "--");
				}
			}else{
				data.put("creator", "--");
			}
			if(assemblyList!=null&&assemblyList.size()>0) {
				for(AssemblyVO items:assemblyList) {
		              Map<String, Object> submap = inventoryService.findInvTUDetailByParentId(items.getSubmid(), shopid);
		              if(submap!=null&&submap.size()>0) {
		            	  if(submap.get("inbound")!=null) {
		            		  items.setInbound(Integer.parseInt(submap.get("inbound").toString()));
		            	  }
		            	  if(submap.get("outbound")!=null) {
		            		  items.setOutbound(Integer.parseInt(submap.get("outbound").toString()));
		            	  }
		            	  if(submap.get("fulfillable")!=null) {
		            		  items.setFulfillable(Integer.parseInt(submap.get("fulfillable").toString()));
		            	  }
		              }
				}
			}
			vo.setMaterial(data);
			vo.setAssemblyList(assemblyList);
			vo.setBoxDim(boxDim);
			vo.setConsumableList(consumableList);
			vo.setCustoms(Customs);
			vo.setItemDim(itemDim);
			vo.setPkgDim(pkgDim);
			vo.setStepWisePrice(priceList);
			vo.setSupplierList(supplierList);
			return Result.success(vo);
		}
	    
	    
	    @ApiOperation("根据本地产品SKU查询产品详情")
	    @GetMapping("/getMaterialBySKU")
		public Result<Map<String,Object>> getMaterialBySKUAction(@ApiParam("SKU")@RequestParam String sku,
				@ApiParam("公司id")@RequestParam String shopid) {
	    	Map<String,Object> map=new HashMap<String, Object>();
	    	QueryWrapper<Material> materialQueryWrapper=new QueryWrapper<Material>();
	    	materialQueryWrapper.eq("sku", sku);
			materialQueryWrapper.eq("shopid", shopid);
			materialQueryWrapper.eq("isDelete", false);
	    	List<Material> list = iMaterialService.list(materialQueryWrapper);
	    	if(list!=null && list.size()>0) {
	    		Material material = list.get(0);
	    		DimensionsInfo dim_local =dimensionsInfoService.getById(material.getPkgdimensions());
	    		if (dim_local == null) {
	    			map.put("code", "fail");
	    		}else {
	    			map.put("cost", material.getPrice());
	    			MaterialCategory cate = materialCategoryService.getById(material.getId());
	    			if(cate!=null) {
	    				String cateName = cate.getName();
	    				map.put("catename", cateName);
	    			}
	    			map.put("height", dim_local.getHeight());
	    			map.put("height_unit", dim_local.getHeightUnits());
	    			map.put("length", dim_local.getLength());
	    			map.put("length_unit", dim_local.getLengthUnits());
	    			map.put("weight", dim_local.getWeight());
	    			map.put("weight_unit", dim_local.getWeightUnits());
	    			map.put("width", dim_local.getWidth());
	    			map.put("width_unit", dim_local.getWidthUnits());
	    		}
	    		map.put("code", "success");
	    	}else {
	    		map.put("code", "fail");
	    	}
			return Result.success(map);
	    }
	    
	    
	    
	    
}
