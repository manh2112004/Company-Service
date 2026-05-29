package org.Company.command.service;

import org.Company.command.model.request.CreateCompanyMemberRequest;
import java.util.concurrent.CompletableFuture;

public interface CompanyMemberService {

    CompletableFuture<String> addMember(String userId, String companyId, CreateCompanyMemberRequest request);
}
