package org.Company.command.controller;

import jakarta.validation.Valid;
import org.Company.command.model.request.CreateCompanyRequest;
import org.Company.command.model.request.UpdateCompanyRequest;
import org.Company.command.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyCommandController {
    @Autowired
    private CompanyService companyService;

    @PostMapping
    public CompletableFuture<String> createCompany(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreateCompanyRequest request
    ) {
        return companyService.createCompany(jwt.getSubject(), request);
    }

    @PutMapping("/{companyId}")
    public CompletableFuture<String> updateCompany(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String companyId,
            @RequestBody UpdateCompanyRequest request
    ) {
        return companyService.updateCompany(jwt.getSubject(), companyId, request);
    }

    @DeleteMapping("/{companyId}")
    public CompletableFuture<String> deleteCompany(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String companyId
    ) {
        return companyService.deleteCompany(jwt.getSubject(), companyId);
    }
}
