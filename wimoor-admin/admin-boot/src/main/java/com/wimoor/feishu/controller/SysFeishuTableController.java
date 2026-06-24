package com.wimoor.feishu.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.feishu.pojo.entity.Auth;
import com.wimoor.feishu.pojo.entity.SysFeishuTable;
import com.wimoor.feishu.service.IAndTablesService;
import com.wimoor.feishu.service.IAuthService;
import com.wimoor.feishu.service.ISysFeishuTableService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feishu/table")
@Api(tags = "飞书应用和表格管理")
public class SysFeishuTableController {
    
    @Autowired
    private ISysFeishuTableService sysFeishuTableService;
 
    @Autowired
    private IAndTablesService andTablesService;
    
    @Autowired
    IAuthService iAuthService;
    /**
     * 上传文档
     */
    @PostMapping("/create")
    @ApiOperation("创建文档")
    public Result<String> create(@RequestBody Map<String, Object> data) {
        String title = (String) data.get("title");
        UserInfo userinfo = UserInfoContext.get();
        Auth auth = iAuthService.lambdaQuery().eq(Auth::getShopid, userinfo.getCompanyid()).one();
        try {
            andTablesService.create(auth, title);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Result.success();
    }


    @PostMapping("/getTableInfo")
    @ApiOperation("获取表格字段列表")
    public Result<String> getTableInfo(@RequestBody Map<String, Object> data) {
        String url = (String) data.get("url");
        UserInfo userinfo = UserInfoContext.get();
        Auth auth = iAuthService.lambdaQuery().eq(Auth::getShopid, userinfo.getCompanyid()).one();
        return Result.success(andTablesService.getTableInfo(auth, url));
    }


    @PostMapping("/getFields")
    @ApiOperation("获取表格字段列表")
    public Result<SysFeishuTable> getFields(@RequestBody Map<String, Object> data) {
        String url = (String) data.get("url");
        String name = (String) data.get("name");
        UserInfo userinfo = UserInfoContext.get();
        Auth auth = iAuthService.lambdaQuery().eq(Auth::getShopid, userinfo.getCompanyid()).one();
        return Result.success(andTablesService.getFields(auth, url, name,userinfo.getId()));
    }
    
    @GetMapping("/getRecord")
    @ApiOperation(value = "获取表格记录", notes = "获取飞书多维表格的记录数据，支持筛选条件")
    public Result getRecord(@RequestParam String url, @RequestParam(required = false) String filter) {
        UserInfo userinfo = UserInfoContext.get();
        Auth auth = iAuthService.lambdaQuery().eq(Auth::getShopid, userinfo.getCompanyid()).one();
        return Result.success(andTablesService.getRecord(auth, url, filter));
    }
    
    @PostMapping("/updateCallback")
    @ApiOperation(value = "更新回调", notes = "批量更新飞书多维表格记录\n" +
            "参数样例：\n" +
            "{\n" +
            "  \"tableId\": \"1\",\n" +
            "  \"fieldkeys\": [\"recqwIwhc6\", \"recabc123\"]\n" +
            "}\n" +
            "说明：每次调用只能处理一个tableId下的多个fieldkey，系统会自动根据t_sys_feishu_table表中存储的feedback字段值生成fields并更新")
    public Result updateCallback(@RequestBody Map<String, Object> data) {
        String tableId = (String) data.get("tableId");
        List<String> fieldkeys = (List<String>) data.get("fieldkeys");
        String value = (String) data.get("value");
        UserInfo userinfo = UserInfoContext.get();
        Auth auth = iAuthService.lambdaQuery().eq(Auth::getShopid, userinfo.getCompanyid()).one();
        return Result.success(andTablesService.updateCallback(auth, tableId, fieldkeys, value));
    }
    
    @PostMapping("/addCallback")
    @ApiOperation(value = "新增回调字段", notes = "在飞书多维表格上新增一个字段，并保存在t_sys_feishu_table的feedback中\n" +
            "参数样例：\n" +
            "{\n" +
            "  \"tableId\": \"1\",\n" +
            "  \"fieldName\": \"同步状态\"\n" +
            "}\n" +
            "说明：tableId为t_sys_feishu_table的ID，fieldName为要新增的字段名称")
    public Result addCallback(@RequestBody Map<String, String> data) {
        String tableId = data.get("tableId");
        String fieldName = data.get("fieldName");
        UserInfo userinfo = UserInfoContext.get();
        Auth auth = iAuthService.lambdaQuery().eq(Auth::getShopid, userinfo.getCompanyid()).one();
        return Result.success(andTablesService.addCallback(auth, tableId, fieldName));
    }
    
    @GetMapping("/list")
    public Result list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                      @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                      @RequestParam(value = "name", defaultValue = "") String name) {
        Page<SysFeishuTable> pageInfo = new Page<>(page, limit);
        UserInfo userinfo = UserInfoContext.get();
        Auth auth = iAuthService.lambdaQuery().eq(Auth::getShopid, userinfo.getCompanyid()).one();
        if(auth != null){
            LambdaQueryWrapper<SysFeishuTable> queryWrapper = new LambdaQueryWrapper<>();
            if(StrUtil.isNotEmpty(name)) {
                queryWrapper.eq(SysFeishuTable::getName, name);
            }
            queryWrapper.eq(SysFeishuTable::getAppid, auth.getAppId());
            queryWrapper.eq(SysFeishuTable::getDisabled, false);
            Page<SysFeishuTable> result = sysFeishuTableService.page(pageInfo, queryWrapper);
            return Result.success(result);
        }else{
            return Result.failed("用户未绑定飞书应用");
        }

    }
    
    @PostMapping("/add")
    public Result add(@RequestBody SysFeishuTable sysFeishuTable) {
        sysFeishuTable.setOpttime(new Date());
        boolean result = sysFeishuTableService.save(sysFeishuTable);
        if (result) {
            return Result.success();
        } else {
            return Result.failed();
        }
    }
    
    @PutMapping("/update")
    public Result update(@RequestBody SysFeishuTable sysFeishuTable) {
        sysFeishuTable.setOpttime(new Date());
        boolean result = sysFeishuTableService.updateById(sysFeishuTable);
        if (result) {
            return Result.success();
        } else {
            return Result.failed();
        }
    }
    
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        boolean result = sysFeishuTableService.removeById(id);
        if (result) {
            return Result.success();
        } else {
            return Result.failed();
        }
    }
    
    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        UserInfo userinfo = UserInfoContext.get();
        Auth auth = iAuthService.lambdaQuery().eq(Auth::getShopid, userinfo.getCompanyid()).one();
        LambdaQueryWrapper<SysFeishuTable> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysFeishuTable::getAppid, auth.getAppId());
        queryWrapper.eq(SysFeishuTable::getId, id);
        List<SysFeishuTable> list = sysFeishuTableService.list(queryWrapper);
        if(list != null && list.size() > 0) {
            return Result.success(list.get(0));
        }else{
            return Result.failed("数据不存在");
        }

    }

    @GetMapping("/typeList")
    @ApiOperation("获取数据表类型列表")
    public Result<List<SysFeishuTable>> getTypeList() {
        UserInfo userinfo = UserInfoContext.get();
        Auth auth = iAuthService.lambdaQuery().eq(Auth::getShopid, userinfo.getCompanyid()).one();
        LambdaQueryWrapper<SysFeishuTable> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysFeishuTable::getAppid, auth.getAppId());
        queryWrapper.eq(SysFeishuTable::getDisabled, false);
        List<SysFeishuTable> list = sysFeishuTableService.list(queryWrapper);
        return Result.success(list);
    }



    
}
