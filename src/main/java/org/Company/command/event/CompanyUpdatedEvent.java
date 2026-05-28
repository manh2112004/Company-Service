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
public class CompanyUpdatedEvent {
    private String id;
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
}
