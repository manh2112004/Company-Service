package org.Company.command.event;

import org.Company.command.data.Company;
import org.Company.command.data.CompanyMember;
import org.Company.command.data.CompanyMemberRepository;
import org.Company.command.data.CompanyRepository;
import org.Company.constant.CompanyMemberRole;
import org.Company.constant.CompanyStatus;
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
                .techStacks(event.getTechStacks())
                .openPositionsCount(event.getOpenPositionsCount())
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

    @EventHandler
    @Transactional
    public void on(CompanyUpdatedEvent event) {
        Company company = companyRepository.findById(event.getId()).orElse(null);
        if (company == null) {
            return;
        }

        if (event.getCompanyName() != null) {
            company.setCompanyName(event.getCompanyName());
        }
        if (event.getLogoUrl() != null) {
            company.setLogoUrl(event.getLogoUrl());
        }
        if (event.getDescription() != null) {
            company.setDescription(event.getDescription());
        }
        if (event.getWebsite() != null) {
            company.setWebsite(event.getWebsite());
        }
        if (event.getIndustry() != null) {
            company.setIndustry(event.getIndustry());
        }
        if (event.getCompanySize() != null) {
            company.setCompanySize(event.getCompanySize());
        }
        if (event.getFoundedYear() != null) {
            company.setFoundedYear(event.getFoundedYear());
        }
        if (event.getEmail() != null) {
            company.setEmail(event.getEmail());
        }
        if (event.getPhoneNumber() != null) {
            company.setPhoneNumber(event.getPhoneNumber());
        }
        if (event.getTaxCode() != null) {
            company.setTaxCode(event.getTaxCode());
        }
        if (event.getTechStacks() != null) {
            company.setTechStacks(event.getTechStacks());
        }
        if (event.getOpenPositionsCount() != null) {
            company.setOpenPositionsCount(event.getOpenPositionsCount());
        }

        company.setUpdatedAt(LocalDateTime.now());
        companyRepository.save(company);
    }

    @EventHandler
    @Transactional
    public void on(CompanyDeletedEvent event) {
        Company company = companyRepository.findById(event.getId()).orElse(null);
        if (company == null) {
            return;
        }

        company.setStatus(CompanyStatus.SUSPENDED);
        company.setUpdatedAt(LocalDateTime.now());
        companyRepository.save(company);
    }

    @EventHandler
    @Transactional
    public void on(CompanyApprovedEvent event) {
        Company company = companyRepository.findById(event.getId()).orElse(null);
        if (company == null) {
            return;
        }

        company.setStatus(CompanyStatus.ACTIVE);
        company.setVerified(true);
        company.setUpdatedAt(LocalDateTime.now());
        companyRepository.save(company);
    }

    @EventHandler
    @Transactional
    public void on(CompanyRejectedEvent event) {
        Company company = companyRepository.findById(event.getId()).orElse(null);
        if (company == null) {
            return;
        }

        company.setStatus(CompanyStatus.REJECTED);
        company.setVerified(false);
        company.setUpdatedAt(LocalDateTime.now());
        companyRepository.save(company);
    }
}
