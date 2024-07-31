package com.project.address.controller;

import com.project.address.dto.AddressRequestDto;
import com.project.address.service.AddressService;
import com.project.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/addresses")
public class AddressController {
    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<ApiResponse> createAddress(@Valid @RequestBody AddressRequestDto request) {
        ApiResponse createdAddress = addressService.createAddress(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAddress);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getAddress(@PathVariable Long id) {
        ApiResponse address = addressService.getAddress(id);
        return ResponseEntity.ok(address);
    }

    @PutMapping
    public ResponseEntity<ApiResponse> updateAddress(@RequestBody AddressRequestDto request) {
        ApiResponse updatedAddress = addressService.updateAddress(request);
        return ResponseEntity.ok(updatedAddress);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}
