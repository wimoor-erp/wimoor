package com.wimoor.amazon.adv.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.adv.common.pojo.AmazonGroup;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.pojo.Marketplace;
import com.wimoor.amazon.adv.common.service.IAmazonGroupService;
import com.wimoor.amazon.adv.common.service.IAmazonReportAdvSummaryService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.common.service.IMarketplaceService;
import com.wimoor.amazon.adv.controller.pojo.dto.QueryForDownload;
import com.wimoor.amazon.adv.controller.pojo.dto.QueryForSumProductDTO;
import com.wimoor.amazon.adv.report.service.IAmzAdvReportService;
import com.wimoor.amazon.adv.report.service.IAmzAdvSumProductAdsService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;


@Api(tags = "账期月报")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/advReport")
public class AdvertReportController {
 
	@Resource
	IAmzAdvReportService amzAdvReportService;
	@Resource
	com.wimoor.amazon.adv.report.service.IAmzAdvReportHandlerService amzAdvReportHandlerService;
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	IAmazonGroupService amazonGroupService;
	@Resource
	IMarketplaceService marketplaceService;
	@Resource
	IAmzAdvSumProductAdsService amzAdvSumProductAdsService;
	@Resource
	IAmazonReportAdvSummaryService amazonReportAdvSummaryService;
	
	@RequestMapping("/view")
	public String viewAction(HttpServletRequest request, Model model){
		
		return "pages/advertising/advert_report_list";
	}
	
 
	@RequestMapping("/downloadview")
	public String downloadviewAction(HttpServletRequest request, Model model){
		UserInfo user = UserInfoContext.get();
		String shopid = user.getCompanyid();
		Example example1 = new Example(AmazonGroup.class);
		Criteria crit2 = example1.createCriteria();
		crit2.andEqualTo("shopid", shopid);
		List<AmazonGroup> glist = amazonGroupService.selectByExample(example1);
		request.setAttribute("grouplist", glist);
		List<Marketplace> marketlist = marketplaceService.findMarketplaceByShopId(shopid);
		request.setAttribute("marketlist", marketlist);
		return "pages/advertising/advert_report_download";
	}
	
	@RequestMapping("/noAdvDate")
	public String noAdvDateviewAction(HttpServletRequest request, Model model){
		return "pages/frame/noAdvDate";
	}
	
