package stockreporter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class StockReporterTest {

  private StocksReporter stocksReporter;
  private StockPriceService stockPriceService;

  private final String GOOG = "GOOG";
  private final String AMZN = "AMZN";
  private final String INTC = "INTC";
  private final String MSFT = "MSFT";
  
  @BeforeEach
  public void setup() {
    stocksReporter = new StocksReporter();
    stockPriceService = Mockito.mock(StockPriceService.class);
    stocksReporter.setStockPriceService(stockPriceService);
  }
  
  @Test
  public void canary() {
    assert(true);
  }
  
  @Test
  public void getHighestPriceForEmptyData() {
    assertEquals(Map.of(), stocksReporter.getHighestPrice(Map.of()));
  }
  
  @Test
  public void getHighestPriceForDataWithOneHighPrice() {
    assertEquals(Map.of(GOOG, 100),
      stocksReporter.getHighestPrice(Map.of(GOOG, 100)));
  }
  
  @Test
  public void getHighestPriceForDataWithTwoHighPrices() {
    assertEquals(Map.of(GOOG, 100, AMZN, 100),
      stocksReporter.getHighestPrice(Map.of(GOOG, 100, INTC, 90, AMZN, 100)));
  }
  
  @Test
  public void getLowestPricePriceForEmptyData() {
    assertEquals(Map.of(), stocksReporter.getLowestPrice(Map.of()));
  }
  
  @Test
  public void getLowestPriceForDataWithOneLowPrice() {
    assertEquals(Map.of(GOOG, 50),
      stocksReporter.getLowestPrice(Map.of(GOOG, 50)));
  }
  
  @Test
  public void getLowestPriceForDataWithTwoLowPrices() {
    assertEquals(Map.of(GOOG, 50, "AMZN", 50),
      stocksReporter.getLowestPrice(Map.of(GOOG, 50, INTC, 100, AMZN, 50)));
  }
  
  @Test
  public void sortByTickersEmptyData() {
    assertEquals(Map.of(), stocksReporter.sortByTickers(Map.of()));
  }
  
  @Test
  public void sortByTickersWithListOfData() {
    assertEquals(List.of(AMZN, GOOG, INTC, MSFT),
      stocksReporter.sortByTickers(
        Map.of(MSFT, 10, GOOG, 20, AMZN, 5, INTC, 100))
          .keySet()
          .stream()
          .collect(toList()));
  }
  
  @Test
  public void fetchPricesWithEmptyTicker() {
    Response response = stocksReporter.fetchPrices(List.of());

    assertEquals(Map.of(), response.prices);
    assertEquals(Map.of(), response.errors);
  }
  
  @Test
  public void fetchPricesWithOneTicker() {
    when(stockPriceService.getPrice(GOOG)).thenReturn(100);
    Response response = stocksReporter.fetchPrices(List.of(GOOG));

    assertEquals(Map.of(GOOG, 100), response.prices);
    assertEquals(Map.of(), response.errors);
  }
  
  @Test
  public void fetchPricesWithTwoTicker() {
    when(stockPriceService.getPrice(GOOG)).thenReturn(100);
    when(stockPriceService.getPrice(AMZN)).thenReturn(200);
    Response response = stocksReporter.fetchPrices(List.of(GOOG, AMZN));

    assertEquals(Map.of(GOOG, 100, AMZN, 200), response.prices);
    assertEquals(Map.of(), response.errors);
  }
  
  @Test
  public void fetchPricesWithOneErrorTicker() {
    when(stockPriceService.getPrice(GOOG))
      .thenThrow(new RuntimeException("Invalid Ticker"));
    Response response = stocksReporter.fetchPrices(List.of(GOOG));

    assertEquals(Map.of(), response.prices);
    assertEquals(Map.of(GOOG, "Invalid Ticker"), response.errors);
  }

  @Test
  public void fetchPricesFromThreeTickersWithTwoErrorTickers() {
    when(stockPriceService.getPrice(AMZN)).thenReturn(100);
    when(stockPriceService.getPrice(GOOG))
      .thenThrow(new RuntimeException("Network Error"));
    when(stockPriceService.getPrice("INVALID"))
      .thenThrow(new RuntimeException("Invalid Ticker"));

    Response response = stocksReporter.fetchPrices(List.of(GOOG, AMZN, "INVALID"));

    assertEquals(Map.of(AMZN, 100), response.prices);
    assertEquals(Map.of(GOOG, "Network Error", "INVALID", "Invalid Ticker"),
                 response.errors);
  }
}
