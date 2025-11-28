package com.wimoor.amazon.product.service;

import com.amazon.spapi.model.customerfeedback.*;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.product.pojo.entity.AmzProductRefresh;
import com.wimoor.amazon.product.pojo.entity.ProductInReview;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author liufei
* @description 针对表【t_product_in_review】的数据库操作Service
* @createDate 2025-07-09 17:29:52
*/
public interface IProductInReviewService extends IService<ProductInReview> {
    void capture(AmazonAuthority amazonAuthority, AmzProductRefresh skuRefresh);
    public ItemReviewTopicsResponse       getItemReviewTopics(String  amazonauthid, String asin, String marketplaceid, String sortBy);
    public ItemReviewTrendsResponse       getItemReviewTrends(String  amazonauthid, String asin, String marketplaceid);
    public BrowseNodeResponse             getItemBrowseNode(String amazonauthid, String asin, String marketplaceid);
    public BrowseNodeReviewTopicsResponse getBrowseNodeReviewTopics(String amazonauthid, String marketplaceid, String browsenode, String sortby) ;
    public BrowseNodeReviewTrendsResponse getBrowseNodeReviewTrends(String amazonauthid, String marketplaceid, String browsenode);
    public BrowseNodeReturnTopicsResponse getBrowseNodeReturnTopics(String amazonauthid, String marketplaceid, String browsenode);
    public BrowseNodeReturnTrendsResponse getBrowseNodeReturnTrends(String amazonauthid, String marketplaceid, String browsenode);
}
