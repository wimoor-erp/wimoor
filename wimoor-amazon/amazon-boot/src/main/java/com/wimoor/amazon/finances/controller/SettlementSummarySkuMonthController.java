package com.wimoor.amazon.finances.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.common.service.IExchangeRateHandlerService;
import com.wimoor.amazon.finances.pojo.dto.AmzSettlementDTO;
import com.wimoor.amazon.finances.pojo.dto.FinDataMonthDTO;
import com.wimoor.amazon.finances.service.IAmzSettlementSummarySkuMonthService;
import com.wimoor.amazon.summary.service.IAmazonSettlementAnalysisService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.ProgressHelper;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2023-07-26
 */

@Api(tags = "账期月报")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/settlementSummarySkuMonth")
public class SettlementSummarySkuMonthController {
	final IAmzSettlementSummarySkuMonthService iAmzSettlementSummarySkuMonthService;
	final IMarketplaceService marketplaceService;
    final IAmazonSettlementAnalysisService amazonSettlementAnalysisAgentService;
	final IAmazonAuthorityService iAmazonAuthorityService;
	final IExchangeRateHandlerService iExchangeRateHandlerService;
    final IAmazonGroupService iAmazonGroupService;
	@PostMapping("/list")
	public Result<IPage<Map<String, Object>>>  listAction(@RequestBody AmzSettlementDTO dto) {
		UserInfo user = UserInfoContext.get();
		String shopid = user.getCompanyid();
		String marketplaceid = dto.getMarketplaceid();
		String currency = dto.getCurrency();
		String fromDate = dto.getFromDate();
		String toDate =dto.getEndDate();
		String groupid = dto.getGroupid();
		String pointname =dto.getMarketplace_name();
		String datetype = dto.getDatetype();
		String parentasin=dto.getParentasin();
		String searchType=dto.getFatype();
		String search=dto.getSearch();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("fromDate", fromDate.trim().subSequence(0, 7)+"-01");
		map.put("endDate", toDate.trim().subSequence(0, 7)+"-01");
		map.put("type", dto.getCharttype());
		if (StrUtil.isEmpty(marketplaceid) || "all".equals(marketplaceid)) {
			marketplaceid = null;
			map.put("marketplaceName",null);
		}else {
			Marketplace market = marketplaceService.findMapByMarketplaceId().get(marketplaceid);
			if(market!=null) {
				map.put("marketplaceName", market.getPointName());
			}else {
				map.put("marketplaceName",null);
			}
		}
		
		if (StrUtil.isEmpty(groupid) || "all".equals(groupid)) {
			groupid = null;
		}

		if (StrUtil.isEmpty(pointname) || "all".equals(pointname)) {
			pointname = null;
		}
		if (StrUtil.isEmpty(parentasin) ) {
			pointname = null;
		}
		if(StrUtil.isNotBlank(search)) {
			search="%"+search.trim()+"%";
		}else {
			search=null;
		}
		String ownerid=dto.getOwnerid();
		if (StrUtil.isBlankOrUndefined(ownerid) || "all".equals(ownerid)) {
			ownerid = null;
		}
		map.put("searchtype", searchType);
		map.put("search", search);
		map.put("shopid", shopid);
		map.put("parentasin", parentasin);
		map.put("currency", currency);
		map.put("groupid", groupid);
		map.put("owner", ownerid);
		map.put("categoryid", dto.getCategoryid());
		map.put("datetype", datetype);
		IPage<Map<String, Object>> list = iAmzSettlementSummarySkuMonthService.findByCondition(dto.getPage(),map);
		return Result.success(list);
	}
	
	
	  @PostMapping("/downloadList")
	   public void downDataExcelByRateAction(@RequestBody AmzSettlementDTO dto, HttpServletResponse response)  {
	   	// 创建新的Excel工作薄
	   	SXSSFWorkbook workbook = new SXSSFWorkbook();
	   	// 将数据写入Excel
	   	UserInfo user = UserInfoContext.get();
		String shopid = user.getCompanyid();
		String marketplaceid = dto.getMarketplaceid();
		String currency = dto.getCurrency();
		String fromDate = dto.getFromDate();
		String toDate =dto.getEndDate();
		String groupid = dto.getGroupid();
		String pointname =dto.getMarketplace_name();
		String datetype = dto.getDatetype();
		String parentasin=dto.getParentasin();
		String searchType=dto.getFatype();
		String search=dto.getSearch();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("fromDate", fromDate.trim().subSequence(0, 7)+"-01");
		map.put("endDate", toDate.trim().subSequence(0, 7)+"-01");
		map.put("type", dto.getCharttype());
		if (StrUtil.isEmpty(marketplaceid) || "all".equals(marketplaceid)) {
			marketplaceid = null;
			map.put("marketplaceName",null);
		}else {
			Marketplace market = marketplaceService.findMapByMarketplaceId().get(marketplaceid);
			if(market!=null) {
				map.put("marketplaceName", market.getPointName());
			}else {
				map.put("marketplaceName",null);
			}
		}
		
		if (StrUtil.isEmpty(groupid) || "all".equals(groupid)) {
			groupid = null;
		}

		if (StrUtil.isEmpty(pointname) || "all".equals(pointname)) {
			pointname = null;
		}
		if (StrUtil.isEmpty(parentasin) ) {
			pointname = null;
		}
		if(StrUtil.isNotBlank(search)) {
			search="%"+search.trim()+"%";
		}else {
			search=null;
		}
		map.put("searchtype", searchType);
		map.put("search", search);
		map.put("shopid", shopid);
		map.put("parentasin", parentasin);
		map.put("currency", currency);
		map.put("groupid", groupid);
		map.put("owner", dto.getOwnerid());
		map.put("categoryid", dto.getCategoryid());
		map.put("datetype", datetype);
		iAmzSettlementSummarySkuMonthService.getDownloadList(workbook, map);
		   	try {
		   		response.setContentType("application/force-download");// 设置强制下载不打开
		   		response.addHeader("Content-Disposition", "attachment;fileName=CommodityRevenueFinRate" + System.currentTimeMillis() + ".xlsx");// 设置文件名
		   		ServletOutputStream fOut = response.getOutputStream();
		   		workbook.write(fOut);
		   		workbook.close();
		   		fOut.flush();
		   		fOut.close();
		   	} catch (Exception e) {
		   		e.printStackTrace();
		   	}
	   }

