package com.wimoor.erp.common.service.impl;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.wimoor.erp.warehouse.service.IWarehouseService;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.api.AmazonClientOneFeignManager;
import com.wimoor.erp.assembly.mapper.AssemblyMapper;
import com.wimoor.erp.assembly.pojo.entity.Assembly;
import com.wimoor.erp.common.service.IExcelDownLoadService;
import com.wimoor.erp.customer.mapper.CustomerMapper;
import com.wimoor.erp.customer.pojo.entity.Customer;
import com.wimoor.erp.material.mapper.MaterialConsumableMapper;
import com.wimoor.erp.material.mapper.MaterialCustomsMapper;
import com.wimoor.erp.material.mapper.MaterialMapper;
import com.wimoor.erp.material.mapper.MaterialSupplierMapper;
import com.wimoor.erp.material.mapper.MaterialSupplierStepwiseMapper;
import com.wimoor.erp.material.pojo.entity.DimensionsInfo;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.pojo.entity.MaterialBrand;
import com.wimoor.erp.material.pojo.entity.MaterialCategory;
import com.wimoor.erp.material.pojo.entity.MaterialCustoms;
import com.wimoor.erp.material.pojo.entity.MaterialSupplier;
import com.wimoor.erp.material.pojo.entity.MaterialSupplierStepwise;
import com.wimoor.erp.material.pojo.vo.MaterialConsumableVO;
import com.wimoor.erp.material.service.IDimensionsInfoService;
import com.wimoor.erp.material.service.IMaterialBrandService;
import com.wimoor.erp.material.service.IMaterialCategoryService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

