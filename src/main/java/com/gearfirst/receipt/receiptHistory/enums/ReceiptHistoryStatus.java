package com.gearfirst.receipt.receiptHistory.enums;

import lombok.Getter;

@Getter
public enum ReceiptHistoryStatus {
    RECEIPT("접수"),
    REPAIRING("수리중"),
    FINISH("완료");

    private final String status;

    ReceiptHistoryStatus(String status) {
        this.status = status;
    }
}
