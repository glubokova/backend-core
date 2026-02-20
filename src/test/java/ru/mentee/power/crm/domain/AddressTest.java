package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AddressTest {

    @Test
    void shouldCreateAddressWhenValidData() {
        Address address = new Address("San Francisco", "123 Main St", "94105");
        assertThat(address.city()).isEqualTo("San Francisco");
        assertThat(address.street()).isEqualTo("123 Main St");
        assertThat(address.zip()).isEqualTo("94105");
    }

    @Test
    void shouldBeEqualWhenSameData() {
        Address first = new Address("San Francisco", "123 Main St", "94105");
        Address second = new Address("San Francisco", "123 Main St", "94105");
        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    void shouldThrowExceptionWhenCityIsBlank() {
        assertThatThrownBy(() ->  new Address(null, "123 Main St", "94105"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowExceptionWhenZipIsBlank() {
        assertThatThrownBy(() -> new Address("San Francisco", "94105", ""))
                .isInstanceOf(IllegalArgumentException.class);
    }
}