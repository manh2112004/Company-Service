package org.Company.command.service.impl;

import org.Company.command.command.AddCompanySocialCommand;
import org.Company.command.command.UpdateCompanySocialCommand;
import org.Company.command.data.CompanySocialRepository;
import org.Company.command.data.CompanyMemberRepository;
import org.Company.command.data.CompanyRepository;
import org.Company.command.model.request.CreateCompanySocialRequest;
import org.Company.command.model.request.UpdateCompanySocialRequest;
import org.Company.command.service.CompanySocialService;
import org.Company.constant.CompanyMemberRole;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class CompanySocialServiceImpl implements CompanySocialService {

    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyMemberRepository companyMemberRepository;

    @Autowired
    private CompanySocialRepository companySocialRepository;

    @Override
    public CompletableFuture<String> addSocial(String userId, String companyId, CreateCompanySocialRequest request) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        companyRepository.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại"));

        boolean isOwner = companyMemberRepository.existsByCompanyIdAndUserIdAndRoleAndActiveTrue(
                companyId, userId, CompanyMemberRole.OWNER
        );
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền thêm liên kết mạng xã hội cho công ty này");
        }

        if (companySocialRepository.existsByCompanyIdAndPlatform(companyId, request.getPlatform())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Liên kết cho nền tảng này đã tồn tại");
        }

        AddCompanySocialCommand command = AddCompanySocialCommand.builder()
                .companyId(companyId)
                .socialId(UUID.randomUUID().toString())
                .platform(request.getPlatform())
                .url(request.getUrl().trim())
                .build();

        return commandGateway.send(command);
    }

    @Override
    public CompletableFuture<String> updateSocial(String userId, String companyId, String socialId, UpdateCompanySocialRequest request) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        companyRepository.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại"));

        boolean isOwner = companyMemberRepository.existsByCompanyIdAndUserIdAndRoleAndActiveTrue(
                companyId, userId, CompanyMemberRole.OWNER
        );
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền sửa liên kết mạng xã hội cho công ty này");
        }

        companySocialRepository.findByIdAndCompanyId(socialId, companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Liên kết mạng xã hội không tồn tại hoặc không thuộc công ty này"));

        if (companySocialRepository.existsByCompanyIdAndPlatformAndIdNot(companyId, request.getPlatform(), socialId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Liên kết cho nền tảng này đã tồn tại");
        }

        UpdateCompanySocialCommand command = UpdateCompanySocialCommand.builder()
                .companyId(companyId)
                .socialId(socialId)
                .platform(request.getPlatform())
                .url(request.getUrl().trim())
                .build();

        return commandGateway.send(command);
    }
}
