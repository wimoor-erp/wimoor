/*
 * Fulfillment Inbound v2024-03-20
 * The Selling Partner API for Fulfillment By Amazon (FBA) Inbound. The FBA Inbound API enables building inbound workflows to create, manage, and send shipments into Amazon's fulfillment network. The API has interoperability with the Send-to-Amazon user interface.
 *
 * OpenAPI spec version: 2024-03-20
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.amazon.spapi.model.fulfillmentinboundV20240320;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * Specifies the &#39;ship from&#39; address for the shipment.
 */
@ApiModel(description = "Specifies the 'ship from' address for the shipment.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2024-11-13T14:17:39.430+08:00")
public class ShipmentSource {
  @SerializedName("address")
  private Address address = null;

  @SerializedName("sourceType")
  private String sourceType = null;

  public ShipmentSource address(Address address) {
    this.address = address;
    return this;
  }

   /**
   * Get address
   * @return address
  **/
  @ApiModelProperty(value = "")
  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public ShipmentSource sourceType(String sourceType) {
    this.sourceType = sourceType;
    return this;
  }

   /**
   * The type of source for this shipment. Possible values: &#x60;SELLER_FACILITY&#x60;.
   * @return sourceType
  **/
  @ApiModelProperty(required = true, value = "The type of source for this shipment. Possible values: `SELLER_FACILITY`.")
  public String getSourceType() {
    return sourceType;
  }

  public void setSourceType(String sourceType) {
    this.sourceType = sourceType;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShipmentSource shipmentSource = (ShipmentSource) o;
    return Objects.equals(this.address, shipmentSource.address) &&
        Objects.equals(this.sourceType, shipmentSource.sourceType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(address, sourceType);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShipmentSource {\n");
    
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    sourceType: ").append(toIndentedString(sourceType)).append("\n");
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

