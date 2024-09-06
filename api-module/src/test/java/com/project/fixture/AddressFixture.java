package com.project.fixture;

import com.project.address.dto.AddressRequestDto;
import com.project.domain.address.Addresses;

public class AddressFixture {
    public static Addresses createAddresses(AddressRequestDto request) {
        return Addresses.builder()
                .city(request.getCity())
                .street(request.getStreet())
                .detail(request.getDetail())
                .zipcode(request.getZipcode())
                .build();
    }
}
