package com.wimoor.admin.api;

import com.wimoor.admin.pojo.dto.SysEmailDTO;
import com.wimoor.admin.pojo.dto.SysUserRoleDTO;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;


@Component
@FeignClient(value = "wimoor-admin")
public interface AdminClientOneFeign {
    /**
     * eureka-client-one的helloworld访问mapping
     */
	@RequestMapping("/admin/api/v1/users/sysrole/userid/{userid}")
    public Result<UserInfo>  getUserByUserId(@PathVariable String userid);
	
    @GetMapping("/admin/api/v1/users/roles/{userid}")
    public Result<List<SysUserRoleDTO>> getUserRoleById(@PathVariable String userid) ;

    @RequestMapping("/admin/api/v1/users/sysrole/account/{account}")
    public Result<UserInfo> getUserByUsername(@PathVariable String account);

    @PostMapping("/admin/api/v1/sysTags/listname")
	public Result<List<Map<String,Object>>> findTagsNameByIds(@RequestBody Set<String> tagsIdsList);

    @PostMapping("/admin/api/v1/sysTags/mapname/{shopid}")
  	public Result<Map<String, Object>> findTagsName(@PathVariable String shopid);
    
    @RequestMapping("admin/api/v1/users/sysrole/openidbind")
    public Result<UserInfo> bindUserByOpenid(@RequestParam("openid")String openid,@RequestParam("appType")String appType,
                                             @RequestParam("account")String account,@RequestParam("password")String password,
                                             @RequestParam("accesstoken")String accesstoken, @RequestParam("refreshtoken")String refreshtoken) ;
 
    @RequestMapping("admin/api/v1/users/sysrole/findbyopenid/{openid}/{appType}")
    public Result<List<UserInfo>> findUserByOpenid(@PathVariable String openid,@PathVariable String appType) ;
    
    @RequestMapping("admin/api/v1/users/sysrole/verifyAccount")
    public Result<UserInfo> verifyAccountAction(@RequestParam("account")String account,@RequestParam("password")String password);
    
    @PostMapping("/admin/api/v1/email/sendBoss")
    public Result<?> sendBoss(@RequestBody SysEmailDTO dto);

    @GetMapping("/admin/api/v1/users/getNameMap")
	public Result<Map<String, String>> getNameMap();

    @GetMapping("/admin/api/v1/users/getIDMap")
    public Result<Map<String, String>> getIDMap();

    @GetMapping("/admin/api/v1/dict-items/select_list/{typeCode}")
    public Result<?> listType(@PathVariable String typeCode);

    @PostMapping(value ="/admin/api/v1/file/upload/{type}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<?> uploadFile(@PathVariable("type") String type,@RequestPart("file") MultipartFile file) ;

    @GetMapping(value ="/admin/api/v1/file/delete/{type}")
    public Result<?> deleteFile(@PathVariable("type") String type,@RequestParam("path")String path);
}