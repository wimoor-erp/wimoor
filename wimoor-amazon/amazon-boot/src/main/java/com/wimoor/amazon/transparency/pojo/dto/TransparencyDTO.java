package com.wimoor.amazon.transparency.pojo.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.common.pojo.entity.BasePageQuery;
import lombok.Data;

import java.util.List;

/**
 * 
 * @TableName t_amz_transparency_skuinfo
 */
@TableName(value ="t_amz_transparency_skuinfo")
@Data
public class TransparencyDTO extends BasePageQuery {
    /**
     * 
     */
    private String gtin;

    /**
     * 
     */
    private String groupid;

    /**
     *
     */
    private String shopid;

    /**
     * 
     */
    private String asin;

    /**
     *
     */
    private String status;
    /**
     * 
     */
    private String sku;

    /**
     *
     */
    private String taskid;

    private List<String> tcodelist;

    /**
     * 
     */
    private String authid;

    private String ftype;

}