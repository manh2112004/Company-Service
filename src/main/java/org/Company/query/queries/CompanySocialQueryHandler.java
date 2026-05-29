package org.Company.query.queries;

import org.Company.command.data.CompanySocial;
import org.Company.command.data.CompanySocialRepository;
import org.Company.command.data.CompanyRepository;
import org.Company.query.model.response.CompanySocialListResponse;
import org.Company.query.model.response.CompanySocialResponse;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompanySocialQueryHandler {

    @Autowired
    private CompanySocialRepository companySocialRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @QueryHandler
    @Transactional(readOnly = true)
    public CompanySocialListResponse handle(GetCompanySocialsQuery query) {
        companyRepository.findById(query.getCompanyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại"));

        List<CompanySocialResponse> socials = companySocialRepository.findAllByCompanyId(query.getCompanyId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new CompanySocialListResponse(socials);
    }

    private CompanySocialResponse mapToResponse(CompanySocial social) {
        return CompanySocialResponse.builder()
                .id(social.getId())
                .companyId(social.getCompanyId())
                .platform(social.getPlatform())
                .url(social.getUrl())
                .build();
    }
}
