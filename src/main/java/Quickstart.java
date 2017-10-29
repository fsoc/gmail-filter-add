import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.*;
import com.google.api.services.gmail.Gmail;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.of;

public class Quickstart {
  /**
   * Application name.
   */
  private static final String APPLICATION_NAME = "api-filter";

  /**
   * Directory to store user credentials for this application.
   */
  private static final java.io.File DATA_STORE_DIR = new java.io.File(
          System.getProperty("user.home"), ".credentials/" + APPLICATION_NAME);

  /**
   * Global instance of the {@link FileDataStoreFactory}.
   */
  private static FileDataStoreFactory DATA_STORE_FACTORY;

  /**
   * Global instance of the JSON factory.
   */
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

  /**
   * Global instance of the HTTP transport.
   */
  private static HttpTransport HTTP_TRANSPORT;

  /**
   * Global instance of the scopes required by this quickstart.
   * <p>
   * If modifying these scopes, delete your previously saved credentials
   * at ~/.credentials/gmail-java-quickstart
   */
  private static final List<String> SCOPES = Arrays.asList(
          GmailScopes.GMAIL_SETTINGS_BASIC,
          GmailScopes.GMAIL_LABELS,
          GmailScopes.GMAIL_METADATA,
          GmailScopes.GMAIL_MODIFY,
          GmailScopes.GMAIL_READONLY
  );

  static {
    try {
      HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
      DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
    } catch (Throwable t) {
      t.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Creates an authorized Credential object.
   *
   * @return an authorized Credential object.
   * @throws IOException
   */
  public static Credential authorize() throws IOException {
    // Load client secrets.
    InputStream in = Quickstart.class.getResourceAsStream("/client_secret.json");
    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

    // Build flow and trigger user authorization request.
    GoogleAuthorizationCodeFlow flow =
            new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                    .setDataStoreFactory(DATA_STORE_FACTORY)
                    .setAccessType("offline")
                    .build();
    Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
    return credential;
  }

  /**
   * Build and return an authorized Gmail client service.
   *
   * @return an authorized Gmail client service
   * @throws IOException
   */
  public static Gmail getGmailService() throws IOException {
    Credential credential = authorize();
    return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
            .setApplicationName(APPLICATION_NAME)
            .build();
  }

  public static void main(String[] args) throws IOException {
    // Build a new authorized API client service.
    Gmail service = getGmailService();

    String junk = getLabelId("Junk", service).orElseThrow(IllegalArgumentException::new);
    createFilter(junk, service);
  }

  private static void createFilter(String labelId, Gmail service) throws IOException {
    Filter filter = new Filter()
            .setCriteria(new FilterCriteria()
                    .setTo("/(test\\.02\\.2016\\@foobar.com$)/"))
            .setAction(new FilterAction()
                    .setAddLabelIds(Arrays.asList(labelId))
                    .setRemoveLabelIds(Arrays.asList("INBOX", "UNREAD")));
    Filter result = service.users().settings().filters().create("me", filter).execute();
    System.out.println("Created filter " + result.getId());
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
