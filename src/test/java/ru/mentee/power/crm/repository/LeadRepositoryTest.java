package ru.mentee.power.crm.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.model.Lead;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class LeadRepositoryTest {
    private LeadRepository leadRepository;

    @BeforeEach
    void setUp() {
        leadRepository = new InMemoryLeadRepository();
    }

    @Test
    void shouldSaveAndFindLeadByIdWhenLeadSaved() {
        UUID id = UUID.randomUUID();
        Lead lead = new Lead(
                id,
                "john@example.com",
                "T",
                LeadStatus.NEW
        );

        leadRepository.save(lead);
        Optional<Lead> found = leadRepository.findById(id);

        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(lead);
        assertThat(leadRepository.findAll()).hasSize(1);
    }

    @Test
    void shouldReturnNullWhenLeadNotFound() {
        UUID unknownId = UUID.randomUUID();
        Optional<Lead> found = leadRepository.findById(unknownId);
        assertThat(found).isEmpty();
    }

    @Test
    void shouldReturnAllLeadsWhenMultipleLeadsSaved() {
        for (int i = 0; i < 3; i++) {
            UUID id = UUID.randomUUID();
            Lead lead = new Lead(
                    id,
                    "user" + i + "@mail.ru",
                    "Company" + i,
                    LeadStatus.NEW
            );
            leadRepository.save(lead);
        }

        List<Lead> allLeads = leadRepository.findAll();

        assertThat(allLeads).hasSize(3);
    }

    @Test
    void shouldDeleteLeadWhenLeadExists() {
        UUID id = UUID.randomUUID();
        Lead lead = new Lead(
                id,
                "test@mail.com",
                "TechCorp",
                LeadStatus.NEW
        );
        leadRepository.save(lead);
        leadRepository.delete(id);

        Optional<Lead> found = leadRepository.findById(id);
        assertThat(found).isEmpty();
        assertThat(leadRepository.findAll()).hasSize(0);
    }

    @Test
    void shouldOverwriteLeadWhenSaveWithSameId() {
        UUID id = UUID.randomUUID();
        Lead firstLead = new Lead(
                id,
                "first@mail.com",
                "FirstCompany",
                LeadStatus.NEW
        );

        Lead secondLead = new Lead(
                id,
                "second@mail.com",
                "SecondCompany",
                LeadStatus.QUALIFIED
        );

        leadRepository.save(firstLead);
        leadRepository.save(secondLead);

        Optional<Lead> found = leadRepository.findById(id);
        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(secondLead);
        assertThat(leadRepository.findAll()).hasSize(1);
    }

    @Test
    void shouldSaveBothLeadsEvenWithSameEmailBecauseRepositoryDoesNotCheckBusinessRules() {
        Lead lead1 = new Lead(
                UUID.randomUUID(),
                "ivan@mail.com",
                "CompanyA",
                LeadStatus.NEW
        );

        Lead lead2 = new Lead(
                UUID.randomUUID(),
                "ivan@mail.com",
                "CompanyB",
                LeadStatus.CONTACTED
        );

        leadRepository.save(lead1);
        leadRepository.save(lead2);

        assertThat(leadRepository.findAll()).hasSize(2);

        Optional<Lead> found1 = leadRepository.findByEmail("ivan@mail.com");
        assertThat(found1).isPresent();
        assertThat(found1.get().company()).isEqualTo("CompanyB");
    }

    @Test
    void shouldFindLeadByEmail() {
        UUID id = UUID.randomUUID();
        Lead lead = new Lead(
                id,
                "search@example.com",
                "Search Company",
                LeadStatus.NEW
        );

        leadRepository.save(lead);
        Optional<Lead> found = leadRepository.findByEmail("search@example.com");

        assertThat(found).isPresent();
        assertThat(found.get().id()).isEqualTo(id);
        assertThat(found.get().company()).isEqualTo("Search Company");
    }

    @Test
    void shouldReturnEmptyWhenEmailNotFound() {
        Optional<Lead> found = leadRepository.findByEmail("nonexistent@example.com");
        assertThat(found).isEmpty();
    }

    @Test
    void shouldDemonstrateMapIsFasterThanList() {
        List<Lead> leadList = new ArrayList<>();

        for (int i = 0; i < 10_000; i++) {
            UUID id = UUID.randomUUID();
            Lead lead = new Lead(
                    id,
                    "email" + i + "@test.com",
                    "Company" + i,
                    LeadStatus.NEW
            );

            leadRepository.save(lead);
            leadList.add(lead);
        }

        UUID targetId = leadList.get(5_000).id();

        for (int i = 0; i < 1000; i++) {
            leadRepository.findById(targetId);
            leadList.stream()
                    .filter(l -> l.id().equals(targetId))
                    .findFirst();
        }

        long mapStart = System.nanoTime();
        Optional<Lead> foundInMap = leadRepository.findById(targetId);
        long mapDuration = System.nanoTime() - mapStart;

        long listStart = System.nanoTime();
        Lead foundInList = leadList.stream()
                .filter(l -> l.id().equals(targetId))
                .findFirst()
                .orElse(null);
        long listDuration = System.nanoTime() - listStart;

        assertThat(foundInMap).isPresent();
        assertThat(foundInMap.get()).isEqualTo(foundInList);

        assertThat(listDuration)
                .isGreaterThan(mapDuration * 10);

        System.out.println("Map поиск: " + mapDuration + " ns");
        System.out.println("List поиск: " + listDuration + " ns");
        System.out.println("Ускорение: " + (listDuration / mapDuration) + "x");
    }
}