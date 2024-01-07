package com.wimoor.erp.purchase.controller;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.common.user.UserLimitDataType;
import com.wimoor.erp.api.AdminClientOneFeignManager;
import com.wimoor.erp.assembly.service.IAssemblyFormEntryService;
import com.wimoor.erp.assembly.service.IAssemblyService;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.finance.pojo.entity.FinAccount;
import com.wimoor.erp.finance.pojo.entity.FinanceProject;
import com.wimoor.erp.finance.service.IFaccountService;
import com.wimoor.erp.finance.service.IFinanceProjectService;
import com.wimoor.erp.inventory.service.IInventoryHisService;
import com.wimoor.erp.material.service.IMaterialConsumableService;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.material.service.IMaterialSupplierService;
import com.wimoor.erp.material.service.IStepWisePriceService;
import com.wimoor.erp.purchase.alibaba.service.IPurchaseFormEntryAlibabaInfoService;
import com.wimoor.erp.purchase.pojo.dto.PaymentReportDTO;
import com.wimoor.erp.purchase.pojo.dto.PaymentSaveDTO;
import com.wimoor.erp.purchase.pojo.dto.PurchaseFormDTO;
import com.wimoor.erp.purchase.pojo.dto.PurchaseFormDownloadDTO;
import com.wimoor.erp.purchase.pojo.dto.PurchaseSaveDTO;
import com.wimoor.erp.purchase.pojo.dto.ReceiveReportDTO;
import com.wimoor.erp.purchase.pojo.dto.ReceiveSaveDTO;
import com.wimoor.erp.purchase.pojo.entity.PurchaseForm;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormEntry;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormPayment;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormPaymentMethod;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormReceive;
import com.wimoor.erp.purchase.pojo.vo.PurchaseFormReceiveVo;
import com.wimoor.erp.purchase.service.IPurchaseFinanceFormService;
import com.wimoor.erp.purchase.service.IPurchaseFormEntryService;
import com.wimoor.erp.purchase.service.IPurchaseFormPaymentService;
import com.wimoor.erp.purchase.service.IPurchaseFormReceiveService;
import com.wimoor.erp.purchase.service.IPurchaseFormService;
import com.wimoor.erp.purchase.service.impl.PurchaseFormPaymentServiceImpl;
import com.wimoor.erp.util.LockCheckUtils;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@Api(tags = "采购单接口")
@RestController
@SystemControllerLog( "采购单接口")
@RequestMapping("/api/v1/purchase_form")
@RequiredArgsConstructor
public class PurchaseFormController {
	
	private final IPurchaseFormService purchaseFormService;
	private final IPurchaseFormEntryService purchaseFormEntryService;
	private final IPurchaseFormEntryAlibabaInfoService purchaseFormEntryAlibabaInfoService;
	private final IMaterialService materialService;
	final IMaterialConsumableService iMaterialConsumableService;
	private final IStepWisePriceService stepWisePriceService;
	final IAssemblyService assemblyService;
	final IAssemblyFormEntryService assemblyFormEntryService;
    final IMaterialSupplierService iMaterialSupplierService;
    final IPurchaseFinanceFormService purchaseFinanceFormService;
    final AdminClientOneFeignManager  adminClientOneFeign;
    final IFaccountService iFaccountService;
    final IFinanceProjectService iFinanceProjectService;
    final IInventoryHisService iInventoryHisService;
    final IFaccountService faccountService;
    final IPurchaseFormPaymentService iPurchaseFormPaymentService;
    final IPurchaseFormReceiveService iPurchaseFormReceiveService;
    final IWarehouseService iWarehouseService;
	@GetMapping("/getdetail")
	public Result<Map<String, Object>> getDetailAction(String id) throws ERPBizException {
		UserInfo userinfo = UserInfoContext.get();
		String shopid = userinfo.getCompanyid();
		return Result.success(purchaseFormService.getDetailMap(id, shopid));
	}
	
	@GetMapping("/getRecdetail")
	public Result<Map<String, Object>> getRecDetailAction(String id,String ftype,String actiontype) throws ERPBizException {
		UserInfo userinfo = UserInfoContext.get();
		String shopid = userinfo.getCompanyid();
		return Result.success(purchaseFormService.getTraceDetailMap(id, shopid, ftype,actiontype));
	}
	
	@GetMapping("/clearRec")
	@SystemControllerLog( "撤回单次入库")
	@Transactional
	public Result<PurchaseFormEntry> clearReceiveAction(String recid) {
		UserInfo userinfo = UserInfoContext.get();
        PurchaseFormEntry entry = iPurchaseFormReceiveService.clearReceiveItem(userinfo,recid);
		return Result.success(entry) ;
	}
	
	@GetMapping("/clearRecAll")
	@SystemControllerLog( "撤回所有入库")
	@Transactional
	public Result<PurchaseFormEntry> clearReceiveAllAction(String entryid) {
		UserInfo userinfo = UserInfoContext.get();
        PurchaseFormEntry entry = iPurchaseFormReceiveService.clearReceive(entryid,userinfo);
		return Result.success(entry) ;
	}
	
	@SystemControllerLog( "采购单收货")
	@PostMapping("/rec")
	@Transactional
	public Result<Map<String, Object>> receiveAction(@RequestBody ReceiveSaveDTO dto) throws ERPBizException {
		UserInfo userinfo = UserInfoContext.get();
		String shopid = userinfo.getCompanyid();
		String entryid = dto.getEntryid();
		String recid = dto.getRecid();
		String amount = dto.getAmount();
		String warehouse = dto.getWarehouseid();
		String ftype = dto.getFtype();
		String status = dto.getStatus();
		String remark = dto.getRemark();
		Integer iamount;
		LockCheckUtils lock = new LockCheckUtils("purchaserec"+dto.getEntryid());
		try {
				if (StrUtil.isNotBlank(amount)) {
					try {
						iamount = Integer.parseInt(amount);
					} catch (Exception e) {
						throw new ERPBizException("数量不能包含非数字字符");
					}
				} else {
					iamount = 0;
				}
				PurchaseFormReceive rec = new PurchaseFormReceive();
				if (StrUtil.isNotBlank(recid)&&!recid.equals("null")) {
					rec.setId(recid);
				}else {
					rec.setId(iWarehouseService.getUUID());
				}
				rec.setFormentryid(entryid);
				rec.setAmount(iamount);
				rec.setWarehouseid(warehouse);
				rec.setOpttime(new Date());
				rec.setOperator(userinfo.getId());
				rec.setFtype(ftype);
				rec.setRemark(remark);
				PurchaseFormEntry entry = purchaseFormEntryService.getById(entryid);
				List<PurchaseFormReceive> reclist = iPurchaseFormReceiveService.lambdaQuery().eq(PurchaseFormReceive::getFormentryid, entryid).list();
				if(reclist!=null&&reclist.size()>0) {
					for(PurchaseFormReceive item:reclist) {
						if(item!=null
							&&item.getOpttime()!=null
							&&item.getFtype()!=null
							&&item.getAmount()!=null
							&&GeneralUtil.distanceOfSecond(item.getOpttime(), new Date())<120
							&&!item.getFtype().equals("clear")
							&&item.getAmount()==iamount) {
							throw new BizException("请勿重复操作，如需入库两次请稍等2分钟后重试");
						}
					}
				}
				if("2".equals(status)) {
					iPurchaseFormReceiveService.restartRec(userinfo, entry);
				}else if("1".equals(status)) {
					iPurchaseFormReceiveService.closeRec(userinfo, entry);
				}else {
					iPurchaseFormReceiveService.saveReceive(userinfo, rec, entry);
				}
				Thread.sleep(1000);
				return Result.success(purchaseFormService.getTraceDetailMap(entryid, shopid, "rec",ftype));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			lock.clear();
			return Result.success(purchaseFormService.getTraceDetailMap(entryid, shopid, "rec",ftype));
		}finally {
			lock.clear();
		}
	}
	

