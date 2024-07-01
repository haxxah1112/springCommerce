package com.project.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Subcategories {
    @Id @GeneratedValue
    private Long id;

    private String name;
}
