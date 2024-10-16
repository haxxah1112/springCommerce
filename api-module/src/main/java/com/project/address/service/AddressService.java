package com.project.address.service;

import com.project.address.dto.AddressRequestDto;
import com.project.common.dto.ApiResponse;

public interface AddressService {
    ApiResponse createAddress(AddressRequestDto request);

    ApiResponse getAddress(Long id);

    ApiResponse updateAddress(AddressRequestDto request);

    void deleteAddress(Long id);
}
