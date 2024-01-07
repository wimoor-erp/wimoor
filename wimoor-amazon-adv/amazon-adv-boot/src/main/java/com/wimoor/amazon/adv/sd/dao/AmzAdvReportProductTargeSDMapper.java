package com.wimoor.amazon.adv.sd.dao;

import java.util.List;

import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportProductTargetsSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportProductTargetsSDAttributed;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportProductTargetsSDAttributedNew;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportProductTargetsSDAttributedSame;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportProductTargetsSDAttributedView;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvReportProductTargeSDMapper extends BaseMapper<AmzAdvReportProductTargetsSD>{

	void insertBatch(List<AmzAdvReportProductTargetsSD> list);

	void insertBatchAttributed(List<AmzAdvReportProductTargetsSDAttributed> listAttributed);

	void insertBatchAttributedView(List<AmzAdvReportProductTargetsSDAttributedView> listView);

	void insertBatchAttributedSame(List<AmzAdvReportProductTargetsSDAttributedSame> listAttributedSame);

	void insertBatchAttributedNew(List<AmzAdvReportProductTargetsSDAttributedNew> listAttributedNew);
     
}