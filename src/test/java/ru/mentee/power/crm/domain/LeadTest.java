package ru.mentee.power.crm.domain;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class LeadTest {

    @Test
    void shouldCreateLeadWhenValidData() {
        Address address = new Address("NY", "Broadway", "10001");
        Contact contact = new Contact("a@mail.com", "111", address);

        Lead lead = new Lead(UUID.randomUUID(), contact, "Google", "NEW");

        assertThat(lead.contact()).isEqualTo(contact);
    }

    @Test
    void shouldAccessEmailThroughDelegationWhenLeadCreated() {
        Address address = new Address("Paris", "Champs", "75000");
        Contact contact = new Contact("test@mail.com", "222", address);

        Lead lead = new Lead(UUID.randomUUID(), contact, "Company", "NEW");

        assertThat(lead.contact().email()).isEqualTo("test@mail.com");
        assertThat(lead.contact().address().city()).isEqualTo("Paris");
    }

    @Test
    void shouldBeEqualWhenSameIdButDifferentContact() {
        UUID id = UUID.randomUUID();

        Contact c1 = new Contact("a@mail.com", "1",
                new Address("City1", "Street1", "111"));
        Contact c2 = new Contact("b@mail.com", "2",
                new Address("City2", "Street2", "222"));

        Lead l1 = new Lead(id, c1, "A", "NEW");
        Lead l2 = new Lead(id, c2, "B", "QUALIFIED");

        assertThat(l1).isEqualTo(l2);
    }

    @Test
    void shouldThrowExceptionWhenContactIsNull() {
        assertThatThrownBy(() ->
                new Lead(UUID.randomUUID(), null, "Company", "NEW")
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowExceptionWhenInvalidStatus() {
        Address address = new Address("City", "Street", "111");
        Contact contact = new Contact("a@mail.com", "1", address);

        assertThatThrownBy(() ->
                new Lead(UUID.randomUUID(), contact, "Company", "INVALID")
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldDemonstrateThreeLevelCompositionWhenAccessingCity() {
        Lead lead = new Lead(
                UUID.randomUUID(),
                new Contact(
                        "mail@test.com",
                        "123",
                        new Address("Berlin", "Unter", "10117")
                ),
                "Company",
                "NEW"
        );

        String city = lead.contact().address().city();

        assertThat(city).isEqualTo("Berlin");
    }
}