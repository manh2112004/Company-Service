package org.Company.query.controller;

import org.Company.query.model.response.CompanyResponse;
import org.Company.query.model.response.CompanyMemberResponse;
import org.Company.query.queries.GetCompanyByIdQuery;
import org.Company.query.queries.GetCompanyMemberByUserIdQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/internal/companies")
public class CompanyInternalQueryController {

    @Autowired
    private QueryGateway queryGateway;

    @GetMapping("/{companyId}")
    public CompletableFuture<CompanyResponse> getCompanyInternal(@PathVariable String companyId) {
        return queryGateway.query(
                new GetCompanyByIdQuery(companyId),
                ResponseTypes.instanceOf(CompanyResponse.class)
        );
    }

    @GetMapping("/{companyId}/users/{userId}")
    public CompletableFuture<CompanyMemberResponse> getCompanyMemberInternal(
            @PathVariable String companyId,
            @PathVariable String userId
    ) {
        return queryGateway.query(
                new GetCompanyMemberByUserIdQuery(companyId, userId),
                ResponseTypes.instanceOf(CompanyMemberResponse.class)
        );
    }
}
