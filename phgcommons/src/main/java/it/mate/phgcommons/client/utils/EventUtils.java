package it.mate.phgcommons.client.utils;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class EventUtils {
  
  public interface PanelGetter {
    Panel getPanel();
  }
  
  /**
   * 
   * @param panelGetter
   *          il panel che deve essere modal
   * @param registrationDelegate
   *          la registration viene restituita con un delegate
   *          
   * NOTA
   * 
   *   serve effettuare la registrazione in deferred perche' bisogna esser certi che il preview handler sia l'ultimo creato
   *   altrimenti gli eventi vengono consumati/cancellati dagli altri handler
   *    
   *   ( vedi com.google.gwt.user.client.ui.PopupPanel.previewNativeEvent )
   *          
   */
  public static void createModalHandler(final PanelGetter panelGetter, final Delegate<HandlerRegistration> registrationDelegate) {
    createModalHandler(panelGetter.getPanel().getElement(), registrationDelegate);
  }
  
  public static void createModalHandler(final Widget widget, final Delegate<HandlerRegistration> registrationDelegate) {
    createModalHandler(widget.getElement(), registrationDelegate);
  }
  
  public static void createModalHandler(final Element targetElement, final Delegate<HandlerRegistration> registrationDelegate) {
    GwtUtils.deferredExecution(new Delegate<Void>() {
      public void execute(Void element) {
        HandlerRegistration registration = Event.addNativePreviewHandler(new NativePreviewHandler() {
          public void onPreviewNativeEvent(NativePreviewEvent event) {
            previewNativeEvent(event, targetElement);
          }
        });
        if (registrationDelegate != null) {
          registrationDelegate.execute(registration);
        }
      }
    });
  }
  
  /**
   * 
   * @param registration
   * 
   * NOTA
   * 
   *    non trovo una soluzione migliore che mettere un delay di mezzo secondo
   *    bisogna esser certi che la remove dell'handler venga fatta dopo che siano stati scartati
   *    tutti gli eventi accodati
   * 
   */
  public static void removeModalHandler(final HandlerRegistration registration) {
    if (registration != null) {
      GwtUtils.deferredExecution(500, new Delegate<Void>() {
        public void execute(Void element) {
          registration.removeHandler();
        }
      });
    }
  }
  
  private static void previewNativeEvent(NativePreviewEvent event, Element targetElement) {
    Event nativeEvent = Event.as(event.getNativeEvent());
    boolean eventTargetsElement = false;
    EventTarget target = nativeEvent.getEventTarget();
    if (Element.is(target)) {
      eventTargetsElement = targetElement.isOrHasChild(Element.as(target));
    }
    // If the event has been canceled or consumed, ignore it
    if (event.isCanceled() || event.isConsumed()) {
      // We need to ensure that we cancel the event even if its been consumed so
      // that popups lower on the stack do not auto hide
      event.cancel();
    } else {
      if (eventTargetsElement) {
        event.consume();
      }
      event.cancel();
    }
  }

}
