package com.project.domain.address.repository;

import com.project.domain.address.Addresses;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Addresses, Long> {
}
