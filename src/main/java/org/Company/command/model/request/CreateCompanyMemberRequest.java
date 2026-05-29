package org.Company.command.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.Company.constant.CompanyMemberRole;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCompanyMemberRequest {

    @NotBlank(message = "userId không được để trống")
    private String userId;

    @NotNull(message = "role không được để trống")
    private CompanyMemberRole role;
}
