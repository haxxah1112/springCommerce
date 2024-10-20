package com.project.handler;

import com.project.domain.users.Users;
import com.project.notification.dto.NotificationSendRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class NotificationHandler {

    public Mono<Void> sendNotificationToUser(Users user, NotificationSendRequestDto requestDto) {
        // TODO: 알림 전송 로직
        return Mono.empty();
    }
}
