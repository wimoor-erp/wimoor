package com.wimoor.amazon.finances.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.finances.pojo.entity.AmzFinUserItem;
import com.wimoor.amazon.finances.service.IAmzFinConfigService;
import com.wimoor.amazon.product.pojo.dto.AmzFinDataDTO;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-10-27
 */
@Api(tags = "其它费用接口")
@RestController
@Component("aAmzFinUserItemController")
@RequiredArgsConstructor
@RequestMapping("/api/v1/amzFinUserItem")
public class AmzFinUserItemController {
     final IAmzFinConfigService amzFinConfigService;
	
	
	 @PostMapping("/getFinDataList")
	   	public Result<IPage<Map<String,Object>>> getFinDataListAction(@RequestBody AmzFinDataDTO dto) {
	    	UserInfo user = UserInfoContext.get();
			String itemid = dto.getItemid();
			String marketplaceid = dto.getMarketplaceid();
			String groupid = dto.getGroupid();
			String fromDate = dto.getFromDate();
			String endDate = dto.getEndDate();
			String sku = dto.getSku();
			String osku = dto.getOsku();
			Map<String, Object> params = new HashMap<String, Object>();
			if(StrUtil.isEmpty(itemid)) {
				params.put("itemid", null);
			}else {
				params.put("itemid", itemid);
			}
			params.put("marketplaceid", marketplaceid);
			if (groupid == null || "all".equals(groupid)) {
				params.put("groupid", null);
			} else {
				params.put("groupid", groupid);
			}
			if (StrUtil.isEmpty(sku)) {
				params.put("sku", null);
			} else {
				params.put("sku", "%" + sku + "%");
			}
			if (StrUtil.isEmpty(osku)) {
				params.put("osku", null);
			} else {
				params.put("osku",osku );
			}
			params.put("fromDate", fromDate.trim());
			params.put("shopid", user.getCompanyid());
			params.put("endDate", endDate.trim());
		    IPage<Map<String, Object>> list = amzFinConfigService.getFinDataList(dto.getPage(), user, params);
		    return Result.success(list);
	   	}
	    
	    @GetMapping("/findFinItemByUser")
	   	public  Result<List<AmzFinUserItem>> findFinItemListAction(){
	    	UserInfo user = UserInfoContext.get();
	   		return Result.success(amzFinConfigService.getFinItemList(user.getCompanyid()));
	   	}
	    
	    
	    
	    
	    
	    
	    
}

