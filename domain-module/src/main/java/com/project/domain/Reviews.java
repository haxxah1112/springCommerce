package com.project.domain;

import com.project.domain.users.Users;
import jakarta.persistence.*;

@Entity
@Table(name = "reviews")
public class Reviews extends BaseEntity {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private Users user;

    private int rating;

    @Column(columnDefinition = "TEXT")
    private String comment;
}
