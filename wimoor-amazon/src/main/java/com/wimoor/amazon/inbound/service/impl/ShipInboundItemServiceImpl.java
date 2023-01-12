package com.wimoor.amazon.inbound.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.inbound.mapper.ShipInboundItemMapper;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundItem;
import com.wimoor.amazon.inbound.service.IShipInboundItemService;
import com.wimoor.api.amzon.inbound.pojo.vo.ShipInboundItemVo;
import com.wimoor.api.amzon.inbound.pojo.vo.ShipInboundShipmenSummarytVo;

import lombok.RequiredArgsConstructor;

@Service("shipInboundItemService")
@RequiredArgsConstructor
public class ShipInboundItemServiceImpl extends  ServiceImpl<ShipInboundItemMapper,ShipInboundItem> implements IShipInboundItemService {
	 

	 public int updateItem(ShipInboundItem item){
				QueryWrapper<ShipInboundItem> queryWrapper = new QueryWrapper<ShipInboundItem>();
				queryWrapper.eq("shipmentid",item.getShipmentid());
				queryWrapper.eq("sellersku",item.getSellersku());
				queryWrapper.eq("inboundplanid",item.getInboundplanid());
	            return super.baseMapper.update(item, queryWrapper);
	        }

		public ShipInboundShipmenSummarytVo summaryShipmentItem(String shipmentid) {
			ShipInboundShipmenSummarytVo itemsum = this.baseMapper.summaryShipmentItem(shipmentid);
			List<ShipInboundItemVo> itemvoList=listByShipmentid(shipmentid);
			itemsum.setItemList(itemvoList);
			return itemsum;
		}
		
		public List<ShipInboundItemVo> listByShipmentid(String shipmentid){
			List<ShipInboundItemVo> itemvoList=this.baseMapper.selectObjByShipmentid(shipmentid);
			return itemvoList;
		}
		public ShipInboundItem findItem(String shipmentid, String inboundplanid, String sellersku) {
			// TODO Auto-generated method stub
			QueryWrapper<ShipInboundItem> queryWrapper = new QueryWrapper<ShipInboundItem>();
			queryWrapper.eq("shipmentid",shipmentid);
			queryWrapper.eq("sellersku",sellersku);
			queryWrapper.eq("inboundplanid",inboundplanid);
			return this.baseMapper.selectOne(queryWrapper);
		}

		@Override
		public List<ShipInboundItem> getItemByShipment(String shipmentid) {
			// TODO Auto-generated method stub
			return this.baseMapper.getOneByShipmentid(shipmentid);
		}

		@Override
		public List<Map<String, Object>> getshotTime(String companyid, String groupid, String marketplaceid,
				String sku) {
			// TODO Auto-generated method stub
			return this.baseMapper.getshotTime(companyid, groupid, marketplaceid, sku);
		}
		
	
}
