package ru.mentee.power.crm.domain;

import java.util.Set;
import java.util.UUID;

public record Lead(
        UUID id,
        Contact contact,
        String company,
        String status
) {
    private static final Set<String> ALLOWED_STATUSES =
            Set.of("NEW", "QUALIFIED", "CONVERTED");

    public Lead {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be not null");
        }
        if (contact == null) {
            throw new IllegalArgumentException("Contact must not be null");
        }
        if (status == null || !ALLOWED_STATUSES.contains(status)) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Lead other)) {
            return false;
        }
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}