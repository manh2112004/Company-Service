package org.Company.query.model.response;

import java.util.List;

public class CompanyPageResponse extends PageResponse<CompanyResponse> {
    public CompanyPageResponse() {
        super();
    }

    public CompanyPageResponse(List<CompanyResponse> content, int page, int size, long totalElements, int totalPages) {
        super(content, page, size, totalElements, totalPages);
    }
}
