package org.Company.command.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCompanyTeamCommand {

    private String companyId;

    @TargetAggregateIdentifier
    private String teamId;

    private String name;

    private String position;

    private String avatarUrl;

    private String linkedinUrl;
}
