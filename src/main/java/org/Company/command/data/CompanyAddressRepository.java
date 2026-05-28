package org.Company.command.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyAddressRepository extends JpaRepository<CompanyAddress, String> {

    boolean existsByCompanyIdAndHeadQuarterTrue(String companyId);
}
