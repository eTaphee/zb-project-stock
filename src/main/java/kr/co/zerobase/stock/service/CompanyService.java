package kr.co.zerobase.stock.service;

import java.util.stream.Collectors;
import javax.transaction.Transactional;
import kr.co.zerobase.stock.model.Company;
import kr.co.zerobase.stock.model.ScrapedResult;
import kr.co.zerobase.stock.persist.entity.CompanyEntity;
import kr.co.zerobase.stock.persist.entity.DividendEntity;
import kr.co.zerobase.stock.persist.repository.CompanyRepository;
import kr.co.zerobase.stock.persist.repository.DividendRepository;
import kr.co.zerobase.stock.scraper.Scraper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final Scraper scraper;
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    @Transactional
    public Company save(String ticker) {
        if (companyRepository.existsByTicker(ticker)) {
            throw new RuntimeException("already exists ticker: " + ticker);
        }
        return storeCompanyAndDividend(ticker);
    }

    private Company storeCompanyAndDividend(String ticker) {
        Company company = scraper.scrapCompanyByTicker(ticker)
            .orElseThrow(() -> new RuntimeException("failed to scrap ticker: " + ticker));

        ScrapedResult scrapedResult = scraper.scrap(company);

        CompanyEntity saved = companyRepository.save(CompanyEntity.from(company));

        dividendRepository.saveAll(scrapedResult
            .getDividends()
            .stream()
            .map((dividend) -> DividendEntity.of(saved.getId(), dividend))
            .collect(Collectors.toList()));

        return company;
    }
}
