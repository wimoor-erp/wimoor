package com.wimoor.sys.tool.service;

import com.wimoor.common.user.UserInfo;
import com.wimoor.sys.tool.pojo.dto.SysChartCompletionRequestDTO;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface ISysDeepSeekService {

    Object completions(UserInfo userInfo, SysChartCompletionRequestDTO dto);

    /**
     * 流式调用AI接口
     */
    SseEmitter completionsStream(UserInfo userInfo, SysChartCompletionRequestDTO dto);

    Object getSession(UserInfo userInfo);

    void deleteSession(UserInfo userInfo, String sessionId);

    Object getKey(UserInfo userInfo);
}
