package ru.mentee.power.crm.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.InMemoryLeadRepository;
import ru.mentee.power.crm.repository.LeadRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class LeadServiceTest {

    private LeadService service;
    private LeadRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryLeadRepository();
        service = new LeadService(repository);
    }

    @Test
    void shouldCreateLeadWhenEmailIsUnique() {
        String email = "test@example.com";
        String company = "Test Company";
        LeadStatus status = LeadStatus.NEW;
        Lead result = service.addLead(email, company, status);
        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo(email);
        assertThat(result.company()).isEqualTo(company);
        assertThat(result.status()).isEqualTo(status);
        assertThat(result.id()).isNotNull();
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        String email = "duplicate@example.com";
        service.addLead(email, "First Company", LeadStatus.NEW);
        assertThatThrownBy(() ->
                service.addLead(email, "Second Company", LeadStatus.NEW)
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Lead with email already exists");
    }

    @Test
    void shouldFindAllLeads() {
        service.addLead("one@example.com", "Company 1", LeadStatus.NEW);
        service.addLead("two@example.com", "Company 2", LeadStatus.CONTACTED);
        List<Lead> result = service.findAll();
        assertThat(result).hasSize(2);
    }

    @Test
    void shouldFindLeadById() {
        Lead created = service.addLead("find@example.com", "Company", LeadStatus.NEW);
        Optional<Lead> result = service.findById(created.id());
        assertThat(result).isPresent();
        assertThat(result.get().email()).isEqualTo("find@example.com");
    }

    @Test
    void shouldFindLeadByEmail() {
        service.addLead("search@example.com", "Company", LeadStatus.NEW);

        Optional<Lead> result = service.findByEmail("search@example.com");
        assertThat(result).isPresent();
        assertThat(result.get().company()).isEqualTo("Company");
    }

    @Test
    void shouldReturnEmptyWhenLeadNotFound() {
        Optional<Lead> result = service.findByEmail("nonexistent@example.com");
        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnOnlyNewLeadsWhenFindByStatusNew() {
        service.addLead("new1@test.com", "A", LeadStatus.NEW);
        service.addLead("new2@test.com", "B", LeadStatus.NEW);
        service.addLead("new3@test.com", "C", LeadStatus.CONTACTED);
        service.addLead("new4@test.com", "D", LeadStatus.QUALIFIED);

        List<Lead> result =
                service.findByStatus(LeadStatus.NEW);
        assertThat(result).hasSize(2);
        assertThat(result)
                .allMatch(
                        lead -> lead.status()
                                .equals(LeadStatus.NEW)
                );
    }

    @Test
    void shouldReturnOnlyContactedLeadsWhenFindByStatusContacted() {
        service.addLead("new@test.com", "A", LeadStatus.NEW);
        service.addLead("cont1@test.com", "B", LeadStatus.CONTACTED);
        service.addLead("cont2@test.com", "C", LeadStatus.CONTACTED);

        List<Lead> result =
                service.findByStatus(
                        LeadStatus.CONTACTED
                );
        assertThat(result).hasSize(2);
        assertThat(result)
                .allMatch(
                        lead -> lead.status()
                                .equals(
                                        LeadStatus.CONTACTED
                                )
                );
    }

    @Test
    void shouldReturnOnlyQualifiedLeadsWhenFindByStatusQualified() {
        service.addLead("new@test.com", "A", LeadStatus.NEW);
        service.addLead("qual@test.com", "B", LeadStatus.QUALIFIED);

        List<Lead> result =
                service.findByStatus(
                        LeadStatus.QUALIFIED
                );

        assertThat(result).hasSize(1);

        assertThat(result)
                .allMatch(
                        lead -> lead.status()
                                .equals(
                                        LeadStatus.QUALIFIED
                                )
                );
    }

    @Test
    void shouldReturnEmptyListWhenNoLeadsWithStatus() {
        service.addLead(
                "new@test.com",
                "A",
                LeadStatus.NEW
        );

        service.addLead(
                "cont@test.com",
                "B",
                LeadStatus.CONTACTED
        );

        List<Lead> result =
                service.findByStatus(
                        LeadStatus.QUALIFIED
                );
        assertThat(result).isEmpty();
    }
}