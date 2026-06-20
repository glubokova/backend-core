## BCORE -16

### Вывод: 
В рамках проекта были реализованы два веб-стека:
Servlet API + Embedded Tomcat
Spring Boot MVC
Интеграционный тест StackComparisonTest выполняет HTTP-запросы к обоим приложениям и сравнивает результаты.
Тест подтверждает, что:
оба приложения отвечают статусом 200 OK;
оба приложения отображают список лидов через JTE-шаблоны;
количество лидов в таблице совпадает;
данные отображаются одинаково независимо от используемого веб-стека.
Trade-offs

### Servlet стек
Преимущества:
полный контроль над конфигурацией;
меньше скрытой магии;
быстрое понимание работы HTTP и сервлетов.

Недостатки:
больше шаблонного кода;
ручное управление зависимостями;
ручная регистрация сервлетов и компонентов.

### Spring Boot
Преимущества:
Dependency Injection;
Auto Configuration;
меньше инфраструктурного кода;
высокая скорость разработки.

Недостатки:
более длительный старт приложения;
дополнительный уровень абстракции;
требуется понимание IoC Container.


# BCORE-10  
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

