package com.project.repository;

import com.project.domain.products.Categories;
import com.project.domain.products.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Products, Long> {
    Page<Products> findByCategory(Categories category, Pageable pageable);
}
