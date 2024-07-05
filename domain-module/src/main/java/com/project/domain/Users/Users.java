package com.project.domain.Users;

import com.project.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class Users extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;
}
