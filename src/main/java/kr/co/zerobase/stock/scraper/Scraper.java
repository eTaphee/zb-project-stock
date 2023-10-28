package kr.co.zerobase.stock.scraper;

import kr.co.zerobase.stock.model.Company;
import kr.co.zerobase.stock.model.ScrapedResult;

public interface Scraper {

    ScrapedResult scrap(Company company);

    Company scrapCompanyByTicker(String ticker);
}
