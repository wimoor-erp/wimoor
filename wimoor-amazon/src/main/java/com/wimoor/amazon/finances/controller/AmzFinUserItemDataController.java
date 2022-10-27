package com.wimoor.amazon.finances.controller;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.wimoor.amazon.finances.service.IAmzFinConfigService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 客户导入的SKU财务项费用-应用于商品营收其他费用项目导入 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-10-27
 */
@Api(tags = "其它费用接口")
@RestController
@Component("aAmzFinUserItemDataController")
@RequiredArgsConstructor
@RequestMapping("/api/v1/amzFinUserItemData")
public class AmzFinUserItemDataController {
	
	final IAmzFinConfigService amzFinConfigService;
	
	@GetMapping("/saveFinItemData")
	public Result<Map<String, Object>> saveFinItemDataAction(String marketplaceid,String sku,String groupid,String itemid,String currency,
			String byday,String amount) {
		UserInfo user = UserInfoContext.get();
		if(StrUtil.isEmpty(amount)) {
			throw new BizException("请输入金额");
		}
		if(StrUtil.isEmpty(byday)) {
			throw new BizException("请输入日期");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", null);
		params.put("marketplaceid", marketplaceid);
		params.put("sku", sku);
		params.put("groupid", groupid);
		params.put("itemid", itemid);
		params.put("currency", currency);
		params.put("byday", byday);
		params.put("amount", new BigDecimal(amount));
		params.put("user", user);
		int temp = amzFinConfigService.saveFinItemData(params);
		params.remove("user");
		if(temp == 0) {
			params.put("status", "error");
		}else {
			params.put("status", "success");
		}
		return Result.success(params);
	}
}

