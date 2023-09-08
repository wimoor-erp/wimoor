package com.wimoor.erp.purchase.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.mvc.FileUpload;
import com.wimoor.common.pojo.entity.Picture;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.IPictureService;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.api.AdminClientOneFeignManager;
import com.wimoor.erp.assembly.pojo.entity.Assembly;
import com.wimoor.erp.assembly.pojo.entity.AssemblyForm;
import com.wimoor.erp.assembly.pojo.entity.AssemblyFormEntry;
import com.wimoor.erp.assembly.service.IAssemblyFormEntryService;
import com.wimoor.erp.assembly.service.IAssemblyFormService;
import com.wimoor.erp.assembly.service.IAssemblyService;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.common.pojo.entity.EnumByFormType;
import com.wimoor.erp.common.pojo.entity.EnumByInventory;
import com.wimoor.erp.common.pojo.entity.Operate;
import com.wimoor.erp.common.pojo.entity.Status;
import com.wimoor.erp.config.IniConfig;
import com.wimoor.erp.customer.pojo.entity.Customer;
import com.wimoor.erp.customer.service.ICustomerService;
import com.wimoor.erp.finance.mapper.FinanceProjectMapper;
import com.wimoor.erp.finance.pojo.entity.FinanceProject;
import com.wimoor.erp.inventory.pojo.entity.InventoryParameter;
import com.wimoor.erp.inventory.service.IInventoryFormAgentService;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.material.mapper.DimensionsInfoMapper;
import com.wimoor.erp.material.mapper.MaterialMarkMapper;
import com.wimoor.erp.material.pojo.entity.DimensionsInfo;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.pojo.entity.MaterialMark;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.material.service.IStepWisePriceService;
import com.wimoor.erp.purchase.alibaba.mapper.PurchaseFormEntryLogisticsMapper;
import com.wimoor.erp.purchase.alibaba.pojo.entity.PurchaseFormEntryLogistics;
import com.wimoor.erp.purchase.alibaba.service.IPurchaseAlibabaAuthService;
import com.wimoor.erp.purchase.alibaba.service.IPurchaseFormEntryAlibabaInfoService;
import com.wimoor.erp.purchase.mapper.PurchaseFormEntryHistoryMapper;
import com.wimoor.erp.purchase.mapper.PurchaseFormEntryMapper;
import com.wimoor.erp.purchase.mapper.PurchaseFormMapper;
import com.wimoor.erp.purchase.mapper.PurchaseFormPaymentMethodMapper;
import com.wimoor.erp.purchase.pojo.dto.PurchaseSaveDTO;
import com.wimoor.erp.purchase.pojo.entity.PurchaseForm;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormEntry;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormEntryHistory;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormPayment;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormPaymentMethod;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormReceive;
import com.wimoor.erp.purchase.pojo.entity.PurchaseWareHouseStatus;
import com.wimoor.erp.purchase.service.IPurchaseFormEntryService;
import com.wimoor.erp.purchase.service.IPurchaseFormPaymentService;
import com.wimoor.erp.purchase.service.IPurchaseFormReceiveService;
import com.wimoor.erp.purchase.service.IPurchaseFormService;
import com.wimoor.erp.purchase.service.IPurchasePlanService;
import com.wimoor.erp.purchase.service.IPurchaseWareHouseStatusService;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelfInventoryOptRecord;
import com.wimoor.erp.warehouse.service.IWarehouseService;
import com.wimoor.erp.warehouse.service.IWarehouseShelfInventoryOptRecordService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

@Service("purchaseFormService")
@RequiredArgsConstructor
public class PurchaseFormServiceImpl extends  ServiceImpl<PurchaseFormMapper,PurchaseForm> implements IPurchaseFormService {

	@Autowired
	PurchaseFormEntryMapper purchaseFormEntryMapper;
	
	@Autowired
	IPurchaseFormPaymentService iPurchaseFormPaymentService;
	
	@Autowired
	MaterialMarkMapper materialMarkMapper;
	 
	@Autowired
	final IMaterialService materialService;

	@Autowired
	@Lazy
	IPurchasePlanService purchasePlanService;
	 
	@Autowired
	IPictureService pictureService;
	 
	@Autowired
	ICustomerService customerService;
	 
	@Autowired
	IInventoryService inventoryService;
	 
	@Autowired
	IWarehouseService warehouseService;
	 
	@Autowired
	IInventoryFormAgentService inventoryFormAgentService;
	 
	@Autowired
	IAssemblyFormService assemblyFormService;
	 
	@Autowired
	IAssemblyFormEntryService assemblyFormEntryService;
	 
	final IPurchaseFormEntryService purchaseFormEntryService;
	 
	@Autowired
	IPurchaseFormPaymentService purchaseFormPaymentService;
	
	@Autowired
	IPurchaseFormReceiveService purchaseFormReceiveService;
	
	final PurchaseFormEntryLogisticsMapper purchaseFormEntryLogisticsMapper;
	 
	final IPurchaseWareHouseStatusService purchaseWareHouseStatusService;
	 
	final DimensionsInfoMapper dimensionsInfoMapper;
	 
	final protected ISerialNumService serialNumService;
	 
	final IStepWisePriceService stepWisePriceService;
	 
	final FinanceProjectMapper financeProjectMapper;
	 
	final PurchaseFormPaymentMethodMapper purchaseFormPaymentMethodMapper;
	 
	final IPurchaseAlibabaAuthService purchaseAlibabaAuthService;
	 
	final IPurchaseFormEntryAlibabaInfoService purchaseFormEntryAlibabaInfoService;
	
	final AdminClientOneFeignManager adminClientOneFeign;
	
	final IWarehouseShelfInventoryOptRecordService iWarehouseShelfInventoryOptRecordService;
	
	final IAssemblyService assemblyService;
	
	final PurchaseFormEntryHistoryMapper purchaseFormEntryHistoryMapper;
	
	@Autowired
	FileUpload fileUpload;
	
	public int delete(String key) {
		int changecount = 0;
		PurchaseFormEntry entry = purchaseFormEntryMapper.selectById(key);
		changecount += purchaseFormEntryMapper.deleteById(key);
		QueryWrapper<PurchaseFormEntry> queryWrapper = new QueryWrapper<PurchaseFormEntry>();
		queryWrapper.eq("formid", entry.getFormid());
		List<PurchaseFormEntry> entrylist = purchaseFormEntryMapper.selectList(queryWrapper);
		if (entrylist == null || entrylist.size() == 0) {
			changecount += this.baseMapper.deleteById(entry.getFormid());
		}
		return changecount;
	}

	public IPage<Map<String, Object>> getPurchaseFormEntry(Page<?> page,Map<String, Object> param) {
		IPage<Map<String, Object>> list = this.baseMapper.selectByCondition(page,param);
		if(list!=null&&list.getRecords()!=null&&list.getRecords().size()>0) {
			Map<String, Object> tagsNameMap = null;
			String shopid=param.get("shopid").toString();
			 try {
			    	Result<Map<String, Object>> tagResult = adminClientOneFeign.findTagsName(shopid);
			    	if(Result.isSuccess(tagResult)&&tagResult.getData()!=null) {
			    		  tagsNameMap = tagResult.getData();
			    	}
			    }catch(Exception e) {
			    	e.printStackTrace();
			    }
			 List<Map<String, Object>> entrylist = list.getRecords();
			 if(entrylist!=null&&entrylist.size()>0) {
					for(Map<String, Object> entry:entrylist) {
						String tags=entry.get("tags")!=null?entry.get("tags").toString():null;
						 if(tagsNameMap!=null &&tags!=null) {
				    			 String[] tagarray =tags.split(",");
				    			 if(tagarray!=null&&tagarray.length>0) {
				    				 List<Object> tagnamelist=new ArrayList<Object>();
				    					for(String tagitem:tagarray) {
                                          if(tagitem!=null) {
                                         	 Object name = tagsNameMap.get(tagitem);
 			    							 if(name!=null) {
 			    								 tagnamelist.add(name);
 			    							 }
                                          }
			    						}
				    					entry.put("TagNameList",tagnamelist);
			    		           }
						   }
					}
		     }
		}
		return list;
	}

