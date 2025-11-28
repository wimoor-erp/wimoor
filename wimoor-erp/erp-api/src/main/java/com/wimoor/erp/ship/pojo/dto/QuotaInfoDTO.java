package com.wimoor.erp.ship.pojo.dto;

import java.util.List;

import com.wimoor.erp.material.pojo.vo.MaterialVO;

import lombok.Data;

@Data
public class QuotaInfoDTO {
 List<MaterialVO> list;
 String warehouseid;
 String formid;
 String type;
}
