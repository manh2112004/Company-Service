package org.Company.command.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.Company.command.command.*;
import org.Company.command.data.*;
import org.Company.command.model.request.*;
import org.Company.command.service.CompanyService;
import org.Company.constant.CompanyMemberRole;
import org.Company.constant.CompanyStatus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyMemberRepository companyMemberRepository;

    @Autowired
    private CompanyAddressRepository companyAddressRepository;

    @Autowired
    private CompanyTechStackRepository companyTechStackRepository;

    @Autowired
    private Cloudinary cloudinary;

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
                .techStacks(trimToNull(request.getTechStacks()))
                .openPositionsCount(request.getOpenPositionsCount() != null ? request.getOpenPositionsCount() : 0)
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
                .techStacks(trimToNull(request.getTechStacks()))
                .openPositionsCount(request.getOpenPositionsCount())
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

    @Override
    public CompletableFuture<String> approveCompany(Jwt jwt, String companyId) {
        if (jwt == null || jwt.getSubject() == null || jwt.getSubject().isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        if (!hasAdminRole(jwt)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền duyệt công ty");
        }

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại"));

        if (company.getStatus() == CompanyStatus.SUSPENDED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể duyệt công ty đã bị khóa");
        }
        if (company.getStatus() == CompanyStatus.ACTIVE && Boolean.TRUE.equals(company.getVerified())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Công ty đã được duyệt");
        }

        ApproveCompanyCommand command = ApproveCompanyCommand.builder()
                .id(companyId)
                .build();
        return commandGateway.send(command);
    }

    @Override
    public CompletableFuture<String> rejectCompany(Jwt jwt, String companyId) {
        if (jwt == null || jwt.getSubject() == null || jwt.getSubject().isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        if (!hasAdminRole(jwt)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền từ chối công ty");
        }

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại"));

        if (company.getStatus() == CompanyStatus.SUSPENDED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể từ chối công ty đã bị khóa");
        }
        if (company.getStatus() == CompanyStatus.REJECTED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Công ty đã bị từ chối trước đó");
        }
        if (company.getStatus() == CompanyStatus.ACTIVE && Boolean.TRUE.equals(company.getVerified())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Công ty đã được duyệt, không thể từ chối");
        }

        RejectCompanyCommand command = RejectCompanyCommand.builder()
                .id(companyId)
                .build();
        return commandGateway.send(command);
    }

    @Override
    public CompletableFuture<String> uploadCompanyLogo(Jwt jwt, String companyId, MultipartFile file) {
        if (jwt == null || jwt.getSubject() == null || jwt.getSubject().isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File logo không được để trống");
        }
        validateImageFile(file);

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại"));

        boolean isOwner = companyMemberRepository.existsByCompanyIdAndUserIdAndRoleAndActiveTrue(
                companyId, jwt.getSubject(), CompanyMemberRole.OWNER
        );
        if (!isOwner && !hasAdminRole(jwt)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền cập nhật logo công ty này");
        }

        String logoUrl = uploadImage(file, "company-service/logos", "Upload logo công ty thất bại");
        company.setLogoUrl(logoUrl);
        company.setUpdatedAt(LocalDateTime.now());
        companyRepository.save(company);

        return CompletableFuture.completedFuture(logoUrl);
    }

    @Override
    public CompletableFuture<String> deleteCompanyLogo(Jwt jwt, String companyId) {
        if (jwt == null || jwt.getSubject() == null || jwt.getSubject().isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại"));

        boolean isOwner = companyMemberRepository.existsByCompanyIdAndUserIdAndRoleAndActiveTrue(
                companyId, jwt.getSubject(), CompanyMemberRole.OWNER
        );
        if (!isOwner && !hasAdminRole(jwt)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền xóa logo công ty này");
        }

        if (company.getLogoUrl() == null || company.getLogoUrl().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Công ty chưa có logo để xóa");
        }

        deleteImageIfPossible(company.getLogoUrl(), "Xóa logo trên Cloudinary thất bại");
        company.setLogoUrl(null);
        company.setUpdatedAt(LocalDateTime.now());
        companyRepository.save(company);

        return CompletableFuture.completedFuture("Xóa logo công ty thành công");
    }

    @Override
    public CompletableFuture<String> addCompanyTechStacks(String userId, String companyId, AddCompanyTechStacksRequest request) {
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

        List<String> techList = new java.util.ArrayList<>();
        String currentTechStacks = company.getTechStacks();
        if (currentTechStacks != null && !currentTechStacks.isBlank()) {
            Arrays.stream(currentTechStacks.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .forEach(techList::add);
        }

        if (request.getTechStacks() != null) {
            for (String newTech : request.getTechStacks()) {
                String trimmedNewTech = newTech.trim();
                if (!trimmedNewTech.isEmpty()) {
                    boolean alreadyExists = techList.stream()
                            .anyMatch(existing -> existing.equalsIgnoreCase(trimmedNewTech));
                    if (!alreadyExists) {
                        techList.add(trimmedNewTech);
                    }
                }
            }
        }

        String techStacksJoined = techList.stream()
                .collect(Collectors.joining(", "));
        if (techStacksJoined.isEmpty()) {
            techStacksJoined = null;
        }

        AddCompanyTechStacksCommand command = AddCompanyTechStacksCommand.builder()
                .id(companyId)
                .techStacks(techStacksJoined)
                .build();

        return commandGateway.send(command);
    }

    @Override
    public CompletableFuture<String> updateCompanyTechStacks(String userId, String companyId, UpdateCompanyTechStacksRequest request) {
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

        List<String> techList = new java.util.ArrayList<>();
        if (request.getTechStacks() != null) {
            for (String tech : request.getTechStacks()) {
                String trimmedTech = tech.trim();
                if (!trimmedTech.isEmpty()) {
                    boolean alreadyExists = techList.stream()
                            .anyMatch(existing -> existing.equalsIgnoreCase(trimmedTech));
                    if (!alreadyExists) {
                        techList.add(trimmedTech);
                    }
                }
            }
        }

        String techStacksJoined = techList.stream()
                .collect(Collectors.joining(", "));
        if (techStacksJoined.isEmpty()) {
            techStacksJoined = null;
        }

        UpdateCompanyTechStacksCommand command = UpdateCompanyTechStacksCommand.builder()
                .id(companyId)
                .techStacks(techStacksJoined)
                .build();

        return commandGateway.send(command);
    }

    @Override
    public CompletableFuture<String> deleteCompanyTechStacks(String userId, String companyId) {
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

        if (company.getTechStacks() == null || company.getTechStacks().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Công ty chưa có tech stack nào để xóa");
        }

        DeleteCompanyTechStacksCommand command = DeleteCompanyTechStacksCommand.builder()
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

    private String uploadImage(MultipartFile file, String folder, String errorMessage) {
        try {
            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", folder,
                    "resource_type", "image"
            ));
            Object secureUrl = result.get("secure_url");
            if (secureUrl == null) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cloudinary không trả về URL ảnh");
            }
            return secureUrl.toString();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, e);
        }
    }

    private void validateImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File upload phải là hình ảnh");
        }
    }

    private void deleteImageIfPossible(String imageUrl, String errorMessage) {
        String publicId = extractCloudinaryPublicId(imageUrl);
        if (publicId == null) {
            return;
        }

        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, e);
        }
    }

    private String extractCloudinaryPublicId(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return null;
        }

        try {
            String path = URI.create(imageUrl).getPath();
            String marker = "/upload/";
            int uploadIndex = path.indexOf(marker);
            if (uploadIndex < 0) {
                return null;
            }

            String publicPath = path.substring(uploadIndex + marker.length());
            publicPath = publicPath.replaceFirst("^v\\d+/", "");
            int extensionIndex = publicPath.lastIndexOf('.');
            if (extensionIndex > 0) {
                publicPath = publicPath.substring(0, extensionIndex);
            }
            return publicPath.isBlank() ? null : publicPath;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private boolean hasAdminRole(Jwt jwt) {
        if (hasRoleInRealmAccess(jwt, "ADMIN", "ROLE_ADMIN", "COMPANY_ADMIN", "ROLE_COMPANY_ADMIN")) {
            return true;
        }
        if (hasRoleInResourceAccess(jwt, "ADMIN", "ROLE_ADMIN", "COMPANY_ADMIN", "ROLE_COMPANY_ADMIN")) {
            return true;
        }
        if (containsRole(jwt.getClaim("authorities"), "ADMIN", "ROLE_ADMIN", "COMPANY_ADMIN", "ROLE_COMPANY_ADMIN")) {
            return true;
        }

        String scope = jwt.getClaimAsString("scope");
        if (scope != null && Arrays.stream(scope.split("\\s+"))
                .map(String::trim)
                .anyMatch(s ->
                        "admin".equalsIgnoreCase(s)
                                || "role_admin".equalsIgnoreCase(s)
                                || "company_admin".equalsIgnoreCase(s)
                                || "role_company_admin".equalsIgnoreCase(s))) {
            return true;
        }

        Object scpClaim = jwt.getClaim("scp");
        return containsRole(scpClaim, "admin", "role_admin", "company_admin", "role_company_admin");
    }

    private boolean hasRoleInRealmAccess(Jwt jwt, String... expectedRoles) {
        Object realmAccess = jwt.getClaim("realm_access");
        if (!(realmAccess instanceof Map<?, ?> realmMap)) {
            return false;
        }
        return containsRole(realmMap.get("roles"), expectedRoles);
    }

    private boolean hasRoleInResourceAccess(Jwt jwt, String... expectedRoles) {
        Object resourceAccess = jwt.getClaim("resource_access");
        if (!(resourceAccess instanceof Map<?, ?> resourceMap)) {
            return false;
        }

        String preferredClient = jwt.getClaimAsString("azp");
        if (preferredClient != null) {
            Object clientAccess = resourceMap.get(preferredClient);
            if (containsRole(clientAccess, expectedRoles)) {
                return true;
            }
        }

        return resourceMap.values().stream().anyMatch(value -> containsRole(value, expectedRoles));
    }

    private boolean containsRole(Object claimValue, String... expectedRoles) {
        Set<String> expected = Arrays.stream(expectedRoles)
                .map(String::toLowerCase)
                .collect(java.util.stream.Collectors.toSet());

        if (claimValue instanceof Collection<?> roles) {
            return roles.stream()
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .map(String::toLowerCase)
                    .anyMatch(expected::contains);
        }

        if (claimValue instanceof Map<?, ?> mapClaim) {
            Object directRoles = mapClaim.get("roles");
            if (containsRole(directRoles, expectedRoles)) {
                return true;
            }
            return mapClaim.values().stream().anyMatch(v -> containsRole(v, expectedRoles));
        }

        return false;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public CompletableFuture<String> updateCompanyOverview(String userId, String companyId, UpdateCompanyOverviewRequest request) {
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

        // Merge fields for partial updates
        String updatedName = company.getCompanyName();
        if (request.getCompanyName() != null) {
            if (request.getCompanyName().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên công ty không được để trống");
            }
            updatedName = request.getCompanyName().trim();
        }

        String updatedLogo = company.getLogoUrl();
        if (request.getLogo() != null) {
            updatedLogo = request.getLogo().trim().isEmpty() ? null : request.getLogo().trim();
        }

        String updatedWebsite = company.getWebsite();
        if (request.getWebsite() != null) {
            updatedWebsite = request.getWebsite().trim().isEmpty() ? null : request.getWebsite().trim();
        }

        Integer updatedCompanySize = company.getCompanySize();
        if (request.getEmployeeCount() != null) {
            updatedCompanySize = request.getEmployeeCount();
        }

        String updatedIndustry = company.getIndustry();
        if (request.getIndustry() != null) {
            updatedIndustry = request.getIndustry().trim().isEmpty() ? null : request.getIndustry().trim();
        }

        String updatedFoundedYear = company.getFoundedYear();
        if (request.getFoundedDate() != null) {
            updatedFoundedYear = request.getFoundedDate().trim().isEmpty() ? null : request.getFoundedDate().trim();
        }

        String updatedDescription = company.getDescription();
        if (request.getDescription() != null) {
            updatedDescription = request.getDescription().trim().isEmpty() ? null : request.getDescription().trim();
        }

        String updatedTechStacks = company.getTechStacks();
        if (request.getTechStack() != null) {
            List<String> techList = new java.util.ArrayList<>();
            for (String tech : request.getTechStack()) {
                String trimmedTech = tech.trim();
                if (!trimmedTech.isEmpty()) {
                    boolean alreadyExists = techList.stream()
                            .anyMatch(existing -> existing.equalsIgnoreCase(trimmedTech));
                    if (!alreadyExists) {
                        techList.add(trimmedTech);
                    }
                }
            }
            updatedTechStacks = techList.isEmpty() ? null : techList.stream().collect(Collectors.joining(", "));
        }

        // Handle locations
        if (request.getLocation() != null) {
            // Delete all existing addresses
            List<org.Company.command.data.CompanyAddress> oldAddresses = companyAddressRepository.findAllByCompanyId(companyId);
            for (org.Company.command.data.CompanyAddress addr : oldAddresses) {
                commandGateway.send(DeleteCompanyAddressCommand.builder()
                        .companyId(companyId)
                        .addressId(addr.getId())
                        .build());
            }

            // Insert new ones
            for (CreateCompanyAddressRequest addrReq : request.getLocation()) {
                commandGateway.send(AddCompanyAddressCommand.builder()
                        .companyId(companyId)
                        .addressId(UUID.randomUUID().toString())
                        .country(addrReq.getCountry())
                        .province(trimToNull(addrReq.getProvince()))
                        .district(trimToNull(addrReq.getDistrict()))
                        .ward(trimToNull(addrReq.getWard()))
                        .addressLine(trimToNull(addrReq.getAddressLine()))
                        .headQuarter(Boolean.TRUE.equals(addrReq.getHeadQuarter()))
                        .build());
            }
        }

        UpdateCompanyCommand command = UpdateCompanyCommand.builder()
                .id(companyId)
                .companyName(updatedName)
                .logoUrl(updatedLogo)
                .website(updatedWebsite)
                .companySize(updatedCompanySize)
                .industry(updatedIndustry)
                .foundedYear(updatedFoundedYear)
                .description(updatedDescription)
                .techStacks(updatedTechStacks)
                .build();

        return commandGateway.send(command);
    }

    @Override
    public CompletableFuture<String> deleteCompanyTechStack(String userId, String companyId, String techStackId) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được user từ token");
        }

        companyRepository.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại"));

        boolean isOwner = companyMemberRepository.existsByCompanyIdAndUserIdAndRoleAndActiveTrue(
                companyId, userId, CompanyMemberRole.OWNER
        );
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền xóa tech stack của công ty này");
        }

        boolean exists = companyTechStackRepository.existsById(techStackId);
        if (!exists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tech stack không tồn tại");
        }

        DeleteCompanyTechStackCommand command = DeleteCompanyTechStackCommand.builder()
                .companyId(companyId)
                .techStackId(techStackId)
                .build();

        return commandGateway.send(command);
    }
}
