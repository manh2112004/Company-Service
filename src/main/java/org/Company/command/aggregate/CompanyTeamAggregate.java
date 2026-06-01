package org.Company.command.aggregate;

import org.Company.command.command.AddCompanyTeamCommand;
import org.Company.command.command.UpdateCompanyTeamCommand;
import org.Company.command.command.DeleteCompanyTeamCommand;
import org.Company.command.event.CompanyTeamAddedEvent;
import org.Company.command.event.CompanyTeamUpdatedEvent;
import org.Company.command.event.CompanyTeamDeletedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class CompanyTeamAggregate {

    @AggregateIdentifier
    private String teamId;

    private String companyId;

    public CompanyTeamAggregate() {
    }

    @CommandHandler
    public CompanyTeamAggregate(AddCompanyTeamCommand command) {
        AggregateLifecycle.apply(CompanyTeamAddedEvent.builder()
                .teamId(command.getTeamId())
                .companyId(command.getCompanyId())
                .name(command.getName())
                .position(command.getPosition())
                .avatarUrl(command.getAvatarUrl())
                .linkedinUrl(command.getLinkedinUrl())
                .build());
    }

    @CommandHandler
    public String handle(UpdateCompanyTeamCommand command) {
        AggregateLifecycle.apply(CompanyTeamUpdatedEvent.builder()
                .teamId(command.getTeamId())
                .companyId(command.getCompanyId())
                .name(command.getName())
                .position(command.getPosition())
                .avatarUrl(command.getAvatarUrl())
                .linkedinUrl(command.getLinkedinUrl())
                .build());
        return "Cập nhật thành viên đội ngũ thành công";
    }

    @EventSourcingHandler
    public void on(CompanyTeamAddedEvent event) {
        this.teamId = event.getTeamId();
        this.companyId = event.getCompanyId();
    }

    @CommandHandler
    public String handle(DeleteCompanyTeamCommand command) {
        AggregateLifecycle.apply(CompanyTeamDeletedEvent.builder()
                .teamId(command.getTeamId())
                .companyId(command.getCompanyId())
                .build());
        return "Xóa thành viên đội ngũ thành công";
    }

    @EventSourcingHandler
    public void on(CompanyTeamUpdatedEvent event) {
        this.teamId = event.getTeamId();
        this.companyId = event.getCompanyId();
    }

    @EventSourcingHandler
    public void on(CompanyTeamDeletedEvent event) {
        AggregateLifecycle.markDeleted();
    }
}
