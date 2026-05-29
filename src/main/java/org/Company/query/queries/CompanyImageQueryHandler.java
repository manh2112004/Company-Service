package org.Company.query.queries;

import org.Company.command.data.CompanyImage;
import org.Company.command.data.CompanyImageRepository;
import org.Company.command.data.CompanyRepository;
import org.Company.query.model.response.CompanyImageListResponse;
import org.Company.query.model.response.CompanyImageResponse;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompanyImageQueryHandler {

    @Autowired
    private CompanyImageRepository companyImageRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @QueryHandler
    @Transactional(readOnly = true)
    public CompanyImageListResponse handle(GetCompanyImagesQuery query) {
        companyRepository.findById(query.getCompanyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại"));

        List<CompanyImageResponse> images = companyImageRepository.findAllByCompanyId(query.getCompanyId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new CompanyImageListResponse(images);
    }

    private CompanyImageResponse mapToResponse(CompanyImage image) {
        return CompanyImageResponse.builder()
                .id(image.getId())
                .companyId(image.getCompanyId())
                .imageUrl(image.getImageUrl())
                .caption(image.getCaption())
                .build();
    }
}
