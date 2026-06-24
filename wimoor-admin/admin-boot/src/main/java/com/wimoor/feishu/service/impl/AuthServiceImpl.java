package com.wimoor.feishu.service.impl;

import com.wimoor.feishu.pojo.entity.Auth;
import com.wimoor.feishu.mapper.AuthMapper;
import com.wimoor.feishu.service.IAuthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2023-09-01
 */
@Service
public class AuthServiceImpl extends ServiceImpl<AuthMapper, Auth> implements IAuthService {

}
