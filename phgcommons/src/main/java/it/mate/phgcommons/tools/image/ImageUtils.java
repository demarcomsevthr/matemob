package it.mate.phgcommons.tools.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;


public class ImageUtils {
  
  private static String USER_DIR = System.getProperty("user.dir");
  
  public static void main(String[] args) {
    
    ImageUtils utils = new ImageUtils();
    try {
//    utils.invertColors("file1.bmp", "bmp");
      utils.invertColors("file2.png", "png");
//    utils.invertColors("file3.png", "png");
//    utils.invertColors("file4.png", "png");
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    
  }
  
  public void invertColors(String filename, String format) throws Exception {
    
    BufferedImage imageIn = ImageIO.read(new File(USER_DIR+"\\extras\\test-data\\"+filename));
    System.out.println("read image: type "+imageIn.getType()+" "+ imageIn.getWidth()+" x "+imageIn.getHeight());
    
    BufferedImage imageOut = new BufferedImage(imageIn.getWidth(), imageIn.getHeight(), imageIn.getType());
    
    List<Color> colors = new ArrayList<Color>();

    for (int iy = 0; iy < imageIn.getHeight(); iy++) {
      
      String line = "LINE " + iy + " - ";
      
      for (int ix = 0; ix < imageIn.getWidth(); ix++) {

        int rgbIn = imageIn.getRGB(ix, iy);
        Color colorIn = null;
        int iType = imageIn.getType();
        if (iType == BufferedImage.TYPE_INT_BGR || iType == BufferedImage.TYPE_3BYTE_BGR || iType == BufferedImage.TYPE_4BYTE_ABGR) {
          colorIn = new Bgr(rgbIn);
        } else {
          colorIn = new Rgb(rgbIn);
        }
        line += "("+rgbIn+","+colorIn.getRed()+","+colorIn.getGreen()+","+colorIn.getBlue()+","+colorIn.getValue()+") ";
        
        Color colorOut = colorIn.clone();
        colorOut.setRed(invertColor(colorIn.getRed()));
        colorOut.setGreen(invertColor(colorIn.getGreen()));
        colorOut.setBlue(invertColor(colorIn.getBlue()));
        imageOut.setRGB(ix, iy, colorOut.getValue());

        boolean found = false;
        for (Color aColor : colors) {
          if (aColor.equalsColors(colorIn)) {
            found = true;
            break;
          }
        }
        if (!found) {
          colors.add(colorIn);
        }

      }
      
      System.out.println(line);
      
    }
    
    for (Color color : colors) {
      System.out.println("color=" + color);
    }
    
    System.out.println("WRITING OUTPUT...");
    File out = new File(USER_DIR+"\\target\\out"+filename);
    ImageIO.write(imageOut, format, out);
    
  }
  
  public static int invertColor(int color) {
    return 255 - color;
  }
  
  public abstract class Color {
    protected int alpha;
    protected int red;
    protected int green;
    protected int blue;
    public Color() {
    }
    public String toString() {
      return "Rgb [alpha=" + alpha + ", red=" + red + ", green=" + green + ", blue=" + blue + "]";
    }
    public boolean equalsColors(Color that) {
      return this.red == that.red && this.green == that.green && this.blue == that.blue;
    }
    public boolean equalsAlpha(Color that) {
      return this.alpha == that.alpha;
    }
    public boolean equals(Color that) {
      return equalsColors(that) && equalsAlpha(that);
    }
    public int getAlpha() {
      return alpha;
    }
    public int getRed() {
      return red;
    }
    public int getGreen() {
      return green;
    }
    public int getBlue() {
      return blue;
    }
    public void setAlpha(int alpha) {
      this.alpha = alpha;
    }
    public void setRed(int red) {
      this.red = red;
    }
    public void setGreen(int green) {
      this.green = green;
    }
    public void setBlue(int blue) {
      this.blue = blue;
    }
    public abstract Color clone();
    public abstract int getValue();
  }
  
  public class Rgb extends Color {
    public Rgb(int rgb) {
      alpha = (rgb >> 24) & 0xff;
      red = (rgb >> 16) & 0xff;
      green = (rgb >> 8) & 0xff;
      blue = (rgb) & 0xff;
    }
    public Rgb(int alpha, int red, int green, int blue) {
      this.alpha = alpha;
      this.red = red;
      this.green = green;
      this.blue = blue;
    }
    public int getValue() {
      return (alpha << 24) | (red << 16) | (green << 8) | (blue);
    }
    public Color clone() {
      return new Rgb(getValue());
    }
  }
  
  public class Bgr extends Color {
    public Bgr(int bgr) {
      alpha = (bgr >> 24) & 0xff;
      blue = (bgr >> 16) & 0xff;
      green = (bgr >> 8) & 0xff;
      red = (bgr) & 0xff;
    }
    public Bgr(int alpha, int red, int green, int blue) {
      this.alpha = alpha;
      this.red = red;
      this.green = green;
      this.blue = blue;
    }
    public int getValue() {
      return (alpha << 24) | (blue << 16) | (green << 8) | (red);
    }
    public Color clone() {
      return new Rgb(getValue());
    }
  }
  

}
