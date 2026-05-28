package org.Company.command.aggregate;

import org.Company.command.command.CreateCompanyCommand;
import org.Company.command.event.CompanyCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class CompanyAggregate {
    @AggregateIdentifier
    private String id;

    public CompanyAggregate() {
    }

    @CommandHandler
    public CompanyAggregate(CreateCompanyCommand command) {
        AggregateLifecycle.apply(CompanyCreatedEvent.builder()
                .id(command.getId())
                .ownerUserId(command.getOwnerUserId())
                .companyName(command.getCompanyName())
                .logoUrl(command.getLogoUrl())
                .description(command.getDescription())
                .website(command.getWebsite())
                .industry(command.getIndustry())
                .companySize(command.getCompanySize())
                .foundedYear(command.getFoundedYear())
                .email(command.getEmail())
                .phoneNumber(command.getPhoneNumber())
                .taxCode(command.getTaxCode())
                .status(command.getStatus())
                .verified(command.getVerified())
                .build());
    }
    @EventSourcingHandler
    public void on(CompanyCreatedEvent event) {
        this.id = event.getId();
    }
}
