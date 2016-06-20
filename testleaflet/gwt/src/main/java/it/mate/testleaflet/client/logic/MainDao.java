package it.mate.testleaflet.client.logic;

import it.mate.testleaflet.shared.model.Account;
import it.mate.testleaflet.shared.model.Categoria;
import it.mate.testleaflet.shared.model.Message;
import it.mate.testleaflet.shared.model.Order;
import it.mate.testleaflet.shared.model.OrderItem;
import it.mate.testleaflet.shared.model.OrderItemRow;
import it.mate.testleaflet.shared.model.Timbro;
import it.mate.testleaflet.shared.model.impl.AccountTx;
import it.mate.testleaflet.shared.model.impl.CategoriaTx;
import it.mate.testleaflet.shared.model.impl.MessageTx;
import it.mate.testleaflet.shared.model.impl.OrderItemRowTx;
import it.mate.testleaflet.shared.model.impl.OrderItemTx;
import it.mate.testleaflet.shared.model.impl.OrderTx;
import it.mate.testleaflet.shared.model.impl.TimbroTx;
import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.phgcommons.client.utils.PhgUtils;
import it.mate.phgcommons.client.utils.PhonegapLog;
import it.mate.phgcommons.client.utils.WebSQLDao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**************************************************
 * 
 * [19/06/2014]
 * 
 * IMPORTANTISSIMO
 * 
 * NEI LOOP DI READ (iterateXXXForRead) NON CHIAMARE MAI METODI CHE CREANO NUOVE TRANSAZIONI
 * 
 * ES: iteratePrescrizioniForRead e findContattoById
 * 
 * DA JavaScriptException (InvalidStateError)
 * 
 *
 */

public class MainDao extends WebSQLDao {
  
  private final static boolean DROP_TABLES_ON_OPEN_DATABASE = false;
  
  private final static long ESTIMATED_SIZE = 5 * 1024 * 1024;
  
  
  private final static String TIMBRI_FIELDS_0 = "nome, codice, width, height, oval, prezzo, codCategoria, descCategoria, remoteId, image, componibile, allowImage, maxNumRighe ";
  private final static String TIMBRI_FIELDS_0_CREATE = "nome, codice, width, height, oval, prezzo, codCategoria, descCategoria, remoteId, image BLOB, componibile, allowImage, maxNumRighe ";

  private final static String TIMBRI_FIELDS = TIMBRI_FIELDS_0;
  
  private final static String ORDER_FIELDS_0 = "codice, state, remoteId, lastUpdate, updateState, created ";

  private final static String ORDER_FIELDS = ORDER_FIELDS_0;
  
  private final static String ORDER_ITEM_FIELDS_0 = "orderId, timbroId, quantity, inCart, remoteId, previewImage, customerImage ";
  private final static String ORDER_ITEM_FIELDS_0_CREATE = "orderId, timbroId, quantity, inCart, remoteId, previewImage BLOB, customerImage BLOB ";

  private final static String ORDER_ITEM_FIELDS = ORDER_ITEM_FIELDS_0;
  
  private final static String ORDER_ITEM_ROW_FIELDS_0 = "orderItemId, text, bold, size, fontFamily, remoteId, italic, underline, align ";

  private final static String ORDER_ITEM_ROW_FIELDS = ORDER_ITEM_ROW_FIELDS_0;
  
  private final static String MESSAGE_FIELDS_0 = "data, text, orderId, orderItemId, remoteId ";

  private final static String MESSAGE_FIELDS = MESSAGE_FIELDS_0;
  
  private final static String ACCOUNT_FIELDS_0 = "id, email, name, password, devInfoId, pushNotifRegId, lastCheckForUpdates";

  private final static String ACCOUNT_FIELDS = ACCOUNT_FIELDS_0;
  
//private List<Timbro> cacheTimbri;
  
  
  
  public MainDao() {
    super("TestleafletDB", ESTIMATED_SIZE, migrationCallbacks, new DatabaseCallback() {
      public void handleEvent(WindowDatabase db) {
        PhonegapLog.log("created db testleaflet");
      }
    }, new SQLTransactionCallback() {
      public void handleEvent(SQLTransaction tr) {
        if (DROP_TABLES_ON_OPEN_DATABASE) {
          dropDBImpl(tr);
        }
      }
    });
  }
  
  public void dropDB(final Delegate<Void> delegate) {
    PhonegapLog.log("resetting db");
    db.doTransaction(new SQLTransactionCallback() {
      public void handleEvent(SQLTransaction tr) {
        dropDBImpl(tr);
        db = null;
        initDB();
        delegate.execute(null);
      }
    });
  }
  
  private static void dropDBImpl(SQLTransaction tr) {
    PhonegapLog.log("dropping all tables");
    
    PhonegapLog.log("dropping table version");
    tr.doExecuteSql("DROP TABLE IF EXISTS version");
    
    PhonegapLog.log("dropping table timbri");
    tr.doExecuteSql("DROP TABLE IF EXISTS timbri");
    
    PhonegapLog.log("dropping table order");
    tr.doExecuteSql("DROP TABLE IF EXISTS orderHeader");
    
    PhonegapLog.log("dropping table orderItem");
    tr.doExecuteSql("DROP TABLE IF EXISTS orderItem");
    
    PhonegapLog.log("dropping table orderItemRow");
    tr.doExecuteSql("DROP TABLE IF EXISTS orderItemRow");
    
    PhonegapLog.log("dropping table messages");
    tr.doExecuteSql("DROP TABLE IF EXISTS messages");
    
    PhonegapLog.log("dropping table account");
    tr.doExecuteSql("DROP TABLE IF EXISTS account");
    
  }
  
