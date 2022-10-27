package com.wimoor.sys.email.contorller;

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
import com.wimoor.sys.email.service.impl.MailService;

import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Api(tags = "邮件处理")
@RestController
@RequestMapping("/api/v1/email")
@Slf4j
@RequiredArgsConstructor
public class SendEmailController {
	
	 private final ISysUserService iSysUserService;
	 private final ISysUserRoleService iSysUserRoleService;
	 private final MailService mailService;
	    /**
	     * 提供用于用户登录认证信息
	     */
	    @ApiOperation(value = "根据登录用户ID获取用户名称等信息")
	    @GetMapping("/sendSimple")
	    public Result<?> sendSimpleMail(String from,String to,String cc,String subject,String content) {
	    	mailService.sendSimpleMail(from, to, cc, subject,content);
	    	return Result.success();
	    }
	    
 
}
