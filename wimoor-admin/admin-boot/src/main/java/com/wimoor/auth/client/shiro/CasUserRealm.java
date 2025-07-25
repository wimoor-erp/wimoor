package com.wimoor.auth.client.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.cas.CasRealm;

 

 

/**
 * 用户登录和授权用的realm
 */
@SuppressWarnings("deprecation")
public class CasUserRealm extends CasRealm {

 
 

    
    /**
     * CAS认证 ,验证用户身份
     * 将用户基本信息设置到会话中
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        //调用CasRealm实现的认证方法,其包含验证ticket、填充CasToken的principal等操作)
        AuthenticationInfo authc = super.doGetAuthenticationInfo(token);
        //String username = (String) authc.getPrincipals().getPrimaryPrincipal();
        return authc;
        //User user = userService.getUserAll(username);
//        if (user != null) {
//            //黑名单限制
//            if (user.getDisable()||user.getLogicDelete()) {
//                throw new AuthenticationException("msg:该帐号禁止登录");
//            }
//            //将用户信息放在session
//            SecurityUtils.getSubject().getSession().setAttribute(Constants.SESSION_USER, user);
//            return authc;
//        } else {
//            return null;
//        }
    }
    
 


}