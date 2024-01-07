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
@Table(name="t_amz_adv_budget_rules")
public class AmzAdvBudgetRules {
	@Id
	@Column(name="rule_id")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private String ruleId;
	
	@Column(name="profileid")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger profileid;
	
	@Column(name="name")
    private String name;
	
	@Column(name="rule_state")
    private String ruleState;
	
	@Column(name="last_updated_date")
    private Date lastUpdatedDate;
	
	@Column(name="created_date")
    private Date createdDate;

	@Column(name="duration")
    private String duration;

	@Column(name="recurrence")
    private String recurrence;
	
	@Column(name="rule_type")
    private String ruleType;
	
	@Column(name="budget_increase_by")
    private String budgetIncreaseBy;
	
	@Column(name="performance_measure_condition")
    private String performanceMeasureCondition;
	
	@Column(name="rule_status")
    private String ruleStatus;
}
