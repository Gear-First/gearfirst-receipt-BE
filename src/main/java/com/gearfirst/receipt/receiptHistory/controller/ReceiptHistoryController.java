package com.gearfirst.receipt.receiptHistory.controller;

import com.gearfirst.receipt.common.response.ApiResponse;
import com.gearfirst.receipt.common.response.SuccessStatus;
import com.gearfirst.receipt.receiptHistory.dto.ReceiptHistoryResponse;
import com.gearfirst.receipt.receiptHistory.entity.ReceiptHistoryEntity;
import com.gearfirst.receipt.receiptHistory.service.ReceiptHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Get Receipt API", description = "접수 조회 API")
public class ReceiptHistoryController {
    private final ReceiptHistoryService receiptHistoryService;

    @Operation(summary = "미처리 접수 조회", description = "대리점에 요청된 접수 중 처리 되지 않은 접수를 반환합니다.")
    @GetMapping("/getUnprocessedReceipt")
    public ResponseEntity<ApiResponse<List<ReceiptHistoryResponse>>> getUnprocessedReceipts() {
        List<ReceiptHistoryResponse> receipts = receiptHistoryService.getUnProcessedReceipts();
        return ApiResponse
                .success(SuccessStatus.GET_UNPROCESSED_RECEIPT_SUCCESS, receipts);
    }
}
