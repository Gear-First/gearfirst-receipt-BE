package com.gearfirst.receipt.receiptHistory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsedPartResponse {
    private String partName;
    private int quantity;
    private int price;
}
