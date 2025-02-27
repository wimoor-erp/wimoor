package com.wimoor.amazon.adv.report.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="t_amz_adv_report_request_type")
public class AmzAdvReportRequestType {
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

	@Column(name="bean")
    private String bean;

	@Column(name="reponsetype")
    private String reponsetype;

	@Column(name="nomarket")
    private String nomarket;

	@Column(name="disablevendor")
    private Boolean disablevendor;
 
}