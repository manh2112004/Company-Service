package org.Company.command.service;

import org.Company.command.model.request.CreateCompanyRequest;

import java.util.concurrent.CompletableFuture;

public interface CompanyService {
    CompletableFuture<String> createCompany(String userId, CreateCompanyRequest request);
}
