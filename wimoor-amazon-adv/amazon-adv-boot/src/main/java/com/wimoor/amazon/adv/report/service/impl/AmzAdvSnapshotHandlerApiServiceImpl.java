package com.wimoor.amazon.adv.report.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.common.pojo.AmzAdvApiPages;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.service.IAmzAdvApiPagesService;
import com.wimoor.amazon.adv.report.service.IAmzAdvSnapshotHandlerApiService;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvKeywordsHsa;
import com.wimoor.amazon.adv.sb.service.IAmzAdvAdgroupsHsaService;
import com.wimoor.amazon.adv.sb.service.IAmzAdvAdsHsaService;
import com.wimoor.amazon.adv.sb.service.IAmzAdvCampaignHsaService;
import com.wimoor.amazon.adv.sb.service.IAmzAdvKeywordsHsaService;
import com.wimoor.amazon.adv.sb.service.IAmzAdvProductTargeHsaService;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductTargeNegativaSD;
import com.wimoor.amazon.adv.sd.service.IAmzAdvProductTargeNegativaSDService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampProductTargeNegativa;
import com.wimoor.amazon.adv.sp.service.IAmzAdvBudgetRulesService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvCampProductTargeNegativaService;
import com.wimoor.common.GeneralUtil;
@Service("amzAdvSnapshotHandlerApiService")
public class AmzAdvSnapshotHandlerApiServiceImpl implements IAmzAdvSnapshotHandlerApiService {
    @Autowired
    @Lazy
    IAmzAdvCampaignHsaService amzAdvCampaignHsaService;
    @Autowired
    @Lazy
    IAmzAdvAdsHsaService amzAdvAdsHsaService;
    @Resource
    IAmzAdvApiPagesService amzAdvApiPagesService;
    @Autowired
    @Lazy
    IAmzAdvKeywordsHsaService amzAdvKeywordsHsaService;
    @Resource
    IAmzAdvProductTargeHsaService amzAdvProductTargeHsaService;
    @Resource
    IAmzAdvAdgroupsHsaService amzAdvAdgroupsHsaService;

    @Resource
    IAmzAdvCampProductTargeNegativaService amzAdvCampProductTargeNegativaService;
    @Autowired
    @Lazy
    IAmzAdvProductTargeNegativaSDService amzAdvProductTargeNegativaSDService;
    @Resource
    IAmzAdvBudgetRulesService iAmzAdvBudgetRulesService;
    
