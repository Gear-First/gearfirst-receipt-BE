package com.gearfirst.receipt.receiptHistory.repository;

import com.gearfirst.receipt.receiptHistory.entity.ReceiptHistoryEntity;
import com.gearfirst.receipt.receiptHistory.enums.ReceiptHistoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReceiptHistoryRepository extends JpaRepository<ReceiptHistoryEntity, String> {
    List<ReceiptHistoryEntity> findByStatus(ReceiptHistoryStatus status);
    ReceiptHistoryEntity findByReceiptHistoryId(String receiptHistoryId);

    @Query("""
        SELECT r FROM ReceiptHistoryEntity r
        WHERE r.createdAt >= :startDate AND r.createdAt < :endDate
          AND r.engineer = :engineer
          AND (r.receipterName LIKE %:keyword% OR r.receipterCarNum LIKE %:keyword%)
    """)
//    ("""
//        SELECT r FROM ReceiptHistoryEntity r
//        WHERE r.createdAt BETWEEN :startDate AND :endDate
//          AND r.engineer = :engineer
//          AND (r.receipterName LIKE %:keyword% OR r.receipterCarNum LIKE %:keyword%)
//    """)
    List<ReceiptHistoryEntity> findByDateAndEngineerAndKeyword(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("engineer") String engineer,
            @Param("keyword") String keyword
    );

    List<ReceiptHistoryEntity> findByEngineer(String engineer);
}
