package org.Company.command.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.Company.constant.SocialPlatform;

import java.util.List;
import java.util.Optional;

public interface CompanySocialRepository extends JpaRepository<CompanySocial, String> {

    List<CompanySocial> findAllByCompanyId(String companyId);

    boolean existsByCompanyIdAndPlatform(String companyId, SocialPlatform platform);

    Optional<CompanySocial> findByIdAndCompanyId(String id, String companyId);
}
