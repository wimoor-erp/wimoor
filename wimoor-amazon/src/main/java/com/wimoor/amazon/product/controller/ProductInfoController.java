package com.wimoor.amazon.product.controller;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.amazon.spapi.model.catalogitems.Item;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.product.pojo.dto.ProductListDTO;
import com.wimoor.amazon.product.pojo.dto.ProductListQuery;
import com.wimoor.amazon.product.pojo.dto.ProductPriceDTO;
import com.wimoor.amazon.product.pojo.entity.ProductInfoStatusDefine;
import com.wimoor.amazon.product.pojo.vo.AmzProductListVo;
import com.wimoor.amazon.product.pojo.vo.ProductInfoListVo;
import com.wimoor.amazon.product.pojo.vo.ProductPriceVo;
import com.wimoor.amazon.product.service.IProductCaptureCatalogItemService;
import com.wimoor.amazon.product.service.IProductInOptService;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.common.mybatisplus.MysqlGenerator;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.common.user.UserLimitDataType;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 产品信息 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-27
 */
@Api(tags = "产品接口")
@RestController
@RequestMapping("/api/v1/report/product/productInfo")
public class ProductInfoController {
	@Resource
	IMarketplaceService marketplaceService;
	@Autowired
	IProductCaptureCatalogItemService iProductCaptureCatalogItemService;
	@Resource
	public IAmazonAuthorityService amazonAuthorityService;
	@Autowired
	IAmazonGroupService iAmazonGroupService;
	@Autowired
	IProductInfoService iProductInfoService;
	@Autowired
	IProductInOptService iProductInOptService;
	    @ApiOperation(value = "创建对象")
	    @ApiImplicitParams({
            @ApiImplicitParam(name = "table", value = "数据库的表明", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "pkg", value = "对应业务的包如product,orders", paramType = "query", dataType = "String")
        })
	    @GetMapping("/createpojo")
	    public Result<String> createPojoAction(String table,String pkg) {
	    	MysqlGenerator.autoGenerator(table, pkg);
	        return Result.success("true");
	    }
	    
	    @GetMapping("/getDim")
		public Result<Item> getAsinItem(String marketplaceid,String asin) {
					    	UserInfo user = UserInfoContext.get();
							String shopid = user.getCompanyid();
							Marketplace marketPlace = marketplaceService.findMapByMarketplaceId().get(marketplaceid);
							AmazonAuthority amazonAuthority = null;
							List<AmazonAuthority>  authlist=amazonAuthorityService.selectByShopAndMarket(shopid,marketplaceid);
							if(authlist==null||authlist.size()==0) {
								authlist = amazonAuthorityService.selectByMarket(marketplaceid);
							}
							int d=(int)(Math.random()*100000)%authlist.size();
							if(d-2>0) {
								d=d-2;
							}
					        amazonAuthority =authlist.get(d);
							amazonAuthority.setMarketPlace(marketPlace);
							Item response = iProductCaptureCatalogItemService.captureCatalogProductDim(amazonAuthority,asin,Arrays.asList(marketPlace.getMarketplaceid()));
						    return Result.success(response);
		       }
	    
