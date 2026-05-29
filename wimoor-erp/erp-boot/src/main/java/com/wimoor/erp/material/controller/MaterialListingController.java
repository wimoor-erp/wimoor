package com.wimoor.erp.material.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.material.pojo.entity.MaterialListing;
import com.wimoor.erp.material.service.IMaterialListingService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "产品Listing多语言信息接口")
@RestController
@RequestMapping("/api/v1/material/listing")
@SystemControllerLog("产品Listing信息")
@RequiredArgsConstructor
public class MaterialListingController {

    private final IMaterialListingService materialListingService;

    @ApiOperation("获取产品所有语言的Listing概要")
    @GetMapping("/list")
    public Result<List<MaterialListing>> list(@RequestParam("materialid") String materialid) {
        List<MaterialListing> list = materialListingService.listByMaterialId(materialid);
        return Result.success(list);
    }

    @ApiOperation("获取产品指定语言的完整Listing信息")
    @GetMapping("/get")
    public Result<MaterialListing> get(@RequestParam("materialid") String materialid,
                                       @RequestParam("lang") String lang) {
        MaterialListing listing = materialListingService.getByMaterialIdAndLang(materialid, lang);
        return Result.success(listing);
    }

    @ApiOperation("保存Listing信息")
    @SystemControllerLog("产品Listing信息修改")
    @PostMapping("/save")
    public Result<MaterialListing> save(@RequestBody MaterialListing listing) {
        UserInfo userinfo = UserInfoContext.get();
        listing.setShopid(userinfo.getCompanyid());
        listing.setOperator(userinfo.getId());
        if (listing.getCreator() == null) {
            listing.setCreator(userinfo.getId());
        }
        MaterialListing result = materialListingService.saveListing(listing);
        return Result.success(result);
    }

    @ApiOperation("删除Listing记录")
    @SystemControllerLog("产品Listing信息删除")
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam("id") String id) {
        materialListingService.deleteListing(id);
        return Result.success();
    }
}
