package com.wimoor.amazon.product.service.impl;

import com.wimoor.amazon.product.pojo.dto.ProductInAftersaleDTO;
import com.wimoor.amazon.product.pojo.entity.ProductInAftersale;
import com.wimoor.amazon.inbound.mapper.ShipInboundItemMapper;
import com.wimoor.amazon.product.mapper.ProductInAftersaleMapper;
import com.wimoor.amazon.product.service.IProductInAftersaleService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-11-28
 */
@Service
public class ProductInAftersaleServiceImpl extends ServiceImpl<ProductInAftersaleMapper, ProductInAftersale> implements IProductInAftersaleService {
    @Resource
    ShipInboundItemMapper shipInboundItemMapper;
	@Override
	public void insertBatch(List<ProductInAftersale> salesafter) {
		// TODO Auto-generated method stub
		if(salesafter!=null&&salesafter.size()>0) {
			this.baseMapper.insertBatch(salesafter);
		}
	}

	
	public List<Map<String,Object>> getSummary(String shopid,String groupid){
		return this.baseMapper.getSummary(shopid, groupid);
	}


	@Override
	public IPage<Map<String, Object>> findList(Page<Object> page, ProductInAftersaleDTO dto) {
		// TODO Auto-generated method stub
		if(dto.getFtype().equals("plan")) {
			return this.baseMapper.findList(page,dto);
		}else {
			return shipInboundItemMapper.findShipList(dto.getPage(), dto);
		}
		
	}
 
}
