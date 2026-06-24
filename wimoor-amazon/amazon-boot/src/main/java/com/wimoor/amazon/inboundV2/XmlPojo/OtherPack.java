package com.wimoor.amazon.inboundV2.XmlPojo;


import lombok.Data;

import java.math.BigDecimal;

/**
     * 其他包装信息类
     */
@Data
    public class OtherPack {
        private BigDecimal packQty;            // 包装件数
        private String packType;               // 包装材料种类
    }