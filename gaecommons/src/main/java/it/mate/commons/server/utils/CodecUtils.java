package it.mate.commons.server.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

public class CodecUtils {
  
  public static String encryptMD5 (String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] md5Pwd = md.digest(text.getBytes("UTF-8"));
    byte[] b64Pwd = Base64.encodeBase64(md5Pwd);
    String result = new String(b64Pwd);
    return result;
  }

}
