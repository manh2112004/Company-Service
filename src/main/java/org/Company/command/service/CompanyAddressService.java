package org.Company.command.service;

import org.Company.command.model.request.CreateCompanyAddressRequest;
import org.Company.command.model.request.UpdateCompanyAddressRequest;

import java.util.concurrent.CompletableFuture;

public interface CompanyAddressService {

    CompletableFuture<String> addAddress(String userId, String companyId, CreateCompanyAddressRequest request);

    CompletableFuture<String> updateAddress(String userId, String companyId, String addressId, UpdateCompanyAddressRequest request);

    CompletableFuture<String> deleteAddress(String userId, String companyId, String addressId);
}
