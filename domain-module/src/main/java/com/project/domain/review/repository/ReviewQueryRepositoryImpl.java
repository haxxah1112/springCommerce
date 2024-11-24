package com.project.domain.review.repository;

import com.project.common.dto.ReviewSearchDto;
import com.project.domain.review.QReviews;
import com.project.domain.review.Reviews;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewQueryRepositoryImpl implements ReviewQueryRepository {
    private final JPAQueryFactory queryFactory;
    private final QReviews review = QReviews.reviews;

    @Override
    public Page<Reviews> findAllByProductId(ReviewSearchDto reviewSearchDto) {
        List<Reviews> reviews = queryFactory
                .selectFrom(review)
                .where(review.product.id.eq(reviewSearchDto.getProductId()))
                .offset(reviewSearchDto.getPage() * reviewSearchDto.getSize())
                .limit(reviewSearchDto.getSize())
                .fetch();

        long total = queryFactory
                .selectFrom(review)
                .where(review.product.id.eq(reviewSearchDto.getProductId()))
                .fetchCount();

        return new PageImpl<>(reviews, reviewSearchDto.toPageable(), total);
    }

}
