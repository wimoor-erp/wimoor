package com.wimoor.amazon.adv.common.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wimoor.amazon.adv.common.dao.AmzAdvRemarkMapper;
import com.wimoor.amazon.adv.common.pojo.AmzAdvRemark;
import com.wimoor.amazon.adv.common.service.IAmzAdvRemarkService;
import com.wimoor.amazon.base.BaseService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service("amzAdvRemarkService")
public class AmzAdvRemarkServiceImpl extends BaseService<AmzAdvRemark> implements IAmzAdvRemarkService{

	@Resource
	AmzAdvRemarkMapper amzAdvRemarkMapper;
	
	public int addRemark(List<AmzAdvRemark> list) {
		// TODO Auto-generated method stub
		return amzAdvRemarkMapper.insertList(list);
	}

	public int updateRemark(List<AmzAdvRemark> list) {
		// TODO Auto-generated method stub
		int temp = 0;
		for(int i = 0; i < list.size(); i++) {
			AmzAdvRemark record = list.get(i);
			Example example = new Example(AmzAdvRemark.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", record.getProfileid());
			crit.andEqualTo("campaignid", record.getCampaignid());
			crit.andEqualTo("adgroupid", record.getAdgroupid());
			crit.andEqualTo("keywordid", record.getKeywordid());
			crit.andEqualTo("adid", record.getAdid());
			crit.andEqualTo("targetid", record.getTargetid());
			AmzAdvRemark amzAdvRemark = amzAdvRemarkMapper.selectOneByExample(example);
			if(amzAdvRemark != null) {
				temp += this.updateNotNull(record);
			}else {
				temp += this.save(record);
			}
		}
		return temp;
	}

}
