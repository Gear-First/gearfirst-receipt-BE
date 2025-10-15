package com.gearfirst.receipt.receiptHistory.service;

import com.gearfirst.receipt.receiptHistory.dto.*;
import com.gearfirst.receipt.receiptHistory.entity.ReceiptHistoryEntity;
import com.gearfirst.receipt.receiptHistory.entity.ReceiptSequence;
import com.gearfirst.receipt.receiptHistory.enums.ReceiptHistoryStatus;
import com.gearfirst.receipt.receiptHistory.repository.ReceiptHistoryRepository;
import com.gearfirst.receipt.receiptHistory.repository.ReceiptSequenceRepository;
import com.gearfirst.receipt.repairHistory.entity.RepairHistoryEntity;
import com.gearfirst.receipt.usedPart.entity.UsedPartEntity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReceiptHistoryService {
    private final ReceiptHistoryRepository receiptHistoryRepository;
    private final ReceiptSequenceRepository sequenceRepository;

    @Transactional
    public void addRepairHistories(String receiptHistoryId, List<RepairDetailRequest> repairDetailRequests) {
        ReceiptHistoryEntity receipt = receiptHistoryRepository.findById(receiptHistoryId)
                .orElseThrow(() -> new EntityNotFoundException("접수 내역을 찾을 수 없습니다: " + receiptHistoryId));


        List<RepairHistoryEntity> newHistories = repairDetailRequests.stream()
                .map(historyDto -> {
                    RepairHistoryEntity newHistory = new RepairHistoryEntity();
                    newHistory.setRepairDetail(historyDto.getRepairDetail());
                    newHistory.setRepairCause(historyDto.getRepairCause());
                    newHistory.setReceiptHistoryEntity(receipt);

                    List<UsedPartEntity> usedParts = historyDto.getUsedParts().stream()
                            .map(partDto -> {
                                if (partDto.getQuantity() < 0 || partDto.getPrice() < 0) {
                                    throw new IllegalArgumentException("부품 수량 또는 가격이 음수일 수 없습니다.");
                                }

                                UsedPartEntity usedPart = new UsedPartEntity();
                                usedPart.setPartId(partDto.getPartId());
                                usedPart.setPartName(partDto.getPartName());
                                usedPart.setQuantity(partDto.getQuantity());
                                usedPart.setPrice(partDto.getPrice());
                                usedPart.setRepairHistoryEntity(newHistory);
                                return usedPart;
                            })
                            .collect(Collectors.toCollection(ArrayList::new));

                    newHistory.setUsedParts(usedParts);
                    return newHistory;
                })
                .collect(Collectors.toCollection(ArrayList::new));

        receipt.getRepairHistories().clear();
        receipt.getRepairHistories().addAll(newHistories);
        receipt.setStatus(ReceiptHistoryStatus.FINISH);
        // 최상위 부모만 저장
        receiptHistoryRepository.save(receipt);
    }

    @Transactional
    public void startRepair(String receiptHistoryId) {
        String engineerName = "티파니 송";

        ReceiptHistoryEntity entity = receiptHistoryRepository.findByReceiptHistoryId(receiptHistoryId);
        if (entity == null) {
            throw new IllegalArgumentException("해당 접수번호(" + receiptHistoryId + ")를 찾을 수 없습니다.");
        }

        // 2️⃣ 이미 수리 중/완료된 상태면 배정 불가
        if (entity.getStatus() != ReceiptHistoryStatus.RECEIPT) {
            throw new IllegalStateException("이미 수리가 시작된 접수입니다. 현재 상태: " + entity.getStatus());
        }

        entity.setEngineer(engineerName);
        entity.setStatus(ReceiptHistoryStatus.REPAIRING);
        receiptHistoryRepository.save(entity);
    }

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
        dto.setStatus(entity.getStatus().getStatus());

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
