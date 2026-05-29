package org.Company.command.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyAddressRepository extends JpaRepository<CompanyAddress, String> {

    boolean existsByCompanyIdAndHeadQuarterTrue(String companyId);

    boolean existsByCompanyIdAndHeadQuarterTrueAndIdNot(String companyId, String id);

    List<CompanyAddress> findAllByCompanyId(String companyId);

    Optional<CompanyAddress> findByIdAndCompanyId(String id, String companyId);
}

