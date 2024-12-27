package com.ltev.orderservice.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Address {

    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}
