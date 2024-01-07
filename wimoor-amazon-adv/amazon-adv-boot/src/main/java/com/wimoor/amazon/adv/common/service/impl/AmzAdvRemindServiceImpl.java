package com.wimoor.amazon.adv.common.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.dao.AmzAdvProfileMapper;
import com.wimoor.amazon.adv.common.dao.AmzAdvRemindMapper;
import com.wimoor.amazon.adv.common.dao.MarketplaceMapper;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.AmzAdvRemind;
import com.wimoor.amazon.adv.common.pojo.Marketplace;
import com.wimoor.amazon.adv.common.service.IAmazonReportAdvSummaryService;
import com.wimoor.amazon.adv.common.service.IAmzAdvRemindService;
import com.wimoor.amazon.adv.report.dao.AmzAdvRequestMapper;
import com.wimoor.amazon.adv.report.pojo.AmzAdvRequest;
import com.wimoor.amazon.adv.report.service.IAmzAdvReportService;
import com.wimoor.amazon.adv.report.service.impl.AmzAdvReportHandlerServiceImpl.AdvResponseRecordType;
import com.wimoor.amazon.adv.sb.dao.AmzAdvCampaignsHsaMapper;
import com.wimoor.amazon.adv.sd.dao.AmzAdvAdgroupsSDMapper;
import com.wimoor.amazon.adv.sd.dao.AmzAdvCampaignsSDMapper;
import com.wimoor.amazon.adv.sd.dao.AmzAdvProductadsSDMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvAdgroupsMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvCampaignsMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvKeywordsMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvProductTargeMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvProductadsMapper;
import com.wimoor.amazon.adv.sp.service.impl.AmzAdvCampaignServiceImpl.CampaignType;
import com.wimoor.amazon.adv.task.service.IAdvSchedulePlanService;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.user.UserInfo;
 
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
@Service("amzAdvRemindService")
public class AmzAdvRemindServiceImpl extends BaseService<AmzAdvRemind> implements IAmzAdvRemindService{
	@Resource
	AmzAdvRemindMapper amzAdvRemindMapper; 
	@Resource
	AmzAdvProfileMapper amzAdvProfileMapper;
	@Resource
	AmzAdvCampaignsMapper amzAdvCampaignsMapper;
	@Resource
	AmzAdvCampaignsHsaMapper amzAdvCampaignsHsaMapper;
	@Resource
	AmzAdvCampaignsSDMapper amzAdvCampaignsSDMapper;
	@Resource
	AmzAdvAdgroupsMapper amzAdvAdgroupsMapper;
	@Resource
	AmzAdvAdgroupsSDMapper amzAdvAdgroupsSDMapper;
	@Resource
	AmzAdvKeywordsMapper amzAdvKeywordsMapper;
	@Resource
	AmzAdvProductadsMapper amzAdvProductadsMapper;
	@Resource
	AmzAdvProductadsSDMapper amzAdvProductadsSDMapper;
	@Resource
	AmzAdvProductTargeMapper amzAdvProductTargeMapper;
	@Resource
	MarketplaceMapper marketplaceMapper;
	//@Resource
	//private ProductInOptMapper productInOptMapper;
	@Resource
	IAdvSchedulePlanService advSchedulePlanService;
	@Resource
	IAmzAdvReportService amzAdvReportService;
    @Resource
    AmzAdvRequestMapper amzAdvRequestMapper;
    @Resource
    IAmazonReportAdvSummaryService amazonReportAdvSummaryService;
 
	@Override
	public int save(AmzAdvRemind entity) throws BizException {
		// TODO Auto-generated method stub
		if(entity.getIswarn()==null) {
			entity.setIswarn(Boolean.FALSE);
		}
		AmzAdvRemind old = this.selectByKey(entity);
		if(old==null) {
			entity.setCreatedate(entity.getOpttime());
			entity.setCreator(entity.getOperator());
			return super.save(entity);
		}else {
			entity.setCreatedate(old.getCreatedate());
			entity.setCreator(old.getOperator());
			return super.updateAll(entity);
		}
	}
	
	public final static Map<String, String> mapTimeZone = new HashMap<String, String>();  
	static {  
		mapTimeZone.put("UK", "Europe/London");
		mapTimeZone.put("FR", "Europe/Paris");  
		mapTimeZone.put("DE", "Europe/Paris");  
		mapTimeZone.put("ES", "Europe/Paris");  
		mapTimeZone.put("IT", "Europe/Paris");
		mapTimeZone.put("JP", "Asia/Tokyo");   
	//	mapTimeZone.put("IN", "");//还没有加入广告
		//mapTimeZone.put("AU", ""); //还没有加入广告
		mapTimeZone.put("CA", "America/Los_Angeles"); 
		mapTimeZone.put("US", "America/Los_Angeles");
	}
	
