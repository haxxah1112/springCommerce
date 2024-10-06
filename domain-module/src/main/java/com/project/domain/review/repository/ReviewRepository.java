package com.project.domain.review.repository;

import com.project.domain.review.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Reviews, Long> {
}
