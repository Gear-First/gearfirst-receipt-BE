package com.gearfirst.receipt.receiptHistory.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RepairDetailRequest {
    private String repairDetail;
    private String repairCause;
    private List<UsedPartInRepair> usedParts;
}
