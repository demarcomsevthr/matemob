package it.mate.testleaflet.server.model;

import it.mate.commons.server.model.HasKey;
import it.mate.testleaflet.shared.model.Timbro;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@SuppressWarnings("serial")
@PersistenceCapable (detachable="true")
public class TimbroDs implements Timbro, HasKey {

  @PrimaryKey
  @Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
  Key remoteId;
  
  @Persistent
  private String nome;
  
  @Persistent
  private String codice;
  
  @Persistent
  private Double width;
  
  @Persistent
  private Double height;
  
  @Persistent
  private Boolean oval;

  @Persistent
  private Double prezzo;

  @NotPersistent
  private String image;

  @NotPersistent
  private Integer localId;
  
  @Persistent
  private String codCategoria;
  
  @Persistent
  private String descCategoria;
  
  @Persistent
  private Boolean componibile;
  
  @Persistent
  private Boolean allowImage;
  
  @Persistent
  private Integer maxNumRighe;

  @Override
  public String toString() {
    return "TimbroDs [remoteId=" + remoteId + ", nome=" + nome + ", codice=" + codice + ", width=" + width + ", height=" + height + ", oval=" + oval
        + ", prezzo=" + prezzo + "]";
  }

  public Integer getId() {
    return localId;
  }

  public void setId(Integer id) {
    localId = id;
  }

  public Key getKey() {
    return remoteId;
  }
  
  public String getRemoteId() {
    return remoteId != null ? KeyFactory.keyToString(remoteId) : null;
  }
  
  public void setRemoteId(String remoteId) {
    this.remoteId = remoteId != null ? KeyFactory.stringToKey(remoteId) : null;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getCodice() {
    return codice;
  }

  public void setCodice(String codice) {
    this.codice = codice;
  }

  public Double getWidth() {
    return width;
  }

  public void setWidth(Double width) {
    this.width = width;
  }

  public Double getHeight() {
    return height;
  }

  public void setHeight(Double height) {
    this.height = height;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }
  
  public String getImageData() {
    return null;
  }

  public Boolean getOval() {
    return oval;
  }

  public void setOval(Boolean oval) {
    this.oval = oval;
  }

  public Double getPrezzo() {
    return prezzo;
  }

  public void setPrezzo(Double prezzo) {
    this.prezzo = prezzo;
  }

  public String getCodCategoria() {
    return codCategoria;
  }

  public void setCodCategoria(String codCategoria) {
    this.codCategoria = codCategoria;
  }

  public String getDescCategoria() {
    return descCategoria;
  }

  public void setDescCategoria(String descCategoria) {
    this.descCategoria = descCategoria;
  }

  public Boolean getComponibile() {
    return componibile;
  }

  public void setComponibile(Boolean componibile) {
    this.componibile = componibile;
  }

  public Boolean getAllowImage() {
    return allowImage;
  }

  public void setAllowImage(Boolean allowImage) {
    this.allowImage = allowImage;
  }

  public Integer getMaxNumRighe() {
    return maxNumRighe;
  }

  public void setMaxNumRighe(Integer maxNumRighe) {
    this.maxNumRighe = maxNumRighe;
  }
  
}
