package org.Company.query.controller;

import org.Company.query.model.response.CompanyAddressListResponse;
import org.Company.query.model.response.CompanyAddressResponse;
import org.Company.query.queries.GetCompanyAddressByIdQuery;
import org.Company.query.queries.GetCompanyAddressesQuery;
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
public class CompanyAddressQueryController {

    @Autowired
    private QueryGateway queryGateway;

    @GetMapping("/{companyId}/addresses")
    public CompletableFuture<List<CompanyAddressResponse>> getCompanyAddresses(
            @PathVariable String companyId
    ) {
        return queryGateway.query(
                new GetCompanyAddressesQuery(companyId),
                ResponseTypes.instanceOf(CompanyAddressListResponse.class)
        ).thenApply(CompanyAddressListResponse::getAddresses);
    }

    @GetMapping("/{companyId}/addresses/{addressId}")
    public CompletableFuture<CompanyAddressResponse> getCompanyAddressById(
            @PathVariable String companyId,
            @PathVariable String addressId
    ) {
        return queryGateway.query(
                new GetCompanyAddressByIdQuery(companyId, addressId),
                ResponseTypes.instanceOf(CompanyAddressResponse.class)
        );
    }
}
