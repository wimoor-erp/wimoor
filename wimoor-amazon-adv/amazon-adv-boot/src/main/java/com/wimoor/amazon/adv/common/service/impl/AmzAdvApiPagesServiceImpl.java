package com.wimoor.amazon.adv.common.service.impl;

import java.math.BigInteger;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wimoor.amazon.adv.common.dao.AmzAdvApiPagesMapper;
import com.wimoor.amazon.adv.common.pojo.AmzAdvApiPages;
import com.wimoor.amazon.adv.common.service.IAmzAdvApiPagesService;
import com.wimoor.amazon.base.BaseService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service("amzAdvApiPagesService")
public class AmzAdvApiPagesServiceImpl extends BaseService<AmzAdvApiPages>
implements IAmzAdvApiPagesService {
@Resource
AmzAdvApiPagesMapper amzAdvApiPagesMapper;

@Override
public AmzAdvApiPages getPage(BigInteger id, String path) {
	// TODO Auto-generated method stub
	Example example=new Example(AmzAdvApiPages.class);
	Criteria crit = example.createCriteria();
	crit.andEqualTo("profileid", id);
	crit.andEqualTo("apipath", path);
	return amzAdvApiPagesMapper.selectOneByExample(example);
}

public synchronized void savePage(BigInteger profileid, String apipath, Integer pageindex,String nexttoken,String flog) {
	// TODO Auto-generated method stub
	Example example=new Example(AmzAdvApiPages.class);
	Criteria crit = example.createCriteria();
	crit.andEqualTo("profileid", profileid);
	crit.andEqualTo("apipath", apipath);
	AmzAdvApiPages old = amzAdvApiPagesMapper.selectOneByExample(example);
	if(flog!=null&&flog.length()>2000) {
		flog=flog.substring(0, 1000);
	}
	if(old==null) {
		AmzAdvApiPages page=new AmzAdvApiPages();
		page.setApipath(apipath);
		page.setProfileid(profileid);
		page.setPages(pageindex);
		page.setOpttime(new Date());
		page.setNexttoken(nexttoken);
		page.setFlog(flog);
		amzAdvApiPagesMapper.insert(page);
	}else {
		old.setApipath(apipath);
		old.setProfileid(profileid);
		old.setNexttoken(nexttoken);
		old.setPages(pageindex);
		old.setOpttime(new Date());
		old.setFlog(flog);
		amzAdvApiPagesMapper.updateByExample(old,example);
	}
}

@Override
synchronized public void savePage(BigInteger profileid, String apipath, String  nexttoken) {
	// TODO Auto-generated method stub
	 this.savePage(profileid, apipath, null,nexttoken, null);
}


@Override
synchronized public void savePage(BigInteger profileid, String apipath, int pageindex) {
	// TODO Auto-generated method stub
	 this.savePage(profileid, apipath, pageindex,null, null);
}

@Override
synchronized public void savePage(BigInteger profileid, String apipath, int pageindex, String message) {
	// TODO Auto-generated method stub
	this.savePage(profileid, apipath, pageindex,null, message);
}

@Override
synchronized public void savePage(BigInteger profileid, String apipath, String nexttoken, String message) {
	// TODO Auto-generated method stub
	 this.savePage(profileid, apipath, null,nexttoken, message);
}

}