	@ApiOperation(value = "根据id 获取产品信息")
	@PostMapping("/list")
	public Result<IPage<Map<String, Object>>> getListData(@RequestBody PurchaseFormDTO dto) throws BizException {
		UserInfo userinfo = UserInfoContext.get();
		Map<String, Object> param = new HashMap<String, Object>();
		  param.put("userid", userinfo.getId());
		  param.put("shopid",userinfo.getCompanyid());
		  param.put("search", null);
		  param.put("fromDate", null);
		  param.put("toDate", null);
		  param.put("auditstatus", null);
		  param.put("supplierid", null);
		  param.put("datetype", null);
		  param.put("name",null);
		  param.put("supplier", null);
		  param.put("categoryId", null);
		  param.put("issfg", null);
		  param.put("remark", null);
		  param.put("owner",null);
		  param.put("auditstatusparam", null);
		  
		  String search = dto.getSearch();
			if (GeneralUtil.isNotEmpty(search)) {
				search ="%"+ search.trim() + "%";
			} else {
				search = null;
			}
			param.put("search", search);
			String datetype = dto.getDatetype();
			if (GeneralUtil.isEmpty(datetype))
				datetype = "createdate";
			param.put("datetype", datetype.trim());
			String auditstatus =dto.getAuditstatus();
			if (GeneralUtil.isNotEmpty(auditstatus)) {
				param.put("auditstatus", auditstatus);
			}
			String supplierid = dto.getSupplierid();
			if (GeneralUtil.isNotEmpty(supplierid)) {
				param.put("supplierid", supplierid);
			}
			String groupid = dto.getGroupid();
			if (StrUtil.isNotEmpty(groupid)) {
				param.put("groupid", groupid);
			}
			String warehouse = dto.getWarehouseid();
			if (GeneralUtil.isNotEmpty(warehouse)) {
				param.put("warehouse", warehouse);
			}
			String name = dto.getName();
			if (GeneralUtil.isNotEmpty(name)) {
				param.put("name",  "%" + name.trim() + "%");
			}
			String categoryId =dto.getCategoryid();
			if (GeneralUtil.isNotEmpty(categoryId)) {
				param.put("categoryId", categoryId);
			}
			String issfg = dto.getIssfg();
			if (GeneralUtil.isNotEmpty(issfg)) {
				param.put("issfg", issfg);
			}
			String remark = dto.getRemark();
			if (GeneralUtil.isNotEmpty(remark)) {
				param.put("remark", "%" + remark.trim() + "%");
			}
			String owner =dto.getOwner();
			if (GeneralUtil.isNotEmpty(owner)) {
				param.put("owner", owner);
			}
			String formid = dto.getFormid();
			if (GeneralUtil.isNotEmpty(formid)) {
				param.put("formid", formid);
			} else {
				String fromDate = dto.getFromDate();
				if (GeneralUtil.isNotEmpty(fromDate)) {
					param.put("fromDate", fromDate.trim());
				}
				String toDate = dto.getToDate();
				if (GeneralUtil.isNotEmpty(toDate)) {
					param.put("toDate", toDate.trim() + " 23:59:59");
				}
			}
			String auditstatusparam =dto.getAuditstatusparam();
			if (GeneralUtil.isNotEmpty(auditstatus)) {
				param.put("auditstatusparam", auditstatusparam);
			}
			if (userinfo.isLimit(UserLimitDataType.owner)) {
				param.put("myself", userinfo.getId());
			}
		
			String ftype = dto.getFtype();
			List<String> entrylist = null;
			Set<String> entrySet = new HashSet<String>();
			if (GeneralUtil.isNotEmpty(ftype)) {
				param.put("ftype", ftype);
				if ("logistics".equals(ftype)) {
					if (GeneralUtil.isNotEmpty(search) && formid == null) {
						search = search.replaceAll("%", "");
						entrylist = purchaseFormEntryAlibabaInfoService.getEntryIdList(search, userinfo.getCompanyid());
						if (entrylist != null && entrylist.size() > 0) {
							param.put("entrylist", entrylist);
							param.put("search", "");
						} else {
							return Result.success(null);
						}
					}
				}
			}
			param.put("shopid",userinfo.getCompanyid());
		    IPage<Map<String, Object>> list = purchaseFormService.getPurchaseFormEntry(dto.getPage(), param);
			if (GeneralUtil.isNotEmpty(formid) && list.getRecords() != null && list.getSize() > 0) {
				if (entrylist == null) {
					return Result.success(list);
				}
				for (String mid : entrylist) {
					entrySet.add(mid);
				}
				for (Map<String, Object> item : list.getRecords()) {
					Object entryid = item.get("id");
					if (entryid != null && entrySet.contains(entryid)) {
						item.put("state", 1);
					} else {
						item.put("state", 0);
					}
				}
         }
		 return Result.success(list);
	}
	
	@PostMapping("/getListByOrder")
	public Result<IPage<Map<String, Object>>> getListByOrder(@RequestBody PurchaseFormDTO dto) throws BizException {
		Map<String, Object> param = new HashMap<String, Object>();
		UserInfo user = UserInfoContext.get();
		param.put("userid", user.getId());
		param.put("shopid", user.getCompanyid());
		String search = dto.getSearch();
		if (StrUtil.isNotEmpty(search)) {
			search = "%"+search.trim() + "%";
		} else {
			search = null;
		}
		param.put("search", search);
		String fromDate = dto.getFromDate();
		if (StrUtil.isNotEmpty(fromDate)) {
			param.put("fromDate", fromDate.trim());
		} 
	 
		String toDate = dto.getToDate();
		if (StrUtil.isNotEmpty(toDate)) {
			param.put("toDate", toDate.trim());
		} 
		String supplierid = dto.getSupplierid();
		if (StrUtil.isNotEmpty(supplierid)) {
			param.put("supplierid", supplierid);
		}
		String groupid = dto.getGroupid();
		if (StrUtil.isNotEmpty(groupid)) {
			param.put("groupid", groupid);
		}
		String warehouse = dto.getWarehouseid();
		if (StrUtil.isNotEmpty(warehouse)) {
			param.put("warehouse", warehouse);
		}
		String auditstatus = dto.getAuditstatus();
		if (StrUtil.isNotEmpty(auditstatus)) {
			param.put("auditstatus", auditstatus);
		}
		String auditstatusparam = dto.getAuditstatusparam();
		if (StrUtil.isNotEmpty(auditstatusparam)) {
			param.put("auditstatusparam", auditstatusparam);
		}
		String datetype = dto.getDatetype();
		if (StrUtil.isEmpty(datetype))
			datetype = "createdate";
		param.put("datetype", datetype.trim());
		String ftype = dto.getFtype();
		if (StrUtil.isNotEmpty(ftype)) {
			param.put("ftype", ftype);
			if ("logistics".equals(ftype)) {
				if (StrUtil.isNotEmpty(search)) {
					search=search.replaceAll("%", "");
					List<String> list = purchaseFormEntryAlibabaInfoService.getEntryIdList(search,user.getCompanyid());
					if (list != null && list.size() > 0) {
						param.put("entrylist", list);
						param.put("search", null);
					}
				}
			}
		}
		if (user.isLimit(UserLimitDataType.owner)) {
			param.put("myself", user.getId());
		}
		return Result.success(purchaseFormService.getPurchaseForm(dto.getPage(),param));
	}
	

