package org.Company.query.controller;

import org.Company.query.model.response.CompanyMemberListResponse;
import org.Company.query.model.response.CompanyMemberResponse;
import org.Company.query.queries.GetCompanyMemberQuery;
import org.Company.query.queries.GetCompanyMembersQuery;
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
public class CompanyMemberQueryController {

    @Autowired
    private QueryGateway queryGateway;

    @GetMapping("/{companyId}/members")
    public CompletableFuture<List<CompanyMemberResponse>> getCompanyMembers(
            @PathVariable String companyId
    ) {
        return queryGateway.query(
                new GetCompanyMembersQuery(companyId),
                ResponseTypes.instanceOf(CompanyMemberListResponse.class)
        ).thenApply(CompanyMemberListResponse::getMembers);
    }

    @GetMapping("/{companyId}/members/{memberId}")
    public CompletableFuture<CompanyMemberResponse> getCompanyMember(
            @PathVariable String companyId,
            @PathVariable String memberId
    ) {
        return queryGateway.query(
                new GetCompanyMemberQuery(companyId, memberId),
                ResponseTypes.instanceOf(CompanyMemberResponse.class)
        );
    }
}
