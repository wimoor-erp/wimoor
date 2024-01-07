package com.wimoor.amazon.auth.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.auth.mapper.MarketplaceMapper;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.common.mvc.BizException;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
 

/**
 * 用户业务类
 */
@Service
@RequiredArgsConstructor
public class MarketplaceServiceImpl extends ServiceImpl<MarketplaceMapper, Marketplace> implements IMarketplaceService {
	 
	Map<String, Marketplace> map = null;
	Map<String, Marketplace> map2 = null;

	public void clearMarketPlaceMap() {
		map = null;
		map2 = null;
	}
	
	@Cacheable(value = "marketplaceCache")
	public Marketplace selectByPKey(String marketplaceid) {
		return this.getById(marketplaceid);
	}

	@Cacheable(value = "marketplaceListCache")
	public List<Marketplace> findAllMarketplace() {
		QueryWrapper<Marketplace> query = new QueryWrapper<Marketplace>();
		query.orderByAsc("findex");
		query.isNotNull("aws_region");
		List<Marketplace> list = this.list(query);
		for(Marketplace market:list) {
			market.setAccesskey("***");
			market.setAwsAccessKey("***");
			market.setDevAccountNum("***");
			market.setSecretkey("***");
			market.setAwsSecretKey("***");
			market.setDeveloperUrl("***");
		}
		return list;
	}

