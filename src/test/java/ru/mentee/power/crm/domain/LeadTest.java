package ru.mentee.power.crm.domain;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class LeadTest {

    @Test
    void shouldReturnIdWhenLeadCreated() {
        UUID id = UUID.randomUUID();

        Lead lead = new Lead(id, "test@example.com", "+71234567890", "TestCompany", "NEW");

        assertThat(lead.id()).isEqualTo(id);
    }

    @Test
    void shouldReturnEmailWhenLeadCreated() {
        Lead lead = new Lead(UUID.randomUUID(), "test@example.com", "+71234567890", "TestCompany", "NEW");

        assertThat(lead.email()).isEqualTo("test@example.com");
    }

    @Test
    void shouldReturnPhoneWhenLeadCreated() {
        Lead lead = new Lead(UUID.randomUUID(), "test@example.com", "+71234567890", "TestCompany", "NEW");
        assertThat(lead.phone()).isEqualTo("+71234567890");
    }

    @Test
    void shouldReturnCompanyWhenLeadCreated() {
        Lead lead = new Lead(UUID.randomUUID(), "test@example.com", "+71234567890", "TestCompany", "NEW");
        assertThat(lead.company()).isEqualTo("TestCompany");
    }

    @Test
    void shouldReturnStatusWhenLeadCreated() {
        Lead lead = new Lead(UUID.randomUUID(), "test@example.com", "+71234567890", "TestCompany", "NEW");
        assertThat(lead.status()).isEqualTo("NEW");
    }

    @Test
    void shouldGenerateUniqueIdsWhenMultipleLeadsCreated() {
        Lead first = new Lead(UUID.randomUUID(), "a@test.com", "+1", "A", "NEW");
        Lead second = new Lead(UUID.randomUUID(), "b@test.com", "+2", "B", "NEW");

        assertThat(first.id()).isNotEqualTo(second.id());
    }

    @Test
    void shouldContainFieldsWhenToStringCalled() {
        UUID id = UUID.randomUUID();
        Lead lead = new Lead(id, "test@example.com", "+71234567890", "TestCompany", "NEW");
        String result = lead.toString();
        assertThat(result)
                .contains(id.toString())
                .contains("TestCompany")
                .contains("NEW");
    }
}