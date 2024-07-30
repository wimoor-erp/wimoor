package com.wimoor.amazon.product.controller;


import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.product.pojo.dto.ProductInAftersaleDTO;
import com.wimoor.amazon.product.service.IProductInAftersaleService;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-11-28
 */
@Api(tags = "产品销量计算后的补货和发货规划接口")
@RestController
@RequestMapping("/api/v1/product/salesplanafter")
@RequiredArgsConstructor
public class ProductInAftersaleController {
	final IProductInAftersaleService iProductInAftersaleService;
	
	@GetMapping("/summary")
	public Result<List<Map<String, Object>>> summaryAction(String groupid) {
		UserInfo userinfo = UserInfoContext.get();
			return Result.success(iProductInAftersaleService.getSummary(userinfo.getCompanyid(), groupid));
		
	}
	
	@PostMapping("/list")
	public Result<IPage<Map<String, Object>>> listAction(@RequestBody ProductInAftersaleDTO dto) {
		UserInfo userinfo = UserInfoContext.get();
		dto.setShopid(userinfo.getCompanyid());
		if(StrUtil.isNotBlank(dto.getSearch())) {
			dto.setSearch("%"+dto.getSearch().trim()+"%");
		}else {
			dto.setSearch(null);
		}
		if(StrUtil.isNotBlank(dto.getSort())) {
			dto.setSort("`"+dto.getSort()+"`");
		}
		return Result.success(iProductInAftersaleService.findList(dto.getPage(), dto));
		
	}
	
}

