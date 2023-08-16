package com.wimoor.amazon.adv.common.service.impl;


import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.wimoor.amazon.adv.common.dao.AmazonGroupMapper;
import com.wimoor.amazon.adv.common.pojo.AmazonGroup;
import com.wimoor.amazon.adv.common.service.IAmazonGroupService;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.mvc.BizException;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service("amazonGroupService")
public class AmazonGroupServiceImpl extends BaseService<AmazonGroup> implements IAmazonGroupService {
	@Resource
	AmazonGroupMapper amazonGroupMapper;
 
	
	@Override
	@CacheEvict(value = "profitCfgCache", allEntries = true)
	public int updateAll(AmazonGroup entity) throws BizException {
		// TODO Auto-generated method stub
		return super.updateAll(entity);
	}

	public List<AmazonGroup> selectUsedByShop(String shopid) {
		return amazonGroupMapper.selectUsedByShop(shopid);
	}

	public int insertInitGroup(String shopid) {
		return amazonGroupMapper.insertInitGroup(shopid);
	}

	public int updateInitAuthGroupId(String shopid) {
		return amazonGroupMapper.updateInitAuthGroupId(shopid);
	}

	public List<AmazonGroup> findShopNameByUser(String shopid) {
		return amazonGroupMapper.findShopNameByUser(shopid);
	}
	
	@Cacheable(value = "amzAdvProfileCache")
	public List<AmazonGroup> findAdvShopNameByUser(String shopid) {
		return amazonGroupMapper.findAdvShopNameByUser(shopid);
	}

	public List<AmazonGroup> selectByShopId(String shopid) {
		Example example = new Example(AmazonGroup.class);
		Criteria crit = example.createCriteria();
		crit.andEqualTo("shopid", shopid);
		List<AmazonGroup> glist = amazonGroupMapper.selectByExample(example);
		return glist;
	}

	public int getShopCountByShopId(String shopid) {
		return amazonGroupMapper.getShopCountByShopId(shopid);
	}

	public int insertAmazonGroup(String shopname, String shopid, String userid) {
		Example example = new Example(AmazonGroup.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("shopid", shopid);
		criteria.andEqualTo("name", shopname);
		AmazonGroup record = new AmazonGroup();
		record.setName(shopname);
		record.setShopid(shopid);
		record.setCreator(userid);
		record.setCreatetime(new Date());
		record.setOperator(userid);
		record.setOpttime(new Date());
		return amazonGroupMapper.insert(record);
	}

	public AmazonGroup findAmazonGroupByName(String groupname, String shopid) {
		Example example = new Example(AmazonGroup.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("shopid", shopid);
		criteria.andEqualTo("name", groupname);
		List<AmazonGroup> amazonGroup = this.selectByExample(example);
		if (amazonGroup != null && amazonGroup.size() > 0) {
			return amazonGroup.get(0);
		}
		return null;
	}
	
	public int deleteAmzAdvGroup(String id, String shopid) {
	 
		return amazonGroupMapper.deleteAmazonGroup(id, shopid);
	}
	
	public int updateAmzAdvGroup(String sname, String id, String shopid) {
		Example example = new Example(AmazonGroup.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("shopid", shopid);
		criteria.andEqualTo("name", sname);
		List<AmazonGroup> amazonGroup = amazonGroupMapper.selectByExample(example);
		if (amazonGroup != null && amazonGroup.size() > 0) {
			if (amazonGroup.get(0).getId().equals(id)) {
				AmazonGroup group = new AmazonGroup();
				group.setId(id);
				group.setName(sname);
				return amazonGroupMapper.updateByPrimaryKeySelective(group);
			}
			return 0;
		} else {
			AmazonGroup group = new AmazonGroup();
			group.setId(id);
			group.setName(sname);
			return amazonGroupMapper.updateByPrimaryKeySelective(group);
		}
	}

	public List<Map<String, Object>> findAdvGroup(String groupid, String shopid) {
		return amazonGroupMapper.findAdvGroup(groupid, shopid);
	}

	public List<Map<String, Object>> findAmazonGroup(String groupid, String shopid) {
		return amazonGroupMapper.findAmazonGroup(groupid, shopid);
	}
	
}
