package org.Company.query.queries;

import org.Company.command.data.CompanyTeam;
import org.Company.command.data.CompanyTeamRepository;
import org.Company.command.data.CompanyRepository;
import org.Company.query.model.response.CompanyTeamListResponse;
import org.Company.query.model.response.CompanyTeamResponse;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompanyTeamQueryHandler {

    @Autowired
    private CompanyTeamRepository companyTeamRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @QueryHandler
    @Transactional(readOnly = true)
    public CompanyTeamListResponse handle(GetCompanyTeamsQuery query) {
        companyRepository.findById(query.getCompanyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại"));

        List<CompanyTeamResponse> teams = companyTeamRepository.findAllByCompanyId(query.getCompanyId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new CompanyTeamListResponse(teams);
    }

    private CompanyTeamResponse mapToResponse(CompanyTeam team) {
        return CompanyTeamResponse.builder()
                .id(team.getId())
                .companyId(team.getCompanyId())
                .name(team.getName())
                .position(team.getPosition())
                .avatarUrl(team.getAvatarUrl())
                .linkedinUrl(team.getLinkedinUrl())
                .build();
    }
}
