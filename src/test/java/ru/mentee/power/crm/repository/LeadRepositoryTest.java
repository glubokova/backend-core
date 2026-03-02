package ru.mentee.power.crm.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.Lead;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class LeadRepositoryTest {
    private LeadRepository leadRepository;

    @BeforeEach
    void setUp() {
        leadRepository = new LeadRepository();
    }

    @Test
    void shouldSaveAndFindLeadByIdWhenLeadSaved() {
        UUID id = UUID.randomUUID();
        Contact contact = new Contact("john@example@com", "+789", new Address("Moscow", "Lenina", "123456")
        );

        Lead lead = new Lead(id, contact, "T", "NEW");

        leadRepository.save(lead);
        Lead found = leadRepository.findById(id);

        assertThat(found).isNotNull();
        assertThat(found).isEqualTo(lead);
        assertThat(leadRepository.size()).isEqualTo(1);
    }

    @Test
    void shouldReturnNullWhenLeadNotFound() {
        UUID unknownId = UUID.randomUUID();
        Lead found = leadRepository.findById(unknownId);
        assertThat(found).isNull();
    }

    @Test
    void shouldReturnAllLeadsWhenMultipleLeadsSaved() {
        for (int i = 0; i < 3; i++) {
            UUID id = UUID.randomUUID();
            Contact contact = new Contact(
                    "user" + i + "@mail.ru",
                    "+789" + i,
                    new Address("City" + i, "Street" + i, "ZIP" + 1)
            );
            Lead lead = new Lead(id, contact, "Company" + i, "NEW");
            leadRepository.save(lead);
        }

        List<Lead> allLeads = leadRepository.findAll();

        assertThat(allLeads).hasSize(3);
    }

    @Test
    void shouldDeleteLeadWhenLeadExists() {
        UUID id = UUID.randomUUID();
        Contact contact = new Contact("@mail.ru", "+789", new Address("City", "Sol", "100010")
        );
        Lead lead = new Lead(id, contact, "TechCorp", "NEW");
        leadRepository.save(lead);
        leadRepository.delete(id);

        assertThat(leadRepository.findById(id)).isNull();
        assertThat(leadRepository.size()).isEqualTo(0);
    }

    @Test
    void shouldOverwriteLeadWhenSaveWithSameId() {
        UUID id = UUID.randomUUID();
        Contact firstContact = new Contact(
                "first@mail.com",
                "+789",
                new Address("City1", "Street1", "ZIP1")
        );
        Lead firstLead = new Lead(id, firstContact, "FirstCompany", "NEW");

        Contact secondContact = new Contact(
                "second@mail.com",
                "+789",
                new Address("City2", "Street2", "ZIP2")
        );
        Lead secondLead = new Lead(id, secondContact, "SecondCompany", "QUALIFIED");

        leadRepository.save(firstLead);
        leadRepository.save(secondLead);

        Lead found = leadRepository.findById(id);
        assertThat(found).isEqualTo(secondLead);
        assertThat(leadRepository.size()).isEqualTo(1);
    }

    @Test
    void shouldSaveBothLeadsEvenWithSameEmailBecauseRepositoryUsesUuidAsKey() {
        Contact contact1 = new Contact(
                "ivan@mail.com",
                "+79990000000",
                new Address("Moscow", "Lenina 1", "101000")
        );

        Contact contact2 = new Contact(
                "ivan@mail.com",
                "+79990000000",
                new Address("Moscow", "Lenina 1", "101000")
        );

        Lead lead1 = new Lead(UUID.randomUUID(), contact1, "CompanyA", "NEW");
        Lead lead2 = new Lead(UUID.randomUUID(), contact2, "CompanyB", "CONVERTED");

        leadRepository.save(lead1);
        leadRepository.save(lead2);

        assertThat(leadRepository.size()).isEqualTo(2);
    }

    @Test
    void shouldDemonstrateMapIsFasterThanList() {
        List<Lead> leadList = new ArrayList<>();

        for (int i = 0; i < 10_000; i++) {
            UUID id = UUID.randomUUID();

            Contact contact = new Contact(
                    "email" + i + "@test.com",
                    "+7" + i,
                    new Address("City" + i, "Street" + i, "ZIP" + i)
            );

            Lead lead = new Lead(id, contact, "Company" + i, "NEW");

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
        Lead foundInMap = leadRepository.findById(targetId);
        long mapDuration = System.nanoTime() - mapStart;

        long listStart = System.nanoTime();
        Lead foundInList = leadList.stream()
                .filter(l -> l.id().equals(targetId))
                .findFirst()
                .orElse(null);
        long listDuration = System.nanoTime() - listStart;

        assertThat(foundInMap).isNotNull();
        assertThat(foundInMap).isEqualTo(foundInList);

        assertThat(listDuration)
                .isGreaterThan(mapDuration * 10);

        System.out.println("Map поиск: " + mapDuration + " ns");
        System.out.println("List поиск: " + listDuration + " ns");
        System.out.println("Ускорение: " + (listDuration / mapDuration) + "x");
    }

    @Test
    void shouldSaveBothLeadsEvenWithSameEmailAndPhoneBecauseRepositoryDoesNotCheckBusinessRules() {
        Contact sharedContact = new Contact("ivan@mail.ru", "+79001234567",
                new Address("Moscow", "Tverskaya 1", "101000"));
        Lead originalLead = new Lead(UUID.randomUUID(), sharedContact, "Acme Corp", "NEW");
        Lead duplicateLead = new Lead(UUID.randomUUID(), sharedContact, "TechCorp", "QUALIFIED");
        leadRepository.save(originalLead);
        leadRepository.save(duplicateLead);

        assertThat(leadRepository.size()).isEqualTo(2);
    }
}