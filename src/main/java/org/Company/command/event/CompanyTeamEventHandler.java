package org.Company.command.event;

import org.Company.command.data.CompanyTeam;
import org.Company.command.data.CompanyTeamRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CompanyTeamEventHandler {

    @Autowired
    private CompanyTeamRepository companyTeamRepository;

    @EventHandler
    @Transactional
    public void on(CompanyTeamAddedEvent event) {
        CompanyTeam team = CompanyTeam.builder()
                .id(event.getTeamId())
                .companyId(event.getCompanyId())
                .name(event.getName())
                .position(event.getPosition())
                .avatarUrl(event.getAvatarUrl())
                .linkedinUrl(event.getLinkedinUrl())
                .build();
        companyTeamRepository.save(team);
    }

    @EventHandler
    @Transactional
    public void on(CompanyTeamUpdatedEvent event) {
        CompanyTeam team = companyTeamRepository.findById(event.getTeamId()).orElse(null);
        if (team == null) {
            return;
        }
        team.setName(event.getName());
        team.setPosition(event.getPosition());
        team.setAvatarUrl(event.getAvatarUrl());
        team.setLinkedinUrl(event.getLinkedinUrl());
        companyTeamRepository.save(team);
    }

    @EventHandler
    @Transactional
    public void on(CompanyTeamDeletedEvent event) {
        companyTeamRepository.deleteById(event.getTeamId());
    }
}
