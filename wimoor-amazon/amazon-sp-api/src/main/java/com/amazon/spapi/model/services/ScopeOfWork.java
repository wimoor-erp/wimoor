/*
 * Selling Partner API for Services
 * With the Services API, you can build applications that help service providers get and modify their service orders and manage their resources.
 *
 * OpenAPI spec version: v1
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.amazon.spapi.model.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.gson.annotations.SerializedName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * The scope of work for the order.
 */
@ApiModel(description = "The scope of work for the order.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2024-01-30T15:25:55.891+08:00")
public class ScopeOfWork {
  @SerializedName("asin")
  private String asin = null;

  @SerializedName("title")
  private String title = null;

  @SerializedName("quantity")
  private Integer quantity = null;

  @SerializedName("requiredSkills")
  private List<String> requiredSkills = null;

  public ScopeOfWork asin(String asin) {
    this.asin = asin;
    return this;
  }

   /**
   * The Amazon Standard Identification Number (ASIN) of the service job.
   * @return asin
  **/
  @ApiModelProperty(value = "The Amazon Standard Identification Number (ASIN) of the service job.")
  public String getAsin() {
    return asin;
  }

  public void setAsin(String asin) {
    this.asin = asin;
  }

  public ScopeOfWork title(String title) {
    this.title = title;
    return this;
  }

   /**
   * The title of the service job.
   * @return title
  **/
  @ApiModelProperty(value = "The title of the service job.")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public ScopeOfWork quantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

   /**
   * The number of service jobs.
   * @return quantity
  **/
  @ApiModelProperty(value = "The number of service jobs.")
  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public ScopeOfWork requiredSkills(List<String> requiredSkills) {
    this.requiredSkills = requiredSkills;
    return this;
  }

  public ScopeOfWork addRequiredSkillsItem(String requiredSkillsItem) {
    if (this.requiredSkills == null) {
      this.requiredSkills = new ArrayList<String>();
    }
    this.requiredSkills.add(requiredSkillsItem);
    return this;
  }

   /**
   * A list of skills required to perform the job.
   * @return requiredSkills
  **/
  @ApiModelProperty(value = "A list of skills required to perform the job.")
  public List<String> getRequiredSkills() {
    return requiredSkills;
  }

  public void setRequiredSkills(List<String> requiredSkills) {
    this.requiredSkills = requiredSkills;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ScopeOfWork scopeOfWork = (ScopeOfWork) o;
    return Objects.equals(this.asin, scopeOfWork.asin) &&
        Objects.equals(this.title, scopeOfWork.title) &&
        Objects.equals(this.quantity, scopeOfWork.quantity) &&
        Objects.equals(this.requiredSkills, scopeOfWork.requiredSkills);
  }

  @Override
  public int hashCode() {
    return Objects.hash(asin, title, quantity, requiredSkills);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ScopeOfWork {\n");
    
    sb.append("    asin: ").append(toIndentedString(asin)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    requiredSkills: ").append(toIndentedString(requiredSkills)).append("\n");
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

