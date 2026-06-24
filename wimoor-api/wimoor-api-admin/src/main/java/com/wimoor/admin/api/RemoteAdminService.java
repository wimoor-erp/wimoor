package com.wimoor.admin.api;

import com.wimoor.admin.api.factory.RemoteAdminFallbackFactory;
import com.wimoor.common.ServiceNameConstants;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@FeignClient(contextId = "remoteAdminService", value = ServiceNameConstants.ADMIN_SERVICE, fallbackFactory = RemoteAdminFallbackFactory.class)
public interface RemoteAdminService
{

    @PostMapping(value ="/admin/api/v1/file/upload/{type}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<?> uploadFile(@PathVariable("type") String type,@RequestParam("file") MultipartFile file) ;

    @GetMapping(value ="/admin/api/v1/file/delete/{type}")
    public Result<?> deleteFile(@PathVariable("type") String type,String path);

    /**
     * 新增回调字段
     */
    @PostMapping(value = "/admin/api/feishu/table/addCallback")
    public Result<?> addCallback(@RequestBody Map<String, String> data);

    /**
     * 获取表格记录
     */
    @GetMapping(value = "/admin/api/feishu/table/getRecord")
    public Result<?> getRecord(@RequestParam("url") String url, @RequestParam(value = "filter", required = false) String filter);

    /**
     * 获取表格字段列表
     */
    @PostMapping(value = "/admin/api/feishu/table/getFields")
    public Result<?> getFields(@RequestBody Map<String, Object> data);

    @GetMapping(value = "/admin/api/feishu/table/{id}")
    public Result<?> getFeishuTableById(@PathVariable("id") String id);

    @PostMapping(value = "/admin/api/feishu/table/updateCallback")
    public Result<?> updateCallback(@RequestBody Map<String, Object> data);

    @RequestMapping("/admin/api/v1/users/sysrole/userid/{userid}")
    public Result<UserInfo>  getUserByUserId(@PathVariable String userid);
}
