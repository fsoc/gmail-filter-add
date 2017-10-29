import com.google.api.services.gmail.model.*;
import com.google.api.services.gmail.Gmail;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.of;

class FilterAdder {

  private final Gmail service;
  private final String USER_ID = "me";

  public FilterAdder(Gmail gmailService) throws IOException {
    service = gmailService;
    //TODO implement filter creation based on date
    //TODO read emails to find new commands and executed commands
    //TODO execute command
    //TODO send email with the result of execution of command
    //TODO implement command that parses forwarded email and create filter
  }

  public String createFilter(String labelId) throws IOException {
    Filter filter = new Filter()
            .setCriteria(new FilterCriteria()
                    .setTo("/(test\\.02\\.2016\\@foobar.com$)/"))
            .setAction(new FilterAction()
                    .setAddLabelIds(Collections.singletonList(labelId))
                    .setRemoveLabelIds(Arrays.asList("INBOX", "UNREAD")));
    Filter result = service.users().settings().filters().create(USER_ID, filter).execute();
    return result.getId();
  }

  public void deleteFilter(String labelId) throws IOException {
    service.users().settings().filters().delete(USER_ID, labelId).execute();
  }

  public Optional<String> getLabelId(String name) throws IOException {
    ListLabelsResponse listResponse = service.users().labels().list(USER_ID).execute();
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
