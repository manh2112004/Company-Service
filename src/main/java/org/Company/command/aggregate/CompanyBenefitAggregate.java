package org.Company.command.aggregate;

import org.Company.command.command.AddCompanyBenefitCommand;
import org.Company.command.command.UpdateCompanyBenefitCommand;
import org.Company.command.event.CompanyBenefitAddedEvent;
import org.Company.command.event.CompanyBenefitUpdatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class CompanyBenefitAggregate {

    @AggregateIdentifier
    private String benefitId;

    private String companyId;

    public CompanyBenefitAggregate() {
    }

    @CommandHandler
    public CompanyBenefitAggregate(AddCompanyBenefitCommand command) {
        AggregateLifecycle.apply(CompanyBenefitAddedEvent.builder()
                .benefitId(command.getBenefitId())
                .companyId(command.getCompanyId())
                .benefitName(command.getBenefitName())
                .build());
    }

    @CommandHandler
    public String handle(UpdateCompanyBenefitCommand command) {
        AggregateLifecycle.apply(CompanyBenefitUpdatedEvent.builder()
                .benefitId(command.getBenefitId())
                .companyId(command.getCompanyId())
                .benefitName(command.getBenefitName())
                .build());
        return "Cập nhật phúc lợi công ty thành công";
    }

    @EventSourcingHandler
    public void on(CompanyBenefitAddedEvent event) {
        this.benefitId = event.getBenefitId();
        this.companyId = event.getCompanyId();
    }

    @EventSourcingHandler
    public void on(CompanyBenefitUpdatedEvent event) {
        this.benefitId = event.getBenefitId();
        this.companyId = event.getCompanyId();
    }
}
