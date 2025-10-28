package com.gearfirst.receipt.receiptHistory.controller;

import com.gearfirst.receipt.common.response.ApiResponse;
import com.gearfirst.receipt.common.response.SuccessStatus;
import com.gearfirst.receipt.receiptHistory.dto.ReceiptHistoryResponse;
import com.gearfirst.receipt.receiptHistory.dto.ReceiptInfoResponse;
import com.gearfirst.receipt.receiptHistory.dto.RepairRequestWrapper;
import com.gearfirst.receipt.receiptHistory.service.ReceiptHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "미처리 접수 수리 시작", description = "미처리 접수 수리를 시작합니다.")
    @PostMapping("/startRepair")
    public ResponseEntity<ApiResponse<Void>> startRepair(@RequestBody String receiptHistoryId) {
        receiptHistoryService.startRepair(receiptHistoryId);

        return ApiResponse
                .success_only(SuccessStatus.ENGINEER_ASSIGNMENT_SUCCESS);
    }

    @Operation(summary = "수리 내역 등록", description = "수리가 끝난 후 수리 내용과 부품 등 정보를 등록")
    @PostMapping("/repairDetail")
    public ResponseEntity<ApiResponse<Void>> repairDetail(@RequestBody RepairRequestWrapper request) {
        receiptHistoryService.addRepairHistories(request.getReceiptHistoryId(), request.getRepairDetailRequests());

        return ApiResponse
                .success_only(SuccessStatus.REGIST_REPAIR_DETAIL_SUCCESS);
    }

    @Operation(summary = "접수 내역 조회", description = "해당 접수에 대한 정보를 조회한다.")
    @GetMapping("/getReceiptDetail")
    public ResponseEntity<ApiResponse<ReceiptHistoryResponse>> getReceiptDetail(@RequestParam String receiptHistoryId) {
        ReceiptHistoryResponse response = receiptHistoryService.getReceiptDetail(receiptHistoryId);

        return ApiResponse
                .success(SuccessStatus.GET_RECEIPT_DETAIL_SUCCESS, response);
    }

    @Operation(summary = "발주할때 내 접수 내역 조회", description = "발주 시 내가 담당하고 있는 접수에 대한 정보를 조회한다.")
    @GetMapping("/getReceiptInfo")
    public ResponseEntity<ApiResponse<List<ReceiptInfoResponse>>> getReceiptInfo() {
        String s = "티파니 송";
        List<ReceiptInfoResponse> response = receiptHistoryService.getReceiptInfo(s);

        return ApiResponse
                .success(SuccessStatus.GET_MY_RECEIPT_ORDERING_SUCCESS, response);
    }

    @Operation(summary = "나의 수리 내역 조회", description = "내가 담당한 수리 내역을 조회한다.")
    @GetMapping("/getMyReceipt")
    public ResponseEntity<ApiResponse<List<ReceiptHistoryResponse>>> getMyReceipt(@RequestParam String startDate,
                                                                                  @RequestParam String endDate,
                                                                                  @RequestParam(required = false) String keyword) {
        String word = "";
        if(keyword != null) word = keyword;
        List<ReceiptHistoryResponse> response = receiptHistoryService.getMyReceipt(startDate, endDate, word);

        return ApiResponse
                .success(SuccessStatus.GET_MY_RECEIPT_SUCCESS, response);
    }
}
