package com.wimoor.sys.gc.controller;

 
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.common.controller.BaseController;
import com.wimoor.common.result.Result;
import com.wimoor.sys.gc.constant.BaseConstant;
import com.wimoor.sys.gc.constant.NumberConst;
import com.wimoor.sys.gc.model.core.BasePage;
import com.wimoor.sys.gc.model.dto.DatasourceDTO;
import com.wimoor.sys.gc.model.entity.Datasource;
import com.wimoor.sys.gc.model.query.DatasourceQuery;
import com.wimoor.sys.gc.model.vo.DatasourceVO;
import com.wimoor.sys.gc.service.DatasourceService;
import com.wimoor.sys.gc.util.Base64Util;
import com.wimoor.sys.gc.util.JdbcPool;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 代码生成数据源维护表
 */
@RestController
@RequestMapping(BaseConstant.Uri.API_ADMIN + "/gc/datasource")
@Api(value = "DatasourceController", tags = "base--gc--代码生成--数据源维护")
public class DatasourceController extends BaseController<DatasourceService> {


    @GetMapping(value = "/findPage")
    @ApiOperation(value = "列表查询")
    public Result<BasePage<DatasourceVO>> findPage(@ModelAttribute @Validated DatasourceQuery query) {
        return Result.success(baseService.findPage(query));
    }


    @GetMapping(value = "/{id}")
    @ApiOperation(value = "id查询")
    public Result<DatasourceVO> findId(@PathVariable String id) {
        return Result.success(baseService.findId(id));
    }


    @PostMapping
    @ApiOperation(value = "添加")
    public Result<String> insert(@RequestBody @Validated DatasourceDTO dto) {
        Datasource xjDatasource = dto.convert(Datasource.class);
        xjDatasource.setDbPassword(Base64Util.encode(xjDatasource.getDbPassword()));
        baseService.save(xjDatasource);
        return Result.success(xjDatasource.getId());
    }


    @PutMapping(value = "/{id}")
    @ApiOperation(value = "ID编辑")
    public Result<Boolean> upd(@PathVariable String id, @RequestBody @Validated DatasourceDTO dto) {
        Datasource entity = dto.convert(Datasource.class);
        entity.setId(id);
        return Result.success(baseService.updateById(entity));
    }


    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "ID删除")
    public Result<Boolean> del(@PathVariable String id) {
        return Result.success(baseService.removeById(id));
    }


    @PutMapping(value = "/{id}/updPwd")
    @ApiOperation(value = "修改/重置密码")
    public Result<Boolean> updDbPwd(@PathVariable String id, @RequestParam String password) {
        Datasource entity = new Datasource();
        entity.setId(id);
        entity.setDbPassword(Base64Util.encode(password));
        return Result.success(baseService.updateById(entity));
    }


    @PostMapping(value = "/dataSourceTest/{id}")
    @ApiOperation("数据源连接测试")
    public Result<Boolean> dataSourceTest(@PathVariable(required = false) String id, @RequestBody @Validated DatasourceDTO dto) {
        String dbPassword = dto.getDbPassword();
        if (StringUtils.isNotBlank(id) && !id.equals(NumberConst.ZERO + "")) {
            Datasource xjAdminDatasource = baseService.getById(id);
            dbPassword = Base64Util.decrypt(xjAdminDatasource.getDbPassword());
        }
        // 主要没报错, 表示连接成功
        JdbcPool.getConn(dto.getDbUrl(), dto.getDbUsername(), dbPassword);
        return Result.success(true);
    }
}