		@PostMapping("/productList")
		public Result<IPage<AmzProductListVo>> productListAction(@RequestBody ProductListQuery query) {
			String search =query.getSearch();
			String searchtype = query.getSearchtype();
			Map<String, Object> parameter = new HashMap<String, Object>();
			parameter.put("searchtype", searchtype);
			parameter.put("search", search != null && !search.isEmpty() ? "%" + search.trim() + "%" : null);
			String marketplace = query.getMarketplace();
			String disable = query.getDisable();
			if (!"all".equals(disable)) {
				parameter.put("disable", disable);
			}
			String changeRate = query.getChangeRate();
			String color = query.getColor();
			String isfba =query.getIsfba();
			String groupid = query.getGroupid();
			String ownerid = query.getOwnerid();
			String isparent=query.getIsparent();
			String parentasin=query.getParentasin();

			String name=query.getName();
			String remark=query.getRemark();
			String category=query.getCategory();
			String isbadreview=query.getIsbadreview();
			String salestatus=query.getSalestatus();
			
			parameter.put("marketplace", marketplace != null && !marketplace.isEmpty() && !"all".equals(marketplace) ? marketplace.trim() : null);
			parameter.put("isfba", isfba != null && !isfba.isEmpty() ? isfba.trim() + "%" : null);
			String sku =query.getSku();
			if (StrUtil.isEmpty(sku)) {
				parameter.put("sku", null);
			} else {
				sku = sku.trim();
				parameter.put("sku", "%" + sku + "%");
			}
			parameter.put("changeRate", StrUtil.isNotEmpty(changeRate) ? changeRate : null);
			String paralist =query.getParalist();

			if(StrUtil.isNotEmpty(salestatus)&&!"all".equals(salestatus)) {
				parameter.put("salestatus", salestatus);
			}else {
				parameter.put("salestatus", null);
			}
			if (StrUtil.isNotEmpty(color)) {
				parameter.put("color", color);
			}
			if (StrUtil.isNotEmpty(paralist)) {
				String para = paralist.substring(0, paralist.length() - 1).replace(",", " and ");
				parameter.put("paralist", para);
			} else {
				parameter.put("paralist", null);
			}
			if (StrUtil.isNotEmpty(ownerid)) {
				parameter.put("ownerid", ownerid);
			} else {
				parameter.put("ownerid", null);
			}
			if (StrUtil.isNotEmpty(isparent)) {
				parameter.put("isparent", "hasparent");
			} else {
				parameter.put("isparent", null);
			}
			if (StrUtil.isNotEmpty(parentasin)) {
				parameter.put("parentasin", parentasin);
			} else {
				parameter.put("parentasin", null);
			}
			if (StrUtil.isNotEmpty(name)) {
				parameter.put("name", "%"+name.trim()+"%");
			} else {
				parameter.put("name", null);
			}
			if (StrUtil.isNotEmpty(remark)) {
				parameter.put("remark", "%"+remark.trim()+"%");
			} else {
				parameter.put("remark", null);
			}
			if (StrUtil.isNotEmpty(category)) {
				parameter.put("category", category);
			} else {
				parameter.put("category", null);
			}
			if (StrUtil.isNotEmpty(isbadreview)&&"true".equals(isbadreview)) {
				parameter.put("isbadreview", isbadreview);
			} else {
				parameter.put("isbadreview", null);
			}
			UserInfo userinfo = UserInfoContext.get();
			if (userinfo.isLimit(UserLimitDataType.owner)) {
				parameter.put("myself", userinfo.getId());
    		}
			if(!"all".equals(groupid)&&StrUtil.isNotEmpty(groupid)) {
				if(StrUtil.isNotEmpty(marketplace)) {
				 AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplace);
				 if(auth!=null) {
					 parameter.put("amazonAuthId",auth.getId());
				 }
				} 
			} 
			if (StrUtil.isNotEmpty(groupid)) {
				if("all".equals(groupid)) {
					List<AmazonGroup> groupList = iAmazonGroupService.getGroupByUser(userinfo);
					parameter.put("groupList", groupList);
				}else {
					parameter.put("groupid", groupid);
				}
			} else {
				parameter.put("groupid", null);
			}
			IPage<AmzProductListVo> list = iProductInfoService.getListByUser(userinfo,query, parameter);
			return Result.success(list);
		}
		
		@PostMapping("/getProductInfoList")
		public Result<IPage<ProductInfoListVo>> getListDataAction(@RequestBody ProductListDTO dto) {
			UserInfo user = UserInfoContext.get();
			dto.setShopid(user.getCompanyid());
			IPage<ProductInfoListVo> list=iProductInfoService.findByCondition(dto);
			return Result.success(list);
		}
		
		//获取当前shopid下的负责人
		@GetMapping("/getOwnerList")
		public Result<List<Map<String,Object>>> getOwnerListAction() {
			UserInfo user = UserInfoContext.get();
			String shopid = user.getCompanyid();
			List<Map<String,Object>> list=iProductInfoService.getOwnerList(shopid);
			return Result.success(list);
		}
		
		//获取当前shopid下的产品状态
		@GetMapping("/getProStatusList")
		public Result<List<ProductInfoStatusDefine>> getProStatusListAction() {
			UserInfo user = UserInfoContext.get();
			String shopid = user.getCompanyid();
			List<ProductInfoStatusDefine> list=iProductInfoService.getProStatusList(shopid);
			return Result.success(list);
		}
		
		//产品调价
		@GetMapping("/changeProPrice")
		public Result<Map<String, Object>> changProductPriceAction(String pid,String currency,String changeprice,
				String businessprice,String businesstype,
				String businesslist,String submittype,String oldprice,String standprice,
				String startTime,String endTime,String ftype) {
			UserInfo user = UserInfoContext.get();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("productid", pid);
			map.put("currency", currency);
			map.put("standardPrice", oldprice);
			map.put("standprice", standprice);
			map.put("submittype", submittype);//save deletebusiness
			if ("isFortime".equals(ftype)) {
				map.put("salesPrice", changeprice);
				map.put("startDate", startTime);
				map.put("endDate", endTime);
			}
			if("isBusiness".equals(ftype)) {
				map.put("businessprice", businessprice);
				map.put("businesstype", businesstype);
				map.put("businesslist", businesslist);
			}
			Map<String, Object> maps = iProductInfoService.updateProductPrice(user,map,ftype);
			return Result.success(maps);
		}
		
		//修改调价公告
		@GetMapping("/updateRemark")
		public Result<Map<String, String>> updateRemarkAction(String id,String remark,String ftype) {
			Map<String, String> result = new HashMap<String, String>();
			UserInfo user = UserInfoContext.get();
			int i = iProductInfoService.updateUpdateRemark(id, remark, ftype,user.getId());
			if (i > 0) {
				result.put("message", "success");
			} else {
				result.put("message", "fail");
			}
			result.put("data", remark);
			return Result.success(result);
		}
		
		//冻结调价
		@GetMapping("/ProductPriceLocked")
		public Result<Map<String, Object>> saveProductPriceLockedAction(String pid,String price,String days) {
			UserInfo user = UserInfoContext.get();
			Map<String, Object> result = iProductInfoService.saveProductPriceLocked(pid, user.getId(), price, days);
			return Result.success(result);
		}
		
		//取消冻结调价
		@GetMapping("/cancelProductPriceLocked")
		public Result<Map<String, Object>> deleteProductPriceLockedAction(String pid) {
			UserInfo user = UserInfoContext.get();
			Map<String, Object> result = iProductInfoService.deleteProductPriceLocked(pid,user.getId());
			return Result.success(result);
		}
		
		@PostMapping("/priceQueue")
		public Result<IPage<ProductPriceVo> > priceQueueAction(@RequestBody ProductPriceDTO dto) {
			UserInfo user = UserInfoContext.get();
			dto.setShopid(user.getCompanyid());
			IPage<ProductPriceVo> result = iProductInOptService.priceQueue(dto);
			return Result.success(result);
		}
	 
		
}

