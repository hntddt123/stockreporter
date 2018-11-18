package stockreporter.consoleui;

import stockreporter.Response;
import stockreporter.StocksReporter;
import stockreporter.UHStockPriceService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StockReporterUI {

  public static void main(String[] args) {
    StocksReporter stocksReporter = new StocksReporter();
    UHStockPriceService uhStockPriceService = new UHStockPriceService();
    List<String> stockTickerList = new ArrayList<>();
    Map<String, Integer> sortedStockMap;
    Map<String, Integer> highestPrices;
    Map<String, Integer> lowestPrices;
    Response response;

    readStockTickerFromFile(stockTickerList);

    stocksReporter.setStockPriceService(uhStockPriceService);
    response = stocksReporter.fetchPrices(stockTickerList);
    sortedStockMap = stocksReporter.sortByTickers(response.prices);
    highestPrices = stocksReporter.getHighestPrice(response.prices);
    lowestPrices = stocksReporter.getLowestPrice(response.prices);

    printStockInfo("StockMarket", sortedStockMap);
    printStockInfo("Highest", highestPrices);
    printStockInfo("Lowest", lowestPrices);
    printStockErrorInfo("Errors", response.errors);
  }

  public static void readStockTickerFromFile(List<String> stockTickerList) {
    try {
      FileReader inputFile = new FileReader("stockTickers.txt");
      BufferedReader inputReader = new BufferedReader(inputFile);
      String stockTicker;

      while ((stockTicker = inputReader.readLine()) != null) {
        stockTickerList.add(stockTicker);
      }
    } catch (Exception ex) {
      System.out.println("File does not exist");
    }
  }

  public static void printStockInfo(String title, Map<String, Integer> stockMap) {
    System.out.println("----" + title + "----");
    for (Map.Entry stockSymbol : stockMap.entrySet()) {
      Integer value = (Integer) stockSymbol.getValue();
      System.out.println(stockSymbol.getKey() + "  " + (value.doubleValue() / 100));
    }
  }

  public static void printStockErrorInfo(String title, Map<String, String> stockMap) {
    System.out.println("----" + title + "----");
    for (Map.Entry stockSymbol : stockMap.entrySet()) {
      System.out.println(stockSymbol.getKey() + "  " + stockSymbol.getValue());
    }
  }
}