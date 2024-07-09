package com.project.repository;

import com.project.domain.brands.Brands;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brands, Long> {
}