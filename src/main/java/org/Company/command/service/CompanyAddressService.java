package org.Company.command.service;

import org.Company.command.model.request.CreateCompanyAddressRequest;

import java.util.concurrent.CompletableFuture;

public interface CompanyAddressService {

    CompletableFuture<String> addAddress(String userId, String companyId, CreateCompanyAddressRequest request);
}
