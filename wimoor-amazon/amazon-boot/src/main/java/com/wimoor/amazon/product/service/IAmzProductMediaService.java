package com.wimoor.amazon.product.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.product.pojo.entity.AmzProductMedia;

/**
 * Amazon 商品媒体服务。
 *
 * @author wimoor
 */
public interface IAmzProductMediaService extends IService<AmzProductMedia> {

    /**
     * 查询指定 SKU 在指定授权+站点下的全部媒体。
     */
    List<AmzProductMedia> listBySku(String authorityId, String marketplaceId, String sku);

    /**
     * 全量替换某 SKU 的媒体（refresh 调用）。
     *
     * @return 写入条数
     */
    int replaceForSku(String shopid, String authorityId, String marketplaceId, String sku,
                      String asin, List<AmzProductMedia> medias);
}