	public final static List<String> listType = new ArrayList<String>();  
	static {  
		listType.add("campaign-sp");
		listType.add("campaign-hsa");
		listType.add("adGroup-sp");
		listType.add("keyword-sp");
		listType.add("keyword-hsa");
		listType.add("productAd-sp");
		listType.add("targets-sp");
	}

	public PageList<Map<String,Object>> selectByCondition(Map<String,Object> param,PageBounds pagebounds) {
		PageList<Map<String,Object>> list = amzAdvRemindMapper.selectByCondition(param,pagebounds);
		for(Map<String, Object> item:list) {
			item.put("currencycode", GeneralUtil.formatCurrency(item.get("currency").toString()));
		}
		return list;
	}
	
	public List<Map<String, Object>> getRemindByMarketplace(Map<String, Object> map){
		Marketplace marketplace = (Marketplace) map.get("marketplace");
		Example example = new Example(AmzAdvProfile.class);
		Criteria crit = example.createCriteria();
		crit.andEqualTo("marketplaceid", marketplace.getMarketplaceid());
		List<AmzAdvProfile> list = amzAdvProfileMapper.selectByExample(example);
		if(list != null && list.size() > 0) {
			List<BigInteger> listparam = new ArrayList<BigInteger>();
			for(int i = 0; i < list.size(); i++) {
				AmzAdvProfile amzAdvProfile = list.get(i);
				listparam.add(amzAdvProfile.getId());
			}
			map.put("profileidList", listparam);
			List<Map<String, Object>> remindList = amzAdvRemindMapper.selectByprofileid(map);
			return remindList;
		}
		return null;
	}
	
	public String getserchStr(String serch,String campaignType) {
		if (CampaignType.sp.equals(campaignType)) {
			if("clicks".equals(serch)) {
				serch = "ifnull(sum(clicks),0) clicks";
			}
			else if("cost".equals(serch)) {
				serch = "ifnull(sum(cost),0) cost";
			}
			else if("acos".equals(serch)) {
				serch = "ifnull((sum(cost) / sum(attributedSales7d)),0) acos";
			}
			else if("cr".equals(serch)) {
				serch = "ifnull((sum(attributedUnitsOrdered7d) / sum(clicks)),0) cr";
			}
			else if("ctr".equals(serch)) {
				serch = "ifnull((sum(clicks) / sum(impressions)),0) ctr";
			}
			else if("impressions".equals(serch)) {
				serch = "ifnull(sum(impressions),0) impressions";
			}
		}else {
			if("clicks".equals(serch)) {
				serch = "ifnull(sum(clicks),0) clicks";
			}
			else if("cost".equals(serch)) {
				serch = "ifnull(sum(cost),0) cost";
			}
			else if("acos".equals(serch)) {
				serch = "ifnull((sum(cost) / sum(attributedSales14d)),0) acos";
			}
			else if("cr".equals(serch)) {
				serch = "ifnull((sum(attributedConversions14d) + sum(attributedConversions14dSameSKU) / sum(clicks)),0) cr";
			}
			else if("ctr".equals(serch)) {
				serch = "ifnull((sum(clicks) / sum(impressions)),0) ctr";
			}
			else if("impressions".equals(serch)) {
				serch = "ifnull(sum(impressions),0) impressions";
			}
		}
		return serch;
	}
	
	public Map<String, Object> getAmount(Map<String, Object> param, String recordType, String campaignType) {
		Map<String, Object> AmountMap = null;
		if (AdvResponseRecordType.campaign.equals(recordType)) {
			if (CampaignType.sp.equals(campaignType)) {
				AmountMap = amzAdvCampaignsMapper.getCampaignByRemind(param);
			}else if(CampaignType.sd.equals(campaignType)) {
				AmountMap = amzAdvCampaignsSDMapper.getCampaignByRemind(param);
			}else {
				AmountMap = amzAdvCampaignsHsaMapper.getCampaignByRemind(param);
			}
		} else if (AdvResponseRecordType.adGroup.equals(recordType)) {
			if(CampaignType.sp.equals(campaignType)) {
				AmountMap = amzAdvAdgroupsMapper.getAdgroupByRemind(param);
			}else if(CampaignType.sd.equals(campaignType)){
				AmountMap = amzAdvAdgroupsSDMapper.getAdgroupByRemind(param);
			}
		} else if (AdvResponseRecordType.productAd.equals(recordType)) {
			if(CampaignType.sd.equals(campaignType)) {
				AmountMap = amzAdvProductadsSDMapper.getProductAdByRemind(param);
			}else {
				AmountMap = amzAdvProductadsMapper.getProductAdByRemind(param);
			}
			
		} else if (AdvResponseRecordType.target.equals(recordType)) {
			AmountMap = amzAdvProductTargeMapper.getProductTargeByRemind(param);
		} else if (AdvResponseRecordType.keyword.equals(recordType)) {
			if (CampaignType.sp.equals(campaignType)) {
				AmountMap = amzAdvKeywordsMapper.getKeywordByRemind(param);
			}else {
				AmountMap = amzAdvKeywordsMapper.getKeywordHSAByRemind(param);
			}
		}
		return AmountMap;
	}
	
