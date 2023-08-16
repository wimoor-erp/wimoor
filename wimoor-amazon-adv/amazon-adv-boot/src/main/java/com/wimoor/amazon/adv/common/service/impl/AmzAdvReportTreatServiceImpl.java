package com.wimoor.amazon.adv.common.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.wimoor.amazon.adv.common.dao.AmzAdvProfileMapper;
import com.wimoor.amazon.adv.common.dao.AmzAdvRptQueryMapper;
import com.wimoor.amazon.adv.common.dao.AmzAdvSumProductAdsMapper;
import com.wimoor.amazon.adv.common.pojo.AdvState;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.AmzAdvRequest;
import com.wimoor.amazon.adv.common.pojo.AmzAdvRptQuery;
import com.wimoor.amazon.adv.common.pojo.AmzAdvSnapshot;
import com.wimoor.amazon.adv.common.pojo.AmzAdvSumAllType;
import com.wimoor.amazon.adv.common.pojo.AmzAdvSumProductAds;
import com.wimoor.amazon.adv.common.service.IAmazonReportAdvSummaryService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.common.service.IAmzAdvReportPlacementService;
import com.wimoor.amazon.adv.common.service.IAmzAdvReportTreatService;
import com.wimoor.amazon.adv.common.service.IAmzAdvSumAllTypeService;
import com.wimoor.amazon.adv.sb.dao.AmzAdvCampaignsHsaMapper;
import com.wimoor.amazon.adv.sb.dao.AmzAdvReportCampaignsHsaMapper;
import com.wimoor.amazon.adv.sb.dao.AmzAdvReportCampaignsPlaceHsaMapper;
import com.wimoor.amazon.adv.sb.dao.AmzAdvReportKeywordsHsaMapper;
import com.wimoor.amazon.adv.sb.dao.AmzAdvReportKeywordsQueryHsaMapper;
import com.wimoor.amazon.adv.sb.dao.AmzAdvReportProductTargeHsaMapper;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvCampaignsHsa;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportCampaignsHsa;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportCampaignsHsaAttributed;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportCampaignsHsaBrand;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportCampaignsHsaVideo;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportCampaignsPlaceHsa;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportCampaignsPlaceHsaAttributed;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportCampaignsPlaceHsaBrand;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportCampaignsPlaceHsaVideo;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportKeywordsHsa;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportKeywordsHsaAttributed;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportKeywordsHsaBrand;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportKeywordsHsaVideo;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportKeywordsQueryHsa;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportProductTargetsHsa;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportProductTargetsHsaAttributed;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportProductTargetsHsaBrand;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportProductTargetsHsaVideo;
import com.wimoor.amazon.adv.sd.dao.AmzAdvAdgroupsSDMapper;
import com.wimoor.amazon.adv.sd.dao.AmzAdvCampaignsSDMapper;
import com.wimoor.amazon.adv.sd.dao.AmzAdvProductTargeNegativaSDMapper;
import com.wimoor.amazon.adv.sd.dao.AmzAdvProductTargeSDMapper;
import com.wimoor.amazon.adv.sd.dao.AmzAdvProductadsSDMapper;
import com.wimoor.amazon.adv.sd.dao.AmzAdvReportAdGroupsSDMapper;
import com.wimoor.amazon.adv.sd.dao.AmzAdvReportAsinsSDMapper;
import com.wimoor.amazon.adv.sd.dao.AmzAdvReportCampaignsSDMapper;
import com.wimoor.amazon.adv.sd.dao.AmzAdvReportCampaignsT0001SDMapper;
import com.wimoor.amazon.adv.sd.dao.AmzAdvReportProductAdsSDMapper;
import com.wimoor.amazon.adv.sd.dao.AmzAdvReportProductTargeSDMapper;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvAdgroupsSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvCampaignsSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductTargeNegativaSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductTargeSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductadsSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportAdGroupsSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportAdGroupsSDAttributed;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportAdGroupsSDAttributedNew;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportAdGroupsSDAttributedSame;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportAdGroupsSDAttributedView;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportAsinsSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportCampaignsSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportCampaignsSDAttributed;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportCampaignsSDAttributedNew;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportCampaignsSDAttributedSame;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportCampaignsSDAttributedView;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportCampaignsT0001SD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportProductAdsSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportProductAdsSDAttributed;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportProductAdsSDAttributedNew;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportProductAdsSDAttributedSame;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportProductAdsSDAttributedView;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportProductTargetsSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportProductTargetsSDAttributed;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportProductTargetsSDAttributedNew;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportProductTargetsSDAttributedSame;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportProductTargetsSDAttributedView;
import com.wimoor.amazon.adv.sp.dao.AmzAdvAdgroupsMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvCampKeywordsNegativaMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvCampaignsMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvKeywordsMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvKeywordsNegativaMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvProductTargeMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvProductTargeNegativaMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvProductadsMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvReportAdgroupsMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvReportAsinsMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvReportCompaignsMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvReportCompaignsPlaceMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvReportKeywordsMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvReportKeywordsQueryMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvReportProductAdsMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvReportProductTargeMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvReportTargetQueryMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvAdgroups;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampKeywordsNegativa;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampaigns;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvKeywords;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvKeywordsNegativa;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductTarge;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductTargeNegativa;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductads;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportAdgroups;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportAdgroupsAttributed;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportAdgroupsAttributedSame;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportAsins;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportCompaigns;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportCompaignsAttributed;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportCompaignsAttributedSame;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportCompaignsPlace;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportCompaignsPlaceAttributed;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportCompaignsPlaceAttributedSame;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportKeywords;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportKeywordsAttributed;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportKeywordsAttributedSame;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportKeywordsQuery;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportKeywordsQueryAttributed;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportKeywordsQueryAttributedSame;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportPlacement;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportProductAds;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportProductAdsAttributed;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportProductAdsAttributedSame;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportProductTargets;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportProductTargetsAttributed;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportProductTargetsAttributedSame;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportTargetQuery;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportTargetQueryAttributed;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportTargetQueryAttributedSame;
import com.wimoor.amazon.adv.utils.EmojiFilterUtils;
import com.wimoor.common.GeneralUtil;

import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

@Service("amzAdvReportTreatService")
public class AmzAdvReportTreatServiceImpl implements IAmzAdvReportTreatService {
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	AmzAdvProfileMapper amzAdvProfileMapper;
	@Resource
	private IAmazonReportAdvSummaryService amazonReportAdvSummaryService;
	@Resource
	AmzAdvCampaignsMapper amzAdvCampaignsMapper;
	@Resource
	AmzAdvAdgroupsMapper amzAdvAdgroupsMapper;
	@Resource
	AmzAdvReportCompaignsMapper amzAdvReportCompaignsMapper;
	@Resource
	AmzAdvReportAdgroupsMapper amzAdvReportAdgroupsMapper;
	@Resource
	AmzAdvReportKeywordsMapper amzAdvReportKeywordsMapper;
	@Resource
	AmzAdvCampKeywordsNegativaMapper amzAdvCampKeywordsNegativaMapper;
	@Resource
	AmzAdvKeywordsMapper amzAdvKeywordsMapper;
	@Resource
	AmzAdvKeywordsNegativaMapper amzAdvKeywordsNegativaMapper;
	@Resource
	AmzAdvCampaignsHsaMapper amzAdvCampaignsHsaMapper;
	@Resource
	AmzAdvProductadsMapper amzAdvProductadsMapper;
	@Resource
	AmzAdvReportAsinsMapper amzAdvReportAsinsMapper;
	@Resource
	AmzAdvReportCompaignsPlaceMapper amzAdvReportCompaignsPlaceMapper;
	@Resource
	AmzAdvReportKeywordsQueryMapper amzAdvReportKeywordsQuryMapper;
	@Resource
	AmzAdvReportCampaignsHsaMapper amzAdvReportCampaignsHsaMapper;
	@Resource
	AmzAdvReportCampaignsPlaceHsaMapper amzAdvReportCampaignsPlaceHsaMapper;
	@Resource
	AmzAdvReportKeywordsHsaMapper amzAdvReportKeywordsHsaMapper;
	@Resource
	AmzAdvReportProductAdsMapper amzAdvReportProductAdsMapper;
	@Resource
	AmzAdvReportProductTargeMapper amzAdvReportProductTargeMapper;
	@Resource
	AmzAdvReportTargetQueryMapper amzAdvReportTargetQueryMapper;
	@Resource
	AmzAdvProductTargeNegativaMapper amzAdvProductTargeNegativaMapper;
	@Resource
	AmzAdvProductTargeMapper amzAdvProductTargeMapper;
	@Resource
	AmzAdvSumProductAdsMapper amzAdvSumProductAdsMapper;
	@Resource
	IAmzAdvSumAllTypeService amzAdvSumAllTypeService;
	@Resource
	AmzAdvReportProductTargeHsaMapper amzAdvReportProductTargeHsaMapper;
	@Resource
	AmzAdvReportKeywordsQueryHsaMapper amzAdvReportKeywordsQueryHsaMapper;
	@Resource
	AmzAdvReportCampaignsSDMapper amzAdvReportCampaignsSDMapper;
	@Resource
	AmzAdvReportProductAdsSDMapper amzAdvReportProductAdsSDMapper;
	@Resource
	AmzAdvReportAdGroupsSDMapper amzAdvReportAdGroupsSDMapper;
	@Resource
	AmzAdvRptQueryMapper amzAdvRptQueryMapper;
	@Resource
	AmzAdvCampaignsSDMapper amzAdvCampaignsSDMapper;
	@Resource
	AmzAdvAdgroupsSDMapper amzAdvAdgroupsSDMapper;
	@Resource
	AmzAdvProductadsSDMapper amzAdvProductadsSDMapper;
	@Resource
	AmzAdvProductTargeSDMapper amzAdvProductTargeSDMapper;
	@Resource
	AmzAdvProductTargeNegativaSDMapper amzAdvProductTargeNegativaSDMapper;
	@Resource
	AmzAdvReportProductTargeSDMapper amzAdvReportProductTargeSDMapper;
	@Resource
	AmzAdvReportAsinsSDMapper amzAdvReportAsinsSDMapper;
	@Resource
	AmzAdvReportCampaignsT0001SDMapper amzAdvReportCampaignsT0001SDMapper;
	@Resource
	IAmzAdvReportPlacementService amzAdvReportPlacementService;
	public   void treatProductAdvertReport(AmzAdvRequest request, JSONReader jsonReader) {
		AmzAdvProfile profile = amzAdvProfileMapper.selectByPrimaryKey(request.getProfileid());
		List<AmzAdvReportProductAds> list = new LinkedList<AmzAdvReportProductAds>();
		List<AmzAdvReportProductAdsAttributed> listAttributed = new LinkedList<AmzAdvReportProductAdsAttributed>();
		List<AmzAdvReportProductAdsAttributedSame> listAttributedSame = new LinkedList<AmzAdvReportProductAdsAttributedSame>();
		//AmazonReportAdvSummaryAdsMap mysumMap = new AmazonReportAdvSummaryAdsMap();
		try {
			jsonReader.startArray();
			while (jsonReader.hasNext()) {
				String elem = jsonReader.readString();
				JSONObject item = GeneralUtil.getJsonObject(elem);
				AmzAdvReportProductAds padv = new AmzAdvReportProductAds();
				AmzAdvReportProductAdsAttributed padvattr = new AmzAdvReportProductAdsAttributed();
				AmzAdvReportProductAdsAttributedSame padvattrsame=new AmzAdvReportProductAdsAttributedSame();
				padv.setBydate(request.getByday());
				padv.setAdid(item.getBigInteger("adId"));
				padv.setProfileid(profile.getId());
				padv.setCampaignid(item.getBigInteger("campaignId"));
	 
				padv.setAdgroupid(item.getBigInteger("adGroupId"));
			 
				padv.setImpressions(item.getInteger("impressions"));
				padv.setClicks(item.getInteger("clicks"));
				padv.setCost(item.getBigDecimal("cost"));
				
				
				padvattr.setBydate(padv.getBydate());
				padvattr.setAdid(padv.getAdid());
				padvattr.setAttributedconversions1d(item.getInteger("attributedConversions1d"));
				padvattr.setAttributedconversions7d(item.getInteger("attributedConversions7d"));
				padvattr.setAttributedconversions14d(item.getInteger("attributedConversions14d"));
				padvattr.setAttributedconversions30d(item.getInteger("attributedConversions30d"));

				padvattr.setAttributedunitsordered1d(item.getInteger("attributedUnitsOrdered1d"));
				padvattr.setAttributedunitsordered7d(item.getInteger("attributedUnitsOrdered7d"));
				padvattr.setAttributedunitsordered14d(item.getInteger("attributedUnitsOrdered14d"));
				padvattr.setAttributedunitsordered30d(item.getInteger("attributedUnitsOrdered30d"));

				padvattr.setAttributedsales1d(item.getBigDecimal("attributedSales1d"));
				padvattr.setAttributedsales7d(item.getBigDecimal("attributedSales7d"));
				padvattr.setAttributedsales14d(item.getBigDecimal("attributedSales14d"));
				padvattr.setAttributedsales30d(item.getBigDecimal("attributedSales30d"));

				padvattrsame.setBydate(padv.getBydate());
				padvattrsame.setAdid(padv.getAdid());
				padvattrsame.setAttributedconversions1dsamesku(item.getInteger("attributedConversions1dSameSKU"));
				padvattrsame.setAttributedconversions7dsamesku(item.getInteger("attributedConversions7dSameSKU"));
				padvattrsame.setAttributedconversions14dsamesku(item.getInteger("attributedConversions14dSameSKU"));
				padvattrsame.setAttributedconversions30dsamesku(item.getInteger("attributedConversions30dSameSKU"));
				
				padvattrsame.setAttributedsales1dsamesku(item.getBigDecimal("attributedSales1dSameSKU"));
				padvattrsame.setAttributedsales7dsamesku(item.getBigDecimal("attributedSales7dSameSKU"));
				padvattrsame.setAttributedsales14dsamesku(item.getBigDecimal("attributedSales14dSameSKU"));
				padvattrsame.setAttributedsales30dsamesku(item.getBigDecimal("attributedSales30dSameSKU"));
				
				padvattrsame.setAttributedUnitsOrdered1dSameSKU(item.getInteger("attributedUnitsOrdered1dSameSKU"));
				padvattrsame.setAttributedUnitsOrdered7dSameSKU(item.getInteger("attributedUnitsOrdered7dSameSKU"));
				padvattrsame.setAttributedUnitsOrdered14dSameSKU(item.getInteger("attributedUnitsOrdered14dSameSKU"));
				padvattrsame.setAttributedUnitsOrdered30dSameSKU(item.getInteger("attributedUnitsOrdered30dSameSKU"));
				
				if(!padvattr.isZero()) {
					listAttributed.add(padvattr);
				}
				if(!padvattrsame.isZero()) {
					listAttributedSame.add(padvattrsame);
				}
				if ((padv.getClicks() == null || padv.getClicks() == 0)
						&& (padv.getImpressions() == null || padv.getImpressions() == 0)
						&& (padv.getCost() == null || padv.getCost().compareTo(new BigDecimal("0")) == 0)
                      ) {
					continue;
				}
//				if (StringUtil.isNotEmpty(sku)) {
//					mysumMap.addAdvReportSummary(profile,padv, padvattr,sku,asin,currency);
//				}
		
				list.add(padv);

		 
				if (list.size() >= 2000) {
					amzAdvReportProductAdsMapper.insertBatch(list);
					list.clear();
				}
				if (listAttributed.size() >= 2000) {
					amzAdvReportProductAdsMapper.insertBatchAttributed(listAttributed);
					listAttributed.clear();
				}
				if (listAttributedSame.size() >= 2000) {
					amzAdvReportProductAdsMapper.insertBatchAttributedSame(listAttributedSame);
					listAttributedSame.clear();
				}
			}
			if (list.size() > 0) {
				amzAdvReportProductAdsMapper.insertBatch(list);
			}
			if (listAttributed.size() > 0) {
				amzAdvReportProductAdsMapper.insertBatchAttributed(listAttributed);
			}
			if (listAttributedSame.size() > 0) {
				amzAdvReportProductAdsMapper.insertBatchAttributedSame(listAttributedSame);
			}
		} catch(Exception e){
			e.printStackTrace();
		} 
	}
	
	public   void treatProductSDAdvertReport(AmzAdvRequest request, JSONReader jsonReader) {
		List<AmzAdvReportProductAdsSD> list = new LinkedList<AmzAdvReportProductAdsSD>();
		List<AmzAdvReportProductAdsSDAttributed> listAttributed = new LinkedList<AmzAdvReportProductAdsSDAttributed>();
		List<AmzAdvReportProductAdsSDAttributedView> listAttributedView = new LinkedList<AmzAdvReportProductAdsSDAttributedView>();
		List<AmzAdvReportProductAdsSDAttributedNew> listAttributedNew = new LinkedList<AmzAdvReportProductAdsSDAttributedNew>();
		List<AmzAdvReportProductAdsSDAttributedSame> listAttributedSame = new LinkedList<AmzAdvReportProductAdsSDAttributedSame>();
		try {
			jsonReader.startArray();

			while (jsonReader.hasNext()) {
				String elem = jsonReader.readString();
				JSONObject item = GeneralUtil.getJsonObject(elem);
				AmzAdvReportProductAdsSD padv = new AmzAdvReportProductAdsSD();
				AmzAdvReportProductAdsSDAttributed     padvAttr =     new AmzAdvReportProductAdsSDAttributed();
				AmzAdvReportProductAdsSDAttributedSame padvAttrSame = new AmzAdvReportProductAdsSDAttributedSame();
				AmzAdvReportProductAdsSDAttributedNew  padvAttrNew =  new AmzAdvReportProductAdsSDAttributedNew();
				AmzAdvReportProductAdsSDAttributedView padvAttrview=  new AmzAdvReportProductAdsSDAttributedView();
				padv.setBydate(request.getByday());
				padv.setAdid(item.getBigInteger("adId"));
				padv.setProfileid(request.getProfileid());
				padv.setCampaignid(item.getBigInteger("campaignId"));
				padv.setAdgroupid(item.getBigInteger("adGroupId"));
				padv.setImpressions(item.getInteger("impressions"));
				padv.setClicks(item.getInteger("clicks"));
				padv.setCost(item.getBigDecimal("cost"));
				padv.setOpttime(new Date());
				padvAttr.setBydate(padv.getBydate());
				padvAttr.setAdid(padv.getAdid());
				padvAttr.setAttributedconversions1d(item.getInteger("attributedConversions1d"));
				padvAttr.setAttributedconversions7d(item.getInteger("attributedConversions7d"));
				padvAttr.setAttributedconversions14d(item.getInteger("attributedConversions14d"));
				padvAttr.setAttributedconversions30d(item.getInteger("attributedConversions30d"));


				padvAttr.setAttributedunitsordered1d(item.getInteger("attributedUnitsOrdered1d"));
				padvAttr.setAttributedunitsordered7d(item.getInteger("attributedUnitsOrdered7d"));
				padvAttr.setAttributedunitsordered14d(item.getInteger("attributedUnitsOrdered14d"));
				padvAttr.setAttributedunitsordered30d(item.getInteger("attributedUnitsOrdered30d"));

				padvAttr.setAttributedsales1d(item.getBigDecimal("attributedSales1d"));
				padvAttr.setAttributedsales7d(item.getBigDecimal("attributedSales7d"));
				padvAttr.setAttributedsales14d(item.getBigDecimal("attributedSales14d"));
				padvAttr.setAttributedsales30d(item.getBigDecimal("attributedSales30d"));
				
				padvAttrSame.setBydate(padv.getBydate());
				padvAttrSame.setAdid(padv.getAdid());
				padvAttrSame.setAttributedconversions1dsamesku(item.getInteger("attributedConversions1dSameSKU"));
				padvAttrSame.setAttributedconversions7dsamesku(item.getInteger("attributedConversions7dSameSKU"));
				padvAttrSame.setAttributedconversions14dsamesku(item.getInteger("attributedConversions14dSameSKU"));
				padvAttrSame.setAttributedconversions30dsamesku(item.getInteger("attributedConversions30dSameSKU"));
				
				padvAttrSame.setAttributedsales1dsamesku(item.getBigDecimal("attributedSales1dSameSKU"));
				padvAttrSame.setAttributedsales7dsamesku(item.getBigDecimal("attributedSales7dSameSKU"));
				padvAttrSame.setAttributedsales14dsamesku(item.getBigDecimal("attributedSales14dSameSKU"));
				padvAttrSame.setAttributedsales30dsamesku(item.getBigDecimal("attributedSales30dSameSKU"));
				
				padvAttrNew.setBydate(padv.getBydate());
				padvAttrNew.setAdid(padv.getAdid());
				padvAttrNew.setAttributedUnitsOrderedNewToBrand14d(item.getInteger("attributedUnitsOrderedNewToBrand14d"));
				padvAttrNew.setAttributedOrdersNewToBrand14d(item.getInteger("attributedOrdersNewToBrand14d"));
				padvAttrNew.setAttributedSalesNewToBrand14d(item.getBigDecimal("attributedSalesNewToBrand14d"));
				
				padvAttrview.setBydate(padv.getBydate());
				padvAttrview.setAdid(padv.getAdid());
				padvAttrview.setViewAttributedConversions14d(item.getInteger("viewAttributedConversions14d"));
				padvAttrview.setViewAttributedSales14d(item.getBigDecimal("viewAttributedSales14d"));
				padvAttrview.setViewAttributedUnitsOrdered14d(item.getInteger("viewAttributedUnitsOrdered14d"));
				padvAttrview.setViewImpressions(item.getInteger("viewImpressions"));
				if(padv.getAdid()==null) {
					continue;
				}
				if(!padvAttr.isZero()) {
					listAttributed.add(padvAttr);
				}
				
				if(!padvAttrview.isZero()) {
					listAttributedView.add(padvAttrview);
				}
				if(!padvAttrSame.isZero()) {
					listAttributedSame.add(padvAttrSame);
				}
				if(!padvAttrNew.isZero()) {
					listAttributedNew.add(padvAttrNew);
				}
				if ((padv.getClicks() == null || padv.getClicks() == 0)
						&& (padv.getImpressions() == null || padv.getImpressions() == 0)
						&& (padv.getCost() == null || padv.getCost().compareTo(new BigDecimal("0")) == 0)
						 ) {
					continue;
				}
				list.add(padv);

			}
			if (list.size() > 0) {
				amzAdvReportProductAdsSDMapper.insertBatch(list);
			}
			if (listAttributed.size() > 0) {
				amzAdvReportProductAdsSDMapper.insertBatchAttributed(listAttributed);
			}
			if (listAttributedView.size() > 0) {
				amzAdvReportProductAdsSDMapper.insertBatchAttributedView(listAttributedView);
			}
			if (listAttributedSame.size() > 0) {
				amzAdvReportProductAdsSDMapper.insertBatchAttributedSame(listAttributedSame);
			}
			if (listAttributedNew.size() > 0) {
				amzAdvReportProductAdsSDMapper.insertBatchAttributedNew(listAttributedNew);
			}

			
		} catch(Exception e){
			e.printStackTrace();
		} 
	}