  private final static MigratorCallback MIGRATION_CALLBACK_0 = new MigratorCallback() {
    public void doMigration(int number, SQLTransaction tr) {
      PhonegapLog.log("updating db testleaflet to version " + number);

      PhonegapLog.log("creating table timbri");
      tr.doExecuteSql("CREATE TABLE timbri (id "+SERIAL_ID+", " + TIMBRI_FIELDS_0_CREATE + " )");

      PhonegapLog.log("creating table order");
      tr.doExecuteSql("CREATE TABLE orderHeader (id "+SERIAL_ID+", " + ORDER_FIELDS_0 + " )");

      PhonegapLog.log("creating table orderItem");
      tr.doExecuteSql("CREATE TABLE orderItem (id "+SERIAL_ID+", " + ORDER_ITEM_FIELDS_0_CREATE + " )");

      PhonegapLog.log("creating table orderItemRow");
      tr.doExecuteSql("CREATE TABLE orderItemRow (id "+SERIAL_ID+", " + ORDER_ITEM_ROW_FIELDS_0 + " )");

      PhonegapLog.log("creating table messages");
      tr.doExecuteSql("CREATE TABLE messages (id "+SERIAL_ID+", " + MESSAGE_FIELDS_0 + " )");

      PhonegapLog.log("creating table account");
      tr.doExecuteSql("CREATE TABLE account (" + ACCOUNT_FIELDS_0 + " )");

    }
  };
  
  private static final MigratorCallback[] migrationCallbacks = new MigratorCallback[] {
    MIGRATION_CALLBACK_0 
  };
  
  public void findAllCategorie(final Delegate<List<Categoria>> delegate) {
    findAllTimbri(new Delegate<List<Timbro>>() {
      public void execute(List<Timbro> timbri) {
        List<Categoria> categorie = new ArrayList<Categoria>();
        if (timbri != null) {
          for (Timbro timbro : timbri) {
            boolean found = false;
            for (Categoria categoria : categorie) {
              if (categoria.getCodice().equals(timbro.getCodCategoria())) {
                found = true;
                break;
              }
            }
            if (!found) {
              categorie.add(new CategoriaTx(timbro.getCodCategoria(), timbro.getDescCategoria()));
            }
          }
        }
        delegate.execute(categorie);
      }
    });
  }
  
  public void findTimbriByCategoria(final String codCategoria, final Delegate<List<Timbro>> delegate) {
    db.doReadTransaction(new SQLTransactionCallback() {
      public void handleEvent(SQLTransaction tr) {
        tr.doExecuteSql("SELECT id, " + TIMBRI_FIELDS + " FROM timbri WHERE codCategoria = ? ORDER BY id", 
            new Object[] {codCategoria}, new SQLStatementCallback() {
          public void handleEvent(SQLTransaction tr, SQLResultSet rs) {
            List<Timbro> results = new ArrayList<Timbro>();
            if (rs.getRows().getLength() > 0) {
              for (int it = 0; it < rs.getRows().getLength(); it++) {
                results.add(flushRSToTimbro(rs, it));
              }
            }
            delegate.execute(cloneList(results));
          }
        });
      }
    });
  }
  
  public void findAllTimbri(final Delegate<List<Timbro>> delegate) {
    db.doReadTransaction(new SQLTransactionCallback() {
      public void handleEvent(SQLTransaction tr) {
        PhgUtils.log("select timbri");
        tr.doExecuteSql("SELECT id, " + TIMBRI_FIELDS + " FROM timbri ORDER BY id", null, new SQLStatementCallback() {
          public void handleEvent(SQLTransaction tr, SQLResultSet rs) {
            List<Timbro> results = new ArrayList<Timbro>();
            if (rs.getRows().getLength() > 0) {
              for (int it = 0; it < rs.getRows().getLength(); it++) {
                results.add(flushRSToTimbro(rs, it));
              }
            }
            delegate.execute(cloneList(results));
          }
        });
      }
    });
    /*
    if (this.cacheTimbri != null) {
      delegate.execute(cloneList(cacheTimbri));
    } else {
      db.doReadTransaction(new SQLTransactionCallback() {
        public void handleEvent(SQLTransaction tr) {
          PhgUtils.log("select timbri");
          tr.doExecuteSql("SELECT id, " + TIMBRI_FIELDS + " FROM timbri ORDER BY id", null, new SQLStatementCallback() {
            public void handleEvent(SQLTransaction tr, SQLResultSet rs) {
              List<Timbro> results = new ArrayList<Timbro>();
              if (rs.getRows().getLength() > 0) {
                for (int it = 0; it < rs.getRows().getLength(); it++) {
                  results.add(flushRSToTimbro(rs, it));
                }
              }
              cacheTimbri = results;
              delegate.execute(cloneList(results));
            }
          });
        }
      });
    }
    */
  }
  
  public void findTimbro(final Integer id, final Delegate<Timbro> delegate) {
    db.doReadTransaction(new SQLTransactionCallback() {
      public void handleEvent(SQLTransaction tr) {
        findTimbro(tr, id, delegate);
      }
    });
  }
  
  private void findTimbro(SQLTransaction tr, final Integer id, final Delegate<Timbro> delegate) {
    /*
    if (this.cacheTimbri != null) {
      for (Timbro timbro : cacheTimbri) {
        if (timbro.getId().equals(id)) {
          delegate.execute(timbro);
          return;
        }
      }
    }
    */
    PhgUtils.log("select timbro");
    tr.doExecuteSql("SELECT id, " + TIMBRI_FIELDS + " FROM timbri WHERE id = ?", 
        new Object[]{id}, new SQLStatementCallback() {
      public void handleEvent(SQLTransaction tr, SQLResultSet rs) {
        if (rs.getRows().getLength() > 0) {
          for (int it = 0; it < rs.getRows().getLength(); it++) {
            Timbro timbro = flushRSToTimbro(rs, it);
            delegate.execute(timbro);
          }
        }
      }
    });
  }
  
