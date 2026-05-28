package org.Company.command.service.impl;

import org.Company.command.command.CreateCompanyCommand;
import org.Company.command.data.CompanyRepository;
import org.Company.command.model.request.CreateCompanyRequest;
import org.Company.command.service.CompanyService;
import org.Company.constant.CompanyStatus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public CompletableFuture<String> createCompany(String userId, CreateCompanyRequest request) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        if (request.getCompanyName() == null || request.getCompanyName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "companyName không được để trống");
        }

        String taxCode = trimToNull(request.getTaxCode());
        if (taxCode != null && companyRepository.existsByTaxCode(taxCode)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mã số thuế đã tồn tại");
        }

        CreateCompanyCommand command = CreateCompanyCommand.builder()
                .id(UUID.randomUUID().toString())
                .ownerUserId(userId)
                .companyName(request.getCompanyName().trim())
                .logoUrl(trimToNull(request.getLogoUrl()))
                .description(trimToNull(request.getDescription()))
                .website(trimToNull(request.getWebsite()))
                .industry(trimToNull(request.getIndustry()))
                .companySize(request.getCompanySize())
                .foundedYear(trimToNull(request.getFoundedYear()))
                .email(trimToNull(request.getEmail()))
                .phoneNumber(trimToNull(request.getPhoneNumber()))
                .taxCode(taxCode)
                .status(CompanyStatus.PENDING)
                .verified(false)
                .build();

        return commandGateway.send(command);
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
