package com.wimoor.amazon.inboundV2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.inboundV2.pojo.entity.CustomData;
import com.wimoor.amazon.inboundV2.service.ICustomDataService;
import com.wimoor.amazon.inboundV2.mapper.CustomDataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

/**
* @author liufei
* @description 针对表【t_erp_ship_custom_data】的数据库操作Service实现
* @createDate 2025-12-09 15:37:35
*/
@Service
@RequiredArgsConstructor
public class CustomDataServiceImpl extends ServiceImpl<CustomDataMapper, CustomData>
    implements ICustomDataService {

    @Override
    public List<CustomData> getByType(String type) {
        return baseMapper.selectList(new LambdaQueryWrapper<CustomData>().eq(CustomData::getFtype, type));
    }

    @Override
    @Cacheable(cacheNames = "customData", key = "#type + '_' + #name")
    public String getByTypeName(String type, String name) {
         CustomData customData = baseMapper.selectOne(new LambdaQueryWrapper<CustomData>().eq(CustomData::getFtype, type).eq(CustomData::getName, name));
        return customData != null ? customData.getCode() : null;
    }

    @Override
    @Cacheable(cacheNames = "customData", key = "#type + '_' + #code")
    public String getByTypeCode(String type, String code) {
        CustomData customData = baseMapper.selectOne(new LambdaQueryWrapper<CustomData>().eq(CustomData::getFtype, type).eq(CustomData::getEncode, code));
        return customData != null ? customData.getCode() : null;
    }

}




