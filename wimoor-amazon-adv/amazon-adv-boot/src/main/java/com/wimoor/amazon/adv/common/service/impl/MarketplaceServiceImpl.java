package com.wimoor.amazon.adv.common.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.wimoor.amazon.adv.common.dao.MarketplaceMapper;
import com.wimoor.amazon.adv.common.pojo.Marketplace;
import com.wimoor.amazon.adv.common.service.IMarketplaceService;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.mvc.BizException;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

@Service("marketplaceService")
public class MarketplaceServiceImpl extends BaseService<Marketplace> implements IMarketplaceService {
	@Resource
	MarketplaceMapper marketplaceMapper;
	Map<String, Marketplace> map = null;
	Map<String, Marketplace> map2 = null;

	public void clearMarketPlaceMap() {
		map = null;
		map2 = null;
	}
	
	@Cacheable(value = "marketplaceCache")
	public Marketplace selectByPKey(String marketplaceid) {
		return super.selectByKey(marketplaceid);
	}

	@Cacheable(value = "marketplaceListCache")
	public List<Marketplace> findAllMarketplace() {
		Example example = new Example(Marketplace.class);
		example.setOrderByClause("findex asc");
		return selectByExample(example);
	}

	public static boolean addRegionMap(Map<String, ArrayList<Marketplace>> resultMap, Marketplace item, String region) {
		if (StringUtil.isEmpty(region)) {
			if (resultMap.get("other") == null) {
				resultMap.put("other", new ArrayList<Marketplace>());
			}
			ArrayList<Marketplace> mlist = resultMap.get("other");
			mlist.add(item);
			return true;
		} else if (region.equals(item.getRegion())||(region.equals("EU")&&item.getRegion().equals("UK"))) {
			if (resultMap.get(region) == null) {
				resultMap.put(region, new ArrayList<Marketplace>());
			}
			ArrayList<Marketplace> mlist = resultMap.get(region);
			mlist.add(item);
			return true;
		}
		return false;
	}

	public static Map<String, ArrayList<Marketplace>> getRegionMapList(List<Marketplace> list) {
		Map<String, ArrayList<Marketplace>> resultMap = new HashMap<String, ArrayList<Marketplace>>();
		for (Marketplace item : list) {
			if (addRegionMap(resultMap, item, "NA")) {
				continue;
			} else if (addRegionMap(resultMap, item, "EU")) {
				continue;
			} else if (addRegionMap(resultMap, item, "IN")) {
				continue;
			} else if (addRegionMap(resultMap, item, "JP")) {
				continue;
			} else if (addRegionMap(resultMap, item, "AU")) {
				continue;
			} else {
				addRegionMap(resultMap, item, null);
			}
		}
		return resultMap;
	}

	public Map<String, Marketplace> findMapByPoint() {
		if (map != null) {
			return map;
		}
		map = new HashMap<String, Marketplace>();
		List<Marketplace> marketlist = findAllMarketplace();
		for (Marketplace market : marketlist) {
			map.put(market.getPointName(), market);
		}
		return map;
	}

	public Map<String, Marketplace> findMapByMarketplaceId() {
		if (map2 != null) {
			return map2;
		}
		map2 = new HashMap<String, Marketplace>();
		List<Marketplace> marketlist = findAllMarketplace();
		for (Marketplace market : marketlist) {
			map2.put(market.getMarketplaceid(), market);
		}
		return map2;
	}

	@Cacheable(value = "marketplaceCache")
	public Marketplace findMarketplaceByCountry(String country) {
		Example example = new Example(Marketplace.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("market", country);
		List<Marketplace> marketlist = selectByExample(example);
		if (marketlist.size() > 0) {
			return marketlist.get(0);
		} else {
			return null;
		}
	}

	@Cacheable(value = "AWSmarketplaceListCache")
	public List<Marketplace> findAWSMarketplaceList() {
		Example example = new Example(Marketplace.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIsNotNull("devAccountNum");
		example.setOrderByClause("findex asc");
		List<Marketplace> marketlist = selectByExample(example);
		return marketlist;
	}

	@Cacheable(value = "marketplaceListCache")
	public List<Marketplace> findMarketplaceByRegion(String region) {
		Example example = new Example(Marketplace.class);
		Example.Criteria criteria = example.createCriteria();
		if(region.equals("UK")) {
			criteria.orEqualTo("region", "EU");
			criteria.orEqualTo("region", "UK");
		}else {
			criteria.andEqualTo("region", region);
		}
		List<Marketplace> marketlist = selectByExample(example);
		return marketlist;
	}

	@Cacheable(value = "authmarketplaceListCache")
	public List<Marketplace> findMarketplaceByGroup(String groupid) {
		List<Marketplace> marketlist = marketplaceMapper.findByGroup(groupid);
		return marketlist;
	}

	@Cacheable(value = "authmarketplaceListCache")
	public List<Marketplace> findMarketplaceByShopId(String shopid) {
		List<Marketplace> marketlist = marketplaceMapper.findByShopid(shopid);
		return marketlist;
	}

	public List<Map<String, Object>> findEUMarketPriorityByGroup(String groupid) {
		List<Map<String, Object>> marketlist = marketplaceMapper.findEUPriorityByGroup(groupid);
		return marketlist;
	}

	public List<Map<String, Object>> findCommMarketpaceByShopid(String shopid) {
		List<Map<String, Object>> marketplaceList = marketplaceMapper.findCommMarketpaceByShopid(shopid);
		return marketplaceList;
	}

	public List<Marketplace> selectMarketByGroupid(String shopid, String groupid) {
		return marketplaceMapper.selectMarketByGroupid(shopid, groupid);
	}

	public List<Marketplace> selectMarketBySellerid(Map<String, Object> param) {
		return marketplaceMapper.selectMarketBySellerid(param);
	}
	
	@Cacheable(value = "authmarketplaceListCache")
	public List<Marketplace> findbyauth(String amazonAuthorityId) {
		return marketplaceMapper.findbyauth(amazonAuthorityId);
	}

	@Override
	@CacheEvict(value = { "AWSmarketplaceListCache", "marketplaceListCache" }, allEntries = true)
	public int save(Marketplace entity) throws BizException {
		return super.save(entity);
	}

	@Override
	@CacheEvict(value = { "AWSmarketplaceListCache", "marketplaceListCache", "marketplaceCache" }, allEntries = true)
	public int delete(Object key) {
		return super.delete(key);
	}

	@CacheEvict(value = { "AWSmarketplaceListCache", "marketplaceListCache", "marketplaceCache" }, allEntries = true)
	public int updateEntity(Marketplace entity) throws BizException {
		return super.updateAll(entity);
	}

	public List<Marketplace> findINMarketplace() {
		Example example = new Example(Marketplace.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("market", "IN");
		List<Marketplace> marketlist = selectByExample(example);
		if (marketlist.size() > 0) {
			return marketlist;
		} else {
			return null;
		}
	}

	@Cacheable(value = "marketplaceCache")
	public Marketplace selectBySalesChannel(String sales_channel) {
		Example example = new Example(Marketplace.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("pointName", sales_channel);
		List<Marketplace> marketlist = selectByExample(example);
		if (marketlist.size() > 0) {
			return marketlist.get(0);
		} else {
			return null;
		}
	}

	public List<Marketplace> findByRole(String roleid, String shopid) {
		return marketplaceMapper.findByRole(roleid, shopid);
	}

	public List<Marketplace> findMarketplaceBySku(Map<String, String> param) {
		return marketplaceMapper.findMarketplaceBySku(param);
	}
}
