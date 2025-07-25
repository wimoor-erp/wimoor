package com.wimoor.sys.email.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.sys.email.mapper.SysMailTemplateMapper;
import com.wimoor.sys.email.pojo.entity.SysMailTemplate;
import com.wimoor.sys.email.service.ISysMailTemplateService;

import lombok.RequiredArgsConstructor;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2023-08-09
 */
@Service
@RequiredArgsConstructor
public class SysMailTemplateServiceImpl extends ServiceImpl<SysMailTemplateMapper, SysMailTemplate> implements ISysMailTemplateService {

	
}
