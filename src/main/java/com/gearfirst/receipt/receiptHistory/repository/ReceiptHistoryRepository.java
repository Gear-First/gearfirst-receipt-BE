package com.gearfirst.receipt.receiptHistory.repository;

import com.gearfirst.receipt.receiptHistory.entity.ReceiptHistoryEntity;
import com.gearfirst.receipt.receiptHistory.enums.ReceiptHistoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReceiptHistoryRepository extends JpaRepository<ReceiptHistoryEntity, String> {
    List<ReceiptHistoryEntity> findByStatus(ReceiptHistoryStatus status);
    ReceiptHistoryEntity findByReceiptHistoryId(String receiptHistoryId);
}
