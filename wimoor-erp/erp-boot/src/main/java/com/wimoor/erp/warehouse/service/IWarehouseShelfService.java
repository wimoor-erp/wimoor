package com.wimoor.erp.warehouse.service;

 
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.warehouse.pojo.entity.ErpWarehouseAddress;
import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelf;
import com.wimoor.erp.warehouse.pojo.vo.WarehouseShelfTreeVo;
import com.wimoor.erp.warehouse.pojo.vo.WarehouseShelfVo;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 仓库货柜 服务类
 * </p>
 *
 * @author wimoor team
 * @since 2022-03-19
 */
public interface IWarehouseShelfService extends IService<WarehouseShelf> {

	List<WarehouseShelfTreeVo> getAllTree(UserInfo user, String warehouseid);

	boolean save(List<WarehouseShelf> shelflist);

	boolean logicDeleteById(String ids);
	
	public String getAllParentName(String shelfid) ;
	
	List<WarehouseShelfVo>  detail(UserInfo user,String id);

	List<WarehouseShelfVo>  detailWarehouse(UserInfo user,String warehouseid);
	
	public WarehouseShelfVo getShelfInfo(UserInfo user,String shelfid);

	WarehouseShelfVo getShelfInfo(UserInfo user, ErpWarehouseAddress address, String shelftreepath);

	WarehouseShelfVo detailWarehouseSum(UserInfo user, String warehouseid); 

}
