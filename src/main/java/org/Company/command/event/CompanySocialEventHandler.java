package org.Company.command.event;

import org.Company.command.data.CompanySocial;
import org.Company.command.data.CompanySocialRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CompanySocialEventHandler {

    @Autowired
    private CompanySocialRepository companySocialRepository;

    @EventHandler
    @Transactional
    public void on(CompanySocialAddedEvent event) {
        CompanySocial social = CompanySocial.builder()
                .id(event.getSocialId())
                .companyId(event.getCompanyId())
                .platform(event.getPlatform())
                .url(event.getUrl())
                .build();
        companySocialRepository.save(social);
    }

    @EventHandler
    @Transactional
    public void on(CompanySocialUpdatedEvent event) {
        CompanySocial social = companySocialRepository.findById(event.getSocialId()).orElse(null);
        if (social == null) {
            return;
        }
        social.setPlatform(event.getPlatform());
        social.setUrl(event.getUrl());
        companySocialRepository.save(social);
    }

    @EventHandler
    @Transactional
    public void on(CompanySocialDeletedEvent event) {
        companySocialRepository.deleteById(event.getSocialId());
    }
}
