package com.wimoor.amazon.inboundV2.pojo.dto;

import com.amazon.spapi.model.fulfillmentinboundV20240320.*;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class ShipmentDTO {
    private String amazonReferenceId ;
    private ContactInformation contactInformation ;
    private ShipmentDestination destination ;
    private FreightInformation freightInformation ;
    private String name ;
    private String placementOptionId ;
    private String selectedTransportationOptionId ;
    private String shipmentConfirmationId ;
    private String shipmentId;
    private ShipmentSource source ;
    private String status ;
    private TrackingDetails trackingDetails ;
    private String companyid ;
    private String channelid;
    private String transtype;
}
