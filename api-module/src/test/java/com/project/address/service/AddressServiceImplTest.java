package com.project.address.service;

import com.project.address.dto.AddressRequestDto;
import com.project.address.dto.AddressResponseDto;
import com.project.common.ApiResponse;
import com.project.domain.address.Addresses;
import com.project.domain.address.repository.AddressRepository;
import com.project.fixture.AddressFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {
    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    @Mock
    private AddressConverter addressConverter;

    private Addresses address;
    private Addresses updatedAddress;
    private AddressRequestDto request;
    private AddressRequestDto updateRequest;

    @BeforeEach
    void setUp() {
        request = AddressRequestDto.builder()
                .street("Test Street")
                .city("Test City")
                .detail("detail")
                .zipcode("1234")
                .build();

        updateRequest = AddressRequestDto.builder()
                .addressId(1L)
                .street("Updated Street")
                .city("Updated City")
                .detail("Updated detail")
                .zipcode("5678")
                .build();


        address = AddressFixture.createAddresses(request);
        updatedAddress = AddressFixture.createAddresses(updateRequest);

    }

    @Test
    void createAddressTest() {
        //Given
        when(addressConverter.convertRequestToAddressEntity(request)).thenReturn(address);
        when(addressRepository.save(any(Addresses.class))).thenReturn(address);
        when(addressConverter.convertAddressEntityToResponse(any(Addresses.class))).thenReturn(
                AddressResponseDto.builder()
                        .id(address.getId())
                        .street(address.getStreet())
                        .city(address.getCity())
                        .detail(address.getDetail())
                        .zipcode(address.getZipcode())
                        .build()
        );

        //When
        ApiResponse<AddressResponseDto> response = addressService.createAddress(request);

        //Then
        assertEquals(address.getId(), response.getData().getId());
        assertEquals(address.getStreet(), response.getData().getStreet());
        assertEquals(address.getCity(), response.getData().getCity());
        assertEquals(address.getDetail(), response.getData().getDetail());
        assertEquals(address.getZipcode(), response.getData().getZipcode());
    }

    @Test
    void getAddressTest() {
        //Given
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(addressConverter.convertAddressEntityToResponse(address)).thenReturn(
                AddressResponseDto.builder()
                        .id(address.getId())
                        .street(address.getStreet())
                        .city(address.getCity())
                        .detail(address.getDetail())
                        .zipcode(address.getZipcode())
                        .build()
        );

        //When
        ApiResponse<AddressResponseDto> response = addressService.getAddress(1L);


        //Then
        assertEquals(address.getId(), response.getData().getId());
        assertEquals(address.getStreet(), response.getData().getStreet());
        assertEquals(address.getCity(), response.getData().getCity());
        assertEquals(address.getDetail(), response.getData().getDetail());
        assertEquals(address.getZipcode(), response.getData().getZipcode());
    }

    @Test
    void updateAddressTest() {
        //Given
        when(addressRepository.findById(updateRequest.getAddressId())).thenReturn(Optional.of(address));
        when(addressConverter.updateAddressFromRequest(address, updateRequest)).thenReturn(updatedAddress);
        when(addressRepository.save(updatedAddress)).thenReturn(updatedAddress);
        when(addressConverter.convertAddressEntityToResponse(updatedAddress)).thenReturn(
                AddressResponseDto.builder()
                        .id(updatedAddress.getId())
                        .street(updatedAddress.getStreet())
                        .city(updatedAddress.getCity())
                        .detail(updatedAddress.getDetail())
                        .zipcode(updatedAddress.getZipcode())
                        .build()
        );

        //When
        ApiResponse<AddressResponseDto> response = addressService.updateAddress(updateRequest);

        //Then
        assertEquals(updatedAddress.getId(), response.getData().getId());
        assertEquals(updatedAddress.getStreet(), response.getData().getStreet());
        assertEquals(updatedAddress.getCity(), response.getData().getCity());
        assertEquals(updatedAddress.getDetail(), response.getData().getDetail());
        assertEquals(updatedAddress.getZipcode(), response.getData().getZipcode());
    }

    @Test
    void deleteAddressTest() {
        //Given
        when(addressRepository.existsById(1L)).thenReturn(true);
        doNothing().when(addressRepository).deleteById(1L);

        //When & Then
        assertDoesNotThrow(() -> addressService.deleteAddress(1L));

        verify(addressRepository, times(1)).deleteById(1L);
    }

}