	public Map<String, Object> getLastAmount(Map<String, Object> param, String recordType, String campaignType) {
		Map<String, Object> AmountLastMap = null;
		if (AdvResponseRecordType.campaign.equals(recordType)) {
			if(CampaignType.sp.equals(campaignType)) {
				AmountLastMap = amzAdvCampaignsMapper.getCampaignByRemindlast(param);
			}else if(CampaignType.sd.equals(campaignType)) {
				AmountLastMap = amzAdvCampaignsSDMapper.getCampaignByRemindlast(param);
			}else{
				AmountLastMap = amzAdvCampaignsHsaMapper.getCampaignByRemindlast(param);
				
			}
		} else if (AdvResponseRecordType.adGroup.equals(recordType)) {
			if(CampaignType.sd.equals(campaignType)) {
				AmountLastMap = amzAdvAdgroupsSDMapper.getAdgroupByRemindlast(param);
			}else {
				AmountLastMap = amzAdvAdgroupsMapper.getAdgroupByRemindlast(param);
			}
		} else if (AdvResponseRecordType.productAd.equals(recordType)) {
			if(CampaignType.sd.equals(campaignType)) {
				AmountLastMap = amzAdvProductadsSDMapper.getProductAdByRemindlast(param);
			}else {
				AmountLastMap = amzAdvProductadsMapper.getProductAdByRemindlast(param);
			}
		} else if (AdvResponseRecordType.target.equals(recordType)) {
			AmountLastMap = amzAdvProductTargeMapper.getProductTargeByRemindlast(param);
		} else if (AdvResponseRecordType.keyword.equals(recordType)) {
			if (CampaignType.sp.equals(campaignType)) {
				AmountLastMap = amzAdvKeywordsMapper.getKeywordByRemindlast(param);
			}else {
				AmountLastMap = amzAdvKeywordsMapper.getKeywordHSAByRemindlast(param);
			}
		}
		return AmountLastMap;
	}
	
	public void getTimezone(Map<String, Object> map) {
		String market = (String) map.get("market") == null ? null : map.get("market").toString();
		Example example = new Example(Marketplace.class);
		Criteria crit = example.createCriteria();
		crit.andEqualTo("market", market);
		Marketplace marketplace = marketplaceMapper.selectOneByExample(example);
		map.put("marketplace", marketplace);
		String timezone = mapTimeZone.get(market);
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    Calendar fijiCalendar = Calendar.getInstance();
	   // Calendar fijiCalendar = Calendar.getInstance(TimeZone.getTimeZone(timezone));
	    format.setTimeZone(TimeZone.getTimeZone(timezone));
	    fijiCalendar.add(Calendar.DATE, -1);
	    String yesterDay = format.format(fijiCalendar.getTime());
	    String severDayEnd = format.format(fijiCalendar.getTime());
	    fijiCalendar.add(Calendar.DATE, -1);
	    String lastDay = format.format(fijiCalendar.getTime());
	    String lastSeverDayEnd = format.format(fijiCalendar.getTime());
	    fijiCalendar.add(Calendar.DATE, -5);
	    String severDayBegin = format.format(fijiCalendar.getTime());
	    fijiCalendar.add(Calendar.DATE, -1);
	    String lastSeverDayBegin = format.format(fijiCalendar.getTime());
	    map.put("yesterDay", yesterDay);
	    map.put("lastDay", lastDay);
	    map.put("severDayBegin", severDayBegin);
	    map.put("severDayEnd", severDayEnd);
	    map.put("lastSeverDayBegin", lastSeverDayBegin);
	    map.put("lastSeverDayEnd", lastSeverDayEnd);
	}
	
