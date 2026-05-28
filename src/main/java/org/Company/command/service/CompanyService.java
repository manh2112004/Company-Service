package org.Company.command.service;

import org.Company.command.model.request.CreateCompanyRequest;
import org.Company.command.model.request.UpdateCompanyRequest;

import java.util.concurrent.CompletableFuture;

public interface CompanyService {
    CompletableFuture<String> createCompany(String userId, CreateCompanyRequest request);

    CompletableFuture<String> updateCompany(String userId, String companyId, UpdateCompanyRequest request);

    CompletableFuture<String> deleteCompany(String userId, String companyId);
}
