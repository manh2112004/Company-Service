package org.Company.query.queries;

import org.Company.command.data.CompanyBenefit;
import org.Company.command.data.CompanyBenefitRepository;
import org.Company.command.data.CompanyRepository;
import org.Company.query.model.response.CompanyBenefitListResponse;
import org.Company.query.model.response.CompanyBenefitResponse;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompanyBenefitQueryHandler {

    @Autowired
    private CompanyBenefitRepository companyBenefitRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @QueryHandler
    @Transactional(readOnly = true)
    public CompanyBenefitListResponse handle(GetCompanyBenefitsQuery query) {
        companyRepository.findById(query.getCompanyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại"));

        List<CompanyBenefitResponse> benefits = companyBenefitRepository.findAllByCompanyId(query.getCompanyId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new CompanyBenefitListResponse(benefits);
    }

    private CompanyBenefitResponse mapToResponse(CompanyBenefit benefit) {
        return CompanyBenefitResponse.builder()
                .id(benefit.getId())
                .companyId(benefit.getCompanyId())
                .benefitName(benefit.getBenefitName())
                .build();
    }
}
