package ru.mentee.power.crm.repository;

import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.Lead;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryLeadRepositoryTest {

    private Lead createLead(UUID id) {
        Address address = new Address("Moscow", "Lenina 1", "101000");
        Contact contact = new Contact("test@mail.ru", "+7123", address);
        return new Lead(id, contact, "TechCorp", "NEW");
    }

    @Test
    void shouldAddUniqueLead() {
        InMemoryLeadRepository repository = new InMemoryLeadRepository();
        Lead firstLead = createLead(UUID.randomUUID());
        repository.add(firstLead);
        assertThat(repository.findAll()).hasSize(1);
        assertThat(repository.findById(firstLead.id()))
                .isEqualTo(Optional.of(firstLead));
    }

    @Test
    void shouldRejectDuplicate() {
        InMemoryLeadRepository repository = new InMemoryLeadRepository();
        UUID id = UUID.randomUUID();
        Lead first = createLead(id);
        Lead duplicate = createLead(id);
        repository.add(first);
        boolean added = repository.add(duplicate);
        assertThat(added).isFalse();
        assertThat(repository.findAll()).hasSize(1);
    }

    @Test
    void shouldReturnEmptyWhenIdNotFound() {
        InMemoryLeadRepository repository = new InMemoryLeadRepository();
        Optional<Lead> result = repository.findById(UUID.randomUUID());
        assertThat(result).isEmpty();
    }

    @Test
    void shouldRemoveExistingLead() {
        InMemoryLeadRepository repository = new InMemoryLeadRepository();
        UUID id = UUID.randomUUID();
        Lead lead = createLead(id);
        repository.add(lead);
        repository.remove(id);
        assertThat(repository.findAll()).hasSize(0);
        assertThat(repository.findById(id)).isEmpty();
    }

    @Test
    void shouldReturnDefensiveCopy() {
        InMemoryLeadRepository repository = new InMemoryLeadRepository();
        Lead lead = createLead(UUID.randomUUID());
        repository.add(lead);
        List<Lead> externalList = repository.findAll();
        externalList.clear(); // клиент ломает список
        assertThat(repository.findAll()).hasSize(1);
    }
}