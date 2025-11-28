package com.wimoor.amazon.adv.report.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.report.pojo.AmzAdvRequest;
import com.wimoor.amazon.adv.report.service.IAmzAdvReportTreatService;
import com.wimoor.amazon.adv.sp.dao.AmzAdvReportProductAdsMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportPurchaseProduct;
import com.wimoor.common.GeneralUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service("amzAdvReportTreatPurchaseProductService")
public class AmzAdvReportTreatPurchaseProductServiceImpl extends AmzAdvReportTreatServiceImpl implements IAmzAdvReportTreatService {
    @Resource
    AmzAdvReportProductAdsMapper amzAdvReportProductAdsMapper;

    @Override
    public void treatReport(AmzAdvProfile profile, AmzAdvRequest request, JSONReader jsonReader) {
        final List<AmzAdvReportPurchaseProduct> listPurchase = new LinkedList<AmzAdvReportPurchaseProduct>();
        try {
            jsonReader.startArray();
            while (jsonReader.hasNext()) {
                String elem = jsonReader.readString();
                JSONObject item = GeneralUtil.getJsonObject(elem);
                AmzAdvReportPurchaseProduct amzAdvReportPurchaseProduct = new AmzAdvReportPurchaseProduct();
                Date date = item.getDate("date");
                if(date==null) {
                    amzAdvReportPurchaseProduct.setBydate(request.getStartDate());
                }else {
                    amzAdvReportPurchaseProduct.setBydate(date);
                }
                amzAdvReportPurchaseProduct.setKeywordId(item.getBigInteger("keywordId"));
                amzAdvReportPurchaseProduct.setAdGroupId(item.getBigInteger("adGroupId"));
                amzAdvReportPurchaseProduct.setCampaignId(item.getBigInteger("campaignId"));
                amzAdvReportPurchaseProduct.setAddToList(item.getInteger("addToList"));
                amzAdvReportPurchaseProduct.setAddToListFromClicks(item.getInteger("addToListFromClicks"));
                amzAdvReportPurchaseProduct.setQualifiedBorrows(item.getInteger("qualifiedBorrows"));
                amzAdvReportPurchaseProduct.setQualifiedBorrowsFromClicks(item.getInteger("qualifiedBorrowsFromClicks"));
                amzAdvReportPurchaseProduct.setRoyaltyQualifiedBorrows(item.getInteger("royaltyQualifiedBorrows"));
                amzAdvReportPurchaseProduct.setRoyaltyQualifiedBorrowsFromClicks(item.getInteger("royaltyQualifiedBorrowsFromClicks"));
                amzAdvReportPurchaseProduct.setPortfolioId(item.getBigInteger("portfolioId"));
                amzAdvReportPurchaseProduct.setCampaignName(item.getString("campaignName"));
                amzAdvReportPurchaseProduct.setAdGroupName(item.getString("adGroupName"));
                amzAdvReportPurchaseProduct.setKeyword(item.getString("keyword"));
                amzAdvReportPurchaseProduct.setKeywordType(item.getString("keywordType"));
                amzAdvReportPurchaseProduct.setAdvertisedAsin(item.getString("advertisedAsin"));
                amzAdvReportPurchaseProduct.setPurchasedAsin(item.getString("purchasedAsin"));
                amzAdvReportPurchaseProduct.setAdvertisedSku(item.getString("advertisedSku"));
                amzAdvReportPurchaseProduct.setCampaignBudgetCurrencyCode(item.getString("campaignBudgetCurrencyCode"));
                amzAdvReportPurchaseProduct.setMatchType(item.getString("matchType"));
                amzAdvReportPurchaseProduct.setUnitsSoldClicks1d(item.getInteger("unitsSoldClicks1d"));
                amzAdvReportPurchaseProduct.setUnitsSoldClicks7d(item.getInteger("unitsSoldClicks7d"));
                amzAdvReportPurchaseProduct.setUnitsSoldClicks14d(item.getInteger("unitsSoldClicks14d"));
                amzAdvReportPurchaseProduct.setUnitsSoldClicks30d(item.getInteger("unitsSoldClicks30d"));
                amzAdvReportPurchaseProduct.setSales1d(item.getBigDecimal("sales1d"));
                amzAdvReportPurchaseProduct.setSales7d(item.getBigDecimal("sales7d"));
                amzAdvReportPurchaseProduct.setSales14d(item.getBigDecimal("sales14d"));
                amzAdvReportPurchaseProduct.setSales30d(item.getBigDecimal("sales30d"));
                amzAdvReportPurchaseProduct.setPurchases1d(item.getInteger("purchases1d"));
                amzAdvReportPurchaseProduct.setPurchases7d(item.getInteger("purchases7d"));
                amzAdvReportPurchaseProduct.setPurchases14d(item.getInteger("purchases14d"));
                amzAdvReportPurchaseProduct.setPurchases30d(item.getInteger("purchases30d"));
                amzAdvReportPurchaseProduct.setUnitsSoldOtherSku1d(item.getInteger("unitsSoldOtherSku1d"));
                amzAdvReportPurchaseProduct.setUnitsSoldOtherSku7d(item.getInteger("unitsSoldOtherSku7d"));
                amzAdvReportPurchaseProduct.setUnitsSoldOtherSku14d(item.getInteger("unitsSoldOtherSku14d"));
                amzAdvReportPurchaseProduct.setUnitsSoldOtherSku30d(item.getInteger("unitsSoldOtherSku30d"));
                amzAdvReportPurchaseProduct.setSalesOtherSku1d(item.getBigDecimal("salesOtherSku1d"));
                amzAdvReportPurchaseProduct.setSalesOtherSku7d(item.getBigDecimal("salesOtherSku7d"));
                amzAdvReportPurchaseProduct.setSalesOtherSku14d(item.getBigDecimal("salesOtherSku14d"));
                amzAdvReportPurchaseProduct.setSalesOtherSku30d(item.getBigDecimal("salesOtherSku30d"));
                amzAdvReportPurchaseProduct.setPurchasesOtherSku1d(item.getInteger("purchasesOtherSku1d"));
                amzAdvReportPurchaseProduct.setPurchasesOtherSku7d(item.getInteger("purchasesOtherSku7d"));
                amzAdvReportPurchaseProduct.setPurchasesOtherSku14d(item.getInteger("purchasesOtherSku14d"));
                amzAdvReportPurchaseProduct.setPurchasesOtherSku30d(item.getInteger("purchasesOtherSku30d"));
                amzAdvReportPurchaseProduct.setKindleEditionNormalizedPagesRead14d(item.getInteger("kindleEditionNormalizedPagesRead14d"));
                amzAdvReportPurchaseProduct.setKindleEditionNormalizedPagesRoyalties14d(item.getBigDecimal("kindleEditionNormalizedPagesRoyalties14d"));
                amzAdvReportPurchaseProduct.setOpttime(new Date());
                listPurchase.add(amzAdvReportPurchaseProduct);
                if (listPurchase.size() >= 2000) {
                    amzAdvReportProductAdsMapper.insertBatchPurchase( listPurchase);
                    listPurchase.clear();
                }
            }
            if (listPurchase.size() >0) {
                amzAdvReportProductAdsMapper.insertBatchPurchase( listPurchase);
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
}
