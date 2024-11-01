package com.project.user.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserLoginRequestDto {
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
}
