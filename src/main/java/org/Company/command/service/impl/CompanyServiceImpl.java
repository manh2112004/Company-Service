package org.Company.command.service.impl;

import org.Company.command.command.CreateCompanyCommand;
import org.Company.command.command.DeleteCompanyCommand;
import org.Company.command.command.UpdateCompanyCommand;
import org.Company.command.data.Company;
import org.Company.command.data.CompanyMemberRepository;
import org.Company.command.data.CompanyRepository;
import org.Company.command.model.request.CreateCompanyRequest;
import org.Company.command.model.request.UpdateCompanyRequest;
import org.Company.command.service.CompanyService;
import org.Company.constant.CompanyMemberRole;
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

    @Autowired
    private CompanyMemberRepository companyMemberRepository;

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

    @Override
    public CompletableFuture<String> updateCompany(String userId, String companyId, UpdateCompanyRequest request) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại"));

        boolean isOwner = companyMemberRepository.existsByCompanyIdAndUserIdAndRoleAndActiveTrue(
                companyId, userId, CompanyMemberRole.OWNER
        );
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền cập nhật công ty này");
        }

        if (request.getCompanyName() != null && request.getCompanyName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "companyName không được để trống");
        }

        String taxCode = trimToNull(request.getTaxCode());
        if (taxCode != null && companyRepository.existsByTaxCodeAndIdNot(taxCode, companyId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mã số thuế đã tồn tại");
        }

        UpdateCompanyCommand command = UpdateCompanyCommand.builder()
                .id(company.getId())
                .companyName(trimToNull(request.getCompanyName()))
                .logoUrl(trimToNull(request.getLogoUrl()))
                .description(trimToNull(request.getDescription()))
                .website(trimToNull(request.getWebsite()))
                .industry(trimToNull(request.getIndustry()))
                .companySize(request.getCompanySize())
                .foundedYear(trimToNull(request.getFoundedYear()))
                .email(trimToNull(request.getEmail()))
                .phoneNumber(trimToNull(request.getPhoneNumber()))
                .taxCode(taxCode)
                .build();

        return commandGateway.send(command);
    }

    @Override
    public CompletableFuture<String> deleteCompany(String userId, String companyId) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại"));

        boolean isOwner = companyMemberRepository.existsByCompanyIdAndUserIdAndRoleAndActiveTrue(
                companyId, userId, CompanyMemberRole.OWNER
        );
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền xóa công ty này");
        }

        if (company.getStatus() == CompanyStatus.SUSPENDED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Công ty đã bị xóa trước đó");
        }

        DeleteCompanyCommand command = DeleteCompanyCommand.builder()
                .id(companyId)
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
