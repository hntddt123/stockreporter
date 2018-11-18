package stockreporter;

import java.util.Map;

public class Response {
  public final Map<String, String> errors;
  public final Map<String, Integer> prices;

  public Response(
    Map<String, Integer> thePrices, Map<String, String> theErrors) {
      
    errors = theErrors;
    prices = thePrices;
  }
}
