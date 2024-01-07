package com.wimoor.amazon.finances.controller;


import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationConstraint.OperatorType;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.common.pojo.entity.ExchangeRate;
import com.wimoor.amazon.common.service.IExchangeRateHandlerService;
import com.wimoor.amazon.common.service.IExchangeRateService;
import com.wimoor.amazon.finances.pojo.dto.FinDataMonthDTO;
import com.wimoor.amazon.finances.pojo.entity.AmzFinUserItemData;
import com.wimoor.amazon.finances.service.IAmzFinUserItemDataService;
import com.wimoor.amazon.finances.service.IAmzFinUserItemService;
import com.wimoor.amazon.product.pojo.dto.AmzFinDataDTO;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 客户导入的SKU财务项费用-应用于商品营收其他费用项目导入 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-10-27
 */
@Api(tags = "其它费用接口")
@RestController
@Component("aAmzFinUserItemDataController")
@RequiredArgsConstructor
@RequestMapping("/api/v1/amzFinUserItemData")
public class AmzFinUserItemDataController {
	
	final IAmzFinUserItemDataService amzFinUserItemDataService;
	final IAmzFinUserItemService amzFinUserItemService;
	final IMarketplaceService marketplaceService;
    final IExchangeRateHandlerService exchangeRateHandlerService;
    final IExchangeRateService exchangeRateService;
    final IAmazonGroupService amazonGroupService;
	@PostMapping("/getFinDataMonthList")
	public Result<?> getFinDataMonthListAction(@RequestBody FinDataMonthDTO dto ) {
		UserInfo user = UserInfoContext.get();
		String itemid =dto.getItemid();
		String marketplaceid =dto.getMarketplaceid();
		String groupid =dto.getGroupid();
		String fromDate = dto.getFromDate();
		String endDate =dto.getEndDate();
		String sku = dto.getSku();
		Map<String, Object> params = new HashMap<String, Object>();
		if(StrUtil.isNotEmpty(itemid)) {
			params.put("itemid", itemid);
		}else {
			params.put("itemid", null);
		}
		params.put("marketplaceid", marketplaceid);
		if (groupid == null || "all".equals(groupid)) {
			params.put("groupid", null);
		} else {
			params.put("groupid", groupid);
		}
		if (StrUtil.isEmpty(sku)) {
			params.put("sku", null);
		} else {
			params.put("sku", "%" + sku + "%");
		}
		params.put("fromDate", fromDate.trim());
		params.put("shopid", user.getCompanyid());
		params.put("endDate", endDate.trim());
		params.put("month", "");
		return  Result.success(amzFinUserItemDataService.getFinDataList(dto.getPage(),user, params));
	}

