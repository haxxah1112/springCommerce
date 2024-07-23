package com.project.domain.brands;

import com.project.domain.BaseEntity;
import com.project.domain.users.Users;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "brands")
public class Brands extends BaseEntity {
    @Id @GeneratedValue
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private Users user;

    private String name;

    @Enumerated(EnumType.STRING)
    private BrandStatus status;

}
