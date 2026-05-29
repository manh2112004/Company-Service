package org.Company.command.event;

import org.Company.command.data.CompanyImage;
import org.Company.command.data.CompanyImageRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CompanyImageEventHandler {

    @Autowired
    private CompanyImageRepository companyImageRepository;

    @EventHandler
    @Transactional
    public void on(CompanyImageAddedEvent event) {
        CompanyImage image = CompanyImage.builder()
                .id(event.getImageId())
                .companyId(event.getCompanyId())
                .imageUrl(event.getImageUrl())
                .caption(event.getCaption())
                .build();
        companyImageRepository.save(image);
    }

    @EventHandler
    @Transactional
    public void on(CompanyImageDeletedEvent event) {
        companyImageRepository.deleteById(event.getImageId());
    }
}
