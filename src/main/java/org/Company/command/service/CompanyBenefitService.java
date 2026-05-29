package org.Company.command.service;

import org.Company.command.model.request.CreateCompanyBenefitRequest;
import org.Company.command.model.request.UpdateCompanyBenefitRequest;
import java.util.concurrent.CompletableFuture;

public interface CompanyBenefitService {

    CompletableFuture<String> addBenefit(String userId, String companyId, CreateCompanyBenefitRequest request);

    CompletableFuture<String> updateBenefit(String userId, String companyId, String benefitId, UpdateCompanyBenefitRequest request);
}
