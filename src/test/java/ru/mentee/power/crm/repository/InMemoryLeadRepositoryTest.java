package ru.mentee.power.crm.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryLeadRepositoryTest {

    private InMemoryLeadRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryLeadRepository();
    }

    private Lead createLead(String email) {
        return new Lead(
                UUID.randomUUID(),
                email,
                "TechCorp",
                LeadStatus.NEW
        );
    }

    @Test
    void shouldSaveAndFindById() {
        Lead lead = createLead("test@mail.com");

        repository.save(lead);

        Optional<Lead> result = repository.findById(lead.id());

        assertThat(result).isPresent();
        assertThat(result.get().email()).isEqualTo("test@mail.com");
    }

    @Test
    void shouldFindByEmail() {
        Lead lead = createLead("email@test.com");

        repository.save(lead);

        Optional<Lead> result = repository.findByEmail("email@test.com");

        assertThat(result).isPresent();
        assertThat(result.get().company()).isEqualTo("TechCorp");
    }

    @Test
    void shouldReturnEmptyWhenEmailNotFound() {
        Optional<Lead> result = repository.findByEmail("nope@mail.com");

        assertThat(result).isEmpty();
    }

    @Test
    void shouldFindAll() {
        repository.save(createLead("one@mail.com"));
        repository.save(createLead("two@mail.com"));

        List<Lead> result = repository.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void shouldDeleteLead() {
        Lead lead = createLead("delete@mail.com");

        repository.save(lead);
        repository.delete(lead.id());

        assertThat(repository.findAll()).isEmpty();
    }
}