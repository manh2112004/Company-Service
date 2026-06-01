package org.Company.command.model.request;

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
public class UpdateCompanyTeamRequest {

    private String name;

    private String position;

    private String avatarUrl;

    private String linkedinUrl;
}
