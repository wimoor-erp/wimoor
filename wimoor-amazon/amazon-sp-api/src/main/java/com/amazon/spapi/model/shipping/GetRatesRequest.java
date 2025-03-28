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
import com.amazon.spapi.model.shipping.AccessPointDetails;
import com.amazon.spapi.model.shipping.Address;
import com.amazon.spapi.model.shipping.ChannelDetails;
import com.amazon.spapi.model.shipping.ClientReferenceDetails;
import com.amazon.spapi.model.shipping.PackageList;
import com.amazon.spapi.model.shipping.ShipmentType;
import com.amazon.spapi.model.shipping.ShipperInstruction;
import com.amazon.spapi.model.shipping.TaxDetailList;
import com.amazon.spapi.model.shipping.ValueAddedServiceDetails;
import java.io.IOException;
import org.threeten.bp.OffsetDateTime;

/**
 * The request schema for the getRates operation. When the channelType is Amazon, the shipTo address is not required and will be ignored.
 */
@ApiModel(description = "The request schema for the getRates operation. When the channelType is Amazon, the shipTo address is not required and will be ignored.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2024-01-30T16:10:03.349+08:00")
public class GetRatesRequest {
  @SerializedName("shipTo")
  private Address shipTo = null;

  @SerializedName("shipFrom")
  private Address shipFrom = null;

  @SerializedName("returnTo")
  private Address returnTo = null;

  @SerializedName("shipDate")
  private OffsetDateTime shipDate = null;

  @SerializedName("shipperInstruction")
  private ShipperInstruction shipperInstruction = null;

  @SerializedName("packages")
  private PackageList packages = null;

  @SerializedName("valueAddedServices")
  private ValueAddedServiceDetails valueAddedServices = null;

  @SerializedName("taxDetails")
  private TaxDetailList taxDetails = null;

  @SerializedName("channelDetails")
  private ChannelDetails channelDetails = null;

  @SerializedName("clientReferenceDetails")
  private ClientReferenceDetails clientReferenceDetails = null;

  @SerializedName("shipmentType")
  private ShipmentType shipmentType = null;

  @SerializedName("destinationAccessPointDetails")
  private AccessPointDetails destinationAccessPointDetails = null;

  public GetRatesRequest shipTo(Address shipTo) {
    this.shipTo = shipTo;
    return this;
  }

   /**
   * The ship to address.
   * @return shipTo
  **/
  @ApiModelProperty(value = "The ship to address.")
  public Address getShipTo() {
    return shipTo;
  }

  public void setShipTo(Address shipTo) {
    this.shipTo = shipTo;
  }

  public GetRatesRequest shipFrom(Address shipFrom) {
    this.shipFrom = shipFrom;
    return this;
  }

   /**
   * The ship from address.
   * @return shipFrom
  **/
  @ApiModelProperty(required = true, value = "The ship from address.")
  public Address getShipFrom() {
    return shipFrom;
  }

  public void setShipFrom(Address shipFrom) {
    this.shipFrom = shipFrom;
  }

  public GetRatesRequest returnTo(Address returnTo) {
    this.returnTo = returnTo;
    return this;
  }

   /**
   * The return to address.
   * @return returnTo
  **/
  @ApiModelProperty(value = "The return to address.")
  public Address getReturnTo() {
    return returnTo;
  }

  public void setReturnTo(Address returnTo) {
    this.returnTo = returnTo;
  }

  public GetRatesRequest shipDate(OffsetDateTime shipDate) {
    this.shipDate = shipDate;
    return this;
  }

   /**
   * The ship date and time (the requested pickup). This defaults to the current date and time.
   * @return shipDate
  **/
  @ApiModelProperty(value = "The ship date and time (the requested pickup). This defaults to the current date and time.")
  public OffsetDateTime getShipDate() {
    return shipDate;
  }

  public void setShipDate(OffsetDateTime shipDate) {
    this.shipDate = shipDate;
  }

  public GetRatesRequest shipperInstruction(ShipperInstruction shipperInstruction) {
    this.shipperInstruction = shipperInstruction;
    return this;
  }

   /**
   * This field describe shipper instruction.
   * @return shipperInstruction
  **/
  @ApiModelProperty(value = "This field describe shipper instruction.")
  public ShipperInstruction getShipperInstruction() {
    return shipperInstruction;
  }

  public void setShipperInstruction(ShipperInstruction shipperInstruction) {
    this.shipperInstruction = shipperInstruction;
  }

  public GetRatesRequest packages(PackageList packages) {
    this.packages = packages;
    return this;
  }

   /**
   * Get packages
   * @return packages
  **/
  @ApiModelProperty(required = true, value = "")
  public PackageList getPackages() {
    return packages;
  }

  public void setPackages(PackageList packages) {
    this.packages = packages;
  }

  public GetRatesRequest valueAddedServices(ValueAddedServiceDetails valueAddedServices) {
    this.valueAddedServices = valueAddedServices;
    return this;
  }

