package com.gearfirst.receipt.receiptHistory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepairHistoryResponse {
    private String repairDetail;
    private String repairCause;
    private List<UsedPartResponse> usedParts;
}
