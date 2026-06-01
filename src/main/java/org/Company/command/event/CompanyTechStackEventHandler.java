package org.Company.command.event;

import org.Company.command.data.CompanyTechStack;
import org.Company.command.data.CompanyTechStackRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CompanyTechStackEventHandler {

    @Autowired
    private CompanyTechStackRepository companyTechStackRepository;

    @EventHandler
    @Transactional
    public void on(CompanyTechStackAddedEvent event) {
        CompanyTechStack tech = CompanyTechStack.builder()
                .id(event.getTechStackId())
                .companyId(event.getCompanyId())
                .techStackName(event.getTechStackName())
                .build();
        companyTechStackRepository.save(tech);
    }

    @EventHandler
    @Transactional
    public void on(CompanyTechStackDeletedEvent event) {
        companyTechStackRepository.deleteById(event.getTechStackId());
    }
}