	public void requestHsaCampaign(AmzAdvProfile profile) {
		String nextToken=null;
		String api="/sb/v4/campaigns/list";
		try {
			AmzAdvApiPages page = amzAdvApiPagesService.getPage(profile.getId(),api);
			JSONObject campaignsParam=new JSONObject();
			campaignsParam.put("nextToken", null);
			if(page!=null&&page.getNexttoken()==null&&page.getOpttime()!=null&&GeneralUtil.distanceOfHour(page.getOpttime(), new Date())<20) {
				return;
			}
			else if(page==null||page.getNexttoken()==null) {
				campaignsParam.put("nextToken", null);
				amzAdvCampaignHsaService.amzListSBCampaigns(profile,campaignsParam);	
				if(campaignsParam.get("nextToken")!=null) {
					amzAdvApiPagesService.savePage(profile.getId(),api,campaignsParam.get("nextToken").toString());
				}else {
					amzAdvApiPagesService.savePage(profile.getId(),api,null);
				}
			}else   {
				nextToken=page.getNexttoken();
				campaignsParam.put("nextToken",page.getNexttoken());
				amzAdvCampaignHsaService.amzListSBCampaigns(profile,campaignsParam);	
				if(campaignsParam.get("nextToken")!=null) {
					amzAdvApiPagesService.savePage(profile.getId(),api,campaignsParam.get("nextToken").toString());
				}else {
					amzAdvApiPagesService.savePage(profile.getId(),api,null);
				}
			} 
			
		}catch (Exception e) { 
			e.printStackTrace();
			//无法获取所在站点权限
			nextToken=null;
			amzAdvApiPagesService.savePage(profile.getId(),api,nextToken,e.getMessage());
		}
	}
	
	
	public void requestHsaKeyword(AmzAdvProfile profile) {
		int pageindex=0;
		try {
			AmzAdvApiPages page = amzAdvApiPagesService.getPage(profile.getId(),"/sb/keywords");
			HashMap<String,Object> campaignsParam=new HashMap<String,Object>();
			campaignsParam.put("creativeType", "video");
			if(page!=null&&(page.getPages()==null||page.getPages()==-1)&&page.getOpttime()!=null&&GeneralUtil.distanceOfHour(page.getOpttime(), new Date())<20) {
				return ;
			}else if(page==null||page.getPages()==null) {
				campaignsParam.put("count", 10);
				campaignsParam.put("startIndex",pageindex);
				List<AmzAdvKeywordsHsa> list = amzAdvKeywordsHsaService.amzListSBKeywords(profile,campaignsParam);
				if(list==null||list.size()<10) {
					amzAdvApiPagesService.savePage(profile.getId(),"/sb/keywords",-1);
				}else {
					amzAdvApiPagesService.savePage(profile.getId(),"/sb/keywords",pageindex+1);
				}
			}else if(page.getPages()!=null&&page.getPages()!=-1) {
				pageindex=page.getPages();
				campaignsParam.put("count", 10);
				campaignsParam.put("startIndex",pageindex);
				List<AmzAdvKeywordsHsa> list = amzAdvKeywordsHsaService.amzListSBKeywords(profile,campaignsParam);
				if(list==null||list.size()<10) {
					amzAdvApiPagesService.savePage(profile.getId(),"/sb/keywords",-1);
				}else {
					amzAdvApiPagesService.savePage(profile.getId(),"/sb/keywords",pageindex+1);
				}
			} 
			
		}catch (Exception e) {
			e.printStackTrace();
			//无法获取所在站点权限
			amzAdvApiPagesService.savePage(profile.getId(),"/sb/keywords",pageindex,e.getMessage());
		}
	}
	public void requestHsaTarget(AmzAdvProfile profile) {
		String nextToken=null;
		String api="/sb/targets/list";
		try {
			AmzAdvApiPages page = amzAdvApiPagesService.getPage(profile.getId(),api);
			JSONObject campaignsParam=new JSONObject();
			campaignsParam.put("maxResults", 5000);
			campaignsParam.put("nextToken", null);
			if(page!=null&&page.getNexttoken()==null&&page.getOpttime()!=null&&GeneralUtil.distanceOfHour(page.getOpttime(), new Date())<20) {
				return ;
			}
			else if(page==null||page.getNexttoken()==null) {
				campaignsParam.put("nextToken", null);
				amzAdvProductTargeHsaService.amzGetTargets(profile,campaignsParam);	
				if(campaignsParam.get("nextToken")!=null) {
					amzAdvApiPagesService.savePage(profile.getId(),api,campaignsParam.get("nextToken").toString());
				}else {
					amzAdvApiPagesService.savePage(profile.getId(),api,null);
				}
			}else   {
				nextToken=page.getNexttoken();
				campaignsParam.put("nextToken",page.getNexttoken());
				amzAdvProductTargeHsaService.amzGetTargets(profile,campaignsParam);	
				if(campaignsParam.get("nextToken")!=null) {
					amzAdvApiPagesService.savePage(profile.getId(),api,campaignsParam.get("nextToken").toString());
				}else {
					amzAdvApiPagesService.savePage(profile.getId(),api,null);
				}
			} 
			
		}catch (Exception e) {
			e.printStackTrace();
			//无法获取所在站点权限
			nextToken=null;
			amzAdvApiPagesService.savePage(profile.getId(),api,nextToken,e.getMessage());
		}
	}
	 
