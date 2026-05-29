package org.Company.command.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.Company.constant.SocialPlatform;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCompanySocialRequest {

    @NotNull(message = "platform không được để trống")
    private SocialPlatform platform;

    @NotBlank(message = "url không được để trống")
    private String url;
}
