package org.Company.command.aggregate;

import org.Company.command.command.AddCompanyMemberCommand;
import org.Company.command.command.UpdateCompanyMemberRoleCommand;
import org.Company.command.event.CompanyMemberAddedEvent;
import org.Company.command.event.CompanyMemberRoleUpdatedEvent;
import org.Company.constant.CompanyMemberRole;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class CompanyMemberAggregate {

    @AggregateIdentifier
    private String memberId;

    private String companyId;

    private CompanyMemberRole role;

    public CompanyMemberAggregate() {
    }

    @CommandHandler
    public CompanyMemberAggregate(AddCompanyMemberCommand command) {
        AggregateLifecycle.apply(CompanyMemberAddedEvent.builder()
                .memberId(command.getMemberId())
                .companyId(command.getCompanyId())
                .userId(command.getUserId())
                .role(command.getRole())
                .build());
    }

    @CommandHandler
    public String handle(UpdateCompanyMemberRoleCommand command) {
        AggregateLifecycle.apply(CompanyMemberRoleUpdatedEvent.builder()
                .memberId(command.getMemberId())
                .companyId(command.getCompanyId())
                .role(command.getRole())
                .build());
        return "Cập nhật vai trò thành viên thành công";
    }

    @EventSourcingHandler
    public void on(CompanyMemberAddedEvent event) {
        this.memberId = event.getMemberId();
        this.companyId = event.getCompanyId();
        this.role = event.getRole();
    }

    @EventSourcingHandler
    public void on(CompanyMemberRoleUpdatedEvent event) {
        this.role = event.getRole();
    }
}

