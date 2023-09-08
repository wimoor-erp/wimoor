package com.wimoor.amazon.adv.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.controller.pojo.dto.QueryForList;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvAdgroupsSD;
import com.wimoor.amazon.adv.sd.service.IAmzAdvAdgroupsSDService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvAdgroups;
import com.wimoor.amazon.adv.sp.service.IAmzAdvAdGroupService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
 
 
@Api(tags = "广告组接口")
@RestController 
@RequestMapping("/api/v1/advAdgroupManager") 
public class AdvertAdgroupManagerController {

	 
	@Resource
	IAmzAdvAdGroupService amzAdvAdGroupService;
	@Resource 
	IAmzAdvAdgroupsSDService amzAdvAdgroupsSDService;
	@RequestMapping("/viewadvgroup")
	public String viewadvgroupAction(HttpServletRequest request, Model model){
		return "pages/advertising/advert_manager_advgroup";
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
	
    @ApiOperation("查询广告组更新SD")
 	@PostMapping("/sd/updateAdGroupList")
	public String updateSDAdGroupListAction(List<AmzAdvAdgroupsSD> adGroupArray) {
	 	   UserInfo userinfo = UserInfoContext.get();
		   Map<BigInteger, List<AmzAdvAdgroupsSD>> map = new HashMap<BigInteger, List<AmzAdvAdgroupsSD>>();
		   for (int i = 0; i < adGroupArray.size(); i++) {
			   AmzAdvAdgroupsSD amzAdvAdgroups = adGroupArray.get(i);
 				if (map.get(amzAdvAdgroups.getProfileid()) == null) {
 					List<AmzAdvAdgroupsSD> list = new ArrayList<AmzAdvAdgroupsSD>();
 					list.add(amzAdvAdgroups);
 					map.put(amzAdvAdgroups.getProfileid(), list);
 				} else {
 					map.get(amzAdvAdgroups.getProfileid()).add(amzAdvAdgroups);
 				}
 			}
			for (BigInteger key : map.keySet()) {
				if(amzAdvAdgroupsSDService.amzUpdateAdGroups(userinfo, key, map.get(key)) == null) {
					throw new BaseException("亚马逊连接异常，修改失败！");
				}
			}
		return "SUCCESS";
	}
    
    @ApiOperation("查询广告组更新SP")
   	@PostMapping("/sp/updateAdGroupList")
  	public String updateSPAdGroupListAction( List<AmzAdvAdgroups> adGroupArray) {
  	 	UserInfo userinfo = UserInfoContext.get();
  			Map<BigInteger, List<AmzAdvAdgroups>> map = new HashMap<BigInteger, List<AmzAdvAdgroups>>();
  			for (int i = 0; i < adGroupArray.size(); i++) {
  				AmzAdvAdgroups amzAdvAdgroups = adGroupArray.get(i);
  				if (map.get(amzAdvAdgroups.getProfileid()) == null) {
  					List<AmzAdvAdgroups> list = new ArrayList<AmzAdvAdgroups>();
  					list.add(amzAdvAdgroups);
  					map.put(amzAdvAdgroups.getProfileid(), list);
  				} else {
  					map.get(amzAdvAdgroups.getProfileid()).add(amzAdvAdgroups);
  				}
  			}
  			for (BigInteger key : map.keySet()) {
  				if(amzAdvAdGroupService.amzUpdateAdGroups(userinfo, key, map.get(key)) == null) {
  					throw new BaseException("亚马逊连接异常，修改失败！");
  				}
  			}
  		
  		return "SUCCESS";
  	}
    
	@ResponseBody
	@RequestMapping("/updateAdGroupName")
	public String updateAdGroupNameAction(HttpServletRequest request, Model model) {
		UserInfo user = UserInfoContext.get();
		String json = request.getParameter("jsonstr");
		JSONObject adGroup = GeneralUtil.getJsonObject(json);
		String name = adGroup.getString("name");
		String state = adGroup.getString("status");
		BigInteger campaignid = adGroup.getBigInteger("campaignId");
		BigInteger adGroupid = adGroup.getBigInteger("id");
		BigInteger profileId = adGroup.getBigInteger("profileid");
		BigDecimal defaultbid = adGroup.getBigDecimal("defaultBid");
		String campaignType=adGroup.getString("campaignType");
		if(campaignType!=null&&"sd".equals(campaignType.toLowerCase())) {
			Example example = new Example(AmzAdvAdgroupsSD.class);
			Criteria criter = example.createCriteria();
			criter.andEqualTo("name", name);
			criter.andEqualTo("profileid", profileId);
			criter.andEqualTo("campaignid", campaignid);
			List<AmzAdvAdgroupsSD> oldamzAdvAdgroups = amzAdvAdgroupsSDService.selectByExample(example);
			if(oldamzAdvAdgroups != null && oldamzAdvAdgroups.size() > 0) {
				throw new BaseException("在该站点的广告活动下,广告活动名称已经存在！");
			}
			AmzAdvAdgroupsSD amzAdvAdgroups = new AmzAdvAdgroupsSD();
			amzAdvAdgroups.setCampaignid(campaignid);
			amzAdvAdgroups.setAdgroupid(adGroupid);
			amzAdvAdgroups.setName(name);
			amzAdvAdgroups.setState(state);
			amzAdvAdgroups.setDefaultbid(defaultbid);
			amzAdvAdgroups.setOpttime(new Date());
			amzAdvAdgroups.setProfileid(profileId);
			List<AmzAdvAdgroupsSD> list = new ArrayList<AmzAdvAdgroupsSD>();
			list.add(amzAdvAdgroups);
			if(amzAdvAdgroupsSDService.amzUpdateAdGroups(user, profileId, list) == null){
				throw new BaseException("亚马逊连接异常，修改失败！");
			}
		}else {
			Example example = new Example(AmzAdvAdgroups.class);
			Criteria criter = example.createCriteria();
			criter.andEqualTo("name", name);
			criter.andEqualTo("profileid", profileId);
			criter.andEqualTo("campaignid", campaignid);
			List<AmzAdvAdgroups> oldamzAdvAdgroups = amzAdvAdGroupService.selectByExample(example);
			if(oldamzAdvAdgroups != null && oldamzAdvAdgroups.size() > 0) {
				throw new BaseException("在该站点的广告活动下,广告活动名称已经存在！");
			}
			AmzAdvAdgroups amzAdvAdgroups = new AmzAdvAdgroups();
			amzAdvAdgroups.setCampaignid(campaignid);
			amzAdvAdgroups.setAdgroupid(adGroupid);
			amzAdvAdgroups.setName(name);
			amzAdvAdgroups.setState(state);
			amzAdvAdgroups.setDefaultbid(defaultbid);
			amzAdvAdgroups.setOpttime(new Date());
			amzAdvAdgroups.setProfileid(profileId);
			List<AmzAdvAdgroups> list = new ArrayList<AmzAdvAdgroups>();
			list.add(amzAdvAdgroups);
			if(amzAdvAdGroupService.amzUpdateAdGroups(user, profileId, list) == null){
				throw new BaseException("亚马逊连接异常，修改失败！");
			}
		}
		
		
		return "SUCCESS";
	}
	
	@ResponseBody
	@RequestMapping("/archiveAdGrou")
	public String archiveAdGroupAction(HttpServletRequest request, Model model){
		UserInfo user = UserInfoContext.get();
		String profileId = request.getParameter("profileid");
		String adgroupId = request.getParameter("id");
		String campaignType = request.getParameter("campaignType");
		if(campaignType!=null&&"sd".equals(campaignType.toLowerCase())){
			AmzAdvAdgroupsSD amzAdvAdgroups = null;
			amzAdvAdgroups = amzAdvAdgroupsSDService.archiveAdGroup(user, new BigInteger(profileId), adgroupId);
			if(amzAdvAdgroups == null) {
				return "ERROR";
			}
		}else {
			AmzAdvAdgroups amzAdvAdgroups = null;
			amzAdvAdgroups = amzAdvAdGroupService.archiveAdGroup(user, new BigInteger(profileId), adgroupId);
			if(amzAdvAdgroups == null) {
				return "ERROR";
			}
		}
		
		return "SUCCESS";
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
	
 
	@GetMapping("/getAdGroupSuggestBid")
	public Result<Map<String,Object>> getAdGroupSuggestBidAction(String profileid,String adgroupid,String campaignid){
		UserInfo user = UserInfoContext.get();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("profileid",profileid);
		map.put("id",adgroupid);
		map.put("campaignId",campaignid);
		return Result.success(amzAdvAdGroupService.catchAdGroupSuggestBid(user, map));
	}
	
	@ResponseBody
	@RequestMapping("/getAdgroupExtList")
	public Object getAdgroupExtListAction(HttpServletRequest request, Model model){
		UserInfo user = UserInfoContext.get();
		String rows = request.getParameter("rows");
		if(StrUtil.isEmpty(rows))return null;
		JSONArray rowsjson = GeneralUtil.getJsonArray(rows);
	    Map<String,String> spgroupmap=new HashMap<String,String>();
	    Map<String,String> sdgroupmap=new HashMap<String,String>();
		for(int i=0;i<rowsjson.size();i++) {
			  JSONObject item = rowsjson.getJSONObject(i);
			String profileid =item.getString("profileid");
			String campaignType=item.getString("campaignType");
			if(campaignType!=null&&"sd".equals(campaignType.toLowerCase())) {
				String ids=sdgroupmap.get(profileid);
				if(!StrUtil.isEmpty(ids)) {
					ids=ids+","+item.getString("id");
				}else {
					ids=item.getString("id");
				}
				sdgroupmap.put(profileid, ids);
			}else {
				String ids=spgroupmap.get(profileid);
				if(!StrUtil.isEmpty(ids)) {
					ids=ids+","+item.getString("id");
				}else {
					ids=item.getString("id");
				}
				spgroupmap.put(profileid, ids);
			}
		}
		List<Object> result=new ArrayList<Object>();
		if(spgroupmap.size()>0) {
			for(Entry<String, String> item:spgroupmap.entrySet()) {
				String profileid = item.getKey();
				String ids=item.getValue();
				Map<String,Object> param=new HashMap<String,Object>();
				param.put("adGroupIdFilter",ids);
				List<AmzAdvAdgroups> list = amzAdvAdGroupService.amzListAdGroupsExt(user,new BigInteger(profileid),param);
				result.addAll(list);
			}
		}
		if(sdgroupmap.size()>0) {
			for(Entry<String, String> item:sdgroupmap.entrySet()) {
				String profileid = item.getKey();
				String ids=item.getValue();
				Map<String,Object> param=new HashMap<String,Object>();
				param.put("adGroupIdFilter",ids);
				List<AmzAdvAdgroupsSD> list = amzAdvAdgroupsSDService.amzListAdGroupsExt(user,new BigInteger(profileid),param);
				result.addAll(list);
			}
		}
		return result;
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
