package org.Company.command.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CompanyRepository extends JpaRepository<Company, String>, JpaSpecificationExecutor<Company> {
    boolean existsByTaxCode(String taxCode);

    boolean existsByTaxCodeAndIdNot(String taxCode, String id);
}
