import com.google.api.services.gmail.Gmail;

import java.io.IOException;

class Main {

  public static void main(String[] args) throws IOException {
    // Build a new authorized API client service.
    Gmail gmailService = GmailHandler.getGmailService();
    if (args.length == 1) {
      FilterAdder filterAdder = new FilterAdder(gmailService, args[0]);
      String junk = filterAdder.getLabelId("Junk").orElseThrow(IllegalArgumentException::new);
      filterAdder.createFilter(junk);
    } else {
      System.out.println("usage: gradle run -PappArgs=\"['example.com']\"");

    }
  }

}
