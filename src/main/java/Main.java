import com.google.api.services.gmail.Gmail;

import java.io.IOException;

class Main {

  public static void main(String[] args) throws IOException {
    // Build a new authorized API client service.
    Gmail gmailService = GmailHandler.getGmailService();
    if (args.length == 1) {
      new FilterAdder(gmailService, args[0]);
    } else {
      System.out.println("usage: gradle run -PappArgs=\"['example.com']\"");

    }
  }

}
