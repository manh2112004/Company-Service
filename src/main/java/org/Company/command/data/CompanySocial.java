package org.Company.command.data;

import jakarta.persistence.*;
import lombok.*;
import org.Company.constant.SocialPlatform;

@Entity
@Table(name = "company_socials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanySocial {

    @Id
    private String id;

    private String companyId;

    @Enumerated(EnumType.STRING)
    private SocialPlatform platform;

    private String url;
}