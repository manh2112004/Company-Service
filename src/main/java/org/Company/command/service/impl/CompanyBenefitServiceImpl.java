package org.Company.command.service.impl;

import org.Company.command.command.AddCompanyBenefitCommand;
import org.Company.command.data.CompanyMemberRepository;
import org.Company.command.data.CompanyRepository;
import org.Company.command.data.CompanyBenefitRepository;
import org.Company.command.model.request.CreateCompanyBenefitRequest;
import org.Company.command.service.CompanyBenefitService;
import org.Company.constant.CompanyMemberRole;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class CompanyBenefitServiceImpl implements CompanyBenefitService {

    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyMemberRepository companyMemberRepository;

    @Autowired
    private CompanyBenefitRepository companyBenefitRepository;

    @Override
    public CompletableFuture<String> addBenefit(String userId, String companyId, CreateCompanyBenefitRequest request) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        companyRepository.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại"));

        boolean isOwner = companyMemberRepository.existsByCompanyIdAndUserIdAndRoleAndActiveTrue(
                companyId, userId, CompanyMemberRole.OWNER
        );
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền thêm phúc lợi cho công ty này");
        }

        String benefitName = request.getBenefitName().trim();
        if (companyBenefitRepository.existsByCompanyIdAndBenefitName(companyId, benefitName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phúc lợi này đã tồn tại cho công ty");
        }

        AddCompanyBenefitCommand command = AddCompanyBenefitCommand.builder()
                .companyId(companyId)
                .benefitId(UUID.randomUUID().toString())
                .benefitName(benefitName)
                .build();

        return commandGateway.send(command);
    }
}
