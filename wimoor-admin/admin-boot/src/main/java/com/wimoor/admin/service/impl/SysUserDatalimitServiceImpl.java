package com.wimoor.admin.service.impl;

import com.wimoor.admin.pojo.entity.SysUserDatalimit;
import com.wimoor.admin.mapper.SysUserDatalimitMapper;
import com.wimoor.admin.service.ISysUserDatalimitService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户数据权限，放在用户信息中，登录后将在所有模块生效 服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-09-29
 */
@Service
public class SysUserDatalimitServiceImpl extends ServiceImpl<SysUserDatalimitMapper, SysUserDatalimit> implements ISysUserDatalimitService {

}
