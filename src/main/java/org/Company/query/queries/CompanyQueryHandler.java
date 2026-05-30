package org.Company.query.queries;

import org.Company.command.data.*;
import org.Company.constant.CompanyStatus;
import org.Company.query.model.response.*;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompanyQueryHandler {
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyAddressRepository companyAddressRepository;

    @Autowired
    private CompanyBenefitRepository companyBenefitRepository;

    @Autowired
    private CompanyImageRepository companyImageRepository;

    @Autowired
    private CompanySocialRepository companySocialRepository;

    @Autowired
    private CompanyMemberRepository companyMemberRepository;

    @QueryHandler
    @Transactional(readOnly = true)
    public CompanyResponse handle(GetCompanyByIdQuery query) {
        Company company = companyRepository.findById(query.getCompanyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại"));

        if (company.getStatus() == CompanyStatus.SUSPENDED) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại");
        }

        return CompanyResponse.builder()
                .id(company.getId())
                .companyName(company.getCompanyName())
                .logoUrl(company.getLogoUrl())
                .description(company.getDescription())
                .website(company.getWebsite())
                .industry(company.getIndustry())
                .companySize(company.getCompanySize())
                .foundedYear(company.getFoundedYear())
                .email(company.getEmail())
                .phoneNumber(company.getPhoneNumber())
                .taxCode(company.getTaxCode())
                .techStacks(company.getTechStacks())
                .openPositionsCount(company.getOpenPositionsCount())
                .status(company.getStatus())
                .verified(company.getVerified())
                .createdAt(company.getCreatedAt())
                .updatedAt(company.getUpdatedAt())
                .build();
    }
    @QueryHandler
    @Transactional(readOnly = true)
    public CompanyPageResponse handle(GetCompaniesQuery query) {
        if (query.getPage() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "page phải >= 0");
        }
        if (query.getSize() <= 0 || query.getSize() > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "size phải trong khoảng 1..100");
        }

        Pageable pageable = PageRequest.of(
                query.getPage(),
                query.getSize(),
                Sort.by(Sort.Direction.DESC, "id")
        );

        Specification<Company> spec = Specification.where((root, cq, cb) ->
                cb.or(
                        cb.isNull(root.get("status")),
                        cb.notEqual(root.get("status"), CompanyStatus.SUSPENDED)
                )
        );

        if (query.getKeyword() != null && !query.getKeyword().isBlank()) {
            String keyword = "%" + query.getKeyword().toLowerCase() + "%";

            spec = spec.and((root, cq, cb) ->
                    cb.or(
                            cb.like(cb.lower(root.get("companyName")), keyword), // Lưu ý: Đổi từ "name" thành "companyName" cho khớp Entity của bạn
                            cb.like(cb.lower(root.get("description")), keyword)
                    )
            );
        }

        if (query.getIndustry() != null && !query.getIndustry().isBlank()) {
            spec = spec.and((root, cq, cb) ->
                    cb.equal(
                            cb.lower(root.get("industry")),
                            query.getIndustry().toLowerCase()
                    )
            );
        }

        Page<Company> companyPage = companyRepository.findAll(spec, pageable);
        List<CompanyResponse> dtoList = companyPage.map(this::mapToResponse).getContent();

        return new CompanyPageResponse(
                dtoList,
                companyPage.getNumber(),
                companyPage.getSize(),
                companyPage.getTotalElements(),
                companyPage.getTotalPages()
        );
    }

    private CompanyResponse mapToResponse(Company company) {
        return CompanyResponse.builder()
                .id(company.getId())
                .companyName(company.getCompanyName())
                .description(company.getDescription())
                .website(company.getWebsite())
                .industry(company.getIndustry())
                .companySize(company.getCompanySize())
                .logoUrl(company.getLogoUrl())
                .email(company.getEmail())
                .phoneNumber(company.getPhoneNumber())
                .techStacks(company.getTechStacks())
                .openPositionsCount(company.getOpenPositionsCount())
                .build();
    }

    @QueryHandler
    @Transactional(readOnly = true)
    public CompanyPublicProfileResponse handle(GetCompanyPublicProfileQuery query) {
        Company company = companyRepository.findById(query.getCompanyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại"));

        if (company.getStatus() == CompanyStatus.SUSPENDED) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại");
        }

        CompanyResponse fullResponse = CompanyResponse.builder()
                .id(company.getId())
                .companyName(company.getCompanyName())
                .logoUrl(company.getLogoUrl())
                .description(company.getDescription())
                .website(company.getWebsite())
                .industry(company.getIndustry())
                .companySize(company.getCompanySize())
                .foundedYear(company.getFoundedYear())
                .email(company.getEmail())
                .phoneNumber(company.getPhoneNumber())
                .taxCode(company.getTaxCode())
                .techStacks(company.getTechStacks())
                .openPositionsCount(company.getOpenPositionsCount())
                .status(company.getStatus())
                .verified(company.getVerified())
                .createdAt(company.getCreatedAt())
                .updatedAt(company.getUpdatedAt())
                .build();

        List<CompanySocialResponse> socialLinks = companySocialRepository.findAllByCompanyId(query.getCompanyId())
                .stream()
                .map(social -> CompanySocialResponse.builder()
                        .id(social.getId())
                        .companyId(social.getCompanyId())
                        .platform(social.getPlatform())
                        .url(social.getUrl())
                        .build())
                .collect(Collectors.toList());

        List<String> techStacksList = Collections.emptyList();
        if (company.getTechStacks() != null && !company.getTechStacks().isBlank()) {
            techStacksList = Arrays.stream(company.getTechStacks().split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        }

        List<CompanyAddressResponse> officeLocations = companyAddressRepository.findAllByCompanyId(query.getCompanyId())
                .stream()
                .map(addr -> CompanyAddressResponse.builder()
                        .id(addr.getId())
                        .companyId(addr.getCompanyId())
                        .country(addr.getCountry())
                        .province(addr.getProvince())
                        .district(addr.getDistrict())
                        .ward(addr.getWard())
                        .addressLine(addr.getAddressLine())
                        .headQuarter(addr.getHeadQuarter())
                        .build())
                .collect(Collectors.toList());

        List<CompanyImageResponse> companyImages = companyImageRepository.findAllByCompanyId(query.getCompanyId())
                .stream()
                .map(img -> CompanyImageResponse.builder()
                        .id(img.getId())
                        .companyId(img.getCompanyId())
                        .imageUrl(img.getImageUrl())
                        .caption(img.getCaption())
                        .build())
                .collect(Collectors.toList());

        List<CompanyMemberResponse> team = companyMemberRepository.findAllByCompanyId(query.getCompanyId())
                .stream()
                .map(m -> CompanyMemberResponse.builder()
                        .id(m.getId())
                        .companyId(m.getCompanyId())
                        .userId(m.getUserId())
                        .role(m.getRole())
                        .active(m.getActive())
                        .build())
                .collect(Collectors.toList());

        List<CompanyBenefitResponse> benefits = companyBenefitRepository.findAllByCompanyId(query.getCompanyId())
                .stream()
                .map(b -> CompanyBenefitResponse.builder()
                        .id(b.getId())
                        .companyId(b.getCompanyId())
                        .benefitName(b.getBenefitName())
                        .build())
                .collect(Collectors.toList());

        Integer openPositions = company.getOpenPositionsCount() != null ? company.getOpenPositionsCount() : 0;

        return CompanyPublicProfileResponse.builder()
                .companyInfo(fullResponse)
                .description(company.getDescription())
                .socialLinks(socialLinks)
                .techStacks(techStacksList)
                .officeLocations(officeLocations)
                .companyImages(companyImages)
                .team(team)
                .benefits(benefits)
                .openPositionsCount(openPositions)
                .build();
    }
}
