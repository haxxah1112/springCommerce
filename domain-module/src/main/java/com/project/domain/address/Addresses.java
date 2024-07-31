package com.project.domain.address;

import com.project.domain.BaseEntity;
import com.project.domain.users.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "addresses")
public class Addresses extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String street;

    private String city;

    private String detail;

    private String zipcode;

}
