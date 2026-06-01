package org.Company.command.service.impl;

import org.Company.command.command.AddCompanyTeamCommand;
import org.Company.command.command.UpdateCompanyTeamCommand;
import org.Company.command.command.DeleteCompanyTeamCommand;
import org.Company.command.data.CompanyMemberRepository;
import org.Company.command.data.CompanyRepository;
import org.Company.command.data.CompanyTeam;
import org.Company.command.data.CompanyTeamRepository;
import org.Company.command.model.request.CreateCompanyTeamRequest;
import org.Company.command.model.request.UpdateCompanyTeamRequest;
import org.Company.command.service.CompanyTeamService;
import org.Company.constant.CompanyMemberRole;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class CompanyTeamServiceImpl implements CompanyTeamService {

    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyMemberRepository companyMemberRepository;

    @Autowired
    private CompanyTeamRepository companyTeamRepository;

    @Override
    public CompletableFuture<String> addTeamMember(String userId, String companyId, CreateCompanyTeamRequest request) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        companyRepository.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại"));

        boolean isOwner = companyMemberRepository.existsByCompanyIdAndUserIdAndRoleAndActiveTrue(
                companyId, userId, CompanyMemberRole.OWNER
        );
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền thêm thành viên đội ngũ cho công ty này");
        }

        AddCompanyTeamCommand command = AddCompanyTeamCommand.builder()
                .companyId(companyId)
                .teamId(UUID.randomUUID().toString())
                .name(request.getName().trim())
                .position(request.getPosition().trim())
                .avatarUrl(request.getAvatarUrl() != null ? request.getAvatarUrl().trim() : null)
                .linkedinUrl(request.getLinkedinUrl() != null ? request.getLinkedinUrl().trim() : null)
                .build();

        return commandGateway.send(command);
    }

    @Override
    public CompletableFuture<String> updateTeamMember(String userId, String companyId, String teamId, UpdateCompanyTeamRequest request) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        companyRepository.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại"));

        boolean isOwner = companyMemberRepository.existsByCompanyIdAndUserIdAndRoleAndActiveTrue(
                companyId, userId, CompanyMemberRole.OWNER
        );
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền sửa thông tin thành viên đội ngũ cho công ty này");
        }

        CompanyTeam existing = companyTeamRepository.findByIdAndCompanyId(teamId, companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Thành viên đội ngũ không tồn tại hoặc không thuộc công ty này"));

        String updatedName = existing.getName();
        if (request.getName() != null) {
            if (request.getName().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên thành viên không được để trống");
            }
            updatedName = request.getName().trim();
        }

        String updatedPosition = existing.getPosition();
        if (request.getPosition() != null) {
            if (request.getPosition().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vị trí không được để trống");
            }
            updatedPosition = request.getPosition().trim();
        }

        String updatedAvatarUrl = existing.getAvatarUrl();
        if (request.getAvatarUrl() != null) {
            updatedAvatarUrl = request.getAvatarUrl().trim().isEmpty() ? null : request.getAvatarUrl().trim();
        }

        String updatedLinkedinUrl = existing.getLinkedinUrl();
        if (request.getLinkedinUrl() != null) {
            updatedLinkedinUrl = request.getLinkedinUrl().trim().isEmpty() ? null : request.getLinkedinUrl().trim();
        }

        UpdateCompanyTeamCommand command = UpdateCompanyTeamCommand.builder()
                .companyId(companyId)
                .teamId(teamId)
                .name(updatedName)
                .position(updatedPosition)
                .avatarUrl(updatedAvatarUrl)
                .linkedinUrl(updatedLinkedinUrl)
                .build();

        return commandGateway.send(command);
    }

    @Override
    public CompletableFuture<String> deleteTeamMember(String userId, String companyId, String teamId) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        companyRepository.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại"));

        boolean isOwner = companyMemberRepository.existsByCompanyIdAndUserIdAndRoleAndActiveTrue(
                companyId, userId, CompanyMemberRole.OWNER
        );
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền xóa thành viên đội ngũ của công ty này");
        }

        companyTeamRepository.findByIdAndCompanyId(teamId, companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Thành viên đội ngũ không tồn tại hoặc không thuộc công ty này"));

        DeleteCompanyTeamCommand command = DeleteCompanyTeamCommand.builder()
                .companyId(companyId)
                .teamId(teamId)
                .build();

        return commandGateway.send(command);
    }
}
