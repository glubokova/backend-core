package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class LeadEqualsHashCodeTest {

    @Test
    void shouldBeReflexiveWhenEqualsCalledOnSameObject() {
        UUID id = UUID.randomUUID();
        Lead lead = new Lead(id, "ivan@mail.ru", "+7123", "TechCorp", "NEW");
        assertThat(lead).isEqualTo(lead);
    }

    @Test
    void shouldBeSymmetricWhenEqualsCalledOnTwoObjects() {
        UUID id = UUID.randomUUID();
        Lead firstLead = new Lead(id, "ivan@mail.ru", "+7123", "TechCorp", "NEW");
        Lead secondLead = new Lead(id, "ivan@mail.ru", "+7123", "TechCorp", "NEW");
        assertThat(firstLead).isEqualTo(secondLead);
        assertThat(secondLead).isEqualTo(firstLead);
    }

    @Test
    void shouldBeTransitiveWhenEqualsChainOfThreeObjects() {
        UUID id = UUID.randomUUID();
        Lead firstLead = new Lead(id, "ivan@mail.ru", "+7123", "TechCorp", "NEW");
        Lead secondLead = new Lead(id, "ivan@mail.ru", "+7123", "TechCorp", "NEW");
        Lead thirdLead = new Lead(id, "ivan@mail.ru", "+7123", "TechCorp", "NEW");
        assertThat(firstLead).isEqualTo(secondLead);
        assertThat(secondLead).isEqualTo(thirdLead);
        assertThat(firstLead).isEqualTo(thirdLead);
    }

    @Test
    void shouldBeConsistentWhenEqualsCalledMultipleTimes() {
        UUID id = UUID.randomUUID();
        Lead firstLead = new Lead(id, "ivan@mail.ru", "+7123", "TechCorp", "NEW");
        Lead secondLead = new Lead(id, "ivan@mail.ru", "+7123", "TechCorp", "NEW");
        assertThat(firstLead).isEqualTo(secondLead);
        assertThat(firstLead).isEqualTo(secondLead);
        assertThat(firstLead).isEqualTo(secondLead);
    }

    @Test
    void shouldReturnFalseWhenEqualsComparedWithNull() {
        UUID id = UUID.randomUUID();
        Lead lead = new Lead(id, "ivan@mail.ru", "+7123", "TechCorp", "NEW");
        assertThat(lead).isNotEqualTo(null);
    }

    @Test
    void shouldHaveSameHashCodeWhenObjectsAreEqual() {
        UUID id = UUID.randomUUID();
        Lead firstLead = new Lead(id, "ivan@mail.ru", "+7123", "TechCorp", "NEW");
        Lead secondLead = new Lead(id, "ivan@mail.ru", "+7123", "TechCorp", "NEW");
        assertThat(firstLead).isEqualTo(secondLead);
        assertThat(firstLead.hashCode()).isEqualTo(secondLead.hashCode());
    }

    @Test
    void shouldWorkInHashMapWhenLeadUsedAsKey() {
        UUID id = UUID.randomUUID();
        Lead keyLead = new Lead(id, "ivan@mail.ru", "+7123", "TechCorp", "NEW");
        Lead lookupLead = new Lead(id, "ivan@mail.ru", "+7123", "TechCorp", "NEW");
        Map<Lead, String> map = new HashMap<>();
        map.put(keyLead, "CONTACTED");
        String status = map.get(lookupLead);
        assertThat(status).isEqualTo("CONTACTED");
    }

    @Test
    void shouldNotBeEqualWhenIdsAreDifferent() {
        Lead firstLead = new Lead(UUID.randomUUID(), "ivan@mail.ru", "+7123", "TechCorp", "NEW");
        Lead differentLead = new Lead(UUID.randomUUID(), "ivan@mail.ru", "+7123", "TechCorp", "NEW");
        assertThat(firstLead).isNotEqualTo(differentLead);
    }
}
