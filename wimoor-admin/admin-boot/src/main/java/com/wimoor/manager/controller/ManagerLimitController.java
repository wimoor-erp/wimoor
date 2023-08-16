package com.wimoor.manager.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.admin.pojo.dto.QueryShopNameDTO;
import com.wimoor.admin.service.ISysUserService;
import com.wimoor.common.result.Result;
import com.wimoor.manager.pojo.entity.ManagerLimit;
import com.wimoor.manager.service.IManagerLimitService;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-09-27
 */
@Api(tags = "用户接口")
@RestController
@RequestMapping("/api/v1/managerLimit")
@RequiredArgsConstructor
public class ManagerLimitController {
   final IManagerLimitService iManagerLimitService;
   final ISysUserService iSysUserService;
   @PostMapping("/queryByShopName")
	public Result<?> queryByShopName(@RequestBody QueryShopNameDTO dto)  {
		String name = dto.getName();
		if (StrUtil.isEmpty(name)) {
			name = null;
		} else {
			name = name + "%";
		}
		String roleid = dto.getRoleid();
		String pkgid = dto.getPkgid();
		if (StrUtil.isEmpty(pkgid) || "all".equals(pkgid)) {
			pkgid = null;
		}
		if (StrUtil.isEmpty(roleid)) {
			roleid = "all";
		}
		if ("all".equals(roleid))
			roleid = null;
		 
		
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("name", name);
		param.put("pkgid", pkgid);
		param.put("roleid", roleid);
		param.put("adminid", iSysUserService.getAdminid());
		
		IPage<Map<String, Object>> list = iManagerLimitService.getCompanyList(dto.getPage(),param);
		return Result.success(list);
	}
   
	@GetMapping("/detail")
	public Result<ManagerLimit> list(String shopid)  {
	     return Result.success(iManagerLimitService.lambdaQuery().eq(ManagerLimit::getShopId, shopid).one());
	}
	
	@PostMapping("/update")
	public Result<?> save(@RequestBody ManagerLimit ml)  {
	     return Result.success(iManagerLimitService.save(ml));
	}
	
	@GetMapping("/summary")
	public Result<List<Map<String,Object>>> list()  {
	     return Result.success(iManagerLimitService.summary());
	}
	
}

