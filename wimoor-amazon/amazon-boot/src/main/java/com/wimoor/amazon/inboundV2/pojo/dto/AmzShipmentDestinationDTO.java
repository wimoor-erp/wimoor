package com.wimoor.amazon.inboundV2.pojo.dto;

import com.amazon.spapi.model.fulfillmentinboundV20240320.Address;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class AmzShipmentDestinationDTO {
        @SerializedName("address")
        private AmzAddressDTO address = null;

        @SerializedName("destinationType")
        private String destinationType = null;

        @SerializedName("warehouseId")
        private String warehouseId = null;
}
