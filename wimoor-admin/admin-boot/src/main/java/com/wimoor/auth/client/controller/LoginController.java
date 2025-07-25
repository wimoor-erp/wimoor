package com.wimoor.auth.client.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

import java.security.MessageDigest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.hutool.crypto.digest.DigestUtil;
import com.wimoor.admin.controller.UserController;
import com.wimoor.auth.client.config.FeiShuLoginConfig;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.bouncycastle.jcajce.provider.digest.SHA1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.wimoor.auth.client.config.FeiShuLoginConfig;
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
@RequestMapping("/api/v1/auth")
public class LoginController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    ShiroConfig shiroConfig;
    @Autowired
	MyWxConfig wxconfig;
    @Autowired
	FeiShuLoginConfig feishuLoginConfig;
	@Autowired
	UserController userController;
	@ResponseBody
	@RequestMapping("/mfacode")
	public Result<String> mfacodeAction(HttpServletRequest request,HttpServletResponse response ) {
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
			Result<String> result = userController.getMFACodeAction(account, password);
			return result;
		}catch(FeignException e) {
			throw new BizException(BizException.getMessage(e, "登录失败"));
		}catch(Exception e) {
			return Result.failed("登录失败");
		}
	}
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
	@RequestMapping("/verifyMPinfo")
	public String verifyMPinfoAction(HttpServletRequest request, HttpServletResponse response) {
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
        String[] array= new String[]{"wimoor2018Fdds",timestamp,nonce};
		Arrays.sort(array);
		String key=Arrays.stream(array).collect(Collectors.joining());
		byte[] sha1 = DigestUtil.sha1(key);
		try {
			// 获取SHA-1实例
			MessageDigest digest = MessageDigest.getInstance("SHA-1");

			// 将输入字符串转换为字节数组
			byte[] encodedhash = digest.digest(key.getBytes());
			// 或者，直接以16进制形式输出
			StringBuilder hexString = new StringBuilder();
			for (byte b : encodedhash) {
				String hex = Integer.toHexString(0xff & b);
				if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}
			key=hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("SHA-1 algorithm not found", e);
		}
        if(signature.equals(key)){
			return echostr;
		}
		return  "fail";
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
			        Result<UserInfo> userresult = userController.getUserByUsername(encodeValue);
			        if(Result.isSuccess(userresult)) {
			        	 UserInfo user = userresult.getData();
					     user.setSession(key);
					     String jsonuser = JSONObject.toJSONString(user);
						 setSession(user.getAccount(),"web",jsessionid,jsonuser);
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
		Result<List<UserInfo>> openresult = userController.findUserByOpenid(openid,appType);
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
		String mfa=userjson.getString("mfa");
		try {
			Result<UserInfo> result =null;
			if(StrUtil.isNotBlank(mfa)){
				mfa=mfa.trim();
				result=userController.verifyMFAAction(account,password,mfa);
			}else{
				result = userController.verifyAccountAction(account, password);
                if(Result.isSuccess(result)&&result.getData()!=null&&result.getData().getPasswordkey()!=null&&result.getData().getPasswordkey().contains("{needmfa}")){
						throw new BizException("登录失败，该账号已绑定MFA，需要维护MFA编码");
				}
			}
			if(result.getData()!=null) {
				String sessionkey=UUID.randomUUID().toString();
				 UserInfo user = result.getData();
				 user.setSession(sessionkey);
				 String jsonuser = JSONObject.toJSONString(user);
				 setSession(user.getAccount(),"web",sessionkey,jsonuser);
			     return result;
			}else {
				return result;
			}
		}catch(FeignException e) {
			throw new BizException(BizException.getMessage(e, "登录失败"));
		}catch(BizException e) {
			return Result.failed(e.getMessage());
		}catch(Exception e) {
			return Result.failed("登录失败");
		}
	}
    private void setSession(String account,String apptype,String sessionkey,String jsonuser){
		int expiresIn =3600;
		String accountkey=account+"{"+apptype+"}";
		if(stringRedisTemplate.opsForValue().get(accountkey)!=null){
			String sessionkeyOld = stringRedisTemplate.opsForValue().get(accountkey);
			if(sessionkeyOld!=null&&!sessionkeyOld.equals(sessionkey)){
				stringRedisTemplate.delete(sessionkeyOld);
			}
		}
		stringRedisTemplate.opsForValue().set(sessionkey,jsonuser,expiresIn,java.util.concurrent.TimeUnit.SECONDS);
		stringRedisTemplate.opsForValue().set(accountkey,sessionkey,expiresIn,java.util.concurrent.TimeUnit.SECONDS);
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
		HttpSession session = request.getSession();
		return this.verifyCodeApp(code,appType);
	}
	//校验用户静默授权网页 登录的方式  提交的信息
	@ResponseBody
	@RequestMapping("/verifyWechatApp")
	public Result<?> verifyWechatAppAction(HttpServletRequest request,HttpServletResponse response) {
		String openid = request.getParameter("openid");
		String account=request.getParameter("email");
		String password=request.getParameter("password");
		String oldjsessionid=request.getParameter("jsessionid");
		String appType=request.getParameter("appType");
		String accesstoken = request.getParameter("wxtoken");
		String refreshtoken =request.getParameter("wxrefshtoken");
		String mfa =request.getParameter("mfa");
	    Result<UserInfo> result = userController.bindUserByOpenid(openid,appType, account, password,accesstoken,refreshtoken,mfa);
				//校验成功
	    int expiresIn =3600;
	 	HttpSession session = request.getSession();
		String jsessionid = session.getId();
		if(StrUtil.isNotBlank(oldjsessionid)&& !oldjsessionid.equals(jsessionid)&&!oldjsessionid.equals("null")){
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
			 setSession(  user.getAccount(),  appType,  jsessionid,  jsonuser);
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
		return loginApp(openid,account,jsessionid,appType);
	}
	
	private Result<Map<String,Object>> loginApp(String openid,String account,String jsessionid,String appType){
		Result<List<UserInfo>> result = userController.findUserByOpenid(openid,appType);
		if(Result.isSuccess(result)) {
			for(UserInfo userinfo:result.getData()) {
				if(userinfo.getAccount().equals(account)) {
					if(userinfo.getLosingeffect().before(new Date())) {
						throw new BizException("账号已过期,"+userinfo.getLastlogintime());
					}
					if(userinfo.getDisable()) {
						throw new BizException("账号已停用");
					}
					if(userinfo.getLogicDelete()) {
						throw new BizException("账号已删除");
					}
					 int expiresIn =3600;
					 UserInfo user = userinfo;
					 user.setSession(jsessionid);
					 String jsonuser = JSONObject.toJSONString(user);
					 setSession(user.getAccount(),appType,jsessionid,jsonuser);
					 Map<String,Object> data=new HashMap<String,Object>();
					 user.setId(null);
					 data.put("jsessionid", jsessionid);
					 data.put("currentuser", user);
					 return Result.success(data);
				}
			}
		 throw new BizException("账号不存在");
		}
		return Result.failed(ResultCode.USER_LOGIN_ERROR);
	}
	
	AppUserInfo getAppUserInfo(String code,String ftype){
		if(ftype.equals("app")) {
			return wxconfig.getLoginInfo(code);
		}else if(ftype.equals("mp")) {
			return wxconfig.getLoginInfoMp(code);
		}else if(ftype.equals("feiapp")) {
			return feishuLoginConfig.getLoginInfo(code);
		}else if(ftype.equals("feishu")) {
			return feishuLoginConfig.getWebLoginInfo(code);
		}
	    return null;
	}
    private Result<Map<String,Object>> verifyCodeApp(String code,String appType) {
    	         AppUserInfo info = getAppUserInfo(code, appType);
		         return verifyAppLogin( info);
	}

	@ResponseBody
	@RequestMapping("/verifyAppLogin")
	public Result<Map<String,Object>> verifyAppLogin(AppUserInfo info){
		if(info!=null&&!GeneralUtil.isEmpty(info.getOpenId())) {
			Result<List<UserInfo>> result = null;
			try {
				result=userController.findUserByOpenid(info.getOpenId(),info.getAppType());
			}catch(Exception e) {
				e.printStackTrace();
			}
			String sessionkey = info.getSessionKey();
			if(result!=null&&Result.isSuccess(result)&&result.getData().size()>0) {
				//已经绑定了 直接登录
				Map<String,Object> data=new HashMap<String,Object>();
				UserInfo lastInfo=null;
				for(UserInfo userinfo:result.getData()) {
					if(userinfo==null||userinfo.getLosingeffect().before(new Date())||userinfo.getDisable()||userinfo.getLogicDelete()) {
						continue;
					}
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
					setSession(user.getAccount(),info.getAppType(),sessionkey,jsonuser);
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
