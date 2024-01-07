package com.wimoor.erp.material.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.erp.assembly.pojo.vo.AssemblyVO;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.mvc.FileUpload;
import com.wimoor.common.pojo.entity.Picture;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.IPictureService;
import com.wimoor.common.service.impl.PictureServiceImpl;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.api.AdminClientOneFeignManager;
import com.wimoor.erp.api.AmazonClientOneFeignManager;
import com.wimoor.erp.assembly.pojo.entity.Assembly;
import com.wimoor.erp.assembly.service.IAssemblyService;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.customer.service.ICustomerService;
import com.wimoor.erp.inventory.mapper.InventoryMapper;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.material.mapper.ERPMaterialHistoryMapper;
import com.wimoor.erp.material.mapper.MaterialBrandMapper;
import com.wimoor.erp.material.mapper.MaterialCategoryMapper;
import com.wimoor.erp.material.mapper.MaterialConsumableMapper;
import com.wimoor.erp.material.mapper.MaterialCustomsFileMapper;
import com.wimoor.erp.material.mapper.MaterialCustomsItemMapper;
import com.wimoor.erp.material.mapper.MaterialCustomsMapper;
import com.wimoor.erp.material.mapper.MaterialMapper;
import com.wimoor.erp.material.mapper.MaterialTagsMapper;
import com.wimoor.erp.material.pojo.dto.MaterialDTO;
import com.wimoor.erp.material.pojo.dto.PlanDTO;
import com.wimoor.erp.material.pojo.entity.DimensionsInfo;
import com.wimoor.erp.material.pojo.entity.ERPMaterialHistory;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.pojo.entity.MaterialCategory;
import com.wimoor.erp.material.pojo.entity.MaterialConsumable;
import com.wimoor.erp.material.pojo.entity.MaterialCustoms;
import com.wimoor.erp.material.pojo.entity.MaterialCustomsItem;
import com.wimoor.erp.material.pojo.entity.MaterialMark;
import com.wimoor.erp.material.pojo.entity.MaterialSupplierStepwise;
import com.wimoor.erp.material.pojo.entity.MaterialTags;
import com.wimoor.erp.material.pojo.entity.StepWisePrice;
import com.wimoor.erp.material.pojo.vo.MaterialConsumableVO;
import com.wimoor.erp.material.pojo.vo.MaterialInfoVO;
import com.wimoor.erp.material.pojo.vo.MaterialSupplierVO;
import com.wimoor.erp.material.pojo.vo.MaterialVO;
import com.wimoor.erp.material.service.IDimensionsInfoService;
import com.wimoor.erp.material.service.IMaterialCategoryService;
import com.wimoor.erp.material.service.IMaterialConsumableInventoryService;
import com.wimoor.erp.material.service.IMaterialConsumableService;
import com.wimoor.erp.material.service.IMaterialMarkService;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.material.service.IMaterialSupplierService;
import com.wimoor.erp.material.service.IStepWisePriceService;
import com.wimoor.erp.material.service.IStockCycleService;
import com.wimoor.erp.material.service.IZipRarUploadService;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlanItem;
import com.wimoor.erp.purchase.service.IPurchasePlanItemService;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
 
 

@Service("materialService")
@RequiredArgsConstructor
public class MaterialServiceImpl extends  ServiceImpl<MaterialMapper,Material> implements IMaterialService {
	 
	final ERPMaterialHistoryMapper erpMaterialHistoryMapper;
	 
	final IMaterialMarkService materialMarkService;
	 
	final MaterialCategoryMapper materialCategoryMapper;
	 
	final IAssemblyService assemblyService;
	 
	final IStockCycleService stockCycleService;
	 
	final IStepWisePriceService stepWisePriceService;
	 
	final IPictureService pictureService;
	 
	final IDimensionsInfoService dimensionsInfoService;
	 
	final IMaterialCategoryService materialCategoryService;
	 
	@Autowired
 	@Lazy
	IPurchasePlanItemService purchasePlanItemService;
	 
	final IZipRarUploadService zipRarUploadService;
	 
	final ICustomerService customerService;
	 
	final MaterialConsumableMapper materialConsumableMapper;
	 
	final IWarehouseService warehouseService;
	 
	final InventoryMapper inventoryMapper;
	
	final FileUpload fileUpload;
	final MaterialTagsMapper materialTagsMapper;
	@Lazy
	@Autowired
	IInventoryService inventoryService;
	 
	@Lazy
	@Autowired
	IMaterialSupplierService iIMaterialSupplierService;
	final IMaterialConsumableInventoryService iMaterialConsumableInventoryService;
	final MaterialCustomsMapper materialCustomsMapper;
	final IMaterialConsumableService  iMaterialConsumableService;
	final MaterialCustomsFileMapper materialCustomsFileMapper;
	final AmazonClientOneFeignManager amazonClientOneFeign;
	final AdminClientOneFeignManager adminClientOneFeign;
	final MaterialCustomsItemMapper materialCustomsItemMapper;
	final MaterialBrandMapper materialBrandMapper;
	
	public MaterialVO findMaterialById(String id) {
		return this.baseMapper.findMaterialById(id);
	}

	public List<String> getmskuList(List<String> list) {
		return materialTagsMapper.getmskuList(list);
	}
	
 
	public IPage<Map<String, Object>> findByCondition(Page<?> page,Map<String, Object> params) {
	    IPage<Map<String, Object>> list = this.baseMapper.findMaterial(page,params);
	    String shopid=params.get("shopid").toString();
	    Map<String, Object>  tagsNameMap= null;
	    try {
	    	Result<Map<String, Object>> tagResult = adminClientOneFeign.findTagsName(shopid);
	    	if(Result.isSuccess(tagResult)&&tagResult.getData()!=null) {
	    		tagsNameMap=tagResult.getData();
	    	}
	    }catch(Exception e) {
	    	e.printStackTrace();
	    }
	    if(list!=null && list.getRecords().size()>0) {
	    	Calendar c = Calendar.getInstance();
	    	Boolean withoutTags=params.get("withoutTags")==null?false:(boolean) params.get("withoutTags");
			Boolean withPriceHis=params.get("withPriceHis")==null?false:(boolean) params.get("withPriceHis");
	    	 for(int i=0;i<list.getRecords().size();i++) {
	    		 Map<String, Object> item = list.getRecords().get(i);
	    		 String mid=item.get("id").toString();
	    		 if(withPriceHis==true) {
	    			 if (item.get("delivery_cycle") != null) {
	 					c.add(Calendar.DATE, Integer.parseInt(item.get("delivery_cycle").toString()));
	 				 }
	 	    		 item.put("deliverycycledate",c.getTime());
	 	    		 List<Map<String, Object>> historylist = this.selectProPriceHisById(mid);
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
	 					    				   GeneralUtil.formatDate((GeneralUtil.getDate(history.get("opttime"))))+
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
	 	    		 item.put("pricestr", pricestr);
	    		 }
	    		 item.put("itemshow", false);
	    		 if(withoutTags!=true) {
	    			 if(item.get("tagids")!=null&&tagsNameMap!=null) {
		    			 String[] tagarray = item.get("tagids").toString().split(",");
		    			 if(tagarray!=null&&tagarray.length>0) {
		    				 List<String>  materialTagsList= Arrays.asList(tagarray);
		    				 List<Object> tagnamelist=new ArrayList<Object>();
		    					for(String tagitem:materialTagsList) {
	    							 Object name = tagsNameMap.get(tagitem);
	    							 if(name!=null) {
	    								 tagnamelist.add(name);
	    							 }
	    						}
	    					   item.put("TagNameList",tagnamelist);
		    			 }
		    		 }
	    		 }
	    		
	    	 }
	    }
		return list;
	}
	
 
	public IPage<Map<String, Object>> findConsumableByCondition(Page<?> page,Map<String, Object> params) {
	    IPage<Map<String, Object>> list = this.baseMapper.findConsumable(page,params);
		return list;
	}
	public IPage<Map<String, Object>> findPackageByCondition(Page<?> page,Map<String, Object> params) {
	    IPage<Map<String, Object>> list = this.baseMapper.findPackage(page,params);
		return list;
	}
	public List<Map<String, Object>> findPackageByCondition(Map<String, Object> params) {
		List<Map<String, Object>> list = this.baseMapper.findPackage(params);
		return list;
	}
	public List<Map<String, Object>> findAllByCondition(Map<String, Object> map) {
		return  this.baseMapper.findAllByCondition(map);
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
		return this.baseMapper.selectAllSKUForSelect(sku, shopid);
	}

