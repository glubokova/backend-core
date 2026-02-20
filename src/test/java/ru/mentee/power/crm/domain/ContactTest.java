package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ContactTest {

    @Test
    void shouldCreateContactWhenValidData() {
        Address address = new Address("San Francisco", "123 Main St", "94105");
        Contact contact = new Contact("john@example.com", "+7891", address);
        assertThat(contact.address()).isEqualTo(address);
        assertThat(contact.address().city()).isEqualTo("San Francisco");
    }

    @Test
    void shouldDelegateToAddressWhenAccessingCity() {
        Address address = new Address("San Francisco", "123 Main St", "94105");
        Contact contact = new Contact("john@example.com", "+7891", address);
        assertThat(contact.address().city()).isEqualTo("San Francisco");
        assertThat(contact.address().street()).isEqualTo("123 Main St");
    }

    @Test
    void shouldThrowExceptionWhenAddressIsNull() {
        assertThatThrownBy(() -> new Contact("john@example.com", "123 Main St",  null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}