package org.Company.command.aggregate;

import org.Company.command.command.*;
import org.Company.command.event.*;
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
                .techStacks(command.getTechStacks())
                .openPositionsCount(command.getOpenPositionsCount())
                .status(command.getStatus())
                .verified(command.getVerified())
                .build());
    }

    @CommandHandler
    public String handle(UpdateCompanyCommand command) {
        AggregateLifecycle.apply(CompanyUpdatedEvent.builder()
                .id(command.getId())
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
                .techStacks(command.getTechStacks())
                .openPositionsCount(command.getOpenPositionsCount())
                .build());
        return "Cập nhật công ty thành công";
    }

    @CommandHandler
    public String handle(DeleteCompanyCommand command) {
        AggregateLifecycle.apply(CompanyDeletedEvent.builder()
                .id(command.getId())
                .build());
        return "Xóa công ty thành công";
    }

    @CommandHandler
    public String handle(ApproveCompanyCommand command) {
        AggregateLifecycle.apply(CompanyApprovedEvent.builder()
                .id(command.getId())
                .build());
        return "Duyệt công ty thành công";
    }

    @CommandHandler
    public String handle(RejectCompanyCommand command) {
        AggregateLifecycle.apply(CompanyRejectedEvent.builder()
                .id(command.getId())
                .build());
        return "Từ chối công ty thành công";
    }

    @CommandHandler
    public String handle(AddCompanyTechStacksCommand command) {
        AggregateLifecycle.apply(CompanyTechStacksAddedEvent.builder()
                .id(command.getId())
                .techStacks(command.getTechStacks())
                .build());
        return "Thêm tech stacks thành công";
    }

    @CommandHandler
    public String handle(UpdateCompanyTechStacksCommand command) {
        AggregateLifecycle.apply(CompanyTechStacksUpdatedEvent.builder()
                .id(command.getId())
                .techStacks(command.getTechStacks())
                .build());
        return "Cập nhật tech stacks thành công";
    }

    @CommandHandler
    public String handle(DeleteCompanyTechStacksCommand command) {
        AggregateLifecycle.apply(CompanyTechStacksDeletedEvent.builder()
                .id(command.getId())
                .build());
        return "Xóa tech stacks thành công";
    }

    @EventSourcingHandler
    public void on(CompanyCreatedEvent event) {
        this.id = event.getId();
    }

    @EventSourcingHandler
    public void on(CompanyUpdatedEvent event) {
        this.id = event.getId();
    }

    @EventSourcingHandler
    public void on(CompanyDeletedEvent event) {
        this.id = event.getId();
    }

    @EventSourcingHandler
    public void on(CompanyApprovedEvent event) {
        this.id = event.getId();
    }

    @EventSourcingHandler
    public void on(CompanyRejectedEvent event) {
        this.id = event.getId();
    }

    @EventSourcingHandler
    public void on(CompanyTechStacksAddedEvent event) {
        this.id = event.getId();
    }

    @EventSourcingHandler
    public void on(CompanyTechStacksUpdatedEvent event) {
        this.id = event.getId();
    }

    @EventSourcingHandler
    public void on(CompanyTechStacksDeletedEvent event) {
        this.id = event.getId();
    }
}