	@PostMapping("/downloadListOld")
	public void downDataExcelOldAction(@RequestBody AmzSettlementDTO dto, HttpServletResponse response)  {
		// 创建新的Excel工作薄
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		// 将数据写入Excel
		UserInfo user = UserInfoContext.get();
		String shopid = user.getCompanyid();
		String marketplaceid = dto.getMarketplaceid();
		String currency = dto.getCurrency();
		String fromDate = dto.getFromDate();
		String toDate =dto.getEndDate();
		String groupid = dto.getGroupid();
		String pointname =dto.getMarketplace_name();
		String datetype = dto.getDatetype();
		String parentasin=dto.getParentasin();
		String searchType=dto.getFatype();
		String search=dto.getSearch();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("fromDate", fromDate.trim().subSequence(0, 7)+"-01");
		map.put("endDate", toDate.trim().subSequence(0, 7)+"-01");
		map.put("type", dto.getCharttype());
		if (StrUtil.isEmpty(marketplaceid) || "all".equals(marketplaceid)) {
			marketplaceid = null;
			map.put("marketplaceName",null);
		}else {
			Marketplace market = marketplaceService.findMapByMarketplaceId().get(marketplaceid);
			if(market!=null) {
				map.put("marketplaceName", market.getPointName());
			}else {
				map.put("marketplaceName",null);
			}
		}

		if (StrUtil.isEmpty(groupid) || "all".equals(groupid)) {
			groupid = null;
		}

		if (StrUtil.isEmpty(pointname) || "all".equals(pointname)) {
			pointname = null;
		}
		if (StrUtil.isEmpty(parentasin) ) {
			pointname = null;
		}
		if(StrUtil.isNotBlank(search)) {
			search="%"+search.trim()+"%";
		}else {
			search=null;
		}
		map.put("searchtype", searchType);
		map.put("search", search);
		map.put("shopid", shopid);
		map.put("parentasin", parentasin);
		map.put("currency", currency);
		map.put("groupid", groupid);
		map.put("owner", dto.getOwnerid());
		map.put("categoryid", dto.getCategoryid());
		map.put("datetype", datetype);
		iAmzSettlementSummarySkuMonthService.getDownloadListOld(workbook, map);
		try {
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=CommodityRevenueFinRate" + System.currentTimeMillis() + ".xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PostMapping("/getRefreshData")
	@CacheEvict(value = { "findSettlementSKUCache" }, allEntries = true)
	public Result<?> getGroupMarketAction(@RequestBody FinDataMonthDTO dto) {
	 	UserInfo user = UserInfoContext.get();
	 	String key=dto.getProgresskey();
	 	if(StrUtil.isBlank(key)) {
	 		key="settlement_refresh_data";
	 	}
	 	dto.setShopid(user.getCompanyid());
	 	ProgressHelper progressHelper=new ProgressHelper(user,key);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				amazonSettlementAnalysisAgentService.refreshData(progressHelper,dto);
			}
		}).start();
		return Result.success(key);
	}


	@PostMapping("/getQuarter")
	public Result<?> getQuarterAction(@RequestBody Map<String,Object> param) {
		UserInfo user = UserInfoContext.get();
		String groupid=param.get("groupid")!=null?param.get("groupid").toString():null;
		String marketplaceid=param.get("marketplaceid")!=null?param.get("marketplaceid").toString():null;
		if(StrUtil.isNotBlank(groupid)&& StrUtil.isNotBlank(marketplaceid)){
			AmazonAuthority auth = iAmazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
			if(auth!=null){
				param.put("amazonauthid", auth);
			}
		}
		Date fromDate= GeneralUtil.getDate(param.get("fromDate"));
		Calendar c=Calendar.getInstance();
		c.setTime(fromDate);
		int quarter=c.get(Calendar.MONTH)/3+1;
		c.set(Calendar.MONTH,quarter*3-3);
		c.set(Calendar.DATE, 1);
		c.add(Calendar.MONTH, -3);
		param.put("fromDate",c.getTime());
		c.add(Calendar.MONTH, 6);
		param.put("endDate",c.getTime());
		param.put("shopid",user.getCompanyid());
		List<Map<String, Object>> list = iAmzSettlementSummarySkuMonthService.summaryQuarter(param);
		BigDecimal amount=new BigDecimal("0");
		BigDecimal oldamount=new BigDecimal("0");
		String toCurrency=param.get("currency").toString();
		if(list!=null&&list.size()>0){
			for(Map<String, Object> item:list){
				String fromCurrency=item.get("currency").toString();
				if(Integer.parseInt(item.get("quarterdate").toString())==quarter) {
					BigDecimal  itemamount = item.get("amount") != null ? new BigDecimal(item.get("amount").toString()) : new BigDecimal("0");
					if(!toCurrency.equals("MARKET")){
						itemamount=iExchangeRateHandlerService.changeCurrencyByLocal(user.getCompanyid(),fromCurrency, toCurrency, itemamount);
					}
					amount=amount.add(itemamount);
				}else{
					BigDecimal  itemamount = item.get("amount") != null ? new BigDecimal(item.get("amount").toString()) : new BigDecimal("0");
					if(!toCurrency.equals("MARKET")){
						itemamount=iExchangeRateHandlerService.changeCurrencyByLocal(user.getCompanyid(),fromCurrency, toCurrency, itemamount);
					}
					oldamount=oldamount.add(itemamount);
				}
			}
		}
		Map<String,Object> result=new HashMap<String,Object>();
		result.put("amount",amount);
		result.put("oldamount",oldamount);
		return Result.success(result);
	}

	@PostMapping("/downloadMonth")
	public void downloadMonthAction(@RequestBody Map<String,Object> param, HttpServletResponse response)  {
		// 创建新的Excel工作薄
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		// 将数据写入Excel
		UserInfo user = UserInfoContext.get();
		Date fromDate = GeneralUtil.getDate(param.get("fromDate"));
		Result<Map<String,Object>> result = getMonthAction(param);
		Map<String, Object> data = result.getData();
		getDownloadMonth(workbook, data);
		try {
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=CommodityRevenueFinRate" + System.currentTimeMillis() + ".xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PostMapping("/getMonth")
	public Result<Map<String,Object>> getMonthAction(@RequestBody Map<String,Object> param) {
		UserInfo user = UserInfoContext.get();
		String fromDate = param.get("fromDate").toString();
		String endTime1 = param.get("endDate").toString();
		String startTime1 = fromDate.substring(0, 7);
		String endMonth = endTime1.substring(0, 7);
		List<String> times = initTimes(endTime1, startTime1);
		SimpleDateFormat FMT_YMD = new SimpleDateFormat("yyyy-MM");
		String currentMonth = FMT_YMD.format(new Date());
		String startdate=startTime1+"-01";
		if (currentMonth.equals(endMonth)) {
			Calendar c=Calendar.getInstance();
			c.add(Calendar.MONTH,-3);
			c.set(c.get(Calendar.YEAR),c.get(Calendar.MONTH), 1);
			if(GeneralUtil.getDatez(startdate).after(c.getTime())){
				startdate=GeneralUtil.formatDate(c.getTime());
			}
		}
		param.put("fromDate",startdate);
		param.put("endDate",getLastDayByCalendar(GeneralUtil.getDatez(endTime1)));
		if(user!=null){
			param.put("shopid",user.getCompanyid());
		}else{
			if(param.get("shopid")==null){
				String groupid=param.get("groupid").toString();
				AmazonGroup group = this.iAmazonGroupService.getById(groupid);
				param.put("shopid",group.getShopid());
			}
		}
		String shopid=param.get("shopid").toString();
		List<Map<String, Object>> summary = iAmzSettlementSummarySkuMonthService.getSummaryMonth(param);
		Map<String,Object> result=new HashMap<String,Object>();
		List<Map<String, Object>> colunmData=new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> list=new LinkedList<Map<String,Object>>();
		Map<String,Object> rpt_adv_spend_feeMap=calSummary(  summary  ,"rpt_adv_spend_fee","广告花费");
		//Map<String,Object> share_reimbursement_feeMap=calSummary(  summary  ,"share_reimbursement_fee","赔偿金");
		//Map<String,Object> share_coupon_redemption_feeMap=calSummary(  summary  ,"share_coupon_redemption_fee","coupon");
		Map<String,Object> priceMap=calSummary(  summary  ,"price","采购成本");
		String toCurrency=param.get("currency")!=null?param.get("currency").toString():"MARKET";
		List<Map<String, Object>> summarylist = iAmzSettlementSummarySkuMonthService.getSummaryMonthStorage(param);
		Map<String,Object> storagefeeMap=calSummaryWithCurrency(shopid,summarylist,"storagefee","仓储费",toCurrency);
		Map<String,Object> principalTaxMap=calSummaryWithCurrency(shopid,summarylist,"principal","销售额(含税）",toCurrency);
		Map<String,Object> principalMap=calSummaryWithCurrency(shopid,summarylist,"principalWithoutTax","销售额",toCurrency);
		Map<String,Object> fbafeeMap=calSummaryWithCurrency(shopid,summarylist  ,"fbafee","FBA费",toCurrency);
		Map<String,Object> refundMap=calSummaryWithCurrency(shopid,summarylist  ,"refund","退货金额",toCurrency);
		Map<String,Object> commissionMap=calSummaryWithCurrency(shopid,summarylist  ,"commission","佣金",toCurrency);
		Map<String,Object> promotionMap=calSummaryWithCurrency(shopid,summarylist  ,"promotion","促销",toCurrency);
		Map<String,Object> shippingMap=calSummaryWithCurrency(shopid,summarylist  ,"shipping","运费补偿",toCurrency);
		Map<String,Object> share_reimbursement_feeMap=calSummaryWithCurrency(shopid,summarylist  ,"reimbursement","赔偿金",toCurrency);
		Map<String,Object> share_coupon_redemption_feeMap=calSummaryWithCurrency(shopid,summarylist  ,"coupon","coupon",toCurrency);
		Map<String,Object> vatMap=calSummaryWithCurrency(shopid,summarylist,"vat","VAT",toCurrency);
		Map<String,Object> otherMap=calSummaryWithCurrency(shopid,summarylist,"other","其他",toCurrency);
		Map<String,Object> shopMap=calSummaryWithCurrency(shopid,summarylist,"shopfee","结算费用",toCurrency);
		Map<String,Object> liquidationsMap=calSummaryWithCurrency(shopid,summarylist,"liquidations","低价清仓",toCurrency);
		Map<String,Object> digitalMap=calSummaryWithCurrency(shopid,summarylist,"digital","数字服务费",toCurrency);
		Map<String,Object> freightMap=calSummaryWithCurrency(shopid,summarylist,"freight","国际物流",toCurrency);

		if (currentMonth.equals(endMonth)) {
			FMT_YMD = new SimpleDateFormat("yy-MM");
			Calendar cal=Calendar.getInstance();
			BigDecimal avgAdv=new BigDecimal(0);
			BigDecimal avgReimbursement=new BigDecimal(0);
			BigDecimal avgRefund=new BigDecimal(0);
			BigDecimal avgStoragefee=new BigDecimal(0);
			BigDecimal avgShare_coupon_redemption_fee=new BigDecimal(0);
			for(int i=0;i<3;i++){
				cal.add(Calendar.MONTH, -1);
				currentMonth = FMT_YMD.format(cal.getTime());
				String label= currentMonth;
				if(rpt_adv_spend_feeMap.get(label)!=null){
					avgAdv=avgAdv.add(new BigDecimal(rpt_adv_spend_feeMap.get(label).toString()))  ;
				}
				if(share_reimbursement_feeMap.get(label)!=null){
					avgReimbursement=avgReimbursement.add(new BigDecimal(share_reimbursement_feeMap.get(label).toString()));
				}
				if(storagefeeMap.get(label)!=null){
					avgStoragefee=avgStoragefee.add(new BigDecimal(storagefeeMap.get(label).toString()));
				}
				if(refundMap.get(label)!=null){
					avgRefund=avgRefund.add(new BigDecimal(refundMap.get(label).toString()));
				}
				if(share_coupon_redemption_feeMap.get(label)!=null){
					avgShare_coupon_redemption_fee=avgShare_coupon_redemption_fee.add(new BigDecimal(share_coupon_redemption_feeMap.get(label).toString()));
				}
			}
			avgAdv=avgAdv.divide(new BigDecimal(3),2,BigDecimal.ROUND_HALF_UP);
			avgReimbursement=avgReimbursement.divide(new BigDecimal(3),2,BigDecimal.ROUND_HALF_UP);
			avgRefund=avgRefund.divide(new BigDecimal(3),2,BigDecimal.ROUND_HALF_UP);
			avgStoragefee=avgStoragefee.divide(new BigDecimal(3),2,BigDecimal.ROUND_HALF_UP);
			avgShare_coupon_redemption_fee=avgShare_coupon_redemption_fee.divide(new BigDecimal(3),2,BigDecimal.ROUND_HALF_UP);
			String currentTodayMonth=FMT_YMD.format(GeneralUtil.getDatez(endTime1));
			rpt_adv_spend_feeMap.put(currentTodayMonth,avgAdv);
			share_reimbursement_feeMap.put(currentTodayMonth,avgReimbursement);
			refundMap.put(currentTodayMonth,avgRefund);
			storagefeeMap.put(currentTodayMonth,avgStoragefee);
			share_coupon_redemption_feeMap.put(currentTodayMonth,avgShare_coupon_redemption_fee);
		}
		list.add(principalTaxMap);
		list.add(principalMap);
		list.add(refundMap);
		list.add(fbafeeMap);
		list.add(digitalMap);
		list.add(commissionMap);
		//list.add(promotionMap);
		//list.add(shippingMap);
		list.add(share_reimbursement_feeMap);
		list.add(storagefeeMap);
		//list.add(vatMap);
		//list.add(share_reimbursement_feeMap);
		list.add(liquidationsMap);

		// list.add(freightMap);
		// list.add(otherMap);
		// list.add(shopMap);
		list.add(rpt_adv_spend_feeMap);
		list.add(priceMap);
		FMT_YMD = new SimpleDateFormat("yy-MM");
		currentMonth = FMT_YMD.format(new Date());
		for(String time:times){
			Map<String,  Object> item=new HashMap<String, Object>();
			item.put("label",time);
			if(time.equals(currentMonth)){
				item.put("isnow",true);
			}
			colunmData.add(item);
		}
		result.put("colunmData",colunmData);
		result.put("tableData",list);
		return Result.success(result);
	}

	public void getDownloadMonth(SXSSFWorkbook workbook, Map<String, Object> data) {
		List<Map<String, Object>> colunmData=(List<Map<String, Object>>)data.get("colunmData");
		if (colunmData.size() > 0 && colunmData != null) {
			Sheet sheet = workbook.createSheet("sheet1");
			// 在索引0的位置创建行（最顶端的行）
			Row trow = sheet.createRow(0);
			Cell cell = null;
			cell = trow.createCell(0); // 在索引0的位置创建单元格(左上端)
			cell.setCellValue("费用名称");
			for (int i = 0; i < colunmData.size(); i++) {
				cell = trow.createCell(i+1); // 在索引0的位置创建单元格(左上端)
				Object value = colunmData.get(i).get("label");
				cell.setCellValue(value.toString());
			}
			cell = trow.createCell(colunmData.size()+1); // 在索引0的位置创建单元格(左上端)
			cell.setCellValue("汇总");
			List<Map<String, Object>>  list=(List<Map<String, Object>>)data.get("tableData");
			for (int i = 0; i < list.size(); i++) {
				Row row = sheet.createRow(i + 1);
				Map<String, Object> map = list.get(i);
				cell = row.createCell(0);
				cell.setCellValue( map.get("name").toString());;
				for (int j = 0; j < colunmData.size(); j++) {
					cell = row.createCell(j+1); // 在索引0的位置创建单元格(左上端)
					Object value = map.get(colunmData.get(j).get("label").toString());
					if (value != null) {
						if(value instanceof BigDecimal) {
							cell.setCellValue(Double.parseDouble(value.toString()));
						}else {
							cell.setCellValue(value.toString());
						}
					}
				}
				cell = row.createCell(colunmData.size()+1);
				cell.setCellValue(map.get("sum")!=null? Double.parseDouble(map.get("sum").toString()):0);
			}
		} else {
			try {
				throw new Exception("没有数据可导出！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public List<String> initTimes(String endTime1, String startTime1) {
		List<String> times = new ArrayList<String>();
		String endTime = endTime1.substring(2, 7);
		String startTime = startTime1.substring(2, startTime1.length());
		String startyear = startTime.split("-")[0];
		int startMonth = Integer.parseInt(startTime.split("-")[1]);
		String endyear = endTime.split("-")[0];
		int endMonth = Integer.parseInt(endTime.split("-")[1]);
		if (startTime.equals(endTime)) {
			times.add(startTime);
			return times;
		}
		if (startyear.equals(endyear)) {
			for (Integer i = startMonth; i < (endMonth + 1); i++) {
				if (i < 10) {
					times.add(startyear + "-0" + i.toString());
				} else {
					times.add(startyear + "-" + i.toString());
				}
			}
		} else {
			for (Integer i = startMonth; i < 13; i++) {
				if (i < 10) {
					times.add(startyear + "-0" + i.toString());
				} else {
					times.add(startyear + "-" + i.toString());
				}
			}
			for (Integer i = 1; i < (endMonth + 1); i++) {
				if (i < 10) {
					times.add(endyear + "-0" + i.toString());
				} else {
					times.add(endyear + "-" + i.toString());
				}
			}
		}
		return times;
	}
	public static Date getLastDayByCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}
	Map<String,Object> calSummary(List<Map<String, Object>> summary  ,  String key,String name){
		BigDecimal sum = new BigDecimal("0");
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("name",name);
		for(Map<String, Object> item:summary){
			BigDecimal value =item.get(key)!=null?new BigDecimal(item.get(key).toString()):new BigDecimal(0);
			String posted_date=item.get("posted_date").toString();
			map.put(posted_date, value);
			sum= sum.add(value);
		}
		map.put("sum", sum);
		return map;
	}

	Map<String,Object> calSummaryWithCurrency(String shopid,List<Map<String, Object>> summary  ,  String key,String name,String toCurrency){
		Map<String,Object> valueMap=new HashMap<String, Object>();
		valueMap.put("name",name);
		BigDecimal value_sum=new BigDecimal(0);
		for(Map<String, Object> map : summary){
			String posted_date = map.get("posted_date").toString();
			String fromCurrency = map.get("currency").toString();
			BigDecimal value =map.get(key)!=null? new BigDecimal(map.get(key).toString()):new BigDecimal(0);
			if(!toCurrency.equals("MARKET")){
				value=iExchangeRateHandlerService.changeCurrencyByLocal(shopid,fromCurrency, toCurrency, value);
			}
			BigDecimal oldfee = valueMap.get(posted_date)!=null?(BigDecimal)valueMap.get(posted_date):new BigDecimal(0);
			value_sum=value_sum.add(value);
			valueMap.put(posted_date, oldfee.add(value));
		}
		valueMap.put("sum", value_sum);
		return valueMap;
	}



	public void eachMaps(String ftype, HashMap<String, HashMap<String, Object>> map, List<String> times) {
		HashMap<String, Object> maplist = map.get(ftype);
		if (maplist != null) {
			for (int i = 0; i < times.size(); i++) {
				if (maplist.get(times.get(i)) == null) {
					maplist.put(times.get(i), 0);
				}
			}
			if (maplist.get("all") == null) {
				maplist.put("all", 0);
			}
		}
	}
}

