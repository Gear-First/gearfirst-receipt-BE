package com.gearfirst.receipt.receiptHistory.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "receipt_sequence")
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReceiptSequence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String branchCode;
    private String datePart;
    private int sequence;

    @Version
    private Long version;
}
