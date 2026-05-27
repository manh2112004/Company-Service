package org.Company.command.data;

import jakarta.persistence.*;
import lombok.*;
import org.Company.constant.CompanyMemberRole;

@Entity
@Table(name = "company_members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyMember {

    @Id
    private String id;

    private String companyId;

    private String userId;

    @Enumerated(EnumType.STRING)
    private CompanyMemberRole role;

    private Boolean active;
}