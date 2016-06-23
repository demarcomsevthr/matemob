package it.mate.testons.server.services;

import it.mate.commons.server.dao.Dao;
import it.mate.commons.server.dao.FindCallback;
import it.mate.commons.server.dao.FindContext;
import it.mate.commons.server.dao.ParameterDefinition;
import it.mate.commons.server.utils.CloneUtils;
import it.mate.commons.server.utils.DateUtils;
import it.mate.commons.server.utils.LoggingUtils;
import it.mate.commons.server.utils.StringUtils;
import it.mate.testons.server.model.AccountDs;
import it.mate.testons.server.model.CounterDs;
import it.mate.testons.server.model.DevInfoDs;
import it.mate.testons.server.model.MessageDs;
import it.mate.testons.server.model.OrderDs;
import it.mate.testons.server.model.OrderItemDs;
import it.mate.testons.server.model.OrderItemRowDs;
import it.mate.testons.server.model.TimbroDs;
import it.mate.testons.shared.model.Account;
import it.mate.testons.shared.model.DevInfo;
import it.mate.testons.shared.model.Message;
import it.mate.testons.shared.model.Order;
import it.mate.testons.shared.model.OrderItem;
import it.mate.testons.shared.model.OrderItemRow;
import it.mate.testons.shared.model.Timbro;
import it.mate.testons.shared.model.impl.AccountTx;
import it.mate.testons.shared.model.impl.DevInfoTx;
import it.mate.testons.shared.model.impl.OrderTx;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.Base64Utils;

@Service
public class MainAdapterImpl implements MainAdapter {
  
  @Autowired private Dao dao;
  
  private List<Timbro> cacheTimbri;
  
  private static byte[] nonalfanum = new byte[48];
  
  private static final String PUSH_GCM_SERVER_KEY = "AIzaSyCH7xyFO1K3EmajORN_MVwDj4lA7yPBxp4";
  
  @PostConstruct
  public void postConstruct() {
    LoggingUtils.debug(getClass(), "initialized " + this);
    if (dao == null) {
      LoggingUtils.error(getClass(), "error", new InstantiationException("dao is null!"));
    }
  }

  @Override
  public void scheduledChecks() {

  }

  @Override
  public DevInfo sendDevInfo(DevInfo devInfo) {
    DevInfoDs ds = null;
    if (devInfo.getDevUuid() != null) {
      ds = dao.findSingle(DevInfoDs.class, "devUuid == devUuidParam" ,
          Dao.Utils.buildParameters(new ParameterDefinition[] {
              new ParameterDefinition(String.class, "devUuidParam")
          }) , null, devInfo.getDevUuid());
    }
    if (ds == null) {
      ds = CloneUtils.clone(devInfo, DevInfoDs.class);
      LoggingUtils.debug(getClass(), "creating " + ds);
      ds = dao.create(ds);
    }
    return CloneUtils.clone(ds, DevInfoTx.class);
  }

  @Override
  public Account saveAccount(Account tx) {
    // controllo se arriva una create doppia dallo stesso device
    if (tx.getId() == null && tx.getDevInfoId() != null) {
      Key devInfoKey = KeyFactory.stringToKey(tx.getDevInfoId());
      AccountDs ds = dao.findSingle(AccountDs.class, "devInfoId == devInfoIdParam", 
          Dao.Utils.buildParameters(new ParameterDefinition[] {
              new ParameterDefinition(Key.class, "devInfoIdParam")
          }), 
        null, devInfoKey );
      if (ds != null) {
        tx.setId(ds.getId());
      }
    }
    AccountDs ds = CloneUtils.clone(tx, AccountDs.class);
    if (tx.getId() == null) {
      ds = dao.create(ds);
      LoggingUtils.debug(getClass(), "created account " + ds);
    } else {
      ds = dao.update(ds);
      LoggingUtils.debug(getClass(), "updated account " + ds);
    }
    return CloneUtils.clone (ds, AccountTx.class);
  }

