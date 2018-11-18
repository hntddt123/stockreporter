package stockreporter;

import java.net.*;
import java.io.*;

public class UHStockPriceService implements StockPriceService {

  @Override
  public int getPrice(String ticker) {
    try {
      String stockPrice = getResponseFromURL(ticker);
      int price = (int) (Double.parseDouble(stockPrice) * 100);

      return price;
    } catch (Exception exception) {
        throw new RuntimeException("Invalid Ticker");
    }
  }

  public String getResponseFromURL(String ticker) {
    try {
      URL stockInfo = new URL("http://agile.cs.uh.edu/stock?ticker=" + ticker);
      URLConnection urlConnection = stockInfo.openConnection();
      BufferedReader inputText = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

      String stockPrice;
      stockPrice = inputText.readLine();
      inputText.close();

      return stockPrice;
    } catch (Exception exception) {
        throw new RuntimeException("Network error");
    }
  }
}
