package com.wimoor.erp.purchase.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.omg.CORBA.UserException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.purchase.pojo.dto.ReceiveReportDTO;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormEntry;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormReceive;
import com.wimoor.erp.purchase.pojo.vo.PurchaseFormReceiveVo;
import com.wimoor.erp.purchase.service.IPurchaseFormEntryAlibabaInfoService;
import com.wimoor.erp.purchase.service.IPurchaseFormEntryService;
import com.wimoor.erp.purchase.service.IPurchaseFormService;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@Api(tags = "采购单接口")
@RestController
@RequestMapping("/api/v1/purchase_form")
@RequiredArgsConstructor
public class PurchaseFormController {
	
	private final IPurchaseFormService purchaseFormService;
	private final IPurchaseFormEntryService purchaseFormEntryService;
	private final IPurchaseFormEntryAlibabaInfoService purchaseFormEntryAlibabaInfoService;
	


	@GetMapping("/getdetail")
	public Object getDetailAction(HttpServletRequest request, HttpServletResponse response, Model model) throws ERPBizException {
		UserInfo userinfo = UserInfoContext.get();
		String id = request.getParameter("id");
		String shopid = userinfo.getCompanyid();
		return Result.success(purchaseFormService.getDetailMap(id, shopid));
	}
	
	@GetMapping("/getRecdetail")
	public Object getRecDetailAction(HttpServletRequest request, HttpServletResponse response, Model model) throws ERPBizException {
		UserInfo userinfo = UserInfoContext.get();
		String id = request.getParameter("id");
		String ftype = request.getParameter("ftype");
		String shopid = userinfo.getCompanyid();
		return Result.success(purchaseFormService.getTraceDetailMap(id, shopid, ftype));
	}
	
	@GetMapping("/rec")
	public Result<Map<String, Object>> receiveAction(HttpServletRequest request, HttpServletResponse response, Model model) throws ERPBizException {
		UserInfo userinfo = UserInfoContext.get();
		String entryid = request.getParameter("entryid");
		String recid = request.getParameter("recid");
		String amount = request.getParameter("amount");
		String warehouse = request.getParameter("warehouse");
		String ftype = request.getParameter("ftype");
		String status = request.getParameter("status");
		 
		String remark = request.getParameter("remark");
		Integer iamount;

		if (GeneralUtil.isNotEmpty(amount)) {
			try {
				iamount = Integer.parseInt(amount);
			} catch (Exception e) {
				throw new ERPBizException("数量不能包含非数字字符");
			}
		} else {
			iamount = 0;
		}
		PurchaseFormReceive rec = new PurchaseFormReceive();
		if (GeneralUtil.isNotEmpty(recid)) {
			rec.setId(recid);
		}
		rec.setFormentryid(entryid);
		rec.setAmount(iamount);
		rec.setWarehouseid(warehouse);
		rec.setOpttime(new Date());
		rec.setOperator(userinfo.getId());
		rec.setFtype(ftype);
		rec.setRemark(remark);
		PurchaseFormEntry entry = purchaseFormEntryService.getById(entryid);
		if("2".equals(status)) {
			purchaseFormService.restartRec(userinfo, entry);
		}else if("1".equals(status)) {
			purchaseFormService.closeRec(userinfo, entry);
		}else {
			purchaseFormService.saveReceive(userinfo, rec, entry);
		}
		String shopid = userinfo.getCompanyid();
		
		return Result.success(purchaseFormService.getTraceDetailMap(entryid, shopid, "rec"));
	}
	
	@GetMapping("/catchLogisticsInfo")
	public Object catchLogisticsInfoAction(HttpServletRequest request, Model model) throws UserException {
		UserInfo userinfo = UserInfoContext.get();
		String alibabaAuthid = request.getParameter("alibabaAuthid");
		String alibabaOrderid = request.getParameter("alibabaOrderid");
		String purchaseEntryid = request.getParameter("purchaseEntryid");
		return Result.success(purchaseFormEntryAlibabaInfoService.captureLogisticsInfo(userinfo, alibabaAuthid, alibabaOrderid, purchaseEntryid));
	}
 
 


