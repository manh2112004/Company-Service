package org.Company.command.event;

import org.Company.command.data.CompanyMember;
import org.Company.command.data.CompanyMemberRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

    @EventHandler
    @Transactional
    public void on(CompanyMemberRoleUpdatedEvent event) {
        CompanyMember member = companyMemberRepository.findByCompanyIdAndId(event.getCompanyId(), event.getMemberId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Thành viên không tồn tại"));
        member.setRole(event.getRole());
        companyMemberRepository.save(member);
    }

    @EventHandler
    @Transactional
    public void on(CompanyMemberDeletedEvent event) {
        CompanyMember member = companyMemberRepository.findByCompanyIdAndId(event.getCompanyId(), event.getMemberId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Thành viên không tồn tại"));
        companyMemberRepository.delete(member);
    }
}

