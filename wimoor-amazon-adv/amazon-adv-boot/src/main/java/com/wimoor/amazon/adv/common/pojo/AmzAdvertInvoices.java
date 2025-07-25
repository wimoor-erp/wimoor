package com.wimoor.amazon.adv.common.pojo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigInteger;
import java.util.Date;


@Entity
@Data
@Table(name="t_amz_advert_invoices")
public class AmzAdvertInvoices extends BaseObject{

    @Column(name="profileid")
    private BigInteger profileid;

    @Column(name="invoice_id")
    private String invoiceId;

    @Column(name="status")
    private String status;

    @Column(name="fromDate")
    private String fromDate;

    @Column(name="toDate")
    private String toDate;

    @Column(name="invoiceDate")
    private String invoiceDate;

    @Column(name="amountDue_amount")
    private String amountDueAmount;

    @Column(name="amountDue_code")
    private String amountDueCode;

    @Column(name="taxAmountDue_amount")
    private String taxAmountDueAmount;

    @Column(name="taxAmountDue_code")
    private String taxAmountDueCode;

    @Column(name="remainingAmountDue_amount")
    private String remainingAmountDueAmount;

    @Column(name="remainingAmountDue_code")
    private String remainingAmountDueCode;

    @Column(name="remainingTaxAmountDue_amount")
    private String remainingTaxAmountDueAmount;

    @Column(name="remainingTaxAmountDue_code")
    private String remainingTaxAmountDueCode;

    @Column(name="fees")
    private String fees;

    @Column(name="remainingFees")
    private String remainingFees;

    @Column(name="downloadable_documents")
    private String downloadableDocuments;

    @Column(name="opttime")
    private Date opttime;

    @Column(name="invoiceDay")
    private Date invoiceDay;


}
