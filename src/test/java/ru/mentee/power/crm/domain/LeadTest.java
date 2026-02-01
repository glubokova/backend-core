package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LeadTest {

    @Test
    void shouldReturnIdWhenGetIdCalled() {
        Lead lead = new Lead("L1", "test@example.com", "+71234567890", "TestCompany", "NEW");
        String id = lead.getId();
        assertThat(id).isEqualTo("L1");
    }

    @Test
    void shouldReturnEmailWhenGetEmailCalled() {
        Lead lead = new Lead("L1", "test@example.com", "+71234567890", "TestCompany", "NEW");
        String email = lead.getEmail();
        assertThat(email).isEqualTo("test@example.com");
    }

    @Test
    void shouldReturnPhoneWhenGetPhoneCalled() {
        Lead lead = new Lead("L1", "test@example.com", "+71234567890", "TestCompany", "NEW");
        String phone = lead.getPhone();
        assertThat(phone).isEqualTo("+71234567890");
    }

    @Test
    void shouldReturnCompanyWhenGetCompanyCalled() {
        Lead lead = new Lead("L1", "test@example.com", "+71234567890", "TestCompany", "NEW");
        String company = lead.getCompany();
        assertThat(company).isEqualTo("TestCompany");
    }

    @Test
    void shouldReturnStatusWhenGetStatusCalled() {
        Lead lead = new Lead("L1", "test@example.com", "+71234567890", "TestCompany", "NEW");
        String status = lead.getStatus();
        assertThat(status).isEqualTo("NEW");
    }

    @Test
    void shouldReturnFormattedStringWhenToStringCalled() {
        Lead lead = new Lead("L1", "test@example.com", "+71234567890", "TestCompany", "NEW");
        String result = lead.toString();
        assertThat(result).contains("L1", "TestCompany", "NEW");
    }

}