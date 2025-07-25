package com.wimoor.erp.common.pojo.entity;

import lombok.Data;

import java.util.List;
@Data
public class Chart {
List<String> legends;
List<String> labels;
List<ChartLine> lines;
}
