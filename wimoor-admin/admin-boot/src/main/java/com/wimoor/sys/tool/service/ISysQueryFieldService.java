package com.wimoor.sys.tool.service;

import com.wimoor.common.user.UserInfo;
import com.wimoor.sys.tool.pojo.entity.SysQueryField;
import com.wimoor.sys.tool.pojo.entity.SysQueryUserVersion;
import com.wimoor.sys.tool.pojo.entity.SysQueryVersionFeild;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
 
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wimoor team
 * @since 2023-04-26
 */
public interface ISysQueryFieldService extends IService<SysQueryField> {
	List<SysQueryField> findByQueryName(UserInfo user, String queryname);
    List<SysQueryField> findByQueryName(String queryname);
    public  Map<String, ArrayList<Map<String, Object>>>  findAllVersionByUser(UserInfo user, String queryname) ;
	SysQueryUserVersion saveUserVersion(UserInfo user, String queryname, List<SysQueryVersionFeild> fieldlist);
	public SysQueryUserVersion updateUserVersion(UserInfo user, SysQueryUserVersion version, List<SysQueryVersionFeild> fieldlist) ;
	public int deleteUserVersion(String version);
	SysQueryUserVersion saveUserVersion(UserInfo user, String queryname, String versionname, List<SysQueryVersionFeild> fieldlist);
	List<SysQueryUserVersion> getMyVersionFieldByUser(String id, String queryname);
	List<SysQueryVersionFeild> getVersionFieldByUserQueryName(String usersid, String queryname);
	List<SysQueryVersionFeild> getVersionFieldById(String id);
}
