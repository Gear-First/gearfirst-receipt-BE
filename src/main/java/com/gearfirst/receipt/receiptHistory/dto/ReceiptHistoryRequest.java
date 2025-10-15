package com.gearfirst.receipt.receiptHistory.dto;

import lombok.Data;

@Data
public class ReceiptHistoryRequest {
    private String receipterName;
    private String receipterCarNum;
    private String receipterCarType;
    private String receipterPhone;
    private String receipterRequest;
}
