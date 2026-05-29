package org.Company.command.service.impl;

import org.Company.command.command.AddCompanyMemberCommand;
import org.Company.command.command.UpdateCompanyMemberRoleCommand;
import org.Company.command.command.DeleteCompanyMemberCommand;
import org.Company.command.data.CompanyMember;
import org.Company.command.data.CompanyMemberRepository;
import org.Company.command.data.CompanyRepository;
import org.Company.command.model.request.CreateCompanyMemberRequest;
import org.Company.command.model.request.UpdateCompanyMemberRoleRequest;
import org.Company.command.service.CompanyMemberService;
import org.Company.constant.CompanyMemberRole;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class CompanyMemberServiceImpl implements CompanyMemberService {

    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyMemberRepository companyMemberRepository;

    @Override
    public CompletableFuture<String> addMember(String userId, String companyId, CreateCompanyMemberRequest request) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        companyRepository.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại"));

        boolean isOwner = companyMemberRepository.existsByCompanyIdAndUserIdAndRoleAndActiveTrue(
                companyId, userId, CompanyMemberRole.OWNER
        );
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền thêm thành viên cho công ty này");
        }

        if (companyMemberRepository.existsByCompanyIdAndUserId(companyId, request.getUserId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Thành viên này đã tồn tại trong công ty");
        }

        AddCompanyMemberCommand command = AddCompanyMemberCommand.builder()
                .companyId(companyId)
                .memberId(UUID.randomUUID().toString())
                .userId(request.getUserId().trim())
                .role(request.getRole())
                .build();

        return commandGateway.send(command);
    }

    @Override
    public CompletableFuture<String> updateMemberRole(String userId, String companyId, String memberId, UpdateCompanyMemberRoleRequest request) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        companyRepository.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại"));

        boolean isOwner = companyMemberRepository.existsByCompanyIdAndUserIdAndRoleAndActiveTrue(
                companyId, userId, CompanyMemberRole.OWNER
        );
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền thay đổi vai trò thành viên");
        }

        companyMemberRepository.findByCompanyIdAndId(companyId, memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Thành viên không tồn tại trong công ty"));

        UpdateCompanyMemberRoleCommand command = UpdateCompanyMemberRoleCommand.builder()
                .companyId(companyId)
                .memberId(memberId)
                .role(request.getRole())
                .build();

        return commandGateway.send(command);
    }

    @Override
    public CompletableFuture<String> deleteMember(String userId, String companyId, String memberId) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        companyRepository.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại"));

        boolean isOwner = companyMemberRepository.existsByCompanyIdAndUserIdAndRoleAndActiveTrue(
                companyId, userId, CompanyMemberRole.OWNER
        );
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền xóa thành viên khỏi công ty");
        }

        CompanyMember member = companyMemberRepository.findByCompanyIdAndId(companyId, memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Thành viên không tồn tại trong công ty"));

        if (CompanyMemberRole.OWNER.equals(member.getRole())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể xóa chủ sở hữu khỏi công ty");
        }

        DeleteCompanyMemberCommand command = DeleteCompanyMemberCommand.builder()
                .companyId(companyId)
                .memberId(memberId)
                .build();

        return commandGateway.send(command);
    }
}

