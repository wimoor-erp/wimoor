package com.wimoor.amazon.finances.controller;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.finances.pojo.dto.AmzSettlementDTO;
import com.wimoor.amazon.finances.pojo.dto.FinDataMonthDTO;
import com.wimoor.amazon.finances.service.IAmzSettlementSummarySkuMonthService;
import com.wimoor.amazon.summary.service.IAmazonSettlementAnalysisService;
import com.wimoor.common.mvc.ProgressHelper;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;


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
}

