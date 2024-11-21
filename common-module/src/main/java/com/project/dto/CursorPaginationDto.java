package com.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CursorPaginationDto {
    private String cursor;
    private Long longCursor;
    private int size = 10;

    public Pageable toPageable() {
        return PageRequest.of(0, size);
    }
}
