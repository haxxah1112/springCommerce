package com.project.domain.users;

import com.project.domain.BaseEntity;
import com.project.domain.address.Addresses;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "userId")
    private List<Addresses> addresses = new ArrayList<>();

    private String name;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;
}
