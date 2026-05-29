package org.Company.command.aggregate;

import org.Company.command.command.AddCompanyImageCommand;
import org.Company.command.command.DeleteCompanyImageCommand;
import org.Company.command.event.CompanyImageAddedEvent;
import org.Company.command.event.CompanyImageDeletedEvent;
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

    @CommandHandler
    public String handle(DeleteCompanyImageCommand command) {
        AggregateLifecycle.apply(CompanyImageDeletedEvent.builder()
                .imageId(command.getImageId())
                .companyId(command.getCompanyId())
                .build());
        return "Xóa ảnh công ty thành công";
    }

    @EventSourcingHandler
    public void on(CompanyImageAddedEvent event) {
        this.imageId = event.getImageId();
        this.companyId = event.getCompanyId();
    }

    @EventSourcingHandler
    public void on(CompanyImageDeletedEvent event) {
        AggregateLifecycle.markDeleted();
    }
}
