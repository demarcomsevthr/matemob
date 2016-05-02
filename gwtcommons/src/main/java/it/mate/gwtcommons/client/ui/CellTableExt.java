package it.mate.gwtcommons.client.ui;

import it.mate.gwtcommons.client.utils.ColumnUtil;
import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasRows;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;

public class CellTableExt<M> extends CellTable<M> {

  private Map<Object, Column<M, ?>> columnMap = new HashMap<Object, Column<M,?>>();
  
  private Map<Object, Comparator<M>> comparatorMap = new HashMap<Object, Comparator<M>>();
  
  private AbstractDataProvider<M> dataProvider;
  
  private ListHandler<M> sortHandler;
  
  private HandlerRegistration sortHandlerRegistration;
  
  private boolean fillerColumnCreated = false;
  
  public interface ValueGetter <M, C> {
    C getValue(M model);
  }
  
  public static class SimpleValueGetter <M> implements ValueGetter<M, M> {
    public M getValue(M model) {return model;};
  }
  
  public CellTableExt(ProvidesKey<M> keyProvider) {
    super(10, keyProvider);
    init();
  }
  
  public CellTableExt() {
    super(10);
    init();
  }
  
  private void init() {
    super.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
    addFillerColumn();
  }
  
  public static class ColumnInfo<M, C> {
    private ValueGetter<M, C> getter;
    private Cell<C> cell;
    private FieldUpdater<M, C> fieldUpdater;
    private boolean sortable;
    private Comparator<M> comparator;
    private String headerText;
    public ColumnInfo<M, C> setGetter(ValueGetter<M, C> getter) {
      this.getter = getter;
      return this;
    }
    public ColumnInfo<M, C> setCell(Cell<C> cell) {
      this.cell = cell;
      return this;
    }
    public ColumnInfo<M, C> setFieldUpdater(FieldUpdater<M, C> fieldUpdater) {
      this.fieldUpdater = fieldUpdater;
      return this;
    }
    public ColumnInfo<M, C> setSortable(boolean sortable) {
      this.sortable = sortable;
      return this;
    }
    public ColumnInfo<M, C> setComparator(Comparator<M> comparator) {
      setSortable(true);
      this.comparator = comparator;
      return this;
    }
    public ColumnInfo<M, C> setHeaderText(String headerText) {
      this.headerText = headerText;
      return this;
    }  
  }
  
  public <C> void addColumn(Object key, Column<M, C> column, String headerText) {
    internalAddColumn(key, column, headerText, null);
  }
  
  public <C> void addColumn(Object key, Column<M, C> column, String headerText, Comparator<M> comparator) {
    internalAddColumn(key, column, headerText, comparator);
  }
  
  public <C> Column<M, C> addColumnExt(Object key, ColumnInfo<M, C> info) {
    if (info.getter == null) {
      info.getter = (ValueGetter<M, C>)new SimpleValueGetter<M>();
    }
    Column<M, C> column = createColumn(info.getter, info.cell, info.fieldUpdater, info.sortable);
    internalAddColumn(key, column, info.headerText, info.comparator);
    return column;
  }
  
  private <C> void internalAddColumn(Object key, Column<M, C> column, String headerText, Comparator<M> comparator) {
    columnMap.put(key, column);
    super.addColumn(column, headerText);
    if (comparator != null) {
      comparatorMap.put(key, comparator);
    }
  }
  
  private <C> Column<M, C> createColumn (
      final ValueGetter<M, C> getter, Cell<C> cell, FieldUpdater<M, C> fieldUpdater, boolean sortable) {
    Column<M, C> column = new Column<M, C>(cell) {
      public C getValue(M model) {
        return getter.getValue(model);
      }
    };
    if (fieldUpdater != null) {
      column.setFieldUpdater(fieldUpdater);
    }
    column.setSortable(sortable);
    return column;
  }
  
  public <C> Column<M, C> getColumn (Object key) {
    return (Column<M, C>)columnMap.get(key);
  }
  
  public void setRowDataExt (List<M> data) {
    setRowDataExt(data, null);
  }
  
  public void setRowDataExt (List<M> data, Object firstSortedColumnKey) {
    setRowDataExt(data, firstSortedColumnKey, -1);
  }
    
