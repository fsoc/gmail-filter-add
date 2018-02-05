import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.of;

class FilterAdder {

  private final Gmail service;
  private final String USER_ID = "me";
  private final String domain;

  FilterAdder(Gmail gmailService, String domain) {
    this.domain = domain;
    service = gmailService;
    //TODO read emails to find new commands and executed commands
    //TODO execute command
    //TODO send email with the result of execution of command
    //TODO implement command that parses forwarded email and create filter
  }

  String createFilter(String labelId) throws IOException {
    Optional<String> filterIdFromCriteria = getFilterIdFromCriteria(getMonthlyFilter());

    if (filterIdFromCriteria.isPresent()) {
      return filterIdFromCriteria.get();
    } else {
      Filter filter = new Filter()
              .setCriteria(new FilterCriteria()
                      .setTo(getMonthlyFilter()))
              .setAction(new FilterAction()
                      .setAddLabelIds(Collections.singletonList(labelId))
                      .setRemoveLabelIds(Arrays.asList("INBOX", "UNREAD")));
      Filter result = service.users().settings().filters().create(USER_ID, filter).execute();
      return result.getId();
    }
  }

  void deleteFilter(String labelId) throws IOException {
    service.users().settings().filters().delete(USER_ID, labelId).execute();
  }

  Optional<String> getLabelId(String name) throws IOException {
    ListLabelsResponse listResponse = service.users().labels().list(USER_ID).execute();
    List<Label> labels = listResponse.getLabels();

    for (Label label : labels) {
      if (label.getName().equals(name)) {
        return of(label.getId());
      }
    }

    System.out.println("No labels found for name:" + name);
    return Optional.empty();
  }

  String getFilterCriteria(String labelId) throws IOException {
    Filter filter = service.users().settings().filters().get(USER_ID, labelId).execute();
    return filter.getCriteria().getTo();
  }

  private Optional<String> getFilterIdFromCriteria(String toExpression) throws IOException {
    ListFiltersResponse filters = service.users().settings().filters().list(USER_ID).execute();
    return filters.getFilter().stream()
            .filter(filter -> toExpression.equals(filter.getCriteria().getTo()))
            .map(Filter::getId)
            .findFirst();
  }

  String getMonthlyFilter() {
    LocalDate date = LocalDate.now();
    return "/(\\." + String.format("%02d",date.getMonthValue()) + "." + date.getYear() + "\\@" + domain + "$)/";
  }
}
