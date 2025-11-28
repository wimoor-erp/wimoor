package com.wimoor.finance.closing.domain;

import lombok.Data;

@Data
class ReverseClosingRequest {
    private Long tenantId;
    private String period;
    private String operator;
    private String reason;
}