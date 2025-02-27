package com.wimoor.erp.material.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.mvc.FileUpload;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.IPictureService;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.common.user.UserLimitDataType;
import com.wimoor.erp.api.AdminClientOneFeignManager;
import com.wimoor.erp.assembly.pojo.entity.Assembly;
import com.wimoor.erp.assembly.pojo.vo.AssemblyVO;
import com.wimoor.erp.assembly.service.IAssemblyService;
import com.wimoor.erp.common.pojo.entity.Status;
import com.wimoor.erp.common.service.IExcelDownLoadService;
import com.wimoor.erp.inventory.pojo.entity.Inventory;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.material.pojo.dto.MaterialDTO;
import com.wimoor.erp.material.pojo.dto.PlanDTO;
import com.wimoor.erp.material.pojo.entity.DimensionsInfo;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.pojo.entity.MaterialCategory;
import com.wimoor.erp.material.pojo.entity.MaterialConsumable;
import com.wimoor.erp.material.pojo.entity.MaterialCustoms;
import com.wimoor.erp.material.pojo.entity.MaterialSupplierStepwise;
import com.wimoor.erp.material.pojo.entity.StepWisePrice;
import com.wimoor.erp.material.pojo.vo.MaterialConsumableVO;
import com.wimoor.erp.material.pojo.vo.MaterialInfoVO;
import com.wimoor.erp.material.pojo.vo.MaterialSupplierVO;
import com.wimoor.erp.material.pojo.vo.MaterialVO;
import com.wimoor.erp.material.service.IDimensionsInfoService;
import com.wimoor.erp.material.service.IMaterialCategoryService;
import com.wimoor.erp.material.service.IMaterialConsumableService;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.material.service.IMaterialSupplierService;
import com.wimoor.erp.material.service.IStepWisePriceService;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormEntry;
import com.wimoor.erp.purchase.service.IPurchaseFormEntryService;
import com.wimoor.erp.stock.pojo.entity.OutWarehouseForm;
import com.wimoor.erp.stock.pojo.entity.OutWarehouseFormEntry;
import com.wimoor.erp.stock.service.IOutWarehouseFormEntryService;
import com.wimoor.erp.stock.service.IOutWarehouseFormService;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import cn.hutool.core.util.StrUtil;
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
	   
	   final AdminClientOneFeignManager adminClientOneFeign;
	   
	   final IMaterialCategoryService materialCategoryService;
	   
	   final ISerialNumService serialNumService;
	   
	   final FileUpload fileUpload;
	   
	   final IPictureService iPictureService;
	   
	   final IWarehouseService warehouseService;
	   
	   final IPurchaseFormEntryService purchaseFormEntryService;
	   
	   final IExcelDownLoadService excelDownLoadService;
	   
	   final IMaterialSupplierService iMaterialSupplierService;
	   
	   final IMaterialConsumableService iMaterialConsumableService;
	   @Autowired
	   @Lazy
	   IOutWarehouseFormService iOutWarehouseFormService;
	   @Autowired
	   @Lazy
	   IOutWarehouseFormEntryService iOutWarehouseFormEntryService;
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
	    @ApiOperation(value = "根据SKU获取产品采购价格")
	    @GetMapping("/getRealityPrice")
	    public Result<Map<String,Object>> getRealityPrice(@RequestParam String shopid,@RequestParam String sku) {
	    	   Material material = iMaterialService.selectBySKU(shopid, sku);
        	   if(material!=null) {
        		   Map<String,Object> mpriceMap=iMaterialService.getRealityPrice(material.getId());
        		   return Result.success(mpriceMap);
        	   }else {
        		   throw new BizException("未找到本地SKU"+sku);
        	   }
          }
	    
	    @ApiOperation(value = "根据SKU获取产品ID")
	    @GetMapping("/getIdByMSku")
	    public Result<String> getIdBySku(@RequestParam String shopid,@RequestParam String sku) {
	    	Material material = iMaterialService.selectBySKU(shopid, sku);
	    	if(material!=null) {
	    		return Result.success(material.getId());
	    	}else {
	    		return Result.failed("未找到对应的本地产品");
	    	}
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
	    
	    @PostMapping("/list")
	    public Result<IPage<Map<String, Object>>> list(@RequestBody MaterialDTO dto) {
	     	Map<String, Object> map = new HashMap<String, Object>();
	    	UserInfo userinfo = UserInfoContext.get();
	    	String ftype= GeneralUtil.getValueWithoutBlank( dto.getFtype());
			String shopid = userinfo.getCompanyid();
			String search=dto.getSearch();
			String searchlist=dto.getSearchlist();
			String issfg=dto.getIssfg();
			String warehouseid = GeneralUtil.getValueWithoutBlank(dto.getWarehouseid());
			String isDelete = GeneralUtil.getValueWithoutBlank(dto.getIsDelete());
			String categoryid = GeneralUtil.getValueWithoutBlank(dto.getCategoryid());
			String supplier = GeneralUtil.getValueWithoutBlank(dto.getSupplier());
			String owner = GeneralUtil.getValueWithoutBlank(dto.getOwner());
			String name=GeneralUtil.getValueWithoutBlank(dto.getName()); 
			String remark= GeneralUtil.getValueWithoutBlank(dto.getRemark()); 
			String color=dto.getColor();
			String addressid= GeneralUtil.getValueWithoutBlank(dto.getAddressid());
			String materialid=GeneralUtil.getValueWithoutBlank(dto.getMaterialid());
			String productname=dto.getProductname();
			String searchtype=dto.getSearchtype();
			if (StrUtil.isBlankOrUndefined(search)) {
				search = null;
			} else {
				search = "%" + search.trim()+ "%";
			}
			if(StrUtil.isNotEmpty(productname)) {
				name = "%" + productname.trim()+ "%";
		 
			}
			String[] list = null;
			if (StrUtil.isNotEmpty(searchlist)) {
				list = searchlist.split(",");
			}
			List<String> skulist = new ArrayList<String>();
			if (list != null) {
				for (int i = 0; i < list.length; i++) {
					if (StrUtil.isNotEmpty(list[i])) {
						skulist.add("%" + list[i] + "%");
					}
				}
			} else {
				skulist = null;
			}
		 
			if ( !StrUtil.isBlankOrUndefined(issfg) && issfg.equals("true")) {
				issfg = "2";
			} else if (StrUtil.isBlankOrUndefined(issfg) || issfg.equals("false")) {
				issfg = null;
			}
			List<Integer> issfglist = new ArrayList<Integer>();
			if (issfg != null) {
				String[] issfgarray = issfg.split(",");
				for (int i = 0; i < issfgarray.length; i++) {
					if (StrUtil.isNotEmpty(issfgarray[i])) {
						issfglist.add(Integer.parseInt(issfgarray[i]));
					}
				}
			} else {
				issfglist = null;
			}
	
			 
			if (StrUtil.isBlankOrUndefined(name)) {
				name = null;
			} else {
				name = "%" + name + "%";
			}
			 
			if (StrUtil.isBlankOrUndefined(remark)) {
				remark = null;
			} else {
				remark = "%" + remark + "%";
			}
		 
			if (StrUtil.isBlankOrUndefined(color) || "all".equals(color)) {
				color = null;
			} else {
				color = "%" + color.trim() + "%";
			}
			if (userinfo.isLimit(UserLimitDataType.owner)) {
				owner=userinfo.getId();
    		}
			if(StrUtil.isNotBlank(dto.getMtype())) {
				if(dto.getMtype().equals("package")) {
					map.put("mtype",2);
				}else if(dto.getMtype().equals("consumable")) {
					map.put("mtype", 1);
				}else if(dto.getMtype().equals("product")) {
					map.put("mtype", 0);
				}
			}
			map.put("shopid", shopid);
			map.put("search", search);
			map.put("searchtype", searchtype);
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
			map.put("addressid", addressid);
			map.put("materialid", materialid);
			map.put("mainid", dto.getMainid()!=null?dto.getMainid():null);
			map.put("withoutTags", dto.getWithoutTags()==null?false:dto.getWithoutTags());
			map.put("withPriceHis", dto.getWithPriceHis()==null?false:dto.getWithPriceHis());
			map.put("midlist", dto.getMaterialList()!=null&&dto.getMaterialList().size()>0?dto.getMaterialList():null);
			List<String> taglist = dto.getTaglist();//123,456,122,
			if(taglist!=null && taglist.size()>0) {
				map.put("taglist", taglist);
			}else {
				map.put("taglist", null);
			}
	        IPage<Map<String, Object>> result = iMaterialService.findByCondition(dto.getPage(),map);
	        return Result.success(result);
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
	    @PostMapping("/consumableList")
	    public Result<IPage<Map<String, Object>>> consumableList(@RequestBody MaterialDTO dto) {
	     	Map<String, Object> map = new HashMap<String, Object>();
	    	UserInfo userinfo = UserInfoContext.get();
	    	String ftype= GeneralUtil.getValueWithoutBlank( dto.getFtype());
			String shopid = userinfo.getCompanyid();
			String search=dto.getSearch();
			String searchlist=dto.getSearchlist();
			String issfg=dto.getIssfg();
			String warehouseid = GeneralUtil.getValueWithoutBlank(dto.getWarehouseid());
			String isDelete = GeneralUtil.getValueWithoutBlank(dto.getIsDelete());
			String categoryid = GeneralUtil.getValueWithoutBlank(dto.getCategoryid());
			String supplier = GeneralUtil.getValueWithoutBlank(dto.getSupplier());
			String owner = GeneralUtil.getValueWithoutBlank(dto.getOwner());
			String productname= GeneralUtil.getValueWithoutBlank(dto.getProductname());
			String name=GeneralUtil.getValueWithoutBlank(dto.getName()); 
			String remark= GeneralUtil.getValueWithoutBlank(dto.getRemark()); 
			String color=dto.getColor();
			String addressid= GeneralUtil.getValueWithoutBlank(dto.getAddressid());
			String materialid=GeneralUtil.getValueWithoutBlank(dto.getMaterialid());
			if (StrUtil.isBlankOrUndefined(search)) {
				search = null;
			} else {
				search = "%" + search.trim()+ "%";
			}
 
			String[] list = null;
			if (StrUtil.isNotEmpty(searchlist)) {
				list = searchlist.split(",");
			}
			List<String> skulist = new ArrayList<String>();
			if (list != null) {
				for (int i = 0; i < list.length; i++) {
					if (StrUtil.isNotEmpty(list[i])) {
						skulist.add("%" + list[i] + "%");
					}
				}
			} else {
				skulist = null;
			}
		 
			if ( !StrUtil.isBlankOrUndefined(issfg) && issfg.equals("true")) {
				issfg = "2";
			} else if (StrUtil.isBlankOrUndefined(issfg) || issfg.equals("false")) {
				issfg = null;
			}
			List<Integer> issfglist = new ArrayList<Integer>();
			if (issfg != null) {
				String[] issfgarray = issfg.split(",");
				for (int i = 0; i < issfgarray.length; i++) {
					if (StrUtil.isNotEmpty(issfgarray[i])) {
						issfglist.add(Integer.parseInt(issfgarray[i]));
					}
				}
			} else {
				issfglist = null;
			}
	
			 
			if (StrUtil.isBlankOrUndefined(name)) {
				name = null;
			} else {
				name = "%" + name + "%";
			}
			if (StrUtil.isBlankOrUndefined(productname)) {
				name = null;
			} else {
				name = "%" + productname + "%";
			}
			
			if (StrUtil.isBlankOrUndefined(remark)) {
				remark = null;
			} else {
				remark = "%" + remark + "%";
			}
		 
			if (StrUtil.isBlankOrUndefined(color) || "all".equals(color)) {
				color = null;
			} else {
				color = "%" + color.trim() + "%";
			}
			if (userinfo.isLimit(UserLimitDataType.owner)) {
				owner=userinfo.getId();
    		}
			map.put("shopid", shopid);
			map.put("search", search);
			map.put("searchtype", dto.getSearchtype());
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
			map.put("isplan", dto.getIsplan()==null?false:dto.getIsplan());
			if(dto.getIswarning()!=null&&dto.getIswarning()==true) {
				map.put("iswarning", "true");
			}
			map.put("addressid", addressid);
			map.put("materialid", materialid);
			map.put("midlist", dto.getMaterialList()!=null&&dto.getMaterialList().size()>0?dto.getMaterialList():null);
	        IPage<Map<String, Object>> result = iMaterialService.findConsumableByCondition(dto.getPage(),map);
	        return Result.success(result);
	    }
	    @PostMapping("/packageList")
	    public Result<IPage<Map<String, Object>>> packageList(@RequestBody MaterialDTO dto) {
	     	Map<String, Object> map = new HashMap<String, Object>();
	    	UserInfo userinfo = UserInfoContext.get();
	    	String ftype= GeneralUtil.getValueWithoutBlank( dto.getFtype());
			String shopid = userinfo.getCompanyid();
			String search=dto.getSearch();
			String searchlist=dto.getSearchlist();
			String warehouseid = GeneralUtil.getValueWithoutBlank(dto.getWarehouseid());
			String isDelete = GeneralUtil.getValueWithoutBlank(dto.getIsDelete());
			String categoryid = GeneralUtil.getValueWithoutBlank(dto.getCategoryid());
			String supplier = GeneralUtil.getValueWithoutBlank(dto.getSupplier());
			String owner = GeneralUtil.getValueWithoutBlank(dto.getOwner());
			String name=GeneralUtil.getValueWithoutBlank(dto.getName()); 
			String remark= GeneralUtil.getValueWithoutBlank(dto.getRemark()); 
			String addressid= GeneralUtil.getValueWithoutBlank(dto.getAddressid());
			String materialid=GeneralUtil.getValueWithoutBlank(dto.getMaterialid());
			if (StrUtil.isBlankOrUndefined(search)) {
				search = null;
			} else {
				search = "%" + search.trim()+ "%";
			}
 
			String[] list = null;
			if (StrUtil.isNotEmpty(searchlist)) {
				list = searchlist.split(",");
			}
			List<String> skulist = new ArrayList<String>();
			if (list != null) {
				for (int i = 0; i < list.length; i++) {
					if (StrUtil.isNotEmpty(list[i])) {
						skulist.add("%" + list[i] + "%");
					}
				}
			} else {
				skulist = null;
			}
			if (StrUtil.isBlankOrUndefined(name)) {
				name = null;
			} else {
				name = "%" + name.trim() + "%";
			}
			 
			if (StrUtil.isBlankOrUndefined(remark)) {
				remark = null;
			} else {
				remark = "%" + remark.trim() + "%";
			}
		 
			if (userinfo.isLimit(UserLimitDataType.owner)) {
				owner=userinfo.getId();
    		}
			map.put("shopid", shopid);
			map.put("search", search);
			map.put("searchtype", dto.getSearchtype());
			map.put("searchsku", skulist);
			map.put("supplierid", supplier);
			map.put("categoryid", categoryid);
			map.put("name", name);
			map.put("remark", remark);
			map.put("owner", owner);
			map.put("warehouseid", warehouseid);
			map.put("ftype", ftype);
			map.put("isDelete", isDelete);
			map.put("addressid", addressid);
			map.put("materialid", materialid);
			map.put("midlist", dto.getMaterialList()!=null&&dto.getMaterialList().size()>0?dto.getMaterialList():null);
	        IPage<Map<String, Object>> result = iMaterialService.findPackageByCondition(dto.getPage(),map);
	        return Result.success(result);
	    }
	    
	    @PostMapping("/packageListAll")
	    public Result<List<Map<String,Object>>> packageListAll(@RequestBody MaterialDTO dto){
	     	Map<String, Object> map = new HashMap<String, Object>();
	    	UserInfo userinfo = UserInfoContext.get();
	    	map.put("shopid", userinfo.getCompanyid());
	    	String warehouseid = GeneralUtil.getValueWithoutBlank(dto.getWarehouseid());
	    	map.put("warehouseid", warehouseid);
	        List<Map<String, Object>> result = iMaterialService.findPackageByCondition(map);
	        if(StrUtil.isNotBlank(dto.getSearch())) {
	        	LambdaQueryWrapper<OutWarehouseForm> query=new LambdaQueryWrapper<OutWarehouseForm>();
	    		query.eq(OutWarehouseForm::getShopid,  userinfo.getCompanyid());
	    		query.eq(OutWarehouseForm::getNumber, dto.getSearch());
	    		OutWarehouseForm form=iOutWarehouseFormService.getOne(query);
	    		Map<String,Integer> entryMap=new HashMap<String,Integer>();
	    		if(form!=null) {
	    			LambdaQueryWrapper<OutWarehouseFormEntry> queryEntry=new LambdaQueryWrapper<OutWarehouseFormEntry>();
	    			queryEntry.eq(OutWarehouseFormEntry::getFormid, form.getId());
	    			List<OutWarehouseFormEntry> list = iOutWarehouseFormEntryService.list(queryEntry);
	    			for(OutWarehouseFormEntry item:list) {
	    				entryMap.put(item.getMaterialid(), item.getAmount());
	    			}
	    			for(Map<String, Object> item:result) {
	    				item.put("out", entryMap.get(item.get("id").toString()));
	    			}
	    		}
	        }
	        return Result.success(result);
	    }
	    
	    
	    @ApiOperation("根据本地产品ID查询产品详情")
	    @GetMapping("/getMaterialInfo")
		public Result<MaterialInfoVO> getMaterialInfoAction(@ApiParam("产品ID")@RequestParam String id) {
	    	MaterialInfoVO vo=new MaterialInfoVO();
	    	MaterialVO data = iMaterialService.findMaterialById(id);
			if (data == null) {
				return Result.success(vo);
			}
			UserInfo userinfo = UserInfoContext.get();
			String shopid=userinfo.getCompanyid();
			DimensionsInfo itemDim =null;
			DimensionsInfo pkgDim =null;
			DimensionsInfo boxDim =null;
			if(data.getItemdimensions()!=null) {
				itemDim = dimensionsInfoService.getById(data.getItemdimensions());
			}
			if(data.getPkgdimensions()!=null) {
				pkgDim = dimensionsInfoService.getById(data.getPkgdimensions());
			}
			if(data.getBoxdimensions()!=null) {
				boxDim = dimensionsInfoService.getById(data.getBoxdimensions());
			}
			List<StepWisePrice> priceList = stepWisePriceService.selectByMaterial(data.getId());
			List<AssemblyVO> assemblyList = assemblyService.selectByMainmid(data.getId());
			List<MaterialConsumableVO> consumableList =  iMaterialConsumableService.selectConsumableByMainmid(data.getId(),shopid);
			List<MaterialConsumableVO> consumablePList=  iMaterialConsumableService.selectConsumableBySubmid(data.getId(),null,shopid);
			List<MaterialSupplierVO> supplierList =iMaterialSupplierService.selectSupplierByMainmid(data.getId());
			List<MaterialCustoms> Customs=iMaterialService.selectCustomsByMaterialId(data.getId());
			List<Map<String, Object>> parentList = assemblyService.selectBySubid(data.getId(),shopid);
			if (data.getOperator() != null) {
				data.setOperator(adminClientOneFeign.getUserName(data.getOperator()));
			}
			if (data.getOperator()==null) {
				data.setOperator("--");
			}
			if (data.getCreator() != null) {
				data.setCreator(adminClientOneFeign.getUserName(data.getCreator()));
			}
			if (data.getCreator()==null) {
				data.setCreator("--");
			}
			if(assemblyList!=null&&assemblyList.size()>0) {
				for(AssemblyVO items:assemblyList) {
		              Map<String, Object> submap = inventoryService.findInvByMaterialId(items.getSubmid(), shopid);
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
			if(parentList!=null&&parentList.size()>0) {
				for(Map<String, Object> items:parentList) {
					String mainmid= items.get("mainmid").toString();
		              Map<String, Object> submap = inventoryService.findInvByMaterialId(mainmid, shopid);
		              if(submap!=null&&submap.size()>0) {
		            	  if(submap.get("inbound")!=null) {
		            		  items.put("inbound",Integer.parseInt(submap.get("inbound").toString()));
		            	  }else {
		            		  items.put("inbound",0);
		            	  }
		            	  if(submap.get("outbound")!=null) {
		            		  items.put("outbound",Integer.parseInt(submap.get("outbound").toString()));
		            	  }else {
		            		  items.put("outbound",0);
		            	  }
		            	  if(submap.get("fulfillable")!=null) {
		            		  items.put("fulfillable",Integer.parseInt(submap.get("fulfillable").toString()));
		            	  }else {
		            		  items.put("fulfillable",0);
		            	  }
		              }else {
		            	  items.put("inbound",0);
		            	  items.put("outbound",0);
		            	  items.put("fulfillable",0);
		              }
				}
			}
			String tagliststr="";
			Set<String> tagsIdsList=new HashSet<String>();
			List<String> taglist = iMaterialService.getTagsIdsListByMsku(data.getSku(), shopid);
			if(taglist!=null && taglist.size()>0) {
				for (int i = 0; i < taglist.size(); i++) {
					tagliststr+=(taglist.get(i).toString()+",");
					tagsIdsList.add(taglist.get(i).toString());
				}
			}
			try {
				Result<List<Map<String,Object>>> tagnamelistResult=adminClientOneFeign.findTagsNameByIds(tagsIdsList);
				List<Map<String,Object>> tagnamelist=tagnamelistResult.getData();
				if(tagnamelist!=null && tagnamelist.size()>0) {
					vo.setTagNameList(tagnamelist);
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			vo.setMaterial(data);
			vo.setAssemblyList(assemblyList);
			vo.setBoxDim(boxDim);
			vo.setConsumableList(consumableList);
			vo.setConsumablePList(consumablePList);
			vo.setCustoms(Customs);
			vo.setItemDim(itemDim);
			vo.setPkgDim(pkgDim);
			vo.setStepWisePrice(priceList);
			vo.setSupplierList(supplierList);
			vo.setTaglist(tagliststr);
			vo.setParentList(parentList);
			return Result.success(vo);
		}
	    
	    
	    @ApiOperation("根据本地产品SKU查询产品详情")
	    @PostMapping("/getMaterialBySKU/{shopid}")
		public Result<List<Map<String,Object>>> getMaterialBySKUAction(@PathVariable String shopid,@RequestBody List<String> skulist){
	    	try {
	    		List<Map<String,Object>> list=null;
		        list = iMaterialService.getDimensions(shopid,skulist);
				return Result.success(list);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	return Result.success();
	    }
	    
	    @ApiOperation("根据本地产品ID查询产品详情")
	    @GetMapping("/getMaterialInventoryInfo")
		public Result<MaterialInfoVO> getMaterialInfoAction(@ApiParam("本地SKU")@RequestParam String sku,@ApiParam("仓库ID")@RequestParam String warehouseid) {
	    	MaterialInfoVO vo=new MaterialInfoVO();
	    	UserInfo userinfo = UserInfoContext.get();
	    	if(StrUtil.isBlankOrUndefined(sku)) {
	    		throw new BizException("找不到SKU【"+sku+"】");
	    	}else {
	    		sku=sku.trim();
	    	}
	    	Material m = iMaterialService.selectBySKU(userinfo.getCompanyid(), sku);
	    	if(m==null) {
	    		throw new BizException("找不到SKU【"+sku+"】");
	    	}
	    	MaterialVO data = iMaterialService.findMaterialById(m.getId());
			if (data == null) {
				return null;
			}
			String shopid=userinfo.getCompanyid();
			DimensionsInfo itemDim =null;
			DimensionsInfo pkgDim =null;
			DimensionsInfo boxDim =null;
			if(data.getItemdimensions()!=null) {
				itemDim = dimensionsInfoService.getById(data.getItemdimensions());
			}
			if(data.getPkgdimensions()!=null) {
				pkgDim = dimensionsInfoService.getById(data.getPkgdimensions());
			}
			if(data.getBoxdimensions()!=null) {
				boxDim = dimensionsInfoService.getById(data.getBoxdimensions());
			}
			List<StepWisePrice> priceList = stepWisePriceService.selectByMaterial(data.getId());
			List<AssemblyVO> assemblyList = assemblyService.selectByMainmid(data.getId());
			List<MaterialConsumableVO> consumableList =  iMaterialConsumableService.selectConsumableByMainmid(data.getId(),shopid);
			List<MaterialSupplierVO> supplierList =iMaterialSupplierService.selectSupplierByMainmid(data.getId());
			List<MaterialCustoms> Customs=iMaterialService.selectCustomsByMaterialId(data.getId());
			if (data.getOperator() != null) {
				String name= adminClientOneFeign.getUserName(data.getOperator());
					if(name!=null) {
						data.setOperator(name);
					}else {
						data.setOperator("--");
					}
			}else{
				data.setOperator("--");
			}
			if (data.getCreator() != null) {
				String name= adminClientOneFeign.getUserName(data.getCreator());
					if(name!=null) {
						data.setOperator(name);
					}else {
						data.setOperator("--");
					}
			 
			}else{
				data.setCreator( "--");
			}
			if(assemblyList!=null&&assemblyList.size()>0) {
				for(AssemblyVO items:assemblyList) {
		              Map<String, Object> submap = inventoryService.findInvByMaterialId(items.getSubmid(), shopid);
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
			if(StrUtil.isNotBlank(warehouseid)) {
				data.setImage(fileUpload.getPictureImage(data.getImage()));
				Inventory inv = inventoryService.selectNowInv(warehouseid, m.getId(), userinfo.getCompanyid(), Status.fulfillable);
				vo.setFulfillable(inv);
				Integer count = assemblyService.findCanAssembly(m.getId(), warehouseid,shopid); 
				if(count==null) {
					count=0;
				}
				vo.setCanAssembly(count);
			}
			return Result.success(vo);
		}
	    
//	    @ApiOperation("上传产品图片")
//		@PostMapping(value="/uploadimg",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//		public Result<Map<String, Object>> uploadMaterialImgAction(
//				@ApiParam("MaterialID")@RequestParam String materialid,
//				@ApiParam("SKU")@RequestParam String sku,
//				@RequestParam("file")MultipartFile file
//				) throws FileNotFoundException {
//	    	Map<String, Object> maps=new HashMap<String, Object>();
//			UserInfo userinfo = UserInfoContext.get();
//			int result=0;
//			try {
//				result=iMaterialService.uploadMaterialImg(userinfo,materialid,file.getInputStream(),file.getOriginalFilename());
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			if (result > 0) {
//				maps.put("msg", "操作成功！");
//				maps.put("isSuccess", "true");
//			} else {
//				maps.put("msg", "操作失败！");
//				maps.put("isSuccess", "fail");
//			}
//			return Result.success(maps);
//		} 
	    
	    //读取编码
	    @ApiOperation("根据类型获取各种编码")
	    @GetMapping("/getSerialNumber")
	    public Result<String> getSerialNumber(String ftype,String isfind){
	    	UserInfo userinfo = UserInfoContext.get();
	    	String number="";
	    	try {
	    		if("true".equals(isfind)){
	    			number=serialNumService.findSerialNumber(userinfo.getCompanyid(), ftype);
	    		}else {
	    			number=serialNumService.readSerialNumber(userinfo.getCompanyid(), ftype);
	    		}
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	return Result.success(number);
	    }
	    
	    @ApiOperation("获取assemblyCan信息")
	    @GetMapping("/getAssemblyCan")
	    public Result<Integer> getAssemblyCanAction(@RequestParam String materialid,@RequestParam String warehouseid,@RequestParam String shopid){
	    	Integer count = assemblyService.findCanAssembly(materialid, warehouseid,shopid); 
	    	if(count==null) {
	    		count=0;
	    	}
	    	return Result.success(count);
	    }
	    
	    @ApiOperation("获取category信息")
	    @GetMapping("/getCategory")
	    public Result<List<MaterialCategory>> getCategoryCanAction(){
	    	UserInfo userinfo = UserInfoContext.get();
	    	List<MaterialCategory> list = iMaterialService.selectAllCateByShopid(userinfo.getCompanyid());
	    	return Result.success(list);
	    }
	    
	    @ApiOperation("获取material信息")
	    @GetMapping("/getMaterialByInfo")
	    public Result<List<Material>>getMaterialByInfoAction(String sku,String name){
	    	UserInfo userinfo = UserInfoContext.get();
	    	List<Material> list = iMaterialService.getMaterialByInfo(userinfo.getCompanyid(),sku,name);
	    	return Result.success(list);
	    }
	    
	    @ApiOperation("获取material信息")
	    @PostMapping("/getMaterialInfos")
		public Result<Map<String, Object>> findMaterialMapBySku(@RequestBody List<String> sku){
	    	UserInfo userinfo = UserInfoContext.get();
	    	Map<String,Object> param=new HashMap<String,Object>();
	    	String shopid=userinfo.getCompanyid();
	    	param.put("skulist", sku);
	    	param.put("shopid", shopid);
	    	if(sku!=null&&sku.size()>0) {
	    		Map<String, Object> result = iMaterialService.findMaterialMapBySku(param);
		    	return Result.success(result);
	    	}else {
	    		return Result.success(new HashMap<String, Object>());
	    	}
	    	
	    }
	    
	    
	    @ApiOperation("获取material信息")
	    @GetMapping("/findMarterialForColorOwner")
		public Result<List<String>> findMarterialForColorOwner( String shopid,  String owner,  String color){
	    	Map<String,Object> param=new HashMap<String,Object>();
	    	param.put("shopid", shopid);
	    	String key="colorown"+shopid;
	    	if(StrUtil.isEmpty(owner)) {
	    		param.put("owner", null);
	    	}else {
	    		param.put("owner", owner);
	    		 key=key+owner;
	    	}
	    	if(StrUtil.isEmpty(color)) {
	    		param.put("color", null);
	    	}else {
	    		param.put("color", color);
	    		 key=key+color;
	    	}
	        key=key+owner;
	    	List<String> result = iMaterialService.findMarterialForColorOwner(key,param);
	    	return Result.success(result);
	    }
	    
	    @PostMapping("/getMskuByTagList/{without}")
		public Result<List<String>> getMskuByTagList(@PathVariable String without,@RequestBody List<String> taglist){
	    	UserInfo userinfo = UserInfoContext.get();
	    	if(without!=null&&without.equals("true")) {
	    		return Result.success(iMaterialService.getmskuListWithout(userinfo.getCompanyid(),taglist));
	    	}else {
	    		return Result.success(iMaterialService.getmskuList(userinfo.getCompanyid(),taglist));
	    	}
			
	    }
	    @PostMapping("/getMaterialMap")
		public Result<Map<String,Object>> getMaterialInfoBySkuList(@RequestBody PlanDTO dto){
	    	return Result.success(iMaterialService.getMaterialInfoBySkuList(dto));
	    }
	 
	    @PostMapping("/getTagsIdsListByMsku/{shopid}")
		public Result<Map<String,String>> getTagsIdsListByMsku(@PathVariable String shopid,@RequestBody List<String> mskulist){
	    	return Result.success(iMaterialService.getTagsIdsListByMsku(shopid,mskulist));
	    }
	    
	    @PostMapping("/batchUpdatePrice")
		public Result<?> batchUpdatePriceAction(@RequestBody List<MaterialSupplierStepwise> prices){
	    	UserInfo user = UserInfoContext.get();
	    	iMaterialSupplierService.updatePriceOfDefaultSupplier(user,prices);
	    	return Result.success();
	    }
	    
	    @Transactional
	    @PostMapping("/batchUpdateCons")
		public Result<?> batchUpdateConsAction(@RequestBody Map<String,Object> dto){
	    	UserInfo user = UserInfoContext.get();
	    	iMaterialConsumableService.updateCons(user,dto);
	    	return Result.success();
	    }
	    
	    @Transactional
	    @PostMapping("/batchClearCons")
		public Result<?> batchClearConsAction(@RequestBody List<String> dto){
	    	UserInfo user = UserInfoContext.get();
	    	iMaterialConsumableService.clearCons(user,dto);
	    	return Result.success();
	    }
	    
	    
	    @GetMapping("/saveMaterialTags")
	    public Result<List<Map<String,Object>>> saveMaterialTagsAction(String mid,String ids) {
	    	UserInfo user = UserInfoContext.get();
	    	List<Map<String, Object>> result = iMaterialService.saveTagsByMid(mid, ids, user.getId());
	    	return Result.success(result);
	    }
	    
	    @PostMapping("/updateMaterial/{ftype}")
	    @Transactional
		public Result<?> updateMaterialAction(@PathVariable String ftype,@RequestBody List<MaterialVO> volist){
	    	UserInfo user = UserInfoContext.get();
	    	iMaterialService.updateMaterialType(user,ftype,volist);
	    	return Result.success();
	    }
	    
	    @GetMapping("/findMaterialTags")
	    public Result<String> findProductTagsAction(String mid) {
	    	String strs=iMaterialService.findMaterialTagsByMid(mid);
	    	return Result.success(strs);
	    }
	    
	    @GetMapping("/getWisePriceList")
	    public Result<List<StepWisePrice>> findWisePriceListAction(String mid) {
			if (StrUtil.isNotEmpty(mid)) {
				return Result.success(stepWisePriceService.selectByMaterial(mid));
			} else {
				return Result.success(null);
			}
		}
	    
	    @PostMapping(value="/saveData",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	    @Transactional
	    public Result<Map<String, Object>> saveAction(String infostr,@RequestParam(value="file",required=false)MultipartFile file){
	    	UserInfo userinfo = UserInfoContext.get();
	    	ObjectMapper mapper = new ObjectMapper();
	    	MaterialInfoVO vo=null;
			try {
				vo = mapper.readValue(infostr, MaterialInfoVO.class);
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		    String id=iMaterialService.saveAllInfo(vo,file,userinfo);
		    Map<String, Object> maps=new HashMap<String, Object>();
		    maps.put("id", id);
	    	return Result.success(maps);
	    }
	    
	    @ApiOperation("删除material")
	    @GetMapping("/deleteData")
	    public Result<String> deleteDataAction(String ids){
	    	UserInfo user = UserInfoContext.get();
    		String[] idlist = ids.split(",");
    		for (int i = 0; i < idlist.length; i++) {
    			String id = idlist[i];
    			if (StrUtil.isNotEmpty(id)) {
    				Material material = iMaterialService.getById(id);
    				String issfg = material.getIssfg();
    				if("2".equals(issfg)) {
    					// 如果是子产品并已被加入了主产品中，无法做删除操作
    					String materialId = material.getId();
    					QueryWrapper<Assembly> assqueryWrapper=new QueryWrapper<Assembly>();
    					assqueryWrapper.eq("submid", materialId);
    					List<Assembly> asslist = assemblyService.list(assqueryWrapper);
    					if(asslist!=null && asslist.size()>0) {
    						for(Assembly assemblymain:asslist) {
    							String mainid = assemblymain.getMainmid();
    							Material mainMaterial = iMaterialService.getById(mainid);
    							if(mainMaterial==null||mainMaterial.isDelete()==true) {
    								continue;
    							}
    							String mainSku=material.getSku()+"已存在于主SKU："+mainMaterial.getSku()+",无法归档!";
    							throw new BizException(mainSku);
    						}
    						
    					} 
    				}

    				// 如果ID存在，删除图片对应位置
    				QueryWrapper<PurchaseFormEntry> queryWrapper=new QueryWrapper<PurchaseFormEntry>();
    				queryWrapper.eq("materialid", id);
    				queryWrapper.between("auditstatus", 1, 2);
					List<PurchaseFormEntry> list = purchaseFormEntryService.list(queryWrapper);
    				if (list != null && list.size() > 0) {
    					throw new BizException("SKU:" + material.getSku() + " 存在采购订单无法删除！系统终止删除操作");
    				}
    				//如果是辅料 已经有了sku使用它  是不能删除的
    				if(material.getMtype()==1) {
    					LambdaQueryWrapper<MaterialConsumable> conqueryWrapper=new LambdaQueryWrapper<MaterialConsumable>();
    					conqueryWrapper.eq(MaterialConsumable::getSubmaterialid, id);
						List<MaterialConsumable> conlist = iMaterialConsumableService.list(conqueryWrapper);
						if(conlist!=null && conlist.size()>0) {
							throw new BizException("辅料:" + material.getSku() + " 正在被其它主SKU使用，无法删除！系统终止删除操作");
						}
    				}
    				iMaterialService.logicalDeleteMaterial(user, material);

					if("1".equals(issfg)) {
						// 如果是子产品并已被加入了主产品中，无法做删除操作
						String materialId = material.getId();
						QueryWrapper<Assembly> assqueryWrapper=new QueryWrapper<Assembly>();
						assqueryWrapper.eq("mainmid", materialId);
						List<Assembly> subasslist = assemblyService.list(assqueryWrapper);
						if(subasslist!=null && subasslist.size()>0) {
							for(Assembly subitem:subasslist) {
								QueryWrapper<Assembly> mainassqueryWrapper=new QueryWrapper<Assembly>();
								mainassqueryWrapper.eq("submid", subitem.getSubmid());
								List<Assembly> mainasslist = assemblyService.list(mainassqueryWrapper);
								Boolean issub=false;
								Material subMaterial = iMaterialService.getById(subitem.getSubmid());
								for(Assembly mianitem:mainasslist){
									String mainid = mianitem.getMainmid();
									Material mainMaterial = iMaterialService.getById(mainid);
									if(mainMaterial==null||mainMaterial.isDelete()==true) {
										continue;
									}else{
										issub=true;
									}
								}
								if(issub==false&&subMaterial.getIssfg().equals("2")){
									subMaterial.setIssfg("0");
									iMaterialService.updateById(subMaterial);
								}

							}

						}
					}
    			}


    		}

    		return Result.success("操作成功！");
	    }
	    
	    @ApiOperation("还原material")
	    @Transactional
	    @GetMapping("/recoverData")
	    public Result<String> recoverDataAction(String id,String sku){
	    	UserInfo user = UserInfoContext.get();
			if (StrUtil.isEmpty(sku)) {
				throw new BizException("该SKU " + sku + "：有误，不能还原回去！");
			}
			if (StrUtil.isEmpty(id)) {
				throw new BizException("该SKU有误，不能还原回去！");
			}
			iMaterialService.updateReductionSKUMaterial(user, id, sku);
			Material material = iMaterialService.getById(id);
			if(material.getIssfg().equals("1")){
				QueryWrapper<Assembly> assqueryWrapper=new QueryWrapper<Assembly>();
				assqueryWrapper.eq("mainmid", id);
				List<Assembly> subasslist = assemblyService.list(assqueryWrapper);
				if(subasslist!=null && subasslist.size()>0) {
					for(Assembly subitem:subasslist) {
						QueryWrapper<Assembly> mainassqueryWrapper=new QueryWrapper<Assembly>();
						mainassqueryWrapper.eq("submid", subitem.getSubmid());
						List<Assembly> mainasslist = assemblyService.list(mainassqueryWrapper);
						Boolean issub=false;
						Material subMaterial = iMaterialService.getById(subitem.getSubmid());
						for(Assembly mianitem:mainasslist){
							String mainid = subitem.getMainmid();
							Material mainMaterial = iMaterialService.getById(mainid);
							if(mainMaterial==null||mainMaterial.isDelete()==true) {
								continue;
							}else{
								issub=true;
							}
						}
						if(issub==true&&subMaterial.getIssfg().equals("0")){
							subMaterial.setIssfg("2");
							iMaterialService.updateById(subMaterial);
						}

					}

				}
			}

			return Result.success("SKU:"+sku+"还原成功!");
	    }
	    
	    @ApiOperation("获取产品供货周期和海外仓库存信息")
	    @GetMapping("/getMSkuDeliveryAndInv")
	    public Result<Material> getMSkuDeliveryAndInv(
	    		@RequestParam String shopid,
	    		@RequestParam String groupid,
	    		@RequestParam String msku,
	    		@RequestParam String country,
	    		@RequestParam String needDeliveryCycle){
	    	if(needDeliveryCycle!=null&&needDeliveryCycle.equals("true")) {
	    		Material  material = inventoryService.findOverseaById(msku, shopid, groupid,country); 
	    		return Result.success(material);
	    	}else {
	    		List<Warehouse> oversea = warehouseService.getOverseaWarehouse(shopid,"oversea_usable",groupid,country);
		    	if(oversea!=null&&oversea.size()>0) {
		    		Material  material = inventoryService.findOverseaById(msku, shopid, groupid,country); 
			    	return Result.success(material);
		    	}else {
		    		return Result.success();
		    	}
	    	}
	    	 
	    	
	    }
	 
	    @SuppressWarnings("unchecked")
		@PostMapping("/getMskuInventory")
		public Result<List<Map<String, Object>>> getMskuInventory(@RequestBody PlanDTO dto){
	    	    String key="";
			    UserInfo user=UserInfoContext.get();
	    	    if(dto.getPlanid()!=null) {
	    	    	key=key+dto.getPlanid();
	    	    }
	    	    if(dto.getGroupid()!=null) {
	    	    	key=key+dto.getGroupid();
	    	    }
	    	    if(dto.getWarehouseid()!=null) {
	    	    	key=key+dto.getWarehouseid();
	    	    }
	    	    if(dto.getName()!=null) {
	    	    	key=key+dto.getName();
	    	    }
	    	    if(dto.getRemark()!=null) {
	    	    	key=key+dto.getWarehouseid();
	    	    }
	    	    if(!StrUtil.isBlankOrUndefined(dto.getOwner())) {
	    	    	key=key+dto.getOwner();
	    		}
	    		if(!StrUtil.isBlankOrUndefined(dto.getCategoryid())) {
	    			key=key+dto.getCategoryid();
	    		}
	    		if(!StrUtil.isBlankOrUndefined(dto.getHasAddFee())) {
	    			key=key+dto.getHasAddFee();
	    		}
	    		if( dto.getSelected()!=null) {
	    			key=key+dto.getSelected();
	    		}
	    	    if(dto.getMskulist()!=null) {
	    	    	for(String item:dto.getMskulist()) {
	    	    		key=key+item;
	    	    	}
	    	    }
				if(StrUtil.isBlank(dto.getShopid())){
					dto.setShopid(user.getCompanyid());
				}
	    	    if(dto.getIscheck()!=null&&dto.getIscheck().equals("all")) {
	    	    	Map<String,Object> param=new HashMap<String,Object>();
	    	    	List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
	    	    	param.put("mskulist", dto.getMskulist());
	    	    	param.put("warehouseid", dto.getWarehouseid());
	    	    	param.put("shopid", dto.getShopid());
	    	    	List<Map<String,Object>> list=assemblyService.selectMainsDetailBySku(param);
	    	    	Map<String,Map<String,Object>> planmap=new HashMap<String,Map<String,Object>>();
	    	    	if(dto.getPlanitem()==null||dto.getPlanitem().size()==0) {
	    	    		return Result.success(result);
	    	    	}
	    	    	for(Map<String, Object> item:dto.getPlanitem()) {
	    	    		if(item.get("vmsku")!=null) {
	    	    			planmap.put(item.get("vmsku").toString(), item);
	    	    		}
	    	    	}
	    	    	Map<String,Map<String,Object>> submap=new HashMap<String,Map<String,Object>>();
	    	    	for(Map<String,Object> item:list) {
	    	    		String submid = item.get("submid").toString();
	    	    	    item.put("simage", fileUpload.getPictureImage(item.get("simage")));
					    item.put("mimage", fileUpload.getPictureImage(item.get("mimage")));
	    	    		Map<String,Object> sub=submap.get(submid);
	    	    		if(sub==null) {
	    	    			sub=new HashMap<String,Object>();
	    	    			sub.putAll(item); 
	    	    		}
						List<Map<String,Object>> mainlist=sub.get("mainlist")!=null?(List<Map<String,Object>>)sub.get("mainlist"):new ArrayList<Map<String,Object>>();
						Map<String,Object> mainitem=new HashMap<String,Object>();
						mainitem.putAll(item);
						mainlist.add(mainitem);
	    	    		sub.put("mainlist",mainlist);
	    	    		submap.put(submid, sub);
	    	    	}
	    	    	
	    	    	 for(Entry<String, Map<String, Object>> subentry:submap.entrySet()) {
	    	    		 Map<String, Object> sub = subentry.getValue();
	    	    		 Integer quantity=sub.get("squantity")!=null?Integer.parseInt(sub.get("squantity").toString()):0;
	    	    		 if(sub.get("mainlist")!=null) {
	    	    			 List<Map<String,Object>> mainlist=(List<Map<String,Object>>)sub.get("mainlist");
		    	    		 for(Map<String, Object> mainitem:mainlist) {
		    	    			 String msku=mainitem.get("msku").toString();
		    	    			 Integer mquantity=mainitem.get("mquantity")!=null?Integer.parseInt(mainitem.get("mquantity").toString()):0;
		    	    			 Integer subnumber=mainitem.get("subnumber")!=null?Integer.parseInt(mainitem.get("subnumber").toString()):0;
		    	    			 Map<String,Object> plan= planmap.get(msku);
		    	    			 if(plan!=null) {
		    	    				 Integer amount=plan.get("amount")!=null?Integer.parseInt(plan.get("amount").toString()):0;
		    	    				 amount=amount-mquantity;
		    	    				 amount=amount>0?amount:0;
		    	    				 quantity=quantity-amount*subnumber;
		    	    			 }
		    	    			 mainitem.put("plan",plan);
		    	    		 }
	    	    		 }
	    	    		if(quantity<0) {
	    	    			sub.put("needqty",quantity*-1);
	    	    			result.add(sub);
	    	    		}
	    	    	 }		
	    	    	return Result.success(result);
	    	    }else {
	    	    	return Result.success(iMaterialService.findInventoryByMsku(dto,key));
	    	    }
	    }
	    
	    @PostMapping(value = "/uploadBaseInfoFile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
		public Result<String> uploadExcelAction(@RequestParam("file")MultipartFile file,@RequestParam("mtype")String mtype)  {
		       UserInfo user=UserInfoContext.get();
				if (file != null) {
					try {
						InputStream inputStream = file.getInputStream();
						Map<String, String> ownermap = adminClientOneFeign.findOwnerAllAction();
						Workbook workbook = WorkbookFactory.create(inputStream);
						Sheet sheet = workbook.getSheetAt(0);
						List<String> list=new LinkedList<String>();
						list.add("SKU");
						list.add("名称");
						list.add("采购成本");
						list.add("带包装");
						list.add("");
						list.add("");
						list.add("");
						list.add("净产品");
						list.add("");
						list.add("");
						list.add("");
						list.add("箱规");
						list.add("");
						list.add("");
						list.add("");
						list.add("单箱数量");
						list.add("品牌名");
						list.add("其它费用");
						list.add("分类");
						list.add("规格");
						list.add("备注");
						list.add("生效日期");
						list.add("负责人");
						if(sheet.getLastRowNum()>0){
							Row row = sheet.getRow(0);
							for (int i = 0; i <=row.getLastCellNum()&&i<list.size() ; i++) {
								Cell info = row.getCell(i);
								if(info==null||info.getStringCellValue()==null||!info.getStringCellValue().equals(list.get(i))){
									throw new BizException("当前导入内容与模板不符");
								}
							}
						}
						for (int i = 2; i <= sheet.getLastRowNum(); i++) {
							Row info=sheet.getRow(i);
							if(info==null || info.getCell(0)==null) {
								continue;
							}
							excelDownLoadService.uploadMaterialBaseInfoFile(user, info,mtype,ownermap);
						}
						workbook.close();
						return Result.success();
					} catch (IOException e) {
						e.printStackTrace();
						return Result.failed();
					} catch (EncryptedDocumentException e) {
						e.printStackTrace();
					} catch (InvalidFormatException e) {
						e.printStackTrace();
					}
				}
			return Result.success("ok");
		}
	    
	    
	    @PostMapping(value = "/uploadSupplierFile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
		public Result<String> uploadSupplierFileAction(@RequestParam("file")MultipartFile file)  {
		       UserInfo user=UserInfoContext.get();
				if (file != null) {
					try {
						InputStream inputStream = file.getInputStream();
						Workbook workbook = WorkbookFactory.create(inputStream);
						Sheet sheet = workbook.getSheetAt(0);
						List<String> list=new LinkedList<String>();
						list.add("SKU");
						list.add("默认供应商名称");
						list.add("采购代码");
						list.add("采购链接");
						list.add("其它成本");
						list.add("供货周期");
						list.add("不良率");
						list.add("起订量");
						list.add("采购价列表");
						if(sheet.getLastRowNum()>0){
							Row row = sheet.getRow(0);
							for (int i = 0; i <=row.getLastCellNum()&&i<list.size() ; i++) {
								Cell info = row.getCell(i);
                                if(info==null||info.getStringCellValue()==null||!info.getStringCellValue().equals(list.get(i))){
									throw new BizException("当前导入内容与模板不符");
								}
							}
						}
						for (int i = 1; i <= sheet.getLastRowNum(); i++) {
							Row info=sheet.getRow(i);
							if(info==null || info.getCell(0)==null) {
								continue;
							}
							excelDownLoadService.uploadMaterialSupplierFile(user, info);
						}
						workbook.close();
						return Result.success();
					} catch (IOException e) {
						e.printStackTrace();
						return Result.failed();
					} catch (EncryptedDocumentException e) {
						e.printStackTrace();
					} catch (InvalidFormatException e) {
						e.printStackTrace();
					}
				}
			return Result.success("ok");
		}

	@PostMapping(value = "/uploadMoreSupplierFile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Result<String> uploadMoreSupplierFileAction(@RequestParam("file")MultipartFile file)  {
		UserInfo user=UserInfoContext.get();
		if (file != null) {
			try {
				InputStream inputStream = file.getInputStream();
				Workbook workbook = WorkbookFactory.create(inputStream);
				Sheet sheet = workbook.getSheetAt(0);
				List<String> list=new LinkedList<String>();
				list.add("SKU");
				list.add("备选供应商名称");
				list.add("采购代码");
				list.add("采购链接");
				list.add("其它成本");
				list.add("供货周期");
				list.add("不良率");
				list.add("起订量");
				list.add("采购价列表");
				list.add("联系人");
				list.add("联系电话");
				list.add("地址");
				if(sheet.getLastRowNum()>0){
					Row row = sheet.getRow(0);
					for (int i = 0; i <=row.getLastCellNum()&&i<list.size() ; i++) {
						Cell info = row.getCell(i);
						if(info==null||info.getStringCellValue()==null||!info.getStringCellValue().equals(list.get(i))){
							throw new BizException("当前导入内容与模板不符");
						}
					}
				}
				for (int i = 1; i <= sheet.getLastRowNum(); i++) {
					Row info=sheet.getRow(i);
					if(info==null || info.getCell(0)==null) {
						continue;
					}
					excelDownLoadService.uploadMaterialMoreSupplierFile(user, info);
				}
				workbook.close();
				return Result.success();
			} catch (IOException e) {
				e.printStackTrace();
				return Result.failed();
			} catch (EncryptedDocumentException e) {
				e.printStackTrace();
			} catch (InvalidFormatException e) {
				e.printStackTrace();
			}
		}
		return Result.success("ok");
	}
	    
	    @PostMapping(value = "/uploadConsumableFile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
		public Result<String> uploadConsumableFileAction(@RequestParam("file")MultipartFile file, HttpServletResponse response)  {
		       UserInfo user=UserInfoContext.get();
				if (file != null) {
					InputStream inputStream = null;
					Workbook workbook =null;
					Sheet sheet =null;
				    try {
						inputStream = file.getInputStream();
						workbook = WorkbookFactory.create(inputStream);
						sheet = workbook.getSheetAt(0);
						List<String> listtitle=new LinkedList<String>();
						listtitle.add("主SKU");
						listtitle.add("耗材SKU");
						listtitle.add("1个主SKU对应耗材数");
						if(sheet.getLastRowNum()>0){
							Row row = sheet.getRow(0);
							for (int i = 0; i <=row.getLastCellNum()&&i<listtitle.size() ; i++) {
								Cell info = row.getCell(i);
								if(info==null||info.getStringCellValue()==null||!info.getStringCellValue().equals(listtitle.get(i))){
									throw new BizException("当前导入内容与模板不符");
								}
							}
						}
						Map<String,List<MaterialConsumableVO>> consumbable=new HashMap<String,List<MaterialConsumableVO>>();
						boolean haserror=false;
						for (int i = 1; i <= sheet.getLastRowNum(); i++) {
							Row info=sheet.getRow(i);
							if(info==null || info.getCell(0)==null) {
								continue;
							}
							MaterialConsumableVO entity = excelDownLoadService.uploadMaterialConsumableFile(user, info);
							if(entity==null) {
			 					 Cell cell = info.createCell(3);
			 					 cell.setCellValue("无法对应SKU");
			 					 haserror=true;
			 				}
							else if(entity.getAmount()==null||entity.getAmount().floatValue()<0.000001) {
			 					 Cell cell = info.createCell(3);
			 					 cell.setCellValue("辅料数量必须大于0！");
			 					 haserror=true;
			 				}
						    if(entity!=null) {
							  List<MaterialConsumableVO> list = consumbable.get(entity.getMaterialid());
								if(list==null) {
									list=new LinkedList<MaterialConsumableVO>();
								}
								list.add(entity);
								consumbable.put(entity.getMaterialid(),list);
						  }
						  
						}
						
						if(haserror==false) {
							for(Entry<String, List<MaterialConsumableVO>> entry:consumbable.entrySet()) {
								this.iMaterialService.saveMaterialConsumable(entry.getValue(), user, entry.getKey());
							}
						}else {
							ServletOutputStream fOut = null;
							try {
								response.setContentType("application/force-download");// 设置强制下载不打开
								response.addHeader("Content-Disposition", "attachment;fileName=error.xlsx");// 设置文件名
								fOut = response.getOutputStream();
								workbook.write(fOut);
							} catch (Exception e2) {
								e2.printStackTrace();
							}finally {
								try {
									if(fOut != null) {
										fOut.flush();
										fOut.close();
									}
									if(workbook != null) {
										workbook.close();
									}
								} catch (IOException e3) {
									e3.printStackTrace();
								}
							}
						}
						workbook.close();
						return Result.success();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						    throw new BizException("读取文件异常");
						} catch (EncryptedDocumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvalidFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							 throw new BizException("文件格式异常");
						}
				}
			return Result.success("ok");
		}
	    
	    @PostMapping(value = "/uploadCustomsFile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
		public Result<String> uploadCustomsFileAction(@RequestParam("file")MultipartFile file)  {
		       UserInfo user=UserInfoContext.get();
				if (file != null) {
					try {
						InputStream inputStream = file.getInputStream();
						Workbook workbook = WorkbookFactory.create(inputStream);
						Sheet sheet = workbook.getSheetAt(0);
						sheet = workbook.getSheetAt(0);
						List<String> listtitle=new LinkedList<String>();
						listtitle.add("SKU");
						listtitle.add("国家编码");
						listtitle.add("中文名");
						listtitle.add("英文名");
						listtitle.add("材质（英文）");
						listtitle.add("材质（中文）");
						listtitle.add("用途");
						listtitle.add("申报单价");
						listtitle.add("海关编码");
						listtitle.add("税率");
						listtitle.add("产品链接");
						if(sheet.getLastRowNum()>0){
							Row row = sheet.getRow(0);
							for (int i = 0; i <=row.getLastCellNum()&&i<listtitle.size() ; i++) {
								Cell info = row.getCell(i);
								if(info==null||info.getStringCellValue()==null||!info.getStringCellValue().equals(listtitle.get(i))){
									throw new BizException("当前导入内容与模板不符");
								}
							}
						}
						for (int i = 1; i <= sheet.getLastRowNum(); i++) {
							Row info=sheet.getRow(i);
							if(info==null || info.getCell(0)==null) {
								continue;
							}
							excelDownLoadService.uploadMaterialCustomsFile(user, info);
						}
						workbook.close();
						return Result.success();
					} catch (IOException e) {
						e.printStackTrace();
						return Result.failed();
					} catch (EncryptedDocumentException e) {
						e.printStackTrace();
					} catch (InvalidFormatException e) {
						e.printStackTrace();
					}
				}
			return Result.success("ok");
		}
	    
	    @PostMapping(value = "/uploadAssemblyFile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
		public Result<String> uploadAssemblyFileAction(@RequestParam("file")MultipartFile file)  {
		       UserInfo user=UserInfoContext.get();
				if (file != null) {
					try {
						InputStream inputStream = file.getInputStream();
						Workbook workbook = WorkbookFactory.create(inputStream);
						Sheet sheet = workbook.getSheetAt(0);
						List<String> listtitle=new LinkedList<String>();
						listtitle.add("主SKU");
						listtitle.add("组装关系列表");
						if(sheet.getLastRowNum()>0){
							Row row = sheet.getRow(0);
							for (int i = 0; i <=row.getLastCellNum()&&i<listtitle.size() ; i++) {
								Cell info = row.getCell(i);
								if(info==null||info.getStringCellValue()==null||!info.getStringCellValue().equals(listtitle.get(i))){
									throw new BizException("当前导入内容与模板不符");
								}
							}
						}
						for (int i = 1; i <= sheet.getLastRowNum(); i++) {
							Row info=sheet.getRow(i);
							if(info==null || info.getCell(0)==null) {
								continue;
							}
							excelDownLoadService.uploadMaterialAssemblyFile(user, info);
						}
						workbook.close();
						return Result.success();
					} catch (IOException e) {
						e.printStackTrace();
						return Result.failed();
					} catch (EncryptedDocumentException e) {
						e.printStackTrace();
					} catch (InvalidFormatException e) {
						e.printStackTrace();
					}
				}
			return Result.success("ok");
		}
	    
	    
	    @GetMapping("/copyImageForProduct")
		public Result<List<Map<String, Object>>> copyImageForProductAction(String sku,String materialid,String image) {
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			UserInfo user=UserInfoContext.get();
			String[] skuStr = null;
			String[] materialidStr = null;
			String[] imageStr = null;
			if(StrUtil.isNotEmpty(sku)) {
				skuStr = sku.split("%,#");
			}
			if(StrUtil.isNotEmpty(materialid)) {
				materialidStr = materialid.split("%,#");
			}
			if(StrUtil.isNotEmpty(image)) {
				imageStr = image.split("%,#");
			}
			for(int i = 0; i < skuStr.length; i++) {
				Map<String, Object> maps = new HashMap<String, Object>();
				maps.put("sku", skuStr[i]);
				maps.put("materialid", materialidStr[i]);
				maps.put("image", imageStr[i]);
				list.add(maps);
			}
			return Result.success(iMaterialService.copyImageForProduct(list, user));
		}
		
	    @GetMapping("/copyDimsForProduct")
		public Result<List<Map<String, Object>>> copyDimsForProductAction(String sku,String materialid,String dims) {
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			UserInfo user=UserInfoContext.get();
			String[] skuStr = null;
			String[] materialidStr = null;
			String[] dimStr = null;
			if(StrUtil.isNotEmpty(sku)) {
				skuStr = sku.split("%,#");
			}
			if(StrUtil.isNotEmpty(materialid)) {
				materialidStr = materialid.split("%,#");
			}
			if(StrUtil.isNotEmpty(dims)) {
				dimStr = dims.split("%,#");
			}
			for(int i = 0; i < skuStr.length; i++) {
				Map<String, Object> maps = new HashMap<String, Object>();
				maps.put("sku", skuStr[i]);
				maps.put("materialid", materialidStr[i]);
				maps.put("dims", dimStr[i]);
				list.add(maps);
			}
			return Result.success(iMaterialService.copyDimsForProduct(list, user));
		}
	    
	    //下载各个模板的数据 导出记录行
	    @SuppressWarnings("resource")
		@PostMapping("/downExcelRecords")
		public void downExcelTempAction(@RequestBody MaterialDTO dto, HttpServletResponse response) {
			Workbook workbook = null;
			UserInfo userinfo = UserInfoContext.get();
			ServletOutputStream fOut = null;
			try {
				response.setContentType("application/force-download");// 设置强制下载不打开
				response.addHeader("Content-Disposition", "attachment;fileName=" + dto.getDowntype() +"Records.xlsx");// 设置文件名
				fOut = response.getOutputStream();
				ClassPathResource cpr = new ClassPathResource("template/"+dto.getDowntype() +".xlsx");
				if(cpr!=null&&cpr.exists()) {
					// 创建新的Excel工作薄
					InputStream is =cpr.getInputStream();
					if(is!=null) {
						workbook = WorkbookFactory.create(is);
					}
				}
				//插入记录条
				workbook=iMaterialService.setMaterialExcelBook(workbook,dto,userinfo);
				workbook.write(fOut);
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				try {
					if(fOut != null) {
						fOut.flush();
						fOut.close();
					}
					if(workbook != null) {
						workbook.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	    
	    
		@GetMapping("/getOwnerList")
		public Result<List<Map<String,Object>>> getOwnerListAction() {
			UserInfo user = UserInfoContext.get();
			String shopid = user.getCompanyid();
			List<Map<String,Object>> list=iMaterialService.getOwnerList(shopid);
			return Result.success(list);
		}
		  
		@GetMapping("/getCustoms")
		public Result<MaterialCustoms> getCustomsAction(String msku,String country) {
			UserInfo user = UserInfoContext.get();
			String shopid = user.getCompanyid();
			return Result.success(iMaterialService.getCustoms(shopid,msku,country));
		}
		 
		@PostMapping("/saveCustoms")
		public Result<?> saveCustomsAction(@RequestBody List<MaterialCustoms> list) {
			UserInfo user = UserInfoContext.get();
			iMaterialService.saveCustoms(user,list);
			return Result.success();
		}
}
