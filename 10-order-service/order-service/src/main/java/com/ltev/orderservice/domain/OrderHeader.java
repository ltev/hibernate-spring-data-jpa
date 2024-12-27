package com.ltev.orderservice.domain;

import jakarta.persistence.*;

@Entity
@AttributeOverrides({
        @AttributeOverride(name = "shippingAddress.address", column = @Column(name = "shipping_address")),
        @AttributeOverride(name = "shippingAddress.city", column = @Column(name = "shipping_city")),
        @AttributeOverride(name = "shippingAddress.state", column = @Column(name = "shipping_state")),
        @AttributeOverride(name = "shippingAddress.zipCode", column = @Column(name = "shipping_zipCode")),
        @AttributeOverride(name = "shippingAddress.country", column = @Column(name = "shipping_country"))
})
public class OrderHeader extends BaseEntity {

    private String customer;

    @Embedded
    private Address shippingAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "address", column = @Column(name = "bill_to_address")),
            @AttributeOverride(name = "city", column = @Column(name = "bill_to_city")),
            @AttributeOverride(name = "state", column = @Column(name = "bill_to_state")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "bill_to_zipCode")),
            @AttributeOverride(name = "country", column = @Column(name = "bill_to_country"))
    })
    private Address toBillAddress;
}
