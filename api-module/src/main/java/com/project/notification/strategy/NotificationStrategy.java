package com.project.notification.strategy;

import com.project.domain.users.Users;
import com.project.notification.dto.NotificationSendRequestDto;
import reactor.core.publisher.Flux;

public interface NotificationStrategy {
    String getKey();
    Flux<Users> getTargetUsers();
}
