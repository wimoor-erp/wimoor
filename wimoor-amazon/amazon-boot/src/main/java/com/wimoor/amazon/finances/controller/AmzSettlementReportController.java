package com.wimoor.amazon.finances.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.common.service.IExchangeRateHandlerService;
import com.wimoor.amazon.common.service.IExchangeRateService;
import com.wimoor.amazon.finances.pojo.dto.AmzSettlementDTO;
import com.wimoor.amazon.finances.pojo.entity.AmzSettlementAccReport;
import com.wimoor.amazon.finances.service.IAmzFinAccountService;
import com.wimoor.amazon.finances.service.IAmzSettlementAccReportService;
import com.wimoor.amazon.finances.service.IAmzSettlementReportService;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.common.user.UserLimitDataType;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "亚马逊账期接口")
@RestController
@RequiredArgsConstructor
@SystemControllerLog("财务账期模块")
@RequestMapping("/api/v1/settlement")
public class AmzSettlementReportController {
	    final IAmzSettlementReportService iAmzSettlementReportService;
		@Resource
		private IAmzSettlementAccReportService iAmzSettlementAccReportService;
		@Resource
		IAmzFinAccountService iAmzFinAccountService;
		@Resource
		IProductInfoService productInfoService;
		@Resource
		IMarketplaceService marketplaceService;
		@Resource
		IAmazonAuthorityService authorityService;
		@Resource
		IAmazonGroupService amazonGroupService;
		@Resource
		IAmazonAuthorityService amazonAuthorityService;
		@Resource
		IExchangeRateService exchangeRateService;
		@Resource
		IExchangeRateHandlerService exchangeRateHandlerService;

