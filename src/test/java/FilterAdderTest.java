import com.google.api.services.gmail.Gmail;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class FilterAdderTest {

  private Gmail gmailService;
  private FilterAdder filterAdder;

  @Before
  public void setup() throws IOException {
    gmailService = GmailHandler.getGmailService();
    filterAdder = new FilterAdder(gmailService);
  }

  @Test
  public void junkLabelExists() throws IOException {
    String junk = "Junk";
    filterAdder.getLabelId(junk).orElseThrow(IllegalArgumentException::new);
  }

  @Test
  public void getAndDeleteLabel() throws IOException {
    Gmail gmailService = GmailHandler.getGmailService();
    FilterAdder filterAdder = new FilterAdder(gmailService);
    String junk = filterAdder.getLabelId("Junk").orElseThrow(IllegalArgumentException::new);
    String filterId = filterAdder.createFilter(junk);
    System.out.println("Created filter " + filterId);
    filterAdder.deleteFilter(filterId);
  }

}
