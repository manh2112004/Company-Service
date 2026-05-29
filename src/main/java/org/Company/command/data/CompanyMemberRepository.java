package org.Company.command.data;

import org.Company.constant.CompanyMemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyMemberRepository extends JpaRepository<CompanyMember, String> {
    List<CompanyMember> findAllByCompanyId(String companyId);

    boolean existsByCompanyIdAndUserId(String companyId, String userId);

    boolean existsByCompanyIdAndUserIdAndRoleAndActiveTrue(String companyId, String userId, CompanyMemberRole role);
}