	@ApiOperation(value = "根据id 获取产品信息")
	@ApiImplicitParams({
		    @ApiImplicitParam(name = "search", value = "按搜索类型搜索SKU，订单，运单", required = true, paramType = "body", dataType = "String"),
		    @ApiImplicitParam(name = "fromDate", value = "开始日期", required = true, paramType = "body", dataType = "String"),
		    @ApiImplicitParam(name = "toDate", value = "结束日期", required = true, paramType = "body", dataType = "String"),
		    @ApiImplicitParam(name = "auditstatus", value = "状态与订单状态不同，属于自定义状态[0:草稿，退回；  1:待审核  ；2:审核通过 ；3：已完成]", required = true, paramType = "body", dataType = "String"),
		    @ApiImplicitParam(name = "supplierid", value = "供应商ID", required = true, paramType = "body", dataType = "String"),
		    @ApiImplicitParam(name = "datetype", value = "日期类型[创建日期，审核日期，预计到货日期]", required = true, paramType = "body", dataType = "String"),
		    @ApiImplicitParam(name = "name", value = "产品名称", required = true, paramType = "body", dataType = "String"),
		    @ApiImplicitParam(name = "supplier", value = "供应商名称", required = true, paramType = "body", dataType = "String"),
		    @ApiImplicitParam(name = "issfg", value = "是否组装", required = true, paramType = "body", dataType = "String"),
		    @ApiImplicitParam(name = "remark", value = "订单SKU上的备注", required = true, paramType = "body", dataType = "String"),
		    @ApiImplicitParam(name = "owner", value = "产品负责人ID", required = true, paramType = "body", dataType = "String"),
		    @ApiImplicitParam(name = "auditstatusparam", value = "审核状态['in','notin','outdate']", required = true, paramType = "body", dataType = "String")
	    })
	@GetMapping("/list")
	public Object getListData(HttpServletRequest request, Model model) throws UserException {
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
		  
		  String search = request.getParameter("search");
			if (GeneralUtil.isNotEmpty(search)) {
				search = search + "%";
			} else {
				search = null;
			}
			param.put("search", search);
			String datetype = request.getParameter("datetype");
			if (GeneralUtil.isEmpty(datetype))
				datetype = "createdate";
			param.put("datetype", datetype.trim());
			String auditstatus = request.getParameter("auditstatus");
			if (GeneralUtil.isNotEmpty(auditstatus)) {
				param.put("auditstatus", auditstatus);
			}
			String supplierid = request.getParameter("supplierid");
			if (GeneralUtil.isNotEmpty(supplierid)) {
				param.put("supplierid", supplierid);
			}
			String warehouse = request.getParameter("warehouse");
			if (GeneralUtil.isNotEmpty(warehouse)) {
				param.put("warehouse", warehouse);
			}
			String name = request.getParameter("name");
			if (GeneralUtil.isNotEmpty(name)) {
				param.put("name", name);
			}
			String categoryId = request.getParameter("categoryId");
			if (GeneralUtil.isNotEmpty(categoryId)) {
				param.put("categoryId", categoryId);
			}
			String issfg = request.getParameter("issfg");
			if (GeneralUtil.isNotEmpty(issfg)) {
				param.put("issfg", issfg);
			}
			String remark = request.getParameter("remark");
			if (GeneralUtil.isNotEmpty(remark)) {
				param.put("remark", "%" + remark.trim() + "%");
			}
			String owner = request.getParameter("owner");
			if (GeneralUtil.isNotEmpty(owner)) {
				param.put("owner", owner);
			}
			String formid = request.getParameter("formid");
			if (GeneralUtil.isNotEmpty(formid)) {
				param.put("formid", formid);
			} else {
				String fromDate = request.getParameter("fromDate");
				if (GeneralUtil.isNotEmpty(fromDate)) {
					param.put("fromDate", fromDate.trim());
				}
				String toDate = request.getParameter("toDate");
				if (GeneralUtil.isNotEmpty(toDate)) {
					param.put("toDate", toDate.trim() + " 23:59:59");
				}
			}
			String auditstatusparam = request.getParameter("auditstatusparam");
			if (GeneralUtil.isNotEmpty(auditstatus)) {
				param.put("auditstatusparam", auditstatusparam);
			}
		
			String ftype = request.getParameter("ftype");
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
		  String page_str = request.getParameter("offset");
		  String limit_str =request.getParameter("limit");
		  IPage<Map<String, Object>> list = purchaseFormService.getPurchaseForm(new Page<>(Integer.parseInt(page_str), Integer.parseInt(limit_str)),
					userinfo, param);
		  if(list!=null && list.getRecords()!=null ) {
			  for(int i=0;i<list.getRecords().size();i++) {
				  Map<String, Object> item = list.getRecords().get(i);
				  if(item.get("image")!=null) {
					  String location = item.get("image").toString();
					  if(location.contains("http")) {
						  item.put("image", location); 
					  }
					  else if(location.contains("photo/")) {
						  location=location.replace("photo/", "https://photo.wimoor.com/");
						  item.put("image", location); 
					  }else {
						  item.put("image", "https://erp.wimoor.com/images/systempicture/noimage40.png"); 
					  }
				  }else {
					  item.put("image", "https://erp.wimoor.com/images/systempicture/noimage40.png"); 
				  } 
			  }
		  }
			
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
		IPage<PurchaseFormReceiveVo> list = purchaseFormService.getReceiveReport(page,param);
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
			purchaseFormService.setReceiveReportExcelBook(workbook, param);
			try {
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
		public Result<Object> refreshAlibabaOrderAction() throws UserException {
			 System.out.println("执行1688采购订单跟踪-------------refreshAlibabaOrder"+new Date());
			return Result.success();
		}
	 

	
	 
}
