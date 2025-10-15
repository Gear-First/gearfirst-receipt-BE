package com.gearfirst.receipt.usedPart.entity;

import com.gearfirst.receipt.common.entity.BaseTimeEntity;
import com.gearfirst.receipt.repairHistory.entity.RepairHistoryEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "used_part")
@Data
@EqualsAndHashCode(callSuper = false)
public class UsedPartEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long partId;

    private String partName;

    private int quantity;

    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repair_history_id")
    private RepairHistoryEntity repairHistoryEntity;
}
