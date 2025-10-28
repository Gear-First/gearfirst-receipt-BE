package com.gearfirst.receipt.receiptHistory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ReceiptInfoResponse {
    private String carNum;
    private String carType;
}
