package org.Company.query.queries;

import org.Company.command.data.CompanyMember;
import org.Company.command.data.CompanyMemberRepository;
import org.Company.command.data.CompanyRepository;
import org.Company.query.model.response.CompanyMemberListResponse;
import org.Company.query.model.response.CompanyMemberResponse;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompanyMemberQueryHandler {

    @Autowired
    private CompanyMemberRepository companyMemberRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @QueryHandler
    @Transactional(readOnly = true)
    public CompanyMemberListResponse handle(GetCompanyMembersQuery query) {
        companyRepository.findById(query.getCompanyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại"));

        List<CompanyMemberResponse> members = companyMemberRepository.findAllByCompanyId(query.getCompanyId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new CompanyMemberListResponse(members);
    }

    private CompanyMemberResponse mapToResponse(CompanyMember member) {
        return CompanyMemberResponse.builder()
                .id(member.getId())
                .companyId(member.getCompanyId())
                .userId(member.getUserId())
                .role(member.getRole())
                .active(member.getActive())
                .build();
    }
}
