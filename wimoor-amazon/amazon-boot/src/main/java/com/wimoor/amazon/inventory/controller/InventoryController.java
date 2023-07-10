package com.wimoor.amazon.inventory.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.amazon.spapi.model.fbainventory.InventorySummary;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.feed.mapper.AmzSubmitFeedQueueMapper;
import com.wimoor.amazon.feed.pojo.entity.AmzSubmitFeedQueue;
import com.wimoor.amazon.feed.service.ISubmitfeedService;
import com.wimoor.amazon.inventory.pojo.dto.InventorySizeDTO;
import com.wimoor.amazon.inventory.pojo.entity.AmzInventoryCountryReport;
import com.wimoor.amazon.inventory.pojo.entity.InventoryReport;
import com.wimoor.amazon.inventory.pojo.vo.ProductInventoryVo;
import com.wimoor.amazon.inventory.service.IInventorySupplyService;
import com.wimoor.amazon.product.service.IProductInOptService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.common.user.UserLimitDataType;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;

@Api(tags = "库存接口")
@RestController
@RequestMapping("/api/v0/inventry")
public class InventoryController {
	@Autowired
	IMarketplaceService marketplaceService;
	@Autowired
	IAmazonAuthorityService amazonAuthorityService;
	@Autowired
	IInventorySupplyService inventorySupplyService;
	@Autowired
	IProductInOptService productInOptService;
	@Autowired
	ISubmitfeedService submitfeedService;
	@Autowired
	AmzSubmitFeedQueueMapper amzSubmitFeedQueueMapper;
	
	@GetMapping("/getInventorySupply")
	public Result<Map<String, InventorySummary>> getInventorySupplyAction(String  groupid,String marketplaceid ,String skuStr) {
		 
		List<String> list = null;
		if (StrUtil.isNotEmpty(skuStr)) {
			String[] skuarray = skuStr.split(",");
			if (skuarray.length > 0) {
				list = Arrays.asList(skuarray);
			}
		}
		Map<String, InventorySummary> result = null;
		if (list != null && list.size() > 0) {
			AmazonAuthority amazonAuthority = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
			Marketplace marketplace = marketplaceService.selectByPKey(marketplaceid);
			amazonAuthority.setMarketPlace(marketplace);
			result = inventorySupplyService.captureInventorySupplyNew(amazonAuthority, list);
		}
		return Result.success(result);
	}
	
	@GetMapping("/syncInventorySupply")
	public Result<InventoryReport> syncInventorySupplyAction(String  groupid,String marketplaceid ,String skus) {
		List<String> list = null;
		if (StrUtil.isNotEmpty(skus)) {
			String[] skuarray = skus.split(",");
			if (skuarray.length > 0) {
				list = Arrays.asList(skuarray);
			}
		}
		InventoryReport result = null;
		if (list != null && list.size() > 0) {
			AmazonAuthority amazonAuthority = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
			if(amazonAuthority!=null) {
				Marketplace marketplace = marketplaceService.selectByPKey(marketplaceid);
				amazonAuthority.setMarketPlace(marketplace);
				result=inventorySupplyService.syncInventorySupply(amazonAuthority, list);
			}else {
				throw new BizException("店铺参数异常！");
			}
		}
		return Result.success(result);
	}
	
	@GetMapping("/findFBA")
	public Result<List<ProductInventoryVo>> findFBAAction(String  groupid,String marketplaceid ,String sku) {
    	UserInfo userinfo = UserInfoContext.get();
    	String region=marketplaceService.findMapByMarketplaceId().get(marketplaceid).getRegion();
    	if(region.equals("EU")) {
    		marketplaceid="EU";
    	}
		return Result.success(inventorySupplyService.findFBA(groupid, marketplaceid, sku, null, userinfo.getCompanyid()));
	}
	
	@PostMapping("/getSizePro")
	public Result<IPage<Map<String, Object>>> getSizeProAction(@RequestBody InventorySizeDTO dto) {
		UserInfo userinfo = UserInfoContext.get();
		Map<String, Object> param = new HashMap<String, Object>();
		String skuname = dto.getSearch();
		if (StrUtil.isNotEmpty(skuname)) {
			param.put("skuname", "%" + skuname + "%");
		} else {
			param.put("skuname", null);
		}
		param.put("shopid", userinfo.getCompanyid());
		String groupid = dto.getGroupid();
		param.put("groupid", groupid);
		String marketplaceid = dto.getMarketplaceid();
		param.put("marketplaceid", marketplaceid);
		String country = dto.getCountry();
		param.put("country", country);
		String isgtself = dto.getIsgtself();
		param.put("isgtself", isgtself);
		String sizetype = dto.getSizetype();
		param.put("sizetype", sizetype);
		if(userinfo.isLimit(UserLimitDataType.operations)) {
			param.put("owner",userinfo.getId());
		} 
		List<Map<String, Object>> list = productInOptService.findMaterialSizeByCondition(param);
		IPage<Map<String, Object>> pagelist= dto.getListPage(list);
		return Result.success(pagelist);
	}
	
	@GetMapping("/findEUFBA")
	public Result<List<AmzInventoryCountryReport>> findEUFBAAction(String  authid ,String sku) {
		if(StrUtil.isNotEmpty(authid)) {
			return Result.success(inventorySupplyService.findEUFBA(authid, sku));
		}else {
			return Result.success(null);
		}
	}
	
	@GetMapping("/callqueue")
	public void findEUFBAAction2(String authid,String marketplaceid,String queueid) {
		AmazonAuthority auth = amazonAuthorityService.getById(authid);
		Marketplace marketplace = marketplaceService.getById(marketplaceid);
		AmzSubmitFeedQueue queue = amzSubmitFeedQueueMapper.selectById(queueid);
		submitfeedService.callSubmitFeed(auth, marketplace, queue);
	}
	
	
}
