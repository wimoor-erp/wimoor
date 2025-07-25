package com.wimoor.erp.common.service.impl;

import java.io.InputStream;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.pojo.entity.Picture;
import com.wimoor.common.service.IPictureService;
import com.wimoor.common.service.impl.StorageService;
import com.wimoor.common.service.impl.PictureServiceImpl;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.mapper.ImportRecordMapper;
import com.wimoor.erp.common.pojo.entity.ImportRecord;
import com.wimoor.erp.common.service.IImportRecordService;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;


@Service("importRecordService")
@RequiredArgsConstructor
public class ImportRecordServiceImpl extends ServiceImpl<ImportRecordMapper, ImportRecord> implements IImportRecordService{
	
	final IPictureService pictureService;
	final StorageService storageService;
	
	@Override
	public List<ImportRecord> getImportRecord(String shopid, String importtype) {
		return this.baseMapper.selectByShopid(shopid, importtype);
	}



}
