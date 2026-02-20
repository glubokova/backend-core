package ru.mentee.power.crm.storage;

import java.util.Objects;

import ru.mentee.power.crm.domain.Lead;

public class LeadStorage {
    private static final int CAPACITY = 100;
    private Lead[] leads = new Lead[CAPACITY];
    private int size = 0;

    public boolean add(Lead lead) {
        if (size >= CAPACITY) {
            throw new IllegalStateException("Storage is full");
        }

        String email = lead.contact().email();

        for (int i = 0; i < size; i++) {
            String existingEmail = leads[i].contact().email();

            if (email == null && existingEmail == null) {
                return false;
            }

            if (email != null && email.equals(existingEmail)) {
                return false;
            }
        }

        leads[size++] = lead;
        return true;
    }

    public Lead[] findAll() {
        int count = 0;
        for (Lead lead : leads) {
            if (lead != null) {
                count++;
            }
        }

        Lead[] result = new Lead[count];

        int i = 0;
        for (Lead lead : leads) {
            if (lead != null) {
                result[i++] = lead;
            }
        }

        return result;
    }

    public int size() {
        int count = 0;
        for (Lead lead : leads) {
            if (lead != null) {
                count++;
            }
        }

        return count;
    }
}