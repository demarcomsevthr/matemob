package it.mate.phgcommons.client.ui;

import it.mate.phgcommons.client.utils.OsDetectionUtils;
import it.mate.phgcommons.client.utils.PhgUtils;

import com.google.gwt.user.client.ui.TextBox;
import com.googlecode.mgwt.ui.client.theme.base.InputCss;
import com.googlecode.mgwt.ui.client.widget.MTextBox;

public class MTextBoxPatched extends MTextBox {

  public MTextBoxPatched() {
    super();
    applyPatch();
  }

  public MTextBoxPatched(InputCss css, TextBox textBox) {
    super(css, textBox);
    applyPatch();
  }

  public MTextBoxPatched(InputCss css) {
    super(css);
    applyPatch();
  }

  // 15/01/2014 - ANDROID 4.0.x INPUT FIELD ISSUE
  // Mgwt imposta la property webkitUserModify per non mostrare l'outline al focus sugli input fields.
  // Da prove fatte sul device riscontro che (almeno fino alla 4.0.3) questo da problemi con
  // la keyboard (non si vede il singolo keypress ed altro...)
  // Quindi disabilito questo settaggio
  // TODO: da testare su device 4.1+ e IOS
  private void applyPatch() {
    if (OsDetectionUtils.isAndroid()) {
      String version = PhgUtils.getDeviceVersion().trim();
      if (version.startsWith("2") || version.startsWith("3") || version.startsWith("4.0")) {
        box.getElement().getStyle().setProperty("webkitUserModify", "initial");
      }
    }
  }
  
  public void setType(String type) {
    if ("password".equalsIgnoreCase(type)) {
      box.getElement().setAttribute("type", "password");
    }
  }

}
