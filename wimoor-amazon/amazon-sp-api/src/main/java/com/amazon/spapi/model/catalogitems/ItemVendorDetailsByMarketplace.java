/*
 * Selling Partner API for Catalog Items
 * The Selling Partner API for Catalog Items provides programmatic access to information about items in the Amazon catalog.  For more information, refer to the [Catalog Items API Use Case Guide](doc:catalog-items-api-v2022-04-01-use-case-guide).
 *
 * OpenAPI spec version: 2022-04-01
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.amazon.spapi.model.catalogitems;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.amazon.spapi.model.catalogitems.ItemVendorDetailsCategory;
import java.io.IOException;

/**
 * Vendor details associated with an Amazon catalog item for the indicated Amazon marketplace.
 */
@ApiModel(description = "Vendor details associated with an Amazon catalog item for the indicated Amazon marketplace.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2024-01-30T16:09:25.977+08:00")
public class ItemVendorDetailsByMarketplace {
  @SerializedName("marketplaceId")
  private String marketplaceId = null;

  @SerializedName("brandCode")
  private String brandCode = null;

  @SerializedName("manufacturerCode")
  private String manufacturerCode = null;

  @SerializedName("manufacturerCodeParent")
  private String manufacturerCodeParent = null;

  @SerializedName("productCategory")
  private ItemVendorDetailsCategory productCategory = null;

  @SerializedName("productGroup")
  private String productGroup = null;

  @SerializedName("productSubcategory")
  private ItemVendorDetailsCategory productSubcategory = null;

  /**
   * Replenishment category associated with an Amazon catalog item.
   */
  @JsonAdapter(ReplenishmentCategoryEnum.Adapter.class)
  public enum ReplenishmentCategoryEnum {
    ALLOCATED("ALLOCATED"),
    
    BASIC_REPLENISHMENT("BASIC_REPLENISHMENT"),
    
    IN_SEASON("IN_SEASON"),
    
    LIMITED_REPLENISHMENT("LIMITED_REPLENISHMENT"),
    
    MANUFACTURER_OUT_OF_STOCK("MANUFACTURER_OUT_OF_STOCK"),
    
    NEW_PRODUCT("NEW_PRODUCT"),
    
    NON_REPLENISHABLE("NON_REPLENISHABLE"),
    
    NON_STOCKUPABLE("NON_STOCKUPABLE"),
    
    OBSOLETE("OBSOLETE"),
    
    PLANNED_REPLENISHMENT("PLANNED_REPLENISHMENT");

    private String value;

    ReplenishmentCategoryEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static ReplenishmentCategoryEnum fromValue(String text) {
      for (ReplenishmentCategoryEnum b : ReplenishmentCategoryEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

    public static class Adapter extends TypeAdapter<ReplenishmentCategoryEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ReplenishmentCategoryEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public ReplenishmentCategoryEnum read(final JsonReader jsonReader) throws IOException {
        String value = jsonReader.nextString();
        return ReplenishmentCategoryEnum.fromValue(String.valueOf(value));
      }
    }
  }

  @SerializedName("replenishmentCategory")
  private ReplenishmentCategoryEnum replenishmentCategory = null;

  public ItemVendorDetailsByMarketplace marketplaceId(String marketplaceId) {
    this.marketplaceId = marketplaceId;
    return this;
  }

   /**
   * Amazon marketplace identifier.
   * @return marketplaceId
  **/
  @ApiModelProperty(required = true, value = "Amazon marketplace identifier.")
  public String getMarketplaceId() {
    return marketplaceId;
  }

  public void setMarketplaceId(String marketplaceId) {
    this.marketplaceId = marketplaceId;
  }

  public ItemVendorDetailsByMarketplace brandCode(String brandCode) {
    this.brandCode = brandCode;
    return this;
  }

   /**
   * Brand code associated with an Amazon catalog item.
   * @return brandCode
  **/
  @ApiModelProperty(value = "Brand code associated with an Amazon catalog item.")
  public String getBrandCode() {
    return brandCode;
  }

  public void setBrandCode(String brandCode) {
    this.brandCode = brandCode;
  }

  public ItemVendorDetailsByMarketplace manufacturerCode(String manufacturerCode) {
    this.manufacturerCode = manufacturerCode;
    return this;
  }

   /**
   * Manufacturer code associated with an Amazon catalog item.
   * @return manufacturerCode
  **/
  @ApiModelProperty(value = "Manufacturer code associated with an Amazon catalog item.")
  public String getManufacturerCode() {
    return manufacturerCode;
  }

  public void setManufacturerCode(String manufacturerCode) {
    this.manufacturerCode = manufacturerCode;
  }

  public ItemVendorDetailsByMarketplace manufacturerCodeParent(String manufacturerCodeParent) {
    this.manufacturerCodeParent = manufacturerCodeParent;
    return this;
  }

