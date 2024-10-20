package com.project.notification.controller;

import com.project.common.dto.ApiResponse;
import com.project.notification.dto.NotificationSendRequestDto;
import com.project.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<ApiResponse> sendNotification(@RequestBody NotificationSendRequestDto request) {

        notificationService.sendNotification(request);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
