package com.project.domain.notification;

import com.project.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Notifications extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    private Long userId;

    private String message;

    private boolean isSuccess;

    private String errorMessage;
}