	public List<Map<String, Object>> selectAllSKUForLabel(String sku, String shopid) {
		return this.baseMapper.selectAllSKUForLabel(sku, shopid);
	}

	public List<MaterialCategory> selectAllCateByShopid(String shopid) {
		QueryWrapper<MaterialCategory> queryWrapper=new QueryWrapper<MaterialCategory>();
		queryWrapper.eq("shopid", shopid);
		queryWrapper.orderByAsc("name");
		List<MaterialCategory> list = materialCategoryMapper.selectList(queryWrapper);
		return list;
	}

	@Override
	@CacheEvict(value = { "materialListCache","inventoryByMskuCache" }, allEntries = true)
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
	@CacheEvict(value = { "materialListCache","inventoryByMskuCache" }, allEntries = true)
	MaterialCustoms saveMaterialCustoms(MaterialInfoVO vo,Material material){
    	//海关信息
		MaterialCustoms customsvo = vo.getCustoms();
		//String customscode=vo.getCustoms();//   海关编码
		String engname=customsvo.getNameEn();//   产品英文名
		String chnname=customsvo.getNameCn();//   产品中文名
		//String stuff=request.getParameter("stuff");//   产品材质
		String materialmodel=customsvo.getModel();//   产品型号
		String materialuse=customsvo.getMaterialUse();//   产品用途
		//String materialbrand=request.getParameter("materialbrand");//   产品品牌
		boolean isele=customsvo.getIselectricity();//   是否带电/磁
		boolean isdanger=customsvo.getIsdanger();//   是否危险品
		BigDecimal unitprice = customsvo.getUnitprice();
		BigDecimal addfee = customsvo.getAddfee();//附加费用
	 
		MaterialCustoms customs=new MaterialCustoms();
		customs.setMaterialid(material.getId());
		customs.setIselectricity(isele);
		customs.setModel(materialmodel);
		customs.setNameCn(chnname);
		customs.setBrand(customsvo.getBrand());
		customs.setMaterial(customsvo.getMaterial());
		customs.setMaterialUse(materialuse);
		customs.setNameEn(engname);
		customs.setIsdanger(isdanger);
		customs.setUnitprice(unitprice);
		customs.setAddfee(addfee);
		customs.setCurrency(customsvo.getCurrency());
		//先删除再加
		materialCustomsMapper.deleteById(material.getId());
		materialCustomsMapper.insert(customs);
		List<MaterialCustomsItem> customsItemList = vo.getCustomsItemList();
		if(customsItemList!=null && customsItemList.size()>0) {
			//先删除后加
			QueryWrapper<MaterialCustomsItem> queryWrapper=new QueryWrapper<MaterialCustomsItem>();
			queryWrapper.eq("materialid", material.getId());
			materialCustomsItemMapper.delete(queryWrapper);
			for (int i = 0; i < customsItemList.size(); i++) {
				MaterialCustomsItem item = customsItemList.get(i);
				if(StrUtil.isNotEmpty(item.getCode()) && item.getFee()!=null && item.getTaxrate()!=null) {
					MaterialCustomsItem entity=new MaterialCustomsItem();
					entity.setMaterialid(material.getId());
					entity.setCode(item.getCode());
					entity.setCountry(item.getCountry());
					entity.setFee(item.getFee());
					entity.setTaxrate(item.getTaxrate());
					entity.setCurrency(item.getCurrency());
					materialCustomsItemMapper.insert(entity);
				}
				
			}
		}
		return customs;
    }
	@CacheEvict(value = { "materialListCache","inventoryByMskuCache" }, allEntries = true)
	public String saveAllInfo(MaterialInfoVO vo,MultipartFile file, UserInfo user) throws ERPBizException {
 		if(vo==null) {
			throw new ERPBizException("填入数据参数异常！");
		}
 		List<MaterialConsumableVO> consumableList = vo.getConsumableList();
 		if(consumableList!=null&&consumableList.size()>0) {
 			for(MaterialConsumableVO item:consumableList) {
 				if(item.getAmount().floatValue()<0.000001) {
 					throw new ERPBizException("辅料数量必须大于0！");
 				}
 			}
 		}
 		List<AssemblyVO> asslist = vo.getAssemblyList();
 		if(asslist!=null&&asslist.size()>0) {
 			for(AssemblyVO item:asslist) {
 				if(item.getSubnumber()<=0) {
 					throw new ERPBizException("组装数量必须大于0！");
 				}
 			}
 		}
		Material material=saveBaseInfo(vo,file,user);
		saveMaterialCustoms(vo,material);
		saveMaterialAssembly(vo,material);
		// 获取物料基本信息
		List<MaterialSupplierVO> supplierlist = vo.getSupplierList();
		iIMaterialSupplierService.saveOrUpdateSupplier(supplierlist,user,material.getId(),material);
		saveMaterialConsumable(consumableList,user,material.getId());
		saveTags(vo,material,user);
		if(material.getId()!=null) {
			return material.getId();
		}else {
			throw new ERPBizException("填入数据参数异常！");
		}
	}
	
	@CacheEvict(value = { "materialListCache","inventoryByMskuCache" }, allEntries = true)
	private void saveTags(MaterialInfoVO vo, Material material, UserInfo user) {
		// TODO Auto-generated method stub
		//标签添加对应关系 先删除后加
		QueryWrapper<MaterialTags> delqueryWrapper=new QueryWrapper<MaterialTags>();
		delqueryWrapper.eq("mid", material.getId());
		materialTagsMapper.delete(delqueryWrapper);
		if(StrUtil.isNotEmpty(vo.getTaglist())) {
			String taglist = vo.getTaglist();
			String[] tagLists = taglist.split(",");
			if(tagLists!=null && tagLists.length>0) {
				for (int i = 0; i < tagLists.length; i++) {
					String tagid = tagLists[i];
					if(StrUtil.isNotEmpty(tagid)) {
						MaterialTags tag=new MaterialTags();
						tag.setMid(material.getId());
						tag.setOperator(user.getId());
						tag.setOpttime(new Date());
						tag.setTagid(tagid);
						materialTagsMapper.insert(tag);
					}
				}
			}
		}
	}

