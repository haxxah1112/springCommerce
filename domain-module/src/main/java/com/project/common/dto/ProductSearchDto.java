package com.project.common.dto;


import com.project.common.enums.Condition;
import com.project.domain.products.Categories;
import com.project.dto.PaginationDto;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class ProductSearchDto extends PaginationDto {
    private String searchKeyword;
    private Condition condition;
    @NotNull
    private Categories category;
}
