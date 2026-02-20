package ru.mentee.power.crm.storage;

import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.Lead;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LeadStorageTest {

    private Lead createLead(String email) {
        Address address = new Address("Moscow", "Lenina 1", "101000");
        Contact contact = new Contact(
                email,
                "+71234567890",
                address
        );
        return new Lead(
                UUID.randomUUID(),
                contact,
                "TechCorp",
                "NEW"
        );
    }

    @Test
    void shouldAddLeadWhenLeadIsUnique() {
        LeadStorage leadStorage = new LeadStorage();
        Lead uniqueLead = createLead("ivan@mail.ru");

        boolean added = leadStorage.add(uniqueLead);

        assertThat(added).isTrue();
        assertThat(leadStorage.size()).isEqualTo(1);
        assertThat(leadStorage.findAll()).containsExactly(uniqueLead);
    }

    @Test
    void shouldRejectDuplicateWhenEmailAlreadyExists() {
        LeadStorage leadStorage = new LeadStorage();

        Lead existingLead = createLead("ivan@mail.ru");
        Lead duplicateLead = createLead("ivan@mail.ru");

        leadStorage.add(existingLead);
        boolean added = leadStorage.add(duplicateLead);

        assertThat(added).isFalse();
        assertThat(leadStorage.size()).isEqualTo(1);
        assertThat(leadStorage.findAll()).containsExactly(existingLead);
    }

    @Test
    void shouldThrowExceptionWhenStorageIsFull() {
        LeadStorage leadStorage = new LeadStorage();

        for (int i = 0; i < 100; i++) {
            leadStorage.add(createLead("lead" + i + "@mail.ru"));
        }

        Lead hundredFirstLead = createLead("overflow@mail.ru");
        assertThatThrownBy(() -> leadStorage.add(hundredFirstLead))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Storage is full");
    }

    @Test
    void shouldReturnOnlyAddedLeadsWhenFindAllCalled() {
        LeadStorage leadStorage = new LeadStorage();
        Lead firstLead = createLead("ivan@mail.ru");
        Lead secondLead = createLead("maria@startup.io");

        leadStorage.add(firstLead);
        leadStorage.add(secondLead);

        Lead[] result = leadStorage.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(firstLead, secondLead);
    }
}