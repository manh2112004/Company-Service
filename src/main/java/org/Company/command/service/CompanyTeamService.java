package org.Company.command.service;

import org.Company.command.model.request.CreateCompanyTeamRequest;
import org.Company.command.model.request.UpdateCompanyTeamRequest;
import java.util.concurrent.CompletableFuture;

public interface CompanyTeamService {

    CompletableFuture<String> addTeamMember(String userId, String companyId, CreateCompanyTeamRequest request);

    CompletableFuture<String> updateTeamMember(String userId, String companyId, String teamId, UpdateCompanyTeamRequest request);

    CompletableFuture<String> deleteTeamMember(String userId, String companyId, String teamId);
}
