package com.wimoor.amazon.transparency.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.transparency.pojo.dto.TransparencyDTO;
import com.wimoor.amazon.transparency.pojo.entity.TransparencyTask;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;

import java.util.Map;

/**
* @author liufei
* @description 针对表【t_amz_transparency_task】的数据库操作Service
* @createDate 2025-08-08 11:31:24
*/
public interface ITransparencyTaskService extends IService<TransparencyTask> {

    TransparencyTask saveTask(UserInfo userinfo, TransparencyTask dto);

    IPage<Map<String, Object>> listTask(UserInfo userinfo, TransparencyDTO dto);

    TransparencyTask refreshTask(UserInfo userinfo, TransparencyTask dto);
}
