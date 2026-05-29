package org.Company.command.data;

import jakarta.persistence.Column;
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

    @Column(length = 1000)
    private String imageUrl;

    @Column(length = 5000)
    private String caption;
}