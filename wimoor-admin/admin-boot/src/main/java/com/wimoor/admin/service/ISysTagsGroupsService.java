package com.wimoor.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.admin.pojo.entity.SysTagsGroups;

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
