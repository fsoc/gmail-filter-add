import com.google.api.services.gmail.Gmail;

import java.io.IOException;

class Main {

  public static void main(String[] args) throws IOException {
    // Build a new authorized API client service.
    Gmail gmailService = GmailHandler.getGmailService();
    new FilterAdder(gmailService, "example.com");
  }

}
