package com.wimoor.erp.ship.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.ship.pojo.entity.ErpShipTransType;

public interface IErpShipTransTypeService  extends IService<ErpShipTransType>{

	boolean updateById(UserInfo user, ErpShipTransType oldone);

	boolean save(UserInfo user, ErpShipTransType item);

	boolean removeById(UserInfo user, String typeid);

	List<ErpShipTransType> list(UserInfo user, QueryWrapper<ErpShipTransType> query);

}
