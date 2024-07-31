package com.project.address.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class AddressRequestDto {
    private Long addressId;

    private Long userId;

    @NotBlank(message = "도로명은 필수 입력 값입니다.")
    @Size(max = 100, message = "도로명은 100자 이내여야 합니다.")
    private String street;

    @NotBlank(message = "도시는 필수 입력 값입니다.")
    @Size(max = 50, message = "도시는 50자 이내여야 합니다.")
    private String city;

    private String detail;

    @NotBlank(message = "우편번호는 필수 입력 값입니다.")
    private String zipcode;

}
