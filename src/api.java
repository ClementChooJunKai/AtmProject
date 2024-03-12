import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

/*------------------------------------------------- API -----
 |  Class api
 |
 |  Purpose: provides a Java class called api with a method called getCurrency that converts between currencies using the exchangerate-api.com API.
 |   Takes two bank IDs as input and returns the conversion rate between their currencies. 
 |  
 |                                                   
 *-------------------------------------------------------------------*/
public class api {
    // Define a private static final variable to store the API key
    private static final String API_KEY = "1ffce32c60ed0147b3d5b677";

    public static void main(String[] args) throws IOException {
        // Call the getCurrency method and print the result
        System.out.println(getCurrency(1, 2)); // SGD to CNY
    }

    // take two bank IDs and returns the conversion rate between their currencies
    public static float getCurrency(int fromBank, int toBank) throws IOException {
        // Get the currency codes for the two banks
        String fromCurrency = getCurrencyCode(fromBank);
        String toCurrency = getCurrencyCode(toBank);

        // If the two currencies are the same, return 1
        if (fromCurrency == toCurrency) {
            return 1;
        } else {
            // Otherwise, construct a URL to call the exchangerate-api.com API
            String urlString = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/pair/" + fromCurrency + "/"
                    + toCurrency;
            URL url = new URL(urlString);

            // Open a connection to the URL and read the response using a Scanner
            Scanner scanner = new Scanner(url.openStream());
            String response = scanner.useDelimiter("\\Z").next();
            scanner.close();

            // Extract the conversion rate from the response string
            int index = response.indexOf("\"conversion_rate\":") + 17;
            String rateString = response.substring(index + 1, index + 7);
            float rate = Float.parseFloat(rateString);
            return rate;
        }
    }

    // helper method that returns the currency code for a given bank ID
    private static String getCurrencyCode(int bankId) {
        switch (bankId) {
            case 1:
                return "SGD";
            case 2:
                return "CNY";
            case 3:
                return "MYR";
            case 4:
                return "AUD";
            default:
                throw new IllegalArgumentException("Invalid bank id");
        }
    }
}