	@PostMapping(value = "/uploadFile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Result<String> uploadExcelAction(@RequestParam("file")MultipartFile file )  {
 		UserInfo user = UserInfoContext.get();
		InputStream inputStream = null;
		if (file != null) {
				try {
					inputStream = file.getInputStream();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			   amzFinUserItemDataService.loadFile(user, inputStream);
		}
		return Result.success();
	} 
	
	@SystemControllerLog("删除费用项目")
	@GetMapping("/deleteFinItemData")
	public Result<?> deleteFinItemDataAction(	String id)  {
		int temp = amzFinUserItemDataService.deleteFinItemData(id);
		return Result.success(temp);
	}
	
	@SystemControllerLog("保存费用项目")
	@PostMapping("/saveFinItemData")
	public Result<Integer> saveFinItemDataAction(@RequestBody AmzFinUserItemData amzFinUserItemData) {
		UserInfo user = UserInfoContext.get();
		if(amzFinUserItemData.getAmount()==null) {
			throw new BizException("请输入金额");
		}
		if(amzFinUserItemData.getByday()==null) {
			throw new BizException("请输入日期");
		}
		amzFinUserItemData.setShopid(user.getCompanyid());
		amzFinUserItemData.setOperator(user.getId());
		amzFinUserItemData.setOpttime(new Date());
		int temp = amzFinUserItemDataService.saveFinItemData(amzFinUserItemData);
		return Result.success(temp);
	}
	
	 
	
	@PostMapping("/getFinDataList")
   	public Result<List<Map<String,Object>>> getFinDataListAction(@RequestBody AmzFinDataDTO dto) {
    	UserInfo user = UserInfoContext.get();
		String itemid = dto.getItemid();
		String marketplaceid = dto.getMarketplaceid();
		String groupid = dto.getGroupid();
		String fromDate = dto.getFromDate();
		String endDate = dto.getEndDate();
		String sku = dto.getSku();
		String osku = dto.getOsku();
		Map<String, Object> params = new HashMap<String, Object>();
		if(StrUtil.isEmpty(itemid)) {
			params.put("itemid", null);
		}else {
			params.put("itemid", itemid);
		}
		params.put("marketplaceid", marketplaceid);
		if (groupid == null || "all".equals(groupid)) {
			params.put("groupid", null);
		} else {
			params.put("groupid", groupid);
		}
		if (StrUtil.isEmpty(sku)) {
			params.put("sku", null);
		} else {
			params.put("sku", "%" + sku + "%");
		}
		if (StrUtil.isEmpty(osku)) {
			params.put("osku", null);
		} else {
			params.put("osku",osku );
		}
		if (StrUtil.isEmpty(dto.getCurrency())) {
			params.put("currency", null);
		} else {
			params.put("currency",dto.getCurrency());
		}
		params.put("fromDate", fromDate.trim());
		params.put("shopid", user.getCompanyid());
		params.put("endDate", endDate.trim());
		List<Map<String, Object>> list = amzFinUserItemDataService.getFinDataList(user, params);
	    return Result.success(list);
   	}
	
	

	@GetMapping("/getFinDataLastWeek")
	public Result<?> getFinDataWeekListAction(String marketplaceid,String groupid,String sku) {
		UserInfo user = UserInfoContext.get();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("marketplaceid", marketplaceid);
		if (groupid == null || "all".equals(groupid)) {
			params.put("groupid", null);
		} else {
			params.put("groupid",groupid);
		}
		if (StrUtil.isEmpty(sku)) {
			params.put("sku", null);
		} else {
			params.put("sku",Arrays.asList(sku));
		}
		params.put("shopid", user.getCompanyid());
		List<Map<String, Object>> list = amzFinUserItemDataService.getFinDataLastWeek(Arrays.asList(params));
		Marketplace marketplace = marketplaceService.selectByPKey(marketplaceid);
		Map<String, Object> map = new HashMap<String, Object>();
		if (list != null) {
			BigDecimal sumAmount = new BigDecimal("0");
			String fcuurency = null;
			for (Map<String, Object> itemMap : list) {
				String currency = itemMap.get("currency").toString();
				String amount = itemMap.get("amount").toString();
				// 货币转换，转换为当前sku所对应的国家货币
				BigDecimal otherCost = exchangeRateHandlerService.changeCurrencyByLocal(currency, marketplace.getCurrency(), new BigDecimal(amount), 2);
				fcuurency = GeneralUtil.formatCurrency(currency);
				itemMap.put("amount", otherCost);
				itemMap.put("fcuurency", fcuurency);
				sumAmount = sumAmount.add(otherCost);
			}
			map.put("sumAmount", sumAmount);
			map.put("fcuurency", fcuurency);
		}
		map.put("resultList", list);
		return Result.success(map);
	}

	

	
	@GetMapping(value = "/downFinItemDataTemp")
	public void downFinItemDataTempAction(HttpServletResponse response) {
		try {
			// 创建新的Excel工作薄
			UserInfo user = UserInfoContext.get();
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=FinItemDataTemplate.xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			// 将数据写入Excel
			Sheet sheet = workbook.createSheet("fee");
			// 在索引0的位置创建行（最顶端的行）
			Row row = sheet.createRow(0);
			Cell cell0 = row.createCell(0);
			cell0.setCellValue("店铺");
			Cell cell1 = row.createCell(1);
			cell1.setCellValue("站点");
			Cell cell2 = row.createCell(2);
			cell2.setCellValue("SKU");
			Cell cell3 = row.createCell(3);
			cell3.setCellValue("日期");
			Cell cell4 = row.createCell(4);
			cell4.setCellValue("费用类型");
			Cell cell5 = row.createCell(5);
			cell5.setCellValue("币别");
			Cell cell6 = row.createCell(6);
			cell6.setCellValue("费用");
			sheet.setColumnWidth((short) 0, (short) 3850);
			sheet.setColumnWidth((short) 2, (short) 3850);
			sheet.setColumnWidth((short) 3, (short) 3850);
			sheet.setColumnWidth((short) 4, (short) 5000);
			List<ExchangeRate> currencylist = exchangeRateService.list();
			List<Map<String, Object>> list = amzFinUserItemService.findFinListByShopid(user.getCompanyid());
			List<AmazonGroup> alllist = amazonGroupService.selectByShopId(user.getCompanyid());
			List<Marketplace> marketlist = marketplaceService.findMarketplaceByShopId(user.getCompanyid());
			List<String> listitem = new ArrayList<String>();
			List<String> listgroup = new ArrayList<String>();
			List<String> listmarket = new ArrayList<String>();
			List<String> listcur = new ArrayList<String>();
			for (Map<String, Object> item : list) {
				listitem.add(item.get("name").toString());
			}
			for (AmazonGroup group : alllist) {
				listgroup.add(group.getName());
			}
			for (Marketplace market : marketlist) {
				listmarket.add(market.getName());
			}
			for (ExchangeRate cur : currencylist) {
				listcur.add(cur.getSymbol());
			}
			String[] array = listitem.toArray(new String[listitem.size()]);
			String[] agroup = listgroup.toArray(new String[listgroup.size()]);
			String[] amarket = listmarket.toArray(new String[listmarket.size()]);
			String[] acurrency = listcur.toArray(new String[listcur.size()]);
			DataValidationHelper helper = sheet.getDataValidationHelper();
			if (acurrency != null && acurrency.length > 0) {
				CellRangeAddressList dstAddrList5 = new CellRangeAddressList(1, 600, 5, 5);// 规则一单元格范围
				DataValidation dstDataValidation5 = helper.createValidation(helper.createExplicitListConstraint(acurrency), dstAddrList5);
				dstDataValidation5.createPromptBox("提示头", "填入币别");
				dstDataValidation5.setShowErrorBox(true);
				dstDataValidation5.setShowPromptBox(true);
				dstDataValidation5.setEmptyCellAllowed(false);
				sheet.addValidationData(dstDataValidation5);
			}
			if (agroup != null && agroup.length > 0) {
				CellRangeAddressList dstAddrList0 = new CellRangeAddressList(1, 700, 0, 0);// 规则一单元格范围
				DataValidation dstDataValidation0 = helper.createValidation(helper.createExplicitListConstraint(agroup), dstAddrList0);
				dstDataValidation0.createPromptBox("提示头", "填入店铺");
				dstDataValidation0.setShowErrorBox(true);
				dstDataValidation0.setShowPromptBox(true);
				dstDataValidation0.setEmptyCellAllowed(false);
				sheet.addValidationData(dstDataValidation0);
			}
			if (amarket != null && amarket.length > 0) {
				CellRangeAddressList dstAddrList1 = new CellRangeAddressList(1, 600, 1, 1);// 规则一单元格范围
				DataValidation dstDataValidation1 = helper.createValidation(helper.createExplicitListConstraint(amarket), dstAddrList1);
				dstDataValidation1.createPromptBox("提示头", "填入站点");
				dstDataValidation1.setShowErrorBox(true);
				dstDataValidation1.setShowPromptBox(true);
				dstDataValidation1.setEmptyCellAllowed(false);
				sheet.addValidationData(dstDataValidation1);
			}
			if (array != null && array.length > 0) {
				CellRangeAddressList dstAddrList4 = new CellRangeAddressList(1, 1000, 4, 4);// 规则一单元格范围
				DataValidation dstDataValidation4 = helper.createValidation(helper.createExplicitListConstraint(array), dstAddrList4);
				dstDataValidation4.createPromptBox("提示头", "填入财务项");
				dstDataValidation4.setShowErrorBox(true);
				dstDataValidation4.setShowPromptBox(true);
				dstDataValidation4.setEmptyCellAllowed(false);
				sheet.addValidationData(dstDataValidation4);
			}
			CellRangeAddressList dstAddrList6 = new CellRangeAddressList(1, 500, 5, 5);// 规则二单元格范围
			DataValidationConstraint dvc = helper.createNumericConstraint
					(DVConstraint.ValidationType.DECIMAL, DVConstraint.OperatorType.BETWEEN, "0", "9999999999");
			DataValidation dstDataValidation6 = helper.createValidation(dvc, dstAddrList6);
			dstDataValidation6.createErrorBox("错误信息", "只能填数字");
			dstDataValidation6.setEmptyCellAllowed(false);
			dstDataValidation6.setShowErrorBox(true);

			sheet.addValidationData(dstDataValidation6);

			CellRangeAddressList dstAddrList3 = new CellRangeAddressList(1, 700, 3, 3);// 规则一单元格范围
			DataValidationConstraint dvc3 = helper.createDateConstraint(OperatorType.BETWEEN, "2010-12-12", "2099-12-31", "YYYY/MM/DD");
			DataValidation dstDataValidation3 = helper.createValidation(dvc3, dstAddrList3);
			dstDataValidation3.createErrorBox("错误信息", "只能填日期格式必须是年-月-日");
			dstDataValidation3.setEmptyCellAllowed(false);
			dstDataValidation3.setShowErrorBox(false);
			dstDataValidation3.setShowPromptBox(true);
			dstDataValidation3.createPromptBox("提示头", "日期格式必须是年-月-日");
			sheet.addValidationData(dstDataValidation3);
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@ResponseBody
	@RequestMapping("/getFinDataForSku")
	public Object getFinDataForSkuAction(HttpServletRequest request) throws ParseException {
		Map<String, Object> param = new HashMap<String, Object>();
		UserInfo user = UserInfoContext.get();
		String marketplaceid = request.getParameter("marketplaceid");
		String sku = request.getParameter("sku");
		String groupid = request.getParameter("groupid");
		String itemid = request.getParameter("itemid");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		String toDate = request.getParameter("endDate");
		if (StrUtil.isNotEmpty(toDate)) {
			param.put("endDate", toDate.trim());
		} else {
			toDate = GeneralUtil.formatDate(cal.getTime(), sdf);
			param.put("endDate", toDate);
		}
		String fromDate = request.getParameter("fromDate");
		if (StrUtil.isNotEmpty(fromDate)) {
			param.put("fromDate", fromDate.trim());
		} else {
			cal.add(Calendar.DAY_OF_MONTH, -6);
			fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
			param.put("fromDate", fromDate);
		}
		param.put("groupid", groupid);
		param.put("shopid", user.getCompanyid());
		param.put("marketplaceid", marketplaceid);
		param.put("sku", sku);
		param.put("itemid", itemid);
		List<Map<String, Object>> list = amzFinUserItemDataService.getFinDataForSku(param);
		List<Map<String, Object>> feerTypeList = amzFinUserItemService.findFinListByShopid(user.getCompanyid());
		if(list == null || list.size() == 0) {
			list = new ArrayList<Map<String,Object>>();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("feerType", feerTypeList);
			list.add(map);
		}else {
			list.get(0).put("feerType", feerTypeList);
		}
		return list;
	}
	
	
	@PostMapping("/downOtherFee")
	public void downDataExcelByOtherFeeAction(@RequestBody FinDataMonthDTO dto, HttpServletResponse response )  {
		// 创建新的Excel工作薄
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		// 将数据写入Excel
		UserInfo user = UserInfoContext.get();
		String itemid = dto.getItemid();
		String marketplaceid = dto.getMarketplaceid();
		String groupid = dto.getGroupid();
		String fromDate = dto.getFromDate();
		String endDate = dto.getEndDate();
		String sku = dto.getSku();
		String ftype = dto.getFtype();
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("marketplaceid", marketplaceid);
		if (groupid == null || "all".equals(groupid)) {
			params.put("groupid", null);
		} else {
			params.put("groupid", groupid);
		}
		if (StrUtil.isEmpty(sku)) {
			params.put("sku", null);
		} else {
			params.put("sku", "%" + sku + "%");
		}
		if (StrUtil.isEmpty(itemid)) {
			params.put("itemid", null);
		} else {
			params.put("itemid", itemid);
		}
		params.put("fromDate", fromDate.trim());
		params.put("shopid", user.getCompanyid());
		params.put("endDate", endDate.trim());
		if(StrUtil.isNotEmpty(ftype) && ftype.equals("month")) {
			params.put("month", "");
		} 
		List<Map<String, Object>> list = amzFinUserItemDataService.getFinDataList(user, params);
		String title=fromDate+"~"+endDate.substring(0, 10);
		if(StrUtil.isNotEmpty(ftype) && ftype.equals("month")) {
			amzFinUserItemDataService.setExcelBookByOtherFeeMonth(workbook, list,title);
		} else {
			amzFinUserItemDataService.setExcelBookByOtherFee(workbook, list);
		}
	
		try {
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=OtherFee" + ".xlsx");// 设置文件名
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

