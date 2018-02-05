import com.google.api.services.gmail.Gmail;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

public class FilterAdderTest {

  private FilterAdder filterAdder;

  @Before
  public void setup() throws IOException {
    Gmail gmailService = GmailHandler.getGmailService();
    filterAdder = new FilterAdder(gmailService, "example.com");
  }

  @Test
  public void junkLabelExists() throws IOException {
    String junk = "Junk";
    filterAdder.getLabelId(junk).orElseThrow(IllegalArgumentException::new);
  }

  @Test
  public void getAndDeleteLabel() throws IOException {
    String junk = filterAdder.getLabelId("Junk").orElseThrow(IllegalArgumentException::new);
    String filterId = filterAdder.createFilter(junk);
    System.out.println("Created filter " + filterId);
    filterAdder.deleteFilter(filterId);
  }

  @Test
  public void getFilterCriteria() throws IOException {
    String monthlyFilter = filterAdder.getMonthlyFilter();
    System.out.println("monthlyFilter " + monthlyFilter);
    String junk = filterAdder.getLabelId("Junk").orElseThrow(IllegalArgumentException::new);
    String filterId = filterAdder.createFilter(junk);
    assertEquals(filterAdder.getFilterCriteria(filterId), monthlyFilter);
    filterAdder.deleteFilter(filterId);
  }

  @Test
  public void filterCriteriaDuplicateCheck() throws IOException {
    String junk = filterAdder.getLabelId("Junk").orElseThrow(IllegalArgumentException::new);
    String filterId = filterAdder.createFilter(junk);

    String newFilterId = filterAdder.createFilter(junk);

    assertEquals(filterId, newFilterId);
    filterAdder.deleteFilter(filterId);
  }
}
