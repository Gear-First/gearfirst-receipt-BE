package com.gearfirst.receipt.receiptHistory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptHistoryResponse {
    private String receiptHistoryId;
    private String receipterName;
    private String receipterCarNum;
    private String receipterCarType;
    private String receipterPhone;
    private String receipterRequest;
    private String engineer;
    private String status;
    private List<RepairHistoryResponse> repairHistories;
}
