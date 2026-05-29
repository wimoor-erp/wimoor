package com.wimoor.amazon.api;

import java.io.Serializable;

import lombok.Data;

/**
 * Amazon 商品媒体 DTO（跨服务 Feign 传输用）。
 *
 * @author wimoor
 */
@Data
public class AmzProductMediaDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String sku;
    private String asin;
    private String marketplaceId;
    private String variant;          // MAIN/PT01..PT08/SWCH
    private Integer mediaType;
    private Integer sortOrder;
    private String url;
    private Integer width;
    private Integer height;
}
