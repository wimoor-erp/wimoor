package com.wimoor.admin.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.admin.mapper.SysCompanyMapper;
import com.wimoor.admin.pojo.entity.SysCompany;
import com.wimoor.admin.service.ISysCompanyService;

@Service
public class SysCompanyServiceImpl extends ServiceImpl<SysCompanyMapper, SysCompany> implements ISysCompanyService {

}
