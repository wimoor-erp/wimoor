package com.wimoor.amazon.product.service.impl;

import com.wimoor.amazon.product.pojo.entity.MerchantShippingGroup;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.product.mapper.MerchantShippingGroupMapper;
import com.wimoor.amazon.product.service.IMerchantShippingGroupService;
import com.wimoor.amazon.product.service.IProductProductTypeService;
import com.wimoor.common.GeneralUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amazon.spapi.model.definitions.ProductTypeDefinition;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2023-07-12
 */
@Service
public class MerchantShippingGroupServiceImpl extends ServiceImpl<MerchantShippingGroupMapper, MerchantShippingGroup> implements IMerchantShippingGroupService {
    @Autowired
    IProductProductTypeService iProductProductTypeService;
	@Resource
	private IAmazonAuthorityService amazonAuthorityService;
	@Override
	public List<MerchantShippingGroup> listGroups(String amazonauthid, String marketplaceid) {
		// TODO Auto-generated method stub
		  List<MerchantShippingGroup> list = this.lambdaQuery().eq(MerchantShippingGroup::getAmazonauthid, amazonauthid)
		                  .eq(MerchantShippingGroup::getMarketplaceid, marketplaceid).list();
		return list;
	}

	
	@Override
	public List<MerchantShippingGroup> refreshListGroups(String amazonauthid, String marketplaceid) {
		// TODO Auto-generated method stub
		AmazonAuthority authority = amazonAuthorityService.getById(amazonauthid);
		ProductTypeDefinition response = iProductProductTypeService.captureTypeDefinition(authority, marketplaceid, "SANDAL");
	    if(response!=null&&response.getSchema()!=null) {
	    	String jsonstr = GeneralUtil.loadJson(response.getSchema().getLink().getResource());
	    	JSONObject json =jsonstr==null?null: GeneralUtil.getJsonObject(jsonstr);
	    	JSONObject properties = json==null?null:json.getJSONObject("properties");
	    	JSONObject merchant_shipping_group =properties==null?null: properties.getJSONObject("merchant_shipping_group");
	    	JSONObject items =merchant_shipping_group==null?null: merchant_shipping_group.getJSONObject("items");
	    	JSONObject itemproperties =items==null?null: items.getJSONObject("properties");
            JSONObject itemvalue=itemproperties==null?null:itemproperties.getJSONObject("value");
	    	JSONArray shipIdEnum =itemvalue==null?null: itemvalue.getJSONArray("enum");
	    	JSONArray shipNameEnumNames =itemvalue==null?null: itemvalue.getJSONArray("enumNames");
	    	if(shipIdEnum!=null&&shipNameEnumNames!=null) {
	    		for(int i=0;i<shipIdEnum.size();i++) {
		    		String id=shipIdEnum.getString(i);
		    		String name=shipNameEnumNames.getString(i);
		    		LambdaQueryWrapper<MerchantShippingGroup> query=new LambdaQueryWrapper<MerchantShippingGroup>();
		    		query.eq(MerchantShippingGroup::getAmazonauthid, amazonauthid);
		    		query.eq(MerchantShippingGroup::getMarketplaceid, marketplaceid);
		    		query.eq(MerchantShippingGroup::getId, id);
		    		MerchantShippingGroup item = this.getOne(query);
		    		 if(item!=null) {
		    			 item.setName(name);
		    			 item.setRefreshtime(new Date());
		    			 this.baseMapper.update(item,query);
		    		 }else {
		    			 item=new MerchantShippingGroup();
		    			 item.setAmazonauthid(new BigInteger(amazonauthid));
		    			 item.setId(id);
		    			 item.setMarketplaceid(marketplaceid);
		    			 item.setName(name);
		    			 item.setRefreshtime(new Date());
		    			 this.baseMapper.insert(item);
		    		 }
		    	}
	    	}
	    }
		 List<MerchantShippingGroup> list = this.lambdaQuery().eq(MerchantShippingGroup::getAmazonauthid, amazonauthid)
                 .eq(MerchantShippingGroup::getMarketplaceid, marketplaceid).list();
         return list;
	}
}