	private void saveMaterialAssembly(MaterialInfoVO vo, Material material) {
		// TODO Auto-generated method stub
		List<AssemblyVO> asslist = vo.getAssemblyList();
		List<AssemblyVO> sublist = assemblyService.selectByMainmid(material.getId());
		Map<String,AssemblyVO> subset=new HashMap<String,AssemblyVO>();
		for(AssemblyVO item:sublist) {
			subset.put(item.getSubmid(),item);
		}
		List<Assembly> mainlist = assemblyService.selectBySubid(material.getId());
	 
		if ( asslist!=null && asslist.size()>0) {
			if(mainlist!=null&&mainlist.size()>0) {
				throw new BizException("半成品不能添加组装清单");
			}
			for (int i = 0; i < asslist.size(); i++) {
				AssemblyVO assitem = asslist.get(i);
				Assembly assembly = new Assembly();
				assembly.setMainmid(material.getId());
				assembly.setSubmid(assitem.getSubmid());
				assembly.setSubnumber(assitem.getSubnumber());
				assembly.setRemark(assitem.getRemark());
				assembly.setOperator(material.getOperator());
				assembly.setOpttime(new Date());
				Material assub = this.getById(assitem.getSubmid());
				if(assub.getIssfg().equals("1")) {
					throw new BizException("组装清单中的子产品不能存在组合产品");
				}
				AssemblyVO oldone = subset.get(assub.getId());
				if(oldone!=null) {
					assembly.setId(oldone.getId());
					assemblyService.updateById(assembly);
					subset.remove(assub.getId());
				}else {
					assemblyService.save(assembly);
				}
				if(assub.getIssfg()==null||!assub.getIssfg().equals("2")) {
					assub.setIssfg("2");
					this.baseMapper.updateById(assub);
				}
			}
			Material main = this.getById(material.getId());
			material.setIssfg("1");
			vo.getMaterial().setIssfg("1");
			if(main.getIssfg()==null||!main.getIssfg().equals("1")) {
				main.setIssfg("1");
				this.baseMapper.updateById(main);
			}
		}else {
			    if(mainlist!=null&&mainlist.size()>0) {
			    	Material main = this.getById(material.getId());
			    	material.setIssfg("2");
			    	vo.getMaterial().setIssfg("2");
			    	if(main.getIssfg()==null||!main.getIssfg().equals("2")) {
			    		main.setIssfg("2");
						this.baseMapper.updateById(main);	
			    	}
			    }else {
			    	material.setIssfg("0");
			    	vo.getMaterial().setIssfg("0");
			    	Material main = this.getById(material.getId());
			    	if(main.getIssfg()==null||!main.getIssfg().equals("0")) {
			    		main.setIssfg("0");
						this.baseMapper.updateById(main);	
			    	}
			    }
		}
		for(Entry<String, AssemblyVO> entry:subset.entrySet()) {
			String id=entry.getKey();
			assemblyService.removeById(entry.getValue().getId());
			LambdaQueryWrapper<Assembly> asquery=new LambdaQueryWrapper<Assembly>();
			asquery.eq(Assembly::getSubmid,id);
			long count = assemblyService.count(asquery);
			if(count==0) {
				Material assub = this.getById(id);
				if(assub.getIssfg()==null||!assub.getIssfg().equals("0"))
				assub.setIssfg("0");
				this.baseMapper.updateById(assub);	
			}
		}
		
	}
 
	//耗材列表
	public void saveMaterialConsumable(List<MaterialConsumableVO> list,UserInfo user,String id) {
		//一进来先删除当前的列表 以最新的ConsumableList为准
		QueryWrapper<MaterialConsumable> queryWrapper=new QueryWrapper<MaterialConsumable>();
		queryWrapper.eq("materialid",id);
		materialConsumableMapper.delete(queryWrapper);
		//ConsumableListpara="["+ConsumableListpara+"]";
		//JSONArray jsonArray = GeneralUtil.getJsonArray(ConsumableListpara);
		if(list!=null && list.size()>0) {
			for(int i=0;i<list.size();i++) {
				MaterialConsumableVO item = list.get(i);
				MaterialConsumable cons=new MaterialConsumable();
				cons.setAmount(item.getAmount());
				cons.setMaterialid(id);
				cons.setOperator(user.getId());
				cons.setOpttime(new Date());
				cons.setSubmaterialid(item.getId());
				materialConsumableMapper.insert(cons);
			}
		}
	}

