package org.Company.command.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyTeamRepository extends JpaRepository<CompanyTeam, String> {

    List<CompanyTeam> findAllByCompanyId(String companyId);

    Optional<CompanyTeam> findByIdAndCompanyId(String id, String companyId);
}
