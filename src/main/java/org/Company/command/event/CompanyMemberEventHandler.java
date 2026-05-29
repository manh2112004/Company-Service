package org.Company.command.event;

import org.Company.command.data.CompanyMember;
import org.Company.command.data.CompanyMemberRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CompanyMemberEventHandler {

    @Autowired
    private CompanyMemberRepository companyMemberRepository;

    @EventHandler
    @Transactional
    public void on(CompanyMemberAddedEvent event) {
        CompanyMember member = CompanyMember.builder()
                .id(event.getMemberId())
                .companyId(event.getCompanyId())
                .userId(event.getUserId())
                .role(event.getRole())
                .active(true)
                .build();
        companyMemberRepository.save(member);
    }
}
