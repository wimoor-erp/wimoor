package com.wimoor.amazon.adv.sp.pojo;


import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wimoor.amazon.adv.common.pojo.JsonBigIntergeSerializer;

import lombok.Data;

@Data
@Entity
@Table(name="t_amz_adv_bidding_rules")
public class AmzAdvBiddingRules {
	@Id
	@Column(name="optimization_rule_id")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private String optimizationRuleId;
	
	@Column(name="profileid")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger profileid;
	
	@Column(name="rule_name")
    private String ruleName;
	
	@Column(name="status")
    private String status;
	
	@Column(name="created_date")
    private Date createdDate;

	@Column(name="rule_sub_category")
    private String ruleSubCategory;

	@Column(name="recurrence")
    private String recurrence;
	
	@Column(name="rule_type")
    private String ruleType;
	
	@Column(name="rule_category")
    private String ruleCategory;
	
	@Column(name="action")
    private String action;

	@Column(name="conditions")
    private String conditions;
}