  private <T> List<T> cloneList(List<T> items) {
    if (items == null) {
      return null;
    }
    List<T> results = new ArrayList<T>();
    for (T item : items) {
      results.add(item);
    }
    return results;
  }
  
  private Timbro flushRSToTimbro(SQLResultSet rs, int it) {
    SQLResultSetRowList rows = rs.getRows();
    Timbro result = new TimbroTx();
    result.setId(rows.getValueInt(it, "id"));
    result.setNome(rows.getValueString(it, "nome"));
    result.setCodice(rows.getValueString(it, "codice"));
    result.setImage(rows.getValueString(it, "image"));
    result.setWidth(rows.getValueDouble(it, "width"));
    result.setHeight(rows.getValueDouble(it, "height"));
    result.setOval(rows.getValueInt(it, "oval") == 1);
    result.setPrezzo(rows.getValueDouble(it, "prezzo"));
    result.setCodCategoria(rows.getValueString(it, "codCategoria"));
    result.setDescCategoria(rows.getValueString(it, "descCategoria"));
    result.setRemoteId(rows.getValueString(it, "remoteId"));
    result.setComponibile(rows.getValueInt(it, "componibile") == 1);
    result.setAllowImage(rows.getValueInt(it, "allowImage") == 1);
    result.setMaxNumRighe(rows.getValueInt(it, "maxNumRighe"));
    return result;
  }
  
  public void saveTimbro(final Timbro entity, final Delegate<Timbro> delegate) {
    db.doTransaction(new SQLTransactionCallback() {
      public void handleEvent(SQLTransaction tr) {
        if (entity.getId() == null) {
          tr.doExecuteSql("INSERT INTO timbri (" + TIMBRI_FIELDS + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 
              new Object[] {
                entity.getNome(), 
                entity.getCodice(),
                entity.getWidth(),
                entity.getHeight(),
                entity.getOval() ? 1 : 0,
                entity.getPrezzo(),
                entity.getCodCategoria(),
                entity.getDescCategoria(),
                entity.getRemoteId(),
                entity.getImage(),
                (entity.getComponibile() ? 1 : 0),
                (entity.getAllowImage() ? 1 : 0),
                entity.getMaxNumRighe()
              }, new SQLStatementCallback() {
                public void handleEvent(SQLTransaction tr, SQLResultSet rs) {
                  entity.setId(rs.getInsertId());
                  PhonegapLog.log("Inserted " + entity);
                  delegate.execute(entity);
                }
              });
        } else {
          String sql = "UPDATE timbri SET ";
          sql += " nome = ?";
          sql += " ,codice = ?";
          sql += " ,image = ?";
          sql += " ,width = ?";
          sql += " ,height = ?";
          sql += " ,oval = ?";
          sql += " ,prezzo = ?";
          sql += " ,codCategoria = ?";
          sql += " ,descCategoria = ?";
          sql += " ,remoteId = ?";
          sql += " ,componibile = ?";
          sql += " ,allowImage = ?";
          sql += " ,maxNumRighe = ?";
          sql += " WHERE id = ?";
          tr.doExecuteSql(sql, new Object[] {
              entity.getNome(), 
              entity.getCodice(),
              entity.getImage(),
              entity.getWidth(),
              entity.getHeight(),
              entity.getOval() ? 1 : 0,
              entity.getPrezzo(),
              entity.getCodCategoria(),
              entity.getDescCategoria(),
              entity.getRemoteId(),
              (entity.getComponibile() ? 1 : 0),
              (entity.getAllowImage() ? 1 : 0),
              entity.getMaxNumRighe(),
              entity.getId()
            }, new SQLStatementCallback() {
              public void handleEvent(SQLTransaction tr, SQLResultSet rs) {
                PhonegapLog.log("Updated " + entity);
                delegate.execute(entity);
              }
            });
        }
      }
    });
  }
  
  /*******************************************************************************
   *   ORDERS
   *******************************************************************************/

  public void findSentOrders(final Delegate<List<Order>> delegate) {
    findAllOrders(new Delegate<List<Order>>() {
      public void execute(List<Order> orders) {
        if (orders != null) {
          for (Iterator<Order> it = orders.iterator(); it.hasNext();) {
            Order order = it.next();
            if (((OrderTx)order).isCartOrder()) {
              it.remove();
            }
          }
        }
        delegate.execute(orders);
      }
    });
  }
  
  public void findAllOrders(final Delegate<List<Order>> delegate) {
    db.doReadTransaction(new SQLTransactionCallback() {
      public void handleEvent(SQLTransaction tr) {
        PhgUtils.log("select all orders");
        tr.doExecuteSql("SELECT id, " + ORDER_FIELDS + " FROM orderHeader ORDER BY id", null, new SQLStatementCallback() {
          public void handleEvent(SQLTransaction tr, SQLResultSet rs) {
            new RSToOrderIterator(tr, rs, new Delegate<List<Order>>() {
              public void execute(List<Order> results) {
                delegate.execute(results);
              }
            });
          }
        });
      }
    });
  }
  
  public void findOrderInCart(final Delegate<List<Order>> delegate) {
    db.doReadTransaction(new SQLTransactionCallback() {
      public void handleEvent(SQLTransaction tr) {
        PhgUtils.log(" ----  select order in cart ----");
        tr.doExecuteSql("SELECT id, " + ORDER_FIELDS + " FROM orderHeader WHERE orderHeader.state = ?", 
            new Object[]{Order.STATE_IN_CART}, new SQLStatementCallback() {
          public void handleEvent(SQLTransaction tr, SQLResultSet rs) {
            new RSToOrderIterator(tr, rs, new Delegate<List<Order>>() {
              public void execute(List<Order> results) {
                delegate.execute(results);
              }
            });
          }
        });
      }
    });
  }
  
