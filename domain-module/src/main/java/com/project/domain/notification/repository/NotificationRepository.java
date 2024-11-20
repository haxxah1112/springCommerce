package com.project.domain.notification.repository;

import com.project.domain.notification.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notifications, Long> {
}
