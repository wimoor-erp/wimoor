package com.wimoor.amazon.inboundV2.pojo.dto;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Objects;
@Data
public class AmzAddressDTO   {
    @SerializedName("addressLine1")
    private String addressLine1 = null;

    @SerializedName("addressLine2")
    private String addressLine2 = null;

    @SerializedName("city")
    private String city = null;

    @SerializedName("companyName")
    private String companyName = null;

    @SerializedName("countryCode")
    private String countryCode = null;

    @SerializedName("email")
    private String email = null;

    @SerializedName("name")
    private String name = null;

    @SerializedName("phoneNumber")
    private String phoneNumber = null;

    @SerializedName("postalCode")
    private String postalCode = null;

    @SerializedName("stateOrProvinceCode")
    private String stateOrProvinceCode = null;

    private String area;

    private Boolean isfar;


}
