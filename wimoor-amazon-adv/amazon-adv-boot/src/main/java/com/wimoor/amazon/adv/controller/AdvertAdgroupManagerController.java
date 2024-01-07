package com.wimoor.amazon.adv.controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.controller.pojo.dto.QueryForList;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvAdgroupsHsa;
import com.wimoor.amazon.adv.sb.service.IAmzAdvAdgroupsHsaService;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvAdgroupsSD;
import com.wimoor.amazon.adv.sd.service.IAmzAdvAdgroupsSDService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvAdgroups;
import com.wimoor.amazon.adv.sp.service.IAmzAdvAdGroupService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import tk.mybatis.mapper.util.StringUtil;
 
 
@Api(tags = "广告组接口")
@RestController 
@RequestMapping("/api/v1/advAdgroupManager") 
public class AdvertAdgroupManagerController {

	 
	@Resource
	IAmzAdvAdGroupService amzAdvAdGroupService;
	@Resource 
	IAmzAdvAdgroupsSDService amzAdvAdgroupsSDService;
	@Resource
	IAmzAdvAdgroupsHsaService  amzAdvAdgroupsHsaService;
 
	
	@GetMapping("/loadAdGroup")
	public Result<?>  getAdGroupAction(String profileid,String campaignsid,String campaignType) {
				UserInfo user = UserInfoContext.get();
		if (StringUtil.isEmpty(profileid) || "all".equals(profileid)) {
			return Result.failed();
		}
		if(campaignType!=null&&"sd".equals(campaignType.toLowerCase())) {
			return Result.success(amzAdvAdgroupsSDService.getAdGroupByCampaignsId(user, new BigInteger(profileid), campaignsid));
		}else if(campaignType!=null&&"sb".equals(campaignType.toLowerCase())){
			return Result.success( amzAdvAdgroupsHsaService.getAdGroupByCampaignsId(user, new BigInteger(profileid), campaignsid));
		}else {
			return Result.success( amzAdvAdGroupService.getAdGroupByCampaignsId(user, new BigInteger(profileid), campaignsid));
		}
	}
	 
    @ApiOperation("查询广告组")
	@PostMapping("/getAdGroupList")
	public Result<PageList<Map<String,Object>>> getAdGroupListAction(@ApiParam("查询广告组") @RequestBody QueryForList query){
	 	UserInfo user = UserInfoContext.get();
		Map<String,Object> map = AdvertController.amzAdvParameterMap(query);
		map.put("shopid", user.getCompanyid());
		PageList<Map<String,Object>> list = amzAdvAdGroupService.getAdGroupList(map, query.getPageBounds());
		if(list != null && list.size() > 0) {
			for(Map<String, Object> item : list) {
				if("paused".equals(item.get("campaignStatus")) && "enabled".equals(item.get("status"))) {
					item.put("passiveState", "paused");
				}
				if("archived".equals(item.get("campaignStatus"))) {
					item.put("passiveState", "archived");
				}
			}
			AdvertController.convertMapRemoveNA(list);
			Boolean isZeroPage = (Boolean) map.get("isZeroPage");
			list.get(0).put("isZeroPage", isZeroPage);
			return Result.success(list);
		}else {
			return Result.success(new PageList<Map<String,Object>>());
		}
	}
	
    @GetMapping("/getAdGroupsBid/{profileid}/{campaignType}")
	public Result<?> getAdGroupsBidAction(@PathVariable String profileid,@PathVariable String campaignType, String adgroupid ) {
		UserInfo user = UserInfoContext.get();
		if("sd".equals(campaignType.toLowerCase())) {
			return Result.success(); 
		}else if("sp".equals(campaignType.toLowerCase())){
			return Result.success(amzAdvAdGroupService.getAdGroupsBid(user, new BigInteger(profileid), new BigInteger(adgroupid))); 
		}else {
			return Result.success(); 
		}
	}
    
