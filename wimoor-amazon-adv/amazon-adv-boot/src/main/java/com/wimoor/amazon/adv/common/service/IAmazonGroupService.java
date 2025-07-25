package com.wimoor.amazon.adv.common.service;
import java.util.List;
import java.util.Map;

import com.wimoor.amazon.adv.common.pojo.AmazonGroup;


public interface IAmazonGroupService extends IService<AmazonGroup> {
	int insertInitGroup(String shopid);

	int updateInitAuthGroupId(String shopid);

	List<AmazonGroup> selectByShopId(String shopid);

    List<AmazonGroup> findShopNameByUser(String shopid);
    
    List<AmazonGroup> findAdvShopNameByUser(String shopid);

	int getShopCountByShopId(String shopid);

	int insertAmazonGroup(String shopname, String shopid, String userid);
	
	AmazonGroup findAmazonGroupByName(String groupname, String shopid) ;
	
	int deleteAmzAdvGroup(String id, String shopid);
	
	int updateAmzAdvGroup(String sname, String id, String shopid);
	
	List<AmazonGroup> selectUsedByShop(String shopid);
	
	List<Map<String, Object>> findAdvGroup(String groupid, String shopid);
	
	List<Map<String, Object>> findAmazonGroup(String groupid, String shopid);
}
