package org.Company.command.service;

import org.Company.command.model.request.CreateCompanyMemberRequest;
import org.Company.command.model.request.UpdateCompanyMemberRoleRequest;

import java.util.concurrent.CompletableFuture;

public interface CompanyMemberService {

    CompletableFuture<String> addMember(String userId, String companyId, CreateCompanyMemberRequest request);

    CompletableFuture<String> updateMemberRole(String userId, String companyId, String memberId, UpdateCompanyMemberRoleRequest request);

    CompletableFuture<String> deleteMember(String userId, String companyId, String memberId);
}

