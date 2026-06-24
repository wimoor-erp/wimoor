package com.wimoor.amazon.inboundV2.XmlPojo;


import com.wimoor.amazon.inboundV2.XmlGenerator.CustomsDeclarationHelper;
import lombok.Data;

import java.math.BigDecimal;

/**
     * 许可证信息
     */
@Data
    public   class GoodsLimit {
        private int goodsNo;                   // 商品序号
        private String licTypeCode;            // 许可证类别代码
        private String licenceNo;              // 许可证编号
        private String licWrtofDetailNo;       // 许可证核销明细序号
        private BigDecimal licWrtofQty;        // 许可证核销数量
        private String licWrtofQtyUnit;        // 许可证核销数量单位

        // VIN信息
        private  GoodsLimitVin goodsLimitVin;
        public GoodsLimit() {
        }
        public GoodsLimit(int goodsNo, String licTypeCode, String licenceNo) {
            this.goodsNo = goodsNo;
            this.licTypeCode = licTypeCode;
            this.licenceNo = licenceNo;
        }
    }
