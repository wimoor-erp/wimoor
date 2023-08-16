package com.wimoor.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.admin.pojo.entity.SysOauthClient;

public interface ISysOauthClientService extends IService<SysOauthClient> {
    void cleanCache();
}
