package com.project.domain.review.repository;

import com.project.domain.review.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Reviews, Long> {
    List<Reviews> findByProductId(Long productId);
}
