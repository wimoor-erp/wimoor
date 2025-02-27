package com.wimoor.amazon.common.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.common.mapper.ImportRecordMapper;
import com.wimoor.amazon.common.pojo.entity.ImportRecord;
import com.wimoor.amazon.common.service.IImportRecordService;

import lombok.RequiredArgsConstructor;

@Service("importRecordService")
@RequiredArgsConstructor
public class ImportRecordServiceImpl extends ServiceImpl<ImportRecordMapper, ImportRecord> implements IImportRecordService{

	@Override
	public List<ImportRecord> getImportRecord(String shopid, String importtype) {
		return this.baseMapper.selectByShopid(shopid, importtype);
	}

	 

}
