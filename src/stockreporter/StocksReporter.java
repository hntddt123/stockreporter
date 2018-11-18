package stockreporter;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

public class StocksReporter {
  private StockPriceService stockPriceService;

  public Map<String, Integer> getHighestPrice(
    Map<String, Integer> stockPrices) {
  
    int highestPrice = stockPrices.isEmpty() ? 0 : 
      Collections.max(stockPrices.values());
    
    return stockPrices.keySet()
      .stream()
      .filter(key -> stockPrices.get(key) == highestPrice)
      .collect(toMap(Function.identity(), stockPrices::get));
  }
  
  public Map<String, Integer> getLowestPrice(
    Map<String, Integer> stockPrices) {
  
    int lowestPrice = stockPrices.isEmpty() ? 0 : 
      Collections.min(stockPrices.values());
    
    return stockPrices.keySet()
      .stream()
      .filter(key -> stockPrices.get(key) == lowestPrice)
      .collect(toMap(Function.identity(), stockPrices::get));
  }
  
  public Map<String, Integer> sortByTickers(
    Map<String, Integer> stockPrices) {

      return new TreeMap<>(stockPrices);
  }
  
  public Response fetchPrices(List<String> stockSymbol) {
    Map<String, Integer> prices = new HashMap<>();
    Map<String, String> errors = new HashMap<>();
    
    for (String symbol : stockSymbol) {
      try {
        prices.put(symbol, stockPriceService.getPrice(symbol));
      } catch (Exception exception) {
        errors.put(symbol, exception.getMessage());
      }
    }
    return new Response(prices, errors);
  }
  
  public void setStockPriceService(StockPriceService priceService) {
    stockPriceService = priceService;
  }

}