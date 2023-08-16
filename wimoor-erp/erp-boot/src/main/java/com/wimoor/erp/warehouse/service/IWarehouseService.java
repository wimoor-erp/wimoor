package com.wimoor.erp.warehouse.service;


import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;

public interface IWarehouseService extends IService<Warehouse> {

	//展示页面
	public IPage<Warehouse> findByCondition(Page<?> page,String search, String shopid, String ftype,String parentid );
	//所有fba仓库
	String selectTypeByName(String ftypename);

	/**
	 * 查詢fba仓库和自有仓的所有仓位
	 * @param ftype
	 * @param shopid
	 * @return
	 */
	List<Warehouse> findByType(String ftype, String shopid);
 
	/**
	 * 查询所有自有仓的仓位
	 * @param shopid
	 * @return
	 */
    List<Warehouse> selectSelfAllByShopId(String shopid);
   
    List<Map<String,Object>> selectFbaSale(String shopid,  String sku, String delivery_cycle);
	
    List<Map<String, Object>> selectSelfSale(String shopid, List<String> warehouseidList,  String materialid);
	
	Integer saveCheckOut(String wname,String sernum,String parentid);
	
	Integer saveQuality(String wname,String sernum,String parentid);
	
	Integer saveScrap(String wname,String sernum,String parentid);
	
	Integer deleteInfoById(String id) throws ERPBizException;
	
	Integer saveMyware(Warehouse wh) throws ERPBizException, Exception;
	/**
	 * 查询所有自有仓（父类仓库），按照默认仓库排序
	 * @param shopid
	 * @return
	 */
    List<Warehouse> getSelfAllByShopIdOrderByDefault(String shopid) ;
   
    Integer updateMyware(Warehouse wh,String parentid) throws ERPBizException;

	Warehouse findAvailableBySelf(String warehouseid);

	LinkedList<Map<String, Object>> selectShipFbaSale(String shopid, String sku, String materialid, String planid,String groupid);

	/**
	 * 查询默认正品仓
	 * @param shopid
	 * @return
	 */
	List<Warehouse> selectDefaultSelfUsableByShopId(String shopid);

	List<Warehouse> getSubWarehouse(String warehouseid);
	
	Warehouse getSubWarehouseByType(String warehouseid,String type);

	/**
	 * 查询自有仓的总库存和总货值
	 * @param warehouseid（父类）
	 * @return
	 */
	Map<String, Object> getTotalInvAndWorth(String warehouseid);
	
	Warehouse getParentWarehouse(String warehouseid);

	public void clearDefaultWare(String shopid,String ftype) ;

	List<Warehouse> findBydefault(String shopid);
 
	public List<Warehouse> getSubWarehouseListByType(String warehouseid,String type);
	
	public Warehouse getWarehouseByid(String warehouseid);
	
	public Warehouse getSelfWarehouse(String warehouseid);

	Warehouse getWarehouseByName(String shopid, String whname);

	void saveWareTypeList(String warehouselistStr, String shopid, UserInfo user,Warehouse house) throws Exception;

	List<Warehouse> findWarehouselistByParent(String warehouseid);

	int updateWarehousePlace(Map<String, Object> map) throws Exception;

	List<Warehouse> getPlaceWarehouseList(String id);

	Warehouse getPlaceWarehouse(String id);
	
	public List<Warehouse> getWarehouseTreeList(UserInfo user);

	String getUUID();
	public List<Warehouse> getOverseaWarehouse(String shopid, String ftype, String groupid, String country);

}