	@PostMapping("/createAdGroups/{profileid}/{campaignType}")
	public Result<?> createAdGroupsAction(@PathVariable String profileid,@PathVariable String campaignType, @RequestBody Map<String,Object> params ) {
		UserInfo user = UserInfoContext.get();
		String json = params.get("jsonstr").toString();
		JSONObject adGroup = GeneralUtil.getJsonObject(json);
		JSONArray adGroupArray =  adGroup.getJSONArray("adGroupArray");
		if(campaignType==null) {
			throw new BizException("广告类型不能为空");
		}
		if("sd".equals(campaignType.toLowerCase())) {
			return Result.success(amzAdvAdgroupsSDService.amzCreateAdGroups(user, profileid, adGroupArray)); 
		}else if("sp".equals(campaignType.toLowerCase())){
			return Result.success(amzAdvAdGroupService.amzCreateAdGroups(user, new BigInteger(profileid), adGroupArray)); 
		}else {
			return Result.success(amzAdvAdgroupsHsaService.amzCreateAdGroups(user, new BigInteger(profileid), adGroupArray)); 
		}
	}
	
    
	@PostMapping("/updateAdGroups/{profileid}/{campaignType}") 
	public Result<String> updateAdGroupsAction(@PathVariable String profileid,@PathVariable String campaignType, @RequestBody Map<String,Object> params ) {
		UserInfo user = UserInfoContext.get();
		String json = params.get("jsonstr").toString();
		JSONObject adGroup = GeneralUtil.getJsonObject(json);
		JSONArray adGroupArray =  adGroup.getJSONArray("adGroupArray");
		if(campaignType==null) {
			throw new BizException("广告类型不能为空");
		}
		if("sd".equals(campaignType.toLowerCase())) {
		    amzAdvAdgroupsSDService.amzUpdateAdGroups(user, new BigInteger(profileid), adGroupArray); 
		}else if("sp".equals(campaignType.toLowerCase())){
		    amzAdvAdGroupService.amzUpdateAdGroups(user, new BigInteger(profileid), adGroupArray); 
		}else {
		    amzAdvAdgroupsHsaService.amzUpdateAdGroups(user, new BigInteger(profileid), adGroupArray); 
		}
		
		return Result.success();
	}
	
    
	
	@ApiOperation("查询广告活动图表")
	@PostMapping("/getAdGroupChart")
	public Result<Map<String,Object>> getAdGroupChartAction(@ApiParam("查询广告活动") @RequestBody QueryForList query){
		UserInfo user = UserInfoContext.get();
		Map<String,Object> map = AdvertController.amzAdvParameterMap(query); 
		String bytime =query.getBytime();
		map.put("bytime", bytime);
		map.put("shopid", user.getCompanyid());
		Map<String,Object> mapList = amzAdvAdGroupService.getAdGroupChart(map);
		return Result.success(mapList);
	}
	
	@PostMapping("/getAdgroups/{profileid}/{campaignType}")
	public Result<?> getAdgroupsAction(@PathVariable String profileid,@PathVariable String campaignType,@RequestBody List<String> idlist){
		if(idlist==null)return Result.failed();
		UserInfo user = UserInfoContext.get();
		List<Object> result=new ArrayList<Object>();
		if(campaignType.toLowerCase().equals("sp")) {
				JSONObject param = new JSONObject();
				JSONObject adGroupIdFilter = new JSONObject();
				adGroupIdFilter.put("include", idlist);
				param.put("adGroupIdFilter", adGroupIdFilter);
				param.put("includeExtendedDataFields", false);
				List<AmzAdvAdgroups> list = amzAdvAdGroupService.amzListAdGroups(new BigInteger(profileid),param);
				result.addAll(list);
		}
		if(campaignType.toLowerCase().equals("sb")) {
			  for(String item:idlist) {
				    String ids=item;
					Map<String,Object> param=new HashMap<String,Object>();
					JSONObject adGroupIdFilter = new JSONObject();
					adGroupIdFilter.put("include", idlist);
					param.put("adGroupIdFilter",ids);
					param.put("includeExtendedDataFields", false);
					List<AmzAdvAdgroupsHsa> list = amzAdvAdgroupsHsaService.amzListAdGroups(new BigInteger(profileid),param);
					result.addAll(list);
			  }
				
		}
		if(campaignType.toLowerCase().equals("sd")) {
			  for(String item:idlist) {
				    String ids=item;
					Map<String,Object> param=new HashMap<String,Object>();
					param.put("adGroupIdFilter",ids);
					List<AmzAdvAdgroupsSD> list = amzAdvAdgroupsSDService.amzListAdGroupsExt(user,new BigInteger(profileid),param);
					result.addAll(list);
			  }
				
		}
		return Result.success(result);
	}
	
