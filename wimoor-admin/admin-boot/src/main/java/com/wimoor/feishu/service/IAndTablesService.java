package com.wimoor.feishu.service;

import com.wimoor.feishu.pojo.entity.Auth;
import com.wimoor.feishu.pojo.entity.SysFeishuTable;

import java.util.List;

public interface IAndTablesService {
    void create(Auth auth, String title);
    String getTableInfo(Auth auth, String url);
    SysFeishuTable getFields(Auth auth, String url, String name, String operator);
    String getRecord(Auth auth, String url, String filter);
    String updateCallback(Auth auth, String tableId, List<String> fieldkeys, String voucher);
    String addCallback(Auth auth, String tableId, String fieldName);
}
