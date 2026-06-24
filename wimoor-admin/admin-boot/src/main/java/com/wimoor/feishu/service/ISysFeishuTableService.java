package com.wimoor.feishu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.feishu.pojo.entity.SysFeishuTable;

import java.util.List;

public interface ISysFeishuTableService extends IService<SysFeishuTable> {
    
    List<String> getTypeList(String appId);
    
}
