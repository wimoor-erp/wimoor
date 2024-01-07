package com.wimoor.sys.tool.controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.admin.common.exception.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.sys.tool.pojo.entity.SysTags;
import com.wimoor.sys.tool.pojo.entity.SysTagsGroups;
import com.wimoor.sys.tool.pojo.vo.SysTagsChildrenVo;
import com.wimoor.sys.tool.pojo.vo.SysTagsVo;
import com.wimoor.sys.tool.service.ISysTagsGroupsService;
import com.wimoor.sys.tool.service.ISysTagsService;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "标签组接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sysTagsGroups")
public class SysTagsGroupsController {

    private final ISysTagsGroupsService iSysTagsGroupsService;
    private final ISysTagsService iSysTagsService;

    @ApiOperation(value = "列表分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页数量", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "name", value = "标签组名称", paramType = "query", dataType = "String"),
    })
    @GetMapping("/page")
    public Result<List<SysTagsGroups>> list( Integer page,Integer limit, String name) {
    	UserInfo userInfo = UserInfoContext.get();
        Page<SysTagsGroups> result = iSysTagsGroupsService.page(new Page<>(page, limit), new LambdaQueryWrapper<SysTagsGroups>()
                .like(StrUtil.isNotBlank(name), SysTagsGroups::getName, StrUtil.trimToNull(name))
                .eq(SysTagsGroups::getShopId,  userInfo.getCompanyid())
                .eq(SysTagsGroups::getStatus,  1)
                .orderByAsc(SysTagsGroups::getSort)
                .orderByDesc(SysTagsGroups::getGmtModified)
                .orderByDesc(SysTagsGroups::getGmtCreate));
        return Result.success(result.getRecords(), result.getTotal());
    }

    
    @ApiOperation(value = "标签组列表")
    @GetMapping
    public Result<List<SysTagsGroups>> list() {
    	UserInfo userInfo = UserInfoContext.get();
        List<SysTagsGroups> list = iSysTagsGroupsService.list( new LambdaQueryWrapper<SysTagsGroups>()
        		.eq(SysTagsGroups::getShopId,  userInfo.getCompanyid())
        		.eq(SysTagsGroups::getStatus,  1)
        		.orderByAsc(SysTagsGroups::getSort)
                .orderByDesc(SysTagsGroups::getGmtModified)
                .orderByDesc(SysTagsGroups::getGmtCreate));
        return Result.success(list);
    }


    @ApiOperation(value = "标签组详情")
    @ApiImplicitParam(name = "id", value = "标签组id", required = true, paramType = "path", dataType = "String")
    @GetMapping("/{id}")
    public Result<SysTagsGroups> detail(@PathVariable String id) {
    	SysTagsGroups dict = iSysTagsGroupsService.getById(id);
        return Result.success(dict);
    }

    @ApiOperation(value = "新增标签组")
    @ApiImplicitParam(name = "SysTagsGroups", value = "实体JSON对象", required = true, paramType = "body", dataType = "SysTagsGroups")
    @PostMapping
    public Result<Boolean> add(@RequestBody SysTagsGroups group) {
    	UserInfo userInfo = UserInfoContext.get();
    	String shopid=userInfo.getCompanyid();
    	LambdaUpdateWrapper<SysTagsGroups> queryWrapper=new LambdaUpdateWrapper<SysTagsGroups>();
    	queryWrapper.eq(SysTagsGroups::getShopId, shopid);
    	queryWrapper.eq(SysTagsGroups::getName, group.getName());
    	queryWrapper.eq(SysTagsGroups::getShopId, shopid);
		List<SysTagsGroups> list = iSysTagsGroupsService.list(queryWrapper);
		if(list!=null && list.size()>0) {
			SysTagsGroups groups = list.get(0);
			groups.setStatus(1);
			boolean status = iSysTagsGroupsService.updateById(groups);
			return Result.judge(status);
		}else {
			group.setShopId(shopid);
	        boolean status = iSysTagsGroupsService.save(group);
	        return Result.judge(status);
		}
    }

    @ApiOperation(value = "修改标签组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "标签组id", required = true, paramType = "path", dataType = "String"),
            @ApiImplicitParam(name = "dictItem", value = "实体JSON对象", required = true, paramType = "body", dataType = "SysDictItem")
    })
    @PutMapping(value = "/{id}")
    public Result<Boolean> update(
            @PathVariable String id,
            @RequestBody SysTagsGroups group) {
    	UserInfo userInfo = UserInfoContext.get();
    	String shopid=userInfo.getCompanyid();
    	LambdaUpdateWrapper<SysTagsGroups> queryWrapper=new LambdaUpdateWrapper<SysTagsGroups>();
    	queryWrapper.eq(SysTagsGroups::getShopId, shopid);
    	queryWrapper.eq(SysTagsGroups::getName, group.getName());
		List<SysTagsGroups> list = iSysTagsGroupsService.list(queryWrapper);
    	if(list!=null && list.size()>0) {
    		if(list.size()==1) {
    			if(!list.get(0).getId().equals(id)) {
    				throw new BizException("已存在此分组名称！");
    			}
    		}
    	}
        boolean status = iSysTagsGroupsService.updateById(group);
        if (status) {
        	SysTagsGroups groups = iSysTagsGroupsService.getById(id);
            // 标签组code更新，同步更新标签组项code
            if (!StrUtil.equals(groups.getId(), group.getId())) {
                iSysTagsService.update(new LambdaUpdateWrapper<SysTags>().eq(SysTags::getTaggroupid, groups.getId())
                        .set(SysTags::getTaggroupid, group.getId()));
            }
        }
        return Result.judge(status);
    }

    @ApiOperation(value = "删除标签组")
    @ApiImplicitParam(name = "ids", value = "以,分割拼接字符串", required = true, paramType = "query", dataType = "String")
    @Transactional
    @DeleteMapping("/{ids}")
    public Result<Boolean> delete(@PathVariable String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        iSysTagsGroupsService.disableList(idList.get(0));
        return Result.success();
    }

    @ApiOperation(value = "选择性更新标签组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, paramType = "path", dataType = "String"),
            @ApiImplicitParam(name = "dictItem", value = "实体JSON对象", required = true, paramType = "body", dataType = "SysDictItem")
    })
    @PatchMapping(value = "/{id}")
    public Result<Boolean> patch(@PathVariable String id, @RequestBody SysTagsGroups dict) {
        LambdaUpdateWrapper<SysTagsGroups> updateWrapper = new LambdaUpdateWrapper<SysTagsGroups>().eq(SysTagsGroups::getId, id);
        updateWrapper.set(dict.getStatus() != null, SysTagsGroups::getStatus, dict.getStatus());
        boolean update = iSysTagsGroupsService.update(updateWrapper);
        return Result.success(update);
    }
    
    //@ApiOperation(value = "查询当前所有的分组和改分组下的标签")
    @GetMapping("/getAllTags")
    public Result<List<SysTagsVo>> getAllSysTagsAction(){
    	UserInfo userInfo = UserInfoContext.get();
    	String shopid=userInfo.getCompanyid();
    	List<SysTagsVo> list=new ArrayList<SysTagsVo>();
    	QueryWrapper<SysTagsGroups> queryWrapper = new QueryWrapper<SysTagsGroups>();
    	queryWrapper.eq("shop_id", shopid);
    	queryWrapper.eq("status", 1);
		List<SysTagsGroups> tagGrouplist = iSysTagsGroupsService.list(queryWrapper);
		if(tagGrouplist!=null && tagGrouplist.size()>0) {
			for (int i = 0; i < tagGrouplist.size(); i++) {
				SysTagsGroups group = tagGrouplist.get(i);
				SysTagsVo vo=new SysTagsVo();
				vo.setValue(group.getId());
				vo.setLabel(group.getName());
				QueryWrapper<SysTags> itemqueryWrapper=new QueryWrapper<SysTags>();
				itemqueryWrapper.eq("taggroupid", group.getId());
				itemqueryWrapper.eq("status", 1);
				List<SysTags> itemlist = iSysTagsService.list(itemqueryWrapper);
				if(itemlist!=null && itemlist.size()>0) {
					List<SysTagsChildrenVo> childrenlist=new ArrayList<SysTagsChildrenVo>();
					for (int j = 0; j < itemlist.size(); j++) {
						SysTags tag = itemlist.get(j);
						SysTagsChildrenVo itemVo=new SysTagsChildrenVo();
						itemVo.setLabel(tag.getName());
						itemVo.setValue(tag.getId());
						childrenlist.add(itemVo);
					}
					if(childrenlist.size()>0) {
						vo.setChildren(childrenlist);
					}else {
						vo.setChildren(null);	
					}
				}else {
					vo.setChildren(null);
				}
				list.add(vo);
			}
		}
    	return Result.success(list);
    }
    
}