@Service("excelDownLoadService")
@RequiredArgsConstructor
public class ExcelDownLoadServiceImpl implements IExcelDownLoadService{
	final CustomerMapper customerMapper;
	final ISerialNumService serialNumService;
	final MaterialMapper materialMapper;
	final IDimensionsInfoService dimensionsInfoService;
	final IMaterialBrandService materialBrandService;
	final IMaterialCategoryService materialCategoryService;
	final MaterialSupplierMapper materialSupplierMapper;
	final MaterialSupplierStepwiseMapper materialSupplierStepwiseMapper;
	final MaterialConsumableMapper materialConsumableMapper;
	final MaterialCustomsMapper materialCustomsMapper;
	final AssemblyMapper assemblyMapper;
	final IWarehouseService warehouseService;
	final AmazonClientOneFeignManager amazonClientOneFeign;
	@Override
	public void uploadMaterialFile(UserInfo user, InputStream inputStream, Row info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void uploadCustomerFile(UserInfo user, Row info) {
			String name = null;
			if (info.getCell(0) != null) {
				name = info.getCell(0).getStringCellValue();
			}
			String ftype = null;
			if (info.getCell(1) != null) {
				ftype = info.getCell(1).getStringCellValue();
			}
			String contacts = null;
			if (info.getCell(2) != null) {
				contacts = info.getCell(2).getStringCellValue();
			}
			String address = null;
			if (info.getCell(3) != null) {
				address = info.getCell(3).getStringCellValue();
			}
			String phone = null;
			if (info.getCell(4) != null) {
				info.getCell(4).setCellType(CellType.STRING);
				phone = info.getCell(4).getStringCellValue();
			}
			String otherContact = null;
			if (info.getCell(5) != null) {
				info.getCell(5).setCellType(CellType.STRING);
				otherContact = info.getCell(5).getStringCellValue();
			}

			if (StrUtil.isEmpty(name) || StrUtil.isEmpty(contacts)
					|| StrUtil.isEmpty(address)) {
				throw new BizException("Excel文件中必填字段为空！");
			}
			if ("供应商".equals(ftype)) {
				ftype = "supplier";
			} else if ("采购商".equals(ftype)) {
				ftype = "purchaser";
			} else {
				throw new BizException("客户分类" + ftype + "填写错误，应该是供应商或者采购商！");
			}
			QueryWrapper<Customer> queryWrapper=new QueryWrapper<Customer>();
			queryWrapper.eq("shopid", user.getCompanyid());
			queryWrapper.eq("name", name);
			List<Customer> customerlist = customerMapper.selectList(queryWrapper);
			Customer customer=null;
			if(customerlist!=null && customerlist.size()>0) {
				customer=customerlist.get(0);
			}
			if (customer == null) {
				customer = new Customer();
				customer.setName(name);
				try {
					customer.setNumber(serialNumService.readSerialNumber(user.getCompanyid(), "K"));
				} catch (Exception e) {
					e.printStackTrace();
					throw new BizException("系统编码错误，请重新导入！");
				}
				customer.setFtype(ftype);
				customer.setContacts(contacts);
				customer.setPhone_num(phone.toString());
				customer.setContact_info(otherContact);
				customer.setAddress(address);
				customer.setOperator(user.getId());
				customer.setShopid(user.getCompanyid());
				customer.setOpttime(new Date());
				customerMapper.insert(customer);
			} else {
				customer.setName(name);
				customer.setFtype(ftype);
				customer.setContacts(contacts);
				customer.setPhone_num(phone.toString());
				customer.setContact_info(otherContact);
				customer.setAddress(address);
				customer.setOperator(user.getId());
				customer.setShopid(user.getCompanyid());
				customer.setOpttime(new Date());
				customerMapper.updateById(customer);
			}
	}

	@Override
	public void uploadCategoryFile(UserInfo user, InputStream inputStream, Row info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SXSSFWorkbook downloadExcel(List<Map<String, Object>> list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SXSSFWorkbook downloaderrorExcel(List<String> list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void uploadMskuFile(UserInfo user, InputStream inputStream, Row info, String marketplaceid, String groupid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void uploadOtherfeeFile(UserInfo user, InputStream inputStream, Row info, String marketplaceid,
			String groupid, String itemid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void uploadFinItemDataFile(UserInfo user, InputStream inputStream, Row info, String marketplaceid,
			String groupid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getExcelStockAllInfoReport(SXSSFWorkbook workbook, Map<String, Object> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void uploadStockTakingFile(UserInfo user, Workbook workbook, String stockid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void uploadShipmentTransFile(UserInfo user, InputStream inputStream, String companyid, String fileName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void uploadMaterialcustomsFile(UserInfo user, InputStream inputStream, String stockid, String fileName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void uploadShipmentcustomsFile(UserInfo user, InputStream inputStream, String fileName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void uploadMaterialBaseInfoFile(UserInfo user, Row info,String mtype, Map<String, String> ownermap) {
		String sku = null;
		if (info.getCell(0) != null) {
			info.getCell(0).setCellType(CellType.STRING);
			sku = info.getCell(0).getStringCellValue();
		}
		String name = null;
		if (info.getCell(1) != null) {
			info.getCell(1).setCellType(CellType.STRING);
			name = info.getCell(1).getStringCellValue();
		}
		String price = null;
		if (info.getCell(2) != null) {
			info.getCell(2).setCellType(CellType.STRING);
			price = info.getCell(2).getStringCellValue();
		}
		String pkglength = null;
		if (info.getCell(3) != null) {
			info.getCell(3).setCellType(CellType.STRING);
			pkglength = info.getCell(3).getStringCellValue();
		}
		String pkgwidth = null;
		if (info.getCell(4) != null) {
			info.getCell(4).setCellType(CellType.STRING);
			pkgwidth = info.getCell(4).getStringCellValue();
		}
		String pkgheight = null;
		if (info.getCell(5) != null) {
			info.getCell(5).setCellType(CellType.STRING);
			pkgheight = info.getCell(5).getStringCellValue();
		}
		String pkgweight = null;
		if (info.getCell(6) != null) {
			info.getCell(6).setCellType(CellType.STRING);
			pkgweight = info.getCell(6).getStringCellValue();
		}
		String itemlength = null;
		if (info.getCell(7) != null) {
			info.getCell(7).setCellType(CellType.STRING);
			itemlength = info.getCell(7).getStringCellValue();
		}
		String itemwidth = null;
		if (info.getCell(8) != null) {
			info.getCell(8).setCellType(CellType.STRING);
			itemwidth = info.getCell(8).getStringCellValue();
		}
		String itemheight = null;
		if (info.getCell(9) != null) {
			info.getCell(9).setCellType(CellType.STRING);
			itemheight = info.getCell(9).getStringCellValue();
		}
		String itemweight = null;
		if (info.getCell(10) != null) {
			info.getCell(10).setCellType(CellType.STRING);
			itemweight = info.getCell(10).getStringCellValue();
		}
		String boxlength = null;
		if (info.getCell(11) != null) {
			info.getCell(11).setCellType(CellType.STRING);
			boxlength = info.getCell(11).getStringCellValue();
		}
		String boxwidth = null;
		if (info.getCell(12) != null) {
			info.getCell(12).setCellType(CellType.STRING);
			boxwidth = info.getCell(12).getStringCellValue();
		}
		String boxheight = null;
		if (info.getCell(13) != null) {
			info.getCell(13).setCellType(CellType.STRING);
			boxheight = info.getCell(13).getStringCellValue();
		}
		String boxweight = null;
		if (info.getCell(14) != null) {
			info.getCell(14).setCellType(CellType.STRING);
			boxweight = info.getCell(14).getStringCellValue();
		}
		String boxnum = null;
		if (info.getCell(15) != null) {
			info.getCell(15).setCellType(CellType.STRING);
			boxnum = info.getCell(15).getStringCellValue();
		}
		String brandname = null;
		if (info.getCell(16) != null) {
			brandname = info.getCell(16).getStringCellValue();
		}
		String othercost = null;
		if (info.getCell(17) != null) {
			info.getCell(17).setCellType(CellType.STRING);
			othercost = info.getCell(17).getStringCellValue();
		}
		String categoryname = null;
		if (info.getCell(18) != null) {
			categoryname = info.getCell(18).getStringCellValue();
		}
		String specification = null;
		if (info.getCell(19) != null) {
			info.getCell(19).setCellType(CellType.STRING);
			specification = info.getCell(19).getStringCellValue();
		}
		String remark = null;
		if (info.getCell(20) != null) {
			info.getCell(20).setCellType(CellType.STRING);
			remark = info.getCell(20).getStringCellValue();
		}
		String effectivedate = null;
		if (info.getCell(21) != null) {
			info.getCell(21).setCellType(CellType.STRING);
			effectivedate = info.getCell(21).getStringCellValue();
		}
		String owner = null;
		if (info.getCell(22) != null) {
			info.getCell(22).setCellType(CellType.STRING);
			owner = info.getCell(22).getStringCellValue();
		}
		if (StrUtil.isEmpty(sku)) {
			throw new BizException("Excel文件中必填字段为空！");
		}else {
			sku=sku.trim();
		}
		Material oldmaterial = getMaterialById(user.getCompanyid(), sku);
		Material material=new Material();
		String oldowner=null;
		if(oldmaterial!=null) {
			material=oldmaterial;
			oldowner=oldmaterial.getOwner();
		}
		material.setShopid(user.getCompanyid());
		material.setSku(sku);
		material.setName(name);
		if("product".equals(mtype)) {
			material.setMtype(0);
		}else if("consumable".equals(mtype)) {
			material.setMtype(1);
		}else if("package".equals(mtype)) {
			material.setMtype(2);
		}
		if(StrUtil.isNotEmpty(price)) {
			material.setPrice(new BigDecimal(price));
		}
		if(StrUtil.isNotEmpty(pkglength) && StrUtil.isNotEmpty(pkgwidth) && StrUtil.isNotEmpty(pkgheight) && StrUtil.isNotEmpty(pkgweight)) {
			BigDecimal length = new BigDecimal(pkglength);
			BigDecimal width = new BigDecimal(pkgwidth);
			BigDecimal height = new BigDecimal(pkgheight);
			BigDecimal weight = new BigDecimal(pkgweight);
			DimensionsInfo dim = new DimensionsInfo();
			dim.setLength(length);
			dim.setWidth(width);
			dim.setHeight(height);
			dim.setWeight(weight);
			if(material.getPkgdimensions()!=null) {
				DimensionsInfo old=dimensionsInfoService.getById(material.getPkgdimensions());
				if(old!=null) {
					dim.setId(old.getId());
					dimensionsInfoService.updateById(dim);
				}else {
					dimensionsInfoService.save(dim);
				}
				
			}else {
				dimensionsInfoService.save(dim);
			}
			material.setPkgdimensions(dim.getId());
		}
		if(StrUtil.isNotEmpty(itemlength) && StrUtil.isNotEmpty(itemwidth) && StrUtil.isNotEmpty(itemheight) && StrUtil.isNotEmpty(itemweight)) {
			BigDecimal length = new BigDecimal(itemlength);
			BigDecimal width = new BigDecimal(itemwidth);
			BigDecimal height = new BigDecimal(itemheight);
			BigDecimal weight = new BigDecimal(itemweight);
			DimensionsInfo dim = new DimensionsInfo();
			dim.setLength(length);
			dim.setWidth(width);
			dim.setHeight(height);
			dim.setWeight(weight);
			if(material.getItemdimensions()!=null) {
				DimensionsInfo old=dimensionsInfoService.getById(material.getItemdimensions());
				if(old!=null) {
					dim.setId(old.getId());
					dimensionsInfoService.updateById(dim);
				}else {
					dimensionsInfoService.save(dim);
				}
				
			}else {
				dimensionsInfoService.save(dim);
			}
			material.setItemdimensions(dim.getId());
		}
		if(StrUtil.isNotEmpty(boxlength) && StrUtil.isNotEmpty(boxwidth) && StrUtil.isNotEmpty(boxheight) && StrUtil.isNotEmpty(boxweight)) {
			BigDecimal length = new BigDecimal(boxlength);
			BigDecimal width = new BigDecimal(boxwidth);
			BigDecimal height = new BigDecimal(boxheight);
			BigDecimal weight = new BigDecimal(boxweight);
			DimensionsInfo dim = new DimensionsInfo();
			dim.setLength(length);
			dim.setWidth(width);
			dim.setHeight(height);
			dim.setWeight(weight);
			if(material.getBoxdimensions()!=null) {
				DimensionsInfo old=dimensionsInfoService.getById(material.getBoxdimensions());
				if(old!=null) {
					dim.setId(old.getId());
					dimensionsInfoService.updateById(dim);
				}else {
					dimensionsInfoService.save(dim);
				}
				
			}else {
				dimensionsInfoService.save(dim);
			}
			material.setBoxdimensions(dim.getId());
		}
		if(StrUtil.isNotEmpty(boxnum)) {
			material.setBoxnum(Integer.parseInt(boxnum));
		}
		if(StrUtil.isNotEmpty(brandname)) {
			QueryWrapper<MaterialBrand> brandqueryWrapper=new QueryWrapper<MaterialBrand>();
			brandqueryWrapper.eq("shopid", user.getCompanyid());
			brandqueryWrapper.eq("name", brandname);
			List<MaterialBrand> brandlist = materialBrandService.list(brandqueryWrapper);
			if(brandlist!=null && brandlist.size()>0) {
				material.setBrand(brandlist.get(0).getId());
			}else {
				material.setBrand(null);
			}
		}
		if(StrUtil.isNotEmpty(categoryname)) {
			QueryWrapper<MaterialCategory> catequeryWrapper=new QueryWrapper<MaterialCategory>();
			catequeryWrapper.eq("shopid", user.getCompanyid());
			catequeryWrapper.eq("name", categoryname);
			List<MaterialCategory> catelist = materialCategoryService.list(catequeryWrapper);
			if(catelist!=null && catelist.size()>0) {
				material.setCategoryid(catelist.get(0).getId());
			}else {
				material.setCategoryid(null);
			}
		}
		if(StrUtil.isNotEmpty(othercost)) {
			material.setOtherCost(new BigDecimal(othercost));
		}
		if(StrUtil.isNotEmpty(effectivedate)) {
			material.setEffectivedate(GeneralUtil.getDatez(effectivedate));
		}
		if(StrUtil.isNotBlank(owner)&&ownermap!=null) {
			String ownerid = ownermap.get(owner);
			if(StrUtil.isNotBlank(ownerid)) {
				material.setOwner(ownerid);
			}else {
				material.setOwner(user.getId());
			}
		}else {
			material.setOwner(user.getId());
		}
		material.setRemark(remark);
		material.setOperator(user.getId());
		material.setOpttime(new Date());
		material.setSpecification(specification);
		if(StrUtil.isNotEmpty(material.getId())) {
			materialMapper.updateById(material);
		}else {
			material.setCreatedate(new Date());
			materialMapper.insert(material);
		}
		 try {
				//同步修改产品的负责人
				 if(material.getOwner()!=null) {
					 amazonClientOneFeign.updateProductOwner(material.getSku(),material.getOwner(),oldowner);
				 }
			 }catch(Exception e) {
				 e.printStackTrace();
			 }
	}

	@Override
	public void uploadMaterialSupplierFile(UserInfo user, Row info) {
		String sku=null;
		if (info.getCell(0) != null) {
			info.getCell(0).setCellType(CellType.STRING);
			sku = info.getCell(0).getStringCellValue();
		}
		String suppliername=null;
		if (info.getCell(1) != null) {
			info.getCell(1).setCellType(CellType.STRING);
			suppliername = info.getCell(1).getStringCellValue();
		}
		QueryWrapper<Customer> queryWrapper=new QueryWrapper<Customer>();
		queryWrapper.eq("shopid", user.getCompanyid());
		queryWrapper.eq("name", suppliername);
		List<Customer> supplierlist = customerMapper.selectList(queryWrapper);
		if(supplierlist!=null && supplierlist.size()>0) {
			Customer supplier = supplierlist.get(0);
			String supid=supplier.getId();
			Material material = getMaterialById(user.getCompanyid(),sku);
			if(material!=null) {
				//先查询所有的供应商列表  先删除关系 再添加
				QueryWrapper<MaterialSupplier> query=new QueryWrapper<MaterialSupplier>();
				query.eq("materialid", material.getId());
				List<MaterialSupplier> suplist = materialSupplierMapper.selectList(query);
				if(suplist!=null && suplist.size()>0) {
					for(MaterialSupplier entity:suplist) {
						entity.setIsdefault(false);
						materialSupplierMapper.updateById(entity);
					}
				}
				String procode=null;
				if (info.getCell(2) != null) {
					info.getCell(2).setCellType(CellType.STRING);
					procode = info.getCell(2).getStringCellValue();
				}
				String purchaseurl=null;
				if (info.getCell(3) != null) {
					info.getCell(3).setCellType(CellType.STRING);
					purchaseurl = info.getCell(3).getStringCellValue();
				}
				String othercost=null;
				if (info.getCell(4) != null) {
					info.getCell(4).setCellType(CellType.STRING);
					othercost = info.getCell(4).getStringCellValue();
				}
				String deliverycycle=null;
				if (info.getCell(5) != null) {
					info.getCell(5).setCellType(CellType.STRING);
					deliverycycle = info.getCell(5).getStringCellValue();
				}
				String badrate=null;
				if (info.getCell(6) != null) {
					info.getCell(6).setCellType(CellType.STRING);
					badrate = info.getCell(6).getStringCellValue();
				}
				String moq=null;
				if (info.getCell(7) != null) {
					info.getCell(7).setCellType(CellType.STRING);
					moq = info.getCell(7).getStringCellValue();
				}
				//再插入新纪录
				MaterialSupplier entity=new MaterialSupplier();
				entity.setSupplierid(supid);
				entity.setPurchaseurl(purchaseurl);
				entity.setProductcode(procode);
				if(StrUtil.isNotEmpty(othercost))entity.setOthercost(new BigDecimal(othercost));
				entity.setOpttime(new Date());
				entity.setOperator(user.getId());
				if(StrUtil.isNotEmpty(moq))entity.setMOQ(Integer.parseInt(moq));
				entity.setMaterialid(material.getId());
				entity.setIsdefault(true);
				if(StrUtil.isNotEmpty(deliverycycle))entity.setDeliverycycle(Integer.parseInt(deliverycycle));
				entity.setCreater(user.getId());
				entity.setCreatedate(new Date());
				if(StrUtil.isNotEmpty(badrate))entity.setBadrate(Float.parseFloat(badrate));
				
				QueryWrapper<MaterialSupplier> mqueryWrapper=new QueryWrapper<MaterialSupplier>();
				mqueryWrapper.eq("materialid", material.getId());
				mqueryWrapper.eq("supplierid", supid);
				MaterialSupplier one = materialSupplierMapper.selectOne(mqueryWrapper);
				if(one!=null) {
					entity.setId(one.getId());
					materialSupplierMapper.updateById(entity);
				}else {
					materialSupplierMapper.insert(entity);
				}
				material.setSupplier(supid);
				material.setPurchaseUrl(purchaseurl);
				material.setDeliveryCycle(entity.getDeliverycycle());
				material.setProductCode(procode);
				materialMapper.updateById(material);
				//插入后删除 一模一样的备选供应商 是0的
			
				//step price同理 先删除后添加
				QueryWrapper<MaterialSupplierStepwise> stepqueryWrapper=new QueryWrapper<MaterialSupplierStepwise>();
				stepqueryWrapper.eq("materialid", material.getId());
				stepqueryWrapper.eq("supplierid", supid);
				materialSupplierStepwiseMapper.delete(stepqueryWrapper);
				String pricelist=null;//{100-9.9},{200-9.5}
				if (info.getCell(8) != null) {
					info.getCell(8).setCellType(CellType.STRING);
					pricelist = info.getCell(8).getStringCellValue();
				}
				if(StrUtil.isNotEmpty(pricelist)&& pricelist.contains("{")) {
					String[] stepArr = pricelist.split(",");
					if(stepArr.length>0) {
						for (int i = 0; i < stepArr.length; i++) {
							String item = stepArr[i];
							item=item.replace("{", "");
							item=item.replace("}", "");
							String amount = item.split("-")[0];
							String price=item.split("-")[1];
							MaterialSupplierStepwise step=new MaterialSupplierStepwise();
							step.setCurrency("0");
							if(StrUtil.isNotEmpty(amount))step.setAmount(Integer.parseInt(amount));
							if(StrUtil.isNotEmpty(price))step.setPrice(new BigDecimal(price));
							step.setMaterialid(material.getId());
							step.setSupplierid(supid);
							step.setOperator(user.getId());
							step.setOpttime(new Date());
							materialSupplierStepwiseMapper.insert(step);
						}
					}
				}
				
			}
		}
	}

	//备选供应商
	@Override
	public void uploadMaterialMoreSupplierFile(UserInfo user, Row info) {
		String sku=null;
		if (info.getCell(0) != null) {
			info.getCell(0).setCellType(CellType.STRING);
			sku = info.getCell(0).getStringCellValue();
		}
		String suppliername=null;
		if (info.getCell(1) != null) {
			info.getCell(1).setCellType(CellType.STRING);
			suppliername = info.getCell(1).getStringCellValue();
		}
		QueryWrapper<Customer> queryWrapper=new QueryWrapper<Customer>();
		queryWrapper.eq("shopid", user.getCompanyid());
		queryWrapper.eq("name", suppliername);
		List<Customer> supplierlist = customerMapper.selectList(queryWrapper);
		//查询是否存在当前供应商
		String supid=null;
		if(supplierlist!=null && supplierlist.size()>0) {
			Customer supplier = supplierlist.get(0);
			 supid=supplier.getId();
			if (info.getCell(9) != null) {
				info.getCell(9).setCellType(CellType.STRING);
				String concat = info.getCell(9).getStringCellValue();
				if(concat!=null && StrUtil.isNotBlank(concat.trim())){
					supplier.setContacts(concat);
				}
			}
			if (info.getCell(10) != null) {
				info.getCell(10).setCellType(CellType.STRING);
				String phone = info.getCell(10).getStringCellValue();
				if(phone!=null && StrUtil.isNotBlank(phone.trim())){
					supplier.setPhone_num(phone);
				}
			}
			if (info.getCell(11) != null) {
				info.getCell(11).setCellType(CellType.STRING);
				String address = info.getCell(11).getStringCellValue();
				if(address!=null && StrUtil.isNotBlank(address.trim())){
					supplier.setAddress(address);
				}
			}
			supplier.setShopid(user.getCompanyid());
			customerMapper.updateById(supplier);
		}else{
			Customer supplier = new Customer();
			supid = warehouseService.getUUID();
			String number=null;
			try {
				number = (serialNumService.findSerialNumber(user.getCompanyid(), "K"));
			} catch (Exception e) {
				e.printStackTrace();
				throw new BizException("编码获取失败,请联系管理员");
			}
			supplier.setNumber(number);
			supplier.setName(suppliername);
			supplier.setFtype("supplier");
			supplier.setOperator(user.getId());
			supplier.setOpttime(new Date());
			supplier.setShopid(user.getCompanyid());
			supplier.setId(supid);
			if (info.getCell(9) != null) {
				info.getCell(9).setCellType(CellType.STRING);
				String concat = info.getCell(9).getStringCellValue();
				if(concat!=null && StrUtil.isNotBlank(concat.trim())){
					supplier.setContacts(concat);
				}
			}
			if (info.getCell(10) != null) {
				info.getCell(10).setCellType(CellType.STRING);
				String phone = info.getCell(10).getStringCellValue();
				if(phone!=null && StrUtil.isNotBlank(phone.trim())){
					supplier.setPhone_num(phone);
				}
			}
			if (info.getCell(11) != null) {
				info.getCell(11).setCellType(CellType.STRING);
				String address = info.getCell(11).getStringCellValue();
				if(address!=null && StrUtil.isNotBlank(address.trim())){
					supplier.setAddress(address);
				}
			}
			customerMapper.insert(supplier);
		}
			Material material = getMaterialById(user.getCompanyid(),sku);
			if(material!=null) {
				String procode=null;
				if (info.getCell(2) != null) {
					info.getCell(2).setCellType(CellType.STRING);
					procode = info.getCell(2).getStringCellValue();
				}
				String purchaseurl=null;
				if (info.getCell(3) != null) {
					info.getCell(3).setCellType(CellType.STRING);
					purchaseurl = info.getCell(3).getStringCellValue();
				}
				String othercost=null;
				if (info.getCell(4) != null) {
					info.getCell(4).setCellType(CellType.STRING);
					othercost = info.getCell(4).getStringCellValue();
				}
				String deliverycycle=null;
				if (info.getCell(5) != null) {
					info.getCell(5).setCellType(CellType.STRING);
					deliverycycle = info.getCell(5).getStringCellValue();
				}
				String badrate=null;
				if (info.getCell(6) != null) {
					info.getCell(6).setCellType(CellType.STRING);
					badrate = info.getCell(6).getStringCellValue();
				}
				String moq=null;
				if (info.getCell(7) != null) {
					info.getCell(7).setCellType(CellType.STRING);
					moq = info.getCell(7).getStringCellValue();
				}
				//再插入新纪录
				MaterialSupplier entity=new MaterialSupplier();
				entity.setSupplierid(supid);
				entity.setPurchaseurl(purchaseurl);
				entity.setProductcode(procode);
				if(StrUtil.isNotEmpty(othercost))entity.setOthercost(new BigDecimal(othercost));
				entity.setOpttime(new Date());
				entity.setOperator(user.getId());
				if(StrUtil.isNotEmpty(moq))entity.setMOQ(Integer.parseInt(moq));
				entity.setMaterialid(material.getId());
				entity.setIsdefault(false);//备选
				if(StrUtil.isNotEmpty(deliverycycle))entity.setDeliverycycle(Integer.parseInt(deliverycycle));
				entity.setCreater(user.getId());
				entity.setCreatedate(new Date());
				if(StrUtil.isNotEmpty(badrate))entity.setBadrate(Float.parseFloat(badrate));

				QueryWrapper<MaterialSupplier> mqueryWrapper=new QueryWrapper<MaterialSupplier>();
				mqueryWrapper.eq("materialid", material.getId());
				mqueryWrapper.eq("supplierid", supid);
				MaterialSupplier one = materialSupplierMapper.selectOne(mqueryWrapper);
				if(one!=null) {
					entity.setId(one.getId());
					materialSupplierMapper.updateById(entity);
				}else {
					materialSupplierMapper.insert(entity);
				}
				material.setPurchaseUrl(purchaseurl);
				material.setDeliveryCycle(entity.getDeliverycycle());
				material.setProductCode(procode);
				materialMapper.updateById(material);
				//插入后删除 一模一样的备选供应商 是0的

				//step price同理 先删除后添加
				QueryWrapper<MaterialSupplierStepwise> stepqueryWrapper=new QueryWrapper<MaterialSupplierStepwise>();
				stepqueryWrapper.eq("materialid", material.getId());
				stepqueryWrapper.eq("supplierid", supid);
				materialSupplierStepwiseMapper.delete(stepqueryWrapper);
				String pricelist=null;//{100-9.9},{200-9.5}
				if (info.getCell(8) != null) {
					info.getCell(8).setCellType(CellType.STRING);
					pricelist = info.getCell(8).getStringCellValue();
				}
				if(StrUtil.isNotEmpty(pricelist)&& pricelist.contains("{")) {
					String[] stepArr = pricelist.split(",");
					if(stepArr.length>0) {
						for (int i = 0; i < stepArr.length; i++) {
							String item = stepArr[i];
							item=item.replace("{", "");
							item=item.replace("}", "");
							String amount = item.split("-")[0];
							String price=item.split("-")[1];
							MaterialSupplierStepwise step=new MaterialSupplierStepwise();
							step.setCurrency("0");
							if(StrUtil.isNotEmpty(amount))step.setAmount(Integer.parseInt(amount));
							if(StrUtil.isNotEmpty(price))step.setPrice(new BigDecimal(price));
							step.setMaterialid(material.getId());
							step.setSupplierid(supid);
							step.setOperator(user.getId());
							step.setOpttime(new Date());
							materialSupplierStepwiseMapper.insert(step);
						}
					}
				}

			}

	}

	@Override
	public MaterialConsumableVO  uploadMaterialConsumableFile(UserInfo user, Row info) {
		String sku=null;
		if (info.getCell(0) != null) {
			info.getCell(0).setCellType(CellType.STRING);
			sku = info.getCell(0).getStringCellValue();
		}
		String subsku=null;
		if (info.getCell(1) != null) {
			info.getCell(1).setCellType(CellType.STRING);
			subsku = info.getCell(1).getStringCellValue();
		}
		String amount=null;
		if (info.getCell(2) != null) {
			info.getCell(2).setCellType(CellType.STRING);
			amount = info.getCell(2).getStringCellValue();
		}
		if(StrUtil.isNotEmpty(sku) && StrUtil.isNotEmpty(subsku)) {
			Material material = getMaterialById(user.getCompanyid(), sku);
			Material submaterial = getMaterialById(user.getCompanyid(), subsku);
			
			if(material!=null && submaterial!=null) {
				MaterialConsumableVO entity=new MaterialConsumableVO();
				if(StrUtil.isNotEmpty(amount))entity.setAmount(new BigDecimal(amount));
				entity.setMaterialid(material.getId());
				entity.setId(submaterial.getId());
				entity.setSku(material.getSku());
				entity.setName(material.getName());
				return entity;
			}
		}
		
		return null;
		
		
	}

	@Override
	public void uploadMaterialCustomsFile(UserInfo user, Row info) {
		//海关信息
		String sku=null;
		if (info.getCell(0) != null) {
			info.getCell(0).setCellType(CellType.STRING);
			sku = info.getCell(0).getStringCellValue();
		}
		String country=null;
		if (info.getCell(1) != null) {
			info.getCell(1).setCellType(CellType.STRING);
			country = info.getCell(1).getStringCellValue();
		}
		List<String> suffix=new ArrayList<String>() ;
		suffix.add("");
		if(sku.contains("[suffix:")){
			sku=sku.replace("]","");
			sku=sku.replace("[","");
			String[] splitsku = sku.split("suffix:");
			sku=splitsku[0];
			String skusuffix=splitsku[1];
			String[] items = skusuffix.split(",");
			if(items!=null&&items.length >0){
				for(int i=0;i<items.length;i++){
					if(StrUtil.isNotBlank(items[i])){
						suffix.add(items[i].trim());
					}
				}
			}
		}
		for(String suffixitem:suffix){
			Material material = getMaterialById(user.getCompanyid(), sku+suffixitem);
			if(material!=null) {
				//先删除后添加
				QueryWrapper<MaterialCustoms> queryWrapper=new QueryWrapper<MaterialCustoms>();
				queryWrapper.eq("materialid", material.getId());
				materialCustomsMapper.delete(queryWrapper);
				String namecn=null;
				if (info.getCell(2) != null) {
					info.getCell(2).setCellType(CellType.STRING);
					namecn = info.getCell(2).getStringCellValue();
				}
				String nameen=null;
				if (info.getCell(3) != null) {
					info.getCell(3).setCellType(CellType.STRING);
					nameen = info.getCell(3).getStringCellValue();
				}
				String materials=null;
				if (info.getCell(4) != null) {
					info.getCell(4).setCellType(CellType.STRING);
					materials = info.getCell(4).getStringCellValue();
				}
				String materialscn=null;
				if (info.getCell(5) != null) {
					info.getCell(5).setCellType(CellType.STRING);
					materialscn = info.getCell(5).getStringCellValue();
				}
				String application=null;
				if (info.getCell(6) != null) {
					info.getCell(6).setCellType(CellType.STRING);
					application = info.getCell(6).getStringCellValue();
				}
				String price=null;
				if (info.getCell(7) != null) {
					info.getCell(7).setCellType(CellType.STRING);
					price = info.getCell(7).getStringCellValue();
				}
				String code=null;
				if (info.getCell(8) != null) {
					info.getCell(8).setCellType(CellType.STRING);
					code = info.getCell(8).getStringCellValue();
				}
				String rate=null;
				if (info.getCell(9) != null) {
					info.getCell(9).setCellType(CellType.STRING);
					rate = info.getCell(9).getStringCellValue();
				}
				String url=null;
				if (info.getCell(10) != null) {
					info.getCell(10).setCellType(CellType.STRING);
					url = info.getCell(10).getStringCellValue();
				}

				MaterialCustoms entity=new MaterialCustoms();
				entity.setMaterialid(material.getId());
				entity.setCountry(country);
				entity.setApplication(application);
				entity.setCname(namecn);
				entity.setEname(nameen);
				entity.setCode(code);
				entity.setMaterial(materials);
				entity.setMaterialcn(materialscn);
				entity.setUrl(url);
				entity.setOpttime(new Date());
				entity.setOperator(user.getId());
				if(price!=null&&GeneralUtil.isNumericzidai(price)){
					entity.setPrice(new BigDecimal(price));
				}
				if(rate!=null&&GeneralUtil.isNumericzidai(rate)){
					entity.setRate(new BigDecimal(rate));
				}
				//先删除后加
				if(StrUtil.isNotBlank(entity.getCountry())){
					QueryWrapper<MaterialCustoms> query=new QueryWrapper<MaterialCustoms>();
					query.eq("materialid", material.getId());
					query.eq("country",country);
					MaterialCustoms old=materialCustomsMapper.selectOne(query);
					if(old==null) {
						entity.setCreator(user.getId());
						entity.setCreatetime(new Date());
						materialCustomsMapper.insert(entity);
					}else {
						materialCustomsMapper.update(entity, query);
					}
				}else{
					String[] countrys=new String[]{"US","MX","CA","JP","IN","TR","UK","SG","IT","ES","SE","FR","DE","PL","NL","SA","EG","BE","BR","AU","AE"};
					for(String item: countrys){
						entity.setCountry(item);
						QueryWrapper<MaterialCustoms> query=new QueryWrapper<MaterialCustoms>();
						query.eq("materialid", material.getId());
						query.eq("country",country);
						MaterialCustoms old=materialCustomsMapper.selectOne(query);
						if(old==null) {
							entity.setCreator(user.getId());
							entity.setCreatetime(new Date());
							materialCustomsMapper.insert(entity);
						}else {
							materialCustomsMapper.update(entity, query);
						}
					}

				}
			}
		}

		
	}
	
	@Override
	public void uploadMaterialAssemblyFile(UserInfo user, Row info) {
		String sku=null;//主sku
		if (info.getCell(0) != null) {
			info.getCell(0).setCellType(CellType.STRING);
			sku = info.getCell(0).getStringCellValue();
		}
		//{A001:2},{A002:1}
		String assemblylist=null;//主sku
		if (info.getCell(1) != null) {
			info.getCell(1).setCellType(CellType.STRING);
			assemblylist = info.getCell(1).getStringCellValue();
		}
		Material material =getMaterialById(user.getCompanyid(), sku);
		if(material!=null && StrUtil.isNotEmpty(assemblylist) && assemblylist.contains("}")) {
			String[] assemblyArr = assemblylist.split(",");
			//先删除后加
			QueryWrapper<Assembly> queryWrapper=new QueryWrapper<Assembly>();
			queryWrapper.eq("mainmid", material.getId());
			assemblyMapper.delete(queryWrapper);
			int count=0;
			for (int i = 0; i < assemblyArr.length; i++) {
				String assemblyitem = assemblyArr[i];
				assemblyitem=assemblyitem.replace("{", "");
				assemblyitem=assemblyitem.replace("}", "");
				String subsku=assemblyitem.split(":")[0];
				String amount=assemblyitem.split(":")[1];
				Material submaterial = getMaterialById(user.getCompanyid(), subsku);
				if(submaterial!=null && ("2".equals(submaterial.getIssfg()) || "0".equals(submaterial.getIssfg()) )) {
					Assembly entity=new Assembly();
					entity.setMainmid(material.getId());
					entity.setSubmid(submaterial.getId());
					entity.setSubnumber(1);
					if (StrUtil.isNotEmpty(amount)) {
						int num = Integer.parseInt(amount);
						if(num>0) {
							entity.setSubnumber(num);
						}
					}
					entity.setRemark(null);
					entity.setOpttime(new Date());
					entity.setOperator(user.getId());
					int savecount = assemblyMapper.insert(entity);
					if(savecount>0) {
						count++;
						if(submaterial.getIssfg().equals("0")) {
							submaterial.setIssfg("2");
							materialMapper.updateById(submaterial);
						}
					}
				}else {
					throw new BizException("子产品不支持添加已经是组合品的产品！");
				}
				
			}
			if(count>0) {
				material.setIssfg("1");
				material.setOpttime(new Date());
				material.setOperator(user.getId());
				materialMapper.updateById(material);
			}
		}
		
	}
	
	public Material getMaterialById(String shopid,String sku) {
		QueryWrapper<Material> skuqueryWrapper=new QueryWrapper<Material>();
		skuqueryWrapper.eq("shopid", shopid);
		skuqueryWrapper.eq("sku", sku);
		Material material = materialMapper.selectOne(skuqueryWrapper);
		return material;
	}

	
	

}
