package com.project.domain.products.repository;

import com.project.common.dto.SearchDto;
import com.project.common.enums.Condition;
import com.project.domain.products.Categories;
import com.project.domain.products.Products;
import com.project.domain.products.QProducts;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductQueryRepositoryImpl implements ProductQueryRepository {
    private final JPAQueryFactory queryFactory;
    private final QProducts product = QProducts.products;

    @Override
    public Page<Products> findAllWithFilters(SearchDto searchDto) {
        BooleanBuilder filterBuilder = createFilterBuilder(searchDto, product);
        OrderSpecifier<?> orderSpecifier = createOrderSpecifier(searchDto.getCondition(), product);

        List<Products> products = queryFactory
                .selectFrom(product)
                .where(filterBuilder)
                .orderBy(orderSpecifier)
                .offset(searchDto.getPage() * searchDto.getSize())
                .limit(searchDto.getSize())
                .fetch();

        long total = queryFactory
                .selectFrom(product)
                .where(filterBuilder)
                .fetchCount();

        return new PageImpl<>(products, PageRequest.of(searchDto.getPage(), searchDto.getSize()), total);
    }

    private BooleanBuilder createFilterBuilder(SearchDto searchDto, QProducts product) {
        BooleanBuilder filterBuilder = new BooleanBuilder();

        addCategoryFilter(searchDto.getCategory(), product, filterBuilder);
        addKeywordFilter(searchDto.getSearchKeyword(), product, filterBuilder);

        return filterBuilder;
    }

    private OrderSpecifier<?> createOrderSpecifier(Condition condition, QProducts product) {
        if (condition == null) {
            return product.id.desc();
        }

        switch (condition) {
            case NEW:
                return product.createDate.desc();
            case BEST:
                return product.salesCount.desc();
            case DISCOUNT:
                return product.discountRate.desc();
            case PRICE_HIGH:
                return product.price.desc();
            case PRICE_LOW:
                return product.price.asc();
            default:
                return product.id.desc();
        }
    }

    private void addCategoryFilter(Categories category, QProducts product, BooleanBuilder filterBuilder) {
        if (category != null) {
            filterBuilder.and(product.category.eq(category));
        }
    }

    private void addKeywordFilter(String keyword, QProducts product, BooleanBuilder filterBuilder) {
        if (keyword != null && !keyword.isEmpty()) {
            filterBuilder.and(product.name.containsIgnoreCase(keyword)
                    .or(product.brand.name.containsIgnoreCase(keyword)));
        }
    }
}
