package com.wimoor.amazon.product.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 * 
 * @TableName t_amz_data_kiosk_request
 */
@Data
@TableName(value ="t_amz_data_kiosk_request")
public class DataKioskRequest implements Serializable {

    /**
     * 
     */
    @TableField(value = "amazonauthid")
    private BigInteger amazonauthid;

    /**
     * 
     */
    @TableField(value = "marketplaceid")
    private String marketplaceid;

    /**
     * 
     */
    @TableField(value = "begin_date")
    private Date beginDate;

    /**
     * 
     */
    @TableField(value = "end_date")
    private Date endDate;

    /**
     * 
     */
    @TableId(value = "query_id")
    private BigInteger queryId;

    /**
     * 
     */
    @TableField(value = "data_document_id")
    private String dataDocumentId;

    @TableField(value = "processing_status")
    private String processingStatus;

    /**
     * 
     */
    @TableField(value = "document_url")
    private String documentUrl;


    @TableField(value = "status")
    private Integer status;

    /**
     * 
     */
    @TableField(value = "lastupdate")
    private Date lastupdate;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}