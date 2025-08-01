/*
 * The Selling Partner API for CustomerFeedback
 * The Selling Partner API for Customer Feedback (Customer Feedback API) provides information about customer reviews and returns at both the item and browse node level.
 *
 * OpenAPI spec version: 2024-06-01
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package com.amazon.spapi.model.customerfeedback;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.IOException;
/**
 * The effects of a topic on a browse node&#x27;s star rating.
 */
@Schema(description = "The effects of a topic on a browse node's star rating.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2025-07-08T10:30:47.588079500+08:00[Asia/Shanghai]")

public class BrowseNodeStarRatingImpact {
  @SerializedName("allProducts")
  private Float allProducts = null;

  public BrowseNodeStarRatingImpact allProducts(Float allProducts) {
    this.allProducts = allProducts;
    return this;
  }

   /**
   * The effect of the topic on the star rating for all products in this browse node. This value can be positive or negative.
   * @return allProducts
  **/
  @Schema(required = true, description = "The effect of the topic on the star rating for all products in this browse node. This value can be positive or negative.")
  public Float getAllProducts() {
    return allProducts;
  }

  public void setAllProducts(Float allProducts) {
    this.allProducts = allProducts;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BrowseNodeStarRatingImpact browseNodeStarRatingImpact = (BrowseNodeStarRatingImpact) o;
    return Objects.equals(this.allProducts, browseNodeStarRatingImpact.allProducts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(allProducts);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BrowseNodeStarRatingImpact {\n");
    
    sb.append("    allProducts: ").append(toIndentedString(allProducts)).append("\n");
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
