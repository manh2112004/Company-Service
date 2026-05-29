package org.Company.command.controller;

import org.Company.command.service.CompanyImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyImageCommandController {

    @Autowired
    private CompanyImageService companyImageService;

    @PostMapping(value = "/{companyId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CompletableFuture<List<String>> uploadImages(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String companyId,
            @RequestPart("files") MultipartFile[] files,
            @RequestParam(value = "captions", required = false) List<String> captions
    ) {
        return companyImageService.uploadImages(jwt.getSubject(), companyId, files, captions);
    }
}
