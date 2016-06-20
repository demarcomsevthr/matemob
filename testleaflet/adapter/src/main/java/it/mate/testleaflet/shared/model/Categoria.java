package it.mate.testleaflet.shared.model;

import java.io.Serializable;

public interface Categoria extends Serializable {

  public void setDescrizione(String descrizione);

  public String getDescrizione();

  public void setCodice(String codice);

  public String getCodice();


}
