package com.wimoor.admin.controller;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.admin.pojo.entity.SysRole;
import com.wimoor.admin.pojo.entity.SysUser;
import com.wimoor.admin.pojo.entity.SysUserRole;
import com.wimoor.admin.pojo.vo.UserVO;
import com.wimoor.admin.service.ISysUserRoleService;
import com.wimoor.admin.service.ISysUserService;
import com.wimoor.api.admin.pojo.dto.SysUserRoleDTO;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.result.ResultCode;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Api(tags = "用户接口")
@RestController
@RequestMapping("/api/v1/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
	
	 private final ISysUserService iSysUserService;
	 private final ISysUserRoleService iSysUserRoleService;
	 
	    /**
	     * 提供用于用户登录认证信息
	     */
	    @ApiOperation(value = "根据登录用户ID获取用户名称等信息")
	    @GetMapping("/roles/{userid}")
	    public Result<List<SysUserRoleDTO>> getUserRoleById(@PathVariable String userid) {
	    	if(userid!=null) {
	    		  List<SysUserRole> list = iSysUserRoleService.list(new LambdaQueryWrapper<SysUserRole>()
	    	                .eq(SysUserRole::getUserId,new BigInteger(userid)));
	    		  List<SysUserRoleDTO> result=new LinkedList<SysUserRoleDTO>();
	    		  if(list!=null) {
	    			  list.stream().forEach(userrole->{
	    				  SysUserRoleDTO item=new SysUserRoleDTO();
	    				  BeanUtil.copyProperties(userrole, item);
	    				  result.add(item);
	    			  });
	    		  }
		        return Result.success(result);
	    	}else {
	    		return Result.failed(ResultCode.USER_NOT_EXIST);
	    	}
	    }
	    
	    @ApiOperation(value = "列表分页")
	    @ApiImplicitParams({
	            @ApiImplicitParam(name = "page", value = "页码", paramType = "query", dataType = "Long"),
	            @ApiImplicitParam(name = "limit", value = "每页数量", paramType = "query", dataType = "Long"),
	            @ApiImplicitParam(name = "name", value = "用户名", paramType = "query", dataType = "String"),
	    })
	    @GetMapping("/sysrole/list")
	    public Result<List<UserVO>> list(Integer page, Integer limit, String name) {
	        IPage<UserVO> result = iSysUserService.listQuery(new Page<>(page,limit),name);
	        return Result.success(result.getRecords(), result.getTotal());
	    }
	    /**
	     * 提供用于用户登录认证信息
	     */
	    @ApiOperation(value = "根据用户账号（电话或邮箱）获取用户信息")
	    @ApiImplicitParam(name = "account", value = "用户账号", required = true, paramType = "path", dataType = "String")
	    @GetMapping("/sysrole/account/{account}")
	    public Result<UserInfo> getUserByUsername(@PathVariable String account) {
	    	SysUser user = iSysUserService.getUserAllByAccount(account);
	    	if(user!=null) {
	    		user.setPassword("***");
		    	user.setSalt("***");
		    	log.debug(account+"获取所有信息---时间："+new Date());
		    	UserInfo info=iSysUserService.convertToUserInfo(user);
		        return Result.success(info);
	    	}else {
	    		return Result.failed(ResultCode.USER_NOT_EXIST);
	    	}
	    }
	    
	    /**
	     * 提供用于用户登录认证信息
	     */
	    @ApiOperation(value = "根据微信openid与账号密码登陆并同时绑定获取用户信息")
	    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "登陆账号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "登陆密码", paramType = "query", dataType = "String")
        })
	    @GetMapping("/sysrole/verifyAccount")
	    public Result<UserInfo> verifyAccountAction(String account,String password) {
	    	SysUser user = iSysUserService.verifyAccount(account,password);
	    	if(user!=null) {
	    		user.setPassword("***");
		    	user.setSalt("***");
		    	log.debug(account+"获取所有信息---时间："+new Date());
		    	UserInfo info=iSysUserService.convertToUserInfo(user);
		        return Result.success(info);
	    	}else {
	    		return Result.failed(ResultCode.USER_NOT_EXIST);
	    	}
	    }
	    
	    @ApiOperation(value = "根据用户ID获取用户信息")
	    @ApiImplicitParam(name = "ID", value = "用户ID", required = true, paramType = "path", dataType = "String")
	    @GetMapping("/sysrole/userid/{userid}")
	    public Result<UserInfo> getUserByUserId(@PathVariable String userid) {
	    	SysUser user = iSysUserService.getUserAllById(userid);
	    	if(user!=null) {
	    		user.setPassword("***");
		    	user.setSalt("***");
		    	UserInfo info=iSysUserService.convertToUserInfo(user);
		        return Result.success(info);
	    	}else {
	    		return Result.failed(ResultCode.USER_NOT_EXIST);
	    	}
	    }
	    
	    
	    /**
	     * 提供用于用户登录认证信息
	     */
	    @ApiOperation(value = "根据登录用户ID获取用户名称等信息")
	    @GetMapping("/sysrole/info/{userid}")
	    public Result<Map<String,Object>> getUserInfoByUserId(@PathVariable String userid) {
	    	if(userid!=null) {
	    		Map<String, Object> info = iSysUserService.getUserInfoById(userid) ;
		        return Result.success(info);
	    	}else {
	    		return Result.failed(ResultCode.USER_NOT_EXIST);
	    	}
	    }
	    
	    /**
	     * 提供用于用户登录认证信息
	     */
	    @ApiOperation(value = "根据登录用户ID获取用户名称等信息")
	    @GetMapping("/info")
	    public Result<Map<String,Object>> getUserInfoById() {
	    	UserInfo userInfo = UserInfoContext.get();
	    	if(userInfo!=null) {
	    		Map<String, Object> info = iSysUserService.getUserInfoById(userInfo.getId()) ;
		        return Result.success(info);
	    	}else {
	    		return Result.failed(ResultCode.USER_NOT_EXIST);
	    	}
	    }
	    
	    /**
	     * 提供用于用户登录认证信息
	     */
	    @ApiOperation(value = "根据微信openid与账号密码登陆并同时绑定获取用户信息")
	    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid", value = "微信openid", paramType = "query", dataType = "String")
        })
	    @GetMapping("/sysrole/findbyopenid/{openid}")
	    public Result<List<UserInfo>> findUserByOpenid(@PathVariable String openid) {
	    	List<SysUser> userList = iSysUserService.findAppUserByOpenid(openid);
	    	List<UserInfo> result=new LinkedList<UserInfo>();
	    	if(userList!=null) {
	    		for(SysUser user:userList) {
	    			user.setPassword("***");
			    	user.setSalt("***");
			    	UserInfo info=iSysUserService.convertToUserInfo(user);
			    	result.add(info);
	    		}
		        return Result.success(result);
	    	}else {
	    		return Result.failed(ResultCode.USERNAME_OR_PASSWORD_ERROR);
	    	}
	    }
	    /**
	     * 提供用于用户登录认证信息
	     */
	    @ApiOperation(value = "根据微信openid与账号密码登陆并同时绑定获取用户信息")
	    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid", value = "微信openid", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "account", value = "登陆账号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "登陆密码", paramType = "query", dataType = "String")
        })
	    @GetMapping("/sysrole/openidbind")
	    public Result<UserInfo> bindUserByOpenid(String openid,String account,String password) {
	    	try {
	    		SysUser user = iSysUserService.bindOpenId(openid,account,password);
		    	user.setPassword("***");
			    user.setSalt("***");
			    log.debug(account+"获取所有信息---时间："+new Date());
			    UserInfo info=iSysUserService.convertToUserInfo(user);
			    return Result.success(info);
		     
	    	}catch(BizException e) {
	    		return Result.failed(ResultCode.USERNAME_OR_PASSWORD_ERROR,e.getMessage());
	    	}
	    	
	    }
	    

	    
	    @ApiOperation(value = "登录用户注销该用户的账户信息")
	    @GetMapping("/unbindAccount") 
	  		public Object unbindAccountAction(HttpServletRequest request) {
	  	    	UserInfo userInfo = UserInfoContext.get();
	  	    	Map<String,Object> map=new HashMap<String, Object>();
	  			int res=iSysUserService.unbindAccount(userInfo);
	  			if(res>0) {
	  				map.put("isOk", true);
	  				map.put("msg", "注销成功！");
	  			}else {
	  				map.put("isOk", false);
	  				map.put("msg", "注销失败，请联系管理员！");
	  			}
	  		 
	  			 return Result.success(map);
	  		}
	    
	    @ApiOperation(value = "当前微信账号解除和wimoor的绑定")
	    @GetMapping("/unbindWechat") 
		public Object unbindWechatAction(HttpServletRequest request) {
	    	//UserInfo userInfo = UserInfoContext.get();
			String ftype=request.getParameter("ftype");
			String account=request.getParameter("account");
			String openid=request.getParameter("openid");
			Map<String,Object> map=new HashMap<String, Object>();
			List<SysUser> userlist = iSysUserService.findAppUserByOpenid(openid);
			SysUser user = userlist.stream().filter(muser->muser.getAccount().equals(account)).findFirst().get();
	    	UserInfo info=iSysUserService.convertToUserInfo(user);
			int res=iSysUserService.unbindWechat(info,ftype);
			if(res>0) {
				map.put("isOk", true);
				map.put("msg", "解绑成功！");
			}else {
				map.put("isOk", false);
				map.put("msg", "操作失败，或者您当前暂无绑定！");
			}
		 
			 return Result.success(map);
		}
}