	@PostMapping("/downExcelDate")
	public String downExcelDateAction(@RequestBody QueryForDownload dto,HttpServletResponse response){
		Map<String, Object> param = new HashMap<String, Object>();
		String groupid = dto.getGroupid();
		String marketplaceid = dto.getMarketplaceid();
		String reporttype = dto.getReporttype();
		String campaigntype = dto.getCampaigntype();
		String fromDatestr = dto.getFromDate();
		String endDatestr = dto.getEndDate();
		String dateType = dto.getDateType().toLowerCase();
		if(fromDatestr != null && endDatestr != null) {
			fromDatestr = fromDatestr.replaceAll("/", "-").trim();
			endDatestr = endDatestr.replaceAll("/", "-").trim();
			param.put("fromDate", fromDatestr);
			param.put("endDate", endDatestr);
		}
		if (!StringUtil.isEmpty(groupid) || !StringUtil.isEmpty(marketplaceid)) {
			Map<String, Object> maplist = amzAdvAuthService.getProfileByGroupAndmarkplace(groupid, marketplaceid);
			param.put("reporttype", reporttype);
			param.put("campaigntype", campaigntype);
			param.put("dateType", dateType);
			param.put("profileid", maplist.get("id"));
			param.put("currency", maplist.get("currency"));
			param.put("marketplacename", maplist.get("region"));
			param.put("groupname", maplist.get("groupname"));
		}
		SXSSFWorkbook workbook = null;
		ServletOutputStream fOut = null;
		try {
			workbook = amzAdvReportService.setExcelBook(param);
		}catch(BaseException e) {
			throw new BizException("没有可下载的数据");
		}
		try {
			response.setHeader("Set-Cookie", "fileDownload=true; path=/");
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName="+campaigntype+" "+reporttype+" report.xlsx");// 设置文件名
			fOut = response.getOutputStream();
			workbook.write(fOut);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(fOut != null) {
				try {
					fOut.flush();
					fOut.close();
					if(workbook != null) {
						workbook.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping("/getRegionByAdvGroup")
	public Object getRegionAction(HttpServletRequest request){
		UserInfo user = UserInfoContext.get();
		String shopid=user.getCompanyid();
		String advgroupid=request.getParameter("advgroupid");
		if(StringUtil.isNotEmpty(advgroupid)){
			List<Marketplace> list = amzAdvAuthService.getRegionByAdvGroup(shopid,advgroupid);
			return list;
		}else{
			return null;
		}
	}
	
	@ResponseBody
	@RequestMapping("/getProfileIdByGroup")
	public Object getProfileIdByGroupAction(HttpServletRequest request){
		UserInfo user = UserInfoContext.get();
		String shopid=user.getCompanyid();
		String advgroupid=request.getParameter("advgroupid");
		if(StringUtil.isNotEmpty(advgroupid)){
			List<AmzAdvProfile> list = amzAdvAuthService.getProfileIdByGroup(shopid,advgroupid);
			return list;
		}else{
			return null;
		}
	}
	@ResponseBody
	@RequestMapping("/getAdvSummary")
	public Object getAdvSummaryAction(HttpServletRequest request){
		UserInfo user = UserInfoContext.get();
		String shopid=user.getCompanyid();
		String advgroupid=request.getParameter("advgroupid");
		if(StringUtil.isNotEmpty(advgroupid)){
			List<AmzAdvProfile> list = amzAdvAuthService.getProfileIdByGroup(shopid,advgroupid);
			return list;
		}else{
			return null;
		}
	}
	
	@PostMapping("/getmonthsum")
	public Result<Map<String, Object>> getMonthSumAction(@RequestBody QueryForSumProductDTO dto){
		String begin =dto.getBegin();
		String end = dto.getEnd();
		String groupid = dto.getGroupid();
		String profileid = dto.getProfileid();
		String currency = dto.getCurrency();
		UserInfo user = UserInfoContext.get();
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("shopid", user.getCompanyid());
		param.put("groupid", groupid);
		param.put("profileid", profileid);
		param.put("currency",  currency);
		//mmarketplaceId 给需要真正亚马逊站点的地方使用
		//pmarketplaceId 给需要销售渠道，或站点的地方用
		//sellerid 给需要seller的地方使用如销量，在销售产品数量等
		if(StringUtil.isNotEmpty(groupid) && !"all".equals(groupid)) {
			if(StringUtil.isNotEmpty(profileid) && !"all".equals(profileid)) {
				AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(new BigInteger(profileid));
				Map<String, Marketplace> allmarket = marketplaceService.findMapByMarketplaceId();
				param.put("pmarketplaceId",allmarket.get(profile.getMarketplaceid()).getPointName());
				param.put("mmarketplaceId",profile.getMarketplaceid());
				param.put("sellerid", profile.getSellerid());
			}else {
				List<String> sellerList = new ArrayList<String>();
				List<Map<String, Object>> list = amzAdvAuthService.getSelleridBygroup(groupid);
				for(Map<String,Object> map : list) {
					String seller = (String) map.get("sellerId");
					sellerList.add(seller);
				}
				param.put("sellerList", sellerList);
				param.put("profileid", null);
			}
		}else {
			param.put("groupid", null);
			param.put("profileid", null);
			if(StringUtil.isNotEmpty(profileid) && !"all".equals(profileid)) {
				Map<String, Marketplace> allmarket = marketplaceService.findMapByMarketplaceId();
				param.put("pmarketplaceId", allmarket.get(profileid).getPointName());
				param.put("marketplaceId", profileid);
				param.put("mmarketplaceId",profileid);
			}
		}
		if(StringUtil.isNotEmpty(begin)) {
			param.put("begin", begin.replaceAll("/", "-").trim()+"-01");
			param.put("beginDate", begin.replaceAll("/", "-").trim()+"-01");
		}
		if(StringUtil.isNotEmpty(end)) {
			String[] endarray=end.split("-");
			if(endarray==null||endarray.length<2) {
				endarray=end.split("/");
			}
			if(endarray.length>=2) {
			   String endate = GeneralUtil.getLastDayOfMonth(Integer.parseInt(endarray[0].trim()), Integer.parseInt(endarray[1].trim()));
				param.put("end",endate);
				param.put("endDate",endate);
			}
			
		}
		Map<String, Object> result = amzAdvSumProductAdsService.getMonthSumProduct(param);
		return Result.success(result) ;
	}
	
	@PostMapping("/getsumproduct")
	public Result<Map<String, Object>> getSumProductAction(@RequestBody QueryForSumProductDTO dto){
		String begin =dto.getBegin();
		String end = dto.getEnd();
		String type = dto.getType();
		String groupid = dto.getGroupid();
		String profileid = dto.getProfileid();
		String currency = dto.getCurrency();
		UserInfo user = UserInfoContext.get();
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("shopid", user.getCompanyid());
		param.put("type", type);
		param.put("currency",  currency);
		Map<String, Marketplace> allmarket = marketplaceService.findMapByMarketplaceId();
		if(StringUtil.isNotEmpty(profileid) && !"all".equals(profileid)) {
			if(StringUtil.isNotEmpty(groupid) && !"all".equals(groupid)) {
				AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(new BigInteger(profileid));
				param.put("pmarketplaceId", allmarket.get(profile.getMarketplaceid()).getPointName());
				param.put("sellerid", profile.getSellerid());
				param.put("profileid", profileid);
				param.put("groupid", groupid);
			}else {
				param.put("marketplaceId", profileid);
				param.put("pmarketplaceId", allmarket.get(profileid).getPointName());
			}
		}else {
			if(StringUtil.isNotEmpty(groupid) && !"all".equals(groupid)) {
				List<Map<String, Object>> list = amzAdvAuthService.getSelleridBygroup(groupid);
				List<String> sellerList = new ArrayList<String>();
				for(Map<String,Object> map : list) {
					String seller = (String) map.get("sellerId");
					sellerList.add(seller);
				}
				param.put("sellerList", sellerList);
				param.put("groupid", groupid);
			} 
		}
		if(StringUtil.isNotEmpty(begin)) {
			param.put("begin", begin.replaceAll("/", "-").trim());
			param.put("beginDate", begin.replaceAll("/", "-").trim());
		}
		if(StringUtil.isNotEmpty(end)) {
			param.put("end",end.replaceAll("/", "-").trim());
			param.put("endDate", end.replaceAll("/", "-").trim());
		}
		Map<String, Object> result =new HashMap<String,Object>();
		Map<String, Object> map = amzAdvSumProductAdsService.getSumProduct(param);
		BigDecimal mapordersum = amzAdvSumProductAdsService.orderSummaryAll(param);
		Map<String, Object> chartdata = amzAdvSumProductAdsService.getDaysSumProduct(param);
		result.put("summary", map);
		result.put("ordersummary", mapordersum);
		result.put("chartdata", chartdata);
		return Result.success(result) ;
	}
	
	@PostMapping("/findAdvert")
	public Result<List<Map<String, Object>>> findAdvert(@RequestBody Map<String,Object> param) {
		return Result.success(amazonReportAdvSummaryService.findAdvert(param)) ;
	}
	
}