	public Material saveMaterial(DimensionsInfo itemdim,DimensionsInfo pkgdim,DimensionsInfo boxdim,MaterialInfoVO vo,MultipartFile file,UserInfo user) {
		MaterialVO materialvo=vo.getMaterial();
		QueryWrapper<Material> queryWrapper=new QueryWrapper<Material>();
		queryWrapper.eq("shopid", user.getCompanyid());
		queryWrapper.eq("sku", vo.getMaterial().getSku());
		queryWrapper.eq("isDelete",false);
		String oldowner=null;
		boolean isupdate=true;
		String oldpicture=null;
		Material  material= this.baseMapper.selectOne(queryWrapper);
		if(material==null) {
			  material = new Material();
			  isupdate=false;
		}else {
			if(material.isDelete()) {
				throw new ERPBizException("产品SKU："+ material.getSku() +"已经归档，请从归档状态还原后使用!");
			}else  if(!material.getId().equals(vo.getMaterial().getId())) {
				throw new ERPBizException("该SKU已经存在！");
			}else {
				if(vo.getMaterial().getIssfg()!=null && (material.getIssfg()==null || !material.getIssfg().equals(vo.getMaterial().getIssfg()) )) {
					QueryWrapper<PurchasePlanItem> checkquery=new QueryWrapper<PurchasePlanItem>();
					checkquery.eq("materialid", materialvo.getId());
					checkquery.eq("shopid", user.getCompanyid());
					List<PurchasePlanItem> purchaseList = purchasePlanItemService.list(checkquery);
					if (purchaseList != null && purchaseList.size() > 0) {
						throw new ERPBizException("已加入补货计划，请移除后再修改产品组装类别！");
					}
			     }
				
			}
			oldowner=material.getOwner();
			oldpicture=material.getImage();
		}
       String pictureid=null;
		if("ok".equals(vo.getIscopy()) && file==null) {
			if(vo.getMaterial().getImageid()!=null) {
				Picture oldpic = pictureService.getById(vo.getMaterial().getImageid());
				if(oldpic!=null) {
					Picture pic=new Picture();
					pic.setHeight(oldpic.getHeight());
					pic.setHeightUnits(oldpic.getHeightUnits());
					pic.setLocation(oldpic.getLocation());
					pic.setUrl(oldpic.getUrl());
					pic.setWidth(oldpic.getWidth());
					pic.setWidthUnits(oldpic.getWidthUnits());
					boolean isok = pictureService.save(pic);
					if(isok==true) {
						material.setImage(pic.getId());
					}
				}
			}
		}
		if(file!=null) {
			//改变了图片
			try {
				pictureid=this.uploadMaterialImg(user, material.getId(), file.getInputStream(), file.getOriginalFilename(),oldpicture);
				material.setImage(pictureid);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		material.setShopid(user.getCompanyid());
		material.setOpttime(new Date());
		material.setOperator(user.getId());
		if(isupdate==false) {
			material.setCreator(material.getOperator());
			material.setCreatedate(new Date());
		}
		material.setPrice(materialvo.getPrice());
		material.setBoxnum(materialvo.getBoxnum());
		material.setVatrate(materialvo.getVatrate());
		if(GeneralUtil.isNotEmpty(materialvo.getCategoryid())) {
			material.setCategoryid(materialvo.getCategoryid());
		}
		material.setOwner(materialvo.getOwner());
		material.setSku(materialvo.getSku());
		material.setUpc(materialvo.getUpc());
		material.setName(materialvo.getName());
		material.setSmlAndLight(false);
		material.setBrand(materialvo.getBrandid());
		material.setSpecification(materialvo.getSpecification());
		if(material.getIssfg()==null) {
			material.setIssfg("0");
		}
		material.setRemark(materialvo.getRemark());
		material.setColor(null);
		material.setMtype(materialvo.getMtype()==null?0:materialvo.getMtype());
		material.setDeliveryCycle(materialvo.getDeliveryCycle());
		material.setEffectivedate(materialvo.getEffectivedate());
		if(itemdim!=null) {
			material.setItemdimensions(itemdim.getId());
		}else {
			material.setItemdimensions(null);
		}
		if(boxdim!=null) {
			material.setBoxdimensions(boxdim.getId());
		}else {
			material.setBoxdimensions(null);
		}
		if(pkgdim!=null) {
			material.setPkgdimensions(pkgdim.getId());
		}else {
			material.setBoxdimensions(null);
		}
		//组装周期
		if (materialvo.getAssemblyTime() != null) {
			int assemblyTime = materialvo.getAssemblyTime();
			material.setAssemblyTime(assemblyTime);
		} else {
			int assemblyTime = 0;
			material.setAssemblyTime(assemblyTime);
		}
		 try {
			//同步修改产品的负责人
			 if(material.getOwner()!=null) {
				 amazonClientOneFeign.updateProductOwner(material.getSku(),material.getOwner(),oldowner);
			 }
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
		if(isupdate){
				  this.baseMapper.updateById(material);
				  ERPMaterialHistory his=new ERPMaterialHistory();
				  BeanUtil.copyProperties(material, his);
				  erpMaterialHistoryMapper.insert(his);
				  return material;
		}else {
			     this.baseMapper.insert(material);
				  ERPMaterialHistory his=new ERPMaterialHistory();
				  BeanUtil.copyProperties(material, his);
				  erpMaterialHistoryMapper.insert(his);
				  return material;
		}
		
	}
	public Material saveBaseInfo(MaterialInfoVO vo,MultipartFile file, UserInfo user) throws  ERPBizException {
		DimensionsInfo itemdim=saveItemDim(vo);
		DimensionsInfo pkgdim=savePkgDim(vo);
		DimensionsInfo boxdim=saveBoxDim(vo);
		Material material =saveMaterial(itemdim,pkgdim,boxdim,vo, file,user);
		//最后做图片处理
		return material;
	}

 

	DimensionsInfo saveDim(DimensionsInfo dimvo){
		if(dimvo.getLength()==null &&  dimvo.getWidth()==null && dimvo.getHeight()==null && dimvo.getWeight()==null) {
			if(dimvo.idIsNULL()) {
				return null;
			}else {
				dimensionsInfoService.removeById(dimvo.getId());
				return null;
			}
        }else if(dimvo.getLength()!=null 
        		&&  dimvo.getWidth()!=null 
        		&& dimvo.getHeight()!=null 
        		&& dimvo.getWeight()!=null
        		&& dimvo.getLength().floatValue()<0.000001
        		&& dimvo.getWidth().floatValue()<0.000001
        		&& dimvo.getHeight().floatValue()<0.000001
        		&& dimvo.getWeight().floatValue()<0.000001
        		) {
        	if(dimvo.idIsNULL()) {
				return null;
			}else {
				dimensionsInfoService.removeById(dimvo.getId());
				return null;
			}
        }
		BigDecimal length = dimvo.getLength();
		BigDecimal width =  dimvo.getWidth();
		BigDecimal height = dimvo.getHeight();
		BigDecimal weight = dimvo.getWeight();
		DimensionsInfo dim = new DimensionsInfo();
		dim.setLength(length);
		dim.setWidth(width);
		dim.setHeight(height);
		dim.setWeight(weight);
		DimensionsInfo old=null;
		if(!dimvo.idIsNULL()) {
		   old=	dimensionsInfoService.getById(dimvo.getId());
		}
		if(old!=null) {
			dimensionsInfoService.removeById(old.getId());
			dim.setId(this.warehouseService.getUUID());
			dimensionsInfoService.save(dim);
		}else {
			dimensionsInfoService.save(dim);
		}
		return dim;
	}
	
	
	private DimensionsInfo saveItemDim(MaterialInfoVO vo) {
        if(vo==null||vo.getItemDim()==null)return null;
		DimensionsInfo dimvo=vo.getItemDim();
		if("ok".equals(vo.getIscopy())) {
			dimvo.setId(null);
		}
		return saveDim(dimvo);
	}
	private DimensionsInfo savePkgDim(MaterialInfoVO vo) {
        if(vo==null||vo.getPkgDim()==null)return null;
		DimensionsInfo dimvo=vo.getPkgDim();
		if("ok".equals(vo.getIscopy())) {
			dimvo.setId(null);
		}
		return saveDim(dimvo);
	}
	private DimensionsInfo saveBoxDim(MaterialInfoVO vo) {
        if(vo==null||vo.getBoxDim()==null)return null;
		DimensionsInfo dimvo=vo.getBoxDim();
		if("ok".equals(vo.getIscopy())) {
			dimvo.setId(null);
		}
		return saveDim(dimvo);
	}


	public Map<String, Object> findDimAndAsinBymid(String sku, String shopid, String marketplaceid, String groupid) {
		return this.baseMapper.findDimAndAsinBymid(sku, shopid, marketplaceid, groupid);
	}

	public List<Map<String, Object>> getForSum(String shopid,String groupid) {
		return this.baseMapper.getForSum(shopid,groupid);
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
		return this.baseMapper.findDimensionsInfoBySKU(sku, shopid);
	}

	public List<Map<String, Object>> getOwnerList(String shopid) {
		return this.baseMapper.getOwnerList(shopid);
	}

	@Cacheable(value = "materialListCache")
	public Map<String, Object> findMaterialMapBySku(Map<String,Object> param) {
		List<Map<String, Object>> list = this.baseMapper.findMaterialMapBySku(param);
		Map<String, Object> result=new HashMap<String,Object>();
		for(Map<String, Object> item:list) {
			String sku=item.get("msku").toString();
			result.put(sku, item);
		}
		return result;
	}

	@Cacheable(value = "materialListCache",key="#key")
	public List<String> findMarterialForColorOwner(String key,Map<String, Object> param) {
		return this.baseMapper.findMarterialForColorOwner(param);
	}

	@CacheEvict(value = { "materialListCache","inventoryByMskuCache" }, allEntries = true)
	public void logicalDeleteMaterial(UserInfo user, Material material) {
		if (material.isDelete()) {
			throw new ERPBizException(material.getSku() + "已经归档！请勿重复操作！");
		}
		material.setDelete(true);
		material.setOpttime(new Date());
		material.setOperator(user.getId());
		this.updateById(material);
	}

	@CacheEvict(value = { "materialListCache","inventoryByMskuCache" }, allEntries = true)
	public boolean updateReductionSKUMaterial(UserInfo user, String id, String sku) {
		QueryWrapper<Material> queryWrapper=new QueryWrapper<Material>();
		queryWrapper.eq("sku", sku);
		queryWrapper.eq("shopid", user.getCompanyid());
		queryWrapper.eq("isDelete", false);
		List<Material> materiallist = this.list(queryWrapper);
		if (materiallist != null && materiallist.size() > 0) {
			throw new ERPBizException("该SKU:"+sku+"已经存在，不能还原回去！");
		} else {
			Material oldmaterial =this.getById(id) ;
			oldmaterial.setSku(sku);
			oldmaterial.setShopid(user.getCompanyid());
			oldmaterial.setId(id);
			oldmaterial.setOpttime(new Date());
			oldmaterial.setOperator(user.getId());
			oldmaterial.setDelete(false);
			return this.updateById(oldmaterial);
		}
	}

	@CacheEvict(value = { "materialListCache","inventoryByMskuCache" }, allEntries = true)
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
	@CacheEvict(value = { "materialListCache","inventoryByMskuCache" }, allEntries = true)
	public int updateItemMaterialByPrice(String[] ids, String priceMapList, UserInfo user) {
		int result = 0;
		for (int k = 0; k < ids.length; k++) {
			String materialid = ids[k];
			Material material = this.baseMapper.selectById(materialid);
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

	@CacheEvict(value = { "materialListCache","inventoryByMskuCache" }, allEntries = true)
	public int updateItemMaterialByType(String[] ids, String ftype, String value, UserInfo user) {
		int result = 0;
		for (int k = 0; k < ids.length; k++) {
			String materialid = ids[k];
			Material material = this.baseMapper.selectById(materialid);
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
		return this.baseMapper.findSKUImageForProduct(param);
	}

	public List<Map<String, Object>> copyImageForProduct(List<Map<String, Object>> list, UserInfo user) {
		List<Map<String, Object>> errorList = new ArrayList<Map<String,Object>>();
	 	for(Map<String, Object> map : list) {
			//https://photo.wimoor.com/productImg/26138972975530085/A1F83G8C2ARO7P/51PglvullIL._SL75_.jpg
			String image = map.get("image").toString();
			String materialid = map.get("materialid").toString();
			Material material = this.baseMapper.selectById(materialid);
			String newPath =PictureServiceImpl.materialImgPath + user.getCompanyid() + "/";
				Picture picture=null;
				try {
					picture = pictureService.downloadPicture(image, newPath, material.getImage());
				} catch (Exception e) {
					e.printStackTrace();
				}
				material.setImage(picture.getId());
				this.baseMapper.updateById(material);
		 
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
						length=length.multiply(new BigDecimal("2.54")).setScale(2, RoundingMode.DOWN);
						lenunits=null;
					}
				}
				if(widthunits!=null) {
					if("inches".equals(widthunits)) {
						width=width.multiply(new BigDecimal("2.54")).setScale(2, RoundingMode.DOWN);
						widthunits=null;
					}
				}
				if(heightunits!=null) {
					if("inches".equals(heightunits)) {
						height=height.multiply(new BigDecimal("2.54")).setScale(2, RoundingMode.DOWN);
						heightunits=null;
					}
				}
				if(weightunits!=null) {
					if("pounds".equals(weightunits)) {
						weight=weight.multiply(new BigDecimal("0.453")).setScale(2, RoundingMode.DOWN);
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
		List<Map<String, Object>> list = this.baseMapper.selectAllMaterialByShop(parammap);
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
		return this.baseMapper.selectCommonImage();
	}

	public List<Material> selectByImage(String image) {
		return this.baseMapper.selectByImage(image);
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




public List<Map<String, Object>> selectProPriceHisById(String id) {
	return erpMaterialHistoryMapper.selectProPriceHisById(id);
}
 
public MaterialCustoms selectCustomsByMaterialId(String id) {
	return materialCustomsMapper.selectById(id);
}

@Override
@CacheEvict(value = { "materialListCache","inventoryByMskuCache" }, allEntries = true)
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
	return this.baseMapper.getRealityPrice(materialid);
}
  public void savetofile(InputStream inputStream) {
	  try {
			  File file=new File("C:\\a.jpg");
			  if(!file.exists()){
			      file.createNewFile();
			  }
			  BufferedInputStream in=null;
			  BufferedOutputStream out=null;
			  in=new BufferedInputStream(inputStream);
			  out=new BufferedOutputStream(new FileOutputStream("C:\\a.jpg"));
			  int len=-1;
			  byte[] b=new byte[1024];
			  while((len=in.read(b))!=-1){
			      out.write(b,0,len);
			  }
			  in.close();
			  out.close();
	  } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 
  }
	@Override
	@CacheEvict(value = { "materialListCache","inventoryByMskuCache" }, allEntries = true)
	public String uploadMaterialImg(UserInfo userinfo, String materialid, InputStream inputStream, String filename,String oldpictureid) {
		Picture picture =null;
		try {
			if(StrUtil.isNotEmpty(filename)) {
					String filePath = PictureServiceImpl.materialImgPath + userinfo.getCompanyid();
					int len = filename.lastIndexOf(".");
					String imgtype=filename.substring(len, filename.length());
					filename=materialid+"-"+System.currentTimeMillis()+imgtype;
					picture = pictureService.uploadPicture(inputStream, null, filePath, filename,oldpictureid);
					if(picture!=null) {
					   return picture.getId();
					}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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

	@Override
	public List<String> getTagsIdsListByMsku(String msku, String shopid) {
		List<String> tagids=null;
		QueryWrapper<Material> queryWrapper=new QueryWrapper<Material>();
		queryWrapper.eq("shopid", shopid);
		queryWrapper.eq("sku", msku);
		queryWrapper.eq("isDelete", 0);
		List<Material> mlist = this.baseMapper.selectList(queryWrapper);
		if(mlist!=null && mlist.size()>0) {
			String mid=mlist.get(0).getId();
			QueryWrapper<MaterialTags> tagWrapper=new QueryWrapper<MaterialTags>();
			tagWrapper.eq("mid", mid);
			List<MaterialTags> taglist = materialTagsMapper.selectList(tagWrapper);
			if(taglist!=null && taglist.size()>0) {
				tagids=new ArrayList<String>();
				for (int i = 0; i < taglist.size(); i++) {
					tagids.add(taglist.get(i).getTagid());
				}
			}
		}
		return tagids;
	}

	@Override
	@CacheEvict(value = { "materialListCache","inventoryByMskuCache" }, allEntries = true)
	public List<Map<String,Object>> saveTagsByMid(String mid, String tagids, String userid) {
		Set<String> tagsIdsList=new HashSet<String>();
		if(tagids.contains(",")) {
			//先删除老的 再save
			QueryWrapper<MaterialTags> queryWrapper=new QueryWrapper<MaterialTags>();
			queryWrapper.eq("mid", mid);
			materialTagsMapper.delete(queryWrapper);
			tagids=tagids.substring(0, tagids.length()-1);
			String[] tagsArray = tagids.split(",");
			for (int i = 0; i < tagsArray.length; i++) {
				String tagid = tagsArray[i];
				MaterialTags entity=new MaterialTags();
				entity.setMid(mid);
				entity.setOperator(userid);
				entity.setOpttime(new Date());
				entity.setTagid(tagid);
				materialTagsMapper.insert(entity);
				tagsIdsList.add(tagid);
			}
		}else {
			//清空了标签
			QueryWrapper<MaterialTags> queryWrapper=new QueryWrapper<MaterialTags>();
			queryWrapper.eq("mid", mid);
			materialTagsMapper.delete(queryWrapper);
		}
		Result<List<Map<String,Object>>> tagnamelistResult=adminClientOneFeign.findTagsNameByIds(tagsIdsList);
		List<Map<String,Object>> tagnamelist=tagnamelistResult.getData();
		return tagnamelist;
	}

	@Override
	public String findMaterialTagsByMid(String mid) {
		String strs="";
		QueryWrapper<MaterialTags> queryWrapper=new QueryWrapper<MaterialTags>();
		queryWrapper.eq("mid", mid);
		List<MaterialTags> list = materialTagsMapper.selectList(queryWrapper);
		if(list!=null && list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				MaterialTags item = list.get(i);
				strs+=(item.getTagid()+",");
			}
		}
		if(strs.contains(",")) {
			strs=strs.substring(0, strs.length()-1);
		}
		return strs;
	}

	@Override
	public List<MaterialCustomsItem> selectCustomsItemListById(String id) {
		QueryWrapper<MaterialCustomsItem> queryWrapper=new QueryWrapper<MaterialCustomsItem>();
		queryWrapper.eq("materialid", id);
		return materialCustomsItemMapper.selectList(queryWrapper);
	}

	@Override
	//@Cacheable(value = "inventoryByMskuCache#30",key="#key")
	public List<Map<String, Object>> findInventoryByMsku(PlanDTO dto,String key) {
		// TODO Auto-generated method stub
		if(StrUtil.isBlankOrUndefined(dto.getOwner())) {
			dto.setOwner(null);
		}
		if(StrUtil.isBlankOrUndefined(dto.getCategoryid())) {
			dto.setCategoryid(null);
		}
		if(StrUtil.isBlankOrUndefined(dto.getHasAddFee())) {
			dto.setHasAddFee(null);
		}
		return this.baseMapper.findInventoryByMsku(dto);
	}

	@Override
	public Material getBySku(String shopid, String sku) {
		// TODO Auto-generated method stub
		return  this.baseMapper.getMaterailBySku(shopid,sku);
		 
	}

	@Override
	@Cacheable(value = "materialListCache")
	public Map<String, String> getTagsIdsListByMsku(String shopid, List<String> mskulist) {
		Map<String, Object> param=new HashMap<String,Object>();
		param.put("shopid", shopid);
		param.put("skulist", mskulist);
		List<Map<String,String>> list=materialTagsMapper.getTagsBySku(param);
		Map<String,String> result=new HashMap<String,String>();
		for(Map<String,String> item:list) {
			result.put(item.get("sku"), item.get("tagids"));
		}
		return result;
	}
    public String getValue(Object value) {
    	return value==null?"":value.toString();
    }
	@Override
	public Workbook setMaterialExcelBook(Workbook workbook, MaterialDTO dto, UserInfo userinfo) {
		Map<String, Object> map = new HashMap<String, Object>();
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
		
		map.put("shopid", shopid);
		if(StrUtil.isNotBlank(dto.getMtype())) {
			if(dto.getMtype().equals("package")) {
				map.put("mtype",2);
			}else if(dto.getMtype().equals("consumable")) {
				map.put("mtype", 1);
			}else {
				map.put("mtype", 0);
			}
		}
		
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
		map.put("addressid", addressid);
		map.put("materialid", materialid);
		if("MaterialBaseInfo".equals(dto.getDowntype())
				||"MaterialBaseInfoConsumable".equals(dto.getDowntype())
				||"MaterialBaseInfoPackage".equals(dto.getDowntype())) {
		map.put("isdownload", "true");
		}
		List<Map<String, Object>> records =null;
		if("MaterialConsumablePlan".equals(dto.getDowntype())) {
			workbook = new SXSSFWorkbook();
			Sheet sheet = workbook.createSheet("Sheet1");
			Row row = sheet.createRow(0);
			Cell cell=row.createCell(0);
			cell.setCellValue("SKU");
			cell=row.createCell(1);
			cell.setCellValue("名称");
			cell=row.createCell(2);
			cell.setCellValue("品类");
			cell=row.createCell(3);
			cell.setCellValue("重量");
			cell=row.createCell(4);
			cell.setCellValue("长宽高");
			cell=row.createCell(5);
			cell.setCellValue("供应商");
			cell=row.createCell(6);
			cell.setCellValue("供货周期");
			cell=row.createCell(7);
			cell.setCellValue("仓库");
			cell=row.createCell(8);
			cell.setCellValue("可用库存");
			cell=row.createCell(9);
			cell.setCellValue("待入库");
			cell=row.createCell(10);
			cell.setCellValue("在途需求量");
			cell=row.createCell(11);
			cell.setCellValue("需求量");
			cell=row.createCell(12);
			cell.setCellValue("建议采购");
			cell=row.createCell(13);
			cell.setCellValue("计划数量");
			cell=row.createCell(14);
			cell.setCellValue("备注");
			records =  this.baseMapper.findConsumable(map);
			for(int i=0;records!=null&&i<records.size();i++) {
				Map<String, Object> item = records.get(i);
				  row = sheet.createRow(i + 1);
				  cell = row.createCell(0); // 在索引0的位置创建单元格(左上端) {100-9.9},{500-9.5}
				  cell.setCellValue(getValue(item.get("sku")));
				  cell = row.createCell(1);
				  cell.setCellValue(getValue(item.get("name")));
				  cell = row.createCell(2);
				  cell.setCellValue(getValue(item.get("category")));
				  cell = row.createCell(3);
				  cell.setCellValue(getValue(item.get("weight")));
				  cell = row.createCell(4);
				  cell.setCellValue(getValue(item.get("length"))+"*"+getValue(item.get("width"))+"*"+getValue(item.get("height")));
				  cell = row.createCell(5);
				  cell.setCellValue(getValue(item.get("supplier")));
				  cell = row.createCell(6);
				  cell.setCellValue(getValue(item.get("delivery_cycle")));
				  cell = row.createCell(7);
				  cell.setCellValue(getValue(item.get("warehousename")));
				  cell = row.createCell(8);
				  cell.setCellValue(getValue(item.get("fulfillable")));
				  cell = row.createCell(9);
				  cell.setCellValue(getValue(item.get("inbound")));
				  cell = row.createCell(10);
				  cell.setCellValue(getValue(item.get("needinboundamount")));
				  cell = row.createCell(11);
				  cell.setCellValue(getValue(item.get("needamount")));
				  cell = row.createCell(12);
				  cell.setCellValue(getValue(item.get("needamount")));
				  cell = row.createCell(13);
				  cell.setCellValue(getValue(item.get("suggest")));
				  cell = row.createCell(14);
				  cell.setCellValue(getValue(item.get("planamount")));
				  cell = row.createCell(15);
				  cell.setCellValue(getValue(item.get("remark")));
			}
			records=null;
			return workbook;
		}else {
			records= this.baseMapper.findMaterial(map);
		}
		
		if(records!=null && records.size()>0) {
			Sheet sheet = workbook.getSheet("Sheet1");
			//处理组装关系数据
			if("MaterialAssembly".equals(dto.getDowntype())) {
				for(int i=0;i<records.size();i++) {
					Map<String, Object> item = records.get(i);
					String sku=item.get("sku").toString();
					String mid=item.get("id").toString();
					List<AssemblyVO> assemblyList = assemblyService.selectByMainmid(mid);
					Row row = sheet.createRow(i + 2);
					Cell cell = row.createCell(0); // 在索引0的位置创建单元格(左上端) {A001:2},{A002:1}
					cell.setCellValue(sku);
					Cell cell2 = row.createCell(1);
					String strs="";
					if(assemblyList!=null && assemblyList.size()>0) {
						for (int j = 0; j < assemblyList.size(); j++) {
							AssemblyVO assvo = assemblyList.get(j);
							strs+=("{"+assvo.getSku()+":"+assvo.getSubnumber()+("},"));
						}
					}else {
						strs="暂无";
					}
					cell2.setCellValue(strs);
				}
			}
			//处理海关关系数据
			if("MaterialCustoms".equals(dto.getDowntype())) {
				for(int i=0;i<records.size();i++) {
					Map<String, Object> item = records.get(i);
					String sku=item.get("sku").toString();
					String mid=item.get("id").toString();
					List<MaterialCustomsItem> customsItemList=this.selectCustomsItemListById(mid);
					MaterialCustoms Customs=this.selectCustomsByMaterialId(mid);
					Row row = sheet.createRow(i + 1);
					Cell cell = row.createCell(0); 
					cell.setCellValue(sku);
					if(Customs!=null) {
						Cell cell2 = row.createCell(1);
						cell2.setCellValue(Customs.getNameCn());
						Cell cell3 = row.createCell(2);
						cell3.setCellValue(Customs.getNameEn());
						Cell cell4 = row.createCell(3);
						cell4.setCellValue(Customs.getMaterial());
						Cell cell5 = row.createCell(4);
						cell5.setCellValue(Customs.getModel());
						Cell cell6 = row.createCell(5);
						cell6.setCellValue(Customs.getMaterialUse());
						Cell cell7 = row.createCell(6);
						cell7.setCellValue(Customs.getBrand());
						Cell cell8 = row.createCell(7);
						if(Customs.getIselectricity()==true) {
							cell8.setCellValue("是");
						}else {
							cell8.setCellValue("否");
						}
						Cell cell9 = row.createCell(8);
						if(Customs.getIsdanger()==true) {
							cell9.setCellValue("是");
						}else {
							cell9.setCellValue("否");
						}
						Cell cell10 = row.createCell(9);
						if(Customs.getUnitprice()!=null) {
							cell10.setCellValue(Customs.getUnitprice().toString());
						}
						Cell cell11 = row.createCell(10);
						if(Customs.getAddfee()!=null) {
							cell11.setCellValue(Customs.getAddfee().toString());
						}
					}
					Cell cell12 = row.createCell(11);
					String strs="";
					// {UK-12344AA-9.99-18} 国家-编码-费用-税率
					if(customsItemList!=null && customsItemList.size()>0) {
						for (int j = 0; j < customsItemList.size(); j++) {
							MaterialCustomsItem consitem = customsItemList.get(j);
							strs+=("{"+consitem.getCountry()+"-"+consitem.getCode()+"-"+consitem.getFee()+"-"+consitem.getTaxrate()+("},"));
						}
					}else {
						strs="";
					}
					cell12.setCellValue(strs);
				}
			}
			//处理基础数据
			if("MaterialBaseInfo".equals(dto.getDowntype())
					||"MaterialBaseInfoConsumable".equals(dto.getDowntype())
					||"MaterialBaseInfoPackage".equals(dto.getDowntype())) {
				for(int i=0;i<records.size();i++) {
					Map<String, Object> item = records.get(i);
					String sku=item.get("sku").toString();
					Row row = sheet.createRow(i + 2);
					Cell cell = row.createCell(0); 
					cell.setCellValue(sku);
						Cell cell2 = row.createCell(1);
						cell2.setCellValue(item.get("name").toString());
						Cell cell3 = row.createCell(2);
						if(item.get("price")!=null) {
							cell3.setCellValue(item.get("price").toString());
						}
						 
						if(item.get("length")!=null) {
							Cell cell4 = row.createCell(3);
							cell4.setCellValue(item.get("length").toString());
						}
						if(item.get("width")!=null) {
							Cell cell5 = row.createCell(4);
							cell5.setCellValue(item.get("width").toString());
						}
						if(item.get("height")!=null) {
							Cell cell6 = row.createCell(5);
							cell6.setCellValue(item.get("height").toString());
						}
						if(item.get("weight")!=null) {
							Cell cell7 = row.createCell(6);
							cell7.setCellValue(item.get("weight").toString());
						}
						 
						if(item.get("ilength")!=null) {
							Cell cell8 = row.createCell(7);
							cell8.setCellValue(item.get("ilength").toString());
						}
						if(item.get("iwidth")!=null) {
							Cell cell9 = row.createCell(8);
							cell9.setCellValue(item.get("iwidth").toString());
						}
						if(item.get("iheight")!=null) {
							Cell cell10 = row.createCell(9);
							cell10.setCellValue(item.get("iheight").toString());
						}
						if(item.get("iweight")!=null) {
							Cell cell11 = row.createCell(10);
							cell11.setCellValue(item.get("iweight").toString());
						}
						
						if(item.get("blength")!=null) {
							Cell cell12 = row.createCell(11);
							cell12.setCellValue(item.get("blength").toString());
						}
						if(item.get("bwidth")!=null) {
							Cell cell13 = row.createCell(12);
							cell13.setCellValue(item.get("bwidth").toString());
						}
						if(item.get("bheight")!=null) {
							Cell cell14 = row.createCell(13);
							cell14.setCellValue(item.get("bheight").toString());
						}
						if(item.get("bweight")!=null) {
							Cell cell15 = row.createCell(14);
							cell15.setCellValue(item.get("bweight").toString());
						}
						  
						Cell cell16 = row.createCell(15);
						if(item.get("boxnum")!=null) {
							cell16.setCellValue(item.get("boxnum").toString());
						}
						Cell cell17 = row.createCell(16);
						if(item.get("brand")!=null) {
							cell17.setCellValue(item.get("brand").toString());
						}
						Cell cell18 = row.createCell(17);
						if(item.get("other_cost")!=null) {
							cell18.setCellValue(item.get("other_cost").toString());
						}
						Cell cell19 = row.createCell(18);
						if(item.get("category")!=null) {
							 cell19.setCellValue(item.get("category").toString());
						}
						Cell cell20 = row.createCell(19);
						cell20.setCellValue(item.get("specification")!=null?item.get("specification").toString():null);
						
						Cell cell21 = row.createCell(20);
						cell21.setCellValue(item.get("remark")!=null?item.get("remark").toString():null);
						Cell cell22 = row.createCell(21);
						if(item.get("createdate")!=null) {
							cell22.setCellValue(GeneralUtil.formatDate(GeneralUtil.getDate(item.get("createdate")), "yyyy-MM-dd"));
						}else {
							cell22.setCellValue("");
						}
						Cell cell23 = row.createCell(22);
						if(item.get("ownername")!=null) {
							cell23.setCellValue(item.get("ownername").toString());
						}else {
							cell23.setCellValue("");
						}
				}
			}
			//处理耗材关系数据
			if("MaterialConsumable".equals(dto.getDowntype())) {
				int rownum=1;
				for(int i=0;i<records.size();i++) {
					Map<String, Object> item = records.get(i);
					String sku=item.get("sku").toString();
					String mid=item.get("id").toString();
					List<MaterialConsumableVO> consumableList =  iMaterialConsumableService.selectConsumableByMainmid(mid,shopid);
					//当有耗材清单时 且清单>1 rownum要++
					if(consumableList!=null && consumableList.size()>0) {
							for (int j = 0; j < consumableList.size(); j++) {
								MaterialConsumableVO vo = consumableList.get(j);
								Row conrow = sheet.createRow(rownum++);
								Cell concell = conrow.createCell(0); 
								concell.setCellValue(sku);
								Cell concell2 = conrow.createCell(1); 
								concell2.setCellValue(vo.getSku());
								Cell concell3 = conrow.createCell(2); 
								if(vo.getAmount()!=null) {
									concell3.setCellValue(vo.getAmount().doubleValue());
								}
								
							}
					} 
					
				}
			}
			//处理供应商关系数据
			if("MaterialSupplier".equals(dto.getDowntype())) {
				for(int i=0;i<records.size();i++) {
					Map<String, Object> item = records.get(i);
					String sku=item.get("sku").toString();
					String mid=item.get("id").toString();
					List<MaterialSupplierVO> lists =iIMaterialSupplierService.selectSupplierByMainmid(mid);
					MaterialSupplierVO listvo=null;
					if(lists!=null && lists.size()>0) {
						for (int k = 0; k < lists.size(); k++) {
							if(lists.get(k).getIsdefault()==true) {
								listvo=lists.get(k);
							}
						}
					}
					Row row = sheet.createRow(i + 1);
					Cell cell = row.createCell(0); // 在索引0的位置创建单元格(左上端) {100-9.9},{500-9.5}
					cell.setCellValue(sku);
					if(listvo==null) {
						continue;
					}
					Cell cell2 = row.createCell(1);
					cell2.setCellValue(listvo.getName());
					Cell cell3 = row.createCell(2);
					cell3.setCellValue(listvo.getProductCode());
					Cell cell4 = row.createCell(3);
					cell4.setCellValue(listvo.getPurchaseUrl());
					Cell cell5 = row.createCell(4);
					if(listvo.getOtherCost()!=null) {
						cell5.setCellValue(listvo.getOtherCost().toString());
					}
					Cell cell6 = row.createCell(5);
					if(listvo.getDeliverycycle()!=null) {
						cell6.setCellValue(listvo.getDeliverycycle());
					}
					Cell cell7 = row.createCell(6);
					if(listvo.getBadrate()!=null) {
						cell7.setCellValue(listvo.getBadrate().toString());
					}
					Cell cell8 = row.createCell(7);
					cell8.setCellValue(listvo.getMOQ());
					Cell cell9 = row.createCell(8);
					String strs="";
					if(listvo.getStepList()!=null && listvo.getStepList().size()>0) {
						for (int j = 0; j < listvo.getStepList().size(); j++) {
							MaterialSupplierStepwise vo = listvo.getStepList().get(j);
							strs+=("{"+vo.getAmount()+"-"+vo.getPrice()+("},"));
						}
					}else {
						strs="无";
					}
					cell9.setCellValue(strs);
				}
			}
		}
		return workbook;
		
	}

	@Override
	public Map<String,Object> getMaterialInfoBySkuList(PlanDTO dto){
		// TODO Auto-generated method stub
		 List<MaterialVO> list = this.baseMapper.getMaterialInfoBySkuList(dto);
		 Map<String,Object> result=new HashMap<String,Object>();
		 if(list!=null&&list.size()>0) {
			 for(MaterialVO item:list) {
				 result.put(item.getSku(), item);
			 }
		 }
		 return result;
	}

	@Override
	public void updateMaterialType(UserInfo user, String ftype, List<MaterialVO> volist) {
		if(volist!=null && volist.size()>0) {
			for (MaterialVO item:volist) {
				if(item.getId()!=null) {
					Material material = this.baseMapper.selectById(item.getId());
					String oldowner=material.getOwner();
					if(material!=null) {
						if("date".equals(ftype)) {
							material.setEffectivedate(GeneralUtil.getDate(item.getEffectivedate()));
						}
						if("cycle".equals(ftype)) {
							material.setDeliveryCycle(item.getDeliveryCycle());
						}
						if("owner".equals(ftype)) {
							material.setOwner(item.getOwner());
						}
						if("supplier".equals(ftype)) {
							material.setSupplier(item.getSupplier());
						}
						if("remark".equals(ftype)) {
							material.setRemark(item.getRemark());
						}
						if("cost".equals(ftype)) {
							material.setPrice(item.getPrice());
						}
						material.setOperator(user.getId());
						this.baseMapper.updateById(material);
						if("owner".equals(ftype)) {
							 try {
									//同步修改产品的负责人
									 if(material.getOwner()!=null) {
										 amazonClientOneFeign.updateProductOwner(material.getSku(),material.getOwner(),oldowner);
									 }
								 }catch(Exception e) {
									 e.printStackTrace();
								 }
						}
					}
				}
				
			}
		}
		
		
	}

	@Override
	public void syncProductList(String shopid) {
		// TODO Auto-generated method stub
		this.baseMapper.syncProductList(shopid);
	}

}
