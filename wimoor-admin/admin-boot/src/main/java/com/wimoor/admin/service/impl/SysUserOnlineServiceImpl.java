package com.wimoor.admin.service.impl;

import com.wimoor.admin.pojo.vo.SysUserOnline;
import com.wimoor.admin.service.ISysUserOnlineService;
import com.wimoor.common.user.UserInfo;
import org.springframework.stereotype.Service;

/**
 * 在线用户 服务层处理
 * 
 * @author wimoor
 */
@Service
public class SysUserOnlineServiceImpl implements ISysUserOnlineService
{
    /**
     * 通过登录地址查询信息
     * 
     * @param ipaddr 登录地址
     * @param user 用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline selectOnlineByIpaddr(String ipaddr, UserInfo user)
    {
        if (ipaddr.equals(user.getLastloginip()))
        {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 通过用户名称查询信息
     * 
     * @param userName 用户名称
     * @param user 用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline selectOnlineByUserName(String userName, UserInfo user)
    {
        if (userName.equals(user.getUserName()))
        {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 通过登录地址/用户名称查询信息
     * 
     * @param ipaddr 登录地址
     * @param userName 用户名称
     * @param user 用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline selectOnlineByInfo(String ipaddr, String userName, UserInfo user)
    {
        if (ipaddr.equals(user.getLastloginip()) && userName.equals(user.getUserName()))
        {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 设置在线用户信息
     * 
     * @param user 用户信息
     * @return 在线用户
     */
    @Override
    public SysUserOnline loginUserToUserOnline(UserInfo user)
    {
        if (user == null)
        {
            return null;
        }
        SysUserOnline sysUserOnline = new SysUserOnline();
        sysUserOnline.setTokenId(user.getSession());
        sysUserOnline.setUserName(user.getUserName());
        sysUserOnline.setIpaddr(user.getLastloginip());
        sysUserOnline.setLoginTime(user.getLastlogintime());
        return sysUserOnline;
    }
}
