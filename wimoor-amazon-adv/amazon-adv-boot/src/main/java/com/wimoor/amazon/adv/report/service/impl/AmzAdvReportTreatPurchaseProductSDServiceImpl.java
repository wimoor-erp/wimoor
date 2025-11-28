package com.wimoor.amazon.adv.report.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.report.pojo.AmzAdvRequest;
import com.wimoor.amazon.adv.report.service.IAmzAdvReportTreatService;
import com.wimoor.amazon.adv.sd.dao.AmzAdvReportProductAdsSDMapper;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportSDPurchaseProduct;
import com.wimoor.common.GeneralUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service("amzAdvReportTreatPurchaseProductSDService")
public class AmzAdvReportTreatPurchaseProductSDServiceImpl extends AmzAdvReportTreatServiceImpl implements IAmzAdvReportTreatService {
    @Resource
    AmzAdvReportProductAdsSDMapper amzAdvReportProductAdsSDMapper;
    @Override
    public void treatReport(AmzAdvProfile profile, AmzAdvRequest request, JSONReader jsonReader) {
        final List<AmzAdvReportSDPurchaseProduct> listPurchase = new LinkedList<AmzAdvReportSDPurchaseProduct>();
        try {
            jsonReader.startArray();
            while (jsonReader.hasNext()) {
                String elem = jsonReader.readString();
                JSONObject item = GeneralUtil.getJsonObject(elem);
                AmzAdvReportSDPurchaseProduct amzAdvReportPurchaseProduct = new AmzAdvReportSDPurchaseProduct();
                Date date = item.getDate("date");
                if(date==null) {
                    amzAdvReportPurchaseProduct.setBydate(request.getStartDate());
                }else {
                    amzAdvReportPurchaseProduct.setBydate(date);
                }
                amzAdvReportPurchaseProduct.setCampaignId(item.getBigInteger("campaignId"));
                amzAdvReportPurchaseProduct.setAdGroupId(item.getBigInteger("adGroupId"));

// String 类型字段
                amzAdvReportPurchaseProduct.setAdGroupName(item.getString("adGroupName"));
                amzAdvReportPurchaseProduct.setAsinBrandHalo(item.getString("asinBrandHalo"));
                amzAdvReportPurchaseProduct.setCampaignBudgetCurrencyCode(item.getString("campaignBudgetCurrencyCode"));
                amzAdvReportPurchaseProduct.setCampaignName(item.getString("campaignName"));
                amzAdvReportPurchaseProduct.setPromotedAsin(item.getString("promotedAsin"));
                amzAdvReportPurchaseProduct.setPromotedSku(item.getString("promotedSku"));

// BigDecimal 类型字段
                amzAdvReportPurchaseProduct.setAddToList(item.getBigDecimal("addToList"));
                amzAdvReportPurchaseProduct.setAddToListFromClicks(item.getBigDecimal("addToListFromClicks"));
                amzAdvReportPurchaseProduct.setQualifiedBorrowsFromClicks(item.getBigDecimal("qualifiedBorrowsFromClicks"));
                amzAdvReportPurchaseProduct.setRoyaltyQualifiedBorrowsFromClicks(item.getBigDecimal("royaltyQualifiedBorrowsFromClicks"));
                amzAdvReportPurchaseProduct.setAddToListFromViews(item.getBigDecimal("addToListFromViews"));
                amzAdvReportPurchaseProduct.setQualifiedBorrows(item.getBigDecimal("qualifiedBorrows"));
                amzAdvReportPurchaseProduct.setQualifiedBorrowsFromViews(item.getBigDecimal("qualifiedBorrowsFromViews"));
                amzAdvReportPurchaseProduct.setRoyaltyQualifiedBorrows(item.getBigDecimal("royaltyQualifiedBorrows"));
                amzAdvReportPurchaseProduct.setRoyaltyQualifiedBorrowsFromViews(item.getBigDecimal("royaltyQualifiedBorrowsFromViews"));
                amzAdvReportPurchaseProduct.setConversionsBrandHalo(item.getBigDecimal("conversionsBrandHalo"));
                amzAdvReportPurchaseProduct.setConversionsBrandHaloClicks(item.getBigDecimal("conversionsBrandHaloClicks"));
                amzAdvReportPurchaseProduct.setSalesBrandHalo(item.getBigDecimal("salesBrandHalo"));
                amzAdvReportPurchaseProduct.setSalesBrandHaloClicks(item.getBigDecimal("salesBrandHaloClicks"));
                amzAdvReportPurchaseProduct.setUnitsSoldBrandHalo(item.getBigDecimal("unitsSoldBrandHalo"));
                amzAdvReportPurchaseProduct.setUnitsSoldBrandHaloClicks(item.getBigDecimal("unitsSoldBrandHaloClicks"));
                amzAdvReportPurchaseProduct.setOpttime(new Date());
                listPurchase.add(amzAdvReportPurchaseProduct);
                if (listPurchase.size() >= 2000) {
                    amzAdvReportProductAdsSDMapper.insertBatchPurchase(listPurchase);
                    listPurchase.clear();
                }
            }
            if (listPurchase.size() >0) {
                amzAdvReportProductAdsSDMapper.insertBatchPurchase(listPurchase);
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
