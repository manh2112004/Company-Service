package org.Company.command.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.Company.constant.SocialPlatform;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddCompanySocialCommand {

    private String companyId;

    @TargetAggregateIdentifier
    private String socialId;

    private SocialPlatform platform;

    private String url;
}
