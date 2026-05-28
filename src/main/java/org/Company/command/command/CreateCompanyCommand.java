package org.Company.command.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.Company.constant.CompanyStatus;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCompanyCommand {
    @TargetAggregateIdentifier
    private String id;
    private String ownerUserId;
    private String companyName;
    private String logoUrl;
    private String description;
    private String website;
    private String industry;
    private Integer companySize;
    private String foundedYear;
    private String email;
    private String phoneNumber;
    private String taxCode;
    private CompanyStatus status;
    private Boolean verified;
}
