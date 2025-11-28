package com.wimoor.amazon.adv.report.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.report.pojo.AmzAdvRequest;
import com.wimoor.amazon.adv.report.service.IAmzAdvReportTreatService;
import com.wimoor.amazon.adv.sb.dao.AmzAdvReportAdsHsaMapper;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportHsaPurchaseProduct;
import com.wimoor.common.GeneralUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service("amzAdvReportTreatPurchaseProductHSAService")
public class AmzAdvReportTreatPurchaseProductHSAServiceImpl extends AmzAdvReportTreatServiceImpl implements IAmzAdvReportTreatService {
    @Resource
    AmzAdvReportAdsHsaMapper amzAdvReportAdsHsaMapper;
    @Override
    public void treatReport(AmzAdvProfile profile, AmzAdvRequest request, JSONReader jsonReader) {
        final List<AmzAdvReportHsaPurchaseProduct> listPurchase = new LinkedList<AmzAdvReportHsaPurchaseProduct>();
        try {
            jsonReader.startArray();
            while (jsonReader.hasNext()) {
                String elem = jsonReader.readString();
                JSONObject item = GeneralUtil.getJsonObject(elem);
                AmzAdvReportHsaPurchaseProduct amzAdvReportPurchaseProduct = new AmzAdvReportHsaPurchaseProduct();
                if(item.getBigInteger("campaignId")==null||item.getBigInteger("adGroupId")==null){
                    continue;
                }
                Date date = item.getDate("date");
                if(date==null) {
                    amzAdvReportPurchaseProduct.setBydate(request.getStartDate());
                }else {
                    amzAdvReportPurchaseProduct.setBydate(date);
                }
                amzAdvReportPurchaseProduct.setCampaignId(item.getBigInteger("campaignId"));
                amzAdvReportPurchaseProduct.setAdGroupId(item.getBigInteger("adGroupId"));
                amzAdvReportPurchaseProduct.setCampaignBudgetCurrencyCode(item.getString("campaignBudgetCurrencyCode"));
                amzAdvReportPurchaseProduct.setCampaignName(item.getString("campaignName"));
                amzAdvReportPurchaseProduct.setCampaignPriceTypeCode(item.getString("campaignPriceTypeCode"));
                amzAdvReportPurchaseProduct.setAdGroupName(item.getString("adGroupName"));
                amzAdvReportPurchaseProduct.setAttributionType(item.getString("attributionType"));
                amzAdvReportPurchaseProduct.setPurchasedAsin(item.getString("purchasedAsin"));
                amzAdvReportPurchaseProduct.setOrdersClicks14d(item.getInteger("ordersClicks14d"));
                amzAdvReportPurchaseProduct.setProductName(item.getString("productName"));
                amzAdvReportPurchaseProduct.setProductCategory(item.getString("productCategory"));
                amzAdvReportPurchaseProduct.setSales14d(item.getBigDecimal("sales14d"));
                amzAdvReportPurchaseProduct.setSalesClicks14d(item.getBigDecimal("salesClicks14d"));
                amzAdvReportPurchaseProduct.setOrders14d(item.getInteger("orders14d"));
                amzAdvReportPurchaseProduct.setUnitsSold14d(item.getInteger("unitsSold14d"));
                amzAdvReportPurchaseProduct.setNewToBrandSales14d(item.getBigDecimal("newToBrandSales14d"));
                amzAdvReportPurchaseProduct.setNewToBrandPurchases14d(item.getInteger("newToBrandPurchases14d"));
                amzAdvReportPurchaseProduct.setNewToBrandUnitsSold14d(item.getInteger("newToBrandUnitsSold14d"));
                amzAdvReportPurchaseProduct.setNewToBrandSalesPercentage14d(item.getBigDecimal("newToBrandSalesPercentage14d"));
                amzAdvReportPurchaseProduct.setNewToBrandPurchasesPercentage14d(item.getBigDecimal("newToBrandPurchasesPercentage14d"));
                amzAdvReportPurchaseProduct.setNewToBrandUnitsSoldPercentage14d(item.getBigDecimal("newToBrandUnitsSoldPercentage14d"));
                amzAdvReportPurchaseProduct.setUnitsSoldClicks14d(item.getInteger("unitsSoldClicks14d"));
                amzAdvReportPurchaseProduct.setKindleEditionNormalizedPagesRead14d(item.getInteger("kindleEditionNormalizedPagesRead14d"));
                amzAdvReportPurchaseProduct.setKindleEditionNormalizedPagesRoyalties14d(item.getBigDecimal("kindleEditionNormalizedPagesRoyalties14d"));
                amzAdvReportPurchaseProduct.setOpttime(new Date());
                listPurchase.add(amzAdvReportPurchaseProduct);
                if (listPurchase.size() >= 2000) {
                    amzAdvReportAdsHsaMapper.insertBatchPurchase( listPurchase);
                    listPurchase.clear();
                }
            }
            if (listPurchase.size() >0) {
                amzAdvReportAdsHsaMapper.insertBatchPurchase( listPurchase);
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
