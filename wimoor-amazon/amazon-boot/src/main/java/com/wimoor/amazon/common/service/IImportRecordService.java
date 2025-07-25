package com.wimoor.amazon.common.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.common.pojo.entity.ImportRecord;

 

public interface IImportRecordService extends IService<ImportRecord>{

	List<ImportRecord> getImportRecord(String shopid, String importtype);

}
