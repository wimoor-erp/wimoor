package com.wimoor.erp.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.pojo.entity.DownloadReport;

public interface IDownloadReportService  extends IService<DownloadReport> {
    void addRun(UserInfo user);
    void updateError(Throwable e, UserInfo user);
    void updateContent(byte[] byteArray, UserInfo user);
}
