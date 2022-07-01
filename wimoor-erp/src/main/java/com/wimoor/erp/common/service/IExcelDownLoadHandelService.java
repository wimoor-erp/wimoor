package com.wimoor.erp.common.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.wimoor.common.user.UserInfo;

public interface IExcelDownLoadHandelService {
	List<Map<String,Object>> loadFile(UserInfo user, InputStream inputStream, String ftype, String marketplaceid, String groupid,String itemid,String stockid,String fileName);
}
