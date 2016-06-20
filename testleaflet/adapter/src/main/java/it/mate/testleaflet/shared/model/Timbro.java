package it.mate.testleaflet.shared.model;

import java.io.Serializable;

public interface Timbro extends Serializable {

  public void setImage(String image);

  public String getImage();
  
  public String getImageData();

  public void setCodice(String codice);

  public String getCodice();

  public void setNome(String nome);

  public String getNome();

  public void setId(Integer id);

  public Integer getId();
  
  public void setHeight(Double height);

  public Double getHeight();

  public void setWidth(Double width);

  public Double getWidth();

  public void setRemoteId(String remoteId);

  public String getRemoteId();

  public Boolean getOval();

  public void setOval(Boolean oval);

  public void setPrezzo(Double prezzo);

  public Double getPrezzo();

  public void setDescCategoria(String descCategoria);

  public String getDescCategoria();

  public void setCodCategoria(String codCategoria);

  public String getCodCategoria();

  public void setMaxNumRighe(Integer maxNumRighe);

  public Integer getMaxNumRighe();

  public void setAllowImage(Boolean allowImage);

  public Boolean getAllowImage();

  public void setComponibile(Boolean componibile);

  public Boolean getComponibile();

}
