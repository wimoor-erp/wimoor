package com.wimoor.amazon.common.pojo.vo;

import java.util.List;

import lombok.Data;
@Data
public class Chart {
List<String> legends;
List<String> labels;
List<ChartLine> lines;
}
