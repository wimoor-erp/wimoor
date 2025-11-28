package com.wimoor.quote.api.dto;


import lombok.Data;

import java.util.List;


@Data
public class QuotePriceDTO  {

  private List<String> orderids;

  private List<String> supplierid;

}
