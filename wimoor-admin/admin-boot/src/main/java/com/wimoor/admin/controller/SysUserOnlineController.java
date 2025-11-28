package com.wimoor.admin.controller;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.wimoor.admin.pojo.vo.SysUserOnline;
import com.wimoor.admin.service.ISysUserOnlineService;
import com.wimoor.common.CacheConstants;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 在线用户监控
 * 
 * @author wimoor
 */
@RestController
@RequestMapping("/online")
public class SysUserOnlineController
{
    @Autowired
    private ISysUserOnlineService userOnlineService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/list")
    public Result<List<SysUserOnline>> list(String ipaddr, String userName)
    {
        Collection<String> keys = stringRedisTemplate.keys(CacheConstants.LOGIN_TOKEN_KEY + "*");
        List<SysUserOnline> userOnlineList = new ArrayList<SysUserOnline>();
        for (String key : keys)
        {
             String userInfoStr = stringRedisTemplate.opsForValue().get(key);
             JSONObject jsonObject=JSONObject.parseObject(userInfoStr);
             UserInfo user=(UserInfo)JSONObject.toJavaObject(jsonObject, UserInfo.class);
            if (StrUtil.isNotEmpty(ipaddr) && StrUtil.isNotEmpty(userName))
            {
                userOnlineList.add(userOnlineService.selectOnlineByInfo(ipaddr, userName, user));
            }
            else if (StrUtil.isNotEmpty(ipaddr))
            {
                userOnlineList.add(userOnlineService.selectOnlineByIpaddr(ipaddr, user));
            }
            else if (StrUtil.isNotEmpty(userName))
            {
                userOnlineList.add(userOnlineService.selectOnlineByUserName(userName, user));
            }
            else
            {
                userOnlineList.add(userOnlineService.loginUserToUserOnline(user));
            }
        }
        Collections.reverse(userOnlineList);
        userOnlineList.removeAll(Collections.singleton(null));
        return Result.success(userOnlineList);
    }

    /**
     * 强退用户
     */
    @DeleteMapping("/{tokenId}")
    public Result<?> forceLogout(@PathVariable String tokenId)
    {
        stringRedisTemplate.delete(CacheConstants.LOGIN_TOKEN_KEY + tokenId);
        return Result.success();
    }
}