	public IPage<Map<String, Object>> getPurchaseForm(Page<?> page,Map<String, Object> param) {
		Map<String, Object> tagsNameMap = null;
		String shopid=param.get("shopid").toString();
		IPage<Map<String, Object>> list = this.baseMapper.selectByConditionForm(page,param);
		if(list!=null &&   list.getRecords().size()>0) {
			//把entrylist加入form
			 try {
			    	Result<Map<String, Object>> tagResult = adminClientOneFeign.findTagsName(shopid);
			    	if(Result.isSuccess(tagResult)&&tagResult.getData()!=null) {
			    		  tagsNameMap = tagResult.getData();
			    	}
			    }catch(Exception e) {
			    	e.printStackTrace();
			    } 
			for (int i = 0; i < list.getRecords().size(); i++) {
				Map<String, Object> item = list.getRecords().get(i);
				item.put("isselect", false);
				Map<String, Object> params=new HashMap<String, Object>();
				params.put("entryorders", "order by m.sku asc");
				params.put("auditstatus", "all");
				params.put("ftype", "sku");
				params.put("formid", item.get("id"));
				params.put("shopid",param.get("shopid"));
				List<Map<String, Object>> entrylist = this.baseMapper.selectByCondition(params);
				if(entrylist!=null&&entrylist.size()>0) {
					for(Map<String, Object> entry:entrylist) {
						String tags=entry.get("tags")!=null?entry.get("tags").toString():null;
						 if(tagsNameMap!=null &&tags!=null) {
				    			 String[] tagarray =tags.split(",");
				    			 if(tagarray!=null&&tagarray.length>0) {
				    				 List<Object> tagnamelist=new ArrayList<Object>();
				    					for(String tagitem:tagarray) {
                                             if(tagitem!=null) {
                                            	 Object name = tagsNameMap.get(tagitem);
    			    							 if(name!=null) {
    			    								 tagnamelist.add(name);
    			    							 }
                                             }
			    						}
				    					entry.put("TagNameList",tagnamelist);
			    		           }
						   }
					}
					item.put("itemlist", entrylist);
				
			     }
		     }//for
		}//if
		return list;
	}
	
