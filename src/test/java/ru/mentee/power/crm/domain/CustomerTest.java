package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class CustomerTest {

    @Test
    void shouldReuseContactWhenCreatingCustomer() {
        Address contactAddress = new Address("Oren", "Mira", "01001");
        Address billingAddress = new Address("Ufa", "Novaya", "45200");
        Contact contact = new Contact("ivan@mail.ru", "+7891", contactAddress);

        Customer customer = new Customer(
                UUID.randomUUID(),
                contact,
                billingAddress,
                "GOLD"
        );

        assertThat(customer.contact().address()).isNotEqualTo(customer.billingAddress());
    }

    @Test
    void shouldDemonstrateContactReuseAcrossLeadAndCustomer() {
        Contact contact = new Contact("ivan@mail.ru", "+7891",
                new Address("Oren", "Mira", "01001")
        );

        Lead lead = new Lead(UUID.randomUUID(), contact, "Company", "NEW");
        Customer customer = new Customer(UUID.randomUUID(), contact,
                new Address("Ufa", "Novaya", "45200"),
                "SILVER"
        );

        assertThat(lead.contact()).isEqualTo(customer.contact());
    }
}