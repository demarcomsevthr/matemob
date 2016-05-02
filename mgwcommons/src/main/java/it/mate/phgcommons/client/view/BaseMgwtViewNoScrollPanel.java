package it.mate.phgcommons.client.view;

import it.mate.gwtcommons.client.mvp.BasePresenter;

import java.util.Iterator;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.ui.client.MGWTStyle;
import com.googlecode.mgwt.ui.client.theme.base.ScrollPanelCss;

public abstract class BaseMgwtViewNoScrollPanel <P extends BasePresenter> extends BaseMgwtView<P> {

  @Override
  protected HasWidgets createScrollPanel() {
    return new MyScrollPanel();
  }
  
  public class MyScrollPanel extends Composite implements HasWidgets {
    
    protected final MyScrollPanelImpl impl = new MyScrollPanelImpl();
    
    public MyScrollPanel() {
      initWidget(impl);
    }

    @Override
    public void add(Widget w) {
      impl.add(w);
    }

    @Override
    public void clear() {
      impl.clear();
    }

    @Override
    public Iterator<Widget> iterator() {
      return impl.iterator();
    }

    @Override
    public boolean remove(Widget w) {
      return impl.remove(w);
    }
    
    public void setWidget(IsWidget child) {
      impl.setWidget(child);
    }

  }
  
  
  public class MyScrollPanelImpl extends Composite implements HasWidgets {
    
    SimplePanel wrapper;
    
    private ScrollPanelCss css;

    public MyScrollPanelImpl() {
      
      wrapper = new SimplePanel();
      
      css = MGWTStyle.getTheme().getMGWTClientBundle().getScrollPanelCss();
      css.ensureInjected();

      wrapper.addStyleName(css.scrollPanel());

      initWidget(wrapper);
      
    }

    public void add(Widget w) {
      wrapper.add(w);
    }

    public void clear() {
      wrapper.clear();
    }

    @Override
    public Iterator<Widget> iterator() {
      return wrapper.iterator();
    }

    @Override
    public boolean remove(Widget w) {
      return wrapper.remove(w);
    }

    public void setWidget(IsWidget child) {
      wrapper.setWidget(child);
    }

  }
  
  
}
