package com.project.address.service;

import com.project.address.dto.AddressRequestDto;
import com.project.address.dto.AddressResponseDto;
import com.project.common.dto.ApiResponse;
import com.project.domain.address.Addresses;
import com.project.domain.address.repository.AddressRepository;
import com.project.exception.NotFoundException;
import com.project.exception.error.CustomError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressConverter addressConverter;
    private final AddressRepository addressRepository;

    @Override
    public ApiResponse createAddress(AddressRequestDto request) {
        Addresses addresses = addressConverter.convertRequestToAddressEntity(request);
        return ApiResponse.success(addressConverter.convertAddressEntityToResponse(addressRepository.save(addresses)));
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse getAddress(Long id) {
        Addresses address = addressRepository.findById(id).orElseThrow(() -> new NotFoundException(CustomError.NOT_FOUND));

        return ApiResponse.success(addressConverter.convertAddressEntityToResponse(address));
    }

    @Override
    public ApiResponse updateAddress(AddressRequestDto request) {
        Addresses address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new NotFoundException(CustomError.NOT_FOUND));

        Addresses updateAddress = addressConverter.updateAddressFromRequest(address, request);

        AddressResponseDto response = addressConverter.convertAddressEntityToResponse(addressRepository.save(updateAddress));

        return ApiResponse.success(response);
    }

    @Override
    public void deleteAddress(Long id) {
        if (!addressRepository.existsById(id)) {
            throw new NotFoundException(CustomError.NOT_FOUND);
        }
        addressRepository.deleteById(id);
    }
}
