package com.gearfirst.receipt.receiptHistory.controller;

import com.gearfirst.receipt.common.response.ApiResponse;
import com.gearfirst.receipt.common.response.SuccessStatus;
import com.gearfirst.receipt.receiptHistory.dto.ReceiptHistoryResponse;
import com.gearfirst.receipt.receiptHistory.entity.ReceiptHistoryEntity;
import com.gearfirst.receipt.receiptHistory.service.ReceiptHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReceiptHistoryController {
    private final ReceiptHistoryService receiptHistoryService;

    @GetMapping("/getUnprocessedReceipt")
    public ResponseEntity<ApiResponse<List<ReceiptHistoryResponse>>> getUnprocessedReceipts() {
        List<ReceiptHistoryResponse> receipts = receiptHistoryService.getUnProcessedReceipts();
        return ApiResponse
                .success(SuccessStatus.GET_UNPROCESSED_RECEIPT_SUCCESS, receipts);
    }
}
