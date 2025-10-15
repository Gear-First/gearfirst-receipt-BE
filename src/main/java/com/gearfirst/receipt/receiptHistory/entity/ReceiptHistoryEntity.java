package com.gearfirst.receipt.receiptHistory.entity;

import com.gearfirst.receipt.common.entity.BaseTimeEntity;
import com.gearfirst.receipt.receiptHistory.enums.ReceiptHistoryStatus;
import com.gearfirst.receipt.repairHistory.entity.RepairHistoryEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "receiptList")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReceiptHistoryEntity extends BaseTimeEntity {
    @Id
    private String receiptHistoryId;

    private String receipterName;

    private String receipterCarNum;

    private String receipterCarType;

    private String receipterPhone;

    private String receipterRequest;

    private String engineer;

    @Enumerated(EnumType.STRING)
    private ReceiptHistoryStatus status;

    @OneToMany(mappedBy = "receiptHistoryEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RepairHistoryEntity> repairHistories = new ArrayList<>();

}
