package com.project.domain.review.repository;

import com.project.common.dto.ReviewSearchDto;
import com.project.domain.review.Reviews;
import org.springframework.data.domain.Page;

public interface ReviewQueryRepository {
    Page<Reviews> findAllByProductId(ReviewSearchDto reviewSearchDto);
}