  public void findUpdatedOrders(final Delegate<List<Order>> delegate) {
    db.doReadTransaction(new SQLTransactionCallback() {
      public void handleEvent(SQLTransaction tr) {
        PhgUtils.log(" ----  select updated orders ----");
        tr.doExecuteSql("SELECT id, " + ORDER_FIELDS + " FROM orderHeader WHERE orderHeader.updateState = 'U'", 
            null, new SQLStatementCallback() {
          public void handleEvent(SQLTransaction tr, SQLResultSet rs) {
            new RSToOrderIterator(tr, rs, new Delegate<List<Order>>() {
              public void execute(List<Order> results) {
                delegate.execute(results);
              }
            });
          }
        });
      }
    });
  }
  
  protected void findOrder(SQLTransaction tr, final Integer id, final Delegate<Order> delegate) {
    if (id == null) {
      delegate.execute(null);
    } else {
      PhgUtils.log("select order by id");
      tr.doExecuteSql("SELECT id, " + ORDER_FIELDS + " FROM orderHeader WHERE orderHeader.id = ?", 
          new Object[]{id}, new SQLStatementCallback() {
        public void handleEvent(SQLTransaction tr, SQLResultSet rs) {
          new RSToOrderIterator(tr, rs, new Delegate<List<Order>>() {
            public void execute(List<Order> results) {
              if (results != null && results.size() == 1) {
                delegate.execute(results.get(0));
              } else {
                delegate.execute(null);
              }
            }
          });
        }
      });
    }
  }
  
  protected class RSToOrderIterator {
    SQLResultSet rs;
    SQLTransaction tr;
    Delegate<List<Order>> delegate;
    List<Order> results = new ArrayList<Order>();
    public RSToOrderIterator(SQLTransaction tr, SQLResultSet rs, Delegate<List<Order>> delegate) {
      this.tr = tr;
      this.rs = rs;
      this.delegate = delegate;
      iterate(0);
    }
    private void iterate(final int it) {
      if (it < rs.getRows().getLength()) {
        final Order result = flushRS(rs, it);
        results.add(result);
        findOrderItems(tr, result.getId(), new Delegate<List<OrderItem>>() {
          public void execute(List<OrderItem> items) {
            result.setItems(items);
            iterate(it + 1);
          }
        });
      } else {
        delegate.execute(results);
      }
    }
    private Order flushRS(SQLResultSet rs, int it) {
      SQLResultSetRowList rows = rs.getRows();
      Order entity = new OrderTx();
      entity.setId(rows.getValueInt(it, "id"));
      entity.setCodice(rows.getValueString(it, "codice"));
      entity.setState(rows.getValueInt(it, "state"));
      entity.setRemoteId(rows.getValueString(it, "remoteId"));
      entity.setLastUpdate(longAsDate(rows.getValueLong(it, "lastUpdate")));
      entity.setUpdateState(rows.getValueString(it, "updateState"));
      entity.setCreated(longAsDate(rows.getValueLong(it, "created")));
      return entity;
    }
    protected List<Order> getResults() {
      return results;
    }
  }
  
  protected void findOrderItems(SQLTransaction tr, Integer orderId, final Delegate<List<OrderItem>> delegate) {
    PhgUtils.log(" ----  select order items  ----");
    tr.doExecuteSql("SELECT id, " + ORDER_ITEM_FIELDS + " FROM orderItem WHERE orderId = ? ORDER BY id", 
        new Object[]{orderId}, new SQLStatementCallback() {
      public void handleEvent(SQLTransaction tr, SQLResultSet rs) {
        new RSToOrderItemIterator(tr, rs, new Delegate<List<OrderItem>>() {
          public void execute(List<OrderItem> results) {
            delegate.execute(results);
          }
        });
      }
    });
  }
  
  protected class RSToOrderItemIterator {
    SQLResultSet rs;
    SQLTransaction tr;
    Delegate<List<OrderItem>> delegate;
    List<OrderItem> results = new ArrayList<OrderItem>();
    public RSToOrderItemIterator(SQLTransaction tr, SQLResultSet rs, Delegate<List<OrderItem>> delegate) {
      this.tr = tr;
      this.rs = rs;
      this.delegate = delegate;
      iterate(0);
    }
    private void iterate(final int it) {
      if (it < rs.getRows().getLength()) {
        final OrderItem result = flushRS(rs, it);
        results.add(result);
        
        findTimbro(tr, result.getTimbroId(), new Delegate<Timbro>() {
          public void execute(Timbro timbro) {
            result.setTimbro(timbro);
            findOrderItemRows(tr, result.getId(), new Delegate<List<OrderItemRow>>() {
              public void execute(List<OrderItemRow> rows) {
                result.setRows(rows);
                
                findMessages(tr, result.getId(), new Delegate<List<Message>>() {
                  public void execute(List<Message> messages) {

                    result.setMessages(messages);
                    iterate(it + 1);
                    
                  }
                });
                
              }
            });
          }
        });
        
      } else {
        delegate.execute(results);
      }
    }
    private OrderItem flushRS(SQLResultSet rs, int it) {
      SQLResultSetRowList rows = rs.getRows();
      OrderItem result = new OrderItemTx(null);
      result.setId(rows.getValueInt(it, "id"));
      result.setOrderId(rows.getValueInt(it, "orderId"));
      result.setTimbroId(rows.getValueInt(it, "timbroId"));
      result.setQuantity(rows.getValueDouble(it, "quantity"));
      result.setInCart(rows.getValueInt(it, "inCart") == 1);
      result.setRemoteId(rows.getValueString(it, "remoteId"));
      result.setPreviewImage(rows.getValueString(it, "previewImage"));
      String appo = rows.getValueString(it, "customerImage");
      result.setCustomerImage(rows.getValueString(it, "customerImage"));
      return result;
    }
    protected List<OrderItem> getResults() {
      return results;
    }
  }
  
