package com.wimoor.erp.material.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.FileUpload;
import com.wimoor.common.pojo.entity.Picture;
import com.wimoor.common.service.IPictureService;
import com.wimoor.common.service.impl.PictureServiceImpl;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.assembly.pojo.entity.Assembly;
import com.wimoor.erp.assembly.service.IAssemblyService;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.common.pojo.entity.EnumByInventory;
import com.wimoor.erp.common.service.IZipRarUploadService;
import com.wimoor.erp.config.IniConfig;
import com.wimoor.erp.customer.pojo.entity.Customer;
import com.wimoor.erp.customer.service.ICustomerService;
import com.wimoor.erp.inventory.mapper.InventoryMapper;
import com.wimoor.erp.inventory.mapper.OutWarehouseFormEntryMapper;
import com.wimoor.erp.inventory.mapper.OutWarehouseFormMapper;
import com.wimoor.erp.inventory.pojo.entity.InventoryParameter;
import com.wimoor.erp.inventory.pojo.entity.OutWarehouseForm;
import com.wimoor.erp.inventory.pojo.entity.OutWarehouseFormEntry;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.inventory.service.IStockCycleService;
import com.wimoor.erp.material.mapper.ERPMaterialHistoryMapper;
import com.wimoor.erp.material.mapper.MaterialCategoryMapper;
import com.wimoor.erp.material.mapper.MaterialConsumableMapper;
import com.wimoor.erp.material.mapper.MaterialCustomsFileMapper;
import com.wimoor.erp.material.mapper.MaterialCustomsMapper;
import com.wimoor.erp.material.mapper.MaterialMapper;
import com.wimoor.erp.material.mapper.MaterialSupplierMapper;
import com.wimoor.erp.material.mapper.MaterialSupplierStepwiseMapper;
import com.wimoor.erp.material.pojo.entity.DimensionsInfo;
import com.wimoor.erp.material.pojo.entity.ERPMaterialHistory;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.pojo.entity.MaterialCategory;
import com.wimoor.erp.material.pojo.entity.MaterialConsumable;
import com.wimoor.erp.material.pojo.entity.MaterialCustoms;
import com.wimoor.erp.material.pojo.entity.MaterialMark;
import com.wimoor.erp.material.pojo.entity.MaterialSupplier;
import com.wimoor.erp.material.pojo.entity.MaterialSupplierStepwise;
import com.wimoor.erp.material.pojo.entity.StepWisePrice;
import com.wimoor.erp.material.pojo.vo.MaterialConsumableVO;
import com.wimoor.erp.material.pojo.vo.MaterialSupplierVO;
import com.wimoor.erp.material.pojo.vo.MaterialVO;
import com.wimoor.erp.material.service.IDimensionsInfoService;
import com.wimoor.erp.material.service.IMaterialCategoryService;
import com.wimoor.erp.material.service.IMaterialMarkService;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.material.service.IStepWisePriceService;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlanItem;
import com.wimoor.erp.purchase.service.IPurchasePlanItemService;
import com.wimoor.erp.util.UUIDUtil;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
 
 

@Service("materialService")
@RequiredArgsConstructor
public class MaterialServiceImpl extends  ServiceImpl<MaterialMapper,Material> implements IMaterialService {
	 
	final MaterialMapper materialMapper;
	 
	final ERPMaterialHistoryMapper erpMaterialHistoryMapper;
	 
	final IMaterialMarkService materialMarkService;
	 
	final MaterialCategoryMapper materialCategoryMapper;
	 
	final IAssemblyService assemblyService;
	 
	final IStockCycleService stockCycleService;
	 
	final IStepWisePriceService stepWisePriceService;
	 
	final IPictureService pictureService;
	 
	final IDimensionsInfoService dimensionsInfoService;
	 
	final IMaterialCategoryService materialCategoryService;
	 
	final IPurchasePlanItemService purchasePlanItemService;
	 
	final IZipRarUploadService zipRarUploadService;
	 
	final ICustomerService customerService;
	 
	final MaterialSupplierStepwiseMapper materialSupplierStepwiseMapper;
	 
	final MaterialSupplierMapper materialSupplierMapper;
	 
	final MaterialConsumableMapper materialConsumableMapper;
	 
	final IWarehouseService warehouseService;
	 
	final OutWarehouseFormMapper outWarehouseFormMapper;
	 
	final OutWarehouseFormEntryMapper outWarehouseFormEntryMapper;
	 
	final InventoryMapper inventoryMapper;
	
	final FileUpload fileUpload;
	
	@Lazy
	@Autowired
	IInventoryService inventoryService;
	 
	final MaterialCustomsMapper materialCustomsMapper;
	 
	final MaterialCustomsFileMapper materialCustomsFileMapper;
	public MaterialVO findMaterialById(String id) {
		return materialMapper.findMaterialById(id);
	}

	public IPage<Map<String, Object>> findByCondition(Page<?> page,Map<String, Object> params) {
	    IPage<Map<String, Object>> list = materialMapper.findMaterial(page,params);
	    if(list!=null && list.getRecords().size()>0) {
	    	 for(int i=0;i<list.getRecords().size();i++) {
	    		 List<Map<String, Object>> historylist = this.selectProPriceHisById(list.getRecords().get(i).get("id").toString());
	    		 String pricestr="";
	    		 if(historylist!=null && historylist.size()>0) {
	    			 for(int j=0;j<historylist.size();j++) {
	    				 Map<String,Object> history = historylist.get(j);
	    				 if(history!=null) {
	    					 if(history.get("price")!=null) {
	    						 String price=history.get("price").toString();
								 int len = price.indexOf(".");
								 price=price.substring(0, len+3);
								 pricestr+="历史价格("+
					    				   GeneralUtil.formatDate((Date)history.get("opttime"))+
					    				   "): "+price+"<br/>";
							 }
	    				 }
	    			 }
	    			 if(GeneralUtil.isEmpty(pricestr)) {
	    				 pricestr="暂无价格历史!";
	    			 }
	    		 }else {
	    			 pricestr="暂无价格历史!";
	    		 }
	    		 list.getRecords().get(i).put("pricestr", pricestr);
	    	 }
	    }
		return list;
	}

	public List<Map<String, Object>> findAllByCondition(Map<String, Object> map) {
		return  materialMapper.findAllByCondition(map);
	}
	
	public boolean saveMark(String materialid, String type, String content,String userid) throws ERPBizException {
		MaterialMark materialmark = new MaterialMark();
		materialmark.setFtype(type);
		materialmark.setMaterialid(materialid);
		materialmark.setMark(content);
		materialmark.setOpttime(new Date());
		materialmark.setOperator(userid);
		QueryWrapper<MaterialMark> queryWrapper=new QueryWrapper<MaterialMark>();
		queryWrapper.eq("materialid",materialid);
		queryWrapper.eq("ftype",type);
		MaterialMark old = materialMarkService.getOne(queryWrapper);
		if (old != null) {
			return materialMarkService.updateById(materialmark);
		} else {
			return materialMarkService.save(materialmark);
		}
	}

