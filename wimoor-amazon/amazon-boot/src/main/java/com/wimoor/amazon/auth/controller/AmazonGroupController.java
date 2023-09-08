package com.wimoor.amazon.auth.controller;

import java.util.Date;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.common.user.UserType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
 

@Api(tags = "店铺接口")
@SystemControllerLog("店铺")
@RestController
@RequestMapping("/api/v1/amzgroup")
//@Slf4j
@RequiredArgsConstructor
public class AmazonGroupController {
	 private final IAmazonAuthorityService iAmazonAuthorityService;
	 private final IAmazonGroupService iAmazonGroupService;
 
	    @ApiOperation(value = "获取店铺")
	    @GetMapping("/list")
	    public Result<List<AmazonGroup>> getAmazonGroupAction() {
	    	UserInfo userinfo = UserInfoContext.get();
	    	List<AmazonGroup> result =iAmazonGroupService.list(
	    			new LambdaQueryWrapper<AmazonGroup>().eq(AmazonGroup::getShopid, userinfo.getCompanyid())
	    			.eq(AmazonGroup::getIsdelete, false).orderByAsc(AmazonGroup::getFindex)
	    			);
	        return Result.success(result);
	    }
	    
	    @ApiOperation(value = "获取店铺")
	    @GetMapping("/listByAdmin")
	    public Result<List<AmazonGroup>> getAmazonGroupByAdminAction(String shopid) {
	    	UserInfo userinfo = UserInfoContext.get();
	    	if(userinfo.getFtype().equals(UserType.admin.getCode())||userinfo.getFtype().equals(UserType.support.getCode())) {
	    		List<AmazonGroup> result =iAmazonGroupService.list(
		    			new LambdaQueryWrapper<AmazonGroup>().eq(AmazonGroup::getShopid, shopid)
		    			.eq(AmazonGroup::getIsdelete, false).orderByAsc(AmazonGroup::getFindex)
		    			);
		        return Result.success(result);
	    	}else {
	    		throw new BizException("权限不够无法根据公司ID查询");
	    	}
	    	
	    }
	    
	    @ApiOperation(value = "获取店铺")
	    @GetMapping("/id/{id}")
	    public Result<AmazonGroup> getAmazonGroupByIdAction(@PathVariable String id) {
	    	AmazonGroup result =iAmazonGroupService.getById(id);
	        return Result.success(result);
	    }
 
	    @ApiOperation(value = "删除店铺")
	    @DeleteMapping("/delete/{id}")
	    public Result<AmazonGroup> delAmazonGroupByIdAction(@PathVariable String id) {
	    	UserInfo userinfo = UserInfoContext.get();
	    	List<AmazonAuthority> list = iAmazonAuthorityService.list(
	    			new LambdaQueryWrapper<AmazonAuthority>()
	    			.eq(AmazonAuthority::getShopId, userinfo.getCompanyid())
	    			.eq(AmazonAuthority::getGroupid, id)
	    			.eq(AmazonAuthority::getDisable, Boolean.FALSE));
	    	if(list.size()>0) {
	    		throw new BizException("该店铺下存在授权无法删除");
	    	}
	    	boolean result=false;
	    	AmazonGroup group = iAmazonGroupService.getById(id);
	    	if(group!=null) {
	    		group.setIsdelete(true);
	    		group.setOpttime(new Date());
	    		group.setOperator(userinfo.getId());
	    		result = iAmazonGroupService.updateById(group);
	    	}
	        return Result.judge(result);
	    }
	    
	    @ApiOperation("店铺更新")
		@SystemControllerLog("店铺利润计算方案更新")
	    @PostMapping("/updateBatch")
		@Caching(evict={@CacheEvict(value = "defaultProfitCfgCache", allEntries = true),@CacheEvict(value = "profitCfgCache", allEntries = true)})
	    public Result<List<AmazonGroup>> updateAmazonGroupConfigAction(@RequestBody List<AmazonGroup> groups) {
	    	UserInfo userinfo = UserInfoContext.get();
	        boolean flag=false;
	        for(AmazonGroup group:groups) {
	        	group.setOpttime(new Date());
				group.setOperator(userinfo.getId());
				flag=iAmazonGroupService.updateById(group);
	        }
	        return Result.judge(flag);
	    }  
	    
		@ApiOperation("保存")
		@SystemControllerLog("保存")
	    @PutMapping("/save")
	    public Result<List<AmazonGroup>> saveAmazonGroupAction(@RequestBody AmazonGroup group) {
	    	UserInfo userinfo = UserInfoContext.get();
	        boolean flag=false;
	        QueryWrapper<AmazonGroup> queryWrapper=new QueryWrapper<AmazonGroup>();
	        queryWrapper.eq("shopid", userinfo.getCompanyid());
	        queryWrapper.eq("name", group.getName());
			AmazonGroup oldgroup = iAmazonGroupService.getOne(queryWrapper);
			if(oldgroup!=null) {
				group.setId(oldgroup.getId());
			}  
			if(group.idIsNULL()) {
				group.setCreator(userinfo.getId());
				group.setOperator(userinfo.getId());
				group.setOpttime(new Date());
				group.setShopid(userinfo.getCompanyid());
				group.setCreatetime(new Date());
				group.setIsdelete(false);
				flag=iAmazonGroupService.save(group);
			}else {
				group.setOpttime(new Date());
				group.setOperator(userinfo.getId());
				group.setIsdelete(false);
				group.setShopid(userinfo.getCompanyid());
				flag=iAmazonGroupService.updateById(group);
			}
	        return Result.judge(flag);
	    }  
}