	public Map<String, Object> savePurchaseDataAction(PurchaseSaveDTO dto) throws ERPBizException {
		Map<String, Object> msgmap = new HashMap<String, Object>();
		String warehouseid = dto.getWarehouseid();
		String planwarehouseid = dto.getPlanwarehouseid();
		String remark = dto.getRemark();
		String groupid=dto.getGroupid();
		if(StrUtil.isBlankOrUndefined(groupid)||groupid.equals("all")) {
			groupid=null;
		}
		String orderitemlist = dto.getOrderitemlist();
		List<Map<String, Object>> listmap = GeneralUtil.jsonStringToMapList(orderitemlist);
		UserInfo user = UserInfoContext.get();
		String shopid = user.getCompanyid();
		Map<String, Map<String, PurchaseFormEntry>> itemSupplier = new HashMap<String, Map<String, PurchaseFormEntry>>();
		for (int i = 0; i < listmap.size(); i++) {
			Map<String, Object> map = listmap.get(i);
			PurchaseFormEntry item = new PurchaseFormEntry();
			Object objamount = map.get("amount");
			Object materialid = map.get("materialid");
			Object supplier = map.get("supplier");
			Object itemprice = map.get("itemprice");
			Object orderprice = map.get("orderprice");
			Object planitemid = map.get("planitemid");
			Object deliverydate = map.get("deliverydate");
			Object sku = map.get("sku");
			if (supplier == null || GeneralUtil.isEmpty(supplier.toString())) {
				throw new ERPBizException("SKU[" + sku.toString() + "]" + "无供应商，无法采购。");
			}
			if (itemprice == null || GeneralUtil.isEmpty(itemprice.toString())) {
				throw new ERPBizException("SKU[" + sku.toString() + "]" + "无商品价格，无法采购。");
			}
			item.setAmount(objamount == null ? null : Integer.parseInt(objamount.toString()));
			item.setMaterialid(materialid == null ? null : materialid.toString());
			item.setOperator(user.getId());
			item.setAuditstatus(1);// 待审核
			item.setOpttime(new Date());
			item.setDeliverydate(GeneralUtil.getDate(deliverydate));
			item.setItemprice(new BigDecimal(itemprice == null ? null : itemprice.toString()));
			if (orderprice != null && StrUtil.isNotEmpty(orderprice.toString()) && Float.parseFloat(orderprice.toString()) > 0.01) {
				item.setOrderprice(new BigDecimal(GeneralUtil.isEmpty(orderprice.toString()) ? null : orderprice.toString().trim()));
			} else {
				throw new ERPBizException("SKU[" + sku.toString() + "]" + "无订单金额，无法采购。");
			}
			item.setPlanitemid(planitemid == null ? null : planitemid.toString());
			item.setSupplier(supplier == null ? null : supplier.toString());
			if (itemSupplier.containsKey(supplier)) {
				Map<String, PurchaseFormEntry> supplierMap = itemSupplier.get(item.getSupplier());
				if (supplierMap.containsKey(item.getMaterialid())) {
					PurchaseFormEntry entry = supplierMap.get(item.getMaterialid());
					entry.setAmount(entry.getAmount() + item.getAmount());
					if (!entry.getSupplier().equals(item.getSupplier())) {
						throw new ERPBizException("SKU[" + sku.toString() + "]" + "供应商不相同。");
					}
					entry.setOrderprice(entry.getOrderprice().add(item.getOrderprice()));
					entry.setItemprice(entry.getOrderprice().divide(new BigDecimal(entry.getAmount().toString()),BigDecimal.ROUND_FLOOR, 2));
					Map<String, Object> tmap = stepWisePriceService.getMaterialPriceByAmount(materialid.toString(),entry.getAmount());
					BigDecimal itempriceold = new BigDecimal(tmap.get("itemprice").toString());
					entry.setItemprice(itempriceold);
					BigDecimal orderp = itempriceold.multiply(new BigDecimal(entry.getAmount()));
					if (entry.getOrderprice().compareTo(orderp) > 0) {
						entry.setOrderprice(orderp);
					}
				} else {
					supplierMap.put(item.getMaterialid(), item);
				}
			} else {
				Map<String, PurchaseFormEntry> map2 = new HashMap<String, PurchaseFormEntry>();
				map2.put(item.getMaterialid(), item);
				itemSupplier.put(item.getSupplier(), map2);
			}
		}

		List<String> formidList = new ArrayList<String>();
		List<PurchaseForm> purFormList = new ArrayList<PurchaseForm>();
		String batchstrs=null;
		try {
			batchstrs = serialNumService.readSerialNumber(shopid, "DP");
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		for (String key : itemSupplier.keySet()) {
			PurchaseForm form = null;
			if(StrUtil.isNotEmpty(dto.getFormid())) {
				form=this.getById(dto.getFormid());
			}else {
				form=new PurchaseForm();
				form.setBatch(batchstrs);
				try {
					form.setNumber(serialNumService.readSerialNumber(shopid, "PO"));
				} catch (Exception e) {
					e.printStackTrace();
					try {
						form.setNumber(serialNumService.readSerialNumber(shopid, "PO"));
					} catch (Exception e1) {
						e1.printStackTrace();
						throw new ERPBizException("编码获取失败,请联系管理员");
					}
				}
			}
			form.setRemark(remark);
			form.setShopid(shopid);
			form.setCreator(user.getId());
			if(StrUtil.isNotEmpty(groupid)) {
				form.setGroupid(groupid);
			}
			form.setCreatedate(new Date());
			form.setWarehouseid(warehouseid);
			form.setPurchaser(dto.getPurchaser());
			for (Entry<String, PurchaseFormEntry> entry : itemSupplier.get(key).entrySet()) {
				PurchaseFormEntry purchase = entry.getValue();
				purchase.setFormid(form.getId());
				form.addEntry(purchase);
			}
			formidList.add(form.getId());
			purFormList.add(form);
		}
		 
		int i =  savePurchaseForm(user ,purFormList, planwarehouseid);
		if(StrUtil.isNotEmpty(dto.getAssList())) {
			List<Map<String, Object>> list = GeneralUtil.jsonStringToMapList(dto.getAssList());
			if(list!=null) {
				saveAssemblyAction(user,warehouseid,list,purFormList);
			}
		}
		msgmap.put("formList", purFormList);
		if (i > 0) {
			msgmap.put("msg", "保存成功");
			msgmap.put("saveid", formidList);
		} else {
			msgmap.put("msg", "保存失败");
		}
		return msgmap;
	}
	public Map<String, Object> saveAssemblyAction(UserInfo user,String warehouseid,List<Map<String,Object>> list, List<PurchaseForm> purFormList) {
		Map<String, Object> msgMap = new HashMap<String, Object>();
		for(int i=0;i<list.size();i++) {
			Map<String, Object> item = list.get(i);
            if(item.get("materialid")==null||item.get("quantity")==null) {
            	continue;
            }
          
			String materialid=item.get("materialid").toString();
			Integer quantity=Integer.parseInt(item.get("quantity").toString());
			AssemblyForm asform = new AssemblyForm();
			String shopid = user.getCompanyid();
			try {
				asform.setNumber(serialNumService.readSerialNumber(shopid, "MO"));
			} catch (Exception e) {
				e.printStackTrace();
				try {
					asform.setNumber(serialNumService.readSerialNumber(shopid, "MO"));
				} catch (Exception e1) {
					e1.printStackTrace();
					throw new BizException("编码获取失败,请联系管理员");
				}
			}
			asform.setShopid(shopid);
			 
			String ftype = "ass";
	 
			String remark ="采购订单同步创建";

			asform.setAudittime(new Date());
			asform.setOpttime(new Date());
			asform.setCreatedate(new Date());
			asform.setAmount(quantity);
			asform.setAmountHandle(0);
			if (ftype != null) {
				asform.setFtype(ftype);
			}
			asform.setAuditstatus(2);// 1:待组装
			asform.setCreator(user.getId());
			asform.setAuditor(user.getId());
			asform.setOperator(user.getId());
			asform.setWarehouseid(warehouseid);
			asform.setMainmid(materialid);
			asform.setRemark(remark);
	
			List<Assembly> itemlist = assemblyService.selectAssemblySub(materialid);
			HashMap<String,String> mappurchase=new HashMap<String,String>();
			  for(PurchaseForm form:purFormList) {
	            	for(PurchaseFormEntry entry:form.getEntrylist()) {
	            		mappurchase.put(entry.getMaterialid(), entry.getId());
	            	}
	            }
			  
			for (int j = 0; j < itemlist.size(); j++) {
				Assembly asitem = itemlist.get(j);
				AssemblyFormEntry entry = new AssemblyFormEntry();
				entry.setAmount(asitem.getSubnumber()*quantity);
				entry.setMaterialid(asitem.getSubmid());
				entry.setWarehouseid(warehouseid);
				entry.setFormid(asform.getId());
				asform.addEntry(entry);
				if(mappurchase.get(asitem.getSubmid())!=null) {
					entry.setPurchaseFromEntryId(mappurchase.get(asitem.getSubmid()));
				}
			}
			msgMap = assemblyFormService.saveAssemblyForm(asform, user);
		}

		return msgMap;
	}
	
	 

	public boolean updatePurchaseFormEntry(PurchaseFormEntry item, String warehouseid) throws ERPBizException {
		PurchaseFormEntry oldentryform = purchaseFormEntryMapper.selectById(item.getId());
		PurchaseForm oldform = this.baseMapper.selectById(oldentryform.getFormid());
		if (!oldform.getWarehouseid().equals(warehouseid) && GeneralUtil.isNotEmpty(warehouseid)) {
			oldform.setWarehouseid(warehouseid);
			this.updateById(oldform);
		}
		return purchaseFormEntryService.updateById(item);
	}

	@Transactional
	public int approvals(UserInfo user, String ids) throws ERPBizException {
		if (GeneralUtil.isNotEmpty(ids)) {
			String[] idarray = ids.trim().split(",");
			for (String id : idarray) {
				PurchaseFormEntry entry = purchaseFormEntryMapper.selectById(id);
				if (entry != null && entry.getAuditstatus() == 1) {// 只能审核待审核状态的订单
					entry.setAuditor(user.getId());
					entry.setAudittime(new Date());
					entry.setAuditstatus(2);// 2:审核通过
					entry.setOperator(user.getId());
					entry.setOpttime(new Date());
					entry.setInwhstatus(0);
					entry.setPaystatus(0);
					approvalInStockByRead(user, entry);
					purchaseFormEntryService.updateById(entry);
				} else if (entry != null) {
					throw new ERPBizException("订单已经被审核，不能重复审核订单！");
				}
			}
			return idarray.length;
		}
		return 0;
	}

	public boolean   updateWarehouse(UserInfo user, String id, String warehouseid) throws ERPBizException {
		boolean flag = false;// 是否已审核
		List<PurchaseFormEntry> list = purchaseFormEntryMapper.findByFormId(id);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				PurchaseFormEntry entry = list.get(i);
				if (entry.getAuditstatus() != 1&&entry.getAuditstatus() != 0) {// 不是未审核状态
					flag = true;
					break;
				}
			}
		}
		if (!flag) {
			PurchaseForm purform = this.baseMapper.selectById(id);
			purform.setWarehouseid(warehouseid);
			return this.updateById(purform);
		} else {
			throw new BizException("已经审核的采购订单不能修改入库仓库！");
		}
		 
		 
	}


	public PurchaseFormEntry updatePrice(UserInfo user, String id, Float itemprice, Integer amount, Float orderprice) throws ERPBizException {
		PurchaseFormEntry entry = purchaseFormEntryMapper.selectById(id);
		if (entry.getAuditstatus() >= 2) {
			throw new ERPBizException("已审核，不能再修改单据信息");
		}
		if (itemprice != null) {
			entry.setItemprice(new BigDecimal(itemprice + ""));
		}
		if (amount != null) {
			entry.setAmount(amount);
		}
		entry.setOrderprice(entry.getItemprice().multiply(new BigDecimal(entry.getAmount())));

		if (orderprice != null) {
			entry.setOrderprice(new BigDecimal(orderprice + ""));
		}
		entry.setOpttime(new Date());
		entry.setOperator(user.getId());
		if (entry.getOrderprice().floatValue() < 0.000001) {
			throw new ERPBizException("采购金额不能为0");
		}
		purchaseFormEntryService.updateById(entry);
		return entry;
	}

	public void approvalInStockByRead(UserInfo user, PurchaseFormEntry entry) throws ERPBizException {
		PurchaseForm ph = this.baseMapper.selectById(entry.getFormid());
		InventoryParameter invpara = new InventoryParameter();
		invpara.setAmount(entry.getAmount());
		invpara.setFormid(entry.getFormid());
		invpara.setMaterial(entry.getMaterialid());
		invpara.setWarehouse(ph.getWarehouseid());
		invpara.setOperator(entry.getOperator());
		invpara.setNumber(ph.getNumber());
		invpara.setOpttime(new Date());
		invpara.setStatus(EnumByInventory.Ready);
		invpara.setShopid(user.getCompanyid());
		invpara.setFormtype("purchase");
		inventoryFormAgentService.inStockByRead(invpara);
	}
	

	public String getNoticeById(String materialid) {
		QueryWrapper<MaterialMark> queryWrapper = new QueryWrapper<MaterialMark>();
		queryWrapper.eq("materialid", materialid);
		queryWrapper.eq("ftype", "notice");
		List<MaterialMark> list = materialMarkMapper.selectList(queryWrapper);
		if (list != null && list.size() > 0) {
			return list.get(0).getMark();
		}
		return null;
	}

	public int findSumaryNeedApply(String shopid) {
		return purchaseFormEntryMapper.findSumaryNeedApply(shopid);
	}

	public int findSumaryNeedin(String shopid) {
		return purchaseFormEntryMapper.findSumaryNeedin(shopid);
	}

	public int findSumaryNeedpay(String shopid) {
		return purchaseFormEntryMapper.findSumaryNeedpay(shopid);
	}




	public Map<String, Object> getTraceDetailMap(String id, String shopid, String ftype,String paytype) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		PurchaseFormEntry entry = purchaseFormEntryMapper.selectById(id);
		if(entry==null) {
			return null;
		}
		resultMap.put("entry", entry);
		Material material = materialService.getById(entry.getMaterialid());
		Integer delivery_cycle = material.getDeliveryCycle()!=null?material.getDeliveryCycle():0;
		if(material!=null&&material.getPkgdimensions()!=null) {
			DimensionsInfo dim = dimensionsInfoMapper.selectById(material.getPkgdimensions());
			if(dim!=null && dim.getWidth()!=null) {
				resultMap.put("weight", dim.getWeight().floatValue()*entry.getAmount());
			}
		}
		if(delivery_cycle>0) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, delivery_cycle);
			resultMap.put("delivery_cycledate", c.getTime());
		}
		resultMap.put("delivery_cycle", delivery_cycle);
		resultMap.put("deliverydate", entry.getDeliverydate());
		if ("rec".equals(ftype)) {
			Integer boxnum = material.getBoxnum();
			if(boxnum != null && boxnum > 0) {
				int amount = entry.getAmount() == null ? 0 : entry.getAmount();
				int amounIn = entry.getTotalin() == null ? 0 : entry.getTotalin();
				int num = amount - amounIn;
				if(num > 0) {
					int boxns = num % boxnum;
					int boxn = num / boxnum;
					resultMap.put("boxns", boxns);
					resultMap.put("boxn", boxn);
				} 
			}
			List<Map<String, Object>> reclist = purchaseFormReceiveService.selectByEntryid(id,paytype);
			if (reclist != null) {
				for(Map<String, Object> rec:reclist) {
					String recid=rec.get("id").toString();
					List<WarehouseShelfInventoryOptRecord> record = iWarehouseShelfInventoryOptRecordService.getRecord(shopid, recid,EnumByFormType.purchase.getValue(),null);
					rec.put("shelfInvRecordList", record);
				}
				resultMap.put("receivelist", reclist);
			}
		} else {
			List<PurchaseFormPayment> paylist = iPurchaseFormPaymentService.getPaymentByEntryid(id,paytype);
			if(paylist!=null && paylist.size()>0) {
				for (int i = 0; i < paylist.size(); i++) {
					PurchaseFormPayment pay = paylist.get(i);
					if(pay.getPaymentMethod()!=null) {
						PurchaseFormPaymentMethod methods = purchaseFormPaymentMethodMapper.selectById(pay.getPaymentMethod());
						pay.setMethodname(methods.getName());
					}
					String projectid = pay.getProjectid();
					if(projectid!=null && !PurchaseFormPaymentServiceImpl.type_ship.equals(projectid) &&  !PurchaseFormPaymentServiceImpl.type_cost.equals(projectid)) {
						FinanceProject project = financeProjectMapper.selectById(projectid);
						pay.setProjectname(project.getName());
					}
					if(PurchaseFormPaymentServiceImpl.type_cost.equals(projectid)) {
						pay.setProjectname("货物费用");
					}
					if(PurchaseFormPaymentServiceImpl.type_ship.equals(projectid)) {
						pay.setProjectname("运费");
					}
					if(pay!=null&&pay.getOperator()!=null) {
						Result<Map<String, Object>> info = adminClientOneFeign.getUserByUserId(pay.getOperator());
						if(Result.isSuccess(info)&&info.getData()!=null) {
							pay.setOperator(info.getData().get("name")!=null?info.getData().get("name").toString():null);
						}
					}
				}
			}
			QueryWrapper<PurchaseFormEntryLogistics> queryWrapper = new QueryWrapper<PurchaseFormEntryLogistics>();
			queryWrapper.eq("entryid", id);
			List<PurchaseFormEntryLogistics> list = purchaseFormEntryLogisticsMapper.selectList(queryWrapper);
			if (list != null && list.size() > 0) {
				resultMap.put("logisticsid", list.get(0).getLogisticsid());
			}
			BigDecimal totalcost = new BigDecimal("0");
			BigDecimal totalship = new BigDecimal("0");
			BigDecimal totalre = new BigDecimal("0");
			if (paylist != null) {
				resultMap.put("paylist", paylist);
				for (PurchaseFormPayment item : paylist) {
					if(item.getAuditstatus()!=1) {
						continue;
					}
					if (PurchaseFormPaymentServiceImpl.type_cost.equals(item.getProjectid()) && item.getPayprice().compareTo(new BigDecimal("0")) > 0) {
						totalcost = totalcost.add(item.getPayprice());
					} else if (PurchaseFormPaymentServiceImpl.type_cost.equals(item.getProjectid()) && item.getPayprice().compareTo(new BigDecimal("0")) < 0) {
						totalre = totalre.add(item.getPayprice());
					} else if (PurchaseFormPaymentServiceImpl.type_ship.equals(item.getProjectid())) {
						totalship = totalship.add(item.getPayprice());
					}
				}
			}
			resultMap.put("totalcost", totalcost);
			resultMap.put("totalship", totalship);
			resultMap.put("totalre", totalre);
			resultMap.put("payneed", "0");
			if (entry != null) {
				if (entry.getAmount() != null && entry.getAmount() != 0) {
					resultMap.put("recrate", (entry.getTotalin() * 100 / entry.getAmount()));
					if (entry.getAmount() - entry.getTotalin() > 0) {
						resultMap.put("recneed", entry.getAmount() - entry.getTotalin());
					} else {
						resultMap.put("recneed", 0);
					}
				}
				if (entry.getOrderprice() != null && entry.getOrderprice().floatValue() != 0) {
					resultMap.put("payrate", entry.getTotalpay().multiply(new BigDecimal("100")).divide(entry.getOrderprice(), 0, RoundingMode.HALF_UP));
					resultMap.put("payneed", entry.getOrderprice().subtract(totalcost));
				}
			}
		}
		return resultMap;
	}

	public Map<String, Object> getDetailMap(String id, String shopid) throws ERPBizException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (id == null) {
			resultMap.put("action", "false");
			resultMap.put("msg", "查询失败，数据异常");
			return resultMap;
		}
		PurchaseFormEntry entry = purchaseFormEntryMapper.selectById(id);
		if(entry==null) {
			resultMap.put("action", "false");
			resultMap.put("msg", "暂无匹配数据!");
			return resultMap;
		}
		List<Map<String, Object>> recordList = purchaseFormEntryMapper.findRecentPurchase(entry.getMaterialid());
		for (int i = 0; i < recordList.size(); i++) {
			Map<String, Object> item = recordList.get(i);
			if (entry.getFormid().equals(item.get("formid"))) {
				recordList.remove(i);
			}
		}
		if (recordList.size() > 0) {
			resultMap.put("recordList", recordList);
		}
		return resultMap;
	}

	public boolean updateDeliveryDate(String entryid,String deliverydate) {
		PurchaseFormEntry entry = purchaseFormEntryMapper.selectById(entryid);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			if (deliverydate != null) {
				date = df.parse(deliverydate);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (date != null) {
			entry.setDeliverydate(date);
			return purchaseFormEntryService.updateById(entry);
		}
		return false;
	}	
	

	
   public void updateLogisiter(PurchaseFormEntry entry,String logisiter) {
		if(GeneralUtil.isNotEmpty(logisiter)) {
			purchaseFormEntryAlibabaInfoService.insetLogisiter(entry.getId(), logisiter);
		}else {
			throw new BizException("运单号不能为空");
		}
   }
	



	

	@Transactional
	public void purchaseReturn(UserInfo user, String ids, String remark) throws ERPBizException {
		if (GeneralUtil.isNotEmpty(ids)) {
			String[] idarray = ids.split(",");
			for (String id : idarray) {
				cancelPurFormEntry(user, id, remark);
			}
		}
	}

	public void cancelPurFormEntry(UserInfo user, String id, String remark) throws ERPBizException {
		PurchaseFormEntry entry = purchaseFormEntryMapper.selectById(id);
		if (entry != null && entry.getAuditstatus() == 1) {
			entry.setAuditor(user.getId());
			entry.setAudittime(new Date());
			entry.setOperator(user.getId());
			entry.setOpttime(new Date());
			entry.setAuditstatus(0);// 0:草稿，退回；
			if (GeneralUtil.isEmpty(remark)) {
				remark = "";
			}
			entry.setRemark(remark);
		} else if (entry != null) {
			throw new ERPBizException("订单已经被审核，不能重复审核订单！");
		}
		purchaseFormEntryMapper.updateById(entry);
	}

	public Map<String, Object> updateNotice(String entryid, String notice, String shopid,String userid) throws ERPBizException {
		 
		PurchaseFormEntry entry = purchaseFormEntryMapper.selectById(entryid);
		int result = 0;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (entry != null) {
			if (GeneralUtil.isEmpty(notice)) {
				notice = "";
			}
			entry.setRemark(notice);
			result = purchaseFormEntryMapper.updateById(entry);
		} else {
			if (GeneralUtil.isEmpty(notice)) {
				notice = "";
			}
			if(materialService.saveMark(entryid, "notice", notice,userid)) {
				result ++ ;
			}
		}
		if (result > 0) {
			resultMap.put("update", "success");
			resultMap.put("count", result);
		} else {
			resultMap.put("update", "fail");
			resultMap.put("count", result);
		}
		return resultMap;
	}

	public List<Map<String, Object>> purchaseFormReport(Map<String, Object> param) {
		return purchaseFormEntryMapper.purchaseFormReport(param);
	}
	public IPage<Map<String, Object>> purchaseFormReport(Page<?>page,Map<String, Object> param) {
		return purchaseFormEntryMapper.purchaseFormReport(page,param);
	}
	public List<Map<String, Object>> getPurchaseSumReport(Map<String, Object> param) {
		return this.baseMapper.purchaseSumReport(param);
	}

	public List<Map<String, Object>> getPurchaseRecSumReport(Map<String, Object> param) {
		return this.baseMapper.purchaseRecSumReport(param);
	}

	public List<Map<String, Object>> getPurchaseSumReportNew(Map<String, Object> param) {
		return this.baseMapper.getPurchaseSumReportNew(param);
	}

	public Map<String, Object> getViewData(String id, String shopid) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (id == null) {
			return null;
		}
		String[] ids = id.split(",");
		String warehouseid = null;
		String warename = null;
		String remark = null;
		List<HashMap<String, Object>> entry = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < ids.length; i++) {
			PurchaseForm purchaseform = this.getById(ids[i]);
			warehouseid = purchaseform.getWarehouseid();
			warename = warehouseService.getById(warehouseid).getName();
			remark = purchaseform.getRemark();
			QueryWrapper<PurchaseFormEntry> queryWrapper = new QueryWrapper<PurchaseFormEntry>();
			queryWrapper.eq("formid", ids[i]);
			List<PurchaseFormEntry> listentry = purchaseFormEntryMapper.selectList(queryWrapper);
			for (PurchaseFormEntry item : listentry) {
				HashMap<String, Object> itemMap = new HashMap<String, Object>();
				itemMap.put("item", item);
				itemMap.put("number", purchaseform.getNumber());
				Material m = materialService.getById(item.getMaterialid());
				Customer supplier = customerService.getById(item.getSupplier());
				itemMap.put("sku", m.getSku());
				itemMap.put("name", m.getName());
				if (supplier == null) {
					itemMap.put("supplier", "-");
				} else {
					itemMap.put("supplier", supplier.getName());
				}
				entry.add(itemMap);
			}
		}
		map.put("warename", warename);
		map.put("remark", remark);
		map.put("entrylist", entry);
		return map;
	}

	public Map<String, Object> getPurchaseFormSummary(UserInfo user, Map<String, Object> param) {
		return this.baseMapper.selectSummarytByCondition(param);
	}

	public List<Map<String, Object>> getLastFormByMaterial(Object id, int i) {
		Page<?> page = new Page<Map<String, Object>>(0,i);
		return this.baseMapper.findeLastByMaterialid(page,id.toString().trim());
	}

	public Map<String, Object> getLastOneFormByMaterial(Object id) {
		if (id == null) {
			return null;
		} else {
			Page<?> page = new Page<Map<String, Object>>(0,1);
			List<Map<String, Object>> list = this.baseMapper.findeLastByMaterialid(page,id.toString().trim());
			if (list != null && list.size() == 1) {
				return list.get(0);
			} else {
				return null;
			}
		}
	}

	public List<Map<String, Object>> getFormDetail(String id, String shopid) {
		return purchaseFormEntryMapper.getFormDetail(id, shopid);
	}




	public Map<String, Object> purchaseFormReportTotal(Map<String, Object> param) {
		return purchaseFormEntryMapper.purchaseFormReportTotal(param);
	}

	public Map<String, Object> getLastPurchaseRecord(String shopid, String warehouseid) {
		return this.baseMapper.getLastPurchaseRecord(shopid, warehouseid);
	}

	public IPage<Map<String, Object>> getPayRecSumReport(Page<?> page,Map<String, Object> param) {
		IPage<Map<String, Object>> result = this.baseMapper.getPayRecSumReport(page,param);
		if (result.getRecords().size() > 0) {
			Map<String, Object> map = result.getRecords().get(0);
			Map<String, Object> summary = this.baseMapper.getPayRecSumTotal(param);
			map.put("summary", summary);
		}
		return result;
	}
	
	public void setPurchaseFormReportExcelBook(SXSSFWorkbook workbook, Map<String, Object> param) {
		Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
		String type = param.get("type").toString();
		if (type.equals("sku")) {
			titlemap.put("sku", "SKU");
		} else {
			titlemap.put("supplier", "供应商");
		}
		titlemap.put("pprice", "实际单价(￥)");
		titlemap.put("actual_totalin", "实际入库数量");
		titlemap.put("totalcostfee", "实际付款金额");
		titlemap.put("itemprice", "采购单价(￥)");
		titlemap.put("orderamount", "产品采购数量");
		titlemap.put("totalorderprice", "产品采购金额(￥)");
		titlemap.put("totalin", "入库数");
		titlemap.put("totalre", "退货数");
		titlemap.put("totalshipfee", "采购运费(￥)");
		titlemap.put("weight", "预估重量");
		titlemap.put("totalotherfee", "其他费用(￥)");
		titlemap.put("lesspay", "付款差额(￥)");
		titlemap.put("lessrec", "入库差额");
		titlemap.put("needin", "待入库数");
		titlemap.put("waitPay", "待付款金额(￥)");
		List<Map<String, Object>> list = purchaseFormEntryMapper.purchaseFormReport(param);
		Sheet sheet = workbook.createSheet("sheet1");
		// 在索引0的位置创建行（最顶端的行）
		Row trow = sheet.createRow(0);
		Object[] titlearray = titlemap.keySet().toArray();
		for (int i = 0; i < titlearray.length; i++) {
			Cell cell = trow.createCell(i); // 在索引0的位置创建单元格(左上端)
			Object value = titlemap.get(titlearray[i].toString());
			cell.setCellValue(value.toString());
		}
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Row row = sheet.createRow(i + 1);
				Map<String, Object> map = list.get(i);
				for (int j = 0; j < titlearray.length; j++) {
					Cell cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
					String key = titlearray[j].toString();
					Object value = map.get(key);
					if (value != null) {
						cell.setCellValue(value.toString());
					}
				}
			}
		}

	}

	public void setExcelBook(Workbook workbook, Map<String, Object> param) {
		Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
		titlemap.put("createdate", "订单日期");
		titlemap.put("supplier", "供应商");
		titlemap.put("number", "订单编码");
		titlemap.put("sku", "SKU");
		titlemap.put("auditstatus", "审核状态");
		titlemap.put("name", "产品名称");
		titlemap.put("itemprice", "单价");
		titlemap.put("amount", "采购数量");
		titlemap.put("orderprice", "采购金额");
		titlemap.put("facttotalpay", "实际付款总金额");
		titlemap.put("factitemprice", "实际单价");
		titlemap.put("paystatus", "付款状态");
		titlemap.put("totalpay", "付款金额");
		titlemap.put("totalrefund", "退款金额");
		titlemap.put("shipfee", "运费");
		titlemap.put("weight", "预估重量");
		titlemap.put("otherfee", "其他费用");
		titlemap.put("waitPay", "待付款");
		titlemap.put("appwaitPay", "申请待付款");
		titlemap.put("payment_method", "付款方式");
		titlemap.put("totalin", "入库数量");
		titlemap.put("totalreturn", "退货数量");
		titlemap.put("inwhstatus", "入库状态");
		titlemap.put("deliverydate", "预计到货时间");
		titlemap.put("closepaydate", "付款完成时间");
		titlemap.put("closerecdate", "收货完成时间");
		titlemap.put("skuRemark", "SKU备注");
		titlemap.put("orderRemark", "订单备注");
		List<Map<String, Object>> list = this.baseMapper.getPayRecSumReport(param);
		Sheet sheet = workbook.createSheet("Sheet1");
		// 在索引0的位置创建行（最顶端的行）
		Row trow = sheet.createRow(0);
		Object[] titlearray = titlemap.keySet().toArray();
		int maxtitle=0;
		for (int i = 0; i < titlearray.length; i++) {
			Cell cell = trow.createCell(i); // 在索引0的位置创建单元格(左上端)
			Object value = titlemap.get(titlearray[i].toString());
			cell.setCellValue(value.toString());
		}
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
		    Object value = map.get("orderRemark");
				if (value != null) {
					String[] remarks=value.toString().split("\n");
					 if(remarks.length>maxtitle)
							maxtitle=remarks.length;
					}
		}
		for (int i = 1;i<maxtitle; i++) {
			Cell cell = trow.createCell(i+ titlearray.length-1); // 在索引0的位置创建单元格(左上端)
			cell.setCellValue("其他订单备注"+i);
		}
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Row row = sheet.createRow(i + 1);
				Map<String, Object> map = list.get(i);
				for (int j = 0; j < titlearray.length; j++) {
					Cell cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
					String key = titlearray[j].toString();
					Object value = map.get(key);
					if ("inwhstatus".equals(key)&& value!=null) {
						if ("1".equals(value.toString())) {
							value = "入库已完成";
						} else {
							value = "入库未完成";
						}
					}
					if ("paystatus".equals(key) && value!=null) {
						if ("1".equals(value.toString())) {
							value = "付款已完成";
						} else {
							value = "付款未完成";
						}
					}	
					if("auditstatus".equals(key)&& value!=null) {
						String entryid = map.get("entryid").toString();
						PurchaseFormEntry entry = purchaseFormEntryMapper.selectById(entryid);
						value=PurchaseFormEntry.getAuditstatusName(entry);
					}
					if("orderRemark".equals(key)) {
						if (value != null) {
							String[] remarks=value.toString().split("\n");
							for(int rindex=0;rindex<remarks.length;rindex++) {
								if(rindex==0) {
									cell.setCellValue(remarks[rindex]);
								}else {
									Cell cell2 = row.createCell(j+rindex); 
									cell2.setCellValue(remarks[rindex]);
									maxtitle++;
								}
							}
							
						}
					}
					else if (value != null && cell!=null) {
						cell.setCellValue(value.toString());
					}
				
				}
			}
		}
  
	}
	
	
	

	public void setPurchaseSkuItemExcelBook(SXSSFWorkbook workbook, Map<String, Object> param) {
		Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
		titlemap.put("createdate", "订单日期");
		titlemap.put("suppliername", "供应商");
		titlemap.put("number", "订单编码");
		titlemap.put("sku", "SKU");
		titlemap.put("itemprice", "单价");
		titlemap.put("amount", "采购数量");
		titlemap.put("orderprice", "采购金额");
		titlemap.put("totalpay", "付款金额");
		titlemap.put("paystatus", "付款状态");
		titlemap.put("totalin", "入库数量");
		titlemap.put("inwhstatus", "入库状态");
		titlemap.put("deliverydate", "预计到货时间");
		List<Map<String, Object>> list = this.baseMapper.selectByCondition(param);
		Sheet sheet = workbook.createSheet("sheet1");
		// 在索引0的位置创建行（最顶端的行）
		Row trow = sheet.createRow(0);
		Object[] titlearray = titlemap.keySet().toArray();
		for (int i = 0; i < titlearray.length; i++) {
			Cell cell = trow.createCell(i); // 在索引0的位置创建单元格(左上端)
			Object value = titlemap.get(titlearray[i].toString());
			cell.setCellValue(value.toString());
		}
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Row row = sheet.createRow(i + 1);
				Map<String, Object> map = list.get(i);
				for (int j = 0; j < titlearray.length; j++) {
					Cell cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
					String key = titlearray[j].toString();
					Object value = map.get(key);
					if ("inwhstatus".equals(key)) {
						if ("1".equals(value.toString())) {
							value = "入库已完成";
						} else {
							value = "入库未完成";
						}
					}
					if ("paystatus".equals(key)) {
						if ("1".equals(value.toString())) {
							value = "付款已完成";
						} else {
							value = "付款未完成";
						}
					}
					if (value != null) {
						cell.setCellValue(value.toString());
					}
				}
			}
		}
	}
	
	public void getPurchaseRecInfoExcelBook(SXSSFWorkbook workbook, Map<String, Object> param) {
		Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
		titlemap.put("createdate", "订单日期");
		titlemap.put("number", "订单编码");
		titlemap.put("sku", "SKU");
		titlemap.put("totalin", "入库数量");
		titlemap.put("inwhstatus", "入库状态");
		titlemap.put("operator", "收货人");
		titlemap.put("opttime", "收货时间");
		Sheet sheet = workbook.createSheet("sheet1");
		// 在索引0的位置创建行（最顶端的行）
		Row trow = sheet.createRow(0);
		Object[] titlearray = titlemap.keySet().toArray();
		for (int i = 0; i < titlearray.length; i++) {
			Cell cell = trow.createCell(i); // 在索引0的位置创建单元格(左上端)
			Object value = titlemap.get(titlearray[i].toString());
			cell.setCellValue(value.toString());
		}
		
		List<Map<String, Object>> list = this.baseMapper.getEnteyInfo(param);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Row row = sheet.createRow(i + 1);
				Map<String, Object> map = list.get(i);
				for (int j = 0; j < titlearray.length; j++) {
					Cell cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
					String key = titlearray[j].toString();
					Object value = map.get(key);
					if ("inwhstatus".equals(key)) {
						if ("1".equals(value.toString())) {
							value = "入库已完成";
						} else {
							value = "入库未完成";
						}
					}
					if (value != null) {
						cell.setCellValue(value.toString());
					}
				}
			}
		}
	}
	public int savePurchaseForm(UserInfo user,List<PurchaseForm> formList, String planwarehouseid) throws ERPBizException {
		int changecount = 0;
		Warehouse planwarehouse = warehouseService.getSelfWarehouse(planwarehouseid);
		if (formList == null || formList.size() <= 0) {
			throw new ERPBizException("请至少输入一个需要采购的SKU及其数量！");
		}
		for (PurchaseForm form : formList) {
			if(this.save(form)) {
				changecount++ ;
			}
			List<PurchaseFormEntry> entrylist = form.getEntrylist();
			for (int i = 0; i < entrylist.size(); i++) {
				PurchaseFormEntry entry = entrylist.get(i);
				entry.setFormid(form.getId());
				if(purchaseFormEntryService.save(entry)) {
					changecount++;
				}
			}
		}
		if(planwarehouse!=null) {
			purchasePlanService.afterSavePOSubPlan(user ,planwarehouse.getId());
		}
		return changecount;
	}
	
	public List<Map<String, Object>> loadPurchaseFormDate(UserInfo user, String planid, String warehouseid, String ftype,List<String> item_material_list) {
		// 这里的warehouseid是指的self仓库，因为原有逻辑里面purchaseitem表中跟的是self仓库的id，
		// 而采购组装记录表中跟的是正品仓的id（应该也要跟self仓库的id，因为现有逻辑采购和组装都默认在正品仓库了，实际上可以在测试仓库组装采购）
		// 按照现有的逻辑 测试仓库和废品仓库实际上根本用不到 （这里可能是以后修改的地方 做个记录）
		List<Map<String, Object>> date = new ArrayList<Map<String, Object>>();
		String[] warehouseArray = warehouseid.split(",");
		for (int i = 0; i < warehouseArray.length; i++) {
			// 拿到正品仓的仓库，并且用正品仓的warehouseid来拿去组装和采购历史记录
			List<Warehouse> list = warehouseService.getSubWarehouseListByType(warehouseArray[i], "self_usable");
			Map<String, Object> map = new HashMap<String, Object>();
			Warehouse warehouse=null;
			for(int j=0;j<list.size();j++) {
				Warehouse ware = list.get(j);
				if(ware.getIsdefault()==true) {
					warehouse=ware;
				}
			}
			Map<String, Object> precord =null;
			Map<String, Object> arecord =null;
			if(warehouse!=null) {
				precord=this.baseMapper.getLastPurchaseRecord(user.getCompanyid(), warehouse.getId());
				arecord=assemblyFormService.getLastAssRecord(user.getCompanyid(), warehouse.getId());
			}
			map.put("precord", precord);
			map.put("arecord", arecord);
			// 页面显示self仓库的名字
			Warehouse selfWarehouse = warehouseService.getWarehouseByid(warehouseArray[i]);
			map.put("warehouseid", warehouseArray[i]);
			map.put("warehouseName", selfWarehouse.getName());

			// 获取该仓库下的补货规划有没有提交
			PurchaseWareHouseStatus warehouseStatus = purchaseWareHouseStatusService.getById(warehouseArray[i]);
			if (warehouseStatus == null) {
				warehouseStatus = new PurchaseWareHouseStatus();
			}
			warehouseStatus.setOpptime(new Date());
			warehouseStatus.setUserid(user.getId());
			  
			map.put("purchaseStatus", warehouseStatus.getPurchaseStatus());
			map.put("assblyStatus", warehouseStatus.getAssblyStatus());
			date.add(map);
		}
		return date;
	}

	public List<Map<String, Object>> findSupplierByForm(String formid) {
		return this.baseMapper.findSupplierByForm(formid);
	}

	public static String getTomcatWebappsPath(HttpServletRequest request) {
		String tomcatRoot = request.getSession().getServletContext().getRealPath("/");
		String[] foo = tomcatRoot.split("\\\\"); // 注意分割符转义
		StringBuilder tomcatWebAppsBuilder = new StringBuilder();
		int i = 0;
		for (String paths : foo) {
			++i;
			if (i != foo.length) {
				tomcatWebAppsBuilder.append(paths);
				tomcatWebAppsBuilder.append("/");
			}
		}
		String tomcatWebApps = tomcatWebAppsBuilder.toString();
		return tomcatWebApps;
	}
	
	public void downloadPurchasePaymentWord(HttpServletRequest request, HttpServletResponse response, String recid) {
		if(GeneralUtil.isNotEmpty(recid)) {
			Workbook workbook = null;
			ServletOutputStream fOut = null;
			PurchaseFormReceive entryRec = purchaseFormReceiveService.getById(recid);
			if(entryRec!=null) {
				Integer amount=null;
				Integer buyamount=null;
				String type="入";
				String sku="";
				String order="";
				String logisticsNo="";
				String supplier="";
				String operator="";
				String image="";
				String createtime=null;
				String opttime=null;
				amount=entryRec.getAmount();
				opttime=GeneralUtil.fmtDate(entryRec.getOpttime());
				operator=entryRec.getOperator();
		 
				if(operator!=null) {
					Result<Map<String, Object>> info = adminClientOneFeign.getUserByUserId(operator);
					if(Result.isSuccess(info)&&info.getData()!=null) {
						operator=info.getData().get("name")!=null?info.getData().get("name").toString():null;
					}
				}
				if("in".equals(entryRec.getFtype())) {
					type="入";
				}
				if("out".equals(entryRec.getFtype())) {
					type="退";
				}
				if("ch".equals(entryRec.getFtype())) {
					type="换";
				}
				if("clear".equals(entryRec.getFtype())) {
					type="撤销";
				}
				String entryid = entryRec.getFormentryid();
				if(entryid!=null) {
					PurchaseFormEntry entry = purchaseFormEntryMapper.selectById(entryid);
					PurchaseFormEntryLogistics logistics = purchaseFormEntryLogisticsMapper.selectById(entryid);
					if(entry!=null) {
						buyamount=entry.getAmount();
						String supplierid = entry.getSupplier();
						if(supplierid!=null) {
							Customer supplierObj = customerService.getById(supplierid);
							if(supplierObj!=null) {
								supplier=supplierObj.getName();
							}
						}
						String materialid=entry.getMaterialid();
						if(materialid!=null) {
							Material materialObj = materialService.getById(materialid);
							sku=materialObj.getSku();
							String picid = materialObj.getImage();
							if(picid!=null) {
								Picture picture = pictureService.getById(picid);
								if(picture!=null) {
									if(picture.getLocation()!=null) {
										image=picture.getLocation();
									}else {
										image=picture.getUrl();
									}
								}
							}
						}
						String fromid=entry.getFormid();
						PurchaseForm form = this.getById(fromid);
						if(form!=null) {
							createtime=GeneralUtil.fmtDate(form.getCreatedate());
							order=form.getNumber();
						}
						if(logistics!=null) {
							logisticsNo=logistics.getLogisticsid();
						}
					}
					
				}
				//填充至excel里面
				 try {
					 response.setContentType("application/force-download");// 设置强制下载不打开
					 response.addHeader("Content-Disposition", "attachment;fileName=downloadPurchaseReceiveWord" + System.currentTimeMillis() +".xlsx");// 设置文件名
					 String filePath = getTomcatWebappsPath(request)+"temp files/purchasePaymentRecord.xlsx";
					 //String filePath="D:\\purchasePaymentRecord.xlsx";
					 File file = new File(filePath);
					 InputStream inputStream = new FileInputStream(file);
					// 创建新的Excel工作薄
					 workbook = WorkbookFactory.create(inputStream);
					 Sheet sheet = workbook.getSheet("Sheet1");
					 //提前生成样式
				 	CellStyle style=workbook.createCellStyle();
					style.setAlignment(HorizontalAlignment.LEFT);
					org.apache.poi.ss.usermodel.Font font = workbook.createFont();
					font.setFontHeightInPoints((short)10);
					style.setFont(font);
					style.setWrapText(true);
					 //先添加表格需要的行
					  //遍历行row
					 for (int i=0;i<=sheet.getLastRowNum();i++){
						 Row sheetRow = sheet.getRow(i);
						 if (sheetRow == null) {
							 continue;
						 }
						//遍历列cell
						 for (int cellnum = 0; cellnum <= sheetRow.getLastCellNum(); cellnum++) {
							 Cell cell = sheetRow.getCell(cellnum);
							 if (cell == null) {
									continue;
							 }
							 cell.setCellType(CellType.STRING);
							 String cellValue = cell.getStringCellValue().trim();
							 if(GeneralUtil.isNotEmpty(cellValue)) {
									if("$type1".equals(cellValue)) {
										cell.setCellValue(type+"货单");
									}
									if("$type2".equals(cellValue)) {
										cell.setCellValue(type+"库数");
									}
									if("$type3".equals(cellValue)) {
										cell.setCellValue(type+"货人");
									}
									if("$type4".equals(cellValue)) {
										cell.setCellValue(type+"货时间");
									}
									if("$sku".equals(cellValue)) {
										cell.setCellValue(sku);
									}
									if("$num".equals(cellValue)) {
										cell.setCellValue(amount);
									}
									if("$order".equals(cellValue)) {
										cell.setCellValue(order);
									}
									if("$createtime".equals(cellValue)) {
										cell.setCellValue(createtime);
									}
									if("$opttime".equals(cellValue)) {
										cell.setCellValue(opttime);
									}
									if("$buyer".equals(cellValue)) {
										cell.setCellValue(operator);
									}
									if("$supplier".equals(cellValue)) {
										cell.setCellValue(supplier);
									}
									if("$logistics".equals(cellValue)) {
										cell.setCellValue(logisticsNo);
									}
									if("$buynum".equals(cellValue)) {
										cell.setCellValue(buyamount);
									}
									if("$image".equals(cellValue)) {
										BufferedImage bufferImg = null;
										if(image.indexOf("photo/materialImg")>=0) {
											image=image.replace("photo/materialImg", IniConfig.photoServerUrl() + "/materialImg");
											Drawing<?> draw = sheet.createDrawingPatriarch();
											ClientAnchor anchor = draw.createAnchor(50, 50, 255, 255, cell.getColumnIndex(), cell.getRowIndex(), cell.getColumnIndex()+1, cell.getRowIndex()+2);
											ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
											bufferImg = ImageIO.read(new URL(image));
											ImageIO.write(bufferImg, "JPEG", byteArrayOut);
											org.apache.poi.ss.usermodel.Picture picexcel = draw.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), Workbook.PICTURE_TYPE_JPEG));
											picexcel.setLineStyleColor(1, 1, 1);
											cell.setCellValue("");
										}else {
											cell.setCellValue("暂无图片");
										}
										
									}
								}
							 
							 
							 
						 }
					 }
					 fOut = response.getOutputStream();
					 workbook.write(fOut);
				 }catch (IOException e) {
						e.printStackTrace();
				 } catch (EncryptedDocumentException e) {
						e.printStackTrace();
				 } catch (InvalidFormatException e) {
						e.printStackTrace();
				 } finally {
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
				
			}else {
				throw new ERPBizException("未找到该笔记录！");
			}
		}
	
	}

	public Map<String, Object> getPurchaseNumAllStatus(Map<String, Object> param) {
		//待审核
		return this.baseMapper.selectPurchaseNumAllStatus(param);
	}

	public Map<String, Object> uploadPurchaseListByExcel(Sheet sheet, Map<String, Object> map) {
		Map<String, Object> skuMap = new HashMap<String, Object>();
		Map<String, Object> priceMap = new HashMap<String, Object>();
		Map<String, Object> supplierMap = new HashMap<String, Object>();
		Map<String, Object> dateMap = new HashMap<String, Object>();
		List<String> skuList = new ArrayList<String>();
		List<String> otherList = new ArrayList<String>();
		boolean isok = true;
		for(int i = 1; i <= sheet.getLastRowNum(); i++){
			Row crow = sheet.getRow(i);
			if(crow==null){
				continue;
			}
			Cell cell = crow.getCell(0);
			if(cell==null){
				continue;
			}
			cell.setCellType(CellType.STRING);
			String sku = cell.getStringCellValue();
			if (GeneralUtil.isNotEmpty(sku)) {
				Material material = materialService.selectBySKU(map.get("shopid").toString(), sku.trim());
				if(material==null){
					isok = false;
					skuList.add(sku);
				} else {
					Double qty = crow.getCell(1).getNumericCellValue();
					if(qty>0){
						boolean flag = true;
						Double price = crow.getCell(2)==null?null:crow.getCell(2).getNumericCellValue();
						if(price!=null && price<=0){
							flag = false;
							otherList.add(sku);
						} else {
							priceMap.put(sku, price);
						}
						String supplier = crow.getCell(3)==null?null:crow.getCell(3).getStringCellValue();
						//供应商是否存在
						if(supplier!=null){
							Customer customer = customerService.findByName(map.get("shopid").toString(), supplier.trim());
							if(customer==null){
								flag = false;
								otherList.add(sku);
							} else {
								supplierMap.put(sku, customer.getId()+","+customer.getName());
							}
						}
						//预计到货日期格式是否正确
						Cell datecell = crow.getCell(4);
						if(datecell!=null){
							datecell.setCellType(CellType.NUMERIC);
							try {
								Date effectivedate = datecell.getDateCellValue();
								if(effectivedate!=null){
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
									String effectivedatestr = sdf.format(effectivedate);
									String today = sdf.format(new Date());
									if(effectivedate.before(new Date())&&!effectivedatestr.equals(today)){
										flag = false;
										otherList.add(sku);
									} else {
										dateMap.put(sku,effectivedatestr);
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
								flag = false;
								otherList.add(sku);
							}
						}
						if(flag){
							skuMap.put(sku, (int)Math.floor(qty));
						} else {
							isok = false;
						}
					} else {
						isok = false;
						skuList.add(sku);
					}
				}
			}
		}
		map.put("searchsku", skuMap.keySet());
		if (isok) {
			List<Map<String, Object>> result = materialService.findAllByCondition(map);
			for(Map<String, Object> rmap : result){
				List<Map<String, Object>> historylist = materialService.selectProPriceHisById(rmap.get("id").toString());
				rmap.put("amount", skuMap.get(rmap.get("sku")));
				rmap.put("num", skuMap.get(rmap.get("sku")));
				rmap.put("checkdsub", false);
				String pricestr="";
				////////////处理选填信息、、、、、、、
				if(priceMap.get(rmap.get("sku"))!=null){
					rmap.put("price", priceMap.get(rmap.get("sku")));
				}
				if(rmap.get("price")!=null && rmap.get("num")!=null) {
					rmap.put("totalprice",Math.round((Float.parseFloat(rmap.get("price").toString())*Integer.parseInt(rmap.get("num").toString())) * 100) / 100f);
				}
				if(historylist!=null && historylist.size()>0) {
					 for(int j=0;j<historylist.size();j++) {
						 Map<String,Object> history = historylist.get(j);
						 if(history!=null) {
							 if(history.get("price")!=null) {
								 String price=history.get("price").toString();
								 int len = price.indexOf(".");
								 price=price.substring(0, len+3);
								 pricestr+="历史价格("+
					    				   GeneralUtil.formatDate(GeneralUtil.getDate(history.get("opttime")))+
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
				rmap.put("pricestr", pricestr);
				if(supplierMap.get(rmap.get("sku"))!=null){
					String temp = supplierMap.get(rmap.get("sku")).toString();
					rmap.put("supplierid", temp.split(",")[0]);
					rmap.put("supplier", temp.split(",")[1]);
				}
				if(dateMap.get(rmap.get("sku"))!=null){
					rmap.put("effectivedate", dateMap.get(rmap.get("sku")));
				}
				
			}
			map.put("result", result);
			map.put("msg", "上传成功");
			return map;
		} else {
			String msg = "导入失败,SKU:";
			if(skuList.size()>0){
				for (int i = 0; i < skuList.size(); i++) {
					msg += skuList.get(i) + " ";
				}
				msg += "匹配失败，请检查本地SKU是否存在或采购数量是否正确。";
			}
			if(otherList.size()>0){
				for (int i = 0; i < otherList.size(); i++) {
					msg += otherList.get(i) + " ";
				}
				msg += "请检查这些SKU对应单价是否为正数、供应商是否存在，预计到货日期格式是否正确，日期是否在今天之后。";
			}
			map.put("msg", msg);
			return map;
		}
	}

	public Map<String, Object> getPurchaseRecordInfo(String reciveId) {
		Map<String, Object> maps=new HashMap<String, Object>();
		PurchaseFormReceive receive = purchaseFormReceiveService.getById(reciveId);
		Integer amount = receive.getAmount();//入库数
		String ftype = receive.getFtype();
		String entryid = receive.getFormentryid();
		String opttime = GeneralUtil.fmtDate(receive.getOpttime());
		String operator = receive.getOperator();
		UserInfo user =  UserInfoContext.get();
		if(user!=null) {
			if(operator!=null) {
				Result<Map<String, Object>> info = adminClientOneFeign.getUserByUserId(operator);
				if(Result.isSuccess(info)&&info.getData()!=null) {
					operator=info.getData().get("name")!=null?info.getData().get("name").toString():null;
				}
			}
		}
		if("in".equals(ftype)) {
			ftype="入";
		}
		if("out".equals(ftype)) {
			ftype="退";
		}
		if("ch".equals(ftype)) {
			ftype="换";
		}
		if("clear".equals(ftype)) {
			ftype="撤销";
		}
		int buyamount = 0;
		String supplier="";
		String sku="";
		String image="";
		String logisticsNo="";
		String ordertime="";
		String orderNumber="";
		if(GeneralUtil.isNotEmpty(entryid)) {
			PurchaseFormEntry entry = purchaseFormEntryMapper.selectById(entryid);
			PurchaseFormEntryLogistics logistics = purchaseFormEntryLogisticsMapper.selectById(entryid);
			if(entry!=null) {
				buyamount=entry.getAmount();//采购总数
				String supplierid = entry.getSupplier();
				if(supplierid!=null) {
					Customer supplierObj = customerService.getById(supplierid);
					if(supplierObj!=null) {
						supplier=supplierObj.getName();
					}
				}
				String materialid=entry.getMaterialid();
				if(materialid!=null) {
					Material materialObj = materialService.getById(materialid);
					sku=materialObj.getSku();
					String picid = materialObj.getImage();
					if(picid!=null) {
						Picture picture = pictureService.getById(picid);
						if(picture!=null) {
							if(picture.getLocation()!=null) {
								image=picture.getLocation();
							}else {
								image=picture.getUrl();
							}
						}
						image=fileUpload.getPictureImage( image) ;
					}
				}
				String fromid=entry.getFormid();
				PurchaseForm form = this.getById(fromid);
				if(form!=null) {
					ordertime=GeneralUtil.fmtDate(form.getCreatedate());
					orderNumber=form.getNumber();
				}
				if(logistics!=null) {
					logisticsNo=logistics.getLogisticsid();
				}
			
			}
		}
		maps.put("title", ftype+"货单");
		maps.put("amount", amount);
		maps.put("buyamount", buyamount);
		maps.put("supplier", supplier);
		maps.put("opttime",opttime);
		maps.put("sku", sku);
		maps.put("image", image);
		maps.put("logisticsNo", logisticsNo);
		maps.put("ordertime", ordertime);
		maps.put("orderNumber", orderNumber);
		maps.put("operator", operator);
		return maps;
	}



	public List<Map<String, Object>> selectNeedSendMsgShop() {
		return purchaseFormEntryMapper.selectNeedSendMsgShop();
	}

	public List<Map<String, Object>> selectPurchaseNotify(String shopid) {
		return purchaseFormEntryMapper.selectPurchaseNotify(shopid);
	}


	@Override
	public Map<String, Object> deleteEntry(UserInfo user,String id) {
		Map<String, Object> map=new HashMap<String, Object>();
		PurchaseFormEntry entry = purchaseFormEntryService.getById(id);
		cancelReady(user,entry);
		if(entry!=null) {
			entry.setAuditstatus(0);
			entry.setOpttime(new Date());
			entry.setOperator(user.getId());
			boolean isok = purchaseFormEntryService.updateById(entry);
			if(isok==true) {
				map.put("isok", isok);
				return map;
			}else {
				return null;
			}
		}else {
			return null;
		}
	}
    public void cancelReady(UserInfo user,PurchaseFormEntry entry) {
    	InventoryParameter invpara = new InventoryParameter();
		invpara.setFormid(entry.getFormid());
		invpara.setMaterial(entry.getMaterialid());
		invpara.setOperator(user.getId());
		invpara.setOpttime(new Date());
		invpara.setStatus(EnumByInventory.alReady);
		invpara.setShopid(user.getCompanyid());
		invpara.setFormtype("purchase");
		PurchaseForm ph = this.baseMapper.selectById(entry.getFormid());
		invpara.setNumber(ph.getNumber());
		invpara.setAmount(entry.getAmount());
		invpara.setWarehouse(ph.getWarehouseid());
	    inventoryService.SubStockByStatus(invpara, Status.inbound, Operate.cancel);
    }
	@Override
	public PurchaseFormEntry recallEntry(UserInfo user,String id) {
		PurchaseFormEntry entry = purchaseFormEntryService.getById(id);
		cancelReady(user,entry);
		PurchaseFormEntryHistory history=new PurchaseFormEntryHistory();
		//把entry先存到history 然后更新entry成为新的
		BeanUtil.copyProperties(entry, history);//左边给右边赋值
		int count = purchaseFormEntryHistoryMapper.insert(history);
		if(count>0) {
			entry.setOpttime(new Date());
			entry.setOperator(user.getId());
			entry.setIschange(true);
			entry.setAuditstatus(1);
			purchaseFormEntryService.updateById(entry);
		}
		return entry;
	}

	@Override
	public List<Map<String, Object>> getEntryData(String id) {
		return purchaseFormEntryHistoryMapper.findHistoryById(id);
	}

	@Override
	public List<Map<String, Object>> getLastFormsByMaterials(List<String> ids) {
		// TODO Auto-generated method stub
		return this.baseMapper.findeLastsByMaterialids(ids);
	}


}
