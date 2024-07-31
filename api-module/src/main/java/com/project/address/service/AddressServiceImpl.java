package com.project.address.service;

import com.project.address.dto.AddressRequestDto;
import com.project.address.dto.AddressResponseDto;
import com.project.common.ApiResponse;
import com.project.domain.address.Addresses;
import com.project.domain.address.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private AddressConverter addressConverter;
    private AddressRepository addressRepository;

    @Override
    public ApiResponse createAddress(AddressRequestDto request) {
        Addresses addresses = addressConverter.convertRequestToAddressEntity(request);
        return ApiResponse.success(addressConverter.convertAddressEntityToResponse(addressRepository.save(addresses)));
    }

    @Override
    public ApiResponse getAddress(Long id) {
        return ApiResponse.success(addressRepository.findById(id));
    }

    @Override
    public ApiResponse updateAddress(AddressRequestDto request) {
        Addresses address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new RuntimeException("Address not found with id " + request.getAddressId()));

        Addresses updateAddress = addressConverter.updateAddressFromRequest(address, request);

        AddressResponseDto response = addressConverter.convertAddressEntityToResponse(addressRepository.save(updateAddress));

        return ApiResponse.success(response);
    }

    @Override
    public void deleteAddress(Long id) {
        addressRepository.deleteById(id);
    }
}
