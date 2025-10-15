package com.gearfirst.receipt.receiptHistory.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UsedPartInRepair {
    private Long partId;
    private String partName;
    private int quantity;
    private int price;
}
