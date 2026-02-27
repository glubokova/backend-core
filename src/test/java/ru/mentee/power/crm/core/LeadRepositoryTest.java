package ru.mentee.power.crm.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.Lead;

import java.util.Set;
import java.util.HashSet;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class LeadRepositoryTest {

    @Test
    @DisplayName("Should automatically deduplicate leads by id")
    void shouldDeduplicateLeadsById() {
        LeadRepository leadRepository = new LeadRepository();
        Address address = new Address("Moscow", "Lenina", "01234");
        Contact contact = new Contact("john@exsample.com", "+7901234567", address);
        UUID id = UUID.randomUUID();
        Lead lead = new Lead(id, contact, "TechCorp", "NEW");

        boolean firstAdd = leadRepository.add(lead);
        boolean secondAdd = leadRepository.add(lead);

        assertThat(firstAdd).isTrue();
        assertThat(secondAdd).isFalse();
        assertThat(leadRepository.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should allow different leads with different ids")
    void shouldAllowDifferentLeads() {
        LeadRepository leadRepository = new LeadRepository();

        Address address1 = new Address("Moscow", "Lenina", "012345");
        Contact contact1 = new Contact("john@exsample.com", "+7901234567", address1);
        Lead first = new Lead(UUID.randomUUID(), contact1, "TechCorp", "NEW");

        Address address2 = new Address("SPB", "Nevsky", "190000");
        Contact contact2 = new Contact("mary@example.com", "+79997654321", address2);
        Lead second = new Lead(UUID.randomUUID(), contact2, "Startup", "NEW");

        boolean firstAdd = leadRepository.add(first);
        boolean secondAdd = leadRepository.add(second);

        assertThat(firstAdd).isTrue();
        assertThat(secondAdd).isTrue();
        assertThat(leadRepository.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should find existing lead through contains")
    void shouldFindExistingLead() {
        LeadRepository leadRepository = new LeadRepository();
        Address address = new Address("Moscow", "Lenina", "012345");
        Contact contact = new Contact("john@exsample.com", "+7901234567", address);
        Lead lead = new Lead(UUID.randomUUID(), contact, "TechCorp", "NEW");
        leadRepository.add(lead);

        boolean exists = leadRepository.contains(lead);
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should return unmodifiable set from findAll")
    void shouldReturnUnmodifiableSet() {
        LeadRepository leadRepository = new LeadRepository();
        Address address = new Address("Moscow", "Lenina", "012345");
        Contact contact = new Contact("john@exsample.com", "+7901234567", address);
        Lead lead = new Lead(UUID.randomUUID(), contact, "TechCorp", "NEW");
        leadRepository.add(lead);

        Set<Lead> result = leadRepository.findAll();

        assertThatThrownBy(() -> result.add(lead))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("Should perform contains() faster than ArrayList")
    void shouldPerformFasterThanArrayList() {
        int size = 10000;

        Address address = new Address("Moscow", "Lenina", "012345");
        Contact contact = new Contact("john@exsample.com", "+7901234567", address);

        Set<Lead> hashSet = new HashSet<>();
        List<Lead> arrayList = new ArrayList<>();

        Lead target = new Lead(UUID.randomUUID(), contact, "TechCorp", "NEW");


        for (int i = 0; i < size; i++) {
            Lead lead = new Lead(UUID.randomUUID(), contact, "TechCorp", "NEW");
            hashSet.add(lead);
            arrayList.add(lead);
        }

        hashSet.add(target);
        arrayList.add(target);

        long startSet = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            hashSet.contains(target);
        }
        long durationSet = System.nanoTime() - startSet;

        long startList = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            arrayList.contains(target);
        }
        long durationList = System.nanoTime() - startList;

        assertThat(durationList).isGreaterThan(durationSet * 100);
    }
}