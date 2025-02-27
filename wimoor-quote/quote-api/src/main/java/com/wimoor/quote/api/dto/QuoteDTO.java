package com.wimoor.quote.api.dto;


import com.wimoor.common.pojo.entity.BasePageQuery;
import com.wimoor.quote.api.entity.Shipment;
import com.wimoor.quote.api.entity.ShipmentDestinationAddress;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Data
public class QuoteDTO extends BasePageQuery {


  private String token;

  private String search;

  private String number;

  private String displayType;

  String fromDate;

  String toDate;

  private Integer status;//1.等待确认,2等待拼团,3等待报价,4 结束

  private BigDecimal baseWeight;//材积基数

  private Date baseline;//拼团结束时间

  private List<Shipment> shipments;

  private List<ShipmentDestinationAddress> address;

  private String buyerid;
}