  protected void findOrderItemRows(SQLTransaction tr, Integer orderItemId, final Delegate<List<OrderItemRow>> delegate) {
    PhgUtils.log(" ----  select order item rowss  ----");
    tr.doExecuteSql("SELECT id, " + ORDER_ITEM_ROW_FIELDS + " FROM orderItemRow WHERE orderItemId = ? ORDER BY id", 
        new Object[]{orderItemId}, new SQLStatementCallback() {
      public void handleEvent(SQLTransaction tr, SQLResultSet rs) {
        new RSToOrderItemRowIterator(tr, rs, new Delegate<List<OrderItemRow>>() {
          public void execute(List<OrderItemRow> results) {
            delegate.execute(results);
          }
        });
      }
    });
  }
  
  protected class RSToOrderItemRowIterator {
    SQLResultSet rs;
    SQLTransaction tr;
    Delegate<List<OrderItemRow>> delegate;
    List<OrderItemRow> results = new ArrayList<OrderItemRow>();
    public RSToOrderItemRowIterator(SQLTransaction tr, SQLResultSet rs, Delegate<List<OrderItemRow>> delegate) {
      this.tr = tr;
      this.rs = rs;
      this.delegate = delegate;
      iterate(0);
    }
    private void iterate(final int it) {
      if (it < rs.getRows().getLength()) {
        OrderItemRow result = flushRS(rs, it);
        results.add(result);
        iterate(it + 1);
      } else {
        delegate.execute(results);
      }
    }
    private OrderItemRow flushRS(SQLResultSet rs, int it) {
      SQLResultSetRowList rows = rs.getRows();
      OrderItemRow result = new OrderItemRowTx();
      result.setId(rows.getValueInt(it, "id"));
      result.setOrderItemId(rows.getValueInt(it, "orderItemId"));
      result.setText(rows.getValueString(it, "text"));
      result.setBold(rows.getValueInt(it, "bold") == 1);
      result.setSize(rows.getValueInt(it, "size"));
      result.setFontFamily(rows.getValueString(it, "fontFamily"));
      result.setRemoteId(rows.getValueString(it, "remoteId"));
      result.setItalic(rows.getValueInt(it, "italic") == 1);
      result.setUnderline(rows.getValueInt(it, "underline") == 1);
      result.setAlign(rows.getValueString(it, "align"));
      return result;
    }
    protected List<OrderItemRow> getResults() {
      return results;
    }
  }
  
  protected void findMessages(SQLTransaction tr, Integer orderItemId, final Delegate<List<Message>> delegate) {
    PhgUtils.log(" ----  select messages  ----");
    tr.doExecuteSql("SELECT id, " + MESSAGE_FIELDS + " FROM messages WHERE orderItemId = ? ORDER BY id", 
        new Object[]{orderItemId}, new SQLStatementCallback() {
      public void handleEvent(SQLTransaction tr, SQLResultSet rs) {
        new RSToMessageIterator(tr, rs, new Delegate<List<Message>>() {
          public void execute(List<Message> results) {
            delegate.execute(results);
          }
        });
      }
    });
  }
  
  
  public void saveOrder(final Order entity, final Delegate<Order> delegate) {
    PhgUtils.log("saving " + entity);
    db.doTransaction(new SQLTransactionCallback() {
      public void handleEvent(SQLTransaction tr) {
        if (entity.getId() == null) {
          tr.doExecuteSql("INSERT INTO orderHeader (" + ORDER_FIELDS + ") VALUES (?, ?, ?, ?, ?, ?)", 
              new Object[] {
                entity.getCodice(), 
                entity.getState(),
                entity.getRemoteId(),
                dateAsLong(entity.getLastUpdate()),
                entity.getUpdateState(),
                dateAsLong(entity.getCreated())
              }, new SQLStatementCallback() {
                public void handleEvent(SQLTransaction tr, SQLResultSet rs) {
                  entity.setId(rs.getInsertId());
                  iterateOrderItemsForUpdate(tr, entity.getItems().iterator(), new Delegate<Void>() {
                    public void execute(Void element) {
                      PhonegapLog.log("Inserted " + entity);
                      if (delegate != null) {
                        delegate.execute(entity);
                      }
                    }
                  });
                }
              });
        } else {
          String sql = "UPDATE orderHeader SET ";
          sql += "  codice = ?";
          sql += " ,state = ?";
          sql += " ,remoteId = ?";
          sql += " ,lastUpdate = ?";
          sql += " ,updateState = ?";
          sql += " ,created = ?";
          sql += " WHERE id = ?";
          tr.doExecuteSql(sql, new Object[] {
              entity.getCodice(), 
              entity.getState(),
              entity.getRemoteId(),
              dateAsLong(entity.getLastUpdate()),
              entity.getUpdateState(),
              dateAsLong(entity.getCreated()),
              entity.getId()
            }, new SQLStatementCallback() {
              public void handleEvent(SQLTransaction tr, SQLResultSet rs) {
                iterateOrderItemsForUpdate(tr, entity.getItems().iterator(), new Delegate<Void>() {
                  public void execute(Void element) {
                    PhonegapLog.log("Updated " + entity);
                    if (delegate != null) {
                      delegate.execute(entity);
                    }
                  }
                });
              }
            });
        }
      }
    });
  }
  
