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
 * The review metrics for a certain month.
 */
@Schema(description = "The review metrics for a certain month.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2025-07-08T10:30:47.588079500+08:00[Asia/Shanghai]")

public class ItemReviewTrendPoint {
  @SerializedName("dateRange")
  private DateRange dateRange = null;

  @SerializedName("asinMetrics")
  private ReviewTrendMetrics asinMetrics = null;

  @SerializedName("parentAsinMetrics")
  private ReviewTrendMetrics parentAsinMetrics = null;

  @SerializedName("browseNodeMetrics")
  private BrowseNodeTrendMetrics browseNodeMetrics = null;

  public ItemReviewTrendPoint dateRange(DateRange dateRange) {
    this.dateRange = dateRange;
    return this;
  }

   /**
   * Get dateRange
   * @return dateRange
  **/
  @Schema(required = true, description = "")
  public DateRange getDateRange() {
    return dateRange;
  }

  public void setDateRange(DateRange dateRange) {
    this.dateRange = dateRange;
  }

  public ItemReviewTrendPoint asinMetrics(ReviewTrendMetrics asinMetrics) {
    this.asinMetrics = asinMetrics;
    return this;
  }

   /**
   * Get asinMetrics
   * @return asinMetrics
  **/
  @Schema(required = true, description = "")
  public ReviewTrendMetrics getAsinMetrics() {
    return asinMetrics;
  }

  public void setAsinMetrics(ReviewTrendMetrics asinMetrics) {
    this.asinMetrics = asinMetrics;
  }

  public ItemReviewTrendPoint parentAsinMetrics(ReviewTrendMetrics parentAsinMetrics) {
    this.parentAsinMetrics = parentAsinMetrics;
    return this;
  }

   /**
   * Get parentAsinMetrics
   * @return parentAsinMetrics
  **/
  @Schema(description = "")
  public ReviewTrendMetrics getParentAsinMetrics() {
    return parentAsinMetrics;
  }

  public void setParentAsinMetrics(ReviewTrendMetrics parentAsinMetrics) {
    this.parentAsinMetrics = parentAsinMetrics;
  }

  public ItemReviewTrendPoint browseNodeMetrics(BrowseNodeTrendMetrics browseNodeMetrics) {
    this.browseNodeMetrics = browseNodeMetrics;
    return this;
  }

   /**
   * Get browseNodeMetrics
   * @return browseNodeMetrics
  **/
  @Schema(description = "")
  public BrowseNodeTrendMetrics getBrowseNodeMetrics() {
    return browseNodeMetrics;
  }

  public void setBrowseNodeMetrics(BrowseNodeTrendMetrics browseNodeMetrics) {
    this.browseNodeMetrics = browseNodeMetrics;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ItemReviewTrendPoint itemReviewTrendPoint = (ItemReviewTrendPoint) o;
    return Objects.equals(this.dateRange, itemReviewTrendPoint.dateRange) &&
        Objects.equals(this.asinMetrics, itemReviewTrendPoint.asinMetrics) &&
        Objects.equals(this.parentAsinMetrics, itemReviewTrendPoint.parentAsinMetrics) &&
        Objects.equals(this.browseNodeMetrics, itemReviewTrendPoint.browseNodeMetrics);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dateRange, asinMetrics, parentAsinMetrics, browseNodeMetrics);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ItemReviewTrendPoint {\n");
    
    sb.append("    dateRange: ").append(toIndentedString(dateRange)).append("\n");
    sb.append("    asinMetrics: ").append(toIndentedString(asinMetrics)).append("\n");
    sb.append("    parentAsinMetrics: ").append(toIndentedString(parentAsinMetrics)).append("\n");
    sb.append("    browseNodeMetrics: ").append(toIndentedString(browseNodeMetrics)).append("\n");
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