	public void requestHsaAdgroups(AmzAdvProfile profile) {
		String nextToken=null;
		String api="/sb/v4/adGroups/list";
		try {
			AmzAdvApiPages page = amzAdvApiPagesService.getPage(profile.getId(),api);
			JSONObject campaignsParam=new JSONObject();
			campaignsParam.put("nextToken", null);
			if(page!=null&&page.getNexttoken()==null&&page.getOpttime()!=null&&GeneralUtil.distanceOfHour(page.getOpttime(), new Date())<20) {
				return ;
			}
			else if(page==null||page.getNexttoken()==null) {
				campaignsParam.put("nextToken", null);
				amzAdvAdgroupsHsaService.amzGetSBAdGroupList(profile,campaignsParam);	
				if(campaignsParam.get("nextToken")!=null) {
					amzAdvApiPagesService.savePage(profile.getId(),api,campaignsParam.get("nextToken").toString());
				}else {
					amzAdvApiPagesService.savePage(profile.getId(),api,null);
				}
			}else   {
				nextToken=page.getNexttoken();
				campaignsParam.put("nextToken",page.getNexttoken());
				amzAdvCampaignHsaService.amzListSBCampaigns(profile,campaignsParam);	
				if(campaignsParam.get("nextToken")!=null) {
					amzAdvApiPagesService.savePage(profile.getId(),api,campaignsParam.get("nextToken").toString());
				}else {
					amzAdvApiPagesService.savePage(profile.getId(),api,null);
				}
			} 
			
		}catch (Exception e) {
			e.printStackTrace();
			//无法获取所在站点权限
			nextToken=null;
			amzAdvApiPagesService.savePage(profile.getId(),api,nextToken,e.getMessage());
		}
		 
	}
	
	public void requestSPCampProductTargeNegative(AmzAdvProfile profile){
		AmzAdvApiPages page = amzAdvApiPagesService.getPage(profile.getId(),"/sp/campaignNegativeTargets/list");
		String nextToken=null;
		try {
			if(page!=null&&(page.getPages()==null||page.getPages()==-1)&&page.getOpttime()!=null&&GeneralUtil.distanceOfHour(page.getOpttime(), new Date())<20) {
				return ;
			}
			else if(page==null||page.getPages()==null) {
				JSONObject param = new JSONObject(); 
				param.put("includeExtendedDataFields", false);
			    List<AmzAdvCampProductTargeNegativa> list = amzAdvCampProductTargeNegativaService.amzListNegativeTargetingClauses(null, profile.getId(), param);
			    nextToken = param.getString("nextToken");
			    if(list==null||nextToken==null) {
					amzAdvApiPagesService.savePage(profile.getId(),"/sp/campaignNegativeTargets/list",-1);
				}else {
					amzAdvApiPagesService.savePage(profile.getId(),"/sp/campaignNegativeTargets/list",nextToken);
				}
			}else if(page.getPages()!=null&&page.getPages()!=-1) {
				JSONObject param = new JSONObject();
				nextToken=page.getNexttoken();
				param.put("nextToken", nextToken);
			    List<AmzAdvCampProductTargeNegativa> list = amzAdvCampProductTargeNegativaService.amzListNegativeTargetingClauses(null, profile.getId(), param);
			    nextToken = param.getString("nextToken");
			    if(list==null||nextToken==null) {
					amzAdvApiPagesService.savePage(profile.getId(),"/sp/campaignNegativeTargets/list",-1);
				}else {
					amzAdvApiPagesService.savePage(profile.getId(),"/sp/campaignNegativeTargets/list",nextToken);
				}
			} 
		}catch(Exception e) {
			e.printStackTrace();
			//无法获取所在站点权限
			amzAdvApiPagesService.savePage(profile.getId(),"/sb/adGroups",nextToken);
		}
	}
	
	public void requestSDProductTargeNegative(AmzAdvProfile profile){
		int pageindex=0;
		try {
			AmzAdvApiPages page = amzAdvApiPagesService.getPage(profile.getId(),"/sd/negativeTargets");
			HashMap<String,Object> param=new HashMap<String,Object>();
			if(page!=null&&(page.getPages()==null||page.getPages()==-1)&&GeneralUtil.distanceOfHour(page.getOpttime(), new Date())<20) {
				return ;
			}
			else if(page==null||page.getPages()==null) {
				param.put("count", 10);
				param.put("startIndex",pageindex);
				 List<AmzAdvProductTargeNegativaSD> list = amzAdvProductTargeNegativaSDService.amzListNegativeTargetingClauses(null,profile.getId(),param);
				if(list==null||list.size()<10) {
					amzAdvApiPagesService.savePage(profile.getId(),"/sd/negativeTargets",-1);
				}else {
					amzAdvApiPagesService.savePage(profile.getId(),"/sd/negativeTargets",pageindex+1);
				}
			}else if(page.getPages()!=null&&page.getPages()!=-1) {
				pageindex=page.getPages();
				param.put("count", 10);
				param.put("startIndex",pageindex);
				List<AmzAdvProductTargeNegativaSD> list = amzAdvProductTargeNegativaSDService.amzListNegativeTargetingClauses(null,profile.getId(),param);
				if(list==null||list.size()<10) {
					amzAdvApiPagesService.savePage(profile.getId(),"/sd/negativeTargets",-1);
				}else {
					amzAdvApiPagesService.savePage(profile.getId(),"/sd/negativeTargets",pageindex+1);
				}
			} 
			
		}catch (Exception e) {
			e.printStackTrace();
			//无法获取所在站点权限
			amzAdvApiPagesService.savePage(profile.getId(),"/sd/negativeTargets",pageindex,e.getMessage());
		}
	}


