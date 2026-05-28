package org.Company.query.queries;

import org.Company.command.data.Company;
import org.Company.command.data.CompanyRepository;
import org.Company.constant.CompanyStatus;
import org.Company.query.model.response.CompanyResponse;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Component
public class CompanyQueryHandler {
    @Autowired
    private CompanyRepository companyRepository;

    @QueryHandler
    @Transactional(readOnly = true)
    public CompanyResponse handle(GetCompanyByIdQuery query) {
        Company company = companyRepository.findById(query.getCompanyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại"));

        if (company.getStatus() == CompanyStatus.SUSPENDED) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại");
        }

        return CompanyResponse.builder()
                .id(company.getId())
                .companyName(company.getCompanyName())
                .logoUrl(company.getLogoUrl())
                .description(company.getDescription())
                .website(company.getWebsite())
                .industry(company.getIndustry())
                .companySize(company.getCompanySize())
                .foundedYear(company.getFoundedYear())
                .email(company.getEmail())
                .phoneNumber(company.getPhoneNumber())
                .taxCode(company.getTaxCode())
                .status(company.getStatus())
                .verified(company.getVerified())
                .createdAt(company.getCreatedAt())
                .updatedAt(company.getUpdatedAt())
                .build();
    }
}