  @Override
  public List<Timbro> getTimbri() throws Exception {
    if (cacheTimbri == null) {
      List<Timbro> timbri;
      List<TimbroDs> timbriDs = dao.findAll(TimbroDs.class);
      if (timbriDs == null || timbriDs.size() == 0) {
        timbri = AdapterUtil.getInitAdapterBean().getTimbri();
        for (Timbro timbro : timbri) {
          TimbroDs timbroDs = (TimbroDs)timbro;
          timbroDs = dao.create(timbroDs);
          timbriDs.add(timbroDs);
        }
      }
      timbri = new ArrayList<Timbro>();
      for (TimbroDs timbroDs : timbriDs) {
        timbri.add(timbroDs);
      }
      timbri = loadTimbriImages(timbri);
      cacheTimbri = timbri;
    }
    return cacheTimbri;
  }
  
  private List<Timbro> loadTimbriImages(List<Timbro> timbri) throws Exception {
    for (Timbro timbro : timbri) {
      LoggingUtils.debug(getClass(), "found " + timbro);
      String imageFileName = String.format("META-INF/setup-data/images/%s.jpg", timbro.getCodice() );
      File imageFile = getResourceFileAllowNull(imageFileName);
      LoggingUtils.debug(getClass(), "found file " + imageFile.getAbsolutePath());
      String imageText = inputStreamToBase64(new FileInputStream(imageFile));
      LoggingUtils.debug(getClass(), "content = " + imageText.substring(0, 200));
      timbro.setImage(imageText);
    }
    for (int ita = 0; ita < nonalfanum.length; ita++) {
      if (nonalfanum[ita] != 0) {
        LoggingUtils.debug(RestController.class, "FOUND NON ALFANUM CAR " + nonalfanum[ita]);
      }
    }
    return timbri;
  }
  
  private static File getResourceFileAllowNull(String filename) {
    File resourceFile = null;
    try {
      resourceFile = new ClassPathResource( filename ).getFile();
    } catch (Exception ex) {
      return null;
    }
    if (resourceFile == null || !resourceFile.exists()) {
      return null;
    }
    return resourceFile;
  }
  
  private static String inputStreamToBase64 (InputStream is) throws IOException {
    byte[] buf = new byte[1024];
    int len;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    while ((len = is.read(buf)) > -1) {
      baos.write(buf, 0, len);
    }
    byte[] content = Base64Utils.toBase64(baos.toByteArray()).getBytes();
    for (int it = 0; it < content.length; it++) {
      
      if (content[it] >= '0' && content[it] <= '9') {
      } else if (content[it] >= 'A' && content[it] <= 'Z') {
      } else if (content[it] >= 'a' && content[it] <= 'z') {
      } else {
        boolean found = false;
        for (int ita = 0; ita < nonalfanum.length; ita++) {
          if (content[it] == nonalfanum[ita]) {
            found = true;
            break;
          }
        }
        if (!found) {
          for (int ita = 0; ita < nonalfanum.length; ita++) {
            if (nonalfanum[ita] == 0) {
              nonalfanum[ita] = content[it];
              break;
            }
          }
        }
      }
      
      if (content[it] == '_') {
        content[it] = '/';
      }
      if (content[it] == '$') {
        content[it] = '+';
      }
    }
    return new String(content);
  }
  

  /** >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> **/
  
  @Override
  public void uploadOrderItemPreview(String orderItemId, byte[] previewImage) throws Exception {
    ByteArrayInputStream previewIS = new ByteArrayInputStream(previewImage);
    String previewB64 = inputStreamToBase64(previewIS);
    OrderItemDs orderItemDs = dao.findSingle(new FindContext<OrderItemDs>(OrderItemDs.class)
          .setFilter("remoteId == remoteIdParam")
          .setParameters(Dao.Utils.buildParameters(new ParameterDefinition[] { new ParameterDefinition(Key.class, "remoteIdParam")}))
          .setParamValues(new Object[]{KeyFactory.stringToKey(orderItemId)})
        );
    orderItemDs.setPreviewImage(previewB64);
    dao.update(orderItemDs);
  }
  