   /**
   * Parent vendor code of the manufacturer code.
   * @return manufacturerCodeParent
  **/
  @ApiModelProperty(value = "Parent vendor code of the manufacturer code.")
  public String getManufacturerCodeParent() {
    return manufacturerCodeParent;
  }

  public void setManufacturerCodeParent(String manufacturerCodeParent) {
    this.manufacturerCodeParent = manufacturerCodeParent;
  }

  public ItemVendorDetailsByMarketplace productCategory(ItemVendorDetailsCategory productCategory) {
    this.productCategory = productCategory;
    return this;
  }

   /**
   * Product category associated with an Amazon catalog item.
   * @return productCategory
  **/
  @ApiModelProperty(value = "Product category associated with an Amazon catalog item.")
  public ItemVendorDetailsCategory getProductCategory() {
    return productCategory;
  }

  public void setProductCategory(ItemVendorDetailsCategory productCategory) {
    this.productCategory = productCategory;
  }

  public ItemVendorDetailsByMarketplace productGroup(String productGroup) {
    this.productGroup = productGroup;
    return this;
  }

   /**
   * Product group associated with an Amazon catalog item.
   * @return productGroup
  **/
  @ApiModelProperty(value = "Product group associated with an Amazon catalog item.")
  public String getProductGroup() {
    return productGroup;
  }

  public void setProductGroup(String productGroup) {
    this.productGroup = productGroup;
  }

  public ItemVendorDetailsByMarketplace productSubcategory(ItemVendorDetailsCategory productSubcategory) {
    this.productSubcategory = productSubcategory;
    return this;
  }

   /**
   * Product subcategory associated with an Amazon catalog item.
   * @return productSubcategory
  **/
  @ApiModelProperty(value = "Product subcategory associated with an Amazon catalog item.")
  public ItemVendorDetailsCategory getProductSubcategory() {
    return productSubcategory;
  }

  public void setProductSubcategory(ItemVendorDetailsCategory productSubcategory) {
    this.productSubcategory = productSubcategory;
  }

  public ItemVendorDetailsByMarketplace replenishmentCategory(ReplenishmentCategoryEnum replenishmentCategory) {
    this.replenishmentCategory = replenishmentCategory;
    return this;
  }

   /**
   * Replenishment category associated with an Amazon catalog item.
   * @return replenishmentCategory
  **/
  @ApiModelProperty(value = "Replenishment category associated with an Amazon catalog item.")
  public ReplenishmentCategoryEnum getReplenishmentCategory() {
    return replenishmentCategory;
  }

  public void setReplenishmentCategory(ReplenishmentCategoryEnum replenishmentCategory) {
    this.replenishmentCategory = replenishmentCategory;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ItemVendorDetailsByMarketplace itemVendorDetailsByMarketplace = (ItemVendorDetailsByMarketplace) o;
    return Objects.equals(this.marketplaceId, itemVendorDetailsByMarketplace.marketplaceId) &&
        Objects.equals(this.brandCode, itemVendorDetailsByMarketplace.brandCode) &&
        Objects.equals(this.manufacturerCode, itemVendorDetailsByMarketplace.manufacturerCode) &&
        Objects.equals(this.manufacturerCodeParent, itemVendorDetailsByMarketplace.manufacturerCodeParent) &&
        Objects.equals(this.productCategory, itemVendorDetailsByMarketplace.productCategory) &&
        Objects.equals(this.productGroup, itemVendorDetailsByMarketplace.productGroup) &&
        Objects.equals(this.productSubcategory, itemVendorDetailsByMarketplace.productSubcategory) &&
        Objects.equals(this.replenishmentCategory, itemVendorDetailsByMarketplace.replenishmentCategory);
  }

  @Override
  public int hashCode() {
    return Objects.hash(marketplaceId, brandCode, manufacturerCode, manufacturerCodeParent, productCategory, productGroup, productSubcategory, replenishmentCategory);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ItemVendorDetailsByMarketplace {\n");
    
    sb.append("    marketplaceId: ").append(toIndentedString(marketplaceId)).append("\n");
    sb.append("    brandCode: ").append(toIndentedString(brandCode)).append("\n");
    sb.append("    manufacturerCode: ").append(toIndentedString(manufacturerCode)).append("\n");
    sb.append("    manufacturerCodeParent: ").append(toIndentedString(manufacturerCodeParent)).append("\n");
    sb.append("    productCategory: ").append(toIndentedString(productCategory)).append("\n");
    sb.append("    productGroup: ").append(toIndentedString(productGroup)).append("\n");
    sb.append("    productSubcategory: ").append(toIndentedString(productSubcategory)).append("\n");
    sb.append("    replenishmentCategory: ").append(toIndentedString(replenishmentCategory)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

