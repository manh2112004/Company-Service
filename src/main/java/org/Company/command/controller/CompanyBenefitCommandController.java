package org.Company.command.controller;

import jakarta.validation.Valid;
import org.Company.command.model.request.CreateCompanyBenefitRequest;
import org.Company.command.service.CompanyBenefitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyBenefitCommandController {

    @Autowired
    private CompanyBenefitService companyBenefitService;

    @PostMapping("/{companyId}/benefits")
    public CompletableFuture<String> addBenefit(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String companyId,
            @Valid @RequestBody CreateCompanyBenefitRequest request
    ) {
        return companyBenefitService.addBenefit(jwt.getSubject(), companyId, request);
    }
}
