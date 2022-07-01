package com.wimoor.auth.client.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.wimoor.auth.client.api.AdminClientOneFeign;
import com.wimoor.auth.client.config.MyWxConfig;
import com.wimoor.auth.client.config.ShiroConfig;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.HttpClientUtil;
import com.wimoor.common.result.Result;
import com.wimoor.common.result.ResultCode;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.util.StrUtil;
@Controller
public class LoginController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    AdminClientOneFeign adminClientOneFeign;
    @Autowired
    ShiroConfig shiroConfig;
    @Autowired
	MyWxConfig wxconfig;
    @RequestMapping("/mylogout")
	String mylogoutAction(HttpServletRequest request,HttpServletResponse response, Model model) {
		HttpSession session = request.getSession();
		String jsessionid = session.getId();
		try {
			String key = jsessionid;
		    stringRedisTemplate.delete(key);
			Subject subject = SecurityUtils.getSubject();
			subject.logout();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
		      
				response.sendRedirect(ShiroConfig.loginUrl);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		  return null;
	}
    
	@RequestMapping("/getJSession")
	String showLoginAction(HttpServletRequest request,HttpServletResponse response, Model model) {
		HttpSession session = request.getSession();
		String jsessionid = session.getId();
		try {
			Subject subject = SecurityUtils.getSubject();
			if(subject.getPrincipal()!=null) {
				  int expiresIn =3600;
			        String key = jsessionid;
			        String value = (String) subject.getPrincipal();
			        Result<UserInfo> userresult = adminClientOneFeign.getUserByUsername(value);
			        if(Result.isSuccess(userresult)) {
			        	 UserInfo user = userresult.getData();
					     user.setSession(key);
					     String jsonuser = JSONObject.toJSONString(user);
						 stringRedisTemplate.opsForValue().set(key,jsonuser ,  expiresIn ,java.util.concurrent.TimeUnit.SECONDS);
			        }else {
			        	Result.failed("账户不存在或者异常");
			        }
			       
			}else {
				response.sendRedirect(ShiroConfig.loginUrl);
				return null;
			}
			response.sendRedirect(ShiroConfig.uiserver+"/ssologin?jsessionid="+jsessionid);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  return null;
	}
	
	@ResponseBody
	@RequestMapping("/getOpenUserlist")
	public Result<List<UserInfo>> loginWechatAction2(HttpServletRequest request,HttpServletResponse response) {
		String openid = request.getParameter("openid");
		Result<List<UserInfo>> openresult = adminClientOneFeign.findUserByOpenid(openid);
		 if(Result.isSuccess(openresult)&&openresult.getData().size()>0) {
			 List<UserInfo> openUserList=new LinkedList<UserInfo>();
			 for(UserInfo openuser:openresult.getData()) {
				 openuser.setId(null);
				 openUserList.add(openuser);
			 }
			 return Result.success(openUserList);
		 }else {
			 return Result.failed("无法获取用户列表");
		 }
	}
	
	@ResponseBody
	@RequestMapping("/login")
	public Result<UserInfo> loginAction(HttpServletRequest request,HttpServletResponse response) {
		String account = request.getParameter("account");
		String password = request.getParameter("password");
		return adminClientOneFeign.verifyAccountAction(account, password);
	}
	
	
	@ResponseBody
	@RequestMapping("/loginWechat")
	public Result<Map<String,Object>> loginWechatAction(HttpServletRequest request,HttpServletResponse response) {
		String code = request.getParameter("code");
		return this.verifyCodeApp(code);
	}
	
	@ResponseBody
	@RequestMapping("/verifyWechatApp")
	public Result<?> verifyWechatAppAction(HttpServletRequest request,HttpServletResponse response) {
		String openid = request.getParameter("openid");
		String account=request.getParameter("email");
		String password=request.getParameter("password");
		String oldjsessionid=request.getParameter("jsessionid");
	    Result<UserInfo> result = adminClientOneFeign.bindUserByOpenid(openid, account, password);
				//校验成功
	    
	    int expiresIn =3600;
	 	HttpSession session = request.getSession();
		String jsessionid = session.getId();
		if(StrUtil.isNotBlank(oldjsessionid)&& !oldjsessionid.equals(jsessionid)){
			try {
				String key = oldjsessionid;
			    stringRedisTemplate.delete(key);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 			
		}
		if(Result.isSuccess(result)) {
			//shiro免密登陆
			 Map<String,Object> data=new HashMap<String,Object>();
			 UserInfo user = result.getData();
			 user.setSession(jsessionid);
			 String jsonuser = JSONObject.toJSONString(user);
			 stringRedisTemplate.opsForValue().set(jsessionid,jsonuser ,  expiresIn ,java.util.concurrent.TimeUnit.SECONDS);
			 user.setId(null);
			 data.put("openid", openid);
			 data.put("jsessionid", jsessionid);
			 data.put("currentuser", user);
			 return Result.success(data);
		}else {
			 return result;
		}
	
	}
	
	
	@ResponseBody
	@RequestMapping("/changeLoginWechatApp")
	public Result<?> changeLoginWechatAppAction(HttpServletRequest request,HttpServletResponse response) {
		String openid = request.getParameter("openid");
		String account=request.getParameter("account");
		String jsessionid=request.getParameter("jsessionid");
		return loginApp(openid,account,jsessionid);
	}
	
	private Result<Map<String,Object>> loginApp(String openid,String account,String jsessionid){
		Result<List<UserInfo>> result = adminClientOneFeign.findUserByOpenid(openid);
		if(Result.isSuccess(result)) {
			for(UserInfo userinfo:result.getData()) {
				if(userinfo.getAccount().equals(account)) {
					 int expiresIn =3600;
					 UserInfo user = userinfo;
					 user.setSession(jsessionid);
					 String jsonuser = JSONObject.toJSONString(user);
					 stringRedisTemplate.opsForValue().set(jsessionid,jsonuser,expiresIn,java.util.concurrent.TimeUnit.SECONDS);
					 Map<String,Object> data=new HashMap<String,Object>();
					 user.setId(null);
					 data.put("jsessionid", jsessionid);
					 data.put("currentuser", user);
					 return Result.success(data);
				}
			}
		}
		return Result.failed(ResultCode.USER_LOGIN_ERROR);
	}
	
	
    private Result<Map<String,Object>> verifyCodeApp(String code) {
    	String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+
    	wxconfig.getSmallAppID()+"&secret="+wxconfig.getSmallAppSecret()+"&js_code="+code+"&grant_type=authorization_code";
    	String json;
	    try {
			 json = HttpClientUtil.getUrl(url, null);
			 JSONObject jsonObject = GeneralUtil.getJsonObject(json);
			if(jsonObject!=null && jsonObject.size()>0) {
				String openid= jsonObject.getString("openid");
				if(!GeneralUtil.isEmpty(openid)) {
					Result<List<UserInfo>> result = null;
					try {
						result=adminClientOneFeign.findUserByOpenid(openid);
					}catch(Exception e) {
						e.printStackTrace();
					}
					
					String sessionkey = jsonObject.getString("session_key");
					if(result!=null&&Result.isSuccess(result)&&result.getData().size()>0) {
						//已经绑定了 直接登录
							Map<String,Object> data=new HashMap<String,Object>();
							UserInfo lastInfo=null;
							for(UserInfo userinfo:result.getData()) {
								if(lastInfo==null||(
										userinfo.getLastlogintime()!=null&&
										lastInfo.getLastlogintime().before(userinfo.getLastlogintime()))){
									lastInfo=userinfo;
								}
							}
							 int expiresIn =3600;
							 if(lastInfo!=null) {
								 UserInfo user = lastInfo;
								 user.setSession(sessionkey);
								 String jsonuser = JSONObject.toJSONString(user);
								 stringRedisTemplate.opsForValue().set(sessionkey,jsonuser,expiresIn,java.util.concurrent.TimeUnit.SECONDS);
								 lastInfo.setId(null);
								 data.put("currentuser", lastInfo);
								 List<UserInfo> openUserList=new LinkedList<UserInfo>();
								 if(result!=null&&result.getData()!=null) {
									 for(UserInfo openuser:result.getData()) {
										 openuser.setId(null);
										 openUserList.add(openuser);
									 }
								 }
								 data.put("openid", openid);
								 data.put("userlist",openUserList);
								 data.put("jsessionid", sessionkey);
								 return Result.success(data);
							 }else {
								data.put("openid", openid);
								data.put("status", "isfail");
								data.put("jsessionid", sessionkey);
								return Result.result(ResultCode.USERNAME_OR_PASSWORD_ERROR,data);
							 }

							
					} else {
						Map<String,Object> data=new HashMap<String,Object>();
						data.put("openid", openid);
						data.put("status", "isfail");
						return Result.result(ResultCode.USERNAME_OR_PASSWORD_ERROR,data);
					}
				}
			}
			} catch (HttpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return Result.failed(ResultCode.USER_LOGIN_ERROR);
	}
	
	
}
