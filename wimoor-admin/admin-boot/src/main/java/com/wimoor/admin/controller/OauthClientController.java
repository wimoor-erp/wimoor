package com.wimoor.admin.controller;

import java.util.Arrays;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.admin.pojo.dto.OAuth2ClientDTO;
import com.wimoor.admin.pojo.entity.SysOauthClient;
import com.wimoor.admin.service.ISysOauthClientService;
import com.wimoor.common.result.Result;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;

@Api(tags = "客户端接口")
@RestController
@RequestMapping("/api/v1/oauth-clients")
@AllArgsConstructor
public class OauthClientController {

    private ISysOauthClientService iSysOauthClientService;

    @ApiOperation(value = "列表分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "页码", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "pageNum", value = "每页数量", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "clientId", value = "客户端ID", paramType = "query", dataType = "String")
    })
    @GetMapping
    public Result<?> list(long pageNum, long pageSize, String clientId) {
        IPage<SysOauthClient> result = iSysOauthClientService.page(
                new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<SysOauthClient>()
                        .like(StrUtil.isNotBlank(clientId), SysOauthClient::getClientId, clientId));
        return Result.success(result.getRecords(), result.getTotal());
    }

    @ApiOperation(value = "客户端详情")
    @ApiImplicitParam(name = "clientId", value = "客户端id", required = true, paramType = "path", dataType = "String")
    @GetMapping("/{clientId}")
    public Result<?> detail(@PathVariable String clientId) {
        SysOauthClient client = iSysOauthClientService.getById(clientId);
        return Result.success(client);
    }

    @ApiOperation(value = "新增客户端")
    @ApiImplicitParam(name = "client", value = "实体JSON对象", required = true, paramType = "body", dataType = "OauthClientDetails")
    @PostMapping
    public Result<?>add(@RequestBody SysOauthClient client) {
        boolean status = iSysOauthClientService.save(client);
        return Result.judge(status);
    }

    @ApiOperation(value = "修改客户端")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "clientId", value = "客户端id", required = true, paramType = "path", dataType = "String"),
            @ApiImplicitParam(name = "client", value = "实体JSON对象", required = true, paramType = "body", dataType = "OauthClientDetails")
    })
    @PutMapping(value = "/{clientId}")
    public Result<?> update(
            @PathVariable String clientId,
            @RequestBody SysOauthClient client) {
        boolean status = iSysOauthClientService.updateById(client);
        return Result.judge(status);
    }

    @ApiOperation(value = "删除客户端")
    @ApiImplicitParam(name = "ids", value = "id集合,以,拼接字符串", required = true, paramType = "query", dataType = "String")
    @DeleteMapping("/{ids}")
    public Result<?> delete(@PathVariable("ids") String ids) {
        boolean status = iSysOauthClientService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.judge(status);
    }


    @ApiOperation(hidden = true, value = "获取 OAuth2 客户端认证信息", notes = "Feign 调用")
    @GetMapping("/getOAuth2ClientById")
    public Result<OAuth2ClientDTO> getOAuth2ClientById(@RequestParam String clientId) {
        SysOauthClient client = iSysOauthClientService.getById(clientId);
        Assert.isTrue(client!=null, "OAuth2 客户端不存在");
        OAuth2ClientDTO oAuth2ClientDTO = new OAuth2ClientDTO();
        BeanUtil.copyProperties(client, oAuth2ClientDTO);
        return Result.success(oAuth2ClientDTO);
    }
}