	@ApiOperation("获取采购付款明细列表")
	@PostMapping("/getPaymentReport")
	public Result<IPage<Map<String,Object>>> getPaymentReport(@RequestBody PaymentReportDTO condition){
		UserInfo userinfo = UserInfoContext.get();
		Map<String, Object> param = new HashMap<String, Object>();
		String shopid = userinfo.getCompanyid();
		param.put("shopid", shopid);
		if (StrUtil.isNotBlank(condition.getSearch())) {
			param.put("search", condition.getSearch().trim() + "%");
		}else{
			param.put("search", null);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		if(condition.getDatetype()!=null) {
			param.put("datetype",condition.getDatetype());
		}else {
			param.put("datetype","paydate");
		}
		if (StrUtil.isNotBlank(condition.getFromDate())) {
			param.put("fromDate", condition.getFromDate().trim());
		} else {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, -30);
			String fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
			param.put("fromDate", fromDate);
		}
		if (StrUtil.isNotBlank(condition.getToDate())) {
			param.put("endDate", condition.getToDate().trim()+" 23:59:59");
		} else {
			String toDate = GeneralUtil.formatDate(new Date(), sdf);
			param.put("endDate", toDate+" 23:59:59");
		}
		if (StrUtil.isNotBlank(condition.getSearchtype())) {
			param.put("searchtype", condition.getSearchtype());
		}else {
			param.put("searchtype", "sku");
		}
		if (StrUtil.isNotBlank(condition.getWarehouseid())) {
			param.put("warehouseid", condition.getWarehouseid());
		}else {
			param.put("warehouseid", null);
		}
		if (StrUtil.isNotBlank(condition.getSettlementid())) {
			param.put("settlementid", condition.getSettlementid());
		}else {
			param.put("settlementid", null);
		}
		if (StrUtil.isNotBlank(condition.getSupplierid())) {
			param.put("supplierid", condition.getSupplierid());
		}else {
			param.put("supplierid", null);
		}
		if (StrUtil.isNotBlank(condition.getPaymethod())) {
			param.put("paymethod", condition.getPaymethod());
		}else {
			param.put("paymethod", null);
		}
		if (StrUtil.isNotBlank(condition.getProjectid())) {
			param.put("projectid", condition.getProjectid());
		}else {
			param.put("projectid", null);
		}
		Page<PurchaseFormReceiveVo> page=condition.getPage();
		IPage<Map<String,Object>> list = iPurchaseFormPaymentService.getPaymentReport(page,param);
		return Result.success(list);
	}
	
