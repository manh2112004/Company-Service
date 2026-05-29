package org.Company.command.event;

import org.Company.command.data.CompanyAddress;
import org.Company.command.data.CompanyAddressRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CompanyAddressEventHandler {

    @Autowired
    private CompanyAddressRepository companyAddressRepository;

    @EventHandler
    @Transactional
    public void on(CompanyAddressAddedEvent event) {
        CompanyAddress address = CompanyAddress.builder()
                .id(event.getAddressId())
                .companyId(event.getCompanyId())
                .country(event.getCountry())
                .province(event.getProvince())
                .district(event.getDistrict())
                .ward(event.getWard())
                .addressLine(event.getAddressLine())
                .headQuarter(event.getHeadQuarter())
                .build();
        companyAddressRepository.save(address);
    }

    @EventHandler
    @Transactional
    public void on(CompanyAddressUpdatedEvent event) {
        CompanyAddress address = companyAddressRepository.findById(event.getAddressId()).orElse(null);
        if (address == null) {
            return;
        }
        address.setCountry(event.getCountry());
        address.setProvince(event.getProvince());
        address.setDistrict(event.getDistrict());
        address.setWard(event.getWard());
        address.setAddressLine(event.getAddressLine());
        address.setHeadQuarter(event.getHeadQuarter());
        companyAddressRepository.save(address);
    }

    @EventHandler
    @Transactional
    public void on(CompanyAddressDeletedEvent event) {
        companyAddressRepository.deleteById(event.getAddressId());
    }
}