	public static boolean addRegionMap(Map<String, ArrayList<Marketplace>> resultMap, Marketplace item, String region) {
		if (StrUtil.isEmpty(region)) {
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

	public   Map<String, ArrayList<Marketplace>> getRegionMapList(List<Marketplace> list) {
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

	public   Map<String, ArrayList<Marketplace>> getRegionMapList() {
		List<Marketplace> list = findAllMarketplace();
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
	
	public   String getMarketPlaceId(String mcurrency) {
		String marketname = null;
		if (mcurrency != null) {
			if("USD".equals(mcurrency)) {
				marketname = "ATVPDKIKX0DER";
			}else if("CAD".equals(mcurrency)) {
				marketname = "A2EUQ1WTGCTBG2";
			}else if("GBP".equals(mcurrency)) {
				marketname = "A1F83G8C2ARO7P";
			}else if("INR".equals(mcurrency)) {
				marketname = "A21TJRUUN4KGV";
			}else if("JPY".equals(mcurrency)) {
				marketname = "A1VC38T7YXB528";
			}else if("AUD".equals(mcurrency)) {
				marketname = "A39IBJ37TRP1C6";
			}else if("MXN".equals(mcurrency)) {
				marketname = "A1AM78C64UM0Y8";
			}else if("EUR".equals(mcurrency)) {
				marketname = "EU";
			}else if("AED".equals(mcurrency)) {
				marketname = "A2VIGQ35RCS4UG";
			}else if("SEK".equals(mcurrency)) {
				marketname = "A2NODRKZP88ZB9";
			}else if("PLN".equals(mcurrency)) {
				marketname = "A1C3SOZRARQ6R3";
			}else if("SAR".equals(mcurrency)) {
				marketname = "A17E79C6D8DWNP";
			}
		}
		return marketname;
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

	@Cacheable(value = "marketplaceListCache")
	public Marketplace findMarketplaceByCountry(String country) {
		QueryWrapper<Marketplace> query = new QueryWrapper<Marketplace>();
		if(country!=null&&country.equals("GB")) {
			country="UK";
		}
		query.eq("market", country);
		List<Marketplace> marketlist = this.list(query);
		if (marketlist.size() > 0) {
			return marketlist.get(0);
		} else {
			return null;
		}
	}

 

	@Cacheable(value = "marketplaceListCache")
	public List<Marketplace> findMarketplaceByRegion(String region) {
		QueryWrapper<Marketplace> query = new QueryWrapper<Marketplace>();
		if(region.equals("UK")) {
			query.eq("region", "EU");
			query.eq("region", "UK");
		}else {
			query.eq("region", region);
		}
		List<Marketplace> marketlist = this.list(query);
		return marketlist;
	}

	@Cacheable(value = "authmarketplaceListCache")
	public List<Marketplace> findMarketplaceByGroup(String groupid) {
		List<Marketplace> marketlist = this.baseMapper.findByGroup(groupid);
		clearMarket(marketlist);
		return marketlist;
	}
    
	@Cacheable(value = "authmarketplaceListCache")
	public List<Marketplace> findMarketplaceByShopId(String shopid) {
		List<Marketplace> marketlist =  this.baseMapper.findByShopid(shopid);
		clearMarket(marketlist);
		return marketlist;
	}
    private void clearMarket(List<Marketplace> marketlist) {
		for(Marketplace market:marketlist) {
			market.setAwsAccessKey(null);
			market.setAwsSecretKey(null);
			market.setDevAccountNum(null);
			market.setAdvEndPoint(null);
			market.setAccesskey(null);
			market.setSecretkey(null);
		}
    }
	public List<Map<String, Object>> findEUMarketPriorityByGroup(String groupid) {
		List<Map<String, Object>> marketlist =  this.baseMapper.findEUPriorityByGroup(groupid);
		return marketlist;
	}

	public List<Map<String, Object>> findCommMarketpaceByShopid(String shopid) {
		List<Map<String, Object>> marketplaceList =  this.baseMapper.findCommMarketpaceByShopid(shopid);
		return marketplaceList;
	}

	public List<Marketplace> selectMarketByGroupid(String shopid, String groupid) {
		return  this.baseMapper.selectMarketByGroupid(shopid, groupid);
	}

	@Cacheable(value = "authmarketplaceListCache")
	public List<Marketplace> selectMarketBySellerid(String sellerid,String Shopid) {
		Map<String,Object> param =new HashMap<String,Object>();
		param.put("sellerid", sellerid);
		param.put("shopid", Shopid);
		List<Marketplace> marketlist =this.baseMapper.selectMarketBySellerid(param);
		this.clearMarket(marketlist);
		return marketlist;
	}
	
	@Cacheable(value = "authmarketplaceListCache")
	public List<Marketplace> findbyauth(String amazonAuthorityId) {
		return  this.baseMapper.findbyauth(amazonAuthorityId);
	}

	@Override
	@CacheEvict(value = { "AWSmarketplaceListCache", "marketplaceListCache" }, allEntries = true)
	public boolean save(Marketplace entity) throws BizException {
		return this.save(entity);
	}

 
	@CacheEvict(value = { "AWSmarketplaceListCache", "marketplaceListCache", "marketplaceCache" }, allEntries = true)
	public boolean delete(String key) {
		return this.delete(key);
	}

	@CacheEvict(value = { "AWSmarketplaceListCache", "marketplaceListCache", "marketplaceCache" }, allEntries = true)
	public int updateEntity(Marketplace entity) throws BizException {
		return this.updateById(entity)?1:0;
	}

	public List<Marketplace> findINMarketplace() {
		QueryWrapper<Marketplace> query = new QueryWrapper<Marketplace>();
		query.eq("market", "IN");
		List<Marketplace> marketlist = this.list(query);
		if (marketlist.size() > 0) {
			this.clearMarket(marketlist);
			return marketlist;
		} else {
			return null;
		}
	}

	@Cacheable(value = "marketplaceCache")
	public Marketplace selectBySalesChannel(String sales_channel) {
		QueryWrapper<Marketplace> query = new QueryWrapper<Marketplace>();
		query.eq("point_name", sales_channel);
		List<Marketplace> marketlist = this.list(query);
		if (marketlist.size() > 0) {
			return marketlist.get(0);
		} else {
			return null;
		}
	}

	public List<Marketplace> findByRole(String roleid, String shopid) {
		return this.baseMapper.findByRole(roleid, shopid);
	}

 

	@Override
	public List<Marketplace> findByShopid(String companyid) {
		// TODO Auto-generated method stub
		List<Marketplace> marketlist =  this.baseMapper.findByShopid(companyid);
		clearMarket(marketlist);
		return marketlist;
	}

	@Override
	@Cacheable(value = "marketplaceCache")
	public Marketplace getById(Serializable id) {
		// TODO Auto-generated method stub
		return this.baseMapper.selectById(id);
	}
 
	public List<Marketplace> getMarketPointBySkuGroup(String groupid,String sku){
		List<Marketplace> marketlist= this.baseMapper.getMarketPointBySkuGroup(sku, groupid);
		clearMarket(marketlist);
		return marketlist;
	}

	@Override
	public List<Marketplace> getMarketPointByMSku(String shopid,String groupid, String msku) {
		// TODO Auto-generated method stub
		List<Marketplace> marketlist= this.baseMapper.getMarketPointByMSku(msku,groupid, shopid);
		clearMarket(marketlist);
		return marketlist;
	}
     
}