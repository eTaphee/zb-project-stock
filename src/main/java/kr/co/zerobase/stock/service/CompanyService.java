package kr.co.zerobase.stock.service;

import java.util.List;
import java.util.stream.Collectors;
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

    private final Trie<String, String> trie;
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

    public void addAutocompleteKeyword(String keyword) {
        trie.put(keyword, null);
    }

    public List<String> autocomplete(String keyword) {
        return trie.prefixMap(keyword).keySet()
            .stream()
            .limit(10)
            .collect(Collectors.toList());
    }

    public void deleteAutocompleteKeyword(String keyword) {
        trie.remove(keyword);
    }
}
