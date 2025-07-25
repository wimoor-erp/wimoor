package com.wimoor.erp.stock.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.erp.stock.mapper.ErpDispatchOverseaCaseMapper;
import com.wimoor.erp.stock.pojo.entity.ErpDispatchOverseaCase;
import com.wimoor.erp.stock.service.IErpDispatchOverseaCaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("shipInboundCaseV2Service")
public class ShipInboundCaseServiceImpl extends  ServiceImpl<ErpDispatchOverseaCaseMapper, ErpDispatchOverseaCase> implements IErpDispatchOverseaCaseService {
 
	
	public List<ErpDispatchOverseaCase> findByBox(String boxid, Integer boxnum) {
		QueryWrapper<ErpDispatchOverseaCase> queryWrapper = new QueryWrapper<ErpDispatchOverseaCase>();
		// TODO Auto-generated method stub
		queryWrapper.eq("boxid", boxid);
		queryWrapper.eq("numberofcase", boxnum);
		return this.baseMapper.selectList(queryWrapper);
	}

	@Override
	public List<ErpDispatchOverseaCase> findShipInboundCaseByBoxid(String boxid) {
		// TODO Auto-generated method stub
		QueryWrapper<ErpDispatchOverseaCase> queryWrapper = new QueryWrapper<ErpDispatchOverseaCase>();
		// TODO Auto-generated method stub
		queryWrapper.eq("boxid", boxid);
		return this.baseMapper.selectList(queryWrapper);
	}

}
