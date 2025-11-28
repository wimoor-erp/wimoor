package com.wimoor.amazon.transparency.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName t_amz_transparency_authority
 */
@TableName(value ="t_amz_transparency_authority")
@Data
public class TransparencyAuthority {
    /**
     * 
     */
    @TableId
    private String clientId;

    /**
     * 
     */
    private String clientSecret;

    /**
     * 
     */
    private String shopid;

    /**
     * 
     */
    private String token;

    /**
     * 
     */
    private Date expiry;

    private Boolean disabled;
    /**
     * 
     */
    private String clientName;

    private String creator;

    private Date createtime;
}