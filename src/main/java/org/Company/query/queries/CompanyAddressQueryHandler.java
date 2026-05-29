package org.Company.query.queries;

import org.Company.command.data.CompanyAddress;
import org.Company.command.data.CompanyAddressRepository;
import org.Company.command.data.CompanyRepository;
import org.Company.query.model.response.CompanyAddressListResponse;
import org.Company.query.model.response.CompanyAddressResponse;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompanyAddressQueryHandler {

    @Autowired
    private CompanyAddressRepository companyAddressRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @QueryHandler
    @Transactional(readOnly = true)
    public CompanyAddressListResponse handle(GetCompanyAddressesQuery query) {
        companyRepository.findById(query.getCompanyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại"));

        List<CompanyAddressResponse> addresses = companyAddressRepository.findAllByCompanyId(query.getCompanyId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new CompanyAddressListResponse(addresses);
    }

    @QueryHandler
    @Transactional(readOnly = true)
    public CompanyAddressResponse handle(GetCompanyAddressByIdQuery query) {
        companyRepository.findById(query.getCompanyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại"));

        CompanyAddress address = companyAddressRepository.findByIdAndCompanyId(query.getAddressId(), query.getCompanyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Địa chỉ không tồn tại"));

        return mapToResponse(address);
    }

    private CompanyAddressResponse mapToResponse(CompanyAddress address) {
        return CompanyAddressResponse.builder()
                .id(address.getId())
                .companyId(address.getCompanyId())
                .country(address.getCountry())
                .province(address.getProvince())
                .district(address.getDistrict())
                .ward(address.getWard())
                .addressLine(address.getAddressLine())
                .headQuarter(address.getHeadQuarter())
                .build();
    }
}
