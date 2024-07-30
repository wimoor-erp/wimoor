package com.wimoor.sys.tool.controller;


import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.sys.tool.pojo.dto.SysQueryVersionDTO;
import com.wimoor.sys.tool.pojo.entity.SysQueryField;
import com.wimoor.sys.tool.pojo.entity.SysQueryUserVersion;
import com.wimoor.sys.tool.pojo.entity.SysQueryVersionFeild;
import com.wimoor.sys.tool.service.ISysQueryFieldService;
import com.wimoor.sys.tool.service.ISysQueryUserVersionService;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2023-04-26
 */
@Api(tags = "用户接口")
@RestController
@RequestMapping("/api/v1/sysQueryField")
@RequiredArgsConstructor
public class SysQueryFieldController {
    @Autowired
    ISysQueryFieldService iSysQueryFieldService;
    @Autowired
    ISysQueryUserVersionService iSysQueryUserVersionService;
	@GetMapping(value = "/loadfield")
 	public   Result<?>  loadFieldAction(String queryname)  {
		  UserInfo user = UserInfoContext.get();
		  List<SysQueryField> list = iSysQueryFieldService.findByQueryName(user,queryname);
		  if(list!=null&&list.size()>0) {
			  list.sort(new Comparator<SysQueryField>() {
				@Override
				public int compare(SysQueryField f1, SysQueryField f2) {
					// TODO Auto-generated method stub
					if(f1==null&&f2==null)return 0;
					if(f1==null)return -1;
					if(f2==null)return 1;
					return f1.getFindex().compareTo(f2.getFindex());
				}});
		  }
		  return Result.success(list);
	}
	
	@PostMapping(value = "/saveMyVersionField/{queryname}" )
	public   Result<?>  saveMyVersionFieldAction(@PathVariable String queryname,@RequestBody  List<SysQueryVersionFeild> fieldlist) {
	     UserInfo user = UserInfoContext.get();
		if(fieldlist==null||fieldlist.size()==0)throw new BizException("选择的列表不能为空");
		SysQueryUserVersion verson= iSysQueryFieldService.saveUserVersion(user,queryname,fieldlist);
	    return Result.success(verson.getId());
	}
	
	@PostMapping(value = "/saveMyVersionFieldWithName" )
	public   Result<?>  saveMyVersionFieldAction(@RequestBody  SysQueryVersionDTO dto) {
	     UserInfo user = UserInfoContext.get();
		if(dto.getFieldlist()==null||dto.getFieldlist().size()==0)throw new BizException("选择的列表不能为空");
		SysQueryUserVersion verson= iSysQueryFieldService.saveUserVersion(user,dto.getQueryname(),dto.getVersionname(),dto.getFieldlist());
	    return Result.success(verson.getId());
	}
	
	@PostMapping(value = "/useMyVersionField/{versionid}" )
	public   Result<?>  useVersionAction(@PathVariable String  versionid,@RequestBody List<SysQueryVersionFeild> list)  {
	     UserInfo user = UserInfoContext.get();
	     SysQueryUserVersion version = iSysQueryUserVersionService.getById(versionid);
		return Result.success(iSysQueryFieldService.updateUserVersion(user, version, list));
	}
	
	
	@GetMapping(value = "/deleteMyVersionField" )
	public   Result<?>  deleteMyVersionFieldAction(String versionid)  {
		return Result.success(iSysQueryFieldService.deleteUserVersion(versionid));
	}
	
	@GetMapping(value = "/getMyVersionFieldByUser" )
	@Cacheable(value = "VersionField")
	public   Result<?>  getMyVersionFieldByUserAction(String queryname)  {
		UserInfo user = UserInfoContext.get();
		return Result.success(iSysQueryFieldService.getMyVersionFieldByUser(user.getId(),queryname));
	}
	
	@GetMapping(value = "/getVersionFieldByUserQueryName" )
	public   Result<?>  getVersionFieldByUserQueryNameAction(String queryname)  {
		UserInfo user = UserInfoContext.get();
		List<SysQueryField> list = iSysQueryFieldService.findByQueryName(user,queryname);
		return Result.success(list);
	}
	
	@GetMapping(value = "/getVersionFieldById" )
	public   Result<?>  getVersionFieldByIdAction(String id)  {
		return Result.success(iSysQueryFieldService.getVersionFieldById(id));
	}
	
}

