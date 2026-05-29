package org.Company.command.data;

import org.Company.constant.CompanyMemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyMemberRepository extends JpaRepository<CompanyMember, String> {
    List<CompanyMember> findAllByCompanyId(String companyId);

    boolean existsByCompanyIdAndUserId(String companyId, String userId);

    boolean existsByCompanyIdAndUserIdAndRoleAndActiveTrue(String companyId, String userId, CompanyMemberRole role);

    Optional<CompanyMember> findByCompanyIdAndId(String companyId, String id);

    Optional<CompanyMember> findByCompanyIdAndUserId(String companyId, String userId);
}

