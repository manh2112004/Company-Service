package org.Company.command.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyImageRepository extends JpaRepository<CompanyImage, String> {

    List<CompanyImage> findAllByCompanyId(String companyId);

    Optional<CompanyImage> findByIdAndCompanyId(String id, String companyId);
}
