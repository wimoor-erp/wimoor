package com.wimoor.sys.tool.pojo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wimoor.sys.tool.pojo.entity.DeepSeekMessage;
import com.wimoor.sys.tool.pojo.entity.ResponseFormat;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("ds接收参数对象")
public class SysChartCompletionRequestDTO implements Serializable {

    private List<DeepSeekMessage> messages;

    private String sessionId;

    private String model;

    private Double frequencyPenalty;

    private Integer maxTokens;

    private Double presencePenalty;

    private ResponseFormat responseFormat;

    private Object stop;

    private Boolean stream;

    private Object streamOptions;

    private Double temperature;

    private Double topP;

    private Object tools;

    private String toolChoice;

    private Boolean logprobs;

    private Object topLogprobs;
}