	public   void treatCampaignsAdvertReport(AmzAdvRequest request, JSONReader jsonReader) {
		final List<AmzAdvReportCompaigns> list = new LinkedList<AmzAdvReportCompaigns>();
		final List<AmzAdvReportCompaignsAttributed> listAttributed = new LinkedList<AmzAdvReportCompaignsAttributed>();
		final List<AmzAdvReportCompaignsAttributedSame> listAttributedSame = new LinkedList<AmzAdvReportCompaignsAttributedSame>();
		Integer clicks = 0;
		String currency = "";
		Integer impressions = 0;
		BigDecimal cost = new BigDecimal("0");
		BigDecimal attributedsales = new BigDecimal("0");
		Integer attributedunitsordered = 0;
		Integer attributedConversions = 0;
		try {
			jsonReader.startArray();
			while (jsonReader.hasNext()) {
				String elem = jsonReader.readString();
				JSONObject item = GeneralUtil.getJsonObject(elem);

				AmzAdvReportCompaigns amzAdvReportCompaigns = new AmzAdvReportCompaigns();
				AmzAdvReportCompaignsAttributed amzAdvReportCompaignsAttributed = new AmzAdvReportCompaignsAttributed();
				AmzAdvReportCompaignsAttributedSame amzAdvReportCompaignsAttributedSame = new AmzAdvReportCompaignsAttributedSame();
				amzAdvReportCompaigns.setProfileid(request.getProfileid());
				amzAdvReportCompaigns.setBydate(request.getByday());
				amzAdvReportCompaigns.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvReportCompaigns.setCampaignbudget(item.getBigDecimal("campaignBudget"));

				amzAdvReportCompaigns.setClicks(item.getInteger("clicks"));
				amzAdvReportCompaigns.setImpressions(item.getInteger("impressions"));
				amzAdvReportCompaigns.setCost(item.getBigDecimal("cost"));
				amzAdvReportCompaigns.setOpttime(new Date());
				amzAdvReportCompaignsAttributed.setBydate(amzAdvReportCompaigns.getBydate());
				amzAdvReportCompaignsAttributed.setCampaignid(amzAdvReportCompaigns.getCampaignid());
				amzAdvReportCompaignsAttributed.setAttributedsales1d(item.getBigDecimal("attributedSales1d"));
				amzAdvReportCompaignsAttributed.setAttributedsales7d(item.getBigDecimal("attributedSales7d"));
				amzAdvReportCompaignsAttributed.setAttributedsales14d(item.getBigDecimal("attributedSales14d"));
				amzAdvReportCompaignsAttributed.setAttributedsales30d(item.getBigDecimal("attributedSales30d"));
                if(item.getString("currency")!=null) {
                	 currency =item.getString("currency");
                 }

				amzAdvReportCompaignsAttributed.setAttributedunitsordered1d(item.getInteger("attributedUnitsOrdered1d"));
				amzAdvReportCompaignsAttributed.setAttributedunitsordered7d(item.getInteger("attributedUnitsOrdered7d"));
				amzAdvReportCompaignsAttributed.setAttributedunitsordered14d(item.getInteger("attributedUnitsOrdered14d"));
				amzAdvReportCompaignsAttributed.setAttributedunitsordered30d(item.getInteger("attributedUnitsOrdered30d"));

				amzAdvReportCompaignsAttributed.setAttributedconversions1d(item.getInteger("attributedConversions1d"));
				amzAdvReportCompaignsAttributed.setAttributedconversions7d(item.getInteger("attributedConversions7d"));
				amzAdvReportCompaignsAttributed.setAttributedconversions14d(item.getInteger("attributedConversions14d"));
				amzAdvReportCompaignsAttributed.setAttributedconversions30d(item.getInteger("attributedConversions30d"));

				
				amzAdvReportCompaignsAttributedSame.setBydate(amzAdvReportCompaigns.getBydate());
				amzAdvReportCompaignsAttributedSame.setCampaignid(amzAdvReportCompaigns.getCampaignid());
				amzAdvReportCompaignsAttributedSame.setAttributedsales1dsamesku(item.getBigDecimal("attributedSales1dSameSKU"));
				amzAdvReportCompaignsAttributedSame.setAttributedsales7dsamesku(item.getBigDecimal("attributedSales7dSameSKU"));
				amzAdvReportCompaignsAttributedSame.setAttributedsales14dsamesku(item.getBigDecimal("attributedSales14dSameSKU"));
				amzAdvReportCompaignsAttributedSame.setAttributedsales30dsamesku(item.getBigDecimal("attributedSales30dSameSKU"));
				
				amzAdvReportCompaignsAttributedSame.setAttributedconversions1dsamesku(item.getInteger("attributedConversions1dSameSKU"));
				amzAdvReportCompaignsAttributedSame.setAttributedconversions7dsamesku(item.getInteger("attributedConversions7dSameSKU"));
				amzAdvReportCompaignsAttributedSame.setAttributedconversions14dsamesku(item.getInteger("attributedConversions14dSameSKU"));
				amzAdvReportCompaignsAttributedSame.setAttributedconversions30dsamesku(item.getInteger("attributedConversions30dSameSKU"));
				
				amzAdvReportCompaignsAttributedSame.setAttributedUnitsOrdered1dSameSKU(item.getInteger("attributedUnitsOrdered1dSameSKU"));
				amzAdvReportCompaignsAttributedSame.setAttributedUnitsOrdered7dSameSKU(item.getInteger("attributedUnitsOrdered7dSameSKU"));
				amzAdvReportCompaignsAttributedSame.setAttributedUnitsOrdered14dSameSKU(item.getInteger("attributedUnitsOrdered14dSameSKU"));
				amzAdvReportCompaignsAttributedSame.setAttributedUnitsOrdered30dSameSKU(item.getInteger("attributedUnitsOrdered30dSameSKU"));
				
				clicks = clicks + amzAdvReportCompaigns.getClicks();
				cost = cost.add(amzAdvReportCompaigns.getCost());
		 
				impressions = impressions + amzAdvReportCompaigns.getImpressions();
				if(amzAdvReportCompaignsAttributed.getAttributedsales7d() != null) {
					attributedsales = attributedsales.add(amzAdvReportCompaignsAttributed.getAttributedsales7d());
				}
				if(amzAdvReportCompaignsAttributed.getAttributedunitsordered7d() != null) {
					attributedunitsordered = attributedunitsordered + amzAdvReportCompaignsAttributed.getAttributedunitsordered7d();
				}
				if (amzAdvReportCompaignsAttributed.getAttributedconversions7d() != null) {
					attributedConversions = attributedConversions + amzAdvReportCompaignsAttributed.getAttributedconversions7d();
				}
	
				
				if ((amzAdvReportCompaigns.getClicks() == null || amzAdvReportCompaigns.getClicks() == 0)
						&& (amzAdvReportCompaigns.getImpressions() == null || amzAdvReportCompaigns.getImpressions() == 0)
						&& (amzAdvReportCompaigns.getCost() == null || amzAdvReportCompaigns.getCost().compareTo(new BigDecimal("0")) == 0)
						) {
					continue;
				}
				if(!amzAdvReportCompaignsAttributed.isZero()) {
					listAttributed.add(amzAdvReportCompaignsAttributed);
				}
				if(!amzAdvReportCompaignsAttributedSame.isZero()) {
					listAttributedSame.add(amzAdvReportCompaignsAttributedSame);
				}
				list.add(amzAdvReportCompaigns);
				if (list.size() >= 2000) {
					amzAdvReportCompaignsMapper.insertBatch(list);
					list.clear();
				}
				if (listAttributed.size() >= 2000) {
					amzAdvReportCompaignsMapper.insertBatchAttributed(listAttributed);
					listAttributed.clear();
				}
				if (listAttributedSame.size() >= 2000) {
					amzAdvReportCompaignsMapper.insertBatchAttributedSame(listAttributedSame);
					listAttributedSame.clear();
				}
			}
			if (list.size() > 0) {
				amzAdvReportCompaignsMapper.insertBatch(list);
			}
			if (listAttributed.size() > 0) {
				amzAdvReportCompaignsMapper.insertBatchAttributed(listAttributed);
			}
			if (listAttributedSame.size() > 0) {
				amzAdvReportCompaignsMapper.insertBatchAttributedSame(listAttributedSame);
			}
			AmzAdvSumProductAds record = new AmzAdvSumProductAds();
			record.setProfileid(request.getProfileid());
			record.setByday(request.getByday());
			record.setCurrency(currency);
			record.setCost(cost);
			record.setClicks(clicks);
			record.setCtype("sp");
			record.setImpressions(impressions);
			record.setAttributedunitsordered(attributedunitsordered);
			record.setAttributedConversions(attributedConversions);
			record.setAttributedsales(attributedsales);
			AmzAdvSumProductAds old = amzAdvSumProductAdsMapper.selectByKey(record);
			if (old != null) {
				amzAdvSumProductAdsMapper.updateByKey(record);
			} else {
				amzAdvSumProductAdsMapper.insert(record);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}  
	}
	
	public   void treatCampaignsSDAdvertReport(AmzAdvRequest request, JSONReader jsonReader) {
		 List<AmzAdvReportCampaignsSD> list = new LinkedList<AmzAdvReportCampaignsSD>();
		 List<AmzAdvReportCampaignsSDAttributed> listAttributed = new LinkedList<AmzAdvReportCampaignsSDAttributed>();
		 List<AmzAdvReportCampaignsSDAttributedView> listAttributedView = new LinkedList<AmzAdvReportCampaignsSDAttributedView>();
		 List<AmzAdvReportCampaignsSDAttributedNew> listAttributedNew = new LinkedList<AmzAdvReportCampaignsSDAttributedNew>();
		 List<AmzAdvReportCampaignsSDAttributedSame> listAttributedSame = new LinkedList<AmzAdvReportCampaignsSDAttributedSame>();
			Integer clicks = 0;
			String currency = "";
			Integer impressions = 0;
			BigDecimal cost = new BigDecimal("0");
			BigDecimal attributedsales = new BigDecimal("0");
			Integer attributedunitsordered = 0;
			Integer attributedConversions = 0;
		try {
			jsonReader.startArray();
			while (jsonReader.hasNext()) {
				String elem = jsonReader.readString();
				JSONObject item = GeneralUtil.getJsonObject(elem);

				AmzAdvReportCampaignsSD amzAdvReportCampaignsSD = new AmzAdvReportCampaignsSD();
				AmzAdvReportCampaignsSDAttributed amzAdvReportCampaignsSDAttributed = new AmzAdvReportCampaignsSDAttributed();
				AmzAdvReportCampaignsSDAttributedView amzAdvReportCampaignsSDAttributedView = new AmzAdvReportCampaignsSDAttributedView();
				AmzAdvReportCampaignsSDAttributedSame amzAdvReportCampaignsSDAttributedSame = new AmzAdvReportCampaignsSDAttributedSame();
				AmzAdvReportCampaignsSDAttributedNew amzAdvReportCampaignsSDAttributedNew = new AmzAdvReportCampaignsSDAttributedNew();
				amzAdvReportCampaignsSD.setProfileid(request.getProfileid());
				amzAdvReportCampaignsSD.setBydate(request.getByday());
				amzAdvReportCampaignsSD.setCampaignid(item.getBigInteger("campaignId"));
				if (currency == null && StringUtil.isNotEmpty(item.getString("currency"))) {
					currency =item.getString("currency");
				}
				amzAdvReportCampaignsSD.setClicks(item.getInteger("clicks"));
				amzAdvReportCampaignsSD.setImpressions(item.getInteger("impressions"));
				amzAdvReportCampaignsSD.setCost(item.getBigDecimal("cost"));
				amzAdvReportCampaignsSD.setOpttime(new Date());
				amzAdvReportCampaignsSDAttributed.setBydate(amzAdvReportCampaignsSD.getBydate());
				amzAdvReportCampaignsSDAttributed.setCampaignid(amzAdvReportCampaignsSD.getCampaignid());
				amzAdvReportCampaignsSDAttributed.setAttributedsales1d(item.getBigDecimal("attributedSales1d"));
				amzAdvReportCampaignsSDAttributed.setAttributedsales7d(item.getBigDecimal("attributedSales7d"));
				amzAdvReportCampaignsSDAttributed.setAttributedsales14d(item.getBigDecimal("attributedSales14d"));
				amzAdvReportCampaignsSDAttributed.setAttributedsales30d(item.getBigDecimal("attributedSales30d"));



				amzAdvReportCampaignsSDAttributed.setAttributedunitsordered1d(item.getInteger("attributedUnitsOrdered1d"));
				amzAdvReportCampaignsSDAttributed.setAttributedunitsordered7d(item.getInteger("attributedUnitsOrdered7d"));
				amzAdvReportCampaignsSDAttributed.setAttributedunitsordered14d(item.getInteger("attributedUnitsOrdered14d"));
				amzAdvReportCampaignsSDAttributed.setAttributedunitsordered30d(item.getInteger("attributedUnitsOrdered30d"));

				amzAdvReportCampaignsSDAttributed.setAttributedconversions1d(item.getInteger("attributedConversions1d"));
				amzAdvReportCampaignsSDAttributed.setAttributedconversions7d(item.getInteger("attributedConversions7d"));
				amzAdvReportCampaignsSDAttributed.setAttributedconversions14d(item.getInteger("attributedConversions14d"));
				amzAdvReportCampaignsSDAttributed.setAttributedconversions30d(item.getInteger("attributedConversions30d"));
				
				amzAdvReportCampaignsSDAttributedSame.setCampaignid(amzAdvReportCampaignsSD.getCampaignid());
				amzAdvReportCampaignsSDAttributedSame.setBydate(amzAdvReportCampaignsSD.getBydate());
				amzAdvReportCampaignsSDAttributedSame.setAttributedsales1dsamesku(item.getBigDecimal("attributedSales1dSameSKU"));
				amzAdvReportCampaignsSDAttributedSame.setAttributedsales7dsamesku(item.getBigDecimal("attributedSales7dSameSKU"));
				amzAdvReportCampaignsSDAttributedSame.setAttributedsales14dsamesku(item.getBigDecimal("attributedSales14dSameSKU"));
				amzAdvReportCampaignsSDAttributedSame.setAttributedsales30dsamesku(item.getBigDecimal("attributedSales30dSameSKU"));
				

				amzAdvReportCampaignsSDAttributedSame.setAttributedconversions1dsamesku(item.getInteger("attributedConversions1dSameSKU"));
				amzAdvReportCampaignsSDAttributedSame.setAttributedconversions7dsamesku(item.getInteger("attributedConversions7dSameSKU"));
				amzAdvReportCampaignsSDAttributedSame.setAttributedconversions14dsamesku(item.getInteger("attributedConversions14dSameSKU"));
				amzAdvReportCampaignsSDAttributedSame.setAttributedconversions30dsamesku(item.getInteger("attributedConversions30dSameSKU"));
				
				
				amzAdvReportCampaignsSDAttributedNew.setCampaignid(amzAdvReportCampaignsSD.getCampaignid());
				amzAdvReportCampaignsSDAttributedNew.setBydate(amzAdvReportCampaignsSD.getBydate());
				amzAdvReportCampaignsSDAttributedNew.setAttributedUnitsOrderedNewToBrand14d(item.getInteger("attributedUnitsOrderedNewToBrand14d"));
				amzAdvReportCampaignsSDAttributedNew.setAttributedOrdersNewToBrand14d(item.getInteger("attributedOrdersNewToBrand14d"));
				amzAdvReportCampaignsSDAttributedNew.setAttributedSalesNewToBrand14d(item.getBigDecimal("attributedSalesNewToBrand14d"));
				
		 
				
				amzAdvReportCampaignsSDAttributedView.setCampaignid(amzAdvReportCampaignsSD.getCampaignid());
				amzAdvReportCampaignsSDAttributedView.setBydate(amzAdvReportCampaignsSD.getBydate());
				amzAdvReportCampaignsSDAttributedView.setViewAttributedConversions14d(item.getInteger("viewAttributedConversions14d"));
				amzAdvReportCampaignsSDAttributedView.setViewAttributedSales14d(item.getBigDecimal("viewAttributedSales14d"));
				amzAdvReportCampaignsSDAttributedView.setViewAttributedUnitsOrdered14d(item.getInteger("viewAttributedUnitsOrdered14d"));
				amzAdvReportCampaignsSDAttributedView.setViewImpressions(item.getInteger("viewImpressions"));
				
				
				
				clicks = clicks + amzAdvReportCampaignsSD.getClicks();
				cost = cost.add(amzAdvReportCampaignsSD.getCost());
			
				impressions = impressions + amzAdvReportCampaignsSD.getImpressions();
				if(amzAdvReportCampaignsSDAttributed.getAttributedsales7d() != null) {
					attributedsales = attributedsales.add(amzAdvReportCampaignsSDAttributed.getAttributedsales7d());
				}
				if(amzAdvReportCampaignsSDAttributed.getAttributedunitsordered7d() != null) {
					attributedunitsordered = attributedunitsordered + amzAdvReportCampaignsSDAttributed.getAttributedunitsordered7d();
				}
				if (amzAdvReportCampaignsSDAttributed.getAttributedconversions7d() != null) {
					attributedConversions = attributedConversions + amzAdvReportCampaignsSDAttributed.getAttributedconversions7d();
				}
				
				if(!amzAdvReportCampaignsSDAttributed.isZero()) {
					listAttributed.add(amzAdvReportCampaignsSDAttributed);
				}
				if(!amzAdvReportCampaignsSDAttributedView.isZero()) {
					listAttributedView.add(amzAdvReportCampaignsSDAttributedView);
				}
				if(!amzAdvReportCampaignsSDAttributedSame.isZero()) {
					listAttributedSame.add(amzAdvReportCampaignsSDAttributedSame);
				}
				if(!amzAdvReportCampaignsSDAttributedNew.isZero()) {
					listAttributedNew.add(amzAdvReportCampaignsSDAttributedNew);
				}
				if ((amzAdvReportCampaignsSD.getClicks() == null || amzAdvReportCampaignsSD.getClicks() == 0)
						&& (amzAdvReportCampaignsSD.getImpressions() == null || amzAdvReportCampaignsSD.getImpressions() == 0)
						&& (amzAdvReportCampaignsSD.getCost() == null || amzAdvReportCampaignsSD.getCost().compareTo(new BigDecimal("0")) == 0)
						) {
					continue;
				}
				list.add(amzAdvReportCampaignsSD);
		
				
				if (list.size() >= 2000) {
					amzAdvReportCampaignsSDMapper.insertBatch(list);
					list.clear();
				}
				if (listAttributed.size() >= 2000) {
					amzAdvReportCampaignsSDMapper.insertBatchAttributed(listAttributed);
					listAttributed.clear();
				}
				if (listAttributedView.size() >= 2000) {
					amzAdvReportCampaignsSDMapper.insertBatchAttributedView(listAttributedView);
					listAttributedView.clear();
				}
				if (listAttributedSame.size() >= 2000) {
					amzAdvReportCampaignsSDMapper.insertBatchAttributedSame(listAttributedSame);
					listAttributedSame.clear();
				}
				if (listAttributedNew.size() >= 2000) {
					amzAdvReportCampaignsSDMapper.insertBatchAttributedNew(listAttributedNew);
					listAttributedNew.clear();
				}
			}
			if (list.size() > 0) {
				amzAdvReportCampaignsSDMapper.insertBatch(list);
			}
			if (listAttributed.size() > 0) {
				amzAdvReportCampaignsSDMapper.insertBatchAttributed(listAttributed);
			}
			if (listAttributedView.size() > 0) {
				amzAdvReportCampaignsSDMapper.insertBatchAttributedView(listAttributedView);
			}
			if (listAttributedSame.size() > 0) {
				amzAdvReportCampaignsSDMapper.insertBatchAttributedSame(listAttributedSame);
			}
			if (listAttributedNew.size() > 0) {
				amzAdvReportCampaignsSDMapper.insertBatchAttributedNew(listAttributedNew);
			}
			AmzAdvSumProductAds record = new AmzAdvSumProductAds();
			record.setProfileid(request.getProfileid());
			record.setByday(request.getByday());
			record.setCurrency(currency);
			record.setCost(cost);
			record.setClicks(clicks);
			record.setCtype("sd");
			record.setImpressions(impressions);
			record.setAttributedunitsordered(attributedunitsordered);
			record.setAttributedConversions(attributedConversions);
			record.setAttributedsales(attributedsales);
			AmzAdvSumProductAds old = amzAdvSumProductAdsMapper.selectByKey(record);
			if (old != null) {
				amzAdvSumProductAdsMapper.updateByKey(record);
			} else {
				amzAdvSumProductAdsMapper.insert(record);
			}
		}  catch(Exception e) {
			e.printStackTrace();
		}
	}

	public   void treatAdroupsAdvertReport(AmzAdvRequest request, JSONReader jsonReader) {
		final List<AmzAdvReportAdgroups> list = new LinkedList<AmzAdvReportAdgroups>();
		final List<AmzAdvReportAdgroupsAttributed> listAttributed = new LinkedList<AmzAdvReportAdgroupsAttributed>();
		final List<AmzAdvReportAdgroupsAttributedSame> listAttributedSame = new LinkedList<AmzAdvReportAdgroupsAttributedSame>();
		try {
			jsonReader.startArray();
			while (jsonReader.hasNext()) {
				String elem = jsonReader.readString();
				JSONObject item = GeneralUtil.getJsonObject(elem);
				AmzAdvReportAdgroups amzAdvReportGroup = new AmzAdvReportAdgroups();
				AmzAdvReportAdgroupsAttributed amzAdvReportAdgroupsAttributed=new AmzAdvReportAdgroupsAttributed();
				AmzAdvReportAdgroupsAttributedSame amzAdvReportAdgroupsAttributedSame=new AmzAdvReportAdgroupsAttributedSame();
				amzAdvReportGroup.setProfileid(request.getProfileid());
				amzAdvReportGroup.setBydate(request.getByday());
				amzAdvReportGroup.setAdgroupid(item.getBigInteger("adGroupId"));
				amzAdvReportGroup.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvReportGroup.setProfileid(request.getProfileid());
				amzAdvReportGroup.setBydate(request.getByday());

				amzAdvReportGroup.setClicks(item.getInteger("clicks"));
				amzAdvReportGroup.setImpressions(item.getInteger("impressions"));
				amzAdvReportGroup.setCost(item.getBigDecimal("cost"));
				amzAdvReportGroup.setOpttime(new Date());
				amzAdvReportAdgroupsAttributed.setBydate(amzAdvReportGroup.getBydate());
				amzAdvReportAdgroupsAttributed.setAdgroupid(amzAdvReportGroup.getAdgroupid());
				amzAdvReportAdgroupsAttributed.setAttributedsales1d(item.getBigDecimal("attributedSales1d"));
				amzAdvReportAdgroupsAttributed.setAttributedsales7d(item.getBigDecimal("attributedSales7d"));
				amzAdvReportAdgroupsAttributed.setAttributedsales14d(item.getBigDecimal("attributedSales14d"));
				amzAdvReportAdgroupsAttributed.setAttributedsales30d(item.getBigDecimal("attributedSales30d"));



				amzAdvReportAdgroupsAttributed.setAttributedunitsordered1d(item.getInteger("attributedUnitsOrdered1d"));
				amzAdvReportAdgroupsAttributed.setAttributedunitsordered7d(item.getInteger("attributedUnitsOrdered7d"));
				amzAdvReportAdgroupsAttributed.setAttributedunitsordered14d(item.getInteger("attributedUnitsOrdered14d"));
				amzAdvReportAdgroupsAttributed.setAttributedunitsordered30d(item.getInteger("attributedUnitsOrdered30d"));

				amzAdvReportAdgroupsAttributed.setAttributedconversions1d(item.getInteger("attributedConversions1d"));
				amzAdvReportAdgroupsAttributed.setAttributedconversions7d(item.getInteger("attributedConversions7d"));
				amzAdvReportAdgroupsAttributed.setAttributedconversions14d(item.getInteger("attributedConversions14d"));
				amzAdvReportAdgroupsAttributed.setAttributedconversions30d(item.getInteger("attributedConversions30d"));
				
				
				
				amzAdvReportAdgroupsAttributedSame.setBydate(amzAdvReportGroup.getBydate());
				amzAdvReportAdgroupsAttributedSame.setAdgroupid(amzAdvReportGroup.getAdgroupid());
				amzAdvReportAdgroupsAttributedSame.setAttributedsales1dsamesku(item.getBigDecimal("attributedSales1dSameSKU"));
				amzAdvReportAdgroupsAttributedSame.setAttributedsales7dsamesku(item.getBigDecimal("attributedSales7dSameSKU"));
				amzAdvReportAdgroupsAttributedSame.setAttributedsales14dsamesku(item.getBigDecimal("attributedSales14dSameSKU"));
				amzAdvReportAdgroupsAttributedSame.setAttributedsales30dsamesku(item.getBigDecimal("attributedSales30dSameSKU"));

				amzAdvReportAdgroupsAttributedSame.setAttributedconversions1dsamesku(item.getInteger("attributedConversions1dSameSKU"));
				amzAdvReportAdgroupsAttributedSame.setAttributedconversions7dsamesku(item.getInteger("attributedConversions7dSameSKU"));
				amzAdvReportAdgroupsAttributedSame.setAttributedconversions14dsamesku(item.getInteger("attributedConversions14dSameSKU"));
				amzAdvReportAdgroupsAttributedSame.setAttributedconversions30dsamesku(item.getInteger("attributedConversions30dSameSKU"));
				
				amzAdvReportAdgroupsAttributedSame.setAttributedUnitsOrdered1dSameSKU(item.getInteger("attributedUnitsOrdered1dSameSKU"));
				amzAdvReportAdgroupsAttributedSame.setAttributedUnitsOrdered7dSameSKU(item.getInteger("attributedUnitsOrdered7dSameSKU"));
				amzAdvReportAdgroupsAttributedSame.setAttributedUnitsOrdered14dSameSKU(item.getInteger("attributedUnitsOrdered14dSameSKU"));
				amzAdvReportAdgroupsAttributedSame.setAttributedUnitsOrdered30dSameSKU(item.getInteger("attributedUnitsOrdered30dSameSKU"));
				
				if(!amzAdvReportAdgroupsAttributed.isZero()) {
					listAttributed.add(amzAdvReportAdgroupsAttributed);
				}
				if(!amzAdvReportAdgroupsAttributedSame.isZero()) {
					listAttributedSame.add(amzAdvReportAdgroupsAttributedSame);
				}
				if ((amzAdvReportGroup.getClicks() == null || amzAdvReportGroup.getClicks() == 0)
						&& (amzAdvReportGroup.getImpressions() == null || amzAdvReportGroup.getImpressions() == 0)
						&& (amzAdvReportGroup.getCost() == null || amzAdvReportGroup.getCost().compareTo(new BigDecimal("0")) == 0)
						) {
					continue;
				}
				list.add(amzAdvReportGroup);
	
				if (list.size() >= 2000) {
					amzAdvReportAdgroupsMapper.insertBatch(list);
					list.clear();
				}
				if (listAttributed.size() >= 2000) {
					amzAdvReportAdgroupsMapper.insertBatchAttributed(listAttributed);
					listAttributed.clear();
				}
				if (listAttributedSame.size() >= 2000) {
					amzAdvReportAdgroupsMapper.insertBatchAttributedSame(listAttributedSame);
					listAttributedSame.clear();
				}
			}
			if (list.size() > 0) {
				amzAdvReportAdgroupsMapper.insertBatch(list);
			}
			if (listAttributed.size() > 0) {
				amzAdvReportAdgroupsMapper.insertBatchAttributed(listAttributed);
			}
			if (listAttributedSame.size() > 0) {
				amzAdvReportAdgroupsMapper.insertBatchAttributedSame(listAttributedSame);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}  
	}
	
	public  void treatAdroupsSDAdvertReport(AmzAdvRequest request, JSONReader jsonReader) {
		final List<AmzAdvReportAdGroupsSD> list = new LinkedList<AmzAdvReportAdGroupsSD>();
		final List<AmzAdvReportAdGroupsSDAttributed> listAttributed = new LinkedList<AmzAdvReportAdGroupsSDAttributed>();
		final List<AmzAdvReportAdGroupsSDAttributedView> listAttributedView = new LinkedList<AmzAdvReportAdGroupsSDAttributedView>();
		final List<AmzAdvReportAdGroupsSDAttributedSame> listAttributedSame = new LinkedList<AmzAdvReportAdGroupsSDAttributedSame>();
		final List<AmzAdvReportAdGroupsSDAttributedNew> listAttributedNew = new LinkedList<AmzAdvReportAdGroupsSDAttributedNew>();
		try {
			jsonReader.startArray();
			while (jsonReader.hasNext()) {
				String elem = jsonReader.readString();
				JSONObject item = GeneralUtil.getJsonObject(elem);
				AmzAdvReportAdGroupsSD amzAdvReportAdGroupsSD = new AmzAdvReportAdGroupsSD();
				AmzAdvReportAdGroupsSDAttributed amzAdvReportAdGroupsSDAttributed = new AmzAdvReportAdGroupsSDAttributed();
				AmzAdvReportAdGroupsSDAttributedView amzAdvReportAdGroupsSDAttributedView = new AmzAdvReportAdGroupsSDAttributedView();
				AmzAdvReportAdGroupsSDAttributedSame amzAdvReportAdGroupsSDAttributedSame = new AmzAdvReportAdGroupsSDAttributedSame();
				AmzAdvReportAdGroupsSDAttributedNew amzAdvReportAdGroupsSDAttributedNew = new AmzAdvReportAdGroupsSDAttributedNew();
				amzAdvReportAdGroupsSD.setProfileid(request.getProfileid());
				amzAdvReportAdGroupsSD.setBydate(request.getByday());
				amzAdvReportAdGroupsSD.setAdgroupid(item.getBigInteger("adGroupId"));
				amzAdvReportAdGroupsSD.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvReportAdGroupsSD.setProfileid(request.getProfileid());
				amzAdvReportAdGroupsSD.setBydate(request.getByday());

				amzAdvReportAdGroupsSD.setClicks(item.getInteger("clicks"));
				amzAdvReportAdGroupsSD.setImpressions(item.getInteger("impressions"));
				amzAdvReportAdGroupsSD.setCost(item.getBigDecimal("cost"));
				amzAdvReportAdGroupsSD.setOpttime(new Date());
				
				amzAdvReportAdGroupsSDAttributed.setBydate(amzAdvReportAdGroupsSD.getBydate());
				amzAdvReportAdGroupsSDAttributed.setAdgroupid(amzAdvReportAdGroupsSD.getAdgroupid());
				amzAdvReportAdGroupsSDAttributed.setAttributedsales1d(item.getBigDecimal("attributedSales1d"));
				amzAdvReportAdGroupsSDAttributed.setAttributedsales7d(item.getBigDecimal("attributedSales7d"));
				amzAdvReportAdGroupsSDAttributed.setAttributedsales14d(item.getBigDecimal("attributedSales14d"));
				amzAdvReportAdGroupsSDAttributed.setAttributedsales30d(item.getBigDecimal("attributedSales30d"));



				amzAdvReportAdGroupsSDAttributed.setAttributedunitsordered1d(item.getInteger("attributedUnitsOrdered1d"));
				amzAdvReportAdGroupsSDAttributed.setAttributedunitsordered7d(item.getInteger("attributedUnitsOrdered7d"));
				amzAdvReportAdGroupsSDAttributed.setAttributedunitsordered14d(item.getInteger("attributedUnitsOrdered14d"));
				amzAdvReportAdGroupsSDAttributed.setAttributedunitsordered30d(item.getInteger("attributedUnitsOrdered30d"));

				amzAdvReportAdGroupsSDAttributed.setAttributedconversions1d(item.getInteger("attributedConversions1d"));
				amzAdvReportAdGroupsSDAttributed.setAttributedconversions7d(item.getInteger("attributedConversions7d"));
				amzAdvReportAdGroupsSDAttributed.setAttributedconversions14d(item.getInteger("attributedConversions14d"));
				amzAdvReportAdGroupsSDAttributed.setAttributedconversions30d(item.getInteger("attributedConversions30d"));
				
				
				amzAdvReportAdGroupsSDAttributedSame.setBydate(amzAdvReportAdGroupsSD.getBydate());
				amzAdvReportAdGroupsSDAttributedSame.setAdgroupid(amzAdvReportAdGroupsSD.getAdgroupid());
				
				amzAdvReportAdGroupsSDAttributedSame.setAttributedsales1dsamesku(item.getBigDecimal("attributedSales1dSameSKU"));
				amzAdvReportAdGroupsSDAttributedSame.setAttributedsales7dsamesku(item.getBigDecimal("attributedSales7dSameSKU"));
				amzAdvReportAdGroupsSDAttributedSame.setAttributedsales14dsamesku(item.getBigDecimal("attributedSales14dSameSKU"));
				amzAdvReportAdGroupsSDAttributedSame.setAttributedsales30dsamesku(item.getBigDecimal("attributedSales30dSameSKU"));

				amzAdvReportAdGroupsSDAttributedSame.setAttributedconversions1dsamesku(item.getInteger("attributedConversions1dSameSKU"));
				amzAdvReportAdGroupsSDAttributedSame.setAttributedconversions7dsamesku(item.getInteger("attributedConversions7dSameSKU"));
				amzAdvReportAdGroupsSDAttributedSame.setAttributedconversions14dsamesku(item.getInteger("attributedConversions14dSameSKU"));
				amzAdvReportAdGroupsSDAttributedSame.setAttributedconversions30dsamesku(item.getInteger("attributedConversions30dSameSKU"));
			 
				amzAdvReportAdGroupsSDAttributedNew.setBydate(amzAdvReportAdGroupsSD.getBydate());
				amzAdvReportAdGroupsSDAttributedNew.setAdgroupid(amzAdvReportAdGroupsSD.getAdgroupid());
				
				amzAdvReportAdGroupsSDAttributedNew.setAttributedOrdersNewToBrand14d(item.getInteger("attributedOrdersNewToBrand14d"));
				amzAdvReportAdGroupsSDAttributedNew.setAttributedSalesNewToBrand14d(item.getBigDecimal("attributedSalesNewToBrand14d"));
				amzAdvReportAdGroupsSDAttributedNew.setAttributedUnitsOrderedNewToBrand14d(item.getInteger("attributedUnitsOrderedNewToBrand14d"));
				
				amzAdvReportAdGroupsSDAttributedView.setAdgroupid(amzAdvReportAdGroupsSD.getAdgroupid());
				amzAdvReportAdGroupsSDAttributedView.setBydate(amzAdvReportAdGroupsSD.getBydate());
				amzAdvReportAdGroupsSDAttributedView.setViewAttributedConversions14d(item.getInteger("viewAttributedConversions14d"));
				amzAdvReportAdGroupsSDAttributedView.setViewAttributedSales14d(item.getBigDecimal("viewAttributedSales14d"));
				amzAdvReportAdGroupsSDAttributedView.setViewAttributedUnitsOrdered14d(item.getInteger("viewAttributedUnitsOrdered14d"));
				amzAdvReportAdGroupsSDAttributedView.setViewImpressions(item.getInteger("viewImpressions"));
				
				list.add(amzAdvReportAdGroupsSD);
				if(!amzAdvReportAdGroupsSDAttributed.isZero()) {
					listAttributed.add(amzAdvReportAdGroupsSDAttributed);
				}
				if(!amzAdvReportAdGroupsSDAttributedView.isZero()) {
					listAttributedView.add(amzAdvReportAdGroupsSDAttributedView);
				}
				if(!amzAdvReportAdGroupsSDAttributedSame.isZero()) {
					listAttributedSame.add(amzAdvReportAdGroupsSDAttributedSame);
				}
				if(!amzAdvReportAdGroupsSDAttributedNew.isZero()) {
					listAttributedNew.add(amzAdvReportAdGroupsSDAttributedNew);
				}
				if (list.size() >= 2000) {
					amzAdvReportAdGroupsSDMapper.insertBatch(list);
					list.clear();
				}
				
				if (listAttributed.size() >= 2000) {
					amzAdvReportAdGroupsSDMapper.insertBatchAttributed(listAttributed);
					listAttributed.clear();
				}
				if (listAttributedView.size() >= 2000) {
					amzAdvReportAdGroupsSDMapper.insertBatchAttributedView(listAttributedView);
					listAttributedView.clear();
				}
				if (listAttributedSame.size() >= 2000) {
					amzAdvReportAdGroupsSDMapper.insertBatchAttributedSame(listAttributedSame);
					listAttributedSame.clear();
				}
				if (listAttributedNew.size() >= 2000) {
					amzAdvReportAdGroupsSDMapper.insertBatchAttributedNew(listAttributedNew);
					listAttributedNew.clear();
				}
				
			}
			if (list.size() > 0) {
				amzAdvReportAdGroupsSDMapper.insertBatch(list);
			}
			if (listAttributed.size() >0) {
				amzAdvReportAdGroupsSDMapper.insertBatchAttributed(listAttributed);
			}
			if (listAttributed.size() >0) {
				amzAdvReportAdGroupsSDMapper.insertBatchAttributedView(listAttributedView);
			}
			if (listAttributedSame.size() >0) {
				amzAdvReportAdGroupsSDMapper.insertBatchAttributedSame(listAttributedSame);
			}
			if (listAttributedNew.size() >0) {
				amzAdvReportAdGroupsSDMapper.insertBatchAttributedNew(listAttributedNew);
			}
		} finally {
			try {
				if (jsonReader != null) {
					jsonReader.close();
				}
			} catch (Exception e) {
			}
		}
	}

	public   void treatKeywordsAdvertReport(AmzAdvRequest request, JSONReader jsonReader) {
		  List<AmzAdvReportKeywords> list = new LinkedList<AmzAdvReportKeywords>();
		  List<AmzAdvReportKeywordsAttributed> listAttributed = new LinkedList<AmzAdvReportKeywordsAttributed>();
		  List<AmzAdvReportKeywordsAttributedSame> listAttributedSame = new LinkedList<AmzAdvReportKeywordsAttributedSame>();
		try {
			jsonReader.startArray();
			while (jsonReader.hasNext()) {
				String elem = jsonReader.readString();
				JSONObject item = GeneralUtil.getJsonObject(elem);
				AmzAdvReportKeywords amzAdvReportKeyword = new AmzAdvReportKeywords();
				AmzAdvReportKeywordsAttributed amzAdvReportKeywordAttributed = new AmzAdvReportKeywordsAttributed();
				AmzAdvReportKeywordsAttributedSame amzAdvReportKeywordAttributedSame = new AmzAdvReportKeywordsAttributedSame();
				amzAdvReportKeyword.setProfileid(request.getProfileid());
				amzAdvReportKeyword.setBydate(request.getByday());
				amzAdvReportKeyword.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvReportKeyword.setKeywordid(item.getBigInteger("keywordId"));
				amzAdvReportKeyword.setAdgroupid(item.getBigInteger("adGroupId"));

				amzAdvReportKeyword.setClicks(item.getInteger("clicks"));
				amzAdvReportKeyword.setImpressions(item.getInteger("impressions"));
				amzAdvReportKeyword.setCost(item.getBigDecimal("cost"));
				
				amzAdvReportKeywordAttributed.setBydate(amzAdvReportKeyword.getBydate());
				amzAdvReportKeywordAttributed.setKeywordid(amzAdvReportKeyword.getKeywordid());
				amzAdvReportKeywordAttributed.setAttributedsales1d(item.getBigDecimal("attributedSales1d"));
				amzAdvReportKeywordAttributed.setAttributedsales7d(item.getBigDecimal("attributedSales7d"));
				amzAdvReportKeywordAttributed.setAttributedsales14d(item.getBigDecimal("attributedSales14d"));
				amzAdvReportKeywordAttributed.setAttributedsales30d(item.getBigDecimal("attributedSales30d"));


				amzAdvReportKeywordAttributed.setAttributedunitsordered1d(item.getInteger("attributedUnitsOrdered1d"));
				amzAdvReportKeywordAttributed.setAttributedunitsordered7d(item.getInteger("attributedUnitsOrdered7d"));
				amzAdvReportKeywordAttributed.setAttributedunitsordered14d(item.getInteger("attributedUnitsOrdered14d"));
				amzAdvReportKeywordAttributed.setAttributedunitsordered30d(item.getInteger("attributedUnitsOrdered30d"));

				amzAdvReportKeywordAttributed.setAttributedconversions1d(item.getInteger("attributedConversions1d"));
				amzAdvReportKeywordAttributed.setAttributedconversions7d(item.getInteger("attributedConversions7d"));
				amzAdvReportKeywordAttributed.setAttributedconversions14d(item.getInteger("attributedConversions14d"));
				amzAdvReportKeywordAttributed.setAttributedconversions30d(item.getInteger("attributedConversions30d"));

				
				amzAdvReportKeywordAttributedSame.setBydate(amzAdvReportKeyword.getBydate());
				amzAdvReportKeywordAttributedSame.setKeywordid(amzAdvReportKeyword.getKeywordid());
				amzAdvReportKeywordAttributedSame.setAttributedsales1dsamesku(item.getBigDecimal("attributedSales1dSameSKU"));
				amzAdvReportKeywordAttributedSame.setAttributedsales7dsamesku(item.getBigDecimal("attributedSales7dSameSKU"));
				amzAdvReportKeywordAttributedSame.setAttributedsales14dsamesku(item.getBigDecimal("attributedSales14dSameSKU"));
				amzAdvReportKeywordAttributedSame.setAttributedsales30dsamesku(item.getBigDecimal("attributedSales30dSameSKU"));
				
				amzAdvReportKeywordAttributedSame.setAttributedconversions1dsamesku(item.getInteger("attributedConversions1dSameSKU"));
				amzAdvReportKeywordAttributedSame.setAttributedconversions7dsamesku(item.getInteger("attributedConversions7dSameSKU"));
				amzAdvReportKeywordAttributedSame.setAttributedconversions14dsamesku(item.getInteger("attributedConversions14dSameSKU"));
				amzAdvReportKeywordAttributedSame.setAttributedconversions30dsamesku(item.getInteger("attributedConversions30dSameSKU"));
				
				amzAdvReportKeywordAttributedSame.setAttributedUnitsOrdered1dSameSKU(item.getInteger("attributedUnitsOrdered1dSameSKU"));
				amzAdvReportKeywordAttributedSame.setAttributedUnitsOrdered7dSameSKU(item.getInteger("attributedUnitsOrdered7dSameSKU"));
				amzAdvReportKeywordAttributedSame.setAttributedUnitsOrdered14dSameSKU(item.getInteger("attributedUnitsOrdered14dSameSKU"));
				amzAdvReportKeywordAttributedSame.setAttributedUnitsOrdered30dSameSKU(item.getInteger("attributedUnitsOrdered30dSameSKU"));
				
				if(!amzAdvReportKeywordAttributed.isZero()) {
					listAttributed.add(amzAdvReportKeywordAttributed);
				}
				if(!amzAdvReportKeywordAttributedSame.isZero()) {
					listAttributedSame.add(amzAdvReportKeywordAttributedSame);
				}
				if ((amzAdvReportKeyword.getClicks() == null || amzAdvReportKeyword.getClicks() == 0)
						&& (amzAdvReportKeyword.getImpressions() == null || amzAdvReportKeyword.getImpressions() == 0)
						&& (amzAdvReportKeyword.getCost() == null || amzAdvReportKeyword.getCost().compareTo(new BigDecimal("0")) == 0)
						) {
					continue;
				}
				list.add(amzAdvReportKeyword);

				if (list.size() >= 2000) {
					amzAdvReportKeywordsMapper.insertBatch(list);
					list.clear();
				}
				if (listAttributed.size() >= 2000) {
					amzAdvReportKeywordsMapper.insertBatchAttributed(listAttributed);
					listAttributed.clear();
				}
				if (listAttributedSame.size() >= 2000) {
					amzAdvReportKeywordsMapper.insertBatchAttributedSame(listAttributedSame);
					listAttributedSame.clear();
				}
			}
			if (list.size() > 0) {
				amzAdvReportKeywordsMapper.insertBatch(list);
			}
			if (listAttributed.size() >0) {
				amzAdvReportKeywordsMapper.insertBatchAttributed(listAttributed);
			}
			if (listAttributedSame.size() >0) {
				amzAdvReportKeywordsMapper.insertBatchAttributedSame(listAttributedSame);
			}
		}catch(Exception e) {
			e.printStackTrace();
		} 
	}

	public   void treatAsinsAdvertReport(AmzAdvRequest request, JSONReader jsonReader) {
		final List<AmzAdvReportAsins> list = new LinkedList<AmzAdvReportAsins>();
		try {
			jsonReader.startArray();
			while (jsonReader.hasNext()) {
				String elem = jsonReader.readString();
				JSONObject item = GeneralUtil.getJsonObject(elem);
				AmzAdvReportAsins amzAdvReportAsins = new AmzAdvReportAsins();
				amzAdvReportAsins.setProfileid(request.getProfileid());
				amzAdvReportAsins.setBydate(request.getByday());
				amzAdvReportAsins.setCampaignid(item.getBigInteger("campaignId"));
				BigInteger keywordId = item.getBigInteger("keywordId");
				if(keywordId.compareTo(new BigInteger("0"))==-1){
					keywordId = keywordId.multiply(new BigInteger("-1"));
				}
				amzAdvReportAsins.setKeywordid(keywordId);
				amzAdvReportAsins.setAdgroupid(item.getBigInteger("adGroupId"));
				amzAdvReportAsins.setTargetid(item.getBigInteger("targetId"));
				String asin = item.getString("asin");
				if(asin==null){
					continue;
				}
				amzAdvReportAsins.setAsin(asin);
				amzAdvReportAsins.setSku(item.getString("sku"));
				amzAdvReportAsins.setOtherasin(item.getString("otherAsin"));
				amzAdvReportAsins.setOpttime(new Date());
				amzAdvReportAsins.setAttributedUnitsOrdered1d(item.getInteger("attributedUnitsOrdered1d"));
				amzAdvReportAsins.setAttributedUnitsOrdered7d(item.getInteger("attributedUnitsOrdered7d"));
				amzAdvReportAsins.setAttributedUnitsOrdered14d(item.getInteger("attributedUnitsOrdered14d"));
				amzAdvReportAsins.setAttributedUnitsOrdered30d(item.getInteger("attributedUnitsOrdered30d"));
				

				amzAdvReportAsins.setAttributedsales1dothersku(item.getBigDecimal("attributedSales1dOtherSKU"));
				amzAdvReportAsins.setAttributedsales7dothersku(item.getBigDecimal("attributedSales7dOtherSKU"));
				amzAdvReportAsins.setAttributedsales14dothersku(item.getBigDecimal("attributedSales14dOtherSKU"));
				amzAdvReportAsins.setAttributedsales30dothersku(item.getBigDecimal("attributedSales30dOtherSKU"));

				amzAdvReportAsins.setAttributedunitsordered1dothersku(item.getInteger("attributedUnitsOrdered1dOtherSKU"));
				amzAdvReportAsins.setAttributedunitsordered7dothersku(item.getInteger("attributedUnitsOrdered7dOtherSKU"));
				amzAdvReportAsins.setAttributedunitsordered14dothersku(item.getInteger("attributedUnitsOrdered14dOtherSKU"));
				amzAdvReportAsins.setAttributedunitsordered30dothersku(item.getInteger("attributedUnitsOrdered30dOtherSKU"));
                if(!amzAdvReportAsins.isZero()) {
                	list.add(amzAdvReportAsins);
                }
				if (list.size() >= 2000) {
					amzAdvReportAsinsMapper.insertBatch(list);
					list.clear();
				}
			}
			if (list.size() > 0) {
				amzAdvReportAsinsMapper.insertBatch(list);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}  
	}

	public   void treatKeywordsQuryAdvertReport(AmzAdvRequest request, JSONReader jsonReader) {
		  List<AmzAdvReportKeywordsQuery> list = new LinkedList<AmzAdvReportKeywordsQuery>();
		  List<AmzAdvReportKeywordsQueryAttributed> listAttributed = new LinkedList<AmzAdvReportKeywordsQueryAttributed>();
		  List<AmzAdvReportKeywordsQueryAttributedSame> listAttributedSame = new LinkedList<AmzAdvReportKeywordsQueryAttributedSame>();
		try {
			jsonReader.startArray();
			int line = 0;
			while (jsonReader.hasNext()) {
				String elem = jsonReader.readString();
				JSONObject item = GeneralUtil.getJsonObject(elem);
				AmzAdvReportKeywordsQuery amzAdvReportKeywordsQury = new AmzAdvReportKeywordsQuery();
				AmzAdvReportKeywordsQueryAttributed amzAdvReportKeywordsQuryAttributed = new AmzAdvReportKeywordsQueryAttributed();
				AmzAdvReportKeywordsQueryAttributedSame amzAdvReportKeywordsQuryAttributedSame = new AmzAdvReportKeywordsQueryAttributedSame();
				String query=EmojiFilterUtils.filterEmoji(item.getString("query"));
				AmzAdvRptQuery queryobj = loadQuery(query);
				amzAdvReportKeywordsQury.setProfileid(request.getProfileid());
				amzAdvReportKeywordsQury.setBydate(request.getByday());
				amzAdvReportKeywordsQury.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvReportKeywordsQury.setKeywordid(item.getBigInteger("keywordId"));
				amzAdvReportKeywordsQury.setAdgroupid(item.getBigInteger("adGroupId"));
				amzAdvReportKeywordsQury.setQueryid(new BigInteger(queryobj.getId()));
				
				amzAdvReportKeywordsQury.setClicks(item.getInteger("clicks"));
				amzAdvReportKeywordsQury.setImpressions(item.getInteger("impressions"));
				amzAdvReportKeywordsQury.setCost(item.getBigDecimal("cost"));

				amzAdvReportKeywordsQuryAttributed.setKeywordid(amzAdvReportKeywordsQury.getKeywordid());
				amzAdvReportKeywordsQuryAttributed.setBydate(amzAdvReportKeywordsQury.getBydate());
				amzAdvReportKeywordsQuryAttributed.setQueryid(amzAdvReportKeywordsQury.getQueryid());
				amzAdvReportKeywordsQuryAttributed.setAttributedsales1d(item.getBigDecimal("attributedSales1d"));
				amzAdvReportKeywordsQuryAttributed.setAttributedsales7d(item.getBigDecimal("attributedSales7d"));
				amzAdvReportKeywordsQuryAttributed.setAttributedsales14d(item.getBigDecimal("attributedSales14d"));
				amzAdvReportKeywordsQuryAttributed.setAttributedsales30d(item.getBigDecimal("attributedSales30d"));
			
				amzAdvReportKeywordsQuryAttributed.setAttributedunitsordered1d(item.getInteger("attributedUnitsOrdered1d"));
				amzAdvReportKeywordsQuryAttributed.setAttributedunitsordered7d(item.getInteger("attributedUnitsOrdered7d"));
				amzAdvReportKeywordsQuryAttributed.setAttributedunitsordered14d(item.getInteger("attributedUnitsOrdered14d"));
				amzAdvReportKeywordsQuryAttributed.setAttributedunitsordered30d(item.getInteger("attributedUnitsOrdered30d"));
				amzAdvReportKeywordsQuryAttributed.setAttributedconversions1d(item.getInteger("attributedConversions1d"));
				amzAdvReportKeywordsQuryAttributed.setAttributedconversions7d(item.getInteger("attributedConversions7d"));
				amzAdvReportKeywordsQuryAttributed.setAttributedconversions14d(item.getInteger("attributedConversions14d"));
				amzAdvReportKeywordsQuryAttributed.setAttributedconversions30d(item.getInteger("attributedConversions30d"));
				
				amzAdvReportKeywordsQuryAttributedSame.setKeywordid(amzAdvReportKeywordsQury.getKeywordid());
				amzAdvReportKeywordsQuryAttributedSame.setBydate(amzAdvReportKeywordsQury.getBydate());
				amzAdvReportKeywordsQuryAttributedSame.setQueryid(amzAdvReportKeywordsQury.getQueryid());
				
				amzAdvReportKeywordsQuryAttributedSame.setAttributedsales1dsamesku(item.getBigDecimal("attributedSales1dSameSKU"));
				amzAdvReportKeywordsQuryAttributedSame.setAttributedsales7dsamesku(item.getBigDecimal("attributedSales7dSameSKU"));
				amzAdvReportKeywordsQuryAttributedSame.setAttributedsales14dsamesku(item.getBigDecimal("attributedSales14dSameSKU"));
				amzAdvReportKeywordsQuryAttributedSame.setAttributedsales30dsamesku(item.getBigDecimal("attributedSales30dSameSKU"));
				
				amzAdvReportKeywordsQuryAttributedSame.setAttributedconversions1dsamesku(item.getInteger("attributedConversions1dSameSKU"));
				amzAdvReportKeywordsQuryAttributedSame.setAttributedconversions7dsamesku(item.getInteger("attributedConversions7dSameSKU"));
				amzAdvReportKeywordsQuryAttributedSame.setAttributedconversions14dsamesku(item.getInteger("attributedConversions14dSameSKU"));
				amzAdvReportKeywordsQuryAttributedSame.setAttributedconversions30dsamesku(item.getInteger("attributedConversions30dSameSKU"));

				amzAdvReportKeywordsQuryAttributedSame.setAttributedUnitsOrdered1dSameSKU(item.getInteger("attributedUnitsOrdered1dSameSKU"));
				amzAdvReportKeywordsQuryAttributedSame.setAttributedUnitsOrdered7dSameSKU(item.getInteger("attributedUnitsOrdered7dSameSKU"));
				amzAdvReportKeywordsQuryAttributedSame.setAttributedUnitsOrdered14dSameSKU(item.getInteger("attributedUnitsOrdered14dSameSKU"));
				amzAdvReportKeywordsQuryAttributedSame.setAttributedUnitsOrdered30dSameSKU(item.getInteger("attributedUnitsOrdered30dSameSKU"));
				
				list.add(amzAdvReportKeywordsQury);
				if(!amzAdvReportKeywordsQuryAttributed.isZero()) {
					listAttributed.add(amzAdvReportKeywordsQuryAttributed);
				}
				if(!amzAdvReportKeywordsQuryAttributedSame.isZero()) {
					listAttributedSame.add(amzAdvReportKeywordsQuryAttributedSame);
				}
				if (list.size() >= 2000) {
					amzAdvReportKeywordsQuryMapper.insertBatch(list);
					list.clear();
				}
				if(listAttributed.size()>=2000) {
					amzAdvReportKeywordsQuryMapper.insertBatchAttributed(listAttributed);
					listAttributed.clear();
				}
				if(listAttributedSame.size()>=2000) {
					amzAdvReportKeywordsQuryMapper.insertBatchAttributedSame(listAttributedSame);
					listAttributedSame.clear();
				}
				line++;
			}
			if (list.size() > 0) {
				amzAdvReportKeywordsQuryMapper.insertBatch(list);
			}
			if(listAttributed.size()>0) {
				amzAdvReportKeywordsQuryMapper.insertBatchAttributed(listAttributed);
				listAttributed.clear();
			}
			if(listAttributedSame.size()>0) {
				amzAdvReportKeywordsQuryMapper.insertBatchAttributedSame(listAttributedSame);
				listAttributed.clear();
			}
			saveAmzAdvSumAllType(line, request);
		}catch(Exception e) {
			e.printStackTrace();
		} 
	}

	public   void treatKeywordsHsaQuryAdvertReport(AmzAdvRequest request, JSONReader jsonReader) {
		final List<AmzAdvReportKeywordsQueryHsa> list = new LinkedList<AmzAdvReportKeywordsQueryHsa>();
 
		try {
			jsonReader.startArray();
			int line = 0;
			while (jsonReader.hasNext()) {
				String elem = jsonReader.readString();
				JSONObject item = GeneralUtil.getJsonObject(elem);
				AmzAdvReportKeywordsQueryHsa amzAdvReportKeywordsQueryHsa = new AmzAdvReportKeywordsQueryHsa();
		 
				amzAdvReportKeywordsQueryHsa.setProfileid(request.getProfileid());
				amzAdvReportKeywordsQueryHsa.setBydate(request.getByday());
				amzAdvReportKeywordsQueryHsa.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvReportKeywordsQueryHsa.setKeywordid(item.getBigInteger("keywordId"));
				amzAdvReportKeywordsQueryHsa.setAdgroupid(item.getBigInteger("adGroupId"));
//				amzAdvReportKeywordsQueryHsa.setKeywordtext(item.getString("keywordText"));
//				amzAdvReportKeywordsQueryHsa.setAdgroupname(item.getString("adGroupName"));
//				amzAdvReportKeywordsQueryHsa.setCampaignname(item.getString("campaignName"));
//				amzAdvReportKeywordsQueryHsa.setMatchtype(item.getString("matchType"));
				amzAdvReportKeywordsQueryHsa.setOpttime(new Date());
				String query = item.getString("query");
				AmzAdvRptQuery queryobj = this.loadQuery(query);
				amzAdvReportKeywordsQueryHsa.setQueryid(new BigInteger(queryobj.getId()));
				amzAdvReportKeywordsQueryHsa.setClicks(item.getInteger("clicks"));
				amzAdvReportKeywordsQueryHsa.setImpressions(item.getInteger("impressions"));
				amzAdvReportKeywordsQueryHsa.setCost(item.getBigDecimal("cost"));

				amzAdvReportKeywordsQueryHsa.setAttributedsales14d(item.getBigDecimal("attributedSales14d"));
				amzAdvReportKeywordsQueryHsa.setAttributedconversions14d(item.getInteger("attributedConversions14d"));
		        
				list.add(amzAdvReportKeywordsQueryHsa);
				if (list.size() >= 2000) {
					amzAdvReportKeywordsQueryHsaMapper.insertBatch(list); 
					list.clear();
				}
				 
			
				line++;
			}
			if (list.size() > 0) {
				amzAdvReportKeywordsQueryHsaMapper.insertBatch(list);
			}
		 
			saveAmzAdvSumAllType(line, request);
		} catch(Exception e) {
			e.printStackTrace();
		}  
	}
	
	public AmzAdvRptQuery loadQuery(String query) {
		Example example = new Example(AmzAdvRptQuery.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("query", query);
		List<AmzAdvRptQuery> list = amzAdvRptQueryMapper.selectByExampleAndRowBounds(example, new RowBounds(0, 1));
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			 AmzAdvRptQuery myquery =new AmzAdvRptQuery();
			 myquery.setQuery(query);
			 try {
				 int result = amzAdvRptQueryMapper.insert(myquery);
				 if(result>0) {
					 return myquery;
				 }
			 }catch(Exception e) {
				return amzAdvRptQueryMapper.selectByName(query);
			 }
			
			return null;
		}
	}
	
	public   void treatCampaignsPlaceAdvertReport(AmzAdvRequest request, JSONReader jsonReader) {
		  List<AmzAdvReportCompaignsPlace> list = new LinkedList<AmzAdvReportCompaignsPlace>();
		  List<AmzAdvReportCompaignsPlaceAttributed> listAttributed = new LinkedList<AmzAdvReportCompaignsPlaceAttributed>();
		  List<AmzAdvReportCompaignsPlaceAttributedSame> listAttributedSame = new LinkedList<AmzAdvReportCompaignsPlaceAttributedSame>();
		try {
			jsonReader.startArray();
			while (jsonReader.hasNext()) {
				String elem = jsonReader.readString();
				JSONObject item = GeneralUtil.getJsonObject(elem);
				AmzAdvReportCompaignsPlace amzAdvReportCompaigns = new AmzAdvReportCompaignsPlace();
				AmzAdvReportCompaignsPlaceAttributed amzAdvReportCompaignsAttributed = new AmzAdvReportCompaignsPlaceAttributed();
				AmzAdvReportCompaignsPlaceAttributedSame amzAdvReportCompaignsAttributedSame = new AmzAdvReportCompaignsPlaceAttributedSame();
				amzAdvReportCompaigns.setProfileid(request.getProfileid());
				amzAdvReportCompaigns.setBydate(request.getByday());
				amzAdvReportCompaigns.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvReportCompaigns.setCampaignbudget(item.getBigDecimal("campaignBudget"));
				String placement = item.getString("placement");
				if(placement==null){
					continue;
				}
				AmzAdvReportPlacement objplacement = amzAdvReportPlacementService.loadIDByPlacementName(placement); 
				amzAdvReportCompaigns.setPlacementid(objplacement.getId());
				amzAdvReportCompaigns.setClicks(item.getInteger("clicks"));
				amzAdvReportCompaigns.setImpressions(item.getInteger("impressions"));
				amzAdvReportCompaigns.setCost(item.getBigDecimal("cost"));
				amzAdvReportCompaigns.setOpttime(new Date());
				
				amzAdvReportCompaignsAttributed.setCampaignid(amzAdvReportCompaigns.getCampaignid());
				amzAdvReportCompaignsAttributed.setBydate(amzAdvReportCompaigns.getBydate());
				amzAdvReportCompaignsAttributed.setPlacementid(amzAdvReportCompaigns.getPlacementid());
				amzAdvReportCompaignsAttributed.setAttributedsales1d(item.getBigDecimal("attributedSales1d"));
				amzAdvReportCompaignsAttributed.setAttributedsales7d(item.getBigDecimal("attributedSales7d"));
				amzAdvReportCompaignsAttributed.setAttributedsales14d(item.getBigDecimal("attributedSales14d"));
				amzAdvReportCompaignsAttributed.setAttributedsales30d(item.getBigDecimal("attributedSales30d"));


				amzAdvReportCompaignsAttributed.setAttributedunitsordered1d(item.getInteger("attributedUnitsOrdered1d"));
				amzAdvReportCompaignsAttributed.setAttributedunitsordered7d(item.getInteger("attributedUnitsOrdered7d"));
				amzAdvReportCompaignsAttributed.setAttributedunitsordered14d(item.getInteger("attributedUnitsOrdered14d"));
				amzAdvReportCompaignsAttributed.setAttributedunitsordered30d(item.getInteger("attributedUnitsOrdered30d"));

				amzAdvReportCompaignsAttributed.setAttributedconversions1d(item.getInteger("attributedConversions1d"));
				amzAdvReportCompaignsAttributed.setAttributedconversions7d(item.getInteger("attributedConversions7d"));
				amzAdvReportCompaignsAttributed.setAttributedconversions14d(item.getInteger("attributedConversions14d"));
				amzAdvReportCompaignsAttributed.setAttributedconversions30d(item.getInteger("attributedConversions30d"));

				
				
				amzAdvReportCompaignsAttributedSame.setCampaignid(amzAdvReportCompaigns.getCampaignid());
				amzAdvReportCompaignsAttributedSame.setBydate(amzAdvReportCompaigns.getBydate());
				amzAdvReportCompaignsAttributedSame.setPlacementid(amzAdvReportCompaigns.getPlacementid());
				amzAdvReportCompaignsAttributedSame.setAttributedsales1dsamesku(item.getBigDecimal("attributedSales1dSameSKU"));
				amzAdvReportCompaignsAttributedSame.setAttributedsales7dsamesku(item.getBigDecimal("attributedSales7dSameSKU"));
				amzAdvReportCompaignsAttributedSame.setAttributedsales14dsamesku(item.getBigDecimal("attributedSales14dSameSKU"));
				amzAdvReportCompaignsAttributedSame.setAttributedsales30dsamesku(item.getBigDecimal("attributedSales30dSameSKU"));
				
				amzAdvReportCompaignsAttributedSame.setAttributedconversions1dsamesku(item.getInteger("attributedConversions1dSameSKU"));
				amzAdvReportCompaignsAttributedSame.setAttributedconversions7dsamesku(item.getInteger("attributedConversions7dSameSKU"));
				amzAdvReportCompaignsAttributedSame.setAttributedconversions14dsamesku(item.getInteger("attributedConversions14dSameSKU"));
				amzAdvReportCompaignsAttributedSame.setAttributedconversions30dsamesku(item.getInteger("attributedConversions30dSameSKU"));

				if(!amzAdvReportCompaignsAttributed.isZero()) {
					listAttributed.add(amzAdvReportCompaignsAttributed);
				}
				if(!amzAdvReportCompaignsAttributedSame.isZero()) {
					listAttributedSame.add(amzAdvReportCompaignsAttributedSame);
				}
				list.add(amzAdvReportCompaigns);
				if (list.size() >= 2000) {
					amzAdvReportCompaignsPlaceMapper.insertBatch(list);
					list.clear();
				}
				if (listAttributed.size() >= 2000) {
					amzAdvReportCompaignsPlaceMapper.insertBatchAttributed(listAttributed);
					listAttributed.clear();
				}
				if (listAttributedSame.size() >= 2000) {
					amzAdvReportCompaignsPlaceMapper.insertBatchAttributedSame(listAttributedSame);
					listAttributedSame.clear();
				}
			}
			if (list.size() > 0) {
				amzAdvReportCompaignsPlaceMapper.insertBatch(list);
			}
			if (listAttributed.size() > 0) {
				amzAdvReportCompaignsPlaceMapper.insertBatchAttributed(listAttributed);
			}
			if (listAttributedSame.size() > 0) {
				amzAdvReportCompaignsPlaceMapper.insertBatchAttributedSame(listAttributedSame);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public   void treatCampaignsPlaceHsaAdvertReport(AmzAdvRequest request, JSONReader jsonReader) {
		final List<AmzAdvReportCampaignsPlaceHsa> list = new LinkedList<AmzAdvReportCampaignsPlaceHsa>();
		final List<AmzAdvReportCampaignsPlaceHsaAttributed> listAttributed = new LinkedList<AmzAdvReportCampaignsPlaceHsaAttributed>();
		final List<AmzAdvReportCampaignsPlaceHsaBrand> listBrand = new LinkedList<AmzAdvReportCampaignsPlaceHsaBrand>();
		final List<AmzAdvReportCampaignsPlaceHsaVideo> listVideo = new LinkedList<AmzAdvReportCampaignsPlaceHsaVideo>();
		try {
			jsonReader.startArray();
			while (jsonReader.hasNext()) {
				String elem = jsonReader.readString();
				JSONObject item = GeneralUtil.getJsonObject(elem);
				AmzAdvReportCampaignsPlaceHsa amzAdvReportHasCampaignPlace = new AmzAdvReportCampaignsPlaceHsa();
				AmzAdvReportCampaignsPlaceHsaAttributed amzAdvReportCampaignsPlaceHsaAttributed=new AmzAdvReportCampaignsPlaceHsaAttributed();
				AmzAdvReportCampaignsPlaceHsaBrand amzAdvReportCampaignsPlaceHsaBrand=null;
				AmzAdvReportCampaignsPlaceHsaVideo amzAdvReportCampaignsPlaceHsaVideo=null;
				amzAdvReportHasCampaignPlace.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvReportHasCampaignPlace.setCampaignbudget(item.getBigDecimal("campaignBudget"));
				String placement = item.getString("placement");
				if(placement==null){
					continue;
				}
				AmzAdvReportPlacement objplacement = amzAdvReportPlacementService.loadIDByPlacementName(placement);
				amzAdvReportHasCampaignPlace.setPlacementid(objplacement.getId());
				amzAdvReportHasCampaignPlace.setBydate(request.getByday());
				amzAdvReportHasCampaignPlace.setProfileid(request.getProfileid());
				amzAdvReportHasCampaignPlace.setOpttime(new Date());
				amzAdvReportHasCampaignPlace.setClicks(item.getInteger("clicks"));
				amzAdvReportHasCampaignPlace.setImpressions(item.getInteger("impressions"));
				amzAdvReportHasCampaignPlace.setCost(item.getBigDecimal("cost"));

				amzAdvReportCampaignsPlaceHsaAttributed.setAttributedsales14d(item.getBigDecimal("attributedSales14d"));
				amzAdvReportCampaignsPlaceHsaAttributed.setAttributedsales14dsamesku(item.getBigDecimal("attributedSales14dSameSKU"));
				amzAdvReportCampaignsPlaceHsaAttributed.setAttributedconversions14d(item.getInteger("attributedConversions14d"));
				amzAdvReportCampaignsPlaceHsaAttributed.setAttributedconversions14dsamesku(item.getInteger("attributedConversions14dSameSKU"));
				amzAdvReportCampaignsPlaceHsaAttributed.setPlacementid(objplacement.getId());
				amzAdvReportCampaignsPlaceHsaAttributed.setCampaignid(amzAdvReportHasCampaignPlace.getCampaignid());
				amzAdvReportCampaignsPlaceHsaAttributed.setBydate(amzAdvReportHasCampaignPlace.getBydate());
				amzAdvReportCampaignsPlaceHsaAttributed.setOpttime(new Date());
				
				if(!amzAdvReportCampaignsPlaceHsaAttributed.isZero()) {
					listAttributed.add(amzAdvReportCampaignsPlaceHsaAttributed);
				}
				
		        if(request.getCreativeType()!=null&&"video".equals(request.getCreativeType())) {
		        	amzAdvReportCampaignsPlaceHsaVideo=	new AmzAdvReportCampaignsPlaceHsaVideo();
		        	amzAdvReportCampaignsPlaceHsaVideo.setPlacementid(objplacement.getId());
		        	amzAdvReportCampaignsPlaceHsaVideo.setCampaignid(amzAdvReportHasCampaignPlace.getCampaignid());
		        	amzAdvReportCampaignsPlaceHsaVideo.setBydate(amzAdvReportHasCampaignPlace.getBydate());
		        	amzAdvReportCampaignsPlaceHsaVideo.setOpttime(new Date());
		        	amzAdvReportCampaignsPlaceHsaVideo.setViewableImpressions(item.getInteger("viewableImpressions"));
		        	amzAdvReportCampaignsPlaceHsaVideo.setVideoFirstQuartileViews(item.getInteger("videoFirstQuartileViews"));
		        	amzAdvReportCampaignsPlaceHsaVideo.setVideoMidpointViews(item.getInteger("videoMidpointViews"));
                	amzAdvReportCampaignsPlaceHsaVideo.setVideoThirdQuartileViews(item.getInteger("videoThirdQuartileViews"));
                	amzAdvReportCampaignsPlaceHsaVideo.setVideoCompleteViews(item.getInteger("videoCompleteViews"));
                	amzAdvReportCampaignsPlaceHsaVideo.setVideo5SecondViews(item.getInteger("video5SecondViews"));
                	amzAdvReportCampaignsPlaceHsaVideo.setVideo5SecondViewRate(item.getBigDecimal("video5SecondViewRate"));
                	amzAdvReportCampaignsPlaceHsaVideo.setVideoUnmutes(item.getInteger("videoUnmutes"));
                	amzAdvReportCampaignsPlaceHsaVideo.setVtr(item.getBigDecimal("vtr"));
                	amzAdvReportCampaignsPlaceHsaVideo.setVctr(item.getBigDecimal("vctr"));
                	if(!amzAdvReportCampaignsPlaceHsaVideo.iszero()) {
                		listVideo.add(amzAdvReportCampaignsPlaceHsaVideo);
                	}
                }else {
                	amzAdvReportCampaignsPlaceHsaBrand=	new AmzAdvReportCampaignsPlaceHsaBrand();
                	amzAdvReportCampaignsPlaceHsaBrand.setPlacementid(objplacement.getId());
                	amzAdvReportCampaignsPlaceHsaBrand.setCampaignid(amzAdvReportHasCampaignPlace.getCampaignid());
                	amzAdvReportCampaignsPlaceHsaBrand.setBydate(amzAdvReportHasCampaignPlace.getBydate());
                	amzAdvReportCampaignsPlaceHsaBrand.setOpttime(new Date());
                	amzAdvReportCampaignsPlaceHsaBrand.setAttributedDetailPageViewsClicks14d(item.getInteger("attributedDetailPageViewsClicks14d"));
                	amzAdvReportCampaignsPlaceHsaBrand.setAttributedOrdersNewToBrand14d(item.getInteger("attributedOrdersNewToBrand14d"));
                	amzAdvReportCampaignsPlaceHsaBrand.setAttributedOrdersNewToBrandPercentage14d(item.getBigDecimal("attributedOrdersNewToBrandPercentage14d"));
                	amzAdvReportCampaignsPlaceHsaBrand.setAttributedOrderRateNewToBrand14d(item.getInteger("attributedOrderRateNewToBrand14d"));
                	amzAdvReportCampaignsPlaceHsaBrand.setAttributedSalesNewToBrand14d(item.getInteger("attributedSalesNewToBrand14d"));
                	amzAdvReportCampaignsPlaceHsaBrand.setAttributedSalesNewToBrandPercentage14d(item.getBigDecimal("attributedSalesNewToBrandPercentage14d"));
                	amzAdvReportCampaignsPlaceHsaBrand.setAttributedUnitsOrderedNewToBrand14d(item.getInteger("attributedUnitsOrderedNewToBrand14d"));
                	amzAdvReportCampaignsPlaceHsaBrand.setAttributedUnitsOrderedNewToBrandPercentage14d(item.getBigDecimal("attributedUnitsOrderedNewToBrandPercentage14d"));
                	amzAdvReportCampaignsPlaceHsaBrand.setUnitsSold14d(item.getInteger("unitsSold14d"));
                	amzAdvReportCampaignsPlaceHsaBrand.setDpv14d(item.getInteger("dpv14d"));
                	if(!amzAdvReportCampaignsPlaceHsaBrand.iszero()) {
                		listBrand.add(amzAdvReportCampaignsPlaceHsaBrand);
                	}
                }
		        
				list.add(amzAdvReportHasCampaignPlace);
				if (list.size() >= 2000) {
					amzAdvReportCampaignsPlaceHsaMapper.insertBatch(list);
					list.clear();
				}
				if (listAttributed.size() >= 2000) {
					amzAdvReportCampaignsPlaceHsaMapper.insertBatchAttributed(listAttributed);
					listAttributed.clear();
				}
				if (listVideo.size() >= 2000) {
					amzAdvReportCampaignsPlaceHsaMapper.insertBatchVideo(listVideo);
					listVideo.clear();
				}
				if (listBrand.size() >= 2000) {
					amzAdvReportCampaignsPlaceHsaMapper.insertBatchBrand(listBrand);
					listBrand.clear();
				}
			}
			if (list.size() > 0) {
				amzAdvReportCampaignsPlaceHsaMapper.insertBatch(list);
			}
			if (listAttributed.size() > 0) {
				amzAdvReportCampaignsPlaceHsaMapper.insertBatchAttributed(listAttributed);
			}
			if (listVideo.size() > 0) {
				amzAdvReportCampaignsPlaceHsaMapper.insertBatchVideo(listVideo);
			}
			if (listBrand.size() > 0) {
				amzAdvReportCampaignsPlaceHsaMapper.insertBatchBrand(listBrand);
			}
		} catch(Exception e){
			e.printStackTrace();
		} 
	}

	public   void treatCampaignsHsaAdvertReport(AmzAdvRequest request, JSONReader jsonReader) {
		final List<AmzAdvReportCampaignsHsa> list = new LinkedList<AmzAdvReportCampaignsHsa>();
		final List<AmzAdvReportCampaignsHsaAttributed> listAttributed = new LinkedList<AmzAdvReportCampaignsHsaAttributed>();
		final List<AmzAdvReportCampaignsHsaBrand> listBrand = new LinkedList<AmzAdvReportCampaignsHsaBrand>();
		final List<AmzAdvReportCampaignsHsaVideo> listVideo = new LinkedList<AmzAdvReportCampaignsHsaVideo>();
		try {
			AmzAdvProfile profile = amzAdvProfileMapper.selectByPrimaryKey(request.getProfileid());
			jsonReader.startArray();
			Integer clicks = 0;
			String currency = "";
			Integer impressions = 0;
			BigDecimal cost = new BigDecimal("0");
			BigDecimal attributedsales = new BigDecimal("0");
			Integer attributedunitsordered = 0;
			Integer attributedConversions = 0;
			while (jsonReader.hasNext()) {
				String elem = jsonReader.readString();
				JSONObject item = GeneralUtil.getJsonObject(elem);
				AmzAdvReportCampaignsHsa amzAdvReportHasCampaign = new AmzAdvReportCampaignsHsa();
				AmzAdvReportCampaignsHsaAttributed amzAdvReportCampaignsHsaAttributed=new AmzAdvReportCampaignsHsaAttributed();
				
				AmzAdvReportCampaignsHsaVideo amzAdvReportHasCampaignVideo = null;
				AmzAdvReportCampaignsHsaBrand amzAdvReportHasCampaignBrand = null;
				amzAdvReportHasCampaign.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvReportHasCampaign.setCampaignbudget(item.getBigDecimal("campaignBudget"));

				amzAdvReportHasCampaign.setBydate(request.getByday());
				amzAdvReportHasCampaign.setProfileid(request.getProfileid());
				amzAdvReportHasCampaign.setOpttime(new Date());
				amzAdvReportHasCampaign.setClicks(item.getInteger("clicks"));
				amzAdvReportHasCampaign.setImpressions(item.getInteger("impressions"));
				amzAdvReportHasCampaign.setCost(item.getBigDecimal("cost"));

				amzAdvReportCampaignsHsaAttributed.setAttributedsales14d(item.getBigDecimal("attributedSales14d"));
				amzAdvReportCampaignsHsaAttributed.setAttributedsales14dsamesku(item.getBigDecimal("attributedSales14dSameSKU"));
				amzAdvReportCampaignsHsaAttributed.setAttributedconversions14d(item.getInteger("attributedConversions14d"));
				amzAdvReportCampaignsHsaAttributed.setAttributedconversions14dsamesku(item.getInteger("attributedConversions14dSameSKU"));
				amzAdvReportCampaignsHsaAttributed.setCampaignid(amzAdvReportHasCampaign.getCampaignid());
				amzAdvReportCampaignsHsaAttributed.setBydate(amzAdvReportHasCampaign.getBydate());
				
				if(!amzAdvReportCampaignsHsaAttributed.isZero()) {
					listAttributed.add(amzAdvReportCampaignsHsaAttributed);
            	}
				
                if(request.getCreativeType()!=null&&"video".equals(request.getCreativeType())) {
                	amzAdvReportHasCampaignVideo=	new AmzAdvReportCampaignsHsaVideo();
                	amzAdvReportHasCampaignVideo.setCampaignid(amzAdvReportHasCampaign.getCampaignid());
                	amzAdvReportHasCampaignVideo.setBydate(amzAdvReportHasCampaign.getBydate());
                	amzAdvReportHasCampaignVideo.setOpttime(amzAdvReportHasCampaign.getOpttime());
                	amzAdvReportHasCampaignVideo.setViewableImpressions(item.getInteger("viewableImpressions"));
                	amzAdvReportHasCampaignVideo.setVideoFirstQuartileViews(item.getInteger("videoFirstQuartileViews"));
                	amzAdvReportHasCampaignVideo.setVideoMidpointViews(item.getInteger("videoMidpointViews"));
                	amzAdvReportHasCampaignVideo.setVideoThirdQuartileViews(item.getInteger("videoThirdQuartileViews"));
                	amzAdvReportHasCampaignVideo.setVideoCompleteViews(item.getInteger("videoCompleteViews"));
                	amzAdvReportHasCampaignVideo.setVideo5SecondViews(item.getInteger("video5SecondViews"));
                	amzAdvReportHasCampaignVideo.setVideo5SecondViewRate(item.getBigDecimal("video5SecondViewRate"));
                	amzAdvReportHasCampaignVideo.setVideoUnmutes(item.getInteger("videoUnmutes"));
                	amzAdvReportHasCampaignVideo.setVtr(item.getBigDecimal("vtr"));
                	amzAdvReportHasCampaignVideo.setVctr(item.getBigDecimal("vctr"));
                	if(!amzAdvReportHasCampaignVideo.iszero()) {
                		listVideo.add(amzAdvReportHasCampaignVideo);
                	}
                }else {
                	amzAdvReportHasCampaignBrand=	new AmzAdvReportCampaignsHsaBrand();
                	amzAdvReportHasCampaignBrand.setCampaignid(amzAdvReportHasCampaign.getCampaignid());
                	amzAdvReportHasCampaignBrand.setBydate(amzAdvReportHasCampaign.getBydate());
                	amzAdvReportHasCampaignBrand.setOpttime(amzAdvReportHasCampaign.getOpttime());
                	amzAdvReportHasCampaignBrand.setAttributedDetailPageViewsClicks14d(item.getInteger("attributedDetailPageViewsClicks14d"));
                	amzAdvReportHasCampaignBrand.setAttributedOrdersNewToBrand14d(item.getInteger("attributedOrdersNewToBrand14d"));
                	amzAdvReportHasCampaignBrand.setAttributedOrdersNewToBrandPercentage14d(item.getBigDecimal("attributedOrdersNewToBrandPercentage14d"));
                	amzAdvReportHasCampaignBrand.setAttributedOrderRateNewToBrand14d(item.getInteger("attributedOrderRateNewToBrand14d"));
                	amzAdvReportHasCampaignBrand.setAttributedSalesNewToBrand14d(item.getInteger("attributedSalesNewToBrand14d"));
                	amzAdvReportHasCampaignBrand.setAttributedSalesNewToBrandPercentage14d(item.getBigDecimal("attributedSalesNewToBrandPercentage14d"));
                	amzAdvReportHasCampaignBrand.setAttributedUnitsOrderedNewToBrand14d(item.getInteger("attributedUnitsOrderedNewToBrand14d"));
                	amzAdvReportHasCampaignBrand.setAttributedUnitsOrderedNewToBrandPercentage14d(item.getBigDecimal("attributedUnitsOrderedNewToBrandPercentage14d"));
                	amzAdvReportHasCampaignBrand.setUnitsSold14d(item.getInteger("unitsSold14d"));
                	amzAdvReportHasCampaignBrand.setDpv14d(item.getInteger("dpv14d"));
                	if(!amzAdvReportHasCampaignBrand.iszero()) {
                		listBrand.add(amzAdvReportHasCampaignBrand);
                	}
                }
				
				
				list.add(amzAdvReportHasCampaign);
				clicks = clicks + amzAdvReportHasCampaign.getClicks();
				cost = cost.add(amzAdvReportHasCampaign.getCost());
				
				if (currency == null && StringUtil.isNotEmpty(profile.getCurrencycode())) {
					currency = profile.getCurrencycode();
				}
				impressions = impressions + amzAdvReportHasCampaign.getImpressions();
				if(amzAdvReportCampaignsHsaAttributed.getAttributedsales14d() != null) {
					attributedsales = attributedsales.add(amzAdvReportCampaignsHsaAttributed.getAttributedsales14d());
				}
				if(amzAdvReportCampaignsHsaAttributed.getAttributedsales14d() != null) {
					attributedunitsordered = attributedunitsordered + 
							(amzAdvReportCampaignsHsaAttributed.getAttributedsales14d()!=null?amzAdvReportCampaignsHsaAttributed.getAttributedsales14d().intValue():0);
				}
				if (amzAdvReportCampaignsHsaAttributed.getAttributedconversions14d() != null) {
					attributedConversions = attributedConversions +
							(amzAdvReportCampaignsHsaAttributed.getAttributedconversions14d()!=null?amzAdvReportCampaignsHsaAttributed.getAttributedconversions14d().intValue():0);
				}
				if (list.size() >= 2000) {
					amzAdvReportCampaignsHsaMapper.insertBatch(list);
					list.clear();
				}
				if (listAttributed.size() >= 2000) {
					amzAdvReportCampaignsHsaMapper.insertBatchAttributed(listAttributed);
					listBrand.clear();
				}
				if (listVideo.size() >= 2000) {
					amzAdvReportCampaignsHsaMapper.insertBatchVideo(listVideo);
					listVideo.clear();
				}
				if (listBrand.size() >= 2000) {
					amzAdvReportCampaignsHsaMapper.insertBatchBrand(listBrand);
					listBrand.clear();
				}
			
			}
			if (list.size() > 0) {
				amzAdvReportCampaignsHsaMapper.insertBatch(list);
			}
			if (listAttributed.size() > 0) {
				amzAdvReportCampaignsHsaMapper.insertBatchAttributed(listAttributed);
			}
			if (listVideo.size() > 0) {
				amzAdvReportCampaignsHsaMapper.insertBatchVideo(listVideo);
			}
			if (listBrand.size() > 0) {
				amzAdvReportCampaignsHsaMapper.insertBatchBrand(listBrand);
			}
			AmzAdvSumProductAds record = new AmzAdvSumProductAds();
			record.setProfileid(profile.getId());
			record.setByday(request.getByday());
			record.setCurrency(currency);
			record.setCost(cost);
			record.setClicks(clicks);
            if(request.getCreativeType()!=null&&"video".equals(request.getCreativeType())) {
            	record.setCtype("sv");
            }else {
            	record.setCtype("sb");
            }
			record.setImpressions(impressions);
			record.setAttributedunitsordered(attributedunitsordered);
			record.setAttributedConversions(attributedConversions);
			record.setAttributedsales(attributedsales);
			AmzAdvSumProductAds old = amzAdvSumProductAdsMapper.selectByKey(record);
			if (old != null) {
				amzAdvSumProductAdsMapper.updateByKey(record);
			} else {
				amzAdvSumProductAdsMapper.insert(record);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}  
	}

	public   void treatKeywordsHsaAdvertReport(AmzAdvRequest request, JSONReader jsonReader) {
		final List<AmzAdvReportKeywordsHsa> list = new LinkedList<AmzAdvReportKeywordsHsa>();
		final List<AmzAdvReportKeywordsHsaAttributed> listAttributed = new LinkedList<AmzAdvReportKeywordsHsaAttributed>();
		final List<AmzAdvReportKeywordsHsaBrand> listBrand = new LinkedList<AmzAdvReportKeywordsHsaBrand>();
		final List<AmzAdvReportKeywordsHsaVideo> listVideo = new LinkedList<AmzAdvReportKeywordsHsaVideo>();
		try {
			jsonReader.startArray();
			while (jsonReader.hasNext()) {
				String elem = jsonReader.readString();
				JSONObject item = GeneralUtil.getJsonObject(elem);
				AmzAdvReportKeywordsHsa amzAdvReportHasKeyword = new AmzAdvReportKeywordsHsa();
				AmzAdvReportKeywordsHsaAttributed amzAdvReportKeywordsHsaAttributed = new AmzAdvReportKeywordsHsaAttributed();
				
				AmzAdvReportKeywordsHsaVideo amzAdvReportHasKeywordVideo = null;
				AmzAdvReportKeywordsHsaBrand amzAdvReportHasKeywordBrand = null;
				amzAdvReportHasKeyword.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvReportHasKeyword.setAdgroupid(item.getBigInteger("adGroupId"));
				amzAdvReportHasKeyword.setKeywordid(item.getBigInteger("keywordId"));
				amzAdvReportHasKeyword.setKeywordbid(item.getBigDecimal("keywordBid"));

				amzAdvReportHasKeyword.setBydate(request.getByday());
				amzAdvReportHasKeyword.setProfileid(request.getProfileid());
				amzAdvReportHasKeyword.setOpttime(new Date());
				amzAdvReportHasKeyword.setClicks(item.getInteger("clicks"));
				amzAdvReportHasKeyword.setImpressions(item.getInteger("impressions"));
				amzAdvReportHasKeyword.setCost(item.getBigDecimal("cost"));

				amzAdvReportKeywordsHsaAttributed.setBydate(amzAdvReportHasKeyword.getBydate());
				amzAdvReportKeywordsHsaAttributed.setKeywordid(amzAdvReportHasKeyword.getKeywordid());
				amzAdvReportKeywordsHsaAttributed.setAttributedsales14d(item.getBigDecimal("attributedSales14d"));
				amzAdvReportKeywordsHsaAttributed.setAttributedsales14dsamesku(item.getBigDecimal("attributedSales14dSameSKU"));
				amzAdvReportKeywordsHsaAttributed.setAttributedconversions14d(item.getInteger("attributedConversions14d"));
				amzAdvReportKeywordsHsaAttributed.setAttributedconversions14dsamesku(item.getInteger("attributedConversions14dSameSKU"));
				 if(request.getCreativeType()!=null&&"video".equals(request.getCreativeType())) {
	                	amzAdvReportHasKeywordVideo=	new AmzAdvReportKeywordsHsaVideo();
	                	amzAdvReportHasKeywordVideo.setKeywordid(amzAdvReportHasKeyword.getKeywordid());
	                	amzAdvReportHasKeywordVideo.setBydate(amzAdvReportHasKeyword.getBydate());
	                	amzAdvReportHasKeywordVideo.setOpttime(amzAdvReportHasKeyword.getOpttime());
	                	amzAdvReportHasKeywordVideo.setViewableImpressions(item.getInteger("viewableImpressions"));
	                	amzAdvReportHasKeywordVideo.setVideoFirstQuartileViews(item.getInteger("videoFirstQuartileViews"));
	                	amzAdvReportHasKeywordVideo.setVideoMidpointViews(item.getInteger("videoMidpointViews"));
	                	amzAdvReportHasKeywordVideo.setVideoThirdQuartileViews(item.getInteger("videoThirdQuartileViews"));
	                	amzAdvReportHasKeywordVideo.setVideoCompleteViews(item.getInteger("videoCompleteViews"));
	                	amzAdvReportHasKeywordVideo.setVideo5SecondViews(item.getInteger("video5SecondViews"));
	                	amzAdvReportHasKeywordVideo.setVideo5SecondViewRate(item.getBigDecimal("video5SecondViewRate"));
	                	amzAdvReportHasKeywordVideo.setVideoUnmutes(item.getInteger("videoUnmutes"));
	                	amzAdvReportHasKeywordVideo.setVtr(item.getBigDecimal("vtr"));
	                	amzAdvReportHasKeywordVideo.setVctr(item.getBigDecimal("vctr"));
	                	if(!amzAdvReportHasKeywordVideo.iszero()) {
	                		listVideo.add(amzAdvReportHasKeywordVideo);
	                	}
	                }else {
	                	amzAdvReportHasKeywordBrand=	new AmzAdvReportKeywordsHsaBrand();
	                	amzAdvReportHasKeywordBrand.setKeywordid(amzAdvReportHasKeyword.getKeywordid());
	                	amzAdvReportHasKeywordBrand.setBydate(amzAdvReportHasKeyword.getBydate());
	                	amzAdvReportHasKeywordBrand.setOpttime(amzAdvReportHasKeyword.getOpttime());
	                	amzAdvReportHasKeywordBrand.setAttributedDetailPageViewsClicks14d(item.getInteger("attributedDetailPageViewsClicks14d"));
	                	amzAdvReportHasKeywordBrand.setAttributedOrdersNewToBrand14d(item.getInteger("attributedOrdersNewToBrand14d"));
	                	amzAdvReportHasKeywordBrand.setAttributedOrdersNewToBrandPercentage14d(item.getBigDecimal("attributedOrdersNewToBrandPercentage14d"));
	                	amzAdvReportHasKeywordBrand.setAttributedOrderRateNewToBrand14d(item.getInteger("attributedOrderRateNewToBrand14d"));
	                	amzAdvReportHasKeywordBrand.setAttributedSalesNewToBrand14d(item.getInteger("attributedSalesNewToBrand14d"));
	                	amzAdvReportHasKeywordBrand.setAttributedSalesNewToBrandPercentage14d(item.getBigDecimal("attributedSalesNewToBrandPercentage14d"));
	                	amzAdvReportHasKeywordBrand.setAttributedUnitsOrderedNewToBrand14d(item.getInteger("attributedUnitsOrderedNewToBrand14d"));
	                	amzAdvReportHasKeywordBrand.setAttributedUnitsOrderedNewToBrandPercentage14d(item.getBigDecimal("attributedUnitsOrderedNewToBrandPercentage14d"));
	                	amzAdvReportHasKeywordBrand.setUnitsSold14d(item.getInteger("unitsSold14d"));
	                	amzAdvReportHasKeywordBrand.setDpv14d(item.getInteger("dpv14d"));
	                	if(!amzAdvReportHasKeywordBrand.iszero()) {
	                		listBrand.add(amzAdvReportHasKeywordBrand);
	                	}
	                }
				if(!amzAdvReportKeywordsHsaAttributed.isZero()) {
					listAttributed.add(amzAdvReportKeywordsHsaAttributed);
				}
				
				list.add(amzAdvReportHasKeyword);
				if (list.size() >= 2000) {
					amzAdvReportKeywordsHsaMapper.insertBatch(list);
					list.clear();
				}
				if (listVideo.size() >= 2000) {
					amzAdvReportKeywordsHsaMapper.insertBatchVideo(listVideo);
					listVideo.clear();
				}
				if (listBrand.size() >= 2000) {
					amzAdvReportKeywordsHsaMapper.insertBatchBrand(listBrand);
					listBrand.clear();
				}
				if (listAttributed.size() >= 2000) {
					amzAdvReportKeywordsHsaMapper.insertBatchAttributed(listAttributed);
					listAttributed.clear();
				}
			}
			if (list.size() > 0) {
				amzAdvReportKeywordsHsaMapper.insertBatch(list);
			}
			if (listVideo.size() > 0) {
				amzAdvReportKeywordsHsaMapper.insertBatchVideo(listVideo);
			}
			if (listBrand.size() > 0) {
				amzAdvReportKeywordsHsaMapper.insertBatchBrand(listBrand);
			}
			if (listAttributed.size() > 0) {
				amzAdvReportKeywordsHsaMapper.insertBatchAttributed(listAttributed);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}  
	}
	
	public   void treatProductTargetAdvertReport(AmzAdvRequest request, JSONReader jsonReader) {
		  List<AmzAdvReportProductTargets> list = new LinkedList<AmzAdvReportProductTargets>();
		  List<AmzAdvReportProductTargetsAttributed> listAttributed = new LinkedList<AmzAdvReportProductTargetsAttributed>();
		  List<AmzAdvReportProductTargetsAttributedSame> listAttributedSame = new LinkedList<AmzAdvReportProductTargetsAttributedSame>();
		try {
			jsonReader.startArray();
			while (jsonReader.hasNext()) {
				String elem = jsonReader.readString();
				JSONObject item = GeneralUtil.getJsonObject(elem);
				AmzAdvReportProductTargets amzAdvReportProductTarge = new AmzAdvReportProductTargets();
				AmzAdvReportProductTargetsAttributed amzAdvReportProductTargetsAttributed=new AmzAdvReportProductTargetsAttributed();
				AmzAdvReportProductTargetsAttributedSame amzAdvReportProductTargetsAttributedSame=new AmzAdvReportProductTargetsAttributedSame();
				amzAdvReportProductTarge.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvReportProductTarge.setTargetid(item.getBigInteger("targetId"));
				amzAdvReportProductTarge.setAdgroupid(item.getBigInteger("adGroupId"));

				amzAdvReportProductTarge.setClicks(item.getInteger("clicks"));
				amzAdvReportProductTarge.setImpressions(item.getInteger("impressions"));
				amzAdvReportProductTarge.setCost(item.getBigDecimal("cost"));
				amzAdvReportProductTarge.setProfileid(request.getProfileid());
				amzAdvReportProductTarge.setBydate(request.getByday());
				amzAdvReportProductTarge.setOpttime(new Date());
				amzAdvReportProductTargetsAttributed.setBydate(amzAdvReportProductTarge.getBydate());
				amzAdvReportProductTargetsAttributed.setTargetid(amzAdvReportProductTarge.getTargetid());
				amzAdvReportProductTargetsAttributed.setAttributedsales1d(item.getBigDecimal("attributedSales1d"));
				amzAdvReportProductTargetsAttributed.setAttributedsales7d(item.getBigDecimal("attributedSales7d"));
				amzAdvReportProductTargetsAttributed.setAttributedsales14d(item.getBigDecimal("attributedSales14d"));
				amzAdvReportProductTargetsAttributed.setAttributedsales30d(item.getBigDecimal("attributedSales30d"));


				amzAdvReportProductTargetsAttributed.setAttributedunitsordered1d(item.getInteger("attributedUnitsOrdered1d"));
				amzAdvReportProductTargetsAttributed.setAttributedunitsordered7d(item.getInteger("attributedUnitsOrdered7d"));
				amzAdvReportProductTargetsAttributed.setAttributedunitsordered14d(item.getInteger("attributedUnitsOrdered14d"));
				amzAdvReportProductTargetsAttributed.setAttributedunitsordered30d(item.getInteger("attributedUnitsOrdered30d"));

				amzAdvReportProductTargetsAttributed.setAttributedconversions1d(item.getInteger("attributedConversions1d"));
				amzAdvReportProductTargetsAttributed.setAttributedconversions7d(item.getInteger("attributedConversions7d"));
				amzAdvReportProductTargetsAttributed.setAttributedconversions14d(item.getInteger("attributedConversions14d"));
				amzAdvReportProductTargetsAttributed.setAttributedconversions30d(item.getInteger("attributedConversions30d"));


				amzAdvReportProductTargetsAttributedSame.setBydate(amzAdvReportProductTarge.getBydate());
				amzAdvReportProductTargetsAttributedSame.setTargetid(amzAdvReportProductTarge.getTargetid());
				amzAdvReportProductTargetsAttributedSame.setAttributedsales1dsamesku(item.getBigDecimal("attributedSales1dSameSKU"));
				amzAdvReportProductTargetsAttributedSame.setAttributedsales7dsamesku(item.getBigDecimal("attributedSales7dSameSKU"));
				amzAdvReportProductTargetsAttributedSame.setAttributedsales14dsamesku(item.getBigDecimal("attributedSales14dSameSKU"));
				amzAdvReportProductTargetsAttributedSame.setAttributedsales30dsamesku(item.getBigDecimal("attributedSales30dSameSKU"));
				
				amzAdvReportProductTargetsAttributedSame.setAttributedconversions1dsamesku(item.getInteger("attributedConversions1dSameSKU"));
				amzAdvReportProductTargetsAttributedSame.setAttributedconversions7dsamesku(item.getInteger("attributedConversions7dSameSKU"));
				amzAdvReportProductTargetsAttributedSame.setAttributedconversions14dsamesku(item.getInteger("attributedConversions14dSameSKU"));
				amzAdvReportProductTargetsAttributedSame.setAttributedconversions30dsamesku(item.getInteger("attributedConversions30dSameSKU"));

				amzAdvReportProductTargetsAttributedSame.setAttributedUnitsOrdered1dSameSKU(item.getInteger("attributedUnitsOrdered1dSameSKU"));
				amzAdvReportProductTargetsAttributedSame.setAttributedUnitsOrdered7dSameSKU(item.getInteger("attributedUnitsOrdered7dSameSKU"));
				amzAdvReportProductTargetsAttributedSame.setAttributedUnitsOrdered14dSameSKU(item.getInteger("attributedUnitsOrdered14dSameSKU"));
				amzAdvReportProductTargetsAttributedSame.setAttributedUnitsOrdered30dSameSKU(item.getInteger("attributedUnitsOrdered30dSameSKU"));
				if ((amzAdvReportProductTarge.getImpressions() == null || amzAdvReportProductTarge.getImpressions() == 0)) {
					continue;
				}
				list.add(amzAdvReportProductTarge);
				if(!amzAdvReportProductTargetsAttributed.isZero()) {
					listAttributed.add(amzAdvReportProductTargetsAttributed);
				}
				if(!amzAdvReportProductTargetsAttributedSame.isZero()) {
					listAttributedSame.add(amzAdvReportProductTargetsAttributedSame);
				}
				if (list.size() >= 2000) {
					amzAdvReportProductTargeMapper.insertBatch(list);
					list.clear();
				}
				if (listAttributed.size() >= 2000) {
					amzAdvReportProductTargeMapper.insertBatchAttributed(listAttributed);
					listAttributed.clear();
				}
				if (listAttributedSame.size() >= 2000) {
					amzAdvReportProductTargeMapper.insertBatchAttributedSame(listAttributedSame);
					listAttributedSame.clear();
				}
			}
			if (list.size() > 0) {
				amzAdvReportProductTargeMapper.insertBatch(list);
			}
			if (listAttributed.size() > 0) {
				amzAdvReportProductTargeMapper.insertBatchAttributed(listAttributed);
			}
			if (listAttributedSame.size() > 0) {
				amzAdvReportProductTargeMapper.insertBatchAttributedSame(listAttributedSame);
			}
		} catch(Exception e){
			e.printStackTrace();
		} 
	}

	public   void treatProductTargetHsaAdvertReport(AmzAdvRequest request, JSONReader jsonReader) {
		final List<AmzAdvReportProductTargetsHsa> list = new LinkedList<AmzAdvReportProductTargetsHsa>();
		final List<AmzAdvReportProductTargetsHsaAttributed> listAttributed = new LinkedList<AmzAdvReportProductTargetsHsaAttributed>();

		final List<AmzAdvReportProductTargetsHsaBrand> listBrand = new LinkedList<AmzAdvReportProductTargetsHsaBrand>();
		final List<AmzAdvReportProductTargetsHsaVideo> listVideo = new LinkedList<AmzAdvReportProductTargetsHsaVideo>();
		try {
			jsonReader.startArray();
			while (jsonReader.hasNext()) {
				String elem = jsonReader.readString();
				JSONObject item = GeneralUtil.getJsonObject(elem);
				AmzAdvReportProductTargetsHsa amzAdvReportProductTargeHsa = new AmzAdvReportProductTargetsHsa();
				AmzAdvReportProductTargetsHsaBrand amzAdvReportProductTargetsHsaBrand=new AmzAdvReportProductTargetsHsaBrand();
				AmzAdvReportProductTargetsHsaVideo amzAdvReportProductTargetsHsaVideo=new AmzAdvReportProductTargetsHsaVideo();
				AmzAdvReportProductTargetsHsaAttributed amzAdvReportProductTargeHsaAttributed=new AmzAdvReportProductTargetsHsaAttributed();
				amzAdvReportProductTargeHsa.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvReportProductTargeHsa.setAdgroupid(item.getBigInteger("adGroupId"));
				amzAdvReportProductTargeHsa.setTargetid(item.getBigInteger("targetId"));

				amzAdvReportProductTargeHsa.setBydate(request.getByday());
				amzAdvReportProductTargeHsa.setProfileid(request.getProfileid());
				amzAdvReportProductTargeHsa.setClicks(item.getInteger("clicks"));
				amzAdvReportProductTargeHsa.setImpressions(item.getInteger("impressions"));
				amzAdvReportProductTargeHsa.setCost(item.getBigDecimal("cost"));
				amzAdvReportProductTargeHsa.setOpttime(new Date());
				amzAdvReportProductTargeHsaAttributed.setBydate(amzAdvReportProductTargeHsa.getBydate());
				amzAdvReportProductTargeHsaAttributed.setTargetid(amzAdvReportProductTargeHsa.getTargetid());
				amzAdvReportProductTargeHsaAttributed.setAttributedsales14d(item.getBigDecimal("attributedSales14d"));
				amzAdvReportProductTargeHsaAttributed.setAttributedsales14dsamesku(item.getBigDecimal("attributedSales14dSameSKU"));
				amzAdvReportProductTargeHsaAttributed.setAttributedconversions14d(item.getInteger("attributedConversions14d"));
				amzAdvReportProductTargeHsaAttributed.setAttributedconversions14dsamesku(item.getInteger("attributedConversions14dSameSKU"));

				 if(request.getCreativeType()!=null&&"video".equals(request.getCreativeType())) {
					 amzAdvReportProductTargetsHsaVideo=	new AmzAdvReportProductTargetsHsaVideo();
					 amzAdvReportProductTargetsHsaVideo.setTargetid(amzAdvReportProductTargeHsa.getTargetid());
					 amzAdvReportProductTargetsHsaVideo.setBydate(amzAdvReportProductTargeHsa.getBydate());
					 amzAdvReportProductTargetsHsaVideo.setOpttime(amzAdvReportProductTargeHsa.getOpttime());
					 amzAdvReportProductTargetsHsaVideo.setViewableImpressions(item.getInteger("viewableImpressions"));
					 amzAdvReportProductTargetsHsaVideo.setVideoFirstQuartileViews(item.getInteger("videoFirstQuartileViews"));
					 amzAdvReportProductTargetsHsaVideo.setVideoMidpointViews(item.getInteger("videoMidpointViews"));
	                 amzAdvReportProductTargetsHsaVideo.setVideoThirdQuartileViews(item.getInteger("videoThirdQuartileViews"));
	                 amzAdvReportProductTargetsHsaVideo.setVideoCompleteViews(item.getInteger("videoCompleteViews"));
	                 amzAdvReportProductTargetsHsaVideo.setVideo5SecondViews(item.getInteger("video5SecondViews"));
	                 amzAdvReportProductTargetsHsaVideo.setVideo5SecondViewRate(item.getBigDecimal("video5SecondViewRate"));
	                 amzAdvReportProductTargetsHsaVideo.setVideoUnmutes(item.getInteger("videoUnmutes"));
	                 amzAdvReportProductTargetsHsaVideo.setVtr(item.getBigDecimal("vtr"));
	                 amzAdvReportProductTargetsHsaVideo.setVctr(item.getBigDecimal("vctr"));
	                	if(amzAdvReportProductTargetsHsaVideo.iszero()) {
	                		listVideo.add(amzAdvReportProductTargetsHsaVideo);
	                	}
	                }else {
	                	amzAdvReportProductTargetsHsaBrand=	new AmzAdvReportProductTargetsHsaBrand();
	                	amzAdvReportProductTargetsHsaBrand.setTargetid(amzAdvReportProductTargeHsa.getTargetid());
	                	amzAdvReportProductTargetsHsaBrand.setBydate(amzAdvReportProductTargeHsa.getBydate());
	                	amzAdvReportProductTargetsHsaBrand.setOpttime(amzAdvReportProductTargeHsa.getOpttime());
	                	amzAdvReportProductTargetsHsaBrand.setAttributedDetailPageViewsClicks14d(item.getInteger("attributedDetailPageViewsClicks14d"));
	                	amzAdvReportProductTargetsHsaBrand.setAttributedOrdersNewToBrand14d(item.getInteger("attributedOrdersNewToBrand14d"));
	                	amzAdvReportProductTargetsHsaBrand.setAttributedOrdersNewToBrandPercentage14d(item.getBigDecimal("attributedOrdersNewToBrandPercentage14d"));
	                	amzAdvReportProductTargetsHsaBrand.setAttributedOrderRateNewToBrand14d(item.getInteger("attributedOrderRateNewToBrand14d"));
	                	amzAdvReportProductTargetsHsaBrand.setAttributedSalesNewToBrand14d(item.getInteger("attributedSalesNewToBrand14d"));
	                	amzAdvReportProductTargetsHsaBrand.setAttributedSalesNewToBrandPercentage14d(item.getBigDecimal("attributedSalesNewToBrandPercentage14d"));
	                	amzAdvReportProductTargetsHsaBrand.setAttributedUnitsOrderedNewToBrand14d(item.getInteger("attributedUnitsOrderedNewToBrand14d"));
	                	amzAdvReportProductTargetsHsaBrand.setAttributedUnitsOrderedNewToBrandPercentage14d(item.getBigDecimal("attributedUnitsOrderedNewToBrandPercentage14d"));
	                	amzAdvReportProductTargetsHsaBrand.setUnitsSold14d(item.getInteger("unitsSold14d"));
	                	amzAdvReportProductTargetsHsaBrand.setDpv14d(item.getInteger("dpv14d"));
	                	if(!amzAdvReportProductTargetsHsaBrand.iszero()) {
	                		listBrand.add(amzAdvReportProductTargetsHsaBrand);
	                	}
	                }
				 
				if ((amzAdvReportProductTargeHsa.getImpressions() == null || amzAdvReportProductTargeHsa.getImpressions() == 0)) {
					continue;
				}
				list.add(amzAdvReportProductTargeHsa);
				if(!amzAdvReportProductTargeHsaAttributed.isZero()) {
					listAttributed.add(amzAdvReportProductTargeHsaAttributed);	
				}
				if (list.size() >= 2000) {
					amzAdvReportProductTargeHsaMapper.insertBatch(list);
					list.clear();
				}
				if (listVideo.size() >= 2000) {
					amzAdvReportProductTargeHsaMapper.insertBatchVideo(listVideo);
					listVideo.clear();
				}
				if (listBrand.size() >= 2000) {
					amzAdvReportProductTargeHsaMapper.insertBatchBrand(listBrand);
					listBrand.clear();
				}
				if (listAttributed.size() >= 2000) {
					amzAdvReportProductTargeHsaMapper.insertBatchAttributed(listAttributed);
					listAttributed.clear();
				}
			}
			if (list.size() > 0) {
				amzAdvReportProductTargeHsaMapper.insertBatch(list);
			}
			if (listVideo.size() > 0) {
				amzAdvReportProductTargeHsaMapper.insertBatchVideo(listVideo);
			}
			if (listBrand.size() > 0) {
				amzAdvReportProductTargeHsaMapper.insertBatchBrand(listBrand);
			}
			if (listAttributed.size() > 0) {
				amzAdvReportProductTargeHsaMapper.insertBatchAttributed(listAttributed);
			}
		}catch(Exception e) {
			e.printStackTrace();
		} 
	}

	public   void treatTargetQueryAdvertReport(AmzAdvRequest request, JSONReader jsonReader) {
		final List<AmzAdvReportTargetQuery> list = new LinkedList<AmzAdvReportTargetQuery>();
		final List<AmzAdvReportTargetQueryAttributed> listAttributed = new LinkedList<AmzAdvReportTargetQueryAttributed>();
		final List<AmzAdvReportTargetQueryAttributedSame> listAttributedSame = new LinkedList<AmzAdvReportTargetQueryAttributedSame>();
		try {
			jsonReader.startArray();
			while (jsonReader.hasNext()) {
				String elem = jsonReader.readString();
				JSONObject item = GeneralUtil.getJsonObject(elem);
				AmzAdvReportTargetQuery amzAdvReportTargetQuery = new AmzAdvReportTargetQuery();
				AmzAdvReportTargetQueryAttributed amzAdvReportTargetQueryAttributed = new AmzAdvReportTargetQueryAttributed();
				AmzAdvReportTargetQueryAttributedSame amzAdvReportTargetQueryAttributedSame = new AmzAdvReportTargetQueryAttributedSame();
				amzAdvReportTargetQuery.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvReportTargetQuery.setTargetid(item.getBigInteger("targetId"));
				amzAdvReportTargetQuery.setAdgroupid(item.getBigInteger("adGroupId"));
		        String query =item.getString("query");
		        AmzAdvRptQuery queryobj = loadQuery(query);
		        if(queryobj!=null&&queryobj.getId()!=null) {
		        	amzAdvReportTargetQuery.setQueryid(new BigInteger(queryobj.getId()));
		        }else {
		        	continue;
		        }
				amzAdvReportTargetQuery.setClicks(item.getInteger("clicks"));
				amzAdvReportTargetQuery.setImpressions(item.getInteger("impressions"));
				amzAdvReportTargetQuery.setCost(item.getBigDecimal("cost"));
				amzAdvReportTargetQuery.setProfileid(request.getProfileid());
				amzAdvReportTargetQuery.setBydate(request.getByday());
				amzAdvReportTargetQuery.setOpttime(new Date());
				amzAdvReportTargetQueryAttributed.setBydate(amzAdvReportTargetQuery.getBydate());
				amzAdvReportTargetQueryAttributed.setQueryid(amzAdvReportTargetQuery.getQueryid());
				amzAdvReportTargetQueryAttributed.setTargetid(amzAdvReportTargetQuery.getTargetid());
				amzAdvReportTargetQueryAttributed.setAttributedsales1d(item.getBigDecimal("attributedSales1d"));
				amzAdvReportTargetQueryAttributed.setAttributedsales7d(item.getBigDecimal("attributedSales7d"));
				amzAdvReportTargetQueryAttributed.setAttributedsales14d(item.getBigDecimal("attributedSales14d"));
				amzAdvReportTargetQueryAttributed.setAttributedsales30d(item.getBigDecimal("attributedSales30d"));


				amzAdvReportTargetQueryAttributed.setAttributedunitsordered1d(item.getInteger("attributedUnitsOrdered1d"));
				amzAdvReportTargetQueryAttributed.setAttributedunitsordered7d(item.getInteger("attributedUnitsOrdered7d"));
				amzAdvReportTargetQueryAttributed.setAttributedunitsordered14d(item.getInteger("attributedUnitsOrdered14d"));
				amzAdvReportTargetQueryAttributed.setAttributedunitsordered30d(item.getInteger("attributedUnitsOrdered30d"));

				amzAdvReportTargetQueryAttributed.setAttributedconversions1d(item.getInteger("attributedConversions1d"));
				amzAdvReportTargetQueryAttributed.setAttributedconversions7d(item.getInteger("attributedConversions7d"));
				amzAdvReportTargetQueryAttributed.setAttributedconversions14d(item.getInteger("attributedConversions14d"));
				amzAdvReportTargetQueryAttributed.setAttributedconversions30d(item.getInteger("attributedConversions30d"));


				amzAdvReportTargetQueryAttributedSame.setBydate(amzAdvReportTargetQuery.getBydate());
				amzAdvReportTargetQueryAttributedSame.setQueryid(amzAdvReportTargetQuery.getQueryid());
				amzAdvReportTargetQueryAttributedSame.setTargetid(amzAdvReportTargetQuery.getTargetid());
				
				amzAdvReportTargetQueryAttributedSame.setAttributedsales1dsamesku(item.getBigDecimal("attributedSales1dSameSKU"));
				amzAdvReportTargetQueryAttributedSame.setAttributedsales7dsamesku(item.getBigDecimal("attributedSales7dSameSKU"));
				amzAdvReportTargetQueryAttributedSame.setAttributedsales14dsamesku(item.getBigDecimal("attributedSales14dSameSKU"));
				amzAdvReportTargetQueryAttributedSame.setAttributedsales30dsamesku(item.getBigDecimal("attributedSales30dSameSKU"));
				
				amzAdvReportTargetQueryAttributedSame.setAttributedconversions1dsamesku(item.getInteger("attributedConversions1dSameSKU"));
				amzAdvReportTargetQueryAttributedSame.setAttributedconversions7dsamesku(item.getInteger("attributedConversions7dSameSKU"));
				amzAdvReportTargetQueryAttributedSame.setAttributedconversions14dsamesku(item.getInteger("attributedConversions14dSameSKU"));
				amzAdvReportTargetQueryAttributedSame.setAttributedconversions30dsamesku(item.getInteger("attributedConversions30dSameSKU"));

				amzAdvReportTargetQueryAttributedSame.setAttributedUnitsOrdered1dSameSKU(item.getInteger("attributedUnitsOrdered1dSameSKU"));
				amzAdvReportTargetQueryAttributedSame.setAttributedUnitsOrdered7dSameSKU(item.getInteger("attributedUnitsOrdered7dSameSKU"));
				amzAdvReportTargetQueryAttributedSame.setAttributedUnitsOrdered14dSameSKU(item.getInteger("attributedUnitsOrdered14dSameSKU"));
				amzAdvReportTargetQueryAttributedSame.setAttributedUnitsOrdered30dSameSKU(item.getInteger("attributedUnitsOrdered30dSameSKU"));
				if ((amzAdvReportTargetQuery.getImpressions() == null || amzAdvReportTargetQuery.getImpressions() == 0)) {
					continue;
				}
				if(!amzAdvReportTargetQueryAttributed.isZero()) {
					listAttributed.add(amzAdvReportTargetQueryAttributed);
				}
				if(!amzAdvReportTargetQueryAttributedSame.isZero()) {
					listAttributedSame.add(amzAdvReportTargetQueryAttributedSame);
				}
				
				list.add(amzAdvReportTargetQuery);
				if (list.size() >= 2000) {
					amzAdvReportTargetQueryMapper.insertBatch(list);
					list.clear();
				}
				if (listAttributed.size() >= 2000) {
					amzAdvReportTargetQueryMapper.insertBatchAttributed(listAttributed);
					listAttributed.clear();
				}
				if (listAttributedSame.size() >= 2000) {
					amzAdvReportTargetQueryMapper.insertBatchAttributedSame(listAttributedSame);
					listAttributedSame.clear();
				}
				
			}
			if (list.size() > 0) {
				amzAdvReportTargetQueryMapper.insertBatch(list);
			}
			if (listAttributed.size() > 0) {
				amzAdvReportTargetQueryMapper.insertBatchAttributed(listAttributed);
			}
			if (listAttributedSame.size() > 0) {
				amzAdvReportTargetQueryMapper.insertBatchAttributedSame(listAttributedSame);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}  
	}

	public   void treatAdvAdgroups(AmzAdvSnapshot record, JSONArray a) {
		Map<String, Integer> stateqty = new HashMap<String, Integer>();
		List<AmzAdvAdgroups> list = new LinkedList<AmzAdvAdgroups>();
		for (int i = 0; i < a.size(); i++) {
			JSONObject item = a.getJSONObject(i);
			AmzAdvAdgroups amzAdvAdgroups = new AmzAdvAdgroups();
			amzAdvAdgroups.setAdgroupid(item.getBigInteger("adGroupId"));
			amzAdvAdgroups.setCampaignid(item.getBigInteger("campaignId"));
			amzAdvAdgroups.setName(item.getString("name"));
			amzAdvAdgroups.setDefaultbid(item.getBigDecimal("defaultBid"));
			amzAdvAdgroups.setState(item.getString("state"));
			amzAdvAdgroups.setOpttime(new Date());
			amzAdvAdgroups.setProfileid(record.getProfileid());
			list.add(amzAdvAdgroups);
			Integer qty = stateqty.get(amzAdvAdgroups.getState());
			if (qty == null) {
				qty = 0;
			}
			stateqty.put(amzAdvAdgroups.getState(), qty + 1);
			if (list.size() >= 2000) {
				amzAdvAdgroupsMapper.insertBatch(list);
				list.clear();
			}
		}
		if (list.size() > 0) {
			amzAdvAdgroupsMapper.insertBatch(list);
		}
		saveAmzAdvSumAllType(stateqty, record);
	}

	public   void treatAdvCampaigns(AmzAdvSnapshot record, JSONArray a) {
		Map<String, Integer> stateqty = new HashMap<String, Integer>();
		List<AmzAdvCampaigns> list = new LinkedList<AmzAdvCampaigns>();
		for (int i = 0; i < a.size(); i++) {
			JSONObject item = a.getJSONObject(i);
			String campaignType = item.getString("campaignType");
			BigInteger portfolioid = item.getBigInteger("portfolioid");
			BigDecimal dailyBudget = item.getBigDecimal("dailyBudget");
			BigInteger campaignId = item.getBigInteger("campaignId");
			String name = item.getString("name");
			String targetingType = item.getString("targetingType");
			String bidding = item.getString("bidding");
			String state = item.getString("state");
			String premiumBidAdjustment = item.getString("premiumBidAdjustment");
			String startDatestr = item.getString("startDate");
			String endDatestr = item.getString("endDate");
			Date startDate = null;
			Date endDate = null;
			if (startDatestr != null) {
				startDate = GeneralUtil.StringfromDate(startDatestr, "yyyyMMdd");
			}
			if (endDatestr != null) {
				if(endDatestr.equals("19700101")) {
					endDate=null;
				}else {
					endDate = GeneralUtil.StringfromDate(endDatestr, "yyyyMMdd");
				}
			}
			AmzAdvCampaigns amzAdvCampaigns = new AmzAdvCampaigns();
			amzAdvCampaigns.setCampaignid(campaignId);
			amzAdvCampaigns.setPortfolioid(portfolioid);
			amzAdvCampaigns.setCampaigntype(campaignType);
			amzAdvCampaigns.setDailybudget(dailyBudget);
			amzAdvCampaigns.setName(name);
			amzAdvCampaigns.setTargetingtype(targetingType);
			amzAdvCampaigns.setBidding(bidding);
			amzAdvCampaigns.setState(state);
			amzAdvCampaigns.setPremiumbidadjustment(premiumBidAdjustment);
			amzAdvCampaigns.setStartdate(startDate);
			Calendar c=Calendar.getInstance();
			c.add(Calendar.YEAR, -10);
			if(endDate!=null&&c.getTime().after(endDate)) {
				amzAdvCampaigns.setEnddate(null);
			}else {
				amzAdvCampaigns.setEnddate(endDate);
			 }
			
			amzAdvCampaigns.setProfileid(record.getProfileid());
			amzAdvCampaigns.setOpttime(new Date());
			list.add(amzAdvCampaigns);
			Integer qty = stateqty.get(amzAdvCampaigns.getState());
			if (qty == null) {
				qty = 0;
			}
			stateqty.put(amzAdvCampaigns.getState(), qty + 1);
			if (list.size() >= 2000) {
				amzAdvCampaignsMapper.insertBatch(list);
				list.clear();
			}
		}
		if (list.size() > 0) {
			amzAdvCampaignsMapper.insertBatch(list);
		}
		saveAmzAdvSumAllType(stateqty, record);
	}

	public   void treatAdvCampKeywordsNegativa(AmzAdvSnapshot record, JSONArray a) {
		Map<String, Integer> stateqty = new HashMap<String, Integer>();
		List<AmzAdvCampKeywordsNegativa> list = new LinkedList<AmzAdvCampKeywordsNegativa>();
		for (int i = 0; i < a.size(); i++) {
			JSONObject item = a.getJSONObject(i);
			BigInteger campaignId = item.getBigInteger("campaignId");
			BigInteger keywordId = item.getBigInteger("keywordId");
			String keywordText = item.getString("keywordText");
			String matchType = item.getString("matchType");
			String state = item.getString("state");

			AmzAdvCampKeywordsNegativa amzAdvCampKeywords = new AmzAdvCampKeywordsNegativa();
			amzAdvCampKeywords.setCampaignid(campaignId);
			amzAdvCampKeywords.setKeywordid(keywordId);
			amzAdvCampKeywords.setKeywordtext(keywordText);
			amzAdvCampKeywords.setMatchtype(matchType);
			amzAdvCampKeywords.setState(state);
			amzAdvCampKeywords.setProfileid(record.getProfileid());
			amzAdvCampKeywords.setOpttime(new Date());
			list.add(amzAdvCampKeywords);
			Integer qty = stateqty.get(amzAdvCampKeywords.getState());
			if (qty == null) {
				qty = 0;
			}
			stateqty.put(amzAdvCampKeywords.getState(), qty + 1);
			if (list.size() >= 2000) {
				amzAdvCampKeywordsNegativaMapper.insertBatch(list);
				list.clear();
			}
		}
		if (list.size() > 0) {
			amzAdvCampKeywordsNegativaMapper.insertBatch(list);
		}
		saveAmzAdvSumAllType(stateqty, record);
	}

	public   void treatAdvKeywords(AmzAdvSnapshot record, JSONArray a) {
		Map<String, Integer> stateqty = new HashMap<String, Integer>();
		List<AmzAdvKeywords> list = new LinkedList<AmzAdvKeywords>();
		for (int i = 0; i < a.size(); i++) {
			JSONObject item = a.getJSONObject(i);
			BigInteger campaignId = item.getBigInteger("campaignId");
			BigInteger keywordId = item.getBigInteger("keywordId");
			BigInteger adGroupId = item.getBigInteger("adGroupId");
			String keywordText = item.getString("keywordText");
			String nativeLanguageKeyword = item.getString("nativeLanguageKeyword");
			String matchType = item.getString("matchType");
			String state = item.getString("state");
			BigDecimal bid = item.getBigDecimal("bid");

			AmzAdvKeywords amzAdvKeywords = new AmzAdvKeywords();
			amzAdvKeywords.setCampaignid(campaignId);
			amzAdvKeywords.setKeywordid(keywordId);
			amzAdvKeywords.setAdgroupid(adGroupId);
			amzAdvKeywords.setKeywordtext(keywordText);
			amzAdvKeywords.setMatchtype(matchType);
			amzAdvKeywords.setState(state);
			amzAdvKeywords.setBid(bid);
			amzAdvKeywords.setCampaigntype(record.getCampaigntype());
			amzAdvKeywords.setNativeLanguageKeyword(nativeLanguageKeyword);
			amzAdvKeywords.setProfileid(record.getProfileid());
			amzAdvKeywords.setOpttime(new Date());
			list.add(amzAdvKeywords);
			Integer qty = stateqty.get(amzAdvKeywords.getState());
			if (qty == null) {
				qty = 0;
			}
			stateqty.put(amzAdvKeywords.getState(), qty + 1);
			if (list.size() >= 2000) {
				amzAdvKeywordsMapper.insertBatch(list);
				list.clear();
			}
		}
		if (list.size() > 0) {
			amzAdvKeywordsMapper.insertBatch(list);
		}
		saveAmzAdvSumAllType(stateqty, record);
	}

	public   void treatAdvKeywordsNegativa(AmzAdvSnapshot record, JSONArray a) {
		Map<String, Integer> stateqty = new HashMap<String, Integer>();
		List<AmzAdvKeywordsNegativa> list = new LinkedList<AmzAdvKeywordsNegativa>();
		for (int i = 0; i < a.size(); i++) {
			JSONObject item = a.getJSONObject(i);
			BigInteger campaignId = item.getBigInteger("campaignId");
			BigInteger keywordId = item.getBigInteger("keywordId");
			BigInteger adGroupId = item.getBigInteger("adGroupId");
			String keywordText = item.getString("keywordText");
			String matchType = item.getString("matchType");
			String state = item.getString("state");

			AmzAdvKeywordsNegativa amzAdvNegKeywords = new AmzAdvKeywordsNegativa();
			amzAdvNegKeywords.setCampaignid(campaignId);
			amzAdvNegKeywords.setKeywordid(keywordId);
			amzAdvNegKeywords.setAdgroupid(adGroupId);
			amzAdvNegKeywords.setCampaigntype(record.getCampaigntype());
			amzAdvNegKeywords.setKeywordtext(keywordText);
			amzAdvNegKeywords.setMatchtype(matchType);
			amzAdvNegKeywords.setState(state);
			amzAdvNegKeywords.setProfileid(record.getProfileid());
			amzAdvNegKeywords.setOpttime(new Date());
			list.add(amzAdvNegKeywords);
			Integer qty = stateqty.get(amzAdvNegKeywords.getState());
			if (qty == null) {
				qty = 0;
			}
			stateqty.put(amzAdvNegKeywords.getState(), qty + 1);
			if (list.size() >= 2000) {
				amzAdvKeywordsNegativaMapper.insertBatch(list);
				list.clear();
			}
		}
		if (list.size() > 0) {
			amzAdvKeywordsNegativaMapper.insertBatch(list);
		}
		saveAmzAdvSumAllType(stateqty, record);
	}

	public   void treatAdvProductads(AmzAdvSnapshot record, JSONArray a) {
		Map<String, Integer> stateqty = new HashMap<String, Integer>();
		Map<String, Integer> stateqty2 = new HashMap<String, Integer>();
		List<AmzAdvProductads> list = new LinkedList<AmzAdvProductads>();
		Set<String> asinSet = new HashSet<String>();
		for (int i = 0; i < a.size(); i++) {
			JSONObject item = a.getJSONObject(i);
			BigInteger campaignId = item.getBigInteger("campaignId");
			BigInteger adGroupId = item.getBigInteger("adGroupId");
			BigInteger adId = item.getBigInteger("adId");
			String sku = item.getString("sku");
			String asin = item.getString("asin");
			String state = item.getString("state");
			asinSet.add(asin);
			AmzAdvProductads amzAdvProductads = new AmzAdvProductads();
			amzAdvProductads.setCampaignid(campaignId);
			amzAdvProductads.setAdgroupid(adGroupId);
			amzAdvProductads.setAdid(adId);
			amzAdvProductads.setSku(sku);
			amzAdvProductads.setAsin(asin);
			amzAdvProductads.setState(state);
			amzAdvProductads.setProfileid(record.getProfileid());
			amzAdvProductads.setOpttime(new Date());
			list.add(amzAdvProductads);
			Integer qty = stateqty.get(amzAdvProductads.getState());
			if (qty == null) {
				qty = 0;
			}
			stateqty.put(amzAdvProductads.getState(), qty + 1);
			if (list.size() >= 2000) {
				amzAdvProductadsMapper.insertBatch(list);
				list.clear();
			}
		}
		if (list.size() > 0) {
			amzAdvProductadsMapper.insertBatch(list);
		}
		saveAmzAdvSumAllType(stateqty, record);
		AmzAdvSnapshot record2 = new AmzAdvSnapshot();
		record2.setProfileid(record.getProfileid());
		record2.setCampaigntype(record.getCampaigntype());
		record2.setRecordtype("asin");
		stateqty2.put("enabled", asinSet.size());
		saveAmzAdvSumAllType(stateqty2, record2);
	}

	public   void treatAdvCampaignsHsa(AmzAdvSnapshot record, JSONArray a) {
		Map<String, Integer> stateqty = new HashMap<String, Integer>();
		List<AmzAdvCampaignsHsa> list = new LinkedList<AmzAdvCampaignsHsa>();
		for (int i = 0; i < a.size(); i++) {
			JSONObject item = a.getJSONObject(i);
			BigInteger campaignId = item.getBigInteger("campaignId");
			BigInteger portfolioid = item.getBigInteger("portfolioid");
			String name = item.getString("name");
			String budgetType = item.getString("budgetType");
			BigDecimal budget = item.getBigDecimal("budget");
			String startDatestr = item.getString("startDate");
			String endDatestr = item.getString("endDate");
			String servingStatus = item.getString("servingStatus");
			String state = item.getString("state");
			String spendingPolicy = item.getString("spendingPolicy");
			String bidOptimization = item.getString("bidOptimization");
			BigDecimal bidMultiplier = item.getBigDecimal("bidMultiplier");
			String bidAdjustments=item.getString("bidAdjustments");
			String adFormat=item.getString("adFormat");
			String creative=item.getString("creative");
			Date startDate = null;
			Date endDate = null;
			if (startDatestr != null) {
				startDate = GeneralUtil.StringfromDate(startDatestr, "yyyyMMdd");
			}
			if (endDatestr != null) {
				endDate = GeneralUtil.StringfromDate(endDatestr, "yyyyMMdd");
			}

			AmzAdvCampaignsHsa amzAdvCampaigns = new AmzAdvCampaignsHsa();
			amzAdvCampaigns.setCampaignid(campaignId);
			amzAdvCampaigns.setPortfolioid(portfolioid);
			amzAdvCampaigns.setName(name);
			amzAdvCampaigns.setBudgettype(budgetType);
			amzAdvCampaigns.setBudget(budget);
			amzAdvCampaigns.setStartdate(startDate);
			amzAdvCampaigns.setEnddate(endDate);
			amzAdvCampaigns.setServingstatus(servingStatus);
			amzAdvCampaigns.setState(state);
			amzAdvCampaigns.setSpendingpolicy(spendingPolicy);
			amzAdvCampaigns.setBidoptimization(bidOptimization);
			amzAdvCampaigns.setBidAdjustments(bidAdjustments);
			amzAdvCampaigns.setBidMultiplier(bidMultiplier);
			amzAdvCampaigns.setProfileid(record.getProfileid());
			amzAdvCampaigns.setCreative(creative);
			amzAdvCampaigns.setAdFormat(adFormat);
			amzAdvCampaigns.setOpttime(new Date());
			list.add(amzAdvCampaigns);
			Integer qty = stateqty.get(amzAdvCampaigns.getState());
			if (qty == null) {
				qty = 0;
			}
			stateqty.put(amzAdvCampaigns.getState(), qty + 1);
			if (list.size() >= 2000) {
				amzAdvCampaignsHsaMapper.insertBatch(list);
				list.clear();
			}
		}
		if (list.size() > 0) {
			amzAdvCampaignsHsaMapper.insertBatch(list);
		}
		saveAmzAdvSumAllType(stateqty, record);
	}

	public   void treatAdvProductTarget(AmzAdvSnapshot record, JSONArray a) {
		List<AmzAdvProductTarge> list = new LinkedList<AmzAdvProductTarge>();
		Map<String, Integer> stateqty = new HashMap<String, Integer>();
		for (int i = 0; i < a.size(); i++) {
			JSONObject item = a.getJSONObject(i);
			BigInteger campaignId = item.getBigInteger("campaignId");
			BigInteger adGroupId = item.getBigInteger("adGroupId");
			BigInteger targetId = item.getBigInteger("targetId");
			String expressionType = item.getString("expressionType");
			JSONArray expression = item.getJSONArray("expression");
			String expressionstr = null;
			if (expression != null) {
				expressionstr = expression.toJSONString();
			}
			String state = item.getString("state");
			BigDecimal bid = item.getBigDecimal("bid");

			AmzAdvProductTarge amzAdvProductTarge = new AmzAdvProductTarge();
			amzAdvProductTarge.setCampaignid(campaignId);
			amzAdvProductTarge.setAdgroupid(adGroupId);
			amzAdvProductTarge.setTargetid(targetId);
			amzAdvProductTarge.setExpression(expressionstr);
			amzAdvProductTarge.setExpressiontype(expressionType);
			amzAdvProductTarge.setState(state);
			amzAdvProductTarge.setBid(bid);
			amzAdvProductTarge.setProfileid(record.getProfileid());
			amzAdvProductTarge.setOpttime(new Date());
			list.add(amzAdvProductTarge);
			Integer qty = stateqty.get(amzAdvProductTarge.getState());
			if (qty == null) {
				qty = 0;
			}
			stateqty.put(amzAdvProductTarge.getState(), qty + 1);
			if (list.size() >= 2000) {
				amzAdvProductTargeMapper.insertBatch(list);
				list.clear();
			}
		}
		if (list.size() > 0) {
			amzAdvProductTargeMapper.insertBatch(list);
		}
		saveAmzAdvSumAllType(stateqty, record);
	}

	public synchronized void treatAdvProductTargetNegativa(AmzAdvSnapshot record, JSONArray a) {
		List<AmzAdvProductTargeNegativa> list = new LinkedList<AmzAdvProductTargeNegativa>();
		Map<String, Integer> stateqty = new HashMap<String, Integer>();
		for (int i = 0; i < a.size(); i++) {
			JSONObject item = a.getJSONObject(i);
			BigInteger campaignId = item.getBigInteger("campaignId");
			BigInteger adGroupId = item.getBigInteger("adGroupId");
			BigInteger targetId = item.getBigInteger("targetId");
			String expressionType = item.getString("expressionType");
			String expression = item.getJSONArray("expression").toJSONString();
			String state = item.getString("state");

			AmzAdvProductTargeNegativa amzAdvProductTargeNegativa = new AmzAdvProductTargeNegativa();
			amzAdvProductTargeNegativa.setCampaignid(campaignId);
			amzAdvProductTargeNegativa.setAdgroupid(adGroupId);
			amzAdvProductTargeNegativa.setTargetid(targetId);
			amzAdvProductTargeNegativa.setExpression(expression);
			amzAdvProductTargeNegativa.setExpressiontype(expressionType);
			amzAdvProductTargeNegativa.setState(state);
			amzAdvProductTargeNegativa.setProfileid(record.getProfileid());
			amzAdvProductTargeNegativa.setOpttime(new Date());
			list.add(amzAdvProductTargeNegativa);
			Integer qty = stateqty.get(amzAdvProductTargeNegativa.getState());
			if (qty == null) {
				qty = 0;
			}
			stateqty.put(amzAdvProductTargeNegativa.getState(), qty + 1);
			if (list.size() >= 2000) {
				amzAdvProductTargeNegativaMapper.insertBatch(list);
				list.clear();
			}
		}
		if (list.size() > 0) {
			amzAdvProductTargeNegativaMapper.insertBatch(list);
		}
		saveAmzAdvSumAllType(stateqty, record);
	}

	public void saveAmzAdvSumAllType(Map<String, Integer> stateqty, AmzAdvSnapshot record) {
		for (Entry<String, Integer> entry : stateqty.entrySet()) {
			AmzAdvSumAllType typesum = new AmzAdvSumAllType();
			typesum.setProfileid(record.getProfileid());
			typesum.setCampaigntype(record.getCampaigntype());
			typesum.setRecordtype(record.getRecordtype());
			typesum.setByday(new Date());
			typesum.setOpttime(new Date());
			typesum.setState(entry.getKey());
			typesum.setQuantity(entry.getValue());
			AmzAdvSumAllType old = amzAdvSumAllTypeService.selectByKey(typesum);
			if (old != null) {
				amzAdvSumAllTypeService.updateByKey(typesum);
			} else {
				amzAdvSumAllTypeService.insert(typesum);
			}
		}
		stateqty.clear();
		stateqty = null;
	}

	public void saveAmzAdvSumAllType(int size, AmzAdvRequest request) {
		AmzAdvSumAllType typesum = new AmzAdvSumAllType();
		typesum.setProfileid(request.getProfileid());
		typesum.setCampaigntype(request.getCampaigntype());
		typesum.setRecordtype(request.getRecordtype());
		typesum.setByday(new Date());
		typesum.setOpttime(new Date());
		typesum.setState(AdvState.enabled);
		typesum.setQuantity(size);
		AmzAdvSumAllType old = amzAdvSumAllTypeService.selectByKey(typesum);
		if (old != null) {
			amzAdvSumAllTypeService.updateByKey(typesum);
		} else {
			amzAdvSumAllTypeService.insert(typesum);
		}
	}

	public   void treatAdvCampaignsSD(AmzAdvSnapshot record, JSONArray a) {
		// TODO Auto-generated method stub
		Map<String, Integer> stateqty = new HashMap<String, Integer>();
		List<AmzAdvCampaignsSD> list = new LinkedList<AmzAdvCampaignsSD>();
		for (int i = 0; i < a.size(); i++) {
			JSONObject item = a.getJSONObject(i);
			BigInteger campaignId = item.getBigInteger("campaignId");
			BigDecimal budget = item.getBigDecimal("budget");
			String name = item.getString("name");
			String budgetType = item.getString("budgetType");
			String state = item.getString("state");
			String tactic = item.getString("tactic");
			String startDatestr = item.getString("startDate");
			String endDatestr = item.getString("endDate");
			Date startDate = null;
			Date endDate = null;
			if (startDatestr != null) {
				startDate = GeneralUtil.StringfromDate(startDatestr, "yyyyMMdd");
			}
			if (endDatestr != null) {
				endDate = GeneralUtil.StringfromDate(endDatestr, "yyyyMMdd");
			}
			AmzAdvCampaignsSD amzAdvCampaigns = new AmzAdvCampaignsSD();
			amzAdvCampaigns.setCampaignid(campaignId);
			amzAdvCampaigns.setName(name);
			amzAdvCampaigns.setTactic(tactic); 
			amzAdvCampaigns.setBudgettype(budgetType);
			amzAdvCampaigns.setBudget(budget);
			amzAdvCampaigns.setStartdate(startDate);
			amzAdvCampaigns.setEnddate(endDate);
			amzAdvCampaigns.setState(state);
			amzAdvCampaigns.setProfileid(record.getProfileid());
			amzAdvCampaigns.setOpttime(new Date());
			list.add(amzAdvCampaigns);
			Integer qty = stateqty.get(amzAdvCampaigns.getState());
			if (qty == null) {
				qty = 0;
			}
			stateqty.put(amzAdvCampaigns.getState(), qty + 1);
			if (list.size() >= 2000) {
				amzAdvCampaignsSDMapper.insertBatch(list);
				list.clear();
			}
		}
		if (list.size() > 0) {
			amzAdvCampaignsSDMapper.insertBatch(list);
		}
		saveAmzAdvSumAllType(stateqty, record);
	}

	public   void treatAdvAdgroupsSD(AmzAdvSnapshot record, JSONArray a) {
		// TODO Auto-generated method stub
		Map<String, Integer> stateqty = new HashMap<String, Integer>();
		List<AmzAdvAdgroupsSD> list = new LinkedList<AmzAdvAdgroupsSD>();
		for (int i = 0; i < a.size(); i++) {
			JSONObject item = a.getJSONObject(i);
			AmzAdvAdgroupsSD amzAdvAdgroups = new AmzAdvAdgroupsSD();
			amzAdvAdgroups.setAdgroupid(item.getBigInteger("adGroupId"));
			amzAdvAdgroups.setCampaignid(item.getBigInteger("campaignId"));
			amzAdvAdgroups.setName(item.getString("name"));
			amzAdvAdgroups.setDefaultbid(item.getBigDecimal("defaultBid"));
			amzAdvAdgroups.setState(item.getString("state"));
			amzAdvAdgroups.setOpttime(new Date());
			amzAdvAdgroups.setProfileid(record.getProfileid());
			list.add(amzAdvAdgroups);
			Integer qty = stateqty.get(amzAdvAdgroups.getState());
			if (qty == null) {
				qty = 0;
			}
			stateqty.put(amzAdvAdgroups.getState(), qty + 1);
			if (list.size() >= 2000) {
				amzAdvAdgroupsSDMapper.insertBatch(list);
				list.clear();
			}
		}
		if (list.size() > 0) {
			amzAdvAdgroupsSDMapper.insertBatch(list);
		}
		saveAmzAdvSumAllType(stateqty, record);
	}

	public   void treatAdvProductadsSD(AmzAdvSnapshot record, JSONArray a) {
		// TODO Auto-generated method stub
		Map<String, Integer> stateqty = new HashMap<String, Integer>();
		Map<String, Integer> stateqty2 = new HashMap<String, Integer>();
		List<AmzAdvProductadsSD> list = new LinkedList<AmzAdvProductadsSD>();
		Set<String> asinSet = new HashSet<String>();
		for (int i = 0; i < a.size(); i++) {
			JSONObject item = a.getJSONObject(i);
			BigInteger campaignId = item.getBigInteger("campaignId");
			BigInteger adGroupId = item.getBigInteger("adGroupId");
			BigInteger adId = item.getBigInteger("adId");
			String sku = item.getString("sku");
			String asin = item.getString("asin");
			String state = item.getString("state");
			asinSet.add(asin);
			AmzAdvProductadsSD amzAdvProductads = new AmzAdvProductadsSD();
			amzAdvProductads.setCampaignid(campaignId);
			amzAdvProductads.setAdgroupid(adGroupId);
			amzAdvProductads.setAdid(adId);
			amzAdvProductads.setSku(sku);
			amzAdvProductads.setAsin(asin);
			amzAdvProductads.setState(state);
			amzAdvProductads.setProfileid(record.getProfileid());
			amzAdvProductads.setOpttime(new Date());
			list.add(amzAdvProductads);
			Integer qty = stateqty.get(amzAdvProductads.getState());
			if (qty == null) {
				qty = 0;
			}
			stateqty.put(amzAdvProductads.getState(), qty + 1);
			if (list.size() >= 2000) {
				amzAdvProductadsSDMapper.insertBatch(list);
				list.clear();
			}
		}
		if (list.size() > 0) {
			amzAdvProductadsSDMapper.insertBatch(list);
		}
		saveAmzAdvSumAllType(stateqty, record);
		AmzAdvSnapshot record2 = new AmzAdvSnapshot();
		record2.setProfileid(record.getProfileid());
		record2.setCampaigntype(record.getCampaigntype());
		record2.setRecordtype("asin");
		stateqty2.put("enabled", asinSet.size());
		saveAmzAdvSumAllType(stateqty2, record2);
	}

	public void treatAdvProductTargetSD(AmzAdvSnapshot record, JSONArray a) {
		// TODO Auto-generated method stub
		List<AmzAdvProductTargeSD> list = new LinkedList<AmzAdvProductTargeSD>();
		Map<String, Integer> stateqty = new HashMap<String, Integer>();
		for (int i = 0; i < a.size(); i++) {
			JSONObject item = a.getJSONObject(i);
			BigInteger adGroupId = item.getBigInteger("adGroupId");
			BigInteger targetId = item.getBigInteger("targetId");
			String expressionType = item.getString("expressionType");
			JSONArray expression = item.getJSONArray("expression");
			String expressionstr = null;
			if (expression != null) {
				expressionstr = expression.toJSONString();
			}
			String state = item.getString("state");
			BigDecimal bid = item.getBigDecimal("bid");

			AmzAdvProductTargeSD amzAdvProductTarge = new AmzAdvProductTargeSD();
			amzAdvProductTarge.setAdgroupid(adGroupId);
			amzAdvProductTarge.setTargetid(targetId);
			amzAdvProductTarge.setExpression(expressionstr);
			amzAdvProductTarge.setExpressiontype(expressionType);
			amzAdvProductTarge.setState(state);
			amzAdvProductTarge.setBid(bid);
			amzAdvProductTarge.setProfileid(record.getProfileid());
			amzAdvProductTarge.setOpttime(new Date());
			list.add(amzAdvProductTarge);
			Integer qty = stateqty.get(amzAdvProductTarge.getState());
			if (qty == null) {
				qty = 0;
			}
			stateqty.put(amzAdvProductTarge.getState(), qty + 1);
			if (list.size() >= 2000) {
				amzAdvProductTargeSDMapper.insertBatch(list);
				list.clear();
			}
		}
		if (list.size() > 0) {
			amzAdvProductTargeSDMapper.insertBatch(list);
		}
		saveAmzAdvSumAllType(stateqty, record);
	}

	public void treatAdvProductTargetNegativaSD(AmzAdvSnapshot record, JSONArray a) {
		// TODO Auto-generated method stub
		List<AmzAdvProductTargeNegativaSD> list = new LinkedList<AmzAdvProductTargeNegativaSD>();
		Map<String, Integer> stateqty = new HashMap<String, Integer>();
		for (int i = 0; i < a.size(); i++) {
			JSONObject item = a.getJSONObject(i);
			//BigInteger campaignId = item.getBigInteger("campaignId");
			BigInteger adGroupId = item.getBigInteger("adGroupId");
			BigInteger targetId = item.getBigInteger("targetId");
			String expressionType = item.getString("expressionType");
			String expression = item.getJSONArray("expression").toJSONString();
			String state = item.getString("state");

			AmzAdvProductTargeNegativaSD amzAdvProductTargeNegativa = new AmzAdvProductTargeNegativaSD();
			amzAdvProductTargeNegativa.setAdgroupid(adGroupId);
			amzAdvProductTargeNegativa.setTargetid(targetId);
			amzAdvProductTargeNegativa.setExpression(expression);
			amzAdvProductTargeNegativa.setExpressiontype(expressionType);
			amzAdvProductTargeNegativa.setState(state);
			amzAdvProductTargeNegativa.setProfileid(record.getProfileid());
			amzAdvProductTargeNegativa.setOpttime(new Date());
			list.add(amzAdvProductTargeNegativa);
			Integer qty = stateqty.get(amzAdvProductTargeNegativa.getState());
			if (qty == null) {
				qty = 0;
			}
			stateqty.put(amzAdvProductTargeNegativa.getState(), qty + 1);
			if (list.size() >= 2000) {
				amzAdvProductTargeNegativaSDMapper.insertBatch(list);
				list.clear();
			}
		}
		if (list.size() > 0) {
			amzAdvProductTargeNegativaSDMapper.insertBatch(list);
		}
		saveAmzAdvSumAllType(stateqty, record);
	}

	public void treatAdvProductTargetSDAdvertReport(AmzAdvRequest request, JSONReader jsonReader) {
		// TODO Auto-generated method stub
		  List<AmzAdvReportProductTargetsSD> list = new LinkedList<AmzAdvReportProductTargetsSD>();
		  List<AmzAdvReportProductTargetsSDAttributed> listAttributed = new LinkedList<AmzAdvReportProductTargetsSDAttributed>();
		  List<AmzAdvReportProductTargetsSDAttributedView> listAttributedView = new LinkedList<AmzAdvReportProductTargetsSDAttributedView>();
		  List<AmzAdvReportProductTargetsSDAttributedSame> listAttributedSame = new LinkedList<AmzAdvReportProductTargetsSDAttributedSame>();
		  List<AmzAdvReportProductTargetsSDAttributedNew> listAttributedNew = new LinkedList<AmzAdvReportProductTargetsSDAttributedNew>();
		try {
			jsonReader.startArray();
			while (jsonReader.hasNext()) {
				String elem = jsonReader.readString();
				JSONObject item = GeneralUtil.getJsonObject(elem);
				AmzAdvReportProductTargetsSD amzAdvReportProductTarge = new AmzAdvReportProductTargetsSD();
				AmzAdvReportProductTargetsSDAttributed amzAdvReportProductTargetsSDAttributed = new AmzAdvReportProductTargetsSDAttributed();
				AmzAdvReportProductTargetsSDAttributedView amzAdvReportProductTargetsSDAttributedView = new AmzAdvReportProductTargetsSDAttributedView();
				AmzAdvReportProductTargetsSDAttributedSame amzAdvReportProductTargetsSDAttributedSame = new AmzAdvReportProductTargetsSDAttributedSame();
				AmzAdvReportProductTargetsSDAttributedNew amzAdvReportProductTargetsSDAttributedNew = new AmzAdvReportProductTargetsSDAttributedNew();
				
				amzAdvReportProductTarge.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvReportProductTarge.setTargetid(item.getBigInteger("targetId"));
				amzAdvReportProductTarge.setAdgroupid(item.getBigInteger("adGroupId"));
			 

				amzAdvReportProductTarge.setClicks(item.getInteger("clicks"));
				amzAdvReportProductTarge.setImpressions(item.getInteger("impressions"));
				amzAdvReportProductTarge.setCost(item.getBigDecimal("cost"));
				amzAdvReportProductTarge.setProfileid(request.getProfileid());
				amzAdvReportProductTarge.setBydate(request.getByday());
				amzAdvReportProductTarge.setOpttime(new Date());
				amzAdvReportProductTargetsSDAttributed.setTargetid(amzAdvReportProductTarge.getTargetid());
				amzAdvReportProductTargetsSDAttributed.setBydate(amzAdvReportProductTarge.getBydate());
				amzAdvReportProductTargetsSDAttributed.setAttributedsales1d(item.getBigDecimal("attributedSales1d"));
				amzAdvReportProductTargetsSDAttributed.setAttributedsales7d(item.getBigDecimal("attributedSales7d"));
				amzAdvReportProductTargetsSDAttributed.setAttributedsales14d(item.getBigDecimal("attributedSales14d"));
				amzAdvReportProductTargetsSDAttributed.setAttributedsales30d(item.getBigDecimal("attributedSales30d"));


				amzAdvReportProductTargetsSDAttributed.setAttributedunitsordered1d(item.getInteger("attributedUnitsOrdered1d"));
				amzAdvReportProductTargetsSDAttributed.setAttributedunitsordered7d(item.getInteger("attributedUnitsOrdered7d"));
				amzAdvReportProductTargetsSDAttributed.setAttributedunitsordered14d(item.getInteger("attributedUnitsOrdered14d"));
				amzAdvReportProductTargetsSDAttributed.setAttributedunitsordered30d(item.getInteger("attributedUnitsOrdered30d"));

				amzAdvReportProductTargetsSDAttributed.setAttributedconversions1d(item.getInteger("attributedConversions1d"));
				amzAdvReportProductTargetsSDAttributed.setAttributedconversions7d(item.getInteger("attributedConversions7d"));
				amzAdvReportProductTargetsSDAttributed.setAttributedconversions14d(item.getInteger("attributedConversions14d"));
				amzAdvReportProductTargetsSDAttributed.setAttributedconversions30d(item.getInteger("attributedConversions30d"));


				amzAdvReportProductTargetsSDAttributedSame.setTargetid(amzAdvReportProductTarge.getTargetid());
				amzAdvReportProductTargetsSDAttributedSame.setBydate(amzAdvReportProductTarge.getBydate());
				amzAdvReportProductTargetsSDAttributedSame.setAttributedsales1dsamesku(item.getBigDecimal("attributedSales1dSameSKU"));
				amzAdvReportProductTargetsSDAttributedSame.setAttributedsales7dsamesku(item.getBigDecimal("attributedSales7dSameSKU"));
				amzAdvReportProductTargetsSDAttributedSame.setAttributedsales14dsamesku(item.getBigDecimal("attributedSales14dSameSKU"));
				amzAdvReportProductTargetsSDAttributedSame.setAttributedsales30dsamesku(item.getBigDecimal("attributedSales30dSameSKU"));
				
				amzAdvReportProductTargetsSDAttributedSame.setAttributedconversions1dsamesku(item.getInteger("attributedConversions1dSameSKU"));
				amzAdvReportProductTargetsSDAttributedSame.setAttributedconversions7dsamesku(item.getInteger("attributedConversions7dSameSKU"));
				amzAdvReportProductTargetsSDAttributedSame.setAttributedconversions14dsamesku(item.getInteger("attributedConversions14dSameSKU"));
				amzAdvReportProductTargetsSDAttributedSame.setAttributedconversions30dsamesku(item.getInteger("attributedConversions30dSameSKU"));

				
				
				amzAdvReportProductTargetsSDAttributedNew.setTargetid(amzAdvReportProductTarge.getTargetid());
				amzAdvReportProductTargetsSDAttributedNew.setBydate(amzAdvReportProductTarge.getBydate());
				amzAdvReportProductTargetsSDAttributedNew.setAttributedUnitsOrderedNewToBrand14d(item.getInteger("attributedUnitsOrderedNewToBrand14d"));
				amzAdvReportProductTargetsSDAttributedNew.setAttributedOrdersNewToBrand14d(item.getInteger("attributedOrdersNewToBrand14d"));
				amzAdvReportProductTargetsSDAttributedNew.setAttributedSalesNewToBrand14d(item.getBigDecimal("attributedSalesNewToBrand14d"));
				
				amzAdvReportProductTargetsSDAttributedView.setTargetid(amzAdvReportProductTarge.getTargetid());
				amzAdvReportProductTargetsSDAttributedView.setBydate(amzAdvReportProductTarge.getBydate());
				amzAdvReportProductTargetsSDAttributedView.setViewAttributedConversions14d(item.getInteger("viewAttributedConversions14d"));
				amzAdvReportProductTargetsSDAttributedView.setViewAttributedSales14d(item.getBigDecimal("viewAttributedSales14d"));
				amzAdvReportProductTargetsSDAttributedView.setViewAttributedUnitsOrdered14d(item.getInteger("viewAttributedUnitsOrdered14d"));
				amzAdvReportProductTargetsSDAttributedView.setViewImpressions(item.getInteger("viewImpressions"));
				if(!amzAdvReportProductTargetsSDAttributedView.isZero()) {
					listAttributedView.add(amzAdvReportProductTargetsSDAttributedView);
				}
				if ((amzAdvReportProductTarge.getImpressions() == null || amzAdvReportProductTarge.getImpressions() == 0)) {
					continue;
				}
				
				if(!amzAdvReportProductTargetsSDAttributed.isZero()) {
					listAttributed.add(amzAdvReportProductTargetsSDAttributed);
				}
				if(!amzAdvReportProductTargetsSDAttributedSame.isZero()) {
					listAttributedSame.add(amzAdvReportProductTargetsSDAttributedSame);
				}
				if(!amzAdvReportProductTargetsSDAttributedNew.isZero()) {
					listAttributedNew.add(amzAdvReportProductTargetsSDAttributedNew);
				}
				list.add(amzAdvReportProductTarge);
				if (list.size() >= 2000) {
					amzAdvReportProductTargeSDMapper.insertBatch(list);
					list.clear();
				}
				if (listAttributed.size() >= 2000) {
					amzAdvReportProductTargeSDMapper.insertBatchAttributed(listAttributed);
					listAttributed.clear();
				}
				if (listAttributedView.size() >= 2000) {
					amzAdvReportProductTargeSDMapper.insertBatchAttributedView(listAttributedView);
					listAttributedView.clear();
				}
				if (listAttributedSame.size() >= 2000) {
					amzAdvReportProductTargeSDMapper.insertBatchAttributedSame(listAttributedSame);
					listAttributedSame.clear();
				}
				if (listAttributedNew.size() >= 2000) {
					amzAdvReportProductTargeSDMapper.insertBatchAttributedNew(listAttributedNew);
					listAttributedNew.clear();
				}
			}
			if (list.size() > 0) {
				amzAdvReportProductTargeSDMapper.insertBatch(list);
			}
			if (listAttributed.size() > 0) {
				amzAdvReportProductTargeSDMapper.insertBatchAttributed(listAttributed);
			}
			if (listAttributedView.size() > 0) {
				amzAdvReportProductTargeSDMapper.insertBatchAttributedView(listAttributedView);
			}
			if (listAttributedSame.size() > 0) {
				amzAdvReportProductTargeSDMapper.insertBatchAttributedSame(listAttributedSame);
			}
			if (listAttributedNew.size() > 0) {
				amzAdvReportProductTargeSDMapper.insertBatchAttributedNew(listAttributedNew);
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public void treatAdvAsinSDAdvertReport(AmzAdvRequest request, JSONReader jsonReader) {
		// TODO Auto-generated method stub
		final List<AmzAdvReportAsinsSD> list = new LinkedList<AmzAdvReportAsinsSD>();
		try {
			jsonReader.startArray();
			while (jsonReader.hasNext()) {
				String elem = jsonReader.readString();
				JSONObject item = GeneralUtil.getJsonObject(elem);
				AmzAdvReportAsinsSD amzAdvReportAsins = new AmzAdvReportAsinsSD();
				amzAdvReportAsins.setProfileid(request.getProfileid());
				amzAdvReportAsins.setBydate(request.getByday());
				amzAdvReportAsins.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvReportAsins.setAdgroupid(item.getBigInteger("adGroupId"));
				String asin = item.getString("asin");
				if(StrUtil.isEmpty(asin)){
					continue;
				}

				amzAdvReportAsins.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvReportAsins.setAdgroupid(item.getBigInteger("adGroupId"));
				amzAdvReportAsins.setAsin(asin);
				amzAdvReportAsins.setOtherasin(item.getString("otherAsin"));
				amzAdvReportAsins.setSku(item.getString("sku"));
	 
				amzAdvReportAsins.setProfileid(request.getProfileid());
				amzAdvReportAsins.setBydate(request.getByday());
				amzAdvReportAsins.setAttributedSales14dOtherSKU(item.getBigDecimal("attributedSales14dOtherSKU"));
				amzAdvReportAsins.setAttributedSales1dOtherSKU(item.getBigDecimal("attributedSales1dOtherSKU"));
				amzAdvReportAsins.setAttributedSales30dOtherSKU(item.getBigDecimal("attributedSales30dOtherSKU"));
				amzAdvReportAsins.setAttributedSales7dOtherSKU(item.getBigDecimal("attributedSales7dOtherSKU"));

				amzAdvReportAsins.setAttributedUnitsOrdered14dOtherSKU(item.getInteger("attributedUnitsOrdered14dOtherSKU"));
				amzAdvReportAsins.setAttributedUnitsOrdered1dOtherSKU(item.getInteger("attributedUnitsOrdered1dOtherSKU"));
				amzAdvReportAsins.setAttributedUnitsOrdered30dOtherSKU(item.getInteger("attributedUnitsOrdered30dOtherSKU"));
				amzAdvReportAsins.setAttributedUnitsOrdered7dOtherSKU(item.getInteger("attributedUnitsOrdered7dOtherSKU"));
				
				list.add(amzAdvReportAsins);
				if (list.size() >= 2000) {
					amzAdvReportAsinsSDMapper.insertBatch(list);
					list.clear();
				}
			}
			if (list.size() > 0) {
				amzAdvReportAsinsSDMapper.insertBatch(list);
			}
		} finally {
			
			if (jsonReader != null) {
				try {
					jsonReader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void treatCampaignsT00001SDAdvertReport(AmzAdvRequest request, JSONReader jsonReader) {
		// TODO Auto-generated method stub
		final List<AmzAdvReportCampaignsT0001SD> list = new LinkedList<AmzAdvReportCampaignsT0001SD>();
		try {
			jsonReader.startArray();
			while (jsonReader.hasNext()) {
				String elem = jsonReader.readString();
				JSONObject item = GeneralUtil.getJsonObject(elem);
				AmzAdvReportCampaignsT0001SD amzAdvReportCampaigns = new AmzAdvReportCampaignsT0001SD();
				amzAdvReportCampaigns.setProfileid(request.getProfileid());
				amzAdvReportCampaigns.setBydate(request.getByday());
				amzAdvReportCampaigns.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvReportCampaigns.setClicks(item.getInteger("clicks"));
				amzAdvReportCampaigns.setCost(item.getBigDecimal("cost"));
				amzAdvReportCampaigns.setImpressions(item.getInteger("impressions"));
				amzAdvReportCampaigns.setAttributeddpv14d(item.getInteger("attributedDPV14d"));
				amzAdvReportCampaigns.setAttributedsales14d(item.getInteger("attributedSales14d"));
				amzAdvReportCampaigns.setAttributedunitssold14d(item.getInteger("attributedUnitsSold14d"));
				
				list.add(amzAdvReportCampaigns);
				if (list.size() >= 2000) {
					amzAdvReportCampaignsT0001SDMapper.insertBatch(list);
					list.clear();
				}
			}
			if (list.size() > 0) {
				amzAdvReportCampaignsT0001SDMapper.insertBatch(list);
			}
		} finally {
			if (jsonReader != null) {
				try {
					jsonReader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
