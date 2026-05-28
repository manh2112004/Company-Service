package org.Company.command.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyAddressRepository extends JpaRepository<CompanyAddress, String> {

    boolean existsByCompanyIdAndHeadQuarterTrue(String companyId);

    List<CompanyAddress> findAllByCompanyId(String companyId);
}

