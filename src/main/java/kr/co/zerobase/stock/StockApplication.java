package kr.co.zerobase.stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StockApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockApplication.class, args);

//        try {
//            Connection connection = Jsoup.connect(
//                "https://finance.yahoo.com/quote/COKE/history?period1=99100800&period2=1698364800&interval=1mo&filter=history&frequency=1mo&includeAdjustedClose=true");
//
//            Document document = connection.get();
//            Element element = document.getElementsByAttributeValue("data-test",
//                "historical-prices").get(0);
//
//            Element tbody = element.children().get(1);
//
//            for(Element e: tbody.children()) {
//                String text = e.text();
//                if (!text.endsWith("Dividend")) {
//                    continue;
//                }
//
//                String[] splits = text.split(" ");
//                String month = splits[0];
//                int day = Integer.parseInt(splits[1].replace(",", ""));
//                int year = Integer.parseInt(splits[2]);
//                String dividend = splits[3];
//
//                System.out.println("month = " + month);
//                System.out.println("day = " + day);
//                System.out.println("year = " + year);
//                System.out.println("dividend = " + dividend);
//                System.out.println();
//            }
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }
}
