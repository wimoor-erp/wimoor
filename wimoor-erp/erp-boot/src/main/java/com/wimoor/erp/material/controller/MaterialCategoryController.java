package com.wimoor.erp.material.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.material.pojo.dto.MaterialCateDTO;
import com.wimoor.erp.material.pojo.entity.MaterialCategory;
import com.wimoor.erp.material.service.IMaterialCategoryService;
import com.wimoor.erp.util.UUIDUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;


@Api(tags = "产品分类接口")
@RestController
@RequestMapping("/api/v1/materialCategory")
@RequiredArgsConstructor
public class MaterialCategoryController {

	final IMaterialCategoryService materialCategoryService;
	
	final ISerialNumService serialNumService;
	
	@PostMapping("/saveData")
	public Result<Map<String,Object>> saveDataAction(@RequestBody MaterialCategory entity){
		Map<String,Object> map=new HashMap<String, Object>();
		UserInfo userinfo = UserInfoContext.get();
		String shopid=userinfo.getCompanyid();
		if(StrUtil.isEmpty(entity.getNumber())) {
			try {
				entity.setNumber(serialNumService.readSerialNumber(shopid, "CG"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		entity.setOpttime(new Date());
		entity.setShopid(shopid);
		entity.setOperator(userinfo.getId());
		entity.setRemark(entity.getRemark());
		QueryWrapper<MaterialCategory> queryWrapper=new QueryWrapper<MaterialCategory>();
		queryWrapper.eq("name", entity.getName());
		queryWrapper.eq("shopid", shopid);
		List<MaterialCategory> list = materialCategoryService.list(queryWrapper);
		boolean result=false;
		if(StrUtil.isEmpty(entity.getId())) {
			//新增
			if(list.size()>0) {
				throw new BizException("类型名称不能重复");
			}
			entity.setId(UUIDUtil.getUUIDshort());
			result=materialCategoryService.save(entity);
		}else {
			//修改
			if(list!=null && list.size()>0) {
				if(list.get(0).getId().equals(entity.getId()) && list.size()==1) {
					result=materialCategoryService.updateById(entity);
				}else{
					throw new BizException("类型名称不能重复");
				}
			}else {
				result=materialCategoryService.updateById(entity);
			}
			
		}
		if(result==true){
			map.put("msg", "添加成功");
		}else{
			map.put("msg", "添加失败");
		}
		return Result.success(map);
	}
	 

	@PostMapping("/list")
	public Result<IPage<MaterialCategory>> getListData(@RequestBody MaterialCateDTO dto){
		UserInfo userinfo = UserInfoContext.get();
		String shopid=userinfo.getCompanyid();
		String search = dto.getSearch();
		return Result.success(materialCategoryService.findByCondition(dto.getPage(),shopid,search));
	}
	
	@GetMapping("/getcategory")
	public Result<List<MaterialCategory>> getCategoryAction(){
		UserInfo userinfo = UserInfoContext.get();
		String shopid=userinfo.getCompanyid();
		List<MaterialCategory> catelist = materialCategoryService.findAllCategory(shopid);
		return Result.success(catelist);
	}
	
	@GetMapping("/getcategoryType")
	public Result<List<MaterialCategory>> getCategoryTypeAction(String type){
		UserInfo userinfo = UserInfoContext.get();
		String shopid=userinfo.getCompanyid();
		List<MaterialCategory> catelist = materialCategoryService.findCategory(shopid,type);
		return Result.success(catelist);
	}
	
	@GetMapping("/delcategory")
	public  Result<Map<String,Object>> delDataAction(String id){
		Map<String,Object> map=new HashMap<String, Object>();
		boolean result = materialCategoryService.removeById(id);
		if(result==true){
			map.put("msg", "删除成功");
		}else{
			map.put("msg", "删除失败");
		}
		return Result.success(map);
	}
}
