/*
 * Amazon Shipping API
 * The Amazon Shipping API is designed to support outbound shipping use cases both for orders originating on Amazon-owned marketplaces as well as external channels/marketplaces. With these APIs, you can request shipping rates, create shipments, cancel shipments, and track shipments.
 *
 * OpenAPI spec version: v2
 * Contact: swa-api-core@amazon.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.amazon.spapi.model.shipping;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.amazon.spapi.model.shipping.CarrierAccountAttributes;
import com.amazon.spapi.model.shipping.ClientReferenceDetails;
import java.io.IOException;

/**
 * The request schema for verify and add the merchant&#39;s account with a certain carrier.
 */
@ApiModel(description = "The request schema for verify and add the merchant's account with a certain carrier.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2024-01-30T16:10:03.349+08:00")
public class LinkCarrierAccountRequest {
  @SerializedName("clientReferenceDetails")
  private ClientReferenceDetails clientReferenceDetails = null;

  @SerializedName("carrierAccountType")
  private String carrierAccountType = null;

  @SerializedName("carrierAccountAttributes")
  private CarrierAccountAttributes carrierAccountAttributes = null;

  @SerializedName("encryptedCarrierAccountAttributes")
  private CarrierAccountAttributes encryptedCarrierAccountAttributes = null;

  public LinkCarrierAccountRequest clientReferenceDetails(ClientReferenceDetails clientReferenceDetails) {
    this.clientReferenceDetails = clientReferenceDetails;
    return this;
  }

   /**
   * Get clientReferenceDetails
   * @return clientReferenceDetails
  **/
  @ApiModelProperty(value = "")
  public ClientReferenceDetails getClientReferenceDetails() {
    return clientReferenceDetails;
  }

  public void setClientReferenceDetails(ClientReferenceDetails clientReferenceDetails) {
    this.clientReferenceDetails = clientReferenceDetails;
  }

  public LinkCarrierAccountRequest carrierAccountType(String carrierAccountType) {
    this.carrierAccountType = carrierAccountType;
    return this;
  }

   /**
   * Get carrierAccountType
   * @return carrierAccountType
  **/
  @ApiModelProperty(required = true, value = "")
  public String getCarrierAccountType() {
    return carrierAccountType;
  }

  public void setCarrierAccountType(String carrierAccountType) {
    this.carrierAccountType = carrierAccountType;
  }

  public LinkCarrierAccountRequest carrierAccountAttributes(CarrierAccountAttributes carrierAccountAttributes) {
    this.carrierAccountAttributes = carrierAccountAttributes;
    return this;
  }

   /**
   * Get carrierAccountAttributes
   * @return carrierAccountAttributes
  **/
  @ApiModelProperty(required = true, value = "")
  public CarrierAccountAttributes getCarrierAccountAttributes() {
    return carrierAccountAttributes;
  }

  public void setCarrierAccountAttributes(CarrierAccountAttributes carrierAccountAttributes) {
    this.carrierAccountAttributes = carrierAccountAttributes;
  }

  public LinkCarrierAccountRequest encryptedCarrierAccountAttributes(CarrierAccountAttributes encryptedCarrierAccountAttributes) {
    this.encryptedCarrierAccountAttributes = encryptedCarrierAccountAttributes;
    return this;
  }

   /**
   * Get encryptedCarrierAccountAttributes
   * @return encryptedCarrierAccountAttributes
  **/
  @ApiModelProperty(value = "")
  public CarrierAccountAttributes getEncryptedCarrierAccountAttributes() {
    return encryptedCarrierAccountAttributes;
  }

  public void setEncryptedCarrierAccountAttributes(CarrierAccountAttributes encryptedCarrierAccountAttributes) {
    this.encryptedCarrierAccountAttributes = encryptedCarrierAccountAttributes;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LinkCarrierAccountRequest linkCarrierAccountRequest = (LinkCarrierAccountRequest) o;
    return Objects.equals(this.clientReferenceDetails, linkCarrierAccountRequest.clientReferenceDetails) &&
        Objects.equals(this.carrierAccountType, linkCarrierAccountRequest.carrierAccountType) &&
        Objects.equals(this.carrierAccountAttributes, linkCarrierAccountRequest.carrierAccountAttributes) &&
        Objects.equals(this.encryptedCarrierAccountAttributes, linkCarrierAccountRequest.encryptedCarrierAccountAttributes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clientReferenceDetails, carrierAccountType, carrierAccountAttributes, encryptedCarrierAccountAttributes);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LinkCarrierAccountRequest {\n");
    
    sb.append("    clientReferenceDetails: ").append(toIndentedString(clientReferenceDetails)).append("\n");
    sb.append("    carrierAccountType: ").append(toIndentedString(carrierAccountType)).append("\n");
    sb.append("    carrierAccountAttributes: ").append(toIndentedString(carrierAccountAttributes)).append("\n");
    sb.append("    encryptedCarrierAccountAttributes: ").append(toIndentedString(encryptedCarrierAccountAttributes)).append("\n");
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

