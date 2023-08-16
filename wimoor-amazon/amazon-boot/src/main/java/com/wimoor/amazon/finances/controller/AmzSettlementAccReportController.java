package com.wimoor.amazon.finances.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.finances.pojo.dto.AmzSettlementDTO;
import com.wimoor.amazon.finances.service.IAmzFinAccountService;
import com.wimoor.amazon.finances.service.IAmzSettlementAccReportService;
import com.wimoor.amazon.finances.service.IAmzSettlementReportService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "账期汇款记录")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fin/settlementAccReport")
public class AmzSettlementAccReportController {
final IAmzSettlementAccReportService iAmzSettlementAccReportService;
final IAmzSettlementReportService iAmzSettlementReportService;
final IAmzFinAccountService iAmzFinAccountService;

@GetMapping("/getAccount")
public Result<?> getAccountAction(String currency ) {
	UserInfo user = UserInfoContext.get();
	String shopid = user.getCompanyid();
	Map<String, Map<String, Object>> list = iAmzFinAccountService.getFinancialByShopId(shopid, currency);
	return Result.success(list) ;
}

 
@PostMapping("/downloadAllSettlementExcel")
@SystemControllerLog("全部下载Acc")
public Result<?> setSettlementExcelAction(@RequestBody AmzSettlementDTO dto, HttpServletResponse response) throws BizException {
	Map<String, Object> map = new HashMap<String, Object>();
	 UserInfo user = UserInfoContext.get();
	String fromDate = dto.getFromDate();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	if (StrUtil.isNotEmpty(fromDate)) {
		try {
			map.put("fromDate", sdf.format(sdf.parse(fromDate.trim())));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} else {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
		map.put("fromDate", fromDate);
	}
	String endDate = dto.getEndDate();
	if (StrUtil.isNotEmpty(endDate)) {
		try {
			map.put("endDate", sdf.format(sdf.parse(endDate.trim())));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} else {
		endDate = GeneralUtil.formatDate(new Date(), sdf);
		map.put("endDate", endDate);
	}
	String marketplace_name = dto.getMarketplace_name();
	if (StrUtil.isEmpty(marketplace_name) || "all".equals(marketplace_name)) {
		marketplace_name = null;
	}
	String groupid = dto.getGroupid();
	if (StrUtil.isEmpty(groupid) || "all".equals(groupid)) {
		groupid = null;
	}
	String currency = dto.getCurrency();
	String datetype = dto.getDatetype();
	if (StrUtil.isEmpty(datetype)) {
		datetype = null;
	}
	map.put("datetype", datetype);
	map.put("groupid", groupid);
	map.put("shopid", user.getCompanyid());
	map.put("marketplace_name", marketplace_name);
	map.put("currency", currency);
	List<Map<String, Object>> list = iAmzSettlementAccReportService.findSettlementAcc(map);
 
	if(list.size()>300) {
		throw new BizException("最大导出账期数量不能超过300个");
	}
    @SuppressWarnings("resource")
	SXSSFWorkbook workbook = new SXSSFWorkbook();
	Sheet sheet = workbook.createSheet("sheet1");
	Row trow = sheet.createRow(0);
		 // 在索引0的位置创建行（最顶端的行）
		String[] titlemap = { "groupname","settlement","settlement_id", "currency", "transaction_type", "order_id", "merchant_order_id",
				"adjustment_id", "shipment_id", "marketplace_name", "amount_type", "amount_description", "amount",
				"fulfillment_id", "posted_date", "posted_date_time", "order_item_code", "merchant_order_item_id",
				"merchant_adjustment_item_id", "sku", "quantity_purchased", "promotion_id" };
		for (int j = 0; j < titlemap.length; j++) {
			Cell cell = trow.createCell(j);  // 在索引0的位置创建单元格(左上端)
			cell.setCellValue(titlemap[j]);
		}
		int line=0;
		for(Map<String, Object>  acc:list) {
			Map<String,Object> parameter=new HashMap<String,Object>();
			parameter.put("shopid", user.getCompanyid());
			parameter.put("groupid", null);
			parameter.put("marketplace_name", null);
			parameter.put("settlementId", acc.get("settlement_id").toString());
			parameter.put("amazonauthid", acc.get("amazonauthid").toString());
			parameter.put("settlement", "【"+GeneralUtil.formatDate(GeneralUtil.getDate(acc.get("startDate")))+"】-【"+GeneralUtil.formatDate(GeneralUtil.getDate(acc.get("endDate")))+"】");
			line = line+iAmzSettlementReportService.downLoadSettlementRpt(sheet,parameter,line);
		}
        ServletOutputStream fOut;
        response.setHeader("Set-Cookie", "fileDownload=true; path=/");
		response.setContentType("application/force-download");// 设置强制下载不打开
		response.addHeader("Content-Disposition", "attachment;fileName=Payment" + System.currentTimeMillis() + ".xlsx");// 设置文件名
		try {
        	fOut = response.getOutputStream();
			workbook.write(fOut);
			fOut.flush();
			fOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	return Result.success();
}

@PostMapping("/getSettlementAccReport")
@SystemControllerLog("下载Acc")
public Result<?> getSettlementAccReportAction(@RequestBody AmzSettlementDTO dto) throws ParseException {
		Map<String, Object> map = new HashMap<String, Object>();
		 UserInfo user = UserInfoContext.get();
		String fromDate = dto.getFromDate();
		String fatype = dto.getFatype();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (StrUtil.isNotEmpty(fromDate)) {
			map.put("fromDate", sdf.format(sdf.parse(fromDate.trim())));
		} else {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -1);
			fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
			map.put("fromDate", fromDate);
		}
		String endDate = dto.getEndDate();
		if (StrUtil.isNotEmpty(endDate)) {
			map.put("endDate", sdf.format(sdf.parse(endDate.trim())));
		} else {
			endDate = GeneralUtil.formatDate(new Date(), sdf);
			map.put("endDate", endDate);
		}
		String marketplace_name = dto.getMarketplace_name();
		if (StrUtil.isEmpty(marketplace_name) || "all".equals(marketplace_name)) {
			marketplace_name = null;
		}
		String groupid = dto.getGroupid();
		if (StrUtil.isEmpty(groupid) || "all".equals(groupid)) {
			groupid = null;
		}
		String currency = dto.getCurrency();
		String datetype = dto.getDatetype();
		if (StrUtil.isEmpty(datetype)) {
			datetype = null;
		}
		map.put("datetype", datetype);
		map.put("groupid", groupid);
		map.put("shopid", user.getCompanyid());
		map.put("marketplace_name", marketplace_name);
		map.put("currency", currency);
		if("finsett".equals(fatype)) {
			  IPage<Map<String, Object>> list = iAmzSettlementAccReportService.findSettlementAcc(dto.getPage(),map);
		      return Result.success(list);
		}else {
			 IPage<Map<String, Object>> list = iAmzFinAccountService.getFinancial(dto.getPage(),map);
			 return Result.success(list);
		}

}



		@SystemControllerLog( "下载sum")
		@PostMapping("/getSettlementAccReportSum")
		public Result<?> getSettlementAccReportSumAction(@RequestBody AmzSettlementDTO dto) throws ParseException {
			Map<String, Object> map = new HashMap<String, Object>();
			 UserInfo user = UserInfoContext.get();
			String fromDate = dto.getFromDate();
			String endDate = dto.getEndDate();
			String currency = dto.getCurrency();
			String datetype =dto.getDatetype();
			String marketplace_name = dto.getMarketplace_name();
			String groupid = dto.getGroupid();
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (StrUtil.isNotEmpty(fromDate)) {
				map.put("fromDate", sdf.format(sdf2.parse(fromDate.trim())));
			} else {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.MONTH, -1);
				fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
				map.put("fromDate", fromDate);
			}

			if (StrUtil.isNotEmpty(endDate)) {
				map.put("endDate", sdf.format(sdf2.parse(endDate.trim())));
			} else {
				endDate = GeneralUtil.formatDate(new Date(), sdf);
				map.put("endDate", endDate);
			}
		
			if (StrUtil.isEmpty(marketplace_name) || "all".equals(marketplace_name)) {
				marketplace_name = null;
			}
	
			if (StrUtil.isEmpty(groupid) || "all".equals(groupid)) {
				groupid = null;
			}

			if (StrUtil.isEmpty(datetype)) {
				datetype = null;
			}
			map.put("datetype", datetype);
			map.put("groupid", groupid);
			map.put("shopid", user.getCompanyid());
			map.put("marketplace_name", marketplace_name);
			map.put("currency", currency);
			Map<String, Object> result1 = iAmzSettlementAccReportService.findSettlementAccSum(map);
			Map<String, Object> result2 = iAmzFinAccountService.getFinancialSum(map);
			BigDecimal acctotalsummary1 = (BigDecimal) result1.get("acctotalsummary");
			BigDecimal acctotalsummary2 = (BigDecimal) result2.get("acctotalsummary");
			BigDecimal totalfailedsummary = (BigDecimal) result2.get("totalfailedsummary");
			Map<String,Object> result=new HashMap<String,Object>();
			String currencyicon =null;
			if(currency!=null) {
				currencyicon=GeneralUtil.formatCurrency(currency) ;
			}
			if(acctotalsummary2!=null) {
				result.put("finacc", currencyicon +GeneralUtil.formatterQuantity(acctotalsummary2));
			}else {
				result.put("finacc",  "0");
			}
			if(acctotalsummary1!=null) {
				result.put("finsett", currencyicon +GeneralUtil.formatterQuantity(acctotalsummary1));
			}else {
				result.put("finsett",  "0");
			}
			if(acctotalsummary2!=null&&acctotalsummary1!=null) {
				result.put("finsum", currencyicon +GeneralUtil.formatterQuantity(acctotalsummary1.add(acctotalsummary2)));
			}else {
				result.put("finsum",  "0");
			}
			if(totalfailedsummary!=null) {
				result.put("totalfailedsummary", currencyicon +GeneralUtil.formatterQuantity(totalfailedsummary));
			}else {
				result.put("totalfailedsummary",  "0");
			}
		    return Result.success(result);
		}
		
	@GetMapping("/faccdetail")
	public Result<?> faccDetailAction(String amazonauthid,String accgroupid,String nextToken) throws ParseException {
		UserInfo user = UserInfoContext.get();
		String shopid = user.getCompanyid();
		return Result.success(iAmzFinAccountService.getFaccDetial(shopid,amazonauthid,accgroupid,nextToken));
	}

}
