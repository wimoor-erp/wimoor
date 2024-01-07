package com.wimoor.sys.tool.controller;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.admin.common.exception.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.sys.tool.pojo.entity.SysTags;
import com.wimoor.sys.tool.service.ISysTagsService;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "标签接口")
@RestController
@RequestMapping("/api/v1/sysTags")
@RequiredArgsConstructor
public class SysTagsController { 

    private final ISysTagsService iSysTagsService;

    @ApiOperation(value = "列表分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", defaultValue = "1", value = "页码", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "limit", defaultValue = "10", value = "每页数量", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "name", value = "标签名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "dictCode", value = "标签编码", paramType = "query", dataType = "String")
    })
    @GetMapping("/page")
    public Result<?> getPageList(
            Integer page,
            Integer limit,
            String name,
            String groupid
    ) {
        SysTags tags=new SysTags();
        tags.setName(name);
        tags.setTaggroupid(groupid);
        tags.setStatus(1);
        Page<SysTags> pagehandler = new Page<SysTags>(page, limit);
        pagehandler.addOrder(OrderItem.ascs("sort"));
		IPage<SysTags> result = iSysTagsService.list(pagehandler,tags);
        return Result.success(result.getRecords(), result.getTotal());
    }

    @ApiOperation(value = "标签项列表")
    @GetMapping
    public Result<?> list(String name, String groupid) {
        List<SysTags> list = iSysTagsService.list(
                new LambdaQueryWrapper<SysTags>()
                        .like(StrUtil.isNotBlank(name), SysTags::getName, name)
                        .eq(StrUtil.isNotBlank(groupid), SysTags::getTaggroupid, groupid)
                        .eq(SysTags::getStatus, 1)
                        .select(SysTags::getName, SysTags::getValue)
        );
        return Result.success(list);
    }

    @ApiOperation(value = "标签项")
    @GetMapping("/item")
    public Result<?> item( String groupid,String value) {
        SysTags item = iSysTagsService.getOne(
                new LambdaQueryWrapper<SysTags>()
                        .eq(SysTags::getTaggroupid, groupid)
                        .eq(SysTags::getValue, value)
                        .eq(SysTags::getStatus, 1)
        );
        return Result.success(item);
    }
    
    @ApiOperation(value = "标签项详情")
    @ApiImplicitParam(name = "id", value = "标签id", required = true, paramType = "path", dataType = "String")
    @GetMapping("/{id}")
    public Result<?> detail(@PathVariable String id) {
        SysTags dictItem = iSysTagsService.getById(id);
        return Result.success(dictItem);
    }

    @ApiOperation(value = "新增标签项")
    @ApiImplicitParam(name = "dictItem", value = "实体JSON对象", required = true, paramType = "body", dataType = "SysTags")
    @PostMapping
	@CacheEvict(value = { "tagsCache"}, allEntries = true)
    public Result<?> add(@RequestBody SysTags item) {
    	UserInfo userInfo = UserInfoContext.get();
    	String shopid=userInfo.getCompanyid();
    	LambdaUpdateWrapper<SysTags> queryWrapper=new LambdaUpdateWrapper<SysTags>();
    	queryWrapper.eq(SysTags::getTaggroupid, item.getTaggroupid());
    	queryWrapper.eq(SysTags::getName, item.getName());
    	queryWrapper.eq(SysTags::getShopid, shopid);
		List<SysTags> list = iSysTagsService.list(queryWrapper);
		if(list!=null && list.size()>0) {
			 SysTags items = list.get(0);
			 items.setStatus(1);
			 boolean status = iSysTagsService.updateById(items);
			 return Result.judge(status);
		}else {
			item.setShopid(new BigInteger(shopid));
	        boolean status = iSysTagsService.save(item);
	        return Result.judge(status);
		}
    }

    @ApiOperation(value = "修改标签项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "标签id", required = true, paramType = "path", dataType = "String"),
            @ApiImplicitParam(name = "dictItem", value = "实体JSON对象", required = true, paramType = "body", dataType = "SysTags")
    })
    @PutMapping(value = "/{id}")
	@CacheEvict(value = { "tagsCache"}, allEntries = true)
    public Result<?> update(
            @PathVariable String id,
            @RequestBody SysTags item) {
    	LambdaUpdateWrapper<SysTags> queryWrapper=new LambdaUpdateWrapper<SysTags>();
    	queryWrapper.eq(SysTags::getTaggroupid, item.getTaggroupid());
    	queryWrapper.eq(SysTags::getName, item.getName());
		List<SysTags> list = iSysTagsService.list(queryWrapper);
		if(list!=null && list.size()>0) {
    		if(list.size()==1) {
    			if(!list.get(0).getId().equals(id)) {
    				throw new BizException("已存在此分组项名称！");
    			}
    		}
    	}
        boolean status = iSysTagsService.updateById(item);
        return Result.judge(status);
    }

    @ApiOperation(value = "删除标签数据")
    @ApiImplicitParam(name = "ids", value = "主键ID集合，以,分割拼接字符串", required = true, paramType = "query", dataType = "String")
    @Transactional
    @DeleteMapping("/{ids}")
	@CacheEvict(value = { "tagsCache"}, allEntries = true)
    public Result<?> delete(@PathVariable String ids) {
    	List<String> idslist = Arrays.asList(ids.split(","));
    	iSysTagsService.disableList(idslist);
        //boolean status = iSysTagsService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.success();
    }

    @ApiOperation(value = "获取标签")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "typeCode", value = "标签类型", required = true, paramType = "query", dataType = "String"),
    })
    @GetMapping(value = "/select_list/{typeCode}")
    public Result<List<SysTags>> listType(@PathVariable String typeCode) {
    	  List<SysTags> list =null;
            if (!StrUtil.isEmpty(typeCode)) {
             list = iSysTagsService.list(new LambdaUpdateWrapper<SysTags>().eq(SysTags::getTaggroupid, typeCode));
            }
        return Result.success(list);
    }
    
    @ApiOperation(value = "选择性更新标签数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, paramType = "path", dataType = "String"),
            @ApiImplicitParam(name = "dictItem", value = "实体JSON对象", required = true, paramType = "body", dataType = "SysTags")
    })
    @PatchMapping(value = "/{id}")
    public Result<?> patch(@PathVariable Integer id, @RequestBody SysTags dictItem) {
        LambdaUpdateWrapper<SysTags> updateWrapper = new LambdaUpdateWrapper<SysTags>().eq(SysTags::getId, id);
        updateWrapper.set(dictItem.getStatus() != null, SysTags::getStatus, dictItem.getStatus());
        boolean status = iSysTagsService.update(updateWrapper);
        return Result.judge(status);
    }
    
    @PostMapping("listname")
    public Result<List<Map<String,Object>>> listName(@RequestBody Set<String> tagsIdsList) {
  	  List<Map<String,Object>> list =null;
      if (tagsIdsList!=null && tagsIdsList.size()>0) {
    	  list=new ArrayList<Map<String,Object>>();
    	  for(String tagid:tagsIdsList) {
    		  SysTags tagItem = iSysTagsService.getById(tagid);
    		  Map<String,Object> maps=new HashMap<String, Object>();
    		  maps.put("name", tagItem.getName());
    		  maps.put("color", tagItem.getColor());
    		  list.add(maps);
    	  }
      }
      return Result.success(list);
    }
    
    @PostMapping("/mapname/{shopid}")
	@Cacheable(value = "tagsCache")
 	public Result<Map<String, Object>> findTagsName(@PathVariable String shopid) {
 	  	  Map<String,Object> map =new HashMap<String,Object>();
 	        List<SysTags> list = iSysTagsService.listbyshop(shopid);
 	        for(SysTags item:list) {
 	        	Map<String,Object> param=new HashMap<String,Object>();
 	        	param.put("name", item.getName());
 	        	param.put("color", item.getColor());
 	        	param.put("sort", item.getSort());
 	        	map.put(item.getId(),param);
 	        }
 	      return Result.success(map);
 	    }
}

