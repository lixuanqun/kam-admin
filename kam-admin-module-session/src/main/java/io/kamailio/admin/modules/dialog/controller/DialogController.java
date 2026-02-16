package io.kamailio.admin.modules.dialog.controller;

import io.kamailio.admin.common.dto.ApiResponse;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.modules.dialog.entity.Dialog;
import io.kamailio.admin.modules.dialog.service.DialogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "对话管理")
@RestController
@RequestMapping("/api/dialogs")
public class DialogController {
    private final DialogService service;

    public DialogController(DialogService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "获取对话列表（数据库）")
    public ResponseEntity<ApiResponse<PaginatedResult<Dialog>>> findAll(PaginationDto dto) {
        return ResponseEntity.ok(ApiResponse.success(service.findAll(dto)));
    }

    @GetMapping("/active")
    @Operation(summary = "获取活动对话（内存）")
    public ResponseEntity<ApiResponse<Object>> getActiveDialogs() {
        return ResponseEntity.ok(ApiResponse.success(service.getActiveDialogs()));
    }

    @GetMapping("/stats")
    @Operation(summary = "获取对话统计")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats() {
        return ResponseEntity.ok(ApiResponse.success(service.getStats()));
    }

    @PostMapping("/end")
    @Operation(summary = "结束对话")
    public ResponseEntity<ApiResponse<Void>> endDialog(@RequestBody EndDialogRequest body) {
        service.endDialog(body.getHashEntry(), body.getHashId());
        return ResponseEntity.ok(ApiResponse.success(null, "对话已结束"));
    }

    @GetMapping("/detail")
    @Operation(summary = "获取对话详情")
    public ResponseEntity<ApiResponse<Object>> getDialogDetail(@RequestParam int hashEntry, @RequestParam int hashId) {
        return ResponseEntity.ok(ApiResponse.success(service.getDialogDetail(hashEntry, hashId)));
    }

    @PostMapping("/bridge")
    @Operation(summary = "桥接/转接对话")
    public ResponseEntity<ApiResponse<Void>> bridgeDialog(@RequestBody BridgeRequest body) {
        service.bridgeDialog(body.getFrom(), body.getTo());
        return ResponseEntity.ok(ApiResponse.success(null, "桥接成功"));
    }

    public static class EndDialogRequest {
        private int hashEntry, hashId;
        public int getHashEntry() { return hashEntry; }
        public void setHashEntry(int hashEntry) { this.hashEntry = hashEntry; }
        public int getHashId() { return hashId; }
        public void setHashId(int hashId) { this.hashId = hashId; }
    }
    public static class BridgeRequest {
        private String from, to;
        public String getFrom() { return from; }
        public void setFrom(String from) { this.from = from; }
        public String getTo() { return to; }
        public void setTo(String to) { this.to = to; }
    }
}
