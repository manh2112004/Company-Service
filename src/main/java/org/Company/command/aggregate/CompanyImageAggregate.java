package org.Company.command.aggregate;

import org.Company.command.command.AddCompanyImageCommand;
import org.Company.command.event.CompanyImageAddedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class CompanyImageAggregate {

    @AggregateIdentifier
    private String imageId;

    private String companyId;

    public CompanyImageAggregate() {
    }

    @CommandHandler
    public CompanyImageAggregate(AddCompanyImageCommand command) {
        AggregateLifecycle.apply(CompanyImageAddedEvent.builder()
                .imageId(command.getImageId())
                .companyId(command.getCompanyId())
                .imageUrl(command.getImageUrl())
                .caption(command.getCaption())
                .build());
    }

    @EventSourcingHandler
    public void on(CompanyImageAddedEvent event) {
        this.imageId = event.getImageId();
        this.companyId = event.getCompanyId();
    }
}
