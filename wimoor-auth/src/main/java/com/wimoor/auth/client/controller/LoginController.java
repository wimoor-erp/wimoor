package com.wimoor.auth.client.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.wimoor.admin.api.AdminClientOneFeign;
import com.wimoor.auth.client.config.FeiShuConfig;
import com.wimoor.auth.client.config.MyWxConfig;
import com.wimoor.auth.client.config.ShiroConfig;
import com.wimoor.auth.client.pojo.AppUserInfo;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.result.ResultCode;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import feign.FeignException;
@EnableAutoConfiguration
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
    @Autowired
    FeiShuConfig feishuConfig;
    
    @RequestMapping("/mylogout")
	String mylogoutAction(HttpServletRequest request,HttpServletResponse response, Model model) {
		HttpSession session = request.getSession();
		String jsessionid = session.getId();
		try {
			String logout=shiroConfig.getCasLogoutUrl()+"?service="+ShiroConfig.loginUrl.split("service=")[1];
			System.out.println(logout);
			response.sendRedirect(logout);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			String key = jsessionid;
		    stringRedisTemplate.delete(key);
			Subject subject = SecurityUtils.getSubject();
			subject.logout();
		}
		  return null;
	}
    
    @ResponseBody
	@RequestMapping("/verifySmsCode")
	public Result<String> verifySmsCodeAction(String key,String code) {
			String smscode = stringRedisTemplate.opsForValue().get(key);
			if(StrUtil.isNotBlank(code)&&StrUtil.isNotBlank(smscode)&&code.equals(smscode)) {
				  return Result.success("success");
			}else {
				throw new BizException("验证码失效或输入不正确");
			}
	}
    
	@RequestMapping("/getJSession")
	String showLoginAction(HttpServletRequest request,HttpServletResponse response, Model model) {
		HttpSession session = request.getSession();
		String jsessionid = session.getId();
		String errorpage="index";
		try {
			Subject subject = SecurityUtils.getSubject();
			if(subject.getPrincipal()!=null) {
				  int expiresIn =3600;
			        String key = jsessionid;
			        String value = (String) subject.getPrincipal();
			        byte[] bytes = value.getBytes();
			        Base64.Encoder encoder = Base64.getEncoder();
			        String encodeValue = encoder.encodeToString(bytes);
			        Result<UserInfo> userresult = adminClientOneFeign.getUserByUsername(encodeValue);
			        if(Result.isSuccess(userresult)) {
			        	 UserInfo user = userresult.getData();
					     user.setSession(key);
					     String jsonuser = JSONObject.toJSONString(user);
						 stringRedisTemplate.opsForValue().set(key,jsonuser,expiresIn,java.util.concurrent.TimeUnit.SECONDS);
			        }else {
			        	if(userresult!=null&&userresult.getMsg()!=null) {
			        		model.addAttribute("message", userresult.getMsg());
			        	}
			        	return errorpage;
			        }
			       
			}else {
				response.sendRedirect(ShiroConfig.loginUrl);
				return errorpage;
			}
			response.sendRedirect(ShiroConfig.uiserver+"/ssologin?jsessionid="+jsessionid);
		}catch(FeignException e) {
			e.printStackTrace();
			model.addAttribute("message", BizException.getMessage(e, ""));
			return errorpage;
		} catch (Exception e) {
			//TODO Auto-generated catch block
			e.printStackTrace();
			model.addAttribute("message", e.getMessage());
			return errorpage;
		}
		return errorpage;
	}
	
	@ResponseBody
	@RequestMapping("/getOpenUserlist")
	public Result<List<UserInfo>> loginWechatAction2(HttpServletRequest request,HttpServletResponse response) {
		String openid = request.getParameter("openid");
		String appType = request.getParameter("appType");
		Result<List<UserInfo>> openresult = adminClientOneFeign.findUserByOpenid(openid,appType);
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
	@RequestMapping("/ssologinisrun")
	public Result<String> ssoLoginisRunAction(HttpServletRequest request,HttpServletResponse response) {
		try {
			if(!StrUtil.isEmpty(ShiroConfig.casserver)) {
				URL url = new URL(ShiroConfig.casserver);
				HttpURLConnection con = (HttpURLConnection)url.openConnection();
				con.setUseCaches(false);
				con.setConnectTimeout(3000);
				int status = con.getResponseCode();
				if(status==200) {
					return Result.success(ShiroConfig.authserver);
				}else {
					return Result.success("false");
				}
			}else {
				return Result.success("false");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Result.success("false");
		}
	
	}
	public static String getBody(HttpServletRequest request) throws IOException {	 
		String body = null;	    
		StringBuilder stringBuilder = new StringBuilder();	    
		BufferedReader bufferedReader = null;	    
		try {	        
			InputStream inputStream = request.getInputStream();	        
			if (inputStream != null) {	           
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));	           
				char[] charBuffer = new char[128];	           
				int bytesRead = -1;	            
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {	               
					stringBuilder.append(charBuffer, 0, bytesRead);	           
					}	        
				} 
			  else {	            
					stringBuilder.append("");	        
					}	    
			} catch (IOException ex) {	        
				    throw ex;	    
				    } 
		    finally {	        
		    	if (bufferedReader != null) {	            
		    		try {	                
		    			bufferedReader.close();	           
		    			} catch (IOException ex) {	                
		    				throw ex;	            
		    			}	      
		    		}	    
		    	}	    
		body = stringBuilder.toString();
		return body;
	}
 
	@ResponseBody
    @RequestMapping("/login")
	public Result<UserInfo> loginAction(HttpServletRequest request,HttpServletResponse response ) {
		String body=null;
		try {
			body = getBody(request);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(body==null)return Result.failed();
		JSONObject userjson = GeneralUtil.getJsonObject(body);
		String account=userjson.getString("account");
		String password=userjson.getString("password");
		try {
			Result<UserInfo> result = adminClientOneFeign.verifyAccountAction(account, password);
			if(result.getData()!=null) {
				String sessionkey=UUID.randomUUID().toString();
				 UserInfo user = result.getData();
				 user.setSession(sessionkey);
				 String jsonuser = JSONObject.toJSONString(user);
				 int expiresIn =3600;
				 stringRedisTemplate.opsForValue().set(sessionkey,jsonuser,expiresIn,java.util.concurrent.TimeUnit.SECONDS);
			     return result;
			}else {
				return result;
			}
		}catch(FeignException e) {
			throw new BizException(BizException.getMessage(e, "登录失败"));
		}catch(Exception e) {
			return Result.failed("登录失败");
		}
	}
    
	@ResponseBody
    @RequestMapping("/apilogout")
	Result<String> logoutAction(HttpServletRequest request,HttpServletResponse response, Model model) {
        String jsessionid =request.getHeader("jsessionid");
		try {
			String key = jsessionid;
		    stringRedisTemplate.delete(key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Result.success("fail");
		} 
		  return Result.success("success");
	}
    
	
	@ResponseBody
	@RequestMapping("/loginWechat")
	public Result<Map<String,Object>> loginWechatAction(HttpServletRequest request,HttpServletResponse response) {
		String code = request.getParameter("code");	
		String appType = request.getParameter("appType");
		return this.verifyCodeApp(code,appType);
	}
	
	@ResponseBody
	@RequestMapping("/verifyWechatApp")
	public Result<?> verifyWechatAppAction(HttpServletRequest request,HttpServletResponse response) {
		String openid = request.getParameter("openid");
		String account=request.getParameter("email");
		String password=request.getParameter("password");
		String oldjsessionid=request.getParameter("jsessionid");
		String appType=request.getParameter("appType");
	    Result<UserInfo> result = adminClientOneFeign.bindUserByOpenid(openid,appType, account, password);
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
		String appType=request.getParameter("appType");
		if(appType.equals("website")) {
			String key=account;
			if(key.length()>16) {
				key=key.substring(0, 16);
	    	}else {
	    		while(key.length()<16) {
	    			key=key+"0";
	    		}
	    	}
			String iv=jsessionid;
	    	if(iv.length()>16) {
	    		iv=iv.substring(0, 16);
	    	}else {
	    		while(iv.length()<16) {
	    			iv=iv+"0";
	    		}
	    	}
			try {
				openid= java.net.URLDecoder.decode(openid,"utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cn.hutool.crypto.symmetric.AES aes = new cn.hutool.crypto.symmetric.AES(Mode.CBC, Padding.ISO10126Padding, key.getBytes(), iv.getBytes());
			openid = aes.decryptStr(Base64.getDecoder().decode(openid), CharsetUtil.CHARSET_UTF_8);
		}
		return loginApp(openid,account,jsessionid,appType);
	}
	
	private Result<Map<String,Object>> loginApp(String openid,String account,String jsessionid,String appType){
		Result<List<UserInfo>> result = adminClientOneFeign.findUserByOpenid(openid,appType);
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
	
	AppUserInfo getAppUserInfo(String code,String ftype){
		if(ftype.equals("app")) {
			return wxconfig.getLoginInfo(code);
		}else if(ftype.equals("feiapp")) {
			return feishuConfig.getLoginInfo(code);
		}
	    return null;
	}
    private Result<Map<String,Object>> verifyCodeApp(String code,String appType) {
    	AppUserInfo info = getAppUserInfo(code, appType);
				if(info!=null&&!GeneralUtil.isEmpty(info.getOpenId())) {
					Result<List<UserInfo>> result = null;
					try {
						result=adminClientOneFeign.findUserByOpenid(info.getOpenId(),appType);
					}catch(Exception e) {
						e.printStackTrace();
					}
					String sessionkey = info.getSessionKey();
					if(result!=null&&Result.isSuccess(result)&&result.getData().size()>0) {
						//已经绑定了 直接登录
							Map<String,Object> data=new HashMap<String,Object>();
							UserInfo lastInfo=null;
							for(UserInfo userinfo:result.getData()) {
								if(lastInfo==null||(
										userinfo.getLastlogintime()!=null&&lastInfo.getLastlogintime()!=null&&
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
								 data.put("openid", info.getOpenId());
								 data.put("userlist",openUserList);
								 data.put("jsessionid", sessionkey);
								 return Result.success(data);
							 }else {
								data.put("openid", info.getOpenId());
								data.put("status", "isfail");
								data.put("jsessionid", sessionkey);
								return Result.result(ResultCode.USER_LOGIN_ERROR,data);
							 }

							
					} else {
						Map<String,Object> data=new HashMap<String,Object>();
						data.put("openid", info.getOpenId());
						data.put("status", "isfail");
						data.put("jsessionid", sessionkey);
						return Result.result(ResultCode.USER_LOGIN_ERROR,data);
					}
				}
		return Result.failed(ResultCode.USER_LOGIN_ERROR);
	}
	
}
