package org.Company.command.data;

import jakarta.persistence.*;
import lombok.*;
import org.Company.constant.CompanyStatus;

import java.time.LocalDateTime;
@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    private String id;

    @Column(nullable = false)
    private String companyName;

    private String logoUrl;

    @Column(length = 5000)
    private String description;

    private String website;

    private String industry;

    private Integer companySize;

    private String foundedYear;

    private String email;

    private String phoneNumber;

    private String taxCode;

    @Column(length = 2000)
    private String techStacks;

    private Integer openPositionsCount;

    @Enumerated(EnumType.STRING)
    private CompanyStatus status;

    private Boolean verified;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}