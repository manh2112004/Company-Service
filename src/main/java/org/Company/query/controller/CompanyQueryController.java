package org.Company.query.controller;

import org.Company.query.model.response.CompanyResponse;
import org.Company.query.model.response.CompanyPageResponse;
import org.Company.query.model.response.CompanyPublicProfileResponse;
import org.Company.query.model.response.CompanyTechStacksResponse;
import org.Company.query.queries.GetCompaniesQuery;
import org.Company.query.queries.GetCompanyByIdQuery;
import org.Company.query.queries.GetCompanyPublicProfileQuery;
import org.Company.query.queries.GetCompanyTechStacksQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyQueryController {
    @Autowired
    private QueryGateway queryGateway;

    @GetMapping("/{companyId}")
    public CompletableFuture<CompanyResponse> getCompanyById(@PathVariable String companyId) {
        return queryGateway.query(
                new GetCompanyByIdQuery(companyId),
                ResponseTypes.instanceOf(CompanyResponse.class)
        );
    }
    @GetMapping
    public CompletableFuture<CompanyPageResponse> getCompanies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String industry
    ) {
        GetCompaniesQuery query = new GetCompaniesQuery(page, size, keyword, industry);
        return queryGateway.query(
                query,
                ResponseTypes.instanceOf(CompanyPageResponse.class)
        );
    }
    @GetMapping("/{companyId}/public-profile")
    public CompletableFuture<CompanyPublicProfileResponse> getCompanyPublicProfile(@PathVariable String companyId) {
        return queryGateway.query(
                new GetCompanyPublicProfileQuery(companyId),
                ResponseTypes.instanceOf(CompanyPublicProfileResponse.class)
        );
    }
    @GetMapping("/{companyId}/tech-stacks")
    public CompletableFuture<CompanyTechStacksResponse> getCompanyTechStacks(@PathVariable String companyId) {
        return queryGateway.query(
                new GetCompanyTechStacksQuery(companyId),
                ResponseTypes.instanceOf(CompanyTechStacksResponse.class)
        );
    }
}
