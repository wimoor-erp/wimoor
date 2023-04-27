package com.wimoor.amazon.report.service;

import java.util.Date;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.report.pojo.entity.ReportRequestRecord;
public interface IReportRequestRecordService  extends IService<ReportRequestRecord> {

	Date lastUpdateRequestByType(Map<String, Object> param);

}