  protected void iterateOrderItemsForUpdate(final SQLTransaction tr, final Iterator<OrderItem> it, final Delegate<Void> delegate) {
    if (it.hasNext()) {
      final OrderItem item = it.next();
      updateOrderItem(tr, item, new Delegate<OrderItem>() {
        public void execute(OrderItem item) {
          iterateOrderItemsForUpdate(tr, it, delegate);
        }
      });
    } else {
      delegate.execute(null);
    }
  }

  protected void updateOrderItem(SQLTransaction tr, final OrderItem entity, final Delegate<OrderItem> delegate) {
    if (entity.getId() == null) {
      tr.doExecuteSql("INSERT INTO orderItem (" + ORDER_ITEM_FIELDS + ") VALUES (?, ?, ?, ?, ?, ?, ?)", 
          new Object[] {
            entity.getOrderId(), 
            entity.getTimbroId(),
            entity.getQuantity(),
            (entity.isInCart() ? 1 : 0),
            entity.getRemoteId(),
            entity.getPreviewImage(),
            entity.getCustomerImage()
          }, new SQLStatementCallback() {
            public void handleEvent(final SQLTransaction tr, SQLResultSet rs) {
              entity.setId(rs.getInsertId());
              afterOrderItemUpdate(tr, entity, delegate);
            }
          });
    } else {
      
      if (entity.getTimbroId() != null) {
        String sql = "UPDATE orderItem SET ";
        sql += "  orderId = ?";
        sql += " ,timbroId = ?";
        sql += " ,quantity = ?";
        sql += " ,inCart = ?";
        sql += " ,remoteId = ?";
        sql += " ,previewImage = ?";
        sql += " ,customerImage = ?";
        sql += " WHERE id = ?";
        tr.doExecuteSql(sql, new Object[] {
            entity.getOrderId(), 
            entity.getTimbroId(),
            entity.getQuantity(),
            (entity.isInCart() ? 1 : 0),
            entity.getRemoteId(),
            entity.getPreviewImage(),
            entity.getCustomerImage(),
            entity.getId()
          }, new SQLStatementCallback() {
            public void handleEvent(final SQLTransaction tr, SQLResultSet rs) {
              afterOrderItemUpdate(tr, entity, delegate);
            }
          });
        
      } else {
        
        String sql = "UPDATE orderItem SET ";
        sql += "  orderId = ?";
        sql += " ,quantity = ?";
        sql += " ,inCart = ?";
        sql += " ,remoteId = ?";
        sql += " ,previewImage = ?";
        sql += " WHERE id = ?";
        tr.doExecuteSql(sql, new Object[] {
            entity.getOrderId(), 
            entity.getQuantity(),
            (entity.isInCart() ? 1 : 0),
            entity.getRemoteId(),
            entity.getPreviewImage(),
            entity.getId()
          }, new SQLStatementCallback() {
            public void handleEvent(final SQLTransaction tr, SQLResultSet rs) {
              afterOrderItemUpdate(tr, entity, delegate);
            }
          });
        
      }
      
    }
  }
  
  private void afterOrderItemUpdate(final SQLTransaction tr, final OrderItem entity, final Delegate<OrderItem> delegate) {
    purgeOrderItemRows(tr, entity, new Delegate<Void>() {
      public void execute(Void element) {
        iterateOrderItemRowsForUpdate(tr, entity.getRows().iterator(), new Delegate<Void>() {
          public void execute(Void element) {
            iterateMessagesForUpdate(tr, entity.getMessages().iterator(), new Delegate<Void>() {
              public void execute(Void element) {
                PhonegapLog.log("Updated " + entity);
                delegate.execute(entity);
              }
            });
          }
        });
      }
    });
  }
  
  protected void purgeOrderItemRows(SQLTransaction tr, OrderItem item, final Delegate<Void> delegate) {
    if (item.getId() == null) {
      delegate.execute(null);
      return;
    }
    String idsList = "";
    for (OrderItemRow row : item.getRows()) {
      if (row.getId() != null) {
        if (idsList.length() > 0) {
          idsList += ",";
        }
        idsList += "" + row.getId();
      }
    }
    if (idsList.length() > 0) {
      tr.doExecuteSql("DELETE FROM orderItemRow WHERE orderItemId = ? AND id NOT IN (" + idsList + ")", 
          new Object[] {
            item.getId()
          }, new SQLStatementCallback() {
            public void handleEvent(SQLTransaction tr, SQLResultSet rs) {
              delegate.execute(null);
            }
          });
    } else {
      delegate.execute(null);
    }
  }
  
  protected void iterateOrderItemRowsForUpdate(final SQLTransaction tr, final Iterator<OrderItemRow> it, final Delegate<Void> delegate) {
    if (it.hasNext()) {
      final OrderItemRow item = it.next();
      updateOrderItemRow(tr, item, new Delegate<OrderItemRow>() {
        public void execute(OrderItemRow item) {
          iterateOrderItemRowsForUpdate(tr, it, delegate);
        }
      });
    } else {
      delegate.execute(null);
    }
  }

