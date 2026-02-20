package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LeadEqualsHashCodeTest {

    private Lead createLead(UUID id) {
        Address address = new Address("San Francisco", "123 Main St", "94105");
        Contact contact = new Contact("ivan@mail.ru", "+7123", address);

        return new Lead(id, contact, "TechCorp", "NEW");

    }

    @Test
    void shouldBeReflexiveWhenEqualsCalledOnSameObject() {
        UUID id = UUID.randomUUID();
        Lead lead = createLead(id);
        assertThat(lead).isEqualTo(lead);
    }

    @Test
    void shouldBeSymmetricWhenEqualsCalledOnTwoObjects() {
        UUID id = UUID.randomUUID();
        Lead first = createLead(id);
        Lead second = createLead(id);


        assertThat(first).isEqualTo(second);
        assertThat(second).isEqualTo(first);
    }

    @Test
    void shouldBeTransitiveWhenEqualsChainOfThreeObjects() {
        UUID id = UUID.randomUUID();
        Lead first = createLead(id);
        Lead second = createLead(id);
        Lead third = createLead(id);
        assertThat(first).isEqualTo(second);
        assertThat(second).isEqualTo(third);
        assertThat(first).isEqualTo(third);
    }

    @Test
    void shouldBeConsistentWhenEqualsCalledMultipleTimes() {
        UUID id = UUID.randomUUID();
        Lead first = createLead(id);
        Lead second = createLead(id);
        assertThat(first).isEqualTo(second);
        assertThat(first).isEqualTo(second);
        assertThat(first).isEqualTo(second);
    }

    @Test
    void shouldReturnFalseWhenEqualsComparedWithNull() {
        Lead lead = createLead(UUID.randomUUID());
        assertThat(lead).isNotEqualTo(null);
    }

    @Test
    void shouldHaveSameHashCodeWhenObjectsAreEqual() {
        UUID id = UUID.randomUUID();
        Lead first = createLead(id);
        Lead second = createLead(id);
        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    void shouldWorkInHashMapWhenLeadUsedAsKey() {
        UUID id = UUID.randomUUID();
        Lead keyLead = createLead(id);
        Lead lookupLead = createLead(id);
        Map<Lead, String> map = new HashMap<>();
        map.put(keyLead, "CONTACTED");
        String status = map.get(lookupLead);
        assertThat(status).isEqualTo("CONTACTED");
    }

    @Test
    void shouldNotBeEqualWhenIdsAreDifferent() {
        Lead first = createLead(UUID.randomUUID());
        Lead second = createLead(UUID.randomUUID());
        assertThat(first).isNotEqualTo(second);
    }
}