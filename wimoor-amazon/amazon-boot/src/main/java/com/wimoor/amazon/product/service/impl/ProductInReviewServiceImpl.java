package com.wimoor.amazon.product.service.impl;

import com.amazon.spapi.SellingPartnerAPIAA.LWAException;
import com.amazon.spapi.api.CustomerFeedbackApi;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.customerfeedback.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.impl.ApiBuildService;
import com.wimoor.amazon.product.pojo.entity.AmzProductRefresh;
import com.wimoor.amazon.product.pojo.entity.ProductInReview;
import com.wimoor.amazon.product.service.IAmzProductRefreshService;
import com.wimoor.amazon.product.service.IProductInReviewService;
import com.wimoor.amazon.product.mapper.ProductInReviewMapper;
import com.wimoor.amazon.product.service.IProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
* @author liufei
* @description 针对表【t_product_in_review】的数据库操作Service实现
* @createDate 2025-07-09 17:29:52
*/
@Service
public class ProductInReviewServiceImpl extends ServiceImpl<ProductInReviewMapper, ProductInReview>
    implements IProductInReviewService {
    @Autowired
    @Lazy
    IAmazonAuthorityService amazonAuthorityService;

    @Autowired
    @Lazy
    IProductInfoService iProductInfoService;

    @Autowired
    @Lazy
    IAmzProductRefreshService iAmzProductRefreshService;

    @Autowired
    ApiBuildService apiBuildService;
    @Override
    public void capture(AmazonAuthority amazonAuthority, AmzProductRefresh skuRefresh) {
        CustomerFeedbackApi api = apiBuildService.getCustomerFeedbackApi(amazonAuthority);
        try {
            ItemReviewTopicsResponse response = api.getItemReviewTopics(skuRefresh.getAsin(), skuRefresh.getMarketplaceid(), "STAR_RATING_IMPACT");
            System.out.println(response.toString());

        } catch (ApiException e) {
            throw new RuntimeException(e);
        } catch (LWAException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ItemReviewTopicsResponse getItemReviewTopics(String  amazonauthid, String asin, String marketplaceid,String sortBy) {
        AmazonAuthority amazonAuthority = this.amazonAuthorityService.getById(amazonauthid);
        CustomerFeedbackApi api = apiBuildService.getCustomerFeedbackApi(amazonAuthority);
        try {
            ItemReviewTopicsResponse response = api.getItemReviewTopics(asin, marketplaceid, sortBy);
            return response;

        } catch (ApiException e) {
            throw new RuntimeException(e);
        } catch (LWAException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ItemReviewTrendsResponse getItemReviewTrends(String  amazonauthid, String asin, String marketplaceid) {
        AmazonAuthority amazonAuthority = this.amazonAuthorityService.getById(amazonauthid);
        CustomerFeedbackApi api = apiBuildService.getCustomerFeedbackApi(amazonAuthority);
        try {
            ItemReviewTrendsResponse response = api.getItemReviewTrends(asin, marketplaceid);
            return response;

        } catch (ApiException e) {
            throw new RuntimeException(e);
        } catch (LWAException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BrowseNodeResponse getItemBrowseNode(String amazonauthid, String asin, String marketplaceid) {
        AmazonAuthority amazonAuthority = this.amazonAuthorityService.getById(amazonauthid);
        CustomerFeedbackApi api = apiBuildService.getCustomerFeedbackApi(amazonAuthority);
        try {
            BrowseNodeResponse response = api.getItemBrowseNode(asin, marketplaceid);
            return response;
        } catch (ApiException e) {
            throw new RuntimeException(e);
        } catch (LWAException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BrowseNodeReviewTopicsResponse getBrowseNodeReviewTopics(String amazonauthid, String marketplaceid, String browsenode, String sortby) {
        AmazonAuthority amazonAuthority = this.amazonAuthorityService.getById(amazonauthid);
        CustomerFeedbackApi api = apiBuildService.getCustomerFeedbackApi(amazonAuthority);
        try {
            BrowseNodeReviewTopicsResponse response = api.getBrowseNodeReviewTopics(browsenode, marketplaceid, sortby);
            return response;
        } catch (ApiException e) {
            throw new RuntimeException(e);
        } catch (LWAException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BrowseNodeReviewTrendsResponse getBrowseNodeReviewTrends(String amazonauthid, String marketplaceid, String browsenode) {
        AmazonAuthority amazonAuthority = this.amazonAuthorityService.getById(amazonauthid);
        CustomerFeedbackApi api = apiBuildService.getCustomerFeedbackApi(amazonAuthority);
        try {
            BrowseNodeReviewTrendsResponse response = api.getBrowseNodeReviewTrends(browsenode, marketplaceid);
            return response;
        } catch (ApiException e) {
            throw new RuntimeException(e);
        } catch (LWAException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public BrowseNodeReturnTopicsResponse getBrowseNodeReturnTopics(String amazonauthid, String marketplaceid, String browsenode) {
        AmazonAuthority amazonAuthority = this.amazonAuthorityService.getById(amazonauthid);
        CustomerFeedbackApi api = apiBuildService.getCustomerFeedbackApi(amazonAuthority);
        try {
            BrowseNodeReturnTopicsResponse response = api.getBrowseNodeReturnTopics(browsenode, marketplaceid);
            return response;
        } catch (ApiException e) {
            throw new RuntimeException(e);
        } catch (LWAException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BrowseNodeReturnTrendsResponse getBrowseNodeReturnTrends(String amazonauthid, String marketplaceid, String browsenode) {
        AmazonAuthority amazonAuthority = this.amazonAuthorityService.getById(amazonauthid);
        CustomerFeedbackApi api = apiBuildService.getCustomerFeedbackApi(amazonAuthority);
        try {
            BrowseNodeReturnTrendsResponse response = api.getBrowseNodeReturnTrends(browsenode, marketplaceid);
            return response;
        } catch (ApiException e) {
            throw new RuntimeException(e);
        } catch (LWAException e) {
            throw new RuntimeException(e);
        }
    }


}




