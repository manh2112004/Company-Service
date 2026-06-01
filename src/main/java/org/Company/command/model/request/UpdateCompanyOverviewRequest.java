package org.Company.command.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCompanyOverviewRequest {

    private String logo;

    private String companyName;

    private String website;

    private List<CreateCompanyAddressRequest> location;

    private Integer employeeCount;

    private String industry;

    private String foundedDate;

    private List<String> techStack;

    private String description;
}
