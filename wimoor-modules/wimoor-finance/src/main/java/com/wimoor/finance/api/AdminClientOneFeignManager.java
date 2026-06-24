package com.wimoor.finance.api;

import com.wimoor.admin.api.RemoteAdminService;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AdminClientOneFeignManager {
    @Autowired
    private RemoteAdminService remoteAdminService;

    /**
     * 新增回调字段
     */
    public Object addCallback(Map<String, String> data) {
        Result<?> result = remoteAdminService.addCallback(data);
        if (Result.isSuccess(result) && result.getData() != null) {
            return result.getData();
        } else {
            return null;
        }
    }

    /**
     * 获取表格记录
     */
    public Object getRecord(String url, String filter) {
        Result<?> result = remoteAdminService.getRecord(url, filter);
        if (Result.isSuccess(result) && result.getData() != null) {
            return result.getData();
        } else {
            return null;
        }
    }

    /**
     * 获取表格字段列表
     */
    public Object getFields(Map<String, Object> data) {
        Result<?> result = remoteAdminService.getFields(data);
        if (Result.isSuccess(result) && result.getData() != null) {
            return result.getData();
        } else {
            return null;
        }
    }
    /**
     * 获取表格信息
     */
    public Object getFeishuTableById(String id) {
        Result<?> result = remoteAdminService.getFeishuTableById(id);
        if (Result.isSuccess(result) && result.getData() != null) {
            return result.getData();
        } else {
            return null;
        }

    }
    /**
     * 更新回调字段
     */
    public Object updateCallback(Map<String, Object> data) {
        Result<?> result = remoteAdminService.updateCallback(data);
        if (Result.isSuccess(result) && result.getData() != null) {
            return result.getData();
        } else {
            return null;
        }
    }
    /**
     * 获取用户信息
     * @param userid 用户ID
     * @return 用户信息
     */
    public UserInfo getUserByUserId(String userid) {
        Result<UserInfo> result = remoteAdminService.getUserByUserId(userid);
        if (Result.isSuccess(result) && result.getData() != null) {
            return result.getData();
        } else {
            return null;
        }
    }

}