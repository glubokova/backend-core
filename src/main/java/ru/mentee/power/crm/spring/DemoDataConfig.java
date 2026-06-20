package ru.mentee.power.crm.spring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.service.LeadService;

@Configuration
public class DemoDataConfig {

    @Bean
    CommandLineRunner loadData(LeadService leadService) {
        return args -> {

            System.out.println("DEMO DATA START");

            leadService.addLead(
                    "andrey@exs.com",
                    "WILD",
                    LeadStatus.NEW);

            leadService.addLead(
                    "katy@exs.com",
                    "OZON",
                    LeadStatus.CONTACTED);

            leadService.addLead(
                    "alex@exs.com",
                    "VK",
                    LeadStatus.QUALIFIED);

            leadService.addLead(
                    "sasha@exs.com",
                    "NETFLIX",
                    LeadStatus.CONTACTED);

            leadService.addLead(
                    "pol.com",
                    "VINNY",
                    LeadStatus.NEW);

            System.out.println(
                    "DEMO DATA COUNT = "
                            + leadService.findAll().size()
            );
        };
    }
}