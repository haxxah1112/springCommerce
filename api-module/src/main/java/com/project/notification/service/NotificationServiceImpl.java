package com.project.notification.service;

import com.project.domain.users.Users;
import com.project.handler.NotificationHandler;
import com.project.notification.dto.NotificationSendRequestDto;
import com.project.notification.strategy.NotificationStrategy;
import com.project.notification.strategy.NotificationStrategyResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Service
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
                .subscribe();
    }

}
