package org.Company.command.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "company_teams")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyTeam {

    @Id
    private String id;

    private String companyId;

    private String name;

    private String position;

    @Column(length = 1000)
    private String avatarUrl;

    @Column(length = 1000)
    private String linkedinUrl;
}
