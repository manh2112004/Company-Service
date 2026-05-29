package org.Company.command.controller;

import jakarta.validation.Valid;
import org.Company.command.model.request.CreateCompanyAddressRequest;
import org.Company.command.model.request.UpdateCompanyAddressRequest;
import org.Company.command.service.CompanyAddressService;
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
public class CompanyAddressCommandController {

    @Autowired
    private CompanyAddressService companyAddressService;

    @PostMapping("/{companyId}/addresses")
    public CompletableFuture<String> addAddress(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String companyId,
            @Valid @RequestBody CreateCompanyAddressRequest request
    ) {
        return companyAddressService.addAddress(jwt.getSubject(), companyId, request);
    }

    @PutMapping("/{companyId}/addresses/{addressId}")
    public CompletableFuture<String> updateAddress(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String companyId,
            @PathVariable String addressId,
            @Valid @RequestBody UpdateCompanyAddressRequest request
    ) {
        return companyAddressService.updateAddress(jwt.getSubject(), companyId, addressId, request);
    }
}

