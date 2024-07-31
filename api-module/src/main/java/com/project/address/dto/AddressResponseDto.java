package com.project.address.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AddressResponseDto {
    private Long id;

    private String street;

    private String city;

    private String detail;

    private String zipcode;

}
