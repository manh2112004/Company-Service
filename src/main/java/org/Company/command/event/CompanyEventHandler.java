package org.Company.command.event;

import org.Company.command.data.Company;
import org.Company.command.data.CompanyMember;
import org.Company.command.data.CompanyMemberRepository;
import org.Company.command.data.CompanyRepository;
import org.Company.constant.CompanyMemberRole;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class CompanyEventHandler {
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyMemberRepository companyMemberRepository;

    @EventHandler
    @Transactional
    public void on(CompanyCreatedEvent event) {
        LocalDateTime now = LocalDateTime.now();
        Company company = Company.builder()
                .id(event.getId())
                .companyName(event.getCompanyName())
                .logoUrl(event.getLogoUrl())
                .description(event.getDescription())
                .website(event.getWebsite())
                .industry(event.getIndustry())
                .companySize(event.getCompanySize())
                .foundedYear(event.getFoundedYear())
                .email(event.getEmail())
                .phoneNumber(event.getPhoneNumber())
                .taxCode(event.getTaxCode())
                .status(event.getStatus())
                .verified(event.getVerified())
                .createdAt(now)
                .updatedAt(now)
                .build();
        companyRepository.save(company);

        if (!companyMemberRepository.existsByCompanyIdAndUserId(event.getId(), event.getOwnerUserId())) {
            CompanyMember owner = CompanyMember.builder()
                    .id(UUID.randomUUID().toString())
                    .companyId(event.getId())
                    .userId(event.getOwnerUserId())
                    .role(CompanyMemberRole.OWNER)
                    .active(true)
                    .build();
            companyMemberRepository.save(owner);
        }
    }
}
