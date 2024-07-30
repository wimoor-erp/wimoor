package com.wimoor.admin.controller;


import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.admin.pojo.entity.SysDept;
import com.wimoor.admin.pojo.vo.DeptVO;
import com.wimoor.admin.service.ISysDeptService;
import com.wimoor.common.SelectVO;
import com.wimoor.common.TreeSelectVO;
import com.wimoor.common.result.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

/**
 * 部门控制器
 *
 * @author <a href="mailto:xianrui0365@163.com">xianrui</a>
 * @date 2020-11-06
 */
@Api(tags = "部门接口")
@RestController
@RequestMapping("/api/v1/depts")
@RequiredArgsConstructor
public class DeptController {

    private final ISysDeptService deptService;

    @ApiOperation(value = "部门表格（Table）层级列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "部门名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "部门状态", paramType = "query", dataType = "Long"),
    })
    @GetMapping("/table")
    public Result<List<DeptVO>> getTableList(Integer status, String name) {
        List<DeptVO> deptTableList = deptService.listTable(status, name);
        return Result.success(deptTableList);
    }

    @ApiOperation(value = "部门下拉（TreeSelect）层级列表")
    @GetMapping("/select-tree")
    public Result<List<TreeSelectVO>> getSelectTreeList() {
        List<TreeSelectVO> deptSelectList = deptService.listTreeSelect();
        return Result.success(deptSelectList);
    }

    @ApiOperation(value = "部门下拉（TreeSelect）层级列表")
    @GetMapping("/select")
    public Result<List<SelectVO>> getSelectList() {
        List<SelectVO> deptSelectList = deptService.listSelect();
        return Result.success(deptSelectList);
    }
    
    
    @ApiOperation(value = "部门详情")
    @ApiImplicitParam(name = "id", value = "部门id", required = true, paramType = "path", dataType = "Long")
    @GetMapping("/{id}")
    public Result<SysDept> detail(@PathVariable Long id) {
        SysDept sysDept = deptService.getById(id);
        return Result.success(sysDept);
    }

    @ApiOperation(value = "新增部门")
    @PostMapping
    public Result<String> add(@RequestBody SysDept dept) {
        String id = deptService.saveDept(dept);
        return Result.success(id);
    }

    @ApiOperation(value = "修改部门")
    @PutMapping(value = "/{id}")
    public Result<String> update(@PathVariable String id, @RequestBody SysDept dept) {
        dept.setId(id);
        String deptId = deptService.saveDept(dept);
        return Result.success(deptId);
    }

    @ApiOperation(value = "删除部门")
    @ApiImplicitParam(name = "ids", value = "部门ID，多个以英文逗号,分割拼接", required = true, paramType = "query", dataType = "String")
    @DeleteMapping("/{ids}")
    public Result<Boolean> delete(@PathVariable("ids") String ids) {
        boolean status= deptService.deleteByIds(ids);
        return Result.judge(status);
    }

}
