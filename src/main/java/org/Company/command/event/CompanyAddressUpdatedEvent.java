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
public class CompanyAddressUpdatedEvent {

    private String companyId;

    private String addressId;

    private String country;

    private String province;

    private String district;

    private String ward;

    private String addressLine;

    private Boolean headQuarter;
}
