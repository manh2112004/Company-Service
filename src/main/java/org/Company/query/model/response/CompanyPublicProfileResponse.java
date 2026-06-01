package org.Company.query.model.response;

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
public class CompanyPublicProfileResponse {
    private CompanyResponse companyInfo;
    private String description;
    private List<CompanySocialResponse> socialLinks;
    private List<String> techStacks;
    private List<CompanyAddressResponse> officeLocations;
    private List<CompanyImageResponse> companyImages;
    private List<CompanyTeamResponse> team;
    private List<CompanyBenefitResponse> benefits;
    private Integer openPositionsCount;
}
