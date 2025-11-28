package com.wimoor.sys.tool.service;

import com.wimoor.common.user.UserInfo;
import com.wimoor.sys.tool.pojo.dto.SysChartCompletionRequestDTO;

public interface ISysDeepSeekService {
    Object completions(UserInfo userInfo,SysChartCompletionRequestDTO dto);

    Object getSession(UserInfo userInfo);

    Object getKey(UserInfo userInfo);
}
