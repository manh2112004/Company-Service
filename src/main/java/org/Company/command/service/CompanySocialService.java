package org.Company.command.service;

import org.Company.command.model.request.CreateCompanySocialRequest;
import org.Company.command.model.request.UpdateCompanySocialRequest;

import java.util.concurrent.CompletableFuture;

public interface CompanySocialService {

    CompletableFuture<String> addSocial(String userId, String companyId, CreateCompanySocialRequest request);

    CompletableFuture<String> updateSocial(String userId, String companyId, String socialId, UpdateCompanySocialRequest request);

    CompletableFuture<String> deleteSocial(String userId, String companyId, String socialId);
}
