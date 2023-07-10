package com.wimoor.amazon.adv.common.service;

import java.util.List;
import java.util.Map;

import com.wimoor.amazon.adv.common.pojo.Marketplace;



public interface IMarketplaceService extends IService<Marketplace> {
	public  Marketplace  findMarketplaceByCountry(String country);

	public List<Marketplace> findAWSMarketplaceList();

	public List<Marketplace> findMarketplaceByRegion(String region);
	
	public List<Marketplace> findMarketplaceByGroup(String groupid);

	public List<Marketplace> findMarketplaceByShopId(String shopid);
	
	public List<Map<String, Object>> findEUMarketPriorityByGroup(String amazongroupid);

	public List<Marketplace> findAllMarketplace();

	public Map<String,Marketplace> findMapByPoint();
	
	public Map<String,Marketplace> findMapByMarketplaceId();

	public List<Map<String, Object>> findCommMarketpaceByShopid(String shopid);

	public List<Marketplace> selectMarketByGroupid(String shopid, String groupid);
	
	List<Marketplace> selectMarketBySellerid(Map<String, Object> param);

	public List<Marketplace> findbyauth(String id);

	Marketplace selectByPKey(String marketplaceid);

	public List<Marketplace> findINMarketplace();

	public Marketplace selectBySalesChannel(String sales_channel);
	
	List<Marketplace> findByRole(String roleid,String shopid);

	public List<Marketplace> findMarketplaceBySku(Map<String, String> param);

	public int updateEntity(Marketplace entity);

	public void clearMarketPlaceMap();
}
