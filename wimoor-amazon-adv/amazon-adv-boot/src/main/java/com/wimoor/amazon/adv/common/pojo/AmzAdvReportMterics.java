package com.wimoor.amazon.adv.common.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="t_amz_adv_report_metrics")
public class AmzAdvReportMterics {
	@Id
	@Column(name="id")
    private Integer id;

	@Column(name="campaigntype")
    private String campaigntype;

	@Column(name="reporttype")
    private String reporttype;

	@Column(name="segment")
    private String segment;

	@Column(name="activetype")
    private String activetype;

	@Column(name="metrics")
    private String metrics;

	@Column(name="level")
    private Integer level;

	@Column(name="reponsetype")
    private String reponsetype;

	@Column(name="nomarket")
    private String nomarket;

	@Column(name="disablevendor")
    private Boolean disablevendor;
 
}