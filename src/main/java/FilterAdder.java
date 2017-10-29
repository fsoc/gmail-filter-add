import com.google.api.services.gmail.model.*;
import com.google.api.services.gmail.Gmail;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.of;

class FilterAdder {

  public static void main(String[] args) throws IOException {
    // Build a new authorized API client service.
    Gmail service = GmailHandler.getGmailService();

    String junk = getLabelId("Junk", service).orElseThrow(IllegalArgumentException::new);
    System.out.println("Created filter " + createFilter(junk, service));
  }

  private static String createFilter(String labelId, Gmail service) throws IOException {
    Filter filter = new Filter()
            .setCriteria(new FilterCriteria()
                    .setTo("/(test\\.02\\.2016\\@foobar.com$)/"))
            .setAction(new FilterAction()
                    .setAddLabelIds(Collections.singletonList(labelId))
                    .setRemoveLabelIds(Arrays.asList("INBOX", "UNREAD")));
    Filter result = service.users().settings().filters().create("me", filter).execute();
    return result.getId();
  }

  private static Optional<String> getLabelId(String name, Gmail service) throws IOException {
    ListLabelsResponse listResponse = service.users().labels().list("me").execute();
    List<Label> labels = listResponse.getLabels();
    if (labels.size() == 0) {
      System.out.println("No labels found.");
    } else {
      System.out.println("Labels:");
      for (Label label : labels) {
        if (label.getName().equals(name)) {
          return of(label.getId());
        }
      }
    }
    return Optional.empty();
  }

}
