package it.mate.testons.tools;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class GenerateIngredientFiles {
  
  static final String CHARACTERS = "0123456789QWERTYUIOPASDFGHJKLZXCVBNM";
  
  public static void main(String[] args) {
    
    GenerateIngredientFiles generator = new GenerateIngredientFiles();
    
    try {
      generator.writeFile(1, 1024);
      generator.writeFile(2, 1024);
      generator.writeFile(3, 1024);
      generator.writeFile(4, 1024);
      generator.writeFile(5, 1024);
      generator.writeFile(6, 1024);
      generator.writeFile(7, 1024);
      generator.writeFile(8, 1024);
      generator.writeFile(9, 1024);
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    
  }
  
  private void writeFile(int num, int size) throws Exception {
    Writer writer = null;
    try {
      String filename = "target/file"+num;
      writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename+".data"), "utf-8"));
      for (int it = 0; it < size; it++) {
        char ch = CHARACTERS.charAt((int)(Math.random() * CHARACTERS.length()));
        writer.append(ch);
      }
      writer.close();
      
      byte[] buffer = new byte[1024];
      ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(filename+".zdat"));
      ZipEntry ze= new ZipEntry("file"+num+".data");
      zos.putNextEntry(ze);
      FileInputStream in = new FileInputStream(filename+".data");
      int len;
      while ((len = in.read(buffer)) > 0) {
        zos.write(buffer, 0, len);
      }
      in.close();
      zos.closeEntry();
      zos.close();      
      
      System.out.println("generated file " + num);
    } finally {
      try {
        writer.close();
      } catch (Exception ex) {}
    }
  }

}
