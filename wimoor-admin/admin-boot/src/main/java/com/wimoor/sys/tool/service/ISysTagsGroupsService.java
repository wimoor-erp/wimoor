package com.wimoor.sys.tool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.sys.tool.pojo.entity.SysTagsGroups;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wimoor team
 * @since 2022-10-28
 */
public interface ISysTagsGroupsService extends IService<SysTagsGroups> {

	void disableList(String groupid);

}
