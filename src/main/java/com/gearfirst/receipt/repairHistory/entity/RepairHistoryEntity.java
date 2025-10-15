package com.gearfirst.receipt.repairHistory.entity;

import com.gearfirst.receipt.common.entity.BaseTimeEntity;
import com.gearfirst.receipt.receiptHistory.entity.ReceiptHistoryEntity;
import com.gearfirst.receipt.usedPart.entity.UsedPartEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "repair_history")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepairHistoryEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long repairHistoryId;

    private String repairDetail;

    private String repairCause;

    // RepairReceipt 와의 N:1 관계
    // @JoinColumn: 외래 키(FK)를 생성
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receipt_history_id")
    private ReceiptHistoryEntity receiptHistoryEntity;

    // UsedPart 와의 1:N 관계
    @OneToMany(mappedBy = "repairHistoryEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsedPartEntity> usedParts = new ArrayList<>();
}
