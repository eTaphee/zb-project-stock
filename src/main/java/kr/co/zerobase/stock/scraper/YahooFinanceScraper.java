package kr.co.zerobase.stock.scraper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kr.co.zerobase.stock.exception.ScrapException;
import kr.co.zerobase.stock.model.Company;
import kr.co.zerobase.stock.model.Dividend;
import kr.co.zerobase.stock.model.ScrapedResult;
import kr.co.zerobase.stock.model.constants.Month;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class YahooFinanceScraper implements Scraper {

    private static final String DIVIDEND_URL = "https://finance.yahoo.com/quote/%s/history?period1=%d&period2=%d&interval=1mo";
    private static final String SUMMARY_URL = "https://finance.yahoo.com/quote/%s";
    private static final long START_TIME = 86400; // 60 * 60 * 24

    @Override
    public ScrapedResult scrap(Company company) {
        return ScrapedResult.builder()
            .company(company)
            .dividends(
                parseDividends(getTBodyElement(company)))
            .build();
    }

    @Override
    public Optional<Company> scrapCompanyByTicker(String ticker) {
        Connection connection = Jsoup.connect(String.format(SUMMARY_URL, ticker));

        try {
            Document document = connection.get();
            Element h1Element = document.getElementsByTag("h1").get(0);
            String name = h1Element.text().split("[(]")[0].trim();
            return Optional.ofNullable(
                Company.builder()
                    .name(name)
                    .ticker(ticker)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private static Element getTBodyElement(Company company) {
        Connection connection = Jsoup.connect(
            String.format(DIVIDEND_URL,
                company.getTicker(),
                START_TIME,
                System.currentTimeMillis() / 1000));

        try {
            Elements table = connection.get()
                .getElementsByAttributeValue("data-test", "historical-prices");

            if (table.isEmpty()) {
                throw new ScrapException("not found historical-prices");
            }

            return table
                .get(0)
                .children()
                .get(1);
        } catch (IOException e) {
            throw new ScrapException(e);
        }
    }

    private static List<Dividend> parseDividends(Element tbody) {
        List<Dividend> dividends = new ArrayList<>();

        for (Element e : tbody.children()) {
            String text = e.text();
            if (!text.endsWith("Dividend")) {
                continue;
            }
            dividends.add(parseDividend(text));
        }

        return dividends;
    }

    private static Dividend parseDividend(String text) {
        String[] splits = text.split(" ");

        try {
            int month = Month.strToNumber(splits[0]);
            int day = Integer.parseInt(splits[1].replace(",", ""));
            int year = Integer.parseInt(splits[2]);
            String dividend = splits[3];

            return Dividend.builder()
                .date(LocalDateTime.of(year, month, day, 0, 0))
                .dividend(dividend)
                .build();
        } catch (IllegalArgumentException e) {
            throw new ScrapException("Unexpected Month enum value: " + splits[0]);
        }
    }
}
