package com.project.notification.service;

import com.project.domain.users.Users;
import com.project.handler.NotificationHandler;
import com.project.notification.dto.NotificationSendRequestDto;
import com.project.notification.strategy.NotificationStrategy;
import com.project.notification.strategy.NotificationStrategyResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationStrategyResolver notificationStrategyResolver;
    private final NotificationHandler notificationHandler;

    @Override
    public void sendNotification(NotificationSendRequestDto request) {
        NotificationStrategy strategy = notificationStrategyResolver.resolveStrategy(request.getNotificationType());
        Flux<Users> targetUsers = strategy.getTargetUsers();
        targetUsers
                .flatMap(user -> notificationHandler.sendNotificationToUser(user, request))
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(
                        result -> log.info("처리 완료: {}", result),
                        error -> log.error("에러 발생: ", error),
                        () -> log.info("모든 처리 완료")
                );

    }

}
