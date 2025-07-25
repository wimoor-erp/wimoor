package com.wimoor.amazon.product.controller;

import com.wimoor.amazon.product.service.IProductInReviewService;
import com.wimoor.amazon.product.service.IProductRankService;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api(tags = "产品Review")
@RestController
@SystemControllerLog("商品Review")
@RequestMapping("/api/v1/report/product/review")
public class ProductReviewController {

    @Autowired
    IProductInReviewService iProductInReviewService;

    @GetMapping("/getItemReviewTopics")
    public Result<?> getItemReviewTopicsAction(String amazonauthid,String asin,String marketplaceid,String sortby) {
        return Result.success(iProductInReviewService.getItemReviewTopics(amazonauthid,asin,marketplaceid,sortby));
    }
    @GetMapping("/getItemReviewTrends")
    public Result<?> getItemReviewTrendsAction(String amazonauthid,String asin,String marketplaceid) {
        return Result.success(iProductInReviewService.getItemReviewTrends(amazonauthid,asin,marketplaceid));
    }
    @GetMapping("/getItemBrowseNode")
    public Result<?> getItemBrowseNodeAction(String amazonauthid,String asin,String marketplaceid) {
        return Result.success(iProductInReviewService.getItemBrowseNode(amazonauthid,asin,marketplaceid));
    }
    @GetMapping("/getBrowseNodeReviewTopics")
    public Result<?> getBrowseNodeReviewTopicsAction(String amazonauthid,String marketplaceid,String browsenode,String sortby) {
        return Result.success(iProductInReviewService.getBrowseNodeReviewTopics(amazonauthid,marketplaceid,browsenode,sortby));
    }
    @GetMapping("/getBrowseNodeReviewTrends")
    public Result<?> getBrowseNodeReviewTrendsAction(String amazonauthid,String marketplaceid,String browsenode) {
        return Result.success(iProductInReviewService.getBrowseNodeReviewTrends(amazonauthid,marketplaceid,browsenode));
    }
    @GetMapping("/getBrowseNodeReturnTopics")
    public Result<?> getBrowseNodeReturnTopicsAction(String amazonauthid,String marketplaceid,String browsenode) {
        return Result.success(iProductInReviewService.getBrowseNodeReturnTopics(amazonauthid,marketplaceid,browsenode));
    }
    @GetMapping("/getBrowseNodeReturnTrends")
    public Result<?> getBrowseNodeReturnTrendsAction(String amazonauthid,String marketplaceid,String browsenode) {
        return Result.success(iProductInReviewService.getBrowseNodeReturnTrends(amazonauthid,marketplaceid,browsenode));
    }

}
