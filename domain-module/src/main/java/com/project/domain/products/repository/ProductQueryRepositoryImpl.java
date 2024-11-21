package com.project.domain.products.repository;

import com.project.common.dto.ProductSearchDto;
import com.project.common.enums.Condition;
import com.project.domain.products.Categories;
import com.project.domain.products.QProducts;
import com.project.domain.products.cursor.ProductCursorResponseDto;
import com.project.domain.products.cursor.ProductCursorStrategyResolver;
import com.project.domain.products.cursor.strategy.CursorStrategy;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductQueryRepositoryImpl implements ProductQueryRepository {
    private final JPAQueryFactory queryFactory;
    private final ProductCursorStrategyResolver cursorStrategyFactory;


    @Override
    public Slice<ProductCursorResponseDto> findAllWithFilters(ProductSearchDto searchDto) {
        QProducts products = QProducts.products;

        String cursorValue = searchDto.getCursor();
        CursorStrategy cursorStrategy = cursorStrategyFactory.resolveStrategy(searchDto.getCondition());
        StringTemplate customCursor = cursorStrategy.generateCursor(products);
        BooleanExpression cursorCondition = cursorValue != null ? customCursor.lt(cursorValue) : null;

        BooleanBuilder filterBuilder = createFilterBuilder(searchDto, products);
        List<OrderSpecifier<?>> sortOrders = getSortOrders(products, searchDto.getCondition());

        List<ProductCursorResponseDto> productList = queryFactory
                .select(Projections.constructor(ProductCursorResponseDto.class,
                        products.id,
                        products.brand.name,
                        products.name,
                        products.category,
                        products.price,
                        products.stockQuantity,
                        products.discountRate,
                        products.salesCount,
                        customCursor.as("customCursor")
                ))
                .from(products)
                .where(
                        filterBuilder,
                        cursorCondition
                )
                .orderBy(sortOrders.toArray(new OrderSpecifier<?>[0]))
                .limit(searchDto.getSize() + 1)
                .fetch();

        boolean hasNext = productList.size() > searchDto.getSize();
        if (hasNext) {
            productList.remove(productList.size() - 1);
        }

        return new SliceImpl<>(productList, Pageable.ofSize(searchDto.getSize()), hasNext);
    }

    private BooleanBuilder createFilterBuilder(ProductSearchDto productSearchDto, QProducts product) {
        BooleanBuilder filterBuilder = new BooleanBuilder();

        addCategoryFilter(productSearchDto.getCategory(), product, filterBuilder);
        addKeywordFilter(productSearchDto.getSearchKeyword(), product, filterBuilder);

        return filterBuilder;
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

    private List<OrderSpecifier<?>> getSortOrders(QProducts products, Condition sortCondition) {
        if (sortCondition == null) {
            return List.of(products.id.desc());
        }

        CursorStrategy strategy = cursorStrategyFactory.resolveStrategy(sortCondition);
        return strategy.getSortOrders(products);
    }

}
