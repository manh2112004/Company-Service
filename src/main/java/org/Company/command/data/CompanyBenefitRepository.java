package org.Company.command.data;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CompanyBenefitRepository extends JpaRepository<CompanyBenefit, String> {

    boolean existsByCompanyIdAndBenefitName(String companyId, String benefitName);
}
