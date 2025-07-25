package com.wimoor.admin.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.admin.mapper.SysUserWechatMPMapper;
import com.wimoor.admin.pojo.entity.SysUserWechatMP;
import com.wimoor.admin.service.ISysUserWechatMPService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SysUserWechatMPServiceImpl extends ServiceImpl<SysUserWechatMPMapper, SysUserWechatMP> implements ISysUserWechatMPService {

}