		@SystemControllerLog( "下载")
		@PostMapping("/downSettlementExcel")
		public Result<?> downSettlementExcelAction(@RequestBody AmzSettlementAccReport query, HttpServletResponse response, Model model) throws BizException {
			UserInfo user = UserInfoContext.get();
			String settlement_id = query.getSettlementId();
			String amazonauthid = query.getAmazonauthid();
			String marketplace_name = query.getMarketplaceName();
			Map<String, Object> parameter = new HashMap<String, Object>();
			parameter.put("shopid", user.getCompanyid());
			SXSSFWorkbook workbook = null;
			ServletOutputStream fOut = null;
			AmzSettlementAccReport acc = iAmzSettlementAccReportService.findSettlementByKey(settlement_id, amazonauthid, marketplace_name);
			if (acc != null) {
				parameter.put("shopid", user.getCompanyid());
				parameter.put("groupid", null);
				parameter.put("marketplace_name", null);
				parameter.put("settlementId", acc.getSettlementId());
				parameter.put("amazonauthid", acc.getAmazonauthid());
				workbook = iAmzSettlementReportService.downLoadSettlementRpt(parameter);
			}
	 
			try {
				response.setHeader("Set-Cookie", "fileDownload=true; path=/");
				response.setContentType("application/force-download");// 设置强制下载不打开
				response.addHeader("Content-Disposition", "attachment;fileName=ProductSalas" + System.currentTimeMillis() + ".xlsx");// 设置文件名
				fOut = response.getOutputStream();
				workbook.write(fOut);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (fOut != null) {
					try {
						fOut.flush();
						fOut.close();
						if (workbook != null) {
							workbook.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return Result.success();
		}
		
		@SystemControllerLog("下载2")
		@PostMapping("/downSettlementExcel2")
		public Result<?> downSettlementExcel2Action(@RequestBody AmzSettlementDTO dto, HttpServletResponse response)   {
			UserInfo user = UserInfoContext.get();
			String fromDate = dto.getFromDate();
			String endDate =dto.getEndDate();
			String groupid = dto.getGroupid();
			String marketplace_name =dto.getMarketplace_name();
			String datetype = dto.getDatetype();
			Map<String, Object> parameter = new HashMap<String, Object>();
			parameter.put("shopid", user.getCompanyid());
			parameter.put("fromDate", fromDate);
			parameter.put("endDate", endDate);
			if (StrUtil.isEmpty(groupid)) {
				parameter.put("groupid", null);
			} else {
				parameter.put("groupid", groupid);
			}
			if (StrUtil.isEmpty(datetype)) {
				parameter.put("datetype", null);
			} else {
				parameter.put("datetype", datetype);
			}
			if (StrUtil.isEmpty(marketplace_name)) {
				parameter.put("marketplace_name", null);
			} else {
				parameter.put("marketplace_name", marketplace_name);
			}
			SXSSFWorkbook workbook = null;
			ServletOutputStream fOut = null;
				workbook = iAmzSettlementReportService.downLoadSettlementRpt(parameter);
			 
			try {
				response.setHeader("Set-Cookie", "fileDownload=true; path=/");
				response.setContentType("application/force-download");// 设置强制下载不打开
				response.addHeader("Content-Disposition", "attachment;fileName=ProductSalas" + System.currentTimeMillis() + ".xlsx");// 设置文件名
				fOut = response.getOutputStream();
				workbook.write(fOut);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (fOut != null) {
					try {
						fOut.flush();
						fOut.close();
						if (workbook != null) {
							workbook.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return Result.success();
		}
		
		@SystemControllerLog( "下载SKU")
		@PostMapping("/downloadSKUSettlementInfo")
		public Result<?>  downloadSKUSettlementInfoAction(@RequestBody AmzSettlementDTO dto, HttpServletResponse response)  {
			 UserInfo user = UserInfoContext.get();
			String sku = dto.getSku();
			String amazonAuthId = dto.getAmazonAuthId();
			String fromDate = dto.getFromDate();
			String endDate =dto.getEndDate();
			String groupid = dto.getGroupid();
			String marketplace_name =dto.getMarketplace_name();
			String datetype = dto.getDatetype();
			Map<String, Object> parameter = new HashMap<String, Object>();
			parameter.put("shopid", user.getCompanyid());
			parameter.put("fromDate", fromDate);
			parameter.put("endDate", endDate);
			if (StrUtil.isEmpty(sku)) {
				parameter.put("sku", null);
			} else {
				parameter.put("sku", sku);
			}
			if (StrUtil.isEmpty(datetype)) {
				parameter.put("datetype", null);
			} else {
				parameter.put("datetype", datetype);
			}
			if (StrUtil.isEmpty(amazonAuthId)) {
				parameter.put("amazonauthid", null);
			} else {
				parameter.put("amazonauthid", amazonAuthId);
			}
			if (StrUtil.isEmpty(groupid)) {
				parameter.put("groupid", null);
			} else {
				parameter.put("groupid", groupid);
			}
			if (StrUtil.isEmpty(marketplace_name)) {
				parameter.put("marketplace_name", null);
			} else {
				parameter.put("marketplace_name", marketplace_name);
			}
			SXSSFWorkbook workbook = null;
			ServletOutputStream fOut = null;
			workbook = iAmzSettlementReportService.downLoadSettlementRpt(parameter);
			try {
				response.setHeader("Set-Cookie", "fileDownload=true; path=/");
				response.setContentType("application/force-download");// 设置强制下载不打开
				response.addHeader("Content-Disposition", "attachment;fileName=ProductSalas" + System.currentTimeMillis() + ".xlsx");// 设置文件名
				fOut = response.getOutputStream();
				workbook.write(fOut);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (fOut != null) {
					try {
						fOut.flush();
						fOut.close();
						if (workbook != null) {
							workbook.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return Result.success();
		}


		

		
		@PostMapping("/getSummaryData")
		public Result<?> getSummaryDataAction(@RequestBody AmzSettlementDTO dto)  {
			Map<String, Object> maps = new HashMap<String, Object>();
			Map<String, Object> param = new HashMap<String, Object>();
			UserInfo user = UserInfoContext.get();
			String shopid = user.getCompanyid();
			String currency = dto.getCurrency();
			String fromDate = dto.getFromDate();
			String toDate =dto.getEndDate();
			String groupid = dto.getGroupid();
			String marketplace_name =dto.getMarketplace_name();
			String datetype = dto.getDatetype();
			if (StrUtil.isEmpty(marketplace_name) || "all".equals(marketplace_name)) {
				marketplace_name = null;
			}
			if (StrUtil.isEmpty(groupid) || "all".equals(groupid)) {
				groupid = null;
			}
			if (StrUtil.isEmpty(datetype) ) {
				datetype = null;
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (StrUtil.isNotEmpty(fromDate)) {
				param.put("fromDate", fromDate.trim());
			} else {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH, -7);
				fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
				param.put("fromDate", fromDate);
			}
			if (StrUtil.isNotEmpty(toDate)) {
				param.put("endDate", toDate.trim());
			} else {
				toDate = GeneralUtil.formatDate(new Date(), sdf);
				param.put("endDate", toDate);
			}
			param.put("datetype", datetype);
			param.put("groupid", groupid);
			param.put("shopid", shopid);
			param.put("currency", currency);
			param.put("marketplace_name", marketplace_name);
			Map<String, Object> list1 = iAmzSettlementReportService.getSummaryAll(param);
			maps.put("allData", list1);
			return Result.success(maps);
		}
		
		
	 
		@PostMapping("/getSummaryOther")
		public  Result<?> getSumSalesChartACtion(@RequestBody AmzSettlementDTO dto) {
			Map<String, Object> maps = new HashMap<String, Object>();
			Map<String, Object> param = new HashMap<String, Object>();
			UserInfo user = UserInfoContext.get();
			String shopid = user.getCompanyid();
			String currency = dto.getCurrency();
			String fromDate = dto.getFromDate();
			String toDate =dto.getEndDate();
			String groupid = dto.getGroupid();
			String pointname =dto.getMarketplace_name();
			String datetype = dto.getDatetype();
			String ftype = dto.getCharttype();
			
			if (StrUtil.isEmpty(pointname) || "all".equals(pointname)) {
				pointname = null;
			}
			if (StrUtil.isEmpty(groupid) || "all".equals(groupid)) {
				groupid = null;
			}
			if (StrUtil.isEmpty(datetype)  ) {
				datetype = null;
			}
			param.put("datetype", datetype);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			if (StrUtil.isNotEmpty(fromDate)) {
				param.put("fromDate", fromDate.trim());
			} else {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH, -7);
				fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
				param.put("fromDate", fromDate);
			}
			if (StrUtil.isNotEmpty(toDate)) {
				param.put("endDate", toDate.trim());
			} else {
				toDate = GeneralUtil.formatDate(new Date(), sdf);
				param.put("endDate", toDate);
			}
			param.put("groupid", groupid);
			param.put("shopid", shopid);
			param.put("currency", currency);
			param.put("marketplace_name", pointname);
			
			Map<String, Object> list = iAmzSettlementReportService.getSumNumsByDay(param, ftype);
			maps.put("labels", list.get("labels"));
			maps.put("datas", list.get("datas"));
			return Result.success(maps);
		}

		@PostMapping("/getSummaryMonth")
		public Result<?>  getAmzSummaryMonthAction(@RequestBody AmzSettlementDTO dto) {
			Map<String, Object> maps = new HashMap<String, Object>();
			Map<String, Object> param = new HashMap<String, Object>();
			UserInfo user = UserInfoContext.get();
			String shopid = user.getCompanyid();
			String marketplaceid = dto.getMarketplaceid();
			String currency = dto.getCurrency();
			String fromDate = dto.getFromDate();
			String toDate =dto.getEndDate();
			String groupid = dto.getGroupid();
			String pointname =dto.getMarketplace_name();
			String datetype = dto.getDatetype();
			if (StrUtil.isEmpty(pointname) || "all".equals(pointname)) {
				pointname = null;
			}
			if (StrUtil.isEmpty(groupid) || "all".equals(groupid)) {
				groupid = null;
			}
			
			if (StrUtil.isEmpty(datetype) ) {
				datetype = null;
			}
			param.put("datetype", datetype);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (StrUtil.isNotEmpty(fromDate)) {
				fromDate = fromDate.replaceAll("/", "-");
				String[] datear = fromDate.split("-");
				Integer year = Integer.valueOf(datear[0].trim());
				Integer month = Integer.valueOf((datear[1]).trim());
				if (month > 1) {
					month = month - 1;
				} else {
					month = 12;
					year = year - 1;
				}
				String monthstr = month.toString();
				if (month < 10) {
					monthstr = "0" + monthstr;
				}
				fromDate = year + "-" + monthstr;
				param.put("fromDate", year + "-" + monthstr + "-01");
			} else {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.MONTH, -1);
				cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
				fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
				param.put("fromDate", fromDate.trim());
			}
		
			if (StrUtil.isNotEmpty(toDate)) {
				toDate = toDate.replaceAll("/", "-");
				Calendar ca = Calendar.getInstance();
				ca.setTime(GeneralUtil.getDatez(toDate.trim() + "-01"));
				ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
				String last = GeneralUtil.formatDate(ca.getTime(), sdf);
				param.put("endDate", last + " 23:59:59");
			} else {

				Calendar ca = Calendar.getInstance();
				ca.setTime(new Date());
				ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
				String last = GeneralUtil.formatDate(ca.getTime(), sdf);
				param.put("endDate", last + " 23:59:59");
			}
		
			if (StrUtil.isEmpty(marketplaceid)) {
				marketplaceid = null;
			}
			Marketplace market = marketplaceService.findMapByMarketplaceId().get(marketplaceid);
			if(market!=null) {
				param.put("marketplace_name", market.getPointName());
				if("market".equals(currency)) {
					currency=market.getCurrency();
				}
			}else {
				param.put("marketplace_name",null);
			}
			param.put("groupid", groupid);
			param.put("shopid", shopid);
			param.put("currency", currency);
			param.put("marketplaceid", marketplaceid);
			String endTime1 = param.get("endDate").toString();
			String startTime1 = fromDate.substring(0, 7);
			List<String> times = iAmzSettlementReportService.initTimes(endTime1, startTime1);
			iAmzSettlementReportService.setParamsSettlementDate(param);
			HashMap<String, HashMap<String, Object>> map = iAmzSettlementReportService.getAmzSummaryMonth(param);
			HashMap<String, HashMap<String, Object>> map2 = iAmzSettlementReportService.getLacolSummaryMonth(param);
			HashMap<String, HashMap<String, Object>> map3 = iAmzSettlementAccReportService.loadSummayAccMaps(param);
			map.putAll(map3);
			Map<String, Object> maplist = iAmzSettlementReportService.loadSummayMonthMaps(map, map2, times);
			maps.put("maps", maplist.get("Maps"));
			maps.put("localmaps", maplist.get("localMaps"));
			maps.put("localName", maplist.get("localName"));
			maps.put("times", times);
			return Result.success(maps);
		}

		@PostMapping("/showOtherin")
		public Result<?>  showOtherinAction(@RequestBody AmzSettlementDTO dto) {
			UserInfo user = UserInfoContext.get();
			String shopid = user.getCompanyid();
			String marketplaceid = dto.getMarketplaceid();
			String currency = dto.getCurrency();
			String fromDate = dto.getFromDate();
			String toDate =dto.getEndDate();
			String groupid = dto.getGroupid();
			String pointname =dto.getMarketplace_name();
			String datetype = dto.getDatetype();
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("fromDate", fromDate);
			map.put("endDate", toDate);
			if (StrUtil.isEmpty(marketplaceid) || "all".equals(marketplaceid)) {
				marketplaceid = null;
			}
			if (StrUtil.isEmpty(groupid) || "all".equals(groupid)) {
				groupid = null;
			}
			if (StrUtil.isEmpty(pointname) || "all".equals(pointname)) {
				pointname = null;
			}
			if (StrUtil.isEmpty(datetype)) {
				datetype=null;
			}
			map.put("shopid", shopid);
			map.put("groupid", groupid);
			map.put("marketplace_name", pointname);
			map.put("currency", currency);
			map.put("datetype", datetype);
			List<Map<String, Object>> list = iAmzSettlementReportService.selectOtherin(map);
			return Result.success(list);
		}

		@PostMapping("/showOtherout")
		public Result<?>  showOtheroutAction(@RequestBody AmzSettlementDTO dto) {
			UserInfo user = UserInfoContext.get();
			String shopid = user.getCompanyid();
			String marketplaceid = dto.getMarketplaceid();
			String currency = dto.getCurrency();
			String fromDate = dto.getFromDate();
			String toDate =dto.getEndDate();
			String groupid = dto.getGroupid();
			String pointname =dto.getMarketplace_name();
			String datetype = dto.getDatetype();
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("fromDate", fromDate.trim());
			
			map.put("endDate", toDate.trim());
			if (StrUtil.isEmpty(marketplaceid) || "all".equals(marketplaceid)) {
				marketplaceid = null;
			}
			if (StrUtil.isEmpty(groupid) || "all".equals(groupid)) {
				groupid = null;
			}

			if (StrUtil.isEmpty(pointname) || "all".equals(pointname)) {
				pointname = null;
			}
			
			if (StrUtil.isEmpty(datetype)) {
				datetype = null;
			}
			map.put("shopid", shopid);
			map.put("marketplace_name", pointname);
			map.put("currency", currency);
			map.put("groupid", groupid);
			map.put("datetype", datetype);
			List<Map<String, Object>> list = iAmzSettlementReportService.selectOtherout(map);
			return Result.success(list);
		}
		

		


	 
		@PostMapping("/proCommodity")
		public Result<?>  getPaoductCommodityAction(@RequestBody AmzSettlementDTO dto) {
			Map<String, Object> map = new HashMap<String, Object>();
			UserInfo user = UserInfoContext.get();
			String ownerid = dto.getOwnerid();
			String search = dto.getSearch();
			String color = dto.getColor();
			String marketplaceid = dto.getMarketplaceid();
			String currency = dto.getCurrency();
			String fromDate = dto.getFromDate();
			String toDate =dto.getEndDate();
			String groupid = dto.getGroupid();
			String datetype = dto.getDatetype();
			Marketplace market = marketplaceService.findMapByMarketplaceId().get(marketplaceid);
			if(market!=null) {
				map.put("marketplace_name", market.getPointName());
				if("market".equals(currency)) {
					currency=market.getCurrency();
					dto.setCurrency(currency);
				}
				map.put("country", market.getMarket());	
			}else {
				if("market".equals(currency)) {
				    throw new BizException("没有选择站点，无法使用站点币种");
				}
				map.put("marketplace_name",null);
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (StrUtil.isNotEmpty(fromDate)) {
				fromDate=fromDate.replace("/", "-");
				map.put("fromDate", fromDate.trim());
			} else {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH, -7);
				fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
				map.put("fromDate", fromDate);
			}
			
			if (StrUtil.isNotEmpty(toDate)) {
				toDate=toDate.replace("/", "-");
				toDate=toDate.substring(0, 10)+" 23:59:59";
				map.put("endDate", toDate.trim());
			} else {
				toDate = GeneralUtil.formatDate(new Date(), sdf);
				map.put("endDate", toDate);
			}
			if (StrUtil.isEmpty(groupid) || "all".equals(groupid)) {
				groupid = null;
			}
			
			if (StrUtil.isEmpty(search)) {
				search = null;
			} else {
				search = "%" + search + "%";
			}
		
			if (StrUtil.isBlankOrUndefined(ownerid) || "all".equals(ownerid)) {
				ownerid = null;
			}
			
			if (StrUtil.isEmpty(color) || "all".equals(color)) {
				color = null;
			}
			if (user.isLimit(UserLimitDataType.operations)) {
				map.put("myself", user.getId());
			}
			if (StrUtil.isEmpty(datetype) ) {
			    map.put("datetype", null);
			}else {
				map.put("datetype", datetype);
			}
			
			
			map.put("groupid", groupid);
			map.put("shopid", user.getCompanyid());
			map.put("marketplaceid", marketplaceid);
			map.put("currency", currency);
			map.put("search", search);
			map.put("owner", ownerid);
			map.put("color", color);
			map.put("issummary", "false");
			String key = JSON.toJSONString(map);
			key=key+"skutype";
			List<Map<String, Object>> list = iAmzSettlementReportService.findCommodity(key,map);
 			if (list != null && list.size() > 0) {
				Map<String, Object> summap = list.get(0);
				IPage<Map<String, Object>> list2 =  dto.getListPage(list);
					if (summap.get("summary")!=null) {
						list2.getRecords().get(0).put("summary",summap.get("summary"));
					}
				return Result.success(list2);
			}
			return Result.success();
		}
		
		@PostMapping(value = "/downDataExcel")
		public void downDataExcelDateAction(@RequestBody AmzSettlementDTO dto,HttpServletResponse response ){
			// 创建新的Excel工作薄
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			// 将数据写入Excel
			Map<String, Object> map = new HashMap<String, Object>();
			UserInfo user = UserInfoContext.get();
			String fromDate = dto.getFromDate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			String datetype=dto.getDatetype();
			if (StrUtil.isNotEmpty(fromDate)) {
				fromDate=fromDate.replace("/", "-");
				map.put("fromDate", fromDate.trim());
			} else {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH, -7);
				fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
				map.put("fromDate", fromDate);
			}
			String toDate = dto.getEndDate();
			if (StrUtil.isNotEmpty(toDate)) {
				toDate=toDate.replace("/", "-");
				map.put("endDate", toDate.trim());
			} else {
				toDate = GeneralUtil.formatDate(new Date(), sdf);
				map.put("endDate", toDate);
			}
			String marketplaceid = dto.getMarketplaceid();
			if (StrUtil.isEmpty(marketplaceid) || "all".equals(marketplaceid)) {
				marketplaceid = null;
			}else {
				Marketplace mark = marketplaceService.findMapByMarketplaceId().get(marketplaceid);
				map.put("country", mark.getMarket());	
			}
			String marketplace_name = dto.getMarketplace_name();
			if (StrUtil.isEmpty(marketplace_name) || "all".equals(marketplace_name)) {
				marketplace_name = null;
			}
			String groupid = dto.getGroupid();
			if (StrUtil.isEmpty(groupid) || "all".equals(groupid)) {
				groupid = null;
			}
			String search = dto.getSearch();
			if (StrUtil.isEmpty(search)) {
				search = null;
			} else {
				search = "%" + search + "%";
			}
			String currency = dto.getCurrency();
			String ownerid = dto.getOwnerid();
			if (StrUtil.isEmpty(ownerid) || "all".equals(ownerid)) {
				ownerid = null;
			}
			String color = dto.getColor();
			if (StrUtil.isEmpty(color) || "all".equals(color)) {
				color = null;
			}
			if (user.isLimit(UserLimitDataType.owner)) {
				map.put("myself", user.getId());
			}
			if (StrUtil.isEmpty(datetype) ) {
			    map.put("datetype", null);
			}else {
				map.put("datetype", datetype);
			}
			Marketplace market = marketplaceService.findMapByMarketplaceId().get(marketplaceid);
			if(market!=null) {
				map.put("marketplace_name", market.getPointName());
				if("market".equals(currency)) {
					currency=market.getCurrency();
				}
			}else {
				map.put("marketplace_name",null);
			}
			if(StrUtil.isNotEmpty(dto.getId())) {
				map.put("id", dto.getId()); 
			}else {
				map.put("id", null); 
			}
			map.put("groupid", groupid);
			map.put("shopid", user.getCompanyid());
			map.put("marketplaceid", marketplaceid);
			map.put("currency", currency);
			map.put("search", search);
			map.put("owner", ownerid);
			map.put("color", color);
			map.put("issummary", "false");
			map.put("user", user);
			iAmzSettlementReportService.setExcelBook(workbook, map,user);
			try {
				response.setContentType("application/force-download");// 设置强制下载不打开
				response.addHeader("Content-Disposition", "attachment;fileName=CommodityRevenue" + System.currentTimeMillis() + ".xlsx");// 设置文件名
				ServletOutputStream fOut = response.getOutputStream();
				workbook.write(fOut);
				workbook.close();
				fOut.flush();
				fOut.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	 
		public Result<Map<String, Object>>  proCommoditySummaryAction(@RequestBody AmzSettlementDTO dto) {
			Map<String, Object> map = new HashMap<String, Object>();
			UserInfo user = UserInfoContext.get();
			String ownerid = dto.getOwnerid();
			String search = dto.getSearch();
			String color = dto.getColor();
			String marketplaceid = dto.getMarketplaceid();
			String currency = dto.getCurrency();
			String fromDate = dto.getFromDate();
			String toDate =dto.getEndDate();
			String groupid = dto.getGroupid();
			String marketplace_name =dto.getMarketplace_name();
			String datetype = dto.getDatetype();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
			if (StrUtil.isEmpty(datetype)  ) {
				datetype = null;
			}
			if (StrUtil.isNotEmpty(fromDate)) {
				map.put("fromDate", fromDate.trim());
			} else {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH, -7);
				fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
				map.put("fromDate", fromDate);
			}
			if (StrUtil.isNotEmpty(toDate)) {
				map.put("endDate", toDate.trim());
			} else {
				toDate = GeneralUtil.formatDate(new Date(), sdf);
				map.put("endDate", toDate);
			}
	
			if (StrUtil.isEmpty(marketplaceid) || "all".equals(marketplaceid)) {
				marketplaceid = null;
			}

			if (StrUtil.isEmpty(marketplace_name) || "all".equals(marketplace_name)) {
				marketplace_name = null;
			}

			if (StrUtil.isEmpty(groupid) || "all".equals(groupid)) {
				groupid = null;
			}

			if (StrUtil.isEmpty(search)) {
				search = null;
			} else {
				search = "%" + search + "%";
			}
		
		
			if (StrUtil.isEmpty(ownerid) || "all".equals(ownerid)) {
				ownerid = null;
			}
			
			if (StrUtil.isEmpty(color) || "all".equals(color)) {
				color = null;
			}
			if (user.isLimit(UserLimitDataType.operations)) {
				map.put("myself", user.getId());
			}
			map.put("datetype", datetype);
			map.put("groupid", groupid);
			map.put("shopid", user.getCompanyid());
			map.put("marketplaceid", marketplaceid);
			map.put("marketplace_name", marketplace_name);
			map.put("currency", currency);
			map.put("search", search);
			map.put("owner", ownerid);
			map.put("color", color);
			String ekey = JSON.toJSONString(map);
			Map<String, Object> result = iAmzSettlementReportService.findSettlementSummary(ekey,map);
			return Result.success( result);
		}
		
	 




		



		@PostMapping("/getDetail")
		public Result<?> getDetailAction(@RequestBody AmzSettlementDTO dto) {
			UserInfo user = UserInfoContext.get();
			String isother =dto.getIsother();
			String amazonAuthId = dto.getAmazonAuthId();
			String sku = dto.getSku();
			String settlement_id = dto.getSettlement_id();
			String currency = dto.getCurrency();
			String fromDate = dto.getFromDate();
			String endDate =dto.getEndDate();
			String groupid = dto.getGroupid();
			String market =dto.getMarketplace_name();
			String datetype = dto.getDatetype();
			
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("shopid", user.getCompanyid());
			param.put("sku", StrUtil.isEmpty(sku) ? null : sku);
			param.put("currency", StrUtil.isEmpty(currency) ? null : currency);
			param.put("isother", StrUtil.isEmpty(isother) ? null : isother);
			param.put("fromDate", StrUtil.isEmpty(fromDate) ? null : fromDate);
			if (endDate != null) {
				endDate = endDate.trim();
			}
			if (endDate != null && endDate.length() >= 10) {
				endDate = endDate.substring(0, 10) + " 23:59:59";
			}
			param.put("endDate", StrUtil.isEmpty(endDate) ? null : endDate);
			param.put("datetype", StrUtil.isEmpty(datetype) ? null : datetype);
			param.put("settlement_id", StrUtil.isEmpty(settlement_id) ? null : settlement_id);
			param.put("groupid", StrUtil.isEmpty(groupid) || "all".equals(groupid) ? null : groupid);
			param.put("market", StrUtil.isEmpty(market) || "all".equals(market) ? null : market);
			param.put("amazonAuthId", StrUtil.isEmpty(amazonAuthId) || "all".equals(amazonAuthId) ? null : amazonAuthId);
			return Result.success( iAmzSettlementReportService.getDetail(param));
		}


		@PostMapping("/getSettmentDetail")
		public Result<?> getSettmentDetailAction(@RequestBody AmzSettlementDTO dto) {
			UserInfo user = UserInfoContext.get();
			String amazonAuthId = dto.getAmazonAuthId();
			String settlement_id = dto.getSettlement_id();
			String currency = dto.getCurrency();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("shopid", user.getCompanyid());
			param.put("currency", StrUtil.isEmpty(currency) ? null : currency);
			param.put("settlement_id", StrUtil.isEmpty(settlement_id) ? null : settlement_id);
			param.put("amazonAuthId", StrUtil.isEmpty(amazonAuthId) || "all".equals(amazonAuthId) ? null : amazonAuthId);
			Map<String, Object> detail = iAmzSettlementReportService.getDetailSettment(param);
			return Result.success( detail );
		}
		
		@PostMapping("/downloadSettlementOverAll")
		public void downloadSettlementOverAllAction(@RequestBody AmzSettlementDTO dto, HttpServletResponse response)  {
			// 创建新的Excel工作薄
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			// 将数据写入Excel
			UserInfo user = UserInfoContext.get();
			String marketplaceid = dto.getMarketplaceid();
			String currency = dto.getCurrency();
			String fromDate = dto.getFromDate();
			String toDate =dto.getEndDate();
			String groupid = dto.getGroupid();
			String marketplace_name =dto.getMarketplace_name();
			String datetype = dto.getDatetype();
			String search=dto.getSearch();
			Map<String, Object> map = new HashMap<String, Object>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (StrUtil.isNotEmpty(fromDate)) {
				fromDate=fromDate.replace("/", "-");
				map.put("fromDate", fromDate.trim());
			} else {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH, -7);
				fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
				map.put("fromDate", fromDate);
			}
			if (StrUtil.isNotEmpty(toDate)) {
				toDate=toDate.replace("/", "-");
				map.put("endDate", toDate.trim());
			} else {
				toDate = GeneralUtil.formatDate(new Date(), sdf);
				map.put("endDate", toDate);
			}
			if (StrUtil.isEmpty(marketplaceid) || "all".equals(marketplaceid)) {
				marketplaceid = null;
			}else {
				Marketplace mark = marketplaceService.findMapByMarketplaceId().get(marketplaceid);
				map.put("country", mark.getMarket());	
			}
			if (StrUtil.isEmpty(marketplace_name) || "all".equals(marketplace_name)) {
				marketplace_name = null;
			}
			if (StrUtil.isEmpty(groupid) || "all".equals(groupid)) {
				groupid = null;
			}
			if (StrUtil.isEmpty(search)) {
				search = null;
			} else {
				search = "%" + search + "%";
			}
	 
			if (user.isLimit(UserLimitDataType.operations)) {
				map.put("myself", user.getId());
			}
			if (StrUtil.isEmpty(datetype) ) {
			    map.put("datetype", null);
			}else {
				map.put("datetype", datetype);
			}
			Marketplace market = marketplaceService.findMapByMarketplaceId().get(marketplaceid);
			if(market!=null) {
				map.put("marketplace_name", market.getPointName());
				if("market".equals(currency)) {
					currency=market.getCurrency();
				}
			}else {
				map.put("marketplace_name",null);
			}
			map.put("groupid", groupid);
			map.put("shopid", user.getCompanyid());
			map.put("marketplaceid", marketplaceid);
			map.put("currency", currency);
			map.put("search", search);
			map.put("isdownload", true);
			if(groupid!=null&&marketplaceid!=null) {
				AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
				if(auth!=null) {
					map.put("amazonAuthId", auth.getId());
					map.put("sellerid", auth.getSellerid());
				}
			}
		    List<Map<String, Object>> list = iAmzSettlementReportService.downloadSettlementGroupOverAll(map);
		    iAmzSettlementReportService.setExcelBookOverall(workbook, list);
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
		
		@PostMapping("/getSettlementOverAll")
		public Result<?> getSettlementOverAllAction(@RequestBody AmzSettlementDTO dto) {
			UserInfo user = UserInfoContext.get();
			
			String marketplaceid = dto.getMarketplaceid();
			String currency = dto.getCurrency();
			String fromDate = dto.getFromDate();
			String toDate =dto.getEndDate();
			String groupid = dto.getGroupid();
			String marketplace_name =dto.getMarketplace_name();
			String datetype = dto.getDatetype();
			String search=dto.getSearch();
			
			Map<String, Object> map = new HashMap<String, Object>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (StrUtil.isNotEmpty(fromDate)) {
				fromDate=fromDate.replace("/", "-");
				map.put("fromDate", fromDate.trim());
			} else {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH, -7);
				fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
				map.put("fromDate", fromDate);
			}
			if (StrUtil.isNotEmpty(toDate)) {
				toDate=toDate.replace("/", "-");
				map.put("endDate", toDate.trim());
			} else {
				toDate = GeneralUtil.formatDate(new Date(), sdf);
				map.put("endDate", toDate);
			}
			if (StrUtil.isEmpty(marketplaceid) || "all".equals(marketplaceid)) {
				marketplaceid = null;
			}else {
				Marketplace mark = marketplaceService.findMapByMarketplaceId().get(marketplaceid);
				map.put("country", mark.getMarket());	
			}
			if (StrUtil.isEmpty(marketplace_name) || "all".equals(marketplace_name)) {
				marketplace_name = null;
			}
			if (StrUtil.isEmpty(groupid) || "all".equals(groupid)) {
				groupid = null;
			}
			if (StrUtil.isEmpty(search)) {
				search = null;
			} else {
				search = "%" + search + "%";
			}
	 
			if (user.isLimit(UserLimitDataType.operations)) {
				map.put("myself", user.getId());
			}
			if (StrUtil.isEmpty(datetype) ) {
			    map.put("datetype", null);
			}else {
				map.put("datetype", datetype);
			}
			map.put("groupid", groupid);
			map.put("shopid", user.getCompanyid());
			map.put("marketplaceid", marketplaceid);
			Marketplace market = marketplaceService.findMapByMarketplaceId().get(marketplaceid);
			if(market!=null) {
				map.put("marketplace_name", market.getPointName());
				if("market".equals(currency)) {
					currency=market.getCurrency();
				}
			}else {
				map.put("marketplace_name",null);
			}
			map.put("currency", currency);
			map.put("search", search);
			if(groupid!=null&&marketplaceid!=null) {
				AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
				if(auth!=null) {
					map.put("amazonAuthId", auth.getId());
					map.put("sellerid", auth.getSellerid());
				}
			}
		   List<Map<String, Object>> list = iAmzSettlementReportService.getSettlementGroupOverAll(map);
		   if(list!=null) {
			   return Result.success( list);
		   }else {
			   return Result.success();
		   }
		}
		
		@GetMapping("/getGroupMarket")
		public Result<?> getGroupMarketAction(String groupid,String marketplaceid) {
			Map<String, Object> map = new HashMap<String, Object>();
			AmazonGroup group = amazonGroupService.getById(groupid);
			Marketplace market = marketplaceService.getById(marketplaceid);
			map.put("group", group);
			map.put("market", market);
			return Result.success(map);
		}
		

		
}
