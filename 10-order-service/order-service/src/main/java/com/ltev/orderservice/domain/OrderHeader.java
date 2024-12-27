package com.ltev.orderservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Entity
@AttributeOverrides({
        @AttributeOverride(name = "shippingAddress.address", column = @Column(name = "shipping_address")),
        @AttributeOverride(name = "shippingAddress.city", column = @Column(name = "shipping_city")),
        @AttributeOverride(name = "shippingAddress.state", column = @Column(name = "shipping_state")),
        @AttributeOverride(name = "shippingAddress.zipCode", column = @Column(name = "shipping_zipCode")),
        @AttributeOverride(name = "shippingAddress.country", column = @Column(name = "shipping_country"))
})
@Getter
@Setter
public class OrderHeader extends BaseEntity {

    private String customer;

    @CreationTimestamp
    @Column(name = "created", updatable = false)
    private ZonedDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "last_modified")
    private ZonedDateTime lastModifiedDate;

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
