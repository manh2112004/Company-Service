package org.Company.query.controller;

import org.Company.query.model.response.CompanySocialListResponse;
import org.Company.query.model.response.CompanySocialResponse;
import org.Company.query.queries.GetCompanySocialsQuery;
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
public class CompanySocialQueryController {

    @Autowired
    private QueryGateway queryGateway;

    @GetMapping("/{companyId}/socials")
    public CompletableFuture<List<CompanySocialResponse>> getCompanySocials(
            @PathVariable String companyId
    ) {
        return queryGateway.query(
                new GetCompanySocialsQuery(companyId),
                ResponseTypes.instanceOf(CompanySocialListResponse.class)
        ).thenApply(CompanySocialListResponse::getSocials);
    }
}
