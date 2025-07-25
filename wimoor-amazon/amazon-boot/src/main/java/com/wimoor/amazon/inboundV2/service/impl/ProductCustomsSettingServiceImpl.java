package com.wimoor.amazon.inboundV2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.inboundV2.mapper.ProductCustomsSettingMapper;
import com.wimoor.amazon.inboundV2.pojo.entity.ProductCustomsSetting;
import com.wimoor.amazon.inboundV2.service.IProductCustomsSettingService;
import com.wimoor.common.user.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductCustomsSettingServiceImpl extends ServiceImpl<ProductCustomsSettingMapper, ProductCustomsSetting> implements IProductCustomsSettingService {


    @Override
    public void saveSettingRate(UserInfo user, List<ProductCustomsSetting> dto) {
        if(dto!=null){
            for(ProductCustomsSetting item:dto){
                item.setShopid(user.getCompanyid());
                if(item.getRate()!=null && item.getRate().compareTo(BigDecimal.ZERO)>0){
                    LambdaQueryWrapper<ProductCustomsSetting> query=new LambdaQueryWrapper<ProductCustomsSetting>();
                    query.eq(ProductCustomsSetting::getMarketplaceid,item.getMarketplaceid());
                    query.eq(ProductCustomsSetting::getShopid,user.getCompanyid());
                    ProductCustomsSetting old = this.baseMapper.selectOne(query);
                    if(old!=null){
                        old.setPriceType(item.getPriceType());
                        old.setRate(item.getRate());
                        this.baseMapper.update(old,query);
                    }else{
                        this.baseMapper.insert(item);
                    }
                }
            }
        }
    }

    @Override
    public List<Map<String, Object>> getSettingRate(UserInfo user) {
        List<Map<String, Object>> list = this.baseMapper.getSettingRate(user.getCompanyid());
        if(list!=null){
            LambdaQueryWrapper<ProductCustomsSetting> query=new LambdaQueryWrapper<ProductCustomsSetting>();
            query.eq(ProductCustomsSetting::getMarketplaceid,"EU");
            query.eq(ProductCustomsSetting::getShopid,user.getCompanyid());
            ProductCustomsSetting euset = this.baseMapper.selectOne(query);
            Map<String, Object> eumap=new HashMap<String, Object>();
            eumap.put("marketplaceId", "EU");
            eumap.put("marketplaceid", "EU");
            eumap.put("region", "EU");
            eumap.put("name", "欧洲");
            eumap.put("region_name", "欧洲");
            eumap.put("market", "EU");
            if(euset!=null){
                eumap.put("price_type", euset.getPriceType());
                eumap.put("rate", euset.getRate());
            }else{
                eumap.put("price_type", "cost");
                eumap.put("rate", 0);
            }
            list.add(eumap);
        }
        return list;
    }
}
