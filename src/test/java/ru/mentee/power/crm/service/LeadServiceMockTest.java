package ru.mentee.power.crm.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.LeadRepository;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.inOrder;


@ExtendWith(MockitoExtension.class)
class LeadServiceMockTest {

    @Mock
    private LeadRepository mockRepository;
    private LeadService service;

    @BeforeEach
    void setUp() {
        service = new LeadService(mockRepository);
    }

    @Test
    void shouldCallRepositorySaveWhenAddingNewLead() {
        when(mockRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        when(mockRepository.save(any(Lead.class)))
                .thenAnswer((Answer<Lead>) invocation -> invocation.getArgument(0));

        Lead result = service.addLead(
                "new@example.com",
                "Company",
                LeadStatus.NEW
        );

        verify(mockRepository, times(1)).save(any(Lead.class));

        assertThat(result.email()).isEqualTo("new@example.com");
        assertThat(result.company()).isEqualTo("Company");
        assertThat(result.status()).isEqualTo(LeadStatus.NEW);
        assertThat(result.id()).isNotNull();
    }

    @Test
    void shouldNotCallSaveWhenEmailExisting() {
        Lead existingLead = new Lead(
                UUID.randomUUID(),
                "existing@example.com",
                "Existing company",
                LeadStatus.CONTACTED
        );
        when(mockRepository.findByEmail("existing@example.com"))
                .thenReturn(Optional.of(existingLead));

        assertThatThrownBy(() ->
                service.addLead("existing@example.com", "New Company", LeadStatus.NEW)
        ).isInstanceOf(IllegalStateException.class);

        verify(mockRepository, never()).save(any(Lead.class));
    }

    @Test
    void shouldCallFindByEmailBeforeSave() {
        when(mockRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        Lead expectedLead = new Lead(
                UUID.randomUUID(),
                "test@example.com",
                "Company",
                LeadStatus.NEW
        );

        when(mockRepository.save(any(Lead.class)))
                .thenReturn(expectedLead);

        service.addLead("test@example.com", "Company",LeadStatus.NEW);

        InOrder inOrder = inOrder(mockRepository);
        inOrder.verify(mockRepository).findByEmail("test@example.com");
        inOrder.verify(mockRepository).save(any(Lead.class));
    }
}