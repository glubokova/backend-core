package ru.mentee.power.crm.repository;

import ru.mentee.power.crm.domain.Lead;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class InMemoryLeadRepository implements Repository<Lead> {

    private final List<Lead> storage = new ArrayList<>();

    @Override
    public boolean add(Lead lead) {
        if(storage.contains(lead)) {
            return false;
        }
        storage.add(lead);
        return true;
    }

    @Override
    public boolean remove(UUID id) {
        return storage.removeIf(lead -> lead.id().equals(id));
    }

    @Override
    public Optional<Lead> findById(UUID id) {
        return storage.stream()
                .filter(lead -> lead.id().equals(id))
                .findFirst();
    }

    @Override
    public List<Lead> findAll() {
        return new ArrayList<>(storage);
    }
}