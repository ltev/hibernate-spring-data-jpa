package com.ltev.orderservice.repository;

import com.ltev.orderservice.domain.Address;
import com.ltev.orderservice.domain.OrderHeader;
import org.assertj.core.api.OffsetDateTimeAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderHeaderRepositoryTest {

    @Autowired
    OrderHeaderRepository repository;

    @Test
    void save() {
        var saved = repository.save(new OrderHeader());

        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void save_createdProperty() {
        OrderHeader order = new OrderHeader();
        repository.save(order);

        OrderHeader found = repository.findById(order.getId()).get();

        assertThat(found.getCreatedDate()).isNotNull();
    }

    @Test
    void testDataCorrectness() {
        Address shippingAddress = new Address("abc 1/2", "main st.", "NY", "12345","US");
        Address billToAddress = new Address("xyz 11/22", "second st.", "NY", "12345","US");

        OrderHeader order = new OrderHeader();
        order.setCustomer("Dave Abraham");
        order.setShippingAddress(shippingAddress);
        order.setToBillAddress(billToAddress);
        repository.save(order);

        OrderHeader found = repository.findById(order.getId()).get();

        assertThat(found).isEqualTo(order);
    }
}