	@Override
	public void requestHsaAds(AmzAdvProfile profile) {
		// TODO Auto-generated method stub
		String nextToken=null;
		String api="/sb/v4/ads/list";
		try {
			AmzAdvApiPages page = amzAdvApiPagesService.getPage(profile.getId(),api);
			JSONObject param=new JSONObject();
			param.put("nextToken", null);
			if(page!=null&&page.getNexttoken()==null&&page.getOpttime()!=null&&GeneralUtil.distanceOfHour(page.getOpttime(), new Date())<20) {
				return;
			}
			else if(page==null||page.getNexttoken()==null) {
				param.put("nextToken", null);
				amzAdvAdsHsaService.amzGetAdsList(profile,param);	
				if(param.get("nextToken")!=null) {
					amzAdvApiPagesService.savePage(profile.getId(),api,param.get("nextToken").toString());
				}else {
					amzAdvApiPagesService.savePage(profile.getId(),api,null);
				}
			}else   {
				nextToken=page.getNexttoken();
				param.put("nextToken",page.getNexttoken());
				amzAdvAdsHsaService.amzGetAdsList(profile,param);	
				if(param.get("nextToken")!=null) {
					amzAdvApiPagesService.savePage(profile.getId(),api,param.get("nextToken").toString());
				}else {
					amzAdvApiPagesService.savePage(profile.getId(),api,null);
				}
			} 
			
		}catch (Exception e) { 
			e.printStackTrace();
			//无法获取所在站点权限
			nextToken=null;
			amzAdvApiPagesService.savePage(profile.getId(),api,nextToken,e.getMessage());
		}
	}


	@Override
	public void requestSPCampBudgetRule(AmzAdvProfile profile) {
		// TODO Auto-generated method stub
				String nextToken=null;
				String api="/sp/budgetRules";
				try {
					AmzAdvApiPages page = amzAdvApiPagesService.getPage(profile.getId(),api);
					JSONObject param=new JSONObject();
					param.put("nextToken", null);
					if(page!=null&&page.getNexttoken()==null&&page.getOpttime()!=null&&GeneralUtil.distanceOfHour(page.getOpttime(), new Date())<20) {
						return;
					}
					else if(page==null||page.getNexttoken()==null) {
						param.put("nextToken", null);
						iAmzAdvBudgetRulesService.amzGetBudgetRules(profile,param);	
						if(param.get("nextToken")!=null) {
							amzAdvApiPagesService.savePage(profile.getId(),api,param.get("nextToken").toString());
						}else {
							amzAdvApiPagesService.savePage(profile.getId(),api,null);
						}
					}else   {
						nextToken=page.getNexttoken();
						param.put("nextToken",page.getNexttoken());
						iAmzAdvBudgetRulesService.amzGetBudgetRules(profile,param);	
						if(param.get("nextToken")!=null) {
							amzAdvApiPagesService.savePage(profile.getId(),api,param.get("nextToken").toString());
						}else {
							amzAdvApiPagesService.savePage(profile.getId(),api,null);
						}
					} 
					
				}catch (Exception e) { 
					e.printStackTrace();
					//无法获取所在站点权限
					nextToken=null;
					amzAdvApiPagesService.savePage(profile.getId(),api,nextToken,e.getMessage());
				}
	}
}
