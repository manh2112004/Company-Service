package org.Company.command.aggregate;

import org.Company.command.command.AddCompanyAddressCommand;
import org.Company.command.command.UpdateCompanyAddressCommand;
import org.Company.command.event.CompanyAddressAddedEvent;
import org.Company.command.event.CompanyAddressUpdatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class CompanyAddressAggregate {

    @AggregateIdentifier
    private String addressId;

    private String companyId;

    public CompanyAddressAggregate() {
    }

    @CommandHandler
    public CompanyAddressAggregate(AddCompanyAddressCommand command) {
        AggregateLifecycle.apply(CompanyAddressAddedEvent.builder()
                .addressId(command.getAddressId())
                .companyId(command.getCompanyId())
                .country(command.getCountry())
                .province(command.getProvince())
                .district(command.getDistrict())
                .ward(command.getWard())
                .addressLine(command.getAddressLine())
                .headQuarter(command.getHeadQuarter())
                .build());
    }

    @CommandHandler
    public String handle(UpdateCompanyAddressCommand command) {
        AggregateLifecycle.apply(CompanyAddressUpdatedEvent.builder()
                .addressId(command.getAddressId())
                .companyId(command.getCompanyId())
                .country(command.getCountry())
                .province(command.getProvince())
                .district(command.getDistrict())
                .ward(command.getWard())
                .addressLine(command.getAddressLine())
                .headQuarter(command.getHeadQuarter())
                .build());
        return "Cập nhật địa chỉ công ty thành công";
    }

    @EventSourcingHandler
    public void on(CompanyAddressAddedEvent event) {
        this.addressId = event.getAddressId();
        this.companyId = event.getCompanyId();
    }

    @EventSourcingHandler
    public void on(CompanyAddressUpdatedEvent event) {
        this.addressId = event.getAddressId();
        this.companyId = event.getCompanyId();
    }
}
