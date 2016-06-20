package it.mate.testleaflet.shared.model.impl;

import it.mate.testleaflet.shared.model.Categoria;
import it.mate.gwtcommons.shared.rpc.IsMappable;
import it.mate.gwtcommons.shared.rpc.RpcMap;

@SuppressWarnings("serial")
public class CategoriaTx implements Categoria, IsMappable {
  
  private String codice;
  
  private String descrizione;
  
  public CategoriaTx() {

  }
  
  public CategoriaTx(String codice, String descrizione) {
    super();
    this.codice = codice;
    this.descrizione = descrizione;
  }

  @Override
  public RpcMap toRpcMap() {
    return null;
  }

  @Override
  public CategoriaTx fromRpcMap(RpcMap map) {
    return this;
  }

  public String getCodice() {
    return codice;
  }

  public void setCodice(String codice) {
    this.codice = codice;
  }

  public String getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

}
