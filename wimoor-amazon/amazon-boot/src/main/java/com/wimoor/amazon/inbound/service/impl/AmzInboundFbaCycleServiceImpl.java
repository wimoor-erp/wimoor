package com.wimoor.amazon.inbound.service.impl;

import com.wimoor.amazon.inbound.pojo.entity.AmzInboundFbaCycle;
import com.wimoor.amazon.inbound.mapper.AmzInboundFbaCycleMapper;
import com.wimoor.amazon.inbound.service.IAmzInboundFbaCycleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigInteger;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
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
public class AmzInboundFbaCycleServiceImpl extends ServiceImpl<AmzInboundFbaCycleMapper, AmzInboundFbaCycle> implements IAmzInboundFbaCycleService {

	@Override
	@Cacheable(value = "FbaCycle")
	public List<AmzInboundFbaCycle> getInboundFbaCycle(String shopid,String marketplaceid) {
		// TODO Auto-generated method stub
		LambdaQueryWrapper<AmzInboundFbaCycle> queryInbound=new LambdaQueryWrapper<AmzInboundFbaCycle>();
		queryInbound.eq(AmzInboundFbaCycle::getShopid, shopid);
		queryInbound.eq(AmzInboundFbaCycle::getMarketplaceid, marketplaceid);
		List<AmzInboundFbaCycle> result = this.list(queryInbound);
	    return result;
	}
	
	@Cacheable(value = "FbaCycle")
	public AmzInboundFbaCycle getDefaultInboundFbaCycle(List<AmzInboundFbaCycle> cycleList) {
		// TODO Auto-generated method stub
		AmzInboundFbaCycle result=null;
		if(cycleList!=null&&cycleList.size()>0) {
			for(AmzInboundFbaCycle item:cycleList) {
				if(item.getIsdefault()) {
					result=item;
					break;
				}
			}
		}
		if(result==null) {
			result=new AmzInboundFbaCycle();
			result.setFirstLegDays(30);
			result.setPutOnDays(2);
			result.setStockingCycle(3);
			result.setMinCycle(7);
			result.setTranstype(new BigInteger("1"));
		}
	    return result;
	}
	@Cacheable(value = "FbaCycle")
	public AmzInboundFbaCycle getTransInboundFbaCycle(List<AmzInboundFbaCycle> cycleList,BigInteger transtype) {
		// TODO Auto-generated method stub
		AmzInboundFbaCycle result=null;
		if(cycleList!=null&&cycleList.size()>0) {
			for(AmzInboundFbaCycle item:cycleList) {
				if(item.getTranstype().equals(transtype)) {
					result=item;
					break;
				}
			}
		}
		if(result==null) {
			result=getDefaultInboundFbaCycle(cycleList);
		}
	    return result;
	}
	

}
