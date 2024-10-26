package com.project.handler;

import com.project.domain.users.Users;
import com.project.notification.dto.NotificationSendRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationHandler {

    public Mono<Void> sendNotificationToUser(Users user, NotificationSendRequestDto request) {
        // TODO: 알림 전송 로직
        return Mono.fromRunnable(() -> blockingMethod(user))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }

}
