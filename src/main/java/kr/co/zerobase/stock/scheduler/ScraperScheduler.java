package kr.co.zerobase.stock.scheduler;

import java.util.List;
import kr.co.zerobase.stock.model.Company;
import kr.co.zerobase.stock.model.ScrapedResult;
import kr.co.zerobase.stock.persist.entity.CompanyEntity;
import kr.co.zerobase.stock.persist.entity.DividendEntity;
import kr.co.zerobase.stock.persist.repository.CompanyRepository;
import kr.co.zerobase.stock.persist.repository.DividendRepository;
import kr.co.zerobase.stock.scraper.Scraper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScraperScheduler {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;
    private final Scraper scraper;

    @Transactional
//    @Scheduled(cron = "${scheduler.scrap.yahoo}")
    public void YahooFinanceScheduling() {
        log.info("scraping scheduler is started");
        List<CompanyEntity> companies = companyRepository.findAll();

        for (CompanyEntity company : companies) {
            log.info("scraping scheduler is started -> {}", company.getName());

            ScrapedResult scrapedResult = scraper.scrap(Company.fromEntity(company));

            scrapedResult.getDividends()
                .stream()
                .map(dividend ->
                    DividendEntity.of(company.getId(), dividend)).forEach(dividend -> {
                    if (!dividendRepository.existsByCompanyIdAndDate(dividend.getCompanyId(),
                        dividend.getDate())) {
                        dividendRepository.save(dividend);
                    }
                });

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }


}
