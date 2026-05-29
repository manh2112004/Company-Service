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
public class CompanyImageAddedEvent {

    private String companyId;

    private String imageId;

    private String imageUrl;

    private String caption;
}
