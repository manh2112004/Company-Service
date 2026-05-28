package org.Company.command.data;

import org.Company.constant.CompanyMemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyMemberRepository extends JpaRepository<CompanyMember, String> {
    boolean existsByCompanyIdAndUserId(String companyId, String userId);

    boolean existsByCompanyIdAndUserIdAndRoleAndActiveTrue(String companyId, String userId, CompanyMemberRole role);
}
