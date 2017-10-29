import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class Main {

  public static void main(String[] args) throws IOException {
    // Build a new authorized API client service.
    Gmail gmailService = GmailHandler.getGmailService();
    new FilterAdder(gmailService);
  }

}
