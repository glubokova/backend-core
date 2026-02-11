package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ContactTest {

    @Test
    void shouldCreateContactWhenValidData() {
        Contact contact = new Contact("John", "Doe", "john@example.com");
        assertThat(contact.firstName()).isEqualTo("John");
        assertThat(contact.lastName()).isEqualTo("Doe");
        assertThat(contact.email()).isEqualTo("john@example.com");
    }

    @Test
    void shouldBeEqualWhenSameData() {
        Contact first = new Contact("John", "Doe", "john@example.com");
        Contact second = new Contact("John", "Doe", "john@example.com");

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentData() {
        Contact first = new Contact("John", "Doe", "john@example.com");
        Contact second = new Contact("Tom", "Scavo", "tom@exsample.com");
        assertThat(first).isNotEqualTo(second);
    }
}