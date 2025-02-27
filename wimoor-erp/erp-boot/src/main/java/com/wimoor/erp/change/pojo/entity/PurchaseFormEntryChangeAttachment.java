package com.wimoor.erp.change.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author wimoor team
 * @since 2024-03-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_erp_purchase_form_entry_change_attachment")
@ApiModel(value="PurchaseFormEntryChangeAttachment", description="")
public class PurchaseFormEntryChangeAttachment implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    private String entrychangeid;

    private String image;

    private String name;

    private String operator;

    private Date opttime;


}
