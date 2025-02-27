package com.wimoor.erp.ship.pojo.dto;

import java.util.List;

import lombok.Data;

@Data
public class ShipFormDTO {
String formid;
String warehouseid;
String number;
String opttype;
List<ShipItemDTO> list;
}
