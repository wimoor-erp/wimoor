package com.wimoor.amazon.finances.controller;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.finances.pojo.dto.AmzSettlementDTO;
import com.wimoor.amazon.finances.pojo.entity.AmzSettlementAccStatement;
import com.wimoor.amazon.finances.service.IAmzFinSettlementFormulaService;
import com.wimoor.amazon.finances.service.IAmzSettlementAccStatementService;
import com.wimoor.amazon.finances.service.IAmzSettlementReportService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.pojo.entity.BasePageQuery;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.common.user.UserLimitDataType;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "账期结账即存档")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/settlementAccStatement")
public class AmzSettlementAccStatementController {
  final IAmzSettlementAccStatementService iAmzSettlementAccStatementService;
  final IAmzSettlementReportService iAmzSettlementReportService;
  final IAmzFinSettlementFormulaService iAmzFinSettlementFormulaService;
  final IMarketplaceService iMarketplaceService;
	@SystemControllerLog( "保存费用结算")
	@PostMapping("/saveFinStatement")
	@CacheEvict(value = { "findSettlementSKUCache"}, allEntries = true)
	public Result<?> saveFinStatementAction(@RequestBody AmzSettlementDTO dto) {
		UserInfo user = UserInfoContext.get();
		String fromDate =dto.getFromDate();
		String endDate = dto.getEndDate();
		String datetype=dto.getDatetype();
		String country=null;
		String marketplaceid =dto.getMarketplaceid();
		String marketplace_name = dto.getMarketplace_name();
		String groupid = dto.getGroupid();
		String search =dto.getSearch();
		String currency =dto.getCurrency();
		String ownerid = dto.getOwnerid();
		String color = dto.getColor();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (StrUtil.isEmpty(datetype)  ) {
			datetype = null;
		}
		if (StrUtil.isNotEmpty(fromDate)) {
			fromDate= fromDate.trim();
		} else {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, -7);
			fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
		}
		if (StrUtil.isNotEmpty(endDate)) {
			endDate= endDate.trim();
		} else {
			endDate = GeneralUtil.formatDate(new Date(), sdf);
		}
		if (StrUtil.isEmpty(marketplaceid) || "all".equals(marketplaceid)) {
			marketplaceid = null;
		}else {
			Marketplace market = iMarketplaceService.selectByPKey(marketplaceid);
			country=market.getMarket();
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
		Map<String,Object> param=new HashMap<String,Object>();
		Marketplace market = iMarketplaceService.findMapByMarketplaceId().get(marketplaceid);
		if(market!=null) {
			param.put("marketplace_name", market.getPointName());
			if("market".equals(currency)) {
				currency=market.getCurrency();
			}
		}else {
			param.put("marketplace_name",null);
		}
		
		 param.put("fromDate", fromDate.trim());
		 param.put("datetype", datetype);
		 param.put("endDate", endDate.trim());
		 param.put("groupid", groupid);
		 param.put("shopid", user.getCompanyid());
		 param.put("marketplaceid", marketplaceid);
		 param.put("currency", currency);
	     param.put("issummary", "false");
	     param.put("country", country);	
	     if (user.isLimit(UserLimitDataType.operations)) {
	    	 param.put("myself", user.getId());
		 }
		String ekey = JSON.toJSONString(param);
		Map<String, Object> summarydata = iAmzSettlementReportService.findSettlementSummary(ekey,param);
		if(summarydata!=null) {
			param.put("summarydata", summarydata);
		}
		List<Map<String, Object>> list = iAmzSettlementReportService.findCommodity(param);
		Map<String, String> field = iAmzFinSettlementFormulaService.getformulaTitle(user.getCompanyid());
		param.put("currency", currency);
		Map<String,Object> res=iAmzSettlementAccStatementService.saveFinStatement(user,param,list,field);
		return Result.success(res);
	}
	
	@PostMapping("/findAmzSettlementAccStatement")
	public Result<?> findAmzSettlementAccStatementAction(@RequestBody BasePageQuery dto) {
		UserInfo user = UserInfoContext.get();
		List<Map<String, Object>> list = iAmzSettlementAccStatementService.findAmzSettlementAccStatement(user.getCompanyid());
		return Result.success(dto.getListPage(list));
	}
	
