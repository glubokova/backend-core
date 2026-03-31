BCORE-10  
Этот документ сравнивает два подхода к управлению зависимостями в Java:
- **BAD**: Создание зависимостей внутри класса (`new InMemoryLeadRepository()`)
- **GOOD**: Dependency Injection через конструктор (передача зависимостей извне)

---

## BAD: Создание зависимости внутри класса

### Код:
```java
public class LeadService {
    Тесная связанность! LeadService сам создает свою зависимость
    private final LeadRepository repository = new InMemoryLeadRepository();
    
    public Lead addLead(String email, String company, LeadStatus status) {
        Бизнес-логика проверки дубликатов
        Optional<Lead> existing = repository.findByEmail(email);
        if (existing.isPresent()) {
            throw new IllegalStateException("Lead with email already exists: " + email);
        }
        
        Lead lead = new Lead(UUID.randomUUID(), email, company, status);
        return repository.save(lead);
    }
    
    public List<Lead> findAll() {
        return repository.findAll();
    }
}