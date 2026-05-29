package org.Company.command.controller;

import jakarta.validation.Valid;
import org.Company.command.model.request.CreateCompanySocialRequest;
import org.Company.command.model.request.UpdateCompanySocialRequest;
import org.Company.command.service.CompanySocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanySocialCommandController {

    @Autowired
    private CompanySocialService companySocialService;

    @PostMapping("/{companyId}/socials")
    public CompletableFuture<String> addSocial(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String companyId,
            @Valid @RequestBody CreateCompanySocialRequest request
    ) {
        return companySocialService.addSocial(jwt.getSubject(), companyId, request);
    }

    @PutMapping("/{companyId}/socials/{socialId}")
    public CompletableFuture<String> updateSocial(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String companyId,
            @PathVariable String socialId,
            @Valid @RequestBody UpdateCompanySocialRequest request
    ) {
        return companySocialService.updateSocial(jwt.getSubject(), companyId, socialId, request);
    }
}
