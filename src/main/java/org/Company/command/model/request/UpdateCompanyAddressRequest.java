package org.Company.command.model.request;

import jakarta.validation.constraints.NotBlank;
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
public class UpdateCompanyAddressRequest {

    @NotBlank(message = "country không được để trống")
    private String country;

    private String province;

    private String district;

    private String ward;

    private String addressLine;

    private Boolean headQuarter;
}
