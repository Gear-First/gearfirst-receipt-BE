package com.gearfirst.receipt.receiptHistory.service;

import com.gearfirst.receipt.receiptHistory.dto.ReceiptHistoryRequest;
import com.gearfirst.receipt.receiptHistory.dto.ReceiptHistoryResponse;
import com.gearfirst.receipt.receiptHistory.dto.RepairHistoryResponse;
import com.gearfirst.receipt.receiptHistory.dto.UsedPartResponse;
import com.gearfirst.receipt.receiptHistory.entity.ReceiptHistoryEntity;
import com.gearfirst.receipt.receiptHistory.entity.ReceiptSequence;
import com.gearfirst.receipt.receiptHistory.enums.ReceiptHistoryStatus;
import com.gearfirst.receipt.receiptHistory.repository.ReceiptHistoryRepository;
import com.gearfirst.receipt.receiptHistory.repository.ReceiptSequenceRepository;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReceiptHistoryService {
    private final ReceiptHistoryRepository receiptHistoryRepository;
    private final ReceiptSequenceRepository sequenceRepository;

    public List<ReceiptHistoryResponse> getUnProcessedReceipts() {
        List<ReceiptHistoryEntity> entities = receiptHistoryRepository.findByStatus(ReceiptHistoryStatus.RECEIPT);

        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    private ReceiptHistoryResponse toDto(ReceiptHistoryEntity entity) {
        ReceiptHistoryResponse dto = new ReceiptHistoryResponse();
        dto.setReceiptHistoryId(entity.getReceiptHistoryId());
        dto.setReceipterName(entity.getReceipterName());
        dto.setReceipterCarNum(entity.getReceipterCarNum());
        dto.setReceipterCarType(entity.getReceipterCarType());
        dto.setReceipterPhone(entity.getReceipterPhone());
        dto.setReceipterRequest(entity.getReceipterRequest());
        dto.setEngineer(entity.getEngineer());
        dto.setStatus(entity.getStatus().name());

        // repairHistories 변환
        List<RepairHistoryResponse> repairDtos = entity.getRepairHistories().stream()
                .map(r -> {
                    RepairHistoryResponse rDto = new RepairHistoryResponse();
                    rDto.setRepairDetail(r.getRepairDetail());
                    rDto.setRepairCause(r.getRepairCause());
                    // UsedParts 변환
                    List<UsedPartResponse> usedPartsDtos = r.getUsedParts().stream()
                            .map(p -> new UsedPartResponse(p.getPartName(), p.getQuantity(), p.getPrice()))
                            .collect(Collectors.toList());
                    rDto.setUsedParts(usedPartsDtos);
                    return rDto;
                }).collect(Collectors.toList());

        dto.setRepairHistories(repairDtos);

        return dto;
    }

    public ReceiptHistoryEntity createReceipt(ReceiptHistoryRequest dto, String branchCode) {
        String newReceiptId = generateWithRetry(branchCode);

        ReceiptHistoryEntity entity = ReceiptHistoryEntity.builder()
                .receiptHistoryId(newReceiptId)
                .receipterName(dto.getReceipterName())
                .receipterCarNum(dto.getReceipterCarNum())
                .receipterCarType(dto.getReceipterCarType())
                .receipterPhone(dto.getReceipterPhone())
                .receipterRequest(dto.getReceipterRequest())
                .engineer(null)
                .status(ReceiptHistoryStatus.RECEIPT)
                .build();

        return receiptHistoryRepository.save(entity);
    }

    public String generateWithRetry(String branchCode) {
        int maxRetry = 3;
        for (int i = 0; i < maxRetry; i++) {
            try {
                return generateReceiptId(branchCode);
            } catch (OptimisticLockException e) {
                try { Thread.sleep(50); } catch (InterruptedException ignored) {}
            }
        }
        throw new RuntimeException("동시 처리 충돌로 ID 생성 실패");
    }

    @Transactional
    public String generateReceiptId(String branchCode) {
        String datePart = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);

        ReceiptSequence sequence = sequenceRepository
                .findByBranchCodeAndDatePart(branchCode, datePart)
                .orElseGet(() -> sequenceRepository.save(
                        ReceiptSequence.builder()
                                .branchCode(branchCode)
                                .datePart(datePart)
                                .sequence(0)
                                .build()
                ));

        // 낙관적 락은 여기서 동작: version 확인 후 +1
        sequence.setSequence(sequence.getSequence() + 1);

        // 버전 충돌 시 OptimisticLockException 발생
        sequenceRepository.save(sequence);

        String formattedSeq = String.format("%03d", sequence.getSequence());
        return String.format("%s-%s-%s", branchCode, datePart, formattedSeq);
    }
}
