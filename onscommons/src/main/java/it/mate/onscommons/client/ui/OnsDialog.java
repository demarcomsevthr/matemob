package it.mate.onscommons.client.ui;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.gwtcommons.client.utils.JQuery;
import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.onscommons.client.onsen.dom.Dialog;
import it.mate.phgcommons.client.utils.EventUtils;
import it.mate.phgcommons.client.utils.PhgUtils;
import it.mate.phgcommons.client.utils.callbacks.JSOCallback;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class OnsDialog extends HTMLPanel implements AcceptsOneWidget {

  private final static String TAG_NAME = "ons-dialog";
  
  private String controllerId;
  
  private Dialog dialog;
  
  private HandlerRegistration modalHandlerRegistration = null;
  
  public OnsDialog() {
    this("");
  }
  
  public OnsDialog(String html) {
    super(TAG_NAME, html);
    OnsenUi.ensureId(getElement());
    initControllerId();
  }
  
  private void initControllerId() {
    controllerId = GwtUtils.replaceEx(getElement().getId(), "-", "") + "Dialog" ;
    PhgUtils.log("OnsDialog::initControllerId - controllerId = " + controllerId);
    getElement().setAttribute("var", controllerId);
  }
  
  public String getControllerId() {
    return controllerId;
  }
  
  @Override
  @SuppressWarnings("deprecation")
  public void add(Widget widget) {
    super.add(widget, getElement());
    if (widget.getElement().getNodeName().toLowerCase().startsWith("ons")) {
      OnsenUi.compileElement(widget.getElement());
    }
  }

  @Override
  public void setWidget(IsWidget w) {
    add(w);
  }
  
  @Override
  protected void onUnload() {
    removeModalHandler();
    super.onUnload();
  }
  
  private void removeModalHandler() {
    if (modalHandlerRegistration != null) {
      EventUtils.removeModalHandler(modalHandlerRegistration);
    }
  }
  
  public void show () {
    show(null, null, false, true, false);
  }
  
  public void show (Widget content, final JavaScriptObject options, final boolean cancelable) {
    show(content, options, cancelable, false, true);
  }
  
  public void show (Widget content, final JavaScriptObject options, final boolean cancelable, final boolean checkDialogAvailable, final boolean compileImmediately) {
    if (dialog != null) {
      showDialogImpl(dialog);
      return;
    }
    if (content != null) {
      this.add(content);
    }
    final Delegate<Void> dialogAvailableDelegate = new Delegate<Void>() {
      public void execute(Void element) {
        final String templateId = getControllerId() + "Template" ;
        if (OnsenUi.isVersion2()) {
          setCancelable(cancelable);
          String templateHTML = GwtUtils.getOuterHtml(OnsDialog.this.getElement());
          GwtUtils.log("dialog show - templateID = " + templateId);
          GwtUtils.log("dialog show - templateHTML = " + templateHTML);
          GwtUtils.log("dialog show - controllerId = " + OnsDialog.this.getControllerId());
          OnsenUi.addCachedTemplate(templateId, templateHTML);
          GwtUtils.deferredExecution(new Delegate<Void>() {
            public void execute(Void element) {
              createAndShowDialogImpl(templateId, OnsDialog.this.getControllerId(), new JSOCallback() {
                public void handle(JavaScriptObject obj) {
                  GwtUtils.log("createAndShowDialogImpl.callback");
                  Dialog dialog = obj.cast();
                  OnsDialog.this.dialog = dialog;
                }
              });
            }
          });
        } else {
          OnsTemplate template = new OnsTemplate(templateId);
          template.add(OnsDialog.this);
          RootPanel.get().add(template);
          GwtUtils.onAvailable(templateId, new Delegate<Element>() {
            public void execute(final Element templateElem) {
              if (compileImmediately) {
                OnsenUi.compileElementImmediately(templateElem);
              } else {
                OnsenUi.compileElement(templateElem);
              }
              GwtUtils.deferredExecution(new Delegate<Void>() {
                public void execute(Void element) {
                  createAndShowDialogImpl(templateElem.getId(), OnsDialog.this.getControllerId(), new JSOCallback() {
                    public void handle(JavaScriptObject jso) {
                      OnsDialog.this.dialog = jso.cast();
                      setCancelable(cancelable);
                    }
                  });
                }
              });
            }
          });
        }
      }
    };
    if (checkDialogAvailable) {
      final String dialogId = OnsDialog.this.getElement().getId();
      GwtUtils.onAvailable(dialogId, new Delegate<Element>() {
        public void execute(Element dialogElem) {
          dialogAvailableDelegate.execute(null);
        }
      });
    } else {
      dialogAvailableDelegate.execute(null);
    }
  }
  
  public void getRealWidth(final Delegate<Integer> delegate) {
    GwtUtils.onAvailable(getElement(), new Delegate<Element>() {
      public void execute(Element dialogElement) {
        for (int it = 0; it < dialogElement.getChildCount(); it++) {
          Element child = dialogElement.getChild(it).cast();
          if (child.getTagName() != null && child.getTagName().toLowerCase().contains("div") && 
              child.getClassName() != null && child.getClassName().toLowerCase().trim().equals("dialog")) {
            delegate.execute(child.getOffsetWidth());
          }
        }
      }
    });
  }
  
  public void setCancelable(final boolean cancelable) {
    if (OnsenUi.isVersion2()) {
      this.getElement().setAttribute("cancelable", ""+cancelable);
      if (!cancelable) {
        EventUtils.createModalHandler(this, new Delegate<HandlerRegistration>() {
          public void execute(HandlerRegistration registration) {
            modalHandlerRegistration = registration;
          }
        });
      }
    } else {
      GwtUtils.deferredExecution(new Delegate<Void>() {
        public void execute(Void element) {
          getDialogObject().setCancelable(cancelable);;
        }
      });
    }
  }
  
  protected static native void showDialogImpl(String id, JavaScriptObject options) /*-{
    if ($wnd[id] != undefined) {
      $wnd[id].show(options);
    }
  }-*/;
  
  public void addOnHideDelegate(final Delegate<JavaScriptObject> delegate) {
    GwtUtils.onAvailable(getElement(), new Delegate<Element>() {
      public void execute(Element element) {
        onHideDialogImpl(getControllerId(), new JSOCallback() {
          public void handle(JavaScriptObject event) {
            delegate.execute(event);
            removeDialogFromParent();
          }
        });
      }
    });
  }
  
  protected static native void onHideDialogImpl(String id, JSOCallback callback) /*-{
    var jsCallback = $entry(function(event) {
      callback.@it.mate.phgcommons.client.utils.callbacks.JSOSuccess::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(event);
    });
    $wnd[id].on('posthide', jsCallback);
  }-*/;

  protected static native void setCancelableImpl(String id, boolean cancelable) /*-{
    $wnd[id].setCancelable(cancelable);
  }-*/;

  public void hide() {
    getDialogObject().hide();
    removeDialogFromParent();
    removeModalHandler();
  }  
  
  private void removeDialogFromParent() {
    OnsenUi.onAvailableElement(getElement(), new Delegate<Element>() {
      public void execute(Element element) {
        element.removeFromParent();
      }
    });
  }
  
  protected static native void createDialogImpl(String templateId) /*-{
    $wnd.ons.createDialog(templateId).then(function(dialog) {
  
    });
  }-*/;
  
  protected static native void createAndShowDialogImpl(String templateId, String varName, JSOCallback callback) /*-{
    $wnd.ons.createDialog(templateId).then(function(dlg) {
      @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)('createAndShowDialogImpl - dialog created with varName = ' + varName);
      var dialog = $wnd[varName];
      @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)('createAndShowDialogImpl - showing dialog ' + dialog);
      dialog.show();
      callback.@it.mate.phgcommons.client.utils.callbacks.JSOCallback::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(dialog);
    });
  }-*/;
  
  protected static native void showDialogImpl(Dialog dialog) /*-{
    dialog.show();
  }-*/;

  public Dialog getDialogObject() {
    return getDialogObjectImpl(controllerId);
  }

  protected static native Dialog getDialogObjectImpl(String varName) /*-{
    return $wnd[varName];
  }-*/;
  
  public void applyAntiFontBlurCorrection() {
    OnsenUi.onAvailableElement(this, new Delegate<Element>() {
      public void execute(Element element) {
        final Element dialogElem = JQuery.select("#"+getElement().getId()+" .dialog").firstElement();
        final int antiFontBlurCorrectionTop = dialogElem.getAbsoluteTop();
        final int antiFontBlurCorrectionLeft = dialogElem.getAbsoluteLeft();
        GwtUtils.deferredExecution(200, new Delegate<Void>() {
          public void execute(Void _v) {
            PhgUtils.log("applyAntiFontBlurCorrection: setting top="+antiFontBlurCorrectionTop + " left="+antiFontBlurCorrectionLeft);
            dialogElem.getStyle().setTop(antiFontBlurCorrectionTop, Unit.PX);
            dialogElem.getStyle().setLeft(antiFontBlurCorrectionLeft, Unit.PX);
            dialogElem.addClassName("dialog_anti_font_blur_correction");
          }
        });
      }
    });
  }
  
}