	public void refreshRemaind(Map<String, Object> map) {
		String recordType = map.get("recordType") == null ? null : map.get("recordType").toString();
		String campaignType = map.get("campaignType") == null ? null : map.get("campaignType").toString();
		getTimezone(map);
		String yesterDay = map.get("yesterDay") == null ? null : map.get("yesterDay").toString();
		String lastDay = map.get("lastDay") == null ? null : map.get("lastDay").toString();
		String severDayBegin = map.get("severDayBegin") == null ? null : map.get("severDayBegin").toString();
		String severDayEnd = map.get("severDayEnd") == null ? null : map.get("severDayEnd").toString();
		String lastSeverDayBegin = map.get("lastSeverDayBegin") == null ? null : map.get("lastSeverDayBegin").toString();
		String lastSeverDayEnd = map.get("lastSeverDayEnd") == null ? null : map.get("lastSeverDayEnd").toString();
		List<Map<String, Object>> remindList = getRemindByMarketplace(map);
		if(remindList != null && remindList.size() > 0) {
			for (Map<String, Object> param : remindList) {
				String subtrahend = (String) param.get("subtrahend");
				String quota = (String) param.get("quota");
				BigDecimal amount = (BigDecimal) param.get("amount");
				int fcondition = (Integer) param.get("fcondition");
				int cycle = (Integer) param.get("cycle");
				String serchStr = getserchStr(quota, campaignType);
				param.put("serchstr", serchStr);
				if (cycle == 1) {
					param.put("yesterDay", yesterDay);
					Map<String, Object> AmountMap = getAmount(param, recordType, campaignType);
					if(AmountMap == null) continue;
					BigDecimal DBamount = (BigDecimal) AmountMap.get(quota);
					if ("last0".equals(subtrahend)) {
						int temp = DBamount.compareTo(amount);
						if ((fcondition == 1 && temp == 1) || (fcondition == 2 && temp == -1)) {
							amzAdvRemindMapper.updateRemindIswarn(param);
						}else {
							amzAdvRemindMapper.updateRemindIsnotwarn(param);
						}
					} else if ("last1".equals(subtrahend)) {
						if(amount.compareTo(BigDecimal.ZERO)==0) {
							amount = amount.divide(new BigDecimal("100"));
						}else {
							amount = new BigDecimal("0");
						}
						param.put("lastDay", lastDay);
						Map<String, Object> AmountLastMap = getLastAmount(param, recordType, campaignType);
						if(AmountLastMap == null) continue;
						BigDecimal amountLast = (BigDecimal) AmountLastMap.get(quota);
						BigDecimal sum = amountLast.multiply(amount);
						int temp = DBamount.compareTo(sum);
						if ((fcondition == 1 && temp == 1) || (fcondition == 2 && temp == -1)) {
							amzAdvRemindMapper.updateRemindIswarn(param);
						}else {
							amzAdvRemindMapper.updateRemindIsnotwarn(param);
						}
					} else if ("last7".equals(subtrahend)) {
						if(amount.compareTo(BigDecimal.ZERO)==0) {
							amount = amount.divide(new BigDecimal("100"));
						}else {
							amount = new BigDecimal("0");
						}
						param.put("lastSeverDayBegin", lastSeverDayBegin);
						param.put("lastSeverDayEnd", lastSeverDayEnd);
						Map<String, Object> AmountLastMap = getLastAmount(param, recordType, campaignType);
						if(AmountLastMap == null) continue;
						BigDecimal amountLast = (BigDecimal) AmountLastMap.get(quota);
						BigDecimal sum = amountLast.multiply(amount);
						int temp = DBamount.compareTo(sum);
						if ((fcondition == 1 && temp == 1) || (fcondition == 2 && temp == -1)) {
							amzAdvRemindMapper.updateRemindIswarn(param);
						}else {
							amzAdvRemindMapper.updateRemindIsnotwarn(param);
						}
					}
				} else {
					param.put("severDayBegin", severDayBegin);
					param.put("severDayEnd", severDayEnd);
					Map<String, Object> AmountMap = getAmount(param, recordType, campaignType);
					if(AmountMap == null) continue;
					BigDecimal DBamount = (BigDecimal) AmountMap.get(quota);
					int temp = DBamount.compareTo(amount);
					if ((fcondition == 1 && temp == 1) || (fcondition == 2 && temp == -1)) {
						amzAdvRemindMapper.updateRemindIswarn(param);
					}else {
						amzAdvRemindMapper.updateRemindIsnotwarn(param);
					}
				}
			}
		}
	}

