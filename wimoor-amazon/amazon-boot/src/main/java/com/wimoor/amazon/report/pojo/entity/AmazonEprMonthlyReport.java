package com.wimoor.amazon.report.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("t_amz_epr_monthly_report")
@ApiModel(value="t_amz_epr_monthly_report对象", description="epr月度报表")
public class AmazonEprMonthlyReport {

    @TableField(value=  "sellerid")
    private String sellerid;

    @TableField(value=  "report_period_start")
    private Date reportPeriodStart;

    @TableField(value=  "report_period_end")
    private Date reportPeriodEnd;

    @TableField(value = "asin")
    private String asin;
    @TableField(value = "amazon_marketplace")
    private String amazonMarketplace;
    @TableField(value = "ship_to_country")
    private String shipToCountry;
    @TableField(value = "ship_to_country_code")
    private String shipToCountryCode;
//    @TableField(value = "item_name_in_english")
//    private String itemNameInEnglish;
//    @TableField(value = "item_name_as_in_marketplace")
//    private String itemNameAsInMarketplace;
    @TableField(value = "registration_number")
    private String registrationNumber;
    @TableField(value = "epr_category")
    private String eprCategory;
    @TableField(value = "epr_subcategory1")
    private String eprSubcategory1;
    @TableField(value = "epr_subcategory2")
    private String eprSubcategory2;
    @TableField(value = "epr_subcategory3")
    private String eprSubcategory3;
    @TableField(value = "epr_subcategory4")
    private String eprSubcategory4;
    @TableField(value = "gl_product_group_description")
    private String glProductGroupDescription;
    @TableField(value = "product_type")
    private String productType;
    @TableField(value = "total_units_sold")
    private Integer totalUnitsSold;
    @TableField(value = "units_per_asin")
    private Integer unitsPerAsin;
    @TableField(value = "battery_embedded")
    private String batteryEmbedded;
    @TableField(value = "item_weight_without_package_kg")
    private BigDecimal itemWeightWithoutPackageKg;
    @TableField(value = "item_weight_with_package_kg")
    private BigDecimal itemWeightWithPackageKg;
    @TableField(value = "total_reported_weight_kg")
    private BigDecimal totalReportedWeightKg;
    @TableField(value = "item_width_cm")
    private BigDecimal itemWidthCm;
    @TableField(value = "package_width_cm")
    private BigDecimal packageWidthCm;
    @TableField(value = "item_height_cm")
    private BigDecimal itemHeightCm;
    @TableField(value = "package_height_cm")
    private BigDecimal packageHeightCm;
    @TableField(value = "paper_kg")
    private BigDecimal paperKg;
    @TableField(value = "glass_kg")
    private BigDecimal glassKg;
    @TableField(value = "aluminum_kg")
    private BigDecimal aluminumKg;
    @TableField(value = "steel_kg")
    private BigDecimal steelKg;
    @TableField(value = "plastic_kg")
    private BigDecimal plasticKg;
    @TableField(value = "wood_kg")
    private BigDecimal woodKg;
    @TableField(value = "other_kg")
    private BigDecimal otherKg;

}
