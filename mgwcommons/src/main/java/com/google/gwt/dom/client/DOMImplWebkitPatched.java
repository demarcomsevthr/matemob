package com.google.gwt.dom.client;

import it.mate.phgcommons.client.utils.PhgUtils;

public class DOMImplWebkitPatched extends DOMImplWebkit {
  
  public DOMImplWebkitPatched() {
    super();
    PhgUtils.log("Using DOMImplWebkitPatched");
  }

  @Override
  public int getAbsoluteTop(Element elem) {
    int top = getBoundingClientRectTop(elem);
    return top > -1 ? top
        + elem.getOwnerDocument().getBody().getScrollTop()
        : getAbsoluteTopUsingOffsetsPatched(elem);
  }

  @Override
  public int getAbsoluteLeft(Element elem) {
    int left = getBoundingClientRectLeft(elem);
    return left > -1 ? left
        + elem.getOwnerDocument().getBody().getScrollLeft()
        : getAbsoluteLeftUsingOffsetsPatched(elem);
  }

  private static native int getBoundingClientRectTop(Element element) /*-{
    // getBoundingClientRect() throws a JS exception if the elem is not attached
    // to the document, so we wrap it in a try/catch block
    try {
      return elem.getBoundingClientRect().top | 0;
    } catch (e) {
      // if not attached return 0
      return -1;
    }
  }-*/;

  private static native int getBoundingClientRectLeft(Element element) /*-{
    // getBoundingClientRect() throws a JS exception if the elem is not attached
    // to the document, so we wrap it in a try/catch block
    try {
      return elem.getBoundingClientRect().left | 0;
    } catch (e) {
      // if not attached return 0
      return -1;
    }
  }-*/;
  
  
  private static native int getAbsoluteTopUsingOffsetsPatched(Element elem) /*-{
    // Unattached elements and elements (or their ancestors) with style
    // 'display: none' have no offsetTop.
    if (elem.offsetTop == null) {
      return 0;
    }
  
    var top = 0;
    var doc = elem.ownerDocument;
    var curr = elem.parentNode;
    if (curr) {
      // This intentionally excludes body which has a null offsetParent.
      while (curr.offsetParent) {
        top -= curr.scrollTop;
        curr = curr.parentNode;
      }
    }
  
    while (elem) {
      top += elem.offsetTop;
  
      if (doc.defaultView.getComputedStyle(elem, '')['position'] == 'fixed') {
        top += doc.body.scrollTop;
        return top;
      }
  
      // Safari 3 does not include borders with offsetTop, so we need to add the
      // borders of the parent manually.
      var parent = elem.offsetParent;
      if (parent && $wnd.devicePixelRatio) {
        top += parseInt(doc.defaultView.getComputedStyle(parent, '').getPropertyValue('border-top-width'));
      }
  
      // Safari bug: a top-level absolutely positioned element includes the
      // body's offset position already.
      if (parent && (parent.tagName == 'BODY') &&
          (elem.style.position == 'absolute')) {
        break;
      }
  
      elem = parent;
    }
    return top;
  }-*/;

  private static native int getAbsoluteLeftUsingOffsetsPatched(Element elem) /*-{
    // Unattached elements and elements (or their ancestors) with style
    // 'display: none' have no offsetLeft.
    if (elem.offsetLeft == null) {
      return 0;
    }
  
    var left = 0;
    var doc = elem.ownerDocument;
    var curr = elem.parentNode;
    if (curr) {
      // This intentionally excludes body which has a null offsetParent.
      while (curr.offsetParent) {
        left -= curr.scrollLeft;
  
        // In RTL mode, offsetLeft is relative to the left edge of the
        // scrollable area when scrolled all the way to the right, so we need
        // to add back that difference.
        if (doc.defaultView.getComputedStyle(curr, '').getPropertyValue('direction') == 'rtl') {
          left += (curr.scrollWidth - curr.clientWidth);
        }
  
        curr = curr.parentNode;
      }
    }
  
    while (elem) {
      left += elem.offsetLeft;
  
      if (doc.defaultView.getComputedStyle(elem, '')['position'] == 'fixed') {
        left += doc.body.scrollLeft;
        return left;
      }
  
      // Safari 3 does not include borders with offsetLeft, so we need to add
      // the borders of the parent manually.
      var parent = elem.offsetParent;
      if (parent && $wnd.devicePixelRatio) {
        left += parseInt(doc.defaultView.getComputedStyle(parent, '').getPropertyValue('border-left-width'));
      }
  
      // Safari bug: a top-level absolutely positioned element includes the
      // body's offset position already.
      if (parent && (parent.tagName == 'BODY') &&
          (elem.style.position == 'absolute')) {
        break;
      }
  
      elem = parent;
    }
    return left;
  }-*/;

}
