package org.Company.command.controller;

import jakarta.validation.Valid;
import org.Company.command.model.request.CreateCompanyTeamRequest;
import org.Company.command.model.request.UpdateCompanyTeamRequest;
import org.Company.command.service.CompanyTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyTeamCommandController {

    @Autowired
    private CompanyTeamService companyTeamService;

    @PostMapping("/{companyId}/teams")
    public CompletableFuture<String> addTeamMember(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String companyId,
            @Valid @RequestBody CreateCompanyTeamRequest request
    ) {
        return companyTeamService.addTeamMember(jwt.getSubject(), companyId, request);
    }

    @PutMapping("/{companyId}/teams/{teamId}")
    public CompletableFuture<String> updateTeamMember(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String companyId,
            @PathVariable String teamId,
            @Valid @RequestBody UpdateCompanyTeamRequest request
    ) {
        return companyTeamService.updateTeamMember(jwt.getSubject(), companyId, teamId, request);
    }

    @DeleteMapping("/{companyId}/teams/{teamId}")
    public CompletableFuture<String> deleteTeamMember(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String companyId,
            @PathVariable String teamId
    ) {
        return companyTeamService.deleteTeamMember(jwt.getSubject(), companyId, teamId);
    }
}
