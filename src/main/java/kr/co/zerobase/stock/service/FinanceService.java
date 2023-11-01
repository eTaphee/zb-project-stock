package kr.co.zerobase.stock.service;

import static kr.co.zerobase.stock.exception.ErrorCode.COMPANY_NOT_FOUND;
import static kr.co.zerobase.stock.model.constants.CacheKey.KEY_FINANCE;

import java.util.List;
import java.util.stream.Collectors;
import kr.co.zerobase.stock.exception.StockException;
import kr.co.zerobase.stock.model.Company;
import kr.co.zerobase.stock.model.Dividend;
import kr.co.zerobase.stock.model.ScrapedResult;
import kr.co.zerobase.stock.persist.entity.CompanyEntity;
import kr.co.zerobase.stock.persist.entity.DividendEntity;
import kr.co.zerobase.stock.persist.repository.CompanyRepository;
import kr.co.zerobase.stock.persist.repository.DividendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinanceService {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    @Transactional(readOnly = true)
    @Cacheable(key = "#companyName", value = KEY_FINANCE)
    public ScrapedResult getDividendByCompanyName(String companyName) {
        log.info("search company -> {}", companyName);

        CompanyEntity company = companyRepository.findByName(companyName)
            .orElseThrow(() -> new StockException(COMPANY_NOT_FOUND));

        List<DividendEntity> dividends = dividendRepository.findAllByCompanyId(company.getId());

        return ScrapedResult.builder()
            .company(Company.fromEntity(company))
            .dividends(dividends.stream().map(Dividend::fromEntity).collect(Collectors.toList()))
            .build();
    }
}
