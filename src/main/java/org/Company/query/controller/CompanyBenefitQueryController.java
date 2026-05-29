package org.Company.query.controller;

import org.Company.query.model.response.CompanyBenefitListResponse;
import org.Company.query.model.response.CompanyBenefitResponse;
import org.Company.query.queries.GetCompanyBenefitsQuery;
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
public class CompanyBenefitQueryController {

    @Autowired
    private QueryGateway queryGateway;

    @GetMapping("/{companyId}/benefits")
    public CompletableFuture<List<CompanyBenefitResponse>> getCompanyBenefits(
            @PathVariable String companyId
    ) {
        return queryGateway.query(
                new GetCompanyBenefitsQuery(companyId),
                ResponseTypes.instanceOf(CompanyBenefitListResponse.class)
        ).thenApply(CompanyBenefitListResponse::getBenefits);
    }
}
