package org.Company.command.service;

import org.Company.command.model.request.CreateCompanyRequest;
import org.Company.command.model.request.UpdateCompanyRequest;
import org.Company.command.model.request.AddCompanyTechStacksRequest;
import org.Company.command.model.request.UpdateCompanyTechStacksRequest;
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

    CompletableFuture<String> deleteCompanyLogo(Jwt jwt, String companyId);

    CompletableFuture<String> addCompanyTechStacks(String userId, String companyId, AddCompanyTechStacksRequest request);

    CompletableFuture<String> updateCompanyTechStacks(String userId, String companyId, UpdateCompanyTechStacksRequest request);

    CompletableFuture<String> deleteCompanyTechStacks(String userId, String companyId);

    CompletableFuture<String> deleteCompanyTechStack(String userId, String companyId, String techStackId);

    CompletableFuture<String> updateCompanyOverview(String userId, String companyId, org.Company.command.model.request.UpdateCompanyOverviewRequest request);
}