	@PostMapping("/selectSettlementOpen")
	public Result<?> selectSettlementOpenAction(@RequestBody AmzSettlementDTO dto) {
		UserInfo user = UserInfoContext.get();
		IPage<Map<String, Object>> list = iAmzSettlementAccStatementService.selectSettlementOpen(dto,user.getCompanyid());
		return Result.success(list);
	}
	
	
	 @PostMapping("/downloadSettlementOpen")
	   public void downDataExcelByRateAction(@RequestBody AmzSettlementDTO dto, HttpServletResponse response)  {
	   	// 创建新的Excel工作薄
	   	SXSSFWorkbook workbook = new SXSSFWorkbook();
	   	// 将数据写入Excel
	   	UserInfo user = UserInfoContext.get();
		   Map<String,Object> map=new HashMap<String, Object>();
			 map.put("shopid",user.getCompanyid());
			 map.put("groupid",dto.getGroupid());
			 map.put("marketplaceid",dto.getMarketplaceid());
			 if(StrUtil.isNotEmpty(dto.getAmazonAuthId())) {
				 map.put("authid",dto.getAmazonAuthId());
			 }
			 if(StrUtil.isNotEmpty(dto.getEndDate())) {
				 map.put("startDate",dto.getFromDate());
				 map.put("endDate",dto.getEndDate());
			 }
		    iAmzSettlementAccStatementService.getDownloadSettOpen(workbook, map);
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
	
	
	@SystemControllerLog( "删除费用结算")
	@GetMapping("/deleteAmzSettlementAccStatement")
	public Result<?> deleteAmzSettlementAccStatementAction(String id) {
		return  Result.success(iAmzSettlementAccStatementService.deleteAmzSettlementAccStatement(id));
	}
	
	@SuppressWarnings({ "unused", "unchecked" })
	@PostMapping("/proCommodityStatement")
	public Object proCommodityStatementAction(@RequestBody AmzSettlementDTO dto) {
		Map<String, Object> map = new HashMap<String, Object>();
		String id = dto.getId();
		String search = dto.getSearch();
		String ownerid = dto.getOwnerid();
		String color =dto.getColor();
		if (StrUtil.isEmpty(search)) {
			search = null;
		}  
		if (StrUtil.isEmpty(ownerid) || "all".equals(ownerid)) {
			ownerid = null;
		}
		if (StrUtil.isEmpty(color) || "all".equals(color)) {
			color = null;
		}
		map.put("id", id);
		map.put("search", search);
		map.put("owner", ownerid);
		map.put("color", color);
 
		AmzSettlementAccStatement statement = iAmzSettlementAccStatementService.findCommodityStatement(id);
		byte[] listbyte = statement.getListdata();
		String liststr = null;
		try {
			liststr = new String(listbyte,"utf-8");
;
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		 
		JSONArray jsonArray =GeneralUtil.getJsonArray(liststr);
		List<Map<String,Object>> restlist=new ArrayList<Map<String,Object>>();
		 for(int i=0;i<jsonArray.size();i++){
			 JSONObject myitem = jsonArray.getJSONObject(i);
			 boolean needadd=true;
			 if(search!=null) {
				Object skuobj=myitem.get("sku");
				 if(skuobj==null||!skuobj.toString().contains(search)) {
					 needadd=false;
				 }
			 }
			 if(color!=null) {
				 Object colorobj=myitem.get("color");
				 if(colorobj==null||!colorobj.toString().equals(color)) {
					 needadd=false;
				 }
			 }
			 if(ownerid!=null) {
				 Object owneridobj=myitem.get("owner");
				 if(owneridobj==null||!owneridobj.toString().equals(ownerid)) {
					 needadd=false;
				 }
			 }
            if(needadd) {
				 restlist.add(myitem);
			 }
		}
		if (restlist != null && restlist.size() > 0) {
			Map<String, Object> summap = restlist.get(0);
			IPage<Map<String, Object>> list2 = dto.getListPage(restlist);
			 
			if(dto.getCurrentpage()==1) {
				byte[] summarydatabyte = statement.getSummaryall();
				String summarydatastr = null;
				try {
					summarydatastr = new String(summarydatabyte,"utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Map<String, Object> summarydata = (Map<String, Object>) JSON.parse(summarydatastr);
				if(summarydata!=null&&list2!=null&&list2.getRecords().size()>0) {
					list2.getRecords().get(0).put("summarydata", summarydata);
				}
			}
			return Result.success(list2) ;
		}else {
			return Result.success();
		}
	}
	
}
