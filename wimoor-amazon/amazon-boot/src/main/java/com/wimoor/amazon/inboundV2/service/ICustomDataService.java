package com.wimoor.amazon.inboundV2.service;

import com.wimoor.amazon.inboundV2.pojo.entity.CustomData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author liufei
* @description 针对表【t_erp_ship_custom_data】的数据库操作Service
* @createDate 2025-12-09 15:37:35
*/
public interface ICustomDataService extends IService<CustomData> {
    /**
     * 根据类型查询自定义数据
     * @param type 类型
     * @return 自定义数据列表
     */
    List<CustomData> getByType(String type);

    String getByTypeName(String type, String name);

    String getByTypeCode(String type, String code);
}
