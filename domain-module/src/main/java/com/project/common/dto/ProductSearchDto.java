package com.project.common.dto;


import com.project.common.enums.Condition;
import com.project.domain.products.Categories;
import com.project.dto.CursorPaginationDto;
import com.project.dto.PaginationDto;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductSearchDto extends CursorPaginationDto {
    private String searchKeyword;
    @Builder.Default
    private Condition condition = Condition.NEW;

    @NotNull
    private Categories category;
}
