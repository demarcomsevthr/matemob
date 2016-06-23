package it.mate.testons.server.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gwt.user.server.Base64Utils;

@SuppressWarnings("serial")
public class ImageServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    
    String name = req.getParameter("name");
    
    String currentDir = System.getProperty("user.dir");
    System.out.println("Current dir using System:" +currentDir);

    File file = new File(currentDir+"\\main\\data\\" + name);
    
    System.out.println("found file " + file.getAbsolutePath());
    
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    
    FileInputStream stream = new FileInputStream(file);
    int read = 0;
    byte[] buffer = new byte[1024];
    while ( (read = stream.read(buffer, 0, 1024) ) != -1 ) {
      baos.write(buffer, 0, read);
    }
    
    String b64 = Base64Utils.toBase64(baos.toByteArray());
    
    b64 = b64.replace("_", "/");
    b64 = b64.replace("$", "+");
    
    resp.setContentType("application/base64");
    
    resp.getOutputStream().write(b64.getBytes());

  }

}
