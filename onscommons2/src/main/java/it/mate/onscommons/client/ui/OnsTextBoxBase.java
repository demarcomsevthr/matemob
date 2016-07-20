package it.mate.onscommons.client.ui;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.onscommons.client.event.NativeGestureEvent;
import it.mate.onscommons.client.event.NativeGestureHandler;
import it.mate.onscommons.client.event.OnsEventUtils;
import it.mate.onscommons.client.onsen.OnsenUi;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.text.shared.testing.PassthroughParser;
import com.google.gwt.text.shared.testing.PassthroughRenderer;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ValueBoxBase;

public abstract class OnsTextBoxBase extends /* Widget */ ValueBoxBase<String>{
  
  private HandlerRegistration onsChangeHandler = null;
  
  private String actualType = "text";
  
  private Element inputElement;
  
  protected OnsTextBoxBase(String type) {
    this(createInternalElement(), type, "ons-textbox");
//  this(DOM.createInputText(), type, "ons-textbox");
  }
  
  private static Element createInternalElement() {
    if (OnsenUi.isVersion2()) {
      return DOM.createElement("ons-input");
//    return DOM.createInputText();
    } else {
      return DOM.createInputText();
    }
  }

  protected OnsTextBoxBase(Element element, String type, String className) {
    super(element, PassthroughRenderer.instance(), PassthroughParser.instance());
    OnsenUi.ensureId(element);
    if (type != null) {
      element.setAttribute("type", type);
      actualType = type;
    }
    if (className != null) {
      element.addClassName(className);
    }
    
    OnsenUi.onAvailableElement(this, new Delegate<Element>() {
      public void execute(Element element) {
        OnsTextBoxBase.this.inputElement = element;
      }
    });
    
  }
  
  public Element getInputElement() {
    return inputElement;
  }
  
  public void setPlaceholder(final String placeholder) {
    if (OnsenUi.isVersion2()) {
//    getElement().setAttribute("float", "true");
      GwtUtils.deferredExecution(500, new Delegate<Void>() {
        public void execute(Void element) {
          OnsenUi.onAvailableElement(OnsTextBoxBase.this, new Delegate<Element>() {
            public void execute(Element element) {
              NodeList<Element> inputs = element.getElementsByTagName("input");
              if (inputs != null && inputs.getLength() > 0) {
                inputs.getItem(0).setAttribute("placeholder", placeholder);
              }
              
            }
          });
        }
      });
    } else {
      getElement().setAttribute("placeholder", placeholder);
    }
  }
  
  @Override
  public String getValue() {
    return getText();
  }
  
  @Override
  public String getText() {
    String id = getElement().getId();
    String text = null;
    Element elem = DOM.getElementById(id);
    if (elem != null) {
      text = elem.getPropertyString("value");
    }
    if (text == null) {
      text = "";
    }
    return text;
  }
  
  @Override
  public void setValue(String value, boolean fireEvents) {
    setText(value);
    if (fireEvents) {
      ValueChangeEvent.fire(OnsTextBoxBase.this, value);
    }
  }
  
  @Override
  public void setText(final String text) {
    /* 20/05/2015 */
    OnsenUi.onAvailableElement(this, new Delegate<Element>() {
      public void execute(Element element) {
        element.setPropertyString("value", text);
        element.setAttribute("value", text);
        OnsenUi.compileElement(element);
      }
    });
  }

  protected static native Element getElementByIdImpl(String elementId) /*-{
    return $doc.getElementById(elementId);
  }-*/;
  
  public void setDisabled(String disabled) {
    if (Boolean.parseBoolean(disabled)) {
      OnsenUi.onAvailableElement(this, new Delegate<Element>() {
        public void execute(Element element) {
          element.setAttribute("disabled", "");
        }
      });
    } else {
      OnsenUi.onAvailableElement(this, new Delegate<Element>() {
        public void execute(Element element) {
          element.removeAttribute("disabled");
        }
      });
    }
  }
  
  public void setReadonly(String readonly) {
    if (Boolean.parseBoolean(readonly)) {
      OnsenUi.onAvailableElement(this, new Delegate<Element>() {
        public void execute(Element element) {
          element.setAttribute("readonly", "");
        }
      });
    }
  }
  
  public void setType(final String type) {
    actualType = type;
    OnsenUi.onAvailableElement(this, new Delegate<Element>() {
      public void execute(Element element) {
        element.setAttribute("type", type);
      }
    });
  }

  public void setMaxlength(final String maxlength) {
    OnsenUi.onAvailableElement(this, new Delegate<Element>() {
      public void execute(final Element element) {
        element.setAttribute("maxlength", maxlength);
        
        if ("number".equals(actualType)) {
          final int maxlen = Integer.parseInt(maxlength);
          OnsEventUtils.addHandler(element, "input", true, new NativeGestureHandler() {
            public void on(NativeGestureEvent event) {
              limitLength(element, maxlen);
            }
          });
          
        }
        
      }
    });
  }
  
  protected static native void limitLength (Element element, int maxlength) /*-{
    if (element.value.length > maxlength) {
      element.value = element.value.slice(0,maxlength); 
    }
  }-*/;
  

  public void setMax(final String max) {
    OnsenUi.onAvailableElement(this, new Delegate<Element>() {
      public void execute(Element element) {
        element.setAttribute("max", max);
      }
    });
  }

  public void setMin(final String min) {
    OnsenUi.onAvailableElement(this, new Delegate<Element>() {
      public void execute(Element element) {
        element.setAttribute("min", min);
      }
    });
  }

  @Override
  public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<String> handler) {
    if (onsChangeHandler == null) {
      OnsenUi.onAvailableElement(this, new Delegate<Element>() {
        public void execute(Element element) {
          onsChangeHandler = OnsEventUtils.addChangeHandler(element, new NativeGestureHandler() {
            public void on(NativeGestureEvent event) {
//            PhgUtils.log("ONS CHANGE EVENT ");
              String value = event.getTarget().getPropertyString("value");
//            PhgUtils.log("ONS CHANGE EVENT " + value);
              ValueChangeEvent.fire(OnsTextBoxBase.this, value);
            }
          });
        }
      });
    }
    return super.addValueChangeHandler(handler);
  }
  
  public void setWidth(final String width) {
    OnsenUi.onAvailableElement(this, new Delegate<Element>() {
      public void execute(Element element) {
        GwtUtils.setJsPropertyString(element.getStyle(), "width", width); 
      }
    });
  }

  public void setModifier(final String modifier) {
    if (OnsenUi.isVersion2()) {
      getElement().setAttribute("modifier", modifier);
    }
  }
}
