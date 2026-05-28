package org.Company.command.service;

import org.Company.command.model.request.CreateCompanyRequest;
import org.Company.command.model.request.UpdateCompanyRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

public interface CompanyService {
    CompletableFuture<String> createCompany(String userId, CreateCompanyRequest request);

    CompletableFuture<String> updateCompany(String userId, String companyId, UpdateCompanyRequest request);

    CompletableFuture<String> deleteCompany(String userId, String companyId);

    CompletableFuture<String> approveCompany(Jwt jwt, String companyId);

    CompletableFuture<String> rejectCompany(Jwt jwt, String companyId);

    CompletableFuture<String> uploadCompanyLogo(Jwt jwt, String companyId, MultipartFile file);
}
