package it.mate.phgcommons.client.utils;

import com.google.gwt.user.client.Window;
//import com.googlecode.mgwt.ui.client.MGWT;

public class OsDetectionUtils {
  
  public final static int IPHONE_WIDTH = 320;
  public final static int IPHONE_HEIGHT = 480;
  public final static int IPHONE_3INCH_HEIGHT = 480;
  public final static int IPHONE_4INCH_HEIGHT = 568;
  public final static int IPAD_LAND_WIDTH = 1024;
  public final static int IPAD_LAND_HEIGHT = 748;
  public final static int IPAD_PORT_WIDTH = 768;
  public final static int IPAD_PORT_HEIGHT = 1004;
  
  public static int IOS_HEADER_PANEL_HEIGHT = 40;
  public static int IOS_MARGIN_TOP = 18;
  
  public final static int APAD_PORT_WIDTH = 600;
  public final static int APAD_PORT_HEIGHT = 1024;
  public final static int APAD_LAND_WIDTH = APAD_PORT_HEIGHT;
  public final static int APAD_LAND_HEIGHT = APAD_PORT_WIDTH;
  
  private static int getDisplayHeight() {
    return Window.getClientHeight();
  }
  
  private static int getDisplayWidth() {
    return Window.getClientWidth();
  }
  
  public static boolean isPhone() {
    return !isTablet();
  }
  
  public static boolean isPhoneLandscape() {
    return isPhone() && (Window.getClientHeight() < Window.getClientWidth());
  }
  
  public static boolean isTablet() {
    return (isTabletLandscape() || isTabletPortrait());
  }
  
  public static boolean isIOs() {
    return PhgUtils.getDevicePlatform().toLowerCase().contains("ios");
  }

  public static boolean isAndroid() {
    return PhgUtils.getDevicePlatform().toLowerCase().contains("android");
  }
  
  public static boolean isDesktop() {
    return Window.Navigator.getUserAgent().toLowerCase().contains("windows nt");
  }
  
  public static boolean is3Inch() {
    int dh = getDisplayHeight();
    if (isIOs() && dh == IPHONE_3INCH_HEIGHT) {
      return true;
    }
    return false;
  }

  public static boolean is4Inch() {
    int dh = getDisplayHeight();
    if (isIOs() && dh == IPHONE_4INCH_HEIGHT) {
      return true;
    }
    return false;
  }

  /*
   * NOTA BENE
   * Utilizzo le condizioni >= per poter debuggare in desktop
   * 
   */
  
  public static boolean isTabletLandscape() {
    if (isIOs()) {
      return (getDisplayWidth() >= IPAD_LAND_WIDTH && getDisplayHeight() >= IPAD_LAND_HEIGHT);
    }
    if (isAndroid()) {
      return (getDisplayWidth() >= APAD_LAND_WIDTH && getDisplayHeight() >= APAD_LAND_HEIGHT);
    }
    return false;
  }
  
  public static boolean isTabletPortrait() {
    if (isIOs()) {
      return (getDisplayWidth() >= IPAD_PORT_WIDTH && getDisplayHeight() >= IPAD_PORT_HEIGHT);
    }
    if (isAndroid()) {
      return (getDisplayWidth() >= APAD_PORT_WIDTH && getDisplayHeight() >= APAD_PORT_HEIGHT);
    }
    return false;
  }
  
  public static String getOsVersion() {
    String ver = PhgUtils.getDeviceVersion();
    if (ver == null) {
      ver = "";
    }
    return ver;
  }
  
  public static boolean isOsVersionLessThanOrEqual(String compares) {
    return PhgUtils.getDeviceVersion().compareTo(compares) <= 0;
  }
  
  public static boolean isOsVersionGreatThanOrEqual(String compares) {
    return PhgUtils.getDeviceVersion().compareTo(compares) >= 0;
  }
  
}
