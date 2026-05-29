package org.Company.command.aggregate;

import org.Company.command.command.AddCompanySocialCommand;
import org.Company.command.event.CompanySocialAddedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class CompanySocialAggregate {

    @AggregateIdentifier
    private String socialId;

    private String companyId;

    public CompanySocialAggregate() {
    }

    @CommandHandler
    public CompanySocialAggregate(AddCompanySocialCommand command) {
        AggregateLifecycle.apply(CompanySocialAddedEvent.builder()
                .socialId(command.getSocialId())
                .companyId(command.getCompanyId())
                .platform(command.getPlatform())
                .url(command.getUrl())
                .build());
    }

    @EventSourcingHandler
    public void on(CompanySocialAddedEvent event) {
        this.socialId = event.getSocialId();
        this.companyId = event.getCompanyId();
    }
}
