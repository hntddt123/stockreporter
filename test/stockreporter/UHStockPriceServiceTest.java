package stockreporter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UHStockPriceServiceTest {

  private UHStockPriceService uhStockPriceService;

  @BeforeEach
  public void setup() {
    uhStockPriceService = new UHStockPriceService();
  }

  @Test
  public void getPricesFromOneTickersUHStockPriceService() {
    assertTrue(uhStockPriceService.getPrice("GOOG") > 0);
  }

  @Test
  public void getPricesFromTwoTickersUHStockPriceService() {
    assertTrue(uhStockPriceService.getPrice("GOOG") !=
      uhStockPriceService.getPrice("AMZN"));
  }

  @Test
  public void getPricesFromOneTickersWithInvalidErrorUHStockPriceService() {
    Throwable exception = assertThrows(RuntimeException.class,
      () -> uhStockPriceService.getPrice("INVA"));

    assertEquals("Invalid Ticker", exception.getMessage());
  }

  @Test
  public void getPricesFromOneTickersWithNetWorkErrorUHStockPriceService() {
    uhStockPriceService = Mockito.mock(UHStockPriceService.class);
    when(uhStockPriceService.getResponseFromURL("GOOG")).thenThrow(new RuntimeException("Network error"));
    Throwable exception = assertThrows(RuntimeException.class,
              () -> uhStockPriceService.getResponseFromURL("GOOG"));

    assertEquals("Network error", exception.getMessage());
  }
}
