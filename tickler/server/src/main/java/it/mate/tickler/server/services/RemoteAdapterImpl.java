package it.mate.tickler.server.services;

import it.mate.gwtcommons.shared.utils.PropertiesHolder;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

@Service
public class RemoteAdapterImpl implements RemoteAdapter {

  private static Logger logger = Logger.getLogger(RemoteAdapterImpl.class);

  /** Application name. */
  private static final String APPLICATION_NAME = PropertiesHolder.getString("com.google.api.services.ApplicationName", "Tickler service account") ;

  /** Global instance of the JSON factory. */
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

  /** Global instance of the HTTP transport. */
  private static HttpTransport HTTP_TRANSPORT;

  /**
   * Global instance of the scopes required by this quickstart.
   *
   * If modifying these scopes, delete your previously saved credentials at
   * ~/.credentials/sheets.googleapis.com-java-quickstart.json
   */
  private static final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS_READONLY);

  static {
    try {
      HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    } catch (Throwable t) {
      logger.error("Error creating user data store", t);
    }
  }

  /**
   * Creates an authorized Credential object.
   * 
   * @return an authorized Credential object.
   * @throws IOException
   */
  protected Credential authorizeWithDwD() throws Exception {
    File p12File = new File(RemoteAdapterImpl.class.getResource("/service_account.p12").toURI());
    GoogleCredential credential = new GoogleCredential.Builder()
      .setTransport(HTTP_TRANSPORT)
      .setJsonFactory(JSON_FACTORY)
      .setServiceAccountId(PropertiesHolder.getString("com.google.api.services.ServiceAccountId", "tickler-service-account@api-project-255786940946.iam.gserviceaccount.com"))
      .setServiceAccountPrivateKeyFromP12File(p12File)
      .setServiceAccountScopes(SCOPES)
      .build();
    return credential;
  }

  /**
   * Build and return an authorized Sheets API client service.
   * 
   * @return an authorized Sheets API client service
   * @throws IOException
   */
  protected Sheets getSheetsService() throws Exception {
    Credential credential = authorizeWithDwD();
    return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
  }

  @PostConstruct
  public void postConstruct() {
    logger.debug("initialized " + this);
  }

  @Override
  public void scheduledChecks() {

  }

  @Override
  public String sendDevInfo(String os, String layout, String devName, String phgVersion, String platform, String devUuid, String devVersion, String devIp) {
    return null;
  }

  public List<List<Object>> test() throws Exception {
    Sheets service = getSheetsService();
    String spreadsheetId = "18g51vz6FrdAVW7lEniyhdGeiN73SVsNQfAI01wm2pnM";
//  String range = "Foglio1!A2:E7";
    String range = "A2:E7";
    ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
    List<List<Object>> values = response.getValues();
    if (values == null || values.size() == 0) {
      logger.debug("No data found.");
    } else {
      logger.debug("Colonna A, Colonna E");
      for (List row : values) {
        // Print columns A and E, which correspond to indices 0 and 4.
        logger.debug(String.format("%s (%s) - %s (%s)", row.get(0), row.get(0).getClass(), row.get(4), row.get(4).getClass()));
      }
    }
    return values;
  }
  
  public static void main(String[] args) {
    try {
      RemoteAdapterImpl impl = new RemoteAdapterImpl();
      impl.test();
    } catch (Exception ex) {
      logger.error("Error", ex);
    }
  }

}
