package com.wimoor.amazon.inbound.service.impl;

import com.wimoor.amazon.inbound.pojo.entity.AmzShipFulfillmentCenter;
import com.wimoor.amazon.inbound.mapper.AmzShipFulfillmentCenterMapper;
import com.wimoor.amazon.inbound.service.IAmzShipFulfillmentCenterService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-12-29
 */
@Service
public class AmzShipFulfillmentCenterServiceImpl extends ServiceImpl<AmzShipFulfillmentCenterMapper, AmzShipFulfillmentCenter> implements IAmzShipFulfillmentCenterService {

	@Override
	public AmzShipFulfillmentCenter getByCode(String destinationfulfillmentcenterid) {
		// TODO Auto-generated method stub
		LambdaQueryWrapper<AmzShipFulfillmentCenter> query=new LambdaQueryWrapper<AmzShipFulfillmentCenter>();
		query.eq(AmzShipFulfillmentCenter::getCode, destinationfulfillmentcenterid);
		 List<AmzShipFulfillmentCenter> list = this.baseMapper.selectList(query);
		 if(list!=null&&list.size()>0)return list.get(0);
		 return null;
	}

}
