package com.wimoor.amazon.product.controller;


import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.product.pojo.dto.ProductListDTO;
import com.wimoor.amazon.product.pojo.entity.ProductInOpt;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;
import com.wimoor.amazon.product.service.IProductInOptService;
import com.wimoor.amazon.product.service.IProductInOrderService;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.common.user.UserLimitDataType;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 产品信息的订单销售数据 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-27
 */
@Api(tags = "商品分析")
@RestController
@SystemControllerLog("商品分析")
@RequestMapping("/api/v1/report/product/analysis")
@RequiredArgsConstructor
public class ProductAnalysisController {
	@Autowired
	IProductInfoService iProductInfoService;
	final IAmazonAuthorityService amazonAuthorityService;
	final IProductInOrderService iProductInOrderService;
	final IProductInOptService iProductInOptService;
	@PostMapping("/productAsinList")
	public Result<IPage<Map<String, Object>>> productListAction(@RequestBody ProductListDTO query) {
		String search =query.getSearch();
		String searchtype = query.getFtype();
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("searchtype", searchtype);
		parameter.put("search", search != null && !search.isEmpty() ? "%" + search.trim() + "%" : null);
		String marketplaceid = query.getMarketplaceid();
		String groupid = query.getGroupid();
		parameter.put("marketplaceid", marketplaceid != null && !marketplaceid.isEmpty() && !"all".equals(marketplaceid) ? marketplaceid.trim() : null);
		UserInfo userinfo = UserInfoContext.get();
		if (userinfo.isLimit(UserLimitDataType.operations)) {
			parameter.put("myself", userinfo.getId());
		}
		
		parameter.put("shopid", userinfo.getCompanyid());
		if(!"all".equals(groupid)&&StrUtil.isNotEmpty(groupid)) {
			if(StrUtil.isNotEmpty(marketplaceid)) {
			 AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
			 if(auth!=null) {
				 parameter.put("amazonAuthId",auth.getId());
			 }
			} 
		} 
		
		if (StrUtil.isBlankOrUndefined(groupid)||"all".equals(groupid)) {
			    parameter.put("groupid", null);
			    if(userinfo.getGroups()!=null&&userinfo.getGroups().size()>0) {
			    	parameter.put("groupList", userinfo.getGroups());
			    }
			}else  {
				parameter.put("groupid", groupid);
			}
		IPage<Map<String, Object>> list = iProductInfoService.getAsinList(query.getPage(),parameter);
		return Result.success(list);
	}
	
	@GetMapping("/productdetail")
	public Result<Map<String, Object>> productListAction(String pid) {
		UserInfo userinfo = UserInfoContext.get();
		return Result.success(iProductInOrderService.selectDetialById(pid, userinfo.getCompanyid()));
	}
	
	@GetMapping("/productdetailByInfo")
	public Result<Map<String, Object>> productdetailByInfoAction(String sku,String marketplaceid,String sellerid,String groupid) {
		UserInfo userinfo = UserInfoContext.get();
		AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
		if(auth!=null) {
			LambdaQueryWrapper<ProductInfo> queryWrapper=new LambdaQueryWrapper<ProductInfo>();
			queryWrapper.eq(ProductInfo::getAmazonAuthId, auth.getId());
			queryWrapper.eq(ProductInfo::getMarketplaceid, marketplaceid);
			queryWrapper.eq(ProductInfo::getSku, sku);
			ProductInfo info = iProductInfoService.getOne(queryWrapper);
			if(info!=null) {
				return Result.success(iProductInOrderService.selectDetialById(info.getId(), userinfo.getCompanyid()));
			}else {
				return Result.failed();
			}
		}else {
			return Result.failed();
		}
		
	}
	 
	
	@SystemControllerLog("修改分析备注")
	@GetMapping("/updateAnyRemark")
	public Result<?> updateAnyRemarkAction(String pid,String remark) {
		ProductInOpt opt = iProductInOptService.getById(pid);
		 if(opt!=null) {
			 opt.setRemarkAnalysis(remark);
			 return Result.success(iProductInOptService.updateById(opt));
		 }else {
			 opt =new ProductInOpt();
			 opt.setPid(new BigInteger(pid));
			 opt.setRemarkAnalysis(remark);
			 return Result.success(iProductInOptService.save(opt));
		 }
	}
	
	@GetMapping("/getChartData")
	public Result<List<Map<String, Object>>> getChartDataAction(String sku,String marketplaceid,String groupid,String ftype,String fromDate,
			String endDate) {
		List<Map<String, Object>> maps = null;
		UserInfo user = UserInfoContext.get();
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("shopid", user.getCompanyid());
		parameter.put("marketplace", marketplaceid != null && !marketplaceid.isEmpty() ? marketplaceid.trim() : null);
		parameter.put("groupid", groupid != null && !groupid.isEmpty() ? groupid.trim() : null);

		if (StrUtil.isEmpty(groupid) || StrUtil.isEmpty(marketplaceid)) {
			throw new BizException("必须有店铺和站点");
		}
		AmazonAuthority amazonAuthority = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
		if (amazonAuthority != null) {
			parameter.put("amazonAuthId", amazonAuthority.getId());
		}
		parameter.put("userid", user.getId());
		String beginDate = fromDate;
		Map<String, Integer> ftypeset = new HashMap<String, Integer>();
		if ("sales".equals(ftype)) {
			ftypeset.put("uns", 0);
			ftypeset.put("ods", 1);
		} else if ("hisrank".equals(ftype)) {
			ftypeset.put("rnks", 0);
		} else {
			String[] ftypeStr = ftype.split(",");
			for (int i = 0; i < ftypeStr.length; i++) {
				if(StrUtil.isNotEmpty(ftypeStr[i])) {
					ftypeset.put(ftypeStr[i], i);
				}
			}
		}
		if (endDate != null) {
			endDate = endDate.substring(0, 10);
		}
		if (beginDate != null) {
			beginDate = beginDate.substring(0, 10);
		}
		parameter.put("endDate", endDate);
		parameter.put("beginDate", beginDate);
		parameter.put("sku", sku);
		if (StrUtil.isNotEmpty(ftype)) {
			maps = iProductInOrderService.getChartData(ftypeset, parameter, user);
		} else {
			maps = null;
		}
		return Result.success(maps);
	}

}

