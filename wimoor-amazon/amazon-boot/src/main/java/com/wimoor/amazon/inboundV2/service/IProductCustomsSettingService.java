package com.wimoor.amazon.inboundV2.service;

import com.wimoor.amazon.inboundV2.pojo.entity.ProductCustomsSetting;
import com.wimoor.common.user.UserInfo;

import java.util.List;
import java.util.Map;

public interface IProductCustomsSettingService {
    void saveSettingRate(UserInfo user, List<ProductCustomsSetting> dto);

    List<Map<String, Object>> getSettingRate(UserInfo user);
}
