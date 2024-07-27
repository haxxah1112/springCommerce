package com.project.common.dto;


import com.project.common.enums.Condition;
import com.project.domain.products.Categories;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SearchDto {
    private int page = 0;
    private int size = 10;
    private String searchKeyword;
    private Condition condition;
    @NotNull
    private Categories category;
}
