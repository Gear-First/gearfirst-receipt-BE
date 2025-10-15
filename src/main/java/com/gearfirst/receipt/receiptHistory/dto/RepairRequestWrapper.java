package com.gearfirst.receipt.receiptHistory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepairRequestWrapper {
    private String receiptHistoryId;
    private List<RepairDetailRequest> repairDetailRequests;
}
