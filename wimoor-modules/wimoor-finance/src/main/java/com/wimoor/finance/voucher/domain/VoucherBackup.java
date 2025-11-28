package com.wimoor.finance.voucher.domain;

import com.wimoor.finance.ledger.domain.FinDetailLedger;
import com.wimoor.finance.ledger.domain.FinGeneralLedger;
import com.wimoor.finance.voucher.service.impl.FinVouchersServiceImpl;

import java.util.List;

/**
 * 凭证备份类，用于修改凭证时的备份与恢复
 */
public   class VoucherBackup {
    private FinVouchers originalVoucher;
    private List<FinVoucherEntries> originalEntries;
    private List<FinDetailLedger> originalLedgerEntries;
    private List<FinGeneralLedger> generalLedger;


    // Getters and Setters
    public FinVouchers getOriginalVoucher() {
        return originalVoucher;
    }
    public void setOriginalVoucher(FinVouchers originalVoucher) {
        this.originalVoucher = originalVoucher;
    }
    public List<FinVoucherEntries> getOriginalEntries() {
        return originalEntries;
    }
    public void setOriginalEntries(List<FinVoucherEntries> originalEntries) {
        this.originalEntries = originalEntries;
    }
    public List<FinDetailLedger> getOriginalLedgerEntries() {
        return originalLedgerEntries;
    }
    public void setOriginalLedgerEntries(List<FinDetailLedger> originalLedgerEntries) {
        this.originalLedgerEntries = originalLedgerEntries;
    }

    public List<FinGeneralLedger> getGeneralLedger() {
        return generalLedger;
    }

    public void setGeneralLedger(List<FinGeneralLedger> generalLedger) {
        this.generalLedger = generalLedger;
    }
}