	@ApiOperation("下载采购入库明细列表Excel")
    @PostMapping("/getPaymentReportExcel")
    public  void getPaymentReportExcel(@RequestBody PaymentReportDTO condition,HttpServletResponse response) {
    	UserInfo userinfo = UserInfoContext.get();
		// 创建新的Excel工作薄
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		// 将数据写入Excel
		Map<String, Object> param = new HashMap<String, Object>();
		String shopid = userinfo.getCompanyid();
		param.put("shopid", shopid);
		if (StrUtil.isNotBlank(condition.getSearch())) {
			param.put("search", condition.getSearch() + "%");
		}else{
			param.put("search", null);
		}
		if(condition.getDatetype()!=null) {
			param.put("datetype",condition.getDatetype());
		}else {
			param.put("datetype","paydate");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		if (StrUtil.isNotBlank(condition.getFromDate())) {
			param.put("fromDate", condition.getFromDate().trim());
		} else {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, -30);
			String fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
			param.put("fromDate", fromDate);
		}
		if (StrUtil.isNotBlank(condition.getToDate())) {
			param.put("endDate", condition.getToDate().trim()+" 23:59:59");
		} else {
			String toDate = GeneralUtil.formatDate(new Date(), sdf);
			param.put("endDate", toDate+" 23:59:59");
		}
		if (StrUtil.isNotBlank(condition.getSearchtype())) {
			param.put("searchtype", condition.getSearchtype());
		}else {
			param.put("searchtype", "sku");
		}
		if (StrUtil.isNotBlank(condition.getWarehouseid())) {
			param.put("warehouseid", condition.getWarehouseid());
		}else {
			param.put("warehouseid", null);
		}
		if (StrUtil.isNotBlank(condition.getSupplierid())) {
			param.put("supplierid", condition.getSupplierid());
		}else {
			param.put("supplierid", null);
		}
		if (StrUtil.isNotBlank(condition.getPaymethod())) {
			param.put("paymethod", condition.getPaymethod());
		}else {
			param.put("paymethod", null);
		}
		if (StrUtil.isNotBlank(condition.getProjectid())) {
			param.put("projectid", condition.getProjectid());
		}else {
			param.put("projectid", null);
		}
		try {
			iPurchaseFormPaymentService.setPaymentReportExcel(workbook, param);
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=purchaseFormInWare" + System.currentTimeMillis() + ".xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@ApiOperation("获取采购入库明细列表")
	@PostMapping("/getReceiveReport")
	public Result<List<PurchaseFormReceiveVo>> getReceiveReport(@RequestBody ReceiveReportDTO condition){
		UserInfo userinfo = UserInfoContext.get();
		Map<String, Object> param = new HashMap<String, Object>();
		String shopid = userinfo.getCompanyid();
		param.put("shopid", shopid);
		if (StrUtil.isNotBlank(condition.getSearch())) {
			param.put("search", condition.getSearch() + "%");
		}else{
			param.put("search", null);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		if (StrUtil.isNotBlank(condition.getFromDate())) {
			param.put("fromDate", condition.getFromDate().trim());
		} else {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, -30);
			String fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
			param.put("fromDate", fromDate);
		}
		if (StrUtil.isNotBlank(condition.getToDate())) {
			param.put("endDate", condition.getToDate().trim()+" 23:59:59");
		} else {
			String toDate = GeneralUtil.formatDate(new Date(), sdf);
			param.put("endDate", toDate+" 23:59:59");
		}
		if (StrUtil.isNotBlank(condition.getSearchtype())) {
			param.put("searchtype", condition.getSearchtype());
		}else {
			param.put("searchtype", "sku");
		}
		if (StrUtil.isNotBlank(condition.getWarehouseid())) {
			param.put("warehouseid", condition.getWarehouseid());
		}else {
			param.put("warehouseid", null);
		}
		Page<PurchaseFormReceiveVo> page=condition.getPage();
		IPage<PurchaseFormReceiveVo> list = iPurchaseFormReceiveService.getReceiveReport(page,param);
		return Result.success(list.getRecords(),list.getTotal());
	}
	
	    @ApiOperation("下载采购入库明细列表Excel")
	    @GetMapping("/getReceiveReportExcel")
	    public  void getReceiveReportExcel(@ApiParam("仓库ID")@RequestParam String warehouseid,
	    		@ApiParam("SKU，订单编码查询")@RequestParam String search,
	    		@ApiParam("查询类型[sku,number]")@RequestParam String searchtype,
	    		@ApiParam("开始日期")@RequestParam String fromDate,
	    		@ApiParam("结束日期")@RequestParam String toDate,
	    		HttpServletResponse response) {
	    	UserInfo userinfo = UserInfoContext.get();
			// 创建新的Excel工作薄
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			// 将数据写入Excel
			Map<String, Object> param = new HashMap<String, Object>();
			String shopid = userinfo.getCompanyid();
			param.put("shopid", shopid);
			if (StrUtil.isNotBlank(search)) {
				param.put("search", search + "%");
			}else{
				param.put("search", null);
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			if (StrUtil.isNotBlank(fromDate)) {
				param.put("fromDate", fromDate.trim());
			} else {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH, -30);
				fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
				param.put("fromDate", fromDate);
			}
			if (StrUtil.isNotBlank(toDate)) {
				param.put("endDate", toDate.trim()+" 23:59:59");
			} else {
				toDate = GeneralUtil.formatDate(new Date(), sdf);
				param.put("endDate", toDate+" 23:59:59");
			}
			if (StrUtil.isNotBlank(searchtype)) {
				param.put("searchtype", searchtype);
			}else {
				param.put("searchtype", "sku");
			}
			if (StrUtil.isNotBlank(warehouseid)) {
				param.put("warehouseid", warehouseid);
			}else {
				param.put("warehouseid", null );
			}
			try {
				iPurchaseFormReceiveService.setReceiveReportExcelBook(workbook, param);
				response.setContentType("application/force-download");// 设置强制下载不打开
				response.addHeader("Content-Disposition", "attachment;fileName=purchaseFormInWare" + System.currentTimeMillis() + ".xlsx");// 设置文件名
				ServletOutputStream fOut = response.getOutputStream();
				workbook.write(fOut);
				workbook.close();
				fOut.flush();
				fOut.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	
		@GetMapping("/refreshAlibabaOrder")
		public Result<Object> refreshAlibabaOrderAction() throws BizException {
			try {
			    iInventoryHisService.runTask();
			}catch(Exception e) {
				e.printStackTrace();
			}
			try {
				purchaseFormEntryAlibabaInfoService.refreshAblibabaDateTask();
				
			}catch(Exception e) {
				e.printStackTrace();
			}
			return Result.success();
		}
		
		@GetMapping("/getPriceBySupplier")
		public Result<Map<String, Object>> getPriceBySupplierAction(String supplierid,String materialid,Integer amount){
			Map<String,Object> map=iMaterialSupplierService.getPriceBySupplier(supplierid,materialid,amount);
			return Result.success(map);
		}
		
		@ApiOperation(value = "添加采购单")
		@Transactional
		@PostMapping("/saveData")
		public Result<Map<String, Object>> saveDataAction(@RequestBody PurchaseSaveDTO dto){
			return Result.success(purchaseFormService.savePurchaseDataAction(dto));
		}
		
		@SuppressWarnings("deprecation")
		@GetMapping("/downExcelTemp")
		public void downExcelTempAction(HttpServletResponse response) {
			try {
				// 创建新的Excel工作薄
				@SuppressWarnings("resource")
				SXSSFWorkbook workbook = new SXSSFWorkbook();
				response.setContentType("application/force-download");// 设置强制下载不打开
				response.addHeader("Content-Disposition", "attachment;fileName=purchaseForm-template.xlsx");// 设置文件名
				ServletOutputStream fOut = response.getOutputStream();
				// 将数据写入Excel
				Sheet sheet = workbook.createSheet("sheet1");
				sheet.setColumnWidth(0, 40 * 256);
				sheet.setColumnWidth(1, 40 * 256);
				sheet.setColumnWidth(2, 40 * 256);
				sheet.setColumnWidth(3, 40 * 256);
				sheet.setColumnWidth(4, 40 * 256);
				
				CellStyle cstitle = workbook.createCellStyle();
				cstitle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
				cstitle.setFillPattern(CellStyle.SOLID_FOREGROUND);
				
				// 第一行
				Row titleRow = sheet.createRow(0);
				titleRow.setHeight((short) (25 * 15));
				Cell title1 = titleRow.createCell(0);
				title1.setCellStyle(cstitle);
				title1.setCellValue("本地SKU");
				Cell title2 = titleRow.createCell(1);
				title2.setCellStyle(cstitle);
				title2.setCellValue("采购数量");
				Cell title3 = titleRow.createCell(2);
				title3.setCellValue("单价(选填)");
				Cell title4 = titleRow.createCell(3);
				title4.setCellValue("供应商(选填)");
				Cell title5 = titleRow.createCell(4);
				title5.setCellValue("预计到货日期（选填，格式：yyyy-mm-dd或yyyy/mm/dd）");
				
				Sheet sheet2 = workbook.createSheet("模板示例");
				sheet2.setColumnWidth(0, 40 * 256);
				sheet2.setColumnWidth(1, 40 * 256);
				sheet2.setColumnWidth(2, 40 * 256);
				sheet2.setColumnWidth(3, 40 * 256);
				sheet2.setColumnWidth(4, 40 * 256);
				
				// 第一行
				Row etitleRow = sheet2.createRow(0);
				etitleRow.setHeight((short) (25 * 15));
				Cell etitle1 = etitleRow.createCell(0);
				etitle1.setCellStyle(cstitle);
				etitle1.setCellValue("本地SKU");
				Cell etitle2 = etitleRow.createCell(1);
				etitle2.setCellStyle(cstitle);
				etitle2.setCellValue("采购数量");
				Cell etitle3 = etitleRow.createCell(2);
				etitle3.setCellValue("单价(选填)");
				Cell etitle4 = etitleRow.createCell(3);
				etitle4.setCellValue("供应商(选填)");
				Cell etitle5 = etitleRow.createCell(4);
				etitle5.setCellValue("预计到货日期（选填，格式：yyyy-mm-dd或yyyy/mm/dd）");
				
				//内容
				Row ecotentRow = sheet2.createRow(1);
				Cell ece1 = ecotentRow.createCell(0);
				ece1.setCellValue("SKU001");
				Cell ece2 = ecotentRow.createCell(1);
				ece2.setCellValue("20");
				Cell ece3 = ecotentRow.createCell(2);
				ece3.setCellValue("9.9");
				Cell ece4 = ecotentRow.createCell(3);
				ece4.setCellValue("我的供应商1");
				Cell ece5 = ecotentRow.createCell(4);
				ece5.setCellValue("2029-01-01");
				
				Row ecotentRow2 = sheet2.createRow(2);
				Cell eceSKU002 = ecotentRow2.createCell(0);
				eceSKU002.setCellValue("SKU002");
				Cell ece22 = ecotentRow2.createCell(1);
				ece22.setCellValue("10");
				Cell ece23 = ecotentRow2.createCell(2);
				ece23.setCellValue("19.9");
				Cell ece24 = ecotentRow2.createCell(3);
				ece24.setCellValue("我的供应商2");
				Cell ece25 = ecotentRow2.createCell(4);
				ece25.setCellValue("2029-01-01");
				workbook.write(fOut);
				fOut.flush();
				fOut.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		@PostMapping(value = "/uploadExcel",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
		public Result<Map<String,Object>> uploadExcelAction(@RequestParam("file")MultipartFile file){
			UserInfo user = UserInfoContext.get();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("shopid", user.getCompanyid());
			if (file != null) {
				try {
					InputStream inputStream = file.getInputStream();
					Workbook workbook = WorkbookFactory.create(inputStream);
					Sheet sheet = workbook.getSheetAt(0);
					Map<String,Object>  result =   purchaseFormService.uploadPurchaseListByExcel(sheet, map);
					workbook.close();
					return Result.success(result);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (EncryptedDocumentException e) {
					e.printStackTrace();
				} catch (InvalidFormatException e) {
					e.printStackTrace();
				} catch (BizException e) {
					e.printStackTrace();
				}
			}
			return Result.failed();
		}
	 
		@GetMapping("/getConsumableByMainSKU")
		public Result<List<Map<String, Object>>> getConsumableByMainSKUAction(String skulist,String warehouseid) {
			UserInfo user = UserInfoContext.get();
			String shopid = user.getCompanyid();
		    if(skulist!=null) {
			    skulist=skulist.replace("%7B", "{");
			    skulist=skulist.replace("%22", "\"");
			    skulist=skulist.replace("%3A", ":");
			    skulist=skulist.replace("%2C", ",");
			    skulist=skulist.replace("%7D", "}");
		    }
			List<Map<String, Object>> sku = GeneralUtil.jsonStringToMapList(skulist);
			List<Map<String, Object>> list = iMaterialConsumableService.selectConsumableByMainSKU(shopid,warehouseid, sku);
			if(list==null)return Result.success(null);
			for(Map<String, Object> item:list) {
			    String material = item.get("id").toString();
			    Integer planamount = Integer.parseInt(item.get("needpurchase").toString());
				Map<String, Object> price = stepWisePriceService.getMaterialPriceByAmount(material, planamount);
				if(price!=null) {
					item.put("price",item.get("itemprice"));
				}
			}
			return Result.success(list);
		}
		
		
		@GetMapping("/getPurchaseNumAllStatus")
		public Result<Map<String, Object>> getPurchaseNumAllStatusAction(String ftype) {
			UserInfo user = UserInfoContext.get();
			String shopid = user.getCompanyid();
			Map<String, Object> params=new HashMap<String, Object>();
			if("orders".equals(ftype)) {
				params.put("method", "1");
			}else {
				params.put("method", "2");
			}
			params.put("shopid", shopid);
			Map<String, Object> map = purchaseFormService.getPurchaseNumAllStatus(params);
			return Result.success(map);
		}
		
		@GetMapping("/getAssemblyInfo")
		public Result<Map<String, Object>> getAssemblyInfoAction(String id) {
			UserInfo user = UserInfoContext.get();
			String shopid = user.getCompanyid();
			Map<String, Object> params=new HashMap<String, Object>();
			if(StrUtil.isEmpty(id)) {
				throw new BizException("请求参数错误");
			}
			params.put("shopid", shopid);
			PurchaseFormEntry entry = purchaseFormEntryService.getById(id);
			if(entry==null) {
				throw new BizException("无法找到采购记录");
			}
			List<Map<String, Object>> assemblylist = assemblyService.selectBySubid(entry.getMaterialid(),shopid);
			params.put("assemblysublist", assemblylist);
			List<Map<String, Object>> assemblyformlist=assemblyFormEntryService.selectAssemblyFormByPurchaseEntryId(id);
			params.put("assemblyformlist", assemblyformlist);
			return Result.success(params);
		}
		
		@SystemControllerLog(value = "更新价格")
		@GetMapping("/updatePrice")
		public Result<PurchaseFormEntry> updatePriceAction(String id,String amount,String price,String totalprice) {
			UserInfo user = UserInfoContext.get();
			Float itemprice = null;
			String itempriceStr = price;
			if (StrUtil.isNotEmpty(itempriceStr)) {
				itempriceStr = itempriceStr.trim().replace(",", "").replace("￥", "");
				if (!itempriceStr.equals("")) {
					itemprice = Float.parseFloat(itempriceStr);
				}
			}
			Float orderprice = null;
			String orderpriceStr = totalprice;
			if (StrUtil.isNotEmpty(orderpriceStr)) {
				orderpriceStr = orderpriceStr.trim().replace(",", "").replace("￥", "");
				if (!orderpriceStr.equals("")) {
					orderprice = Float.parseFloat(orderpriceStr);
				}
			}

			String amountStr = amount;
			Integer amounts = null;
			if (StrUtil.isNotEmpty(amountStr)) {
				if (!amountStr.equals("")) {
					amounts = Integer.parseInt(amountStr);
				}
			}
			return Result.success(purchaseFormService.updatePrice(user, id, itemprice, amounts, orderprice));
		}
		
		@SystemControllerLog(value = "申请付款")
		@PostMapping("/paymentApply")
		public Result<Map<String, Object>> paymentApplyAction(@RequestBody PaymentSaveDTO dto) {
			String entryid = dto.getEntryid();
			UserInfo user = UserInfoContext.get();
			PurchaseFormEntry entry = purchaseFormEntryService.getById(entryid);
			Map<String, Object> maps=purchaseFinanceFormService.applyPayment(user,entry,dto);
			if(dto.getCostamount()!=null) {
				//存在待审核的货物费用 需要把pay状态设置为3
				BigDecimal cost = new BigDecimal(dto.getCostamount().trim());
				if(cost.compareTo(new BigDecimal(0))!=0) {
					entry.setPaystatus(3);
					Date deliverydate =dto.getDeliverydate();
					String deliverydatestr=dto.getDeliverydatestr();
					Date date = null;
					try {
						if (deliverydatestr == null) {
							date = (deliverydate);
						}else {
							date=GeneralUtil.getDatez(deliverydatestr);
						}
					}catch (Exception e) {
						e.printStackTrace();
					} 
					if (date != null) {
						entry.setDeliverydate(date);
					}
					purchaseFormEntryService.updateById(entry);
				}
			}
			return Result.success(maps);
		}
		
		@SystemControllerLog(value = "付款")
		@Transactional
		@PostMapping("/payment")
		public Result<Map<String, Object>> paymentAction(@RequestBody PaymentSaveDTO dto) {
			String entryid = dto.getEntryid();
			String payid = dto.getPayid();
			String costamount = dto.getCostamount();
			String shipamount = dto.getShipamount();
			String remark = dto.getRemark();
			Date deliverydate =dto.getDeliverydate();
			String deliverydatestr=dto.getDeliverydatestr();
			String status = dto.getStatus();
			String feelist=dto.getFeelist();
			String paymethod = dto.getPaymethod();
			String paytype=dto.getPaytype();
			String payacc=dto.getPayacc();
			Integer paymethodValue=null;
			if(StrUtil.isNotEmpty(paymethod)) {
				paymethodValue=Integer.parseInt(paymethod);
			}
			JSONArray feeArray = null;
			if(StrUtil.isNotBlank(feelist)&&feelist.contains("{")) {
				feeArray=GeneralUtil.getJsonArray("["+feelist+"]");	
			}
			UserInfo user = UserInfoContext.get();
			String shopid=user.getCompanyid();
			PurchaseFormEntry entry = purchaseFormEntryService.getById(entryid);
			if (entry == null) {
				 throw new BizException("表单异常，无法找到对应采购记录");
			}
			if(StrUtil.isBlankOrUndefined(payacc)&&!"1".equals(status)&&!"2".equals(status)) {
				QueryWrapper<FinAccount> queryWrapper=new QueryWrapper<FinAccount>();
				queryWrapper.eq("shopid", shopid);
				queryWrapper.eq("isdelete", false);
				long accnum = faccountService.count(queryWrapper);
				if(accnum>0) {
					 throw new BizException("请选择支付账户");
				}else {
					FinAccount account =faccountService.getAccByMeth(shopid,paymethod);
					payacc=account.getId();
				}
			} 
			PurchaseFormPayment paymentcost = null;
			PurchaseFormPayment paymentship = null;
			try {
				if ("1".equals(status) && StrUtil.isNotEmpty(entryid)) {
					iPurchaseFormPaymentService.closePay(entry);
					return Result.success(purchaseFormService.getTraceDetailMap(entryid, shopid, "pay",paytype));
				}
				if ("2".equals(status) && StrUtil.isNotEmpty(entryid)) {
					iPurchaseFormPaymentService.restartPay(entry);
					return Result.success(purchaseFormService.getTraceDetailMap(entryid, shopid, "pay",paytype));
				}
				
				if (StrUtil.isNotEmpty(costamount) && !costamount.equals("0")) {
					try {
						BigDecimal iamount = new BigDecimal(costamount.trim());
						paymentcost = new PurchaseFormPayment();
						if (StrUtil.isNotEmpty(payid)) {
							paymentcost.setId(payid);
						}
						paymentcost.setFormentryid(entryid);
						paymentcost.setProjectid(PurchaseFormPaymentServiceImpl.type_cost);
						paymentcost.setPayprice(iamount);
						paymentcost.setAcct(payacc);
						paymentcost.setRemark(remark);
						paymentcost.setOpttime(new Date());
						paymentcost.setOperator(user.getId());
						paymentcost.setPaymentMethod(paymethodValue);
						if("3".equals(status)) {
							paymentcost.setAuditstatus(2);
						}else {
							paymentcost.setAuditstatus(1);
						}
					} catch (Exception e) {
						throw new BizException("付款金额不能包含非数字字符");
					}
				}
				if (StrUtil.isNotEmpty(shipamount) && !shipamount.equals("0")) {
					try {
						BigDecimal iamount = new BigDecimal(shipamount);
						paymentship = new PurchaseFormPayment();
						if (StrUtil.isNotEmpty(payid)) {
							paymentship.setId(payid);
						}
						paymentship.setFormentryid(entryid);
						paymentship.setProjectid(PurchaseFormPaymentServiceImpl.type_ship);
						paymentship.setPayprice(iamount);
						paymentship.setRemark(remark);
						paymentship.setAcct(payacc);
						paymentship.setOpttime(new Date());
						paymentship.setOperator(user.getId());
						paymentship.setPaymentMethod(paymethodValue);
						if("3".equals(status)) {
							paymentship.setAuditstatus(2);
						}else {
							paymentship.setAuditstatus(1);
						}
					} catch (Exception e) {
						throw new BizException("付款金额不能包含非数字字符");
					}
				}
				//做新增其它项目费用feelist操作
				List<PurchaseFormPayment> payList=new ArrayList<PurchaseFormPayment>();
				if(feeArray!=null && feeArray.size()>0) {
					for(int i=0;i<feeArray.size();i++) {
						JSONObject obj = feeArray.getJSONObject(i);
						BigDecimal amount = obj.getBigDecimal("amount");
					    if(amount.compareTo(new BigDecimal("0"))==0) {
					    	continue;
					    }
						PurchaseFormPayment paymentOther =new PurchaseFormPayment();
						String objectid = obj.getString("objectid");
						paymentOther.setAuditstatus(1);
						if("3".equals(status)) {
							paymentOther.setAuditstatus(2);
						}
						if (StrUtil.isNotEmpty(payid)) {
							paymentOther.setId(payid);
						}
						paymentOther.setProjectid(objectid);
						paymentOther.setFormentryid(entryid);
						paymentOther.setPayprice(amount);
						paymentOther.setAcct(payacc);
						paymentOther.setOpttime(new Date());
						paymentOther.setOperator(user.getId());
						paymentOther.setRemark(remark);
						paymentOther.setPaymentMethod(paymethodValue);
						if(paymentOther!=null) {
							payList.add(paymentOther);
						}
					}
				}
			
				if (paymentcost != null) {
					payList.add(paymentcost);
				
				}
				if (paymentship != null) {
					payList.add(paymentship);
				}
	            if(payList.size()==0&&paymentcost==null&&paymentship==null) {
	            	throw new BizException("付款金额不能为空");
	            }

				Date date = null;
				try {
					if (deliverydatestr == null) {
						date = (deliverydate);
					}else {
						date=GeneralUtil.getDatez(deliverydatestr);
					}
				}catch (Exception e) {
					e.printStackTrace();
				} 
				if (date != null) {
					entry.setDeliverydate(date);
				}
				FinAccount fin = faccountService.getById(payacc);
				iPurchaseFormPaymentService.updatePayment(payList,entry,fin, user);
				
			}catch (Exception e) {
				e.printStackTrace();
			} 
			return Result.success(purchaseFormService.getTraceDetailMap(entryid, shopid, "pay",paytype));

		} 
		
		@SystemControllerLog(value = "撤销付款")
		@Transactional
		@GetMapping("/cancelPayment")
		public Result<Map<String, Object>> cancelPaymentAction(String id) {
			UserInfo user = UserInfoContext.get();
			PurchaseFormPayment payment = iPurchaseFormPaymentService.getById(id);
			String actiontype=payment.getPayprice().floatValue()>0?"out":"in";
			iPurchaseFormPaymentService.cancelPayment(payment,actiontype, user);
			return Result.success(purchaseFormService.getTraceDetailMap(payment.getFormentryid(), user.getCompanyid(), "pay",actiontype));

		} 
		
		public int sysAutoPay(UserInfo user, List<String> ids,String type, String acct) {
			int result=0;
			FinAccount account =null;
			if(StrUtil.isNotBlank(acct)) {
				account=faccountService.getById(acct);
			}
			if(acct==null) {
				if(StrUtil.isNotBlank(type)) {
					account=faccountService.getAccByMeth(user.getCompanyid(),type);
				}else {
					account=faccountService.getAccByMeth(user.getCompanyid(),"1");
				}
			}
			for (int i=0;i<ids.size();i++) {
				String id=ids.get(i);
				PurchaseFormEntry entry = purchaseFormEntryService.getById(id);
				String payacc=account.getId();
				if(entry==null) {
					continue;
				}
				if (entry.getTotalpay().compareTo(entry.getOrderprice()) < 0) {
					entry.setPaystatus(1);
					if (entry.getInwhstatus() == 1) {
						entry.setAuditstatus(3);
					}
					PurchaseFormPayment paymentcost = new PurchaseFormPayment();
					paymentcost.setFormentryid(id);
					paymentcost.setAuditstatus(1);
					if(StrUtil.isNotBlank(type)) {
						paymentcost.setPaymentMethod(Integer.parseInt(type));
					}
					if(entry.getTotalpay()==null) {
						entry.setTotalpay(new BigDecimal("0"));
					}
					paymentcost.setProjectid(PurchaseFormPaymentServiceImpl.type_cost);
					paymentcost.setPayprice(entry.getOrderprice().subtract(entry.getTotalpay()));
					paymentcost.setRemark("批量完成付款");
					paymentcost.setOpttime(new Date());
					paymentcost.setAcct(payacc);
					paymentcost.setOperator(user.getId());
					ArrayList<PurchaseFormPayment> list = new ArrayList<PurchaseFormPayment>();
					list.add(paymentcost);
					result+=iPurchaseFormPaymentService.updatePayment(list,entry,account, user);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					if (entry.getPaystatus() != 1) {
						result+=iPurchaseFormPaymentService.closePay(entry);
					}
				}
			}
			return result;
		}
		
		@PostMapping("/autopay/{type}/{acct}")
		@SystemControllerLog(value = "自动付款")
		public Result<Integer> syspayAction(@PathVariable String type,@PathVariable String acct,@RequestBody List<String> ids) {
			UserInfo user = UserInfoContext.get();
			LockCheckUtils lock = new LockCheckUtils("purchaseautopay"+user.getCompanyid());
			int result;
			try {
				result=sysAutoPay(user, ids,type,acct);
			}catch(Exception e) {
				e.printStackTrace();
				throw e;
			}finally{
				lock.clear();
			}
			return Result.success(result);
		}
		
		@PostMapping("/autorec")
		@SystemControllerLog(value = "自动收货")
		public Result<Integer> autorecAction(@RequestBody List<String> ids) {
			UserInfo user = UserInfoContext.get();
			return Result.success(iPurchaseFormReceiveService.sysAutoRec(user, ids));
		}
		
		@SystemControllerLog(value = "审核通过")
		@GetMapping("/approval")
		public Result<String> approvalAction(String ids){
			UserInfo user = UserInfoContext.get();
			purchaseFormService.approvals(user, ids);
			return Result.success("审核通过操作成功！");
		}
		
		@SystemControllerLog(value = "审核驳回")
		@GetMapping("/purchaseReturn")
		public Result<String> purchaseReturnAction(String ids,String remark) {
			//String remark = request.getParameter("remark");
			UserInfo user = UserInfoContext.get();
			purchaseFormService.purchaseReturn(user, ids, remark);
			return Result.success("审核退回操作成功！");
		}
		
		@SystemControllerLog(value = "修改SKU公告")
		@GetMapping("/updateNotice")
		public Result<Map<String, Object>> updateNoticeAction(String entryid,String notice){
			UserInfo user = UserInfoContext.get();
			String shopid = user.getCompanyid();
			Map<String, Object> result = purchaseFormService.updateNotice(entryid, notice, shopid,user.getId());
			result.put("notice", notice);
			return Result.success(result);
		}
		
		
		@PostMapping("/downExcelReports")
		public void downExcelReportsAction(@RequestBody PurchaseFormDTO dto, HttpServletResponse response){
			// 创建新的Excel工作薄
			Workbook workbook = new SXSSFWorkbook();
			UserInfo userinfo = UserInfoContext.get();
			Map<String, Object> param = new HashMap<String, Object>();
			  param.put("userid", userinfo.getId());
			  param.put("shopid",userinfo.getCompanyid());
			  param.put("search", null);
			  param.put("fromDate", null);
			  param.put("toDate", null);
			  param.put("auditstatus", null);
			  param.put("supplierid", null);
			  param.put("datetype", null);
			  param.put("name",null);
			  param.put("supplier", null);
			  param.put("categoryId", null);
			  param.put("issfg", null);
			  param.put("remark", null);
			  param.put("owner",null);
			  param.put("auditstatusparam", null);
			  String search = dto.getSearch();
				if (GeneralUtil.isNotEmpty(search)) {
					search = search + "%";
				} else {
					search = null;
				}
				param.put("search", search);
				String datetype = dto.getDatetype();
				if (GeneralUtil.isEmpty(datetype))
					datetype = "createdate";
				param.put("datetype", datetype.trim());
				String auditstatus =dto.getAuditstatus();
				if (GeneralUtil.isNotEmpty(auditstatus)) {
					param.put("auditstatus", auditstatus);
				}
				String supplierid = dto.getSupplierid();
				if (GeneralUtil.isNotEmpty(supplierid)) {
					param.put("supplierid", supplierid);
				}
				String warehouse = dto.getWarehouseid();
				if (GeneralUtil.isNotEmpty(warehouse)) {
					param.put("warehouse", warehouse);
				}
				String name = dto.getName();
				if (GeneralUtil.isNotEmpty(name)) {
					param.put("name", name);
				}
				String categoryId =dto.getCategoryid();
				if (GeneralUtil.isNotEmpty(categoryId)) {
					param.put("categoryId", categoryId);
				}
				String issfg = dto.getIssfg();
				if (GeneralUtil.isNotEmpty(issfg)) {
					param.put("issfg", issfg);
				}
				String remark = dto.getRemark();
				if (GeneralUtil.isNotEmpty(remark)) {
					param.put("remark", "%" + remark.trim() + "%");
				}
				String owner =dto.getOwner();
				if (GeneralUtil.isNotEmpty(owner)) {
					param.put("owner", owner);
				}
				String formid = dto.getFormid();
				if (GeneralUtil.isNotEmpty(formid)) {
					param.put("formid", formid);
				} else {
					String fromDate = dto.getFromDate();
					if (GeneralUtil.isNotEmpty(fromDate)) {
						param.put("fromDate", fromDate.trim());
					}
					String toDate = dto.getToDate();
					if (GeneralUtil.isNotEmpty(toDate)) {
						toDate=toDate.substring(0, 10);
						param.put("toDate", toDate.trim() + " 23:59:59");
					}
				}
				String auditstatusparam =dto.getAuditstatusparam();
				if (GeneralUtil.isNotEmpty(auditstatus)) {
					param.put("auditstatusparam", auditstatusparam);
				}
			
				String ftype = dto.getFtype();
				List<String> entrylist = null;
				if (GeneralUtil.isNotEmpty(ftype)) {
					param.put("searchtype", ftype);
					if ("logistics".equals(ftype)) {
						if (GeneralUtil.isNotEmpty(search) && formid == null) {
							search = search.replaceAll("%", "");
							entrylist = purchaseFormEntryAlibabaInfoService.getEntryIdList(search, userinfo.getCompanyid());
							if (entrylist != null && entrylist.size() > 0) {
								param.put("entrylist", entrylist);
								param.put("search", "");
							} else {
								param.put("search", "#####");
							}
						}
					}
				}
			param.put("shopid",userinfo.getCompanyid());
			String hasStatus=dto.getHasStatus();
			param.put("hasStatus", hasStatus);
			ServletOutputStream fOut = null;
			try {
				response.setContentType("application/force-download");// 设置强制下载不打开
				response.addHeader("Content-Disposition", "attachment;fileName=purchaseItemRecords.xlsx");// 设置文件名
				fOut = response.getOutputStream();
				//插入记录条
				purchaseFormService.setExcelBook(workbook, param);
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
		
		@SystemControllerLog(value = "作废采购单")
		@GetMapping("/deleteEntry")
		public Result<Map<String, Object>> deleteEntryAction(String id){
			UserInfo userinfo = UserInfoContext.get();
			Map<String, Object> result = purchaseFormService.deleteEntry(userinfo,id);
			return Result.success(result);
		}
		
		@SystemControllerLog("更新仓库")
		@GetMapping(value = "/updateWarehouse")
		public Result<?> updateWarehouseAction(String id,String warehouseid){
			UserInfo user = UserInfoContext.get();
			return Result.success(purchaseFormService.updateWarehouse(user, id, warehouseid));
		}
		
		@SystemControllerLog(value = "撤回采购单")
		@GetMapping("/recallEntry")
		public Result<PurchaseFormEntry> recallEntryAction(String id){
			UserInfo userinfo = UserInfoContext.get();
			PurchaseFormEntry result = purchaseFormService.recallEntry(userinfo,id);
			return Result.success(result);
		}
		
		@GetMapping("/getEntryData")
		public Result<List<Map<String, Object>>> getDataAction(String id){
			List<Map<String, Object>> result = purchaseFormService.getEntryData(id);
			return Result.success(result);
		}
		
		@PostMapping("/downloadPurchaseInfo")
		public void downloadPurchaseInfoAction(@RequestBody PurchaseFormDownloadDTO dto, HttpServletResponse response) {
			UserInfo userinfo = UserInfoContext.get();
			String shopid = userinfo.getCompanyid();
			String number = dto.getNumber();
			String warehouseid =dto.getWarehouseid();
			String buyerName = dto.getBuyerName();
			String buyerDate = dto.getBuyerDate();
			String totalprice = dto.getTotalprice();
			String orderRemark = dto.getRemark();
			String creator = dto.getCreator();
			String supplierid=dto.getSupplierid();
			PurchaseForm  form = purchaseFormService.getById(dto.getFormid());
			String formid = form.getId();// form id
			// 创建word文件 并下载
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("formid", formid);
			map.put("shopid", shopid);
			map.put("supplierid", supplierid);
			map.put("orderRemark", orderRemark);
			map.put("totalprice", totalprice);
			map.put("buyerDate", buyerDate);
			map.put("buyerName", buyerName);
			map.put("warehouseid", warehouseid);
			map.put("number", number);
			map.put("creator", creator);
			if(userinfo.getUserinfo().get("companyname")!=null) {
				String company=URLDecoder.decode(userinfo.getUserinfo().get("companyname").toString(), Charset.defaultCharset());
				map.put("company", company);
			}else {
				map.put("company", "");
			}
			purchaseFormEntryService.downloadPurchaseInfoWord(response, map,userinfo);
		}
		
		@PostMapping(value = "/uploadPaymentFile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
		public Result<String> uploadPaymentFileAction(@RequestParam("file")MultipartFile file)  {
		       UserInfo user=UserInfoContext.get();
		       List<FinanceProject> projectlist = iFinanceProjectService.findProject(user.getCompanyid());
		       List<PurchaseFormPaymentMethod> paymethlist = iFaccountService.findPurchasePayMethod();
		       FinAccount fin = iFaccountService.readFinAccount(user.getCompanyid());
		       Map<String,FinanceProject> projectMap=new HashMap<String,FinanceProject>();
		       Map<String,PurchaseFormPaymentMethod> paymethMap=new HashMap<String,PurchaseFormPaymentMethod>();
		       for(FinanceProject item:projectlist) {
		    	   projectMap.put(item.getName(), item);
		       }
		       for(PurchaseFormPaymentMethod item:paymethlist) {
		    	   paymethMap.put(item.getName(), item);
		       }
				if (file != null) {
					try {
						InputStream inputStream = file.getInputStream();
						Workbook workbook = WorkbookFactory.create(inputStream);
						Map<String,PurchaseFormEntry> entryMap=new HashMap<String,PurchaseFormEntry>();
						Sheet sheet = workbook.getSheetAt(0);
						String message="";
						for (int i = 1; i <= sheet.getLastRowNum(); i++) {
							Row info=sheet.getRow(i);
							if(info==null || info.getCell(0)==null) {
								continue;
							}
							Cell numbercell = info.getCell(0);
							String number = numbercell.getStringCellValue();
							Cell skucell=info.getCell(1);
							String sku=skucell.getStringCellValue();
							Cell feetypecell=info.getCell(2);
							String feetype=feetypecell.getStringCellValue();
							Cell paytypecell=info.getCell(3);
							String paytype=paytypecell.getStringCellValue();
							Cell amountcell=info.getCell(4);
							Double amount = amountcell.getNumericCellValue();
							Cell remarkcell=info.getCell(5);
							String remark=remarkcell.getStringCellValue();
							
							String key=user.getCompanyid()+number+sku;
							PurchaseFormEntry entry = entryMap.get(key);
							if(entry==null) {
								  entry = purchaseFormEntryService.getByNumberSku(user.getCompanyid(),number,sku);
							}
							FinanceProject feetypeEntity = projectMap.get(feetype);
							PurchaseFormPaymentMethod paytypeEntity = paymethMap.get(paytype);
							if(amount!=null&&feetypeEntity!=null&&paytypeEntity!=null&&entry!=null&&amount>0.0000001) {
								PurchaseFormPayment pay =new PurchaseFormPayment();
								List<PurchaseFormPayment> paylist = entry.getPayList();
								if(paylist==null) {
									paylist=new LinkedList<PurchaseFormPayment>();
								}
								pay.setProjectid(feetypeEntity.getId());
								pay.setProjectname(feetypeEntity.getName());
								pay.setPaymentMethod(paytypeEntity.getId());
								pay.setMethodname(paytypeEntity.getName());
								pay.setAcct(fin.getId());
								pay.setAuditstatus(1);
								pay.setCreatedate(new Date());
								pay.setOpttime(new Date());
								pay.setOperator(user.getId());
								pay.setFormentryid(entry.getId());
								pay.setPayprice(new BigDecimal(amount));
								pay.setRemark(remark);
								paylist.add(pay);
								entry.setPayList(paylist);
								entryMap.put(key,entry);
							}else {
								message=message+"订单编码:"+number+",SKU:"+sku+",费用类型:"+feetype+",付款方式:"+paytype+"无法匹配，或金额为空";
							}
							
						}
						if(!StrUtil.isBlankOrUndefined(message)) {
							throw new BizException(message);
						}
						for(Entry<String, PurchaseFormEntry> item:entryMap.entrySet()) {
							iPurchaseFormPaymentService.updatePayment(item.getValue().getPayList(),item.getValue(),fin, user);
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
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			return Result.success("ok");
		}
		
		
		@SystemControllerLog(value = "修改预计到货时间")
		@GetMapping("/changeDeliveryDate")
		public Result<String> changeDeliveryDateAction(String id,String deliverydate) {
			boolean isok = purchaseFormService.updateDeliveryDate(id, deliverydate);
			if(isok) {
				return Result.success("ok");
			}else {
				return Result.success("fail");
			}
		}
		
		@SystemControllerLog(value = "更新周期")
		@GetMapping("/updateCycle")
		public Result<String> updateCycleAction(String id,String amount) {
			UserInfo user=UserInfoContext.get();
			Integer amounts = null;
			if (StrUtil.isNotEmpty(amount)) {
				amounts = Integer.parseInt(amount);
			} else {
				throw new BizException("请输入正确的供货周期值");
			}
			boolean isok = materialService.updateCycle(user, id, amounts);
			if(isok) {
				return Result.success("ok");
			}else {
				return Result.success("fail");
			}
		}
		
		@GetMapping("/reviewPruchaseStockPage")
		public Result<Map<String,Object>> getPurchaseStockInfoAction(String recid) {
			Map<String,Object> maps=purchaseFormService.getPurchaseRecordInfo(recid);
			return  Result.success(maps);
		}
		
		@PostMapping(value = "/getPayRecSumReport")
		public Result<IPage<Map<String, Object>>> getPayRecSumReport(@RequestBody PurchaseFormDTO dto) {
			Map<String, Object> param = new HashMap<String, Object>();
			UserInfo user=UserInfoContext.get();
			String shopid =user.getCompanyid();
			param.put("shopid", shopid);
			String warehouseid = dto.getWarehouseid();
			if (StrUtil.isBlank(warehouseid)) {
				warehouseid = null;
			}
			param.put("warehouse", warehouseid);
			String search = dto.getSearch();
			if (StrUtil.isNotEmpty(search)) {
				param.put("search", "%"+search + "%");
			} else {
				param.put("search", null);
			}
			String searchtype = dto.getFtype();
			param.put("searchtype", searchtype);
			param.put("hasStatus", null);
			param.put("datetype", "createdate");
			param.put("auditstatus", "15");
			param.put("fromDate", dto.getFromDate());
			param.put("toDate", dto.getToDate());
			param.put("entryList", null);
	 
			IPage<Map<String, Object>> pagelist = purchaseFormService.getPayRecSumReport(dto.getPage(),param);
			return Result.success(pagelist);
		}
	 
		@PostMapping(value = "/getPurchaseFormReport")
		public Result<IPage<Map<String, Object>>> getPurchaseFormReport(@RequestBody PurchaseFormDTO dto)  {
			Map<String, Object> param = new HashMap<String, Object>();
			UserInfo user=UserInfoContext.get();
			String shopid = user.getCompanyid();
			param.put("shopid", shopid);
			String warehouseid = dto.getWarehouseid();
			if (StrUtil.isEmpty(warehouseid)) {
				warehouseid = null;
			}
			param.put("warehouseid", warehouseid);
			String type = dto.getFtype();
			param.put("type", type);
			String search =dto.getSearch();
			if (StrUtil.isNotEmpty(search)) {
				param.put("search", "%"+search + "%");
			}
			String fromDate =dto.getFromDate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (StrUtil.isNotEmpty(fromDate)) {
				param.put("fromDate", fromDate.trim());
			} else {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH, -30);
				fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
				param.put("fromDate", fromDate);
			}
			String toDate = dto.getToDate();
			if (StrUtil.isNotEmpty(toDate)) {
				param.put("endDate", toDate.trim().substring(0,10)+" 23:59:59");
			} else {
				toDate = GeneralUtil.formatDate(new Date(), sdf);
				param.put("endDate", toDate+" 23:59:59");
			}
			IPage<Map<String, Object>> list = purchaseFormService.purchaseFormReport(dto.getPage(),param);
			if (list!=null&&list.getRecords()!=null&&list.getRecords().size()>0&&dto.getCurrentpage()==1) {
				Map<String, Object> map = purchaseFormService.purchaseFormReportTotal(param);
				list.getRecords().get(0).put("summary", map);
			}
			return Result.success(list);
		}
		
		@PostMapping(value = "/downExcelPurchaseFormReport")
		public void downExcelPurchaseFormReportAction(@RequestBody PurchaseFormDTO dto,HttpServletResponse response) {
			// 创建新的Excel工作薄
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			// 将数据写入Excel
			Map<String, Object> param = new HashMap<String, Object>();
			UserInfo user=UserInfoContext.get();
			String shopid = user.getCompanyid();
			param.put("shopid", shopid);
			String warehouseid =dto.getWarehouseid();
			if (StrUtil.isEmpty(warehouseid)) {
				warehouseid = null;
			}
			param.put("warehouseid", warehouseid);
			String type = dto.getFtype();
			param.put("type", type);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			String fromDate = dto.getFromDate();
			if (StrUtil.isNotEmpty(fromDate)) {
				param.put("fromDate", fromDate.trim());
			} else {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH, -30);
				fromDate = sdf.format(cal.getTime());
				param.put("fromDate", fromDate);
			}
			String toDate =dto.getToDate();
			if (StrUtil.isNotEmpty(toDate)) {
				param.put("endDate", toDate.trim());
			} else {
				toDate = sdf.format(new Date());
				param.put("endDate", toDate);
			}
			String search =dto.getSearch();
			if (StrUtil.isNotEmpty(search)) {
				search = ("%" + search + "%");
			} else {
				search = null;
			}
			param.put("search", search);
			purchaseFormService.setPurchaseFormReportExcelBook(workbook, param);
			try {
				response.setContentType("application/force-download");// 设置强制下载不打开
				response.addHeader("Content-Disposition", "attachment;fileName=purchaseFormReport" + System.currentTimeMillis() + ".xlsx");// 设置文件名
				ServletOutputStream fOut = response.getOutputStream();
				workbook.write(fOut);
				workbook.close();
				fOut.flush();
				fOut.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}
