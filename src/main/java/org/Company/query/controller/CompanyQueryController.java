package org.Company.query.controller;

import org.Company.query.model.response.CompanyResponse;
import org.Company.query.queries.GetCompanyByIdQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
