package com.wimoor.erp.ship.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.erp.ship.mapper.ShipTransChannelMapper;
import com.wimoor.erp.ship.mapper.ShipTransCompanyMapper;
import com.wimoor.erp.ship.pojo.entity.ShipTransChannel;
import com.wimoor.erp.ship.pojo.entity.ShipTransCompany;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.erp.ship.mapper.ShipTransDetailMapper;
import com.wimoor.erp.ship.pojo.entity.ShipTransDetail;
import com.wimoor.erp.ship.service.IShipTransDetailService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service("shipTransDetailService")
public class ShipTransDetailServiceImpl extends  ServiceImpl<ShipTransDetailMapper,ShipTransDetail> implements IShipTransDetailService {

	final ShipTransCompanyMapper shipTransCompanyMapper;
	final ShipTransChannelMapper shipTransChannelMapper;
	@Override
	public int usedTransType(String shopid, String transtype) {
		// TODO Auto-generated method stub
		return this.baseMapper.usedTransType(shopid,transtype);
	}


	@Override
	public Map<String,Object> searchChannelDetails(Map<String,Object> params) {
		Map<String,Object> map=new HashMap<String,Object>();
		String company = params.get("company").toString();
		String channeltype = params.get("channeltype").toString();
		String channel = params.get("channel").toString();
		String shopid = params.get("shopid").toString();
		String marketplaceid = params.get("marketplaceid").toString();
		String companyid=null;
		LambdaQueryWrapper<ShipTransCompany> query=new LambdaQueryWrapper<ShipTransCompany>();
		query.eq(ShipTransCompany::getName, company);
		query.eq(ShipTransCompany::getShopid, shopid);
		List<ShipTransCompany> companyPojo = shipTransCompanyMapper.selectList(query);
		if(companyPojo!=null&&companyPojo.size()>0){
			companyid=companyPojo.get(0).getId();
			map.put("companyid", companyid);
			LambdaQueryWrapper<ShipTransDetail> chanquery=new LambdaQueryWrapper<ShipTransDetail>();
			chanquery.eq(ShipTransDetail::getChanname, channel);
			chanquery.eq(ShipTransDetail::getCompany, companyid);
			chanquery.eq(ShipTransDetail::getMarketplaceid, marketplaceid);
			ShipTransDetail channeldetail = this.baseMapper.selectOne(chanquery);
			if(channeldetail!=null){
				map.put("id", channeldetail.getId());
				map.put("pretime", channeldetail.getId());
			}else{
				LambdaQueryWrapper<ShipTransChannel> query2=new LambdaQueryWrapper<ShipTransChannel>();
				query2.eq(ShipTransChannel::getShopid, shopid);
				query2.eq(ShipTransChannel::getName, channeltype);
				List<ShipTransChannel> typelist = shipTransChannelMapper.selectList(query2);
				if(typelist!=null&&typelist.size()>0){
					ShipTransChannel chantype = typelist.get(0);
					LambdaQueryWrapper<ShipTransDetail> chanquery2=new LambdaQueryWrapper<ShipTransDetail>();
					chanquery2.eq(ShipTransDetail::getCompany, companyid);
					chanquery2.eq(ShipTransDetail::getChannel, chantype.getId());
					chanquery2.eq(ShipTransDetail::getMarketplaceid, marketplaceid);
					ShipTransDetail channeldetail2 = this.baseMapper.selectOne(chanquery2);
					if(channeldetail2!=null){
						map.put("id", channeldetail2.getId());
						map.put("pretime", channeldetail2.getId());
					}
				}
			}
		}else{
			throw new RuntimeException("公司不存在");
		}
		return map;
	}
	
}
