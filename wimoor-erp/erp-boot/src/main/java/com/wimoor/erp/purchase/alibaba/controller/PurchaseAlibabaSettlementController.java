package com.wimoor.erp.purchase.alibaba.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.purchase.alibaba.pojo.entity.PurchaseAlibabaSettlement;
import com.wimoor.erp.purchase.alibaba.pojo.entity.PurchaseAlibabaSettlementOrder;
import com.wimoor.erp.purchase.alibaba.pojo.entity.PurchaseAlibabaSettlementOrderReturn;
import com.wimoor.erp.purchase.alibaba.pojo.entity.PurchaseAlibabaSettlementPay;
import com.wimoor.erp.purchase.alibaba.pojo.entity.PurchaseAlibabaSettlementPayReturn;
import com.wimoor.erp.purchase.alibaba.service.IPurchaseAlibabaSettlementOrderReturnService;
import com.wimoor.erp.purchase.alibaba.service.IPurchaseAlibabaSettlementOrderService;
import com.wimoor.erp.purchase.alibaba.service.IPurchaseAlibabaSettlementPayReturnService;
import com.wimoor.erp.purchase.alibaba.service.IPurchaseAlibabaSettlementPayService;
import com.wimoor.erp.purchase.alibaba.service.IPurchaseAlibabaSettlementService;
import com.wimoor.erp.purchase.alibaba.service.IPurchaseFormEntryAlibabaInfoService;
import com.wimoor.erp.purchase.pojo.dto.PurchaseSettlementDTO;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2023-11-01
 */
@Api(tags = "1688账期接口")
@RestController
@SystemControllerLog( "1688账期")
@RequestMapping("/api/v1/purchase/alibaba/entry/purchaseAlibabaSettlement")
@RequiredArgsConstructor
public class PurchaseAlibabaSettlementController {

