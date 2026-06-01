package org.Company.command.controller;

import jakarta.validation.Valid;
import org.Company.command.model.request.*;
import org.Company.command.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
    @PutMapping("/{companyId}/approve")
    public CompletableFuture<String> approveCompany(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String companyId
    ) {
        return companyService.approveCompany(jwt, companyId);
    }

    @PutMapping("/{companyId}/reject")
    public CompletableFuture<String> rejectCompany(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String companyId
    ) {
        return companyService.rejectCompany(jwt, companyId);
    }

    @PostMapping(value = "/{companyId}/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CompletableFuture<String> uploadCompanyLogo(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String companyId,
            @RequestParam("file") MultipartFile file
    ) {
        return companyService.uploadCompanyLogo(jwt, companyId, file);
    }

    @DeleteMapping("/{companyId}/logo")
    public CompletableFuture<String> deleteCompanyLogo(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String companyId
    ) {
        return companyService.deleteCompanyLogo(jwt, companyId);
    }

    @PostMapping("/{companyId}/tech-stacks")
    public CompletableFuture<String> addCompanyTechStacks(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String companyId,
            @Valid @RequestBody AddCompanyTechStacksRequest request
    ) {
        return companyService.addCompanyTechStacks(jwt.getSubject(), companyId, request);
    }

    @PutMapping("/{companyId}/tech-stacks")
    public CompletableFuture<String> updateCompanyTechStacks(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String companyId,
            @Valid @RequestBody UpdateCompanyTechStacksRequest request
    ) {
        return companyService.updateCompanyTechStacks(jwt.getSubject(), companyId, request);
    }

    @DeleteMapping("/{companyId}/tech-stacks")
    public CompletableFuture<String> deleteCompanyTechStacks(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String companyId
    ) {
        return companyService.deleteCompanyTechStacks(jwt.getSubject(), companyId);
    }

    @PutMapping("/{companyId}/settings/overview")
    public CompletableFuture<String> updateCompanyOverview(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String companyId,
            @RequestBody UpdateCompanyOverviewRequest request
    ) {
        return companyService.updateCompanyOverview(jwt.getSubject(), companyId, request);
    }
}