  protected void updateOrderItemRow(SQLTransaction tr, final OrderItemRow entity, final Delegate<OrderItemRow> delegate) {
    if (entity.getId() == null) {
      tr.doExecuteSql("INSERT INTO orderItemRow (" + ORDER_ITEM_ROW_FIELDS + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", 
          new Object[] {
            entity.getOrderItemId(), 
            entity.getText(),
            (entity.getBold() ? 1 : 0),
            entity.getSize(),
            entity.getFontFamily(),
            entity.getRemoteId(),
            (entity.getItalic() ? 1 : 0),
            (entity.getUnderline() ? 1 : 0),
            entity.getAlign()
          }, new SQLStatementCallback() {
            public void handleEvent(SQLTransaction tr, SQLResultSet rs) {
              entity.setId(rs.getInsertId());
              PhonegapLog.log("Inserted " + entity);
              delegate.execute(entity);
            }
          });
    } else {
      String sql = "UPDATE orderItemRow SET ";
      sql += "  orderItemId = ?";
      sql += " ,text = ?";
      sql += " ,bold = ?";
      sql += " ,size = ?";
      sql += " ,fontFamily = ?";
      sql += " ,remoteId = ?";
      sql += " ,italic = ?";
      sql += " ,underline = ?";
      sql += " ,align = ?";
      sql += " WHERE id = ?";
      tr.doExecuteSql(sql, new Object[] {
          entity.getOrderItemId(), 
          entity.getText(),
          (entity.getBold() ? 1 : 0),
          entity.getSize(),
          entity.getFontFamily(),
          entity.getRemoteId(),
          (entity.getItalic() ? 1 : 0),
          (entity.getUnderline() ? 1 : 0),
          entity.getAlign(),
          entity.getId()
        }, new SQLStatementCallback() {
          public void handleEvent(SQLTransaction tr, SQLResultSet rs) {
            PhonegapLog.log("Updated " + entity);
            delegate.execute(entity);
          }
        });
    }
  }
  
  protected void iterateMessagesForUpdate(final SQLTransaction tr, final Iterator<Message> it, final Delegate<Void> delegate) {
    if (it.hasNext()) {
      Message message = it.next();
      saveMessage(tr, message, new Delegate<Message>() {
        public void execute(Message message) {
          iterateMessagesForUpdate(tr, it, delegate);
        }
      });
    } else {
      delegate.execute(null);
    }
  }
  
  /* >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */
  /* MESSAGES */
  
  public void findAllMessages(final Delegate<List<Message>> delegate) {
    db.doTransaction(new SQLTransactionCallback() {
      public void handleEvent(SQLTransaction tr) {
        tr.doExecuteSql("SELECT id, " + MESSAGE_FIELDS + " FROM messages ORDER BY id", 
            new Object[]{}, new SQLStatementCallback() {
          public void handleEvent(SQLTransaction tr, SQLResultSet rs) {
            new RSToMessageIterator(tr, rs, new Delegate<List<Message>>() {
              public void execute(List<Message> results) {
                delegate.execute(results);
              }
            });
          }
        });
      }
    });
  }
  
  protected class RSToMessageIterator {
    SQLResultSet rs;
    SQLTransaction tr;
    Delegate<List<Message>> delegate;
    List<Message> results = new ArrayList<Message>();
    public RSToMessageIterator(SQLTransaction tr, SQLResultSet rs, Delegate<List<Message>> delegate) {
      this.tr = tr;
      this.rs = rs;
      this.delegate = delegate;
      iterate(0);
    }
    private void iterate(final int it) {
      if (it < rs.getRows().getLength()) {
        final Message result = flushRS(rs, it);
        results.add(result);
        findOrder(tr, result.getOrderId(), new Delegate<Order>() {
          public void execute(Order order) {
            result.setOrder(order);
            iterate(it + 1);
          }
        });
      } else {
        delegate.execute(results);
      }
    }
    private Message flushRS(SQLResultSet rs, int it) {
      SQLResultSetRowList rows = rs.getRows();
      Message result = new MessageTx();
      result.setId(rows.getValueInt(it, "id"));
      ((MessageTx)result).setOrderId(rows.getValueInt(it, "orderId"));
      result.setData(longAsDate(rows.getValueLong(it, "data")));
      result.setText(rows.getValueString(it, "text"));
      result.setOrderItemId(rows.getValueInt(it, "orderItemId"));
      result.setRemoteId(rows.getValueString(it, "remoteId"));
      return result;
    }
    protected List<Message> getResults() {
      return results;
    }
  }
  
  public void saveMessage(final Message entity, final Delegate<Message> delegate) {
    db.doTransaction(new SQLTransactionCallback() {
      public void handleEvent(SQLTransaction tr) {
        saveMessage(tr, entity, delegate);
      }
    });
  }
  
  protected void saveMessage(SQLTransaction tr, final Message entity, final Delegate<Message> delegate) {
    if (entity.getId() == null) {
      tr.doExecuteSql("INSERT INTO messages (" + MESSAGE_FIELDS + ") VALUES (?, ?, ?, ?, ?)", 
          new Object[] {
            dateAsLong(entity.getData()), 
            entity.getText(),
            entity.getOrderId(),
            entity.getOrderItemId(),
            entity.getRemoteId()
          }, new SQLStatementCallback() {
            public void handleEvent(final SQLTransaction tr, SQLResultSet rs) {
              entity.setId(rs.getInsertId());
              PhonegapLog.log("Inserted " + entity);
              delegate.execute(entity);
            }
          });
    } else {
      String sql = "UPDATE messages SET ";
      sql += "  data = ?";
      sql += " ,text = ?";
      sql += " ,orderId = ?";
      sql += " ,orderItemId = ?";
      sql += " ,remoteId = ?";
      sql += " WHERE id = ?";
      tr.doExecuteSql(sql, new Object[] {
          dateAsLong(entity.getData()), 
          entity.getText(),
          entity.getOrderId(),
          entity.getOrderItemId(),
          entity.getRemoteId(),
          entity.getId()
        }, new SQLStatementCallback() {
          public void handleEvent(final SQLTransaction tr, SQLResultSet rs) {
            PhonegapLog.log("Updated " + entity);
            delegate.execute(entity);
          }
        });
    }
  }
  