	@Autowired
	IPurchaseAlibabaSettlementService purchaseAlibabaSettlementService;
    @Autowired
    IPurchaseAlibabaSettlementOrderService purchaseAlibabaSettlementOrderService;
	@Autowired
	IPurchaseAlibabaSettlementPayService purchaseAlibabaSettlementPayService;
    @Autowired
    IPurchaseAlibabaSettlementOrderReturnService purchaseAlibabaSettlementOrderReturnService;
	@Autowired
	IPurchaseAlibabaSettlementPayReturnService purchaseAlibabaSettlementPayReturnService;
	@Autowired
	IPurchaseFormEntryAlibabaInfoService purchaseFormEntryAlibabaInfoService;
	@PostMapping(value = "/uploadPayDate",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Result<String> uploadPayDateAction(@RequestParam("file")MultipartFile file,@RequestParam String acct, HttpServletResponse response)  {
			if (file != null) {
				try {
					UserInfo userinfo = UserInfoContext.get();
					InputStream inputStream = file.getInputStream();
					Workbook workbook = WorkbookFactory.create(inputStream);
					PurchaseAlibabaSettlement settlement=new PurchaseAlibabaSettlement();
					settlement.setShopid(userinfo.getCompanyid());
					settlement.setAcct(acct);
					Boolean haserror=purchaseAlibabaSettlementService.setSettlementSheet(workbook,settlement);
				    Boolean ordererror=purchaseAlibabaSettlementOrderService.paySettlementSheetOrder(workbook, settlement);
				    Boolean payerror=purchaseAlibabaSettlementPayService.uploadSettlementSheet(workbook, settlement);
					Boolean orderReturnError=purchaseAlibabaSettlementOrderReturnService.paySettlementSheetOrder(workbook, settlement);
					Boolean payReturnError=purchaseAlibabaSettlementPayReturnService.uploadSettlementSheet(workbook, settlement);
				    if(haserror||ordererror||payerror||orderReturnError||payReturnError) {
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
					return null;
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

	
	@PostMapping(value = "/list" )
	public Result<?> listAction(@RequestBody PurchaseSettlementDTO dto)  {
		UserInfo userinfo = UserInfoContext.get();
		List<PurchaseAlibabaSettlement> list = purchaseAlibabaSettlementService
												.lambdaQuery()
												.ge(PurchaseAlibabaSettlement::getPostdate,dto.getFromDate())
										        .le(PurchaseAlibabaSettlement::getPostdate, dto.getToDate())
										        .eq(PurchaseAlibabaSettlement::getAcct, dto.getAcct())
										        .eq(PurchaseAlibabaSettlement::getShopid, userinfo.getCompanyid())
										        .orderByDesc(PurchaseAlibabaSettlement::getPostdate)
										        .list();
		return Result.success(list);
	}
	
	@PostMapping(value = "/orderList" )
	public Result<?> orderListAction(@RequestBody PurchaseSettlementDTO dto)  {
		LambdaQueryChainWrapper<PurchaseAlibabaSettlementOrder> query = purchaseAlibabaSettlementOrderService.lambdaQuery();
		if(StrUtil.isNotBlank(dto.getSearch())) {
			query.eq(PurchaseAlibabaSettlementOrder::getOrderid, dto.getSearch().trim());
		}
		PurchaseAlibabaSettlement settlment = purchaseAlibabaSettlementService.getById(dto.getSettlementid());
		List<PurchaseAlibabaSettlementOrder> list = query.eq(PurchaseAlibabaSettlementOrder::getSettlementid, dto.getSettlementid())
										        .list();
		List<PurchaseAlibabaSettlementOrder> resultlist=new LinkedList<PurchaseAlibabaSettlementOrder>();
		if(dto.getIscheck()!=null&&dto.getIscheck()==true) {
		   for(PurchaseAlibabaSettlementOrder item:list) {
			   try {
					BigDecimal price = item.getConfirmamount();
					if(item.getReturnamount()!=null) {
						price=price.subtract(item.getReturnamount());
					}
					purchaseFormEntryAlibabaInfoService.checkPay(new BigInteger(item.getOrderid().toString()),price,settlment.getAcct());
				}catch(BizException e) {
					 item.setRemark(e.getMessage());
					 resultlist.add(item);
				}
		   }
			IPage<PurchaseAlibabaSettlementOrder> page = dto.getListPage(resultlist);
			return Result.success(page);
		}else {
			IPage<PurchaseAlibabaSettlementOrder> page = dto.getListPage(list);
			for(PurchaseAlibabaSettlementOrder item:page.getRecords()) {
				try {
					BigDecimal price = item.getConfirmamount();
					if(item.getReturnamount()!=null) {
						price=price.subtract(item.getReturnamount());
					}
					purchaseFormEntryAlibabaInfoService.checkPay(new BigInteger(item.getOrderid().toString()),price,settlment.getAcct());
				}catch(BizException e) {
					 item.setRemark(e.getMessage());
				}
			}
			return Result.success(page);
		}
		
	}
	
	@PostMapping(value = "/payList" )
	public Result<?> payListAction(@RequestBody PurchaseSettlementDTO dto)  {
		List<PurchaseAlibabaSettlementPay> list = purchaseAlibabaSettlementPayService
												.lambdaQuery()
										        .eq(PurchaseAlibabaSettlementPay::getSettlementid, dto.getSettlementid())
										        .list();
		IPage<PurchaseAlibabaSettlementPay> page = dto.getListPage(list);
		return Result.success(page);
	}
	
	@PostMapping(value = "/returnPayList" )
	public Result<?> returnPayListAction(@RequestBody PurchaseSettlementDTO dto)  {
		List<PurchaseAlibabaSettlementPayReturn> list = purchaseAlibabaSettlementPayReturnService
												.lambdaQuery()
										        .eq(PurchaseAlibabaSettlementPayReturn::getSettlementid, dto.getSettlementid())
										        .list();
		IPage<PurchaseAlibabaSettlementPayReturn> page = dto.getListPage(list);
		return Result.success(page);
	}
	
	@PostMapping(value = "/orderReturnList" )
	public Result<?> orderReturnListAction(@RequestBody PurchaseSettlementDTO dto)  {
		List<PurchaseAlibabaSettlementOrderReturn> list = purchaseAlibabaSettlementOrderReturnService
												.lambdaQuery()
										        .eq(PurchaseAlibabaSettlementOrderReturn::getSettlementid, dto.getSettlementid())
										        .list();
		IPage<PurchaseAlibabaSettlementOrderReturn> page = dto.getListPage(list);
		return Result.success(page);
	}
	
	@PostMapping(value = "/delete" )
	public Result<?> deleteAction(@RequestBody PurchaseSettlementDTO dto)  {
		 LambdaQueryWrapper<PurchaseAlibabaSettlementPay> payquery = new LambdaQueryWrapper<PurchaseAlibabaSettlementPay>();
		 payquery.eq(PurchaseAlibabaSettlementPay::getSettlementid, dto.getSettlementid());
		 purchaseAlibabaSettlementPayService.remove(payquery);
		  
		 LambdaQueryWrapper<PurchaseAlibabaSettlementOrder> orderquery = new LambdaQueryWrapper<PurchaseAlibabaSettlementOrder>();
		 orderquery.eq(PurchaseAlibabaSettlementOrder::getSettlementid, dto.getSettlementid());
		 purchaseAlibabaSettlementOrderService.remove(orderquery);
		
		 LambdaQueryWrapper<PurchaseAlibabaSettlementOrderReturn> orderReturnQuery = new LambdaQueryWrapper<PurchaseAlibabaSettlementOrderReturn>();
		 orderReturnQuery.eq(PurchaseAlibabaSettlementOrderReturn::getSettlementid, dto.getSettlementid());
		 purchaseAlibabaSettlementOrderReturnService.remove(orderReturnQuery);
		
		 LambdaQueryWrapper<PurchaseAlibabaSettlementPayReturn> payReturnQuery = new LambdaQueryWrapper<PurchaseAlibabaSettlementPayReturn>();
		 payReturnQuery.eq(PurchaseAlibabaSettlementPayReturn::getSettlementid, dto.getSettlementid());
		 purchaseAlibabaSettlementPayReturnService.remove(payReturnQuery);
		
		 purchaseAlibabaSettlementService.removeById(dto.getSettlementid());
		 return Result.success("success");
	}
	
}

