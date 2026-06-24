package com.wimoor.admin.api.factory;

import com.wimoor.admin.api.RemoteAdminService;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 日志服务降级处理
 * 
 * @author wimoor
 */
@Component
public class RemoteAdminFallbackFactory implements FallbackFactory<RemoteAdminService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteAdminFallbackFactory.class);
    @Override
    public RemoteAdminService create(Throwable throwable) {
        log.error("管理员服务调用失败:{}", throwable.getMessage());
        return new RemoteAdminService() {


            @Override
            public Result<?> uploadFile(String type, MultipartFile file) {
                return null;
            }

            @Override
            public Result<?> deleteFile(String type, String path) {
                return null;
            }

            @Override
            public Result<?> addCallback(Map<String, String> data) {
                return null;
            }

            @Override
            public Result<?> getRecord(String url, String filter) {
                return null;
            }

            @Override
            public Result<?> getFields(Map<String, Object> data) {
                return null;
            }

            @Override
            public Result<?> getFeishuTableById(String id) {   return null;  }

            @Override
            public Result<?> updateCallback(Map<String, Object> data) {
                return null;
            }
            @Override
            public Result<UserInfo> getUserByUserId(String userid) {
                return null;
            }
        };
    }

}
