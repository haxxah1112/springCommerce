package com.project.domain.products;

import jakarta.persistence.*;

@Entity
public class Subcategories {
    @Id @GeneratedValue
    private Long id;

    private String name;
    @Enumerated(EnumType.STRING)
    private Categories categories;
}
