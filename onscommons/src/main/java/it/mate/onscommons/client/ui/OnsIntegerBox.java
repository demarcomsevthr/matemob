package it.mate.onscommons.client.ui;



public class OnsIntegerBox extends OnsTextBox {
  
  public OnsIntegerBox() {
    super("number");
    addStyleName("text-input");
  }
  
  public Integer getValueAsInteger() {
    return Integer.parseInt(getText());
  }
  
  public void setValueAsInteger(Integer value) {
    setValueAsInteger(value, false);
  }
  
  public void setValueAsInteger(Integer value, boolean fireValueChangeEvent) {
    if (value != null) {
      setValue(value.toString(), fireValueChangeEvent);
    } else {
      setValue("", fireValueChangeEvent);
    }
  }

}
