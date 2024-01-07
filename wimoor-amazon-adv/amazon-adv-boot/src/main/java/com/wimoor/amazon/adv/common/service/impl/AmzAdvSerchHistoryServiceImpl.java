package com.wimoor.amazon.adv.common.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wimoor.amazon.adv.common.dao.AmzAdvSerchHistoryMapper;
import com.wimoor.amazon.adv.common.pojo.AmzAdvSerchHistory;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.IAmzAdvSerchHistoryService;
import com.wimoor.amazon.base.BaseService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service("amzAdvSerchHistoryService")
public class AmzAdvSerchHistoryServiceImpl extends BaseService<AmzAdvSerchHistory> implements IAmzAdvSerchHistoryService{

	@Resource
	AmzAdvSerchHistoryMapper amzAdvSerchHistoryMapper;
	
	public int addSerchHistoryAction(String userId, String condition,String ftype) {
		// TODO Auto-generated method stub
		Example example = new Example(AmzAdvSerchHistory.class);
		Criteria crit = example.createCriteria();
		crit.andEqualTo("userid", userId);
		crit.andEqualTo("ftype", ftype);
		List<AmzAdvSerchHistory> list = amzAdvSerchHistoryMapper.selectByExample(example);
		if(list != null && list.size() > 4) {
			throw new BaseException("搜索历史记录已经达到上限！");
		}else {
			AmzAdvSerchHistory amzAdvSerchHistory = new AmzAdvSerchHistory();
			amzAdvSerchHistory.setFcondition(condition);
			amzAdvSerchHistory.setOpttime(new Date());
			amzAdvSerchHistory.setUserid(userId);
			amzAdvSerchHistory.setFtype(ftype);
			return this.save(amzAdvSerchHistory);
		}
	}

	public List<AmzAdvSerchHistory> getSerchHistoryAction(String userId,String ftype) {
		// TODO Auto-generated method stub
		Example example = new Example(AmzAdvSerchHistory.class);
		Criteria crit = example.createCriteria();
		crit.andEqualTo("userid", userId);
		crit.andEqualTo("ftype", ftype);
		List<AmzAdvSerchHistory> list = amzAdvSerchHistoryMapper.selectByExample(example);
		return list;
	}

	public int deleteSerchHistoryAction(String id) {
		// TODO Auto-generated method stub
		return this.delete(id);
	}

}
