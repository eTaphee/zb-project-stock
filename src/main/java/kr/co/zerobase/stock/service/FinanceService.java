package kr.co.zerobase.stock.service;

import java.util.List;
import java.util.stream.Collectors;
import kr.co.zerobase.stock.model.Company;
import kr.co.zerobase.stock.model.Dividend;
import kr.co.zerobase.stock.model.ScrapedResult;
import kr.co.zerobase.stock.persist.entity.CompanyEntity;
import kr.co.zerobase.stock.persist.entity.DividendEntity;
import kr.co.zerobase.stock.persist.repository.CompanyRepository;
import kr.co.zerobase.stock.persist.repository.DividendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FinanceService {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    @Transactional(readOnly = true)
    public ScrapedResult getDividendByCompanyName(String companyName) {
        CompanyEntity company = companyRepository.findByName(companyName)
            .orElseThrow(() -> new RuntimeException("not found company name"));

        List<DividendEntity> dividends = dividendRepository.findAllByCompanyId(company.getId());

        return ScrapedResult.builder()
            .company(Company.fromEntity(company))
            .dividends(dividends.stream().map(Dividend::fromEntity).collect(Collectors.toList()))
            .build();
    }
}
