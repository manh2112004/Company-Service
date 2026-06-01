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
public class CreateCompanyTeamRequest {

    @NotBlank(message = "Tên thành viên không được để trống")
    private String name;

    @NotBlank(message = "Vị trí không được để trống")
    private String position;

    private String avatarUrl;

    private String linkedinUrl;
}
