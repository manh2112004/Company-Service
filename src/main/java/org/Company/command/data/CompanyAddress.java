package org.Company.command.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "company_addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyAddress {
    @Id
    private String id;

    private String companyId;

    private String country;

    private String province;

    private String district;

    private String ward;

    private String addressLine;

    private Boolean headQuarter;
}