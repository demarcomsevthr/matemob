package it.mate.commons.server.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;

import com.google.appengine.api.datastore.Blob;

public class BlobUtils {
  
  public static Blob inputStreamToBlob (InputStream is) throws IOException {
    byte[] buf = new byte[1024];
    int len;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    while ((len = is.read(buf)) > -1) {
      baos.write(buf, 0, len);
    }
    return new Blob(baos.toByteArray());
  }
  
  public static String blobToString(Blob blob) {
    return new String(Base64.encodeBase64(blob.getBytes()));
  }
  
  public static Blob stringToBlob(String base64String) {
    return new Blob(Base64.decodeBase64(base64String.getBytes()));
  }

}