  @Override
  public Order saveOrder(Order order) {
    OrderDs orderDs = CloneUtils.clone(order, OrderDs.class);
    
    // REGISTRAZIONE ORDINE DAL DEVICE
    if (orderDs.getState() == Order.STATE_IN_CART) {
      orderDs.setState(Order.STATE_RECEIVED);
      String codice = DateUtils.dateToString(new Date(), "yyyy");
      codice += StringUtils.formatNumber(getNextCounterValue(), 4);
      orderDs.setCodice(codice);

    // AGGIORNAMENTI EFFETTUATI DA ADMIN
    } else {
      orderDs.setUpdateState("N");

      if (order.getAccount() != null) {
        try {
          LoggingUtils.debug(getClass(), "Sending push notification");
          sendPushNotification(order.getAccount(), "L'ordine " + order.getCodice() + " e' stato aggiornato", null);
        } catch (Exception ex) {
          LoggingUtils.error(getClass(), "Sending push notification error", ex);
        }
      } else {
        LoggingUtils.error(getClass(), "Cannot send push notification (order.account = null) ");
      }
      
    }
    orderDs.setLastUpdate(new Date());
    orderDs = createOrUpdateOrderDs(orderDs);
    return CloneUtils.clone (orderDs, OrderTx.class);
  }
  
  private OrderDs createOrUpdateOrderDs (OrderDs orderDs) {
    List<OrderItem> items = orderDs.getItems();
    if (items != null) {
      for (int it = 0; it < items.size(); it++) {
        OrderItemDs itemDs = (OrderItemDs)items.get(it);
        itemDs = createOrUpdateOrderItemDs(itemDs);
        items.set(it, itemDs);
      }
      orderDs.setItems(items);
    }
    if (orderDs.getKey() == null) {
      orderDs = dao.create(orderDs);
    } else {
      orderDs = dao.update(orderDs);
    }
    return orderDs;
  }
  
  private OrderItemDs createOrUpdateOrderItemDs (OrderItemDs itemDs) {
    List<OrderItemRow> rows = itemDs.getRows();
    if (rows != null) {
      for (int it = 0; it < rows.size(); it++) {
        OrderItemRowDs rowDs = (OrderItemRowDs)rows.get(it);
        rowDs = createOrUpdateOrderItemRowDs(rowDs);
        rows.set(it, rowDs);
      }
      itemDs.setRows(rows);
    }
    List<Message> messages = itemDs.getMessages();
    if (messages != null) {
      for (int it = 0; it < messages.size(); it++) {
        MessageDs messageDs = (MessageDs)messages.get(it);
        messageDs = createOrUpdateMessageDs(messageDs);
        messages.set(it, messageDs);
      }
      itemDs.setMessages(messages);
    }
    if (itemDs.getKey() == null) {
      itemDs = dao.create(itemDs);
    } else {
      itemDs = dao.update(itemDs);
    }
    return itemDs;
  }
  
  private OrderItemRowDs createOrUpdateOrderItemRowDs (OrderItemRowDs rowDs) {
    if (rowDs.getKey() == null) {
      rowDs = dao.create(rowDs);
    } else {
      rowDs = dao.update(rowDs);
    }
    return rowDs;
  }
  
  private MessageDs createOrUpdateMessageDs (MessageDs messageDs) {
    if (messageDs.getKey() == null) {
      messageDs = dao.create(messageDs);
    } else {
      messageDs = dao.update(messageDs);
    }
    return messageDs;
  }
  
  protected long getNextCounterValue () {
    long result = -1;
    List<CounterDs> results = dao.findAll(CounterDs.class);
    if (results != null && results.size() > 0) {
      CounterDs counter = results.get(0);
      result = counter.getValue();
      result ++;
      counter.setValue(result);
      counter = dao.update(counter);
    } else {
      result = 1;
      CounterDs counter = new CounterDs();
      counter.setValue(result);
      counter = dao.create(counter);
    }
    return result;
  }
  
  @Override
  public List<Order> findAllOrders() throws Exception {
    List<OrderDs> ordersDs = dao.findAll(OrderDs.class, new FindCallback<OrderDs>() {
      public void processResultsInTransaction(OrderDs entity) {
        entity.itemKeysTraverse();
      }
    });
    return CloneUtils.clone(ordersDs, OrderTx.class, Order.class);
  }
  
  @Override
  public Order findOrderById(String id) throws Exception {
    OrderDs ds = dao.findById(OrderDs.class, id);
    return CloneUtils.clone(ds, OrderTx.class);
  }

