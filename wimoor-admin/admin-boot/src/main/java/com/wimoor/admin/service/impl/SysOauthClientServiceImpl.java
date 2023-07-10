package com.wimoor.admin.service.impl;

import java.util.Set;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.admin.mapper.SysOauthClientMapper;
import com.wimoor.admin.pojo.entity.SysOauthClient;
import com.wimoor.admin.service.ISysOauthClientService;

import cn.hutool.core.collection.CollectionUtil;
import lombok.RequiredArgsConstructor;

/**
 * OAuth2 客户端业务类
 *
 * @author haoxr
 * @date 2020-11-06
 */
@Service
@RequiredArgsConstructor
public class SysOauthClientServiceImpl extends ServiceImpl<SysOauthClientMapper, SysOauthClient> implements ISysOauthClientService {

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 清理客户端缓存
     */
    @Override
    public void cleanCache() {
        Set<String> keys = stringRedisTemplate.keys("auth:oauth-client:*");
        if (CollectionUtil.isNotEmpty(keys)) {
            stringRedisTemplate.delete(keys);
        }
    }
}
