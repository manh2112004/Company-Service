package org.Company.query.queries;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCompaniesQuery {
    private int page;
    private int size;
    private String keyword;
    private String industry;
}
