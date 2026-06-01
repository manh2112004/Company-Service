package org.Company.command.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyTechStackRepository extends JpaRepository<CompanyTechStack, String> {

    List<CompanyTechStack> findAllByCompanyId(String companyId);

    void deleteAllByCompanyId(String companyId);

    boolean existsByCompanyIdAndTechStackNameIgnoreCase(String companyId, String techStackName);
}
