package com.project.notification.strategy;

import com.project.domain.users.Users;
import com.project.domain.users.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class AllUsersStrategy implements NotificationStrategy {
    private final UserRepository userRepository;

    @Override
    public String getKey() {
        return "ALL";
    }

    @Override
    public Flux<Users> getTargetUsers() {
        return Flux.fromIterable(userRepository.findAllByIsNotificationActive(true));
    }
}
