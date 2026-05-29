package org.Company.command.service;

import org.Company.command.model.request.CreateCompanySocialRequest;

import java.util.concurrent.CompletableFuture;

public interface CompanySocialService {

    CompletableFuture<String> addSocial(String userId, String companyId, CreateCompanySocialRequest request);
}
