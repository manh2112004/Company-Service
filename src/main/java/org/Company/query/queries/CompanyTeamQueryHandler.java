package org.Company.query.queries;

import org.Company.command.data.CompanyTeam;
import org.Company.command.data.CompanyTeamRepository;
import org.Company.command.data.CompanyRepository;
import org.Company.command.data.CompanyMemberRepository;
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

    @Autowired
    private CompanyMemberRepository companyMemberRepository;

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

    @QueryHandler
    @Transactional(readOnly = true)
    public CompanyTeamListResponse handle(GetCompanySettingsTeamQuery query) {
        if (query.getUserId() == null || query.getUserId().isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        companyRepository.findById(query.getCompanyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại"));

        boolean isMember = companyMemberRepository.existsByCompanyIdAndUserId(query.getCompanyId(), query.getUserId());
        if (!isMember) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập thông tin công ty này");
        }

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
