package org.Company.query.controller;

import org.Company.query.model.response.CompanyTeamListResponse;
import org.Company.query.model.response.CompanyTeamResponse;
import org.Company.query.queries.GetCompanyTeamsQuery;
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
public class CompanyTeamQueryController {

    @Autowired
    private QueryGateway queryGateway;

    @GetMapping("/{companyId}/teams")
    public CompletableFuture<List<CompanyTeamResponse>> getCompanyTeams(
            @PathVariable String companyId
    ) {
        return queryGateway.query(
                new GetCompanyTeamsQuery(companyId),
                ResponseTypes.instanceOf(CompanyTeamListResponse.class)
        ).thenApply(CompanyTeamListResponse::getTeams);
    }
}
