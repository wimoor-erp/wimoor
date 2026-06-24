package com.wimoor.manager.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.admin.pojo.dto.QueryShopNameDTO;
import com.wimoor.admin.service.ISysUserService;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.manager.pojo.entity.ManagerLimit;
import com.wimoor.manager.service.IManagerLimitService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
			name = name.trim() + "%";
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
		ManagerLimit old = iManagerLimitService.lambdaQuery().eq(ManagerLimit::getShopId, ml.getShopId()).one();
		if(old!=null){
			ml.setId(old.getId());
			return Result.success(iManagerLimitService.updateById(ml));
		}else{
			return Result.failed("用户管理信息不存在");
		}

	}

	@PostMapping("/updateRemark")
	public Result<?> saveRemark(@RequestBody ManagerLimit ml)  {
		ManagerLimit old = iManagerLimitService.lambdaQuery().eq(ManagerLimit::getShopId, ml.getShopId()).one();
		old.setRemark(ml.getRemark());
		return Result.success(iManagerLimitService.updateById(old));
	}
	
	@GetMapping("/summary")
	public Result<List<Map<String,Object>>> list()  {
	     return Result.success(iManagerLimitService.summary());
	}
	
	@GetMapping("/getMyManagerLimitmeal")
	public Result<Map<String,Object>> getMyManagerLimitmealAction()  {
		 UserInfo user = UserInfoContext.get();
	     return Result.success(iManagerLimitService.findManagerLimitByShopId(user.getCompanyid()));
	}
	
	
}

