package com.wimoor.admin.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wimoor.admin.pojo.entity.SysQueryField;
import com.wimoor.admin.pojo.entity.SysQueryUserVersion;
import com.wimoor.admin.service.ISysQueryFieldService;
import com.wimoor.admin.service.ISysQueryUserVersionService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2023-04-26
 */
@Controller
@RequestMapping("/sysQueryField")
public class SysQueryFieldController {
    @Autowired
    ISysQueryFieldService iSysQueryFieldService;
    @Autowired
    ISysQueryUserVersionService iSysQueryUserVersionService;
	@GetMapping(value = "/loadfield")
	public   Result<?>  loadFieldAction(String queryname)  {
	     UserInfo user = UserInfoContext.get();
	    List<SysQueryField> list = iSysQueryFieldService.findByQueryName(queryname);
	      Map<String, ArrayList<Map<String, Object>>> allversion = iSysQueryFieldService.findAllVersionByUser(user, queryname);
		Map<String,Object> result=new HashMap<String,Object>();
		result.put("list", list);
	    result.put("allversion", allversion);
	    return Result.success(result);
	}
	
	@GetMapping(value = "/loadMyField")
	public   Result<?>  loadMyFieldAction(String queryname)   {
	     UserInfo user = UserInfoContext.get();
	    List<SysQueryField> list = iSysQueryFieldService.findByQueryName(user,queryname);
	    return Result.success(list);
	}
	
 
	@PostMapping(value = "/saveMyVersionField/{queryname}" )
	public   Result<?>  saveMyVersionFieldAction(String queryname,@RequestBody  List<String> fieldlist) {
	     UserInfo user = UserInfoContext.get();
		if(fieldlist==null||fieldlist.size()==0)throw new BizException("选择的列表不能为空");
		SysQueryUserVersion verson= iSysQueryFieldService.saveUserVersion(user,queryname,fieldlist);
	    return Result.success(verson.getId());
	}
	
	@PostMapping(value = "/saveMyVersionFieldWithName/{queryname}/{versionname}" )
	public   Result<?>  saveMyVersionFieldAction(String queryname,String versionname,@RequestBody  List<String> fieldlist) {
	     UserInfo user = UserInfoContext.get();
		if(fieldlist==null||fieldlist.size()==0)throw new BizException("选择的列表不能为空");
		SysQueryUserVersion verson= iSysQueryFieldService.saveUserVersion(user,queryname,versionname,fieldlist);
	    return Result.success(verson.getId());
	}
	
	@PostMapping(value = "/useMyVersionField/{versionid}" )
	public   Result<?>  useVersionAction(String  versionid,@RequestBody List<String> list)  {
	     UserInfo user = UserInfoContext.get();
	     SysQueryUserVersion version = iSysQueryUserVersionService.getById(versionid);
		return Result.success(iSysQueryFieldService.updateUserVersion(user, version, list));
	}
	
	
	@GetMapping(value = "/deleteMyVersionField" )
	public   Result<?>  deleteMyVersionFieldAction(String versionid)  {
		return Result.success(iSysQueryFieldService.deleteUserVersion(versionid));
	}
}

