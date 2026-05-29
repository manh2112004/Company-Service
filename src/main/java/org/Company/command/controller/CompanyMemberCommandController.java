package org.Company.command.controller;

import jakarta.validation.Valid;
import org.Company.command.model.request.CreateCompanyMemberRequest;
import org.Company.command.model.request.UpdateCompanyMemberRoleRequest;
import org.Company.command.service.CompanyMemberService;
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
public class CompanyMemberCommandController {

    @Autowired
    private CompanyMemberService companyMemberService;

    @PostMapping("/{companyId}/members")
    public CompletableFuture<String> addMember(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String companyId,
            @Valid @RequestBody CreateCompanyMemberRequest request
    ) {
        return companyMemberService.addMember(jwt.getSubject(), companyId, request);
    }

    @PutMapping("/{companyId}/members/{memberId}/role")
    public CompletableFuture<String> updateMemberRole(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String companyId,
            @PathVariable String memberId,
            @Valid @RequestBody UpdateCompanyMemberRoleRequest request
    ) {
        return companyMemberService.updateMemberRole(jwt.getSubject(), companyId, memberId, request);
    }

    @DeleteMapping("/{companyId}/members/{memberId}")
    public CompletableFuture<String> deleteMember(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String companyId,
            @PathVariable String memberId
    ) {
        return companyMemberService.deleteMember(jwt.getSubject(), companyId, memberId);
    }
}

