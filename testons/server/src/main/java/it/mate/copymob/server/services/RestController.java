package it.mate.testons.server.services;

import it.mate.commons.server.utils.LoggingUtils;
import it.mate.testons.shared.model.Timbro;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gwt.user.server.Base64Utils;

@Controller
public class RestController {
  
  @Autowired private MainAdapter adapter;

  @PostConstruct
  public void onPostConstruct() {
    LoggingUtils.debug(getClass(), "initialized " + this);
    LoggingUtils.debug(getClass(), "adapter = " + adapter);
  }

  @RequestMapping ("/scheduledChecks")
  public void scheduledChecks(HttpServletResponse response) throws Exception {
    LoggingUtils.debug(getClass(), "adapter scheduled checks start");
    adapter.scheduledChecks();
    response.getOutputStream().print("Adapter scheduled checks started");
  }
  
  @RequestMapping ("/initTimbri")
  public void initTimbri(HttpServletResponse response) throws Exception {
    LoggingUtils.debug(getClass(), "initTimbri operation requested");
    
    List<Timbro> timbri = AdapterUtil.getInitAdapterBean().getTimbri();
    
    for (Timbro timbro : timbri) {
      LoggingUtils.debug(getClass(), "found " + timbro);
      String fileName = String.format("META-INF/setup-data/images/%s.jpg", timbro.getCodice() );
      File imageFile = getResourceFileAllowNull(fileName);
      LoggingUtils.debug(getClass(), "found file " + imageFile.getAbsolutePath());
      String text = inputStreamToBase64(new FileInputStream(imageFile));
      LoggingUtils.debug(getClass(), "content = " + text);
    }
    
    for (int ita = 0; ita < nonalfanum.length; ita++) {
      if (nonalfanum[ita] != 0) {
        LoggingUtils.debug(RestController.class, "FOUND NON ALFANUM CAR " + nonalfanum[ita]);
      }
    }
    
    response.getOutputStream().print("Init Timbri executed");
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
  
  private static byte[] nonalfanum = new byte[48];
  
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
  
}
