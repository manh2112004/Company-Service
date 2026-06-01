package org.Company.command.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "company_tech_stacks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyTechStack {

    @Id
    private String id;

    private String companyId;

    private String techStackName;
}
