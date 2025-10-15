package com.gearfirst.receipt.receiptHistory.repository;

import com.gearfirst.receipt.receiptHistory.entity.ReceiptSequence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReceiptSequenceRepository extends JpaRepository<ReceiptSequence, Long> {
    Optional<ReceiptSequence> findByBranchCodeAndDatePart(String branchCode, String datePart);
}
