package com.wimoor.erp.stock.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.stock.pojo.entity.ErpDispatchOverseaCase;

import java.util.List;

public interface IErpDispatchOverseaCaseService  extends IService<ErpDispatchOverseaCase>{

	List<ErpDispatchOverseaCase> findByBox(String boxid, Integer boxnum);

	List<ErpDispatchOverseaCase> findShipInboundCaseByBoxid(String boxid);

}
