package com.wimoor.amazon.inboundV2.pojo.dto;

import com.amazon.spapi.model.fulfillmentinboundV20240320.*;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
@Data
public class AmzShipmentDTO {

        @SerializedName("amazonReferenceId")
        private String amazonReferenceId = null;

        @SerializedName("contactInformation")
        private ContactInformation contactInformation = null;

        @SerializedName("dates")
        private Dates dates = null;

        @SerializedName("destination")
        private AmzShipmentDestinationDTO destination = null;

        @SerializedName("freightInformation")
        private FreightInformation freightInformation = null;

        @SerializedName("name")
        private String name = null;

        @SerializedName("placementOptionId")
        private String placementOptionId = null;

        @SerializedName("selectedDeliveryWindow")
        private SelectedDeliveryWindow selectedDeliveryWindow = null;

        @SerializedName("selectedTransportationOptionId")
        private String selectedTransportationOptionId = null;

        @SerializedName("selfShipAppointmentDetails")
        private List<SelfShipAppointmentDetails> selfShipAppointmentDetails = null;

        @SerializedName("shipmentConfirmationId")
        private String shipmentConfirmationId = null;

        @SerializedName("shipmentId")
        private String shipmentId = null;

        @SerializedName("source")
        private ShipmentSource source = null;

        @SerializedName("status")
        private String status = null;

        @SerializedName("trackingDetails")
        private TrackingDetails trackingDetails = null;

        Map<String,Object> transinfo;


    }


