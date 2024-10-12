package com.project.address.service;

import com.project.address.dto.AddressRequestDto;
import com.project.address.dto.AddressResponseDto;
import com.project.common.dto.ApiResponse;
import com.project.domain.address.Addresses;
import com.project.domain.address.repository.AddressRepository;
import com.project.exception.AddressException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
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
    public ApiResponse getAddress(Long id) {
        Addresses address = addressRepository.findById(id).orElseThrow(() -> new AddressException("Address not found with id: " + id));

        return ApiResponse.success(addressConverter.convertAddressEntityToResponse(address));
    }

    @Override
    public ApiResponse updateAddress(AddressRequestDto request) {
        Addresses address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new AddressException("Address not found with id " + request.getAddressId()));

        Addresses updateAddress = addressConverter.updateAddressFromRequest(address, request);

        AddressResponseDto response = addressConverter.convertAddressEntityToResponse(addressRepository.save(updateAddress));

        return ApiResponse.success(response);
    }

    @Override
    public void deleteAddress(Long id) {
        if (!addressRepository.existsById(id)) {
            throw new AddressException("Address not found with id: " + id);
        }
        addressRepository.deleteById(id);
    }
}
