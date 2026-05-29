package org.Company.command.service;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CompanyImageService {

    CompletableFuture<List<String>> uploadImages(String userId, String companyId, MultipartFile[] files, List<String> captions);
}