	public void refreshRemaind() {
		deleteRemaindForInvalid();
		advSchedulePlanService.deletePlanDateForInvalid();
		for(int i = 0; i < AmzAdvRemindServiceImpl.listType.size(); i++) {
			String[] array = AmzAdvRemindServiceImpl.listType.get(i).split("-");
			String recordType = array[0];
			String campaignType = array[1];
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("recordType", recordType);
			map.put("campaignType", campaignType);
			for(String market : AmzAdvRemindServiceImpl.mapTimeZone.keySet()) {
				map.put("market", market);
				refreshRemaind(map);
			} 
		}
		amzAdvReportService.refreshAmzadvertWarningData();
		Example example = new Example(AmzAdvRequest.class);
		Criteria crit = example.createCriteria();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -2);
		crit.andLessThan("requesttime", c.getTime());
		amzAdvRequestMapper.deleteByExample(example);
	}
	
	public void deleteRemaindForInvalid(){
		List<Map<String, Object>> list = amzAdvRemindMapper.selectAllRemind();
		for(Map<String, Object> map : list) {
			BigInteger campaignid = (BigInteger) map.get("campaignid");
			BigInteger adgroupid = (BigInteger) map.get("adgroupid");
			BigInteger adid = (BigInteger) map.get("adid");
			BigInteger keywordid = (BigInteger) map.get("keywordid");
			BigInteger profileid = (BigInteger) map.get("profileid");
			if(keywordid != null && keywordid.compareTo(BigInteger.ZERO) != 0) {
				String keywordText = map.get("keywordText") == null ? null : map.get("keywordText").toString();
				if(keywordText != null) {
					continue;
				}else {
					Example example = new Example(AmzAdvRemind.class);
					Criteria cri = example.createCriteria();
					cri.andEqualTo("profileid", profileid);
					cri.andEqualTo("campaignid", campaignid);
					cri.andEqualTo("adgroupid", adgroupid);
					cri.andEqualTo("keywordid", keywordid);
					cri.andEqualTo("adid", adid);
					amzAdvRemindMapper.deleteByExample(example);
				}
			}
			if(adid != null && adid.compareTo(BigInteger.ZERO) != 0) {
				String productads_sku = map.get("productads_sku") == null ? null : map.get("productads_sku").toString();
				if(productads_sku != null) {
					continue;
				}else {
					Example example = new Example(AmzAdvRemind.class);
					Criteria cri = example.createCriteria();
					cri.andEqualTo("profileid", profileid);
					cri.andEqualTo("campaignid", campaignid);
					cri.andEqualTo("adgroupid", adgroupid);
					cri.andEqualTo("keywordid", keywordid);
					cri.andEqualTo("adid", adid);
					amzAdvRemindMapper.deleteByExample(example);
				}
			}
			if(adgroupid != null && adgroupid.compareTo(BigInteger.ZERO) != 0) {
				String adgroups_name = map.get("adgroups_name") == null ? null : map.get("adgroups_name").toString();
				if(adgroups_name != null) {
					continue;
				}else {
					Example example = new Example(AmzAdvRemind.class);
					Criteria cri = example.createCriteria();
					cri.andEqualTo("profileid", profileid);
					cri.andEqualTo("campaignid", campaignid);
					cri.andEqualTo("adgroupid", adgroupid);
					cri.andEqualTo("keywordid", keywordid);
					cri.andEqualTo("adid", adid);
					amzAdvRemindMapper.deleteByExample(example);
				}
			}
			if(campaignid != null && campaignid.compareTo(BigInteger.ZERO) != 0) {
				String campaigns_name = map.get("campaigns_name") == null ? null : map.get("campaigns_name").toString();
				if(campaigns_name != null) {
					continue;
				}else {
					Example example = new Example(AmzAdvRemind.class);
					Criteria cri = example.createCriteria();
					cri.andEqualTo("profileid", profileid);
					cri.andEqualTo("campaignid", campaignid);
					cri.andEqualTo("adgroupid", adgroupid);
					cri.andEqualTo("keywordid", keywordid);
					cri.andEqualTo("adid", adid);
					amzAdvRemindMapper.deleteByExample(example);
				}
			}
		}
	}
	
	public List<Map<String, Object>> getRemaindUserList(UserInfo user) {
		return amzAdvRemindMapper.getCreatorList(user.getCompanyid());
	}
	
	public void deleteReminForProfileId(BigInteger profileid) {
		amzAdvRemindMapper.deleteReminForProfileId(profileid);
	}

}
