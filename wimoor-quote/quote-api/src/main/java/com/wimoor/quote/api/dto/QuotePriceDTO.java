package com.wimoor.quote.api.dto;


import com.wimoor.common.pojo.entity.BasePageQuery;
import com.wimoor.quote.api.entity.Shipment;
import com.wimoor.quote.api.entity.ShipmentDestinationAddress;
import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class QuotePriceDTO  {

  private List<String> orderids;

  private List<String> supplierid;

}