  public void setRowDataExt (List<M> data, Object firstSortedColumnKey, int start) {

    if (dataProvider != null) {
      if (dataProvider instanceof ListDataProvider) {
        ListDataProvider<M> listDataProvider = (ListDataProvider<M>)dataProvider;
        listDataProvider.setList(new ArrayList<M>());
        listDataProvider.refresh();
      }
    }
    
    if (data == null) {
      dataProvider = new ListDataProvider<M>(new ArrayList<M>());
      dataProvider.addDataDisplay(this);
      return;
    }

    if (dataProvider == null) {
      dataProvider = new ListDataProvider<M>(data);
      dataProvider.addDataDisplay(this);
    } else {
      if (dataProvider instanceof ListDataProvider) {
        ListDataProvider<M> listDataProvider = (ListDataProvider<M>)dataProvider;
        listDataProvider.setList(data);
        listDataProvider.refresh();
      } else if (dataProvider instanceof AsyncDataProvider) {
        this.setRowData(start, data);
      }
    }

    if (dataProvider instanceof ListDataProvider) {
      ListDataProvider<M> listDataProvider = (ListDataProvider<M>)dataProvider;
      sortHandler = new ListHandler<M>(listDataProvider.getList());
    }

    if (sortHandler != null) {
      for (Object key : columnMap.keySet()) {
        Column<M, ?> column = columnMap.get(key);
        Comparator<M> comparator = comparatorMap.get(key);
        sortHandler.setComparator(column, comparator);
      }
      sortHandlerRegistration = super.addColumnSortHandler(sortHandler);
    }

    if (firstSortedColumnKey != null) {
      super.getColumnSortList().push(columnMap.get(firstSortedColumnKey));
    }
    
  }
  
  public List<M> getModel() {
    if (dataProvider instanceof ListDataProvider) {
      ListDataProvider<M> listDataProvider = (ListDataProvider<M>)dataProvider;
      return listDataProvider.getList();
    }
    return null;
  }

  // TODO
  public void setDataProviderExt (AbstractDataProvider<M> dataProvider, Object firstSortedColumnKey) {
    if (this.dataProvider != null) {
      this.dataProvider.removeDataDisplay(this);
    }
    if (sortHandlerRegistration != null) {
      sortHandlerRegistration.removeHandler();
      sortHandler = null;
    }
    this.dataProvider = dataProvider;
    this.dataProvider.addDataDisplay(this);
  }
  
  public void refreshDataProvider() {
    if (dataProvider instanceof ListDataProvider) {
      ListDataProvider<M> listDataProvider = (ListDataProvider<M>)dataProvider;
      listDataProvider.refresh();
    }
  }
  
  public SimplePager createPager () {
    SimplePager pager = new SimplePager(TextLocation.CENTER, 
        (SimplePager.Resources)GWT.create(SimplePager.Resources.class), 
        false, 1000, true) {
      @Override
      protected String createText() {
        // Default text is 1 based.
        NumberFormat formatter = NumberFormat.getFormat("#,###");
        HasRows display = getDisplay();
        Range range = display.getVisibleRange();
        int pageStart = range.getStart() + 1;
        int pageSize = range.getLength();
        int dataSize = display.getRowCount();
        int endIndex = Math.min(dataSize, pageStart + pageSize - 1);
        endIndex = Math.max(pageStart, endIndex);
        boolean exact = display.isRowCountExact();
        return formatter.format(pageStart) + "-" + formatter.format(endIndex)
            + (exact ? " di " : " di oltre ") + formatter.format(dataSize);
      }
    };
    pager.setDisplay(this);
    return pager;
  }
  
  public void addFillerColumn() {
    if (fillerColumnCreated)
      return;
    fillerColumnCreated = true;
    Column<M, M> fillerColumn = ColumnUtil.createColumn(new ColumnUtil.SimpleValueGetter<M>(), 
      new HtmlCell<M>() {
        protected SafeHtml getCellHtml(M model) {
          return SafeHtmlUtils.fromTrustedString("<div id=\"fillerColumn\" style=\"height:100%;width:1px\">&nbsp;</div>");
        }
      }, 
      null);
    addColumn(fillerColumn, "");
    setColumnWidth(fillerColumn, "0px");
  }
  
  private class Semaphore {
    private boolean red = false;
    public boolean isRed() {
      return red;
    }
    public void setRed() {
      this.red = true;
    }
    public void setGreen() {
      this.red = false;
    }
  }
  
  private class WrappedValue <T> {
    private T value;
    public T get() {
      return value;
    }
    public void set(T value) {
      this.value = value;
    }
    public WrappedValue(T value) {
      super();
      this.value = value;
    }
  }
  
  public void adaptToViewHeight(Composite view, final Delegate<SimplePager> pagerDelegate) {
    adaptToViewHeight(view.getOffsetHeight(), pagerDelegate);
  }
  
