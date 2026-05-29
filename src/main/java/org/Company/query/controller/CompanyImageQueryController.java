package org.Company.query.controller;

import org.Company.query.model.response.CompanyImageListResponse;
import org.Company.query.model.response.CompanyImageResponse;
import org.Company.query.queries.GetCompanyImagesQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyImageQueryController {

    @Autowired
    private QueryGateway queryGateway;

    @GetMapping("/{companyId}/images")
    public CompletableFuture<List<CompanyImageResponse>> getCompanyImages(
            @PathVariable String companyId
    ) {
        return queryGateway.query(
                new GetCompanyImagesQuery(companyId),
                ResponseTypes.instanceOf(CompanyImageListResponse.class)
        ).thenApply(CompanyImageListResponse::getImages);
    }
}
