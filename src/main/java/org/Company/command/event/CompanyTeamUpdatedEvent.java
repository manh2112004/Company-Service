package org.Company.command.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyTeamUpdatedEvent {

    private String companyId;

    private String teamId;

    private String name;

    private String position;

    private String avatarUrl;

    private String linkedinUrl;
}
