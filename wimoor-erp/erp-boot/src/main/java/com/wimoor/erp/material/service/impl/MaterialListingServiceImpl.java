package com.wimoor.erp.material.service.impl;

import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.erp.material.mapper.MaterialListingMapper;
import com.wimoor.erp.material.pojo.entity.MaterialListing;
import com.wimoor.erp.material.service.IMaterialListingService;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;

@Service
public class MaterialListingServiceImpl extends ServiceImpl<MaterialListingMapper, MaterialListing>
        implements IMaterialListingService {

    /**
     * HTML白名单：允许常用Listing格式化标签和安全属性
     */
    private static final Safelist LISTING_SAFELIST = Safelist.relaxed()
            .addTags("div", "span")
            .addAttributes(":all", "style", "class")
            .addAttributes("img", "alt", "width", "height")
            .addAttributes("table", "border", "cellpadding", "cellspacing")
            .addAttributes("td", "colspan", "rowspan")
            .addAttributes("th", "colspan", "rowspan");

    @Override
    public List<MaterialListing> listByMaterialId(String materialid) {
        LambdaQueryWrapper<MaterialListing> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MaterialListing::getMaterialid, materialid)
               .select(MaterialListing::getId,
                       MaterialListing::getMaterialid,
                       MaterialListing::getLang,
                       MaterialListing::getTitle,
                       MaterialListing::getUpdateTime);
        return this.list(wrapper);
    }

    @Override
    public MaterialListing getByMaterialIdAndLang(String materialid, String lang) {
        LambdaQueryWrapper<MaterialListing> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MaterialListing::getMaterialid, materialid)
               .eq(MaterialListing::getLang, lang);
        return this.getOne(wrapper);
    }

    @Override
    public MaterialListing saveListing(MaterialListing listing) {
        // XSS过滤description
        if (StrUtil.isNotBlank(listing.getDescription())) {
            String cleanHtml = Jsoup.clean(listing.getDescription(), LISTING_SAFELIST);
            listing.setDescription(cleanHtml);
        }

        // 查询是否已存在
        MaterialListing existing = getByMaterialIdAndLang(listing.getMaterialid(), listing.getLang());
        Date now = new Date();

        if (existing != null) {
            // 更新
            existing.setTitle(listing.getTitle());
            existing.setDescription(listing.getDescription());
            existing.setOperator(listing.getOperator());
            existing.setUpdateTime(now);
            this.updateById(existing);
            return existing;
        } else {
            // 新增
            if (StrUtil.isBlank(listing.getId())) {
                listing.setId(IdUtil.getSnowflakeNextIdStr());
            }
            listing.setCreateTime(now);
            listing.setUpdateTime(now);
            this.save(listing);
            return listing;
        }
    }

    @Override
    public boolean deleteListing(String id) {
        if (StrUtil.isBlank(id)) {
            return true;
        }
        MaterialListing existing = this.getById(id);
        if (existing == null) {
            return true; // 幂等
        }
        return this.removeById(id);
    }
}
