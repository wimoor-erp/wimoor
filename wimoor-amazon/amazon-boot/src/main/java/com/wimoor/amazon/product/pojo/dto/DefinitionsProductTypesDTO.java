package com.wimoor.amazon.product.pojo.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@ApiModel(value="DefinitionsProductTypesDTO", description="获取商品Type列表")
public class DefinitionsProductTypesDTO {
    private String groupid;
    private String marketplaceid;
    private List<String> keywords;
    private String itemName;
    private String locale;
    private String searchLocale;

}
