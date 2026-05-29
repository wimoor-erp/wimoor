package com.wimoor.amazon.product.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.product.mapper.AmzProductMediaMapper;
import com.wimoor.amazon.product.pojo.entity.AmzProductMedia;
import com.wimoor.amazon.product.service.IAmzProductMediaService;

@Service
public class AmzProductMediaServiceImpl
        extends ServiceImpl<AmzProductMediaMapper, AmzProductMedia>
        implements IAmzProductMediaService {

    @Override
    public List<AmzProductMedia> listBySku(String authorityId, String marketplaceId, String sku) {
        return this.baseMapper.selectBySku(authorityId, marketplaceId, sku);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int replaceForSku(String shopid, String authorityId, String marketplaceId, String sku,
                             String asin, List<AmzProductMedia> medias) {
        this.baseMapper.deleteBySku(authorityId, marketplaceId, sku);
        if (medias == null || medias.isEmpty()) {
            return 0;
        }
        Date now = new Date();
        int count = 0;
        for (AmzProductMedia m : medias) {
            m.setId(null);
            m.setShopid(shopid);
            m.setAuthorityId(authorityId);
            m.setMarketplaceId(marketplaceId);
            m.setSku(sku);
            m.setAsin(asin);
            m.setSyncTime(now);
            m.setCreateTime(now);
            if (m.getMediaType() == null) m.setMediaType(0);
            if (m.getSortOrder() == null) m.setSortOrder(count);
            this.baseMapper.insert(m);
            count++;
        }
        return count;
    }
}