  public void adaptToViewHeight(final Composite view, final Panel pagerPanel) {
    if (getModelRowCount() > 0) {
      final Semaphore timerSemaphore = new Semaphore();
      @SuppressWarnings({ "unchecked", "rawtypes" }) 
      final WrappedValue<Integer> previousHeight = new WrappedValue(0);
      @SuppressWarnings({ "unchecked", "rawtypes" }) 
      final WrappedValue<Integer> rowHeightWrapper = new WrappedValue(0);
      GwtUtils.createTimer(100, new Delegate<Void>() {
        public void execute(Void element) {
          if (timerSemaphore.isRed()) {
            timerSemaphore.setGreen();
            return;
          }
          int viewHeight = view.getOffsetHeight();
          if (previousHeight.get() == viewHeight) {
            return;
          } else {
            previousHeight.set(viewHeight);
          }
          if (rowHeightWrapper.get() == 0) {
            rowHeightWrapper.set(getRowHeight2());
          }
          int spacerHeight = 0;
          int rowHeight = rowHeightWrapper.get();
          if (rowHeight > 0) {
            rowHeight += 4; // aggiungo i bordi
            int maxRowsPerPage = viewHeight / rowHeight - 3;
            if (maxRowsPerPage > 0) {
              if (getModelRowCount() > maxRowsPerPage) {
                timerSemaphore.setRed();
                CellTableExt.this.setHeight( (viewHeight - rowHeight - 12) + "px");
              } else {
                spacerHeight = viewHeight - getRowHeightSum() - 120;
              }
              CellTableExt.this.setPageSize(maxRowsPerPage);
              CellTableExt.this.refreshDataProvider();
            }
          }
          pagerPanel.clear();
          VerticalPanel vp = new VerticalPanel();
          if (spacerHeight > 0) {
            vp.add(new Spacer("1px", spacerHeight+"px"));
          }
          vp.add(CellTableExt.this.createPager());
          pagerPanel.add(vp);
        }
      });
    } else {
      pagerPanel.clear();
    }
    
  }
  
  public void adaptToViewHeight(final int viewHeight, final Delegate<SimplePager> pagerDelegate) {
    if (getModelRowCount() > 0) {
      GwtUtils.deferredExecution(100, new Delegate<Void>() {
        public void execute(Void element) {
          int rowHeight = getRowHeight2();
          if (rowHeight > 0) {
            rowHeight += 4; // aggiungo i bordi
            int maxRowsPerPage = viewHeight / rowHeight - 3;
            if (maxRowsPerPage > 0) {
              if (getModelRowCount() > maxRowsPerPage) {
                CellTableExt.this.setHeight( (viewHeight - rowHeight - 12) + "px");
              }
              CellTableExt.this.setPageSize(maxRowsPerPage);
              CellTableExt.this.refreshDataProvider();
            }
          }
          pagerDelegate.execute(CellTableExt.this.createPager());
        }
      });
    }
  }
  
  private int getRowHeight1() {
    Element fillerColumn = null;
    try {
      fillerColumn = DOM.getElementById("fillerColumn");
    } catch (JavaScriptException ex) { 
    } catch (Exception ex) { }
    if (fillerColumn == null) {
      throw new NullPointerException("Manca la fillerColumn!");
    }
    return fillerColumn.getParentElement().getParentElement().getClientHeight();
  }
  
  private int getRowHeight2() {
    List<Integer> heights = getRowHeights();
    int avgHeight = 0;
    for (Integer height : heights) {
      avgHeight += height;
    }
    avgHeight = avgHeight / heights.size();
    return avgHeight;
  }
  
  private int getRowHeightSum() {
    List<Integer> heights = getRowHeights();
    int sum = 0;
    for (Integer height : heights) {
      sum += height;
    }
    return sum;
  }
  
  private List<Integer> getRowHeights() {
    List<Integer> heights = new ArrayList<Integer>();
    Element tableElem = this.getElement();
    NodeList<Element> nodeList = tableElem.getElementsByTagName("div");
    for (int it = 0; it < nodeList.getLength(); it++) {
      Element elem = nodeList.getItem(it);
      if ("fillerColumn".equalsIgnoreCase(elem.getId())) {
        Element fillerColumn = elem;
        heights.add(fillerColumn.getParentElement().getParentElement().getClientHeight());
      }
    }
    if (heights.size() == 0) {
      throw new NullPointerException("Manca la fillerColumn!");
    }
    return heights;
  }
  
  private int getModelRowCount() {
    int modelRows = 0;
    if (dataProvider instanceof AsyncDataProvider) {
      modelRows = this.getRowCount();
    } else if (dataProvider instanceof ListDataProvider) {
      ListDataProvider<M> listDataProvider = (ListDataProvider<M>)dataProvider;
      List<M> model = listDataProvider.getList();
      if (model != null && model.size() > 0) {
        modelRows = model.size();
      }
    }
    return modelRows;
  }
  
  
}
