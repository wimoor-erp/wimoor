/*
 * Selling Partner API for Merchant Fulfillment
 * The Selling Partner API for Merchant Fulfillment helps you build applications that let sellers purchase shipping for non-Prime and Prime orders using Amazon’s Buy Shipping Services.
 *
 * OpenAPI spec version: v0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.amazon.spapi.model.merchantfulfillment;

import java.util.Objects;

import com.google.gson.annotations.SerializedName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * A validation constraint.
 */
@ApiModel(description = "A validation constraint.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2024-01-30T16:09:43.032+08:00")
public class Constraint {
  @SerializedName("ValidationRegEx")
  private String validationRegEx = null;

  @SerializedName("ValidationString")
  private String validationString = null;

  public Constraint validationRegEx(String validationRegEx) {
    this.validationRegEx = validationRegEx;
    return this;
  }

   /**
   * A regular expression.
   * @return validationRegEx
  **/
  @ApiModelProperty(value = "A regular expression.")
  public String getValidationRegEx() {
    return validationRegEx;
  }

  public void setValidationRegEx(String validationRegEx) {
    this.validationRegEx = validationRegEx;
  }

  public Constraint validationString(String validationString) {
    this.validationString = validationString;
    return this;
  }

   /**
   * A validation string.
   * @return validationString
  **/
  @ApiModelProperty(required = true, value = "A validation string.")
  public String getValidationString() {
    return validationString;
  }

  public void setValidationString(String validationString) {
    this.validationString = validationString;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Constraint constraint = (Constraint) o;
    return Objects.equals(this.validationRegEx, constraint.validationRegEx) &&
        Objects.equals(this.validationString, constraint.validationString);
  }

  @Override
  public int hashCode() {
    return Objects.hash(validationRegEx, validationString);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Constraint {\n");
    
    sb.append("    validationRegEx: ").append(toIndentedString(validationRegEx)).append("\n");
    sb.append("    validationString: ").append(toIndentedString(validationString)).append("\n");
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

