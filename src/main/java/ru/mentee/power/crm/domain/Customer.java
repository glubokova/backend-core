package ru.mentee.power.crm.domain;

import java.util.Set;
import java.util.UUID;

public record Customer(
        UUID id,
        Contact contact,
        Address billingAddress,
        String loyaltyTier
) {

    private static final Set<String> ALLOWED_TIERS =
            Set.of("BRONZE", "SILVER", "GOLD");

    public Customer {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        if (contact == null) {
            throw new IllegalArgumentException("Contact must not be null");
        }
        if (billingAddress == null) {
            throw new IllegalArgumentException("BillingAddress must not be null");
        }
        if (loyaltyTier == null || !ALLOWED_TIERS.contains(loyaltyTier)) {
            throw new IllegalArgumentException("Invalid loyalty tier");
        }
    }
}