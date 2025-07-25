package com.wimoor.amazon.auth.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;

 
public interface IMarketplaceService  extends IService<Marketplace> {
	public  Marketplace  findMarketplaceByCountry(String country);

	public List<Marketplace> findMarketplaceByRegion(String region);
	
	public List<Marketplace> findMarketplaceByGroup(String groupid);

	public List<Marketplace> findMarketplaceByShopId(String shopid);
	
	public List<Map<String, Object>> findEUMarketPriorityByGroup(String amazongroupid);

	public List<Marketplace> findAllMarketplace();

	public Map<String,Marketplace> findMapByPoint();
	
	public Map<String,Marketplace> findMapByMarketplaceId();

	public List<Map<String, Object>> findCommMarketpaceByShopid(String shopid);

	public List<Marketplace> selectMarketByGroupid(String shopid, String groupid);
	
	public List<Marketplace> selectMarketBySellerid(String sellerid,String Shopid);

	public List<Marketplace> findbyauth(String id);

	Marketplace selectByPKey(String marketplaceid);

	public List<Marketplace> findINMarketplace();

	public Marketplace selectBySalesChannel(String sales_channel);
	
	List<Marketplace> findByRole(String roleid,String shopid);


	public int updateEntity(Marketplace entity);

	public void clearMarketPlaceMap();
	
	public   Map<String, ArrayList<Marketplace>> getRegionMapList(List<Marketplace> list);
	
	public   Map<String, ArrayList<Marketplace>> getRegionMapList();
	
	public   String getMarketPlaceId(String mcurrency) ;

	public List<Marketplace> findByShopid(String companyid);
	List<Marketplace> getMarketPointBySkuGroup(String groupid,String sku);

	public List<Marketplace> getMarketPointByMSku(String companyid,String groupid,  String msku);
}
