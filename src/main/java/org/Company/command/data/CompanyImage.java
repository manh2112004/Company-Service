package org.Company.command.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "company_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyImage {

    @Id
    private String id;

    private String companyId;

    private String imageUrl;

    private String caption;
}