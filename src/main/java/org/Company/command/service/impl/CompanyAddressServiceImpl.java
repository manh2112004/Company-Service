package org.Company.command.service.impl;

import org.Company.command.command.AddCompanyAddressCommand;
import org.Company.command.command.UpdateCompanyAddressCommand;
import org.Company.command.command.DeleteCompanyAddressCommand;
import org.Company.command.data.CompanyAddress;
import org.Company.command.data.CompanyAddressRepository;
import org.Company.command.data.CompanyMemberRepository;
import org.Company.command.data.CompanyRepository;
import org.Company.command.model.request.CreateCompanyAddressRequest;
import org.Company.command.model.request.UpdateCompanyAddressRequest;
import org.Company.command.service.CompanyAddressService;
import org.Company.constant.CompanyMemberRole;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class CompanyAddressServiceImpl implements CompanyAddressService {

    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyMemberRepository companyMemberRepository;

    @Autowired
    private CompanyAddressRepository companyAddressRepository;

    @Override
    public CompletableFuture<String> addAddress(String userId, String companyId, CreateCompanyAddressRequest request) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        companyRepository.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại"));

        boolean isOwner = companyMemberRepository.existsByCompanyIdAndUserIdAndRoleAndActiveTrue(
                companyId, userId, CompanyMemberRole.OWNER
        );
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền thêm địa chỉ cho công ty này");
        }

        if (Boolean.TRUE.equals(request.getHeadQuarter())
                && companyAddressRepository.existsByCompanyIdAndHeadQuarterTrue(companyId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Công ty đã có trụ sở chính, không thể thêm thêm");
        }

        AddCompanyAddressCommand command = AddCompanyAddressCommand.builder()
                .companyId(companyId)
                .addressId(UUID.randomUUID().toString())
                .country(request.getCountry())
                .province(trimToNull(request.getProvince()))
                .district(trimToNull(request.getDistrict()))
                .ward(trimToNull(request.getWard()))
                .addressLine(trimToNull(request.getAddressLine()))
                .headQuarter(Boolean.TRUE.equals(request.getHeadQuarter()))
                .build();

        return commandGateway.send(command);
    }

    @Override
    public CompletableFuture<String> updateAddress(String userId, String companyId, String addressId, UpdateCompanyAddressRequest request) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        companyRepository.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại"));

        boolean isOwner = companyMemberRepository.existsByCompanyIdAndUserIdAndRoleAndActiveTrue(
                companyId, userId, CompanyMemberRole.OWNER
        );
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền sửa địa chỉ cho công ty này");
        }

        companyAddressRepository.findByIdAndCompanyId(addressId, companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Địa chỉ không tồn tại hoặc không thuộc công ty này"));

        if (Boolean.TRUE.equals(request.getHeadQuarter())
                && companyAddressRepository.existsByCompanyIdAndHeadQuarterTrueAndIdNot(companyId, addressId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Công ty đã có trụ sở chính, không thể đặt địa chỉ này làm trụ sở chính");
        }

        UpdateCompanyAddressCommand command = UpdateCompanyAddressCommand.builder()
                .companyId(companyId)
                .addressId(addressId)
                .country(request.getCountry())
                .province(trimToNull(request.getProvince()))
                .district(trimToNull(request.getDistrict()))
                .ward(trimToNull(request.getWard()))
                .addressLine(trimToNull(request.getAddressLine()))
                .headQuarter(Boolean.TRUE.equals(request.getHeadQuarter()))
                .build();

        return commandGateway.send(command);
    }

    @Override
    public CompletableFuture<String> deleteAddress(String userId, String companyId, String addressId) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        companyRepository.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại"));

        boolean isOwner = companyMemberRepository.existsByCompanyIdAndUserIdAndRoleAndActiveTrue(
                companyId, userId, CompanyMemberRole.OWNER
        );
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền xóa địa chỉ cho công ty này");
        }

        companyAddressRepository.findByIdAndCompanyId(addressId, companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Địa chỉ không tồn tại hoặc không thuộc công ty này"));

        DeleteCompanyAddressCommand command = DeleteCompanyAddressCommand.builder()
                .companyId(companyId)
                .addressId(addressId)
                .build();

        return commandGateway.send(command);
    }

    private String trimToNull(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