  @Override
  public List<Order> findUpdatedOrdersByAccount(String accountId) throws Exception {
    List<OrderDs> orders = dao.findList(new FindContext<OrderDs>(OrderDs.class).setResultAsList(true)
        .setFilter("accountKey == accountKeyParam && updateState == updateStateParam")
        .setParameters(Dao.Utils.buildParameters(new ParameterDefinition[] { new ParameterDefinition(Key.class, "accountKeyParam"), new ParameterDefinition(String.class, "updateStateParam")}))
        .setParamValues(new Object[]{KeyFactory.stringToKey(accountId), "N"})
        .includedField("itemKeys")
      );

    if (orders != null) {

      // NB: non risettare l'ordine nella lista perche il ritorno dell'update non ha le righe
      for (OrderDs order : orders) {
        order.setUpdateState("U");
        order = dao.update(order);
      }
      
    }
    
    return CloneUtils.clone(orders, OrderTx.class, Order.class);
  }
  
  @Override
  public List<Order> findOrdersByAccount(String accountId, Date lastUpdateOnDevice) throws Exception {
    
    List<OrderDs> ordersDs = dao.findList(new FindContext<OrderDs>(OrderDs.class).setResultAsList(true)
          .setFilter("accountKey == accountKeyParam")
          .setParameters(Dao.Utils.buildParameters(new ParameterDefinition[] { new ParameterDefinition(Key.class, "accountKeyParam")}))
          .setParamValues(new Object[]{KeyFactory.stringToKey(accountId)})
          .includedField("itemKeys")
        );

    if (lastUpdateOnDevice != null) {
      
      String logMessage = "Ricerca ordini da aggiornare sul device\n";
      
      for (Iterator<OrderDs> it = ordersDs.iterator(); it.hasNext();) {
        OrderDs order = it.next();
        logMessage += String.format("Device lastUpdateTime = %s - Order %s lastUpdateTime = %s - ", lastUpdateOnDevice.getTime(), order.getCodice(), order.getLastUpdate().getTime());
        if (order.getLastUpdate() != null && (order.getLastUpdate().before(lastUpdateOnDevice) || order.getLastUpdate().equals(lastUpdateOnDevice) )) {
          it.remove();
          logMessage += "AGGIORNATO";
        } else {
          logMessage += "DA AGGIORNARE";
        }
        logMessage += "\n";
      }
      
      LoggingUtils.debug(getClass(), logMessage);
      
    }
    
    return CloneUtils.clone(ordersDs, OrderTx.class, Order.class);
  }
  
  @Override
  public List<Account> findAllAccounts() throws Exception {
    List<AccountDs> dss = dao.findAll(AccountDs.class);
    return CloneUtils.clone(dss, AccountTx.class, Account.class);
  }
  
  @Override
  public void sendPushNotification(Account account, String message, String regId) throws Exception {
    
    if (regId != null && regId.trim().length() > 0) {
      account.setPushNotifRegId(regId);
      AccountDs ds = CloneUtils.clone(account, AccountDs.class);
      dao.update(ds);
    }
    
    try {
      
      URL url = new URL("https://android.googleapis.com/gcm/send");
      
      HttpURLConnection conn = (HttpURLConnection)url.openConnection();
      
      conn.setInstanceFollowRedirects(false);
      conn.setDoOutput(true);
      conn.setRequestMethod("POST");
      conn.setDoInput(true);
      
      conn.setRequestProperty("Authorization", "key=" + PUSH_GCM_SERVER_KEY);
//    conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
      
      OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
      writer.write(String.format("registration_id=%s&data.message=%s", account.getPushNotifRegId(), message));
      writer.close();
      
      if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

        String responseText = "";
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = null;
        while ((line = reader.readLine()) != null) {
          responseText += line;
        }
        reader.close();
        
        LoggingUtils.debug(getClass(), "Received from GCM: " + responseText);
        
      } else {
        LoggingUtils.debug(getClass(), "HTTP ERROR Received from GCM: " + conn.getResponseCode());
      }
      
    } catch (Exception ex) {
      throw ex;
    }
    
  }
  
}
