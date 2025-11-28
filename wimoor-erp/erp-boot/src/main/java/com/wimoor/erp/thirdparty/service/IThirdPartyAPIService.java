package com.wimoor.erp.thirdparty.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyAPI;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartySystem;

import java.util.List;

public interface IThirdPartyAPIService extends IService<ThirdPartyAPI> {
    List<ThirdPartySystem> getAllSupportSystem();
    List<ThirdPartyAPI> getSupportApi(UserInfo user,String support);
    public ThirdPartySystem getApiSystemById(String id);
}
