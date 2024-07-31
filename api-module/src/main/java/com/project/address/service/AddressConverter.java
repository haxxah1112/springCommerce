package com.project.address.service;

import com.project.address.dto.AddressRequestDto;
import com.project.address.dto.AddressResponseDto;
import com.project.domain.address.Addresses;
import org.springframework.stereotype.Component;

@Component
public class AddressConverter {
    public Addresses convertRequestToAddressEntity(AddressRequestDto request) {
        return Addresses.builder()
                .city(request.getCity())
                .street(request.getStreet())
                .detail(request.getDetail())
                .zipcode(request.getZipcode())
                .build();
    }

    public Addresses updateAddressFromRequest(Addresses address, AddressRequestDto request) {
        return Addresses.builder()
                .id(address.getId())
                .street(request.getStreet())
                .city(request.getCity())
                .zipcode(request.getZipcode())
                .build();
    }

    public AddressResponseDto convertAddressEntityToResponse(Addresses address) {
        return AddressResponseDto.builder()
                .id(address.getId())
                .city(address.getCity())
                .street(address.getStreet())
                .detail(address.getDetail())
                .zipcode(address.getZipcode())
                .build();

    }
}
