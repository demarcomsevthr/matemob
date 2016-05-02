package it.mate.gwtcommons.shared.model;

import java.io.Serializable;

public interface PageConfig extends Serializable {
  
  public static class InternalHelper {
    public static <T extends PageConfig> T copy (PageConfig source, T target) {
      if (source != null) {
        target.setId(source.getId());
        target.setName(source.getName());
        target.setXml(source.getXml());
      }
      return target;
    }
  }

  public String getName();

  public void setName(String name);

  public String getXml();

  public void setXml(String xml);

  public String getId();

  public void setId(String id);

}