  /* >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */
  /* ACCOUNT */
  
  public void findAllAccounts(final Delegate<List<Account>> delegate) {
    db.doTransaction(new SQLTransactionCallback() {
      public void handleEvent(SQLTransaction tr) {
        tr.doExecuteSql("SELECT " + ACCOUNT_FIELDS + " FROM account", 
            new Object[]{}, new SQLStatementCallback() {
          public void handleEvent(SQLTransaction tr, SQLResultSet rs) {
            new RSToAccountIterator(tr, rs, new Delegate<List<Account>>() {
              public void execute(List<Account> results) {
                delegate.execute(results);
              }
            });
          }
        });
      }
    });
  }
  
  public void findAccount(final Delegate<Account> delegate) {
    db.doTransaction(new SQLTransactionCallback() {
      public void handleEvent(SQLTransaction tr) {
        tr.doExecuteSql("SELECT " + ACCOUNT_FIELDS + " FROM account", 
            new Object[]{}, new SQLStatementCallback() {
          public void handleEvent(SQLTransaction tr, SQLResultSet rs) {
            new RSToAccountIterator(tr, rs, new Delegate<List<Account>>() {
              public void execute(List<Account> results) {
                if (results != null && results.size() > 0) {
                  delegate.execute(results.get(0));
                } else{
                  delegate.execute(null);
                }
              }
            });
          }
        });
      }
    });
  }
  
  protected void findAccountById(SQLTransaction tr, String id, final Delegate<Account> delegate) {
    tr.doExecuteSql("SELECT " + ACCOUNT_FIELDS + " FROM account WHERE id = ?", 
        new Object[]{id}, new SQLStatementCallback() {
      public void handleEvent(SQLTransaction tr, SQLResultSet rs) {
        new RSToAccountIterator(tr, rs, new Delegate<List<Account>>() {
          public void execute(List<Account> results) {
            if (results != null && results.size() > 0) {
              delegate.execute(results.get(0));
            } else{
              delegate.execute(null);
            }
          }
        });
      }
    });
  }
  
  protected class RSToAccountIterator {
    SQLResultSet rs;
    SQLTransaction tr;
    Delegate<List<Account>> delegate;
    List<Account> results = new ArrayList<Account>();
    public RSToAccountIterator(SQLTransaction tr, SQLResultSet rs, Delegate<List<Account>> delegate) {
      this.tr = tr;
      this.rs = rs;
      this.delegate = delegate;
      iterate(0);
    }
    private void iterate(final int it) {
      if (it < rs.getRows().getLength()) {
        final Account result = flushRS(rs, it);
        results.add(result);
        iterate(it + 1);
      } else {
        delegate.execute(results);
      }
    }
    private Account flushRS(SQLResultSet rs, int it) {
      SQLResultSetRowList rows = rs.getRows();
      Account entity = new AccountTx();
      entity.setId(rows.getValueString(it, "id"));
      entity.setEmail(rows.getValueString(it, "email"));
      entity.setName(rows.getValueString(it, "name"));
      entity.setPassword(rows.getValueString(it, "password"));
      entity.setDevInfoId(rows.getValueString(it, "devInfoId"));
      entity.setPushNotifRegId(rows.getValueString(it, "pushNotifRegId"));
      ((AccountTx)entity).setLastCheckForUpdates(longAsDate(rows.getValueLong(it, "lastCheckForUpdates")));
      return entity;
    }
    protected List<Account> getResults() {
      return results;
    }
  }
  
  public void saveAccount(final Account entity, final Delegate<Account> delegate) {
    db.doTransaction(new SQLTransactionCallback() {
      public void handleEvent(SQLTransaction tr) {
        saveAccount(tr, entity, delegate);
      }
    });
  }
  
  protected void saveAccount(final SQLTransaction tr, final Account entity, final Delegate<Account> delegate) {
    if (entity.getId() == null) {
      PhgUtils.log("SAVE ACCOUNT LOCAL ERROR (id = null!)");
    } else {
      
      findAccountById(tr, entity.getId(), new Delegate<Account>() {
        public void execute(Account account) {
          if (account == null) {
            
            tr.doExecuteSql("INSERT INTO account (" + ACCOUNT_FIELDS + ") VALUES (?, ?, ?, ?, ?, ?, ?)", 
                new Object[] {
                  entity.getId(),
                  entity.getEmail(),
                  entity.getName(),
                  entity.getPassword(),
                  entity.getDevInfoId(),
                  entity.getPushNotifRegId(),
                  dateAsLong(((AccountTx)entity).getLastCheckForUpdates())
                }, new SQLStatementCallback() {
                  public void handleEvent(final SQLTransaction tr, SQLResultSet rs) {
                    PhonegapLog.log("Inserted " + entity);
                    delegate.execute(entity);
                  }
                });
            
          } else {
            
            String sql = "UPDATE account SET ";
            sql += " email = ?";
            sql += " ,name = ?";
            sql += " ,password = ?";
            sql += " ,devInfoId = ?";
            sql += " ,pushNotifRegId = ?";
            sql += " ,lastCheckForUpdates = ?";
            sql += " WHERE id = ?";
            tr.doExecuteSql(sql, new Object[] {
                entity.getEmail(),
                entity.getName(),
                entity.getPassword(),
                entity.getDevInfoId(),
                entity.getPushNotifRegId(),
                dateAsLong(((AccountTx)entity).getLastCheckForUpdates()),
                entity.getId()
              }, new SQLStatementCallback() {
                public void handleEvent(final SQLTransaction tr, SQLResultSet rs) {
                  PhonegapLog.log("Updated " + entity);
                  delegate.execute(entity);
                }
              });
            
          }
        }
      });
      
    }
  }
  
}
