package org.Company.command.data;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CompanyBenefitRepository extends JpaRepository<CompanyBenefit, String> {

    List<CompanyBenefit> findAllByCompanyId(String companyId);

    boolean existsByCompanyIdAndBenefitName(String companyId, String benefitName);
}