   /**
   * Get valueAddedServices
   * @return valueAddedServices
  **/
  @ApiModelProperty(value = "")
  public ValueAddedServiceDetails getValueAddedServices() {
    return valueAddedServices;
  }

  public void setValueAddedServices(ValueAddedServiceDetails valueAddedServices) {
    this.valueAddedServices = valueAddedServices;
  }

  public GetRatesRequest taxDetails(TaxDetailList taxDetails) {
    this.taxDetails = taxDetails;
    return this;
  }

   /**
   * Get taxDetails
   * @return taxDetails
  **/
  @ApiModelProperty(value = "")
  public TaxDetailList getTaxDetails() {
    return taxDetails;
  }

  public void setTaxDetails(TaxDetailList taxDetails) {
    this.taxDetails = taxDetails;
  }

  public GetRatesRequest channelDetails(ChannelDetails channelDetails) {
    this.channelDetails = channelDetails;
    return this;
  }

   /**
   * Get channelDetails
   * @return channelDetails
  **/
  @ApiModelProperty(required = true, value = "")
  public ChannelDetails getChannelDetails() {
    return channelDetails;
  }

  public void setChannelDetails(ChannelDetails channelDetails) {
    this.channelDetails = channelDetails;
  }

  public GetRatesRequest clientReferenceDetails(ClientReferenceDetails clientReferenceDetails) {
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

  public GetRatesRequest shipmentType(ShipmentType shipmentType) {
    this.shipmentType = shipmentType;
    return this;
  }

   /**
   * Get shipmentType
   * @return shipmentType
  **/
  @ApiModelProperty(value = "")
  public ShipmentType getShipmentType() {
    return shipmentType;
  }

  public void setShipmentType(ShipmentType shipmentType) {
    this.shipmentType = shipmentType;
  }

  public GetRatesRequest destinationAccessPointDetails(AccessPointDetails destinationAccessPointDetails) {
    this.destinationAccessPointDetails = destinationAccessPointDetails;
    return this;
  }

   /**
   * Get destinationAccessPointDetails
   * @return destinationAccessPointDetails
  **/
  @ApiModelProperty(value = "")
  public AccessPointDetails getDestinationAccessPointDetails() {
    return destinationAccessPointDetails;
  }

  public void setDestinationAccessPointDetails(AccessPointDetails destinationAccessPointDetails) {
    this.destinationAccessPointDetails = destinationAccessPointDetails;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetRatesRequest getRatesRequest = (GetRatesRequest) o;
    return Objects.equals(this.shipTo, getRatesRequest.shipTo) &&
        Objects.equals(this.shipFrom, getRatesRequest.shipFrom) &&
        Objects.equals(this.returnTo, getRatesRequest.returnTo) &&
        Objects.equals(this.shipDate, getRatesRequest.shipDate) &&
        Objects.equals(this.shipperInstruction, getRatesRequest.shipperInstruction) &&
        Objects.equals(this.packages, getRatesRequest.packages) &&
        Objects.equals(this.valueAddedServices, getRatesRequest.valueAddedServices) &&
        Objects.equals(this.taxDetails, getRatesRequest.taxDetails) &&
        Objects.equals(this.channelDetails, getRatesRequest.channelDetails) &&
        Objects.equals(this.clientReferenceDetails, getRatesRequest.clientReferenceDetails) &&
        Objects.equals(this.shipmentType, getRatesRequest.shipmentType) &&
        Objects.equals(this.destinationAccessPointDetails, getRatesRequest.destinationAccessPointDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(shipTo, shipFrom, returnTo, shipDate, shipperInstruction, packages, valueAddedServices, taxDetails, channelDetails, clientReferenceDetails, shipmentType, destinationAccessPointDetails);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetRatesRequest {\n");
    
    sb.append("    shipTo: ").append(toIndentedString(shipTo)).append("\n");
    sb.append("    shipFrom: ").append(toIndentedString(shipFrom)).append("\n");
    sb.append("    returnTo: ").append(toIndentedString(returnTo)).append("\n");
    sb.append("    shipDate: ").append(toIndentedString(shipDate)).append("\n");
    sb.append("    shipperInstruction: ").append(toIndentedString(shipperInstruction)).append("\n");
    sb.append("    packages: ").append(toIndentedString(packages)).append("\n");
    sb.append("    valueAddedServices: ").append(toIndentedString(valueAddedServices)).append("\n");
    sb.append("    taxDetails: ").append(toIndentedString(taxDetails)).append("\n");
    sb.append("    channelDetails: ").append(toIndentedString(channelDetails)).append("\n");
    sb.append("    clientReferenceDetails: ").append(toIndentedString(clientReferenceDetails)).append("\n");
    sb.append("    shipmentType: ").append(toIndentedString(shipmentType)).append("\n");
    sb.append("    destinationAccessPointDetails: ").append(toIndentedString(destinationAccessPointDetails)).append("\n");
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

