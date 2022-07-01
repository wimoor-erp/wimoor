package com.wimoor.erp.material.service.impl;


import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.erp.material.mapper.MaterialMarkMapper;
import com.wimoor.erp.material.pojo.entity.MaterialMark;
import com.wimoor.erp.material.service.IMaterialMarkService;

import lombok.RequiredArgsConstructor;

@Service("materialMarkService")
@RequiredArgsConstructor
public class MaterialMarkServiceImpl extends ServiceImpl<MaterialMarkMapper,MaterialMark> implements IMaterialMarkService {

}