	@PostMapping("/deleteAdgroups/{profileid}/{campaignType}")
	public Result<?> deleteAdgroupsAction(@PathVariable String profileid,@PathVariable String campaignType,@RequestBody List<String> idlist){
		if(idlist==null)return Result.failed();
		UserInfo user = UserInfoContext.get();
		List<Object> result=new ArrayList<Object>();
		if(campaignType.toLowerCase().equals("sp")) {
				JSONObject param = new JSONObject();
				JSONObject adGroupIdFilter = new JSONObject();
				adGroupIdFilter.put("include", idlist);
				param.put("adGroupIdFilter", adGroupIdFilter);
				param.put("includeExtendedDataFields", true);
				List<AmzAdvAdgroups> list = amzAdvAdGroupService.amzDeleteAdGroups(user, new BigInteger(profileid),param);
				result.addAll(list);
		}
		if(campaignType.toLowerCase().equals("sb")) {
			JSONObject param = new JSONObject();
			JSONObject adGroupIdFilter = new JSONObject();
			adGroupIdFilter.put("include", idlist);
			param.put("adGroupIdFilter", adGroupIdFilter);
			param.put("includeExtendedDataFields", true);
			List<AmzAdvAdgroupsHsa> list = amzAdvAdgroupsHsaService.amzDeleteAdGroups(user,new BigInteger(profileid),param);
			result.addAll(list);
		}
		if(campaignType.toLowerCase().equals("sd")) {
			for(String id:idlist) {
				AmzAdvAdgroupsSD sd=amzAdvAdgroupsSDService.archiveAdGroup(user, new BigInteger(profileid), id);
				if(sd!=null) {
					result.add(sd);
				}
			}
		}
		return Result.success(result);
	}

	@PostMapping("/getAdgroupExtList/{profileid}/{campaignType}/ignoreRepeat")
	public Result<?> getAdgroupExtListAction(@PathVariable String profileid,@PathVariable String campaignType,@RequestBody List<String> idlist){
		if(idlist==null)return Result.failed();
		List<Object> result=new ArrayList<Object>();
		if(campaignType.toLowerCase().equals("sp")) {
				JSONObject param = new JSONObject();
				JSONObject adGroupIdFilter = new JSONObject();
				adGroupIdFilter.put("include", idlist);
				param.put("adGroupIdFilter", adGroupIdFilter);
				param.put("includeExtendedDataFields", true);
				
				List<AmzAdvAdgroups> list = amzAdvAdGroupService.amzListAdGroups(new BigInteger(profileid),param);
				result.addAll(list);
		}
		if(campaignType.toLowerCase().equals("sb")) {
			JSONObject param = new JSONObject();
			JSONObject adGroupIdFilter = new JSONObject();
			adGroupIdFilter.put("include", idlist);
			param.put("adGroupIdFilter", adGroupIdFilter);
			param.put("includeExtendedDataFields", true);
			List<AmzAdvAdgroupsHsa> list = amzAdvAdgroupsHsaService.amzListAdGroups(new BigInteger(profileid),param);
			result.addAll(list);
		}
		if(campaignType.toLowerCase().equals("sd")) {
			UserInfo user = UserInfoContext.get();
			for(String item:idlist) {
				AmzAdvAdgroupsSD adgroup = amzAdvAdgroupsSDService.amzGetSDAdGroupExt(user, new BigInteger(profileid), item);
				result.add(adgroup);
			}
		}
		return Result.success(result);
	}

	
	@ApiOperation("查询广告组汇总")
	@PostMapping("/getSumAdGroup")
	public Result<Object> getSumAdGroupAction(@ApiParam("查询广告组") @RequestBody QueryForList query){
		UserInfo user = UserInfoContext.get();
		Map<String,Object> map = AdvertController.amzAdvParameterMap(query); 
		map.put("shopid", user.getCompanyid());
		List<Map<String, Object>> param =amzAdvAdGroupService.getSumAdGroup(map);
		return  Result.success(AdvertController.SumAdvertDate(param));
	}

}
