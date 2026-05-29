package org.Company.command.event;

import org.Company.command.data.CompanyBenefit;
import org.Company.command.data.CompanyBenefitRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CompanyBenefitEventHandler {

    @Autowired
    private CompanyBenefitRepository companyBenefitRepository;

    @EventHandler
    @Transactional
    public void on(CompanyBenefitAddedEvent event) {
        CompanyBenefit benefit = CompanyBenefit.builder()
                .id(event.getBenefitId())
                .companyId(event.getCompanyId())
                .benefitName(event.getBenefitName())
                .build();
        companyBenefitRepository.save(benefit);
    }
}
