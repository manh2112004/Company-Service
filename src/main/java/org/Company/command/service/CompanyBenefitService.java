package org.Company.command.service;

import org.Company.command.model.request.CreateCompanyBenefitRequest;
import java.util.concurrent.CompletableFuture;

public interface CompanyBenefitService {

    CompletableFuture<String> addBenefit(String userId, String companyId, CreateCompanyBenefitRequest request);
}
