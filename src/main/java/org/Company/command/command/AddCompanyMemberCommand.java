package org.Company.command.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.Company.constant.CompanyMemberRole;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddCompanyMemberCommand {

    private String companyId;

    @TargetAggregateIdentifier
    private String memberId;

    private String userId;

    private CompanyMemberRole role;
}
