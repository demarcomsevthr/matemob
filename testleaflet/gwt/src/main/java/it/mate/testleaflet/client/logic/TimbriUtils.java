package it.mate.testleaflet.client.logic;

import it.mate.testleaflet.client.activities.MainActivity;
import it.mate.testleaflet.client.factories.AppClientFactory;
import it.mate.testleaflet.shared.model.Timbro;
import it.mate.testleaflet.shared.model.impl.TimbroTx;
import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.shared.rpc.RpcMap;
import it.mate.phgcommons.client.plugins.FileSystemPlugin;
import it.mate.phgcommons.client.utils.OsDetectionUtils;
import it.mate.phgcommons.client.utils.PhgUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TimbriUtils {

  private MainDao dao = (MainDao)AppClientFactory.IMPL.getGinjector().getMainDao();
  
  private final static int NUMBER_OF_ITEMS = 5;
  
  private final static String dataPath = "www/main/data";
  
  private final static boolean LOAD_TIMBRI_FROM_CLOUD = true;
  
  private static boolean initializationInProgress = false;
  
  public static void doRun() {
    new TimbriUtils().run();
  }
  
  protected TimbriUtils() {  }

  protected void run() {
    
    dao.findAllTimbri(new Delegate<List<Timbro>>() {
      public void execute(List<Timbro> timbri) {
        if (timbri == null || timbri.size() == 0) {
          
          if (initializationInProgress) {
            return;
          }
          
          initializationInProgress = true;
          
          if (LOAD_TIMBRI_FROM_CLOUD) {
            PhgUtils.log("RELOADING TIMBRI FROM SERVER...");
            loadTimbriFromCloudServer(new Delegate<List<Timbro>>() {
              public void execute(List<Timbro> timbri) {
                if (timbri != null) {
                  iterateTimbriForSave(timbri.iterator(), new Delegate<Void>() {
                    public void execute(Void element) {
                      initializationInProgress = false;
                    }
                  });
                } else {
                  initializationInProgress = false;
                }
                /*
                for (Timbro timbro : timbri) {
                  dao.saveTimbro(timbro, new Delegate<Timbro>() {
                    public void execute(Timbro element) {
                      
                    }
                  });
                }
                */
              }
            });
          } else {
            iterateDataFiles(0, new ArrayList<Timbro>(), new Delegate<List<Timbro>>() {
              public void execute(List<Timbro> results) {
                for (Timbro timbro : results) {
                  dao.saveTimbro(timbro, new Delegate<Timbro>() {
                    public void execute(Timbro element) {
                      
                    }
                  });
                }
              }
            });
            
            initializationInProgress = false;
            
          }
          
        }
      }
    });
    
  }
  
  private void iterateTimbriForSave(final Iterator<Timbro> it, final Delegate<Void> delegate) {
    if (it.hasNext()) {
      Timbro timbro = it.next();
      dao.saveTimbro(timbro, new Delegate<Timbro>() {
        public void execute(Timbro savedTimbro) {
          iterateTimbriForSave(it, delegate);
        }
      });
    } else {
      delegate.execute(null);
    }
  }
  
  
  private void loadTimbriFromCloudServer(final Delegate<List<Timbro>> delegate) {
    MainActivity.setWaitingState(true);
    AppClientFactory.IMPL.getRemoteFacade().getTimbri(new AsyncCallback<List<RpcMap>>() {
      public void onSuccess(List<RpcMap> results) {
        MainActivity.setWaitingState(false);
        if (results != null) {
          List<Timbro> timbri = new ArrayList<Timbro>();
          for (RpcMap map : results) {
            TimbroTx timbro = new TimbroTx().fromRpcMap(map);
            timbri.add(timbro);
          }
          delegate.execute(timbri);
        } else {
          PhgUtils.log("GET TIMBRI FROM CLOUD EMPTY RESULTS!");
        }
      }
      public void onFailure(Throwable caught) {
        MainActivity.setWaitingState(false);
        PhgUtils.log("GET TIMBRI FROM CLOUD FAILURE!");
        caught.printStackTrace();
      }
    });
  }
  
  
  private Timbro createTimbro(int index, String imgData) {
    int n = index + 1;
    Timbro result = new TimbroTx();
    result.setCodice("T" + n);
    result.setNome("TIMBRO " + n);
    result.setImage(imgData);
    result.setWidth(1200d);
    result.setHeight(800d);
    result.setOval(false);
    return result;
  }
  
  private void iterateDataFiles(final int it, final List<Timbro> results, final Delegate<List<Timbro>> endDelegate) {
    if (it < NUMBER_OF_ITEMS) {
      if (OsDetectionUtils.isDesktop()) {
        readFromLocalhost("http://127.0.0.1:8888/.image?name=timbro"+it+".jpg", new Delegate<String>() {
          public void execute(String imgData) {
            results.add(createTimbro(it, imgData));
            iterateDataFiles(it + 1, results, endDelegate);
          }
        });
      } else {
        if (FileSystemPlugin.isInstalled()) {
          String fileName = "timbro" + it + ".jpg";
          FileSystemPlugin.readApplicationFileAsEncodedData(dataPath + "/" + fileName, new Delegate<String>() {
            public void execute(String imgData) {
              if (imgData == null) {
                PhgUtils.log("RESULT NULL");
              } else {
                PhgUtils.log("READ: " + imgData.substring(0, 200));
              }
              results.add(createTimbro(it, imgData));
              iterateDataFiles(it + 1, results, endDelegate);
            }
          });
        } else {
          PhgUtils.log("FILE SYSTEM PLUGIN NOT INSTALLED!");
        }
      }
    } else {
      endDelegate.execute(results);
    }
  }
  
  /**
   * 
   * PER CONTROLLARE LA CORRETTEZZA DEL B64 UTILIZZARE:
   * 
   * http://www.motobit.com/util/base64-decoder-encoder.asp
   * 
   */
  
  public static void readFromLocalhost(String uri, final Delegate<String> imageDelegate) {
    RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, uri);
    rb.setHeader("Content-Type", "application/base64");
    try {
      rb.sendRequest(null, new RequestCallback() {
        public void onResponseReceived(Request request, Response response) {
          if (200 == response.getStatusCode()) {
//          String imgB64 = Base64UtilsClient.toBase64(response.getText().getBytes());
//          String imgB64 = PhgUtils.btoa(response.getText());
            String imgB64 = response.getText();
            
            /*
            imgB64 = GwtUtils.replaceEx(imgB64, "_", "/");
            imgB64 = GwtUtils.replaceEx(imgB64, "$", "+");
            */
            
            PhgUtils.log("RECEIVED " + imgB64);
            imageDelegate.execute(imgB64);
          } else {
            PhgUtils.log("status code error " + response.getStatusCode() + " - " + response.getStatusText());
          }
        }
        @Override
        public void onError(Request request, Throwable exception) {
          PhgUtils.log("response error");
        }
      });
    } catch (RequestException ex) {
      PhgUtils.log("request error");
    }
  }
  
  public static String encodeImageData(String fileContent, String type) {
    if (type == null) {
      type = "jpeg";
    }
    return !fileContent.startsWith("data:") ? ("data:image/"+type+";base64," + fileContent) : fileContent;
  }
  
}
