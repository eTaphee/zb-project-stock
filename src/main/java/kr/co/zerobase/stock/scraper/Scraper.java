package kr.co.zerobase.stock.scraper;

import java.util.Optional;
import kr.co.zerobase.stock.model.Company;
import kr.co.zerobase.stock.model.ScrapedResult;

public interface Scraper {

    ScrapedResult scrap(Company company);

    Optional<Company> scrapCompanyByTicker(String ticker);
}
