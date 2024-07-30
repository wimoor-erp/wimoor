package com.wimoor.amazon.adv.sp.pojo;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wimoor.amazon.adv.common.pojo.JsonBigIntergeSerializer;

import lombok.Data;


@Data
@Entity
@Table(name="t_amz_adv_campaign_budget_rule")
public class AmzAdvCampaignBudgetRule {
	
	@Id
	@Column(name="rule_id")
    private String ruleId;
	
	@Id
	@Column(name="campaignid")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger campaignid;
	
	@Column(name="profileid")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger profileid;

}
