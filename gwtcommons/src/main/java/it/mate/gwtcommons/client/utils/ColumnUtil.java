package it.mate.gwtcommons.client.utils;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;

public class ColumnUtil {
  
  public static class Options <M, T> {
    ValueGetter<M, T> getter;
    Cell<T> cell;
    FieldUpdater<M, T> fieldUpdater;
    CellTable<M> cellTable;
    String width;
    public Options(ValueGetter<M, T> getter, Cell<T> cell) {
      super();
      this.getter = getter;
      this.cell = cell;
    }
    public Options(ValueGetter<M, T> getter, Cell<T> cell, FieldUpdater<M, T> fieldUpdater) {
      super();
      this.getter = getter;
      this.cell = cell;
      this.fieldUpdater = fieldUpdater;
    }
    public Options<M,T> setFieldUpdater(FieldUpdater<M, T> fieldUpdater) {
      this.fieldUpdater = fieldUpdater;
      return this;
    }
    public Options<M,T> setCellTable(CellTable<M> cellTable) {
      this.cellTable = cellTable;
      return this;
    }
    public Options<M,T> setWidth(String width) {
      this.width = width;
      return this;
    }
  }

  public interface ValueGetter <M, T> {
    T getValue(M model);
  }
  
  public static class SimpleValueGetter <M> implements ValueGetter<M, M> {
    public M getValue(M model) {return model;};
  }
  
  public static <M, T> Column<M, T> createColumn (final ValueGetter<M, T> getter, Cell<T> cell, FieldUpdater<M, T> fieldUpdater) {
    return createColumn(new ColumnUtil.Options<M, T>(getter, cell, fieldUpdater));
  }
  
  public static <M, T> Column<M, T> createColumn (final Options<M, T> options) {
    Column<M, T> column = new Column<M, T>(options.cell) {
      public T getValue(M model) {
        return options.getter.getValue(model);
      }
    };
    if (options.fieldUpdater != null) {
      column.setFieldUpdater(options.fieldUpdater);
    }
    if (options.cellTable != null && options.width != null) {
      options.cellTable.setColumnWidth(column, options.width);
    }
    return column;
  }
  
}