	public String getNotice(String id) {
		QueryWrapper<MaterialMark> queryWrapper=new QueryWrapper<MaterialMark>();
		queryWrapper.eq("materialid",id);
		queryWrapper.eq("ftype","notice");
		MaterialMark old = materialMarkService.getOne(queryWrapper);
		return old.getMark();
	}

	public List<Material> selectAllSKUForSelect(String sku, String shopid) {
		return materialMapper.selectAllSKUForSelect(sku, shopid);
	}

	public List<Map<String, Object>> selectAllSKUForLabel(String sku, String shopid) {
		return materialMapper.selectAllSKUForLabel(sku, shopid);
	}

	public List<MaterialCategory> selectAllCateByShopid(String shopid) {
		QueryWrapper<MaterialCategory> queryWrapper=new QueryWrapper<MaterialCategory>();
		queryWrapper.eq("shopid", shopid);
		queryWrapper.orderByAsc("name");
		List<MaterialCategory> list = materialCategoryMapper.selectList(queryWrapper);
		return list;
	}

	@Override
	public boolean delete(String id) {
		Material material = this.getById(id);
		// 如果ID存在，删除图片对应位置
		Picture oldpicture = pictureService.getById(material.getImage());
		if (oldpicture != null && GeneralUtil.isNotEmpty(oldpicture.getLocation())) {
			pictureService.removeById(material.getImage());
		}
		dimensionsInfoService.removeById(material.getItemdimensions());
		dimensionsInfoService.removeById(material.getPkgdimensions());
		dimensionsInfoService.removeById(material.getBoxdimensions());
		stepWisePriceService.deleteByMaterial(id.toString());
		assemblyService.deleteByMainmid(id.toString());
		stockCycleService.deleteByMaterial(id.toString());
		return this.removeById(id);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> saveAllInfo(HttpServletRequest request, Model model) throws ERPBizException {
		UserInfo user = UserInfoContext.get();
		String shopid =user.getCompanyid();
		String issfg = request.getParameter("issfg");
		Map<String, Object> msgmap = new HashMap<String, Object>();
		String msg = "";
		if (user == null) {
			msg = "当前用户为空，请先登录！";
			msgmap.put("msg", msg);
			return msgmap;
		}
		Material material = new Material();
		ERPMaterialHistory materialhistory = new ERPMaterialHistory();
		material.setShopid(shopid);
		material.setOperator(user.getId());
		materialhistory.setShopid(shopid);
		materialhistory.setOperator(user.getId());
		// 获取物料基本信息
		Material oldmater = null;
		String iscopystr = request.getParameter("iscopy");
		boolean iscopy = false;
		if (GeneralUtil.isNotEmpty(iscopystr) && iscopystr.equals("true")) {
			iscopy = true;
		}
		String id = request.getParameter("id");
		if (GeneralUtil.isNotEmpty(id)) {
			oldmater = this.getById(id);
			if (oldmater != null) {
				String oldIssfg = oldmater.getIssfg();
				if (!oldIssfg.equals(issfg)) {
					QueryWrapper<PurchasePlanItem> queryWrapper=new QueryWrapper<PurchasePlanItem>();
					queryWrapper.eq("materialid", id);
					queryWrapper.eq("status", 1);
					queryWrapper.eq("shopid", shopid);
					List<PurchasePlanItem> purchaseList = purchasePlanItemService.list(queryWrapper);
					if (purchaseList != null && purchaseList.size() > 0) {
						throw new ERPBizException("已加入补货计划，请移除后再修改产品组装类别！");
					}
				}
			}
			if (!iscopy) {
				material.setId(id);
				materialhistory.setId(id);
			}else {
				BigInteger idshort = UUIDUtil.getBigIntUUIDshort();
				id=idshort.toString();
				material.setId(id);
			}
		}else {
			BigInteger idshort = UUIDUtil.getBigIntUUIDshort();
			id=idshort.toString();
			material.setId(id);
		} 
		String sku = request.getParameter("sku");
		String upc = request.getParameter("upc");
		String name = request.getParameter("name");
		String vatRate=request.getParameter("vatrate");
		String isSmlAndLight_ = request.getParameter("isSmlAndLight");
		boolean isSmlAndLight = false;
		if (isSmlAndLight_ != null && "true".equals(isSmlAndLight_)) {
			isSmlAndLight = true;
		}
		String brand = request.getParameter("brand");
		String categoryid = request.getParameter("categoryid");
		String specification = request.getParameter("specification");
		String remark = request.getParameter("remark");
		String image = request.getParameter("image");
		String color = request.getParameter("color");
		String owner = request.getParameter("owner");
		String effectivedate = request.getParameter("effectivedate");
		String boxnum = request.getParameter("boxnum");
		if (remark == null) {
			remark = "";
		}
		if (GeneralUtil.isNotEmpty(image) && !image.contains("images/addimg.png") && !image.contains(IniConfig.photoServerUrl())) {//如果是图片流文件，则需要上传图片
			String filePath = PictureServiceImpl.materialImgPath + shopid + "/";
			Picture picture = null;
			try {
				String oldpictureid = null;
				if (!iscopy && oldmater != null) {
					oldpictureid = oldmater.getImage();
				}
				picture = pictureService.uploadPicture(image, filePath, oldpictureid);
				if (picture != null) {
					material.setImage(picture.getId());
					materialhistory.setImage(picture.getId());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if(iscopy && image.contains(IniConfig.photoServerUrl())){//如果是复制新增，且image链接含有photo.wimoor.com
			//https://photo.wimoor.com/materialImg/26138972975530085/1589272652752_after.jpeg?version=1589272966000
			Picture picture = new Picture();
			picture.setLocation("photo"+image.substring(image.indexOf(IniConfig.photoServerUrl())+24, image.indexOf("?version")));
			material.setImage(picture.getId());
			pictureService.save(picture);
			materialhistory.setImage(picture.getId());
		} else {
			if (oldmater != null) {
				material.setImage(oldmater.getImage());
				materialhistory.setImage(oldmater.getImage());
			}
		}
		if(GeneralUtil.isNotEmpty(boxnum)){
			material.setBoxnum(Integer.parseInt(boxnum));
			materialhistory.setBoxnum(Integer.parseInt(boxnum));
		}else {
			material.setBoxnum(0);
			materialhistory.setBoxnum(0);
		}
		if(GeneralUtil.isEmpty(vatRate)) {
			material.setVatrate(null);
		}else {
			material.setVatrate(Float.parseFloat(vatRate));	
		}
		if(GeneralUtil.isNotEmpty(categoryid)) {
			material.setCategoryid(categoryid);
			materialhistory.setCategoryid(categoryid);
		}
		material.setOwner(owner);
		material.setSku(sku);
		material.setUpc(upc);
		material.setName(name);
		material.setSmlAndLight(isSmlAndLight);
		material.setBrand(brand);
		material.setSpecification(specification);
		material.setIssfg(issfg);
		material.setRemark(remark);
		material.setColor(color);
		material.setEffectivedate(GeneralUtil.getDatez(effectivedate));
		material.setOpttime(new Date());
		materialhistory.setOwner(owner);
		materialhistory.setSku(sku);
		materialhistory.setUpc(upc);
		materialhistory.setName(name);
 
		materialhistory.setBrand(brand);
		materialhistory.setSpecification(specification);
		materialhistory.setIssfg(issfg);
		materialhistory.setRemark(remark);
		materialhistory.setColor(color);
		materialhistory.setEffectivedate(GeneralUtil.getDatez(effectivedate));
		materialhistory.setOpttime(new Date());
		
		// 净尺寸
		String length = request.getParameter("length");
		String width = request.getParameter("width");
		String height = request.getParameter("height");
		String weight = request.getParameter("weight");
		if (GeneralUtil.isNotEmpty(length) && GeneralUtil.isNotEmpty(width) && GeneralUtil.isNotEmpty(height) && GeneralUtil.isNotEmpty(weight)) {
			DimensionsInfo itemdim = new DimensionsInfo();
			itemdim.setLength(new BigDecimal(length));
			itemdim.setWidth(new BigDecimal(width));
			itemdim.setHeight(new BigDecimal(height));
			itemdim.setWeight(new BigDecimal(weight));
			if (!iscopy && oldmater != null && oldmater.getItemdimensions() != null) {
				itemdim.setId(oldmater.getItemdimensions());
				dimensionsInfoService.updateById(itemdim);
				material.setItemdimensions(oldmater.getItemdimensions());
				materialhistory.setItemdimensions(oldmater.getItemdimensions());
			} else {
				material.setItemdimensions(itemdim.getId());
				materialhistory.setItemdimensions(itemdim.getId());
				dimensionsInfoService.save(itemdim);
			}
		}
		// 带包装尺寸
		String pkglength = request.getParameter("pkglength");
		String pkgwidth = request.getParameter("pkgwidth");
		String pkgheight = request.getParameter("pkgheight");
		String pkgweight = request.getParameter("pkgweight");
		if (GeneralUtil.isNotEmpty(pkglength) && GeneralUtil.isNotEmpty(pkgwidth) && GeneralUtil.isNotEmpty(pkgheight) && GeneralUtil.isNotEmpty(pkgweight)) {
			DimensionsInfo pkgdim = new DimensionsInfo();
			pkgdim.setLength(new BigDecimal(pkglength));
			pkgdim.setWidth(new BigDecimal(pkgwidth));
			pkgdim.setHeight(new BigDecimal(pkgheight));
			pkgdim.setWeight(new BigDecimal(pkgweight));
			if (!iscopy && oldmater != null && oldmater.getPkgdimensions() != null) {
				if (dimensionsInfoService.getById(oldmater.getPkgdimensions()) != null) {
					pkgdim.setId(oldmater.getPkgdimensions());
					dimensionsInfoService.updateById(pkgdim);
					material.setPkgdimensions(oldmater.getPkgdimensions());
					materialhistory.setPkgdimensions(oldmater.getPkgdimensions());
				} else {
					material.setPkgdimensions(pkgdim.getId());
					materialhistory.setPkgdimensions(pkgdim.getId());
					dimensionsInfoService.save(pkgdim);
				}
			} else {
				material.setPkgdimensions(pkgdim.getId());
				materialhistory.setPkgdimensions(pkgdim.getId());
				dimensionsInfoService.save(pkgdim);
			}
		} else {
			throw new ERPBizException("带包装尺寸不能为空且不能为0");
		}
		// 箱子尺寸
		String boxlength = request.getParameter("boxlength");
		String boxwidth = request.getParameter("boxwidth");
		String boxheight = request.getParameter("boxheight");
		String boxweight = request.getParameter("boxweight");
		if (GeneralUtil.isNotEmpty(boxlength) && GeneralUtil.isNotEmpty(boxwidth) && GeneralUtil.isNotEmpty(boxheight) && GeneralUtil.isNotEmpty(boxweight)) {
			DimensionsInfo itemdim = new DimensionsInfo();
			itemdim.setLength(new BigDecimal(boxlength));
			itemdim.setWidth(new BigDecimal(boxwidth));
			itemdim.setHeight(new BigDecimal(boxheight));
			itemdim.setWeight(new BigDecimal(boxweight));
			if (!iscopy && oldmater != null && oldmater.getBoxdimensions() != null) {
				itemdim.setId(oldmater.getBoxdimensions());
				dimensionsInfoService.updateById(itemdim);
				material.setBoxdimensions(oldmater.getBoxdimensions());
				materialhistory.setBoxdimensions(oldmater.getBoxdimensions());
			} else {
				material.setBoxdimensions(itemdim.getId());
				materialhistory.setBoxdimensions(itemdim.getId());
				dimensionsInfoService.save(itemdim);
			}
		}
		
		//海关信息
		String customscode=request.getParameter("customscode");//   海关编码
		String engname=request.getParameter("engname");//   产品英文名
		String chnname=request.getParameter("chnname");//   产品中文名
		String stuff=request.getParameter("stuff");//   产品材质
		String materialmodel=request.getParameter("materialmodel");//   产品型号
		String materialuse=request.getParameter("materialuse");//   产品用途
		String materialbrand=request.getParameter("materialbrand");//   产品品牌
		String iselectricity=request.getParameter("iselectricity");//   是否带电/磁
		String isdangerstr=request.getParameter("isdanger");//   是否危险品
		String unitpricestr=request.getParameter("unitprice");
		String materialaddfee=request.getParameter("materialaddfee");//附加费用
		boolean isele=false;
		boolean isdanger=false;
		BigDecimal unitprice=null;
		BigDecimal addfee=null;
		if (GeneralUtil.isNotEmpty(iselectricity) && iselectricity.equals("1")) {
			isele = true;
		}
		if (GeneralUtil.isNotEmpty(isdangerstr) && isdangerstr.equals("1")) {
			isdanger = true;
		}
		if (GeneralUtil.isNotEmpty(unitpricestr)) {
			unitprice = new BigDecimal(unitpricestr);
		}
		if (GeneralUtil.isNotEmpty(materialaddfee)) {
			addfee = new BigDecimal(materialaddfee);
		}
		MaterialCustoms customs=new MaterialCustoms();
		customs.setMatreialid(id);
		customs.setBrand(materialbrand);
		customs.setCustomsCode(customscode);
		customs.setIselectricity(isele);
		customs.setMaterial(stuff);
		customs.setModel(materialmodel);
		customs.setNameCn(chnname);
		customs.setNameEn(engname);
		customs.setMaterialUse(materialuse);
		customs.setIsdanger(isdanger);
		customs.setUnitprice(unitprice);
		customs.setAddfee(addfee);
		//先删除再加
		materialCustomsMapper.deleteById(id);
		materialCustomsMapper.insert(customs);
		 

		if (request.getParameter("asscycle") != null && !"".equals(request.getParameter("asscycle"))) {
			int assemblyTime = Integer.parseInt(request.getParameter("asscycle").toString());
			material.setAssemblyTime(assemblyTime);
			materialhistory.setAssemblyTime(assemblyTime);
		} else {
			int assemblyTime = 0;
			material.setAssemblyTime(assemblyTime);
			materialhistory.setAssemblyTime(assemblyTime);
		}

		String assemblyMapList = request.getParameter("assemblyMapList");
 
		assemblyService.deleteByMainmid(material.getId());
		if ("1".equals(issfg) && GeneralUtil.isNotEmpty(assemblyMapList)) {
			String[] assemblyArr = assemblyMapList.split("},");
			for (int i = 0; i < assemblyArr.length; i++) {
				Assembly assembly = new Assembly();
				Map<String, Object> map = new HashMap<String, Object>();
				if (i == assemblyArr.length - 1) {
					map = (Map<String, Object>) JSON.parse(assemblyArr[i]);
				} else {
					map = (Map<String, Object>) JSON.parse(assemblyArr[i] + "}");
				}
				assembly.setMainmid(material.getId());
				assembly.setSubmid(map.get("sku").toString());
				assembly.setSubnumber(map.get("amount").toString());
				assembly.setRemark(map.get("remark").toString());
				assembly.setOperator(user.getId());
				assemblyService.save(assembly);
			}
		} 
		String supplierListpara = request.getParameter("supplierList");
		saveOrUpdateSupplier(supplierListpara,user,id,material);
		String consumableList=request.getParameter("consumableList");
		saveMaterialConsumable(consumableList,user,id);
		int result = 0;
		int result_his = 0;
		if (!iscopy && oldmater != null) {
			result = this.baseMapper.updateById(material);
			materialhistory.setCreator(oldmater.getCreator());
			materialhistory.setCreatedate(oldmater.getCreatedate());
			result_his = erpMaterialHistoryMapper.insert((materialhistory));
			msg = "更新";
		} else {
			if(saveNowMaterial(material)) {
			    result++;
			}
			msg = "添加";
		}
		if (result > 0 || result_his > 0) {
			msg += "成功！";
			msgmap.put("material", material);
		} else {
			msg += "失败！";
		}
		msgmap.put("msg", msg);
		return msgmap;
	}
	
	//供应商列表
	public void saveOrUpdateSupplier(String supplierListpara,UserInfo user,String id,Material material) {
		//一进来先删除当前的列表 以最新的supplierlist为准
 
		QueryWrapper<MaterialSupplier> queryWrapper=new QueryWrapper<MaterialSupplier>();
		queryWrapper.eq("materialid",id);
		materialSupplierMapper.delete(queryWrapper);
	 
		QueryWrapper<MaterialSupplierStepwise> queryWrapper2=new QueryWrapper<MaterialSupplierStepwise>();
		queryWrapper2.eq("materialid",id);
		materialSupplierStepwiseMapper.delete(queryWrapper2);
		stepWisePriceService.deleteByMaterial(id);
		supplierListpara="["+supplierListpara+"]";
		JSONArray jsonArray = GeneralUtil.getJsonArray(supplierListpara);
		//List<Map<String, Object>> supplierList = GeneralUtil.jsonStringToMapList(supplierListpara);
		if(jsonArray!=null && jsonArray.size()>0) {
			for(int i=0;i<jsonArray.size();i++) {
				JSONObject item = jsonArray.getJSONObject(i);
				MaterialSupplier materialSupplier=new MaterialSupplier();
				String supid = item.getString("supplierId");
				boolean isdefault=item.getBooleanValue("isdefault");
				String procode = item.getString("productCode");
				Integer deliverycycle=null;
				if(item.get("deliveryCycle")==null || ("").equals(item.get("deliveryCycle"))) {
					deliverycycle=null;
				}else {
					deliverycycle=item.getInteger("deliveryCycle");
				}
				BigDecimal costother= item.getBigDecimal("otherCost");
				String purchaseurl = item.getString("purchaseUrl");
				Float badRate= item.getFloat("badrate");
				int moq=item.getInteger("moq");
				JSONArray priceArray = item.getJSONArray("priceMapList");
				BigDecimal maxprice = new BigDecimal("0");
				if(priceArray!=null && priceArray.size()>0) {
					for(int j=0;j<priceArray.size();j++) {
						JSONObject step = priceArray.getJSONObject(j);
						int amount= step.getInteger("amount");
						BigDecimal price=step.getBigDecimal("price");
						if (price.toString().split("\\.").length > 1 && price.toString().split("\\.")[1].length() > 2) {
							throw new ERPBizException("阶梯采购采购价不能超过两位小数！");
						}
						if (maxprice.compareTo(price) < 0) {
							maxprice = price;
						}
						MaterialSupplierStepwise stepwise=new MaterialSupplierStepwise();
						stepwise.setAmount(amount);
						stepwise.setMaterialid(id);
						stepwise.setOperator(user.getId());
						stepwise.setOpttime(new Date());
						stepwise.setPrice(price);
						stepwise.setSupplierid(supid);
						materialSupplierStepwiseMapper.insert(stepwise);
					}
				}
				if(isdefault==true) {
					material.setBadrate(badRate);
					material.setSupplier(supid);
					material.setOtherCost(costother);
					material.setPrice(maxprice);
					material.setMOQ(moq);
					material.setDeliveryCycle(deliverycycle);
					material.setProductCode(procode);
					material.setPurchaseUrl(purchaseurl);
					
					if(priceArray!=null && priceArray.size()>0) {
						for(int j=0;j<priceArray.size();j++) {
							JSONObject step = priceArray.getJSONObject(j);
							int amount= step.getInteger("amount");
							BigDecimal price=step.getBigDecimal("price");
							if (price.toString().split("\\.").length > 1 && price.toString().split("\\.")[1].length() > 2) {
								throw new ERPBizException("阶梯采购采购价不能超过两位小数！");
							}
							StepWisePrice stepWisePrice = new StepWisePrice();
							stepWisePrice.setAmount(amount);
							stepWisePrice.setPrice(price);
							stepWisePrice.setMaterial(id);
							stepWisePrice.setOperator(user.getId());
							stepWisePriceService.save(stepWisePrice);
						}
					}
					
				}
				materialSupplier.setCreatedate(new Date());
				materialSupplier.setCreater(user.getId());
				materialSupplier.setIsdefault(isdefault);
				materialSupplier.setMaterialid(id);
				materialSupplier.setSupplierid(supid);
				materialSupplier.setOperator(user.getId());
				materialSupplier.setOpttime(new Date());
				materialSupplier.setOthercost(costother);
				materialSupplier.setProductcode(procode);
				materialSupplier.setPurchaseurl(purchaseurl);
				materialSupplier.setDeliverycycle(deliverycycle);
				materialSupplier.setBadrate(badRate);
				materialSupplier.setMOQ(moq);
				materialSupplierMapper.insert(materialSupplier);
			}
			
		}
	}
	
	//耗材列表
	public void saveMaterialConsumable(String ConsumableListpara,UserInfo user,String id) {
		//一进来先删除当前的列表 以最新的ConsumableList为准
		QueryWrapper<MaterialConsumable> queryWrapper=new QueryWrapper<MaterialConsumable>();
		queryWrapper.eq("materialid",id);
		materialConsumableMapper.delete(queryWrapper);
		ConsumableListpara="["+ConsumableListpara+"]";
		JSONArray jsonArray = GeneralUtil.getJsonArray(ConsumableListpara);
		if(jsonArray!=null && jsonArray.size()>0) {
			for(int i=0;i<jsonArray.size();i++) {
				JSONObject item = jsonArray.getJSONObject(i);
				MaterialConsumable cons=new MaterialConsumable();
				cons.setAmount(item.getBigDecimal("amount"));
				cons.setMaterialid(id);
				cons.setOperator(user.getId());
				cons.setOpttime(new Date());
				cons.setSubmaterialid(item.getString("subid"));
				materialConsumableMapper.insert(cons);
			}
		}
	}

	public boolean saveNowMaterial(Material material) throws  ERPBizException {
		QueryWrapper<Material> queryWrapper=new QueryWrapper<Material>();
		queryWrapper.eq("shopid", material.getShopid());
		queryWrapper.eq("sku", material.getSku());
		List<Material> list = materialMapper.selectList(queryWrapper);
		if (list.size() > 0) {
			Material dbMaterial = list.get(0);
			if(dbMaterial.isDelete()) {
				throw new ERPBizException("产品SKU："+ dbMaterial.getSku() +"已经归档，请从归档状态还原后使用!");
			}else {
				throw new ERPBizException("该SKU已经存在！");
			}
		} else {
			material.setCreator(material.getOperator());
			material.setCreatedate(new Date());
			return this.save(material);
		}
	}

	public List<Map<String, Object>> findMaterialSizeByCondition(Map<String, Object> param) {
		if (param.get("marketplaceid") != null) {
			return materialMapper.selectMaterialSize(param);
		} else {
			return null;
		}
	}

	public Map<String, Object> findDimAndAsinBymid(String sku, String shopid, String marketplaceid, String groupid) {
		return materialMapper.findDimAndAsinBymid(sku, shopid, marketplaceid, groupid);
	}

	public List<Map<String, Object>> getForSum(String shopid,String groupid) {
		return materialMapper.getForSum(shopid,groupid);
	}

	public Material findBySKU(String sku, String shopid) {
		QueryWrapper<Material> queryWrapper=new QueryWrapper<Material>();
		queryWrapper.eq("sku", sku);
		queryWrapper.eq("shopid", shopid);
		queryWrapper.eq("isDelete", false);
		List<Material> materiallist = this.list(queryWrapper);
		if (materiallist.size() > 0) {
			return materiallist.get(0);
		}
		return null;
	}

	public String getImage(Material material) {
		Picture picture;
		if (material.getImage() != null) {
			picture = pictureService.getById(material.getImage());
			if (picture != null) {
				return picture.getLocation();
			}
		}
		return "images/systempicture/noimage40.png";
	}

	public String getImageByMaterialid(String id) {
		Material material = this.getById(id);
		if (material.getImage() != null) {
			Picture picture = pictureService.getById(material.getImage());
			if (picture != null) {
				return picture.getLocation();
			}
		}
		return "images/systempicture/noimage40.png";
	}

	public Map<String, BigDecimal> findDimensionsInfoBySKU(String sku, String shopid) {
		return materialMapper.findDimensionsInfoBySKU(sku, shopid);
	}

	public List<Map<String, Object>> getOwnerList(String shopid) {
		return materialMapper.getOwnerList(shopid);
	}

	@Cacheable(value = "materialListCache")
	public Map<String, Object> findMaterialMapBySku(String sku, String shopid) {
		return materialMapper.findMaterialMapBySku(sku, shopid);
	}

	@Cacheable(value = "materialListCache",key="#key")
	public List<String> findMarterialForColorOwner(String key,Map<String, Object> param) {
		return materialMapper.findMarterialForColorOwner(param);
	}

	@CacheEvict(value = "materialListCache", allEntries = true)
	public void logicalDeleteMaterial(UserInfo user, Material material) {
		if (material.isDelete()) {
			throw new ERPBizException(material.getSku() + "已经归档！请勿重复操作！");
		}
		material.setDelete(true);
		material.setOpttime(new Date());
		material.setOperator(user.getId());
		this.updateById(material);
	}

	@CacheEvict(value = "materialListCache", allEntries = true)
	public boolean updateReductionSKUMaterial(UserInfo user, String id, String sku) {
		QueryWrapper<Material> queryWrapper=new QueryWrapper<Material>();
		queryWrapper.eq("sku", sku);
		queryWrapper.eq("shopid", user.getCompanyid());
		queryWrapper.eq("isDelete", false);
		List<Material> materiallist = this.list(queryWrapper);
		if (materiallist != null && materiallist.size() > 0) {
			throw new ERPBizException("该SKU已经存在，不能还原回去！");
		} else {
			Material material = new Material();
			material.setSku(sku);
			material.setShopid(user.getCompanyid());
			material.setId(id);
			material.setOpttime(new Date());
			material.setOperator(user.getId());
			material.setDelete(false);
			return this.updateById(material);
		}
	}

	public boolean updateCycle(UserInfo user, String id, int amount) {
		Material materal = this.getById(id);
		if (materal != null) {
			materal.setDeliveryCycle(amount);
			return this.updateById(materal);
		} else {
			throw new ERPBizException("该产品不存在！");
		}
	}

	@SuppressWarnings("unchecked")
	public int updateItemMaterialByPrice(String[] ids, String priceMapList, UserInfo user) {
		int result = 0;
		for (int k = 0; k < ids.length; k++) {
			String materialid = ids[k];
			Material material = materialMapper.selectById(materialid);
			BigDecimal maxprice = new BigDecimal("0");
			if (GeneralUtil.isNotEmpty(priceMapList) && material != null) {
				String[] priceIdArr = priceMapList.split("},");
				List<StepWisePrice> oldPriceList = stepWisePriceService.selectByMaterial(material.getId());
				if (oldPriceList != null && oldPriceList.size() > 0) {
					stepWisePriceService.deleteByMaterial(material.getId());
				}
				for (int i = 0; i < priceIdArr.length; i++) {
					StepWisePrice stepWisePrice = new StepWisePrice();
					Map<String, Object> map = new HashMap<String, Object>();
					if (i == priceIdArr.length - 1) {
						map = (Map<String, Object>) JSON.parse(priceIdArr[i]);
					} else {
						map = (Map<String, Object>) JSON.parse(priceIdArr[i] + "}");
					}
					BigDecimal price = new BigDecimal(map.get("price").toString());
					if (maxprice.compareTo(price) < 0) {
						maxprice = price;
					}
					stepWisePrice.setAmount(Integer.parseInt(map.get("amount").toString()));
					stepWisePrice.setPrice(price);
					if (price.toString().split("\\.").length > 1 && price.toString().split("\\.")[1].length() > 2) {
						throw new ERPBizException("阶梯采购采购价不能超过两位小数！");
					}
					stepWisePrice.setMaterial(material.getId());
					stepWisePrice.setOperator(user.getId());
					stepWisePriceService.save(stepWisePrice);
				}
			}
			if (material != null) {
				material.setPrice(maxprice);
				material.setOpttime(new Date());
				material.setOperator(user.getId());
				if(this.updateById(material)) {
					result++ ;
				}
			}
		}
		return result;
	}

	public int updateItemMaterialByType(String[] ids, String ftype, String value, UserInfo user) {
		int result = 0;
		for (int k = 0; k < ids.length; k++) {
			String materialid = ids[k];
			Material material = materialMapper.selectById(materialid);
			if (material != null && GeneralUtil.isNotEmpty(value)) {
				if (ftype.equals("date")) {
					material.setEffectivedate(GeneralUtil.getDatez(value));
				} else if (ftype.equals("cycle")) {
					material.setDeliveryCycle(Integer.parseInt(value));
				} else if (ftype.equals("owner")) {
					material.setOwner(value);
				} else if (ftype.equals("supplier")) {
					material.setSupplier(value);
				} else if (ftype.equals("remark")) {
					material.setRemark(value);
				}
				material.setOpttime(new Date());
				material.setOperator(user.getId());
				if(this.updateById(material)) {
					result++ ;
				}
			} else {
				result += 0;
			}
		}
		return result;
	}

	public List<Map<String, Object>> findSKUImageForProduct(Map<String, Object> param) {
		return materialMapper.findSKUImageForProduct(param);
	}

	public List<Map<String, Object>> copyImageForProduct(List<Map<String, Object>> list, UserInfo user) {
		List<Map<String, Object>> errorList = new ArrayList<Map<String,Object>>();
	 	for(Map<String, Object> map : list) {
			//https://photo.wimoor.com/productImg/26138972975530085/A1F83G8C2ARO7P/51PglvullIL._SL75_.jpg
			String image = map.get("image").toString();
			String materialid = map.get("materialid").toString();
			Material material = materialMapper.selectById(materialid);
			String newPath =PictureServiceImpl.materialImgPath + user.getCompanyid() + "/";
				Picture picture=null;
				try {
					picture = pictureService.downloadPicture(image, newPath, material.getImage());
				} catch (Exception e) {
					e.printStackTrace();
				}
				material.setImage(picture.getId());
				materialMapper.updateById(material);
		 
		} 
		return errorList;
	}
	
	public List<Map<String, Object>> copyDimsForProduct(List<Map<String, Object>> list, UserInfo user) {
		List<Map<String, Object>> errorList = new ArrayList<Map<String,Object>>();
	 	for(Map<String, Object> map : list) {
			String dimsid = map.get("dims").toString();
			String materialid = map.get("materialid").toString();
			Material material = this.getById(materialid);
			DimensionsInfo dimension = dimensionsInfoService.getById(dimsid);
			if(material != null && dimension!=null) {
				String odldimid = material.getPkgdimensions();
				DimensionsInfo olddim = dimensionsInfoService.getById(odldimid);
				String lenunits=dimension.getLengthUnits();
				BigDecimal length = dimension.getLength();
				String widthunits=dimension.getWidthUnits();
				BigDecimal width = dimension.getWidth();
				String heightunits=dimension.getHeightUnits();
				BigDecimal height = dimension.getHeight();
				String weightunits=dimension.getWeightUnits();
				BigDecimal weight = dimension.getWeight();
				if(lenunits!=null) {
					if("inches".equals(lenunits)) {
						length=length.multiply(new BigDecimal("2.54")).setScale(2, BigDecimal.ROUND_DOWN);
						lenunits=null;
					}
				}
				if(widthunits!=null) {
					if("inches".equals(widthunits)) {
						width=width.multiply(new BigDecimal("2.54")).setScale(2, BigDecimal.ROUND_DOWN);
						widthunits=null;
					}
				}
				if(heightunits!=null) {
					if("inches".equals(heightunits)) {
						height=height.multiply(new BigDecimal("2.54")).setScale(2, BigDecimal.ROUND_DOWN);
						heightunits=null;
					}
				}
				if(weightunits!=null) {
					if("pounds".equals(weightunits)) {
						weight=weight.multiply(new BigDecimal("0.453")).setScale(2, BigDecimal.ROUND_DOWN);
						weightunits=null;
					}
				}
				if(olddim!=null) {
					//修改
					olddim.setLength(length);
					olddim.setLengthUnits(lenunits);
					olddim.setWidth(width);
					olddim.setWidthUnits(widthunits);
					olddim.setHeight(height);
					olddim.setHeightUnits(heightunits);
					olddim.setWeight(weight);
					olddim.setWeightUnits(weightunits);
					dimensionsInfoService.updateById(olddim);
				}else {
					//新增
					DimensionsInfo entity=new DimensionsInfo();
					entity.setLength(length);
					entity.setLengthUnits(lenunits);
					entity.setWidth(width);
					entity.setWidthUnits(widthunits);
					entity.setHeight(height);
					entity.setHeightUnits(heightunits);
					entity.setWeight(weight);
					entity.setWeightUnits(weightunits);
					dimensionsInfoService.save(entity);
				}
			}else {
				errorList.add(map);
			}
	 	}
		return errorList;
	}
	
	public List<Map<String, Object>> selectAllMaterialByShop(Map<String, Object> parammap) {
		List<Map<String, Object>> list = materialMapper.selectAllMaterialByShop(parammap);
		if(list!=null && list.size()>0) {
			for(int i=0;i<list.size();i++) {
				Map<String, Object> map = list.get(i);
				String materialid = map.get("id").toString();
				List<StepWisePrice> steplist = stepWisePriceService.selectByMaterial(materialid);
				if(steplist!=null && steplist.size()>0) {
					String stepPrice="";
					if(steplist.size()==1) {
						StepWisePrice step = steplist.get(0);
						map.put("stepPrice", step.getPrice());
						continue;
					}
					Map<Integer,Boolean> stepAmount=new HashMap<Integer, Boolean>();
					for(int j=0;j<steplist.size();j++) {
						StepWisePrice step = steplist.get(j);
						if(j==0) {
							stepAmount.put(step.getAmount(),true);
						}else {
							if(stepAmount.containsKey(step.getAmount())) {
								continue;
							}
						}
						stepPrice+=("{amount:"+step.getAmount()+",price:"+step.getPrice()+"},");
					}
					map.put("stepPrice", stepPrice.substring(0,stepPrice.length()-1));
				}else {
					map.put("stepPrice", map.get("price"));
				}
			}
		}
		return list;
	}

	public void getExcelMaterialAllInfoReport(SXSSFWorkbook workbook, List<Map<String, Object>> list) {
		Sheet sheet = workbook.createSheet("sheet1");
		List<String> titlelist = new ArrayList<String>();
		Map<String, String> titlechange = new HashMap<String, String>();
		titlelist.add("SKU");
		titlelist.add("产品名称");
		titlelist.add("带包装长-宽-高(cm)-重量(kg)");
		titlelist.add("采购单价(含阶梯价)");
		titlelist.add("类别");
		titlelist.add("组装清单");
		titlelist.add("耗材清单");
		titlelist.add("箱规长-宽-高(cm)-重量(kg)");
		titlelist.add("单箱数量");
		titlelist.add("品牌");
		titlelist.add("规格");
		titlelist.add("分类");
		titlelist.add("退税率");
		titlelist.add("产品负责人");
		titlelist.add("生效日期");
		titlelist.add("净产品长-宽-高(cm)-重量(kg)");
		titlelist.add("供应商");
		titlelist.add("供货周期");
		titlelist.add("不良率");
		titlelist.add("其他成本");
		titlelist.add("供应商代码");
		titlelist.add("采购链接");
		titlelist.add("颜色标签");
		titlelist.add("备注");
		titlelist.add("在途库存");
		titlelist.add("库存");
		titlelist.add("是否归档");

		titlechange.put("SKU", "sku");
		titlechange.put("产品名称", "name");
		titlechange.put("带包装长-宽-高(cm)-重量(kg)", "pageDimensions");
		titlechange.put("采购单价(含阶梯价)", "stepPrice");
		titlechange.put("类别", "issfg");
		titlechange.put("组装清单", "asslist");
		titlechange.put("耗材清单", "consumablelist");
		titlechange.put("品牌", "brand");
		titlechange.put("规格", "specification");
		titlechange.put("分类", "category");
		titlechange.put("退税率", "vatrate");
		titlechange.put("产品负责人", "owner");
		titlechange.put("生效日期", "effectivedate");
		titlechange.put("净产品长-宽-高(cm)-重量(kg)", "itemDimensions");
		titlechange.put("箱规长-宽-高(cm)-重量(kg)", "boxDimensions");
		titlechange.put("单箱数量", "boxnum");
		titlechange.put("供应商", "supplier");
		titlechange.put("供货周期", "deliverycycle");
		titlechange.put("不良率", "badrate");
		titlechange.put("其他成本", "othercost");
		titlechange.put("供应商代码", "purchasecode");
		titlechange.put("采购链接", "purchaseurl");
		titlechange.put("颜色标签", "color");
		titlechange.put("备注", "remark");
		titlechange.put("在途库存", "inbound");
		titlechange.put("库存", "qty");
		titlechange.put("是否归档", "isDelete");

		// 在索引0的位置创建行（最顶端的行）
		Row row = sheet.createRow(0);
		for (int i = 0; i < titlelist.size(); i++) {
			Cell cell = row.createCell(i);
			cell.setCellValue(titlelist.get(i));
		}

		for (int i = 0; i < list.size(); i++) {
			Row skurow = sheet.createRow(i + 1);
			Map<String, Object> skumap = list.get(i);
			for (int j = 0; j < titlelist.size(); j++) {
				Cell cell = skurow.createCell(j);
				if (skumap.get(titlechange.get(titlelist.get(j))) == null) {
					cell.setCellValue("-");
				} else {
					cell.setCellValue(skumap.get(titlechange.get(titlelist.get(j))).toString());
				}
			}
		}
	}

	public List<Map<String, Object>> selectCommonImage() {
		return materialMapper.selectCommonImage();
	}

	public List<Material> selectByImage(String image) {
		return materialMapper.selectByImage(image);
	}

	public Material selectBySKU(String shopid, String sku) {
		QueryWrapper<Material> queryWrapper=new QueryWrapper<Material>();
		queryWrapper.eq("sku", sku);
		queryWrapper.eq("shopid", shopid);
		queryWrapper.eq("isDelete", false);
		List<Material> mlist = this.list(queryWrapper);
		if(mlist!=null && mlist.size()>0){
			return mlist.get(0);
		} 
		return null;
	}

	public List<MaterialConsumableVO> selectConsumableByMainmid(String id,String shopid) {
		List<MaterialConsumableVO> list=materialConsumableMapper.selectConsumableByMainMmid(id);
		if(list!=null && list.size()>0) {
			for(int i=0;i<list.size();i++) {
				String submaterialid = list.get(i).getId();
				Map<String, Object> invlist = inventoryMapper.findInvTUDetailByParentId(submaterialid, null, shopid);
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
	public List<MaterialConsumableVO> selectConsumableByMainmid(String id,String warehouseid ,String shopid) {
		List<MaterialConsumableVO> list=materialConsumableMapper.selectConsumableByMainMmid(id);
		if(list!=null && list.size()>0) {
			for(int i=0;i<list.size();i++) {
				String submaterialid = list.get(i).getId();
				Map<String, Object> invlist = inventoryMapper.findInvTUDetailByParentId(submaterialid, warehouseid, shopid);
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
	public List<MaterialSupplierVO> selectSupplierByMainmid(String id) {
		List<MaterialSupplierVO> supplierList=materialSupplierMapper.selectSupplierByMainId(id);
		if(supplierList!=null && supplierList.size()>0) {
			for(MaterialSupplierVO item:supplierList) {
				String supid = item.getSupplierid();
				if(GeneralUtil.isNotEmpty(supid)) {
					List<Map<String, Object>> stepList=materialSupplierStepwiseMapper.selectSupplierByMainId(id,supid);
					if(stepList!=null && stepList.size()>0) {
						item.setStepList(stepList);
					}
				}
			}
			return supplierList;
		}else {
			return null;
		}
	}
	
	public List<Map<String, Object>> selectSupplerOtherById(String id) {
		List<Map<String, Object>> supplierList=materialSupplierMapper.selectSupplerOtherById(id);
		if(supplierList!=null && supplierList.size()>0) {
			for(Map<String, Object> item:supplierList) {
				String supid = item.get("supplierid").toString();
				if(GeneralUtil.isNotEmpty(supid)) {
					List<Map<String, Object>> stepList=materialSupplierStepwiseMapper.selectSupplierByMainId(id,supid);
					if(stepList!=null && stepList.size()>0) {
						item.put("stepList", stepList);
					}
				}
			}
			return supplierList;
		}else {
			return null;
		}
	}

	public List<MaterialSupplier> selectSupplierByMaterialId(String id) {
		QueryWrapper<MaterialSupplier> queryWrapper=new QueryWrapper<MaterialSupplier>();
		queryWrapper.eq("materialid",id);
		return  materialSupplierMapper.selectList(queryWrapper);
	}
	
	
	public int deleteMaterialSupplierStepwise(String materialid,String supplierid) {
		QueryWrapper<MaterialSupplierStepwise> queryWrapper=new QueryWrapper<MaterialSupplierStepwise>();
		queryWrapper.eq("materialid",materialid);
		queryWrapper.eq("supplierid",supplierid);
		return materialSupplierStepwiseMapper.delete(queryWrapper);
	}

	public int saveMaterialSupplierStepwise(MaterialSupplierStepwise mss) {
		// TODO Auto-generated method stub
	 	return materialSupplierStepwiseMapper.insert(mss);
	}

	public List<Map<String, Object>> findConsumableDetailByShipment(String shopid, String shipmentid) {
		// TODO Auto-generated method stub
		return materialConsumableMapper.findConsumableDetailByShipment( shopid,  shipmentid) ;
	}

	
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
	     Material mainMaterial = this.findBySKU(sku, shopid);
	     Integer mainPurchaseQty = mainSKUQty.get(sku);
		 List<Map<String, Object>> list = null;
		 this.selectConsumableByMainmid(mainMaterial.getId(),shopid);
		 Map<String, Object> mainmap = inventoryMapper.getSelfInvBySKU(warehouseid ,mainMaterial.getId());
		 mainmap.put("material", mainMaterial);
		 mainmap.put("mainPurchaseQty", mainPurchaseQty);
		 for(Map<String, Object> item:list) {
			 BigDecimal unitAmount=(BigDecimal) item.get("amount");
			 String key = item.get("id").toString();
			 Material submaterial = this.getById(key);
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
	return materialConsumableMapper.findConsumableDetailList(maps);
}

public int saveInventoryConsumable(String skulist,UserInfo user,String warehousename,String shipmentid) {
	skulist=skulist.replace("[", "");
	skulist=skulist.replace("]", "");
    List<Map<String, Object>> skuArray = GeneralUtil.jsonStringToMapList(skulist);
    int result=0;
	QueryWrapper<Warehouse> queryWrapper=new QueryWrapper<Warehouse>();
	queryWrapper.eq("name",warehousename);
	queryWrapper.eq("shopid",user.getCompanyid());
	List<Warehouse> warelist = warehouseService.list(queryWrapper);
	if(warelist!=null && warelist.size()>0) {
		String warehouseid=warelist.get(0).getId();
		if(skuArray!=null && skuArray.size()>0) {
			OutWarehouseForm form=new OutWarehouseForm();
			BigInteger bigid = UUIDUtil.getBigIntUUIDshort();
			String formid = bigid.toString();
			form.setNumber(shipmentid);
			form.setAudittime(new Date());
			form.setAuditstatus(2);
			form.setCreatedate(new Date());
			form.setCreator(user.getId());
			form.setWarehouseid(warehouseid);
			form.setShopid(user.getCompanyid());
			form.setOpttime(new Date());
			form.setOperator(user.getId());
			form.setRemark("发货单耗材出库");
			form.setId(formid);
			outWarehouseFormMapper.insert(form);
			for(int i=0;i<skuArray.size();i++) {
				Map<String, Object> item = skuArray.get(i);
				String sku = item.get("sku").toString();
				int qty= Integer.parseInt(item.get("qty").toString());
				Material material = this.findBySKU(sku, user.getCompanyid());
				if(material!=null) {
					OutWarehouseFormEntry record=new OutWarehouseFormEntry();
					record.setAmount(qty);
					record.setFormid(formid);
					record.setMaterialid(material.getId());
					outWarehouseFormEntryMapper.insert(record);
					InventoryParameter para=new InventoryParameter();
					para.setAmount(qty);
					para.setFormid(formid);
					para.setFormtype("outstockform");
					para.setMaterial(material.getId());
					para.setNumber(shipmentid);
					para.setOperator(user.getId());
					para.setOpttime(new Date());
					para.setShopid(user.getCompanyid());
					para.setStatus(EnumByInventory.alReady);
					para.setWarehouse(warehouseid);
					result+= inventoryService.outStockByDirect(para);
				}
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

public List<Map<String, Object>> selectProPriceHisById(String id) {
	return erpMaterialHistoryMapper.selectProPriceHisById(id);
}
 
public MaterialCustoms selectCustomsByMaterialId(String id) {
	return materialCustomsMapper.selectById(id);
}

@Override
public Map<String, Object> updateMaterialCustoms(String id, String addfee, String material,String ftype) {
	MaterialCustoms cust = materialCustomsMapper.selectById(id);
	int res=0;
	if(cust!=null) {
		if("addfee".equals(ftype)) {
			if(GeneralUtil.isNotEmpty(addfee)) {
				cust.setAddfee(new BigDecimal(addfee));
			}else {
				cust.setAddfee(null);
			}
		}else {
			if(GeneralUtil.isNotEmpty(material)) {
				cust.setMaterial(material);
			}else {
				cust.setMaterial(null);
			}
		}
		res=materialCustomsMapper.updateById(cust);
	}
	Map<String,Object> maps=new HashMap<>();
	if(res>0) {
		maps.put("msg", "操作成功!");
		maps.put("code", "success");
	}else {
		maps.put("msg", "操作失败!");
		maps.put("code", "fail");
	}
	return maps;
}

public Map<String, Object> getRealityPrice(String materialid){
	return materialMapper.getRealityPrice(materialid);
}

	@Override
	public int uploadMaterialImg(UserInfo userinfo, String materialid, InputStream inputStream, String filename) {
		int result=0;
		Picture picture =null;
		try {
			if(StrUtil.isNotEmpty(filename)) {
				Material material = this.baseMapper.selectById(materialid);
				if(material!=null) {
					String filePath = PictureServiceImpl.materialImgPath + userinfo.getCompanyid();
					int len = filename.lastIndexOf(".");
					String filenames = filename.substring(0, len);
					String imgtype=filename.substring(len, filename.length());
					filename=filenames+System.currentTimeMillis()+imgtype;
					picture = pictureService.uploadPicture(inputStream, null, filePath, filename, material.getImage());
					if(picture!=null) {
						material.setImage(picture.getId());
						this.updateById(material);
						result=1;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<Material> getMaterialByInfo(String shopid, String sku, String name) {
		QueryWrapper<Material> queryWrapper=new QueryWrapper<Material>();
		queryWrapper.eq("shopid", shopid);
		queryWrapper.eq("isDelete", false);
		if(StrUtil.isNotEmpty(name)) {
			queryWrapper.like("name", "%"+name+"%");
		}
		if(StrUtil.isNotEmpty(sku)) {
			queryWrapper.eq("sku", sku);
		}
		List<Material> list = this.baseMapper.selectList(queryWrapper);
		if(list!=null && list.size()>0) {
			for(int i=0;i<list.size();i++) {
				String picid = list.get(i).getImage();
				Picture picture = pictureService.getById(picid);
				if(picture!=null && picture.getLocation()!=null) {
					String value=picture.getLocation();
					list.get(i).setImage(fileUpload.getPictureImage(value)); 
				}
			}
		}
		return list;
	}

}
