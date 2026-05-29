package org.Company.query.model.response;

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
public class CompanyMemberResponse {

    private String id;

    private String companyId;

    private String userId;

    private CompanyMemberRole role;

    private Boolean active;
}
