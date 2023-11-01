package kr.co.zerobase.stock.service;

import static kr.co.zerobase.stock.exception.ErrorCode.COMPANY_ALREADY_EXISTS;
import static kr.co.zerobase.stock.exception.ErrorCode.COMPANY_NOT_FOUND;

import java.util.List;
import java.util.stream.Collectors;
import kr.co.zerobase.stock.exception.StockException;
import kr.co.zerobase.stock.model.Company;
import kr.co.zerobase.stock.model.ScrapedResult;
import kr.co.zerobase.stock.persist.entity.CompanyEntity;
import kr.co.zerobase.stock.persist.entity.DividendEntity;
import kr.co.zerobase.stock.persist.repository.CompanyRepository;
import kr.co.zerobase.stock.persist.repository.DividendRepository;
import kr.co.zerobase.stock.scraper.Scraper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.Trie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final Scraper scraper;
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    @Transactional
    public Company save(String ticker) {
        if (companyRepository.existsByTicker(ticker)) {
            throw new StockException(COMPANY_ALREADY_EXISTS);
        }
        return storeCompanyAndDividend(ticker);
    }

    @Transactional(readOnly = true)
    public Page<Company> getAllCompany(Pageable pageable) {
        return new PageImpl<>(this.companyRepository.findAll(pageable)
            .stream()
            .map(Company::fromEntity)
            .collect(Collectors.toList()));
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

    public List<String> getCompanyNamesByKeyword(String keyword) {
        return companyRepository.findByNameStartingWithIgnoreCase(keyword,
                PageRequest.of(0, 10))
            .stream()
            .map(CompanyEntity::getName)
            .collect(Collectors.toList());
    }

    @Transactional
    public String deleteCompany(String ticker) {
        CompanyEntity companyEntity = companyRepository.findByTicker(ticker)
            .orElseThrow(() -> new StockException(COMPANY_NOT_FOUND));

        dividendRepository.deleteAllByCompanyId(companyEntity.getId());
        companyRepository.delete(companyEntity);

        return companyEntity.getName();
    }
}
