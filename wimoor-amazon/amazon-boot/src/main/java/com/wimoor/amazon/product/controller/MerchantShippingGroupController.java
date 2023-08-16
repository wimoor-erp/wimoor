package com.wimoor.amazon.product.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.product.pojo.dto.ProductListingItemDTO;
import com.wimoor.amazon.product.pojo.entity.MerchantShippingGroup;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;
import com.wimoor.amazon.product.service.IMerchantShippingGroupService;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2023-07-12
 */
 

@Api(tags = "运费模板接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product/merchantShippingGroup")
public class MerchantShippingGroupController {
	final IMerchantShippingGroupService iMerchantShippingGroupServices;
	final IMarketplaceService marketplaceService;
	final IProductInfoService iProductInfoService;
	final IAmazonAuthorityService amazonAuthorityService;
    @PostMapping("/list")
    public Result<?> getAllGroupAction(@RequestBody ProductListingItemDTO dto) {
    	 setAuthority(dto);
    	 List<MerchantShippingGroup> list=iMerchantShippingGroupServices.listGroups(dto.getAmazonauthid(),dto.getMarketplaceids().get(0));
        return Result.success(list);
    }
    @PostMapping("/refresh")
    public Result<?> refreshAllGroupAction(@RequestBody ProductListingItemDTO dto) {
    	 setAuthority(dto);
    	 List<MerchantShippingGroup> list=iMerchantShippingGroupServices.refreshListGroups(dto.getAmazonauthid(),dto.getMarketplaceids().get(0));
        return Result.success(list);
    }
    private void setAuthority(ProductListingItemDTO dto) {
		if( !StrUtil.isBlank(dto.getPid())) {
			ProductInfo info = iProductInfoService.getById(dto.getPid());
			AmazonAuthority auth = amazonAuthorityService.getById(info.getAmazonAuthId());
			dto.setAmazonauthid(auth.getId());
			dto.setAsin(info.getAsin());
			dto.setSku(info.getSku());
			dto.setMarketplaceids(Arrays.asList(info.getMarketplaceid()));
			dto.setGroupid(auth.getGroupid());
		}
		else if(StrUtil.isBlank(dto.getAmazonauthid())) {
    		if(dto.getGroupid()!=null) {
    			AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(dto.getGroupid(), dto.getMarketplaceids().get(0));
    			if(auth!=null) {
    				dto.setAmazonauthid(auth.getId());
    			}else {
        			throw new BizException("无法定为授权");
        		}
    		}else {
    			throw new BizException("无法定为授权");
    		}
    	}
	}
}

