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
public class UpdateCompanyAddressCommand {

    private String companyId;

    @TargetAggregateIdentifier
    private String addressId;

    private String country;

    private String province;

    private String district;

    private String ward;

    private String addressLine;

    private Boolean headQuarter;
}
